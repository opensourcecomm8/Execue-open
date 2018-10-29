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


package com.execue.web.core.action.swi;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.execue.core.common.bean.entity.Asset;

public class SDXActionTest extends SWIActionBaseTest {

   SDXAction testAction = new SDXAction();
   String    result;

   /*
    * @BeforeClass public static void setup () { swiActionBaseSetup(); } @AfterClass public static void teardown() {
    * swiActionBaseTearDown(); }
    */

   @Test
   public void testCreateAsset () {
      Asset asset = createAsset("Service Test for Asset Creation", 200);
      SDXAction sdxAction = getSDXAction();
      try {
         sdxAction.setAsset(asset);
         sdxAction.createAsset();
      } catch (Exception e) {
         e.printStackTrace();
         throw new RuntimeException(e);
      }

      // TODO: -VG- has to check the return type .
      //sdxAction.getAsset("Service Test for Asset Creation");
      assertNotNull("asset not null ", asset);

   }

   private Asset createAsset (String name, long port) {
      final Asset asset = new Asset();
      asset.setName(name);
      // asset.setPort(port);
      return asset;
   }

}
