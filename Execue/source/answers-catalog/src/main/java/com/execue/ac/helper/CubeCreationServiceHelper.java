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


package com.execue.ac.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.execue.ac.bean.CubeConfigurationContext;
import com.execue.ac.bean.CubeCreationOutputInfo;
import com.execue.ac.exception.AnswersCatalogException;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetCreationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetOwnerType;
import com.execue.core.common.type.AssetSubType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.SelectEntityType;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.core.util.CombinationGenerator;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.swi.exception.SDXException;

/**
 * This class contains helper methods specific to cube process
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeCreationServiceHelper extends AnswersCatalogServiceHelper {

   /**
    * This method generates the all c(n,r) combinations for dimension select entities.
    * 
    * @param cubeDimensionSelectEntities
    * @param cubeConfigurationContext
    * @return
    */
   public List<List<SelectEntity>> populateDimensionSelectEntityCombinations (
            List<SelectEntity> cubeDimensionSelectEntities, CubeConfigurationContext cubeConfigurationContext) {
      DataType dimensionDataType = cubeConfigurationContext.getDimensionDataType();
      String cubeAllValueRepresentation = cubeConfigurationContext.getCubeAllValueRepresentation();
      List<List<SelectEntity>> combinations = new ArrayList<List<SelectEntity>>();
      List<SelectEntity> originalCombination = new ArrayList<SelectEntity>(cubeDimensionSelectEntities);
      List<SelectEntity> tempCombination = new ArrayList<SelectEntity>(originalCombination);
      // iterate over n-1 times where n is number of dimensions.
      for (int num = 1; num < originalCombination.size(); num++) {
         // pass all the dimensions list and number n for which u want to generate combinations
         CombinationGenerator combinationGenerator = new CombinationGenerator(originalCombination.size(), num);
         // if there are more combinations
         while (combinationGenerator.hasMore()) {
            // get the combination indexes
            int[] indices = combinationGenerator.getNext();
            // for each index, replace the SelectEntity with -1 id
            for (int i = 0; i < indices.length; i++) {
               SelectEntity originalSelectEntity = tempCombination.get(indices[i]);
               tempCombination.set(indices[i], prepareValueBasedSelectEntity(originalSelectEntity, dimensionDataType,
                        cubeAllValueRepresentation));
            }
            // add this combination to existing combinations
            combinations.add(tempCombination);
            // initialize the combination back
            tempCombination = new ArrayList<SelectEntity>(originalCombination);
         }
      }
      combinations.add(cubeDimensionSelectEntities);
      tempCombination = new ArrayList<SelectEntity>(originalCombination);
      for (int i = 0; i < cubeDimensionSelectEntities.size(); i++) {
         SelectEntity originalSelectEntity = cubeDimensionSelectEntities.get(i);
         tempCombination.set(i, prepareValueBasedSelectEntity(originalSelectEntity, dimensionDataType,
                  cubeAllValueRepresentation));
      }
      combinations.add(tempCombination);
      return combinations;
   }

   /**
    * This method prepares the cube all value representation based select entity.
    * 
    * @param originalSelectEntity
    * @param dimensionDataType
    * @param cubeAllValueRepresentation
    * @return
    */
   private SelectEntity prepareValueBasedSelectEntity (SelectEntity originalSelectEntity, DataType dimensionDataType,
            String cubeAllValueRepresentation) {
      SelectEntity selectEntity = new SelectEntity();
      selectEntity.setType(SelectEntityType.VALUE);
      selectEntity.setAlias(originalSelectEntity.getAlias());
      selectEntity.setQueryValue(ExecueBeanManagementUtil.createQueryValue(dimensionDataType,
               cubeAllValueRepresentation));
      return selectEntity;
   }

   /**
    * This method adjusts the dimension column data type and precision too. As we have to insert "all" value to lookup
    * tables we need datatype to be string and size to be minimum of 3.
    * 
    * @param dimensionQueryColumn
    * @param dimensionDataType
    * @param minDimensionPrecision
    */
   public void adjustDimensionColumnPrecision (QueryColumn dimensionQueryColumn, DataType dimensionDataType,
            int minDimensionPrecision) {
      dimensionQueryColumn.setDataType(dimensionDataType);
      // adjust the precision
      int originalPrecision = dimensionQueryColumn.getPrecision();
      if (dimensionQueryColumn.getScale() > 0) {
         originalPrecision = originalPrecision + dimensionQueryColumn.getScale() + 1;
      }
      if (originalPrecision < minDimensionPrecision) {
         originalPrecision = minDimensionPrecision;
      }
      dimensionQueryColumn.setPrecision(originalPrecision);
      dimensionQueryColumn.setScale(0);
   }

   /**
    * This method updates the asset properties for cube from source asset
    * 
    * @param sourceAsset
    * @param targetAsset
    * @throws AnswersCatalogException
    */
   public void updateAssetProperties (Asset sourceAsset, Asset targetAsset, AssetCreationInfo assetCreationInfo)
            throws AnswersCatalogException {
      try {
         targetAsset.setBaseAssetId(sourceAsset.getId());
         targetAsset.setStatus(StatusEnum.INACTIVE);
         targetAsset.setType(AssetType.Cube);
         targetAsset.setSubType(AssetSubType.SuperSet);
         targetAsset.setOwnerType(AssetOwnerType.ExeCue);
         getSdxManagementService().updateAsset(targetAsset);
         copyAssetDetailProperties(sourceAsset, targetAsset, assetCreationInfo);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   public void updateCubeFactTableProperties (Tabl cubeFactTable) throws AnswersCatalogException {
      try {
         cubeFactTable.setEligibleDefaultMetric(CheckType.YES);
         getSdxManagementService().updateTable(cubeFactTable);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   public void updateCubeColumnKDXType (Long assetId, Long tableId, Colum cubeFrequencyColumn, ColumnType columnType)
            throws AnswersCatalogException {
      try {
         cubeFrequencyColumn.setKdxDataType(columnType);
         getSdxManagementService().updateColumn(assetId, tableId, cubeFrequencyColumn);
      } catch (SDXException e) {
         throw new AnswersCatalogException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
   }

   public AssetCreationInfo buildAssetCreationInfo (CubeCreationOutputInfo cubeCreationOutputInfo) {
      AssetCreationInfo assetCreationInfo = new AssetCreationInfo();

      List<Long> dimensionBEDIDs = cubeCreationOutputInfo.getCubeCreationInputInfo().getCubeCreationContext()
               .getSimpleLookupDimensionBEDIDs();
      if (ExecueCoreUtil.isCollectionNotEmpty(dimensionBEDIDs)) {

      } else {
         dimensionBEDIDs = new ArrayList<Long>();
      }
      Collection<Long> tempDimensionBEDIDs = cubeCreationOutputInfo.getCubeCreationInputInfo().getCubeCreationContext()
               .getRangeLookupDimensionBEDIDs().keySet();
      if (ExecueCoreUtil.isCollectionNotEmpty(tempDimensionBEDIDs)) {
         dimensionBEDIDs.addAll(tempDimensionBEDIDs);
      }
      assetCreationInfo.setDimensionBEDIDs(dimensionBEDIDs);

      assetCreationInfo.setPopulationBEDIDs(getBEDIDsFromConceptColumnMappings(cubeCreationOutputInfo
               .getCubeCreationInputInfo().getCubeCreationPopulatedContext().getPopulatedFrequencyMeasures()));

      assetCreationInfo.setStats(cubeCreationOutputInfo.getCubeCreationInputInfo().getCubeConfigurationContext()
               .getApplicableStats());

      return assetCreationInfo;
   }
}
