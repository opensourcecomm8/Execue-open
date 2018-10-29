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

import com.thoughtworks.xstream.XStream;

public class ChartAppearanceMeta {

   private ChartAppearanceLayout                        LAYOUT;
   private ChartAppearanceSeriesProps                   SERIESPROPS;
   private ChartAppearanceTitle                         CHARTTITLE;
   private ChartAppearanceAxisColor                     AXISCOLOR;
   private ChartAppearanceBarlineChartColor             BARLINECHARTCOLOR;
   private ChartAppearancePieChartColor                 PIECHARTCOLOR;
   private ChartAppearanceLegend                        LEGEND;
   private ChartAppearanceChartWidth                    CHARTWIDTH;
   private ChartAppearanceChartTitleAlignment           CHARTTITLEALIGNMENT;
   private ChartAppearanceChartTitlePosition            CHARTTITLEPOTISION;
   private ChartAppearancePropertiesForBigChart         PROPERTIESFORBIGCHART;
   private ChartAppearancePropertiesForClusterChart     PROPERTIESFORCLUSTERCHART;
   private ChartAppearancePropertiesForSingleImageChart PROPERTIESFORSINGLEIMAGECHART;
   private ChartAppearancePropertiesForPieChart         PROPERTIESFORPIECHART;

   public ChartAppearancePropertiesForSingleImageChart getPROPERTIESFORSINGLEIMAGECHART () {
      return PROPERTIESFORSINGLEIMAGECHART;
   }

   public void setPROPERTIESFORSINGLEIMAGECHART (
            ChartAppearancePropertiesForSingleImageChart propertiesforsingleimagechart) {
      PROPERTIESFORSINGLEIMAGECHART = propertiesforsingleimagechart;
   }

   public ChartAppearancePropertiesForBigChart getPROPERTIESFORBIGCHART () {
      return PROPERTIESFORBIGCHART;
   }

   public void setPROPERTIESFORBIGCHART (ChartAppearancePropertiesForBigChart propertiesforbigchart) {
      PROPERTIESFORBIGCHART = propertiesforbigchart;
   }

   public ChartAppearanceChartTitlePosition getCHARTTITLEPOTISION () {
      return CHARTTITLEPOTISION;
   }

   public void setCHARTTITLEPOTISION (ChartAppearanceChartTitlePosition charttitlepotision) {
      CHARTTITLEPOTISION = charttitlepotision;
   }

   public ChartAppearanceChartTitleAlignment getCHARTTITLEALIGNMENT () {
      return CHARTTITLEALIGNMENT;
   }

   public void setCHARTTITLEALIGNMENT (ChartAppearanceChartTitleAlignment charttitlealignment) {
      CHARTTITLEALIGNMENT = charttitlealignment;
   }

   public ChartAppearanceLayout getLAYOUT () {
      return LAYOUT;
   }

   public void setLAYOUT (ChartAppearanceLayout layout) {
      LAYOUT = layout;
   }

   public ChartAppearanceTitle getCHARTTITLE () {
      return CHARTTITLE;
   }

   public void setCHARTTITLE (ChartAppearanceTitle charttitle) {
      CHARTTITLE = charttitle;
   }

   public ChartAppearanceAxisColor getAXISCOLOR () {
      return AXISCOLOR;
   }

   public void setAXISCOLOR (ChartAppearanceAxisColor axiscolor) {
      AXISCOLOR = axiscolor;
   }

   public ChartAppearanceLegend getLEGEND () {
      return LEGEND;
   }

   public void setLEGEND (ChartAppearanceLegend legend) {
      LEGEND = legend;
   }

   public static XStream getXStreamInstance () {
      XStream xstream = new XStream();
      xstream.alias("CHARTAPPEARANCE", ChartAppearanceMeta.class);
      return xstream;
   }

   public ChartAppearanceBarlineChartColor getBARLINECHARTCOLOR () {
      return BARLINECHARTCOLOR;
   }

   public void setBARLINECHARTCOLOR (ChartAppearanceBarlineChartColor barlinechartcolor) {
      BARLINECHARTCOLOR = barlinechartcolor;
   }

   public ChartAppearanceSeriesProps getSERIESPROPS () {
      return SERIESPROPS;
   }

   public void setSERIESPROPS (ChartAppearanceSeriesProps seriesprops) {
      SERIESPROPS = seriesprops;
   }

   public ChartAppearanceChartWidth getCHARTWIDTH () {
      return CHARTWIDTH;
   }

   public void setCHARTWIDTH (ChartAppearanceChartWidth chartwidth) {
      CHARTWIDTH = chartwidth;
   }

   public ChartAppearancePropertiesForClusterChart getPROPERTIESFORCLUSTERCHART () {
      return PROPERTIESFORCLUSTERCHART;
   }

   public void setPROPERTIESFORCLUSTERCHART (ChartAppearancePropertiesForClusterChart propertiesforclusterchart) {
      PROPERTIESFORCLUSTERCHART = propertiesforclusterchart;
   }

   public ChartAppearancePieChartColor getPIECHARTCOLOR () {
      return PIECHARTCOLOR;
   }

   public void setPIECHARTCOLOR (ChartAppearancePieChartColor piechartcolor) {
      PIECHARTCOLOR = piechartcolor;
   }

   public ChartAppearancePropertiesForPieChart getPROPERTIESFORPIECHART () {
      return PROPERTIESFORPIECHART;
   }

   public void setPROPERTIESFORPIECHART (ChartAppearancePropertiesForPieChart propertiesforpiechart) {
      PROPERTIESFORPIECHART = propertiesforpiechart;
   }

}
