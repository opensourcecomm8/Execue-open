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


package com.execue.core.common.bean.util;

import java.util.List;

public class AbsorptionWizardLinkDetail {

   private String       linkDescription;
   private String       breadcrumbDescription;
   private String       baseLink;
   private List<String> params;
   private boolean      scriptCall;

   public String getLinkDescription () {
      return linkDescription;
   }

   public void setLinkDescription (String linkDescription) {
      this.linkDescription = linkDescription;
   }

   public String getBreadcrumbDescription () {
      return breadcrumbDescription;
   }

   public void setBreadcrumbDescription (String breadcrumbDescription) {
      this.breadcrumbDescription = breadcrumbDescription;
   }

   public String getBaseLink () {
      return baseLink;
   }

   public void setBaseLink (String baseLink) {
      this.baseLink = baseLink;
   }

   public List<String> getParams () {
      return params;
   }

   public void setParams (List<String> params) {
      this.params = params;
   }

   public boolean isScriptCall () {
      return scriptCall;
   }

   public void setScriptCall (boolean scriptCall) {
      this.scriptCall = scriptCall;
   }

}
