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


package com.execue.core.common.bean.qdata;

import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the list of qdata dimensions and list of qdata dimension combinations information. 
 * 
 * @author Nitesh
 * @version 1.0
 * @since 11/05/11
 */
public class QDataDimensionInput {

   List<QDataDimensionInfo>            qDataDimensions;
   List<QDataDimensionCombinationInfo> qDataDimensionCombinations;

   /**
    * @return the qDataDimensions
    */
   public List<QDataDimensionInfo> getQDataDimensions () {
      if (qDataDimensions == null) {
         qDataDimensions = new ArrayList<QDataDimensionInfo>(1);
      }
      return qDataDimensions;
   }

   /**
    * @param dataDimensions the qDataDimensions to set
    */
   public void setQDataDimensions (List<QDataDimensionInfo> dataDimensions) {
      qDataDimensions = dataDimensions;
   }

   /**
    * @return the qDataDimensionCombinations
    */
   public List<QDataDimensionCombinationInfo> getQDataDimensionCombinations () {
      if (qDataDimensionCombinations == null) {
         qDataDimensionCombinations = new ArrayList<QDataDimensionCombinationInfo>(1);
      }
      return qDataDimensionCombinations;
   }

   /**
    * @param dataDimensionCombinations the qDataDimensionCombinations to set
    */
   public void setQDataDimensionCombinations (List<QDataDimensionCombinationInfo> dataDimensionCombinations) {
      qDataDimensionCombinations = dataDimensionCombinations;
   }
}
