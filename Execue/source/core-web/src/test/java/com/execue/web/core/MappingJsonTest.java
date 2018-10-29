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


package com.execue.web.core;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.util.MappingEntityType;
import com.execue.handler.bean.mapping.AssetColumn;
import com.execue.handler.bean.mapping.AssetColumnMember;
import com.execue.handler.bean.mapping.AssetMember;
import com.execue.handler.bean.mapping.AssetTable;
import com.execue.handler.bean.mapping.ConceptAssetMapping;
import com.execue.handler.bean.mapping.ConceptMapping;
import com.execue.handler.bean.mapping.InstanceAssetMapping;
import com.execue.handler.bean.mapping.InstanceMapping;
import com.execue.handler.bean.mapping.MappableBusinessTerm;
import com.execue.handler.bean.mapping.MappableInstanceTerm;
import com.execue.handler.bean.mapping.MappingHeader;
import com.execue.handler.bean.mapping.TermInfo;
import com.googlecode.jsonplugin.JSONUtil;

public class MappingJsonTest {

   public static void main (String[] args) throws Exception {
      printConceptMappingList();
      printConceptList();
      printTableList();
      printInstanceList();
      printMemberList();
      printMappableInstanceList();
   }

   private static void printMemberList () throws Exception {
      AssetColumnMember columnMember = new AssetColumnMember();
      List<AssetMember> memList = new ArrayList<AssetMember>();
      columnMember.setMembers(memList);
      columnMember.setAedId(100);
      columnMember.setId(10);
      columnMember.setTblDispName("Table 1");
      columnMember.setColDispName("Column 1");

      AssetMember assetMember = new AssetMember();
      assetMember.setAedId(1001);
      assetMember.setId(151);
      assetMember.setDispName("Member 1");
      memList.add(assetMember);

      AssetMember assetMember1 = new AssetMember();
      assetMember1.setAedId(1002);
      assetMember1.setId(152);
      assetMember1.setDispName("Member 2");
      memList.add(assetMember1);
      System.out.println(JSONUtil.serialize(columnMember));
   }

   private static void printTableList () throws Exception {
      List<AssetTable> aList = new ArrayList<AssetTable>();
      AssetTable table = new AssetTable();
      table.setId(100);
      table.setDispName("Table 1");
      table.setAedId(1001);
      List<AssetColumn> coList = new ArrayList<AssetColumn>();
      table.setCols(coList);
      AssetColumn column = new AssetColumn();
      column.setAedId(1);
      column.setId(10);
      column.setDispName("column 1");
      coList.add(column);
      AssetColumn column1 = new AssetColumn();
      column1.setAedId(2);
      column1.setId(11);
      column1.setDispName("column 2");
      coList.add(column1);
      aList.add(table);

      AssetTable table1 = new AssetTable();
      table1.setId(101);
      table1.setDispName("Table 2");
      table1.setAedId(1002);
      List<AssetColumn> coList1 = new ArrayList<AssetColumn>();
      table1.setCols(coList1);
      AssetColumn column2 = new AssetColumn();
      column2.setAedId(3);
      column2.setId(12);
      column2.setDispName("column 21");
      coList1.add(column2);
      aList.add(table1);
      System.out.println(JSONUtil.serialize(aList));
   }

   private static void printConceptList () throws Exception {
      MappableBusinessTerm businssTerm = new MappableBusinessTerm();
      List<TermInfo> conList = new ArrayList<TermInfo>();
      businssTerm.setTerms(conList);
      TermInfo cInfo = new TermInfo();
      cInfo.setDispName("Concept 1");
      cInfo.setId(1);
      cInfo.setMapped(true);

      TermInfo cInfo2 = new TermInfo();
      cInfo2.setDispName("Concept 2");
      cInfo2.setId(2);
      cInfo2.setMapped(false);

      conList.add(cInfo);
      conList.add(cInfo2);

      businssTerm.setErrorMsg("Error message");
      System.out.println(JSONUtil.serialize(businssTerm));

   }

   private static void printMappableInstanceList () throws Exception {
      MappableInstanceTerm instanceTerm = new MappableInstanceTerm();
      instanceTerm.setConDispName("Concept 1");
      instanceTerm.setBedId(10);
      List<TermInfo> insList = new ArrayList<TermInfo>();
      instanceTerm.setTerms(insList);
      TermInfo iInfo = new TermInfo();
      iInfo.setDispName("Instance 1");
      iInfo.setId(1);
      iInfo.setMapped(true);

      TermInfo iInfo2 = new TermInfo();
      iInfo2.setDispName("Instance 2");
      iInfo2.setId(2);
      iInfo2.setMapped(false);

      insList.add(iInfo);
      insList.add(iInfo2);

      System.out.println(JSONUtil.serialize(instanceTerm));

   }

   private static void printConceptMappingList () throws Exception {
      ConceptMapping conceptMapping = new ConceptMapping();
      MappingHeader header = new MappingHeader();
      conceptMapping.setHeader(header);
      header.setCurrentPage(2);
      header.setTotalPages(10);
      header.setFilterType("none");

      List<ConceptAssetMapping> caList = new ArrayList<ConceptAssetMapping>();
      conceptMapping.setMappings(caList);
      ConceptAssetMapping conceptAssetMapping1 = new ConceptAssetMapping();
      caList.add(conceptAssetMapping1);
      conceptAssetMapping1.setAedId(1L);
      conceptAssetMapping1.setBedId(2);
      conceptAssetMapping1.setColDispName("Column Display Name");
      conceptAssetMapping1.setTblDispName("Table Display Name");
      conceptAssetMapping1.setConDispName("Concept Name");
      conceptAssetMapping1.setConType(MappingEntityType.EXISTING);
      conceptAssetMapping1.setMapInstance(false);
      conceptAssetMapping1.setMsgType("N");
      conceptAssetMapping1.setMsg("");

      ConceptAssetMapping conceptAssetMapping2 = new ConceptAssetMapping();
      caList.add(conceptAssetMapping2);
      conceptAssetMapping2.setAedId(10L);
      conceptAssetMapping2.setBedId(20);
      conceptAssetMapping2.setColDispName("Column Display Name 2");
      conceptAssetMapping2.setTblDispName("Table Display Name 2");
      conceptAssetMapping2.setConDispName("Concept Name 2");
      conceptAssetMapping2.setConType(MappingEntityType.NEW);
      conceptAssetMapping2.setMapInstance(false);
      conceptAssetMapping2.setMsgType("E");
      conceptAssetMapping2.setMsg("Cant access");

      System.out.println(JSONUtil.serialize(conceptMapping));
   }

   private static void printInstanceList () throws Exception {
      InstanceMapping instanceMapping = new InstanceMapping();
      MappingHeader header = new MappingHeader();
      instanceMapping.setHeader(header);
      header.setCurrentPage(2);
      header.setTotalPages(10);
      header.setFilterType("none");

      List<InstanceAssetMapping> iList = new ArrayList<InstanceAssetMapping>();
      instanceMapping.setMappings(iList);
      InstanceAssetMapping instanceAssetMapping1 = new InstanceAssetMapping();
      iList.add(instanceAssetMapping1);
      instanceAssetMapping1.setAedId(1L);
      instanceAssetMapping1.setBedId(2);
      instanceAssetMapping1.setMemDispName("Member Display Name");
      instanceAssetMapping1.setInstanceDispName("Instance 1");
      instanceAssetMapping1.setInstanceType("E");
      instanceAssetMapping1.setMsgType("N");
      instanceAssetMapping1.setMsg("");

      InstanceAssetMapping instanceAssetMapping2 = new InstanceAssetMapping();
      iList.add(instanceAssetMapping2);
      instanceAssetMapping2.setAedId(10L);
      instanceAssetMapping2.setBedId(20);
      instanceAssetMapping2.setMemDispName("Member Dispaly 2");
      instanceAssetMapping2.setInstanceDispName("Instance 2");
      instanceAssetMapping2.setInstanceType("E");
      instanceAssetMapping2.setMsgType("E");
      instanceAssetMapping2.setMsg("Cant access");

      System.out.println(JSONUtil.serialize(instanceMapping));
   }
}