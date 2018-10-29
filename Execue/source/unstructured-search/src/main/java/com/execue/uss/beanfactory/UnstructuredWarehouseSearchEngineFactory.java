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


package com.execue.uss.beanfactory;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.execue.uss.service.IUnstructuredWarehouseSearchEngine;

/**
 * Factory class to return the exact warehouse search engine implementation based on the application name
 * 
 * @author Nitesh
 * @version 1.0
 * @since 25/04/11
 */
public class UnstructuredWarehouseSearchEngineFactory implements BeanFactoryAware {

   private static UnstructuredWarehouseSearchEngineFactory _factory;
   private Map<String, String>                             beanMapping;
   private BeanFactory                                     beanFactory;

   public IUnstructuredWarehouseSearchEngine getUnstructuredWarehouseSearchEngine (String applicationName) {
      IUnstructuredWarehouseSearchEngine unstructuredWarehouseSearchEngine = null;
      try {
         String beanName = beanMapping.get(applicationName);
         if (StringUtils.isEmpty(beanName)) {
            beanName = beanMapping.get("Default");
         }
         unstructuredWarehouseSearchEngine = (IUnstructuredWarehouseSearchEngine) beanFactory.getBean(beanName);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return unstructuredWarehouseSearchEngine;
   }

   public static UnstructuredWarehouseSearchEngineFactory getInstance () {
      if (_factory == null) {
         _factory = new UnstructuredWarehouseSearchEngineFactory();
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
