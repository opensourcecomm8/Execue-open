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


package com.execue.offline.batch.contenttransportation;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.offline.batch.OfflineBatchComponents;
import com.execue.platform.exception.UnstructuredContentTransporterException;

public class UnstructuredTargetBasedContentTransporterOfflineBatch extends OfflineBatchComponents {

   private static final Logger log = Logger.getLogger(UnstructuredTargetBasedContentTransporterOfflineBatch.class);

   public static void main (String[] args) {
      try {
         UnstructuredTargetBasedContentTransporterOfflineBatch unstructuredTargetBasedContentTransporterOfflineBatch = new UnstructuredTargetBasedContentTransporterOfflineBatch();
         unstructuredTargetBasedContentTransporterOfflineBatch.initializeSystem();

         List<Long> targetWareHouseDataSourceIds = new ArrayList<Long>();

         if (args == null) {
            log.error("Target Warehouse Data Source Id(s) is Needed !!!");
         } else {
            for (String argument : args) {
               targetWareHouseDataSourceIds.add(Long.valueOf(argument));
            }
         }

         if (ExecueCoreUtil.isCollectionNotEmpty(targetWareHouseDataSourceIds)) {
            unstructuredTargetBasedContentTransporterOfflineBatch.transportContent(targetWareHouseDataSourceIds);
         }
      } catch (Exception exception) {
         log.error(exception, exception);
      } finally {
         System.exit(0);
      }
   }

   private void transportContent (List<Long> targetWareHouseDataSourceIds) {
      try {
         getUnstructuredTargetBasedContentTransporter().transportContent(targetWareHouseDataSourceIds);
      } catch (UnstructuredContentTransporterException usctException) {
         log.error("Error in Transporting Content : " + usctException.getMessage());
         usctException.printStackTrace();
      }
   }
}
