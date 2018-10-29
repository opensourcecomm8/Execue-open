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


package com.execue.nlp.configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.kb.POSContext;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.RecognitionType;
import com.execue.core.common.type.SearchType;
import com.execue.nlp.bean.rules.validation.ValidationRulesContent;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRulesContent;
import com.execue.nlp.preprocessor.IPreProcessor;
import com.execue.nlp.processor.IProcessor;

/**
 * Base NLP Configuration
 * 
 * @author kaliki
 */
public interface INLPConfigurationService {

   /**
    * List of processors to be used by NLP Engine
    * 
    * @return
    */
   public List<IProcessor> getProcessors ();

   /**
    * List of processors to be used by NLP Engine based On passed context Information
    * 
    * @return
    */
   public List<IProcessor> getProcessors (String context);

   public Map<String, ProcessorConfiguration> getProcessorsMap ();

   /**
    * List of pre-processors to be used by NLP Engine
    * 
    * @return
    */

   public List<IPreProcessor> getPreProcessors ();

   /**
    * Rules used as part of validation framework of the TypeCloudProcessor
    * 
    * @return
    */
   public ValidationRulesContent getValidationRulesContent ();

   /**
    * Rules used as part of the association framework of the AppCloudProcessor
    * 
    * @return
    */
   public WeightAssignmentRulesContent getWeightAssignmentRulesContent ();

   public Double getWeightForRecognitionType (RecognitionType recognitionType);

   /**
    * List of processors to be used by NLP Engine for Semantic Scoping based On passed context Information
    * 
    * @param context
    * @return
    */
   public List<IProcessor> getSemanticScopingProcessors (String context);

   /**
    * Method to return the part of speech context.
    * 
    * @return
    */
   public POSContext getPosContext ();

   /**
    * Method to return the concept recognition weight
    * 
    * @return
    */
   public Double getConceptRecognitionWeight ();

   /**
    * Method to return the default instance Recognition weights
    * 
    * @return
    */
   public Double getInstanceRecognitionWeight ();

   /**
    * Method to return the default recognition weight for relation
    * 
    * @return
    */
   public Double getRelationRecognitionWeight ();

   /**
    * @return
    */
   public Integer getMaxNoOfPWWords ();

   /**
    * @return
    */
   public Integer getMaxAllowedPossibilitiesForQuery ();

   /**
    * @param searchType
    * @param flagName
    * @return
    */
   public boolean getFalgValueForSearchType (SearchType searchType, String flagName);

   public Integer getMaxPossibilityCountForSearchType (SearchType searchType);

   public Boolean getPenaltyFlagForImplicitConceptAssociationByAppName (String appName);

   public String getAppSpecificMethodByAppName (String appName);

   public Map<String, String> getUnitScaleMap ();

   public Map<String, String> getUnitSymbolMap ();

   public Map<String, String> getOperatorMap ();

   public String getDisplaySymbolBasedOnInstanceName (String instanceName);

   public OperatorType getOperatorTypeBasedOnOpName (String operatorName);

   /**
    * Method to return Set of the Type Names which are allowed and for which weight will not be reduced in a possibility
    * if they come unconnected/unassociated.
    * 
    * @return
    */
   public Set<String> getAllowedUnconnectedTypes ();

   /**
    * get the cloned cloud object by ckloud id from teh configuratuion
    * 
    * @param cloudId
    * @return
    */
   public Cloud getClonedCloudById (Long cloudId);

   public Integer getMaxToken ();

   public boolean isNlpTimeBasedCutOffEnabled ();

   public Long getMaxAllowledTimeForFindingSemantics ();

   public Long getMaxAllowledSemanticScopingTime ();

   public String getUnassociatedEntityWeightReduction ();

   public String getIndirectAssociationWeightReduction ();

   public String getImplicitAssociationWeightReduction ();

   public Long getUnificationFrameworkCloudId ();

   public boolean isFilterPossibilityWithHangingRelations ();

   public boolean isFilterOntoRecognitions ();

   public Integer getMaxAllowledInstanceRecognitions ();

   public Integer getPenaltyForMissingNamePart ();

   public boolean isAddLinguisticRoot ();

   public String getSnowflakeOutOfOrderWeightReduction ();

   public String getSnowflakeOProximityWeightReduction ();

   public Integer getMaximumProximityForSFL ();

   public boolean isChooseBestpossibility ();

   public Long getMaxAllowledTimeForEnhancingSemantics ();

   public String getCurrencyPatternRegex ();

   public String getAreaPatternRegex ();

   public Double getPathQualityThreshold ();

   public String getTaggerDataFileLocationFlag ();
}