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


package com.execue.das.datatransfer.etl.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.configuration.ExecueConfiguration;
import com.execue.core.exception.ConfigurationException;
import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.exception.ETLException;
import com.execue.das.exception.DataAccessServicesExceptionCodes;

public class KettleETLServiceTest {

   ETLInput            etlInput = new ETLInput();

   ExecueConfiguration execueConfiguration;

   private Logger      logger   = Logger.getLogger(KettleETLServiceTest.class);

   // boolean test = true;

   @Before
   public void setUp () {

      try {
         List<String> filenames = new ArrayList<String>();
         filenames.add("/bean-config/answers-catalog-test/kettle-etl-input.xml");
         ExecueConfiguration execueConfiguration = new ExecueConfiguration(filenames);
         Map<String, Object> remoteMap = new HashMap<String, Object>();
         Map<String, Object> localMap = new HashMap<String, Object>();

         remoteMap.put("driver", execueConfiguration.getProperty("remote-asset-properties.driver"));
         remoteMap.put("url", execueConfiguration.getProperty("remote-asset-properties.url"));
         remoteMap.put("user", execueConfiguration.getProperty("remote-asset-properties.userid"));
         remoteMap.put("password", execueConfiguration.getProperty("remote-asset-properties.password"));
         etlInput.setSourceConnectionPropsMap(remoteMap);
         /*
          * for localConnectionProperties
          */
         logger.debug(execueConfiguration.getProperty("local-asset-properties.driver"));
         localMap.put("driver", execueConfiguration.getProperty("local-asset-properties.driver"));
         localMap.put("url", execueConfiguration.getProperty("local-asset-properties.url"));
         localMap.put("user", execueConfiguration.getProperty("local-asset-properties.userid"));
         localMap.put("password", execueConfiguration.getProperty("local-asset-properties.password"));
         etlInput.setTargetConnectionPropsMap(localMap);
         etlInput.setSourceQuery(execueConfiguration.getProperty("select-statement"));
         etlInput.setTargetInsert(execueConfiguration.getProperty("insert-statement"));
      } catch (ConfigurationException e) {
         e.printStackTrace();
      }

   }

   @After
   public void tearDown () {

   }

   @Test
   public void testKettleTransformationSuccessCase () throws ETLException {
      try {
         KettleETLServiceImpl kettleETLImpl = new KettleETLServiceImpl();

         kettleETLImpl.executeETLProcess(etlInput);

      } catch (ETLException e) {

         throw new ETLException(DataAccessServicesExceptionCodes.DEFAULT_ETL_KETTLE_EXCEPTION_CODE, "KettleException",
                  e);
      }
   }

}
