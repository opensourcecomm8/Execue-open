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


package com.execue.platform.test.swi;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AppSourceType;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.test.PlatformCommonBaseTest;
import com.execue.swi.exception.KDXException;

public class AssetActivationServiceTest extends PlatformCommonBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testLocationDataPopulation () throws PlatformException, KDXException {
      Application application = new Application();
      application.setPopularity(1L);
      User user = new User();
      user.setId(1L);
      application.setOwner(user);
      application.setSourceType(AppSourceType.UNSTRUCTURED);
      application.setUnstructuredApplicationDetail(new UnstructuredApplicationDetail());
      application.setName("Testwsdsd7fdf867");

      getApplicationManagementWrapperService().createApplicationHierarchy(application);
   }

}
