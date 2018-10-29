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


package com.execue.swi.service;

import java.util.List;
import java.util.Map;

import com.execue.core.IService;
import com.execue.core.bean.Resource;
import com.execue.core.common.bean.entity.CountryLookup;
import com.execue.core.common.bean.entity.DateFormat;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.ReportGroup;
import com.execue.core.common.bean.entity.ReportType;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.swi.exception.SWIException;

/**
 * @author kaliki
 * @since 4.0 This service is used to lookup (cached) objects. These can be database or in memory objects.
 */

public interface ILookupService extends IService {

   public List<ReportGroup> getReportGroups ();

   public List<ReportType> getReportTypes ();

   public DefaultDynamicValue getDefaultDynamicValue (Long assetId, Long lhsDEDId, DynamicValueQualifierType qualifier);

   /**
    * Get all the EnumLookup objects available and prepare a HashMap with key and value as described key - concatenation
    * of type and value, ~#~ (from configuration) as the separator value - description
    * 
    * @return
    * @throws SWIException
    */
   public Map<String, String> getEnumLookupValueDescriptionMap (String typeValueSeparator) throws SWIException;

   public Map<String, Resource> getResourcesLookupMap () throws SWIException;

   public List<CountryLookup> getCountryCodes () throws SWIException;

   public Map<Long, VerticalEntityBasedRedirection> getVerticalEntityBasedRedirectionMap () throws SWIException;

   public Map<String, DateFormat> getDateFormatByFormatAndProviderTypeMap () throws SWIException;

   public Map<String, DateFormat> getSupportedDateFormatByFormatAndProviderTypeMap () throws SWIException;

   public Map<String, DateQualifier> getDateQualifierByFormatMap () throws SWIException;

   public Map<Long, List<VerticalEntityBasedRedirection>> getAppBusinessEntityMap () throws SWIException;

}
