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


package com.execue.swi.configuration.impl;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.util.AbsorptionWizardLinkDetail;

public class AbsorptionWizardHelper {

   private AbsorptionWizardHelper () {

   }

   private static Map<String, AbsorptionWizardLinkDetail> wizardPathLinkDetailMap;
   private static Map<String, List<String>>               sourceTypeWizardPathsMap;
   private static List<String>                            sourceTypes;
   private static List<String>                            wizardPaths;
   private static List<String>                            urlParams;

   public static Map<String, AbsorptionWizardLinkDetail> getWizardPathLinkDetailMap () {
      return wizardPathLinkDetailMap;
   }

   static void setWizardPathLinkDetailMap (Map<String, AbsorptionWizardLinkDetail> wizardPathLinkDetailMap) {
      AbsorptionWizardHelper.wizardPathLinkDetailMap = wizardPathLinkDetailMap;
   }

   public static Map<String, List<String>> getSourceTypeWizardPathsMap () {
      return sourceTypeWizardPathsMap;
   }

   static void setSourceTypeWizardPathsMap (Map<String, List<String>> sourceTypeWizardPathsMap) {
      AbsorptionWizardHelper.sourceTypeWizardPathsMap = sourceTypeWizardPathsMap;
   }

   public static List<String> getSourceTypes () {
      return sourceTypes;
   }

   static void setSourceTypes (List<String> sourceTypes) {
      AbsorptionWizardHelper.sourceTypes = sourceTypes;
   }

   public static List<String> getWizardPaths () {
      return wizardPaths;
   }

   static void setWizardPaths (List<String> wizardPaths) {
      AbsorptionWizardHelper.wizardPaths = wizardPaths;
   }

   public static List<String> getUrlParams () {
      return urlParams;
   }

   static void setUrlParams (List<String> urlParams) {
      AbsorptionWizardHelper.urlParams = urlParams;
   }

}
