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


package com.execue.core.common.bean;

import java.util.List;

import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ExecueFacetType;

/**
 * @author Vishay
 */
public class ExecueFacet {

   private String                  id;
   private Integer                 displayOrder;
   private String                  name;
   private ExecueFacetType         type      = ExecueFacetType.STRING;
   private CheckType               prominent = CheckType.NO;
   private List<ExecueFacetDetail> facetDetails;

   public String getId () {
      return id;
   }

   public void setId (String id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public ExecueFacetType getType () {
      return type;
   }

   public void setType (ExecueFacetType type) {
      this.type = type;
   }

   public CheckType getProminent () {
      return prominent;
   }

   public void setProminent (CheckType prominent) {
      this.prominent = prominent;
   }

   public List<ExecueFacetDetail> getFacetDetails () {
      return facetDetails;
   }

   public void setFacetDetails (List<ExecueFacetDetail> facetDetails) {
      this.facetDetails = facetDetails;
   }

   public Integer getDisplayOrder () {
      return displayOrder;
   }

   public void setDisplayOrder (Integer displayOrder) {
      this.displayOrder = displayOrder;
   }
}