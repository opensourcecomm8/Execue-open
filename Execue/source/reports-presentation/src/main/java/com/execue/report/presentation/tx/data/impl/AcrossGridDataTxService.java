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


package com.execue.report.presentation.tx.data.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalMember;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.common.util.UniversalReportParser;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.ReportDataTxServiceException;

public class AcrossGridDataTxService extends GridDataTxService {

   @Override
   public void transformData (UniversalReport universalReport) throws ReportDataTxServiceException {
      try {
         // Process the default transformation
         super.transformData(universalReport);

         // Process the across and group by dimensions
         super.processAcrossReportData(universalReport);

         // Applying normalization on the universal object applicable only for across grids for performance.
         List<String> groupbyMemberList = new ArrayList<String>();
         int totalACRMembers = 0;
         for (UniversalColumn column : universalReport.getHeader().getColumns()) {
            if (null != column.getGroupby()) {
               for (UniversalMember member : column.getMembers()) {
                  groupbyMemberList.add(member.getDesc().trim());
               }
            }
            if (null != column.getAcross()) {
               totalACRMembers += column.getMembers().size();
            }
         }

         if (groupbyMemberList.size() * totalACRMembers > 0
                  && groupbyMemberList.size() * totalACRMembers != universalReport.getData().getValues().size()) {

            String universalReportXml = UniversalReportParser.generateXML(universalReport);
            universalReportXml = ExeCueXMLUtils.denormalizeDataSection(universalReportXml);

            // Build and get the DOM
            InputSource inputStream = new InputSource();
            inputStream.setCharacterStream(new StringReader(universalReportXml));

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);
            document.getDocumentElement().normalize();

            // Normalize the document as per the across grid layout
            ExeCueXMLUtils.normalizeDocument(document, groupbyMemberList);

            universalReportXml = ExeCueXMLUtils.generateXML(document);
            universalReportXml = ExeCueXMLUtils.normalizeDataSection(universalReportXml);

            // Transform the report xml and get the universal report
            UniversalReport processedReportData = UniversalReportParser.parseReport(universalReportXml);

            // Revert back to the default gird, if after the normalization the values are greater than nXn
            if (groupbyMemberList.size() * totalACRMembers > 0
                     && groupbyMemberList.size() * totalACRMembers < processedReportData.getData().getValues().size()) {
               throw new ReportDataTxServiceException(PresentationExceptionCodes.REVERT_TO_DEFAULT_GRID,
                        "Not Eligible for across type reports");
            }
            universalReport.setData(processedReportData.getData());
            universalReport.setHeader(processedReportData.getHeader());
         }
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }
}
