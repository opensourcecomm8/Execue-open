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


package com.execue.querygen.service;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.type.AssetProviderType;

/**
 * ServiceFactory
 * 
 * @author kaliki
 * @since 4.0
 */

public class QueryGenerationServiceFactory implements BeanFactoryAware {

   private static Logger                        logger = Logger.getLogger(QueryGenerationServiceFactory.class);
   private static QueryGenerationServiceFactory _factory;
   private Map<String, String>                  beanMapping;
   private BeanFactory                          beanFactory;

   public IQueryGenerationService getQueryGenerationService (Asset asset) {
      // TODO Changed logic to get bean name
      try {
         String providerType = asset.getDataSource().getProviderType().getValue().toString();
         if (!beanMapping.containsKey(providerType)) {
            providerType = "0";// set to default
         }
         String beanName = beanMapping.get(providerType);
         return (IQueryGenerationService) beanFactory.getBean(beanName);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public IQueryGenerationService getQueryGenerationService (AssetProviderType assetProviderType) {
      // TODO Changed logic to get bean name
      try {
         String providerType = assetProviderType.getValue().toString();
         if (!beanMapping.containsKey(providerType)) {
            providerType = "0";// set to default
         }
         String beanName = beanMapping.get(providerType);
         return (IQueryGenerationService) beanFactory.getBean(beanName);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public static QueryGenerationServiceFactory getInstance () {
      if (_factory == null) {
         _factory = new QueryGenerationServiceFactory();
      }
      return _factory;
   }

   public Map<String, String> getBeanMapping () {
      return beanMapping;
   }

   public void setBeanMapping (Map<String, String> beanMapping) {
      this.beanMapping = beanMapping;
   }

   public void setBeanFactory (BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
   }

}
