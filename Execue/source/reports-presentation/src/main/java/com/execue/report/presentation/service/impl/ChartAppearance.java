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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package com.execue.report.presentation.service.impl;

import java.awt.Color;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.core.common.bean.reports.view.data.ChartAppearanceMeta;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.DockBorder;
import com.softwarefx.chartfx.server.ImageToolTipStyle;
import com.softwarefx.chartfx.server.MarkerShape;
import com.softwarefx.chartfx.server.SeriesAttributes;
import com.softwarefx.chartfx.server.adornments.SolidBackground;


/**
 * @author admin
 */
public class ChartAppearance {

   private ChartAppearanceMeta            chartXML;
   private ClassPathXmlApplicationContext classpathContext;

   public ChartAppearance () {
      chartXML = (ChartAppearanceMeta) ChartAppearanceMeta.getXStreamInstance().fromXML(
               getClass().getResourceAsStream("/ChartAppearance.xml"));
      classpathContext = new ClassPathXmlApplicationContext(new String[] { "/bean-config/applicaton.xml" });
   }

   String   panAxis      = "";
   String[] panAxisArray = null;

   public void setPropertiesForSmallCharts (ChartServer chart1) {
      chart1.setHeight(60);
      chart1.setWidth(80);
      chart1.getLegendBox().setVisible(false);
      chart1.getLegendBox().setBorder(DockBorder.EXTERNAL);
      chart1.getImageSettings().setToolTips(ImageToolTipStyle.NONE);
      chart1.getImageSettings().setInteractive(false);
      chart1.getAxisX().setVisible(false);
      chart1.getAxisY().setVisible(false);
      chart1.getAxisY2().setVisible(false);
      chart1.getAllSeries().setMarkerShape(MarkerShape.NONE);
      chart1.getPlotAreaMargin().setBottom(1);
      chart1.getPlotAreaMargin().setTop(1);
      chart1.getPlotAreaMargin().setLeft(1);
      chart1.setBackground(new SolidBackground());
      chart1.setBackColor(new Color(255, 255, 255, 255));
      chart1.setPlotAreaColor(new Color(245, 245, 245, 255));
      chart1.getPlotAreaMargin().setRight(1);
      setChartSeriesColors(chart1);
   }

   public void setChartSeriesColors (ChartServer chartCurve) {
      try {
         String rgb = "";
         String colors[] = new String[40];
         String rgbColor[] = new String[3];
         int noOfSeries = 0;
         if (chartXML.getBARLINECHARTCOLOR().isUSECUSTOMCOLORS()) {
            colors = StringUtils.split(chartXML.getBARLINECHARTCOLOR().getCOLORCODESERIES(), ReportingConstants.SEMI_COLON);

            noOfSeries = chartCurve.getData().getSeries();
            for (int cntr = 0; cntr < noOfSeries; cntr++) {
               if (cntr < colors.length) {
                  rgb = colors[cntr];
                  rgbColor = rgb.split(ReportingConstants.COMMA);
                  SeriesAttributes series;
                  series = chartCurve.getSeries().get(cntr);
                  series.setColor(new Color(Integer.parseInt(rgbColor[0]), Integer.parseInt(rgbColor[1]), Integer
                           .parseInt(rgbColor[2])));
               }
            }
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
