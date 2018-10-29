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


package com.execue.sforce.convertable.impl;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.DataType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExeCueUtils;
import com.execue.core.util.ExecueDateTimeUtils;
import com.execue.sforce.bean.SObjectColumn;
import com.execue.sforce.bean.SObjectNormalizedData;
import com.execue.sforce.bean.SObjectNormalizedMeta;
import com.execue.sforce.bean.SObjectTable;
import com.execue.sforce.bean.type.SoapColumnDataType;
import com.execue.sforce.convertable.ISforceConvertableService;
import com.execue.sforce.exception.SforceException;
import com.execue.sforce.exception.SforceExceptionCodes;
import com.execue.sforce.helper.SforceUtilityHelper;

/**
 * This class populates the normalized sObjects
 * 
 * @author Vishay
 * @version 1.0
 * @since 18/08/09
 */
public class SforceConvertableServiceImpl implements ISforceConvertableService {

   public SObjectNormalizedData populateSObjectNormalizedData (SObjectTable sObjectTable,
            List<QueryColumn> queryColumns, List<List<String>> soapResponseData) throws SforceException {
      try {
         SObjectNormalizedData sObjectNormalizedData = new SObjectNormalizedData();
         sObjectNormalizedData.setSObjectTable(sObjectTable);
         sObjectNormalizedData.setNormalizedQueryColumns(queryColumns);
         sObjectNormalizedData.setSObjectDataPoints(soapResponseData);
         List<List<Object>> normalizedDataPoints = new ArrayList<List<Object>>();
         for (List<String> row : soapResponseData) {
            List<Object> rowData = new ArrayList<Object>();
            int columnCounter = 0;
            for (String column : row) {
               Object columnData = column;
               if (columnData != null) {
                  if (DataType.TIME.equals(queryColumns.get(columnCounter).getDataType())) {
                     columnData = ExecueDateTimeUtils.getSQLTimeFromString(columnData.toString());
                  } else if (DataType.DATE.equals(queryColumns.get(columnCounter).getDataType())) {
                     columnData = ExecueDateTimeUtils.getSQLDateFromStringDate(columnData.toString());
                  } else if (DataType.DATETIME.equals(queryColumns.get(columnCounter).getDataType())) {
                     columnData = ExecueDateTimeUtils.getSQLTimeStampFromString(columnData.toString());
                  } else if (DataType.INT.equals(queryColumns.get(columnCounter).getDataType())) {
                     // if the boolean data is true false we need to convert it back to 0 and 1
                     columnData = ExeCueUtils.getCorrespondingBooleanValueAsInteger(columnData.toString());
                  }
               }
               rowData.add(columnData);
               columnCounter++;
            }
            normalizedDataPoints.add(rowData);
         }
         sObjectNormalizedData.setNormalizedDataPoints(normalizedDataPoints);
         return sObjectNormalizedData;
      } catch (ExeCueException exeCueException) {
         throw new SforceException(SforceExceptionCodes.SFORCE_NORMALIZATION_DATA_EXCEPTION_CODE, exeCueException);
      }
   }

   public SObjectNormalizedMeta populateSObjectNormalizedMeta (SObjectTable sObjectTable,
            List<SObjectColumn> sObjectColumns) throws SforceException {
      SObjectNormalizedMeta sObjectNormalizedMeta = new SObjectNormalizedMeta();
      sObjectNormalizedMeta.setSObjectTable(sObjectTable);
      sObjectNormalizedMeta.setSobjectColumns(sObjectColumns);
      List<QueryColumn> normalizedQueryColumns = new ArrayList<QueryColumn>();
      for (SObjectColumn sObjectColumn : sObjectColumns) {
         QueryColumn queryColumn = new QueryColumn();
         queryColumn.setColumnName(sObjectColumn.getColumnName());
         // calculate the data type and compute the precision and scale
         SoapColumnDataType soapColumnType = SoapColumnDataType.getSoapColumnType(sObjectColumn.getSoapType());
         queryColumn.setDataType(SforceUtilityHelper.getNormalizedDataType(soapColumnType));
         String precision = "0";
         String scale = "0";
         switch (soapColumnType) {
            case BOOLEAN:
               precision = "1";
               break;
            case DATE:
               break;
            case DATETIME:
               break;
            case TIME:
               break;
            case DOUBLE:
               precision = sObjectColumn.getPrecision();
               scale = sObjectColumn.getScale();
               break;
            case ID:
               precision = sObjectColumn.getLength();
               break;
            case INT:
               precision = sObjectColumn.getDigits();
               break;
            case STRING:
               precision = sObjectColumn.getLength();
               break;
            case BINARY:
               precision = "32000";
               break;
         }
         queryColumn.setPrecision(Integer.parseInt(precision));
         queryColumn.setScale(Integer.parseInt(scale));
         queryColumn.setNullable(ExeCueUtils.getCorrespondingBooleanValueAsBoolean(sObjectColumn.getNullable()));
         queryColumn.setDistinct(ExeCueUtils.getCorrespondingBooleanValueAsBoolean(sObjectColumn.getUnique()));
         normalizedQueryColumns.add(queryColumn);
      }
      sObjectNormalizedMeta.setNormalizedQueryColumns(normalizedQueryColumns);
      return sObjectNormalizedMeta;
   }
}
