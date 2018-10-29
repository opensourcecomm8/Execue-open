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

public class ChartAppearanceTitle {

   private String POSITION;
   private String ALIGNMENT;
   private String FONTFAMILY;
   private String FONTSIZE;
   private String FONTCOLOR;

   public String getPOSITION () {
      return POSITION;
   }

   public void setPOSITION (String position) {
      POSITION = position;
   }

   public String getALIGNMENT () {
      return ALIGNMENT;
   }

   public void setALIGNMENT (String alignment) {
      ALIGNMENT = alignment;
   }

   public String getFONTFAMILY () {
      return FONTFAMILY;
   }

   public void setFONTFAMILY (String fontfamily) {
      FONTFAMILY = fontfamily;
   }

   public String getFONTSIZE () {
      return FONTSIZE;
   }

   public void setFONTSIZE (String fontsize) {
      FONTSIZE = fontsize;
   }

   public String getFONTCOLOR () {
      return FONTCOLOR;
   }

   public void setFONTCOLOR (String fontcolor) {
      FONTCOLOR = fontcolor;
   }

}
