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

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.IAssetOperationDetailDAO;
import com.execue.swi.dataaccess.IAssetOperationDetailDataAcessManager;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;

public class AssetOperationDetailDataAcessManagerImpl implements IAssetOperationDetailDataAcessManager {

   public IAssetOperationDetailDAO assetOperationDetailDAO;

   @Override
   public void createAssetOperationDetail (AssetOperationDetail assetOperationDetail) throws SDXException {
      try {
         getAssetOperationDetailDAO().create(assetOperationDetail);
      } catch (DataAccessException dae) {
         throw new SDXException(dae.getCode(), dae);
      }
   }

   @Override
   public void deleteAssetOperationDetail (AssetOperationDetail assetOperationDetail) throws SDXException {
      try {
         getAssetOperationDetailDAO().delete(assetOperationDetail);
      } catch (DataAccessException dae) {
         throw new SDXException(dae.getCode(), dae);
      }
   }

   @Override
   public void updateAssetOperationDetail (AssetOperationDetail assetOperationDetail) throws SDXException {
      try {
         getAssetOperationDetailDAO().update(assetOperationDetail);
      } catch (DataAccessException dae) {
         throw new SDXException(dae.getCode(), dae);
      }
   }

   @Override
   public List<AssetOperationDetail> getAssetOperationDetailByAssetId (Long assetId) throws SDXException {
      try {
         return getAssetOperationDetailDAO().getAssetOperationDetailByAssetId(assetId);
      } catch (DataAccessException dae) {
         throw new SDXException(dae.getCode(), dae);
      }
   }

   @Override
   public boolean isAssetUnderMaintenance (Long assetId) throws SDXException {
      try {
         return getAssetOperationDetailDAO().isAssetUnderMaintenance(assetId);
      } catch (DataAccessException dae) {
         throw new SDXException(dae.getCode(), dae);
      }
   }

   @Override
   public List<AssetOperationDetail> getUnderProcessAssetOperationDetailsByParentAssetId (Long patentAssetId)
            throws SDXException {
      try {
         return getAssetOperationDetailDAO().getUnderProcessAssetOperationDetailsByParentAssetId(patentAssetId);
      } catch (DataAccessException dae) {
         throw new SDXException(dae.getCode(), dae);
      }
   }

   public IAssetOperationDetailDAO getAssetOperationDetailDAO () {
      return assetOperationDetailDAO;
   }

   public void setAssetOperationDetailDAO (IAssetOperationDetailDAO assetOperationDetailDAO) {
      this.assetOperationDetailDAO = assetOperationDetailDAO;
   }

   @Override
   public <BusinessObject extends Serializable> BusinessObject getByField (String fieldValue, String fieldName,
            Class<BusinessObject> clazz) throws SWIException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws SWIException {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public List<? extends Serializable> getByIds (List<Long> ids, Class<?> clazz) throws SWIException {
      // TODO Auto-generated method stub
      return null;
   }

}
