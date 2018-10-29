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
import com.execue.semantification.exception.SemantificationException;

public class GenericArticleBadImageURLCleanupOfflineBatch extends OfflineBatchComponents {

   private static final Logger logger = Logger.getLogger(GenericArticleBadImageURLCleanupOfflineBatch.class);

   public static void main (String[] args) {
      GenericArticleBadImageURLCleanupOfflineBatch genericArticleBadImageURLCleanupBatch = new GenericArticleBadImageURLCleanupOfflineBatch();
      genericArticleBadImageURLCleanupBatch.initializeSystem();

      Long contextId = null;

      if (args == null) {
         logger.error("Context Id is Needed !!!");
      } else if (args.length == 1) {
         contextId = Long.valueOf(args[0]).longValue();
      } else if (args.length > 1) {
         logger.error("Only Context Id needs to be specified !!!)");
      }

      if (contextId != null) {
         genericArticleBadImageURLCleanupBatch.cleanupGenericArticleBadImageURLs(contextId);
      }

      System.exit(0);
   }

   public void cleanupGenericArticleBadImageURLs (Long contextId) {
      try {
         getGenericArticleBadImageURLCleanupService().cleanupArticleBadImageURLs(contextId);
         logger.fatal("Cleanup Bad Image URLs Batch Job Completed");
      } catch (SemantificationException e) {
         logger.fatal("Inside GenericArticleBadImageURLCleanupBatch.cleanupArticleBadImageURLs:Error during cleanup");
         e.printStackTrace();
      }
   }
}