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


package com.execue.ac;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetInvocationService;
import com.execue.ac.algorithm.optimaldset.service.IOptimalDSetWrapperService;
import com.execue.ac.bean.MartCreationPopulatedContext;
import com.execue.ac.service.IAnswersCatalogContextBuilderService;
import com.execue.ac.service.IAnswersCatalogDataAccessService;
import com.execue.ac.service.ICubeCreationService;
import com.execue.ac.service.ICubeUpdationService;
import com.execue.ac.service.IDataMartCreationService;
import com.execue.ac.service.IMartBatchCountPopulationService;
import com.execue.ac.service.IMartContextPopulationService;
import com.execue.ac.service.IMartTotalSlicesCalculatorService;
import com.execue.ac.service.IRandomNumberGeneratorService;
import com.execue.core.system.ExeCueSystemConfigurationUtility;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.ISDXRetrievalService;

/**
 * This is the base class for Junit tests related to answers catalog module
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public abstract class AnswersCatalogBaseTest {

   private static ApplicationContext answersCatalogContext;

   public static void answersCatalogBaseSetUp () {

      answersCatalogContext = new ClassPathXmlApplicationContext(new String[] {
               "/platform/bean-config/execue-logging-configuration.xml",
               "/platform/bean-config/execue-configuration.xml", "/ext/bean-config/execue-configuration-ext.xml",
               "spring-hibernate.xml", "spring-hibernate-qdata.xml", "spring-hibernate-sdata.xml",
               "platform/bean-config/execue-core.xml", "platform/bean-config/execue-swi.xml",
               "ext/bean-config/execue-swi-ext.xml", "platform/bean-config/execue-answers-catalog.xml",
               "/platform/bean-config/execue-dataaccess.xml", "platform/bean-config/execue-unstructured-wh.xml",
               "platform/bean-config/execue-security.xml",
               "platform/bean-config/execue-unstructured-wh-dataaccess.xml", "platform/bean-config/execue-qdata.xml",
               "platform/bean-config/execue-dataaccess-services.xml", "platform/bean-config/execue-util-services.xml",
               "platform/bean-config/execue-platform-services.xml", "platform/bean-config/execue-query-generation.xml",
               "platform/bean-config/execue-util.xml", "platform/bean-config/execue-qdata-dataaccess.xml",
               "platform/bean-config/execue-sdata.xml", "platform/bean-config/execue-sdata-dataaccess.xml",
               "platform/bean-config/execue-query-builder.xml",
               "platform/bean-config/execue-unstructured-ca-dataaccess.xml",
               "platform/bean-config/execue-unstructured-ca.xml", "platform/bean-config/execue-ac-query-history.xml",
               "platform/bean-config/execue-audit-trail.xml", "platform/bean-config/execue-audit-trail-dataaccess.xml",
               "spring-hibernate-audittrail.xml" });
      initialize();

   }

   public static void answersCatalogBaseTearDown () {

   }

   public static void initialize () {
      try {
         ExeCueSystemConfigurationUtility.loadConfigurationServices(answersCatalogContext);
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public IOptimalDSetInvocationService getOptimalDsetInvocationService () {
      return (IOptimalDSetInvocationService) answersCatalogContext.getBean("optimalDSetInvocationService");
   }

   public ICubeCreationService getCubeCreationService () {
      return (ICubeCreationService) answersCatalogContext.getBean("cubeCreationService");
   }

   public static ApplicationContext getAnswersCatalogContext () {
      return answersCatalogContext;
   }

   public static void setAnswersCatalogContext (ApplicationContext answersCatalogContext) {
      AnswersCatalogBaseTest.answersCatalogContext = answersCatalogContext;
   }

   public ICubeUpdationService getCubeUpdationService () {
      return (ICubeUpdationService) answersCatalogContext.getBean("cubeUpdationService");
   }

   public IMartContextPopulationService getMartContextPopulationService () {
      return (IMartContextPopulationService) answersCatalogContext.getBean("martContextPopulationService");
   }

   public IMartBatchCountPopulationService getMartBatchCountPopulationService () {
      return (IMartBatchCountPopulationService) answersCatalogContext.getBean("martBatchCountPopulationService");
   }

   public IMartTotalSlicesCalculatorService getMartTotalSlicesCalculatorService () {
      return (IMartTotalSlicesCalculatorService) answersCatalogContext.getBean("martTotalSlicesCalculatorService");
   }

   public IRandomNumberGeneratorService getRandomNumberGeneratorService () {
      return (IRandomNumberGeneratorService) answersCatalogContext.getBean("randomNumberGeneratorService");
   }

   public IAnswersCatalogDataAccessService getMartDataAccessService () {
      return (IAnswersCatalogDataAccessService) answersCatalogContext.getBean("martDataAccessService");
   }

   public void printMartCreationPopulatedContext (MartCreationPopulatedContext populateMartContext) {
      System.out.println(populateMartContext.getSourceAssetMappingInfo().getPopulatedDistributions().get(0).getColumn()
               .getDisplayName());
   }

   public IDataMartCreationService getDataMartCreationService () {
      return (IDataMartCreationService) answersCatalogContext.getBean("dataMartCreationService");
   }

   public ISDXRetrievalService getSdxRetrievalService () {
      return (ISDXRetrievalService) answersCatalogContext.getBean("sdxRetrievalService");
   }

   public IKDXRetrievalService getKDXRetrievalService () {
      return (IKDXRetrievalService) answersCatalogContext.getBean("kdxRetrievalService");
   }

   public IOptimalDSetInvocationService getOptimalDSetInvocationService () {
      return (IOptimalDSetInvocationService) answersCatalogContext.getBean("optimalDSetInvocationService");
   }

   public IOptimalDSetWrapperService getOptimalDSetWrapperService () {
      return (IOptimalDSetWrapperService) answersCatalogContext.getBean("optimalDSetWrapperService");
   }

   public IAnswersCatalogContextBuilderService getAnswersCatalogContextBuilderService () {
      return (IAnswersCatalogContextBuilderService) answersCatalogContext
               .getBean("answersCatalogContextBuilderService");
   }
}