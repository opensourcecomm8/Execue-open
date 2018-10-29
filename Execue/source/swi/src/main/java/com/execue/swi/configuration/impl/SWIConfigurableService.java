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


package com.execue.swi.configuration.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SystemVariable;
import com.execue.core.common.bean.kb.POSContext;
import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.configuration.ResourceHelper;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ILookupService;
import com.execue.swi.service.IVerticalService;

public class SWIConfigurableService implements IConfigurable {

   private ILookupService              lookupService;
   private IBaseKDXRetrievalService    baseKDXRetrievalService;
   private IKDXRetrievalService        kdxRetrievalService;
   private IVerticalService            verticalService;
   private SWIConfigurationServiceImpl swiConfigurationService;

   public void doConfigure () throws ConfigurationException {

      loadAbsorptionWizardDetails();

      // Load enum lookup value description map
      try {
         String enumTypeValueSeparator = getSwiConfigurationService().getEnumLookupNameValueSeprator();
         Map<String, String> enumLookupDescriptionMap = getLookupService().getEnumLookupValueDescriptionMap(
                  enumTypeValueSeparator);
         EnumLookupHelper.setEnumTypeValueSeparator(enumTypeValueSeparator);
         EnumLookupHelper.setEnumLookupDescriptionMap(enumLookupDescriptionMap);

         // loading list of resource table into memory (initializing)
         ResourceHelper.setResourceLookupMap(getLookupService().getResourcesLookupMap());
         //load pos context
         loadPosContext();
         //load type bed ids for secondary RI cloud entry
         loadTypeBEDIdsRestrictedForSecondaryRICloudEntry();
         // set visible variables
         getSwiConfigurationService().setVisibleVerticals(getVerticalService().getAllVisibleVerticals());
         // Load the DateFormat details into the map
         getSwiConfigurationService().setSupportedDateFormatByFormatAndProviderTypeMap(
                  getLookupService().getSupportedDateFormatByFormatAndProviderTypeMap());

         // Load the DateQualifier map
         getSwiConfigurationService().setDateQualifierByFormatMap(getLookupService().getDateQualifierByFormatMap());
         // set System variable
         getSwiConfigurationService().setSystemVariables(getBaseKDXRetrievalService().getSystemVariables());
         getSwiConfigurationService().setSystemVariableWords(
                  getSystemVariableWords(getSwiConfigurationService().getSystemVariables()));
         loadBaseRelations();
         loadNonRealizableTypeBedIds();
      } catch (SWIException swiException) {
         throw new ConfigurationException(swiException.getCode(), swiException.getMessage());
      }
   }

   private void loadPosContext () throws ConfigurationException {
      try {
         POSContext posContext = new POSContext();
         posContext.setConjAndByConjTermNames(baseKDXRetrievalService.getInstanceTermsForConjuctionConcepts());
         getSwiConfigurationService().setPosContext(posContext);
      } catch (KDXException kdxe) {
         throw new ConfigurationException(kdxe.getCode(), kdxe);
      }
   }

   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   private void loadTypeBEDIdsRestrictedForSecondaryRICloudEntry () throws KDXException {
      List<String> typeNames = getSwiConfigurationService().getTypesRestrictedForSecondaryRICloud();
      List<Long> typeBeds = new ArrayList<Long>();
      for (String typeName : typeNames) {
         typeBeds.add(getKdxRetrievalService().getTypeBedByName(typeName).getId());
      }
      getSwiConfigurationService().setTypeBEDIdsRestrictedForSecondaryRICloudEntry(typeBeds);
   }

   private void loadAbsorptionWizardDetails () {
      AbsorptionWizardHelper.setSourceTypes(getSwiConfigurationService().getSwiWizardSourceTypes());
      AbsorptionWizardHelper.setWizardPaths(getSwiConfigurationService().getSwiWizardPaths());
      AbsorptionWizardHelper.setUrlParams(getSwiConfigurationService().getSwiWizardUrlRequestParameters());
      AbsorptionWizardHelper.setSourceTypeWizardPathsMap(getSwiConfigurationService()
               .populateSourceTypeWizardPathsMap());
      AbsorptionWizardHelper.setWizardPathLinkDetailMap(getSwiConfigurationService().populateWizardPathLinkDetailMap());
   }

   public ILookupService getLookupService () {
      return lookupService;
   }

   public void setLookupService (ILookupService lookupService) {
      this.lookupService = lookupService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService
    *           the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   /**
    * @param typeBEDIdsRestrictedForSecondaryRICloudEntry
    *           the typeBEDIdsRestrictedForSecondaryRICloudEntry to set
    */

   public IVerticalService getVerticalService () {
      return verticalService;
   }

   public void setVerticalService (IVerticalService verticalService) {
      this.verticalService = verticalService;
   }

   private Set<String> getSystemVariableWords (Set<SystemVariable> systemVariables) {
      Set<String> sysVariableWords = new HashSet<String>();
      for (SystemVariable sysVar : systemVariables) {
         sysVariableWords.add(sysVar.getWord().toUpperCase());
      }
      return sysVariableWords;
   }

   private void loadBaseRelations () {
      try {
         List<Relation> relations = getBaseKDXRetrievalService().getBaseRelations();
         if (ExecueCoreUtil.isCollectionNotEmpty(relations)) {
            for (Relation relation : relations) {
               getSwiConfigurationService().getBaseRelations().add(relation.getName());
            }
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   private void loadNonRealizableTypeBedIds () throws KDXException {
      Set<Long> nonRealizableTypesbedIds = getBaseKDXRetrievalService().getNonRealizableTypeBedIds();
      if (ExecueCoreUtil.isCollectionNotEmpty(nonRealizableTypesbedIds)) {
         getSwiConfigurationService().getNonRealizableTypeBedIds().addAll(nonRealizableTypesbedIds);
      }

   }

   /**
    * @return the swiConfigurationService
    */
   public SWIConfigurationServiceImpl getSwiConfigurationService () {
      return swiConfigurationService;
   }

   /**
    * @param swiConfigurationService
    *           the swiConfigurationService to set
    */
   public void setSwiConfigurationService (SWIConfigurationServiceImpl swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

}
