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


/*
 * Created on Sep 10, 2008
 */
package com.execue.nlp.bean.entity;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.IWeightedEntity;
import com.execue.core.common.bean.WeightInformation;

/**
 * @author kaliki
 */
public class AssociationGroupEntity implements IWeightedEntity {

   private String                   groupId;
   private List<AssociationlEntity> associationlEntites;
   private Map<Integer, Double>     updateRecognitionWeightTokenMap;

   public double getWeight () {
      double retValue = 0.0;
      for (AssociationlEntity entity : associationlEntites) {
         retValue += entity.getWeight();
      }
      return retValue;
   }

   public WeightInformation getWeightInformation() {
      return null;  
   }

   public double averageWeight () {
      return getWeight() / associationlEntites.size();
   }

   public double getQuality () {
      double retValue = 0.0;
      for (AssociationlEntity entity : associationlEntites) {
         retValue += entity.getWeightInformation().getRecognitionQuality();
      }
      if (associationlEntites.size() == 0) {
         return retValue;
      }
      return retValue / associationlEntites.size();
   }

   public double getAssociationSumWeight () {
      double retValue = 0.0;
      for (AssociationlEntity entity : associationlEntites) {
         retValue += entity.getWeightInformation().getRecognitionWeight();
      }
      return retValue;
   }

   public String getGroupId () {
      return groupId;
   }

   public void setGroupId (String groupId) {
      this.groupId = groupId;
   }

   public List<AssociationlEntity> getAssociationlEntites () {
      return associationlEntites;
   }

   public void setAssociationlEntites (List<AssociationlEntity> associationlEntites) {
      this.associationlEntites = associationlEntites;
   }

   public Map<Integer, Double> getUpdateRecognitionWeightTokenMap () {
      return updateRecognitionWeightTokenMap;
   }

   public void setUpdateRecognitionWeightTokenMap (Map<Integer, Double> updateRecognitionWeightTokenMap) {
      this.updateRecognitionWeightTokenMap = updateRecognitionWeightTokenMap;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      return super.clone();
   }

}
