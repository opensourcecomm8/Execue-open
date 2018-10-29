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


package com.execue.offline.batch;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExecueSystem;
import com.execue.core.type.SystemType;

public abstract class OfflineBaseBatch {

   private ApplicationContext context;

   public ApplicationContext getContext () {
      return context;
   }

   protected void initializeSystem () {
      context = new ClassPathXmlApplicationContext(new String[] { "execue-application.xml",
               "ext/bean-config/execue-offline-batch.xml" });
      try {

         ((ExecueSystem) getContext().getBean("execueSystem")).initialize(SystemType.BATCH);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
