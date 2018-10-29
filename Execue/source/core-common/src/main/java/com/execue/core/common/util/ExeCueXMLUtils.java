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


package com.execue.core.common.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.qi.QIBusinessTerm;
import com.execue.core.common.bean.qi.QICondition;
import com.execue.core.common.bean.qi.QIConditionLHS;
import com.execue.core.common.bean.qi.QIConditionRHS;
import com.execue.core.common.bean.qi.QIOrderBy;
import com.execue.core.common.bean.qi.QISelect;
import com.execue.core.common.bean.qi.QISubQuery;
import com.execue.core.common.bean.qi.QIValue;
import com.execue.core.common.bean.reports.Row;
import com.execue.core.common.bean.reports.prsntion.Scale;
import com.execue.core.common.bean.reports.prsntion.UniversalColumnData;
import com.execue.core.common.bean.reports.prsntion.UniversalValue;
import com.execue.core.common.bean.reports.view.ColumnData;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerRequestTransformException;
import com.execue.core.util.ExecueCoreUtil;
import com.thoughtworks.xstream.XStream;

public class ExeCueXMLUtils {

   private static final Logger logger             = Logger.getLogger(ExeCueXMLUtils.class);
   private static final String reportTypesTag     = "REPORTTYPES";
   private static final String columnTag          = "COLUMN";
   private static final String rowTag             = "ROW";
   private static final String acrossTag          = "ACROSS";
   private static final String groupTag           = "GROUPBY";
   private static final String idTag              = "ID";
   private static final String memberTag          = "MEMBER";
   private static final String valuesTag          = "VALUES";
   private static final String dataTag            = "DATA";
   private static final String columnDataTag      = "COLUMNDATA";
   private static final String nameAttr           = "NAME";
   private static final String valueAttr          = "VALUE";
   private static final String cTypeTag           = "CTYPE";
   private static final char   CLOSING_TAG_SYMBOL = '>';
   private static final char   OPENING_TAG_SYMBOL = '<';
   private static String       NULL               = "N/A";

   private ExeCueXMLUtils () {

   }

   private static Node cloneNode (Node nodeToBeCloned, String acrossMemberValue, String groupMemberValue,
            int acrossMemberPosition, int groupMemberPosition) {
      Node node = nodeToBeCloned.cloneNode(true);
      NodeList nodeList = node.getChildNodes();
      for (int childNodeCntr = 0; childNodeCntr < nodeList.getLength(); childNodeCntr++) {
         nodeList.item(childNodeCntr).setTextContent(NULL);
         nodeList.item(acrossMemberPosition).setTextContent(acrossMemberValue);
         nodeList.item(groupMemberPosition).setTextContent(groupMemberValue);
      }
      return node;
   }

   private static String findTagContentBetweenTags (String inputString, int currIndex) {
      String currentString = inputString.substring(currIndex + 1, inputString.length());
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < currentString.length(); i++) {
         char currCharacter = currentString.charAt(i);
         stringBuilder.append(currCharacter);
         if (currCharacter == OPENING_TAG_SYMBOL) {
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

   public static String generateXML (Document document) {
      String xmlData = "";
      try {
         // set up a transformer
         TransformerFactory transfac = TransformerFactory.newInstance();
         Transformer trans = transfac.newTransformer();
         trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
         trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
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

   public static String getXMLStringFromObject (Object object) {
      XStream xStream = new XStream();
      return xStream.toXML(object);
   }

   public static Object getObjectFromXMLString (String xmlString) {
      XStream xStream = new XStream();
      return xStream.fromXML(xmlString);
   }

   public static String getSelectedReportTypesFromXML (String path) {
      String reportType = "";
      try {
         path = path.substring(path.indexOf(reportTypesTag) + reportTypesTag.length() + 1);
         reportType = path.substring(0, (path.indexOf(reportTypesTag) - 2));
      } catch (Exception e) {
         e.printStackTrace();
      }
      return reportType;
   }

   public static void normalizeDocument (Document doc, List<String> groupbyMemberList) {

      NodeList colNodeList = doc.getElementsByTagName(columnTag);

      // re-modify the data object for handling missing values.
      List<String> acrossMemberList = new ArrayList<String>();
      String acrossIdNameTag = "";
      int acrossNodePosition = 0;
      int groupNodePosition = 0;
      for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
         Node fstColNode = colNodeList.item(cntr);
         Element colElement = (Element) fstColNode;

         if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
            NodeList acrossColNodeList = colElement.getElementsByTagName(acrossTag);
            if (acrossColNodeList.item(0) != null) {
               NodeList idNodeList = colElement.getElementsByTagName(idTag);
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               acrossIdNameTag = idChNodeList.item(0).getNodeValue().toString();

               acrossNodePosition = cntr;
               NodeList memberList = colElement.getElementsByTagName(memberTag);
               for (int memCount = 0; memCount < memberList.getLength(); memCount++) {
                  NodeList valNodeList = memberList.item(memCount).getChildNodes();
                  Element valElement = (Element) valNodeList.item(1);
                  NodeList valChNodeList = valElement.getChildNodes();
                  acrossMemberList.add(valChNodeList.item(0).getNodeValue().toString());
               }// done with getting the list of members for
               // across columns.
            }
            NodeList groupNodeList = colElement.getElementsByTagName(groupTag);
            if (groupNodeList.item(0) != null) {
               groupNodePosition = cntr;
            }
         }
      }

      // parsing the list of values for missing elements
      NodeList valuesNodeList = doc.getElementsByTagName(valuesTag);
      Node parentNode = valuesNodeList.item(0).getParentNode();
      String currentDimensionName = "";
      String seconDimensionName = "";
      Node lastNode = valuesNodeList.item(valuesNodeList.getLength() - 1);

      Map<String, Map<String, Node>> acrossMemberValueMap = new TreeMap<String, Map<String, Node>>();
      // get the list of existing values for across members, to be compared later with non-existing ones and sort
      // them
      for (int j = 0; j < acrossMemberList.size(); j++) {
         Map<String, Node> acrossMemberValueNodesSortedbyGrpMemberName = new TreeMap<String, Node>();
         for (int k = 0; k < valuesNodeList.getLength(); k++) {
            Node currentNode = valuesNodeList.item(k);
            NodeList valuesChildNodes = currentNode.getChildNodes();
            for (int valCntr = 0; valCntr < valuesChildNodes.getLength(); valCntr++) {
               Node node = valuesChildNodes.item(valCntr);
               if (("c" + acrossNodePosition).equalsIgnoreCase(node.getNodeName())) {
                  currentDimensionName = node.getTextContent().trim();
               }
               if (("c" + groupNodePosition).equalsIgnoreCase(node.getNodeName())) {
                  seconDimensionName = node.getTextContent().trim();
               }
            }
            // match the across dimension present in xml values with across members
            if (acrossMemberList.get(j).equalsIgnoreCase(currentDimensionName)) {
               acrossMemberValueNodesSortedbyGrpMemberName.put(seconDimensionName, currentNode);
            }
         }
         // put the list of found nodes for the current member value into the map
         acrossMemberValueMap.put(acrossMemberList.get(j), acrossMemberValueNodesSortedbyGrpMemberName);
      }
      Collections.sort(groupbyMemberList);
      for (int j = 0; j < acrossMemberList.size(); j++) {
         // process the map values to find out the missing elements based on sorted order
         Map<String, Node> acrossValueMap = acrossMemberValueMap.get(acrossMemberList.get(j));
         Set<String> keySet = new LinkedHashSet<String>(acrossValueMap.keySet());
         for (int i = 0, valueNodeCntr = 0; i < groupbyMemberList.size(); i++) {
            if (!keySet.contains(groupbyMemberList.get(i))) {
               // node is not present clone node accordingly
               Node clonedNode = null;
               if (valueNodeCntr < acrossValueMap.size()) {
                  Object[] keys = keySet.toArray();
                  String key = (String) keys[valueNodeCntr];
                  Node currentNode = acrossValueMap.get(key);
                  clonedNode = cloneNode(currentNode, acrossMemberList.get(j), groupbyMemberList.get(i),
                           acrossNodePosition, groupNodePosition);

                  parentNode.insertBefore(clonedNode, currentNode);
               } else {
                  clonedNode = cloneNode(lastNode, acrossMemberList.get(j), groupbyMemberList.get(i),
                           acrossNodePosition, groupNodePosition);
                  parentNode.appendChild(clonedNode);
               }
            } else {
               valueNodeCntr++;
            }
         }
      }

      // end of re-modify
   }

   /**
    * This method transforms the column data into a different format to make it xstream ready.<br>
    * Old format : &lt;c0&gt;101&lt;/c0&gt;<br>
    * New format : &lt;columndata name="c0" value="101"/&gt;
    */
   public static String normalizeDataSection (String xmlData) {
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
         NodeList values = document.getElementsByTagName(valuesTag);
         List<UniversalValue> newColMap = new ArrayList<UniversalValue>();
         for (int iVal = 0; iVal < values.getLength(); iVal++) {
            Node value = values.item(iVal);
            NodeList children = value.getChildNodes();
            UniversalValue aValue = new UniversalValue();
            List<UniversalColumnData> aColDataList = new ArrayList<UniversalColumnData>();
            for (int i = 0; i < children.getLength(); i++) {
               UniversalColumnData aColData = new UniversalColumnData();
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
         Element data = (Element) document.getElementsByTagName(dataTag).item(0);
         Element root = document.getDocumentElement();
         root.removeChild(data);

         // add the new data section
         if (newColMap.size() > 0) {
            Element eData = document.createElement(dataTag);
            for (UniversalValue aVal : newColMap) {
               Element eValues = document.createElement(valuesTag);
               for (UniversalColumnData colData : aVal.getColumndata()) {
                  String cTypeData = "";
                  NodeList column = document.getElementsByTagName(columnTag);
                  for (int colCntr = 0; colCntr < column.getLength(); colCntr++) {
                     Node columnData = column.item(colCntr);
                     if (columnData.getChildNodes().item(0).getTextContent().equalsIgnoreCase(colData.getName())) {
                        NodeList childNodes = columnData.getChildNodes();
                        for (int childCntr = 0; childCntr < childNodes.getLength(); childCntr++) {
                           String ctype = childNodes.item(childCntr).getNodeName();
                           if (ctype.equalsIgnoreCase(cTypeTag)) {
                              cTypeData = childNodes.item(childCntr).getTextContent();
                           }
                        }
                     }
                  }

                  Element eColData = document.createElement(columnDataTag);
                  eColData.setAttribute(nameAttr, colData.getName());
                  eColData.setAttribute(valueAttr, colData.getValue());
                  eColData.setAttribute(cTypeTag, cTypeData);
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
    * This method transforms the column data into a different format conforming to the reports.<br>
    * Old format : &lt;columndata name="c0" value="101"/&gt;<br>
    * New format : &lt;c0&gt;101&lt;/c0&gt;
    */
   public static String denormalizeDataSectionForChartFx (String xmlData) {
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
         NodeList values = document.getElementsByTagName(rowTag);
         List<Row> aValues = new ArrayList<Row>();
         // need to skip the first iteration as it's the root node for row elements
         for (int iVal = 1; iVal < values.getLength(); iVal++) {
            Node value = values.item(iVal);
            NodeList children = value.getChildNodes();
            Row aValue = new Row();
            List<ColumnData> aColDataList = new ArrayList<ColumnData>();
            for (int i = 0; i < children.getLength(); i++) {
               Node coldata = children.item(i);
               if (Node.ELEMENT_NODE != coldata.getNodeType()) {
                  continue;
               }
               String name = "";
               String val = "";
               Element element = (Element) coldata;
               name = element.getAttribute(nameAttr);
               val = element.getAttribute(valueAttr);
               ColumnData aColData = new ColumnData();
               aColData.setNAME(name);
               aColData.setVALUE(val);

               aColDataList.add(aColData);
            }
            aValue.setCOLUMNDATA(aColDataList);
            aValues.add(aValue);
         }
         // remove the row tag from the original xml
         Element data = (Element) document.getElementsByTagName(rowTag).item(0);
         Element root = document.getDocumentElement();
         root.removeChild(data);

         // add the new data section
         if (aValues.size() > 0) {
            for (Row aVal : aValues) {
               Element eData = document.createElement(rowTag);
               for (ColumnData colData : aVal.getCOLUMNDATA()) {
                  Attr eValues = document.createAttribute(colData.getNAME());
                  eValues.setValue(colData.getVALUE());
                  eData.setAttributeNode(eValues);
               }
               root.appendChild(eData);
            }
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
    * This method transforms the column data into a different format conforming to the reports.<br>
    * Old format : &lt;columndata name="c0" value="101"/&gt;<br>
    * New format : &lt;c0&gt;101&lt;/c0&gt;
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
               name = element.getAttribute(nameAttr);
               val = element.getAttribute(valueAttr);
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
         if (currCharacter == CLOSING_TAG_SYMBOL) {
            String intermediateString = findTagContentBetweenTags(inputString, i);
            if (!isAllWhiteSpaces(intermediateString)) {
               stringBuilder.append(intermediateString);
            }
            i += intermediateString.length();
         }
      }
      return stringBuilder.toString();
   }

   // private static Scale getScale (List<String> values) throws Exception {
   // double avgLog = 0.0;
   // double sumOfLog = 0.0;
   // int scaleToBeApplied = 0;
   // int precision = 0;
   // String suffix = "";
   // try {
   // for (String value : values) {
   // if (NumberUtils.isNumber(value)) {
   // double colValue = Double.parseDouble(value);
   // if (colValue != 0) {
   // sumOfLog = sumOfLog + (Math.log(Math.abs(colValue)) / 2.303);
   // }
   // }// TODO: -RG- this check is a must?
   // }
   // avgLog = sumOfLog / values.size();
   // avgLog = Math.round(avgLog);
   // if (avgLog < 0) {
   // scaleToBeApplied = (int) avgLog;
   // precision = 2;
   // suffix = "E" + (int) avgLog;
   // } else if (avgLog <= 4) {
   // scaleToBeApplied = 0;
   // precision = 0;
   // suffix = "";
   // } else if (avgLog <= 7) {
   // scaleToBeApplied = 3;
   // precision = 1;
   // suffix = "K";
   // } else if (avgLog <= 10) {
   // scaleToBeApplied = 6;
   // precision = 1;
   // suffix = "M";
   // } else if (avgLog <= 13) {
   // scaleToBeApplied = 9;
   // precision = 2;
   // suffix = "B";
   // }
   // } catch (Exception e) {
   // e.printStackTrace();
   // }
   // Scale scale = new Scale();
   // scale.setPrecision(precision);
   // scale.setScaleTobeApplied(scaleToBeApplied);
   // scale.setSuffix(suffix);
   // return scale;
   // }
   //
   // private static List<String> getScaleFormattedValues (List<String> values, Scale scale) {
   // List<String> scaledValues = new ArrayList<String>();
   // for (String value : values) {
   // String scaledValue = value;
   // if (NumberUtils.isNumber(value)) {
   // double bValue = Double.parseDouble(value);
   // double dvalue = (bValue / Math.pow(10, scale.getScaleTobeApplied()));
   // scaledValue = Math.round(dvalue * Math.pow(10, scale.getPrecision())) / Math.pow(10, scale.getPrecision())
   // + "";
   // } else if (NULL.equalsIgnoreCase(value)) {
   // // commented for not replacing the N/A's with Zeros.
   // // scaledValue = "0";
   // }
   // // TODO: -RG- Do we need to set it back to NA?
   // // As these are always numeric data, might be ok?
   // scaledValues.add(scaledValue);
   // }
   // return scaledValues;
   // }

   public static String getSuffixForFactor (int factor, String suffix) {
      if (factor == 3) {
         return "K";
      } else if (factor == 6) {
         return "M";
      } else if (factor == 9) {
         return "B";
      } else if (factor == 12) {
         return "T";
      } else {
         if (ExecueCoreUtil.isNotEmpty(suffix)) {
            return suffix;
         }
         return "";
      }
   }

   // TODO: -JVK- remove hard-coding and get from config
   public static String getSuffixByFactor (int factor) {
      String suffix = "";
      if (factor == 3) {
         suffix = "K";
      } else if (factor == 6) {
         suffix = "M";
      } else if (factor == 9) {
         suffix = "B";
      } else if (factor == 12) {
         suffix = "T";
      }
      return suffix;
   }

   public static int getFactor (String colUnitFactor) {
      int factor = 0;
      if (ExecueCoreUtil.isNotEmpty(colUnitFactor)) {
         colUnitFactor = colUnitFactor.toUpperCase();
         if (colUnitFactor.startsWith("THOUSAND")) {
            factor = 3;
         } else if (colUnitFactor.startsWith("MILLION")) {
            factor = 6;
         } else if (colUnitFactor.startsWith("BILLION")) {
            factor = 9;
         } else if (colUnitFactor.startsWith("TRILLION")) {
            factor = 12;
         }
      }
      return factor;
   }

   public static Scale getScale (List<String> values) throws Exception {
      double avgLog = 0.0;
      double sumOfLog = 0.0;
      boolean validValues = true;
      Scale scale = new Scale();
      try {
         int count = 0;
         for (String value : values) {
            if (NumberUtils.isNumber(value)) {
               double colValue = Double.parseDouble(value);
               if (colValue != 0) {
                  sumOfLog = sumOfLog + Math.log(Math.abs(colValue)) / 2.303;
                  count++;
               }
            } else {
               if (!NULL.equalsIgnoreCase(value)) { // except for N/A, treat others as failure cases
                  validValues = false;
                  break;
               }
            }
         }
         if (validValues) {
            // avgLog = sumOfLog / values.size();
            avgLog = sumOfLog / count;
            avgLog = Math.round(avgLog);

            scale = getRevisedScale(avgLog);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return scale;
   }

   public static Scale getRevisedScale (double avgLog) throws Exception {
      int scaleToBeApplied = 0;
      int precision = 0;
      String suffix = "";
      Scale revisedScale = new Scale();
      revisedScale.setAvgLog(avgLog);

      if (avgLog < -2) {
         scaleToBeApplied = (int) avgLog;
         precision = 2;
         suffix = "E" + (int) avgLog;
      } else if (avgLog <= 0) {
         scaleToBeApplied = 0;
         precision = 2;
         suffix = "";
      } else if (avgLog <= 4) {
         scaleToBeApplied = 0;
         precision = 0;
         suffix = "";
      } else if (avgLog <= 7) {
         scaleToBeApplied = 3;
         precision = 1;
         suffix = "K";
      } else if (avgLog <= 10) {
         scaleToBeApplied = 6;
         precision = 1;
         suffix = "M";
      } else if (avgLog <= 13) {
         scaleToBeApplied = 9;
         precision = 2;
         suffix = "B";
      } else if (avgLog <= 16) {
         scaleToBeApplied = 12;
         precision = 2;
         suffix = "T";
      }

      revisedScale.setPrecision(precision);
      revisedScale.setScaleTobeApplied(scaleToBeApplied);
      revisedScale.setSuffix(suffix);

      return revisedScale;
   }

   public static List<String> getScaleFormattedValues (List<String> values, Scale scale) {
      List<String> scaledValues = new ArrayList<String>();
      // 08th April 2010
      // -JVK- added the value check to bye-pass the formatting when the scale(and precision) is zero
      if (scale.getScaleTobeApplied() != 0) {
         for (String value : values) {
            String scaledValue = value;
            if (NumberUtils.isNumber(value)) {
               double bValue = Double.parseDouble(value);
               double dvalue = bValue / Math.pow(10, scale.getScaleTobeApplied());
               scaledValue = Math.round(dvalue * Math.pow(10, scale.getPrecision()))
                        / Math.pow(10, scale.getPrecision()) + "";
            } else if (NULL.equalsIgnoreCase(value)) {
               // logger.debug("Encountered NULL(N/A) value - replacing with 0");
               // scaledValue = "0";
            }
            // TODO: -RG- Do we need to set it back to NA?
            // As these are always numeric data, might be ok?
            scaledValues.add(scaledValue);
         }
      } else {
         scaledValues = values;
      }
      return scaledValues;
   }

   public static QueryForm parseXML (String xmlRequest) throws HandlerRequestTransformException {
      if (logger.isDebugEnabled()) {
         logger.debug("in parseXML : parsing QI xml");
      }
      QueryForm queryForm = null;
      long start = System.currentTimeMillis();
      XStream xstream = new XStream();
      xstream.alias("qf", QueryForm.class);
      xstream.alias("select", QISelect.class);
      xstream.alias("term", QIBusinessTerm.class);
      xstream.alias("condition", QICondition.class);
      xstream.alias("lhsBusinessTerm", QIConditionLHS.class);
      xstream.alias("rhsValue", QIConditionRHS.class);
      xstream.alias("qiValue", QIValue.class);
      xstream.alias("query", QISubQuery.class);
      xstream.alias("value", String.class);
      xstream.alias("orderBy", QIOrderBy.class);
      try {
         queryForm = (QueryForm) xstream.fromXML(xmlRequest);
      } catch (Exception e) {
         logger.error("QI Request parsing ", e);
         throw new HandlerRequestTransformException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE,
                  "Unable to parse queryForm request");
      }
      long end = System.currentTimeMillis();
      if (logger.isDebugEnabled()) {
         logger.debug("Time Taken to parse xml : " + (end - start));
      }
      return queryForm;
   }
}
