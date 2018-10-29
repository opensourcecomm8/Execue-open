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


package com.execue.semantification.unstructured.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UDXKeyword;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.type.RFXType;
import com.execue.core.common.type.RFXValueType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.qdata.exception.UDXException;
import com.execue.semantification.service.BaseArticlePopulationService;

/**
 * @author Nihar
 */
public class NewsArticlePopulationService extends BaseArticlePopulationService {

   private static Logger logger = Logger.getLogger(NewsArticlePopulationService.class);

   @Override
   public void populateCategorySpecificTables (List<SemanticPossibility> semanticPossibilities,
            List<String> newsItemLinesToSemantify, UnStructuredIndex unStructuredIndex) {
   }

   @Override
   protected void populateCategorySpecificTablesWithDummyData (List<SemanticPossibility> semanticPossibilities,
            List<String> newsItemLinesToSemantify, UnStructuredIndex unstructuredIndex) {
   }

   @Override
   protected void createUdxKeyWord (Long applicationId, UDXKeyword udxKeyWord) {
      List<UDXKeyword> udxKeywords = new ArrayList<UDXKeyword>(1);
      udxKeywords.add(udxKeyWord);
      try {
         // TODO : - VG- need to store appId
         getUdxService().createUDXKeywords(udxKeywords);
      } catch (UDXException e) {
         e.printStackTrace();
      }
   }

   private void populateUdxKeyWord (List<SemanticPossibility> semanticPossibilities,
            List<String> newsItemLinesToSemantify, Long udxId) {
      UDXKeyword udxKeyword = new UDXKeyword();
      udxKeyword.setUdxId(udxId);
      StringBuilder sb = new StringBuilder(1);
      for (SemanticPossibility semanticPossibility : semanticPossibilities) {
         sb.append(getReducedFormHelper().getKeyWordMatchText(semanticPossibility, new HashMap<Long, Integer>(1)));
      }
      sb.append(ExecueCoreUtil.convertAsString(newsItemLinesToSemantify));
      String keyWordText = sb.toString();
      udxKeyword.setKeywordText(keyWordText);

   }

   @Override
   public void generateRFXAndRFXValueFromSemanticPossibilities (NewsItem newsItem,
            List<SemanticPossibility> semanticPossibilities, Set<ReducedFormIndex> rfxSet, Set<RFXValue> rfxValueSet,
            Long reducedFormId) {
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return;
      }
      for (SemanticPossibility semanticPossibility : semanticPossibilities) {
         Set<ReducedFormIndex> currentRfx = new HashSet<ReducedFormIndex>(1);
         Set<RFXValue> currentRfxValues = new HashSet<RFXValue>(1);
         getRfxServiceHelper().populateRFXAndRFXValueFromSemanticPossibility(semanticPossibility, currentRfx,
                  currentRfxValues, reducedFormId, -1L, false, RFXType.RFX_CONTENT, RFXValueType.RFX_VALUE_CONTENT,
                  new HashSet<String>(1), new HashSet<String>(1), new HashMap<Long, Integer>(1),
                  new HashMap<String, String>(1));
         rfxSet.addAll(currentRfx);
         rfxValueSet.addAll(currentRfxValues);
      }
   }

   @Override
   public List<SemanticPossibility> updatePossibilities (List<SemanticPossibility> semanticPossibilities) {
      return semanticPossibilities;
   }
}