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


package com.execue.semantification.unstructured.adapter.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.type.FeatureValueType;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IUDXService;
import com.execue.semantification.configuration.ISemantificationConfiguration;
import com.execue.semantification.exception.SemantificationException;
import com.execue.semantification.unstructured.adapter.IStructuredSemantificationAdapterImpl;
import com.execue.sus.helper.SemantificationHelper;
import com.execue.uswh.configuration.IUnstructuredWHConfigurationService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;
import com.execue.uswh.service.IUnstructuredWHFeatureContentLookupService;
import com.execue.uswh.service.IUnstructuredWarehouseManagementService;

/**
 * @author abhijit
 * @since Feb 17, 2011 : 11:04:11 AM
 * @version 1.0
 */
public class ClassifiedArticleSemantificationAdapterImpl implements IStructuredSemantificationAdapterImpl {

   private static Logger                              logger = Logger
                                                                      .getLogger(ClassifiedArticleSemantificationAdapterImpl.class);
   private IUnstructuredWarehouseManagementService    unstructuredWarehouseManagementService;
   private IUDXService                                udxService;
   private ISemantificationConfiguration              semantificationConfiguration;
   private SemantificationHelper                      semantificationHelper;
   private IUnstructuredWHConfigurationService        unstructuredWHConfigurationService;
   private IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService;

   public List<SemantifiedContentFeatureInformation> getFeatureInformationFromPossibilities (
            List<SemanticPossibility> semanticPossibilities) throws SemantificationException {
      try {
         return getSemantificationHelper().getSemantifiedContentFeaturesInformation(
                  semanticPossibilities.iterator().next(), true);
      } catch (UnstructuredWarehouseException e) {
         throw new SemantificationException(e.getCode(), e);
      }
   }

   /**
    * Store Reduced Form in Structured Content table
    * 
    * @param semanticPossibilities
    *           Possibility containing Reduced form
    * @param udxId
    *           UDX ID of the content
    */
   public void storeReducedFormAsStructuredContent (List<SemanticPossibility> semanticPossibilities, Long udxId) {
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return;
      }
      try {
         List<SemantifiedContentFeatureInformation> structuredContentList = getFeatureInformationFromPossibilities(semanticPossibilities);

         if (CollectionUtils.isEmpty(structuredContentList)) {
            if (logger.isDebugEnabled()) {
               logger.debug("No Car Info Found...");
            }
            return;
         }

         Map<Long, List<SemantifiedContentFeatureInformation>> carInfoByFeatureMap = UnstructuredWarehouseHelper
                  .getFeaturesInformationByFeatureId(structuredContentList);

         // TODO: NK: Update feature information for missing feature info based on the feature knowledge info we have

         // Populate the unknown features
         Map<Long, Map<Long, FeatureValueType>> existingFeatureByFeatureTypeMapByAppId = getSemantificationConfiguration()
                  .getExistingFeatureByFeatureTypeMapByAppId();
         Long applicationId = semanticPossibilities.get(0).getApplication().getId();
         Map<Long, FeatureValueType> existingFeatureByFeatureTypeMap = existingFeatureByFeatureTypeMapByAppId
                  .get(applicationId);
         if (!MapUtils.isEmpty(existingFeatureByFeatureTypeMap)) {
            for (Entry<Long, FeatureValueType> entry : existingFeatureByFeatureTypeMap.entrySet()) {
               Long featureId = entry.getKey();
               if (!carInfoByFeatureMap.containsKey(featureId)) {
                  try {
                     addUnknownFeatureInformation(structuredContentList, entry);
                  } catch (CloneNotSupportedException e) {
                     e.printStackTrace();
                  }
               }
            }
         }

         Date contentDate = null;
         try {
            UnStructuredIndex unstructuredIndex = getUdxService().getUnstructuredIndexById(udxId);
            contentDate = unstructuredIndex.getContentDate();
         } catch (UDXException e) {
            // For now, if there is no udx found because of some inconsistency, then set the content date as current
            // date
            e.printStackTrace();
            contentDate = Calendar.getInstance().getTime();
         }
         for (SemantifiedContentFeatureInformation structuredContent : structuredContentList) {
            structuredContent.setSemantifiedContentId(udxId);
            structuredContent.setSemantifiedContentDate(contentDate);
         }
         getUnstructuredWarehouseManagementService().saveSemantifiedContentFeatureInformation(applicationId,
                  structuredContentList);
      } catch (UnstructuredWarehouseException e) {
         e.printStackTrace();
      } catch (SemantificationException e) {
         e.printStackTrace();
      }
   }

   private void addUnknownFeatureInformation (List<SemantifiedContentFeatureInformation> structuredContentList,
            Entry<Long, FeatureValueType> entry) throws CloneNotSupportedException, UnstructuredWarehouseException {
      Long featureId = entry.getKey();
      FeatureValueType featureValueType = entry.getValue();
      // Getting the first car information and cloning it
      SemantifiedContentFeatureInformation unknownCarInformation = (SemantifiedContentFeatureInformation) structuredContentList
               .get(0).clone();
      unknownCarInformation.setFeatureId(featureId);
      unknownCarInformation.setFeatureValueType(featureValueType);
      unknownCarInformation.setFeatureWeight(10d);
      if (FeatureValueType.VALUE_STRING == featureValueType) {
         unknownCarInformation.setFeatureValue(getUnstructuredWHConfigurationService().getFeatureStringUnknownValue());
         unknownCarInformation.setFeatureNumberValue(null);
      } else if (FeatureValueType.VALUE_NUMBER == featureValueType) {
         unknownCarInformation.setFeatureValue(null);
         unknownCarInformation.setFeatureNumberValue(getUnstructuredWHFeatureContentLookupService()
                  .getFeatureRangeDefaultValue(unknownCarInformation.getContextId(), featureId));
      }
      structuredContentList.add(unknownCarInformation);
   }

   /**
    * @return the udxService
    */
   public IUDXService getUdxService () {
      return udxService;
   }

   /**
    * @param udxService
    *           the udxService to set
    */
   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }

   /**
    * @return the semantificationConfiguration
    */
   public ISemantificationConfiguration getSemantificationConfiguration () {
      return semantificationConfiguration;
   }

   /**
    * @param semantificationConfiguration
    *           the semantificationConfiguration to set
    */
   public void setSemantificationConfiguration (ISemantificationConfiguration semantificationConfiguration) {
      this.semantificationConfiguration = semantificationConfiguration;
   }

   public SemantificationHelper getSemantificationHelper () {
      return semantificationHelper;
   }

   public void setSemantificationHelper (SemantificationHelper semantificationHelper) {
      this.semantificationHelper = semantificationHelper;
   }

   /**
    * @return the unstructuredWarehouseManagementService
    */
   public IUnstructuredWarehouseManagementService getUnstructuredWarehouseManagementService () {
      return unstructuredWarehouseManagementService;
   }

   /**
    * @param unstructuredWarehouseManagementService the unstructuredWarehouseManagementService to set
    */
   public void setUnstructuredWarehouseManagementService (
            IUnstructuredWarehouseManagementService unstructuredWarehouseManagementService) {
      this.unstructuredWarehouseManagementService = unstructuredWarehouseManagementService;
   }

   public IUnstructuredWHConfigurationService getUnstructuredWHConfigurationService () {
      return unstructuredWHConfigurationService;
   }

   public void setUnstructuredWHConfigurationService (
            IUnstructuredWHConfigurationService unstructuredWHConfigurationService) {
      this.unstructuredWHConfigurationService = unstructuredWHConfigurationService;
   }

   public IUnstructuredWHFeatureContentLookupService getUnstructuredWHFeatureContentLookupService () {
      return unstructuredWHFeatureContentLookupService;
   }

   public void setUnstructuredWHFeatureContentLookupService (
            IUnstructuredWHFeatureContentLookupService unstructuredWHFeatureContentLookupService) {
      this.unstructuredWHFeatureContentLookupService = unstructuredWHFeatureContentLookupService;
   }

}