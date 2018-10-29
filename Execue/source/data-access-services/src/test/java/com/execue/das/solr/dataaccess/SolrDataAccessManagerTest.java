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


package com.execue.das.solr.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.das.DataAccessServicesCommonBaseTest;
import com.execue.das.solr.bean.SolrFacetQuery;
import com.execue.das.solr.bean.SolrFacetQueryConstantParameters;
import com.execue.das.solr.bean.SolrFacetQueryInfo;
import com.execue.das.solr.exception.SolrException;

/**
 * @author Vishay
 */
public class SolrDataAccessManagerTest extends DataAccessServicesCommonBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testexecuteSolrFacetQuery () throws SolrException {
      SolrFacetQuery solrFacetQuery = new SolrFacetQuery();
      solrFacetQuery.setQuery("*:*");
      SolrFacetQueryConstantParameters solrFacetQueryConstantParameters = new SolrFacetQueryConstantParameters();
      solrFacetQueryConstantParameters.setFacet(true);
      solrFacetQueryConstantParameters.setFacetLimit(-1);
      solrFacetQueryConstantParameters.setFetchFacetsWithMinCount(1);
      solrFacetQueryConstantParameters.setFacetSortByCount(true);
      solrFacetQueryConstantParameters.setTotalRows(0);
      solrFacetQueryConstantParameters.setFacetMethodParamName("facet.method");
      solrFacetQueryConstantParameters.setFacetMethodParamValue("enum");
      solrFacetQuery.setSolrFacetQueryConstantParameters(solrFacetQueryConstantParameters);
      // solrQuery.addFilterQuery(filterQuery);
      List<String> facetFields = new ArrayList<String>();
      facetFields.add("name_s");
      solrFacetQuery.setFacetFields(facetFields);
      Map<String, List<SolrFacetQueryInfo>> facetQueries = new HashMap<String, List<SolrFacetQueryInfo>>();
      List<SolrFacetQueryInfo> solrFacetQueryInfoList = new ArrayList<SolrFacetQueryInfo>();
      solrFacetQueryInfoList.add(new SolrFacetQueryInfo("price_d:[* TO 100]", "low"));
      solrFacetQueryInfoList.add(new SolrFacetQueryInfo("price_d:[100 TO 300]", "Mid"));
      solrFacetQueryInfoList.add(new SolrFacetQueryInfo("price_d:[300 TO *]", "High"));
      facetQueries.put("price_d", solrFacetQueryInfoList);
      solrFacetQueryInfoList = new ArrayList<SolrFacetQueryInfo>();
      solrFacetQueryInfoList.add(new SolrFacetQueryInfo("ctx_l:[* TO 100]", "low"));
      solrFacetQueryInfoList.add(new SolrFacetQueryInfo("ctx_l:[100 TO 300]", "Mid"));
      solrFacetQueryInfoList.add(new SolrFacetQueryInfo("ctx_l:[300 TO *]", "High"));
      facetQueries.put("ctx_l", solrFacetQueryInfoList);
      solrFacetQuery.setFacetQueries(facetQueries);
      getSolrDataAccessManager().executeSolrFacetQuery(110L, solrFacetQuery);
   }
}
