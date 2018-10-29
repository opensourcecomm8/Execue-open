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


package com.execue.core.common.bean.qi;

/**
 * @author kaliki
 * @since 4.0
 */
public class QIConditionLHS {

   private QIBusinessTerm term;
   private String         stat;
   private String         statDisplayName;
   private boolean        hasInstances;
   private String         datatype;

   public QIBusinessTerm getTerm () {
      return term;
   }

   public void setTerm (QIBusinessTerm term) {
      this.term = term;
   }

   public String getStat () {
      return stat;
   }

   public void setStat (String stat) {
      this.stat = stat;
   }

   public boolean isHasInstances () {
      return hasInstances;
   }

   public void setHasInstances (boolean hasInstances) {
      this.hasInstances = hasInstances;
   }

   public String getDatatype () {
      return datatype;
   }

   public void setDatatype (String datatype) {
      this.datatype = datatype;
   }

   public String getStatDisplayName () {
      return statDisplayName;
   }

   public void setStatDisplayName (String statDisplayName) {
      this.statDisplayName = statDisplayName;
   }

}
