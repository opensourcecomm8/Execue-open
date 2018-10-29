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


package com.execue.reporting.presentation.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.core.ISystem;
import com.execue.reporting.presentation.service.PresentationTransformServiceFactory;

public abstract class PresentationBaseTest {

   static ApplicationContext presentationContext;

   public ApplicationContext getContext () {
      return presentationContext;
   }

   public static void baseSetup () {
      presentationContext = new ClassPathXmlApplicationContext(new String[] { "/bean-config/execue-report-presentation.xml", "/bean-config/execue-logging-configuration.xml"});
   }

   public static void baseTeardown () {
   }

   public ISystem getExecueSystem () {
      return (ISystem) presentationContext.getBean("execueSystem");
   }
   public PresentationTransformServiceFactory getPresentationTransformServiceFactory () {
      return (PresentationTransformServiceFactory) presentationContext.getBean("presentationTransformServiceFactory");
   }
}