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

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author Vishay
 */
public abstract class ExecueThreadPoolManager {

   private ExecutorService executorService;

   public <V> Future<V> submitTask (Callable<V> task) {
      return getExecutorService().submit(task);
   }

   public void submitTask (Runnable task) {
      getExecutorService().submit(task);
   }

   public void setExecutorService (ExecutorService executorService) {
      this.executorService = executorService;
   }

   protected ExecutorService getExecutorService () {
      return executorService;
   }

}
