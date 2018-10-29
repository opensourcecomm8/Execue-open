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
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.reports.ReportingConstants;
import com.execue.exception.LocalizedViewTxServiceException;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.ReportViewTxServiceException;
import com.softwarefx.chartfx.server.ChartServer;
import com.softwarefx.chartfx.server.Gallery;
import com.softwarefx.chartfx.server.PointAttributes;
import com.softwarefx.chartfx.server.galleries.Pie;

public class ChartFxPieChartViewTxService extends ChartFxChartViewTxService {

   @Override
   protected int getChartWidth (ChartServer chartServer, String chartType) throws LocalizedViewTxServiceException {
      try {
         int width = 0;
         int widthFromXml = 0;
         int noOfSeries = chartServer.getSeries().size();
         widthFromXml = chartMetaInfo.getLAYOUT().getWIDTH();
         if (noOfSeries == 2) {
            width = widthFromXml + 100;
         } else if (noOfSeries > 2) {
            width = widthFromXml + 250;
         } else {
            width = widthFromXml;
         }
         return width;
      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   @Override
   public void prepareViewData (Map<String, Object> domainObject) throws ReportViewTxServiceException {
      try {
         super.prepareViewData(domainObject);

         // ValueFormat valueFormate = (ValueFormat) domainObject.get(ReportingConstants.CHART_SERVER_KEY);
         ChartServer chartServer = (ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY);

         // set the properties related to Pie Chart
         chartServer.setGallery(Gallery.PIE);

         // 3D View
         chartServer.getView3D().setEnabled(chartMetaInfo.getPROPERTIESFORPIECHART().isVIEW3D());
         chartServer.getImageSettings().setInteractive(chartMetaInfo.getPROPERTIESFORPIECHART().isIMAGESETTINGS());

         // Show Labels in the chart

         chartServer.getAllSeries().getPointLabels().setVisible(
                  chartMetaInfo.getPROPERTIESFORPIECHART().isPOINTLABELS());

         // Lables Inside or Outside
         int numberOfPoints = 0;
         numberOfPoints = chartServer.getData().getPoints();
         if (numberOfPoints > 8) {
            Pie pie = (Pie) chartServer.getGalleryAttributes();
            // logic to show lables outside
            pie.setLabelsInside(chartMetaInfo.getPROPERTIESFORPIECHART().isLABELSINSIDE());
            // logi to remove showLine property
            // pie.setShowLines(chartMetaInfo.getPROPERTIESFORPIECHART().isSHOWLINE());
         }

         // Separate Slice
         // chartServer.getPoints().get(0).setSeparateSlice((short) 25);

         setChartSeriesColors((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
         setTitleProperties((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));
         setLegendBoxProps((ChartServer) domainObject.get(ReportingConstants.CHART_SERVER_KEY));

      } catch (Exception e) {
         throw new ReportViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }

   @Override
   protected void setChartSeriesColors (ChartServer chartServer) throws LocalizedViewTxServiceException {
      try {
         String rgb = "";
         String colors[] = new String[40];
         String rgbColor[] = new String[3];
         int numberOfPoints = 0;
         if (chartMetaInfo.getPIECHARTCOLOR().isUSECUSTOMCOLORS()) {
            colors = StringUtils.split(chartMetaInfo.getPIECHARTCOLOR().getCOLORCODESERIES(),
                     ReportingConstants.SEMI_COLON);

            numberOfPoints = chartServer.getData().getPoints();
            for (int counter = 0; counter < numberOfPoints; counter++) {
               rgb = colors[counter];
               rgbColor = rgb.split(ReportingConstants.COMMA);
               PointAttributes point = chartServer.getPoints().get(counter);
               point.setColor(new Color(Integer.parseInt(rgbColor[0]), Integer.parseInt(rgbColor[1]), Integer
                        .parseInt(rgbColor[2])));
            }
         }
      } catch (Exception e) {
         throw new LocalizedViewTxServiceException(PresentationExceptionCodes.DEFAULT_LOCALIZED_VIEWTX_EXCEPTION, e);
      }
   }
}
