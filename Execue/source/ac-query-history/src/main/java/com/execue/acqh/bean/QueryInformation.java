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


package com.execue.acqh.bean;

import java.util.Set;

public class QueryInformation {

   private Set<String> select;
   private Set<String> groupBy;
   private Set<String> where;
   private Long        usageCount;

   public Long getUsageCount () {
      return usageCount;
   }

   public void setUsageCount (Long usageCount) {
      this.usageCount = usageCount;
   }

   public Set<String> getWhere () {
      return where;
   }

   public void setWhere (Set<String> where) {
      this.where = where;
   }

   
   public Set<String> getSelect () {
      return select;
   }

   
   public void setSelect (Set<String> select) {
      this.select = select;
   }

   
   public Set<String> getGroupBy () {
      return groupBy;
   }

   
   public void setGroupBy (Set<String> groupBy) {
      this.groupBy = groupBy;
   }

}
