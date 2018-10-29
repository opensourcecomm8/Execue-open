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


package com.execue.report.presentation.tx.structure.impl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.util.UniversalReportParser;
import com.execue.exception.ReportStructureTxServiceException;
import com.execue.report.presentation.tx.structure.IReportStructureTxService;

public class BasePivotStructureTxService implements IReportStructureTxService {

   private static final String valuesTag = "VALUES";
   private static final String dataTag   = "DATA";

   public String transformStructure (UniversalReport universalReport) throws ReportStructureTxServiceException {
      String transformedXMLData = UniversalReportParser.generateXML(universalReport);
      InputStream iStream = null;
      try {
         // logger.debug("Inside the denormalizeDataSection method...");
         iStream = new ByteArrayInputStream(UniversalReportParser.generateXML(universalReport).getBytes());
         // Create instance of DocumentBuilderFactory
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         // Get the DocumentBuilder
         DocumentBuilder docBuilder = factory.newDocumentBuilder();
         // Using existing XML Document
         Document document = docBuilder.parse(iStream);

         // get the values list
         NodeList values = document.getElementsByTagName(valuesTag);
         List<UniversalValue> aValues = new ArrayList<UniversalValue>();
         for (int iVal = 0; iVal < values.getLength(); iVal++) {
            Node value = values.item(iVal);
            NodeList children = value.getChildNodes();
            UniversalValue aValue = new UniversalValue();
            List<UniversalColumnData> aColDataList = new ArrayList<UniversalColumnData>();
            for (int i = 0; i < children.getLength(); i++) {
               Node coldata = children.item(i);
               if (Node.ELEMENT_NODE != coldata.getNodeType()) {
                  continue;
               }
               String name = "";
               String val = "";
               Element element = (Element) coldata;
               name = element.getAttribute("NAME");
               val = element.getAttribute("VALUE");
               UniversalColumnData aColData = new UniversalColumnData();
               aColData.setName(name);
               aColData.setValue(val);
               aColDataList.add(aColData);
            }
            aValue.setColumndata(aColDataList);
            aValues.add(aValue);
         }
         // remove the data tag from the original xml
         Element data = (Element) document.getElementsByTagName(dataTag).item(0);
         Element root = document.getDocumentElement();
         root.removeChild(data);

         // add the new data section
         if (aValues.size() > 0) {
            Element eData = document.createElement(dataTag);
            for (UniversalValue aVal : aValues) {
               Element eValues = document.createElement(valuesTag);
               for (UniversalColumnData colData : aVal.getColumndata()) {
                  Element eColData = document.createElement(colData.getName());
                  Text eColValue = document.createTextNode(colData.getValue());
                  eColData.appendChild(eColValue);
                  eValues.appendChild(eColData);
               }
               eData.appendChild(eValues);
            }
            root.appendChild(eData);
         }
         transformedXMLData = generateXML(document);
      } catch (SAXException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      }
      return transformedXMLData;
   }

   private static String generateXML (Document document) {
      String xmlData = "";
      try {
         // set up a transformer
         TransformerFactory transfac = TransformerFactory.newInstance();
         Transformer trans = transfac.newTransformer();
         trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         // create string from xml tree
         StringWriter sw = new StringWriter();
         StreamResult result = new StreamResult(sw);
         DOMSource source = new DOMSource(document);
         trans.transform(source, result);
         xmlData = sw.toString();
      } catch (TransformerConfigurationException e) {
         e.printStackTrace();
      } catch (TransformerException e) {
         e.printStackTrace();
      }
      return xmlData;
   }
}
