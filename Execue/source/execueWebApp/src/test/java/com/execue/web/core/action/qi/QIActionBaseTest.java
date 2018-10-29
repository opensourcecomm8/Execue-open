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


package com.execue.web.core.action.qi;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.hibernate.SessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExecueSystem;
import com.execue.web.core.action.swi.ParallelWordsAction;

public class QIActionBaseTest {

   static ApplicationContext context;

   public static ApplicationContext getContext () {
      return context;
   }

   @BeforeClass
   public static void setup () {
      context = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-application.xml" });
      try {
         ((ExecueSystem) getContext().getBean("execueSystem")).initialize();
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @AfterClass
   public static void shutdown () {
      if (context == null) {
         return;
      }
      SessionFactory factory = (SessionFactory) context.getBean("swiSessionFactory");
      if (factory != null) {
         factory.close();
      }
   }

   public QueryInterfaceSearchAction getSearchAction () {
      return (QueryInterfaceSearchAction) getContext().getBean("qiSearchAction");
   }

   public QueryInterfaceSuggestAction getConversionAction () {
      return (QueryInterfaceSuggestAction) getContext().getBean("qiSuggestAction");
   }
   
   public ParallelWordsAction getParallelWordsAction () {
      return (ParallelWordsAction) getContext().getBean("parallelWordsAction");
   }

   public String getResourceData (String name) throws Exception {
      StringBuilder stringBuilder = new StringBuilder();
      BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(name)));
      String line = null;
      while ((line = br.readLine()) != null) {
         stringBuilder.append(line);
      }
      br.close();
      return stringBuilder.toString();
   }
}