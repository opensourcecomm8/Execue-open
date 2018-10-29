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


package com.execue.swi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.type.LookupType;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.swi.exception.SDXException;

public class SWICommonBaseTest extends SWIBaseTest {

   protected static Random random;

   private static int      SAMPLE_ASSET_DATA     = 300;
   private static String   SAMPLE_ASSET_BASENAME = "SampleAsset";
   private static String   SAMPLE_TABLE_DATA     = "VirtualTableName";
   private static String   SAMPLE_COLUMN_DATA1   = "SampleColumn1Name";
   private static String   SAMPLE_COLUMN_DATA2   = "SampleColumn2Name";
   private static String   SAMPLE_MEMBER_DATA    = "SampleLongDesc";
   private static int      TABLE_INFO_INDEX      = 0;

   public List<TableInfo> createTableInfo () {
      List<TableInfo> tableInfoList = new ArrayList<TableInfo>();
      TableInfo tableInfo = new TableInfo();
      Tabl table = getSampleTabl(SAMPLE_TABLE_DATA, LookupType.SIMPLE_LOOKUP);
      table.setLookupValueColumn(SAMPLE_COLUMN_DATA1);
      tableInfo.setTable(table);
      List<Colum> listColumns = new ArrayList<Colum>();
      Colum col1 = getSampleColum(SAMPLE_COLUMN_DATA1);
      listColumns.add(col1);
      listColumns.add(getSampleColum(SAMPLE_COLUMN_DATA2));
      List<Membr> listMembers = new ArrayList<Membr>();
      Membr member = getSampleMembr(SAMPLE_MEMBER_DATA);
      member.setLookupColumn(col1);
      listMembers.add(member);
      tableInfo.setColumns(listColumns);
      tableInfo.setMembers(listMembers);
      tableInfoList.add(tableInfo);
      return tableInfoList;
   }

   public Asset createAsset () {
      Asset asset = getSampleAsset(SAMPLE_ASSET_BASENAME + random.nextInt(), SAMPLE_ASSET_DATA);
      return asset;
   }

   public void testCreateAsset (Asset asset) {
      boolean creationSucceeded = true;
      try {
         getSDXDataAccessManager().createAsset(asset);

      } catch (SDXException e) {
         creationSucceeded = false;
      }
      Assert.assertTrue("Failed to create asset", creationSucceeded);
   }

   public void testCreateAssetTables (Asset asset, List<TableInfo> listTableInfo) {
      boolean creationSucceeded = true;
      try {
         getSDXDataAccessManager().createAssetTables(asset, listTableInfo);
      } catch (SDXException e) {
         creationSucceeded = true;
      }

      Assert.assertTrue("Failed to create asset", creationSucceeded);
   }

   public void testGetAssetTable (Asset asset, List<TableInfo> listTableInfo) {

      TableInfo tableInfo = null;

      try {
         tableInfo = getSDXDataAccessManager().getAssetTable(asset, listTableInfo.get(TABLE_INFO_INDEX).getTable());

      } catch (SDXException e) {
      }
      Assert.assertTrue("retrieval failed ", tableInfo != null);

      Tabl table = tableInfo.getTable();
      List<Colum> columns = tableInfo.getColumns();
      List<Membr> members = tableInfo.getMembers();

      Assert.assertTrue("Table Retrieval failed", table.getName().equals(SAMPLE_TABLE_DATA));

      Assert.assertTrue("There should be two columns", columns.size() == 2);

      Assert.assertTrue("Column Retrieval failed for first column", (columns.get(0).getName().equalsIgnoreCase(
               (SAMPLE_COLUMN_DATA1)) || (columns.get(0).getName().equalsIgnoreCase(SAMPLE_COLUMN_DATA2))));
      Assert.assertTrue("Column Retrieval failed for first column", (columns.get(1).getName().equalsIgnoreCase(
               (SAMPLE_COLUMN_DATA1)) || (columns.get(1).getName().equalsIgnoreCase(SAMPLE_COLUMN_DATA2))));

      try {
         Assert.assertTrue("Members in Lookup Colum is Not Correct", (getSDXDataAccessManager().getColumnMembers(
                  columns.get(0)).size() == 1));
         Assert.assertTrue("Member Retrieval failed", (members.get(0).getLongDescription()
                  .equalsIgnoreCase((SAMPLE_MEMBER_DATA))));

      } catch (SDXException e) {
         e.printStackTrace();
      }

   }

   public void testGetAssetTables (Asset asset) {
      List<TableInfo> listTableInfo = null;
      try {
         listTableInfo = getSDXDataAccessManager().getAssetTables(asset);
      } catch (SDXException e) {

      }

      for (TableInfo tableInfo : listTableInfo) {
         Tabl table = tableInfo.getTable();
         List<Colum> columns = tableInfo.getColumns();
         List<Membr> members = tableInfo.getMembers();
         Assert.assertTrue("Table Retrieval failed", table.getName().equals(SAMPLE_TABLE_DATA));

         Assert.assertTrue("There should be two columns", columns.size() == 2);

         Assert.assertTrue("Column Retrieval failed for first column", columns.get(0).getName().equals(
                  (SAMPLE_COLUMN_DATA1)));
         Assert.assertTrue("Column Retrieval failed for second column", columns.get(1).getName().equals(
                  (SAMPLE_COLUMN_DATA2)));
         try {
            Assert.assertTrue("Members in Lookup Colum is Not Correct", (getSDXDataAccessManager().getColumnMembers(
                     columns.get(0)).size() == 1));
         } catch (SDXException e) {
         }
         Assert.assertTrue("Member belongs to look up column", members.get(0).getLookupColumn().getName().equals(
                  SAMPLE_COLUMN_DATA1));
      }

   }

   public void testDeleteAsset (Asset asset) {

      try {
         getSDXDataAccessManager().deleteAsset(asset);

      } catch (SDXException e) {
         e.printStackTrace();

      }
      Asset deletedAsset = null;
      try {
         deletedAsset = getAssetEntityDefinitionDAO().getById(asset.getId(), Asset.class);
      } catch (DataAccessException e) {
         e.printStackTrace();
      }
      Assert.assertTrue("Failed to delte asset ", deletedAsset == null);
   }

   public Set<Long> getModelGroupIds (List<ModelGroup> userModelGroups) {
      Set<Long> userModelGroupIds = new HashSet<Long>();
      for (ModelGroup modelGroup : userModelGroups) {
         userModelGroupIds.add(modelGroup.getId());
      }
      return userModelGroupIds;
   }
}
