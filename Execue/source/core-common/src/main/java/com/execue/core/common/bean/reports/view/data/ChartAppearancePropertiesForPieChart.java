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


package com.execue.core.common.bean.reports.view.data;

public class ChartAppearancePropertiesForPieChart {

   private boolean IMAGESETTINGS;
   private boolean VIEW3D;
   private boolean POINTLABELS;
   private boolean LABELSINSIDE;
   private boolean SHOWLINE;
   private boolean SHADOWS;

   public boolean isIMAGESETTINGS () {
      return IMAGESETTINGS;
   }

   public void setIMAGESETTINGS (boolean imagesettings) {
      IMAGESETTINGS = imagesettings;
   }

   public boolean isVIEW3D () {
      return VIEW3D;
   }

   public void setVIEW3D (boolean view3d) {
      VIEW3D = view3d;
   }

   public boolean isPOINTLABELS () {
      return POINTLABELS;
   }

   public void setPOINTLABELS (boolean pointlabels) {
      POINTLABELS = pointlabels;
   }

   public boolean isLABELSINSIDE () {
      return LABELSINSIDE;
   }

   public void setLABELSINSIDE (boolean labelsinside) {
      LABELSINSIDE = labelsinside;
   }

   public boolean isSHOWLINE () {
      return SHOWLINE;
   }

   public void setSHOWLINE (boolean showline) {
      SHOWLINE = showline;
   }

   public boolean isSHADOWS () {
      return SHADOWS;
   }

   public void setSHADOWS (boolean shadows) {
      SHADOWS = shadows;
   }

}
