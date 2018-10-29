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
package com.execue.core.common.bean.aggregation;

/**
 * @author Nitesh
 *
 */
public class QueryDataHeaderHierarchyColumnMeta {

   private String referenceColumnId;
   private int    level;

   /**
    * @return the referenceColumnId
    */
   public String getReferenceColumnId () {
      return referenceColumnId;
   }

   /**
    * @param referenceColumnId the referenceColumnId to set
    */
   public void setReferenceColumnId (String referenceColumnId) {
      this.referenceColumnId = referenceColumnId;
   }

   /**
    * @return the level
    */
   public int getLevel () {
      return level;
   }

   /**
    * @param level the level to set
    */
   public void setLevel (int level) {
      this.level = level;
   }
}