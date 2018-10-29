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

import java.io.Serializable;

import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.type.ColumnType;

public class OptimalDSetSWIInfo implements Serializable, Cloneable {

   private Long            id;
   private Long            assetId;
   private Long            bedId;
   private String          conceptName;
   private ColumnType      kdxDataType;
   private Long            rangeId;
   private Integer         memberCount;
   private transient Range range;

   @Override
   public Object clone () throws CloneNotSupportedException {
      OptimalDSetSWIInfo clonedEntity = (OptimalDSetSWIInfo) super.clone();
      clonedEntity.setRange(null);
      return clonedEntity;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getBedId () {
      return bedId;
   }

   public void setBedId (Long bedId) {
      this.bedId = bedId;
   }

   public String getConceptName () {
      return conceptName;
   }

   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }

   public ColumnType getKdxDataType () {
      return kdxDataType;
   }

   public void setKdxDataType (ColumnType kdxDataType) {
      this.kdxDataType = kdxDataType;
   }

   public Long getRangeId () {
      return rangeId;
   }

   public void setRangeId (Long rangeId) {
      this.rangeId = rangeId;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   /**
    * @return the range
    */
   public Range getRange () {
      return range;
   }

   /**
    * @param range
    *           the range to set
    */
   public void setRange (Range range) {
      this.range = range;
   }

   
   public Integer getMemberCount () {
      return memberCount;
   }

   
   public void setMemberCount (Integer memberCount) {
      this.memberCount = memberCount;
   }
}
