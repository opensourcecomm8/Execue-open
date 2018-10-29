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


package com.execue.das.solr.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 */
public class SolrFacetQueryEntity extends SolrFacetFieldEntity {

   private List<SolrRangeEntity> facetRanges;

   public List<SolrRangeEntity> getFacetRanges () {
      return facetRanges;
   }

   public void setFacetRanges (List<SolrRangeEntity> facetRanges) {
      this.facetRanges = facetRanges;
   }

   public void addRangeEntity (SolrRangeEntity rangeEntity) {
      if (ExecueCoreUtil.isCollectionEmpty(facetRanges)) {
         facetRanges = new ArrayList<SolrRangeEntity>();
      }
      facetRanges.add(rangeEntity);
   }

}
