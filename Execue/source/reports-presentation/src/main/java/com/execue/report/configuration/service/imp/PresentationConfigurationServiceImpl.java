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


package com.execue.report.configuration.service.imp;

import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.execue.core.common.bean.reports.view.data.ChartAppearanceMeta;
import com.execue.core.common.bean.reports.view.data.GridAppearanceMeta;
import com.execue.core.configuration.IConfiguration;
import com.execue.core.exception.ConfigurationException;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.report.configuration.service.IPresentationConfigurationService;

/**
 * This class define the configuration constants and methods that will be further used by presentation/reporting
 * services
 * 
 * @author Jitendra
 * @version 1.0
 * @since 13/10/11
 */
public class PresentationConfigurationServiceImpl implements IPresentationConfigurationService {

   private String              CHART_META_INFO           = "reports.CHARTAPPEARANCE";
   private String              GRID_META_INFO            = "reports.GRIDAPPEARANCE";
   private String              FIRST_SERIES_ON_LEFT_AXIS = "reports.rules.left-axis-rule";
   private String              SAME_COLORED_MEASURE      = "reports.rules.same-colored-measures";
   private String              SHOW_DISTINCT_LEGENDS     = "reports.rules.distinct-legends";
   private String              RULE_TITLE                = "reports.rules.title";
   private String              BAR_SIZE                  = "reports.rules.bar-size";
   
   private String              REVERT_TO_DEFAULT_GRID    = "reports.configuration.revert-to-default-grid";
   private String              SAVE_REPORTS_TO_DB        = "reports.configuration.save-reports-in-db";
   private String              IMAGE_RETRIEVAL_ACTION    = "reports.configuration.image-retrieval-action";
   
   private String              IMAGE_LOCATION            = "reports.configuration.image-location";
   
   private ChartAppearanceMeta chartFxMeta;
   private GridAppearanceMeta  gridFxMeta;
   private IConfiguration      rpConfiguration;
   private IConfiguration      rpDBConfiguration;

   public ChartAppearanceMeta getChartLayoutObject () {
      return chartFxMeta;
   }

   public GridAppearanceMeta getGridLayoutObject () {
      return gridFxMeta;
   }

   /**
    * @return the rpConfiguration
    */
   public IConfiguration getRpConfiguration () {
      return rpConfiguration;
   }

   /**
    * @param rpConfiguration
    *           the rpConfiguration to set
    */
   public void setRpConfiguration (IConfiguration rpConfiguration) {
      this.rpConfiguration = rpConfiguration;
   }

   public void loadChartFxMeta () throws ConfigurationException {
      Document document = getRpConfiguration().getDocument();
      // int beginIndex =
      // PresentationConfigurationConstants.CHART_META_INFO.length() - 15;
      int beginIndex = 8;
      String searchKey = CHART_META_INFO.substring(beginIndex);
      NodeList nodeList = document.getElementsByTagName(searchKey);
      Node chartMetaNode = nodeList.item(0);
      String chartMetaXml = "";

      try {
         // set up a transformer
         TransformerFactory transfac = TransformerFactory.newInstance();
         Transformer trans = transfac.newTransformer();
         trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         // create string from xml tree
         StringWriter sw = new StringWriter();
         StreamResult result = new StreamResult(sw);
         DOMSource source = new DOMSource(chartMetaNode);
         trans.transform(source, result);
         chartMetaXml = sw.toString();
      } catch (TransformerConfigurationException e) {
         e.printStackTrace();
         throw new ConfigurationException(PresentationExceptionCodes.CONFIGURATION_EXCEPTION,
                  "Issue in presentation configuration", e);
      } catch (TransformerException e) {
         e.printStackTrace();
         throw new ConfigurationException(PresentationExceptionCodes.CONFIGURATION_EXCEPTION,
                  "Issue in transformation for loading presentation configuration", e);
      }
      // a necessary step to revert the changes made to the xml for able to
      // read the escaped commas.
      chartFxMeta = (ChartAppearanceMeta) ChartAppearanceMeta.getXStreamInstance().fromXML(
               chartMetaXml.replace("\\,", ","));
   }

   public void loadGridFxMeta () throws ConfigurationException {
      Document document = getRpConfiguration().getDocument();
      // int beginIndex =
      // PresentationConfigurationConstants.GRID_META_INFO.length() - 15;
      int beginIndex = 8;
      String searchKey = GRID_META_INFO.substring(beginIndex);
      NodeList nodeList = document.getElementsByTagName(searchKey);
      Node chartMetaNode = nodeList.item(0);
      String gridMetaXml = "";
      try {
         // set up a transformer
         TransformerFactory transfac = TransformerFactory.newInstance();
         Transformer trans = transfac.newTransformer();
         trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         // create string from xml tree
         StringWriter sw = new StringWriter();
         StreamResult result = new StreamResult(sw);
         DOMSource source = new DOMSource(chartMetaNode);
         trans.transform(source, result);
         gridMetaXml = sw.toString();
      } catch (TransformerConfigurationException e) {
         e.printStackTrace();
         throw new ConfigurationException(PresentationExceptionCodes.CONFIGURATION_EXCEPTION,
                  "Issue in presentation configuration", e);
      } catch (TransformerException e) {
         e.printStackTrace();
         throw new ConfigurationException(PresentationExceptionCodes.CONFIGURATION_EXCEPTION,
                  "Issue in transformation for loading presentation configuration", e);
      }
      // a necessary step to revert the changes made to the xml for able to
      // read the escaped commas.
      gridFxMeta = (GridAppearanceMeta) GridAppearanceMeta.getXStreamInstance()
               .fromXML(gridMetaXml.replace("\\,", ","));

   }

   @Override
   public String getBarSize () {
      return getRpConfiguration().getProperty(BAR_SIZE);
   }

   @Override
   public String getDistinctLegends () {
      return getRpConfiguration().getProperty(SHOW_DISTINCT_LEGENDS);
   }

   @Override
   public String getFirstSeriesOnLeftAxis () {
      return getRpConfiguration().getProperty(FIRST_SERIES_ON_LEFT_AXIS);
   }

   @Override
   public String getImageRetrievalAction () {
      return getRpConfiguration().getProperty(IMAGE_RETRIEVAL_ACTION);
   }

   @Override
   public String getRuleTitle () {
      return getRpConfiguration().getProperty(RULE_TITLE);
   }

   @Override
   public String getSameColoredMeasure () {
      return getRpConfiguration().getProperty(SAME_COLORED_MEASURE);
   }

   @Override
   public boolean isRevertToDefaultGriid () {
      return getRpConfiguration().getBoolean(REVERT_TO_DEFAULT_GRID);
   }

   @Override
   public boolean isSaveReportsToDB () {
      return getRpConfiguration().getBoolean(SAVE_REPORTS_TO_DB);
   }

   @Override
   public Integer getAxisLabelMaxLength () {
      return getRpDBConfiguration().getInt(AXIS_LABEL_MAX_LENGTH);
   }

   @Override
   public String getImageLocation () {
      return getRpConfiguration().getProperty(IMAGE_LOCATION);
   }

   public IConfiguration getRpDBConfiguration () {
      return rpDBConfiguration;
   }

   public void setRpDBConfiguration (IConfiguration rpDBConfiguration) {
      this.rpDBConfiguration = rpDBConfiguration;
   }

}
