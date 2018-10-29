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

import com.execue.core.common.type.CheckType;

/**
 * This class represents the PossibleAttribute object.
 * 
 * @author Nihar
 * @version 1.0
 * @since 11/05/10
 */
public class PossibleDetailType implements Serializable {

   private static final long        serialVersionUID = 1L;
   private Long                     id;
   private BusinessEntityDefinition typeBed;
   private BusinessEntityDefinition detailTypeBed;
   private CheckType                dfault;

   public CheckType getDfault () {
      return dfault;
   }

   public void setDfault (CheckType dfault) {
      this.dfault = dfault;
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
    * @return the entityBed
    */
   public BusinessEntityDefinition getTypeBed () {
      return typeBed;
   }

   /**
    * @param entityBed
    *           the entityBed to set
    */
   public void setTypeBed (BusinessEntityDefinition typeBed) {
      this.typeBed = typeBed;
   }

}
