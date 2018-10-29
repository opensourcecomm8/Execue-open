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


package com.execue.core.common.bean.reports.view;

public class Column {

   private String DESCRIPTION;
   private String NAME;
   private String TYPE;
   private String AXIS;

   public String getDESCRIPTION () {
      return DESCRIPTION;
   }

   public void setDESCRIPTION (String description) {
      DESCRIPTION = description;
   }

   public String getNAME () {
      return NAME;
   }

   public void setNAME (String name) {
      NAME = name;
   }

   public String getTYPE () {
      return TYPE;
   }

   public void setTYPE (String type) {
      TYPE = type;
   }

   public String getAXIS () {
      return AXIS;
   }

   public void setAXIS (String axis) {
      AXIS = axis;
   }
}
