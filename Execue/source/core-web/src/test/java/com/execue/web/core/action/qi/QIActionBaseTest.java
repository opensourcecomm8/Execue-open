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

import java.io.InputStream;

import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class QIActionBaseTest {

   static ApplicationContext context;

   public static ApplicationContext getContext () {
      return context;
   }

   @BeforeClass
   public static void setup () {
      context = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-application.xml" });
   }

   public QueryInterfaceSearchAction getSearchAction () {
      return (QueryInterfaceSearchAction) getContext().getBean("qiSearchAction");
   }

   public String getResourceData (String name) throws Exception {
      InputStream inputStream = this.getClass().getResourceAsStream(name);
      StringBuilder stringBuilder = new StringBuilder();
      int availble = -1;
      while ((availble = inputStream.available()) > 0) {
         byte[] data = new byte[availble];
         inputStream.read(data);
         stringBuilder.append(new String(data));
      }
      inputStream.close();
      return stringBuilder.toString();
   }
}
