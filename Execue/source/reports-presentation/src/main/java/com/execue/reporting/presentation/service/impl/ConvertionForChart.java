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


/***********************************************************************************************************************
 * <ExcCueLite - Reporting views> www.vbsoftindia.com FILENAME :ConvertionForChart.java DEPENDENCIES:ReportView.java
 * (action class) KNOWN ISSUES: CREATED ON: 27 January,2009
 **********************************************************************************************************************/

package com.execue.reporting.presentation.service.impl;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.execue.reporting.presentation.helper.PresentationTransformHelper;
import com.execue.reporting.presentation.service.IConvertionForChart;

/**
 * ConvertionForChart Class
 * 
 * @author Jaimin Bhavsar
 */
public class ConvertionForChart implements IConvertionForChart {

   private static final Logger log        = Logger.getLogger(ConvertionForChart.class);
   /**
    * @param path
    * @return String
    */
   public static String        PANVALUE   = "";
   public static int           PANSIZE;
   public static int           PANSIZEFORCROSS;
   public static int           noOfColumns;
   public static int           noOfRows;
   public static String        chartTitle = "";
   public static String        reportType = "";
   public static ArrayList     panLableAL = new ArrayList();
   public static ArrayList     avgAL      = null;                                      // jaimin on 26-6-09
   public static int           dimiensionLabelLength;

   public ConvertionForChart () {
      XMLFileReader xMLFileReader = new XMLFileReader();
      dimiensionLabelLength = Integer.parseInt(xMLFileReader.getValueByNode("/ChartApperance.xml", "XAXISLABELMAX"));
   }

   public String convertionForChart (String path, String reportQueryDataXML) {
      path = reportQueryDataXML;
      ArrayList idAList = new ArrayList();
      ArrayList newIdAList = new ArrayList();
      ArrayList valueAList = new ArrayList();
      LinkedHashMap someMap = new LinkedHashMap();
      HashMap descIdMap = new HashMap();
      String[] rTypeArr;
      String value = "", filePath = "", dType = "", outPath = "", ChartXMLStr = "", reportType = "";
      int doubleColCntr = 0;
      int dimentionCntr = 0;
      StringBuffer chartXMLStrBuffer = new StringBuffer();
      try {

         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document document = documentBuilder.parse(inStream);
         document.getDocumentElement().normalize();

         NodeList colNodeList = document.getElementsByTagName("COLUMN");

         chartXMLStrBuffer.append("<?xml version=\"1.0\"?>");
         chartXMLStrBuffer.append("<CHARTFX>");
         chartXMLStrBuffer.append("<REPORTTYPES>");
         NodeList headerNodeList = document.getElementsByTagName("HEADER");
         for (int cntr = 0; cntr < headerNodeList.getLength(); cntr++) {
            Node headerColNode = headerNodeList.item(cntr);
            Element headerElement = (Element) headerColNode;
            if (headerColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList idNodeList = headerElement.getElementsByTagName("REPORTTYPES");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               reportType = new String(((Node) idChNodeList.item(0)).getNodeValue());

            }
         }
         chartXMLStrBuffer.append(reportType);

         chartXMLStrBuffer.append("</REPORTTYPES>");
         chartXMLStrBuffer.append("<COLUMNS>");
         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));

               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               value = ((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');
               valueAList.add(new String(value));

               NodeList dTypeNodeList = colElement.getElementsByTagName("CTYPE");
               Element dTypeElement = (Element) dTypeNodeList.item(0);
               NodeList dTypeChNodeList = dTypeElement.getChildNodes();
               dType = ((Node) dTypeChNodeList.item(0)).getNodeValue();

               if (dType.equalsIgnoreCase("DIMENSION")) {
                  chartXMLStrBuffer.append("<COLUMN NAME=\"" + idAList.get(cntr) + "\" TYPE=\"String\" DESCRIPTION=\""
                           + escape((String) value) + "\"/>");
                  dimentionCntr++;
               } else {
                  doubleColCntr++;
               }
            }
         }

         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            descIdMap.put(idAList.get(cntr), valueAList.get(cntr));
         }
         // // End--</COLUMNS>
         noOfColumns = doubleColCntr;
         // // Start--<ROW>
         NodeList rowNodeList = document.getElementsByTagName("VALUES");
         TreeSet sortedSet = null;
         for (int cntr = 0; cntr < 1; cntr++) {
            Node fstRowNode = rowNodeList.item(cntr);
            Element rowElement = (Element) fstRowNode;

            if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {
               for (int cntrAList = dimentionCntr; cntrAList < idAList.size(); cntrAList++) {

                  NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                  Element idRowElement = (Element) idRowNodeList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();
                  // if(!((Node) idRowChNodeList.item(0)).getNodeValue().toString().equalsIgnoreCase("N/A"))
                  try {
                     someMap.put(idAList.get(cntrAList).toString(), Double.parseDouble(((Node) idRowChNodeList.item(0))
                              .getNodeValue().toString()));
                  } catch (NumberFormatException nfe) {
                     someMap.put(idAList.get(cntrAList).toString(), ((Node) idRowChNodeList.item(0)).getNodeValue());
                  }
               }
               /*
                * List mapKeys = new ArrayList(someMap.keySet()); List mapValues = new ArrayList(someMap.values());
                * someMap.clear(); sortedSet = new TreeSet(mapValues); Object[] sortedArray = sortedSet.toArray(); int
                * size = sortedArray.length; for (int i=0; i<size; i++) {
                * someMap.put(mapKeys.get(mapValues.indexOf(sortedArray[i])), sortedArray[i]); } someMap.clear(); for
                * (int i=size; i>0;) { someMap.put(mapKeys.get(mapValues.indexOf(sortedArray[--i])), sortedArray[i]); }
                */
            }
         }
         // axisMap = sortedSet;//jaimin on 26-6-09
         newIdAList.add(idAList.get(0));
         ArrayList mapKeys = new ArrayList(someMap.keySet());
         for (int cnt = 0; cnt < mapKeys.size(); cnt++) {
            newIdAList.add(cnt + 1, mapKeys.get(cnt));
         }
         avgAL = getAvgValuesOfMeauser(path, reportQueryDataXML, newIdAList);// By jaimin

         for (int alCntr = 1; alCntr < newIdAList.size(); alCntr++) {
            // if(!(descIdMap.get(newIdAList.get(alCntr)).toString().toUpperCase().contains("COUNT")))
            // {
            chartXMLStrBuffer.append("<COLUMN NAME=\"" + newIdAList.get(alCntr) + "\" TYPE=\"Double\" DESCRIPTION=\""
                     + escape((String) descIdMap.get(newIdAList.get(alCntr))) + "\"/>");
            // }
         }
         chartXMLStrBuffer.append("</COLUMNS>");

         for (int cntr = 0; cntr < rowNodeList.getLength(); cntr++) {
            Node fstRowNode = rowNodeList.item(cntr);
            Element rowElement = (Element) fstRowNode;
            chartXMLStrBuffer.append("<ROW ");
            if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {
               for (int cntrAList = 0; cntrAList < newIdAList.size(); cntrAList++) {

                  NodeList idRowNodeList = rowElement.getElementsByTagName(newIdAList.get(cntrAList).toString());
                  Element idRowElement = (Element) idRowNodeList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();

                  if (!idRowChNodeList.item(0).getNodeValue().equalsIgnoreCase("N/A")) {
                     String dimName = idRowChNodeList.item(0).getNodeValue();
                     if (dimName.length() > dimiensionLabelLength && cntrAList == 0)
                        dimName = dimName.substring(0, (dimiensionLabelLength - 3)) + "...";
                     chartXMLStrBuffer.append(" " + newIdAList.get(cntrAList).toString() + "=\"" + escape(dimName)
                              + "\"");
                  }
               }
            }
            chartXMLStrBuffer.append("></ROW>");
         }
         noOfRows = rowNodeList.getLength();
         chartXMLStrBuffer.append("</CHARTFX>");
         ChartXMLStr = chartXMLStrBuffer.toString();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return ChartXMLStr;
   }

   // Start - jaimin 7.7.09
   public ArrayList getAvgValuesOfMeauser (String path, String reportQueryDataXML, ArrayList inputAList) {
      ArrayList returnAList = new ArrayList();
      ArrayList tempAList = new ArrayList();

      double sumOfValue = 0.0;
      double avgOfValue = 0.0;

      try {

         path = reportQueryDataXML;
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document document = documentBuilder.parse(inStream);
         document.getDocumentElement().normalize();

         NodeList colNodeList = document.getElementsByTagName("VALUES");
         for (int inCnt = 1; inCnt < inputAList.size(); inCnt++) {
            tempAList.clear();
            for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
               Node fstColNode = colNodeList.item(cntr);
               Element colElement = (Element) fstColNode;
               if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
                  NodeList idNodeList = colElement.getElementsByTagName(inputAList.get(inCnt).toString());
                  Element idElement = (Element) idNodeList.item(0);
                  NodeList idChNodeList = idElement.getChildNodes();
                  tempAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));
               }
            }
            sumOfValue = 0.0;
            for (int aListCnt = 0; aListCnt < tempAList.size(); aListCnt++) {
               if (!tempAList.get(aListCnt).toString().equalsIgnoreCase("N/A"))
                  sumOfValue = sumOfValue + Double.parseDouble(tempAList.get(aListCnt).toString());
            }
            returnAList.add(sumOfValue / tempAList.size());
         }
      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }
      return returnAList;
   }

   // End - jaimin 7.7.09

   /*
    * public String convertionForChart(String path,String reportQueryDataXML) { path = reportQueryDataXML; ArrayList
    * idAList = new ArrayList(); ArrayList valueAList = new ArrayList(); String[] rTypeArr; String value = "", filePath =
    * "", dType = "" , outPath="" , ChartXMLStr = "",reportType=""; int doubleColCntr = 0; StringBuffer
    * chartXMLStrBuffer = new StringBuffer(); try { // File file = new File(path); // File file1 = new
    * File("c:\\ExeCueLite-XMLs"); // outPath = file.getName().substring(0, file.getName().lastIndexOf("."));
    * DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance(); DocumentBuilder
    * documentBuilder = documentBuilderFactory.newDocumentBuilder(); // Document document = documentBuilder.parse(file);
    * InputSource inStream = new InputSource(); inStream.setCharacterStream(new StringReader(path)); Document document=
    * documentBuilder.parse(inStream); // Document document = documentBuilder.parse(path);
    * document.getDocumentElement().normalize(); NodeList colNodeList = document.getElementsByTagName("COLUMN"); // if(
    * file1.isDirectory() == false ) // { // boolean success = file1.mkdir(); // } // finalPath =
    * "c:\\ExeCueLite-XMLs\\"+outPath+"ChartFx.xml"; // OutputStream outputStream = new FileOutputStream(finalPath); //
    * OutputStream bufferedOutputStream= new BufferedOutputStream(outputStream); // OutputStreamWriter
    * outputStreamWriter = new OutputStreamWriter(bufferedOutputStream, "8859_1"); chartXMLStrBuffer.append("<?xml
    * version=\"1.0\"?> "); chartXMLStrBuffer.append("<CHARTFX>"); chartXMLStrBuffer.append("<REPORTTYPES>"); NodeList
    * headerNodeList = document.getElementsByTagName("HEADER"); for (int cntr = 0; cntr < headerNodeList.getLength();
    * cntr++) { Node headerColNode = headerNodeList.item(cntr); Element headerElement = (Element) headerColNode; if
    * (headerColNode.getNodeType() == Node.ELEMENT_NODE) { NodeList idNodeList =
    * headerElement.getElementsByTagName("REPORTTYPES"); Element idElement = (Element) idNodeList.item(0); NodeList
    * idChNodeList = idElement.getChildNodes(); reportType = new String(((Node) idChNodeList.item(0)).getNodeValue()); //
    * NodeList titleNodeList = headerElement.getElementsByTagName("TITLE"); // Element titleElement = (Element)
    * titleNodeList.item(0); // NodeList titleChNodeList = titleElement.getChildNodes(); // chartTitle = new
    * String(((Node) titleChNodeList.item(0)).getNodeValue()); } } chartXMLStrBuffer.append(reportType); // rTypeArr =
    * reportType.split(","); // for (int strCntr = 0 ; strCntr < rTypeArr.length ; strCntr++) // { /// if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("GRID")) // { // chartXMLStrBuffer.append("1,"); // } // if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("BAR")) // { // chartXMLStrBuffer.append("2,"); // } // if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("LINE")) // { // chartXMLStrBuffer.append("3,"); // } // if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("PIVOT")) // { // chartXMLStrBuffer.append("4,"); // } // if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("GROUP")) // { // chartXMLStrBuffer.append("5,"); // } /// if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CROSS")) // { // chartXMLStrBuffer.append("6,"); /// } // if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CBAR")) // { // chartXMLStrBuffer.append("7,"); // } // if
    * ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CLINE")) // { // chartXMLStrBuffer.append("8,"); // } //
    * //HierarchyChart // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("BARLINE")) // { //
    * chartXMLStrBuffer.append("10,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("MULTIBAR")) // { //
    * chartXMLStrBuffer.append("11,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CLUSTERBAR")) // { //
    * chartXMLStrBuffer.append("12,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("Cmultibar")) // { //
    * chartXMLStrBuffer.append("13,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("MULTILINE")) // { //
    * chartXMLStrBuffer.append("14,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CLUSTERLINE")) // { //
    * chartXMLStrBuffer.append("15,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CMULTILINE")) // { //
    * chartXMLStrBuffer.append("16,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("MULTILINECLUSTER")) // { //
    * chartXMLStrBuffer.append("17,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CMMULTIBAR")) // { //
    * chartXMLStrBuffer.append("18,"); // } // if ((rTypeArr[strCntr]).trim().equalsIgnoreCase("CMMultiLine")) // { //
    * chartXMLStrBuffer.append("19,"); // } // } chartXMLStrBuffer.append("</REPORTTYPES>"); //// End - report type
    * chartXMLStrBuffer.append("<COLUMNS>"); //// Start - <COLUMNS> for (int cntr = 0; cntr < colNodeList.getLength();
    * cntr++) { Node fstColNode = colNodeList.item(cntr); Element colElement = (Element) fstColNode; if
    * (fstColNode.getNodeType() == Node.ELEMENT_NODE) { NodeList idNodeList = colElement.getElementsByTagName("ID");
    * Element idElement = (Element) idNodeList.item(0); NodeList idChNodeList = idElement.getChildNodes();
    * idAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue())); NodeList valueNodeList =
    * colElement.getElementsByTagName("DESC"); Element valueElement = (Element) valueNodeList.item(0); NodeList
    * valueChNodeList = valueElement.getChildNodes(); value = ((Node)
    * valueChNodeList.item(0)).getNodeValue().toString().replace(' ','_'); valueAList.add(new String(value)); NodeList
    * dTypeNodeList = colElement.getElementsByTagName("CTYPE"); Element dTypeElement = (Element) dTypeNodeList.item(0);
    * NodeList dTypeChNodeList = dTypeElement.getChildNodes(); dType = ((Node) dTypeChNodeList.item(0)).getNodeValue();
    * if (dType.equalsIgnoreCase("DIMENSION")) { chartXMLStrBuffer.append("<COLUMN NAME=\""+value+"\" TYPE=\"String\"
    * />"); } else { chartXMLStrBuffer.append("<COLUMN NAME=\""+value+"\" TYPE=\"Double\" />"); doubleColCntr++; } } }
    * chartXMLStrBuffer.append("</COLUMNS>"); //// End--</COLUMNS> noOfColumns = doubleColCntr; //// Start--<ROW>
    * NodeList rowNodeList = document.getElementsByTagName("VALUES"); for (int cntr = 0; cntr < rowNodeList.getLength();
    * cntr++) { Node fstRowNode = rowNodeList.item(cntr); Element rowElement = (Element) fstRowNode;
    * chartXMLStrBuffer.append("<ROW "); if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) { for (int cntrAList = 0;
    * cntrAList < idAList.size();cntrAList++ ) { NodeList idRowNodeList =
    * rowElement.getElementsByTagName(idAList.get(cntrAList).toString()); Element idRowElement = (Element)
    * idRowNodeList.item(0); NodeList idRowChNodeList = idRowElement.getChildNodes(); // try // { // Double dValue =
    * Double.parseDouble(((Node) idRowChNodeList.item(0)).getNodeValue().toString()); // NumberFormat numberFormat =
    * NumberFormat.getNumberInstance(Locale.getDefault() ); // numberFormat.setMaximumFractionDigits(2); // String temp1 =
    * numberFormat.format(dValue); // chartXMLStrBuffer.append(" "+valueAList.get(cntrAList)+"=\""+temp1+"\""); // } //
    * catch (NumberFormatException e) // { // // log.error(e); chartXMLStrBuffer.append("
    * "+valueAList.get(cntrAList)+"=\""+((Node) idRowChNodeList.item(0)).getNodeValue()+"\""); // } } }
    * chartXMLStrBuffer.append("></ROW>"); } noOfRows = rowNodeList.getLength(); chartXMLStrBuffer.append("</CHARTFX>"); //
    * outputStreamWriter.flush(); // outputStreamWriter.close(); ChartXMLStr = chartXMLStrBuffer.toString(); } catch
    * (Exception e) { e.printStackTrace(); } return ChartXMLStr; }
    */

   /**
    * cmMultiChartXMLConvertor method()
    * 
    * @author Ankur M. Bhalodia
    * @param path
    * @return String
    */
   public String cmMultiChartXMLConvertor (String path, String reportQueryDataXML) {
      path = reportQueryDataXML;
      ArrayList idAList = new ArrayList();
      // ArrayList idAList1 = new ArrayList();
      ArrayList valueAList = new ArrayList();
      ArrayList colAList = new ArrayList();

      List tempRowKey = new LinkedList();
      List tempRowValue = new LinkedList();

      // Map m = new HashMap();
      int iteratorCntr = 0;
      String colValue = "", filePath = "", dataType = "", outPath = "", chartXMLStr = "", iteratorData = "", rowTempData2 = "", rowKeyEndValue = "", rowTempData1 = "";
      StringBuffer chartXMLStrBuffer = new StringBuffer();

      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         // Document document = documentBuilder.parse(file);
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document document = documentBuilder.parse(inStream);
         document.getDocumentElement().normalize();
         NodeList colNodeList = document.getElementsByTagName("COLUMN");

         // ---- converted XML File path
         // finalPath = "c:\\ExeCueLite-XMLs\\"+outPath+"CMMultiChart.xml";

         // OutputStream outputStream = new FileOutputStream(finalPath);
         // OutputStream bufferedOutputStream= new BufferedOutputStream(outputStream);
         // OutputStreamWriter outputStreamWriter = new OutputStreamWriter(bufferedOutputStream, "8859_1");
         chartXMLStrBuffer.append("<?xml version=\"1.0\"?>");
         chartXMLStrBuffer.append("<CHARTFX>");
         chartXMLStrBuffer.append("<COLUMNS>");
         // ---- Start Columns
         HashSet hSet4Pro = new HashSet();
         NodeList rowNodeList3 = document.getElementsByTagName("VALUES");
         for (int size = 0; size < rowNodeList3.getLength(); size++) {
            Node fstColNode = rowNodeList3.item(size);
            Element colElement = (Element) fstColNode;
            NodeList idNodeList = colElement.getElementsByTagName("c11");
            Element idElement = (Element) idNodeList.item(0);
            NodeList idChNodeList = idElement.getChildNodes();
            String data4Has = ((Node) idChNodeList.item(0)).getNodeValue();
            hSet4Pro.add(data4Has);
            // idAList1.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));
         }

         Iterator iterator = hSet4Pro.iterator();
         PANSIZE = hSet4Pro.size();
         while (iterator.hasNext()) {
            iteratorData = iterator.next().toString();
            colAList.add(iteratorCntr, iteratorData);
            // m.put(data1, iteratorI);
            iteratorCntr++;
         }
         for (int cntr1 = 0; cntr1 < hSet4Pro.size(); cntr1++) {
            for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
               Node fstColNode = colNodeList.item(cntr);
               Element colElement = (Element) fstColNode;

               if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
                  NodeList idNodeList = colElement.getElementsByTagName("ID");
                  Element idElement = (Element) idNodeList.item(0);
                  NodeList idChNodeList = idElement.getChildNodes();
                  idAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));

                  NodeList valueNodeList = colElement.getElementsByTagName("DESC");
                  Element valueElement = (Element) valueNodeList.item(0);
                  NodeList valueChNodeList = valueElement.getChildNodes();
                  colValue = ((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');
                  valueAList.add(new String(colValue));

                  NodeList dTypeNodeList = colElement.getElementsByTagName("DTYPE");
                  Element dTypeElement = (Element) dTypeNodeList.item(0);
                  NodeList dTypeChNodeList = dTypeElement.getChildNodes();
                  dataType = ((Node) dTypeChNodeList.item(0)).getNodeValue();

                  if (((Node) idChNodeList.item(0)).getNodeValue().equals("c10")) {
                     if (cntr1 == 0) {
                        chartXMLStrBuffer.append("<COLUMN NAME=\"" + colValue + "\" TYPE=\"String\" />");
                     }
                  } else {
                     if (dataType.equalsIgnoreCase("string")) {
                        chartXMLStrBuffer.append("<COLUMN NAME=\"" + colValue + "_" + colAList.get(cntr1)
                                 + "\" TYPE=\"String\" />");
                     } else {
                        chartXMLStrBuffer.append("<COLUMN NAME=\"" + colValue + "_" + colAList.get(cntr1)
                                 + "\" TYPE=\"Double\" />");
                     }
                  }
               }
            }
         }
         chartXMLStrBuffer.append("</COLUMNS>");
         // ---- End Columns
         NodeList rowNodeList = document.getElementsByTagName("VALUES");

         for (int cntr = 0; cntr < rowNodeList.getLength(); cntr++) {
            Node fstRowNode = rowNodeList.item(cntr);
            Element rowElement = (Element) fstRowNode;

            NodeList idRowNodeList1 = rowElement.getElementsByTagName(idAList.get(0).toString());
            Element idRowElement1 = (Element) idRowNodeList1.item(0);
            NodeList idRowChNodeList1 = idRowElement1.getChildNodes();

            if (cntr < rowNodeList.getLength() - 1) {
               int cntrNo = cntr;
               Node fstRowNode2 = rowNodeList.item(cntrNo + 1);
               Element rowElement2 = (Element) fstRowNode2;
               NodeList idRowNodeList2 = rowElement2.getElementsByTagName(idAList.get(0).toString());
               Element idRowElement2 = (Element) idRowNodeList2.item(0);
               NodeList idRowChNodeList2 = idRowElement2.getChildNodes();
               rowTempData2 = (String) ((Node) idRowChNodeList2.item(0)).getNodeValue();
            }
            rowTempData1 = (String) ((Node) idRowChNodeList1.item(0)).getNodeValue();

            if (rowTempData1.equals(rowTempData2)) {
               NodeList idRowNodeList4 = rowElement.getElementsByTagName(idAList.get(1).toString());
               Element idRowElement4 = (Element) idRowNodeList4.item(0);
               NodeList idRowChNodeList4 = idRowElement4.getChildNodes();
               for (int cntrAList = 0; cntrAList < colNodeList.getLength(); cntrAList++) {
                  NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                  Element idRowElement = (Element) idRowNodeList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();

                  rowKeyEndValue = ((Node) idRowChNodeList4.item(0)).getNodeValue();
                  if (tempRowKey.size() == 0) {
                     tempRowKey.add(cntrAList, valueAList.get(cntrAList));
                     tempRowValue.add(cntrAList, ((Node) idRowChNodeList.item(0)).getNodeValue());
                  } else {
                     if (cntrAList == 0) {
                        continue;
                     }
                     tempRowKey.add(cntrAList, valueAList.get(cntrAList) + "_" + rowKeyEndValue);
                     tempRowValue.add(cntrAList, ((Node) idRowChNodeList.item(0)).getNodeValue());
                  }
               }
            } else {
               // ---- Start ROW
               chartXMLStrBuffer.append("<ROW ");
               if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {
                  NodeList idRowNodeList4 = rowElement.getElementsByTagName(idAList.get(1).toString());
                  Element idRowElement4 = (Element) idRowNodeList4.item(0);
                  NodeList idRowChNodeList4 = idRowElement4.getChildNodes();
                  for (int cntrAList = 0; cntrAList < colNodeList.getLength(); cntrAList++) {
                     NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                     Element idRowElement = (Element) idRowNodeList.item(0);
                     NodeList idRowChNodeList = idRowElement.getChildNodes();
                     if (tempRowKey.size() == 0) {
                        tempRowKey.add(cntrAList, valueAList.get(cntrAList));
                        tempRowValue.add(cntrAList, ((Node) idRowChNodeList.item(0)).getNodeValue());
                     } else {
                        if (cntrAList == 0) {
                           continue;
                        }

                        rowKeyEndValue = ((Node) idRowChNodeList4.item(0)).getNodeValue();
                        tempRowKey.add(cntrAList, valueAList.get(cntrAList) + "_" + rowKeyEndValue);
                        tempRowValue.add(cntrAList, ((Node) idRowChNodeList.item(0)).getNodeValue());
                     }
                  }
                  for (int cntr1 = 0; cntr1 < tempRowKey.size(); cntr1++) {
                     chartXMLStrBuffer.append(" " + tempRowKey.get(cntr1) + "=\""
                              + escape((String) tempRowValue.get(cntr1)) + "\" ");
                  }
                  tempRowKey.clear();
               }
               chartXMLStrBuffer.append("></ROW>");
            }
            if (cntr == (rowNodeList.getLength() - 1)) {
               chartXMLStrBuffer.append("<ROW ");
               for (int cntr1 = 0; cntr1 < tempRowKey.size(); cntr1++) {
                  chartXMLStrBuffer.append(" " + tempRowKey.get(cntr1) + "=\""
                           + escape((String) tempRowValue.get(cntr1)) + "\" ");
               }
               chartXMLStrBuffer.append("></ROW>");

            }
         }
         // ---- End ROW
         chartXMLStrBuffer.append("</CHARTFX>");
         chartXMLStr = chartXMLStrBuffer.toString();

      } catch (Exception e) {
         e.printStackTrace();
      }
      return chartXMLStr;
   }

   /**
    * crossChartXMLConvertor method()
    * 
    * @author Dhananjay
    * @param path
    * @return String
    */
   public String crossChartXMLConvertor (String path, String reportQueryDataXML) {
      path = reportQueryDataXML;
      ArrayList idAList = new ArrayList();
      ArrayList notDimen = new ArrayList();
      ArrayList valueAList = new ArrayList();
      ArrayList cTypeAlist = new ArrayList();
      ArrayList toRemoveDup = new ArrayList();
      ArrayList dimensionList = new ArrayList();
      ArrayList fstDimenVal = new ArrayList();
      ArrayList finalMeasure1 = new ArrayList();
      ArrayList finalMeasure2 = new ArrayList();
      ArrayList finalMeasure3 = new ArrayList();
      ArrayList fstDim = new ArrayList();
      ArrayList valArr = new ArrayList();
      ArrayList dimArr = new ArrayList();
      ArrayList panValAL = new ArrayList();
      List<String> idList = new ArrayList<String>();
      List<String> measureIdList = new ArrayList<String>();
      String value = "";
      String idValue = "";
      String cType = "";
      String Dimen = "dimension";
      String removed = "";
      String idCombinatioin = "";
      String hmKey = "";
      String hmVal = "";
      String valueAListdata = "";
      String outPath = "";
      String chartXMLStr = "";
      String panV = "";
      int acrossMemberCount = 0;

      StringBuffer chartXMLStrBuffer = new StringBuffer();
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         // Document document = documentBuilder.parse(file);
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document doc = documentBuilder.parse(inStream);
         doc.getDocumentElement().normalize();

         NodeList colNodeList = doc.getElementsByTagName("COLUMN");
         NodeList rowNodeList = doc.getElementsByTagName("VALUES");

         chartXMLStrBuffer.append("<?xml version=\"1.0\"?>");
         chartXMLStrBuffer.append("<CHARTFX>");
         chartXMLStrBuffer.append("<COLUMNS>");

         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idValue = ((Node) idChNodeList.item(0)).getNodeValue().toString();
               idAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));

               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               value = ((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');
               valueAList.add(new String(value));

               NodeList cTypeNodeList = colElement.getElementsByTagName("CTYPE");
               Element cTypeElement = (Element) cTypeNodeList.item(0);
               NodeList cTypeChNodeList = cTypeElement.getChildNodes();
               cType = ((Node) cTypeChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');

               NodeList chNodeList = colElement.getElementsByTagName("ACROSS");
               if (chNodeList.item(0) != null) {
                  NodeList memberList = colElement.getElementsByTagName("NUMMEMBERS");
                  Element memberElement = (Element) memberList.item(0);
                  NodeList memberChNodeList = memberElement.getChildNodes();
                  acrossMemberCount = Integer.parseInt(((Node) memberChNodeList.item(0)).getNodeValue().toString());
               }

               if (cType.equalsIgnoreCase(Dimen)) {
                  cTypeAlist.add(new String(cType));
               } else {
                  if (cntr == 0) {
                     // null
                  } else if (cntr == 2) {
                     panV = value;
                     panValAL.add(value);
                  } else {
                     panV = panV + "," + value;
                     panValAL.add(value);
                  }
                  notDimen.add(new String(value));
                  measureIdList.add(idAList.get(cntr).toString());
               }
               if (cntr == 0) {
                  chartXMLStrBuffer.append("<COLUMN NAME=\"" + idAList.get(cntr) + "\" TYPE=\"String\" DESCRIPTION=\""
                           + escape((String) value) + "\"/>");
               }
            }
         }
         PANVALUE = panV;
         panLableAL = panValAL;
         PANSIZEFORCROSS = notDimen.size();
         int tempAcrossMembersCount = 0;
         for (int notdimcntr = 0; notdimcntr < notDimen.size(); notdimcntr++) {
            tempAcrossMembersCount = 0;
            for (int ncntr = 0; ncntr < rowNodeList.getLength(); ncntr++, tempAcrossMembersCount++) {
               Node valNode = rowNodeList.item(ncntr);
               Element valElement = (Element) valNode;
               if (valNode.getNodeType() == Node.ELEMENT_NODE) {
                  for (int cntrAList = 1; cntrAList < 2; cntrAList++) {
                     NodeList idRowNodeList = valElement.getElementsByTagName(idAList.get(cntrAList).toString());
                     Element idRowElement = (Element) idRowNodeList.item(0);
                     NodeList idRowChNodeList = idRowElement.getChildNodes();
                     removed = ("" + notDimen.get(notdimcntr) + "_"
                              + ((Node) idRowChNodeList.item(0)).getNodeValue().toString().replace(' ', '_') + "");
                     if (tempAcrossMembersCount >= acrossMemberCount) {
                        tempAcrossMembersCount = 0;
                     }
                     idCombinatioin = ("" + measureIdList.get(notdimcntr) + "_" + idAList.get(cntrAList).toString()
                              + "_" + tempAcrossMembersCount);
                     toRemoveDup.add(new String(removed));
                     idList.add(idCombinatioin);
                     dimensionList.add("" + ((Node) idRowChNodeList.item(0)).getNodeValue());
                  }
               }
            }
         }
         removeDuplicate(toRemoveDup);
         removeDuplicate((ArrayList) idList);
         removeDuplicate(dimensionList);
         tempAcrossMembersCount = 0;
         for (int colData = 0; colData < idList.size(); colData++, tempAcrossMembersCount++) {
            if (tempAcrossMembersCount >= acrossMemberCount) {
               tempAcrossMembersCount = 0;
            }
            chartXMLStrBuffer.append("<COLUMN NAME=\"" + idList.get(colData) + "\" TYPE=\"Double\" DESCRIPTION=\""
                     + escape((String) dimensionList.get(tempAcrossMembersCount)) + "\"/>");
         }
         chartXMLStrBuffer.append("</COLUMNS>");

         NodeList rowList = doc.getElementsByTagName("VALUES");
         for (int vcount = 0; vcount < rowList.getLength(); vcount++) {
            Node rowValNode = rowList.item(vcount);
            Element rowElement = (Element) rowValNode;
            if (rowValNode.getNodeType() == Node.ELEMENT_NODE) {
               for (int cntrAList = 1; cntrAList < 2; cntrAList++) {
                  NodeList idrowList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                  Element idRowElement = (Element) idrowList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();
                  fstDimenVal.add(((Node) idRowChNodeList.item(0)).getNodeValue());
               }
               for (int rowElecntr = 0; rowElecntr < 1; rowElecntr++) {
                  NodeList dim1 = rowElement.getElementsByTagName(idAList.get(rowElecntr).toString());
                  Element rdim1 = (Element) dim1.item(0);
                  NodeList nodedim = rdim1.getChildNodes();
                  fstDim.add(((Node) nodedim.item(0)).getNodeValue());
               }
            }

            for (int cntrAList = 2; cntrAList < valueAList.size(); cntrAList++) {
               NodeList idrowList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
               Element idRowElement = (Element) idrowList.item(0);
               NodeList idRowChNodeList = idRowElement.getChildNodes();
               hmVal = ((Node) idRowChNodeList.item(0)).getNodeValue();

               hmKey = (String) valueAList.get(cntrAList) + "_" + fstDimenVal.get(vcount);
               finalMeasure1.add(fstDim.get(vcount));// dimensoon 1
               finalMeasure2.add(hmKey);//
               finalMeasure3.add(hmVal);// measure value
            }
         }

         removeDuplicate(fstDim);
         for (int readhmCnt = 0; readhmCnt < toRemoveDup.size(); readhmCnt++) {
            String duprmd = (String) toRemoveDup.get(readhmCnt).toString().replace(' ', '_');
            for (int btp = 0; btp < finalMeasure2.size(); btp++) {
               String dataFmeasure2 = (String) finalMeasure2.get(btp).toString().replace(' ', '_');
               if (duprmd.equalsIgnoreCase(dataFmeasure2)) {
                  valArr.add(finalMeasure1.get(btp));
                  dimArr.add(idList.get(readhmCnt) + "=\"" + escape((String) finalMeasure3.get(btp)));
               }
            }
         }
         for (int val = 0; val < fstDim.size(); val++) {
            String valstr = (String) fstDim.get(val).toString().replace(' ', '_');
            String rmUnd = valstr.toString().replace('_', ' ');
            chartXMLStrBuffer.append("<ROW ");
            for (int frstdimen = 0; frstdimen < 1; frstdimen++) {
               valueAListdata = (String) idAList.get(frstdimen).toString().replace(' ', '_');
            }
            if (!rmUnd.equalsIgnoreCase("N/A")) {
               if (rmUnd.length() > dimiensionLabelLength)
                  rmUnd = rmUnd.substring(0, (dimiensionLabelLength - 3)) + "...";
               chartXMLStrBuffer.append(valueAListdata.toString().replace(' ', '_') + " =\"" + escape((String) rmUnd)
                        + "\" ");
            }
            for (int valdim = 0; valdim < valArr.size(); valdim++) {
               String valdimfinal = (String) valArr.get(valdim).toString().replace(' ', '_');
               if (valstr.contentEquals(valdimfinal) && !dimArr.get(valdim).toString().toUpperCase().contains("N/A")) {
                  chartXMLStrBuffer.append("" + dimArr.get(valdim).toString().replace(' ', '_') + "\" ");
               }
            }
            chartXMLStrBuffer.append("></ROW>");
         }
         chartXMLStrBuffer.append("</CHARTFX>");
         chartXMLStr = chartXMLStrBuffer.toString();
      } catch (Exception e) {
         e.printStackTrace();
      }
      return chartXMLStr;
   }

   // /iterator to remove dulpicate rows
   public static void removeDuplicate (ArrayList arlList) {

      Set remove = new HashSet();
      for (Iterator iter = arlList.iterator(); iter.hasNext();) {
         Object element = iter.next();
         if (!remove.add(element)) // if current element is a duplicate,
            iter.remove(); // remove it
      }
   }

   // //end of iterator
   public String[] cMultiChart (String path) {
      String retString = "";
      ArrayList numField = new ArrayList();
      ArrayList valueAList = new ArrayList();
      ArrayList idAList = new ArrayList();
      String strField = "";
      String dType = "";
      String value = "";
      StringBuffer chartStrBuffer = new StringBuffer();
      String cMultiArry[] = null;

      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         // Document document = documentBuilder.parse(file);
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document document = documentBuilder.parse(inStream);
         document.getDocumentElement().normalize();

         NodeList colNodeList = document.getElementsByTagName("COLUMN");

         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));

               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               value = ((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');
               valueAList.add(new String(value));

               NodeList dTypeNodeList = colElement.getElementsByTagName("CTYPE");
               Element dTypeElement = (Element) dTypeNodeList.item(0);
               NodeList dTypeChNodeList = dTypeElement.getChildNodes();
               dType = ((Node) dTypeChNodeList.item(0)).getNodeValue();

               if (!dType.equalsIgnoreCase("dimension")) {
                  // numField.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));
                  numField.add(cntr);
               } else {
                  strField = (new String(((Node) idChNodeList.item(0)).getNodeValue()));
               }
            }
         }

         cMultiArry = new String[numField.size()];

         for (int arCntr = 0; arCntr < numField.size(); arCntr++) {
            chartStrBuffer = new StringBuffer();
            chartStrBuffer.append("<?xml version=\"1.0\"?><CHARTFX><COLUMNS>");
            chartStrBuffer.append("<COLUMN NAME=\"" + idAList.get(0) + "\" TYPE=\"String\" DESCRIPTION=\""
                     + escape((String) valueAList.get(0)) + "\"/>");
            chartStrBuffer.append("<COLUMN NAME=\"" + idAList.get(Integer.parseInt(numField.get(arCntr).toString()))
                     + "\" TYPE=\"Double\"  DESCRIPTION=\""
                     + escape((String) valueAList.get(Integer.parseInt(numField.get(arCntr).toString()))) + "\"/>");
            chartStrBuffer.append("</COLUMNS>");

            NodeList rowNodeList = document.getElementsByTagName("VALUES");
            for (int cntr = 0; cntr < rowNodeList.getLength(); cntr++) {
               Node fstRowNode = rowNodeList.item(cntr);
               Element rowElement = (Element) fstRowNode;
               chartStrBuffer.append("<ROW ");
               if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {

                  NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(0).toString());
                  Element idRowElement = (Element) idRowNodeList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();
                  String dimName = idRowChNodeList.item(0).getNodeValue();
                  if (dimName.length() > dimiensionLabelLength)
                     dimName = dimName.substring(0, (dimiensionLabelLength - 3)) + "...";
                  chartStrBuffer.append(" " + idAList.get(0) + "=\"" + escape(dimName) + "\"");

                  NodeList numNodeList = rowElement.getElementsByTagName(idAList.get(
                           Integer.parseInt(numField.get(arCntr).toString())).toString());
                  Element numElement = (Element) numNodeList.item(0);
                  NodeList numChNodeList = numElement.getChildNodes();
                  if (!numChNodeList.item(0).getNodeValue().equalsIgnoreCase("N/A"))
                     chartStrBuffer.append(" " + idAList.get(Integer.parseInt(numField.get(arCntr).toString())) + "=\""
                              + escape(((Node) numChNodeList.item(0)).getNodeValue()) + "\"");

               }
               chartStrBuffer.append("></ROW>");
            }
            chartStrBuffer.append("</CHARTFX>");
            retString = chartStrBuffer.toString();

            cMultiArry[arCntr] = retString;
         }
      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }
      return cMultiArry;
   }

   // /////////////////////
   // //end of iterator
   // By Dhananajy Chuhan
   public String[] crossLineChartXMLConvertor (String path, String reportQueryDataXML) {
      path = reportQueryDataXML;
      ArrayList idAList = new ArrayList();
      ArrayList notDimen = new ArrayList();
      ArrayList valueAList = new ArrayList();
      ArrayList cTypeAlist = new ArrayList();
      ArrayList unidimenplusCtype = new ArrayList();
      ArrayList idUnidimenplusCtype = new ArrayList();
      ArrayList fstDimenVal = new ArrayList();
      ArrayList idFstDimenVal = new ArrayList();
      ArrayList finalMeasure1 = new ArrayList();
      ArrayList finalMeasure2 = new ArrayList();
      ArrayList finalMeasure = new ArrayList();
      ArrayList fstDim = new ArrayList();
      ArrayList acrossMemberList = new ArrayList();
      // ArrayList valArr =new ArrayList();
      // ArrayList dimArr =new ArrayList();

      String crossChartArry[] = null;
      String value = "";
      String idValue = "";
      String cType = "";
      String Dimen = "dimension";
      String dimenplusCtype = "";
      String djay = "";
      String hmKey = "";
      // String hmVal ="";
      // String valueAListdata ="";
      // String outPath ="";
      String chartXMLStr = "";
      String panV = "";
      int totalGRPMembers = 0;
      int totalACRMembers = 0;

      StringBuffer chartXMLStrBuffer = new StringBuffer();
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         // Document document = documentBuilder.parse(file);
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document doc = documentBuilder.parse(inStream);
         doc.getDocumentElement().normalize();

         NodeList colNodeList = doc.getElementsByTagName("COLUMN");
         NodeList rowNodeList = doc.getElementsByTagName("VALUES");

         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idValue = ((Node) idChNodeList.item(0)).getNodeValue().toString();
               idAList.add(new String(((Node) idChNodeList.item(0)).getNodeValue()));

               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               value = ((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');
               valueAList.add(new String(value));

               NodeList cTypeNodeList = colElement.getElementsByTagName("CTYPE");
               Element cTypeElement = (Element) cTypeNodeList.item(0);
               NodeList cTypeChNodeList = cTypeElement.getChildNodes();
               cType = ((Node) cTypeChNodeList.item(0)).getNodeValue().toString().replace(' ', '_');

               NodeList chNodeList = colElement.getElementsByTagName("ACROSS");
               if (chNodeList.item(0) != null) {
                  NodeList memberList = colElement.getElementsByTagName("MEMBER");

                  for (int memCount = 0; memCount < memberList.getLength(); memCount++) {
                     NodeList valNodeList = memberList.item(memCount).getChildNodes();
                     Element valElement = (Element) valNodeList.item(1);
                     NodeList valChNodeList = valElement.getChildNodes();
                     acrossMemberList.add(((Node) valChNodeList.item(0)).getNodeValue().toString());
                  }// done with getting the list of members for
                  // across columns.
               }

               if (cType.equalsIgnoreCase(Dimen)) {
                  cTypeAlist.add(new String(cType));
               } else {
                  if (cntr == 0) {
                     // null
                  } else if (cntr == 2) {
                     panV = value;
                  } else {
                     panV = panV + "," + value;
                  }
                  notDimen.add(new String(value));
               }
               // get the infrastructure variables for normalize document
               NodeList grpNodeList = colElement.getElementsByTagName("GROUPBY");
               Element grpElement = (Element) grpNodeList.item(0);

               NodeList memberNodeList = colElement.getElementsByTagName("NUMMEMBERS");
               Element memberElement = (Element) memberNodeList.item(0);

               if (memberElement != null) {
                  NodeList memberChNodeList = memberElement.getChildNodes();
                  if (grpElement != null) {
                     totalGRPMembers = Integer.parseInt(memberChNodeList.item(0).getTextContent());
                  }
                  totalACRMembers = Integer.parseInt(memberChNodeList.item(0).getTextContent());
               }
            }
         }
         // re-modify the data object for handling missing values.
         if ((totalGRPMembers * totalACRMembers) != rowNodeList.getLength()) {
            PresentationTransformHelper.normalizeDocument(doc, colNodeList, totalGRPMembers);
         }
         // end of re-modify

         crossChartArry = new String[notDimen.size()];
         int accrossMemberCounter = 0;
         PANVALUE = panV;
         PANSIZEFORCROSS = notDimen.size();
         String[][] mulArr = new String[rowNodeList.getLength()][notDimen.size()];

         for (int notdimcntr = 0; notdimcntr < notDimen.size(); notdimcntr++) {
            for (int ncntr = 0; ncntr < rowNodeList.getLength(); ncntr++) {
               Node valNode = rowNodeList.item(ncntr);
               Element valElement = (Element) valNode;
               if (valNode.getNodeType() == Node.ELEMENT_NODE) {
                  for (int cntrAList = 1; cntrAList < 2; cntrAList++) {
                     NodeList idRowNodeList = valElement.getElementsByTagName(idAList.get(cntrAList).toString());
                     Element idRowElement = (Element) idRowNodeList.item(0);
                     NodeList idRowChNodeList = idRowElement.getChildNodes();
                     dimenplusCtype = (""
                              + ((Node) idRowChNodeList.item(0)).getNodeValue().toString().replace(' ', '_') + "");
                     unidimenplusCtype.add(new String(dimenplusCtype));
                     if (accrossMemberCounter >= acrossMemberList.size()) {
                        accrossMemberCounter = 0;
                     }
                     idUnidimenplusCtype.add(idAList.get(cntrAList).toString() + "_" + accrossMemberCounter);
                     accrossMemberCounter++;

                  }
               }
            }
         }
         removeDuplicate(unidimenplusCtype);
         removeDuplicate(idUnidimenplusCtype);

         for (int vcount = 0; vcount < rowNodeList.getLength(); vcount++) {
            Node rowValNode = rowNodeList.item(vcount);
            Element rowElement = (Element) rowValNode;
            if (rowValNode.getNodeType() == Node.ELEMENT_NODE) {
               for (int cntrAList = 1; cntrAList < 2; cntrAList++) {
                  NodeList idrowList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                  Element idRowElement = (Element) idrowList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();
                  fstDimenVal.add(((Node) idRowChNodeList.item(0)).getNodeValue());
                  if (accrossMemberCounter >= acrossMemberList.size()) {
                     accrossMemberCounter = 0;
                  }
                  idFstDimenVal.add(idUnidimenplusCtype.get(accrossMemberCounter).toString());
                  accrossMemberCounter++;
               }
               for (int rowElecntr = 0; rowElecntr < 1; rowElecntr++) {
                  NodeList dim1 = rowElement.getElementsByTagName(idAList.get(rowElecntr).toString());
                  Element rdim1 = (Element) dim1.item(0);
                  NodeList nodedim = rdim1.getChildNodes();
                  fstDim.add(((Node) nodedim.item(0)).getNodeValue());
               }
            }

            for (int cntrAList = 2; cntrAList < valueAList.size(); cntrAList++) {

               NodeList idrowList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
               Element idRowElement = (Element) idrowList.item(0);
               NodeList idRowChNodeList = idRowElement.getChildNodes();
               finalMeasure.add(((Node) idRowChNodeList.item(0)).getNodeValue());
               djay = (((Node) idRowChNodeList.item(0)).getNodeValue());
               mulArr[vcount][cntrAList - 2] = djay;

               hmKey = (String) valueAList.get(cntrAList) + "_" + fstDimenVal.get(vcount);
               finalMeasure1.add(fstDim.get(vcount));// dimensoon 1
               finalMeasure2.add(hmKey);//
            }
         }

         // 
         removeDuplicate(finalMeasure1);
         //  
         for (int val = 2; val < valueAList.size(); val++) {
            String colNameData = (String) valueAList.get(val).toString().replace(' ', '_');
            chartXMLStrBuffer = new StringBuffer();

            chartXMLStrBuffer.append("<?xml version=\"1.0\"?><CHARTFX><COLUMNS>");
            chartXMLStrBuffer.append("<COLUMN NAME=\"" + idAList.get(0).toString()
                     + "\" TYPE=\"String\" DESCRIPTION=\""
                     + escape((String) valueAList.get(0).toString().replace(' ', '_')) + "\"/>");

            for (int colData = 0; colData < unidimenplusCtype.size(); colData++) {
               chartXMLStrBuffer.append("<COLUMN NAME=\"" + idAList.get(val) + "_"
                        + idUnidimenplusCtype.get(colData).toString().replace(' ', '_')
                        + "\" TYPE=\"Double\" DESCRIPTION=\"" + escape((String) unidimenplusCtype.get(colData))
                        + "\"/>");

            }
            chartXMLStrBuffer.append("</COLUMNS>");

            for (int colDataVal = 0; colDataVal < finalMeasure1.size(); colDataVal++) {

               chartXMLStrBuffer.append("<ROW ");
               if (!escape((String) finalMeasure1.get(colDataVal)).equalsIgnoreCase("N/A")) {
                  String dimName = (String) finalMeasure1.get(colDataVal);
                  if (dimName.length() > dimiensionLabelLength)
                     dimName = dimName.substring(0, (dimiensionLabelLength - 3)) + "...";
                  chartXMLStrBuffer.append(" " + idAList.get(0) + "=\"" + escape(dimName) + "\"");
               }
               String dimVal = finalMeasure1.get(colDataVal).toString().replace(' ', '_');
               for (int dataLine = 0; dataLine < rowNodeList.getLength(); dataLine++) {

                  String valFstdim = (String) fstDim.get(dataLine).toString().replace(' ', '_');

                  if (valFstdim.equals(dimVal) && !escape((String) mulArr[dataLine][val - 2]).equalsIgnoreCase("N/A")) {
                     chartXMLStrBuffer.append("  " + idAList.get(val) + "_" + idFstDimenVal.get(dataLine).toString()
                              + "=\"" + escape((String) mulArr[dataLine][val - 2]) + "\"");
                  }
               }
               chartXMLStrBuffer.append("></ROW>");
            }

            chartXMLStrBuffer.append("</CHARTFX>");
            chartXMLStr = chartXMLStrBuffer.toString();
            crossChartArry[val - 2] = chartXMLStr;

         }
      } catch (Exception e) {
         e.printStackTrace();
      }

      return crossChartArry;
   }

   public String getPivotXMLData (String path, String reportQueryDataXML) // Added by jaimin
   {
      path = reportQueryDataXML;
      String xmlString = "";
      try {
         // FileInputStream fstream = new FileInputStream(path);
         // DataInputStream in = new DataInputStream(fstream);
         // BufferedReader br = new BufferedReader(new InputStreamReader(in));
         // StringBuffer sb = new StringBuffer();

         // while ((path = br.readLine()) != null)
         // {
         // sb.append(path);
         // }
         xmlString = path;
         // xmlString = sb.toString();
         // in.close();

      } catch (Exception e) {
         log.error(e);
         e.printStackTrace();
      }
      return xmlString;

   }

   public String getReportType (String path) {
      reportType = "";
      try {

         reportType = path.substring(path.indexOf("<REPORTTYPES>") + "<REPORTTYPES>".length(), path
                  .indexOf("</REPORTTYPES>"));
      } catch (Exception e) {
         e.printStackTrace();
      }
      return reportType;
   }

   public String getReportTitle (String path) {
      String reportTitle = "";
      try {
         reportTitle = path.substring(path.indexOf("<TITLE>") + "<TITLE>".length(), path.indexOf("</TITLE>"));
      } catch (Exception e) {
         e.printStackTrace();
      }
      return reportTitle;
   }

   private static String escape (String xmlData) {
      StringBuffer sb = new StringBuffer();
      // regex to find non-UTF-8 supported characters and chop them off
      Pattern specialCharRegEx = Pattern.compile("[^\\t\\n\\r\\x20-\\x79]+");
      Matcher regExMatcher = specialCharRegEx.matcher(xmlData);
      StringBuffer regExSb = new StringBuffer();
      boolean regExpressionExist = false;
      regExpressionExist = regExMatcher.find();
      if (regExpressionExist) {
         while (regExpressionExist) {
            regExMatcher.appendReplacement(regExSb, "");
            regExpressionExist = regExMatcher.find();
         }
         regExMatcher.appendTail(regExSb);
         xmlData = regExSb.toString();
      }
      for (int i = 0, len = xmlData.length(); i < len; i++) {
         char c = xmlData.charAt(i);
         switch (c) {
            case '&':
               sb.append("&amp;");
               break;
            case '<':
               sb.append("&lt;");
               break;
            case '>':
               sb.append("&gt;");
               break;
            case '"':
               sb.append("&quot;");
               break;
            case '\'':
               sb.append("&apos;");
               break;
            default:
               sb.append(c);
         }
      }
      return sb.toString();
   }
}
