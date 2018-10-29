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


package com.execue.core.constants;

/**
 * These are the constants being used by query generation extract query method.
 * 
 * @author Vishay
 * @version 1.0
 */
public interface ISQLQueryConstants {

   public static final String  SQL_SELECT_CLAUSE                      = "SELECT";
   public static final String  SQL_JOIN_FROM_CLAUSE                   = "FROM";
   public static final String  SQL_WHERE_CLAUSE                       = "WHERE";
   public static final String  SQL_ORDER_BY_CLAUSE                    = "ORDER BY";
   public static final String  SQL_ORDER_BY_ASC                       = "ASC";
   public static final String  SQL_ORDER_BY_DESC                      = "DESC";
   public static final String  SQL_GROUP_BY_CLAUSE                    = "GROUP BY";
   public static final String  SQL_HAVING_CLAUSE                      = "HAVING";
   public static final String  SQL_LIMIT_CLAUSE                       = "LIMIT";
   public static final String  SQL_ALIAS                              = "AS";

   public static final String  DROP_TABLE_COMMAND                     = "DROP TABLE";
   public static final String  DELETE_TABLE_COMMAND                   = "DELETE FROM";
   public static final String  TRUNCATE_TABLE_COMMAND                 = "TRUNCATE TABLE";
   public static final String  INSERT_TABLE_COMMAND                   = "INSERT INTO";
   public static final String  CREATE_TABLE_COMMAND                   = "CREATE TABLE";
   public static final String  RENAME_TABLE_COMMAND                   = "RENAME TABLE";
   public static final String  ALTER_TABLE_COMMAND                    = "ALTER TABLE";

   public static final String  SQL_SPACE_DELIMITER                    = " ";
   public static final String  SQL_ALIAS_SEPERATOR                    = ".";
   public static final String  SQL_ENTITY_SEPERATOR                   = ",";
   public static final String  SQL_SUBQUERY_START_WRAPPER             = "(";
   public static final String  SQL_SUBQUERY_END_WRAPPER               = ")";
   public static final String  SQL_FUNCTION_START_WRAPPER             = "(";
   public static final String  SQL_FUNCTION_END_WRAPPER               = ")";
   public static final String  SQL_COLUMN_BINDER                      = "`";
   public static final String  QUOTE                                  = "'";
   public static final String  DOUBLE_QUOTE                           = "\"";
   public static final String  EMPTY_QUOTE                            = "''";
   public static final String  JOIN_CONDITION_OPERATOR                = "=";
   public static final String  GREATER_THAN_OPERATOR                  = ">";
   public static final String  LESSER_THAN_OPERATOR                   = "<";
   public static final String  GREATER_THAN_EQUAL_OPERATOR            = ">=";
   public static final String  LESSER_THAN_EQUAL_OPERATOR             = "<=";
   public static final String  TAB_SPACE                              = "  ";
   public static final String  DIVISION_SYMBOL                        = "/";
   public static final String  SINGLE_QUOTE                           = "\'";
   public static final String  SQL_WILD_CHAR                          = "*";
   public static final String  SQL_WILD_CHAR_EXPRESSION_FOR_DECIMAL   = "%.%";
   public static final String  SQL_DOBULE_QUOTE_ALIAS_SEPERATOR       = "'.'";
   public static final String  SQL_ADDITION_OPERATOR                  = "+";
   public static final String  SQL_MINUS_OPERATOR                     = "-";

   public static final String  JOIN_CONDITION_SEPERATOR               = "ON";
   public static final String  INNER_JOIN                             = "INNER JOIN";
   public static final String  LEFT_OUTER_JOIN                        = "LEFT OUTER JOIN";
   public static final String  RIGHT_OUTER_JOIN                       = "RIGHT OUTER JOIN";

   public static final String  SQL_MAX_FUNCTION                       = "MAX";
   public static final String  SQL_MIN_FUNCTION                       = "MIN";
   public static final String  SQL_LENGTH_FUNCTION                    = "LENGTH";
   public static final String  SQL_INSTR_FUNCTION                     = "INSTR";
   public static final String  SQL_IFNULL_FUNCTION                    = "IFNULL";
   public static final String  SQL_SUBSTR_FUNCTION                    = "SUBSTR";
   public static final String  SQL_RANDOM_FUNCTION                    = "RAND";
   public static final String  SQL_ROUND_FUNCTION                     = "ROUND";
   public static final String  SQL_COUNT_FUNCTION                     = "COUNT";
   public static final String  SQL_BETWEEN_FUNCTION                   = "BETWEEN";

   public static final String  SQL_LIKE_KEYWORD                       = "LIKE";
   public static final String  SQL_NOT_LIKE_KEYWORD                   = "NOT LIKE";
   public static final String  SQL_OVER_KEYWORD                       = "OVER";
   public static final String  SQL_REGEX_KEYWORD                      = "REGEXP";
   public static final String  SQL_SET_KEYWORD                        = "SET";
   public static final String  SQL_TO_KEYWORD                         = "TO";
   public static final String  SQL_UNION_KEYWORD                      = "UNION";
   public static final String  SQL_DISTINCT_KEYWORD                   = "DISTINCT";
   public static final String  CREATE_TABLE_KEYWORD                   = "CREATE";
   public static final String  UPDATE_TABLE_KEYWORD                   = "UPDATE";
   public static final String  INSERT_VALUES_KEYWORD                  = "VALUES";
   public static final String  INDEX_UNIQUE_KEYWORD                   = "UNIQUE";
   public static final String  INDEX_KEYWORD                          = "INDEX";
   public static final String  ADD_COLUMN_KEYWORD                     = "ADD";
   public static final String  CHANGE_COLUMN_KEYWORD                  = "CHANGE";
   public static final String  ADD_CONSTRAINTS_KEYWORD                = "ADD CONSTRAINT";
   public static final String  REFERENCED_CONSTRAINTS_KEYWORD         = "REFERENCES";
   public static final String  NOT_NULL_KEYWORD                       = "NOT NULL";
   public static final String  IS_NOT_NULL_KEYWORD                    = "IS NOT NULL";
   public static final String  SQL_TRUNC_FUNCTION                     = "trunc";
   public static final String  WHERE_CLAUSE_SEPERATOR                 = "AND";
   public static final String  WHERE_CLAUSE_OR_SEPERATOR              = "OR";

   // MySql
   public static final String  MYSQL_SHOW_COMMAND                     = "show";
   public static final String  MYSQL_SYSTEM_INDEX_NAME                = "Index";
   public static final String  MYSQL_SYSTEM_INDEX_COLUMN_NAME         = "key_name";
   public static final String  MYSQL_LIMIT_INNER_QUERY_ALIAS_NAME     = "myiq";
   public static final String  MYSQL_ROLLUP_QUERY_ALIAS_NAME          = "myrq";
   public static final String  MYSQL_NULL_CHECK_KEYWORD               = "IFNULL";
   public static final String  MYSQL_LIMIT_CLAUSE                     = "LIMIT";
   public static final String  SQL_MYSQL_STRING_TO_DATE_FUNCTION      = "STR_TO_DATE";
   public static final String  SQL_MYSQL_DATE_FORMAT_FUNCTION         = "DATE_FORMAT";
   public static final String  SQL_MYSQL_ENGINE_DEFAULT               = "Engine=MyISAM";
   public static final String  SQL_MYSQL_FLOOR_FUNCTION               = "floor";
   public static final String  SQL_MYSQL_RAND_FUNCTION                = "rand";
   public static final String  SQL_MYSQL_AUTO_INCREMENT_VALUE         = "Auto_Increment primary key";
   public static final String  SQL_MYSQL_ROLLUP_KEYWORD               = "WITH ROLLUP";

   // MSSql
   public static final String  MSSQL_NULL_CHECK_KEYWORD               = "ISNULL";
   public static final String  MSSQL_LIMIT_CLAUSE                     = "TOP";
   public static final String  MSSQL_ROW_NUMBER_KEYWORD_ALIAS_NAME    = "RNUM";
   public static final String  MSSQL_ROW_NUMBER_FUNCTION              = "ROW_NUMBER";
   public static final String  MSSQL_AUTO_INCREMENT_VALUE             = "IDENTITY(1,1) PRIMARY KEY";
   public static final String  MSSQL_SYSTEM_INDEX_NAME                = "SYSINDEXES";
   public static final String  MSSQL_SYSTEM_INDEX_COLUMN_NAME         = "NAME";
   public static final String  MSSQL_LIMIT_INNER_QUERY_ALIAS_NAME     = "msiq";
   public static final String  MSSQL_SQL_STDDEV                       = "STDEVP";
   public static final String  MSSQL_SQL_LENGTH_FUNCTION              = "LEN";
   public static final String  SQL_MSSQL_CONVERT_FUNCTION             = "CONVERT";
   public static final String  SQL_MSSQL_RAND_FUNCTION                = "RAND";
   public static final String  SQL_MSSQL_CHECKSUM_FUNCTION            = "CHECKSUM";
   public static final String  SQL_MSSQL_NEWID_FUNCTION               = "NEWID";
   public static final String  SQL_MSSQL_ROLLUP_KEYWORD               = "WITH ROLLUP";
   public static final String  SQL_MSSQL_GROUPING_FUNCTION            = "GROUPING";

   // Oracle
   public static final String  ORACLE_STRING_TO_DATE_FUNCTION         = "TO_DATE";
   public static final String  ORACLE_DATE_TO_STRING_FUNCTION         = "TO_CHAR";
   public static final String  ORACLE_NULL_CHECK_KEYWORD              = "NVL";
   public static final String  ORACLE_ROWNUM_KEYWORD_ALIAS_NAME       = "RNUM";
   public static final String  ORACLE_LIMIT_INNER_QUERY_ALIAS_NAME    = "liq";
   public static final String  ORACLE_USER_INDEX_NAME                 = "user_indexes";
   public static final String  ORACLE_USER_INDEX_COLUMN_NAME          = "index_name";
   public static final String  ORACLE_WHERE_EXISTS_CLAUSE             = "WHERE EXISTS";
   public static final String  ORACLE_LIMIT_CLAUSE                    = "ROWNUM";
   public static final String  ORACLE_RANDOM_NUMBER_FUNCTION          = "dbms_random.value";
   public static final String  SQL_ORACLE_ROLLUP_FUNCTION             = "ROLLUP";
   public static final String  SQL_ORACLE_GROUPING_FUNCTION           = "GROUPING";

   // Teradata
   public static final String  TERADATA_NULL_CHECK_KEYWORD            = "COALESCE";
   public static final String  TERADATA_QUALIFY_KEYWORD               = "QUALIFY";
   public static final String  TERADATA_LIMIT_INNER_QUERY_ALIAS_NAME  = "tiq";
   public static final String  TERADATA_ROW_NUMBER_FUNCTION           = "ROW_NUMBER";
   public static final String  SQL_TERADATA_RAND_FUNCTION             = "RANDOM";
   public static final String  SQL_TERADATA_LENGTH_FUNCTION           = "CHARACTER_LENGTH";
   public static final String  SQL_TERADATA_CAST_FUNCTION             = "CAST";
   public static final String  SQL_TERADATA_ROLLUP_FUNCTION           = "ROLLUP";
   public static final String  SQL_TERADATA_GROUPING_FUNCTION         = "GROUPING";

   // SAS
   public static final String  SAS_STRING_TO_DATE_FUNCTION            = "INPUT";
   public static final String  SAS_LIMIT_INNER_QUERY_ALIAS_NAME       = "sasiq";
   public static final String  SAS_NULL_CHECK_KEYWORD                 = "COALESCE";                          // numeric
   public static final String  SAS_ROLLUP_QUERY_ALIAS_NAME            = "sasrq";

   // DB2
   public static final String  DB2_LIMIT_CLAUSE                       = "LIMIT";
   public static final String  DB2_NULL_CHECK_KEYWORD                 = "COALESCE";
   public static final String  SQL_DB2_RAND_FUNCTION                  = "RAND";
   public static final String  DB2_INT_KEYWORD                        = "INT";
   public static final String  DB2_SQL_STDDEV                         = "STDDEV";
   public static final String  DB2_SQL_LENGTH_FUNCTION                = "LENGTH";
   public static final String  DB2_AUTO_INCREMENT_VALUE               = "GENERATED ALWAYS AS IDENTITY";
   public static final String  DB2_SYSTEM_CATLOG_NAME                 = "SYSCAT.INDEXES";
   public static final String  DB2_SYSTEM_CATALOG_INDEX_NAME          = "INDNAME";
   public static final String  DB2_SYSTEM_CATALOG_SCHEMA_NAME         = "TABSCHEMA";
   public static final String  DB2_SYSTEM_CATALOG                     = "SYS%";
   public static final String  DB2_MERGE_KEYWORD                      = "MERGE INTO";
   public static final String  DB2_USING_KEYWORD                      = "USING";
   public static final String  DB2_WHEN_MATCHED_KEYWORD               = "WHEN MATCHED";
   public static final String  DB2_THEN_KEYWORD                       = "THEN";
   public static final String  DB2_UPDATE_SELECT_QUERY_ALIAS_NAME     = "DBUSQ";
   public static final String  DB2_LIMIT_INNER_QUERY_ALIAS_NAME       = "DBIQ";
   public static final String  SQL_DB2_CAST_FUNCTION                  = "CAST";
   public static final Integer SQL_DB2_MAX_DECIMAL_PRECISION          = 30;
   public static final Integer SQL_DB2_MAX_DECIMAL_SCALE              = 6;
   public static final Integer SQL_DB2_MAX_STRING_PRECISION           = 25;
   public static final String  SQL_DB2_ROLLUP_FUNCTION                = "ROLLUP";
   public static final String  SQL_DB2_GROUPING_FUNCTION              = "GROUPING";

   // Postgres
   public static final String  POSTGRES_LIMIT_CLAUSE                  = "LIMIT";
   public static final String  POSTGRES_NULL_CHECK_KEYWORD            = "ISNULL";
   public static final String  SQL_POSTGRES_RAND_FUNCTION             = "RANDOM";
   public static final String  SQL_POSTGRES_TRUNC_FUNCTION            = "TRUNC";
   public static final String  POSTGRES_OFFSET_CLAUSE                 = "OFFSET";
   public static final String  POSTGRES_SQL_STDDEV                    = "STDDEV_POP";
   public static final String  POSTGRES_AUTO_INCREMENT_VALUE          = "SERIAL";
   public static final String  POSTGRES_STRING_TO_DATE_FUNCTION       = "TO_DATE";
   public static final String  POSTGRES_DATE_TO_STRING_FUNCTION       = "TO_CHAR";
   public static final String  POSTGRES_ROLLUP_QUERY_ALIAS_NAME       = "POSTRQ";
   public static final String  POSTGRES_REGEX_KEYWORD                 = "~";
   public static final String  POSTGRES_IF_NULL_KEYWORD               = "COALESCE";
   public static final String  POSTGRES_PG_CLASS_TABLE                = "pg_class";
   public static final String  POSTGRES_REL_NAME                      = "relname";
   public static final String  POSTGRES_TO_NUMBER_FUNCTION            = "TO_NUMBER";
   public static final String  POSTGRES_TO_TIMESTAMP_FUNCTION         = "TO_TIMESTAMP";

   // argument
   public static final String  SAS_NULL_CHECK_KEYWORD_FOR_CHAR        = "COALESCEC";                         // character
   // argument
   public static final String  SAS_LIMIT_CLAUSE_MONOTONIC             = "MONOTONIC";
   public static final String  SAS_LIMIT_CLAUSE_FIRST_OBS             = "FIRSTOBS";
   public static final String  SAS_LIMIT_CLAUSE_OBS                   = "OBS";
   public static final String  SQL_SAS_CAT_FUNCTION                   = "CAT";
   public static final String  SQL_SAS_RAND_FUNCTION                  = "RANUNI";
   public static final String  SAS_SQL_STDDEV                         = "STD";
   public static final String  SAS_SQL_DATE_FORMAT_DEFAULT_IDENTIFIER = "format=yymmdd10.";

   // miscellaneous
   public static final String  SQL_MIN_FUNCTION_ALIAS                 = "MIN_VAL";
   public static final String  SQL_MAX_FUNCTION_ALIAS                 = "MAX_VAL";
   public static final String  DEFAULT_VALUE_STRING                   = "DEFAULT '<defaultValue>'";
   public static final String  CONSRAINT_KEY_CONSTANT                 = "KEY";
   public static final String  DEFAULT_VALUE_PLACE_HOLDER             = "<defaultValue>";
   public static final String  NEW_LINE                               = System.getProperty("line.separator");
   public static final String  NULL_VALUE_STRING_REPRESENTATION       = "~#?%";
   public static final String  NULL_VALUE_NUMBER_REPRESENTATION       = "-909090909090";
}
