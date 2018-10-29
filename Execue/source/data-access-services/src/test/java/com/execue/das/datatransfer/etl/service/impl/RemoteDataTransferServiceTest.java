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

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.exception.ConfigurationException;
import com.execue.das.datatransfer.etl.bean.DataTransferInput;
import com.execue.das.datatransfer.etl.bean.DataTransferStatus;
import com.execue.das.datatransfer.etl.bean.DataTransferQuery;
import com.execue.das.datatransfer.etl.exception.RemoteDataTransferException;
import com.execue.das.datatransfer.etl.service.IRemoteDataTransferService;
import com.execue.dataaccess.configuration.impl.DataAccessConfigurableService;
import com.thoughtworks.xstream.XStream;

public class RemoteDataTransferServiceTest {

   private ApplicationContext springContext;
   private DataTransferInput  goodDataTransferInput;
   private DataTransferInput  badDataTransferInput;

   @Before
   public void setUp () throws ConfigurationException {

      springContext = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-configuration.xml",
               "/bean-config/spring-hibernate.xml", "/bean-config/execue-dataaccess.xml",
               "/bean-config/execue-swi.xml", "/bean-config/execue-query-generation.xml",
               "/bean-config/execue-answers-catalog.xml" });

      DataAccessConfigurableService dataAccessConfigurableService = (DataAccessConfigurableService) springContext
               .getBean("dataAccessConfigurableService");
      String goodFile = null;
      String badFile = null;

      try {
         dataAccessConfigurableService.doConfigure();
         goodFile = readFileAsString("/bean-config/answers-catalog-test/pre-queries-good.xml");
         // badFile = readFileAsString("/bean-config/answers-catalog-test/pre-queries-bad.xml");
      } catch (IOException e) {
         e.printStackTrace();
      } catch (ConfigurationException e) {
         e.printStackTrace();
      }

      XStream xstreamGood = new XStream();
      xstreamGood.alias("query", DataTransferQuery.class);
      xstreamGood.alias("execue", DataTransferInput.class);
      goodDataTransferInput = (DataTransferInput) xstreamGood.fromXML(goodFile);

      // XStream xstreamBad = new XStream();
      // xstreamBad.alias("query", RemoteQuery.class);
      // xstreamBad.alias("execue", DataTransferInput.class);
      // badDataTransferInput = (DataTransferInput) xstreamBad.fromXML(badFile);

   }

   @After
   public void tearDown () {

   }

   @Test
   public void testRemoteDataTransferSuccessCase () {
      boolean status = true;
      IRemoteDataTransferService remoteDataTransferImpl = (IRemoteDataTransferService) springContext
               .getBean("remoteDataTransferService");

      try {
         DataTransferStatus dataTransferStatus = remoteDataTransferImpl.transferRemoteData(goodDataTransferInput);
         for (int count = 0; count < dataTransferStatus.getDataTransferQueryStatus().size(); count++) {
            if (dataTransferStatus.getDataTransferQueryStatus().get(count).getErrorCode() > 0)
               status = false;
         }
      } catch (RemoteDataTransferException e) { // TODO Auto-generated catch
         // block
         e.printStackTrace();
      }
      assertTrue("Remote DATA Transfer SUCCCESS", status);
   }

   public void testRemoteDataTransferFailureCase () {
      IRemoteDataTransferService remoteDataTransferImpl = (IRemoteDataTransferService) springContext
               .getBean("remoteDataTransferService");
      boolean status = false;
      try {
         DataTransferStatus dataTransferStatus = remoteDataTransferImpl.transferRemoteData(badDataTransferInput);

         for (int count = 0; count < dataTransferStatus.getDataTransferQueryStatus().size(); count++) {
            if (dataTransferStatus.getDataTransferQueryStatus().get(count).getErrorCode() > 0)
               status = true;
         }

      } catch (RemoteDataTransferException e) { // TODO Auto-generated catch
         // // block
         e.printStackTrace();
      }
      assertTrue("Remote DATA Transfer FAILED", status);
   }

   private String readFileAsString (String filePath) throws java.io.IOException {

      InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(filePath));
      StringBuffer fileData = new StringBuffer(1000);

      // BufferedReader reader = new BufferedReader(new FileReader(filePath));
      BufferedReader reader = new BufferedReader(inputStreamReader);

      char[] buf = new char[1024];
      int numRead = 0;
      while ((numRead = reader.read(buf)) != -1) {
         String readData = String.valueOf(buf, 0, numRead);
         fileData.append(readData);
         buf = new char[1024];
      }
      reader.close();

      return fileData.toString();
   }
}
