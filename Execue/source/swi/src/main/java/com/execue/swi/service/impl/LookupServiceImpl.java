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


package com.execue.swi.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.bean.Resource;
import com.execue.core.common.bean.entity.CountryLookup;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.EnumLookup;
import com.execue.core.common.bean.entity.ReportGroup;
import com.execue.core.common.bean.entity.ReportType;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.dataaccess.ILookupDataAccessManager;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.ILookupService;

public class LookupServiceImpl implements ILookupService {

   private static final Logger      logger = Logger.getLogger(LookupServiceImpl.class);
   private ILookupDataAccessManager lookupDataAccessManager;
   private IConversionService       conversionService;

   public ILookupDataAccessManager getLookupDataAccessManager () {
      return lookupDataAccessManager;
   }

   public void setLookupDataAccessManager (ILookupDataAccessManager lookupDataAccessManager) {
      this.lookupDataAccessManager = lookupDataAccessManager;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public List<ReportGroup> getReportGroups () {
      try {
         return lookupDataAccessManager.getReportGroups();
      } catch (Exception e) {
         logger.error("Error", e);
      }
      return new ArrayList<ReportGroup>();
   }

   public List<ReportType> getReportTypes () {
      try {
         return lookupDataAccessManager.getReportTypes();
      } catch (Exception e) {
         logger.error("Error", e);
      }
      return new ArrayList<ReportType>();
   }

   public DefaultDynamicValue getDefaultDynamicValue (Long assetId, Long lhsDEDId, DynamicValueQualifierType qualifier) {
      DefaultDynamicValue defaultDynamicValue = null;
      try {
         defaultDynamicValue = lookupDataAccessManager.getDefaultDynamicValue(assetId, lhsDEDId, qualifier);
      } catch (DataAccessException e) {
         e.printStackTrace();
      }
      return defaultDynamicValue;
   }

   public Map<String, String> getEnumLookupValueDescriptionMap (String typeValueSeparator) throws SWIException {
      Map<String, String> enumLookupValueDescriptionMap = new HashMap<String, String>();
      try {
         List<EnumLookup> enumLookups = getLookupDataAccessManager().getAllEnumLookupValueDescriptions();
         for (EnumLookup enumLookup : enumLookups) {
            enumLookupValueDescriptionMap.put(enumLookup.getType() + typeValueSeparator + enumLookup.getValue(),
                     enumLookup.getDescription());
         }
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae.getMessage());
      }
      return enumLookupValueDescriptionMap;
   }

   public Map<String, Resource> getResourcesLookupMap () throws SWIException {
      try {
         Map<String, Resource> resourceLookupMap = new HashMap<String, Resource>();
         for (Object resourceItem : lookupDataAccessManager.getList(Resource.class)) {
            Resource resource = new Resource();
            resource = (Resource) resourceItem;
            String nameAsMapKey = resource.getName();
            resourceLookupMap.put(nameAsMapKey, resource);
         }
         logger.debug("Done with Loading of resourceLookupMap with size: " + resourceLookupMap.size());
         return resourceLookupMap;
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae.getMessage());
      }
   }

   public List<CountryLookup> getCountryCodes () throws SWIException {
      List<CountryLookup> countryLookups = new ArrayList<CountryLookup>();
      try {
         countryLookups = lookupDataAccessManager.getCountryCodes();
      } catch (DataAccessException e) {
         e.printStackTrace();
         throw new SWIException(e.getCode(), e.getMessage());
      }
      return countryLookups;
   }

   public Map<Long, VerticalEntityBasedRedirection> getVerticalEntityBasedRedirectionMap () throws SWIException {
      try {
         Map<Long, VerticalEntityBasedRedirection> verticalEntityBasedRedirectionMap = new HashMap<Long, VerticalEntityBasedRedirection>();
         for (Object verticalBasedEntity : lookupDataAccessManager.getList(VerticalEntityBasedRedirection.class)) {
            VerticalEntityBasedRedirection verticalEntityBasedRedirection = new VerticalEntityBasedRedirection();
            verticalEntityBasedRedirection = (VerticalEntityBasedRedirection) verticalBasedEntity;
            Long verticalIdMapKey = 10L;
            verticalEntityBasedRedirectionMap.put(verticalIdMapKey, verticalEntityBasedRedirection);
         }
         return verticalEntityBasedRedirectionMap;
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae.getMessage());
      }
   }

   public Map<Long, List<VerticalEntityBasedRedirection>> getAppBusinessEntityMap () throws SWIException {
      try {
         Map<Long, List<VerticalEntityBasedRedirection>> appBusinessEntityMap = new HashMap<Long, List<VerticalEntityBasedRedirection>>();
         List<Object> list = lookupDataAccessManager.getList(VerticalEntityBasedRedirection.class);
         for (Object verticalEntityBasedRedirection : list) {
            VerticalEntityBasedRedirection appBusinessEntity = (VerticalEntityBasedRedirection) verticalEntityBasedRedirection;
            if (appBusinessEntity != null) {
               if (appBusinessEntityMap.containsKey(appBusinessEntity.getApplicationId())) {
                  List<VerticalEntityBasedRedirection> beds = appBusinessEntityMap.get(appBusinessEntity
                           .getApplicationId());
                  beds.add(appBusinessEntity);
               } else {
                  List<VerticalEntityBasedRedirection> bedIds = new ArrayList<VerticalEntityBasedRedirection>();
                  bedIds.add(appBusinessEntity);
                  appBusinessEntityMap.put(appBusinessEntity.getApplicationId(), bedIds);
               }
            }
         }
         return appBusinessEntityMap;
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae.getMessage());
      }
   }

   public Map<String, DateFormat> getDateFormatByFormatAndProviderTypeMap () throws SWIException {
      Map<String, DateFormat> dateFormatByFormatAndProviderTypeMap = new HashMap<String, DateFormat>();

      fillMapByFormatAndProviderType(dateFormatByFormatAndProviderTypeMap, getConversionService().getAllDateFormats());

      return dateFormatByFormatAndProviderTypeMap;
   }

   public Map<String, DateFormat> getSupportedDateFormatByFormatAndProviderTypeMap () throws SWIException {
      Map<String, DateFormat> supportedDateFormatByFormatAndProviderTypeMap = new HashMap<String, DateFormat>();

      fillMapByFormatAndProviderType(supportedDateFormatByFormatAndProviderTypeMap, getConversionService()
               .getAllSupportedDateFormats());

      return supportedDateFormatByFormatAndProviderTypeMap;
   }

   public Map<String, DateQualifier> getDateQualifierByFormatMap () throws SWIException {
      try {
         return getLookupDataAccessManager().getDateQualifierByFormatMap();
      } catch (DataAccessException dae) {
         throw new SWIException(dae.getCode(), dae);
      }
   }

   private void fillMapByFormatAndProviderType (Map<String, DateFormat> dateFormatByFormatAndProviderTypeMap,
            List<DateFormat> allDateFormats) {
      for (DateFormat dateFormat : allDateFormats) {
         dateFormatByFormatAndProviderTypeMap.put(
                  dateFormat.getFormat() + dateFormat.getAssetProviderType().getValue(), dateFormat);
      }
   }
}
