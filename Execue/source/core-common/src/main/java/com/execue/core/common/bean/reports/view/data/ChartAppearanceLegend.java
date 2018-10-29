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

public class ChartAppearanceLegend {

   private String USECUSTOMLEGEND;
   private String POSITIONLEG;
   private String SERIESTEXTMINLEN;
   private String SERIESTEXTMAXLEN;
   private String FONTSTYLE;
   private int    FONTSIZE;
   private int    TITLEFONTSIZE;

   public int getTITLEFONTSIZE () {
      return TITLEFONTSIZE;
   }

   public void setTITLEFONTSIZE (int titlefontsize) {
      TITLEFONTSIZE = titlefontsize;
   }

   public String getUSECUSTOMLEGEND () {
      return USECUSTOMLEGEND;
   }

   public void setUSECUSTOMLEGEND (String customlegend) {
      USECUSTOMLEGEND = customlegend;
   }

   public String getPOSITIONLEG () {
      return POSITIONLEG;
   }

   public void setPOSITIONLEG (String positionleg) {
      POSITIONLEG = positionleg;
   }

   public String getSERIESTEXTMINLEN () {
      return SERIESTEXTMINLEN;
   }

   public void setSERIESTEXTMINLEN (String seriestextminlen) {
      SERIESTEXTMINLEN = seriestextminlen;
   }

   public String getSERIESTEXTMAXLEN () {
      return SERIESTEXTMAXLEN;
   }

   public void setSERIESTEXTMAXLEN (String seriestextmaxlen) {
      SERIESTEXTMAXLEN = seriestextmaxlen;
   }

   public String getFONTSTYLE () {
      return FONTSTYLE;
   }

   public void setFONTSTYLE (String fontstyle) {
      FONTSTYLE = fontstyle;
   }

   public int getFONTSIZE () {
      return FONTSIZE;
   }

   public void setFONTSIZE (int fontsize) {
      FONTSIZE = fontsize;
   }

}
