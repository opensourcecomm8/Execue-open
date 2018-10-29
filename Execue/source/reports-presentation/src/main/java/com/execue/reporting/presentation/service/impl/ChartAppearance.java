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

package com.execue.reporting.presentation.service.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.EnumSet;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.reports.view.data.ChartAppearanceMeta;
import com.softwarefx.StringAlignment;
import com.softwarefx.chartfx.server.AxesStyle;
import com.softwarefx.chartfx.server.AxisFormat;
import com.softwarefx.chartfx.server.AxisPosition;
import com.softwarefx.chartfx.server.AxisStyles;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.ContentLayout;
import com.softwarefx.chartfx.server.DockArea;
import com.softwarefx.chartfx.server.DockBorder;
import com.softwarefx.chartfx.server.ImageToolTipStyle;
import com.softwarefx.chartfx.server.LegendBoxStyles;
import com.softwarefx.chartfx.server.LegendItemAttributes;
import com.softwarefx.chartfx.server.MarkerShape;
import com.softwarefx.chartfx.server.SeriesAttributes;
import com.softwarefx.chartfx.server.TitleDockable;
import com.softwarefx.chartfx.server.adornments.SimpleBorder;
import com.softwarefx.chartfx.server.adornments.SimpleBorderType;
import com.softwarefx.chartfx.server.adornments.SolidBackground;

/**
 * @author admin
 */
public class ChartAppearance {

   private static final Logger log           = Logger.getLogger(ChartAppearance.class);
   private ChartAppearanceMeta chartMetaInfo;

   XMLFileReader               xMLFileReader = new XMLFileReader();

   public int getChartWidth (String chartType) {
      int width = 0;
      String strWidth = "";
      int noOfRows = ConvertionForChart.noOfRows;
      XMLFileReader xMLFileReader = new XMLFileReader();
      try {
         strWidth = xMLFileReader.getValueByNode("/ChartApperance.xml", "width");
         int minWidth = Integer.parseInt(xMLFileReader.getValueByNode("/ChartApperance.xml", "minwidth"));
         int minNoOfRows = chartMetaInfo.getCHARTWIDTH().getMINNOOFROWS();
         int maxNoOfRows = chartMetaInfo.getCHARTWIDTH().getMAXNOOFROWS();

         if (!chartType.equalsIgnoreCase("CLUSTER")) {
            if (noOfRows < minNoOfRows) {
               width = noOfRows * ((int) (Double.parseDouble(strWidth) * 0.25));
            } else if (minNoOfRows < noOfRows && noOfRows < maxNoOfRows) {
               width = (int) Double.parseDouble(strWidth);
            } else {
               width = noOfRows * ((int) (Double.parseDouble(strWidth) * 0.034));
            }// for across bar and lines new rule to have the max width from xml in case of large data points
            if (chartType.equalsIgnoreCase("7") || chartType.equalsIgnoreCase("8")) {
               if (noOfRows > maxNoOfRows) {
                  width = (int) Double.parseDouble(strWidth);
               }
            }
            if (width < minWidth) {
               width = minWidth;
            }
         } else {
            if (noOfRows < minNoOfRows) {
               width = (int) (Double.parseDouble(strWidth) * 0.40);
            } else {
               width = (int) (Double.parseDouble(strWidth) * 0.55);
            }
         }
      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }
      return width;
   }

   ArrayList         panAxis            = new ArrayList();
   String[]          panAxisArray       = null;
   String            chartTypeString    = "";
   protected boolean y2AxisPlottingFlag = false;

   public int getChartHeight (int chartWidth) {
      int chartHeight = 0;

      // maintain the aspect ration of 4:3 (nearly) by dividing the width by 1.35416 which is near to 4:3 aspect ratio
      int minwidth = chartMetaInfo.getLAYOUT().getMINWIDTH();
      chartHeight = (int) ((double) chartWidth / 1.35416);
      if (chartHeight > minwidth)
         return 500;
      else
         return chartHeight;
   }

   public void getLegendBoxLabels (ChartServer chartServer) {
      try {
         int xData = chartServer.getData().getSeries();
         for (int cntr = 0; cntr < xData; cntr++) {
            chartServer.getSeries().get(cntr).setText(chartServer.getSeries().get(cntr).toString().replace("_", " "));
            // chartServer.getLegendBox().setFont(new Font("arial",java.awt.Font.PLAIN, 8));
            // chartServer.getLegendBox().setPlotAreaOnly(false);
            // chartServer.getLegendBox().setContentLayout(ContentLayout.CENTER);
            // chartServer.getSeries().get(cntr).setVolume((short)20);
         }
      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }

   }

   public void setPropertiesForBigCharts (ChartServer chartCurve, String chartName) {

      int minNoOfRows = chartMetaInfo.getPROPERTIESFORBIGCHART().getMINNOOFROWS();
      int maxNoOfRows = chartMetaInfo.getPROPERTIESFORBIGCHART().getMAXNOOFROWS();
      // chartCurve.getAxisX().setLabelAngle((short) 0);
      // getMeasureOnY1Y2Axis();//jaimin 26-6-09
      setBasicPropertiesForCharts(chartCurve);
      chartCurve.getAxisX().setStaggered(true);
      // chartCurve.getHighlight().isIgnorePointAttributes();
      // setYAxisLabel(chartCurve,(int)chartCurve.getAxisY().getMax(),"Y");
      // setYAxisLabel(chartCurve,(int)chartCurve.getAxisY2().getMax(),"Y2");
      // chartCurve.getHighlight().setDimmed(false);
      // chartCurve.getAxisX().setLabelTrimming("");
      chartCurve.getAxisX().toString().subSequence(0, 6);
      chartCurve.getAxisX().getLabels().size();
      chartCurve.getAxisX().getLabels().remove(chartCurve.getAxisX().getFirstLabel());

      chartCurve.getPlotAreaMargin().setBottom(1);
      chartCurve.getPlotAreaMargin().setTop(40);
      chartCurve.getPlotAreaMargin().setLeft(1);
      chartCurve.getTitles().add(new TitleDockable(" "));

      setChartTitle(chartCurve, chartName);
      setAxisColor(chartCurve);
      setBarLineChartColor(chartCurve);
      chartCurve.setToolTips(true);
      chartCurve.getImageSettings().setToolTips(ImageToolTipStyle.AS_TITLE);
      chartCurve.setToolTipFormat("%s In %l - %v");

      chartCurve.getToolBar().setVisible(false);
      chartCurve.getLegendBox().setVisible(true);

      chartCurve.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
      chartCurve.getAxisY2().getLabelsFormat().setFormat(AxisFormat.NUMBER);
      chartCurve.setFont(new java.awt.Font(chartMetaInfo.getPROPERTIESFORBIGCHART().getFONTSTYLE(),
               java.awt.Font.PLAIN, chartMetaInfo.getPROPERTIESFORBIGCHART().getFONTSIZE()));
      chartCurve.getImageSettings().setInteractive(false);
      chartCurve.getAxisY().getTitle().setFont(
               new java.awt.Font(chartMetaInfo.getPROPERTIESFORBIGCHART().getFONTSTYLE(), java.awt.Font.PLAIN,
                        chartMetaInfo.getPROPERTIESFORBIGCHART().getFONTSIZE()));
      // chartCurve.getLegendBox().setDock(DockArea.BOTTOM);
      // ---- call for get ReportType

      // getChartWidth(chart);
      // setLegendBoxPosition(chartCurve);
      chartCurve.setAxesStyle(AxesStyle.MATH);
      int chartWidth = getChartWidth("NORMAL");
      int chartHeight = getChartHeight(chartWidth);

      int chartTitleLenght = chartCurve.getTitles().get(1).getText().length();
      int maxTitleLenght = (int) (chartWidth * .167);
      boolean showTitle = maxTitleLenght > chartTitleLenght ? true : false;
      if (!showTitle) {
         String trimmedTitle = chartCurve.getTitles().get(1).getText().substring(0, maxTitleLenght - 8);
         chartCurve.getTitles().get(1).setText(trimmedTitle + "...");
         chartCurve.getTitles().get(1).setPlotAreaOnly(false);
         chartCurve.getTitles().get(1).setAlignment(StringAlignment.NEAR);
         // chartCurve.getPlotAreaMargin().setTop(3);
      }
      chartCurve.getLegendBox().setWidth(chartWidth);
      if (ConvertionForChart.noOfRows < minNoOfRows) {
         chartCurve.getLegendBox().setHeight((int) (chartHeight * .22));
         chartCurve.getAxisX().setStaggered(false);
      }
      if (ConvertionForChart.noOfRows > maxNoOfRows) {
         chartCurve.getAxisX().setLabelAngle((short) 45);
         chartCurve.getAxisX().setStaggered(false);
      }// else{
      chartCurve.setWidth(chartWidth);
      chartCurve.setHeight(chartHeight);
      // }
   }

   public void setPropertiesForSmallCharts (ChartServer chart1) {
      chart1.setHeight(60);
      chart1.setWidth(80);
      chart1.getLegendBox().setVisible(false);
      // chart1.getLegendBox().setBorder(DockBorder.EXTERNAL);
      chart1.getImageSettings().setToolTips(ImageToolTipStyle.NONE);
      chart1.getImageSettings().setInteractive(false);

      chart1.getAxisX().setVisible(false);
      chart1.getAxisY().setVisible(false);
      chart1.getAxisY2().setVisible(false);
      chart1.getAllSeries().setMarkerShape(MarkerShape.NONE);
      chart1.getPlotAreaMargin().setBottom(1);
      chart1.getPlotAreaMargin().setTop(1);
      chart1.getPlotAreaMargin().setLeft(1);
      chart1.getPlotAreaMargin().setRight(1);
      setBarLineChartColor(chart1);
      // if (chart1.getAxisY().getMax() < 400000 ){
      // chart1.getAxisY().setMax(chart1.getAxisY().getMax()+( 10 * chart1.getAxisY().getMax())/100);
      // }
      // else{
      // chart1.getAxisY().setMax(chart1.getAxisY().getMax()+( 4 * chart1.getAxisY().getMax())/100);
      // }
   }

   public void setPropertiesForClusterCharts (ChartServer[] chartCurveTemp, int tempCntr, String chartType) {
      setBasicPropertiesForCharts(chartCurveTemp[tempCntr]);
      chartCurveTemp[tempCntr].setToolTipFormat("%s: In %l - %v");
      // chartCurveTemp[tempCntr].getAxisX().setLabelAngle((short) 90);
      chartCurveTemp[tempCntr].getAxisX().setStaggered(true);

      int chartWidth = getChartWidth("CLUSTER");
      int chartHeight = getChartHeight(chartWidth);
      int minNoOfRows = chartMetaInfo.getPROPERTIESFORCLUSTERCHART().getMINNOOFROWS();
      int maxNoOfRows = chartMetaInfo.getPROPERTIESFORCLUSTERCHART().getMAXNOOFROWS();
      chartCurveTemp[tempCntr].setWidth(chartWidth);
      chartCurveTemp[tempCntr].setHeight(chartHeight);

      /*
       * if(tempCntr < 0){ setYAxisLabel( chartCurveTemp[tempCntr],(int)
       * chartCurveTemp[tempCntr].getAxisY().getMax(),"Y"); } else{
       * chartCurveTemp[tempCntr].getAxisY2().setAutoScale(true);
       * setYAxisLabel(chartCurveTemp[tempCntr],(int)chartCurveTemp[tempCntr].getAxisY2().getMax(),"Y2"); }
       */
      // chartCurveTemp[tempCntr].getAxisX().getLabels().set(tempCntr, (String)
      // chartCurveTemp[tempCntr].getAxisX().getLabels().toString().subSequence(0, 8));
      // chartCurveTemp[tempCntr].setZoom(true);
      // chartCurveTemp[tempCntr].getAxisX().getLabels().toString().subSequence(1, 6);
      chartCurveTemp[tempCntr].getToolBar().setVisible(false);
      chartCurveTemp[tempCntr].setBorder((new SimpleBorder(SimpleBorderType.NONE)));
      chartCurveTemp[tempCntr].getLegendBox().setVisible(false);
      if (chartType.equals("8") || chartType.equals("7")) {
         if (tempCntr == chartCurveTemp.length - 1) {
            chartCurveTemp[tempCntr].getLegendBox().setVisible(true);
         } else {
            chartCurveTemp[tempCntr].getLegendBox().setVisible(false);
            chartCurveTemp[tempCntr].getLegendBox().setHeight(1);
            chartCurveTemp[tempCntr].getLegendBox().setWidth(1);
         }
         // chartCurveTemp[tempCntr].getLegendBox().setVisible(true);
         panAxis = ConvertionForChart.panLableAL;
         // panAxisArray = panAxis.split(",");
         chartCurveTemp[tempCntr].getAxisY().getTitle().setText(panAxis.get(tempCntr).toString().replace("_", " "));
         chartWidth = getChartWidth(chartType);
         chartHeight = getChartHeight(chartWidth);
         chartCurveTemp[tempCntr].setWidth(chartWidth);
         chartCurveTemp[tempCntr].setHeight(chartHeight);
      } else {
         chartCurveTemp[tempCntr].getAxisY().getTitle().setText(
                  chartCurveTemp[tempCntr].getSeries().get(0).getText().replaceAll("_", " "));
      }
      // chartCurveTemp[tempCntr].getLegendBox().setDock(DockArea.BOTTOM);
      chartCurveTemp[tempCntr].getImageSettings().setInteractive(false);
      chartCurveTemp[tempCntr].setFont(new java.awt.Font(chartMetaInfo.getPROPERTIESFORCLUSTERCHART().getFONTSTYLE(),
               java.awt.Font.PLAIN, chartMetaInfo.getPROPERTIESFORCLUSTERCHART().getFONTSIZE()));

      chartCurveTemp[tempCntr].getPlotAreaMargin().setBottom(1);
      chartCurveTemp[tempCntr].getPlotAreaMargin().setTop(5);
      chartCurveTemp[tempCntr].getPlotAreaMargin().setLeft(1);
      chartCurveTemp[tempCntr].getPlotAreaMargin().setRight(1);
      chartCurveTemp[tempCntr].setAxesStyle(AxesStyle.MATH);

      chartCurveTemp[tempCntr].getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
      chartCurveTemp[tempCntr].getAxisY2().getLabelsFormat().setFormat(AxisFormat.NUMBER);
      // chartCurveTemp[tempCntr].getAxisY().getTitle().setText(chartCurveTemp[tempCntr].getSeries().get(0).getText().replaceAll("_","
      // "));
      // chartCurveTemp[tempCntr].getAxisY().getGrids().getMajor().setVisible(false);
      // chartCurveTemp[tempCntr].getAxisX().getGrids().getMajor().setVisible(false);
      // chartCurveTemp[tempCntr].getAxisY2().getGrids().getMajor().setVisible(false);
      setAxisColor(chartCurveTemp[tempCntr]);

      if (chartType.equals("13") || chartType.equals("12") || chartType.equals("15") || chartType.equals("16")) {
         // if(chartType.equals("12") ||chartType.equals("15"))
         // {
         // if(tempCntr > 0 )
         // {
         // chartCurveTemp[tempCntr].getAxisY().setVisible(false);
         SeriesAttributes serie3;
         serie3 = chartCurveTemp[tempCntr].getSeries().get(0);
         serie3.setAxisY(chartCurveTemp[tempCntr].getAxisY());
         chartCurveTemp[tempCntr].getAxisY().getTitle().setText(serie3.getText().replaceAll("_", " "));
         // chartCurveTemp[tempCntr].getAxisY().getTitle().isRichText();
         // setYAxisLabel(chartCurveTemp[tempCntr],(int)chartCurveTemp[tempCntr].getAxisY2().getMax(),"Y");
         // }
         // else{
         // chartCurveTemp[tempCntr].getAxisY().getTitle().setText(chartCurveTemp[tempCntr].getSeries().get(0).getText().replaceAll("_","
         // "));
         // }
         // }
         if (chartCurveTemp[tempCntr].getAxisY().getMax() < 400000) {
            chartCurveTemp[tempCntr].getAxisY().setMax(
                     chartCurveTemp[tempCntr].getAxisY().getMax() + (10 * chartCurveTemp[tempCntr].getAxisY().getMax())
                              / 100);
         } else {
            chartCurveTemp[tempCntr].getAxisY().setMax(
                     chartCurveTemp[tempCntr].getAxisY().getMax() + (4 * chartCurveTemp[tempCntr].getAxisY().getMax())
                              / 100);
         }
         //
         setClusterChartColor(chartCurveTemp[tempCntr], tempCntr);
      } else if (chartType.equals("8") || chartType.equals("7")) {
         setBarLineChartColor(chartCurveTemp[tempCntr]);
      }

      if (chartType.equals("16")) {
         chartCurveTemp[tempCntr].getLegendBox().setVisible(false);
      }

      if (ConvertionForChart.noOfRows < minNoOfRows) {
         // chartCurveTemp[tempCntr].getAxisX().setStaggered(false);
      }
      if (ConvertionForChart.noOfRows > maxNoOfRows) {
         chartCurveTemp[tempCntr].getAxisX().setLabelAngle((short) 45);
         chartCurveTemp[tempCntr].getAxisX().setStaggered(false);
      }// else{
      // chartCurveTemp[tempCntr].setWidth(chartWidth);
      // chartCurveTemp[tempCntr].setHeight(chartHeight);
      // }
      chartCurveTemp[tempCntr].getLegendBox().setWidth(chartWidth);
      chartCurveTemp[tempCntr].getLegendBox().setHeight((int) (chartHeight * .20));
      chartCurveTemp[tempCntr].renderControl();
   }

   public void setYAxisLabel (ChartServer chart, int maxYAxis, String axisName) {
      if (maxYAxis > 10000) {

         if (axisName.equalsIgnoreCase("Y")) {
            int yAxisArr[] = new int[4];
            int stepVal = (int) Math.round((chart.getAxisY().getMax()) / yAxisArr.length);
            for (int cntr = 0; cntr < yAxisArr.length; cntr++) {
               if (cntr == 0) {
                  yAxisArr[cntr] = cntr;
               } else {
                  yAxisArr[cntr] = yAxisArr[cntr - 1] + stepVal;
               }
            }
            chart.getAxisY().setMax(maxYAxis);
            chart.getAxisY().setMin(chart.getAxisY().getMin());
            chart.getAxisY().setStep(stepVal);
            for (int cntr = 0; cntr < yAxisArr.length; cntr++) {
               if (cntr == 0) {
                  chart.getAxisY().getLabels().set((short) cntr, 0 + "K");
               } else {
                  chart.getAxisY().getLabels().set((short) cntr, (yAxisArr[cntr - 1] + stepVal) / 1000 + "K");
               }
            }
            chart.getAxisY().setLabelValue(stepVal);

         }

         else if (axisName.equalsIgnoreCase("Y2")) {

            int yAxisArr[] = new int[4];
            int stepVal = (int) Math.round((chart.getAxisY().getMax()) / yAxisArr.length);
            for (int cntr = 0; cntr < yAxisArr.length; cntr++) {
               if (cntr == 0) {
                  yAxisArr[cntr] = cntr;
               } else {
                  yAxisArr[cntr] = yAxisArr[cntr - 1] + stepVal;
               }
            }
            chart.getAxisY2().setMax(maxYAxis);
            chart.getAxisY2().setMin(chart.getAxisY2().getMin());
            chart.getAxisY2().setStep(stepVal);
            for (int cntr = 0; cntr < yAxisArr.length; cntr++) {
               if (cntr == 0) {
                  chart.getAxisY2().getLabels().set((short) cntr, 0 + "K");
               } else {
                  chart.getAxisY2().getLabels().set((short) cntr, (yAxisArr[cntr - 1] + stepVal) / 1000 + "K");

               }
            }
            chart.getAxisY2().setLabelValue(stepVal);
         }
      }

   }

   public void setChartTitle (ChartServer chartCurve, String chartName) {
      try {
         String titlePosition = "";
         String titleAlign = "";
         String fontFamily = "";
         String fontColor = "";
         int fontSize = 0;
         String colorArr[] = new String[3];

         // XMLFileReader xMLFileReader = new XMLFileReader();

         TitleDockable titleDockable = new TitleDockable(chartName);

         titlePosition = xMLFileReader.getValueByNode("/ChartApperance.xml", "position");
         if (titlePosition.equalsIgnoreCase("left")) {
            titleDockable.setDock(DockArea.LEFT);
         } else if (titlePosition.equalsIgnoreCase("right")) {
            titleDockable.setDock(DockArea.RIGHT);
         } else if (titlePosition.equalsIgnoreCase("top")) {
            titleDockable.setDock(DockArea.TOP);
         } else if (titlePosition.equalsIgnoreCase("bottom")) {
            titleDockable.setDock(DockArea.BOTTOM);
         }
         chartCurve.getTitles().add(titleDockable);

         titleAlign = xMLFileReader.getValueByNode("/ChartApperance.xml", "ALIGNMENT");
         if (titleAlign.equalsIgnoreCase("center")) {
            chartCurve.getTitles().get(1).setAlignment(StringAlignment.CENTER);
         } else if (titleAlign.equalsIgnoreCase("right")) {
            chartCurve.getTitles().get(1).setAlignment(StringAlignment.FAR);
         } else if (titleAlign.equalsIgnoreCase("left")) {
            chartCurve.getTitles().get(1).setAlignment(StringAlignment.NEAR);
         }
         fontFamily = xMLFileReader.getValueByNode("/ChartApperance.xml", "FONTFAMILY");
         fontSize = Integer.parseInt(xMLFileReader.getValueByNode("/ChartApperance.xml", "FONTSIZE"));

         chartCurve.getTitles().get(1).setFont(new java.awt.Font(fontFamily, java.awt.Font.BOLD, fontSize));
         fontColor = xMLFileReader.getValueByNode("/ChartApperance.xml", "FONTCOLOR");
         colorArr = fontColor.split(",");
         chartCurve.getTitles().get(1)
                  .setTextColor(
                           new Color(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer
                                    .parseInt(colorArr[2])));

      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }

   }

   public void setAxisColor (ChartServer chartCurve) {
      try {
         String colorString = "";
         String xAxesColor[] = new String[3];
         String yAxesColor[] = new String[3];
         chartCurve.setAxesStyle(AxesStyle.MATH);
         colorString = xMLFileReader.getValueByNode("/ChartApperance.xml", "XAXESCOLOR");
         xAxesColor = colorString.split(",");
         chartCurve.getAxisY().getLine().setColor(
                  new Color(Integer.parseInt(xAxesColor[0]), Integer.parseInt(xAxesColor[1]), Integer
                           .parseInt(xAxesColor[2])));
         colorString = xMLFileReader.getValueByNode("/ChartApperance.xml", "YAXESCOLOR");
         yAxesColor = colorString.split(",");
         chartCurve.getAxisX().getLine().setColor(
                  new Color(Integer.parseInt(yAxesColor[0]), Integer.parseInt(yAxesColor[1]), Integer
                           .parseInt(yAxesColor[2])));

         colorString = xMLFileReader.getValueByNode("/ChartApperance.xml", "YGRIDCOLOR");
         yAxesColor = colorString.split(",");
         chartCurve.getAxisY().getGrids().getMajor().setColor(
                  new Color(Integer.parseInt(yAxesColor[0]), Integer.parseInt(yAxesColor[1]), Integer
                           .parseInt(yAxesColor[2])));
      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }
   }

   public void setBarLineChartColor (ChartServer chartCurve) {
      try {
         String setColor = "";
         String rgb = "";
         String colors[] = new String[40];
         String rgbColor[] = new String[3];
         int noOfSeries = 0;

         setColor = xMLFileReader.getValueByNode("/ChartApperance.xml", "CUSTOMIZEBARLINE");
         if (setColor.equalsIgnoreCase("1")) {
            for (int cntr = 0; cntr < colors.length; cntr++) {
               colors[cntr] = xMLFileReader.getValueByNode("/ChartApperance.xml", "S" + (cntr + 1));
            }

            noOfSeries = chartCurve.getData().getSeries();
            for (int cntr = 0; cntr < noOfSeries; cntr++) {
               if (cntr < colors.length) {
                  rgb = colors[cntr];
                  rgbColor = rgb.split(",");
                  SeriesAttributes series;
                  series = chartCurve.getSeries().get(cntr);
                  series.setColor(new Color(Integer.parseInt(rgbColor[0]), Integer.parseInt(rgbColor[1]), Integer
                           .parseInt(rgbColor[2])));
               }
            }
         }

      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }
   }

   public void setClusterChartColor (ChartServer chartCurve, int tempCntr) {
      try {
         String setColor = "";
         String rgb = "";
         String colors[] = new String[40];
         String rgbColor[] = new String[3];

         setColor = xMLFileReader.getValueByNode("/ChartApperance.xml", "CUSTOMIZEBARLINE");
         if (setColor.equalsIgnoreCase("0")) {
            if (tempCntr == 0) {
               chartCurve.getAllSeries().setColor(new Color(65, 105, 225));
            }
            if (tempCntr == 1) {
               chartCurve.getAllSeries().setColor(new Color(178, 34, 34));
            }
            if (tempCntr == 2) {
               chartCurve.getAllSeries().setColor(new Color(69, 177, 194));
            }
            if (tempCntr == 3) {
               chartCurve.getAllSeries().setColor(new Color(118, 200, 44));
            }
            if (tempCntr == 4) {
               chartCurve.getAllSeries().setColor(new Color(112, 219, 219));
            }
            if (tempCntr == 5) {
               chartCurve.getAllSeries().setColor(new Color(205, 96, 144));
            }
            if (tempCntr == 6) {
               chartCurve.getAllSeries().setColor(java.awt.Color.BLUE);
            }
            if (tempCntr == 7) {
               chartCurve.getAllSeries().setColor(java.awt.Color.BLUE);
            }
            if (tempCntr == 8) {
               chartCurve.getAllSeries().setColor(java.awt.Color.BLUE);
            }
            if (tempCntr == 9) {
               chartCurve.getAllSeries().setColor(java.awt.Color.BLUE);
            }
         } else if (setColor.equalsIgnoreCase("1")) {
            for (int cntr = 0; cntr < colors.length; cntr++) {
               colors[cntr] = xMLFileReader.getValueByNode("/ChartApperance.xml", "S" + (cntr + 1));
            }

            for (int cntr = 0; cntr < 40; cntr++) {

               if (tempCntr == cntr) {
                  rgb = colors[cntr];
                  rgbColor = rgb.split(",");
                  chartCurve.getAllSeries().setColor(
                           new Color(Integer.parseInt(rgbColor[0]), Integer.parseInt(rgbColor[1]), Integer
                                    .parseInt(rgbColor[2])));
               }
            }

         }
      }
      //

      catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }

   }

   public void setLegendBoxPosition (ChartServer ChartLegendBox) {
      String condForLegend = "";
      condForLegend = xMLFileReader.getValueByNode("/ChartApperance.xml", "CUSTOM_LEGEND");
      if (condForLegend.equalsIgnoreCase("1")) {
         try {

            String position = xMLFileReader.getValueByNode("/ChartApperance.xml", "POSITION_LEG");
            // String height = xMLFileReader.getValueByNode("/ChartApperance.xml", "HEIGHT_LEGENDBOX");
            // String width = xMLFileReader.getValueByNode("/ChartApperance.xml", "WIDTH_LEGENDBOX");
            // int hInt = Integer.parseInt(height);
            // int wInt = Integer.parseInt(width);
            // ChartLegendBox.getLegendBox().setContentLayout(ContentLayout.CENTER);
            ChartLegendBox.getLegendBox().setDock(DockArea.valueOf(position));
            EnumSet<LegendBoxStyles> legendStyles = ChartLegendBox.getLegendBox().getStyle();
            legendStyles.add(LegendBoxStyles.WORDBREAK);
            ChartLegendBox.getLegendBox().setStyle(legendStyles);
            // ChartLegendBox.getLegendBox().setHeight(hInt);
            // ChartLegendBox.getLegendBox().setWidth(wInt);

         } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
         }
      } else {
         // ChartLegendBox.getLegendBox().setContentLayout(ContentLayout.CENTER);
         ChartLegendBox.getLegendBox().setDock(DockArea.BOTTOM);
      }
   }

   public String[] getMeasureOnY1Y2Axis () {

      ConvertionForChart convertionForChart = new ConvertionForChart();
      ArrayList avgAList = convertionForChart.avgAL;

      ArrayList diffAList = new ArrayList();
      String axisArry[] = new String[avgAList.size()];
      boolean yFlag = false;
      for (int cntr1 = 0; cntr1 < avgAList.size(); cntr1++) {
         if (avgAList.size() > 1) {
            if (cntr1 < avgAList.size() - 1) {
               diffAList.add(Double.parseDouble(avgAList.get(cntr1).toString())
                        - Math.abs(Double.parseDouble(avgAList.get(cntr1 + 1).toString())));
            }
         } else {
            diffAList.add(Double.parseDouble(avgAList.get(0).toString()));
         }
         // some considerable range of negative series to show on the chart else ignore.
         if (Double.parseDouble(avgAList.get(cntr1).toString()) <= -50) {
            yFlag = true;
         }
      }

      double maxDiff = (((Double) (diffAList.get(0))).doubleValue());

      for (int cntr = 0; cntr < diffAList.size(); cntr++) {
         if (((Double) (diffAList.get(cntr))).doubleValue() > maxDiff) {
            maxDiff = ((Double) (diffAList.get(cntr))).doubleValue();
         }
      }

      for (int cnt = 0; cnt < avgAList.size(); cnt++) {
         if (avgAList.size() == 1) {
            axisArry[cnt] = "Y1";
         } else {
            if (((Double) (avgAList.get(cnt))).doubleValue() > maxDiff && !yFlag) {
               axisArry[cnt] = "Y1";
            } else { // same as above criteria
               if (Double.parseDouble(avgAList.get(cnt).toString()) <= -50) {
                  axisArry[cnt] = "Y2_dummy";
               } else {
                  axisArry[cnt] = "Y2";
               }
               y2AxisPlottingFlag = true;
            }
         }
      }
      if (log.isDebugEnabled()) {
         for (String axis : axisArry) {
            log.debug("Axis:: " + axis);
         }
      }
      return axisArry;
   }

   public void setBasicPropertiesForCharts (ChartServer chartServer) {
      // set the grid lines
      chartServer.getAxisY().getGrids().getMajor().setVisible(true); // as its the default axis to be plotted
      chartServer.getAxisY().setPosition(AxisPosition.NEAR);
      chartServer.getAxisY2().getGrids().getMajor().setVisible(false);
      /*
       * if(y2AxisPlottingFlag){ y2AxisPlottingFlag = false; // to initialize for cluster type charts. }else{
       */
      chartServer.getAxisY2().setVisible(false);
      chartServer.getAxisY2().setPosition(AxisPosition.NEAR);
      // }
      chartServer.getAxisX().getGrids().getMajor().setVisible(false);

      // set only major tickers by setting minor tickers width to 0 we are hiding it.
      chartServer.getAxisY().getGrids().getMinor().setWidth(0);
      chartServer.getAxisY2().getGrids().getMinor().setWidth(0);

      // set property for legend boxes
      chartServer.getLegendBox().setFont(
               new Font(chartMetaInfo.getLEGEND().getFONTSTYLE(), java.awt.Font.PLAIN, chartMetaInfo.getLEGEND()
                        .getFONTSIZE()));
      chartServer.getLegendBox().setPlotAreaOnly(false);
      chartServer.getLegendBox().setBorder(DockBorder.NONE);
      chartServer.getLegendBox().setContentLayout(ContentLayout.NEAR);
      LegendItemAttributes item = new LegendItemAttributes();
      item.setInverted(true);
      chartServer.getLegendBox().getItemAttributes().set(chartServer.getSeries(), item);
      setLegendBoxPosition(chartServer);

      // set background color
      chartServer.setBackground(new SolidBackground());
      chartServer.setBackColor(Color.WHITE);
      chartServer.setPlotAreaColor(Color.WHITE);

      // set Axis Style properties
      EnumSet<AxisStyles> styles = chartServer.getAxisY2().getStyle();
      styles.add(AxisStyles.LEFT_ALIGNED);
      styles.add(AxisStyles.AUTO_MINOR_STEP);
      styles.add(AxisStyles.AUTO_SCALE);
      chartServer.getAxisY2().setStyle(styles);

      styles = chartServer.getAxisX().getStyle();
      styles.add(AxisStyles.ALLOW_HALF);
      styles.add(AxisStyles.AUTO_LABEL_LAYOUT);
      styles.add(AxisStyles.ROUND_STEP);
      chartServer.getAxisX().setStyle(styles);
   }

   public void setPropertiesForSingleImageChart (ChartServer chartCurve, String chartTitle) {
      int minNoOfRows = chartMetaInfo.getPROPERTIESFORSINGLEIMAGECHART().getMINNOOFROWS();
      setBasicPropertiesForCharts(chartCurve);
      chartCurve.setToolTipFormat("%s %l: %v");
      chartCurve.getAxisX().setStaggered(true);
      chartCurve.getTitles().add(new TitleDockable(""));
      setChartTitle(chartCurve, chartTitle);
      chartCurve.setBorder((new SimpleBorder(SimpleBorderType.NONE)));

      chartCurve.getToolBar().setVisible(false);
      chartCurve.getImageSettings().setInteractive(false);
      chartCurve.getLegendBox().setVisible(true);
      LegendItemAttributes item = new LegendItemAttributes();
      item.setInverted(false);
      chartCurve.getLegendBox().getItemAttributes().set(chartCurve.getSeries(), item);

      chartCurve.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
      chartCurve.setFont(new java.awt.Font(chartMetaInfo.getPROPERTIESFORSINGLEIMAGECHART().getFONTSTYLE(),
               java.awt.Font.PLAIN, chartMetaInfo.getPROPERTIESFORSINGLEIMAGECHART().getFONTSIZE()));
      chartCurve.setAxesStyle(AxesStyle.MATH);

      int chartWidth = getChartWidth("NORMAL");
      int chartHeight = getChartHeight(chartWidth);
      chartCurve.setWidth(chartWidth);
      chartCurve.setHeight(chartHeight);

      int chartTitleLenght = chartCurve.getTitles().get(1).getText().length();
      boolean showTitle = ((int) (chartWidth * .1775) > chartTitleLenght) ? true : false;
      if (!showTitle) {
         chartCurve.getTitles().get(1).setText("");
         chartCurve.getPlotAreaMargin().setTop(3);
      }
      chartCurve.getLegendBox().setWidth(chartWidth);
      if (ConvertionForChart.noOfRows < minNoOfRows) {
         chartCurve.getAxisX().setStaggered(false);
      }
   }

   public ChartAppearanceMeta getChartMetaInfo () {
      return chartMetaInfo;
   }
}
