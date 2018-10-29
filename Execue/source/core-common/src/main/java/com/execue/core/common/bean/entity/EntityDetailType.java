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

import java.io.Serializable;

/**
 * This class represents the PossibleAttribute object.
 * 
 * @author Nihar
 * @version 1.0
 * @since 11/05/10
 */
public class EntityDetailType implements Serializable {

   private static final long        serialVersionUID = 1L;
   private Long                     id;
   private BusinessEntityDefinition entityBed;
   private BusinessEntityDefinition detailTypeBed;

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
    * @return the detailTypeBed
    */
   public BusinessEntityDefinition getDetailTypeBed () {
      return detailTypeBed;
   }

   /**
    * @param detailTypeBed
    *           the detailTypeBed to set
    */
   public void setDetailTypeBed (BusinessEntityDefinition detailTypeBed) {
      this.detailTypeBed = detailTypeBed;
   }

   /**
    * @return the typeBed
    */
   public BusinessEntityDefinition getEntityBed () {
      return entityBed;
   }

   /**
    * @param typeBed
    *           the typeBed to set
    */
   public void setEntityBed (BusinessEntityDefinition entityBed) {
      this.entityBed = entityBed;
   }

   public EntityDetailType (BusinessEntityDefinition entityBed, BusinessEntityDefinition detailTypeBed) {
      super();
      this.entityBed = entityBed;
      this.detailTypeBed = detailTypeBed;
   }

   public EntityDetailType () {
      super();
   }

}
