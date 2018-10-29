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


package com.execue.core.common.bean.publisher;

import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;

/**
 * This bean represents the DBTable information along with normalized information
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class DBTableNormalizedInfo extends DBTableInfo {

   private List<QueryColumn>       normalizedColumns;
   private Long                    totalRecordsCount;
   private List<QueryColumn>       virtualLookupColumns;
   private List<QueryColumnDetail> normalizedColumnDetails;

   public Long getTotalRecordsCount () {
      return totalRecordsCount;
   }

   public void setTotalRecordsCount (Long totalRecordsCount) {
      this.totalRecordsCount = totalRecordsCount;
   }

   public List<QueryColumn> getNormalizedColumns () {
      return normalizedColumns;
   }

   public void setNormalizedColumns (List<QueryColumn> normalizedColumns) {
      this.normalizedColumns = normalizedColumns;
   }

   public List<QueryColumn> getVirtualLookupColumns () {
      return virtualLookupColumns;
   }

   public void setVirtualLookupColumns (List<QueryColumn> virtualLookupColumns) {
      this.virtualLookupColumns = virtualLookupColumns;
   }

   public List<QueryColumnDetail> getNormalizedColumnDetails () {
      return normalizedColumnDetails;
   }

   public void setNormalizedColumnDetails (List<QueryColumnDetail> normalizedColumnDetails) {
      this.normalizedColumnDetails = normalizedColumnDetails;
   }

}
