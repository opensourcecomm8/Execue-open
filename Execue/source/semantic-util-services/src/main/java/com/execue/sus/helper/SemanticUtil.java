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


package com.execue.sus.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.unstructured.RIFeatureContent;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.UniversalSearchResultFeatureHeader;

/**
 * @author kaliki
 */
public class SemanticUtil {

   public static List<String> getUnrecognizedQueryWords (SemanticPossibility semanticPossibility) {
      List<IGraphComponent> allGraphComponents = semanticPossibility.getAllGraphComponents();
      Set<Integer> positionsCovered = new HashSet<Integer>(1);
      for (IGraphComponent graphComponent : allGraphComponents) {
         positionsCovered.addAll(((DomainRecognition) graphComponent).getOriginalPositions());
      }
      List<String> unrecognizedQueryWords = new ArrayList<String>(1);
      List<String> queryWords = semanticPossibility.getQueryWords();
      int queryWordPosition = 0;
      for (String queryWord : queryWords) {
         if (!positionsCovered.contains(queryWordPosition)) {
            unrecognizedQueryWords.add(queryWord);
         }
         queryWordPosition++;
      }
      return unrecognizedQueryWords;
   }

   /**
    * This method iterates over the graph component and returns the set of bed ids
    * 
    * @param reducedForm
    * @return the Set<Long>
    */
   public static Set<Long> getAllBedIds (SemanticPossibility reducedForm) {
      Set<Long> existingUserQueryBedIds = new HashSet<Long>(1);
      Collection<IGraphComponent> rootVertices = reducedForm.getRootVertices();
      com.execue.core.common.bean.graph.Graph reducedFormGraph = reducedForm.getReducedForm();
      for (IGraphComponent rv : rootVertices) {
         List<IGraphComponent> components = (List<IGraphComponent>) reducedFormGraph.getDepthFirstRoute(rv);
         for (IGraphComponent graphComponent : components) {
            existingUserQueryBedIds.add(((DomainRecognition) graphComponent).getEntityBeId());
         }
      }
      return existingUserQueryBedIds;
   }

   /**
    * This method take list of column name and return list UniversalSearchResultFeatureHeader
    * 
    * @param List
    *           <RIFeatureContent> riFeatureContentList
    * @return the List<UniversalSearchResultFeatureHeader>
    */
   public static List<UniversalSearchResultFeatureHeader> populateUiversalSearchResultFeatureHeader (
            List<RIFeatureContent> riFeatureContentList) {
      List<UniversalSearchResultFeatureHeader> universalSearchResultFeatureHeaders = new ArrayList<UniversalSearchResultFeatureHeader>();
      for (RIFeatureContent riFeatureContent : riFeatureContentList) {
         UniversalSearchResultFeatureHeader universalSearchResultFeatureHeader = new UniversalSearchResultFeatureHeader();
         universalSearchResultFeatureHeader.setId(riFeatureContent.getFeatureId().toString());
         universalSearchResultFeatureHeader.setName(riFeatureContent.getFeatureDisplayName());
         universalSearchResultFeatureHeader.setFormat(riFeatureContent.getFeatureSymbol());
         universalSearchResultFeatureHeader.setColumnName(riFeatureContent.getDisplayableColumnName());
         universalSearchResultFeatureHeader.setSortable(riFeatureContent.getSortable());
         universalSearchResultFeatureHeader.setDiplayableFeatureAlignmentType(riFeatureContent
                  .getDisplayableFeatureAlignmentType());
         universalSearchResultFeatureHeader.setDefaultSortOrder(riFeatureContent.getDefaultSortOrder());
         universalSearchResultFeatureHeader.setFeatureValueType(riFeatureContent.getFeatureValueType());
         universalSearchResultFeatureHeader.setDataHeader(riFeatureContent.getDataHeader());
         universalSearchResultFeatureHeaders.add(universalSearchResultFeatureHeader);
      }

      return universalSearchResultFeatureHeaders;
   }
}