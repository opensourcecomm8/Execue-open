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


package com.execue.das.datatransfer.etl.helper;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.util.Connection;
import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.scriptella.bean.Etl;
import com.execue.das.datatransfer.etl.scriptella.bean.Query;
import com.execue.das.datatransfer.etl.scriptella.bean.Script;
import com.thoughtworks.xstream.XStream;

public class ScriptellaETLInputHelper {

   private static final Logger log = Logger.getLogger(ScriptellaETLInputHelper.class);

   /**
    * Prepares xml that will be used by scriptella
    * 
    * @param etlInput
    * @return
    */
   public static String prepareScriptellaInput (ETLInput etlInput) {
      XStream xstream = new XStream();
      
      xstream.alias("etl", Etl.class);
      xstream.alias("connection", Connection.class);
      
      xstream.useAttributeFor(Connection.class, "id");
      xstream.useAttributeFor(Connection.class, "driver");
      xstream.useAttributeFor(Connection.class, "url");
      xstream.useAttributeFor(Connection.class, "user");
      xstream.useAttributeFor(Connection.class, "password");
      xstream.useAttributeFor(Connection.class, "lazyInit");
      
      xstream.addImplicitCollection(Etl.class, "QueryList");
      xstream.addImplicitCollection(Connection.class, "initialContextEnvironmentProperties");
      
      Etl e = new Etl("etl.xml file for ETL generation");

      if ("properties".equals(etlInput.getSourceConnectionPropsMap().get("type"))) {
         e.addConnection(new Connection("source", (String) etlInput.getSourceConnectionPropsMap().get("driver"),
                  (String) etlInput.getSourceConnectionPropsMap().get("url"), (String) etlInput
                           .getSourceConnectionPropsMap().get("user"), (String) etlInput.getSourceConnectionPropsMap()
                           .get("password")));
      } else if ("jndi".equals(etlInput.getSourceConnectionPropsMap().get("type"))) {
         e.addConnection(new Connection("source", (String) etlInput.getSourceConnectionPropsMap().get("driver"),
                  (String) etlInput.getSourceConnectionPropsMap().get("url")));
      } else {
         e.addConnection(new Connection("source", (String) etlInput.getSourceConnectionPropsMap().get("driver"),
                  (String) etlInput.getSourceConnectionPropsMap().get("url"),
                  getInitialContextEnvironmentPropertiesAsListOfStrings((Hashtable<String, String>) etlInput
                           .getSourceConnectionPropsMap().get("initial-context-environment-properties"))));
      }

      if ("properties".equals(etlInput.getTargetConnectionPropsMap().get("type"))) {
         e.addConnection(new Connection("target", (String) etlInput.getTargetConnectionPropsMap().get("driver"),
                  (String) etlInput.getTargetConnectionPropsMap().get("url"), (String) etlInput
                           .getTargetConnectionPropsMap().get("user"), (String) etlInput.getTargetConnectionPropsMap()
                           .get("password")));
      } else if ("jndi".equals(etlInput.getTargetConnectionPropsMap().get("type"))) {
         e.addConnection(new Connection("target", (String) etlInput.getTargetConnectionPropsMap().get("driver"),
                  (String) etlInput.getTargetConnectionPropsMap().get("url")));
      } else {
         e.addConnection(new Connection("target", (String) etlInput.getTargetConnectionPropsMap().get("driver"),
                  (String) etlInput.getTargetConnectionPropsMap().get("url"),
                  getInitialContextEnvironmentPropertiesAsListOfStrings((Hashtable<String, String>) etlInput
                           .getSourceConnectionPropsMap().get("initial-context-environment-properties"))));
      }
      
      // e.addConnection(new Connections("log", "text"));
      
      xstream.addImplicitCollection(Etl.class, "connections");
      
      xstream.aliasField("connection-id", Query.class, "connection_id");
      xstream.alias("query", Query.class);
      xstream.useAttributeFor(Query.class, "connection_id");
      
      xstream.aliasField("connection-id", Script.class, "connection_id");
      xstream.useAttributeFor(Script.class, "connection_id");
      
      xstream.aliasField("lazy-init", Connection.class, "lazyInit");

      e.addQuery(new Query("source", etlInput.getSourceQuery(), new Script("target", etlInput.getTargetInsert())));
      // String xml = "<!DOCTYPE etl SYSTEM \"http://scriptella.javaforge.com/dtd/etl.dtd\">";
      String xml = xstream.toXML(e);
      // TODO: Below code is hack and needs to be fixed -- Begin
      xml = xml.replaceAll("<sourceQuery>", "");
      xml = xml.replaceAll("</sourceQuery>", "");
      xml = xml.replaceAll("<insertQuery>", "");
      xml = xml.replaceAll("</insertQuery>", "");
      xml = xml.replaceAll("<string>", "");
      xml = xml.replaceAll("</string>", "");

      if (log.isDebugEnabled()) {
         log.debug(xml);
      }
      return xml;
   }

   private static List<String> getInitialContextEnvironmentPropertiesAsListOfStrings (
            Hashtable<String, String> initialContextEnvironmentPropertiesTable) {
      List<String> initialContextEnvironmentProperties = new ArrayList<String>();
      Set<String> keySet = initialContextEnvironmentPropertiesTable.keySet();
      for (String key : keySet) {
         initialContextEnvironmentProperties.add(key + "=" + initialContextEnvironmentPropertiesTable.get(key));
      }
      return initialContextEnvironmentProperties;
   }
}
