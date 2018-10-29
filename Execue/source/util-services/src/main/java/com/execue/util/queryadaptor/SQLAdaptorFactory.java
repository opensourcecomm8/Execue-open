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


package com.execue.util.queryadaptor;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * Factory class for sql adaptors
 * 
 * @author Nitesh
 * @version 1.0
 * @since 22/02/12
 */

public class SQLAdaptorFactory implements BeanFactoryAware {

   private static Logger            logger = Logger.getLogger(SQLAdaptorFactory.class);
   private static SQLAdaptorFactory _factory;
   private Map<Integer, String>     beanMapping;
   private BeanFactory              beanFactory;

   public ISQLAdaptor getSQLAdaptor (Integer providerTypeValue) {
      try {
         String beanName = beanMapping.get(providerTypeValue);
         return (ISQLAdaptor) beanFactory.getBean(beanName);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
   }

   public static SQLAdaptorFactory getInstance () {
      if (_factory == null) {
         _factory = new SQLAdaptorFactory();
      }
      return _factory;
   }

   public Map<Integer, String> getBeanMapping () {
      return beanMapping;
   }

   public void setBeanMapping (Map<Integer, String> beanMapping) {
      this.beanMapping = beanMapping;
   }

   public void setBeanFactory (BeanFactory beanFactory) throws BeansException {
      this.beanFactory = beanFactory;
   }
}