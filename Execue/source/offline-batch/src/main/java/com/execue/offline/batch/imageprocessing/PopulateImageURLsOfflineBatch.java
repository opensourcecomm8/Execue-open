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


package com.execue.offline.batch.imageprocessing;

import org.apache.log4j.Logger;

import com.execue.offline.batch.OfflineBatchComponents;

public class PopulateImageURLsOfflineBatch extends OfflineBatchComponents {

   private static final Logger logger = Logger.getLogger(PopulateImageURLsOfflineBatch.class);

   public static void main (String[] args) {
      PopulateImageURLsOfflineBatch populateImageURLsBatch = new PopulateImageURLsOfflineBatch();
      populateImageURLsBatch.initializeSystem();

      Long contextId = null;
      Long batchId = null;

      if (args == null) {
         logger.error("Context Id is Needed !!!");
      } else if (args.length == 2) {
         contextId = Long.valueOf(args[0]).longValue();
         batchId = Long.valueOf(args[1]).longValue();
      } else if (args.length == 1) {
         contextId = Long.valueOf(args[0]).longValue();
      } else {
         logger.error("Context Id and Batch Id are required !!! (Batch Id is optional, default Batch Id is Node Id)");
      }

      if (contextId != null) {
         populateImageURLsBatch.populateSemantifiedContentImages(contextId, batchId);
      }

      System.exit(0);
   }

   public void populateSemantifiedContentImages (Long contextId, Long batchId) {
      if (batchId != null) {
         getGenericArticleSemantificationService().setBatchId(batchId);
      }
      getGenericArticleSemantificationService().populateSemantifiedArticleImages(contextId);
      logger.fatal("Image URL Processing Batch Job Completed");
   }
}
