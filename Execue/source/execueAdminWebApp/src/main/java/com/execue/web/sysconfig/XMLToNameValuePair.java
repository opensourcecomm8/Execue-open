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


package com.execue.web.sysconfig;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLToNameValuePair {

   public void processXML (String xmlfileName) throws ParserConfigurationException, SAXException, IOException {
      DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      docBuilderFactory.setValidating(false);
      DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(XMLToNameValuePair.class.getResourceAsStream("/" + xmlfileName));
      NodeList recordList = doc.getElementsByTagName("execue-configuration");
      for (int i = 0; i < recordList.getLength(); i++) {
         Element recordNode = (Element) recordList.item(i);
         if (recordNode.getNodeType() == Node.ELEMENT_NODE) {
            getNameValue((Element) recordNode, "");
         }
      }
   }

   private void getNameValue (Element e, String key) {
      NodeList nodelist = e.getChildNodes();
      // System.out.println("key " + key);
      for (int i = 0; i < nodelist.getLength(); i++) {
         Node node = nodelist.item(i);
         if (node.getNodeType() == Node.ELEMENT_NODE) {
            // check for Leaf node
            if (isLeafNode(node)) {
               // printout
               // System.out.println(key + node.getNodeName() + "\t" + getElementValue((Element) node));
               // print insert statements
               System.out.println("insert into configuration values ('AnswersCatalog','" + key + node.getNodeName()
                        + "','" + getElementValue((Element) node) + "');");
            } else {
               String key1 = key + node.getNodeName() + ".";
               getNameValue((Element) node, key1);
            }
         }
      }

   }

   public boolean isLeafNode (Node node) {
      boolean singleChild = true;
      NodeList childNodeList = node.getChildNodes();
      for (int i = 0, len = childNodeList.getLength(); i < len; i++) {
         if (childNodeList.item(i).getNodeType() == Node.ELEMENT_NODE) {
            singleChild = false;
            break;
         }
      }

      return singleChild;
   }

   private static String getElementValue (Element element) {

      String textVal = null;
      try {
         textVal = element.getFirstChild().getNodeValue();

      } catch (Exception e) {
         e.printStackTrace();
      }
      return textVal;
   }

   public static void main (String[] args) throws Exception {
      XMLToNameValuePair xmlToPair = new XMLToNameValuePair();
      xmlToPair.processXML("execue-answers-catalog-configuration.xml");

   }

}
