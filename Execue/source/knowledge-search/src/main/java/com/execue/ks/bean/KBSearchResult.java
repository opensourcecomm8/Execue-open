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


package com.execue.ks.bean;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Abhijit
 * @since Jul 6, 2009 : 11:03:03 PM
 */
public class KBSearchResult {
   private Map<Long, KBDataEntity> entityMap;

   // Getter Setters

   public Map<Long, KBDataEntity> getEntityMap() {
      return this.entityMap;
   }

   public void setEntityMap(Map<Long, KBDataEntity> entityMap) {
      this.entityMap = entityMap;
   }

   // Utility Methods

   public void addDataEntity(KBDataEntity entity) {
      if(this.entityMap == null) this.entityMap = new HashMap<Long, KBDataEntity>();
      entityMap.put(entity.getId(), entity);
   }

   public void addDataEntity(long id, KBDataEntity entity) {
      if(this.entityMap == null) this.entityMap = new HashMap<Long, KBDataEntity>();
      entityMap.put(id, entity);
   }

   public boolean containsDataEntity(long entityId) {
      return (this.entityMap != null) && this.entityMap.containsKey(entityId);
   }

   public KBDataEntity getDataEntity(long entitytId) {
      return this.entityMap.get(entitytId);
   }
}
