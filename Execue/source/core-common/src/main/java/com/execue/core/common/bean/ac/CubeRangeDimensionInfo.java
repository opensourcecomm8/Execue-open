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


package com.execue.core.common.bean.ac;

import com.execue.core.common.bean.entity.Range;

/**
 * @author Vishay
 */
public class CubeRangeDimensionInfo {

   private String dimensionName;
   private Range  range;

   public CubeRangeDimensionInfo (String dimensionName, Range range) {
      super();
      this.dimensionName = dimensionName;
      this.range = range;
   }

   public String getDimensionName () {
      return dimensionName;
   }

   public void setDimensionName (String dimensionName) {
      this.dimensionName = dimensionName;
   }

   public Range getRange () {
      return range;
   }

   public void setRange (Range range) {
      this.range = range;
   }
}
