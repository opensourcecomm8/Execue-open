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

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.RFXObjectType;
import com.execue.core.common.type.RFXRecordType;
import com.execue.core.common.type.TripleVariationSubType;
import com.execue.core.common.type.TripleVariationType;

/**
 * @author John Mallavalli
 */
public class UQRFX implements Serializable {

   private Long                   id;
   private Long                   queryRFId;
   // private RFXEntityType rfxEntityType; // TODO: check if this is required, same as recordType
   private Long                   order;
   // private double weight; // TODO: check if this is required, other weights are available
   private CheckType              inferredFlag;
   private Long                   userId;

   // From RFTriple bean
   private Long                   subjectBeId;
   private Long                   predicateBeId;
   private Long                   objectBeId;
   private RFXObjectType          objectType;
   private String                 value;
   private Long                   pathIndex;
   private Long                   level;
   private Long                   listOrder;
   private Long                   listId;
   private RFXRecordType          recordType;
   private TripleVariationType    variationType;
   private TripleVariationSubType varirationSubtype;
   private Double                 subjectBeWeight;
   private Double                 predicateBeWeight;
   private Double                 objectBeWeight;
   private Double                 tripleWeight;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getQueryRFId () {
      return queryRFId;
   }

   public void setQueryRFId (Long queryRFId) {
      this.queryRFId = queryRFId;
   }

   // public RFXEntityType getRfxEntityType () {
   // return rfxEntityType;
   // }
   //
   // public void setRfxEntityType (RFXEntityType rfxEntityType) {
   // this.rfxEntityType = rfxEntityType;
   // }

   public Long getOrder () {
      return order;
   }

   public void setOrder (Long order) {
      this.order = order;
   }

   // public double getWeight () {
   // return weight;
   // }
   //
   // public void setWeight (double weight) {
   // this.weight = weight;
   // }

   public CheckType getInferredFlag () {
      return inferredFlag;
   }

   public void setInferredFlag (CheckType inferredFlag) {
      this.inferredFlag = inferredFlag;
   }

   public Long getUserId () {
      return userId;
   }

   public void setUserId (Long userId) {
      this.userId = userId;
   }

   public Long getSubjectBeId () {
      return subjectBeId;
   }

   public void setSubjectBeId (Long subjectBeId) {
      this.subjectBeId = subjectBeId;
   }

   public Long getPredicateBeId () {
      return predicateBeId;
   }

   public void setPredicateBeId (Long predicateBeId) {
      this.predicateBeId = predicateBeId;
   }

   public Long getObjectBeId () {
      return objectBeId;
   }

   public void setObjectBeId (Long objectBeId) {
      this.objectBeId = objectBeId;
   }

   public RFXObjectType getObjectType () {
      return objectType;
   }

   public void setObjectType (RFXObjectType objectType) {
      this.objectType = objectType;
   }

   public String getValue () {
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public Long getPathIndex () {
      return pathIndex;
   }

   public void setPathIndex (Long pathIndex) {
      this.pathIndex = pathIndex;
   }

   public Long getLevel () {
      return level;
   }

   public void setLevel (Long level) {
      this.level = level;
   }

   public Long getListOrder () {
      return listOrder;
   }

   public void setListOrder (Long listOrder) {
      this.listOrder = listOrder;
   }

   public Long getListId () {
      return listId;
   }

   public void setListId (Long listId) {
      this.listId = listId;
   }

   public RFXRecordType getRecordType () {
      return recordType;
   }

   public void setRecordType (RFXRecordType recordType) {
      this.recordType = recordType;
   }

   public TripleVariationType getVariationType () {
      return variationType;
   }

   public void setVariationType (TripleVariationType variationType) {
      this.variationType = variationType;
   }

   public TripleVariationSubType getVarirationSubtype () {
      return varirationSubtype;
   }

   public void setVarirationSubtype (TripleVariationSubType varirationSubtype) {
      this.varirationSubtype = varirationSubtype;
   }

   public Double getSubjectBeWeight () {
      return subjectBeWeight;
   }

   public void setSubjectBeWeight (Double subjectBeWeight) {
      this.subjectBeWeight = subjectBeWeight;
   }

   public Double getPredicateBeWeight () {
      return predicateBeWeight;
   }

   public void setPredicateBeWeight (Double predicateBeWeight) {
      this.predicateBeWeight = predicateBeWeight;
   }

   public Double getObjectBeWeight () {
      return objectBeWeight;
   }

   public void setObjectBeWeight (Double objectBeWeight) {
      this.objectBeWeight = objectBeWeight;
   }

   public Double getTripleWeight () {
      return tripleWeight;
   }

   public void setTripleWeight (Double tripleWeight) {
      this.tripleWeight = tripleWeight;
   }
}
