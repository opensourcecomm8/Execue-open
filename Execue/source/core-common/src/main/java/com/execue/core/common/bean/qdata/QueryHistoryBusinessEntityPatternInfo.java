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
package com.execue.core.common.bean.qdata;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Nitesh
 *
 */
public class QueryHistoryBusinessEntityPatternInfo {

   private List<QueryHistoryBusinessEntityInfo> dimensions;
   private List<QueryHistoryBusinessEntityInfo> measures;

   /**
    * @return the dimensions
    */
   public List<QueryHistoryBusinessEntityInfo> getDimensions () {
      if (dimensions == null) {
         dimensions = new ArrayList<QueryHistoryBusinessEntityInfo>();
      }
      return dimensions;
   }

   /**
    * @param dimensions the dimensions to set
    */
   public void setDimensions (List<QueryHistoryBusinessEntityInfo> dimensions) {
      this.dimensions = dimensions;
   }

   /**
    * @return the measures
    */
   public List<QueryHistoryBusinessEntityInfo> getMeasures () {
      if (measures == null) {
         measures = new ArrayList<QueryHistoryBusinessEntityInfo>();
      }
      return measures;
   }

   /**
    * @param measures the measures to set
    */
   public void setMeasures (List<QueryHistoryBusinessEntityInfo> measures) {
      this.measures = measures;
   }
}
