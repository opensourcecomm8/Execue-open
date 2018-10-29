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


package com.execue.ks.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.system.ExecueSystem;
import com.execue.core.type.SystemType;
import com.execue.ks.service.IKnowledgeBaseSearchEngine;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 12:33:27 AM
 */
public abstract class KBBaseTest {

   static ApplicationContext kbContext;

   public static ApplicationContext getContext () {
      return kbContext;
   }

   public static void baseSetup () {
      kbContext = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-configuration.xml",
               "/bean-config/execue-nlp.xml", "/bean-config/execue-ontology.xml",
               "/bean-config/execue-logging-configuration.xml", "/bean-config/execue-swi.xml",
               "/bean-config/execue-dataaccess.xml", "/bean-config/spring-hibernate.xml",
               "/bean-config/execue-query-generation.xml", "/bean-config/execue-util.xml",
               "/bean-config/execue-knowledge-search.xml" });
      try {
         ((ExecueSystem) getContext().getBean("execueSystem")).initialize(SystemType.WEBAPP);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static void baseTeardown () {
   }

   public IKnowledgeBaseSearchEngine getKBSearchEngine () {
      return (IKnowledgeBaseSearchEngine) kbContext.getBean("knowledgeSearchEngine");
   }

}