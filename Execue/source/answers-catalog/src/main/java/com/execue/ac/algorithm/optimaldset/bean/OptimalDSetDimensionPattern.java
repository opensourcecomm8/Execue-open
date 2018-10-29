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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 * @version 1.0
 * @since 18/02/2012
 */
public class OptimalDSetDimensionPattern implements Serializable {

   private List<OptimalDSetDimensionInfo> dimensions;

   public OptimalDSetDimensionPattern () {
      super();
   }

   public OptimalDSetDimensionPattern (List<OptimalDSetDimensionInfo> dimensions) {
      super();
      this.dimensions = dimensions;
   }

   @Override
   public String toString () {
      StringBuilder toStringRepresenation = new StringBuilder();
      List<String> dimensionsAsString = new ArrayList<String>();
      for (OptimalDSetDimensionInfo dimensionInfo : dimensions) {
         dimensionsAsString.add(dimensionInfo.toString());
      }
      Collections.sort(dimensionsAsString);
      toStringRepresenation.append(ExecueCoreUtil.joinCollection(dimensionsAsString));
      return toStringRepresenation.toString();
   }

   @Override
   public boolean equals (Object obj) {
      return ((OptimalDSetDimensionPattern) obj).toString().equalsIgnoreCase(toString());
   }

   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public List<OptimalDSetDimensionInfo> getDimensions () {
      return dimensions;
   }

   public void setDimensions (List<OptimalDSetDimensionInfo> dimensions) {
      this.dimensions = dimensions;
   }
}
