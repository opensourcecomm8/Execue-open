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


package com.execue.ac.algorithm.optimaldset.bean;

import java.util.Map;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetSpaceCalculationInputInfo {

   // all size are in GB.
   private Map<Long, Double> dimensionFactColumnSizePerCell;
   private Map<Long, Double> dimensionLookupTableTotalSize;
   private Double            allMeasureColumnsSizePerRow;
   private Double            statFactColumnSizePerCell;
   private Double            statLookupTableTotalSize;
   private Integer           statRecordCount;
   private Double            populationColumnSizePerCell;

   public OptimalDSetSpaceCalculationInputInfo (Map<Long, Double> dimensionFactColumnSizePerCell,
            Map<Long, Double> dimensionLookupTableTotalSize, Double allMeasureColumnsSizePerRow,
            Double statFactColumnSizePerCell, Double statLookupTableTotalSize, Integer statRecordCount,
            Double populationColumnSizePerCell) {
      super();
      this.dimensionFactColumnSizePerCell = dimensionFactColumnSizePerCell;
      this.dimensionLookupTableTotalSize = dimensionLookupTableTotalSize;
      this.allMeasureColumnsSizePerRow = allMeasureColumnsSizePerRow;
      this.statFactColumnSizePerCell = statFactColumnSizePerCell;
      this.statLookupTableTotalSize = statLookupTableTotalSize;
      this.statRecordCount = statRecordCount;
      this.populationColumnSizePerCell = populationColumnSizePerCell;
   }

   public Map<Long, Double> getDimensionFactColumnSizePerCell () {
      return dimensionFactColumnSizePerCell;
   }

   public void setDimensionFactColumnSizePerCell (Map<Long, Double> dimensionFactColumnSizePerCell) {
      this.dimensionFactColumnSizePerCell = dimensionFactColumnSizePerCell;
   }

   public Map<Long, Double> getDimensionLookupTableTotalSize () {
      return dimensionLookupTableTotalSize;
   }

   public void setDimensionLookupTableTotalSize (Map<Long, Double> dimensionLookupTableTotalSize) {
      this.dimensionLookupTableTotalSize = dimensionLookupTableTotalSize;
   }

   public Double getAllMeasureColumnsSizePerRow () {
      return allMeasureColumnsSizePerRow;
   }

   public void setAllMeasureColumnsSizePerRow (Double allMeasureColumnsSizePerRow) {
      this.allMeasureColumnsSizePerRow = allMeasureColumnsSizePerRow;
   }

   public Double getStatFactColumnSizePerCell () {
      return statFactColumnSizePerCell;
   }

   public void setStatFactColumnSizePerCell (Double statFactColumnSizePerCell) {
      this.statFactColumnSizePerCell = statFactColumnSizePerCell;
   }

   public Double getStatLookupTableTotalSize () {
      return statLookupTableTotalSize;
   }

   public void setStatLookupTableTotalSize (Double statLookupTableTotalSize) {
      this.statLookupTableTotalSize = statLookupTableTotalSize;
   }

   public Double getPopulationColumnSizePerCell () {
      return populationColumnSizePerCell;
   }

   public void setPopulationColumnSizePerCell (Double populationColumnSizePerCell) {
      this.populationColumnSizePerCell = populationColumnSizePerCell;
   }

   public Integer getStatRecordCount () {
      return statRecordCount;
   }

   public void setStatRecordCount (Integer statRecordCount) {
      this.statRecordCount = statRecordCount;
   }

}
