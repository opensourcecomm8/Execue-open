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


package com.execue.offline.batch.semantification;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.offline.batch.OfflineBatchComponents;

public class GenericArticleRuntimeTablesCleanupOfflineBatch extends OfflineBatchComponents {

   private static final Logger logger = Logger.getLogger(GenericArticleRuntimeTablesCleanupOfflineBatch.class);

   public static void main (String[] args) {
      GenericArticleRuntimeTablesCleanupOfflineBatch portalCraigslistRuntimeTablesCleanupBatch = new GenericArticleRuntimeTablesCleanupOfflineBatch();
      List<Long> contextIds = new ArrayList<Long>();

      if (args == null) {
         logger.error("Context Id(s) is Needed !!!");
      } else if (args.length == 1) {
         for (String argument : args) {
            contextIds.add(Long.valueOf(argument));
         }
      }
      portalCraigslistRuntimeTablesCleanupBatch.initializeSystem();

      portalCraigslistRuntimeTablesCleanupBatch.cleanupRuntimeTables(contextIds);

      System.exit(0);
   }

   public void cleanupRuntimeTables (List<Long> contextIds) {
      getGenericArticleRuntimeTablesCleanupService().cleanupRuntimeTables(contextIds);
      logger.fatal("Cleanup Runtime Tables Batch Job Completed");
   }
}