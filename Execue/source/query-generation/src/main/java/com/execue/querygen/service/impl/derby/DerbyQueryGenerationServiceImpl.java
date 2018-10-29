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


package com.execue.querygen.service.impl.derby;

import com.execue.core.common.bean.querygen.QueryStructure;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.querygen.service.impl.SQLQueryGenerationServiceImpl;

/**
 * @author Prasanna
 */
public class DerbyQueryGenerationServiceImpl extends SQLQueryGenerationServiceImpl {

   @Override
   protected QueryStructure enhanceQueryStructure (QueryStructure queryStructure) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   protected String prepareColumnForDateToStringHandling (String columnString, String columnDBDateFormat) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   protected String prepareColumnForStringToDateHandling (String columnString, String columnDBDateFormat) {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   protected String prepareDateFormOfValue (QueryValue queryValue, String columnDBDateFormat) {
      // TODO Auto-generated method stub
      return null;
   }

}
