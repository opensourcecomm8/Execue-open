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


package com.execue.handler;

import com.execue.core.common.bean.qi.QIConversion;
import com.execue.core.common.type.ConversionType;

public class UIPublishedFileColumnInfo {

   private ConversionType conversionType;
   private QIConversion   qiConversion;
   private Long           publishedFileTableDetailId;
   private boolean        conceptExist;
   private Long           conceptId;

   public ConversionType getConversionType () {
      return conversionType;
   }

   public void setConversionType (ConversionType conversionType) {
      this.conversionType = conversionType;
   }

   public QIConversion getQiConversion () {
      return qiConversion;
   }

   public void setQiConversion (QIConversion qiConversion) {
      this.qiConversion = qiConversion;
   }

   public Long getPublishedFileTableDetailId () {
      return publishedFileTableDetailId;
   }

   public void setPublishedFileTableDetailId (Long publishedFileTableDetailId) {
      this.publishedFileTableDetailId = publishedFileTableDetailId;
   }

   public boolean isConceptExist () {
      return conceptExist;
   }

   public void setConceptExist (boolean conceptExist) {
      this.conceptExist = conceptExist;
   }

   public Long getConceptId () {
      return conceptId;
   }

   public void setConceptId (Long conceptId) {
      this.conceptId = conceptId;
   }

}
