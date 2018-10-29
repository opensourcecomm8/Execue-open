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


package com.execue.acqh.bean;

import java.util.Set;

/**
 * @author Prasanna
 */
public class QueryEntityInformation {

   private String      name;
   private String      bedId;
   private String      kdxType;
   private Set<String> variations;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getBedId () {
      return bedId;
   }

   public void setBedId (String bedId) {
      this.bedId = bedId;
   }

   public String getKdxType () {
      return kdxType;
   }

   public void setKdxType (String kdxType) {
      this.kdxType = kdxType;
   }

   public Set<String> getVariations () {
      return variations;
   }

   public void setVariations (Set<String> variations) {
      this.variations = variations;
   }

}
