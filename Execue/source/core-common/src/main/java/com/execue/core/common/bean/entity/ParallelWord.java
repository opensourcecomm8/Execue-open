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

import java.util.Set;

import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ParallelWordType;

public class ParallelWord implements java.io.Serializable {

   private Long                          id;
   private KeyWord                       keyWord;
   private User                          user;
   private String                        parallelWord;
   private boolean                       prefixSpace;
   private boolean                       suffixSpace;
   private ParallelWordType              pwdType;
   private Set<BusinessEntityDefinition> cluster;
   private Long                          popularity      = 0L;
   private double                        quality;
   private String                        posType;
   private CheckType                     isDifferentWord = CheckType.YES;

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
    * @return the quality
    */
   public double getQuality () {
      return quality;
   }

   /**
    * @param quality
    *           the quality to set
    */
   public void setQuality (double quality) {
      this.quality = quality;
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

   public KeyWord getKeyWord () {
      return keyWord;
   }

   public void setKeyWord (KeyWord keyWord) {
      this.keyWord = keyWord;
   }

   public String getParallelWord () {
      return parallelWord;
   }

   public void setParallelWord (String parallelWord) {
      this.parallelWord = parallelWord;
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

   public Set<BusinessEntityDefinition> getCluster () {
      return cluster;
   }

   public void setCluster (Set<BusinessEntityDefinition> cluster) {
      this.cluster = cluster;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

   public ParallelWordType getPwdType () {
      return pwdType;
   }

   public void setPwdType (ParallelWordType pwdType) {
      this.pwdType = pwdType;
   }

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

}
