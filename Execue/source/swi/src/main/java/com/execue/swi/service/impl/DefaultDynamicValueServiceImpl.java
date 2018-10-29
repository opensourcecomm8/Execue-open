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

import java.util.List;

import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.swi.dataaccess.IKDXDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.service.IDefaultDynamicValueService;

public class DefaultDynamicValueServiceImpl implements IDefaultDynamicValueService {

   private IKDXDataAccessManager kdxDataAccessManager;

   public void createDefaultDynamicValue (DefaultDynamicValue defaultDynamicValue) throws MappingException {
      try {
         kdxDataAccessManager.createDefaultDynamicValue(defaultDynamicValue);
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
   }

   public void createDefaultDynamicValues (List<DefaultDynamicValue> defaultDynamicValues) throws MappingException {
      try {
         kdxDataAccessManager.createDefaultDynamicValues(defaultDynamicValues);
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
   }

   public void deleteDefaultDynamicValue (DefaultDynamicValue defaultDynamicValue) throws MappingException {
      try {
         kdxDataAccessManager.deleteDefaultDynamicValue(defaultDynamicValue);
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
   }

   public void deleteDefaultDynamicValueByBedId (Long bedId) throws MappingException {
      try {
         kdxDataAccessManager.deleteDefaultDynamicValueByBedId(bedId);
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
   }

   public void deleteDefaultDynamicValues (Long assetId) throws MappingException {
      deleteDefaultDynamicValues(getDefaultDynamicValues(assetId));
   }

   public void deleteDefaultDynamicValues (List<DefaultDynamicValue> defaultDynamicValues) throws MappingException {
      try {
         for (DefaultDynamicValue defaultDynamicValue : defaultDynamicValues) {
            kdxDataAccessManager.deleteDefaultDynamicValue(defaultDynamicValue);
         }
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }

   }

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId) throws MappingException {
      try {
         return kdxDataAccessManager.getDefaultDynamicValues(assetId);
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
   }

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId, List<Long> bedIds) throws MappingException {
      try {
         return kdxDataAccessManager.getDefaultDynamicValues(assetId, bedIds);
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
   }

   public List<DefaultDynamicValue> getDefaultDynamicValues (Long assetId, Long bedId) throws MappingException {
      try {
         return kdxDataAccessManager.getDefaultDynamicValues(assetId, bedId);
      } catch (KDXException e) {
         throw new MappingException(e.getCode(), e);
      }
   }

   /**
    * @return the kdxDataAccessManager
    */
   public IKDXDataAccessManager getKdxDataAccessManager () {
      return kdxDataAccessManager;
   }

   /**
    * @param kdxDataAccessManager
    *           the kdxDataAccessManager to set
    */
   public void setKdxDataAccessManager (IKDXDataAccessManager kdxDataAccessManager) {
      this.kdxDataAccessManager = kdxDataAccessManager;
   }

}
