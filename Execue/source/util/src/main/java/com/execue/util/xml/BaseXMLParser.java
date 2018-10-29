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


package com.execue.util.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.execue.util.EncryptionUtil;
import com.execue.util.conversion.DateConversion;

/**
 * @author Gopal
 * @since Sep 20 2006
 */
public class BaseXMLParser {

  // Configuration parameters for XML Parser used to parse a XML file
  private static final boolean AWARENESS = true;

  private static final boolean IGNORE_WHITE_SPACE = false;

  private static final boolean IGNORE_COMMENTS = false;

  private static final boolean PUT_CDATA_INTO_TEXT = false;

  private static final boolean CREATE_ENTITY_REFERENCES = true;

  private static DocumentBuilderFactory dbf;

  static {

    String jaxpPropertyName = "javax.xml.parsers.DocumentBuilderFactory";
    if (System.getProperty(jaxpPropertyName) == null) {
      String apacheXercesPropertyValue = "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl";
      System.setProperty(jaxpPropertyName, apacheXercesPropertyValue);
    }
    // To Make Sure We Are Using Xerces Parser Implementation Only

    dbf = DocumentBuilderFactory.newInstance();

    dbf.setNamespaceAware(AWARENESS);
    dbf.setIgnoringComments(IGNORE_COMMENTS);
    dbf.setIgnoringElementContentWhitespace(IGNORE_WHITE_SPACE);
    dbf.setCoalescing(PUT_CDATA_INTO_TEXT);
    dbf.setExpandEntityReferences(CREATE_ENTITY_REFERENCES);
  }

  private BaseXMLParser() {
    // To avoid object creation
  }

  public static Document getXMLDoc(String fileURI) throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = null;
    //doc = db.parse(new ByteArrayInputStream(getFileContentAsStringPlain(fileURI).getBytes("UTF-8")));
    doc = db.parse((new DateConversion()).getClass().getResourceAsStream(fileURI));
    return doc;
  }

  private static String getEncryptedFileContentAsString(String fileURI) {
    String xmlContent = "";
    try {
      File file = new File(fileURI);
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      StringBuffer sb = new StringBuffer();
      while ((line = br.readLine()) != null) {
        sb.append(EncryptionUtil.decrypt(line));
      }
      br.close();
      xmlContent = sb.toString();
    } catch (Exception exp) {
      //WE should not handle this
      //TODO: -Gopal- Clean this stuff
    }
    return xmlContent;
  }

//  private static String getFileContentAsString(String fileURI) {
//    String xmlContent = "";
//    try {
//      File file = new File(fileURI);
//      BufferedReader br = new BufferedReader(new FileReader(file));
//      String line = null;
//      StringBuffer sb = new StringBuffer();
//      while ((line = br.readLine()) != null) {
//        if (ConfigurationContext.getProperty(ExecueConstants.ENCRYPT_CONFIG_FLAG) == null
//            || Boolean.valueOf(ConfigurationContext.getProperty(ExecueConstants.ENCRYPT_CONFIG_FLAG)).booleanValue()) {
//          sb.append(EncryptionUtil.decrypt(line));
//        } else {
//          sb.append(line.trim());
//        }
//      }
//      br.close();
//      xmlContent = sb.toString();
//    } catch (Exception exp) {
//      //WE should not handle this
//      //TODO: -Gopal- Clean this stuff
//    }
//    return xmlContent;
//  }

  private static String getFileContentAsStringPlain(String fileURI) {
    String xmlContent = "";
    try {
      File file = new File(fileURI);
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      StringBuffer sb = new StringBuffer();
      while ((line = br.readLine()) != null) {
        sb.append(line.trim());
      }
      br.close();
      xmlContent = sb.toString();
    } catch (Exception exp) {
      //WE should not handle this
      //TODO: -Gopal- Clean this stuff
    }
    return xmlContent;
  }

  public static Document getNewXMLDoc() throws ParserConfigurationException, SAXException, IOException {
    DocumentBuilder db = dbf.newDocumentBuilder();
    return db.newDocument();
  }

  public static void saveXMLFile(String fileURI, Document doc) throws TransformerException {
    // Output of the XML document
    DOMSource ds = new DOMSource(doc);
    StreamResult sr = new StreamResult(new File(fileURI));
    TransformerFactory tf = TransformerFactory.newInstance();
    Transformer trans = tf.newTransformer();
    trans.transform(ds, sr);
  }
}