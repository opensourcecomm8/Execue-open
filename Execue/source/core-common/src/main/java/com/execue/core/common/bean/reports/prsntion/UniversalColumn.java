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

import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author John Mallavalli
 */
public class UniversalColumn {

   private String                ID;
   private String                DESC;
   private String                CTYPE;
   private String                PLOTAS;
   private String                DTYPE;
   private String                DATAFORMAT;
   private String                UNITTYPE;
   private String                UNIT;
   private String                FROMCOHORT;
   private String                NUMMEMBERS;
   private List<UniversalMember> MEMBERS;
   private String                ACROSS;
   private String                GROUPBY;
   private String                PRECISION;
   private String                SCALE;

   private static final String   idTag = "ID";

   public String getId () {
      return ID;
   }

   public void setId (String id) {
      this.ID = id;
   }

   public String getDesc () {
      return DESC;
   }

   public void setDesc (String desc) {
      this.DESC = desc;
   }

   public String getCtype () {
      return CTYPE;
   }

   public void setCtype (String ctype) {
      this.CTYPE = ctype;
   }

   public String getPlotAs () {
      return PLOTAS;
   }

   public void setPlotAs (String plotas) {
      this.CTYPE = plotas;
   }

   public String getDtype () {
      return DTYPE;
   }

   public void setDtype (String dtype) {
      this.DTYPE = dtype;
   }

   public String getNummembers () {
      return NUMMEMBERS;
   }

   public void setNummembers (String nummembers) {
      this.NUMMEMBERS = nummembers;
   }

   public String getAcross () {
      return ACROSS;
   }

   /**
    * This method returns true if ctype is id column else returns false.
    * 
    * @return
    */
   public boolean isIdColumn () {
      return idTag.equalsIgnoreCase(CTYPE);
   }

   public void setAcross (String across) {
      this.ACROSS = across;
   }

   public String getGroupby () {
      return GROUPBY;
   }

   public void setGroupby (String groupby) {
      this.GROUPBY = groupby;
   }

   public List<UniversalMember> getMembers () {
      return MEMBERS;
   }

   public void setMembers (List<UniversalMember> members) {
      this.MEMBERS = members;
   }

   public String getDataFormat () {
      return DATAFORMAT;
   }

   public void setDataFormat (String dataformat) {
      DATAFORMAT = dataformat;
   }

   public String getUnitType () {
      return UNITTYPE;
   }

   public void setUnitType (String unitType) {
      UNITTYPE = unitType;
   }

   public String getUnit () {
      return UNIT;
   }

   public void setUnit (String unit) {
      UNIT = unit;
   }

   public String getFromCohort () {
      return FROMCOHORT;
   }

   public void setFromCohort (String fromCohort) {
      FROMCOHORT = fromCohort;
   }

   public int getPrecision () {
      if (ExecueCoreUtil.isNotEmpty(PRECISION)) {
         return Integer.parseInt(PRECISION);
      } else {
         return 0;
      }
   }

   public void setPrecision (String precision) {
      PRECISION = precision;
   }

   public int getScale () {
      if (ExecueCoreUtil.isNotEmpty(SCALE)) {
         return Integer.parseInt(SCALE);
      } else {
         return 0;
      }
   }

   public void setScale (String scale) {
      SCALE = scale;
   }

}
