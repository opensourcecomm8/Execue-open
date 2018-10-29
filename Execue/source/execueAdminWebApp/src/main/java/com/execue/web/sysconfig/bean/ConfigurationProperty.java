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


package com.execue.web.sysconfig.bean;

import java.io.Serializable;

public class ConfigurationProperty implements Serializable {

   private String label;
   private String identity;
   private String name;
   private String value;
   private String displayValue;

   public String getLabel () {
      return label;
   }

   public void setLabel (String label) {
      this.label = label;
   }

   public String getIdentity () {
      return identity;
   }

   public void setIdentity (String identity) {
      this.identity = identity;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getValue () {
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public String getDisplayValue () {
      return displayValue;
   }

   public void setDisplayValue (String displayValue) {
      this.displayValue = displayValue;
   }

}
