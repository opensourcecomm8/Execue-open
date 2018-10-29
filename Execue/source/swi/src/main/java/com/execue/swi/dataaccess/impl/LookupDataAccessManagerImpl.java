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


package com.execue.swi.dataaccess.impl;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.CountryLookup;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.EnumLookup;
import com.execue.core.common.bean.entity.ReportGroup;
import com.execue.core.common.bean.entity.ReportType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.ILookupDAO;
import com.execue.swi.dataaccess.ILookupDataAccessManager;

public class LookupDataAccessManagerImpl implements ILookupDataAccessManager {

   private ILookupDAO lookupDAO;

   public ILookupDAO getLookupDAO () {
      return lookupDAO;
   }

   public void setLookupDAO (ILookupDAO lookupDAO) {
      this.lookupDAO = lookupDAO;
   }

   public List<ReportGroup> getReportGroups () throws DataAccessException {
      return getLookupDAO().getList(ReportGroup.class);
   }

   public List<ReportType> getReportTypes () throws DataAccessException {
      return getLookupDAO().getList(ReportType.class);
   }

   public DefaultDynamicValue getDefaultDynamicValue (Long assetId, Long lhsDEDId, DynamicValueQualifierType qualifier)
            throws DataAccessException {
      return lookupDAO.getDefaultDynamicValue(assetId, lhsDEDId, qualifier);
   }

   public List<EnumLookup> getAllEnumLookupValueDescriptions () throws DataAccessException {
      return getLookupDAO().getEnumLookupValueDescriptions();
   }

   public List<Object> getList (Class clazz) throws DataAccessException {
      return getLookupDAO().getList(clazz);
   }

   public List<CountryLookup> getCountryCodes () throws DataAccessException {
      return getLookupDAO().getCountryCodes();
   }
   
   public Map<String, DateQualifier> getDateQualifierByFormatMap () throws DataAccessException {
      return getLookupDAO().getDateQualifierByFormatMap();
   }
}
