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


package com.execue.handler.swi.mappings;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.exception.ExeCueException;
import com.execue.handler.HandlerBaseTest;
import com.execue.handler.bean.mapping.AssetColumn;
import com.execue.handler.bean.mapping.AssetColumnMember;
import com.execue.handler.bean.mapping.AssetMember;
import com.execue.handler.bean.mapping.AssetTable;
import com.execue.handler.bean.mapping.ConceptAssetMapping;
import com.execue.handler.bean.mapping.ConceptMapping;
import com.execue.handler.bean.mapping.MappableBusinessTerm;
import com.execue.handler.bean.mapping.MappingsPageProvider;
import com.execue.handler.bean.mapping.SaveMapping;
import com.execue.handler.bean.mapping.TermInfo;

public class MappingServiceHandlerTest extends HandlerBaseTest {

   private static final Logger logger  = Logger.getLogger(MappingServiceHandlerTest.class);
   private Long                assetId = 11L;

   @BeforeClass
   public static void setup () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   private MappingsPageProvider getProviderObject () {
      List<ConceptAssetMapping> existingMappings;
      MappingsPageProvider provider = null;
      try {
         existingMappings = getMappingServiceHandler().showExistingConceptMappings(13L);
         provider = new MappingsPageProvider();
         provider.setConceptMappings(existingMappings);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return provider;
   }

   // @Test
   public void saveMappingsExistingModifiedConceptDisplayNameTest () {
      try {
         Long modelId = 1L;
         ConceptMapping mapping = getMappingServiceHandler().saveConceptMappings(modelId, genSaveMappings1(),
                  getProviderObject(), null);
         List<ConceptAssetMapping> caMappings = mapping.getMappings();
         logger.debug("Successfully saved the mappings; " + caMappings.size());
         for (ConceptAssetMapping cam : caMappings) {
            logger.debug("message : " + cam.getMsg());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void saveMappingsExistingMappingTest () {
      try {
         Long modelId = 1L;
         ConceptMapping mapping = getMappingServiceHandler().saveConceptMappings(modelId, genSaveMappings2(),
                  getProviderObject(), null);
         List<ConceptAssetMapping> caMappings = mapping.getMappings();
         for (ConceptAssetMapping cam : caMappings) {
            logger.debug("message : " + cam.getMsg());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void saveMappingsExistingMappingDiffConceptDiffDispNameTest () {
      try {
         Long modelId = 1L;
         ConceptMapping mapping = getMappingServiceHandler().saveConceptMappings(modelId, genSaveMappings3(),
                  getProviderObject(), null);
         List<ConceptAssetMapping> caMappings = mapping.getMappings();
         for (ConceptAssetMapping cam : caMappings) {
            logger.debug("message : " + cam.getMsg());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void saveMappingsExistingMappingDiffConceptTest () {
      try {
         Long modelId = 1L;
         ConceptMapping mapping = getMappingServiceHandler().saveConceptMappings(modelId, genSaveMappings4(),
                  getProviderObject(), null);
         List<ConceptAssetMapping> caMappings = mapping.getMappings();
         for (ConceptAssetMapping cam : caMappings) {
            logger.debug(cam.getConDispName());
            logger.debug("message : " + cam.getMsg());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void saveMappingsNewMappingTest () {
      try {
         Long modelId = 1L;
         ConceptMapping mapping = getMappingServiceHandler().saveConceptMappings(modelId, genSaveMappings5(),
                  getProviderObject(), null);
         List<ConceptAssetMapping> caMappings = mapping.getMappings();
         for (ConceptAssetMapping cam : caMappings) {
            logger.debug("message : " + cam.getMsg());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   // public void suggestMappingsForTableTest () {
   // try {
   // List<ConceptAssetMapping> caMappings = getMappingServiceHandler().suggestConceptMappingsForTable(assetId,
   // 101L, 8888L);
   // logger.debug("Completed the suggestion process....");
   // for (ConceptAssetMapping mapping : caMappings) {
   // logger.debug(mapping.getColDispName() + "; AED id : " + mapping.getAedId() + " - "
   // + mapping.getConDispName() + "; DED id : " + mapping.getDedId() + "; Concept Type : "
   // + mapping.getConType());
   // }
   // } catch (ExeCueException e) {
   // e.printStackTrace();
   // }
   // }

   // @Test
   public void showExistingMappingsTest () {
      try {
         getMappingServiceHandler().showExistingConceptMappings(assetId);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void getAssetTablesTest () {
      try {
         List<AssetTable> assetTables = getMappingServiceHandler().getAssetTables(assetId);
         for (AssetTable assetTable : assetTables) {
            System.out.println("------------" + assetTable.getDispName() + "-------------");
            for (AssetColumn col : assetTable.getCols()) {
               System.out.println(col.getDispName());
            }
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void showConceptsTest () {
      try {
         MappableBusinessTerm term = getMappingServiceHandler().showConcepts(1L);
         List<TermInfo> infoList = term.getTerms();
         for (TermInfo c : infoList) {
            System.out.println(c.getDispName());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void suggestConceptsTest () {
      String searchString = "age";
      try {
         List<TermInfo> infoList = getMappingServiceHandler().suggestConcepts(1L, searchString);
         for (TermInfo c : infoList) {
            System.out.println(c.getDispName());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }

   // saveMapping[0].aedId:312,saveMapping[0].dedId:11,saveMapping[0].dedType:E,saveMapping[0].delMap:false,saveMapping[0].conDispName:Account,saveMapping[0].mapType:E,
   // saveMapping[1].aedId:312,saveMapping[1].dedId:255,saveMapping[1].dedType:E,saveMapping[1].delMap:false,saveMapping[1].conDispName:Vintage,saveMapping[1].mapType:E,
   // saveMapping[2].aedId:313,saveMapping[2].dedId:254,saveMapping[2].dedType:E,saveMapping[2].delMap:false,saveMapping[2].conDispName:Joining
   // Month,saveMapping[2].mapType:E,
   // saveMapping[3].aedId:316,saveMapping[3].dedId:11,saveMapping[3].dedType:E,saveMapping[3].delMap:false,saveMapping[3].conDispName:Account,saveMapping[3].mapType:E,
   // saveMapping[4].aedId:316,saveMapping[4].dedId:255,saveMapping[4].dedType:E,saveMapping[4].delMap:false,saveMapping[4].conDispName:Vintage,saveMapping[4].mapType:E,
   // saveMapping[5].aedId:318,saveMapping[5].dedId:12,saveMapping[5].dedType:E,saveMapping[5].delMap:false,saveMapping[5].conDispName:Account
   // Status,saveMapping[5].mapType:E,
   private List<SaveMapping> genSaveMappings1 () {
      List<SaveMapping> mappings = new ArrayList<SaveMapping>();
      SaveMapping mapping = new SaveMapping();
      mapping.setAedId(318L);
      mapping.setBedId(12L);
      mapping.setBedType("E");
      mapping.setDelMap("false");
      mapping.setDispName("AccountStatusTEST");
      mapping.setMapType("E");
      mappings.add(mapping);
      return mappings;
   }

   private List<SaveMapping> genSaveMappings2 () {
      List<SaveMapping> mappings = new ArrayList<SaveMapping>();
      SaveMapping mapping = new SaveMapping();
      mapping.setAedId(318L);
      mapping.setBedId(12L);
      mapping.setBedType("E");
      mapping.setDelMap("false");
      mapping.setDispName("AccountStatus");
      mapping.setMapType("E");
      mappings.add(mapping);
      return mappings;
   }

   private List<SaveMapping> genSaveMappings3 () {
      List<SaveMapping> mappings = new ArrayList<SaveMapping>();
      SaveMapping mapping = new SaveMapping();
      mapping.setAedId(318L);
      mapping.setBedId(14L);
      mapping.setBedType("E");
      mapping.setDelMap("false");
      mapping.setDispName("Acct Status");
      mapping.setMapType("E");
      mappings.add(mapping);
      return mappings;
   }

   private List<SaveMapping> genSaveMappings4 () {
      List<SaveMapping> mappings = new ArrayList<SaveMapping>();
      SaveMapping mapping = new SaveMapping();
      mapping.setAedId(318L);
      mapping.setBedId(14L);
      mapping.setBedType("E");
      mapping.setDelMap("false");
      mapping.setDispName("Billing Balance");
      mapping.setMapType("E");
      mappings.add(mapping);
      return mappings;
   }

   private List<SaveMapping> genSaveMappings5 () {
      List<SaveMapping> mappings = new ArrayList<SaveMapping>();
      SaveMapping mapping = new SaveMapping();
      mapping.setAedId(318L);
      mapping.setBedId(12L);
      mapping.setBedType("E");
      mapping.setDelMap("false");
      mapping.setDispName("AccountStatus");
      mapping.setMapType("N");
      mappings.add(mapping);
      return mappings;
   }

   @Test
   public void getColumnMembersTest () {
      try {
         AssetColumnMember acm = getMappingServiceHandler().getColumnMembers(414L);
         List<AssetMember> mlist = acm.getMembers();
         System.out.println(mlist.size());
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
   }
}