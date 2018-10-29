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

import com.execue.core.common.bean.entity.AssetDetail;
import com.execue.core.common.bean.entity.AssetExtendedDetail;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.dataaccess.IAssetDetailDataAccessManager;
import com.execue.swi.dataaccess.SDXDAOComponents;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIExceptionCodes;

public class AssetDetailDataAccessManagerImpl extends SDXDAOComponents implements IAssetDetailDataAccessManager {

   public <BusinessObject extends Serializable> BusinessObject getById (Long id, Class<BusinessObject> clazz)
            throws SDXException {
      try {
         return getAssetDetailDAO().getById(id, clazz);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public AssetDetail getAssetDetailInfo (Long assetId) throws SDXException {
      AssetDetail assetDetail = null;
      try {
         assetDetail = getAssetDetailDAO().getAssetDetailInfo(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return assetDetail;
   }

   public AssetExtendedDetail getAssetExtendedDetailInfo (Long assetDetailId) throws SDXException {
      AssetExtendedDetail assetExtendedDetail = null;
      try {
         assetExtendedDetail = getAssetDetailDAO().getAssetExtendedDetailInfo(assetDetailId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
      return assetExtendedDetail;
   }

   public void createAssetDetail (AssetDetail assetDetail) throws SDXException {
      try {
         getAssetDetailDAO().create(assetDetail);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void createAssetExtendedDetail (AssetExtendedDetail assetExtendedDetail) throws SDXException {
      try {
         getAssetDetailDAO().create(assetExtendedDetail);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public void updateAssetDetail (AssetDetail assetDetail) throws SDXException {
      try {
         getAssetDetailDAO().update(assetDetail);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public void updateAssetExtendedDetail (AssetExtendedDetail assetExtendedDetail) throws SDXException {
      try {
         getAssetDetailDAO().update(assetExtendedDetail);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }
   }

   public void deleteAssetDetail (Long assetId) throws SDXException {
      try {
         AssetDetail assetDetail = getAssetDetailDAO().getAssetDetailInfo(assetId);
         AssetExtendedDetail assetExtendedDetail = getAssetExtendedDetailInfo(assetDetail.getId());
         getAssetDetailDAO().delete(assetExtendedDetail);
         getAssetDetailDAO().delete(assetDetail);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, dataAccessException);
      }

   }

   public AssetExtendedDetail getAssetExtendedDetailByAssetId (Long assetId) throws SDXException {
      try {
         return getAssetDetailDAO().getAssetExtendedDetailByAssetId(assetId);
      } catch (DataAccessException dataAccessException) {
         throw new SDXException(dataAccessException.getCode(), dataAccessException);
      }

   }

}
