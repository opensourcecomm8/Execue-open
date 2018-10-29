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


package com.execue.semantification.configuration.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.unstructured.Feature;
import com.execue.core.common.bean.qdata.AppCategoryMapping;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.service.IRFXService;
import com.execue.semantification.configuration.ISemantificationConfiguration;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IApplicationRetrievalService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

public class SemantificationConfigurableService implements IConfigurable {

   private ISemantificationConfiguration          semantificationConfigurationService;
   private IRFXService                            rfxService;
   private ICoreConfigurationService              coreConfigurationService;
   private IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService;
   private IApplicationRetrievalService           applicationRetrievalService;

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   @Override
   public void doConfigure () throws ConfigurationException {
      loadSemantificationConfiguration();
   }

   private void loadSemantificationConfiguration () {
      try {
         getSemantificationConfigurationService().populateInvalidArticleTexts();
         getSemantificationConfigurationService().populateInvalidArticleContents();
         populateFeatureIdByFeatureTypeMapByAppId();
         populateAppCategoryMaps();
      } catch (UnstructuredWarehouseException e) {
         e.printStackTrace();
      } catch (RFXException e) {
         e.printStackTrace();
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   private void populateAppCategoryMaps () throws RFXException {
      List<AppCategoryMapping> appCategoryMappings = getRfxService().getApplicationCategoryMappings();
      for (AppCategoryMapping appCategoryMapping : appCategoryMappings) {
         NewsCategory newsCategory = NewsCategory.getType(appCategoryMapping.getCategoryName());
         if (SearchFilterType.VERTICAL == appCategoryMapping.getSearchFilterType()) {
            getSemantificationConfigurationService().getVerticalCategoryMap().put(newsCategory,
                     appCategoryMapping.getContextId());
         } else if (SearchFilterType.APP == appCategoryMapping.getSearchFilterType()) {
            getSemantificationConfigurationService().getApplicationCategoryMap().put(newsCategory,
                     appCategoryMapping.getContextId());
         }
      }
   }

   private void populateFeatureIdByFeatureTypeMapByAppId () throws UnstructuredWarehouseException, KDXException {
      List<Application> unstructuredApplications = getApplicationRetrievalService().getApplicationsByType(
               AppSourceType.UNSTRUCTURED);
      if (ExecueCoreUtil.isCollectionNotEmpty(unstructuredApplications)) {
         for (Application application : unstructuredApplications) {
            // HACK:: NK:: We are skipping the craigslist auto app here, should remove this check
            // once we have the craigslist auto app as generic unstructured app
            if (application.getName()
                     .equalsIgnoreCase(getCoreConfigurationService().getSkipDerivedUserQueryVariation())) {
               continue;
            }
            List<Feature> featureList = getUnstructuredWarehouseRetrievalService().getAllFeatures(application.getId());
            if (!CollectionUtils.isEmpty(featureList)) {
               for (Feature carFeature : featureList) {
                  Long contextId = carFeature.getContextId();
                  Map<Long, FeatureValueType> existingFeatureByFeatureTypeMap = getSemantificationConfigurationService()
                           .getExistingFeatureByFeatureTypeMapByAppId().get(contextId);
                  if (existingFeatureByFeatureTypeMap == null) {
                     existingFeatureByFeatureTypeMap = new HashMap<Long, FeatureValueType>(1);
                     getSemantificationConfigurationService().getExistingFeatureByFeatureTypeMapByAppId().put(
                              contextId, existingFeatureByFeatureTypeMap);
                  }
                  existingFeatureByFeatureTypeMap.put(carFeature.getFeatureId(), carFeature.getFeatureValueType());
               }
            }
         }
      }
   }

   public ISemantificationConfiguration getSemantificationConfigurationService () {
      return semantificationConfigurationService;
   }

   public void setSemantificationConfigurationService (ISemantificationConfiguration semantificationConfigurationService) {
      this.semantificationConfigurationService = semantificationConfigurationService;
   }

   public IRFXService getRfxService () {
      return rfxService;
   }

   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   /**
    * @return the applicationRetrievalService
    */
   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   /**
    * @param applicationRetrievalService the applicationRetrievalService to set
    */
   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }

}
