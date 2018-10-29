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


package com.execue.querybuilder.service.client.helper;

import com.execue.core.constants.ISQLQueryConstants;

/**
 * @author John Mallavalli
 */
public class ClientSourceQueryBuilderServiceHelper implements ISQLQueryConstants {

   public static int[] resetBandValuesIfRequired (int[] band) {
      int[] newBand = new int[2];
      if (band[0] == 1) {
         newBand[0] = 0;
      } else {
         newBand[0] = band[0];
      }
      newBand[1] = band[1];
      return newBand;
   }

   public static String surroundWithSpaces (String text) {
      StringBuffer sb = new StringBuffer();
      sb.append(SQL_SPACE_DELIMITER).append(text).append(SQL_SPACE_DELIMITER);
      return sb.toString();
   }

   public static String modifyToAddTopKeyword (int workingSetCount, String innerQueryString) {
      // find the index of distinct and add the top clause after the distinct keyword
      int index = innerQueryString.indexOf(SQL_DISTINCT_KEYWORD);
      if (index != -1) {
         int indexAfterDistinctKeyword = index + SQL_DISTINCT_KEYWORD.length();
         StringBuffer modifiedInnerQueryString = new StringBuffer();
         StringBuffer topClause = new StringBuffer();
         topClause.append(MSSQL_LIMIT_CLAUSE).append(SQL_SPACE_DELIMITER).append(workingSetCount);
         String queryTillDistinctKeyword = innerQueryString.substring(0, (indexAfterDistinctKeyword));
         String queryAfterDistinctKeyword = innerQueryString.substring(indexAfterDistinctKeyword);
         modifiedInnerQueryString.append(queryTillDistinctKeyword).append(SQL_SPACE_DELIMITER).append(topClause)
                  .append(queryAfterDistinctKeyword);
         return modifiedInnerQueryString.toString();
      }
      return innerQueryString;
   }

   public static String getOrderByType (String orderByClause) {
      String orderByType = SQL_ORDER_BY_ASC;
      String[] splits = orderByClause.split(SQL_SPACE_DELIMITER);
      // get the last token
      String type = splits[splits.length - 1];
      if (type.equalsIgnoreCase(SQL_ORDER_BY_DESC)) {
         orderByType = SQL_ORDER_BY_DESC;
      }
      return orderByType;
   }
}
