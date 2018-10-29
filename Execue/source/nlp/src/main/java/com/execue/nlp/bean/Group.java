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


package com.execue.nlp.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kaliki
 */
public class Group {

   private int                  possibilityId;
   private int                  iterationId;
   private Map<String, Integer> groupIdMap;

   public Group () {
      super();
      // TODO Auto-generated constructor stub
   }

   public Group (int possibilityId, int iterationId) {
      this.possibilityId = possibilityId;
      this.iterationId = iterationId;
      this.groupIdMap = new HashMap<String, Integer>();
   }

   public String getNewGroupIdForEntity (String entityName) {
      if (entityName == null) {
         return null;
      }
      String retGroupId = "";
      Integer newIdCount = null;

      if (groupIdMap.containsKey(entityName)) {
         Integer idCount = groupIdMap.get(entityName);
         newIdCount = idCount + 1;
         groupIdMap.put(entityName, newIdCount);
      } else {
         newIdCount = 1;
         groupIdMap.put(entityName, newIdCount);
      }

      // generate Group ID
      retGroupId = entityName + "#P" + possibilityId + "#I" + iterationId + "#" + newIdCount;

      return retGroupId;
   }

   public int getPossibilityId () {
      return possibilityId;
   }

   public void setPossibilityId (int possibilityId) {
      this.possibilityId = possibilityId;
   }

   public int getIterationId () {
      return iterationId;
   }

   public void setIterationId (int iterationId) {
      this.iterationId = iterationId;
   }

   public Map<String, Integer> getGroupIdMap () {
      return groupIdMap;
   }

   public void setGroupIdMap (Map<String, Integer> groupIdMap) {
      this.groupIdMap = groupIdMap;
   }

   @Override
   protected Object clone () throws CloneNotSupportedException {
      Group groupToBeCloned = this;
      Group clonedGroup = new Group();
      clonedGroup.setIterationId(groupToBeCloned.getIterationId());
      clonedGroup.setPossibilityId(groupToBeCloned.getPossibilityId());
      Map<String, Integer> toBeClonedMap = groupToBeCloned.getGroupIdMap();
      Map<String, Integer> clonedMap = new HashMap<String, Integer>();
      for (String str : toBeClonedMap.keySet()) {
         clonedMap.put(str, toBeClonedMap.get(str));
      }
      clonedGroup.setGroupIdMap(clonedMap);
      return clonedGroup;
   }

}
