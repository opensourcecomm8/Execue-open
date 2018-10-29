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

import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.RecognitionType;

public class RIOntoTerm implements java.io.Serializable {

   private static final long  serialVersionUID = 1L;

   private Long               id;
   private String             word;
   private String             typeName;
   private String             conceptName;
   private String             instanceName;
   private String             relationName;
   private String             profileName;
   private RecognitionType    wordType;
   private BusinessEntityType entityType;
   private Long               typeBEDID;
   private Long               conceptBEDID;
   private Long               instanceBEDID;
   private Long               relationBEDID;
   private Long               profileBEDID;
   private Long               modelGroupId;
   private Long               popularity;
   private Long               knowledgeId;
   private Long               detailTypeBedId;
   private String             detailTypeName;
   private ConversionType     defaultConversionType;
   private String             defaultUnit;
   private String             defaultDataFormat;
   private Long               entityBEDID;

   /**
    * @return the detailTypeBedName
    */
   public String getDetailTypeName () {
      return detailTypeName;
   }

   /**
    * @param detailTypeBedName
    *           the detailTypeBedName to set
    */
   public void setDetailTypeName (String detailTypeName) {
      this.detailTypeName = detailTypeName;
   }

   /**
    * @return the id
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the word
    */
   public String getWord () {
      return word;
   }

   /**
    * @param word
    *           the word to set
    */
   public void setWord (String word) {
      this.word = word;
   }

   /**
    * @return the typeName
    */
   public String getTypeName () {
      return typeName;
   }

   /**
    * @param typeName
    *           the typeName to set
    */
   public void setTypeName (String typeName) {
      this.typeName = typeName;
   }

   /**
    * @return the conceptName
    */
   public String getConceptName () {
      return conceptName;
   }

   /**
    * @param conceptName
    *           the conceptName to set
    */
   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }

   /**
    * @return the instanceName
    */
   public String getInstanceName () {
      return instanceName;
   }

   /**
    * @param instanceName
    *           the instanceName to set
    */
   public void setInstanceName (String instanceName) {
      this.instanceName = instanceName;
   }

   /**
    * @return the relationName
    */
   public String getRelationName () {
      return relationName;
   }

   /**
    * @param relationName
    *           the relationName to set
    */
   public void setRelationName (String relationName) {
      this.relationName = relationName;
   }

   /**
    * @return the profileName
    */
   public String getProfileName () {
      return profileName;
   }

   /**
    * @param profileName
    *           the profileName to set
    */
   public void setProfileName (String profileName) {
      this.profileName = profileName;
   }

   /**
    * @return the wordType
    */
   public RecognitionType getWordType () {
      return wordType;
   }

   /**
    * @param wordType
    *           the wordType to set
    */
   public void setWordType (RecognitionType wordType) {
      this.wordType = wordType;
   }

   /**
    * @return the entityType
    */
   public BusinessEntityType getEntityType () {
      return entityType;
   }

   /**
    * @param entityType
    *           the entityType to set
    */
   public void setEntityType (BusinessEntityType entityType) {
      this.entityType = entityType;
   }

   /**
    * @return the conceptBEDID
    */
   public Long getConceptBEDID () {
      return conceptBEDID;
   }

   /**
    * @param conceptBEDID
    *           the conceptBEDID to set
    */
   public void setConceptBEDID (Long conceptBEDID) {
      this.conceptBEDID = conceptBEDID;
   }

   /**
    * @return the instanceBEDID
    */
   public Long getInstanceBEDID () {
      return instanceBEDID;
   }

   /**
    * @param instanceBEDID
    *           the instanceBEDID to set
    */
   public void setInstanceBEDID (Long instanceBEDID) {
      this.instanceBEDID = instanceBEDID;
   }

   /**
    * @return the relationBEDID
    */
   public Long getRelationBEDID () {
      return relationBEDID;
   }

   /**
    * @param relationBEDID
    *           the relationBEDID to set
    */
   public void setRelationBEDID (Long relationBEDID) {
      this.relationBEDID = relationBEDID;
   }

   /**
    * @return the profileBEDID
    */
   public Long getProfileBEDID () {
      return profileBEDID;
   }

   /**
    * @param profileBEDID
    *           the profileBEDID to set
    */
   public void setProfileBEDID (Long profileBEDID) {
      this.profileBEDID = profileBEDID;
   }

   /**
    * @return the modelGroupId
    */
   public Long getModelGroupId () {
      return modelGroupId;
   }

   /**
    * @param modelGroupId
    *           the modelGroupId to set
    */
   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

   /**
    * @return the popularity
    */
   public Long getPopularity () {
      return popularity;
   }

   /**
    * @param popularity
    *           the popularity to set
    */
   public void setPopularity (Long popularity) {
      this.popularity = popularity;
   }

   /**
    * @return the knowledgeId
    */
   public Long getKnowledgeId () {
      return knowledgeId;
   }

   /**
    * @param knowledgeId
    *           the knowledgeId to set
    */
   public void setKnowledgeId (Long knowledgeId) {
      this.knowledgeId = knowledgeId;
   }

   /**
    * @return the typeBEDID
    */
   public Long getTypeBEDID () {
      return typeBEDID;
   }

   /**
    * @param typeBEDID
    *           the typeBEDID to set
    */
   public void setTypeBEDID (Long typeBEDID) {
      this.typeBEDID = typeBEDID;
   }

   /**
    * @return the detailTypeBedId
    */
   public Long getDetailTypeBedId () {
      return detailTypeBedId;
   }

   /**
    * @param detailTypeBedId
    *           the detailTypeBedId to set
    */
   public void setDetailTypeBedId (Long detailTypeBedId) {
      this.detailTypeBedId = detailTypeBedId;
   }

   public ConversionType getDefaultConversionType () {
      return defaultConversionType;
   }

   public void setDefaultConversionType (ConversionType defaultConversionType) {
      this.defaultConversionType = defaultConversionType;
   }

   public String getDefaultUnit () {
      return defaultUnit;
   }

   public void setDefaultUnit (String defaultUnit) {
      this.defaultUnit = defaultUnit;
   }

   public String getDefaultDataFormat () {
      return defaultDataFormat;
   }

   public void setDefaultDataFormat (String defaultDataFormat) {
      this.defaultDataFormat = defaultDataFormat;
   }

   /**
    * @return the entityBEDID
    */
   public Long getEntityBEDID () {
      return entityBEDID;
   }

   /**
    * @param entityBEDID
    *           the entityBEDID to set
    */
   public void setEntityBEDID (Long entityBEDID) {
      this.entityBEDID = entityBEDID;
   }

}