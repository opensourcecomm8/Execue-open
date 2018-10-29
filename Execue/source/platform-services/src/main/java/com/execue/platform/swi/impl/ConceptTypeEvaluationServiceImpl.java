/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.IBusinessEntity;
import com.execue.core.common.bean.TypeConceptEvaluationInfo;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.type.AssetGrainType;
import com.execue.core.common.type.BehaviorType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConceptBaseType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.GranularityType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.IConceptTypeEvaluationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This service is for evaluation of the concepts for their base types.
 * 
 * @author Vishay
 * @since 22/10/09
 * @version 1.0
 */
public class ConceptTypeEvaluationServiceImpl implements IConceptTypeEvaluationService {

   private ISDXRetrievalService     sdxRetrievalService;
   private IKDXRetrievalService     kdxRetrievalService;
   private IMappingRetrievalService mappingRetrievalService;
   private IConversionService       conversionService;

   public List<TypeConceptEvaluationInfo> evaluateConceptsForBaseTypes (Long assetId, Long modelId,
            List<Long> conceptBEDs, OperationType operationType) throws KDXException {
      List<TypeConceptEvaluationInfo> evaluatedConcepts = new ArrayList<TypeConceptEvaluationInfo>();
      if (ExecueCoreUtil.isCollectionNotEmpty(conceptBEDs)) {
         try {
            Asset asset = getSdxRetrievalService().getAssetById(assetId);
            // DataSource dataSource = getSdxService().getDataSourceByAssetId(asset.getId());
            for (Long conceptBED : conceptBEDs) {
               Set<BehaviorType> behaviourTypes = new HashSet<BehaviorType>();
               BusinessEntityDefinition businessEntityDefinition = kdxRetrievalService
                        .getBusinessEntityDefinitionById(conceptBED);
               if (businessEntityDefinition != null) {
                  IBusinessEntity businessEntity = businessEntityDefinition.getConcept();
                  BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
                  businessEntityTerm.setBusinessEntity(businessEntity);
                  businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);
                  businessEntityTerm.setBusinessEntityDefinitionId(conceptBED);
                  List<Mapping> assetEntities = getMappingRetrievalService().getAssetEntities(businessEntityTerm,
                           modelId, assetId);
                  if (ExecueCoreUtil.isCollectionNotEmpty(assetEntities)) {
                     List<AssetEntityDefinition> assetEntityDefinitions = ExecueBeanUtil
                              .getAssetEntityDefinitions(assetEntities);
                     if (ExecueCoreUtil.isCollectionNotEmpty(assetEntityDefinitions)) {
                        AssetEntityDefinition correctAED = getSdxRetrievalService().pickCorrectAssetEntityDefinition(
                                 assetEntityDefinitions);
                        Colum colum = correctAED.getColum();
                        ConceptBaseType conceptBaseType = null;
                        BusinessEntityDefinition detailedTypeBed = null;
                        BusinessEntityDefinition valueRealizationBed = null;
                        boolean nonRealizedType = false;
                        if (ColumnType.MEASURE.equals(colum.getKdxDataType())) {
                           conceptBaseType = ConceptBaseType.MEASURABLE_ENTITY;
                           // try to get value realizationBedId
                           if (colum.getConversionType() != null) {
                              Long valueRealizationBedId = getConversionService().getValueRealizationBedId(
                                       colum.getConversionType());
                              if (valueRealizationBedId != null) {
                                 valueRealizationBed = getKdxRetrievalService().getBusinessEntityDefinitionById(
                                          valueRealizationBedId);
                              }
                           }
                        } else if (isConceptTimeFrame(asset, colum)) {
                           conceptBaseType = ConceptBaseType.TIME_FRAME;
                           nonRealizedType = true;
                           // Get the detail type beId.
                           if (colum.getConversionType() != null && colum.getUnit() != null) {
                              Long detailTypeBedId = getConversionService().getDetailTypeBedId(
                                       colum.getConversionType(), colum.getUnit());
                              if (detailTypeBedId != null) {
                                 detailedTypeBed = getKdxRetrievalService().getBusinessEntityDefinitionById(
                                          detailTypeBedId);
                              }
                           }
                        } else {
                           conceptBaseType = ConceptBaseType.ONTO_ENTITY;
                        }

                        if (isConceptDimension(colum)) {
                           behaviourTypes.add(BehaviorType.ENUMERATION);
                        }

                        if (isConceptIndicator(colum)) {
                           behaviourTypes.add(BehaviorType.INDICATOR);
                        }

                        if (isConceptPartOfGrain(colum)) {
                           behaviourTypes.add(BehaviorType.GRAIN);
                           if (!ConceptBaseType.TIME_FRAME.equals(conceptBaseType)) {
                              behaviourTypes.add(BehaviorType.COMPARATIVE);
                           }
                           // TODO : -RG- Location should also be considered in this if condition
                           if (ConceptBaseType.TIME_FRAME.equals(conceptBaseType)) {
                              // TODO : -RG- If a grain is on time frame and/or location then make it a distribution
                              // In this Context, isConceptPopulation and isConceptDistribution checks need to be taken
                              // off
                              // The only check that needs to be maintained is to verify is whether it is a population
                              // or not
                              // if it is a population, whether default or not, we should add POPULATION as behavior
                              // Tough leaving the below two if conditions as is does not hurt
                              behaviourTypes.add(BehaviorType.DISTRIBUTION);
                           }
                        }

                        if (isConceptDistribution(assetEntities)) {
                           behaviourTypes.add(BehaviorType.DISTRIBUTION);
                           behaviourTypes.add(BehaviorType.GRAIN);
                        } else if (isConceptPopulation(assetEntities)) {
                           behaviourTypes.add(BehaviorType.POPULATION);
                           behaviourTypes.add(BehaviorType.GRAIN);
                           behaviourTypes.add(BehaviorType.COMPARATIVE);
                        }

                        List<BehaviorType> behaviorList = new ArrayList<BehaviorType>();
                        behaviorList.addAll(behaviourTypes);
                        TypeConceptEvaluationInfo typeConceptEvaluationInfo = new TypeConceptEvaluationInfo();
                        typeConceptEvaluationInfo.setConceptBed(businessEntityDefinition);
                        typeConceptEvaluationInfo.setConceptBaseType(conceptBaseType);
                        typeConceptEvaluationInfo.setBehaviorTypes(behaviorList);
                        typeConceptEvaluationInfo.setNonRealizedType(nonRealizedType);
                        typeConceptEvaluationInfo.setOperationType(operationType);
                        typeConceptEvaluationInfo.setDetailedTypeBed(detailedTypeBed);
                        typeConceptEvaluationInfo.setValueRealizationBed(valueRealizationBed);
                        evaluatedConcepts.add(typeConceptEvaluationInfo);
                     }
                  }
               }
            }
         } catch (MappingException e) {
            throw new KDXException(e.getCode(), e);
         } catch (SDXException e) {
            throw new KDXException(e.getCode(), e);
         } catch (SWIException e) {
            throw new KDXException(e.getCode(), e);
         }
      }
      return evaluatedConcepts;
   }

   private boolean isConceptIndicator (Colum colum) {
      boolean isIndicator = false;
      if (CheckType.YES.equals(colum.getIndicator())) {
         isIndicator = true;
      }
      return isIndicator;
   }

   private boolean isConceptDistribution (List<Mapping> assetEntities) {
      boolean isDistribution = false;
      for (Mapping mapping : assetEntities) {
         if (AssetGrainType.DISTRIBUTION_CONCEPT.equals(mapping.getAssetGrainType())
                  || AssetGrainType.DEFAULT_DISTRIBUTION_CONCEPT.equals(mapping.getAssetGrainType())) {
            isDistribution = true;
            break;
         }
      }
      return isDistribution;
   }

   private boolean isConceptPopulation (List<Mapping> assetEntities) {
      boolean isPopulation = false;
      for (Mapping mapping : assetEntities) {
         if (AssetGrainType.POPULATION_CONCEPT.equals(mapping.getAssetGrainType())
                  || AssetGrainType.DEFAULT_POPULATION_CONCEPT.equals(mapping.getAssetGrainType())) {
            isPopulation = true;
            break;
         }
      }
      return isPopulation;
   }

   private boolean isConceptPartOfGrain (Colum colum) {
      boolean isGrain = false;
      if (GranularityType.GRAIN.equals(colum.getGranularity())) {
         isGrain = true;
      }
      return isGrain;
   }

   private boolean isConceptTimeFrame (Asset asset, Colum colum) throws SWIException {
      boolean isConceptTimeFrame = false;
      if (ExecueBeanManagementUtil.isDataTypeDateFamily(colum) && isDateFormatComplex(asset, colum)) {
         isConceptTimeFrame = true;
      } else if (isDateFormatPlain(asset, colum)) {
         isConceptTimeFrame = true;
      }
      return isConceptTimeFrame;
   }

   private boolean isDateFormatPlain (Asset asset, Colum colum) throws SWIException {
      boolean isDateFormatPlain = false;
      String dataFormat = colum.getDataFormat();
      ConversionType conversionType = colum.getConversionType();
      if (dataFormat != null && ConversionType.DATE.equals(conversionType)) {
         if (CheckType.YES.equals(getConversionService().isDateFormatPlain(dataFormat))) {
            isDateFormatPlain = true;
         }
      }
      return isDateFormatPlain;
   }

   private boolean isDateFormatComplex (Asset asset, Colum colum) throws SWIException {
      boolean isDateFormatComplex = false;
      String dataFormat = colum.getDataFormat();
      ConversionType conversionType = colum.getConversionType();
      if (dataFormat != null && ConversionType.DATE.equals(conversionType)) {
         if (CheckType.NO.equals(getConversionService().isDateFormatPlain(dataFormat))) {
            isDateFormatComplex = true;
         }
      }
      return isDateFormatComplex;
   }

   private boolean isConceptDimension (Colum colum) {
      boolean isDimension = false;
      if (ColumnType.DIMENSION.equals(colum.getKdxDataType())
               || ColumnType.SIMPLE_LOOKUP.equals(colum.getKdxDataType())
               | ColumnType.RANGE_LOOKUP.equals(colum.getKdxDataType())) {
         isDimension = true;
      }
      return isDimension;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the conversionService
    */
   public IConversionService getConversionService () {
      return conversionService;
   }

   /**
    * @param conversionService
    *           the conversionService to set
    */
   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

}
