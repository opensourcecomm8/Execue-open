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


package com.execue.reporting.presentation.helper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.execue.core.common.bean.reports.prsntion.Scale;
import com.execue.core.common.bean.reports.prsntion.UniversalColumn;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalReport;
import com.execue.core.common.bean.reports.prsntion.UniversalReportData;
import com.execue.core.common.bean.reports.prsntion.UniversalReportHeader;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.common.util.UniversalReportParser;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.exception.PresentationExceptionCodes;
import com.execue.exception.UniversalDataTransformationException;
import com.execue.reporting.presentation.bean.AggregationColumn;
import com.execue.reporting.presentation.bean.AggregationColumnData;
import com.execue.reporting.presentation.bean.AggregationReport;
import com.execue.reporting.presentation.bean.AggregationReportData;
import com.execue.reporting.presentation.bean.AggregationReportHeader;
import com.execue.reporting.presentation.bean.AggregationValue;
import com.execue.reporting.presentation.bean.ReportPresentationConstants;

public class PresentationTransformHelper {

   private static Logger logger = Logger.getLogger(PresentationTransformHelper.class);

   public static AggregationReport applyTransformationOnUniversalXml (String xmlData, boolean applyNumericTransformation)
            throws UniversalDataTransformationException {

      // normalize the xml dimension data values with the corresponding dimension column header member description
      String transformedXMLData = transformXMLDataDescription(xmlData);

      // normalize the modified xml data
      String normalizedXMLData = normalizeDataSection(transformedXMLData);

      // generate the AggregationReport object out of the modified xml
      AggregationReport aggregationReport = AggregationReportParser.parseReport(normalizedXMLData);

      // applying numeric transformation which changes the header text based on the unit of the data
      if (applyNumericTransformation) {
         applyNumericTransformation(aggregationReport);
      }

      return aggregationReport;
   }

   public static UniversalReport getUniversalXMLTranformedToUniversalReport (String xmlData)
            throws UniversalDataTransformationException {

      // normalize the xml dimension data values with the corresponding dimension column header member description
      String transformedXMLData = transformXMLDataDescription(xmlData);

      // normalize the modified xml data
      String normalizedXMLData = normalizeDataSection(transformedXMLData);

      // generate the AggregationReport object out of the modified xml
      UniversalReport universalReport = UniversalReportParser.parseReport(normalizedXMLData);

      // applying numeric transformation which changes the header text based on the unit of the data
      applyNumericTransformation(universalReport);

      return universalReport;
   }

   public static String transformXMLDataForCSVReport (String xmlData) {
      StringBuilder csvString = new StringBuilder();
      InputStream iStream = null;
      try {
         // logger.debug("Inside the CSVTransform method...");
         iStream = new ByteArrayInputStream(xmlData.getBytes());
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         Document document = documentBuilder.parse(iStream);
         document.getDocumentElement().normalize();
         // add header to the csvString
         csvString.append(getHeader(ReportPresentationConstants.columnTag, ReportPresentationConstants.descTag,
                  document));
         csvString.append(getRow(ReportPresentationConstants.valuesTag, document));
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (iStream != null) {
               iStream.close();
            }
         } catch (IOException ioe) {
            ioe.printStackTrace();
         }
      }
      return csvString.toString();
   }

   private static String getHeader (String headerNode, String childNode, Document document) {
      NodeList idNodeList = document.getElementsByTagName(childNode);
      StringBuilder rowValues = new StringBuilder();
      for (int j = 0; j < idNodeList.getLength(); j++) {
         Element columnElement = (Element) idNodeList.item(j).getChildNodes();
         if (columnElement.hasChildNodes() && columnElement.getParentNode().getNodeName().equals(headerNode)) {
            rowValues.append(ReportPresentationConstants.doubleQuote + columnElement.getTextContent()
                     + ReportPresentationConstants.doubleQuote + ReportPresentationConstants.commaSeparator);
         }
      }
      return rowValues.toString().substring(0, rowValues.length() - 1);
   }

   private static String getRow (String childNode, Document document) {
      NodeList idNodeList = document.getElementsByTagName(childNode);
      String rowValues = "";
      StringBuilder newString = new StringBuilder();
      for (int i = 0; i < idNodeList.getLength(); i++) {
         // adding a new line character
         newString.append(ReportPresentationConstants.newLine);
         NodeList columnElementList = idNodeList.item(i).getChildNodes();
         for (int j = 0; j < columnElementList.getLength(); j++) {
            rowValues = rowValues + columnElementList.item(j).getTextContent()
                     + ReportPresentationConstants.commaSeparator;
         }
         newString.append(rowValues.substring(0, rowValues.length() - 1));
         rowValues = "";
      }
      return newString.toString();
   }

   /**
    * In this method all the member lookup values of all the dimensions are replaced by their respective lookup descriptions.
    */
   public static String transformXMLDataDescription (String xmlData) throws UniversalDataTransformationException {
      String newData = xmlData;
      try {
         // logger.debug("Inside the GridTransform method...\n" + newData);
         InputSource iSource = new InputSource(new ByteArrayInputStream(newData.getBytes()));
         iSource.setEncoding("ISO-8859-1");

         // Create and get the DocumentBuilder to parse the input xml and create the dom
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = factory.newDocumentBuilder();
         Document doc = docBuilder.parse(iSource);

         List<Integer> indices = getDimensionColumnIndices(doc);

         if (indices.size() > 0) {
            Map<String, Map<String, String>> membersMap = new HashMap<String, Map<String, String>>();

            // prepare the map of the members for the dimension columns
            for (Integer index : indices) {
               Element col = (Element) doc.getElementsByTagName(ReportPresentationConstants.columnTag).item(index);
               String id = col.getElementsByTagName(ReportPresentationConstants.idTag).item(0).getTextContent();
               String val = "";
               String desc = "";
               Element members = (Element) col.getElementsByTagName(ReportPresentationConstants.membersTag).item(0);
               // Check if the members tag is available - if no then bye-pass this process
               if (members != null) {
                  NodeList memberList = members.getElementsByTagName(ReportPresentationConstants.memberTag);
                  Map<String, String> memberMap = new HashMap<String, String>();
                  for (int i = 0; i < memberList.getLength(); i++) {
                     Element member = (Element) memberList.item(i);
                     val = member.getElementsByTagName(ReportPresentationConstants.valueTag).item(0).getTextContent();
                     desc = member.getElementsByTagName(ReportPresentationConstants.descTag).item(0).getTextContent();
                     memberMap.put(val, desc);
                  }
                  membersMap.put(id, memberMap);
               }
            }

            // Modify the data section-replace the values with descriptions
            Element data = (Element) doc.getElementsByTagName(ReportPresentationConstants.dataTag).item(0);
            NodeList values = data.getElementsByTagName(ReportPresentationConstants.valuesTag);
            if (membersMap.size() > 0) {
               for (int i = 0; i < values.getLength(); i++) {
                  NodeList dataNodes = values.item(i).getChildNodes();
                  for (int j = 0; j < dataNodes.getLength(); j++) {
                     Node node = dataNodes.item(j);
                     if (membersMap.containsKey(node.getNodeName())) {
                        Map<String, String> memberMap = membersMap.get(node.getNodeName());
                        String value = memberMap.get(node.getTextContent());
                        node.setTextContent(value);
                     }
                  }
               }
            }

            // Generate the xml from the modified document
            newData = generateXML(doc);
         }
      } catch (SAXException e) {
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      } catch (IOException e) {
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      } catch (ParserConfigurationException e) {
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      }
      return newData;
   }

   public static String generateXML (Document document) {
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

   public static List<Integer> getDimensionColumnIndices (Document document) {

      NodeList columns = document.getElementsByTagName(ReportPresentationConstants.columnTag);

      List<Integer> dimensionColumnIndices = new ArrayList<Integer>();

      for (int columnIndex = 0; columnIndex < columns.getLength(); columnIndex++) {
         Element columnElement = (Element) columns.item(columnIndex);
         String ctypeValue = columnElement.getElementsByTagName(ReportPresentationConstants.ctypeTag).item(0)
                  .getTextContent();
         String plotAsValue = columnElement.getElementsByTagName(ReportPresentationConstants.plotAsTag).item(0)
                  .getTextContent();
         if (ReportPresentationConstants.dimensionTag.equalsIgnoreCase(ctypeValue)
                  || ReportPresentationConstants.dimensionTag.equals(plotAsValue)) {
            dimensionColumnIndices.add(columnIndex);
         }
      }

      // logger.debug("Inside the getDimensionColumnIndices method : " + dimColumns.size());
      return dimensionColumnIndices;
   }

   public static List<Integer> getDimensionColumnIndices (List<UniversalColumn> columns) {

      List<Integer> dimensionColumnIndices = new ArrayList<Integer>();

      int columnIndex = 0;
      for (UniversalColumn universalColumn : columns) {
         String ctypeValue = universalColumn.getCtype();
         String plotAsValue = universalColumn.getPlotAs();
         if (ReportPresentationConstants.dimensionTag.equalsIgnoreCase(ctypeValue)
                  || ReportPresentationConstants.dimensionTag.equals(plotAsValue)) {
            dimensionColumnIndices.add(columnIndex);
         }
         columnIndex++;
      }

      // logger.debug("Inside the getDimensionColumnIndices method : " + dimColumns.size());
      return dimensionColumnIndices;
   }

   /**
    * This method transforms the column data into a different format to make it xstream ready.<br>
    * Old format : <c0>101</c0><br>
    * New format : <columndata name="c0" value="101"/>
    */
   public static String normalizeDataSection (String xmlData) throws UniversalDataTransformationException {
      String toBeTransformedXMLData = xmlData;
      String transformedXMLData = xmlData;
      InputSource iSource = null;
      try {
         // logger.debug("Inside the normalizeDataSection method...\n" +
         // toBeTransformedXMLData);
         iSource = new InputSource(new ByteArrayInputStream(toBeTransformedXMLData.getBytes()));
         iSource.setEncoding("ISO-8859-1");
         // Create instance of DocumentBuilderFactory
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         // Get the DocumentBuilder
         DocumentBuilder docBuilder = factory.newDocumentBuilder();
         // Using existing XML Document
         Document document = docBuilder.parse(iSource);

         // get the values list
         NodeList values = document.getElementsByTagName(ReportPresentationConstants.valuesTag);
         List<AggregationValue> newColMap = new ArrayList<AggregationValue>();
         for (int iVal = 0; iVal < values.getLength(); iVal++) {
            Node value = values.item(iVal);
            NodeList children = value.getChildNodes();
            AggregationValue aValue = new AggregationValue();
            List<AggregationColumnData> aColDataList = new ArrayList<AggregationColumnData>();
            for (int i = 0; i < children.getLength(); i++) {
               AggregationColumnData aColData = new AggregationColumnData();
               String name = children.item(i).getNodeName();
               String val = children.item(i).getTextContent();
               aColData.setName(name);
               aColData.setValue(val);
               aColDataList.add(aColData);
            }
            aValue.setColumndata(aColDataList);
            newColMap.add(aValue);
         }

         // remove the data tag from the original xml
         Element data = (Element) document.getElementsByTagName(ReportPresentationConstants.dataTag).item(0);
         Element root = document.getDocumentElement();
         root.removeChild(data);

         // add the new data section
         if (newColMap.size() > 0) {
            Element eData = document.createElement(ReportPresentationConstants.dataTag);
            for (AggregationValue aVal : newColMap) {
               Element eValues = document.createElement(ReportPresentationConstants.valuesTag);
               for (AggregationColumnData colData : aVal.getColumndata()) {
                  String cTypeData = "";
                  NodeList column = document.getElementsByTagName(ReportPresentationConstants.columnTag);
                  for (int colCntr = 0; colCntr < column.getLength(); colCntr++) {
                     Node columnData = column.item(colCntr);
                     // comparing the id's - column's id value = value's i'th node name
                     if (columnData.getChildNodes().item(0).getTextContent().equalsIgnoreCase(colData.getName())) {
                        NodeList childNodes = columnData.getChildNodes();
                        for (int childCntr = 0; childCntr < childNodes.getLength(); childCntr++) {
                           String ctype = childNodes.item(childCntr).getNodeName();
                           if (ctype.equalsIgnoreCase(ReportPresentationConstants.ctypeTag)) {
                              cTypeData = childNodes.item(childCntr).getTextContent();
                              convertToNumber(colData, cTypeData);
                           }
                        }
                     }
                  }

                  Element eColData = document.createElement(ReportPresentationConstants.columnDataTag);
                  eColData.setAttribute(ReportPresentationConstants.nameAttr, colData.getName());
                  eColData.setAttribute(ReportPresentationConstants.valueAttr, colData.getValue());
                  eColData.setAttribute(ReportPresentationConstants.ctypeTag, cTypeData);
                  eValues.appendChild(eColData);
               }
               eData.appendChild(eValues);
            }
            root.appendChild(eData);
         }
         transformedXMLData = generateXML(document);
      } catch (SAXException e) {
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      } catch (IOException e) {
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      } catch (ParserConfigurationException e) {
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      }
      return transformedXMLData;
   }

   /**
    * This method transforms the column data into a different format conforming to the reports.<br>
    * Old format : <columndata name="c0" value="101"/><br>
    * New format : <c0>101</c0>
    */
   public static String denormalizeDataSection (String xmlData) {
      String toBeTransformedXMLData = xmlData;
      String transformedXMLData = xmlData;
      InputSource iSource = null;
      try {
         // logger.debug("Inside the denormalizeDataSection method...");
         iSource = new InputSource(new ByteArrayInputStream(toBeTransformedXMLData.getBytes()));
         iSource.setEncoding("ISO-8859-1");
         // Create instance of DocumentBuilderFactory
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         // Get the DocumentBuilder
         DocumentBuilder docBuilder = factory.newDocumentBuilder();
         // Using existing XML Document
         Document document = docBuilder.parse(iSource);

         // get the values list
         NodeList values = document.getElementsByTagName(ReportPresentationConstants.valuesTag);
         List<AggregationValue> aValues = new ArrayList<AggregationValue>();
         for (int iVal = 0; iVal < values.getLength(); iVal++) {
            Node value = values.item(iVal);
            NodeList children = value.getChildNodes();
            AggregationValue aValue = new AggregationValue();
            List<AggregationColumnData> aColDataList = new ArrayList<AggregationColumnData>();
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
               AggregationColumnData aColData = new AggregationColumnData();
               aColData.setName(name);
               aColData.setValue(val);
               aColDataList.add(aColData);
            }
            aValue.setColumndata(aColDataList);
            aValues.add(aValue);
         }
         // remove the data tag from the original xml
         Element data = (Element) document.getElementsByTagName(ReportPresentationConstants.dataTag).item(0);
         Element root = document.getDocumentElement();
         root.removeChild(data);

         // add the new data section
         if (aValues.size() > 0) {
            Element eData = document.createElement(ReportPresentationConstants.dataTag);
            for (AggregationValue aVal : aValues) {
               Element eValues = document.createElement(ReportPresentationConstants.valuesTag);
               for (AggregationColumnData colData : aVal.getColumndata()) {
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

   /**
    * Determines the scaling factor based on the values. Finds the display type unit and updates the header description
    * to reflect the correct display type and the scaling factor
    */
   public static AggregationReport applyNumericTransformation (AggregationReport aggregationReport) {
      try {
         AggregationReportData reportData = aggregationReport.getData();
         AggregationReportHeader reportHeader = aggregationReport.getHeader();
         for (AggregationColumn reportHeaderColumn : reportHeader.getColumns()) {
            if (isColumnEligibleForScaling(reportHeaderColumn.getCtype(), reportHeaderColumn.getPlotAs())) {
               // Get the column unit factor
               String colUnitFactor = reportHeaderColumn.getDataFormat();
               List<String> columnValues = new ArrayList<String>();
               List<String> scaledColumnValues = new ArrayList<String>();
               for (AggregationValue reportValue : reportData.getValues()) {
                  for (AggregationColumnData reportColumn : reportValue.getColumndata()) {
                     if (reportHeaderColumn.getId().equalsIgnoreCase(reportColumn.getName())) {
                        columnValues.add(reportColumn.getValue());
                     }
                  }
               }
               // Identify the unit factor of the column
               int factor = ExeCueXMLUtils.getFactor(colUnitFactor);
               // Deduce the scale
               Scale scale = ExeCueXMLUtils.getScale(columnValues);

               scaledColumnValues = ExeCueXMLUtils.getScaleFormattedValues(columnValues, scale);

               scale.setSuffix(ExeCueXMLUtils.getSuffixForFactor(factor + scale.getScaleTobeApplied(), scale
                        .getSuffix()));

               // revise the scale only if the avg log value is greater than 0
               // if (scale.getAvgLog() > 0.0) {
               // Scale revisedScale = getRevisedScale(factor + scale.getAvgLog());
               // scale.setSuffix(revisedScale.getSuffix());
               // } else {
               // // fall back to the original data format - use factor to get the suffix
               // String suffix = getSuffixByFactor(factor);
               // scale.setSuffix(suffix);
               // }

               // reset the values after scaling.
               int index = 0;
               for (AggregationValue reportValue : reportData.getValues()) {
                  for (AggregationColumnData reportColumnData : reportValue.getColumndata()) {
                     if (reportHeaderColumn.getId().equalsIgnoreCase(reportColumnData.getName())) {
                        reportColumnData.setValue(scaledColumnValues.get(index++));
                     }
                  }
               }
               // Build the tokens to append to the header
               List<String> tokens = new ArrayList<String>();
               String scaleUnit = scale.getSuffix();
               String dtype = "";
               if ("Currency".equalsIgnoreCase(reportHeaderColumn.getUnitType())) {
                  // TODO: defaulting it to $, need to get it from the user preferences
                  dtype = "$";
               } else if ("Percentage".equalsIgnoreCase(reportHeaderColumn.getUnitType())) {
                  dtype = "%";
               }
               if (ExecueCoreUtil.isNotEmpty(dtype)) {
                  tokens.add(dtype);
               }
               if (ExecueCoreUtil.isNotEmpty(scaleUnit)) {
                  tokens.add(scaleUnit);
               }
               if (tokens.size() > 0) {
                  StringBuilder header = new StringBuilder();
                  String term = "";
                  for (String token : tokens) {
                     term = term + token + ", ";
                  }
                  term = term.substring(0, term.length() - 2);
                  header.append(reportHeaderColumn.getDesc()).append(" ").append("(").append(term).append(")");
                  reportHeaderColumn.setDesc(header.toString());
               }
            } else if (reportHeaderColumn.getCtype().equalsIgnoreCase("DIMENSION")) {
               List<String> tokens = new ArrayList<String>();
               String dtype = "";
               if ("Currency".equalsIgnoreCase(reportHeaderColumn.getUnitType())) {
                  // TODO: defaulting it to $, need to get it from the user preferences
                  dtype = "$";
               }
               if (ExecueCoreUtil.isNotEmpty(dtype)) {
                  tokens.add(dtype);
               }
               if (ExecueCoreUtil.isNotEmpty(reportHeaderColumn.getDataFormat())) {
                  String suffixByFactor = ExeCueXMLUtils.getSuffixByFactor(ExeCueXMLUtils.getFactor(reportHeaderColumn
                           .getDataFormat()));
                  if (ExecueCoreUtil.isNotEmpty(suffixByFactor)) {
                     tokens.add(suffixByFactor);
                  }
               }
               if (tokens.size() > 0) {
                  StringBuilder header = new StringBuilder();
                  String term = "";
                  for (String token : tokens) {
                     term = term + token + ", ";
                  }
                  term = term.substring(0, term.length() - 2);

                  header.append(reportHeaderColumn.getDesc()).append(" ").append("(").append(term).append(")");
                  reportHeaderColumn.setDesc(header.toString());
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return aggregationReport;
   }

   private static boolean isColumnEligibleForScaling (String ctype, String plotAs) {
      boolean eligible = false;
      if (ctype.equalsIgnoreCase("MEASURE")) {
         eligible = true;
      } else if (ctype.equalsIgnoreCase("ID") && "measure".equalsIgnoreCase(plotAs)) {
         eligible = true;
      }
      return eligible;
   }

   /**
    * Determines the scaling factor based on the values. Finds the display type unit and updates the header description
    * to reflect the correct display type and the scaling factor for new reporting framework.
    */
   public static UniversalReport applyNumericTransformation (UniversalReport universalReport)
            throws UniversalDataTransformationException {
      UniversalReportData reportData = universalReport.getData();
      try {
         reportData.getValues().size();
      } catch (NullPointerException e) {
         // means the data is missing for this universal xml
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      }
      try {
         UniversalReportHeader reportHeader = universalReport.getHeader();
         for (UniversalColumn reportHeaderColumn : reportHeader.getColumns()) {
            if (isColumnEligibleForScaling(reportHeaderColumn.getCtype(), reportHeaderColumn.getPlotAs())) {
               // Get the column unit factor
               String colUnitFactor = reportHeaderColumn.getDataFormat();
               List<String> columnValues = new ArrayList<String>();
               List<String> scaledColumnValues = new ArrayList<String>();
               for (UniversalValue reportValue : reportData.getValues()) {
                  for (UniversalColumnData reportColumn : reportValue.getColumndata()) {
                     if (reportHeaderColumn.getId().equalsIgnoreCase(reportColumn.getName())) {
                        columnValues.add(reportColumn.getValue());
                     }
                  }
               }
               // Identify the unit factor of the column
               int factor = ExeCueXMLUtils.getFactor(colUnitFactor);
               // Deduce the scale
               Scale scale = ExeCueXMLUtils.getScale(columnValues);

               scaledColumnValues = ExeCueXMLUtils.getScaleFormattedValues(columnValues, scale);

               scale.setSuffix(ExeCueXMLUtils.getSuffixForFactor(factor + scale.getScaleTobeApplied(), scale
                        .getSuffix()));

               // reset the values after scaling.
               int index = 0;
               for (UniversalValue reportValue : reportData.getValues()) {
                  for (UniversalColumnData reportColumnData : reportValue.getColumndata()) {
                     if (reportHeaderColumn.getId().equalsIgnoreCase(reportColumnData.getName())) {
                        reportColumnData.setValue(scaledColumnValues.get(index++));
                     }
                  }
               }
               // Build the tokens to append to the header
               List<String> tokens = new ArrayList<String>();
               String scaleUnit = scale.getSuffix();
               String dtype = "";
               // if ("Currency".equalsIgnoreCase(reportHeaderColumn.getDtype())) {
               // // TODO: defaulting it to $, need to get it from the user preferences
               // dtype = "$";
               // }

               // -JM- 13-SEP-2010 : modified the logic to add currency unit to the report column header
               // using unit type instead of dtype for the check
               if ("CURRENCY".equalsIgnoreCase(reportHeaderColumn.getUnitType())) {
                  dtype = "$";
               } else if ("Percentage".equalsIgnoreCase(reportHeaderColumn.getUnitType())) {
                  dtype = "%";
               }
               if (logger.isDebugEnabled()) {
                  logger.debug("Dtype : " + dtype);
               }
               if (ExecueCoreUtil.isNotEmpty(dtype)) {
                  tokens.add(dtype);
               }
               if (ExecueCoreUtil.isNotEmpty(scaleUnit)) {
                  tokens.add(scaleUnit);
               }
               if (tokens.size() > 0) {
                  StringBuilder header = new StringBuilder();
                  String term = "";
                  for (String token : tokens) {
                     term = term + token + ", ";
                  }
                  term = term.substring(0, term.length() - 2);
                  header.append(reportHeaderColumn.getDesc()).append(" ").append("(").append(term).append(")");
                  reportHeaderColumn.setDesc(header.toString());
               }
            } else if (reportHeaderColumn.getCtype().equalsIgnoreCase("DIMENSION")) {
               List<String> tokens = new ArrayList<String>();
               String dtype = "";
               if ("Currency".equalsIgnoreCase(reportHeaderColumn.getUnitType())) {
                  // TODO: defaulting it to $, need to get it from the user preferences
                  dtype = "$";
               }
               if (ExecueCoreUtil.isNotEmpty(dtype)) {
                  tokens.add(dtype);
               }
               if (logger.isDebugEnabled()) {
                  logger.debug("Dtype : " + dtype);
               }
               if (ExecueCoreUtil.isNotEmpty(reportHeaderColumn.getDataFormat())) {
                  if (ExecueCoreUtil.isNotEmpty(reportHeaderColumn.getDataFormat())) {
                     String suffixByFactor = ExeCueXMLUtils.getSuffixByFactor(ExeCueXMLUtils
                              .getFactor(reportHeaderColumn.getDataFormat()));
                     if (ExecueCoreUtil.isNotEmpty(suffixByFactor)) {
                        tokens.add(suffixByFactor);
                     }
                  }
               }
               if (tokens.size() > 0) {
                  StringBuilder header = new StringBuilder();
                  String term = "";
                  for (String token : tokens) {
                     term = term + token + ", ";
                  }
                  term = term.substring(0, term.length() - 2);

                  header.append(reportHeaderColumn.getDesc()).append(" ").append("(").append(term).append(")");
                  reportHeaderColumn.setDesc(header.toString());
               }
            }
         }
      } catch (Exception e) {
         throw new UniversalDataTransformationException(PresentationExceptionCodes.DEFAULT_PRESENTAION_REPORTS_FAILED,
                  e);
      }
      return universalReport;
   }

   /**
    * This method will remove all the white spaces that occur in the XML string and format so that the entire XML is in
    * a single line
    */
   public static String stripWhiteSpacesBetweenTags (String inputString) {
      inputString = inputString.trim();
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < inputString.length(); i++) {
         char currCharacter = inputString.charAt(i);
         stringBuilder.append(currCharacter);
         if (currCharacter == ReportPresentationConstants.CLOSING_TAG_SYMBOL) {
            String intermediateString = findTagContentBetweenTags(inputString, i);
            if (!isAllWhiteSpaces(intermediateString)) {
               stringBuilder.append(intermediateString);
            }
            i += intermediateString.length();
         }
      }
      return stringBuilder.toString();
   }

   private static String findTagContentBetweenTags (String inputString, int currIndex) {
      String currentString = inputString.substring(currIndex + 1, inputString.length());
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < currentString.length(); i++) {
         char currCharacter = currentString.charAt(i);
         stringBuilder.append(currCharacter);
         if (currCharacter == ReportPresentationConstants.OPENING_TAG_SYMBOL) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            break;
         }
      }
      return stringBuilder.toString();
   }

   private static boolean isAllWhiteSpaces (String intermediateString) {
      boolean isAllWhiteSpaces = true;
      for (int i = 0; i < intermediateString.length(); i++) {
         if (!Character.isWhitespace(intermediateString.charAt(i))) {
            isAllWhiteSpaces = false;
            break;
         }
      }
      return isAllWhiteSpaces;
   }

   public static void normalizeDocument (Document doc, NodeList colNodeList, int totalGRPMembers) {

      // re-modify the data object for handling missing values.
      List<String> acrossMemberList = new ArrayList<String>();
      String idTag = "";
      for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
         Node fstColNode = colNodeList.item(cntr);
         Element colElement = (Element) fstColNode;

         if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList valueChNodeList = colElement.getElementsByTagName(ReportPresentationConstants.acrossTag);
            if (valueChNodeList.item(0) != null) {
               NodeList idNodeList = colElement.getElementsByTagName(ReportPresentationConstants.CTYPE_ID);
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idTag = idChNodeList.item(0).getNodeValue().toString();
               NodeList memberList = colElement.getElementsByTagName(ReportPresentationConstants.memberTag);
               for (int memCount = 0; memCount < memberList.getLength(); memCount++) {
                  NodeList valNodeList = memberList.item(memCount).getChildNodes();
                  Element valElement = (Element) valNodeList.item(1);
                  NodeList valChNodeList = valElement.getChildNodes();
                  acrossMemberList.add(valChNodeList.item(0).getNodeValue().toString());
               }// done with getting the list of members for
               // across columns.
            }
         }
      }
      // parsing the list of values for missing elements
      NodeList valuesNodeList = doc.getElementsByTagName(ReportPresentationConstants.valuesTag);
      Node parentNode = valuesNodeList.item(0).getParentNode();
      int valueNodeCounter = 0;
      String grpDemensionName = valuesNodeList.item(0).getFirstChild().getTextContent();
      String currentGrpDemensionName = "";
      Node lastNode = valuesNodeList.item(valuesNodeList.getLength() - 1);

      for (int i = 0; i < totalGRPMembers; i++) {
         for (int j = 0; j < acrossMemberList.size(); j++, valueNodeCounter++) {

            Node currentNode = valuesNodeList.item(valueNodeCounter);
            String acrossMemberValue = acrossMemberList.get(j).toString();

            if (currentNode != null && currentNode.getNodeType() == Node.ELEMENT_NODE) {

               Element rowElement = (Element) currentNode;
               NodeList cTagList = rowElement.getElementsByTagName(idTag);
               Element cTagElement = (Element) cTagList.item(0);
               NodeList cTagChNodeList = cTagElement.getChildNodes();
               Node cChNodeList = cTagChNodeList.item(0);
               currentGrpDemensionName = currentNode.getFirstChild().getTextContent();
               Node clonedNode = null;
               if (j == 0) {
                  grpDemensionName = currentGrpDemensionName;
               }
               if (!acrossMemberValue.equals(cChNodeList.getNodeValue())
                        && currentGrpDemensionName.equals(grpDemensionName)) {
                  if (valueNodeCounter % acrossMemberList.size() == 0) {
                     clonedNode = cloneNode(currentNode, acrossMemberValue);
                  } else {
                     clonedNode = cloneNode(currentNode.getPreviousSibling(), acrossMemberValue);
                     grpDemensionName = clonedNode.getFirstChild().getTextContent();
                  }
                  parentNode.insertBefore(clonedNode, currentNode);
               } else if (!currentGrpDemensionName.equals(grpDemensionName)) {
                  clonedNode = cloneNode(currentNode.getPreviousSibling(), acrossMemberValue);
                  if (j == acrossMemberList.size() - 1) {
                     grpDemensionName = currentGrpDemensionName;
                  }
                  parentNode.insertBefore(clonedNode, currentNode);
               }
            } else {
               Node clonedNode = cloneNode(lastNode, acrossMemberValue);
               parentNode.appendChild(clonedNode);
            }
         }
      }
      // end of re-modify
   }

   private static Node cloneNode (Node nodeToBeCloned, String acrossMemberValue) {
      Node node = nodeToBeCloned.cloneNode(true);
      NodeList nodeList = node.getChildNodes();
      for (int childNode = 0; childNode < nodeList.getLength(); childNode++) {
         if (childNode == 1) {
            nodeList.item(childNode).setTextContent(acrossMemberValue);
         } else if (childNode > 1) {
            nodeList.item(childNode).setTextContent(ReportPresentationConstants.NULL);
         }
      }
      return node;
   }

   /**
    * This method reads the AggregationColumnData value and checks if it's ctype is measure and contains commas then it
    * replaces them and converts it into a proper number and modifies the passed object.
    * 
    * @param colData
    * @param ctype
    */
   private static void convertToNumber (AggregationColumnData colData, String ctype) {
      if (ctype.equalsIgnoreCase(ReportPresentationConstants.measureTag)) {
         String columValue = colData.getValue();
         if (!NumberUtils.isNumber(columValue)
                  && !(ReportPresentationConstants.NULL.equalsIgnoreCase(columValue) || ReportPresentationConstants.NULL_STRING
                           .equalsIgnoreCase(columValue))) {
            colData.setValue(NumberUtils.createNumber(columValue.trim().replaceAll(",", "")) + "");
         }
      }
   }

   public static NumberFormat getNumberFormatWithTwoDigitPrecision (int maxScale) {
      NumberFormat numberFormat = new DecimalFormat("#,###.##");
      numberFormat.setMaximumFractionDigits(maxScale);
      return numberFormat;
   }

}
