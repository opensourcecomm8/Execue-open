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


package com.execue.reporting.presentation.service.impl;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class XMLFileReader
{
   private static final Logger log = Logger.getLogger(XMLFileReader.class);
    public String getValueByNode(String xmlFilePath, String nodeName)
    {
        String retStr = "";
        DocumentBuilderFactory documentBuilderFactory = null;
        DocumentBuilder documentBuilder = null;
        Document document = null;
        try
        {
            InputStream iStream = getClass().getResourceAsStream(xmlFilePath);
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(iStream);
            document.getDocumentElement().normalize();

            NodeList headerNodeList = document.getElementsByTagName("CHARTAPPEARANCE");
            for (int cntr = 0; cntr < headerNodeList.getLength(); cntr++)
            {
                Node headerColNode = headerNodeList.item(cntr);
                Element headerElement = (Element) headerColNode;
                if (headerColNode.getNodeType() == Node.ELEMENT_NODE)
                {
                        NodeList idNodeList = headerElement.getElementsByTagName(nodeName.toUpperCase());
                        Element idElement = (Element) idNodeList.item(0);
                        NodeList idChNodeList = idElement.getChildNodes();
                        retStr = new String(((Node) idChNodeList.item(0)).getNodeValue());
                }
            }

        }
        catch (Exception e)
        {
           log.error(e);
            e.printStackTrace();
        }
        return retStr;
    }
}
