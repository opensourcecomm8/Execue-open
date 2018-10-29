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


package com.execue.core.common.util;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.entity.AppDataSource;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetEntityDefinition;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.querygen.QueryColumn;
import com.execue.core.common.type.AssetOwnerType;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.EntityType;
import com.execue.core.common.type.ExecueFacetType;
import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.LocationType;
import com.execue.core.common.type.LookupType;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.core.configuration.ICoreConfigurationService;

public class ExecueBeanUtil {

   public static ColumnType getColumnType (LookupType lookupType) {
      ColumnType columnType = ColumnType.NULL;
      switch (lookupType) {
         case SIMPLE_LOOKUP:
            columnType = ColumnType.SIMPLE_LOOKUP;
            break;
         case RANGE_LOOKUP:
            columnType = ColumnType.RANGE_LOOKUP;
            break;
      }
      return columnType;
   }

   public static boolean isColumnTypeDimension (ColumnType columnType) {
      return columnType == ColumnType.DIMENSION || columnType == ColumnType.DEDUCED_DIMENSION
               || columnType == ColumnType.RANGE_LOOKUP || columnType == ColumnType.SIMPLE_HIERARCHY_LOOKUP
               || columnType == ColumnType.SIMPLE_LOOKUP;
   }

   public static boolean isColumnTypeMeasure (ColumnType columnType) {
      return columnType == ColumnType.MEASURE;
   }

   /**
    * This method is will return the List of AssetEntityDefinitions inside the Mapping object.
    * 
    * @param mappings
    * @return assetEntityDefinitions
    */
   public static List<AssetEntityDefinition> getAssetEntityDefinitions (List<Mapping> mappings) {
      List<AssetEntityDefinition> assetEntityDefinitions = new ArrayList<AssetEntityDefinition>();
      for (Mapping mapping : mappings) {
         assetEntityDefinitions.add(mapping.getAssetEntityDefinition());
      }
      return assetEntityDefinitions;
   }

   /**
    * This method id helper method to get corresponding SQL dataType for QueryColum DataType.
    * 
    * @param DataType
    * @return SQLDataType
    */
   public static Integer getSQLDataType (DataType dataType) {
      Integer sqlType = Types.VARCHAR;
      if (DataType.STRING.equals(dataType)) {
         sqlType = Types.VARCHAR;
      } else if (DataType.INT.equals(dataType)) {
         sqlType = Types.INTEGER;
      } else if (DataType.NUMBER.equals(dataType)) {
         sqlType = Types.DECIMAL;
      } else if (DataType.DATE.equals(dataType)) {
         sqlType = Types.DATE;
      } else if (DataType.TIME.equals(dataType)) {
         sqlType = Types.TIME;
      } else if (DataType.CHARACTER.equals(dataType)) {
         sqlType = Types.CHAR;
      } else if (DataType.DATETIME.equals(dataType)) {
         sqlType = Types.TIMESTAMP;
      } else if (DataType.LARGE_INTEGER.equals(dataType)) {
         sqlType = Types.BIGINT;
      } else if (DataType.TEXT.equals(dataType)) {
         sqlType = Types.VARCHAR;
      }
      return sqlType;
   }

   /**
    * Converts the data type code obtained from the DB MetaData into readable Standard data types Case1: If there are no
    * decimal digits for a number then it is treated as INTEGER Case2:
    * 
    * @param sqlDataType
    * @return dataType
    */
   public static DataType getNormalizedDataType (int sqlDataType) {
      DataType dataType = DataType.STRING;
      switch (sqlDataType) {
         case Types.BIGINT:
            dataType = DataType.LARGE_INTEGER;
            break;
         case Types.BINARY:
            dataType = DataType.STRING;
            break;
         case Types.BLOB:
            dataType = DataType.STRING;
            break;
         case Types.BOOLEAN:
            dataType = DataType.INT;
            break;
         case Types.CHAR:
            dataType = DataType.CHARACTER;
            break;
         case Types.CLOB:
            dataType = DataType.STRING;
            break;
         case Types.DATE:
            dataType = DataType.DATE;
            break;
         case Types.DECIMAL:
            dataType = DataType.NUMBER;
            break;
         case Types.DOUBLE:
            dataType = DataType.NUMBER;
            break;
         case Types.FLOAT:
            dataType = DataType.NUMBER;
            break;
         case Types.INTEGER:
            dataType = DataType.INT;
            break;
         case Types.LONGVARBINARY:
            dataType = DataType.STRING;
            break;
         case Types.LONGVARCHAR:
            dataType = DataType.STRING;
            break;
         case Types.NUMERIC:
            dataType = DataType.NUMBER;
            break;
         case Types.REAL:
            dataType = DataType.NUMBER;
            break;
         case Types.SMALLINT:
            dataType = DataType.INT;
            break;
         case Types.TIME:
            dataType = DataType.TIME;
            break;
         case Types.TIMESTAMP:
            dataType = DataType.DATETIME;
            break;
         case Types.TINYINT:
            dataType = DataType.INT;
            break;
         case Types.BIT:
            dataType = DataType.INT;
            break;
         case Types.VARBINARY:
            dataType = DataType.STRING;
            break;
         case Types.VARCHAR:
            dataType = DataType.STRING;
            break;
      }
      return dataType;
   }

   public static CheckType getCorrespondingCheckTypeValue (boolean bool) {
      CheckType checkType = CheckType.NO;
      if (bool) {
         checkType = CheckType.YES;
      }
      return checkType;
   }

   public static boolean getCorrespondingBooleanValue (String booleanValue) {
      boolean value = false;
      if ("true".equalsIgnoreCase(booleanValue.trim())) {
         value = true;
      }
      return value;
   }

   /**
    * This method will return the default date format based on asset provider type
    * 
    * @param assetProviderType
    * @return defaultDateFormat
    */
   public static String getDefaultDateFormat (AssetProviderType assetProviderType,
            ICoreConfigurationService coreConfigurationService) {
      String defaultDateFormat = null;
      switch (assetProviderType) {
         case Oracle:
            defaultDateFormat = coreConfigurationService.getOracleDefaultDateFormat();
            break;
         case MSSql:
            defaultDateFormat = coreConfigurationService.getMsSQLDefaultDateFormat();
            break;
         case MySql:
            defaultDateFormat = coreConfigurationService.getMySQLDefaultDateFormat();
            break;
         case Teradata:
            defaultDateFormat = coreConfigurationService.getTeradataDefaultDateFormat();
         case SAS_SHARENET:
         case SAS_WORKSPACE:
            // NOTE: Using same default date format for both the sas provider types
            defaultDateFormat = coreConfigurationService.getSASDefaultDateFormat();
      }
      return defaultDateFormat;
   }

   /**
    * This method will return the default date format based on asset provider type
    * 
    * @param assetProviderType
    * @return defaultDateFormat
    */
   public static String getDefaultDateTimeFormat (AssetProviderType assetProviderType,
            ICoreConfigurationService coreConfigurationService) {
      String defaultDateFormat = null;
      switch (assetProviderType) {
         case Oracle:
            defaultDateFormat = coreConfigurationService.getOracleDefaultDateTimeFormat();
            break;
         case MSSql:
            defaultDateFormat = coreConfigurationService.getMsSQLDefaultDateTimeFormat();
            break;
         case MySql:
            defaultDateFormat = coreConfigurationService.getMySQLDefaultDateTimeFormat();
            break;
         case Teradata:
            defaultDateFormat = coreConfigurationService.getTeradataDefaultDateTimeFormat();
            break;
         case SAS_SHARENET:
         case SAS_WORKSPACE:
            // NOTE: Using same default date time format for both the sas provider types
            defaultDateFormat = coreConfigurationService.getSASDefaultDateTimeFormat();
            break;
      }
      return defaultDateFormat;
   }

   public static String getTextMessage (String message, String columnName, String dateFormat) {
      if (message != null) {
         if (dateFormat != null) {
            message = message.replace("@@dateformat@@", dateFormat);
         }
         message = message.replace("@@columnName@@", columnName);
      }
      return message;
   }

   public static List<Integer> getSQLParameterTypes (List<QueryColumn> queryColumns) {
      List<Integer> paramTypes = new ArrayList<Integer>();
      for (QueryColumn queryColumn : queryColumns) {
         paramTypes.add(getSQLDataType(queryColumn.getDataType()));
      }
      return paramTypes;
   }

   public static Membr getMatchingMember (List<Membr> members, String lookupValue) {
      Membr matchedMember = null;
      for (Membr membr : members) {
         if (membr.getLookupValue().equalsIgnoreCase(lookupValue)) {
            matchedMember = membr;
            break;
         }
      }
      return matchedMember;
   }

   public static boolean findCorrespondingBooleanValue (CheckType checkType) {
      boolean correspondingBooleanValue = false;
      if (CheckType.YES.equals(checkType)) {
         correspondingBooleanValue = true;
      }
      return correspondingBooleanValue;
   }

   public static Colum findCorrespondingColumn (List<Colum> columns, String columnName) {
      Colum matchedColumn = null;
      for (Colum colum : columns) {
         if (colum.getName().equalsIgnoreCase(columnName)) {
            matchedColumn = colum;
            break;
         }
      }
      return matchedColumn;
   }

   public static EntityType getCorrespondingEntityType (BusinessEntityType businessEntityType) {
      EntityType entityType = null;

      if (BusinessEntityType.CONCEPT == businessEntityType) {
         entityType = EntityType.CONCEPT;
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE == businessEntityType) {
         entityType = EntityType.CONCEPT_LOOKUP_INSTANCE;
      } else if (BusinessEntityType.TYPE_LOOKUP_INSTANCE == businessEntityType) {
         entityType = EntityType.TYPE_LOOKUP_INSTANCE;
      } else if (BusinessEntityType.RELATION == businessEntityType) {
         entityType = EntityType.RELATION;
      } else if (BusinessEntityType.CONCEPT_PROFILE == businessEntityType) {
         entityType = EntityType.CONCEPT_PROFILE;
      } else if (BusinessEntityType.INSTANCE_PROFILE == businessEntityType) {
         entityType = EntityType.INSTANCE_PROFILE;
      }
      return entityType;
   }

   public static boolean getCorrespondingBooleanValue (SuccessFailureType successFailureType) {
      boolean successFailureBooleanValue = false;
      if (SuccessFailureType.SUCCESS.equals(successFailureType)) {
         successFailureBooleanValue = true;
      }
      return successFailureBooleanValue;
   }

   public static AppDataSource buildAppDataSource (Long appId, Long dataSourceId) {
      AppDataSource appDataSource = new AppDataSource();
      appDataSource.setAppId(appId);
      appDataSource.setDataSourceId(dataSourceId);
      return appDataSource;
   }

   public static ExecueFacetType getCorrespondingExecueFacetType (FeatureValueType featureValueType) {
      ExecueFacetType facetType = null;
      if (FeatureValueType.VALUE_NUMBER.equals(featureValueType)) {
         facetType = ExecueFacetType.NUMBER;
      } else if (FeatureValueType.VALUE_STRING.equals(featureValueType)) {
         facetType = ExecueFacetType.STRING;
      }
      return facetType;
   }

   public static FeatureValueType getCorrespondingFeatureValueType (ExecueFacetType execueFacetType) {
      FeatureValueType featureValueType = null;
      if (ExecueFacetType.NUMBER.equals(execueFacetType)) {
         featureValueType = FeatureValueType.VALUE_NUMBER;
      } else if (ExecueFacetType.STRING.equals(execueFacetType)) {
         featureValueType = FeatureValueType.VALUE_STRING;
      }
      return featureValueType;
   }

   public static NormalizedLocationType getNormalizedLocationType (LocationType locationType) {
      NormalizedLocationType normalizedLocationType = null;
      switch (locationType) {
         case CITY:
         case STATE:
         case COUNTRY:
         case COUNTY:
            normalizedLocationType = NormalizedLocationType.BED_BASED;
            break;
         case LAT_LONG:
            normalizedLocationType = NormalizedLocationType.LAT_LONG;
            break;
         case ZIPCODE:
            normalizedLocationType = NormalizedLocationType.ZIPCODE;
            break;
      }
      return normalizedLocationType;
   }

   public static boolean isNumericDataType (DataType dataType) {
      boolean numericType = false;
      switch (dataType) {
         case INT:
            numericType = true;
            break;
         case NUMBER:
            numericType = true;
            break;
         case LARGE_INTEGER:
            numericType = true;
            break;
         default:
            break;
      }
      return numericType;
   }

   public static boolean isStringDataType (DataType dataType) {
      boolean stringType = false;
      switch (dataType) {
         case CHARACTER:
            stringType = true;
            break;
         case STRING:
            stringType = true;
            break;
         case TEXT:
            stringType = true;
            break;
         default:
            break;
      }
      return stringType;
   }

   public static boolean isDateDataType (DataType dataType) {
      boolean dateType = false;
      switch (dataType) {
         case TIME:
            dateType = true;
            break;
         case DATE:
            dateType = true;
            break;
         case DATETIME:
            dateType = true;
            break;
         default:
            break;
      }
      return dateType;
   }

   public static boolean isExecueOwnedCube (Asset asset) {
      boolean isExecueOwnedCube = false;
      if (AssetType.Cube.equals(asset.getType()) && AssetOwnerType.ExeCue.equals(asset.getOwnerType())) {
         isExecueOwnedCube = true;
      }

      return isExecueOwnedCube;
   }

   public static boolean isExecueOwnedMart (Asset asset) {
      boolean isExecueOwnedMart = false;
      if (AssetType.Mart.equals(asset.getType()) && AssetOwnerType.ExeCue.equals(asset.getOwnerType())) {
         isExecueOwnedMart = true;
      }

      return isExecueOwnedMart;
   }
}
