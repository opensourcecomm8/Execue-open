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


package com.execue.swi.service;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.ac.AssetOperationDetail;
import com.execue.swi.SWIBaseTest;
import com.execue.swi.exception.SDXException;

public class AssetOperationDetailServiceTest extends SWIBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   // @Test
   public void testGetAssetOperationDetailByAssetId () {
      try {
         List<AssetOperationDetail> AssetOperationDetil = getAssetOperationDetailService()
                  .getAssetOperationDetailByAssetId(1002L);
         for (AssetOperationDetail assetOperationDetail : AssetOperationDetil) {
            System.out.println("asset operation detail:::" + assetOperationDetail.getAssetId());
            System.out.println("asset operation detail:::" + assetOperationDetail.getJobRequestId());
         }
      } catch (SDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   @Test
   public void testAssetUnderMaintenance () {
      try {
         boolean AssetUnderMaintenance = getAssetOperationDetailService().isAssetUnderMaintenance(1002L);
         System.out.println("AssetUnderMaintenance::::" + AssetUnderMaintenance);
      } catch (SDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
}
