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


package com.execue.web.core.action.swi;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SWIActionBaseTest {

   private static ApplicationContext swiActionContext;

   public ApplicationContext getswiContext () {
      return swiActionContext;
   }

   public void setswiContext (ApplicationContext swiContxt) {
      swiActionContext = swiContxt;
   }

   public static void swiActionBaseTearDown () {

   }

   @BeforeClass
   public static void swiActionBaseSetup () {
      swiActionContext = new ClassPathXmlApplicationContext(new String[] { "/bean-config/spring-hibernate.xml",
               "/bean-config/execue-dataaccess.xml", "/bean-config/execue-swi.xml", "/bean-config/execue-swi-web.xml" });

   }

   public SDXAction getSDXAction () {
      return (SDXAction) getswiContext().getBean("sdxAction");
   }
   public KDXAction getKDXAction () {
      return (KDXAction) getswiContext().getBean("kdxAction");
   }
   
   public ParallelWordsAction getParallelWordsAction () {
      return (ParallelWordsAction) getswiContext().getBean("parallelWordsAction");
   }
}
