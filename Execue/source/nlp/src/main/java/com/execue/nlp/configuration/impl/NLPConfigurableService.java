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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections.CollectionUtils;
import org.xml.sax.SAXException;

import com.execue.core.common.bean.entity.Cloud;
import com.execue.core.common.type.CloudCategory;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.nlp.bean.rules.RuleRegexComponent;
import com.execue.nlp.bean.rules.validation.ValidationRulesContent;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRule;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRulesContent;
import com.execue.nlp.exception.NLPSystemException;
import com.execue.nlp.parser.RegexComponentParser;
import com.execue.nlp.parser.ValidationRulesParser;
import com.execue.nlp.parser.WeightAssignmentRulesParser;
import com.execue.nlp.rule.IValidationRule;
import com.execue.nlp.rule.IWeightAssignmentRule;
import com.execue.nlp.service.impl.VerbFormServiceImpl;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXCloudRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class NLPConfigurableService implements IConfigurable {

   private IKDXRetrievalService        kdxRetrievalService;
   private IBaseKDXRetrievalService    baseKDXRetrievalService;
   private IKDXCloudRetrievalService   kdxCloudRetrievalService;
   private NLPConfigurationServiceImpl nlpConfigurationService;
   private RegexComponentParser        regexComponentParser;
   private ValidationRulesParser       validationRulesParser;
   private WeightAssignmentRulesParser weightAssignmentRulesParser;
   private VerbFormServiceImpl         verbFormService;
   private ICoreConfigurationService   coreConfigurationService;

   @Override
   public void doConfigure () throws ConfigurationException {
      getNlpConfigurationService().loadWordTypeWeightMap();
      if (getCoreConfigurationService().isNLPTaggerInitiationRequired()) {
         MaxentTagger.init(getNlpConfigurationService().getTaggerDataFile());
      }
      getNlpConfigurationService().loadProcessors();
      loadRegExComponents();
      loadValidationRules();
      loadWeightAssignmentRules();
      getNlpConfigurationService().loadSearchTypeFlagMap();
      getVerbFormService().loadVerbForms();
      getNlpConfigurationService().loadAppMapForPenaltyValues();
      getNlpConfigurationService().loadAppSpecificMethodMap();
      loadUnitAndOperatorMaps();
      getNlpConfigurationService().loadAllowedUnconnectedTypes();
      populateCloudByIdMap();

   }

   private void loadRegExComponents () {
      List<String> fileNames = getNlpConfigurationService().getRegexComponentDataFiles();
      List<RuleRegexComponent> ruleRegExComponents = new ArrayList<RuleRegexComponent>();
      try {
         for (String fileName : fileNames) {
            getRegexComponentParser().setFileName(fileName);
            ruleRegExComponents.addAll(getRegexComponentParser().getRegExComponents());
         }
         getNlpConfigurationService().loadRuleRegExComponents(ruleRegExComponents);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   private void loadValidationRules () {
      try {
         List<String> fileNames = getNlpConfigurationService().getValidationRuleFiles();
         getValidationRulesParser().setRuleComponents(getNlpConfigurationService().getRuleRegExComponents());
         ValidationRulesContent validationRulesContent = new ValidationRulesContent();
         validationRulesContent.setValidationRules(new HashMap<Long, IValidationRule>());

         for (String fileName : fileNames) {
            getValidationRulesParser().setFileName(fileName);
            ValidationRulesContent tempValidationRulesContent = getValidationRulesParser().getValidationRulesContent(
                     getValidationRulesParser().getRuleComponents());
            validationRulesContent.getValidationRules().putAll(tempValidationRulesContent.getValidationRules());
         }
         getNlpConfigurationService().loadValidationRulesContent(validationRulesContent);
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   private void loadWeightAssignmentRules () {
      List<String> fileNames = getNlpConfigurationService().getWeightAssignmentRuleDataFiles();
      getWeightAssignmentRulesParser().setRuleComponents(getNlpConfigurationService().getRuleRegExComponents());
      WeightAssignmentRulesContent tempAssignmentRulesContent = null;

      WeightAssignmentRulesContent weightAssignmentRulesContent = new WeightAssignmentRulesContent();
      weightAssignmentRulesContent.setWeightAssignmentRules(new HashMap<Long, IWeightAssignmentRule>());
      try {
         for (String fileName : fileNames) {
            tempAssignmentRulesContent = null;
            getWeightAssignmentRulesParser().setFileName(fileName);
            tempAssignmentRulesContent = getWeightAssignmentRulesParser().getWeightAssignmentRules();
            if (weightAssignmentRulesContent.getDefaultLeftWeightAssignmentRule() == null
                     || weightAssignmentRulesContent.getDefaultRightWeightAssignmentRule() == null) {
               weightAssignmentRulesContent
                        .setDefaultLeftWeightAssignmentRule((WeightAssignmentRule) tempAssignmentRulesContent
                                 .getDefaultLeftWeightAssignmentRule());
               weightAssignmentRulesContent
                        .setDefaultRightWeightAssignmentRule((WeightAssignmentRule) tempAssignmentRulesContent
                                 .getDefaultRightWeightAssignmentRule());
            }
            weightAssignmentRulesContent.getWeightAssignmentRules().putAll(
                     tempAssignmentRulesContent.getWeightAssignmentRules());
         }
         getNlpConfigurationService().loadWeightAssignmentRulesContent(weightAssignmentRulesContent);

         // weightAssignmentRulesContent = getWeightAssignmentRulesParser().getWeightAssignmentRules();
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (SWIException e) {
         e.printStackTrace();
      }

   }

   private void loadUnitAndOperatorMaps () throws ConfigurationException {
      try {
         getNlpConfigurationService().loadUnitSymbolMap(
                  getBaseKDXRetrievalService().getUnitMapByConceptName("CurrencySymbol"));
         getNlpConfigurationService().loadUnitScaleMap(
                  getBaseKDXRetrievalService().getUnitMapByConceptName("UnitScale"));
         getNlpConfigurationService().loadOperatorMap(
                  getBaseKDXRetrievalService().getOperatorMapByConceptName("Operator"));
         // add value prepositions in operatorMap
         addValuePrepositonsToOperartorMap();
         getNlpConfigurationService().loadInstanceNameToSymbolMap(
                  getBaseKDXRetrievalService().getOperatorNameToExprMap());
         ;
      } catch (KDXException e) {
         throw new ConfigurationException(ExeCueExceptionCodes.CONFIGURATION_LOAD_ERROR, e);
      }
   }

   private void addValuePrepositonsToOperartorMap () {
      getNlpConfigurationService().getOperatorMap().put("under", "<");
      getNlpConfigurationService().getOperatorMap().put("over", ">");
      getNlpConfigurationService().getOperatorMap().put("below", "<");
      getNlpConfigurationService().getOperatorMap().put("above", ">");
   }

   /**
    * Load the clouds type clouds/concept clouds and framework clouds by there id so that we don't need to query the
    * cloud table again and again.
    */
   private void populateCloudByIdMap () {
      List<Cloud> clouds = new ArrayList<Cloud>(1);
      try {
         List<Cloud> typeClouds = getKdxCloudRetrievalService().getCloudsByCategory(CloudCategory.TYPE_CLOUD);
         if (!CollectionUtils.isEmpty(typeClouds)) {
            clouds.addAll(typeClouds);
         }
         List<Cloud> conceptClouds = getKdxCloudRetrievalService().getCloudsByCategory(CloudCategory.CONCEPT_CLOUD);
         if (!CollectionUtils.isEmpty(conceptClouds)) {
            clouds.addAll(conceptClouds);
         }
         List<Cloud> frameworkClouds = getKdxCloudRetrievalService().getCloudsByCategory(CloudCategory.FRAMEWORK_CLOUD);
         if (!CollectionUtils.isEmpty(frameworkClouds)) {
            clouds.addAll(frameworkClouds);
         }
      } catch (KDXException e) {
         throw new NLPSystemException(e.code, e);
      }
      Map<Long, Cloud> baseCloudsById = new HashMap<Long, Cloud>(1);
      for (Cloud cloud : clouds) {
         baseCloudsById.put(cloud.getId(), cloud);
      }
      getNlpConfigurationService().loadBaseCloudsById(baseCloudsById);

   }

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();

   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the kdxCloudRetrievalService
    */
   public IKDXCloudRetrievalService getKdxCloudRetrievalService () {
      return kdxCloudRetrievalService;
   }

   /**
    * @param kdxCloudRetrievalService the kdxCloudRetrievalService to set
    */
   public void setKdxCloudRetrievalService (IKDXCloudRetrievalService kdxCloudRetrievalService) {
      this.kdxCloudRetrievalService = kdxCloudRetrievalService;
   }

   /**
    * @return the nlpConfigurationService
    */
   public NLPConfigurationServiceImpl getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   /**
    * @param nlpConfigurationService the nlpConfigurationService to set
    */
   public void setNlpConfigurationService (NLPConfigurationServiceImpl nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }

   /**
    * @return the regexComponentParser
    */
   public RegexComponentParser getRegexComponentParser () {
      return regexComponentParser;
   }

   /**
    * @param regexComponentParser the regexComponentParser to set
    */
   public void setRegexComponentParser (RegexComponentParser regexComponentParser) {
      this.regexComponentParser = regexComponentParser;
   }

   /**
    * @return the validationRulesParser
    */
   public ValidationRulesParser getValidationRulesParser () {
      return validationRulesParser;
   }

   /**
    * @param validationRulesParser the validationRulesParser to set
    */
   public void setValidationRulesParser (ValidationRulesParser validationRulesParser) {
      this.validationRulesParser = validationRulesParser;
   }

   /**
    * @return the weightAssignmentRulesParser
    */
   public WeightAssignmentRulesParser getWeightAssignmentRulesParser () {
      return weightAssignmentRulesParser;
   }

   /**
    * @param weightAssignmentRulesParser the weightAssignmentRulesParser to set
    */
   public void setWeightAssignmentRulesParser (WeightAssignmentRulesParser weightAssignmentRulesParser) {
      this.weightAssignmentRulesParser = weightAssignmentRulesParser;
   }

   /**
    * @return the verbFormService
    */
   public VerbFormServiceImpl getVerbFormService () {
      return verbFormService;
   }

   /**
    * @param verbFormService the verbFormService to set
    */
   public void setVerbFormService (VerbFormServiceImpl verbFormService) {
      this.verbFormService = verbFormService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
