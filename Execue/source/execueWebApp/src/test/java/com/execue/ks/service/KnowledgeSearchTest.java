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


package com.execue.ks.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.KnowledgeSearchResultItem;
import com.execue.core.common.bean.kb.DataConnectionEntity;
import com.execue.core.common.bean.kb.DataEntity;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.RIInstanceTripleDefinition;
import com.execue.core.common.type.SearchType;
import com.execue.ks.bean.KBDataEntity;
import com.execue.ks.bean.KBSearchResult;
import com.execue.nlp.engine.barcode.matrix.MatrixUtility;
import com.execue.nlp.util.NLPUtilities;
import com.execue.util.CollectionUtil;

/**
 * @author Nitesh
 * @since Jan 26, 2010
 */
public class KnowledgeSearchTest extends ExeCueBaseTest {

   private final Logger        logger   = Logger.getLogger(KnowledgeSearchTest.class);

   private static final String NEW_LINE = System.getProperty("line.separator");

   @BeforeClass
   public static void setUp () {
      baseTestSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void testKnowledgeSearch () {
      NLPInformation information = null;
      String query = null;
      try {
         query = "winner of wimbledon 2007";

         information = getNLPEngine().processQuery(query);
         logger.info("Reduced forms " + information.getReducedForms().size());
         if (logger.isInfoEnabled()) {
            MatrixUtility.displayReducedForms(information.getReducedForms());
         }
         // Sort the reduced forms by descending weight
         // be careful with this as Each JAVA implmentation by default tends to set it as descending or ascending at
         // random
         Map<SemanticPossibility, Double> reducedFormMap = new HashMap<SemanticPossibility, Double>(1);
         for (SemanticPossibility reducedForm : information.getReducedForms().keySet()) {
            reducedFormMap.put(reducedForm, reducedForm.getPossiblityWeight());
         }
         Map<SemanticPossibility, Double> sortedReducedFormMap = CollectionUtil.sortMapOnValue(NLPUtilities
                  .getRelativePercentageMap(reducedFormMap));
         // Need to implment such a way that
         // May be get the results only for the top most ones and if they dont provide any results then get the next
         // ones
         List<SemanticPossibility> semList = new ArrayList<SemanticPossibility>(sortedReducedFormMap.keySet());
         Map<SemanticPossibility, List<RIInstanceTripleDefinition>> bestPossiblitiesRIInstanceTriplesMap = getKnowledgeSearchEngine()
                  .knowledgeSearch(semList, SearchType.KNOWLEDGE_SEARCH);
         for (Entry<SemanticPossibility, List<RIInstanceTripleDefinition>> entry : bestPossiblitiesRIInstanceTriplesMap
                  .entrySet()) {
            SemanticPossibility reducedFormPossibility = entry.getKey();
            List<RIInstanceTripleDefinition> riInstanceTriples = entry.getValue();
            Map<Long, KnowledgeSearchResultItem> knowledgeSearchResultItemMap = getKnowledgeSearchEngine()
                     .getKnowledgeSearchResultItemMap(reducedFormPossibility, riInstanceTriples);
            KBSearchResult result = getKnowledgeSearchEngine().getKnowledgeSearchResult(knowledgeSearchResultItemMap);
            print(result);
            logger.debug("DONE");

         }
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   private void print (KBSearchResult result) {
      Map<Long, KBDataEntity> entityMap = result.getEntityMap();
      if (entityMap == null || entityMap.isEmpty()) {
         System.out.println("Result is empty");
         return;
      }
      Set<Entry<Long, KBDataEntity>> entrySet = entityMap.entrySet();
      for (Entry<Long, KBDataEntity> entry : entrySet) {
         KBDataEntity resultDataEntity = entry.getValue();
         if (resultDataEntity != null) {
            System.out.println(resultDataEntity.getName());
            Map<Long, DataConnectionEntity> connections = resultDataEntity.getConnections();
            if (connections != null && !connections.isEmpty()) {
               Set<Entry<Long, DataConnectionEntity>> connectionsSet = connections.entrySet();
               for (Entry<Long, DataConnectionEntity> connection : connectionsSet) {
                  DataConnectionEntity connectionEntity = connection.getValue();
                  if (connectionEntity != null) {
                     System.out.println("\t\t" + connectionEntity.getName());
                     Map<Long, DataEntity> dataItemsByInstancePathID = connectionEntity.getDataItemsByInstancePathID();
                     if (!dataItemsByInstancePathID.isEmpty()) {
                        Set<Entry<Long, DataEntity>> dateItemsSet = dataItemsByInstancePathID.entrySet();
                        for (Entry<Long, DataEntity> dataItem : dateItemsSet) {
                           DataEntity dataEntity = dataItem.getValue();
                           System.out.println("\t\t\t\t" + dataEntity.getName() + "\t\t" + dataItem.getKey());
                        }
                     }
                  }
               }
            }
         }
      }
   }
}
