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

import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ParallelWordType;

public class RIParallelWord implements java.io.Serializable {

   private Long             id;
   private String           word;
   private String           equivalentWord;
   private String           clusterTerms;
   private boolean          prefixSpace;
   private boolean          suffixSpace;
   private boolean          keyWord;
   private boolean          multiWord;
   private Long             hits;
   private Double           quality;
   private User             user;
   private Long             modelGroupId;
   private Long             bedId;
   private Long             keyWordId;
   private ParallelWordType pwdType;
   private String           posType;
   private Integer          preferedSelect  = 0;
   private CheckType        isDifferentWord = CheckType.YES;

   /**
    * @return the isDifferentWord
    */
   public CheckType getIsDifferentWord () {
      return isDifferentWord;
   }

   /**
    * @param isDifferentWord
    *           the isDifferentWord to set
    */
   public void setIsDifferentWord (CheckType isDifferentWord) {
      this.isDifferentWord = isDifferentWord;
   }

   /**
    * @return the preferedSelect
    */
   public Integer getPreferedSelect () {
      return preferedSelect;
   }

   /**
    * @param preferedSelect
    *           the preferedSelect to set
    */
   public void setPreferedSelect (Integer preferedSelect) {
      this.preferedSelect = preferedSelect;
   }

   /**
    * @return the posType
    */
   public String getPosType () {
      return posType;
   }

   /**
    * @param posType
    *           the posType to set
    */
   public void setPosType (String posType) {
      this.posType = posType;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getWord () {
      return word;
   }

   public void setWord (String word) {
      this.word = word;
   }

   public String getEquivalentWord () {
      return equivalentWord;
   }

   public void setEquivalentWord (String equivalentWord) {
      this.equivalentWord = equivalentWord;
   }

   public String getClusterTerms () {
      return clusterTerms;
   }

   public void setClusterTerms (String clusterTerms) {
      this.clusterTerms = clusterTerms;
   }

   public boolean isPrefixSpace () {
      return prefixSpace;
   }

   public void setPrefixSpace (boolean prefixSpace) {
      this.prefixSpace = prefixSpace;
   }

   public boolean isSuffixSpace () {
      return suffixSpace;
   }

   public void setSuffixSpace (boolean suffixSpace) {
      this.suffixSpace = suffixSpace;
   }

   public boolean isKeyWord () {
      return keyWord;
   }

   public void setKeyWord (boolean keyWord) {
      this.keyWord = keyWord;
   }

   public boolean isMultiWord () {
      return multiWord;
   }

   public void setMultiWord (boolean multiWord) {
      this.multiWord = multiWord;
   }

   public Long getKeyWordId () {
      return keyWordId;
   }

   public void setKeyWordId (Long keyWordId) {
      this.keyWordId = keyWordId;
   }

   public Long getHits () {
      return hits;
   }

   public void setHits (Long hits) {
      this.hits = hits;
   }

   public Double getQuality () {
      return quality;
   }

   public void setQuality (Double weight) {
      this.quality = weight;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   public Long getBedId () {
      return bedId;
   }

   public void setBedId (Long bedId) {
      this.bedId = bedId;
   }

   public ParallelWordType getPwdType () {
      return pwdType;
   }

   public void setPwdType (ParallelWordType pwdType) {
      this.pwdType = pwdType;
   }

   public Long getModelGroupId () {
      return modelGroupId;
   }

   public void setModelGroupId (Long modelGroupId) {
      this.modelGroupId = modelGroupId;
   }

}
