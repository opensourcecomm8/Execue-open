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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.querygen.ForeignKeyEntity;
import com.execue.core.common.bean.querygen.PrimaryKeyEntity;
import com.execue.swi.exception.SDXException;
import com.execue.swi.SWIBaseTest;

public class SDXServiceTest extends SWIBaseTest {

   private static final Logger logger = Logger.getLogger(SDXServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   /**
    * Test case for getting the primary key colums(composite if any)
    */
   // @Test
   public void testGetPrimaryKey () {
      try {
         PrimaryKeyEntity primaryKeyEntity = getSdxRetrievalService().getPrimaryKey(1000L);
         if (primaryKeyEntity != null) {
            logger.debug("Primary Key Information : ");
            logger.debug("Table Name : " + primaryKeyEntity.getTabl().getName());
            for (Colum colum : primaryKeyEntity.getColums()) {
               System.out.print("colum Names : " + colum.getName());
            }
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   /**
    * Test case for getting the foreign key colums(parent table, parent colums,child colums)
    */
   // @Test
   public void testGetForeignKey () {
      try {
         List<ForeignKeyEntity> foreignKeyEntities = getSdxRetrievalService().getForeignKeys(1000L);
         logger.debug("Foreign Key Information : ");
         for (ForeignKeyEntity foreignKeyEntity : foreignKeyEntities) {
            if (foreignKeyEntity.getParentTable() != null) {
               System.out.print("Parent Table : " + foreignKeyEntity.getParentTable().getName() + "\t");
               logger.debug("Child Table " + foreignKeyEntity.getChildTable().getName());
               for (Colum colum : foreignKeyEntity.getChildColums()) {
                  System.out.print("Child Colums :" + colum.getName());
               }
               for (Colum colum : foreignKeyEntity.getParentColums()) {
                  System.out.print("Parent colums: " + colum.getName());
               }
            }
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testIsPrimaryKey () {
      try {
         logger.debug(getSdxRetrievalService().isPartOfPrimaryKey(1001L));
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testIsForeignKey () {
      try {
         logger.debug(getSdxRetrievalService().isForeignKeyColum(1005L));
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetTablesOfAsset () {
      Asset asset = new Asset();
      asset.setId(19L);
      try {
         List<TableInfo> assetTables = getSdxRetrievalService().getAssetTables(asset);
         for (TableInfo tableInfo : assetTables) {
            System.out.println(tableInfo.getTable().getName());
            System.out.println(tableInfo.getTable().getId());
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetColumsofTable () {
      Tabl table = new Tabl();
      table.setId(359L);
      try {
         List<Colum> columnsOfTable = getSdxRetrievalService().getColumnsOfTable(table);
         for (Colum colum : columnsOfTable) {
            System.out.println(colum.getName());
            System.out.println(colum.getId());
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testDeleteConstraints () {
      try {
         getSDXDeletionService().deleteConstraints(11L);
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetReferencedConstraints () {
      try {
         List<Constraint> referencedConstraints = getSdxRetrievalService().getReferencedConstraints(1084L);
         System.out.println("Referenced Constraints size " + referencedConstraints.size());
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   // public void testGetDeltaSourceChanges () {
   // try {
   // Asset asset = new Asset();
   // DataSource dataSource = getSDXService().getDataSource("answerscatalog");
   // asset.setId(103L);
   // asset.setDataSource(dataSource);
   // getSDXService().getDeltaSourceChanges(asset);
   // } catch (SDXException e) {
   // e.printStackTrace();
   // }
   // }

   // @Test
   public void getMembersByLookupValuesTest () {
      try {
         AssetEntityDefinition aed = getSdxRetrievalService().getAssetEntityDefinitionById(107L);
         List<String> lookupValues = new ArrayList<String>();
         lookupValues.add("10");
         // lookupValues.add("105");
         List<Membr> members = getSdxRetrievalService().getMembersByLookupValues(aed.getAsset(), aed.getTabl(),
                  aed.getColum(), lookupValues);
         if (members != null && members.size() > 0) {
            for (Membr m : members) {
               System.out.println(m.getLookupDescription());
            }
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllParentAssets () {
      try {
         List<Asset> parentAssets = getSdxRetrievalService().getAllParentAssets(1L);
         for (Asset asset : parentAssets) {
            System.out.println("Asset Name " + asset.getName());
         }
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAssetTable () {
      try {

         Tabl table = getSdxRetrievalService().getAssetTable(13L, "ACCOUNT_FX");
         System.out.println(table.getId());
      } catch (SDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetLightAssets () {
      try {
         List<Long> assetIds = new ArrayList<Long>();
         assetIds.add(1L);
         assetIds.add(2L);
         assetIds.add(3L);
         assetIds.add(4L);
         List<Asset> lightAssets = getSdxRetrievalService().getLightAssets(assetIds);
         System.out.println(lightAssets.size());
      } catch (SDXException e) {
         Assert.fail(e.getCode() + ", " + e.getMessage());
         e.printStackTrace();
      }
   }

}
