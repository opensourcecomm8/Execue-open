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


package com.execue.swi.dataaccess;

import java.util.List;
import java.util.Random;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.swi.SWICommonBaseTest;

/**
 * This is Junit Test class for testing DAO operations on AssetEntityDefinition object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class SDXDataAccessManagerTest extends SWICommonBaseTest {

   @BeforeClass
   public static void setup () {
      baseSetup();
      random = new Random();
   }

   @AfterClass
   public static void tearDown () {
      baseTearDown();
   }

   /**
    * This test case tests the basic DAO operations on Asset. It will create an Asset and then Table,Colum and Membr
    * information wrapped as TableInfo objet. It will update after creation and then try to retrieve the updated data.
    * After that we will delete the asset and tableInfo
    */
   @Test
   public void testProcessor () {
      Asset asset = createAsset();
      List<TableInfo> listTableInfo = createTableInfo();
      testCreateAsset(asset);
      testCreateAssetTables(asset, listTableInfo);
      testGetAssetTable(asset, listTableInfo);
      testGetAssetTables(asset);
      testDeleteAsset(asset);

   }

}
