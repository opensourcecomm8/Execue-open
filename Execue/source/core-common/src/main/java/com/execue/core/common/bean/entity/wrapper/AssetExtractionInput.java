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


/**
 * 
 */
package com.execue.core.common.bean.entity.wrapper;

import java.util.List;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Tabl;

/**
 * @author Jayadev
 */
public class AssetExtractionInput {

   private boolean remainingTablesAreFacts;
   private Asset   sourceAsset;
   /*
    * Lookup tables are always mentioned If remainingTablesAreFacts = true, no need to mention anything else, as all the
    * other tables will be absorbed as facts; If remainingTablesAreFacts = false, the exact set of lookup and fact
    * tables are supplied in the list
    */
   List<Tabl>      tables;
   
   public boolean isRemainingTablesAreFacts () {
      return remainingTablesAreFacts;
   }
   
   public void setRemainingTablesAreFacts (boolean remainingTablesAreFacts) {
      this.remainingTablesAreFacts = remainingTablesAreFacts;
   }
   
   public Asset getSourceAsset () {
      return sourceAsset;
   }
   
   public void setSourceAsset (Asset sourceAsset) {
      this.sourceAsset = sourceAsset;
   }
   
   public List<Tabl> getTables () {
      return tables;
   }
   
   public void setTables (List<Tabl> tables) {
      this.tables = tables;
   }

}
