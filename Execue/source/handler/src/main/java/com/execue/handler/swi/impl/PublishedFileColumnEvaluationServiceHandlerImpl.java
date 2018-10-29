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


package com.execue.handler.swi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.entity.PublishedFileTableInfo;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.qi.QIConversion;
import com.execue.core.common.bean.qi.suggest.SuggestionUnit;
import com.execue.core.common.bean.querygen.LimitEntity;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.PrimaryMappingType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.UIPublishedFileColumnInfo;
import com.execue.handler.UIPublishedFileEvaluatedColumnDetail;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;
import com.execue.handler.swi.IConversionServiceHandler;
import com.execue.handler.swi.IPublishedFileColumnEvaluationServiceHandler;
import com.execue.handler.transformer.TableTransformer;
import com.execue.platform.IVirtualTableManagementService;
import com.execue.platform.swi.IKDXMaintenanceService;
import com.execue.platform.swi.ISDX2KDXMappingService;
import com.execue.platform.swi.ISDXDeletionWrapperService;
import com.execue.platform.swi.ISWIPlatformRetrievalService;
import com.execue.publisher.evaluate.IPublisherDataEvaluationService;
import com.execue.publisher.validator.IPublisherColumnValidator;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IConversionService;
import com.execue.swi.service.IKDXManagementService;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IMappingManagementService;
import com.execue.swi.service.IMappingRetrievalService;
import com.execue.swi.service.IPublishedFileManagementService;
import com.execue.swi.service.IPublishedFileRetrievalService;
import com.execue.swi.service.ISDXDeletionService;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class PublishedFileColumnEvaluationServiceHandlerImpl implements IPublishedFileColumnEvaluationServiceHandler {

   private IConversionServiceHandler       conversionServiceHandler;
   private IPublishedFileRetrievalService  publishedFileRetrievalService;
   private IPublishedFileManagementService publishedFileManagementService;
   private IPublisherDataEvaluationService publisherDataEvaluationService;
   private ISDXRetrievalService            sdxRetrievalService;
   private ISDXManagementService           sdxManagementService;
   private ISDXDeletionService             sdxDeletionService;
   private IMappingRetrievalService        mappingRetrievalService;
   private IMappingManagementService       mappingManagementService;
   private IKDXRetrievalService            kdxRetrievalService;
   private IKDXManagementService           kdxManagementService;
   private IVirtualTableManagementService  virtualTableManagementService;
   private ISDX2KDXMappingService          sdx2kdxMappingService;
   private IConversionService              conversionService;
   private IKDXMaintenanceService          kdxMaintenanceService;
   private IPublisherColumnValidator       publisherColumnValidator;
   private ISWIPlatformRetrievalService    swiPlatformRetrievalService;
   private ISDXDeletionWrapperService      sdxDeletionWrapperService;

   public List<PublishedFileTableInfo> getPublishedFileTables (Long fileId) throws PublishedFileException {
      return getPublishedFileRetrievalService().getPublishedFileTableInfoByFileId(fileId);
   }

   public UIPublishedFileColumnInfo getModifiedUIPublishedFileColumnInfo (
            UIPublishedFileColumnInfo uiPublishedFileColumnInfo) throws PublishedFileException {
      QIConversion modifiedQIConversion = prepareQIConversion(uiPublishedFileColumnInfo.getConversionType());
      uiPublishedFileColumnInfo.setQiConversion(modifiedQIConversion);
      return uiPublishedFileColumnInfo;
   }

   public List<UIPublishedFileEvaluatedColumnDetail> getEvaluatedColumns (Long fileTableId)
            throws PublishedFileException {
      List<UIPublishedFileEvaluatedColumnDetail> UIPublishedFileEvaluatedColumnDetail = new ArrayList<UIPublishedFileEvaluatedColumnDetail>();
      List<PublishedFileTableDetails> publishedFileTableDetails = getPublishedFileRetrievalService()
               .getPublishedFileTableDetailsByTableId(fileTableId);

      if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTableDetails)) {
         UIPublishedFileEvaluatedColumnDetail = transformUIPublishedFileEvaluatedColumnDetail(publishedFileTableDetails);
      }
      return UIPublishedFileEvaluatedColumnDetail;
   }

   public List<UIPublishedFileEvaluatedColumnDetail> getEvaluatedColumnsByPage (Long fileTableId, Page page)
            throws PublishedFileException {
      List<UIPublishedFileEvaluatedColumnDetail> UIPublishedFileEvaluatedColumnDetail = new ArrayList<UIPublishedFileEvaluatedColumnDetail>();

      List<PublishedFileTableDetails> publishedFileTableDetails = getPublishedFileRetrievalService()
               .getPublishedFileTableDetailsByPage(fileTableId, page);
      if (ExecueCoreUtil.isCollectionNotEmpty(publishedFileTableDetails)) {
         UIPublishedFileEvaluatedColumnDetail = transformUIPublishedFileEvaluatedColumnDetail(publishedFileTableDetails);
      }
      return UIPublishedFileEvaluatedColumnDetail;
   }

   public List<UIPublishedFileEvaluatedColumnDetail> getAssetTableColumns (Long assetId, Long tableId)
            throws ExeCueException {
      List<UIPublishedFileEvaluatedColumnDetail> UIPublishedFileEvaluatedColumnDetail = new ArrayList<UIPublishedFileEvaluatedColumnDetail>();
      Tabl table = getSdxRetrievalService().getTableById(tableId);
      List<Colum> tableColumns = getSdxRetrievalService().getColumnsOfTable(table);

      if (ExecueCoreUtil.isCollectionNotEmpty(tableColumns)) {
         UIPublishedFileEvaluatedColumnDetail = transformUIAssetTableColumnDetail(assetId, tableId, tableColumns);
      }
      return UIPublishedFileEvaluatedColumnDetail;
   }

   public List<UIPublishedFileEvaluatedColumnDetail> getAssetTableColumnsByPage (Long assetId, Long tableId, Page page)
            throws ExeCueException {
      List<UIPublishedFileEvaluatedColumnDetail> UIPublishedFileEvaluatedColumnDetail = new ArrayList<UIPublishedFileEvaluatedColumnDetail>();

      List<Colum> tableColumns = getSdxRetrievalService().getAssetTableColumnsByPage(assetId, tableId, page);

      if (ExecueCoreUtil.isCollectionNotEmpty(tableColumns)) {
         UIPublishedFileEvaluatedColumnDetail = transformUIAssetTableColumnDetail(assetId, tableId, tableColumns);
      }
      return UIPublishedFileEvaluatedColumnDetail;
   }

   public List<UIMember> getAssetTableMembersByPage (Long assetId, Long tableId, Page page) throws ExeCueException {
      List<UIMember> membersByPage = new ArrayList<UIMember>();
      UIMember tempUIMember = null;
      List<Membr> assetTableMembers = getSdxRetrievalService().getAssetTableMembersByPage(assetId, tableId, page);
      if (ExecueCoreUtil.isCollectionNotEmpty(assetTableMembers)) {
         for (Membr membr : assetTableMembers) {
            BusinessEntityDefinition instanceBed = getMappingRetrievalService().getMappedInstanceForMember(assetId,
                     tableId, membr.getId());
            if (instanceBed != null) {
               tempUIMember = transformUIMember(instanceBed, membr);
               tempUIMember.setName(membr.getLookupValue());
               membersByPage.add(tempUIMember);
            } else {
               membersByPage.add(transformUIMember(membr));
            }
         }
      }
      return membersByPage;
   }

   /*
    * As of now we have validations for checking date formats supported and data type change to date if successful.
    * Another validation is about changing numeric nature to string nature, adjust the precision and data type to
    * string. Another validation is to stop user from changing string nature, this has a glitch lets say soemthing was
    * number to begin with, user changed to string then again he is changing to number, this will not be allowed(need a
    * solution here) I think of maintaining the original nature of column and then write validator on top of that. I
    * mean consider the original nature also.
    */
   public List<String> updateEvaluatedColumns (Long fileTableId,
            List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns) throws PublishedFileException {
      List<String> invalidErrorMessages = new ArrayList<String>();
      try {
         // TODO :-VG- validation for duplicate column names.
         // check the columns for date validity if original conversion type was date and user changed to non date,
         // then if existing data type is date,change it to string.
         // if user changed conversion type to date, if it is non api supported format like quarter,
         // we have to skip the date validator and if the corresponding data type was date, we will change to string
         // if column comes out of date validator with pass result, we will update the conversion type and format in the
         // database else return as error message
         /* Check if uniqueness is violated with in the requested columns itself, if so return from here it self */

         List<String> columnNames = getColumnNames(evaluatedColumns);

         invalidErrorMessages = validateUIColumnNamesForUniqueness(columnNames, invalidErrorMessages);

         /* If there are any error messages from column name validation, return error messages */
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidErrorMessages)) {
            return invalidErrorMessages;
         }

         List<String> dbColumnDisplayNames = getPublishedFileRetrievalService().getColumnDisplayNames(fileTableId);

         /* Transformed PublishedFileTableDetails from the input UI objects */
         List<PublishedFileTableDetails> transformedPublishedFileTableDetails = new ArrayList<PublishedFileTableDetails>();

         /* map for PublishedFileTableDetails coming from db (original, before updation) by id */
         Map<Long, PublishedFileTableDetails> unAlteredPublishedFileTableDetailsByIdMap = new HashMap<Long, PublishedFileTableDetails>();

         List<String> nonUniqueColumnNames = new ArrayList<String>();

         for (UIPublishedFileEvaluatedColumnDetail uiEvaluatedColumn : evaluatedColumns) {
            // pull the column from DB respective to the column object came from UI.
            PublishedFileTableDetails dbPublishedFileColumn = getPublishedFileRetrievalService()
                     .getPublishedFileTableDetailsById(
                              uiEvaluatedColumn.getColumnDetail().getPublishedFileTableDetailId());

            // If the display name is not changed then bypass the validation for uniqueness.
            if (!dbPublishedFileColumn.getEvaluatedColumnDisplayName().equalsIgnoreCase(
                     uiEvaluatedColumn.getColumnName())) {
               // if column display name is not unique, add error message
               if (dbColumnDisplayNames.contains(uiEvaluatedColumn.getColumnName())) {
                  if (!nonUniqueColumnNames.contains(uiEvaluatedColumn.getColumnName())) {
                     invalidErrorMessages.add("Field Name [" + uiEvaluatedColumn.getColumnName() + "] is not unique");
                     nonUniqueColumnNames.add(uiEvaluatedColumn.getColumnName());
                  }
               }
            }

            /*
             * populate the map for the later processing (used by date validation and updation), if column name
             * uniqueness validation is through with out any errors
             */
            PublishedFileTableDetails clonedPublishedFileTableDetails = ExecueBeanCloneUtil
                     .clonePublishedFileTableDetails(dbPublishedFileColumn);

            clonedPublishedFileTableDetails.setId(dbPublishedFileColumn.getId());

            unAlteredPublishedFileTableDetailsByIdMap.put(dbPublishedFileColumn.getId(),
                     clonedPublishedFileTableDetails);

            // add the column from db that is transformed from ui object to the list.
            transformedPublishedFileTableDetails.add(transformPublishedFileTableDetail(uiEvaluatedColumn,
                     dbPublishedFileColumn));
         }

         // Map populated and column display name uniqueness validation done.
         // If there are any error messages from column name validation, return error messages.
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidErrorMessages)) {
            return invalidErrorMessages;
         }

         // Validation to check column KDXDataType.
         invalidErrorMessages = validateColumnKDXDataType(evaluatedColumns, invalidErrorMessages);
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidErrorMessages)) {
            return invalidErrorMessages;
         }
         // Validation to check weather column conversion type can be changed.
         List<PublishedFileTableDetails> validTableColumns = new ArrayList<PublishedFileTableDetails>();
         invalidErrorMessages = publisherColumnValidator.validateColumnConversionType(fileTableId,
                  transformedPublishedFileTableDetails, unAlteredPublishedFileTableDetailsByIdMap, validTableColumns);
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidErrorMessages)) {
            return invalidErrorMessages;
         }
         getPublishedFileManagementService().updatePublishedFileTableDetails(validTableColumns);
      } catch (PublishedFileException publisherException) {
         publisherException.printStackTrace();
         throw new PublishedFileException(publisherException.getCode(), publisherException);
      } catch (SWIException swiException) {
         throw new PublishedFileException(swiException.getCode(), swiException);
      }
      return invalidErrorMessages;
   }

   private List<String> validateColumnKDXDataType (List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns,
            List<String> errorMessages) {
      for (UIPublishedFileEvaluatedColumnDetail evaluatedColumn : evaluatedColumns) {
         ColumnType kdxDataType = evaluatedColumn.getKdxDataType();
         ConversionType conversionType = evaluatedColumn.getColumnDetail().getConversionType();
         if (ColumnType.MEASURE.equals(kdxDataType)) {
            if (isConversionTypeNotNumericNature(conversionType)) {
               errorMessages.add("Column type can not be changed to MEASURE for field name "
                        + evaluatedColumn.getColumnName());
            }
         }
      }
      return errorMessages;
   }

   private boolean isConversionTypeNotNumericNature (ConversionType conversionType) {
      boolean conversionTypeNotNumeric = false;
      if (ConversionType.NULL.equals(conversionType) || ConversionType.DEFAULT.equals(conversionType)
               || ConversionType.DATE.equals(conversionType) || ConversionType.LOCATION.equals(conversionType)) {
         conversionTypeNotNumeric = true;
      }
      return conversionTypeNotNumeric;
   }

   public List<String> updateAssetTableColumns (Long modelId, Long assetId, Long tableId,
            List<UIPublishedFileEvaluatedColumnDetail> evaluatedColumns) throws ExeCueException {
      List<String> invalidErrorMessages = new ArrayList<String>();
      try {

         List<String> nonUniqueFieldNames = new ArrayList<String>();

         List<String> columnNames = getColumnNames(evaluatedColumns);
         invalidErrorMessages = validateUIColumnNamesForUniqueness(columnNames, invalidErrorMessages);

         /* If there are any error messages from column name validation, return error messages */
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidErrorMessages)) {
            return invalidErrorMessages;
         }

         // Validation to check column KDXDataType.
         invalidErrorMessages = validateColumnKDXDataType(evaluatedColumns, invalidErrorMessages);
         if (ExecueCoreUtil.isCollectionNotEmpty(invalidErrorMessages)) {
            return invalidErrorMessages;
         }

         List<String> dbColumnDisplayNames = getSdxRetrievalService().getColumnDisplayNames(assetId, tableId);

         List<String> dbConceptDisplayNames = getMappingRetrievalService().getMappedConceptDisplayNames(modelId,
                  assetId, tableId);
         Map<Long, Colum> originalDBColumByIdMap = new HashMap<Long, Colum>();

         Map<Long, Concept> originalDBConceptByIdMap = new HashMap<Long, Concept>();

         Colum originalDBColumn = null;

         Concept originalDBConcept = null;

         for (UIPublishedFileEvaluatedColumnDetail uiColumn : evaluatedColumns) {
            originalDBColumn = getSdxRetrievalService().getColumnById(
                     uiColumn.getColumnDetail().getPublishedFileTableDetailId());
            originalDBColumByIdMap.put(originalDBColumn.getId(), originalDBColumn);
            if (uiColumn.getColumnDetail().isConceptExist()) {
               originalDBConcept = getKdxRetrievalService().getConceptById(uiColumn.getColumnDetail().getConceptId());
               originalDBConceptByIdMap.put(originalDBConcept.getId(), originalDBConcept);
            }

            /**
             * All the validation comes here and return the corresponding error messages. it includes testing duplicates
             * for column display names as well as mapped concept display names both column display name and concept
             * display name should be in sync. it means whenever u modify concept display name, column display name
             * should also get modified, not the other way around(because that column will not be shown on screen and
             * instead concept will be shown)
             */
            if (!originalDBColumn.getDisplayName().equalsIgnoreCase(uiColumn.getColumnName())) {
               if (dbColumnDisplayNames.contains(uiColumn.getColumnName())) {
                  if (!nonUniqueFieldNames.contains(uiColumn.getColumnName())) {
                     invalidErrorMessages.add("Field Name [" + uiColumn.getColumnName() + "] is not unique");
                     nonUniqueFieldNames.add(uiColumn.getColumnName());
                  }
               }
            }

            if (uiColumn.getColumnDetail().isConceptExist()
                     && !originalDBConcept.getDisplayName().equalsIgnoreCase(uiColumn.getColumnName())) {
               if (dbConceptDisplayNames.contains(uiColumn.getColumnName())) {
                  if (!nonUniqueFieldNames.contains(uiColumn.getColumnName())) {
                     invalidErrorMessages.add("Field Name [" + uiColumn.getColumnName() + "] is not unique");
                     nonUniqueFieldNames.add(uiColumn.getColumnName());
                  }
               }
            }
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(invalidErrorMessages)) {
            return invalidErrorMessages;
         }

         for (UIPublishedFileEvaluatedColumnDetail uiColumn : evaluatedColumns) {
            originalDBColumn = originalDBColumByIdMap.get(uiColumn.getColumnDetail().getPublishedFileTableDetailId());

            handleColumnTypeChange(modelId, assetId, tableId, originalDBColumn, uiColumn);

            getSdxManagementService().updateColumn(assetId, tableId,
                     transformUIColumnsToDBColumn(uiColumn, originalDBColumn));

            if (uiColumn.getColumnDetail().isConceptExist()) {
               Concept concept = originalDBConceptByIdMap.get(uiColumn.getColumnDetail().getConceptId());
               concept.setDisplayName(uiColumn.getColumnName());
               getKdxManagementService().updateConcept(modelId, concept);
            }
            // TODO : -RG- Corresponding VL table value column needs updation
            // Get the original column and then propagate the changes to the related VL table value column if exists
            // Make sure only the required changes goes in to the VL table column, (should not over-ride the KDX Data
            // type)
            // Things to look after are, conversion details, Granularity, Display Name

            // get the related Virtual Lookup column
            Colum relatedVLColumn = getSdxRetrievalService().getRelatedVirtualLookupColumn(assetId, tableId,
                     originalDBColumn.getId());

            // if related Virtual Lookup column exists then transfer the details to related VL column
            if (relatedVLColumn != null) {
               relatedVLColumn.setConversionType(originalDBColumn.getConversionType());
               relatedVLColumn.setDataFormat(originalDBColumn.getDataFormat());
               relatedVLColumn.setDisplayName(originalDBColumn.getDisplayName());
               relatedVLColumn.setGranularity(originalDBColumn.getGranularity());
               relatedVLColumn.setUnit(originalDBColumn.getUnit());

               getSdxManagementService().updateColumn(assetId, tableId, relatedVLColumn);
            }
         }
      } catch (SDXException sdxException) {
         throw new PublishedFileException(sdxException.getCode(), sdxException);
      }
      return invalidErrorMessages;
   }

   public void updateAssetTableMembers (Long modelId, List<UIMember> members) throws ExeCueException {
      transformAndUpdateUIMembersToMembers(members);
      transformAndUpdateUIMembersToInstances(modelId, members);
   }

   private PublishedFileTableDetails transformPublishedFileTableDetail (
            UIPublishedFileEvaluatedColumnDetail uiEvaluatedColumn, PublishedFileTableDetails dbEvaluatedColumn)
            throws PublishedFileException {
      dbEvaluatedColumn.setEvaluatedColumnDisplayName(uiEvaluatedColumn.getColumnName());
      dbEvaluatedColumn.setGranularity(uiEvaluatedColumn.getGranularity());
      dbEvaluatedColumn.setKdxDataType(uiEvaluatedColumn.getKdxDataType());
      dbEvaluatedColumn.setUnitType(uiEvaluatedColumn.getColumnDetail().getConversionType());
      dbEvaluatedColumn.setFormat(uiEvaluatedColumn.getFormat());
      dbEvaluatedColumn.setUnit(uiEvaluatedColumn.getUnit());

      if ("null".equals(dbEvaluatedColumn.getFormat()) || StringUtils.isEmpty(dbEvaluatedColumn.getFormat())) {
         dbEvaluatedColumn.setFormat(null);
      }
      if ("null".equals(dbEvaluatedColumn.getUnit()) || StringUtils.isEmpty(dbEvaluatedColumn.getUnit())) {
         dbEvaluatedColumn.setUnit(null);
      }
      return dbEvaluatedColumn;
   }

   private Colum transformUIColumnsToDBColumn (UIPublishedFileEvaluatedColumnDetail uiColumn, Colum originalDBColumn)
            throws SWIException {
      originalDBColumn.setDisplayName(uiColumn.getColumnName());
      originalDBColumn.setDataFormat(uiColumn.getFormat());
      originalDBColumn.setUnit(uiColumn.getUnit());
      originalDBColumn.setGranularity(uiColumn.getGranularity());
      originalDBColumn.setKdxDataType(uiColumn.getKdxDataType());
      originalDBColumn.setConversionType(uiColumn.getColumnDetail().getConversionType());

      if ("null".equals(originalDBColumn.getDataFormat()) || StringUtils.isEmpty(originalDBColumn.getDataFormat())) {
         originalDBColumn.setDataFormat(null);
      }
      if ("null".equals(originalDBColumn.getUnit()) || StringUtils.isEmpty(originalDBColumn.getUnit())) {
         originalDBColumn.setUnit(null);
      }
      if (ConversionType.DEFAULT.equals(originalDBColumn.getConversionType())) {
         originalDBColumn.setConversionType(null);
      }
      return originalDBColumn;
   }

   private void handleColumnTypeChange (Long modelId, Long assetId, Long tableId, Colum originalAssetTableColumn,
            UIPublishedFileEvaluatedColumnDetail uiPublishedFileEvaluatedColumnDetail) throws SWIException {
      Asset asset = getSdxRetrievalService().getAsset(assetId);
      ColumnType originalColumnType = originalAssetTableColumn.getKdxDataType();
      ColumnType newColumnType = uiPublishedFileEvaluatedColumnDetail.getKdxDataType();
      if (ColumnType.DIMENSION.equals(originalColumnType) && !ColumnType.DIMENSION.equals(newColumnType)) {
         List<Tabl> tables = new ArrayList<Tabl>();
         Tabl virtualLookupTable = getSdxRetrievalService().getVirtualTableFromFactTableColumn(assetId, tableId,
                  originalAssetTableColumn.getName());
         tables.add(virtualLookupTable);
         getSdxDeletionWrapperService().deleteAssetTables(asset, tables);
         // TODO : -VG- this needs to be tested .
         // if (uiPublishedFileEvaluatedColumnDetail.getColumnDetail().isConceptExist()) {
         // Long conceptId = uiPublishedFileEvaluatedColumnDetail.getColumnDetail().getConceptId();
         // getKdxMaintenanceService().deleteInstancesHierarchyForConcept(modelId, conceptId);
         // }
      } else if (!ColumnType.DIMENSION.equals(originalColumnType) && ColumnType.DIMENSION.equals(newColumnType)) {
         String originalColumnName = originalAssetTableColumn.getName();
         Tabl factTable = getSdxRetrievalService().getTableById(tableId);
         Model model = getKdxRetrievalService().getModelById(modelId);
         Tabl virtualTable = new Tabl();
         virtualTable.setName(originalColumnName);
         virtualTable.setDisplayName(originalColumnName);
         virtualTable.setDescription(originalColumnName);
         virtualTable.setLookupType(LookupType.SIMPLE_LOOKUP);
         virtualTable.setLookupValueColumn(originalColumnName);
         virtualTable.setLookupDescColumn(originalColumnName);
         TableInfo virtualTableInfo = virtualTableManagementService.prepareVirtualTableInfo(asset, factTable,
                  virtualTable);
         virtualTableManagementService.createVirtualTableInfo(asset, virtualTableInfo);
         if (uiPublishedFileEvaluatedColumnDetail.getColumnDetail().isConceptExist()) {
            Long conceptId = uiPublishedFileEvaluatedColumnDetail.getColumnDetail().getConceptId();
            Colum virtualTableLookupValueColumn = getMatchedLookupValueColumn(virtualTableInfo, originalColumnName);
            Mapping mapping = new Mapping();
            AssetEntityDefinition aed = getSdxRetrievalService().getAssetEntityDefinitionByIds(asset,
                     virtualTableInfo.getTable(), virtualTableLookupValueColumn, null);
            BusinessEntityDefinition bed = getKdxRetrievalService().getBusinessEntityDefinitionByIds(modelId,
                     conceptId, null);
            mapping.setAssetEntityDefinition(aed);
            mapping.setBusinessEntityDefinition(bed);
            mapping.setPrimary(PrimaryMappingType.PRIMARY);
            List<Mapping> mappings = new ArrayList<Mapping>();
            mappings.add(mapping);
            getMappingManagementService().createMappings(mappings);
            getSdx2kdxMappingService().mapMembersForAssetSyncUpProcess(asset, virtualTableInfo.getTable(),
                     virtualTableLookupValueColumn, virtualTableInfo.getMembers(), bed, model);
         }
      }
   }

   private Colum getMatchedLookupValueColumn (TableInfo virtualTableInfo, String originalColumnName) {
      Colum matchedColumn = null;
      for (Colum colum : virtualTableInfo.getColumns()) {
         if (originalColumnName.equalsIgnoreCase(colum.getName())) {
            matchedColumn = colum;
            break;
         }
      }
      return matchedColumn;
   }

   private void transformAndUpdateUIMembersToMembers (List<UIMember> members) throws SDXException {
      List<Membr> transformedMembers = new ArrayList<Membr>();
      for (UIMember uiMember : members) {
         if (!uiMember.isInstanceExist()) {
            Membr originalMember = getSdxRetrievalService().getMemberById(uiMember.getId());
            if (!StringUtils.isBlank(originalMember.getLookupDescription())
                     && !originalMember.getLookupDescription().equals(uiMember.getDescription())) {
               originalMember.setLookupDescription(uiMember.getDescription());
               transformedMembers.add(originalMember);
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(transformedMembers)) {
         getSdxManagementService().updateMembers(transformedMembers);
      }
   }

   private void transformAndUpdateUIMembersToInstances (Long modelId, List<UIMember> members) throws KDXException,
            SDXException {
      for (UIMember uiMember : members) {
         if (uiMember.isInstanceExist()) {
            Instance originalInstance = getKdxRetrievalService().getInstanceById(uiMember.getId());
            if (!StringUtils.isBlank(originalInstance.getDisplayName())
                     && !originalInstance.getDisplayName().equals(uiMember.getDescription())) {
               originalInstance.setDisplayName(uiMember.getDescription());
               getKdxManagementService().updateInstance(modelId, uiMember.getParentConceptId(), originalInstance);
               Membr originalMember = getSdxRetrievalService().getMemberById(uiMember.getOptionalMemberId());
               originalMember.setLookupDescription(uiMember.getDescription());
               getSdxManagementService().updateMember(originalMember);
            }
         }
      }
   }

   private List<UIPublishedFileEvaluatedColumnDetail> transformUIPublishedFileEvaluatedColumnDetail (
            List<PublishedFileTableDetails> publishedFileTableDetails) throws PublishedFileException {
      List<UIPublishedFileEvaluatedColumnDetail> uiPublishedFileEvaluatedColumnDetails = new ArrayList<UIPublishedFileEvaluatedColumnDetail>();
      for (PublishedFileTableDetails publishedFileTableDetail : publishedFileTableDetails) {
         UIPublishedFileEvaluatedColumnDetail uiPublishedFileEvaluatedColumnDetail = new UIPublishedFileEvaluatedColumnDetail();
         uiPublishedFileEvaluatedColumnDetail.setColumnName(publishedFileTableDetail.getEvaluatedColumnDisplayName());

         UIPublishedFileColumnInfo uiPublishedFileColumnInfo = new UIPublishedFileColumnInfo();
         uiPublishedFileColumnInfo.setConversionType(publishedFileTableDetail.getUnitType());
         uiPublishedFileColumnInfo.setQiConversion(prepareQIConversion(publishedFileTableDetail.getUnitType()));
         uiPublishedFileColumnInfo.setPublishedFileTableDetailId(publishedFileTableDetail.getId());
         uiPublishedFileEvaluatedColumnDetail.setColumnDetail(uiPublishedFileColumnInfo);

         uiPublishedFileEvaluatedColumnDetail.setFormat(publishedFileTableDetail.getFormat());
         uiPublishedFileEvaluatedColumnDetail.setUnit(publishedFileTableDetail.getUnit());

         uiPublishedFileEvaluatedColumnDetail.setKdxDataType(publishedFileTableDetail.getKdxDataType());
         uiPublishedFileEvaluatedColumnDetail.setGranularity(publishedFileTableDetail.getGranularity());
         uiPublishedFileEvaluatedColumnDetails.add(uiPublishedFileEvaluatedColumnDetail);
      }
      return uiPublishedFileEvaluatedColumnDetails;

   }

   private List<UIPublishedFileEvaluatedColumnDetail> transformUIAssetTableColumnDetail (Long assetId, Long tableId,
            List<Colum> tableColumns) throws ExeCueException {
      List<UIPublishedFileEvaluatedColumnDetail> uiPublishedFileEvaluatedColumnDetails = new ArrayList<UIPublishedFileEvaluatedColumnDetail>();
      for (Colum column : tableColumns) {
         String columConceptName = column.getDisplayName();
         if (StringUtils.isBlank(columConceptName)) {
            columConceptName = column.getName().replaceAll("_", " ").trim();
         }
         boolean conceptExists = false;
         Long conceptId = null;
         // check if column is mapped
         Concept concept = getMappingRetrievalService().getMappedConceptForColumn(assetId, tableId, column.getId());
         if (concept != null) {
            columConceptName = concept.getDisplayName();
            conceptExists = true;
            conceptId = concept.getId();
         }
         UIPublishedFileEvaluatedColumnDetail uiPublishedFileEvaluatedColumnDetail = new UIPublishedFileEvaluatedColumnDetail();
         uiPublishedFileEvaluatedColumnDetail.setColumnName(columConceptName);
         UIPublishedFileColumnInfo uiPublishedFileColumnInfo = new UIPublishedFileColumnInfo();
         uiPublishedFileColumnInfo.setConversionType(column.getConversionType());
         uiPublishedFileColumnInfo.setQiConversion(prepareQIConversion(column.getConversionType()));
         uiPublishedFileColumnInfo.setPublishedFileTableDetailId(column.getId());
         uiPublishedFileColumnInfo.setConceptExist(conceptExists);
         uiPublishedFileColumnInfo.setConceptId(conceptId);
         uiPublishedFileEvaluatedColumnDetail.setColumnDetail(uiPublishedFileColumnInfo);

         uiPublishedFileEvaluatedColumnDetail.setFormat(column.getDataFormat());
         uiPublishedFileEvaluatedColumnDetail.setUnit(column.getUnit());
         uiPublishedFileEvaluatedColumnDetail.setKdxDataType(column.getKdxDataType());
         uiPublishedFileEvaluatedColumnDetail.setGranularity(column.getGranularity());
         uiPublishedFileEvaluatedColumnDetails.add(uiPublishedFileEvaluatedColumnDetail);
      }
      return uiPublishedFileEvaluatedColumnDetails;

   }

   private UIMember transformUIMember (Membr member) throws ExeCueException {
      UIMember uiMember = new UIMember();
      uiMember.setId(member.getId());
      uiMember.setName(member.getLookupValue());
      uiMember.setDescription(member.getLookupDescription());
      uiMember.setInstanceExist(false);
      return uiMember;
   }

   private UIMember transformUIMember (BusinessEntityDefinition instanceBed, Membr membr) throws ExeCueException {
      UIMember uiMember = new UIMember();
      Instance instance = instanceBed.getInstance();
      Concept concept = instanceBed.getConcept();
      uiMember.setId(instance.getId());
      uiMember.setName(instance.getName());
      uiMember.setDescription(instance.getDisplayName());
      uiMember.setInstanceExist(true);
      uiMember.setParentConceptId(concept.getId());
      uiMember.setOptionalMemberId(membr.getId());
      return uiMember;
   }

   public List<List<String>> getUploadedFileDataFromSource (Long fileId, Long tableId, Page metaPageDetail,
            Page pageDetail) throws ExeCueException {
      LimitEntity limitEntity = new LimitEntity();
      int startingNumber = 1;
      int endingNumber = pageDetail.getPageSize().intValue();
      if (pageDetail.getRequestedPage().intValue() > 1) {
         startingNumber = (pageDetail.getRequestedPage().intValue() - 1) * endingNumber + 1;
         endingNumber = endingNumber * pageDetail.getRequestedPage().intValue();
      }

      limitEntity.setStartingNumber(new Long(startingNumber));
      limitEntity.setEndingNumber(new Long(endingNumber));

      List<String> tempTableBaseColumns = getPublishedFileRetrievalService()
               .getPublishedFileTempTableBaseColumnNamesByPage(tableId, metaPageDetail);
      return getSwiPlatformRetrievalService().getUploadedFileDataFromSource(fileId, tableId, tempTableBaseColumns,
               limitEntity, pageDetail);
   }

   public List<List<String>> getAssetTableDataFromSourceByPage (Long assetId, Long tableId, Page metaPageDetail,
            Page pageDetail) throws ExeCueException {
      LimitEntity limitEntity = new LimitEntity();
      int startingNumber = 1;
      int endingNumber = pageDetail.getPageSize().intValue();
      if (pageDetail.getRequestedPage().intValue() > 1) {
         startingNumber = (pageDetail.getRequestedPage().intValue() - 1) * endingNumber + 1;
         endingNumber = endingNumber * pageDetail.getRequestedPage().intValue();
      }

      limitEntity.setStartingNumber(new Long(startingNumber));
      limitEntity.setEndingNumber(new Long(endingNumber));

      List<String> requestedColumnNames = getSdxRetrievalService().getAssetTableColumnNamesByPage(assetId, tableId,
               metaPageDetail);

      return getSwiPlatformRetrievalService().getAssetTableDataFromSourceByPage(assetId, tableId, requestedColumnNames,
               limitEntity, pageDetail);
   }

   private QIConversion prepareQIConversion (ConversionType conversionType) throws PublishedFileException {
      try {
         QIConversion qiConversion = null;
         if (conversionType == null) {
            conversionType = ConversionType.DEFAULT;
         }
         qiConversion = conversionServiceHandler.getQIConversion(conversionType.getValue());
         if (qiConversion != null) {
            if (ExecueCoreUtil.isCollectionEmpty(qiConversion.getDataFormats())) {
               qiConversion.setDataFormats(new ArrayList<SuggestionUnit>());
            }
            if (ExecueCoreUtil.isCollectionEmpty(qiConversion.getUnits())) {
               qiConversion.setUnits(new ArrayList<SuggestionUnit>());
            }
         } else {
            qiConversion = new QIConversion();
            qiConversion.setUnits(new ArrayList<SuggestionUnit>());
            qiConversion.setDataFormats(new ArrayList<SuggestionUnit>());
         }
         return qiConversion;
      } catch (SWIException e) {
         throw new PublishedFileException(e.getCode(), e);
      }
   }

   public List<UITable> getAssetLookupTables (Long assetId) throws ExeCueException {
      List<Tabl> assetLookupTables = getSdxRetrievalService().getLookupTables(assetId);
      TableTransformer tableTransformer = new TableTransformer();
      List<UITable> uiTables = new ArrayList<UITable>();
      UITable uiTable = null;
      for (Tabl table : assetLookupTables) {
         uiTable = new UITable();
         tableTransformer.transformToUIObject(table, uiTable);
         uiTables.add(uiTable);
      }
      return uiTables;
   }

   public Asset getApplicationAsset (Long applicationId) throws ExeCueException {
      List<Asset> assets = getSdxRetrievalService().getAssetsByApplicationId(applicationId);
      Asset asset = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(assets)) {
         asset = assets.get(0);
      }
      return asset;
   }

   public PublishedFileInfo getApplicationPublishedFileInfo (Long applicationId) throws ExeCueException {
      List<PublishedFileInfo> fileInfos = getPublishedFileRetrievalService().getPublishedFileInfoByApplicationId(
               applicationId);
      PublishedFileInfo fileInfo = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(fileInfos)) {
         fileInfo = fileInfos.get(0);
      }
      return fileInfo;
   }

   private List<String> validateUIColumnNamesForUniqueness (List<String> columnNames, List<String> errorMessages) {
      Set<String> namesSet = new HashSet<String>(columnNames);
      if (namesSet.size() == columnNames.size()) {
         return errorMessages;
      }
      int sourceCounter = 0;
      List<String> nonUniqueColumnNames = new ArrayList<String>();
      for (String sourceColumnName : columnNames) {
         int destinationCounter = 0;
         for (String destinationColumnName : columnNames) {
            if (sourceCounter != destinationCounter && sourceColumnName.equalsIgnoreCase(destinationColumnName)) {
               if (!nonUniqueColumnNames.contains(sourceColumnName)) {
                  errorMessages.add("Field Name [" + sourceColumnName + "] is not unique");
                  nonUniqueColumnNames.add(sourceColumnName);
               }
            }
            destinationCounter = destinationCounter + 1;
         }
         sourceCounter = sourceCounter + 1;
      }
      return errorMessages;
   }

   private List<String> getColumnNames (List<UIPublishedFileEvaluatedColumnDetail> uiColumns) {
      List<String> columnNames = new ArrayList<String>();
      for (UIPublishedFileEvaluatedColumnDetail uiColumn : uiColumns) {
         columnNames.add(uiColumn.getColumnName());
      }
      return columnNames;
   }

   public PublishedFileInfo getPublishedFileInfoByFileId (Long fileId) throws ExeCueException {
      return getPublishedFileRetrievalService().getPublishedFileInfoById(fileId);

   }

   public IConversionServiceHandler getConversionServiceHandler () {
      return conversionServiceHandler;
   }

   public void setConversionServiceHandler (IConversionServiceHandler conversionServiceHandler) {
      this.conversionServiceHandler = conversionServiceHandler;
   }

   public IPublisherDataEvaluationService getPublisherDataEvaluationService () {
      return publisherDataEvaluationService;
   }

   public void setPublisherDataEvaluationService (IPublisherDataEvaluationService publisherDataEvaluationService) {
      this.publisherDataEvaluationService = publisherDataEvaluationService;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IKDXManagementService getKdxManagementService () {
      return kdxManagementService;
   }

   public void setKdxManagementService (IKDXManagementService kdxManagementService) {
      this.kdxManagementService = kdxManagementService;
   }

   public IVirtualTableManagementService getVirtualTableManagementService () {
      return virtualTableManagementService;
   }

   public void setVirtualTableManagementService (IVirtualTableManagementService virtualTableManagementService) {
      this.virtualTableManagementService = virtualTableManagementService;
   }

   public IConversionService getConversionService () {
      return conversionService;
   }

   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }

   public IKDXMaintenanceService getKdxMaintenanceService () {
      return kdxMaintenanceService;
   }

   public void setKdxMaintenanceService (IKDXMaintenanceService kdxMaintenanceService) {
      this.kdxMaintenanceService = kdxMaintenanceService;
   }

   public IPublisherColumnValidator getPublisherColumnValidator () {
      return publisherColumnValidator;
   }

   public void setPublisherColumnValidator (IPublisherColumnValidator publisherColumnValidator) {
      this.publisherColumnValidator = publisherColumnValidator;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISDXManagementService getSdxManagementService () {
      return sdxManagementService;
   }

   public void setSdxManagementService (ISDXManagementService sdxManagementService) {
      this.sdxManagementService = sdxManagementService;
   }

   public IMappingRetrievalService getMappingRetrievalService () {
      return mappingRetrievalService;
   }

   public void setMappingRetrievalService (IMappingRetrievalService mappingRetrievalService) {
      this.mappingRetrievalService = mappingRetrievalService;
   }

   public IMappingManagementService getMappingManagementService () {
      return mappingManagementService;
   }

   public void setMappingManagementService (IMappingManagementService mappingManagementService) {
      this.mappingManagementService = mappingManagementService;
   }

   public IPublishedFileRetrievalService getPublishedFileRetrievalService () {
      return publishedFileRetrievalService;
   }

   public void setPublishedFileRetrievalService (IPublishedFileRetrievalService publishedFileRetrievalService) {
      this.publishedFileRetrievalService = publishedFileRetrievalService;
   }

   public IPublishedFileManagementService getPublishedFileManagementService () {
      return publishedFileManagementService;
   }

   public void setPublishedFileManagementService (IPublishedFileManagementService publishedFileManagementService) {
      this.publishedFileManagementService = publishedFileManagementService;
   }

   public ISDXDeletionService getSdxDeletionService () {
      return sdxDeletionService;
   }

   public void setSdxDeletionService (ISDXDeletionService sdxDeletionService) {
      this.sdxDeletionService = sdxDeletionService;
   }

   public ISWIPlatformRetrievalService getSwiPlatformRetrievalService () {
      return swiPlatformRetrievalService;
   }

   public void setSwiPlatformRetrievalService (ISWIPlatformRetrievalService swiPlatformRetrievalService) {
      this.swiPlatformRetrievalService = swiPlatformRetrievalService;
   }

   public ISDX2KDXMappingService getSdx2kdxMappingService () {
      return sdx2kdxMappingService;
   }

   public void setSdx2kdxMappingService (ISDX2KDXMappingService sdx2kdxMappingService) {
      this.sdx2kdxMappingService = sdx2kdxMappingService;
   }

   
   public ISDXDeletionWrapperService getSdxDeletionWrapperService () {
      return sdxDeletionWrapperService;
   }

   
   public void setSdxDeletionWrapperService (ISDXDeletionWrapperService sdxDeletionWrapperService) {
      this.sdxDeletionWrapperService = sdxDeletionWrapperService;
   }

}
