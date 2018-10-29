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


package com.execue.core.common.bean.reports.prsntion;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class HierarchicalReportInfo implements Serializable {

   private String                                 title;
   private String                                 hierarchyColumnName;
   private List<String>                           hierarchicalReportColumnNames;
   private List<Map<String, Object>>              hierarchicalReportData;
   private List<HierarchicalReportColumnMetaData> hierarchicalReportColumnMetaInfo;

   public String getTitle () {
      return title;
   }

   public void setTitle (String title) {
      this.title = title;
   }

   public String getHierarchyColumnName () {
      return hierarchyColumnName;
   }

   public void setHierarchyColumnName (String hierarchyColumnName) {
      this.hierarchyColumnName = hierarchyColumnName;
   }

   public List<String> getHierarchicalReportColumnNames () {
      return hierarchicalReportColumnNames;
   }

   public void setHierarchicalReportColumnNames (List<String> hierarchicalReportColumnNames) {
      this.hierarchicalReportColumnNames = hierarchicalReportColumnNames;
   }

   public List<Map<String, Object>> getHierarchicalReportData () {
      return hierarchicalReportData;
   }

   public void setHierarchicalReportData (List<Map<String, Object>> hierarchicalReportData) {
      this.hierarchicalReportData = hierarchicalReportData;
   }

   public List<HierarchicalReportColumnMetaData> getHierarchicalReportColumnMetaInfo () {
      return hierarchicalReportColumnMetaInfo;
   }

   public void setHierarchicalReportColumnMetaInfo (
            List<HierarchicalReportColumnMetaData> hierarchicalReportColumnMetaInfo) {
      this.hierarchicalReportColumnMetaInfo = hierarchicalReportColumnMetaInfo;
   }

}
