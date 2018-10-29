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


package com.execue.qi.service.impl;

import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.QueryForm;
import com.execue.qi.exception.QIException;
import com.execue.qi.processor.DefaultsProcessor;
import com.execue.qi.processor.IBusinessQueryGenerator;
import com.execue.qi.service.IQueryInterfaceService;
import com.execue.security.UserContextService;

/**
 * @author kaliki
 * @since 4.0
 */
public class QueryInterfaceServiceImpl extends UserContextService implements IQueryInterfaceService {

   private DefaultsProcessor       defaultsProcessor;
   private IBusinessQueryGenerator businessQueryGenerator;

   public void setDefaultsProcessor (DefaultsProcessor defaultsProcessor) {
      this.defaultsProcessor = defaultsProcessor;
   }

   public IBusinessQueryGenerator getBusinessQueryGenerator () {
      return businessQueryGenerator;
   }

   public void setBusinessQueryGenerator (IBusinessQueryGenerator businessQueryGenerator) {
      this.businessQueryGenerator = businessQueryGenerator;
   }

   @SuppressWarnings ("unused")
   public BusinessQuery generateBusinessQuery (QueryForm queryForm) throws QIException {
      BusinessQuery bQuery = businessQueryGenerator.generate(queryForm);
      defaultsProcessor.assingDefaults(bQuery);
      return bQuery;
   }

}
