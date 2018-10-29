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


/**
 * 
 */
package com.execue.nlp.bean.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.IWeightedEntity;

/**
 * @author Nitesh
 */
public class AppCloudEntity extends RecognizedCloudEntity {

   Map<String, List<IWeightedEntity>> entityListByTFposId;

   /**
    * @return the entityListByTFposId
    */
   public Map<String, List<IWeightedEntity>> getEntityListByTFposId () {
      if (entityListByTFposId == null) {
         entityListByTFposId = new HashMap<String, List<IWeightedEntity>>(1);
      }
      return entityListByTFposId;
   }

   /**
    * @param entityListByTFposId
    *           the entityListByTFposId to set
    */
   public void setEntityListByTFposId (Map<String, List<IWeightedEntity>> entityListByTFposId) {
      this.entityListByTFposId = entityListByTFposId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.RecognizedCloudEntity#toString()
    */
   @Override
   public String toString () {
      return super.toString();
   }

   /**
    * This method returns true if weight assignment rule is associated to cloud, else false
    * 
    * @return boolean true/false
    */
   // TODO: NK: Currently returning true by default, should get set from the db??
   @Override
   public boolean isWeightAssignmentRuleAssociated () {
      return true;
   }

}