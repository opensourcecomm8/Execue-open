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


package com.execue.platform.swi.conversion.unitscale;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import com.execue.core.common.type.ConversionType;

/**
 * Factory class to return the exact convertor implementation based on conversion type
 * 
 * @author Vishay
 * @version 1.0
 * @since 18/06/09
 */
public class TypeConvertorFactory implements BeanFactoryAware {

   private static TypeConvertorFactory _factory;
   private Map<String, String>         beanMapping;
   private BeanFactory                 beanFactory;

   public ITypeConvertor getTypeConvertor (ConversionType conversionType) {
      ITypeConvertor typeConvertor = null;
      try {
         String beanName = beanMapping.get(conversionType.getValue());
         if (StringUtils.isEmpty(beanName)) {
            beanName = beanMapping.get("DEFAULT");
         }
         typeConvertor = (ITypeConvertor) beanFactory.getBean(beanName);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return typeConvertor;
   }

   public static TypeConvertorFactory getInstance () {
      if (_factory == null) {
         _factory = new TypeConvertorFactory();
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
