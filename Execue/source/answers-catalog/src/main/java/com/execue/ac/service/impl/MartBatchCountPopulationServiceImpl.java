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


package com.execue.ac.service.impl;

import com.execue.ac.bean.BatchCountAlgorithmStaticInput;
import com.execue.ac.service.IMartBatchCountPopulationService;
import com.execue.ac.exception.AnswersCatalogException;

/**
 * This service prepares population batch count which can be used in one query
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartBatchCountPopulationServiceImpl implements IMartBatchCountPopulationService {

   /**
    * This method returns the batch count for query. It uses static input as well as the query size without where
    * condition for which batch count is required. StaticQuerySize is the size of the query without where condition.
    * 
    * @param batchCountAlgorithmStaticInput
    * @param staticQuerySize
    * @return batch count
    * @throws AnswersCatalogException
    */

   public Integer populateBatchCountForPopulation (BatchCountAlgorithmStaticInput batchCountAlgorithmStaticInput,
            Integer staticQuerySize) throws AnswersCatalogException {

      Long sqlQueryMaxSize = batchCountAlgorithmStaticInput.getSqlQueryMaxSize();
      Integer emptyWhereConditionSize = batchCountAlgorithmStaticInput.getEmptyWhereConditionSize();
      Integer populationMaxDataLength = batchCountAlgorithmStaticInput.getPopulationMaxDataLength();
      Integer whereConditionRecordBufferLength = batchCountAlgorithmStaticInput.getWhereConditionRecordBufferLength();
      // calculate actual query size
      Integer actualQuerySize = staticQuerySize + emptyWhereConditionSize;
      // available query size
      Long availableQuerySize = sqlQueryMaxSize - actualQuerySize;
      // actual record length which includes buffer for reach record which includes ', kind of characters
      Integer actualRecordLength = populationMaxDataLength + whereConditionRecordBufferLength;
      Long batchCount = availableQuerySize / actualRecordLength;
      Integer batchSize = batchCount.intValue();
      return batchSize;
   }

}