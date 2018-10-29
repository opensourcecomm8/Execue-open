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


package com.execue.report.view.chartfx.tx.impl;

import java.awt.Color;
import java.awt.Font;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.core.common.bean.reports.view.data.ChartAppearanceMeta;
import com.execue.exception.LocalizedViewTxServiceException;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.ReportViewTxServiceException;
import com.execue.report.configuration.service.IPresentationConfigurationService;
import com.execue.report.view.chartfx.tx.IChartFxReportViewTxService;
import com.execue.rules.bean.RuleParameters;
import com.execue.rules.exception.RuleException;
import com.execue.rules.service.IRuleService;
import com.softwarefx.StringAlignment;
import com.softwarefx.chartfx.server.AxesStyle;
import com.softwarefx.chartfx.server.AxisFormat;
import com.softwarefx.chartfx.server.AxisPosition;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.ContentLayout;
import com.softwarefx.chartfx.server.DockArea;
import com.softwarefx.chartfx.server.DockBorder;
import com.softwarefx.chartfx.server.LegendItemAttributes;
import com.softwarefx.chartfx.server.SeriesAttributes;
import com.softwarefx.chartfx.server.TickMark;
import com.softwarefx.chartfx.server.TitleCollection;
import com.softwarefx.chartfx.server.TitleDockable;
import com.softwarefx.chartfx.server.adornments.SolidBackground;

public abstract class ChartFxChartViewTxService implements IChartFxReportViewTxService {

   protected ChartAppearanceMeta             chartMetaInfo;
   private IRuleService                      ruleService;
   private IPresentationConfigurationService presentationConfigurationService;

   protected void setChartSeriesColors (ChartServer chartServer) throws LocalizedViewTxServiceException {
      try {
         String rgb = "";
         String colors[] = new String[40];
         String rgbColor[] = new String[3];
         int noOfSeries = 0;
         if (chartMetaInfo.getBARLINECHARTCOLOR().isUSECUSTOMCOLORS()) {
            colors = StringUtils.split(chartMetaInfo.getBARLINECHARTCOLOR().getCOLORCODESERIES(),
                     ReportingConstants.SEMI_COLON);

            noOfSeries = chartServer.getData().getSeries();
            for (int cntr = 0; cntr < noOfSeries; cntr++) {
               if (cntr < colors.length) {
                  rgb = colors[cntr];
                  rgbColor = rgb.split(ReportingConstants.COMMA);
                  SeriesAttributes series;
                  series = chartServer.getSeries().get(cntr);
                  series.setColor(new Color(Integer.parseInt(rgbColor[0]), Integer.parseInt(rgbColor[1]), Integer
                           .parseInt(rgbColor[2])));
               }
            }
         }
      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   protected void setTitleProperties (ChartServer chartServer) throws LocalizedViewTxServiceException {
      try {

         // Set the main title position which is at zero index
         String titlePosition = chartMetaInfo.getCHARTTITLE().getPOSITION();
         TitleCollection titles = chartServer.getTitles();
         if (titles.size() == 0) {
            TitleDockable titleDockable = new TitleDockable("");
            titles.add(titleDockable);
         }

         if (titlePosition.equalsIgnoreCase(chartMetaInfo.getCHARTTITLEPOTISION().getPOSITIONLEFT())) {
            chartServer.getTitles().get(0).setDock(DockArea.LEFT);
         } else if (titlePosition.equalsIgnoreCase(chartMetaInfo.getCHARTTITLEPOTISION().getPOSITIONRIGHT())) {
            chartServer.getTitles().get(0).setDock(DockArea.RIGHT);
         } else if (titlePosition.equalsIgnoreCase(chartMetaInfo.getCHARTTITLEPOTISION().getPOSITIONTOP())) {
            chartServer.getTitles().get(0).setDock(DockArea.TOP);
         } else if (titlePosition.equalsIgnoreCase(chartMetaInfo.getCHARTTITLEPOTISION().getPOSITIONBOTTOM())) {
            chartServer.getTitles().get(0).setDock(DockArea.BOTTOM);
         }

         // Set the other properties like alignment, font family, size, color, etc for all the titles 
         String titleAlign = chartMetaInfo.getCHARTTITLE().getALIGNMENT();
         String fontFamily = chartMetaInfo.getCHARTTITLE().getFONTFAMILY();
         int fontSize = Integer.parseInt(chartMetaInfo.getCHARTTITLE().getFONTSIZE());
         String fontColor = chartMetaInfo.getCHARTTITLE().getFONTCOLOR();
         String colorArr[] = fontColor.split(ReportingConstants.COMMA);

         for (TitleDockable titleDockable : chartServer.getTitles()) {

            if (titleAlign.equalsIgnoreCase(chartMetaInfo.getCHARTTITLEALIGNMENT().getALIGNCENTER())) {
               titleDockable.setAlignment(StringAlignment.CENTER);
            } else if (titleAlign.equalsIgnoreCase(chartMetaInfo.getCHARTTITLEALIGNMENT().getALIGNRIGHT())) {
               titleDockable.setAlignment(StringAlignment.FAR);
            } else if (titleAlign.equalsIgnoreCase(chartMetaInfo.getCHARTTITLEALIGNMENT().getALIGNLEFT())) {
               titleDockable.setAlignment(StringAlignment.NEAR);
            }

            titleDockable.setFont(new Font(fontFamily, Font.BOLD, fontSize));

            titleDockable.setTextColor(new Color(Integer.parseInt(colorArr[0]), Integer.parseInt(colorArr[1]), Integer
                     .parseInt(colorArr[2])));
         }

      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   protected void setLegendBoxProps (ChartServer chartServer) throws LocalizedViewTxServiceException {
      try {
         // set legend box labels
         int xData = chartServer.getData().getSeries();
         String fontStyle = chartMetaInfo.getLEGEND().getFONTSTYLE();
         int fontSize = chartMetaInfo.getLEGEND().getFONTSIZE();
         for (int cntr = 0; cntr < xData; cntr++) {
            chartServer.getSeries().get(cntr).setText(
                     chartServer.getSeries().get(cntr).toString().replace(ReportingConstants.UNDERSCORE,
                              ReportingConstants.SPACE));
         }
         chartServer.getLegendBox().setFont(new Font(fontStyle, Font.PLAIN, fontSize));
         chartServer.getLegendBox().setPlotAreaOnly(false);
         chartServer.getLegendBox().setBorder(DockBorder.NONE);
         chartServer.getLegendBox().setContentLayout(ContentLayout.NEAR);

         // disable the inverse property for legend display order
         LegendItemAttributes item = new LegendItemAttributes();
         item.setInverted(true);
         chartServer.getLegendBox().getItemAttributes().set(chartServer.getSeries(), item);

         // set legend box position
         if (Boolean.parseBoolean(chartMetaInfo.getLEGEND().getUSECUSTOMLEGEND())) {
            try {

               String position = chartMetaInfo.getLEGEND().getPOSITIONLEG();
               chartServer.getLegendBox().setContentLayout(ContentLayout.NEAR);
               chartServer.getLegendBox().setDock(DockArea.valueOf(position));
            } catch (Exception e) {
               throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION,
                        e);
            }
         } else {
            chartServer.getLegendBox().setContentLayout(ContentLayout.NEAR);
            chartServer.getLegendBox().setDock(DockArea.BOTTOM);
         }
      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   protected void setSeriesAxesAllocation (ChartServer chartCurve) throws LocalizedViewTxServiceException {
      try {
         for (int alCntr = 0; alCntr < chartCurve.getData().getSeries(); alCntr++) {
            SeriesAttributes series = chartCurve.getSeries().get(alCntr);
            // default all the series to Y-axis; The child will override as per need
            series.setAxisY(chartCurve.getAxisY());
         }
      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   protected int getChartWidth (ChartServer chartServer, String chartType) throws LocalizedViewTxServiceException {
      try {
         // TODO -SS- Enable the code below once the interactive charts are back.
         /*
          * int width = 0; final int noOfRows = chartServer.getData().getPoints(); final int widthFromXml =
          * chartMetaInfo.getLAYOUT().getWIDTH(); final int minWidthFromXml = chartMetaInfo.getLAYOUT().getMINWIDTH();
          * if (noOfRows < 4) { width = noOfRows * ((int) (((double) widthFromXml) * 0.25)); } else if (4 < noOfRows &&
          * noOfRows < 20) { width = widthFromXml; } else { width = widthFromXml; chartServer.setZoom(true);
          * chartServer.getAxisX().setAutoScroll(true); } if (width < minWidthFromXml) { width = minWidthFromXml; }
          * return width;
          */

         int width = 0;
         int widthFromXml = 0;
         int noOfRows = chartServer.getData().getPoints();
         int noOfSeries = chartServer.getSeries().size();
         widthFromXml = chartMetaInfo.getLAYOUT().getWIDTH();
         int minWidth = chartMetaInfo.getLAYOUT().getMINWIDTH();
         int minNoOfRows = chartMetaInfo.getCHARTWIDTH().getMINNOOFROWS();
         int maxNoOfRows = chartMetaInfo.getCHARTWIDTH().getMAXNOOFROWS();
         int seriesCount = chartMetaInfo.getCHARTWIDTH().getSERIESCOUNT();

         if (!chartType.equalsIgnoreCase(chartMetaInfo.getLAYOUT().getCHARTTYPECLUSTER())) {
            if (noOfRows < minNoOfRows) {
               width = noOfRows * (int) (widthFromXml * 0.25);
            } else if (minNoOfRows < noOfRows && noOfRows < maxNoOfRows) {
               width = widthFromXml;
            } else {
               width = noOfRows * (int) (widthFromXml * 0.034);
            }// for across bar and lines new rule to have the max width from xml in case of large data points
            if (chartType.equalsIgnoreCase("7") || chartType.equalsIgnoreCase("8")) {
               if (noOfRows > maxNoOfRows) {
                  width = widthFromXml;
               }
            }
            if (width < minWidth) {
               width = minWidth;
            }
         } else {
            if (noOfRows < minNoOfRows) {
               width = (int) (widthFromXml * 0.40);
            } else {
               width = (int) (widthFromXml * 0.55);
            }
         }
         if (noOfSeries > seriesCount && width <= minWidth) {
            double variable = 0;
            variable = noOfSeries * 0.12;
            if (variable > 1.6) {
               variable = 1.6;
            }

            width = (int) (width * variable);
         }
         return width;
      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   protected int getChartHeight (int chartWidth) throws LocalizedViewTxServiceException {
      try {
         int chartHeight = 0;

         // maintain the aspect ration of 4:3 (nearly) by dividing the width by 1.35416 which is near to 4:3 aspect
         // ratio
         int minWidth = chartMetaInfo.getLAYOUT().getMINWIDTH();
         chartHeight = (int) (chartWidth / 1.35416);
         if (chartHeight > minWidth) {
            return 500;
         } else {
            return chartHeight;
         }
      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   public void prepareViewData (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      try {
         // start off with initializing the chartMetaInfo object.
         // For this to work all the sub classes must call super for prepareViewData.
         ChartServer chartServer = (ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY);
         initializeChartMetaInfo();

         String fontFamily = "";
         int fontSize = 0;

         fontFamily = chartMetaInfo.getCHARTTITLE().getFONTFAMILY();
         fontSize = Integer.parseInt(chartMetaInfo.getCHARTTITLE().getFONTSIZE());

         // set the most generic properties those are applicable to all charts
         // specific charts can choose not to use this by overriding
         int chartWidth = getChartWidth(chartServer, chartMetaInfo.getLAYOUT().getCHARTTYPEDEFAULT());
         int chartHeight = getChartHeight(chartWidth);
         chartServer.setWidth(chartWidth);
         chartServer.setHeight(chartHeight);
         chartServer.getLegendBox().setWidth(chartWidth);
         chartServer.getAllSeries().getLine().setWidth(chartMetaInfo.getSERIESPROPS().getLINETHICKNESS());
         chartServer.getAllSeries().setMarkerSize(chartMetaInfo.getSERIESPROPS().getPOINTSIZE());
         chartServer.getAxisY().getDataFormat().setCustomFormat(chartMetaInfo.getSERIESPROPS().getYAXISFORMAT());
         chartServer.getAxisY2().getDataFormat().setCustomFormat(chartMetaInfo.getSERIESPROPS().getYAXISFORMAT());
         // chartServer.getAxisY2().getDataFormat().setCustomFormat("#,###.##");

         chartServer.getToolBar().setVisible(chartMetaInfo.getLAYOUT().isISTOOLBARVISBLE());

         chartServer.getImageSettings().setInteractive(chartMetaInfo.getLAYOUT().isIMAGEINTERACTIVE());
         if (!chartMetaInfo.getLAYOUT().isYAXISMINORTICKERVISIBLE()) {
            chartServer.getAxisY().getGrids().getMinor().setTickMark(TickMark.NONE);
         }
         if (!chartMetaInfo.getLAYOUT().isY2AXISMINORTICKERVISIBLE()) {
            chartServer.getAxisY2().getGrids().getMinor().setTickMark(TickMark.NONE);
         }

         // set axes angles, labels, grids, visibility
         chartServer.getAxisX().setLabelAngle(chartMetaInfo.getLAYOUT().getXAXISLABELANGLE());
         chartServer.getAxisX().getGrids().getMajor().setVisible(chartMetaInfo.getLAYOUT().isXAXISGRIDVISIBLE());
         chartServer.getAxisY().getGrids().getMajor().setVisible(chartMetaInfo.getLAYOUT().isYAXISGRIDVISIBLE());
         chartServer.getAxisY2().getGrids().getMajor().setVisible(chartMetaInfo.getLAYOUT().isY2AXISGRIDVISIBLE());
         chartServer.getAxisY().getLabelsFormat().setFormat(AxisFormat.NUMBER);
         chartServer.getAxisY().getTitle().setFont(new Font(fontFamily, Font.PLAIN, fontSize));
         chartServer.getAxisY2().getLabelsFormat().setFormat(AxisFormat.NUMBER);
         chartServer.getAxisY2().getTitle().setFont(new Font(fontFamily, Font.PLAIN, fontSize));
         chartServer.getAxisY2().setVisible(false);
         chartServer.getAxisY2().setPosition(AxisPosition.NEAR);
         chartServer.setAxesStyle(AxesStyle.MATH);

         // set colors
         String colorString = "";
         String xAxesColor[] = new String[3];
         String yAxesColor[] = new String[3];
         chartServer.setAxesStyle(AxesStyle.MATH);
         colorString = chartMetaInfo.getAXISCOLOR().getXAXESCOLOR();
         xAxesColor = colorString.split(ReportingConstants.COMMA);
         chartServer.getAxisY().getLine().setColor(
                  new Color(Integer.parseInt(xAxesColor[0]), Integer.parseInt(xAxesColor[1]), Integer
                           .parseInt(xAxesColor[2])));
         chartServer.getAxisY2().getLine().setColor(
                  new Color(Integer.parseInt(xAxesColor[0]), Integer.parseInt(xAxesColor[1]), Integer
                           .parseInt(xAxesColor[2])));
         colorString = chartMetaInfo.getAXISCOLOR().getYAXESCOLOR();
         yAxesColor = colorString.split(ReportingConstants.COMMA);
         chartServer.getAxisX().getLine().setColor(
                  new Color(Integer.parseInt(yAxesColor[0]), Integer.parseInt(yAxesColor[1]), Integer
                           .parseInt(yAxesColor[2])));

         chartServer.setBackground(new SolidBackground());
         String[] backgroundRGB = StringUtils.split(chartMetaInfo.getLAYOUT().getBACKGROUNDCOLOR(),
                  ReportingConstants.COMMA);
         chartServer.setBackColor(new java.awt.Color(Integer.parseInt(backgroundRGB[0]), Integer
                  .parseInt(backgroundRGB[1]), Integer.parseInt(backgroundRGB[2]), 255));

         String[] plotAreaRGB = StringUtils.split(chartMetaInfo.getLAYOUT().getPLOTAREACOLOR(),
                  ReportingConstants.COMMA);
         chartServer.setPlotAreaColor(new java.awt.Color(Integer.parseInt(plotAreaRGB[0]), Integer
                  .parseInt(plotAreaRGB[1]), Integer.parseInt(plotAreaRGB[2]), 255));

         // set plot area limits
         chartServer.getPlotAreaMargin().setBottom(chartMetaInfo.getLAYOUT().getPLOTAREAMARGINBOTTOM());
         chartServer.getPlotAreaMargin().setTop(chartMetaInfo.getLAYOUT().getPLOTAREAMARGINTOP());
         chartServer.getPlotAreaMargin().setRight(chartMetaInfo.getLAYOUT().getPLOTAREAMARGINRIGHT());
         chartServer.getPlotAreaMargin().setLeft(chartMetaInfo.getLAYOUT().getPLOTAREAMARGINLEFT());

         // set tool tip format
         chartServer.setToolTipFormat(chartMetaInfo.getSERIESPROPS().getTOOLTIPFORMAT());

         // set foot note on legend titles
         String sourceString = "  Source: " + domainObject.get(ReportingConstants.CHART_SOURCE_KEY);
         String webSiteString = "  " + ReportingConstants.CHART_WEBSITE_ADDRESS;
         chartServer.getLegendBox().getTitles().add(new TitleDockable(sourceString));
         chartServer.getLegendBox().getTitles().get(0).setDock(DockArea.BOTTOM);
         chartServer.getLegendBox().getTitles().get(0).setAlignment(StringAlignment.NEAR);
         chartServer.getLegendBox().getTitles().get(0).setFont(
                  new Font(chartMetaInfo.getLEGEND().getFONTSTYLE(), Font.ITALIC, chartMetaInfo.getLEGEND()
                           .getTITLEFONTSIZE()));

         chartServer.getLegendBox().getTitles().add(new TitleDockable(webSiteString));
         chartServer.getLegendBox().getTitles().get(1).setDock(DockArea.BOTTOM);
         chartServer.getLegendBox().getTitles().get(1).setAlignment(StringAlignment.NEAR);
         chartServer.getLegendBox().getTitles().get(1).setFont(
                  new Font(chartMetaInfo.getLEGEND().getFONTSTYLE(), Font.ITALIC, chartMetaInfo.getLEGEND()
                           .getTITLEFONTSIZE()));
      } catch (Exception e) {
         throw new ReportViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   public void applyViewRules (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      try {
         RuleParameters ruleParams = new RuleParameters();
         ruleParams.addInput(domainObject.get(ReportingConstants.CHART_SERVER_KEY));
         ruleParams.addInput(domainObject.get(ReportingConstants.CHART_TITLE_KEY));
         try {
            getRuleService().executeRule(getPresentationConfigurationService().getRuleTitle(), ruleParams);
         } catch (RuleException e) {
            throw new ReportViewTxServiceException(e.getCode(), e);
         }
      } catch (Exception e) {
         throw new ReportViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   public void adjustChartWidthAndHeightOnLegendTextSize (ChartServer chartServer) throws ReportViewTxServiceException {
      int avgOfSeriesTextLength = 0;
      int chartWidth = chartServer.getWidth();
      int chartHeight = chartServer.getHeight();
      int minLimit = Integer.parseInt(getChartMetaInfo().getLEGEND().getSERIESTEXTMINLEN());
      int maxLimit = Integer.parseInt(getChartMetaInfo().getLEGEND().getSERIESTEXTMAXLEN());
      int i;
      boolean increaseWidth = false;
      boolean increaseHeight = false;
      for (i = 0; i < chartServer.getSeries().size(); i++) {
         int seriesTextLength = chartServer.getSeries().get(i).getText().length();
         avgOfSeriesTextLength += seriesTextLength;

         if (i + 1 == chartServer.getSeries().size()) {
            avgOfSeriesTextLength = avgOfSeriesTextLength / (i + 1); // avg of the length of the series text
            if (avgOfSeriesTextLength >= minLimit && avgOfSeriesTextLength <= maxLimit
                     || chartServer.getSeries().size() > 5) {
               // then it makes sense to increase the width of the chart, hence setting flag
               increaseWidth = true;
               increaseHeight = true;
            }
         }
      }
      // increase the chartHeight
      if (increaseHeight) {
         if (chartHeight < 500) {
            int j = i - 1;
            chartHeight += chartServer.getSeries().get(j).getText().length();
            chartHeight += (int) (chartHeight * 0.30);
         } else {
            chartHeight += (int) (chartHeight * 0.13);
         }
      }
      // algo to decide the position of the avgNumber with the chosen middle number from the config cap defined.
      if (increaseWidth) {
         int middleNumber = (maxLimit + minLimit) / 2;
         int lowerLimitAvg = (middleNumber + minLimit) / 2;
         int upperLimitAvg = (maxLimit + middleNumber) / 2;

         if (avgOfSeriesTextLength < middleNumber) {
            if (avgOfSeriesTextLength <= lowerLimitAvg) {
               // if less than middle increment width by 3%
               chartWidth += (int) (chartWidth * 0.03);

            } else if (avgOfSeriesTextLength > lowerLimitAvg) {
               // if near to middle increment width by 5%
               chartWidth += (int) (chartWidth * 0.05);
            }
         } else {
            // greater than middle number
            if (avgOfSeriesTextLength >= middleNumber && avgOfSeriesTextLength < upperLimitAvg) {
               // if near to middle increment width by 5%
               chartWidth += (int) (chartWidth * 0.05);
            } else if (avgOfSeriesTextLength > upperLimitAvg) {
               // if greater than middle increment width by 7%
               chartWidth += (int) (chartWidth * 0.07);
            }
         }
         chartServer.getLegendBox().setVisible(true);
         // chartServer.getLegendBox().setBorder(DockBorder.External);
         // chartServer.getLegendBox().setDock(DockArea.Bottom);
         chartServer.getLegendBox().setContentLayout(ContentLayout.SPREAD);
         chartServer.setWidth(chartWidth);
         chartServer.setHeight(chartHeight);
      }
   }

   private void initializeChartMetaInfo () {
      chartMetaInfo = getPresentationConfigurationService().getChartLayoutObject();
   }

   public ChartAppearanceMeta getChartMetaInfo () {
      return chartMetaInfo;
   }

   /**
    * @return the presentationConfigurationService
    */
   public IPresentationConfigurationService getPresentationConfigurationService () {
      return presentationConfigurationService;
   }

   /**
    * @param presentationConfigurationService
    *           the presentationConfigurationService to set
    */
   public void setPresentationConfigurationService (IPresentationConfigurationService presentationConfigurationService) {
      this.presentationConfigurationService = presentationConfigurationService;
   }

   /**
    * @return the ruleService
    */
   public IRuleService getRuleService () {
      return ruleService;
   }

   /**
    * @param ruleService
    *           the ruleService to set
    */
   public void setRuleService (IRuleService ruleService) {
      this.ruleService = ruleService;
   }
}
