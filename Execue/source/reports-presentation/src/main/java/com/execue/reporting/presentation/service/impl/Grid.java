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
 * <ExcCueLite - Reporting views> www.vbsoftindia.com FILENAME : Grid.java DEPENDENCIES : ReportView.java KNOWN ISSUES :
 * CREATED ON : 16 February,2009
 **********************************************************************************************************************/
package com.execue.reporting.presentation.service.impl;

import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.execue.report.presentation.tx.IGrid;
import com.execue.reporting.presentation.helper.PresentationTransformHelper;

/**
 * Grid Class
 * 
 * @author Jaimin Bhavsar
 */
public class Grid implements IGrid {

   /**
    * @param path
    * @return String
    */
   public String getHtmlGrid (String path, String reportQueryDataXML)// To Render normal Grid
   {
      String[] sourceTitleText = path.split("~");
      String source = sourceTitleText[0];
      String title = sourceTitleText[1];
      path = reportQueryDataXML;
      String htmlString = "";
      String fileName = "";
      ArrayList idAList = new ArrayList();
      ArrayList cTypeAList = new ArrayList();
      ArrayList dTypeAList = new ArrayList();
      ArrayList descrAList = new ArrayList();
      StringBuffer gridBuffer = new StringBuffer();
      NumberFormat number = NumberFormat.getInstance();
      String colorCode = "";
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         // Document document = documentBuilder.parse(file);
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document document = documentBuilder.parse(inStream);
         document.getDocumentElement().normalize();

         NodeList colNodeList = document.getElementsByTagName("COLUMN");

         gridBuffer.append("<table class='textDataTable'> ");
         gridBuffer.append("<tr><th colspan=100 align='center' valign='middle' class='gridTitle' >" + title
                  + "</th></tr>");
         gridBuffer.append("<tr>");

         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idAList.add(((Node) idChNodeList.item(0)).getNodeValue().toString());

               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               descrAList.add(((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_'));

               NodeList cTypeNodeList = colElement.getElementsByTagName("CTYPE");
               Element cTypeElement = (Element) cTypeNodeList.item(0);
               NodeList cTypeChNodeList = cTypeElement.getChildNodes();
               cTypeAList.add(((Node) cTypeChNodeList.item(0)).getNodeValue().toString().replace(' ', '_'));

               NodeList dTypeNodeList = colElement.getElementsByTagName("DTYPE");
               Element dTypeElement = (Element) dTypeNodeList.item(0);
               NodeList dTypeChNodeList = dTypeElement.getChildNodes();
               dTypeAList.add(((Node) dTypeChNodeList.item(0)).getNodeValue().toString().replace(' ', '_'));
            }
         }

         for (int cntr = 0; cntr < descrAList.size(); cntr++) {
            gridBuffer.append("<th align='center' valign='middle' class='textHeader' >"
                     + descrAList.get(cntr).toString().replace("_", " ") + "</th>");
         }
         gridBuffer.append("</tr>");

         NodeList rowNodeList = document.getElementsByTagName("VALUES");
         for (int cntr = 0; cntr < rowNodeList.getLength(); cntr++) {
            Node fstRowNode = rowNodeList.item(cntr);
            Element rowElement = (Element) fstRowNode;
            if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {
               gridBuffer.append("<tr>");
               if (cntr % 2 == 0) {
                  colorCode = "#FFFFFF";
               } else {
                  colorCode = "#EBEBEB";
               }
               for (int cntrAList = 0; cntrAList < idAList.size(); cntrAList++) {
                  NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                  Element idRowElement = (Element) idRowNodeList.item(0);
                  NodeList idRowChNodeList = idRowElement.getChildNodes();
                  // gridBuffer.append("<td WIDTH='80' align='right' valign='top' bgcolor='#E8F8FF' class='text'>"+
                  // getFormatString(((Node) idRowChNodeList.item(0)).getNodeValue().toString().replace("_"," ")) +
                  // "</td>");

                  // if(cntrAList == 0)
                  // {
                  // gridBuffer.append("<td WIDTH='80' align='left' valign='top' bgcolor='"+ colorCode +"'
                  // class='text'>"+ (((Node) idRowChNodeList.item(0)).getNodeValue().toString().replace("_"," ")) +
                  // "</td>");
                  // }else
                  {
                     if (cTypeAList.get(cntrAList).toString().equalsIgnoreCase("dimension")) {
                        // gridBuffer.append(getFormatString(((Node)
                        // idRowChNodeList.item(0)).getNodeValue().toString().replace("_"," "),colorCode));
                        gridBuffer.append("<td WIDTH='80' align='left' valign='top' class='textData'>"
                                 + ((Node) idRowChNodeList.item(0)).getNodeValue().toString().replace("_", " ")
                                 + "</td>");
                     } else {
                        String dTypeValue = dTypeAList.get(cntrAList).toString();
                        if (dTypeValue.equalsIgnoreCase("string") || dTypeValue.equalsIgnoreCase("varchar")) {
                           gridBuffer.append("<td align='left' class='textData'>"
                                    + ((Node) idRowChNodeList.item(0)).getNodeValue().toString().replace("_", " ")
                                    + "</td>");
                        } else {
                           gridBuffer.append(getFormatString(((Node) idRowChNodeList.item(0)).getNodeValue().toString()
                                    .replace("_", " "), colorCode));
                        }
                     }
                     // gridBuffer.append(getFormatString(((Node)
                     // idRowChNodeList.item(0)).getNodeValue().toString().replace("_"," "),colorCode));
                  }
               }
               gridBuffer.append("</tr>");
            }
         }
         gridBuffer.append("<tr><td class='textSource' colspan=50>Source: " + source + "</td></tr>");
         gridBuffer.append("<tr><td class='textSource' colspan=50>www.semantifi.com</td></tr>");
         gridBuffer.append("</table>");
      } catch (Exception e) {
         e.printStackTrace();
      }
      htmlString = gridBuffer.toString();
      return htmlString;
   }

   /**
    * @param path
    * @return String
    */
   public String getHtmlGroupGrid (String path, String reportQueryDataXML) {

      String[] sourceTitleText = path.split("~");
      String source = sourceTitleText[0];
      String title = sourceTitleText[1];
      path = reportQueryDataXML;
      String htmlString = "", fileName = "", groupByID = "";
      ArrayList groupAList = new ArrayList();
      ArrayList idAList = new ArrayList();
      ArrayList descrAList = new ArrayList();
      ArrayList dTypeAList = new ArrayList();
      StringBuffer groupGridBuffer = new StringBuffer();
      int groupBycntr = 0;
      String colorCode = "";
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         // Document document = documentBuilder.parse(file);
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document document = documentBuilder.parse(inStream);
         document.getDocumentElement().normalize();
         NodeList colNodeList = document.getElementsByTagName("COLUMN");

         groupGridBuffer.append("<table class='textDataTable'>");
         groupGridBuffer.append("<tr><th colspan=100 align='center' valign='middle' class='gridTitle' >" + title
                  + "</th></tr>");
         groupGridBuffer.append("<tr>");

         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               descrAList.add(((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_'));

               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList idChNodeList = idElement.getChildNodes();
               idAList.add(((Node) idChNodeList.item(0)).getNodeValue().toString());

               NodeList dTypeNodeList = colElement.getElementsByTagName("DTYPE");
               Element dTypeElement = (Element) dTypeNodeList.item(0);
               NodeList dTypeChNodeList = dTypeElement.getChildNodes();
               dTypeAList.add(((Node) dTypeChNodeList.item(0)).getNodeValue().toString().replace(' ', '_'));

               NodeList grpNodeList = colElement.getElementsByTagName("GROUPBY");
               Element grpElement = (Element) grpNodeList.item(0);
               if (grpElement == null) {
                  groupBycntr++;
                  continue;
               }
               groupAList.add(cntr);

            }
         }

         if (groupBycntr != idAList.size()) {
            for (int cntr = 0; cntr < descrAList.size(); cntr++) {
               groupGridBuffer.append("<th align='center' valign='middle' width='104' height='24' class='textHeader' >"
                        + descrAList.get(cntr).toString().replace("_", " ") + "</th>");
            }
            groupGridBuffer.append("</tr>");

            for (int ALCntr = 0; ALCntr < groupAList.size(); ALCntr++) {
               Node fstColNode = colNodeList.item(Integer.parseInt(groupAList.get(ALCntr).toString()));
               Element colElement = (Element) fstColNode;
               if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
                  NodeList idNodeList = colElement.getElementsByTagName("ID");
                  Element idElement = (Element) idNodeList.item(0);
                  NodeList idChNodeList = idElement.getChildNodes();
                  groupByID = ((Node) idChNodeList.item(0)).getNodeValue();
               }
            }

            NodeList rowNodeList = document.getElementsByTagName("VALUES");
            ArrayList mainAList = new ArrayList();
            Set setID = new LinkedHashSet();
            for (int cntr = 0; cntr < rowNodeList.getLength(); cntr++) {
               Node fstRowNode = rowNodeList.item(cntr);
               Element rowElement = (Element) fstRowNode;
               if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {
                  NodeList idRowNodeListNew = rowElement.getElementsByTagName(groupByID);
                  Element idRowElementNew = (Element) idRowNodeListNew.item(0);
                  NodeList idRowChNodeListNew = idRowElementNew.getChildNodes();
                  setID.add(((Node) idRowChNodeListNew.item(0)).getNodeValue());

                  ArrayList dataAList = new ArrayList();
                  for (int cntrAList = 0; cntrAList < idAList.size(); cntrAList++) {
                     NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                     Element idRowElement = (Element) idRowNodeList.item(0);
                     NodeList idRowChNodeList = idRowElement.getChildNodes();
                     dataAList.add(((Node) idRowChNodeList.item(0)).getNodeValue());
                  }
                  mainAList.add(dataAList);
               }
            }
            Iterator it = setID.iterator();
            Object element = null;
            HashMap mainMap = new LinkedHashMap();
            while (it.hasNext()) {
               element = it.next();
               ArrayList mapAList = new ArrayList();
               mainMap.put((String) element, mapAList);
            }
            Set keySet = mainMap.keySet();
            Object keyArr[] = keySet.toArray();

            for (int mapCntr = 0; mapCntr < mainMap.size(); mapCntr++) {
               for (int alCntr = 0; alCntr < mainAList.size(); alCntr++) {
                  ArrayList alTemp = (ArrayList) mainAList.get(alCntr);
                  // if (keyArr[mapCntr].equals(alTemp.get(0).toString()))
                  if (keyArr[mapCntr].equals(alTemp.get(Integer.parseInt(groupAList.get(0).toString())).toString())) {
                     ArrayList mapTempList = (ArrayList) mainMap.get(keyArr[mapCntr]);
                     mapTempList.add((ArrayList) mainAList.get(alCntr));
                  }
               }
            }
            int tempCntr = 0;

            for (int mapCntr = 0; mapCntr < mainMap.size(); mapCntr++) {
               ArrayList aListFrMap = (ArrayList) mainMap.get(keyArr[mapCntr]);
               tempCntr = 0;
               for (int innerAList = 0; innerAList < aListFrMap.size(); innerAList++) {
                  ArrayList innerAListFrMap = (ArrayList) aListFrMap.get(innerAList);
                  groupGridBuffer.append("<tr>");

                  if (innerAList % 2 == 0) {
                     colorCode = "#FFFFFF";
                  } else {
                     colorCode = "#EBEBEB";
                  }

                  for (int innerAList2 = 0; innerAList2 < innerAListFrMap.size(); innerAList2++) {
                     String retStr = "";
                     if (innerAList2 == 0 && tempCntr == 0) {
                        NumberFormat number = NumberFormat.getInstance();

                        groupGridBuffer.append("<td WIDTH='80' align='left' valign='middle' class='textData' rowspan='"
                                 + aListFrMap.size() + "'>"
                                 + innerAListFrMap.get(innerAList2).toString().replace("_", " ") + "</td>");
                        tempCntr++;
                     }
                     if (innerAList2 > 0 && innerAList2 <= innerAListFrMap.size()) {
                        // groupGridBuffer.append("<td width='102' align='right' valign='top' bgcolor='#E8F8FF'
                        // class='text'>"+getFormatString(innerAListFrMap.get(innerAList2).toString().replace("_","
                        // "))+"</td>");
                        String dTypeValue = dTypeAList.get(innerAList2).toString();
                        if (dTypeValue.equalsIgnoreCase("string") || dTypeValue.equalsIgnoreCase("varchar")) {
                           groupGridBuffer.append("<td align='left' class='textData'>"
                                    + innerAListFrMap.get(innerAList2).toString().replace("_", " ") + "</td>");
                        } else {
                           groupGridBuffer.append(getFormatString(innerAListFrMap.get(innerAList2).toString().replace(
                                    "_", " "), colorCode));
                        }
                     }
                  }
                  groupGridBuffer.append("</tr>");
               }
            }
            groupGridBuffer.append("<tr><td class='textSource' colspan=50>Source: " + source + "</td></tr>");
            groupGridBuffer.append("<tr><td class='textSource' colspan=50>www.semantifi.com</td></tr>");
            groupGridBuffer.append("</table>");
            htmlString = groupGridBuffer.toString();
         } else {
            htmlString = getHtmlGrid(source + "~" + title, path);
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
      return htmlString;

   }

   public String getHtmlAcrossGrid (String path, String reportQueryDataXML) {

      String[] sourceTitleText = path.split("~");
      String source = sourceTitleText[0];
      String title = sourceTitleText[1];
      path = reportQueryDataXML;
      String htmlString = "", fileName = "", groupByID = "";
      ArrayList groupAList = new ArrayList();
      ArrayList acrossAList = new ArrayList();
      ArrayList cTypeAList = new ArrayList();
      ArrayList dTypeAList = new ArrayList();
      ArrayList idAList = new ArrayList();
      ArrayList descrAList = new ArrayList();
      ArrayList numAList = new ArrayList();
      StringBuffer groupGridBuffer = new StringBuffer();
      Set sortedSetAcr = new LinkedHashSet();
      int acrossCntr = 0, groupBycntr = 0;
      int totalGRPMembers = 0;
      int totalACRMembers = 0;
      String colorCode = "";
      try {
         DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
         // Document document = documentBuilder.parse(file);
         InputSource inStream = new InputSource();
         inStream.setCharacterStream(new StringReader(path));
         Document doc = documentBuilder.parse(inStream);
         doc.getDocumentElement().normalize();
         NodeList colNodeList = doc.getElementsByTagName("COLUMN");

         for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
            Node fstColNode = colNodeList.item(cntr);
            Element colElement = (Element) fstColNode;
            if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
               NodeList valueNodeList = colElement.getElementsByTagName("DESC");
               Element valueElement = (Element) valueNodeList.item(0);
               NodeList valueChNodeList = valueElement.getChildNodes();
               descrAList.add(((Node) valueChNodeList.item(0)).getNodeValue().toString().replace(' ', '_'));

               NodeList cTypeNodeList = colElement.getElementsByTagName("CTYPE");
               Element cTypeElement = (Element) cTypeNodeList.item(0);
               NodeList cTypeChNodeList = cTypeElement.getChildNodes();
               cTypeAList.add(((Node) cTypeChNodeList.item(0)).getNodeValue().toString());

               NodeList dTypeNodeList = colElement.getElementsByTagName("DTYPE");
               Element dTypeElement = (Element) dTypeNodeList.item(0);
               NodeList dTypeChNodeList = dTypeElement.getChildNodes();
               dTypeAList.add(((Node) dTypeChNodeList.item(0)).getNodeValue().toString().replace(' ', '_'));

               NodeList idNodeList = colElement.getElementsByTagName("ID");
               Element idElement = (Element) idNodeList.item(0);
               NodeList tmpIdChNodeList = idElement.getChildNodes();
               idAList.add(((Node) tmpIdChNodeList.item(0)).getNodeValue().toString());

               NodeList acrNodeList = colElement.getElementsByTagName("ACROSS");
               Element acrElement = (Element) acrNodeList.item(0);
               if (acrElement == null) {
                  acrossCntr++;
                  continue;
               }
               acrossAList.add(cntr);

            }
         }
         if (acrossCntr != idAList.size()) {
            for (int cntr = 0; cntr < colNodeList.getLength(); cntr++) {
               Node fstColNode = colNodeList.item(cntr);
               Element colElement = (Element) fstColNode;
               if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
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
                  if (grpElement == null) {
                     groupBycntr++;
                     continue;
                  }
                  groupAList.add(cntr);
               }
            }
            for (int arCntr = 0; arCntr < cTypeAList.size(); arCntr++) {
               if (cTypeAList.get(arCntr).toString().equalsIgnoreCase("MEASURE")
                        || cTypeAList.get(arCntr).toString().equalsIgnoreCase("ID")) {
                  numAList.add(arCntr);
               }
            }

            NodeList rowNodeList = doc.getElementsByTagName("VALUES");
            // re-modify the data object for handling missing values.
            if ((totalGRPMembers * totalACRMembers) != rowNodeList.getLength()) {
               PresentationTransformHelper.normalizeDocument(doc, colNodeList, totalGRPMembers);
            }
            // end of re-modify

            for (int cntr = 0; cntr < rowNodeList.getLength(); cntr++) {
               Node fstRowNode = rowNodeList.item(cntr);
               Element rowElement = (Element) fstRowNode;
               if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {

                  for (int cntrAList = 0; cntrAList < idAList.size(); cntrAList++) {
                     NodeList idRowNodeList = rowElement.getElementsByTagName(idAList.get(
                              Integer.parseInt(acrossAList.get(0).toString())).toString());
                     Element idRowElement = (Element) idRowNodeList.item(0);
                     NodeList idRowChNodeList = idRowElement.getChildNodes();
                     sortedSetAcr.add(((Node) idRowChNodeList.item(0)).getNodeValue());
                  }
               }
            }
            groupGridBuffer.append("<table class='textDataTable'>");
            groupGridBuffer.append("<tr><th colspan=100 align='center' valign='middle' class='gridTitle' >" + title
                     + "</th></tr>");
            Iterator it = sortedSetAcr.iterator();
            for (int tblRowCntr = 0; tblRowCntr < 2; tblRowCntr++) {
               groupGridBuffer.append("<tr>");
               if (tblRowCntr == 0) {
                  groupGridBuffer.append("<td class='textHeader'/>");
                  while (it.hasNext()) {
                     Object element = it.next();
                     groupGridBuffer.append("<th align='center' class='textHeader' colspan=" + numAList.size() + ">"
                              + ((String) element).replace("_", " ") + "</th>");
                  }
               } else if (tblRowCntr == 1) {
                  groupGridBuffer.append("<th align='center' class='textHeader'>"
                           + descrAList.get(0).toString().replace("_", " ") + "</th>");
                  for (int temp = 0; temp < sortedSetAcr.size(); temp++) {
                     for (int cntr = 0; cntr < numAList.size(); cntr++) {
                        groupGridBuffer.append("<th align='center' class='textHeader'>"
                                 + descrAList.get(Integer.parseInt(numAList.get(cntr).toString())).toString().replace(
                                          "_", " ") + "</th>");
                     }
                  }
               }
               groupGridBuffer.append("</tr>");
            }
            for (int ALCntr = 0; ALCntr < groupAList.size(); ALCntr++) {
               Node fstColNode = colNodeList.item(Integer.parseInt(groupAList.get(ALCntr).toString()));
               Element colElement = (Element) fstColNode;
               if (fstColNode.getNodeType() == Node.ELEMENT_NODE) {
                  NodeList idNodeList = colElement.getElementsByTagName("ID");
                  Element idElement = (Element) idNodeList.item(0);
                  NodeList tmpIdChNodeList = idElement.getChildNodes();
                  groupByID = ((Node) tmpIdChNodeList.item(0)).getNodeValue();
               }
            }
            NodeList rowNodeListNew = doc.getElementsByTagName("VALUES");
            ArrayList mainAList = new ArrayList();
            Set setID = new LinkedHashSet();
            for (int cntr = 0; cntr < rowNodeListNew.getLength(); cntr++) {
               Node fstRowNode = rowNodeListNew.item(cntr);
               Element rowElement = (Element) fstRowNode;
               if (fstRowNode.getNodeType() == Node.ELEMENT_NODE) {
                  NodeList idrowNodeListNewNew = rowElement.getElementsByTagName(groupByID);
                  Element idRowElementNew = (Element) idrowNodeListNewNew.item(0);
                  NodeList idRowChNodeListNew = idRowElementNew.getChildNodes();
                  setID.add(((Node) idRowChNodeListNew.item(0)).getNodeValue());

                  ArrayList dataAList = new ArrayList();
                  for (int cntrAList = 0; cntrAList < idAList.size(); cntrAList++) {
                     NodeList idrowNodeListNew = rowElement.getElementsByTagName(idAList.get(cntrAList).toString());
                     Element idRowElement = (Element) idrowNodeListNew.item(0);
                     NodeList idRowChNodeList = idRowElement.getChildNodes();
                     dataAList.add(((Node) idRowChNodeList.item(0)).getNodeValue());
                  }
                  mainAList.add(dataAList);
               }
            }
            Iterator itSet = setID.iterator();
            Object element = null;
            HashMap mainMap = new LinkedHashMap();
            while (itSet.hasNext()) {
               element = itSet.next();
               ArrayList mapAList = new ArrayList();
               mainMap.put((String) element, mapAList);
            }
            Set keySet = mainMap.keySet();
            Object keyArr[] = keySet.toArray();

            for (int mapCntr = 0; mapCntr < mainMap.size(); mapCntr++) {
               for (int alCntr = 0; alCntr < mainAList.size(); alCntr++) {
                  ArrayList alTemp = (ArrayList) mainAList.get(alCntr);
                  // if (keyArr[mapCntr].equals(alTemp.get(0).toString()))
                  if (keyArr[mapCntr].equals(alTemp.get(Integer.parseInt(groupAList.get(0).toString())).toString())) {
                     ArrayList mapTempList = (ArrayList) mainMap.get(keyArr[mapCntr]);
                     mapTempList.add((ArrayList) mainAList.get(alCntr));
                  }
               }
            }
            for (int mapCntr = 0; mapCntr < mainMap.size(); mapCntr++) {
               if (mapCntr % 2 == 0) {
                  colorCode = "#FFFFFF";
               } else {
                  colorCode = "#EBEBEB";
               }
               ArrayList aListFrMap = (ArrayList) mainMap.get(keyArr[mapCntr]);
               groupGridBuffer.append("<tr>");
               groupGridBuffer.append("<td align='left' class='textData'>"
                        + (keyArr[mapCntr].toString().replace("_", " ")) + "</td>");

               for (int cnt = 0; cnt < aListFrMap.size(); cnt++) {

                  ArrayList innerAListFrMap = (ArrayList) aListFrMap.get(cnt);
                  for (int c = 0; c < numAList.size(); c++) {
                     // groupGridBuffer.append("<td width='102' align='left' valign='top' bgcolor='#E8F8FF'
                     // class='text'>");
                     // groupGridBuffer.append(innerAListFrMap.get(Integer.parseInt(numAList.get(c).toString())));
                     // groupGridBuffer.append("</td>");
                     String dTypeValue = dTypeAList.get(Integer.parseInt(numAList.get(c).toString())).toString();
                     if (dTypeValue.equalsIgnoreCase("string") || dTypeValue.equalsIgnoreCase("varchar")) {
                        groupGridBuffer.append("<td align='left' class='textData'>"
                                 + innerAListFrMap.get(Integer.parseInt(numAList.get(c).toString())).toString()
                                          .replace("_", " ") + "</td>");
                     } else {
                        groupGridBuffer.append(getFormatString(innerAListFrMap.get(
                                 Integer.parseInt(numAList.get(c).toString())).toString(), colorCode));
                     }
                  }
               }
               groupGridBuffer.append("</tr>");
            }
            groupGridBuffer.append("<tr><td class='textSource' colspan=50>Source: " + source + "</td></tr>");
            groupGridBuffer.append("<tr><td class='textSource' colspan=50>www.semantifi.com</td></tr>");
            groupGridBuffer.append("</table>");
            htmlString = groupGridBuffer.toString();
         } else {
            htmlString = getHtmlGrid(source + "~" + title, path);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return htmlString;
   }

   private String getFormatString (String inputStr, String colorCode) {
      String retStr = "";
      NumberFormat number = new DecimalFormat("#0.00");

      try {
         try {
            Double temp = Double.parseDouble(inputStr.replace("_", " "));

            NumberFormat numberFormat = new DecimalFormat("#,###.##");
            numberFormat.setMaximumFractionDigits(2);
            String temp1 = numberFormat.format(temp);
            // retStr = number.format(temp);
            // retStr= "<td WIDTH='80' align='right' valign='top' bgcolor='#E8F8FF' class='text'>"+ number.format(temp)
            // + "</td>";
            retStr = "<td align='right' bgcolor='#ffffff' class='textDataItem'>" + temp1 + "</td>";
         } catch (NumberFormatException e) {
            retStr = "<td align='right' bgcolor='#ffffff' class='textDataItem'>" + inputStr.replace("_", " ") + "</td>";
            // retStr = inputStr.replace("_"," ");
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      return retStr;
   }
}
