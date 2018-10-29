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


package com.execue.core.common.bean;

import java.util.Date;

/**
 * @author Vishy Dasari
 */
public interface ExeCueResultSet {

   public ExeCueResultSetMetaData getMetaData ();

   public void last () throws Exception;

   public boolean next () throws Exception;

   public boolean previous () throws Exception;

   public int getRow () throws Exception;

   public void beforeFirst () throws Exception;

   public String getString (String name) throws Exception;

   public String getString (int index) throws Exception;

   public Double getDouble (String columnName) throws Exception;

   public Double getDouble (int columnIndex) throws Exception;

   public Integer getInt (String columnName) throws Exception;

   public Integer getInt (int columnIndex) throws Exception;

   public Long getLong (int columnIndex) throws Exception;

   public Long getLong (String columnName) throws Exception;

   public Date getDate (int columnIndex) throws Exception;

   public Date getDate (String columnName) throws Exception;

   public void close () throws Exception;

   /**
    * This method extracts the time taken to execute a query
    */
   public Long getQueryExecutionTime ();

   public void setQueryExecutionTime (Long queryTime);
}
