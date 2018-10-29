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


package com.execue.ac.algorithm.optimaldset.service.impl;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
import java.util.List;
import java.util.Map;

import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetDimensionPattern;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetSpaceCalculationInputInfo;
import com.execue.ac.algorithm.optimaldset.bean.OptimalDSetStaticLevelInputInfo;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetSpaceCalculationService;
import com.execue.ac.algorithm.optimaldset.util.OptimalDSetUtil;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.QueryTable;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SDXException;
import com.execue.swi.service.ISDXRetrievalService;

public class OptimalDSetSpaceCalculationServiceImpl implements IOptimalDSetSpaceCalculationService {

   private ISDXRetrievalService     sdxRetrievalService;
   private ISystemDataAccessService systemDataAccessService;

   /**
    * This method computes size based on dimension pattern. For each of the dimension need to have a lookup table and
    * participation in fact table. Compute row size of fact table first. Record count is dim1 members * dim2 members
    * *....n
    */
   @Override
   public Double computeProjectedSpaceForCube (OptimalDSetDimensionPattern dimensionPattern,
            OptimalDSetSpaceCalculationInputInfo spaceCalculationInputInfo) throws AnswersCatalogException {
      Double totalSpace = 0d;
      List<OptimalDSetDimensionInfo> dimensions = dimensionPattern.getDimensions();
      Map<Long, Double> dimensionLookupTableTotalSize = spaceCalculationInputInfo.getDimensionLookupTableTotalSize();
      Map<Long, Double> dimensionFactColumnSizePerCell = spaceCalculationInputInfo.getDimensionFactColumnSizePerCell();
      // calculate lookup table size
      for (OptimalDSetDimensionInfo dimensionInfo : dimensions) {
         totalSpace += dimensionLookupTableTotalSize.get(dimensionInfo.getBedId());
      }
      // calculate fact table size
      Double factTableDimensionsSizePerRow = 0d;
      Double recordCount = 1d;
      // extra one member because out cubes are superset to accomodate 'all' record
      for (OptimalDSetDimensionInfo dimensionInfo : dimensions) {
         factTableDimensionsSizePerRow += dimensionFactColumnSizePerCell.get(dimensionInfo.getBedId());
         recordCount *= dimensionInfo.getNumMembers() + 1;
      }
      // stat table.
      recordCount *= spaceCalculationInputInfo.getStatRecordCount();

      // size per row of fact table.
      Double factTableSizePerRow = spaceCalculationInputInfo.getPopulationColumnSizePerCell()
               + factTableDimensionsSizePerRow + spaceCalculationInputInfo.getStatLookupTableTotalSize()
               + spaceCalculationInputInfo.getAllMeasureColumnsSizePerRow();
      Double factTableTotalSize = recordCount * factTableSizePerRow;
      totalSpace += factTableTotalSize;
      return totalSpace;
   }

   /**
    * This method calculates the space for parent asset. It gets all the tables, lookup and fact. for fact it makes
    * count(*) query to get no. or rows and for lookups information comes from swi by way of number of members. Using
    * column data types we calculate size to be taken by each row and then multiply by no of records.
    */
   @Override
   public Double computeSpaceForParentAsset (Long parentAssetId, OptimalDSetStaticLevelInputInfo staticLevelInputInfo)
            throws AnswersCatalogException {
      Double totalSpace = 0d;
      try {
         // If parent asset space should not be calculated based on configuration,
         //    then get parent asset space from configured value
         if (!staticLevelInputInfo.isDeduceSpaceAtRuntime()) {
            totalSpace = staticLevelInputInfo.getConfiguredParentAssetSpace();
            return totalSpace;
         }

         Asset asset = getSdxRetrievalService().getAssetById(parentAssetId);
         DataSource dataSource = getSdxRetrievalService().getDataSourceByAssetId(parentAssetId);
         List<Tabl> tables = getSdxRetrievalService().getAllTables(asset);
         for (Tabl table : tables) {
            List<Colum> columns = getSdxRetrievalService().getColumnsOfTable(table);
            Double tableSpacePerRow = 0d;
            for (Colum column : columns) {
               tableSpacePerRow += OptimalDSetUtil.calculateLengthOfColumnForParentAsset(column);
            }
            tableSpacePerRow = OptimalDSetUtil.convertToGigaBytes(tableSpacePerRow);
            Long tableRecordCount = 0l;
            // fact table, count of any column
            if (LookupType.None.equals(table.getLookupType())) {
               QueryTable queryTable = ExecueBeanManagementUtil.prepareQueryTable(table);
               QueryColumn queryColumn = ExecueBeanManagementUtil.prepareQueryColumn(columns.get(0));
               tableRecordCount = getSystemDataAccessService().getStatBasedColumnValue(dataSource, queryTable,
                        queryColumn, StatType.COUNT);
            } else
            // get the members count from swi.
            {
               tableRecordCount = getSdxRetrievalService().getTotalMembersCountOfColumn(
                        ExecueBeanUtil.findCorrespondingColumn(columns, table.getLookupValueColumn()));
            }
            totalSpace += tableSpacePerRow * tableRecordCount;
         }
      } catch (SDXException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      } catch (DataAccessException e) {
         throw new AnswersCatalogException(e.getCode(), e);
      }
      return totalSpace;
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   public ISystemDataAccessService getSystemDataAccessService () {
      return systemDataAccessService;
   }

   public void setSystemDataAccessService (ISystemDataAccessService systemDataAccessService) {
      this.systemDataAccessService = systemDataAccessService;
   }

}
