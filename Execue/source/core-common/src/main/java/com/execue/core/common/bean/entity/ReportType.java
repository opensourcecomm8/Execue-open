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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;

/**
 * @author kaliki
 * @since 4.0
 */
public class ReportType implements Serializable {

   private int              id;
   private String           name;
   private Set<ReportGroup> reportGroups;

   public ReportType () {
   }

   public ReportType (int id, String name) {
      this.id = id;
      this.name = name;
   }

   public int getId () {
      return this.id;
   }

   public void setId (int id) {
      this.id = id;
   }

   public String getName () {
      return this.name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public Set<ReportGroup> getReportGroups () {
      return this.reportGroups;
   }

   public void setReportGroups (Set<ReportGroup> reportGroups) {
      this.reportGroups = reportGroups;
   }

}
