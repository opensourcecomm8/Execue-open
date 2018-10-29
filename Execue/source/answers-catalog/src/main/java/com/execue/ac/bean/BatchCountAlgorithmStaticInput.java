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


package com.execue.ac.bean;

/**
 * This bean contains the input needed for batch size calculation algorithm which is same for whole of the mart creation
 * process.
 * 
 * @author Vishay
 * @version 1.0
 * @since 21/01/2011
 */
public class BatchCountAlgorithmStaticInput {

   private Long    sqlQueryMaxSize;
   private Integer emptyWhereConditionSize;
   private Integer whereConditionRecordBufferLength;
   private Integer populationMaxDataLength;
   private Integer maxAllowedExpressionsInCondition;

   public Integer getEmptyWhereConditionSize () {
      return emptyWhereConditionSize;
   }

   public void setEmptyWhereConditionSize (Integer emptyWhereConditionSize) {
      this.emptyWhereConditionSize = emptyWhereConditionSize;
   }

   public Integer getWhereConditionRecordBufferLength () {
      return whereConditionRecordBufferLength;
   }

   public void setWhereConditionRecordBufferLength (Integer whereConditionRecordBufferLength) {
      this.whereConditionRecordBufferLength = whereConditionRecordBufferLength;
   }

   public Integer getPopulationMaxDataLength () {
      return populationMaxDataLength;
   }

   public void setPopulationMaxDataLength (Integer populationMaxDataLength) {
      this.populationMaxDataLength = populationMaxDataLength;
   }

   public Long getSqlQueryMaxSize () {
      return sqlQueryMaxSize;
   }

   public void setSqlQueryMaxSize (Long sqlQueryMaxSize) {
      this.sqlQueryMaxSize = sqlQueryMaxSize;
   }

   public Integer getMaxAllowedExpressionsInCondition () {
      return maxAllowedExpressionsInCondition;
   }

   public void setMaxAllowedExpressionsInCondition (Integer maxAllowedExpressionsInCondition) {
      this.maxAllowedExpressionsInCondition = maxAllowedExpressionsInCondition;
   }

}
