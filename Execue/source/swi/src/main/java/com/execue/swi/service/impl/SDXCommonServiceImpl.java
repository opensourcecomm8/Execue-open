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

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.OperationType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.MappingException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.dataaccess.ISDXDataAccessManager;
import com.execue.swi.exception.SWIExceptionCodes;
import com.execue.swi.service.IBusinessEntityMaintenanceService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXCommonService;

public abstract class SDXCommonServiceImpl implements ISDXCommonService {

   private ISDXDataAccessManager             sdxDataAccessManager;

   private ISWIConfigurationService          swiConfigurationService;

   private IKDXRetrievalService              kdxRetrievalService;

   private IBusinessEntityMaintenanceService businessEntityMaintenanceService;

   public void updateColumn (Long assetId, Long tableId, Colum colum) throws SDXException {
      updateColumnForMissingUnitPopulation(colum);
      updateBusinessEntityDefinitionInfoForColumn(assetId, tableId, colum);
      getSdxDataAccessManager().updateColumn(colum);
   }

   private void updateColumnForMissingUnitPopulation (Colum colum) {
      if (colum.getConversionType() != null && colum.getUnit() == null) {
         if (colum.getDataFormat() != null) {
            String dateFormat = colum.getDataFormat();
            if (DataType.DATETIME.equals(colum.getDataType()) && colum.getFileDateFormat() != null) {
               dateFormat = colum.getFileDateFormat();
            }
            DateQualifier dateQualifier = getSwiConfigurationService().getDateQualifier(dateFormat);
            if (dateQualifier != null) {
               colum.setUnit(dateQualifier.getValue());
            }
         }
      }
   }

   private void updateBusinessEntityDefinitionInfoForColumn (Long assetId, Long tableId, Colum colum)
            throws SDXException {
      try {
         BusinessEntityDefinition mappedConceptBED = getKdxRetrievalService().getMappedConceptBEDForColumn(assetId,
                  tableId, colum.getId());
         if (mappedConceptBED != null) {
            Model model = getKdxRetrievalService().getModelByUserModelGroupId(mappedConceptBED.getModelGroup().getId());
            getBusinessEntityMaintenanceService().createBusinessEntityMaintenanceInfo(
                     ExecueBeanManagementUtil.prepareBusinessEntityMaintenanceInfo(mappedConceptBED.getId(),
                              EntityType.CONCEPT, model.getId(), OperationType.UPDATE, null));
         }
      } catch (MappingException mappingException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, mappingException);
      } catch (KDXException kdxException) {
         throw new SDXException(SWIExceptionCodes.DEFAULT_EXCEPTION_CODE, kdxException);
      }
   }

   public ISDXDataAccessManager getSdxDataAccessManager () {
      return sdxDataAccessManager;
   }

   public void setSdxDataAccessManager (ISDXDataAccessManager sdxDataAccessManager) {
      this.sdxDataAccessManager = sdxDataAccessManager;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IBusinessEntityMaintenanceService getBusinessEntityMaintenanceService () {
      return businessEntityMaintenanceService;
   }

   public void setBusinessEntityMaintenanceService (IBusinessEntityMaintenanceService businessEntityMaintenanceService) {
      this.businessEntityMaintenanceService = businessEntityMaintenanceService;
   }

}
