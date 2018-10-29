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


package com.execue.governor.service.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.governor.BusinessAssetTerm;
import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.bean.governor.StructuredOrderClause;
import com.execue.core.common.bean.governor.StructuredQuery;
import com.execue.core.common.type.OperandType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.governor.configuration.IGovernorConfigurationService;
import com.execue.governor.exception.GovernorException;
import com.execue.governor.exception.GovernorExceptionCodes;
import com.execue.governor.service.IStructuredQueryWeightCalculationService;

/**
 * This service calculates the weight for business as well as structured queries.
 * 
 * @author Vishay Gupta
 * @version 1.0
 * @since 24/08/10
 */
public class StructuredQueryWeightCalculationServiceImpl implements IStructuredQueryWeightCalculationService {

   private static final Logger           logger = Logger.getLogger(StructuredQueryWeightCalculationServiceImpl.class);

   private IGovernorConfigurationService governorConfigurationService;

   /**
    * This method is used to assign weights to each business term inside the business query based on which section of
    * the query it appers in. We will calculate the weight of the business query by adding all the weights of business
    * terms. If business query has subquery inside, it will recursively calculate the weight of the subquery first and
    * add that to the main query weight. The calculated weight will be assigned to the business Query passed. The weight
    * assigments to business terms will be configurable.
    * 
    * @param businessQuery
    */
   public void assignBusinessQueryWeight (BusinessQuery businessQuery) throws GovernorException {
      logger.debug("Inside assignBusinessQueryWeight method");
      logger.debug("Got BusinessQuery to whom we have to assign weight : " + businessQuery);
      businessQuery.setBusinessQueryWeight(calculateBusinessQueryWeight(businessQuery));
   }

   /**
    * This method is helper method to assignBusinessQueryWeight() in order to handle sub queries. In this method, we are
    * assigning weight to each business term and calculating the weight of all business terms and finally returning the
    * calculated weight to the caller as an weight for the business query. If the business query encounters an sub
    * query, it recursively calls this method and get the weight back and add that weight to main query weight.
    * 
    * @param businessQuery
    * @return businessQueryWeight
    */
   private double calculateBusinessQueryWeight (BusinessQuery businessQuery) throws GovernorException {
      logger.debug("Inside calculateBusinessQueryWeight method");
      logger.debug("Got BusinessQuery to whom we need to assign weight : " + businessQuery);
      double businessQueryWeight = 0;
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getMetrics())) {
            for (BusinessTerm businessTerm : businessQuery.getMetrics()) {
               if (businessTerm.isRequestedByUser()) {
                  businessTerm.setBusinessTermWeight(getGovernorConfigurationService().getBQTermSelectWeight());
                  businessQueryWeight += businessTerm.getBusinessTermWeight();
               }
            }
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getConditions())) {
            for (BusinessCondition businessCondition : businessQuery.getConditions()) {
               if (businessCondition.getLhsBusinessTerm().isRequestedByUser()) {
                  businessCondition.getLhsBusinessTerm().setBusinessTermWeight(
                           getGovernorConfigurationService().getBQTermConditionWeight());
                  businessQueryWeight += businessCondition.getLhsBusinessTerm().getBusinessTermWeight();
               }
               if (OperandType.BUSINESS_TERM.equals(businessCondition.getOperandType())) {
                  for (BusinessTerm businessTerm : businessCondition.getRhsBusinessTerms()) {
                     if (businessTerm.isRequestedByUser()) {
                        businessTerm
                                 .setBusinessTermWeight(getGovernorConfigurationService().getBQTermConditionWeight());
                        businessQueryWeight += businessTerm.getBusinessTermWeight();
                     }
                  }
               } else if (OperandType.BUSINESS_QUERY.equals(businessCondition.getOperandType())) {
                  logger.debug("Found RHS of businessQuery as subQuery : " + businessCondition.getRhsBusinessQuery());
                  double subQueryWeight = calculateBusinessQueryWeight(businessCondition.getRhsBusinessQuery());
                  businessQueryWeight += subQueryWeight;
               }
            }
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getOrderClauses())) {
            for (BusinessOrderClause businessOrderClause : businessQuery.getOrderClauses()) {
               if (businessOrderClause.getBusinessTerm().isRequestedByUser()) {
                  businessOrderClause.getBusinessTerm().setBusinessTermWeight(
                           getGovernorConfigurationService().getBQTermOrderByWeight());
                  businessQueryWeight += businessOrderClause.getBusinessTerm().getBusinessTermWeight();
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getSummarizations())) {
            for (BusinessTerm businessTerm : businessQuery.getSummarizations()) {
               if (businessTerm.isRequestedByUser()) {
                  businessTerm.setBusinessTermWeight(getGovernorConfigurationService().getBQTermGroupByWeight());
                  businessQueryWeight += businessTerm.getBusinessTermWeight();
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getHavingClauses())) {
            for (BusinessCondition businessCondition : businessQuery.getHavingClauses()) {
               if (businessCondition.getLhsBusinessTerm().isRequestedByUser()) {
                  businessCondition.getLhsBusinessTerm().setBusinessTermWeight(
                           getGovernorConfigurationService().getBQTermHavingWeight());
                  businessQueryWeight += businessCondition.getLhsBusinessTerm().getBusinessTermWeight();
               }
               if (businessCondition.getOperandType().equals(OperandType.BUSINESS_TERM)) {
                  for (BusinessTerm businessTerm : businessCondition.getRhsBusinessTerms()) {
                     if (businessTerm.isRequestedByUser()) {
                        businessTerm.setBusinessTermWeight(getGovernorConfigurationService().getBQTermHavingWeight());
                        businessQueryWeight += businessTerm.getBusinessTermWeight();
                     }
                  }
               } else if (businessCondition.getOperandType().equals(OperandType.BUSINESS_QUERY)) {
                  logger.debug("Found RHS of businessQuery as subQuery : " + businessCondition.getRhsBusinessQuery());
                  double subQueryWeight = calculateBusinessQueryWeight(businessCondition.getRhsBusinessQuery());
                  businessQueryWeight += subQueryWeight;
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(businessQuery.getPopulations())) {
            for (BusinessTerm businessTerm : businessQuery.getPopulations()) {
               if (businessTerm.isRequestedByUser()) {
                  businessTerm.setBusinessTermWeight(getGovernorConfigurationService().getBQTermSelectWeight());
                  businessQueryWeight += businessTerm.getBusinessTermWeight();
               }
            }
         }
         if (businessQuery.getCohort() != null) {
            businessQueryWeight += calculateBusinessQueryWeight(businessQuery.getCohort());
         }
      } catch (Exception exception) {
         throw new GovernorException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE,
                  "Error while calculating the BusinessQueryWeight", exception.getCause());
      }
      return businessQueryWeight;
   }

   /**
    * This method is used to calculate the weight of the structured query by adding all the weights of business terms
    * inside the structured query. If structured query has subquery inside, it will recursively calculate the weight of
    * the subquery first and add that to the main query weight. The calculated weight will be assigned to the structured
    * Query passed.
    * 
    * @param structuredQueries
    */
   public void assignStructuredQueryWeight (List<StructuredQuery> structuredQueries) throws GovernorException {
      logger.debug("Inside assignStructuredQueryWeight method");
      logger.debug("Got List of structured queries for whom we need to calculate weights : " + structuredQueries);
      for (StructuredQuery structuredQuery : structuredQueries) {
         structuredQuery.setStructuredQueryWeight(calculateStructuredQueryWeight(structuredQuery));
      }
   }

   /**
    * This method is helper method to assignStructuredQueryWeight() in order to handle subqueries. This method will add
    * the weights of all the business terms and add them to structuredQueryWeight the calculated weight to the caller.
    * If the business query encounters an subquery, it recursively calls this method and get the weight back and add
    * that weight to main query weight.
    * 
    * @param structuredQuery
    * @return structuredQueryWeight
    */
   private double calculateStructuredQueryWeight (StructuredQuery structuredQuery) throws GovernorException {
      logger.debug("Inside calculateStructuredQueryWeight method");
      logger.debug("Got StructuredQuery for which we need to calculate the weight : " + structuredQuery);
      double structuredQueryWeight = 0;
      try {
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getMetrics())) {
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getMetrics()) {
               if (businessAssetTerm.getBusinessTerm().isRequestedByUser()) {
                  structuredQueryWeight += businessAssetTerm.getBusinessTerm().getBusinessTermWeight();
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getConditions())) {
            for (StructuredCondition structuredCondition : structuredQuery.getConditions()) {
               if (structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm().isRequestedByUser()) {
                  structuredQueryWeight += structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm()
                           .getBusinessTermWeight();
               }
               if (OperandType.BUSINESS_TERM.equals(structuredCondition.getOperandType())) {
                  for (BusinessAssetTerm businessAssetTerm : structuredCondition.getRhsBusinessAssetTerms()) {
                     if (businessAssetTerm.getBusinessTerm().isRequestedByUser()) {
                        structuredQueryWeight = structuredQueryWeight
                                 + businessAssetTerm.getBusinessTerm().getBusinessTermWeight();
                     }
                  }
               } else if (OperandType.BUSINESS_QUERY.equals(structuredCondition.getOperandType())) {
                  structuredQueryWeight += calculateStructuredQueryWeight(structuredCondition.getRhsStructuredQuery());
               }
            }
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getOrderClauses())) {
            for (StructuredOrderClause structuredOrderClause : structuredQuery.getOrderClauses()) {
               if (structuredOrderClause.getBusinessAssetTerm().getBusinessTerm().isRequestedByUser()) {
                  structuredQueryWeight += structuredOrderClause.getBusinessAssetTerm().getBusinessTerm()
                           .getBusinessTermWeight();
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getSummarizations())) {
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getSummarizations()) {
               if (businessAssetTerm.getBusinessTerm().isRequestedByUser()) {
                  structuredQueryWeight += businessAssetTerm.getBusinessTerm().getBusinessTermWeight();
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getHavingClauses())) {
            for (StructuredCondition structuredCondition : structuredQuery.getHavingClauses()) {
               if (structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm().isRequestedByUser()) {
                  structuredQueryWeight = structuredQueryWeight
                           + structuredCondition.getLhsBusinessAssetTerm().getBusinessTerm().getBusinessTermWeight();
               }
               if (structuredCondition.getOperandType().equals(OperandType.BUSINESS_TERM)) {
                  for (BusinessAssetTerm businessAssetTerm : structuredCondition.getRhsBusinessAssetTerms()) {
                     if (businessAssetTerm.getBusinessTerm().isRequestedByUser()) {
                        structuredQueryWeight += businessAssetTerm.getBusinessTerm().getBusinessTermWeight();
                     }
                  }
               } else if (structuredCondition.getOperandType().equals(OperandType.BUSINESS_QUERY)) {
                  structuredQueryWeight += calculateStructuredQueryWeight(structuredCondition.getRhsStructuredQuery());
               }
            }
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(structuredQuery.getPopulations())) {
            for (BusinessAssetTerm businessAssetTerm : structuredQuery.getPopulations()) {
               if (businessAssetTerm.getBusinessTerm().isRequestedByUser()) {
                  structuredQueryWeight += businessAssetTerm.getBusinessTerm().getBusinessTermWeight();
               }
            }
         }
         if (structuredQuery.getCohort() != null) {
            structuredQueryWeight += calculateStructuredQueryWeight(structuredQuery.getCohort());
         }
      } catch (Exception exception) {
         exception.printStackTrace();
         throw new GovernorException(GovernorExceptionCodes.GOV_DEFAULT_EXCEPTION_CODE,
                  "Error while calculating StructuredQueryWeight", exception.getCause());
      }
      return structuredQueryWeight;
   }

   /**
    * @return the governorConfigurationService
    */
   public IGovernorConfigurationService getGovernorConfigurationService () {
      return governorConfigurationService;
   }

   /**
    * @param governorConfigurationService the governorConfigurationService to set
    */
   public void setGovernorConfigurationService (IGovernorConfigurationService governorConfigurationService) {
      this.governorConfigurationService = governorConfigurationService;
   }

}
