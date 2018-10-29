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


package com.execue.offline.batch.shareddata.location;

import java.util.List;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.offline.batch.OfflineBatchComponents;
import com.execue.offline.batch.exception.OfflineBatchException;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.shareddata.location.bean.LocationDataInputInfo;

public class LocationDataPopulationOfflineBatch extends OfflineBatchComponents {

   /**
    * @param args
    * @throws Exception
    */
   public static void main (String[] args) throws Exception {

      LocationDataPopulationOfflineBatch locationDataPopulationOfflineBatch = new LocationDataPopulationOfflineBatch();

      locationDataPopulationOfflineBatch.initializeSystem();

      locationDataPopulationOfflineBatch.populateLocationData();

      System.exit(0);
   }

   public void populateLocationData () throws Exception {

      System.out
               .println("[LocationDataPopulationOfflineBatch::populateLocationData] Location Data Input Source Wrapper : "
                        + getLocationDataInputSourceWrapper().getClass().getName());

      try {
         long totalRecordsProcessed = 0;
         int batchNumber = 1;
         boolean moreRecordsFromSource = true;
         int currentBatchSize = 0;
         List<LocationDataInputInfo> locationDataInputInfosByBatch = null;
         do {
            locationDataInputInfosByBatch = getLocationDataInputSourceWrapper().getLocationDataInputInfo(batchNumber);
            if (ExecueCoreUtil.isCollectionEmpty(locationDataInputInfosByBatch)) {
               moreRecordsFromSource = false;
            } else {
               currentBatchSize = locationDataInputInfosByBatch.size();
               System.out
                        .println("[LocationDataPopulationOfflineBatch::populateLocationData] Processing Batch Number ["
                                 + batchNumber + "] with [" + currentBatchSize + "] number of records");
               for (LocationDataInputInfo locationDataInputInfo : locationDataInputInfosByBatch) {

                  if (!getLocationDataPopulationService().absorbLocationData(locationDataInputInfo)) {
                     System.out
                              .println("[LocationDataPopulationOfflineBatch::populateLocationData] Failed to absorb record : "
                                       + locationDataInputInfo.getPrintString());
                  }
               }

               totalRecordsProcessed = totalRecordsProcessed + currentBatchSize;

               System.out.println("[LocationDataPopulationOfflineBatch::populateLocationData] Batch Number ["
                        + batchNumber + "] Processing Completed and Total Number of Records Processed till now : "
                        + totalRecordsProcessed);

               if (getOfflineBatchConfigurationService().getLocationInputDataSourceBatchSize() > currentBatchSize) {
                  moreRecordsFromSource = false;
               }
               batchNumber = batchNumber + 1;
            }
         } while (moreRecordsFromSource);

         System.out
                  .println("[LocationDataPopulationOfflineBatch::populateLocationData] Completed with Total Records Processed : "
                           + totalRecordsProcessed);

      } catch (OfflineBatchException offlineBatchException) {
         offlineBatchException.printStackTrace();
      } catch (PlatformException platformException) {
         platformException.printStackTrace();
      }
   }
}
