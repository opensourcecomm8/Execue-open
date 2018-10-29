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


package com.execue.content.postprocessor.bean;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;

/**
 * @author Nitesh
 */
public class PostProcessorOutput {

   private SemanticPossibility semanticPossibility;
   private LocationPointInfo   locationPointInfo;

   /**
    * @return the semanticPossibility
    */
   public SemanticPossibility getSemanticPossibility () {
      return semanticPossibility;
   }

   /**
    * @param semanticPossibility the semanticPossibility to set
    */
   public void setSemanticPossibility (SemanticPossibility semanticPossibility) {
      this.semanticPossibility = semanticPossibility;
   }

   /**
    * @return the locationPointInfo
    */
   public LocationPointInfo getLocationPointInfo () {
      return locationPointInfo;
   }

   /**
    * @param locationPointInfo the locationPointInfo to set
    */
   public void setLocationPointInfo (LocationPointInfo locationPointInfo) {
      this.locationPointInfo = locationPointInfo;
   }

}
