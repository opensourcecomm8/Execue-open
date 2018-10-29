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

public class ChartAppearanceSeriesProps {

   private short  LINETHICKNESS;
   private short  POINTSIZE;
   private String BARSHAPE;
   private String YAXISFORMAT;
   private String XAXISFORMAT;
   private String TOOLTIPFORMAT;

   public short getLINETHICKNESS () {
      return LINETHICKNESS;
   }

   public void setLINETHICKNESS (short linethickness) {
      LINETHICKNESS = linethickness;
   }

   public short getPOINTSIZE () {
      return POINTSIZE;
   }

   public void setPOINTSIZE (short pointsize) {
      POINTSIZE = pointsize;
   }

   public String getBARSHAPE () {
      return BARSHAPE;
   }

   public void setBARSHAPE (String barshape) {
      BARSHAPE = barshape;
   }

   public String getYAXISFORMAT () {
      return YAXISFORMAT;
   }

   public void setYAXISFORMAT (String yaxisformat) {
      YAXISFORMAT = yaxisformat;
   }

   public String getXAXISFORMAT () {
      return XAXISFORMAT;
   }

   public void setXAXISFORMAT (String xaxisformat) {
      XAXISFORMAT = xaxisformat;
   }

   public String getTOOLTIPFORMAT () {
      return TOOLTIPFORMAT;
   }

   public void setTOOLTIPFORMAT (String tooltipformat) {
      TOOLTIPFORMAT = tooltipformat;
   }

}
