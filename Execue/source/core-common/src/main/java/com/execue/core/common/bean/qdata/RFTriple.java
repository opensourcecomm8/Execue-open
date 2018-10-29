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

import com.execue.core.common.type.RFXObjectType;
import com.execue.core.common.type.RFXRecordType;
import com.execue.core.common.type.TripleVariationSubType;
import com.execue.core.common.type.TripleVariationType;

/**
 * @author John Mallavalli
 */
public class RFTriple implements Serializable {

   private Long                   id;
   private Long                   reducedFormId;

   // DED id of the subject
   private Long                   subjectBeId;

   // DED id of the predicate
   private Long                   predicateBeId;

   // DED id of the object
   private Long                   objectBeId;

   // TODO: -JVK- remove subject, predicate & object fields later, only for debug mode
   private String                 subject;
   private String                 predicate;
   private String                 object;

   // type of the object [DED | VALUE | LIST | RANGE]
   private RFXObjectType          objectType;

   // if RFXObjectType is other than DED, value has some value
   private String                 value;

   // index of the path in the RF starting from root vertex
   private Long                   pathIndex;

   // the order of the entries as they appear in the RF
   private Long                   order;

   // indicates the level of entries in a path
   private Long                   level;

   // when the OBJECT is a list of entries, this indicates the index in which this entity appears in the list
   private Long                   listOrder;

   // indicates whether the operator to be applied on the list items is OR(1) or AND(0)
   private Long                   listId;

   // indicates the type of the Triple [FULL-TRIPLE(3) | PARTIAL-TRIPLE(2) | ENTITY(1)]
   private RFXRecordType          recordType;

   private TripleVariationType    variationType;
   private TripleVariationSubType variationSubtype;
   private Double                 subjectBeWeight;
   private Double                 predicateBeWeight;
   private Double                 objectBeWeight;
   private Double                 tripleWeight;

   public String toString () {
      StringBuilder sb = new StringBuilder();
      sb.append(subject + " - ").append(predicate + " - ").append(object);
      sb.append(" {").append(subjectBeId).append(", ").append(predicateBeId).append(", ").append(objectBeId)
               .append("}");
      sb.append(" [").append(pathIndex).append(":").append(order).append(":").append(level).append(":").append(
               listOrder).append(":").append(listId).append("]").append(" {" + this.getValue() + "}").append(
               " {" + this.getObjectType() + "}");
      return sb.toString();
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Long getReducedFormId () {
      return reducedFormId;
   }

   public void setReducedFormId (Long reducedFormId) {
      this.reducedFormId = reducedFormId;
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

   public String getSubject () {
      return subject;
   }

   public void setSubject (String subject) {
      this.subject = subject;
   }

   public String getPredicate () {
      return predicate;
   }

   public void setPredicate (String predicate) {
      this.predicate = predicate;
   }

   public String getObject () {
      return object;
   }

   public void setObject (String object) {
      this.object = object;
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

   public Long getOrder () {
      return order;
   }

   public void setOrder (Long order) {
      this.order = order;
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

   public TripleVariationSubType getVariationSubtype () {
      return variationSubtype;
   }

   public void setVariationSubtype (TripleVariationSubType variationSubtype) {
      this.variationSubtype = variationSubtype;
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
