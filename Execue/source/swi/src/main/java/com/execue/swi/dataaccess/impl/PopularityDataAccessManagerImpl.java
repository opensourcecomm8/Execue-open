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


package com.execue.swi.dataaccess.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.RIParallelWord;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.swi.BusinessEntityDefinitionInfo;
import com.execue.core.common.bean.swi.PopularityHit;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.ParallelWordType;
import com.execue.core.common.type.TermType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.IPopularityDataAccessManager;
import com.execue.swi.dataaccess.MappingDAOComponents;
import com.execue.swi.exception.SWIException;
import com.execue.swi.exception.SWIExceptionCodes;

/**
 * @author John Mallavalli
 */
public class PopularityDataAccessManagerImpl extends MappingDAOComponents implements IPopularityDataAccessManager {

   private static final Logger logger = Logger.getLogger(PopularityDataAccessManagerImpl.class);

   public void updatePopularity (Long termId, TermType type, Long hits) throws SWIException {
      try {
         PopularityHit popularityHit = new PopularityHit();
         popularityHit.setTermId(termId);
         popularityHit.setType(type);
         popularityHit.setHits(hits);
         getMappingDAO().create(popularityHit);
      } catch (DataAccessException de) {
         throw new SWIException(de.getCode(), de.getCause());
      }
   }

   public void updatePopularity (PopularityHit popularityHit) throws SWIException {
      try {
         getMappingDAO().create(popularityHit);
      } catch (DataAccessException de) {
         throw new SWIException(de.getCode(), de.getCause());
      }
   }

   public void saveAll (List<PopularityHit> popularityHits) throws SWIException {
      try {
         getMappingDAO().saveOrUpdateAll(popularityHits);
      } catch (DataAccessException de) {
         throw new SWIException(de.getCode(), de.getCause());
      }
   }

   public List<Model> updateTermsBasedOnPopularity (int batchSize) throws SWIException {
      // collect all the models which are getting touched
      List<Model> models = new ArrayList<Model>();
      List<Application> applications = new ArrayList<Application>();
      List<AssetEntityDefinition> assetEntityDefinitions = new ArrayList<AssetEntityDefinition>();
      List<BusinessEntityDefinition> businessEntityDefinitions = new ArrayList<BusinessEntityDefinition>();
      List<RIParallelWord> riParallelWords = new ArrayList<RIParallelWord>();
      List<SFLTermToken> sflTermTokens = new ArrayList<SFLTermToken>();
      try {
         boolean hasRecordsToProcess = false;
         do {
            List<PopularityHit> popularityHits = getPopularityHitDAO().getTermBasedPopularityHit(batchSize);
            hasRecordsToProcess = (ExecueCoreUtil.isCollectionNotEmpty(popularityHits)) ? true : false;
            Long popularity = 0L;
            for (PopularityHit popularityHit : popularityHits) {
               switch (popularityHit.getType()) {
                  case ASSET_ENTITY:
                     AssetEntityDefinition assetEntityDefinition = null;
                     try {
                        assetEntityDefinition = getMappingDAO().getById(popularityHit.getTermId(),
                                 AssetEntityDefinition.class);
                     } catch (DataAccessException de) {
                        if (logger.isDebugEnabled()) {
                           logger.debug("AssetEntityDefinition, with id [" + popularityHit.getTermId()
                                    + "] does not exist");
                        }
                        break;
                     }
                     popularity = assetEntityDefinition.getPopularity();
                     if (popularity == null) {
                        popularity = 0L;
                     }
                     popularity += popularityHit.getHits();
                     assetEntityDefinition.setPopularity(popularity);
                     assetEntityDefinitions.add(assetEntityDefinition);

                     break;
                  case BUSINESS_ENTITY:
                     BusinessEntityDefinition businessEntityDefinition = null;
                     try {
                        businessEntityDefinition = getMappingDAO().getById(popularityHit.getTermId(),
                                 BusinessEntityDefinition.class);
                     } catch (DataAccessException de) {
                        if (logger.isDebugEnabled()) {
                           logger.debug("BusinessEntityDefinition, with id [" + popularityHit.getTermId()
                                    + "] does not exist");
                        }
                        break;
                     }
                     popularity = businessEntityDefinition.getPopularity();
                     if (popularity == null) {
                        popularity = 0L;
                     }
                     popularity += popularityHit.getHits();
                     businessEntityDefinition.setPopularity(popularity);
                     businessEntityDefinitions.add(businessEntityDefinition);
                     break;
                  case RI_PARALLEL_TERM_ENTITY:
                     RIParallelWord riParallelWord = null;
                     try {
                        riParallelWord = getMappingDAO().getById(popularityHit.getTermId(), RIParallelWord.class);
                     } catch (DataAccessException de) {
                        if (logger.isDebugEnabled()) {
                           logger.debug("RIParallelWord, with id [" + popularityHit.getTermId() + "] does not exist");
                        }
                        break;
                     }
                     popularity = riParallelWord.getHits();
                     if (popularity == null) {
                        popularity = 0L;
                     }
                     popularity += popularityHit.getHits();
                     riParallelWord.setHits(popularity);
                     riParallelWord.setPwdType(ParallelWordType.DEFAULT);
                     riParallelWords.add(riParallelWord);
                     break;
                  case SFL_TERM_TOKEN_ENTITY:
                     SFLTermToken sflTermToken = null;
                     try {
                        sflTermToken = getMappingDAO().getById(popularityHit.getTermId(), SFLTermToken.class);
                     } catch (DataAccessException de) {
                        if (logger.isDebugEnabled()) {
                           logger.debug("SFLTermToken, with id [" + popularityHit.getTermId() + "] does not exist");
                        }
                        break;
                     }
                     popularity = sflTermToken.getHits();
                     if (popularity == null) {
                        popularity = 0L;
                     }
                     popularity += popularityHit.getHits();
                     sflTermToken.setHits(popularity);
                     sflTermTokens.add(sflTermToken);
                     break;
                  case APPLICATION:
                     Application application = null;
                     try {
                        application = getMappingDAO().getById(popularityHit.getTermId(), Application.class);
                     } catch (DataAccessException de) {
                        if (logger.isDebugEnabled()) {
                           logger.debug("Application, with id [" + popularityHit.getTermId() + "] does not exist");
                        }
                        break;
                     }
                     popularity = application.getPopularity();
                     if (popularity == null) {
                        popularity = 0L;
                     }
                     popularity += popularityHit.getHits();
                     application.setPopularity(popularity);
                     applications.add(application);
                     break;
                  case MODEL:
                     Model model = null;
                     try {
                        model = getMappingDAO().getById(popularityHit.getTermId(), Model.class);
                     } catch (DataAccessException de) {
                        if (logger.isDebugEnabled()) {
                           logger.debug("Model, with id [" + popularityHit.getTermId() + "] does not exist");
                        }
                        break;
                     }
                     popularity = model.getPopularity();
                     if (popularity == null) {
                        popularity = 0L;
                     }
                     popularity += popularityHit.getHits();
                     model.setPopularity(popularity);
                     models.add(model);
               }
            }
            getMappingDAO().updateAll(models);
            getMappingDAO().updateAll(applications);
            getMappingDAO().updateAll(assetEntityDefinitions);
            getMappingDAO().updateAll(businessEntityDefinitions);
            // updateRIOntoTermsPopularity(businessEntityDefinitions);
            getMappingDAO().updateAll(riParallelWords);
            getMappingDAO().updateAll(sflTermTokens);
            logger.debug("Updated the popularity Hits of the AssetEntity");
            // Update ProcessingState of processed PopularityHits

            long updateCount = getPopularityHitDAO().updateProcessedPopularityHits(getTermIds(popularityHits));
            if (logger.isDebugEnabled()) {
               logger.debug("Total Updated Popularity Hits: " + updateCount);
            }
         } while (hasRecordsToProcess);
      } catch (DataAccessException de) {
         throw new SWIException(de.getCode(), de.getCause());
      }
      return models;
   }

   private List<Long> getTermIds (List<PopularityHit> popularityHits) {
      List<Long> termIds = new ArrayList<Long>();
      for (PopularityHit popularityHit : popularityHits) {
         termIds.add(popularityHit.getTermId());
      }
      return termIds;
   }

   public void updateRIOntoTermsPopularity (Long modelId, int batchSize) throws SWIException {
      try {
         List<BusinessEntityDefinitionInfo> businessEntityDefinitionInfoList = getBusinessEntityDefinitionDAO()
                  .getBusinessEntityDefinitionInfoForModel(modelId);
         logger.debug("BusinessEntityTerms List ::" + businessEntityDefinitionInfoList.size());
         List<RIOntoTerm> riOntoTermsToBeUpdated = new ArrayList<RIOntoTerm>();
         int counter = 0;
         for (BusinessEntityDefinitionInfo businessEntityDefinitionInfo : businessEntityDefinitionInfoList) {

            if (businessEntityDefinitionInfo.getBusinessEntityType().equals(BusinessEntityType.CONCEPT)) {
               List<RIOntoTerm> riOntoterms = getOntoReverseIndexDAO().getConceptTermsForConceptBedId(
                        businessEntityDefinitionInfo.getModelGroupId(), businessEntityDefinitionInfo.getId());
               for (RIOntoTerm riOntoTerm : riOntoterms) {
                  riOntoTerm.setPopularity(businessEntityDefinitionInfo.getPopularity());
                  riOntoTermsToBeUpdated.add(riOntoTerm);
                  logger.debug("Updated the RI-Ontoterm popularity Hit for the ConceptBusinessEntity ["
                           + businessEntityDefinitionInfo.getId() + "] for RI-OntoTerm [" + riOntoTerm.getId()
                           + "] with value [" + businessEntityDefinitionInfo.getPopularity() + "]");
               }
            } else if (businessEntityDefinitionInfo.getBusinessEntityType().equals(
                     BusinessEntityType.CONCEPT_LOOKUP_INSTANCE)) {
               List<RIOntoTerm> riOntoterms = getOntoReverseIndexDAO().getInstanceTermsForInstanceBedId(
                        businessEntityDefinitionInfo.getModelGroupId(), businessEntityDefinitionInfo.getId());
               for (RIOntoTerm riOntoTerm : riOntoterms) {
                  riOntoTerm.setPopularity(businessEntityDefinitionInfo.getPopularity());
                  riOntoTermsToBeUpdated.add(riOntoTerm);
                  logger.debug("Updated the RI-Ontoterm popularity Hit for the InstanceBusinessEntity ["
                           + businessEntityDefinitionInfo.getId() + "] for RI-OntoTerm [" + riOntoTerm.getId()
                           + "] with value [" + businessEntityDefinitionInfo.getPopularity() + "]");
               }
            }
            if (counter % batchSize == 0) {
               getOntoReverseIndexDAO().updateAll(riOntoTermsToBeUpdated);
               riOntoTermsToBeUpdated = new ArrayList<RIOntoTerm>();
            }
            counter++;
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(riOntoTermsToBeUpdated)) {
            getOntoReverseIndexDAO().updateAll(riOntoTermsToBeUpdated);
         }
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteProcessedPopularityHit () throws SWIException {
      try {
         long deletedCount = getPopularityHitDAO().deleteProcessedPopularityHits();
         if (logger.isDebugEnabled()) {
            logger.debug("Total Cleaned Up Popularity Hits: " + deletedCount);
         }
      } catch (DataAccessException dataAccessException) {
         throw new SWIException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

}
