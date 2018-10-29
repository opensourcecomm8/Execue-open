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


package com.execue.nlp.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.BusinessOrderClause;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.nlp.NLPInformation;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.UserQuery;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.common.type.SearchType;
import com.execue.nlp.engine.barcode.matrix.MatrixUtility;

public class NLPEngineTest extends NLPCommonBaseTest {

   private Logger              logger   = Logger.getLogger(NLPEngineTest.class);

   private static final String NEW_LINE = System.getProperty("line.separator");

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @Test
   public void testBaseEngine () {
      NLPInformation information = null;
      String query = null;
      List<BusinessQuery> queries = new ArrayList<BusinessQuery>();
      try {
         //         query = "30 days DLQ" ;
         //         query = "summation NetSales > 2000";
         //         query = "sum sales, fico";
         //         query = "average sales summation cash";
         //         query = "month1 MerchandiseAmount";
         //         query = "january 2008 ficoscore where december 2007 MerchandiseAmount > 10000";
         //         query = "sales where product = more";
         //         query = "marketing program for Account";
         //         query = "account for joiningdate < jan2007";
         //         query = "film";
         //         query = "clint eastwood";
         //         query = "films by director clint eastwood";
         //         query = "AMAZON.COM netsales";
         //         query = "year 2007 amazon sales > 20000";
         //         query = "company net sales for year 2007";
         //         query = "net sales income";
         //         query = "Sales for GM and Toyota";
         //         query = "Inventory Cash amzn";
         //         query = "NetSales where NetIncome <= 1000000";
         //         query = "sales between year 2005 and year 2007";
         //         query = "sales between nominalyear 2005 and 2007";
         //         query = "sales in 500 and 1000";
         //         query = "amazon sales";
         //         query = "sales amazon";
         //         query = "net sales of Military industry";
         //         query = "net sales of military industry for year 2005";
         //         query = "amzn sales last 5 years" ;
         //         query = "sales for year 2007 and 2008";
         //         query = "net sales for year 2005";
         //         query = "Amzn inventory last 5 fiscal year";
         //         query = "amzn sales NominalQuarter1 2008";
         //         query = "sum sales";
         //         query = "amzn sales > 1000";
         //         query = "amzn sales";
         //         query = "sales by market capital";
         //         query = "sales by sector";
         //         query = "Mini financials of Military industry year 2001";
         //         query = "sales of Military industry year 2001";
         //         query = "short ratio in electronics sector";
         //         query = "amazon sales > 1000" ;
         //         query = "year 2008 net sales and income" ;
         //         query = "inventory cash amzn";
         //         query = "fiscal year 3 totalassets";
         query = "sales > 1000 income year 2005";
         // query = "net sales year 2005";
         // query = "sales last 5 year" ;
         // query = "sales for quarter 4 2009" ;
         // query = "sales between quarter2 and quarter3 2007";
         // query = "sales between quarter3 2007";
         // query = "sales for quarter1 2007";
         // query = "average sales summation income";
         // query = "sales for amzn";
         // query = "net for amzn" ;
         // query = "sales by income";
         // query = "sales";
         // query = "Population > 10000 by age";
         query = "Population > 10000";
         query = "asian population in alaska where age between 15 and 50";

         // UserQuery userQuery = getUserQuery(query, SearchFilterType.APP, 1508L);// craigslist
         UserQuery userQuery = getUserQuery(query, SearchFilterType.APP, 1092L);// Census 2000
         // UserQuery userQuery = getUserQuery(query, SearchFilterType.APP, 101L);// SEC fillings
         information = getNLPEngine().processQuery(userQuery, null);
         logger.info("Reduced forms " + information.getReducedForms().size());
         if (logger.isInfoEnabled()) {
            MatrixUtility.displayReducedForms(information.getReducedForms());
         }
         List<BusinessQuery> businessQueries = null;
         boolean isReducedFormValid = false;
         int possibilityCounter = 0;
         StringBuilder sb = new StringBuilder();
         PrintUtil printUtil = new PrintUtil();
         for (SemanticPossibility reducedForm : information.getReducedForms().keySet()) {
            possibilityCounter++;
            sb.append(NEW_LINE);
            sb.append("Possibility : ");
            sb.append(possibilityCounter);
            sb.append(NEW_LINE);
            sb.append("Validity : ");
            sb.append(isReducedFormValid);
            businessQueries = getReducedFormBusinessQueryGenerator().generateBQs(reducedForm);
            queries.addAll(businessQueries);
            sb.append(NEW_LINE);
            sb.append("BQs,");
            for (BusinessQuery businessQuery : businessQueries) {
               sb.append(NEW_LINE);
               sb.append(printUtil.printBusinessQuery(businessQuery));
               System.out.println(printUtil.printBusinessQuery(businessQuery));
               getBusinessQueryOrganizationService().organizeBusinessQuery(businessQuery);
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
      Assert.assertNotNull(information);
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   private UserQuery getUserQuery (String query, SearchFilterType searchFilterType, Long contextId) {
      UserQuery userQuery = new UserQuery();
      userQuery.setOriginalQuery(query);
      userQuery.setSearchType(SearchType.DEFAULT);
      SearchFilter searchFilter = new SearchFilter();
      searchFilter.setSearchFilterType(searchFilterType);
      searchFilter.setId(contextId);
      userQuery.setSearchFilter(searchFilter);
      return userQuery;
   }
}

/**
 * @author execue
 */
class PrintUtil {

   private static final String NEW_LINE = System.getProperty("line.separator");

   // TODO: -RG- should be moved to Util and should never be used in run time.
   // Should only be used for debugging purposes.

   public String printBusinessQuery (BusinessQuery businessQuery) {
      String stringFormOfBQ = null;

      StringBuilder sb = new StringBuilder();

      if (!CollectionUtils.isEmpty(businessQuery.getMetrics())) {
         sb.append("Select Size : ");
         sb.append(businessQuery.getMetrics().size());
         sb.append(NEW_LINE);
         for (BusinessTerm businessTerm : businessQuery.getMetrics()) {
            sb.append("Concept Name : "
                     + ((Concept) businessTerm.getBusinessEntityTerm().getBusinessEntity()).getDisplayName());
            if (businessTerm.getBusinessStat() != null) {
               sb.append(NEW_LINE);
               sb.append("  Stat : " + businessTerm.getBusinessStat().getStat().getDisplayName());
            }
            sb.append(NEW_LINE);
         }
      }
      if (!CollectionUtils.isEmpty(businessQuery.getConditions())) {
         sb.append("Conditions Size : ");
         sb.append(businessQuery.getConditions().size());
         sb.append(NEW_LINE);
      }
      if (!CollectionUtils.isEmpty(businessQuery.getPopulations())) {
         sb.append("Populations : ");
         for (BusinessTerm businessTerm : businessQuery.getPopulations()) {
            sb.append(((Concept) businessTerm.getBusinessEntityTerm().getBusinessEntity()).getDisplayName())
                     .append(",");
         }
         sb.append(NEW_LINE);
      }
      if (!CollectionUtils.isEmpty(businessQuery.getSummarizations())) {
         sb.append("Summarization : ");
         for (BusinessTerm businessTerm : businessQuery.getSummarizations()) {
            sb.append(((Concept) businessTerm.getBusinessEntityTerm().getBusinessEntity()).getDisplayName())
                     .append(",");
         }
         sb.append(NEW_LINE);
      }
      if (!CollectionUtils.isEmpty(businessQuery.getHavingClauses())) {
         sb.append("Having Size : ");
         sb.append(businessQuery.getHavingClauses().size());
         sb.append(NEW_LINE);
      }
      if (!CollectionUtils.isEmpty(businessQuery.getOrderClauses())) {
         sb.append("Order : ");
         for (BusinessOrderClause businessClause : businessQuery.getOrderClauses()) {
            sb.append(
                     ((Concept) businessClause.getBusinessTerm().getBusinessEntityTerm().getBusinessEntity())
                              .getDisplayName()).append(",");
         }
         sb.append(NEW_LINE);
      }
      stringFormOfBQ = sb.toString().trim();

      return stringFormOfBQ;
   }
}