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


package com.execue.nlp.configuration.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.bean.kb.POSContext;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.RecognitionType;
import com.execue.core.common.type.SearchType;
import com.execue.core.configuration.IConfiguration;
import com.execue.nlp.bean.rules.RuleRegexComponent;
import com.execue.nlp.bean.rules.validation.ValidationRulesContent;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRulesContent;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.configuration.ProcessorConfiguration;
import com.execue.nlp.exception.NLPExceptionCodes;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.preprocessor.IPreProcessor;
import com.execue.nlp.processor.IProcessor;
import com.execue.nlp.processor.RecognitionProcessor;
import com.execue.swi.configuration.ISWIConfigurationService;

/**
 * Base NLP COnfiguration
 * 
 * @author kaliki
 */
public class NLPConfigurationServiceImpl implements INLPConfigurationService, BeanFactoryAware {

   private final Logger                        logger                                                = Logger
                                                                                                              .getLogger(NLPConfigurationServiceImpl.class);

   private String                              WORD_TYPE_VALUE                                       = "value";
   private String                              WORD_TYPE_WEIGHT                                      = "weight";

   private String                              TAGGER_DATA_FILE_KEY                                  = "nlp.files.taggerDataFile";
   private String                              TAGGER_DATA_FILE_LOCATION_FLAG_KEY                    = "nlp.flags.taggerDataFileLocationFlag";
   private String                              LANGUAGE_PATTERNS_DATA_FILE_KEY                       = "nlp.files.languagePatternsFile";
   private String                              LANGUAGE_PATTERNS_ASSOCIATION_DATA_FILE_KEY           = "nlp.files.languageAssociationPatternsFile";
   private String                              TIMEFRAME_RULES_DATA_FILE_KEY                         = "nlp.files.timeframeRulesFile";
   private String                              CA_ASSOCIATIONS_RULES_DATA_FILE_KEY                   = "nlp.files.CAAssociationsRulesFile";
   private String                              WEIGHT_ASSIGNMENT_RULES_DATA_FILE_KEY                 = "nlp.files.weightAssignmentRulesFile";
   private String                              VALIDATION_RULES_DATA_FILE_KEY                        = "nlp.files.validationRulesFile";
   private String                              REGEX_COMPONENT_DATA_FILE_KEY                         = "nlp.files.regexComponentsFile";
   private String                              PROCESSOR_CONTEXT_NAME                                = "nlp.contexts.context.name";

   private String                              PROCESSOR_CONTEXTS                                    = "nlp.contexts.context";

   private String                              PROCESSOR_CLASS_NAME                                  = "processors.processorClassName";
   private String                              PRE_PROCESSOR_CLASS_NAME                              = "nlp.preProcessors.preProcessorClassName";
   private String                              PRE_PROCESSOR_QUERY_NORMALIZER_CURRENCY_PATTERN_REGEX = "nlp.preProcessors.query-normalizer.currency-pattern-regex";
   private String                              PRE_PROCESSOR_QUERY_NORMALIZER_AREA_PATTERN_REGEX     = "nlp.preProcessors.query-normalizer.area-pattern-regex";

   private String                              SNOWFLAKE_PERCENTAGE_THRESHOLD                        = "nlp.flags.snowflakePercentageThreshold";
   private String                              SNOWFLAKE_OUT_OF_ORDER_WEIGHT_REDUCTION               = "nlp.flags.outOfOrderWeightReductionPercent";
   private String                              SNOWFLAKE_PERCENTILE_THRESHOLD                        = "nlp.flags.snowflakePercentileThreshold";
   private String                              SNOWFLAKE_PROXIMITY_WEIGHT_REDUCTION                  = "nlp.flags.sflProximityReductionPercent";

   private String                              NUMBER_SNOWFLAKES_SELECTED                            = "nlp.flags.numberSnowflakesSelected";

   private String                              WORD_TYPE_KEY                                         = "nlp.wordTypeWeights.wordType";
   private String                              WORD_TYPE_NAME_KEY                                    = "nlp.wordTypeWeights.wordType.name";
   private String                              WORD_TYPE_VALUE_KEY                                   = "nlp.wordTypeWeights.wordType.value";
   private String                              WORD_TYPE_WEIGHT_KEY                                  = "nlp.wordTypeWeights.wordType.weight";
   private String                              SEARCH_TYPE_KEY                                       = "nlp.searchTypes.searchType";
   private String                              SEARCH_TYPE_NAME_KEY                                  = "nlp.searchTypes.searchType.name";
   private String                              MAX_TOKENS_KEY                                        = "nlp.static-values.max-tokens-limit";
   private String                              MAX_ALLOWED_NLP_TIME                                  = "nlp.static-values.max-allowed-nlp-time";
   private String                              MAX_ALLOWED_SEMANTIC_SCOPING_TME                      = "nlp.static-values.max-allowed-nlp-time-for-semantic-scoping";
   private String                              MAX_ALLOWED_NLP_TIME_FOR_FINDING_SEMANTICS            = "nlp.static-values.max-allowed-nlp-time-for-finding-semantics";
   private String                              MAX_ALLOWED_NLP_TIME_FOR_ENHANCING_SEMANTICS          = "nlp.static-values.max-allowed-nlp-time-for-enhancing-semantics";
   private String                              NLP_TIME_BASED_CUTOFF_ENABLED_KEY                     = "nlp.static-values.nlp-time-based-cutoff-enabled";
   private String                              MAX_NO_OF_PW_WORDS                                    = "nlp.static-values.max-allowed-pw-words";
   private String                              MAX_NO_OF_ALLOWED_POSSIBILITY                         = "nlp.static-values.max-allowed-possibility";
   private String                              CHOOSE_BEST_POSSIBILITY                               = "nlp.static-values.choose-best-possibility";
   private String                              ADD_LINGUISTIC_ROOT                                   = "nlp.static-values.add-linguistic-root";
   private String                              FILTER_ONTO_RECOGNITIONS                              = "nlp.static-values.filter-onto-recognitions";
   private String                              FILTER_POSSIBILITY_WITH_HANGING_RELATIONS             = "nlp.static-values.filter-possibility-with-hanging-relations";
   private String                              PENALTY_FOR_MISSING_NAME_PART                         = "nlp.static-values.penalty-for-missing-part-in-name";
   private String                              PENALIZE_FOR_IMPLICIT_CONCEPT_ASSOCIATION             = "nlp.static-values.penalize-for-implicit-concept-association-by-application.penalize-for-implicit-concept-association";
   private String                              PENALIZE_FOR_IMPLICIT_CONCEPT_ASSOCIATION_BY_APPNAME  = "nlp.static-values.penalize-for-implicit-concept-association-by-application";
   private String                              MAXIMUM_PROXIMITY_FOR_SFL                             = "nlp.static-values.maximum-proximity-for-sfl";
   private String                              UNIFICATION_FRAMEWORK_CLOUD_ID                        = "nlp.static-values.unification_framework_cloud_id";
   private String                              ALLOWED_UNCONNETED_TYPES                              = "nlp.allowed-unconnected-types.name";
   private String                              MAX_ALLOWED_INSTANCE_RECOGNITIONS                     = "nlp.static-values.max-allowed-best-instance-recognitions-for-token";
   private String                              PENALIZE_FOR_IMPLICIT_CONCEPT_ASSOCIATION_NAME_KEY    = "nlp.static-values.penalize-for-implicit-concept-association-by-application.penalize-for-implicit-concept-association.name";
   private String                              PENALIZE_FOR_IMPLICIT_CONCEPT_ASSOCIATION_VALUE_KEY   = "nlp.static-values.penalize-for-implicit-concept-association-by-application.penalize-for-implicit-concept-association.value";
   private String                              PATH_QUALITY_THRESHOLD                                = "nlp.static-values.path_quality_threshold";

   /* NLP Flag Constants */
   private String                              UNASSOCIATED_ENTITY_WEIGHT_REDUCTION                  = "nlp.flags.unAssociatedEntityReductionPercent";
   private String                              INDIRECT_ASSOCIATION_WEIGHT_REDUCTION                 = "nlp.flags.indirectAssociationReductionPercent";
   private String                              IMPLICIT_ASSOCIATION_WEIGHT_REDUCTION                 = "nlp.flags.implicitAssociationReductionPercent";

   /* NLP App Specific Constants */
   private String                              APP_SPECIFIC_METHODS                                  = "nlp.appSpecificMethods.appSpecificMethod";
   private String                              APP_SPECIFIC_METHODS_METHODNAME_KEY                   = "nlp.appSpecificMethods.appSpecificMethod.methodName";
   private String                              APP_SPECIFIC_METHODS_APPNAME_KEY                      = "nlp.appSpecificMethods.appSpecificMethod.appName";

   /* NLP Search Type Constants */
   private String                              SEARCH_TYPE_FLAGS_SEMANTIC_SCOPING_KEY                = "nlp.searchTypes.searchType.flags.runSemanticScoping";
   private String                              SEARCH_TYPE_FLAGS_MEANING_FINDER_KEY                  = "nlp.searchTypes.searchType.flags.runMeaningFinder";
   private String                              SEARCH_TYPE_FLAGS_MEANING_ENHANCER_KEY                = "nlp.searchTypes.searchType.flags.runMeaningEnhancer";
   private String                              SEARCH_TYPE_MAX_POS_COUNT_KEY                         = "nlp.searchTypes.searchType.maxPossibilityCount";

   private String                              NLP_CONCEPT_DEFAULT_RECOGNITION_WEIGHT                = "nlp.default-rf-recognition-weights.concept-weight";
   private String                              NLP_INSTANCE_DEFAULT_RECOGNITION_WEIGHT               = "nlp.default-rf-recognition-weights.instance-weight";
   private String                              NLP_RELATION_DEFAULT_RECOGNITION_WEIGHT               = "nlp.default-rf-recognition-weights.relation-weight";
   private String                              NLP_SFL_WORD_DEFAULT_RECOGNITION_WEIGHT               = "nlp.default-rf-recognition-weights.sfl-word-weight";

   private ISWIConfigurationService            swiConfigurationService;
   private IConfiguration                      nlpConfiguration;

   private List<IPreProcessor>                 preProcessors                                         = null;
   private Map<String, ProcessorConfiguration> processorsMap                                         = null;
   private Map<String, List<IProcessor>>       contextProcessorsMap                                  = null;

   private List<RuleRegexComponent>            ruleRegExComponents;
   private ValidationRulesContent              validationRulesContent;
   private WeightAssignmentRulesContent        weightAssignmentRulesContent;

   private BeanFactory                         beanFactory;
   private HashMap<String, List<IProcessor>>   contextSemanticProcessorsMap;
   private HashMap<String, List<IProcessor>>   contextTypeCloudProcessorsMap;
   private HashMap<String, List<IProcessor>>   contextAppCloudProcessorsMap;

   private Map<Integer, Double>                wordTypeWeightMap;
   private Map<String, Map<String, Boolean>>   searchTypeFlagMap;
   private Map<String, Integer>                searchTypemaxpossibilityMap;
   private Map<String, Boolean>                penalizeByAppNameMap;
   private Map<String, String>                 appSpecificMethodByAppName;
   private Map<String, String>                 unitSymbolMap;
   private Map<String, String>                 unitScaleMap;
   private Map<String, String>                 operatorMap;
   private Map<String, String>                 instanceNameToSymbolMap;
   private Set<String>                         allowedUnconnectedTypes;
   private Map<Long, Cloud>                    baseCloudsById;

   public String getTaggerDataFile () {
      String taggerDataFileLocation = getNlpConfiguration().getProperty(TAGGER_DATA_FILE_KEY);
      if (!"FILE_SYSTEM".equalsIgnoreCase(getTaggerDataFileLocationFlag())) {
         URL resource = NLPConfigurationServiceImpl.class.getResource(getNlpConfiguration().getProperty(TAGGER_DATA_FILE_KEY));
         taggerDataFileLocation = resource.getFile();
      }
      return taggerDataFileLocation;
   }

   @SuppressWarnings ("unchecked")
   public void loadAllowedUnconnectedTypes () {
      Object prop = getNlpConfiguration().getPropertyObject(ALLOWED_UNCONNETED_TYPES);
      if (prop instanceof Collection) {
         this.allowedUnconnectedTypes = new HashSet<String>(1);
         List<String> typeNames = (List<String>) prop;
         for (String typeName : typeNames) {
            this.allowedUnconnectedTypes.add(typeName.toLowerCase());
         }
      }
   }

   public void loadAppMapForPenaltyValues () {
      List<String> nameList = getNlpConfiguration().getList(PENALIZE_FOR_IMPLICIT_CONCEPT_ASSOCIATION_NAME_KEY);
      List<String> valueList = getNlpConfiguration().getList(PENALIZE_FOR_IMPLICIT_CONCEPT_ASSOCIATION_VALUE_KEY);
      int size = nameList.size();
      this.penalizeByAppNameMap = new HashMap<String, Boolean>();
      for (int appNameIndex = 0; appNameIndex < size; appNameIndex++) {
         this.penalizeByAppNameMap.put(nameList.get(appNameIndex), Boolean.valueOf(valueList.get(appNameIndex)));
      }
   }

   public void loadAppSpecificMethodMap () {
      List<String> appNameList = getNlpConfiguration().getList(APP_SPECIFIC_METHODS_APPNAME_KEY);
      List<String> methodNameList = getNlpConfiguration().getList(APP_SPECIFIC_METHODS_METHODNAME_KEY);
      int size = appNameList.size();
      this.appSpecificMethodByAppName = new HashMap<String, String>(1);
      for (int appNameIndex = 0; appNameIndex < size; appNameIndex++) {
         this.appSpecificMethodByAppName.put(appNameList.get(appNameIndex).toLowerCase(), methodNameList
                  .get(appNameIndex));
      }
   }

   public List<IPreProcessor> getPreProcessors () {
      return preProcessors;
   }

   public Map<String, ProcessorConfiguration> getProcessorsMap () {
      return processorsMap;
   }

   public void loadProcessors () {
      ProcessorConfiguration processorConfig;
      processorsMap = new LinkedHashMap<String, ProcessorConfiguration>();
      contextProcessorsMap = new HashMap<String, List<IProcessor>>();
      contextSemanticProcessorsMap = new HashMap<String, List<IProcessor>>();
      String processorClassName = null;
      List<String> contextNames = getNlpConfiguration().getList(PROCESSOR_CONTEXT_NAME);
      int contextCount = 0;
      for (String contextname : contextNames) {

         Iterator<String> processorClassNames = getNlpConfiguration().getList(
                  PROCESSOR_CONTEXTS + "." + contextname + "." + PROCESSOR_CLASS_NAME).iterator();
         Iterator<String> semanticProcessorClassNames = getNlpConfiguration().getList(
                  PROCESSOR_CONTEXTS + "." + contextname + ".semanticScoping" + "." + PROCESSOR_CLASS_NAME).iterator();
         while (semanticProcessorClassNames.hasNext()) {
            try {
               processorClassName = semanticProcessorClassNames.next();
               processorConfig = new ProcessorConfiguration();
               IProcessor processor = getProcessor(processorConfig, processorClassName);
               if (contextSemanticProcessorsMap.get(contextname) == null) {
                  List<IProcessor> processorList = new ArrayList<IProcessor>();
                  processorList.add(processor);
                  contextSemanticProcessorsMap.put(contextname, processorList);
               } else {
                  contextSemanticProcessorsMap.get(contextname).add(processor);
               }

            } catch (BeansException e) {
               logger.error(e);
            }
         }
         while (processorClassNames.hasNext()) {
            try {
               processorClassName = processorClassNames.next();
               processorConfig = new ProcessorConfiguration();
               IProcessor processor = getProcessor(processorConfig, processorClassName);
               processorsMap.put(processorClassName, processorConfig);
               if (contextProcessorsMap.get(contextname) == null) {
                  List<IProcessor> processorList = new ArrayList<IProcessor>();
                  processorList.add(processor);
                  contextProcessorsMap.put(contextname, processorList);
               } else {
                  contextProcessorsMap.get(contextname).add(processor);
               }
            } catch (BeansException e) {
               logger.error(e);
            }
         }
         contextCount++;
      }

   }

   /**
    * @param processorConfig
    * @param processorClassName
    * @return
    */
   private IProcessor getProcessor (ProcessorConfiguration processorConfig, String processorClassName) {
      IProcessor processor = (IProcessor) beanFactory.getBean(processorClassName);
      processorConfig.setProcessor(processor);
      if (processor instanceof RecognitionProcessor) {
         processorConfig.setProcessorType(IProcessor.RecognitionProcessor);
      }
      return processor;
   }

   /**
    * 
    */

   public List<String> getRegexComponentDataFiles () {
      return getNlpConfiguration().getList(REGEX_COMPONENT_DATA_FILE_KEY);
   }

   public List<String> getValidationRuleFiles () {
      return getNlpConfiguration().getList(VALIDATION_RULES_DATA_FILE_KEY);
   }

   /**
    * This method loads the valiation rules TODO: NK: Currenlty laoding it from time frames xml
    */
   public List<String> getWeightAssignmentRuleDataFiles () {
      return getNlpConfiguration().getList(WEIGHT_ASSIGNMENT_RULES_DATA_FILE_KEY);
   }

   @SuppressWarnings ("unchecked")
   /*
    * private void loadWordTypeWeightMap () { Object prop =
    * getConfiguration().getPropertyObject(NLPConfigurationConstants.WORD_TYPE_NAME_KEY); if (prop instanceof
    * Collection) { this.wordTypeWeightMap = new HashMap<Integer, Double>(); Collection wordTypeWeightList =
    * (Collection) prop; int size = wordTypeWeightList.size(); Integer value = 0; Double weight = 0.0; for (int
    * wordTypeWeightConfigIndex = 0; wordTypeWeightConfigIndex < size; wordTypeWeightConfigIndex++) { value =
    * getConfiguration().getInt( NLPConfigurationConstants.WORD_TYPE_KEY + "(" + wordTypeWeightConfigIndex + ")." +
    * NLPConfigurationConstants.WORD_TYPE_VALUE); weight = getConfiguration().getDouble(
    * NLPConfigurationConstants.WORD_TYPE_KEY + "(" + wordTypeWeightConfigIndex + ")." +
    * NLPConfigurationConstants.WORD_TYPE_WEIGHT); this.wordTypeWeightMap.put(value, weight); } } }
    */
   public void loadWordTypeWeightMap () {
      Object prop = getNlpConfiguration().getPropertyObject(WORD_TYPE_NAME_KEY);
      if (prop instanceof Collection) {
         this.wordTypeWeightMap = new HashMap<Integer, Double>();

         Collection wordTypeWeightList = (Collection) prop;
         List wordTypeWeightValueList = getNlpConfiguration().getList(WORD_TYPE_VALUE_KEY);
         List wordTypeWeightWeightList = getNlpConfiguration().getList(WORD_TYPE_WEIGHT_KEY);

         int size = wordTypeWeightList.size();
         Integer value = 0;
         Double weight = 0.0;
         for (int wordTypeWeightConfigIndex = 0; wordTypeWeightConfigIndex < size; wordTypeWeightConfigIndex++) {
            value = Integer.valueOf((String) wordTypeWeightValueList.get(wordTypeWeightConfigIndex));
            weight = Double.valueOf((String) wordTypeWeightWeightList.get(wordTypeWeightConfigIndex));
            this.wordTypeWeightMap.put(value, weight);
         }
      }
   }

   @SuppressWarnings ("unchecked")
   public void loadSearchTypeFlagMap () {

      this.searchTypeFlagMap = new HashMap<String, Map<String, Boolean>>(1);
      this.searchTypemaxpossibilityMap = new HashMap<String, Integer>(1);

      List searchNameList = getNlpConfiguration().getList(SEARCH_TYPE_NAME_KEY);
      List semanticScopingFlagList = getNlpConfiguration().getList(SEARCH_TYPE_FLAGS_SEMANTIC_SCOPING_KEY);
      List meaningFinderFlagList = getNlpConfiguration().getList(SEARCH_TYPE_FLAGS_MEANING_FINDER_KEY);
      List meaningEnhancerFlagList = getNlpConfiguration().getList(SEARCH_TYPE_FLAGS_MEANING_ENHANCER_KEY);
      List maxPosCountList = getNlpConfiguration().getList(SEARCH_TYPE_MAX_POS_COUNT_KEY);

      int size = searchNameList.size();
      for (int searchTypeFlagConfigIndex = 0; searchTypeFlagConfigIndex < size; searchTypeFlagConfigIndex++) {
         Map<String, Boolean> flagMap = new HashMap<String, Boolean>();

         flagMap.put(NLPConstants.NLP_RECOGNITION_SEMANTIC_SCOPING, Boolean.valueOf((String) semanticScopingFlagList
                  .get(searchTypeFlagConfigIndex)));
         flagMap.put(NLPConstants.NLP_RECOGNITION_FIND_MEANING, Boolean.valueOf((String) meaningFinderFlagList
                  .get(searchTypeFlagConfigIndex)));
         flagMap.put(NLPConstants.NLP_RECOGNITION_ENHANCE_MEANING, Boolean.valueOf((String) meaningEnhancerFlagList
                  .get(searchTypeFlagConfigIndex)));

         this.searchTypeFlagMap.put((String) searchNameList.get(searchTypeFlagConfigIndex), flagMap);
         this.searchTypemaxpossibilityMap.put((String) searchNameList.get(searchTypeFlagConfigIndex), Integer
                  .valueOf((String) maxPosCountList.get(searchTypeFlagConfigIndex)));
      }
   }

   public Integer getMaxPossibilityCountForSearchType (SearchType searchType) {
      return this.searchTypemaxpossibilityMap.get(searchType.getValue());

   }

   public Boolean getPenaltyFlagForImplicitConceptAssociationByAppName (String appName) {
      Boolean flag = this.penalizeByAppNameMap.get(appName.toLowerCase());
      if (flag == null) {
         flag = this.penalizeByAppNameMap.get("default");
      }
      return flag;
   }

   public String getAppSpecificMethodByAppName (String appName) {
      String methodName = this.appSpecificMethodByAppName.get(appName.toLowerCase());
      return methodName;
   }

   public boolean getFalgValueForSearchType (SearchType searchType, String flagName) {
      Map<String, Boolean> flagMapForSearchType = this.searchTypeFlagMap.get(searchType.getValue());
      return flagMapForSearchType.get(flagName);
   }

   public void setBeanFactory (BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
   }

   /**
    * List of processors to be used by NLP Engine
    * 
    * @see com.execue.nlp.configuration.INLPConfigurationService#getProcessors(java.lang.String)
    */
   public List<IProcessor> getProcessors () {
      return getProcessors("Default");
   }

   public List<IProcessor> getProcessors (String context) {
      if (context == null) {
         // TODO AP - Throw Exception
      }
      List<IProcessor> processorList = contextProcessorsMap.get(context);
      if (processorList == null) {
         return new ArrayList<IProcessor>();
      }
      return processorList;
   }

   public Double getWeightForRecognitionType (RecognitionType recognitionType) {
      return this.wordTypeWeightMap.get(recognitionType.getValue());
   }

   public List<IProcessor> getSemanticScopingProcessors (String context) {
      List<IProcessor> processorList = contextSemanticProcessorsMap.get(context);
      if (processorList == null) {
         return new ArrayList<IProcessor>();
      }
      return processorList;
   }

   public POSContext getPosContext () {
      return getSwiConfigurationService().getPosContext();
   }

   public Double getConceptRecognitionWeight () {
      return getNlpConfiguration().getDouble(NLP_CONCEPT_DEFAULT_RECOGNITION_WEIGHT);
   }

   public Double getInstanceRecognitionWeight () {
      return getNlpConfiguration().getDouble(NLP_INSTANCE_DEFAULT_RECOGNITION_WEIGHT);
   }

   public Double getRelationRecognitionWeight () {
      return getNlpConfiguration().getDouble(NLP_RELATION_DEFAULT_RECOGNITION_WEIGHT);
   }

   public ValidationRulesContent getValidationRulesContent () {
      return validationRulesContent;
   }

   public HashMap<String, List<IProcessor>> getContextTypeCloudProcessorsMap () {
      return contextTypeCloudProcessorsMap;
   }

   public HashMap<String, List<IProcessor>> getContextAppCloudProcessorsMap () {
      return contextAppCloudProcessorsMap;
   }

   public WeightAssignmentRulesContent getWeightAssignmentRulesContent () {
      return weightAssignmentRulesContent;
   }

   public void loadWeightAssignmentRulesContent (WeightAssignmentRulesContent weightAssignmentRulesContent) {
      this.weightAssignmentRulesContent = weightAssignmentRulesContent;
   }

   public List<RuleRegexComponent> getRuleRegExComponents () {
      return ruleRegExComponents;
   }

   public void loadRuleRegExComponents (List<RuleRegexComponent> ruleRegExComponents) {
      this.ruleRegExComponents = ruleRegExComponents;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public Map<String, String> getUnitSymbolMap () {
      return unitSymbolMap;
   }

   public void loadUnitSymbolMap (Map<String, String> unitSymbolMap) {
      this.unitSymbolMap = unitSymbolMap;
   }

   public Map<String, String> getUnitScaleMap () {
      return unitScaleMap;
   }

   public void loadUnitScaleMap (Map<String, String> unitScaleMap) {
      this.unitScaleMap = unitScaleMap;
   }

   public Map<String, String> getOperatorMap () {
      return operatorMap;
   }

   public void loadOperatorMap (Map<String, String> operatorMap) {
      this.operatorMap = operatorMap;
   }

   public String getDisplaySymbolBasedOnInstanceName (String instanceName) {
      String displaySymbol = this.instanceNameToSymbolMap.get(instanceName.toLowerCase());
      return displaySymbol;
   }

   public OperatorType getOperatorTypeBasedOnOpName (String operatorName) {
      String opExpr = this.instanceNameToSymbolMap.get(operatorName.toLowerCase());
      if (opExpr != null) {
         return OperatorType.getOperatorType(opExpr);
      }
      return null;
   }

   public Set<String> getAllowedUnconnectedTypes () {
      return allowedUnconnectedTypes;
   }

   public void setAllowedUnconnectedTypes (Set<String> allowedUnconnectedTypes) {
      this.allowedUnconnectedTypes = allowedUnconnectedTypes;
   }

   public void loadBaseCloudsById (Map<Long, Cloud> baseCloudsById) {
      this.baseCloudsById = baseCloudsById;
   }

   public Cloud getClonedCloudById (Long cloudId) {
      Cloud cloud = baseCloudsById.get(cloudId);
      if (cloud == null) {
         return cloud;
      }
      try {
         return cloud.clone();
      } catch (CloneNotSupportedException e) {
         throw new NLPSystemException(NLPExceptionCodes.CLONE_FAILED, e);
      }
   }

   public IConfiguration getNlpConfiguration () {
      return nlpConfiguration;
   }

   public void setNlpConfiguration (IConfiguration nlpConfiguration) {
      this.nlpConfiguration = nlpConfiguration;
   }

   public void loadValidationRulesContent (ValidationRulesContent validationRulesContent) {
      this.validationRulesContent = validationRulesContent;
   }

   public void loadInstanceNameToSymbolMap (Map<String, String> instanceNameToSymbolMap) {
      this.instanceNameToSymbolMap = instanceNameToSymbolMap;
   }

   public Integer getMaxNoOfPWWords () {
      return getNlpConfiguration().getInt(MAX_NO_OF_PW_WORDS);
   }

   public Integer getMaxAllowedPossibilitiesForQuery () {
      return getNlpConfiguration().getInt(MAX_NO_OF_ALLOWED_POSSIBILITY);
   }

   /**
    * @return the contextProcessorsMap
    */
   public Map<String, List<IProcessor>> getContextProcessorsMap () {
      return contextProcessorsMap;
   }

   /**
    * @return the contextSemanticProcessorsMap
    */
   public HashMap<String, List<IProcessor>> getContextSemanticProcessorsMap () {
      return contextSemanticProcessorsMap;
   }

   /**
    * @return the wordTypeWeightMap
    */
   public Map<Integer, Double> getWordTypeWeightMap () {
      return wordTypeWeightMap;
   }

   /**
    * @return the searchTypeFlagMap
    */
   public Map<String, Map<String, Boolean>> getSearchTypeFlagMap () {
      return searchTypeFlagMap;
   }

   /**
    * @return the searchTypemaxpossibilityMap
    */
   public Map<String, Integer> getSearchTypemaxpossibilityMap () {
      return searchTypemaxpossibilityMap;
   }

   /**
    * @return the penalizeByAppNameMap
    */
   public Map<String, Boolean> getPenalizeByAppNameMap () {
      return penalizeByAppNameMap;
   }

   /**
    * @return the appSpecificMethodByAppName
    */
   public Map<String, String> getAppSpecificMethodByAppName () {
      return appSpecificMethodByAppName;
   }

   /**
    * @return the baseCloudsById
    */
   public Map<Long, Cloud> getBaseCloudsById () {
      return baseCloudsById;
   }

   /**
    * @return the instanceNameToSymbolMap
    */
   public Map<String, String> getInstanceNameToSymbolMap () {
      return instanceNameToSymbolMap;
   }

   @Override
   public String getImplicitAssociationWeightReduction () {
      return getNlpConfiguration().getProperty(IMPLICIT_ASSOCIATION_WEIGHT_REDUCTION);
   }

   @Override
   public String getIndirectAssociationWeightReduction () {
      return getNlpConfiguration().getProperty(INDIRECT_ASSOCIATION_WEIGHT_REDUCTION);
   }

   @Override
   public Integer getMaxAllowledInstanceRecognitions () {
      return getNlpConfiguration().getInt(MAX_ALLOWED_INSTANCE_RECOGNITIONS);
   }

   @Override
   public Long getMaxAllowledSemanticScopingTime () {
      return getNlpConfiguration().getLong(MAX_ALLOWED_SEMANTIC_SCOPING_TME);
   }

   @Override
   public Long getMaxAllowledTimeForFindingSemantics () {
      return getNlpConfiguration().getLong(MAX_ALLOWED_NLP_TIME_FOR_FINDING_SEMANTICS);
   }

   @Override
   public Integer getMaxToken () {
      return getNlpConfiguration().getInt(MAX_TOKENS_KEY);
   }

   @Override
   public Integer getMaximumProximityForSFL () {
      return getNlpConfiguration().getInt(MAXIMUM_PROXIMITY_FOR_SFL);
   }

   @Override
   public Integer getPenaltyForMissingNamePart () {
      return getNlpConfiguration().getInt(PENALTY_FOR_MISSING_NAME_PART);
   }

   @Override
   public String getSnowflakeOProximityWeightReduction () {
      return getNlpConfiguration().getProperty(SNOWFLAKE_PROXIMITY_WEIGHT_REDUCTION);
   }

   @Override
   public String getSnowflakeOutOfOrderWeightReduction () {
      return getNlpConfiguration().getProperty(SNOWFLAKE_OUT_OF_ORDER_WEIGHT_REDUCTION);
   }

   @Override
   public String getUnassociatedEntityWeightReduction () {
      return getNlpConfiguration().getProperty(UNASSOCIATED_ENTITY_WEIGHT_REDUCTION);
   }

   @Override
   public Long getUnificationFrameworkCloudId () {
      return getNlpConfiguration().getLong(UNIFICATION_FRAMEWORK_CLOUD_ID);
   }

   @Override
   public boolean isAddLinguisticRoot () {
      return getNlpConfiguration().getBoolean(ADD_LINGUISTIC_ROOT);
   }

   @Override
   public boolean isFilterOntoRecognitions () {
      return getNlpConfiguration().getBoolean(FILTER_ONTO_RECOGNITIONS);
   }

   @Override
   public boolean isFilterPossibilityWithHangingRelations () {
      return getNlpConfiguration().getBoolean(FILTER_POSSIBILITY_WITH_HANGING_RELATIONS);
   }

   @Override
   public boolean isNlpTimeBasedCutOffEnabled () {
      return getNlpConfiguration().getBoolean(NLP_TIME_BASED_CUTOFF_ENABLED_KEY);
   }

   @Override
   public boolean isChooseBestpossibility () {
      return getNlpConfiguration().getBoolean(CHOOSE_BEST_POSSIBILITY);
   }

   @Override
   public Long getMaxAllowledTimeForEnhancingSemantics () {
      return getNlpConfiguration().getLong(MAX_ALLOWED_NLP_TIME_FOR_ENHANCING_SEMANTICS);
   }

   @Override
   public String getCurrencyPatternRegex () {
      return getNlpConfiguration().getProperty(PRE_PROCESSOR_QUERY_NORMALIZER_CURRENCY_PATTERN_REGEX);
   }

   @Override
   public String getAreaPatternRegex () {
      return getNlpConfiguration().getProperty(PRE_PROCESSOR_QUERY_NORMALIZER_AREA_PATTERN_REGEX);
   }

   public Double getPathQualityThreshold () {
      return getNlpConfiguration().getDouble(PATH_QUALITY_THRESHOLD);
   }
   
   public String getTaggerDataFileLocationFlag () {
      return getNlpConfiguration().getProperty(TAGGER_DATA_FILE_LOCATION_FLAG_KEY);
   }
}