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


package com.execue.uswhda.dataaccess.dao;

import com.execue.querybuilder.service.uswh.IUnstructuredWHQueryBuilderService;
import com.execue.querybuilder.service.uswh.UnstructuredWHQueryBuilderServiceFactory;
import com.execue.uswhda.dataaccess.dao.impl.UnstructuredWHContextCrudDAOImpl;

public class BaseUnstructuredWHDAO extends UnstructuredWHContextCrudDAOImpl {

   private UnstructuredWHQueryBuilderServiceFactory unstructuredWHQueryBuilderServiceFactory;

   protected IUnstructuredWHQueryBuilderService getQueryBuilderService () {
      return getUnstructuredWHQueryBuilderServiceFactory().getQueryBuilderService();
   }

   public UnstructuredWHQueryBuilderServiceFactory getUnstructuredWHQueryBuilderServiceFactory () {
      return unstructuredWHQueryBuilderServiceFactory;
   }

   public void setUnstructuredWHQueryBuilderServiceFactory (
            UnstructuredWHQueryBuilderServiceFactory unstructuredWHQueryBuilderServiceFactory) {
      this.unstructuredWHQueryBuilderServiceFactory = unstructuredWHQueryBuilderServiceFactory;
   }

}
