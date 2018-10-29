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


package com.execue.ac.algorithm.optimaldsetoldversion;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;

import com.execue.ac.AnswersCatalogBaseTest;
import com.execue.core.common.bean.optimaldset.OptimalDSet;
import com.execue.core.common.bean.optimaldset.OptimalDSetAlgorithmInput;
import com.execue.core.common.bean.optimaldset.OptimalDSetDimension;
import com.execue.core.util.ExecueCoreUtil;
import com.thoughtworks.xstream.XStream;

/**
 * This test case is for testing optimalDset algorithm
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/06/09
 */
public class OptimalDSetGeneratorTest extends AnswersCatalogBaseTest {

   private static final Logger log = Logger.getLogger(OptimalDSetGeneratorTest.class);

   @Before
   public void setup () {
      answersCatalogBaseSetUp();
   }

   @After
   public void teardown () {
      answersCatalogBaseTearDown();
   }

   // Private helper methods
   /**
    * Load the input from file "optimalDSet-input.xml", placed in test resources
    */
   private OptimalDSetAlgorithmInput prepareInput () throws IOException {

      InputStream is = getClass().getResourceAsStream("/optimalDSet-input.xml");
      String inputString = ExecueCoreUtil.readFileAsString(is);

      XStream xStream = new XStream();
      xStream.alias("dimension", OptimalDSetDimension.class);
      xStream.alias("container", OptimalDSetAlgorithmInput.class);
      xStream.alias("optimalDSet", OptimalDSet.class);

      OptimalDSetAlgorithmInput container = (OptimalDSetAlgorithmInput) xStream.fromXML(inputString);
      if (log.isDebugEnabled()) {
         log.debug("Number of Dimensions : " + container.getDimensions().size());
         log.debug("Number of OptimalDSets : " + container.getOptimalDSets().size());
      }

      return container;
   }
}
