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


package com.execue.ac.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * This bean contains the information from each step of mart creation so that other steps can use it.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartCreationOutputInfo {

   private MartCreationInputInfo                         martCreationInputInfo;
   private MartPopulationTableStructure                  populationTable;
   private List<MartFractionalDataSetTempTableStructure> fractionalDataSetTempTables;
   private List<MartFractionalDatasetTableStructure>     fractionalDataSetTables;
   private MartFractionalPopulationTableStructure        fractionalPopulationTable;
   private List<MartWarehouseTableStructure>             warehouseTableStructure;
   private boolean                                       creationSuccessful = false;

   public MartPopulationTableStructure getPopulationTable () {
      return populationTable;
   }

   public void addFractionalDataSetTempTable (
            MartFractionalDataSetTempTableStructure fractionalDataSetTempTableStructure) {
      if (ExecueCoreUtil.isCollectionEmpty(fractionalDataSetTempTables)) {
         fractionalDataSetTempTables = new ArrayList<MartFractionalDataSetTempTableStructure>();
      }
      fractionalDataSetTempTables.add(fractionalDataSetTempTableStructure);
   }

   public void addFractionalDataSetTable (MartFractionalDatasetTableStructure fractionalDatasetTableStructure) {
      if (ExecueCoreUtil.isCollectionEmpty(fractionalDataSetTables)) {
         fractionalDataSetTables = new ArrayList<MartFractionalDatasetTableStructure>();
      }
      fractionalDataSetTables.add(fractionalDatasetTableStructure);
   }

   public void setPopulationTable (MartPopulationTableStructure populationTable) {
      this.populationTable = populationTable;
   }

   public List<MartFractionalDataSetTempTableStructure> getFractionalDataSetTempTables () {
      return fractionalDataSetTempTables;
   }

   public void setFractionalDataSetTempTables (List<MartFractionalDataSetTempTableStructure> fractionalDataSetTempTables) {
      this.fractionalDataSetTempTables = fractionalDataSetTempTables;
   }

   public List<MartFractionalDatasetTableStructure> getFractionalDataSetTables () {
      return fractionalDataSetTables;
   }

   public void setFractionalDataSetTables (List<MartFractionalDatasetTableStructure> fractionalDataSetTables) {
      this.fractionalDataSetTables = fractionalDataSetTables;
   }

   public MartFractionalPopulationTableStructure getFractionalPopulationTable () {
      return fractionalPopulationTable;
   }

   public void setFractionalPopulationTable (MartFractionalPopulationTableStructure fractionalPopulationTable) {
      this.fractionalPopulationTable = fractionalPopulationTable;
   }

   public MartCreationInputInfo getMartCreationInputInfo () {
      return martCreationInputInfo;
   }

   public void setMartCreationInputInfo (MartCreationInputInfo martCreationInputInfo) {
      this.martCreationInputInfo = martCreationInputInfo;
   }

   public boolean isCreationSuccessful () {
      return creationSuccessful;
   }

   public void setCreationSuccessful (boolean creationSuccessful) {
      this.creationSuccessful = creationSuccessful;
   }

   public List<MartWarehouseTableStructure> getWarehouseTableStructure () {
      return warehouseTableStructure;
   }

   public void setWarehouseTableStructure (List<MartWarehouseTableStructure> warehouseTableStructure) {
      this.warehouseTableStructure = warehouseTableStructure;
   }

}
