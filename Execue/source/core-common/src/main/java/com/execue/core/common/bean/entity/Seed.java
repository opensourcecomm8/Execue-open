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

public class Seed implements Serializable {

   private static final long serialVersionUID = 1L;
   private Long              id;
   private Long           nodeId;
   private String            type;
   private Integer           nextValue;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Integer getNextValue () {
      return nextValue;
   }

   public void setNextValue (Integer value) {
      this.nextValue = value;
   }

   public Long getNodeId () {
      return nodeId;
   }

   public void setNodeId (Long nodeId) {
      this.nodeId = nodeId;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

}