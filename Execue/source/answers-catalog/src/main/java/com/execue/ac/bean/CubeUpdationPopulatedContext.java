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

import java.util.List;

import com.execue.core.common.bean.querygen.QueryTable;

/**
 * This bean contains the populated context information for cube
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
@SuppressWarnings ("serial")
public class CubeUpdationPopulatedContext extends CubeUpdationContext {

   private CubeCreationPopulatedContext existingCubeCreationPopulatedContext;
   // this is needed when we want to operate on lookup tables on existing cube in catalog
   private List<ConceptColumnMapping>   updatedDimensionsExistingCubeLookupTableMappings;
   // when we want to delete deleted members from fact table in catalog. so, we need dimension mapping to fact table.
   private List<ConceptColumnMapping>   updatedDimensionsExistingCubeFactTableMappings;

   private QueryTable                   existingCubeFactTable;

   public CubeCreationPopulatedContext getExistingCubeCreationPopulatedContext () {
      return existingCubeCreationPopulatedContext;
   }

   public void setExistingCubeCreationPopulatedContext (
            CubeCreationPopulatedContext existingCubeCreationPopulatedContext) {
      this.existingCubeCreationPopulatedContext = existingCubeCreationPopulatedContext;
   }

   public List<ConceptColumnMapping> getUpdatedDimensionsExistingCubeLookupTableMappings () {
      return updatedDimensionsExistingCubeLookupTableMappings;
   }

   public void setUpdatedDimensionsExistingCubeLookupTableMappings (
            List<ConceptColumnMapping> updatedDimensionsExistingCubeLookupTableMappings) {
      this.updatedDimensionsExistingCubeLookupTableMappings = updatedDimensionsExistingCubeLookupTableMappings;
   }

   public List<ConceptColumnMapping> getUpdatedDimensionsExistingCubeFactTableMappings () {
      return updatedDimensionsExistingCubeFactTableMappings;
   }

   public void setUpdatedDimensionsExistingCubeFactTableMappings (
            List<ConceptColumnMapping> updatedDimensionsExistingCubeFactTableMappings) {
      this.updatedDimensionsExistingCubeFactTableMappings = updatedDimensionsExistingCubeFactTableMappings;
   }

   public QueryTable getExistingCubeFactTable () {
      // take any fact table mapping for current cube(existing cube) and use the query table.
      ConceptColumnMapping conceptColumnMapping = getUpdatedDimensionsExistingCubeFactTableMappings().get(0);
      this.existingCubeFactTable = conceptColumnMapping.getQueryTable();
      return existingCubeFactTable;
   }

   public void setExistingCubeFactTable (QueryTable existingCubeFactTable) {
      this.existingCubeFactTable = existingCubeFactTable;
   }

}
