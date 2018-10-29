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


package com.execue.util;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;

public class XMLUtilities {

  private static final Logger log = Logger.getLogger(XMLUtilities.class);

  private XMLUtilities() {

  }

  public static String getObjectAsXMLString(Object targetObject) {
    String content = null;
    XStream xstream = new XStream();
    content = xstream.toXML(targetObject);
    return content;
  }

  public static Object getXMLStringAsObject(String xmlContent) {
    Object targetObject = null;
    XStream xstream = new XStream();
    targetObject = xstream.fromXML(xmlContent);
    return targetObject;
  }
}
