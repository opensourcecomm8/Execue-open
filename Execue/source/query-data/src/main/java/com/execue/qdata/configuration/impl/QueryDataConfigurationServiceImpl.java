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


package com.execue.qdata.configuration.impl;

import com.execue.core.configuration.IConfiguration;
import com.execue.qdata.configuration.IQueryDataConfigurationService;

/**
 * This class define the configuration constants and methods  that will be further used by query data services.
 * 
 * @author Jitendra
 * @version 1.0
 * @since 18/10/11
 */
public class QueryDataConfigurationServiceImpl implements IQueryDataConfigurationService {

   private IConfiguration      qdataConfiguration;

   /**
    * This key will retrieve the path of the qdata configuration properties file
    */
   private static final String SQL_GROUP_CONCAT_DEFAULT_DELIMITER_KEY = "qdata.static-values.sql-group-concat-default-delimiter";
   private static final String SQL_CONCAT_DELIMITER_KEY               = "qdata.static-values.sql-concat-delimiter";

   @Override
   public String getSqlGroupConcatDefaultDelimeter () {
      return getQdataConfiguration().getProperty(SQL_GROUP_CONCAT_DEFAULT_DELIMITER_KEY);
   }

   @Override
   public String getSqlConcatDelimeter () {
      return getQdataConfiguration().getProperty(SQL_CONCAT_DELIMITER_KEY);
   }

   /**
    * @return the qdataConfiguration
    */
   public IConfiguration getQdataConfiguration () {
      return qdataConfiguration;
   }

   /**
    * @param qdataConfiguration the qdataConfiguration to set
    */
   public void setQdataConfiguration (IConfiguration qdataConfiguration) {
      this.qdataConfiguration = qdataConfiguration;
   }
}
