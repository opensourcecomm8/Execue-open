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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.mapping.ColumnMapping;
import com.execue.core.common.bean.mapping.MemberMapping;
import com.execue.core.common.bean.mapping.TableMapping;

/**
 * @author John Mallavalli
 */
public class SDX2KDXMappingTest extends ExeCueBaseTest {

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   // @Test
   public void testMapSDX2KDXForTable () {
      Asset asset;
      Long startCounter = 7777L;
      Long modelId = 100L;
      try {
         Model model = getKDXRetrievalService().getModelById(modelId);
         asset = getSDXService().getAsset(new Long(101), "amazon");
         if (asset != null) {
            Tabl table = getSDXService().getAssetTable(asset.getId(), "EXT_STATUS");
            TableMapping mapping = getSDX2KDXMappingService().mapSDX2KDXForTable(asset, table, false, false, true,
                     startCounter, model);
            if (mapping != null) {
               List<ColumnMapping> colMappings = mapping.getColumnMappings();
               if (colMappings != null) {
                  System.out.println(colMappings.size() + " colMappings size; " + colMappings);
                  if (colMappings.size() > 0) {
                     for (ColumnMapping cm : colMappings) {
                        AssetEntityDefinition aed = cm.getAssetEntityDefinition();
                        BusinessEntityDefinition bed = cm.getBusinessEntityDefinition();
                        System.out.println(aed.getColum().getName() + "(" + aed.getId() + ") <-> "
                                 + bed.getConcept().getDisplayName() + "(" + bed.getId() + ")");
                     }
                  } else {
                     System.out.println("NO SUGGESTIONS as all columns have been mapped!");
                  }
               } else {
                  System.out.println("returned NULL for colmappings list");
               }
            } else {
               System.out.println("returned NULL for tablemapping");
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Test
   public void testMapSDX2KDXForColumnMembers () {
      Asset asset;
      Long startCounter = 998877L;
      Long modelId = 100L;
      try {
         Model model = getKDXRetrievalService().getModelById(modelId);
         asset = getSDXService().getAsset(new Long(101), "CompanyFinacials");
         if (asset != null) {
            Tabl table = getSDXService().getAssetTable(asset.getId(), "industry");
            Colum column = getSDXService().getColumnById(3L);
            BusinessEntityDefinition conceptDED = getKDXRetrievalService().getBusinessEntityDefinitionById(2L);
            if (column != null) {
               List<Membr> members = getSDXService().getColumnMembers(column);
               System.out.println("Members count : " + members.size());
               long start = System.currentTimeMillis();
               ColumnMapping columnMapping = getSDX2KDXMappingService().mapSDX2KDXForColumnMembers(asset, table,
                        column, members, conceptDED, false, true, startCounter, model);
               long end = System.currentTimeMillis();
               System.out.println("Time taken : " + (end - start) + " msecs");
               for (MemberMapping mm : columnMapping.getMemberMappings()) {
                  if (mm != null)
                     System.out.println(mm.getAssetEntityDefinition().getId() + " -> "
                              + mm.getBusinessEntityDefinition().getId() + "; "
                              + mm.getBusinessEntityDefinition().getInstance().getDisplayName());
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testMapSDX2KDXForColumnMembers1 () {
      Asset asset;
      Long startCounter = 998877L;
      Long modelId = 100L;
      try {
         Model model = getKDXRetrievalService().getModelById(modelId);
         asset = getSDXService().getAsset(new Long(101), "billing");
         if (asset != null) {
            Tabl table = getSDXService().getAssetTable(asset.getId(), "PERF_YM");
            Colum column = getSDXService().getColumnById(1091L);
            BusinessEntityDefinition conceptBED = getKDXRetrievalService().getBusinessEntityDefinitionById(2L);
            if (column != null) {
               List<Membr> members = getSDXService().getColumnMembers(column);
               System.out.println("Members count : " + members.size());
               long start = System.currentTimeMillis();
               ColumnMapping columnMapping = getSDX2KDXMappingService().mapSDX2KDXForColumnMembers(asset, table,
                        column, members, conceptBED, true, false, startCounter, model);
               long end = System.currentTimeMillis();
               System.out.println("Time taken : " + (end - start) + " msecs");
               System.out.println(columnMapping);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }
}
