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

/**
 * @author Prasanna
 */
public class SystemVariable implements java.io.Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private String            word;
   private String            entityType;

   /**
    * @return
    */
   public Long getId () {
      return id;
   }

   /**
    * @param id
    */
   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return
    */
   public String getWord () {
      return word;
   }

   public void setWord (String word) {
      this.word = word;
   }

   public String getEntityType () {
      return entityType;
   }

   public void setEntityType (String entityType) {
      this.entityType = entityType;
   }

   public static long getSerialVersionUID () {
      return serialVersionUID;
   }

}
