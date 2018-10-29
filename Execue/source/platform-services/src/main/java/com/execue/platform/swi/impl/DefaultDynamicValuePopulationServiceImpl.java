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
import java.util.List;

import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.StatType;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.exception.SourceMetaDataException;
import com.execue.platform.swi.IDefaultDynamicValuePopulationService;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IDefaultDynamicValueService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

public class DefaultDynamicValuePopulationServiceImpl implements IDefaultDynamicValuePopulationService {

   private IKDXRetrievalService        kdxRetrievalService;
   private ISDXRetrievalService        sdxRetrievalService;
   private IMappingRetrievalService    mappingRetrievalService;
   private IDefaultDynamicValueService defaultDynamicValueService;
   private ISourceMetaDataService      sourceMetaDataService;
   private ISWIConfigurationService    swiConfigurationService;

   public void populateDefaultDynamicValues (Long applicationId) throws MappingException {
      try {
         List<Model> models = kdxRetrievalService.getModelsByApplicationId(applicationId);
         // as of now only one model per application.
         Long modelId = models.get(0).getId();
         List<Long> assetIds = getSdxRetrievalService().getAllAssetIds(applicationId);
         // for each asset find the mapped concepts which are time frames
         // for each mapped column, get the max and min value of the members.
         // use these values to build dynamic value formulae
         // insert the records into the table, if they exist already, delete them.
         for (Long assetId : assetIds) {
            List<DefaultDynamicValue> existingDefaultDynamicValues = getDefaultDynamicValueService()
                     .getDefaultDynamicValues(assetId);
            List<DefaultDynamicValue> freshDefaultDynamicValues = new ArrayList<DefaultDynamicValue>();
            List<Mapping> mappedTimeFrameConcepts = getMappingRetrievalService().getMappedConceptsOfParticularType(
                     assetId, modelId, ExecueConstants.TIME_FRAME_TYPE);
            if (ExecueCoreUtil.isCollectionNotEmpty(mappedTimeFrameConcepts)) {
               for (Mapping mapping : mappedTimeFrameConcepts) {
                  AssetEntityDefinition aed = mapping.getAssetEntityDefinition();
                  String minMemberValue = null;
                  String maxMemberValue = null;
                  String dataFormat = aed.getColum().getDataFormat();

                  if (isTableLookupAndTimeFrameColumnAsLookupValueColumn(aed.getTabl(), aed.getColum())) {
                     minMemberValue = getSdxRetrievalService()
                              .getMinMaxMemberValue(aed.getAsset().getId(), aed.getTabl().getId(),
                                       aed.getColum().getId(), StatType.MINIMUM, aed.getAsset().getType());
                     maxMemberValue = getSdxRetrievalService()
                              .getMinMaxMemberValue(aed.getAsset().getId(), aed.getTabl().getId(),
                                       aed.getColum().getId(), StatType.MAXIMUM, aed.getAsset().getType());
                  } else {
                     List<String> minMaxMemberValueFromSource = getSourceMetaDataService()
                              .getMinMaxMemberValueFromSource(aed.getAsset(), aed.getTabl(), aed.getColum());
                     minMemberValue = minMaxMemberValueFromSource.get(0);
                     maxMemberValue = minMaxMemberValueFromSource.get(1);
                  }

                  DateQualifier dateQualifier = getSwiConfigurationService().getDateQualifier(dataFormat);
                  DefaultDynamicValue minDefaultDynamicValue = populateDefaultDynamicValue(mapping, dataFormat,
                           dateQualifier, StatType.MINIMUM, minMemberValue);
                  DefaultDynamicValue maxDefaultDynamicValue = populateDefaultDynamicValue(mapping, dataFormat,
                           dateQualifier, StatType.MAXIMUM, maxMemberValue);
                  freshDefaultDynamicValues.add(minDefaultDynamicValue);
                  freshDefaultDynamicValues.add(maxDefaultDynamicValue);
               }
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(existingDefaultDynamicValues)) {
               getDefaultDynamicValueService().deleteDefaultDynamicValues(existingDefaultDynamicValues);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(freshDefaultDynamicValues)) {
               getDefaultDynamicValueService().createDefaultDynamicValues(freshDefaultDynamicValues);
            }
         }
      } catch (SDXException e) {
         e.printStackTrace();
      } catch (SWIException e) {
         e.printStackTrace();
      } catch (SourceMetaDataException sourceMetaDataException) {
         sourceMetaDataException.printStackTrace();
      }

   }

   private boolean isTableLookupAndTimeFrameColumnAsLookupValueColumn (Tabl tabl, Colum colum) {
      boolean isTableLookupAndTimeFrameColumnAsLookupValueColumn = false;
      if (!LookupType.None.equals(tabl.getLookupType())
               && tabl.getLookupValueColumn().equalsIgnoreCase(colum.getName())) {
         isTableLookupAndTimeFrameColumnAsLookupValueColumn = true;
      }
      return isTableLookupAndTimeFrameColumnAsLookupValueColumn;
   }

   private DefaultDynamicValue populateDefaultDynamicValue (Mapping mapping, String dateFormat,
            DateQualifier dateQualifier, StatType statType, String memberValue) throws SDXException,
            SourceMetaDataException {
      AssetEntityDefinition aed = mapping.getAssetEntityDefinition();
      DynamicValueQualifierType dynamicValueQualifierType = DynamicValueQualifierType.LAST;
      if (StatType.MINIMUM.equals(statType)) {
         dynamicValueQualifierType = DynamicValueQualifierType.FIRST;
      }
      DefaultDynamicValue defaultDynamicValue = new DefaultDynamicValue();
      defaultDynamicValue.setAssetId(aed.getAsset().getId());
      defaultDynamicValue.setLhsDEDId(mapping.getBusinessEntityDefinition().getId());
      defaultDynamicValue.setQualifier(dynamicValueQualifierType);
      defaultDynamicValue.setDefaultValue(memberValue);
      return defaultDynamicValue;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
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

   public IDefaultDynamicValueService getDefaultDynamicValueService () {
      return defaultDynamicValueService;
   }

   public void setDefaultDynamicValueService (IDefaultDynamicValueService defaultDynamicValueService) {
      this.defaultDynamicValueService = defaultDynamicValueService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
   }

}
