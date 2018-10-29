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


package com.execue.core.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;

public class ExeCueXMLUtils {

   private ExeCueXMLUtils () {

   }

   public static String getXMLStringFromObject (Object object) {
      XStream xStream = new XStream();
      return xStream.toXML(object);
   }

   public static Object getObjectFromXMLString (String xmlString) {
      XStream xStream = new XStream();
      return xStream.fromXML(xmlString);
   }

   public static Map<String, String> getDatabaseQueriesFromXML (String xmlData) {
      Map<String, String> queriesMap = new HashMap<String, String>();
      try {
         InputSource inputSource = new InputSource(new ByteArrayInputStream(xmlData.getBytes()));
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder docBuilder = factory.newDocumentBuilder();
         Document document = docBuilder.parse(inputSource);
         Element rootElement = document.getDocumentElement();
         NodeList nodes = rootElement.getChildNodes();
         for (int count = 0; count < nodes.getLength(); count++) {
            Node node = nodes.item(count);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
               String queryName = node.getNodeName();
               String query = node.getTextContent();
               queriesMap.put(queryName, query);
            }
         }
      } catch (ParserConfigurationException parserConfigurationException) {
         parserConfigurationException.printStackTrace();
      } catch (SAXException saxException) {
         saxException.printStackTrace();
      } catch (IOException ioException) {
         ioException.printStackTrace();
      }
      return queriesMap;
   }

}
