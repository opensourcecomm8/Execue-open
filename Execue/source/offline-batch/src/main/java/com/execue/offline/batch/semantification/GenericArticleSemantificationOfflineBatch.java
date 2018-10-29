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

import org.apache.log4j.Logger;

import com.execue.offline.batch.OfflineBatchComponents;
import com.execue.semantification.batch.service.IGenericArticleSemantificationService;

public class GenericArticleSemantificationOfflineBatch extends OfflineBatchComponents {

   private static final Logger log = Logger.getLogger(GenericArticleSemantificationOfflineBatch.class);

   /**
    * @param args
    */
   public static void main (String[] args) {
      try {
         GenericArticleSemantificationOfflineBatch genericArticleSemantificationOfflineBatch = new GenericArticleSemantificationOfflineBatch();

         genericArticleSemantificationOfflineBatch.initializeSystem();

         Long contextId = null;
         Long batchId = null;

         if (args == null) {
            log.error("Context Id is Needed !!!");
         } else if (args.length == 2) {
            contextId = Long.valueOf(args[0]).longValue();
            batchId = Long.valueOf(args[1]).longValue();
         } else if (args.length == 1) {
            contextId = Long.valueOf(args[0]).longValue();
         } else {
            log.error("Context Id and Batch Id are required !!! (Batch Id is optional, default Batch Id is Node Id)");
         }

         if (contextId != null) {
            genericArticleSemantificationOfflineBatch.semantifyContent(contextId, batchId);
         }
      } catch (Exception exception) {
         log.error(exception, exception);
      } finally {
         System.exit(0);
      }
   }

   public void semantifyContent (Long contextId, Long batchId) {
      IGenericArticleSemantificationService genericArticleSemantificationService = getGenericArticleSemantificationService();
      if (batchId != null) {
         genericArticleSemantificationService.setBatchId(batchId);
      }
      genericArticleSemantificationService.semantifiArticles(contextId);
      log.fatal("Semantification Batch Job Completed");
   }

}
