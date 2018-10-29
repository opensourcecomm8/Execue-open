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


package com.execue.nlp.bean.matrix;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.IWeightedEntity;

/**
 * Hold data for each potential possibility.
 * 
 * @author Nitesh
 */

public class PotentialPossibility implements Cloneable {

   private int                   id;
   private List<IWeightedEntity> recognitionEntities;

   @Override
   public Object clone () throws CloneNotSupportedException {
      PotentialPossibility clonedPotentialPossibility = (PotentialPossibility) super.clone();
      List<IWeightedEntity> recognitionEntities = new ArrayList<IWeightedEntity>(1);
      recognitionEntities.addAll(getRecognitionEntities());
      clonedPotentialPossibility.setRecognitionEntities(recognitionEntities);
      return clonedPotentialPossibility;
   }

   /**
    * @return the id
    */
   public int getId () {
      return id;
   }

   /**
    * @param id
    *           the id to set
    */
   public void setId (int id) {
      this.id = id;
   }

   /**
    * @return the recognitionEntities
    */
   public List<IWeightedEntity> getRecognitionEntities () {
      if (this.recognitionEntities == null) {
         recognitionEntities = new ArrayList<IWeightedEntity>(1);
      }
      return recognitionEntities;
   }

   /**
    * @param recognitionEntities
    *           the recognitionEntities to set
    */
   public void setRecognitionEntities (List<IWeightedEntity> recognitionEntities) {
      this.recognitionEntities = recognitionEntities;
   }

   /**
    * @param weightedEntity
    */
   public void addRecognitionEntities (IWeightedEntity weightedEntity) {
      if (this.recognitionEntities == null) {
         recognitionEntities = new ArrayList<IWeightedEntity>(1);
      }
      recognitionEntities.add(weightedEntity);
   }
}
