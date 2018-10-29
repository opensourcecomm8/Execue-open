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


package com.execue.answersCatalog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.ac.exception.AssetCreationException;
import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.type.JobType;
import com.execue.core.exception.ConfigurationException;
import com.execue.core.exception.dataaccess.DataAccessException;
import com.thoughtworks.xstream.XStream;

/**
 * Cube Creation Test
 * 
 * @author Vishay
 */
public class CubeCreationTest extends ExeCueBaseTest {

   private CubeCreationContext cubeCreationContext;

   private String              xmlFile;

   @Before
   public void setUp () throws ConfigurationException, IOException {

      baseTestSetup();
      xmlFile = readFileAsString("/bean-config/answers-catalog-test/cubeCreation.xml");
      XStream xstream = new XStream();
      xstream.alias("CubeCreationContext", CubeCreationContext.class);
      xstream.alias("Concept", Concept.class);
      xstream.alias("Stat", Stat.class);
      xstream.alias("Range", Range.class);
      xstream.alias("RangeDetail", RangeDetail.class);
      cubeCreationContext = (CubeCreationContext) xstream.fromXML(xmlFile);
   }

   @After
   public void tearDown () {

   }

   @Test
   public void testCubeCreation () throws DataAccessException, AssetCreationException {
      JobRequest jobRequest = new JobRequest();
      jobRequest.setId(15L);
      jobRequest.setUserId(1L);
      jobRequest.setJobType(JobType.CUBE_CREATION);
      cubeCreationContext.setJobRequest(jobRequest);
      getCubeCreationService().cubeCreation(cubeCreationContext);
   }

   private String readFileAsString (String filePath) throws java.io.IOException {
      InputStreamReader inputStreamReader = new InputStreamReader(getClass().getResourceAsStream(filePath));
      StringBuffer fileData = new StringBuffer(1000);
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
