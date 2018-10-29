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


package com.execue.platform.impl;

import org.apache.log4j.Logger;

import com.execue.core.exception.UidException;
import com.execue.platform.IUidService;
import com.execue.platform.querydata.IQueryDataPlatformRetrievalService;
import com.execue.qdata.exception.QueryDataException;

public abstract class SeedIdGenerationServiceImpl implements IUidService {

   private IQueryDataPlatformRetrievalService queryDataPlatformRetrievalService;
   private Logger                             logger          = Logger.getLogger(SeedIdGenerationServiceImpl.class);
   private static int                         nodeId          = 0;
   private static int                         seed            = 0;
   private static long                        base            = 0;
   private static long                        id              = 0;                                              // start
   // id
   private static long                        MAX_SEQUENCE_ID = 999L;

   /**
    * combination of node+seed+incrementValue
    */
   public synchronized long getNextId () throws UidException {
      if (id > MAX_SEQUENCE_ID) {
         // reset id
         try {
            refreshBaseValue();
         } catch (Exception e) {
            logger.error("Error setting get Seed value", e);
         }

         id = 1;
         return base + id++;
      }
      return base + id++;
   }

   public void refreshBaseValue () throws QueryDataException {
      String type = getType();
      Integer value = getQueryDataPlatformRetrievalService().getNextSeedValueByTypeAndNodeId(new Integer(getNodeId()).longValue(),
               type);
      setSeed(value);
      // set base seed
      // TODO -KA- CHECH THE LOGIC AGAIN ON BOUNDARY CONDITIONS
      // move to formating and setup to configuration file
      long base = Long.valueOf(getNodeId() + "" + String.format("%07d", getSeed())) * 1000L;
      setBase(base);
   }

   public int getSeed () {
      return seed;
   }

   protected abstract String getType () throws QueryDataException;

   public int getNodeId () {
      return nodeId;
   }

   public void setNodeId (int nodeId) {
      SeedIdGenerationServiceImpl.nodeId = nodeId;
   }

   public IQueryDataPlatformRetrievalService getQueryDataPlatformRetrievalService () {
      return queryDataPlatformRetrievalService;
   }

   public void setQueryDataPlatformRetrievalService (
            IQueryDataPlatformRetrievalService queryDataPlatformRetrievalService) {
      this.queryDataPlatformRetrievalService = queryDataPlatformRetrievalService;
   }

   public static void setSeed (int seed) {
      SeedIdGenerationServiceImpl.seed = seed;
   }

   public static long getBase () {
      return base;
   }

   public static void setBase (long base) {
      SeedIdGenerationServiceImpl.base = base;
   }

}
