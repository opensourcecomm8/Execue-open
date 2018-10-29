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


package com.execue.querybuilder.service.qdata;

import org.apache.log4j.Logger;

import com.execue.querybuilder.service.QueryBuilderServiceFactory;

/**
 * Factory class for query builder services
 * 
 * @author Vishay
 * @version 1.0
 * @since 03/01/12
 */

public class QDataQueryBuilderServiceFactory extends QueryBuilderServiceFactory {

   private static Logger                        logger = Logger.getLogger(QDataQueryBuilderServiceFactory.class);

   private static QDataQueryBuilderServiceFactory _factory;

   private Integer                              providerTypeValue;

   public IQDataQueryBuilderService getQueryBuilderService () {
      // TODO Changed logic to get bean name
      try {
         String providerType = String.valueOf(getProviderTypeValue());
         if (!getBeanMapping().containsKey(providerType)) {
            providerType = "0";// set to default
         }
         String beanName = getBeanMapping().get(providerType);
         return (IQDataQueryBuilderService) getBeanFactory().getBean(beanName);
      } catch (Exception e) {
         logger.error(e, e);
         return null;
      }
   }

   public static QDataQueryBuilderServiceFactory getInstance () {
      if (_factory == null) {
         _factory = new QDataQueryBuilderServiceFactory();
      }
      return _factory;
   }

   public Integer getProviderTypeValue () {
      return providerTypeValue;
   }

   public void setProviderTypeValue (Integer providerTypeValue) {
      this.providerTypeValue = providerTypeValue;
   }

}
