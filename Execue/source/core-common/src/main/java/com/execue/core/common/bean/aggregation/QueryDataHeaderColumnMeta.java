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


package com.execue.core.common.bean.aggregation;

import java.util.List;

/**
 * This bean class contains the meta information of each column qualified by the id, column type, data type and the
 * description of the column
 * 
 * @author John Mallavalli
 */

public class QueryDataHeaderColumnMeta {

   private String                      id;
   private String                      description;
   private String                      ctype;
   private String                      plotAs;
   private String                      dtype;
   private String                      dataFormat;
   private String                      unitType;
   private String                      unit;
   private String                      precision;
   private String                      scale;
   // private String parent;
   private boolean                     fromCohort;
   private int                         memberCount;
   private List<QueryDataColumnMember> members;

   public String getCtype () {
      return ctype;
   }

   public void setCtype (String ctype) {
      this.ctype = ctype;
   }

   public String getPlotAs () {
      return plotAs;
   }

   public void setPlotAs (String plotAs) {
      this.plotAs = plotAs;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getDtype () {
      return dtype;
   }

   public void setDtype (String dtype) {
      this.dtype = dtype;
   }

   public String getId () {
      return id;
   }

   public void setId (String id) {
      this.id = id;
   }

   public int getMemberCount () {
      return memberCount;
   }

   public void setMemberCount (int memberCount) {
      this.memberCount = memberCount;
   }

   public List<QueryDataColumnMember> getMembers () {
      return members;
   }

   public void setMembers (List<QueryDataColumnMember> members) {
      this.members = members;
   }

   public String getDataFormat () {
      return dataFormat;
   }

   public void setDataFormat (String dataFormat) {
      this.dataFormat = dataFormat;
   }

   public String getUnitType () {
      return unitType;
   }

   public void setUnitType (String unitType) {
      this.unitType = unitType;
   }

   public String getUnit () {
      return unit;
   }

   public void setUnit (String unit) {
      this.unit = unit;
   }

   // public String getParent () {
   // return parent;
   // }
   //
   // public void setParent (String parent) {
   // this.parent = parent;
   // }

   public boolean isFromCohort () {
      return fromCohort;
   }

   public void setFromCohort (boolean fromCohort) {
      this.fromCohort = fromCohort;
   }

   public String getPrecision () {
      return precision;
   }

   public void setPrecision (String precision) {
      this.precision = precision;
   }

   public String getScale () {
      return scale;
   }

   public void setScale (String scale) {
      this.scale = scale;
   }
}