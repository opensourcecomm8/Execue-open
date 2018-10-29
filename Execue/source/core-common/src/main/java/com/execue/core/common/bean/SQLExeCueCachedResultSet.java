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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.util.ExecueStringUtil;

/**
 * @author Vishy dasari
 */
public class SQLExeCueCachedResultSet implements ExeCueResultSet {

   private static final Logger logger    = Logger.getLogger(SQLExeCueCachedResultSet.class);
   private int                 cursor    = -1;
   private int                 max_count = 0;
   private HashMap[]           rows      = null;
   ExeCueResultSetMetaData     exeCueResultSetMetaData;
   private Long                queryExecutionTime;

   public SQLExeCueCachedResultSet (int maxRecords) {
      max_count = maxRecords;
      rows = new HashMap[maxRecords];
   }

   public ExeCueResultSetMetaData getMetaData () {
      return exeCueResultSetMetaData;
   }

   public void last () throws Exception {
      cursor = max_count;
   }

   public boolean next () throws Exception {
      cursor++;
      if (cursor < max_count) {
         return true;
      }
      return false;
   }

   public boolean previous () throws Exception {
      if (cursor >= 0) {
         cursor--;
         return true;
      }
      return false;
   }

   public int getRow () throws Exception {
      return cursor;
   }

   public void beforeFirst () throws Exception {
      cursor = -1;
   }

   /**
    * - John Removed the dependency on the column name case, i.e, works for any case
    */
   public String getString (String columnAlias) throws Exception {
      if (cursor >= 0) {
         String value = null;
         for (Object key : rows[cursor].keySet()) {
            if (String.valueOf(key).equalsIgnoreCase(columnAlias)) {
               Object objColumnValue = rows[cursor].get(key);
               value = String.valueOf(objColumnValue);
               break;
            }
         }
         return value;
      }
      throw new Exception("Illegal index operation");
   }

   public void addString (String columnAlias, String value) throws Exception {
      if (cursor >= 0) {
         if (rows[cursor] == null) {
            rows[cursor] = new HashMap();
         }
         rows[cursor].put(columnAlias, value);
      } else {
         throw new Exception("Illegal index operation");
      }

   }

   public String getString (int index) throws Exception {
      return getString(exeCueResultSetMetaData.getColumnLabel(index));
   }

   // This method is only for special tranpose logic
   public List getValues (String columnAlias1, String column1Value, String columnAlias2) {
      List list = new ArrayList();
      int row_count = 0;
      while (row_count < max_count) {
         Map map = rows[row_count];
         String value = (String) map.get(columnAlias1);
         if (value.equalsIgnoreCase(column1Value)) {
            list.add(map.get(columnAlias2));
         }
         row_count++;
      }
      return list;
   }

   public Double getDouble (String columnAlias) throws Exception {
      if (cursor >= 0) {
         String value = null;
         for (Object key : rows[cursor].keySet()) {
            if (String.valueOf(key).equalsIgnoreCase(columnAlias)) {
               Object objColumnValue = rows[cursor].get(key);
               value = String.valueOf(objColumnValue);
               break;
            }
         }
         if (ExecueStringUtil.isStringDataBaseNull(value)) {
            value = "0";
         }
         return new Double(value);
      }
      throw new Exception("Illegal index operation");
   }

   public void close () throws Exception {
      // _resultSet.close();
   }

   public Integer getInt (String columnAlias) throws Exception {
      if (cursor >= 0) {
         String value = null;
         for (Object key : rows[cursor].keySet()) {
            if (String.valueOf(key).equalsIgnoreCase(columnAlias)) {
               Object objColumnValue = rows[cursor].get(key);
               value = String.valueOf(objColumnValue);
               break;
            }
         }
         if (ExecueStringUtil.isStringDataBaseNull(value)) {
            value = "0";
         }
         return new Integer(value);
      }
      throw new Exception("Illegal index operation");
   }

   public Integer getInt (int columnIndex) throws Exception {
      return getInt(exeCueResultSetMetaData.getColumnLabel(columnIndex));
   }

   public Long getLong (int columnIndex) throws Exception {
      return getLong(exeCueResultSetMetaData.getColumnLabel(columnIndex));
   }

   public Long getLong (String columnAlias) throws Exception {
      if (cursor >= 0) {
         String value = null;
         for (Object key : rows[cursor].keySet()) {
            if (String.valueOf(key).equalsIgnoreCase(columnAlias)) {
               Object objColumnValue = rows[cursor].get(key);
               value = String.valueOf(objColumnValue);
               break;
            }
         }
         if (value != null) {
            return new Long(value);
         }
      }
      throw new Exception("Illegal index operation");
   }

   public Date getDate (String columnAlias) throws Exception {
      if (cursor >= 0) {
         Date dt = null;
         for (Object key : rows[cursor].keySet()) {
            if (String.valueOf(key).equalsIgnoreCase(columnAlias)) {
               Object objColumnValue = rows[cursor].get(key);
               if (objColumnValue != null) {
                  dt = (Date) objColumnValue;
               }
               break;
            }
         }
         return dt;
      }
      throw new Exception("Illegal index operation");
   }

   public BigDecimal getBigDecimal (String columnAlias) throws Exception {
      if (cursor >= 0) {
         String value = null;
         for (Object key : rows[cursor].keySet()) {
            if (String.valueOf(key).equalsIgnoreCase(columnAlias)) {
               Object objColumnValue = rows[cursor].get(key);
               value = String.valueOf(objColumnValue);
               break;
            }
         }
         if (value != null) {
            return new BigDecimal(value);
         }
      }
      throw new Exception("Illegal index operation");
   }

   public BigDecimal getBigDecimal (int columnIndex) throws Exception {
      return getBigDecimal(exeCueResultSetMetaData.getColumnLabel(columnIndex));
   }

   public Object getObject (String columnLabel) throws Exception {
      if (cursor >= 0) {
         Object value = null;
         for (Object key : rows[cursor].keySet()) {
            if (String.valueOf(key).equalsIgnoreCase(columnLabel)) {
               value = rows[cursor].get(key);
               break;
            }
         }
         return value;
      }
      throw new Exception("Illegal index operation");
   }

   public Object getObject (int columnIndex) throws Exception {
      return getObject(exeCueResultSetMetaData.getColumnLabel(columnIndex));
   }

   /**
    * 
    */
   public void printCurrentRow () {
      // logger.info("CachedResultSet:: row number=" + cursor);
      // logger.info("CachedResultSet:: columnKeys= " +
      // rows[cursor].keySet());
      logger.debug("CachedResultSet:: row number=" + cursor);
      logger.debug("CachedResultSet:: columnKeys= " + rows[cursor].keySet());
   }

   public void setMaxCount (int numberOfRecords) {
      max_count = numberOfRecords;
   }

   public HashMap[] getRows () {
      return rows;
   }

   public void setRows (HashMap[] rows) {
      this.rows = rows;
   }

   public ExeCueResultSetMetaData getExeCueResultSetMetaData () {
      return exeCueResultSetMetaData;
   }

   public void setExeCueResultSetMetaData (ExeCueResultSetMetaData exeCueResultSetMetaData) {
      this.exeCueResultSetMetaData = exeCueResultSetMetaData;
   }

   public Double getDouble (int columnIndex) throws Exception {
      return getDouble(exeCueResultSetMetaData.getColumnLabel(columnIndex));
   }

   public Date getDate (int columnIndex) throws Exception {
      return getDate(exeCueResultSetMetaData.getColumnLabel(columnIndex));
   }

   public Long getQueryExecutionTime () {
      return queryExecutionTime;
   }

   public void setQueryExecutionTime (Long queryExecutionTime) {
      this.queryExecutionTime = queryExecutionTime;
   }
}
