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

import com.execue.core.common.type.JoinType;

/**
 * This class represents the JoinDefinition object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 22/01/09
 */
public class JoinDefinition implements java.io.Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private String            sourceTableName;
   private String            destTableName;
   private Asset             asset;
   private String            sourceColumnName;
   private String            destColumnName;
   private JoinType          type;
   private Set<Join>         joins;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getSourceTableName () {
      return sourceTableName;
   }

   public void setSourceTableName (String sourceTableName) {
      this.sourceTableName = sourceTableName;
   }

   public String getDestTableName () {
      return destTableName;
   }

   public void setDestTableName (String destTableName) {
      this.destTableName = destTableName;
   }

   public Asset getAsset () {
      return asset;
   }

   public void setAsset (Asset asset) {
      this.asset = asset;
   }

   public String getSourceColumnName () {
      return sourceColumnName;
   }

   public void setSourceColumnName (String sourceColumnName) {
      this.sourceColumnName = sourceColumnName;
   }

   public String getDestColumnName () {
      return destColumnName;
   }

   public void setDestColumnName (String destColumnName) {
      this.destColumnName = destColumnName;
   }

   public Set<Join> getJoins () {
      return joins;
   }

   public void setJoins (Set<Join> joins) {
      this.joins = joins;
   }

   public JoinType getType () {
      return type;
   }

   public void setType (JoinType type) {
      this.type = type;
   }
}
