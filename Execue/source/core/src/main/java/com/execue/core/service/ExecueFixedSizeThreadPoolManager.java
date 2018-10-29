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


package com.execue.core.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.execue.core.bean.ThreadPoolTaskStatus;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.type.ThreadPoolTaskStatusType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 */
public class ExecueFixedSizeThreadPoolManager extends ExecueThreadPoolManager {

   private static final Logger logger = Logger.getLogger(ExecueFixedSizeThreadPoolManager.class);

   private Integer             numThreads;
   // This is used in waitTillTaskComplete method to delay the checking by calling the Thread.wait with this value
   private Integer             threadWaitTime;

   public ExecueFixedSizeThreadPoolManager (Integer numThreads, Integer threadWaitTime) {
      super();
      this.numThreads = numThreads;
      this.threadWaitTime = threadWaitTime;
      setExecutorService(Executors.newFixedThreadPool(numThreads));
   }

   public void forceShutdown () {
      if (!getExecutorService().isShutdown()) {
         getExecutorService().shutdownNow();
      }
   }

   public void waitTillComplete () {
      getExecutorService().shutdown();
      while (!getExecutorService().isTerminated()) {

      }
   }

   public boolean doesAllTasksCompletedSuccessfully (List<Future<? extends ThreadPoolTaskStatus>> taskStatusList)
            throws InterruptedException, ExecutionException {
      boolean allTasksCompletedSuccessfully = true;
      for (Future<? extends ThreadPoolTaskStatus> future : taskStatusList) {
         if (!future.get().getTaskStatusType().equals(ThreadPoolTaskStatusType.SUCCESS)) {
            allTasksCompletedSuccessfully = false;
            break;
         }
      }
      return allTasksCompletedSuccessfully;
   }

   /**
    * Method to wait till all the tasks in the given input list are completed. 
    * It uses the threadWaitTime to delay the wait check by calling Thread.wait method
    * Note it doesn't call the shutdown call, its just checks whether the ThreadPoolTaskStatusType
    * is SUCCESS OR FAILURE.
    * 
    * @param taskStatusList
    * @throws InterruptedException
    * @throws ExecutionException
    */
   public void waitTillTaskComplete (List<Future<? extends ThreadPoolTaskStatus>> taskStatusList)
            throws InterruptedException, ExecutionException {
      List<Future<? extends ThreadPoolTaskStatus>> newTaskStatusList = new ArrayList<Future<? extends ThreadPoolTaskStatus>>();
      newTaskStatusList.addAll(taskStatusList);
      boolean allTasksCompleted = false;
      do {
         for (Iterator<Future<? extends ThreadPoolTaskStatus>> iterator = newTaskStatusList.iterator(); iterator
                  .hasNext();) {
            Future<? extends ThreadPoolTaskStatus> future = iterator.next();
            if (future.get().getTaskStatusType().equals(ThreadPoolTaskStatusType.SUCCESS)
                     || future.get().getTaskStatusType().equals(ThreadPoolTaskStatusType.FAILED)) {
               iterator.remove();
            }
         }
         if (ExecueCoreUtil.isCollectionEmpty(newTaskStatusList)) {
            allTasksCompleted = true;
         } else {
            if (logger.isDebugEnabled()) {
               logger.debug("Waiting for thread pool tasks to complete...");
            }
            Thread.sleep(threadWaitTime);
         }

      } while (!allTasksCompleted);
   }

   public String composeFailureReason (List<Future<? extends ThreadPoolTaskStatus>> taskStatusList)
            throws InterruptedException, ExecutionException {
      StringBuilder sb = new StringBuilder("");
      int taskIndex = 0;
      for (Future<? extends ThreadPoolTaskStatus> future : taskStatusList) {
         if (!future.get().getTaskStatusType().equals(ThreadPoolTaskStatusType.SUCCESS)) {
            sb.append(ExecueConstants.LINE_SEPARATOR);
            sb.append("Reason at Task [" + taskIndex + "] : " + future.get().getFailureReason());
         }
         taskIndex++;
      }
      return sb.toString();
   }
}