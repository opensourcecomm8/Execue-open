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


package com.execue.platform.swi.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConstraintType;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.platform.exception.SourceMetaDataException;
import com.execue.platform.swi.IConstraintPopulationService;
import com.execue.platform.swi.ISourceMetaDataService;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXManagementService;
import com.execue.swi.service.ISDXRetrievalService;

public class ConstraintPopulationServiceImpl implements IConstraintPopulationService {

   private ISDXRetrievalService   sdxRetrievalService;
   private ISDXManagementService  sdxManagementService;
   private ISourceMetaDataService sourceMetaDataService;
   private static final Logger    logger = Logger.getLogger(ConstraintPopulationServiceImpl.class);

   public void createConstraintsByRelations (Long assetId) throws SDXException {
      logger.debug("Inside createConstraints method");
      Asset asset = getSdxRetrievalService().getAsset(assetId);
      List<TableInfo> tablesInfo = getSdxRetrievalService().getAssetTables(asset);
      for (TableInfo tableInfo : tablesInfo) {
         Tabl table = tableInfo.getTable();
         List<Colum> columns = tableInfo.getColumns();
         Map<String, String> foreignKeyColums = new HashMap<String, String>();
         List<String> primaryKeyColums = new ArrayList<String>();
         try {
            logger.debug("Get the Foreign keys for the table " + table.getName());
            foreignKeyColums = getSourceMetaDataService().getForeignKeysFromSource(asset, table);
            logger.debug("Get the Primary keys for the table " + table.getName());
            primaryKeyColums = getSourceMetaDataService().getPrimaryKeysFromSource(asset, table);
         } catch (SourceMetaDataException e) {
            logger.error("Error in getting the primary/foreign keys from source");
            throw new SDXException(DataAccessExceptionCodes.META_DATA_EXTRACTION_FAILED, e);
         }
         int primaryKeyColumOrder = 0;
         int foreignKeyColumOrder = 0;
         for (Colum colum : columns) {
            Set<Constraint> constraints = new HashSet<Constraint>();
            // check if column is primary key
            if (primaryKeyColums.contains(colum.getName())) {
               colum.setRequired(CheckType.YES);
               Constraint constraint = new Constraint();
               constraint.setName(table.getName() + "_" + colum.getName() + "_" + primaryKeyColumOrder);
               constraint.setType(ConstraintType.PRIMARY_KEY);
               constraint.setDescription(table.getName() + "_" + colum.getName() + "_" + primaryKeyColumOrder);
               constraint.setColumOrder(primaryKeyColumOrder++);
               Set<Colum> constraintColums = new HashSet<Colum>();
               constraintColums.add(colum);
               constraint.setConstraintColums(constraintColums);
               constraints.add(constraint);
            }
            // check if column is foreign key
            if (foreignKeyColums.containsKey(colum.getName())) {
               // colum.setIsConstraintColum(CheckType.YES);
               String parentTableName = null;
               String parentTableColum = null;
               for (String columName : foreignKeyColums.keySet()) {
                  if (columName.equalsIgnoreCase(colum.getName())) {
                     StringTokenizer stringTokenizer = new StringTokenizer(foreignKeyColums.get(columName), ".");
                     parentTableName = stringTokenizer.nextToken();
                     parentTableColum = stringTokenizer.nextToken();
                  }
               }
               Tabl referenceTable = null;
               Colum referenceColumn = null;

               referenceTable = getSdxRetrievalService().getAssetTable(asset.getId(), parentTableName);
               referenceColumn = getSdxRetrievalService().getAssetTableColum(asset.getId(), parentTableName,
                        parentTableColum);

               Constraint constraint = new Constraint();
               constraint.setName(table.getName() + "_" + colum.getName() + "_" + foreignKeyColumOrder);
               constraint.setType(ConstraintType.FOREIGN_KEY);
               constraint.setDescription(table.getName() + "_" + colum.getName() + "_" + foreignKeyColumOrder);
               constraint.setColumOrder(foreignKeyColumOrder++);
               constraint.setReferenceTable(referenceTable);
               constraint.setReferenceColumn(referenceColumn);
               Set<Colum> constraintColums = new HashSet<Colum>();
               constraintColums.add(colum);
               constraint.setConstraintColums(constraintColums);
               constraints.add(constraint);
            }
            CheckType checkType = colum.getRequired();
            colum = getSdxRetrievalService().getAssetTableColum(asset.getId(), table.getName(), colum.getName());
            colum.setRequired(checkType);
            if (constraints.size() > 0) {
               logger.debug("Creating Constraints for colum " + colum.getName());
               getSdxManagementService().createConstraints(new ArrayList<Constraint>(constraints));
            }
         }
      }
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

   public ISourceMetaDataService getSourceMetaDataService () {
      return sourceMetaDataService;
   }

   public void setSourceMetaDataService (ISourceMetaDataService sourceMetaDataService) {
      this.sourceMetaDataService = sourceMetaDataService;
   }
}
