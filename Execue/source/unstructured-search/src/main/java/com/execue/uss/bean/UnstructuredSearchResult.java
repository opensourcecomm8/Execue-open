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


package com.execue.uss.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 4:21:41 PM
 */
public class UnstructuredSearchResult {
   private Map<Long, ResultDataEntity> entityMap;

   // Getter Setters

   public Map<Long, ResultDataEntity> getEntityMap() {
      return this.entityMap;
   }

   public void setEntityMap(Map<Long, ResultDataEntity> entityMap) {
      this.entityMap = entityMap;
   }

   // Utility Methods

   public void addResultDataentity(ResultDataEntity entity) {
      if(this.entityMap == null) this.entityMap = new HashMap<Long, ResultDataEntity>();
      entityMap.put(entity.getId(), entity);
   }

   public void addResultDataentity(long id, ResultDataEntity entity) {
      if(this.entityMap == null) this.entityMap = new HashMap<Long, ResultDataEntity>();
      entityMap.put(id, entity);
   }

   public boolean containsResultDataentity(long entityId) {
      return (this.entityMap != null) && this.entityMap.containsKey(entityId);
   }

   public ResultDataEntity getResultDataentity(long entitytId) {
      return this.entityMap.get(entitytId);
   }
}
