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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.JobOperationalStatus;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.core.common.bean.entity.SecondaryWord;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.bean.swi.SFLTermTokenWeightContext;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.swi.IKDXMaintenanceService;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IJobDataService;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXDeletionService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPreferencesDeletionService;
import com.execue.swi.service.IPreferencesManagementService;
import com.execue.swi.service.IPreferencesRetrievalService;

/**
 * This service maintains the kdx information for the system R
 * 
 * @author Vishay
 * @version 1.0
 * @since 10/09/09
 */
public class KDXMaintenanceServiceImpl implements IKDXMaintenanceService {

   private static final Logger               logger = Logger.getLogger(KDXMaintenanceServiceImpl.class);

   private IKDXManagementService             kdxManagementService;
   private IKDXRetrievalService              kdxRetrievalService;
   private IPreferencesRetrievalService      preferencesRetrievalService;
   private IPreferencesManagementService     preferencesManagementService;
   private IPreferencesDeletionService       preferencesDeletionService;
   private ISWIConfigurationService          swiConfigurationService;
   private IJobDataService                   jobDataService;
   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;
   private IKDXDeletionService               kdxDeletionService;

   public void updateSFLTermTokensWeightOnHits (SFLTermTokenWeightContext sflTermTokenWeightContext)
            throws KDXException {

      JobOperationalStatus jobOperationalStatus = null;
      JobRequest jobRequest = sflTermTokenWeightContext.getJobRequest();
      try {

         logger.debug("Inside Update SFLTermToken weight updation logic");
         // TODO : -VG- use the job request id to update the qdata table
         // get all the sfl terms for the domain
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest, "Getting SFLTerms",
                  JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);

         List<Long> sflTermIds = kdxRetrievalService.getSFLTermIdsForNonZeroHits();

         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
         getJobDataService().updateJobOperationStatus(jobOperationalStatus);
         // check if list is not empty
         if (ExecueCoreUtil.isCollectionNotEmpty(sflTermIds)) {
            int batchSize = 50;
            int batchNumber = 1;
            int totalBatches = sflTermIds.size() / batchSize;
            for (int index = 0; index < sflTermIds.size(); index += batchSize) {
               int startingIndex = index;
               int endingIndex = index + batchSize;
               if (endingIndex > sflTermIds.size()) {
                  endingIndex = sflTermIds.size();
               }
               List<Long> subList = sflTermIds.subList(startingIndex, endingIndex);
               processBatchForSFLTerms(subList, jobOperationalStatus, jobRequest, batchNumber, totalBatches);
               batchNumber++;
            }
         }
      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e1);
            }
         }
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public void updateSFLTermTokensWeightBasedOnSecondaryWord (SFLTermTokenWeightContext sflTermTokenWeightContext)
            throws KDXException {

      JobRequest jobRequest = sflTermTokenWeightContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      try {

         // TODO:NK: Commented the regeneration of secondary words for now, as we
         // are manually providing the secondary words patch on the SWI.
         /*
          * 1. Populate secondary words a. get and delete existing secondary words b. create the secondary words
          */
         // populateSecondaryWords(sflTermTokenWeightContext);
         // 2. updateSFLTermTokensWeight
         if (jobRequest != null) {
            jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                     "Updating SFLTerm Token Weight", JobStatus.INPROGRESS, null, new Date());
            getJobDataService().createJobOperationStatus(jobOperationalStatus);
         }
         updateSFLTermTokensWeight(sflTermTokenWeightContext, jobOperationalStatus);
         if (jobRequest != null) {
            jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                     JobStatus.SUCCESS, null, new Date());
         }
      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e1);
            }
         }
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private void populateSecondaryWords (SFLTermTokenWeightContext sflTermTokenWeightContext) throws KDXException,
            QueryDataException {
      JobRequest jobRequest = sflTermTokenWeightContext.getJobRequest();
      JobOperationalStatus jobOperationalStatus = null;
      if (jobRequest != null) {
         jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                  "Populating Secondary Words", JobStatus.INPROGRESS, null, new Date());
         getJobDataService().createJobOperationStatus(jobOperationalStatus);
      }
      // First, delete all the existing secondary words for model
      Long modelId = sflTermTokenWeightContext.getModelId();
      deleteSecondaryWords(modelId);

      // Now, get and create the secondary words
      ModelGroup modelGroup = getKdxRetrievalService().getPrimaryGroup(modelId);

      // TODO: NK: currently getting the threshold count from configuration, later should calculate based on some
      // percentage
      Long threshold = Long.parseLong(getSwiConfigurationService().getDefaultThresholdForSecondaryWords());
      Set<String> secondaryWordsStr = getKdxRetrievalService().getEligibleSecondaryWordsForModel(modelId, threshold);
      List<SecondaryWord> secondaryWords = new ArrayList<SecondaryWord>(1);
      for (String word : secondaryWordsStr) {
         SecondaryWord sw = populateSecondaryWord(modelGroup, word);
         secondaryWords.add(sw);
      }
      getKdxManagementService().createSecondaryWords(secondaryWords);
      if (jobRequest != null) {
         jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                  JobStatus.SUCCESS, null, new Date());
      }
   }

   /**
    * @param modelGroup
    * @param word
    * @return
    */
   private SecondaryWord populateSecondaryWord (ModelGroup modelGroup, String word) {
      SecondaryWord sw = new SecondaryWord();
      sw.setWord(word);
      sw.setDefaultWeight(5.0d);
      sw.setFrequency(1L);
      sw.setModelGroup(modelGroup);
      return sw;
   }

   private void deleteSecondaryWords (Long modelId) throws KDXException {
      List<SecondaryWord> secondaryWords = getKdxRetrievalService().getAllSecondaryWordsForModel(modelId);
      getKdxDeletionService().deleteSecondaryWords(secondaryWords);
   }

   public void updateSFLTermTokensWeight (SFLTermTokenWeightContext sflTermTokenWeightContext,
            JobOperationalStatus jobOperationalStatus) throws KDXException {

      try {
         Long modelId = sflTermTokenWeightContext.getModelId();

         // Prepare the secondary word map for the given concept bed
         Map<String, Double> secondaryWordWeightMap = getKdxRetrievalService().getAllSecondaryWordsWeightMapForModel(
                  modelId);

         JobRequest jobRequest = sflTermTokenWeightContext.getJobRequest();
         if (secondaryWordWeightMap.isEmpty()) {
            if (logger.isDebugEnabled()) {
               logger.debug("Nothing to update. No secondary words found for the model Id... " + modelId);
            }
            if (jobRequest != null) {
               jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                        JobStatus.SUCCESS,
                        "Nothing to update. No secondary words found for the model Id... " + modelId, new Date());
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            }
            return;
         }

         // Also add the conjunction and other POS terms from the POS Context to the secondary words map
         Set<String> conjAndByConjTermNames = getSwiConfigurationService().getPosContext().getConjAndByConjTermNames();
         for (String conjTerm : conjAndByConjTermNames) {
            secondaryWordWeightMap.put(conjTerm.toLowerCase(), 5.0d);
         }

         if (logger.isDebugEnabled()) {
            logger.debug("Start updating the weights based on the secondary words: " + secondaryWordWeightMap.keySet());
         }

         List<BusinessEntityInfo> allConceptBusinessEntities = getKdxRetrievalService().getAllConceptBusinessEntities(
                  modelId);
         for (BusinessEntityInfo businessEntityInfo : allConceptBusinessEntities) {
            Long conceptBedId = businessEntityInfo.getBusinessEntityTermId();
            String conceptDisplayName = businessEntityInfo.getBusinessEntityTermDisplayName();
            if (logger.isDebugEnabled()) {
               logger.debug("Updating the weight for concept... " + conceptDisplayName);
            }
            Long batchNumber = 1L;
            // TODO: NK: should externalize the batch size
            Long batchSize = 1000L;
            Long startTime = System.currentTimeMillis();
            List<SFLTerm> sflTerms = new ArrayList<SFLTerm>(1);
            do {
               Long currentTime = System.currentTimeMillis();
               if (jobRequest != null) {
                  jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
                           "Updating Weight for SFL Term Tokens... " + " Batch Size: " + batchSize + " Batch Number: "
                                    + batchNumber, JobStatus.INPROGRESS, null, new Date());
                  getJobDataService().createJobOperationStatus(jobOperationalStatus);
               }
               if (logger.isDebugEnabled()) {
                  logger.debug("Updating Weight for SFL Term Tokens... Time Elapsed(secs): "
                           + (currentTime - startTime) / 1000 + " Batch Size: " + batchSize + " Batch Number: "
                           + batchNumber);
               }
               sflTerms = kdxRetrievalService.getSFLTermsForInstancesOfConceptByBatchNumber(conceptBedId, batchNumber,
                        batchSize);
               updateWeightsInTokens(sflTerms, secondaryWordWeightMap);
               batchNumber++;
               if (jobRequest != null) {
                  jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
                           JobStatus.SUCCESS, null, new Date());
                  getJobDataService().updateJobOperationStatus(jobOperationalStatus);
               }
            } while (!CollectionUtils.isEmpty(sflTerms) || sflTerms.size() == batchSize);
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Finished updating the weights based on the secondary words!!");
         }
      } catch (Exception e) {
         if (jobOperationalStatus != null) {
            ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus, JobStatus.FAILURE,
                     e.getMessage(), new Date());
            try {
               getJobDataService().updateJobOperationStatus(jobOperationalStatus);
            } catch (QueryDataException e1) {
               throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e1);
            }
         }
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   private void updateWeightsInTokens (List<SFLTerm> sflTerms, Map<String, Double> secondaryWordWeightMap)
            throws KDXException {
      if (CollectionUtils.isEmpty(sflTerms)) {
         return;
      }
      List<SFLTermToken> tokensList = new ArrayList<SFLTermToken>(1);
      for (SFLTerm sflTerm : sflTerms) {
         Set<SFLTermToken> sflTermTokens = sflTerm.getSflTermTokens();
         Double weightGained = 0d;
         List<SFLTermToken> primaryWords = new ArrayList<SFLTermToken>(1);
         boolean noSecondaryWordsFound = true;
         Double totalPrimaryTokensWeight = 0d;
         for (SFLTermToken termToken : sflTermTokens) {
            Double weight = secondaryWordWeightMap.get(termToken.getBusinessTermToken().toLowerCase());
            if (weight != null) {
               weightGained += termToken.getWeight() - weight;
               termToken.setPrimaryWord(0); // Mark as secondary
               termToken.setWeight(weight); // Update the secondary word weight
               noSecondaryWordsFound = false;
            } else if (termToken.getPrimaryWord().equals(1)) {
               primaryWords.add(termToken);
               totalPrimaryTokensWeight += termToken.getWeight();
            }
         }

         // If no secondary words found or there are no primary words in the sfl then do not adjust the weight
         if (noSecondaryWordsFound || CollectionUtils.isEmpty(primaryWords) || weightGained.equals(0d)) {
            continue;
         }

         // If only one primary word left, then directly add the gained weight to it
         SFLTermToken firstPrimaryTermToken = primaryWords.get(0);
         if (primaryWords.size() == 1) {
            firstPrimaryTermToken.setWeight(firstPrimaryTermToken.getWeight() + weightGained);
         } else {
            // Adjust the weight for primary words
            Double allPrimaryTokensWeightNew = 0d;
            for (SFLTermToken primaryTermToken : primaryWords) {
               double primaryTokenWeightGain = Math.floor(primaryTermToken.getWeight() / totalPrimaryTokensWeight
                        * weightGained);
               allPrimaryTokensWeightNew += primaryTermToken.getWeight() + primaryTokenWeightGain;
               primaryTermToken.setWeight(primaryTermToken.getWeight() + primaryTokenWeightGain);
            }
            double remainder = totalPrimaryTokensWeight + weightGained - allPrimaryTokensWeightNew;
            if (remainder > 0) {
               // Add the remainder to the first primary token
               firstPrimaryTermToken.setWeight(firstPrimaryTermToken.getWeight() + remainder);
            }
         }
         try {
            // TODO: NK: Currently update 500 tokens at a time, check if we need to externalize it??
            if (tokensList.size() > 500) {
               getKdxManagementService().updateSFLTermTokens(tokensList);
               tokensList.clear();
            } else {
               tokensList.addAll(sflTerm.getSflTermTokens());
            }
         } catch (KDXException e) {
            throw e;
         }
      }
      if (tokensList.size() > 0) {
         getKdxManagementService().updateSFLTermTokens(tokensList);
      }
   }

   private Map<String, SFLTermToken> getSFLTermTokensMap (Set<SFLTermToken> sflTermTokens) {
      Map<String, SFLTermToken> sflTermTokenMap = new HashMap<String, SFLTermToken>(1);

      for (SFLTermToken termToken : sflTermTokens) {
         sflTermTokenMap.put(termToken.getBusinessTermToken(), termToken);
      }
      return sflTermTokenMap;
   }

   private void processBatchForSFLTerms (List<Long> sflTermIds, JobOperationalStatus jobOperationalStatus,
            JobRequest jobRequest, int batchNumber, int totalBatches) throws KDXException, QueryDataException {
      jobOperationalStatus = ExecueBeanManagementUtil.prepareJobOperationalStatus(jobRequest,
               "Processing batch Number : " + batchNumber + " out of " + totalBatches, JobStatus.INPROGRESS, null,
               new Date());
      getJobDataService().createJobOperationStatus(jobOperationalStatus);
      List<SFLTermToken> toBeUpdatedSFLTermTokens = new ArrayList<SFLTermToken>();
      List<SFLTerm> populatedSFLTerms = getKdxRetrievalService().getPopulatedSFLTerms(sflTermIds);
      for (SFLTerm sflTerm : populatedSFLTerms) {
         // get the total number of hits for the term
         Long totalTermHits = getTotalTermHits(sflTerm);
         Set<SFLTermToken> sflTermTokens = sflTerm.getSflTermTokens();
         // this indicated the total weight for the terms of the token after each term assigned new weight, so
         // that for last term we can calculate 100-totalWeight to get the entire weight to 100.
         double totalWeight = 0;
         // counter is used to count the number of tokens for the term
         int counter = 1;
         for (SFLTermToken sflTermToken : sflTermTokens) {
            // weight is the new weight for the sflTermToken in picture
            double weight = 0;
            // if last token for the term
            if (counter == sflTermTokens.size()) {
               weight = 100 - totalWeight;
            } else {
               // round the weight
               weight = Math.ceil((double) sflTermToken.getHits() / totalTermHits * 100);
               // add the weight to total weight
               totalWeight += weight;
            }
            // set the weight to sflTermToken
            sflTermToken.setWeight(weight);
            // add it to the list of tokens to be updated
            toBeUpdatedSFLTermTokens.add(sflTermToken);
            counter++;
         }
      }
      // update all the tokens with new weights
      kdxManagementService.updateSFLTermTokens(toBeUpdatedSFLTermTokens);
      jobOperationalStatus = ExecueBeanManagementUtil.modifyJobOperationalStatus(jobOperationalStatus,
               JobStatus.SUCCESS, null, new Date());
      getJobDataService().updateJobOperationStatus(jobOperationalStatus);

   }

   private Long getTotalTermHits (SFLTerm sflTerm) {
      Long totalTermHits = 0L;
      for (SFLTermToken sflTermToken : sflTerm.getSflTermTokens()) {
         totalTermHits += sflTermToken.getHits();
      }
      return totalTermHits;
   }

   public void deleteSFLTermForParallelWord (Long parallelWordId, String originalParallelWordName) throws KDXException {
      try {
         SFLTerm toBeDeletedSFLTerm = kdxRetrievalService.getSFLTermByWord(originalParallelWordName);
         if (toBeDeletedSFLTerm != null) {
            getKdxDeletionService().deleteSFLTerm(toBeDeletedSFLTerm);
         }
      } catch (Exception e) {
         throw new KDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, e);
      }
   }

   public IJobDataService getJobDataService () {
      return jobDataService;
   }

   public void setJobDataService (IJobDataService jobDataService) {
      this.jobDataService = jobDataService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the swiConfigurationService
    */
   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService
    *           the swiConfigurationService to set
    */
   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

   public IPreferencesRetrievalService getPreferencesRetrievalService () {
      return preferencesRetrievalService;
   }

   public void setPreferencesRetrievalService (IPreferencesRetrievalService preferencesRetrievalService) {
      this.preferencesRetrievalService = preferencesRetrievalService;
   }

   public IPreferencesManagementService getPreferencesManagementService () {
      return preferencesManagementService;
   }

   public void setPreferencesManagementService (IPreferencesManagementService preferencesManagementService) {
      this.preferencesManagementService = preferencesManagementService;
   }

   public IPreferencesDeletionService getPreferencesDeletionService () {
      return preferencesDeletionService;
   }

   public void setPreferencesDeletionService (IPreferencesDeletionService preferencesDeletionService) {
      this.preferencesDeletionService = preferencesDeletionService;
   }

   public IKDXDeletionService getKdxDeletionService () {
      return kdxDeletionService;
   }

   public void setKdxDeletionService (IKDXDeletionService kdxDeletionService) {
      this.kdxDeletionService = kdxDeletionService;
   }

}
