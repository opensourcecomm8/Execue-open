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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.execue.core.configuration.SystemConstants;

public class ExecueDAOUtil {

   private static final Logger              log                           = Logger.getLogger(ExecueDAOUtil.class);
   private static final Map<String, String> swiQueryFilePointerMap        = new HashMap<String, String>();
   private static final Map<String, String> qdataQueryFilePointerMap      = new HashMap<String, String>();
   private static final Map<String, String> sdataQueryFilePointerMap      = new HashMap<String, String>();
   private static final Map<String, String> uswhQueryFilePointerMap       = new HashMap<String, String>();
   private static final Map<String, String> uscaQueryFilePointerMap       = new HashMap<String, String>();
   private static final Map<String, String> nativeQueryFilePointerMap     = new HashMap<String, String>();
   private static final Map<String, String> audittrailQueryFilePointerMap = new HashMap<String, String>();
   static {
      buildQueryPointerMap(SystemConstants.DAO_SWI_QUERY_XML_FILE_POINTER_KEY, swiQueryFilePointerMap);
      buildQueryPointerMap(SystemConstants.DAO_QDATA_QUERY_XML_FILE_POINTER_KEY, qdataQueryFilePointerMap);
      buildQueryPointerMap(SystemConstants.DAO_SDATA_QUERY_XML_FILE_POINTER_KEY, sdataQueryFilePointerMap);
      buildQueryPointerMap(SystemConstants.DAO_USWH_QUERY_XML_FILE_POINTER_KEY, uswhQueryFilePointerMap);
      buildQueryPointerMap(SystemConstants.DAO_USCA_QUERY_XML_FILE_POINTER_KEY, uscaQueryFilePointerMap);
      buildQueryPointerMap(SystemConstants.DAO_AUDIT_TRAIL_QUERY_XML_FILE_POINTER_KEY, audittrailQueryFilePointerMap);
      buildQueryPointerMap(SystemConstants.DAO_NATIVE_QUERY_XML_FILE_POINTER_KEY, nativeQueryFilePointerMap);
   }

   private static void buildQueryPointerMap (String filePointer, Map<String, String> queryPointerMap) {
      try {
         Properties props = new Properties();
         InputStream is = ExecueDAOUtil.class.getResourceAsStream(filePointer);
         props.load(is);
         String key = null;
         for (Object queryPointerName : props.keySet()) {
            key = ((String) queryPointerName).trim();
            queryPointerMap.put(key, props.getProperty(key).trim());
         }
      } catch (IOException e) {
         // NOTE: -RG- Eaten intentionally
      }
   }

   public static Map<String, String> loadNativeQueriesMap (String queryFileKey) {
      return loadQueriesMap(nativeQueryFilePointerMap.get(queryFileKey));
   }

   public static Map<String, String> loadSWIQueriesMap (String queryFileKey) {
      return loadQueriesMap(swiQueryFilePointerMap.get(queryFileKey));
   }

   public static Map<String, String> loadSDataQueriesMap (String queryFileKey) {
      return loadQueriesMap(sdataQueryFilePointerMap.get(queryFileKey));
   }

   public static Map<String, String> loadQdataQueriesMap (String queryFileKey) {
      return loadQueriesMap(qdataQueryFilePointerMap.get(queryFileKey));
   }

   public static Map<String, String> loadUSWHQueriesMap (String queryFileKey) {
      return loadQueriesMap(uswhQueryFilePointerMap.get(queryFileKey));
   }

   public static Map<String, String> loadUSCAQueriesMap (String queryFileKey) {
      return loadQueriesMap(uscaQueryFilePointerMap.get(queryFileKey));
   }

   public static Map<String, String> loadAuditTrailQueriesMap (String queryFileKey) {
      return loadQueriesMap(audittrailQueryFilePointerMap.get(queryFileKey));
   }

   private static Map<String, String> loadQueriesMap (String queryFileName) {
      Map<String, String> queriesMap = new HashMap<String, String>();
      InputStream inputStream = null;
      try {
         inputStream = ExecueDAOUtil.class.getResourceAsStream(queryFileName);
         String queriesXML = ExecueCoreUtil.readFileAsString(inputStream);
         queriesMap = ExeCueXMLUtils.getDatabaseQueriesFromXML(queriesXML);
      } catch (FileNotFoundException e) {
         log.error(e, e);
      } catch (IOException e) {
         log.error(e, e);
      } finally {
         try {
            if (inputStream != null) {
               inputStream.close();
            }
         } catch (IOException e) {
            // Eaten;
         }
      }
      return queriesMap;
   }

}
