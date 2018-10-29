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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExecueConstants {

   public static final String ORACLE                          = "Oracle";
   public static final String MYSQL                           = "MySQL";
   public static final String SQLSERVER                       = "SQLServer";

   public static final String LEFT                            = "left";
   public static final String RIGHT                           = "right";

   public static final String INDICATORS                      = "Indicator";

   public static final String FLAGS                           = "Flags";
   public static final String VALUE                           = "Value";
   public static final String DATE                            = "DATE";
   public static final String TIMEFRAME                       = "TFRAME";
   public static final String OPERATOR                        = "OPER";

   public static final String PRE_SUBORDINATING_CONJUNCTION   = "+IN";
   public static final String POST_SUBORDINATING_CONJUNCTION  = "IN+";

   public static final String CARDINAL_NUMBER                 = "CD";
   public static final String ADJECTIVE                       = "JJ";
   public static final String ADJECTIVE_COMPARATIVE           = "JJR";
   public static final String ADJECTIVE_SUPERLATIVE           = "JJS";

   public static final String REPLACED_DETERMINER             = "BY";
   public static final String EXISTENTIAL_THERE               = "EX";
   public static final String FOREIGN_WORD                    = "FW";
   public static final String LIST_ITEM_MAKER                 = "LS";

   public static final String SINGULAR_NOUN                   = "NN";
   public static final String NOUN                            = "NN";
   public static final String SINGULAR_PROPER_NOUN            = "NNP";
   public static final String PLURAL_PROPER_NOUN              = "NNPS";
   public static final String PLURAL_NOUN                     = "NNS";
   public static final String WORD                            = "WRD";
   public static final String PREDETRMINER                    = "PDT";
   public static final String POSSESSIVE_ENDING               = "POS";
   public static final String PERSONAL_PRONOUN                = "PRP";
   public static final String POSSESSSIVE_PRONOUN             = "PRP$";
   public static final String ADVERB                          = "RB";
   public static final String ADVERB_COMPARATIVE              = "RBR";
   public static final String ADVERB_SUPERLATIVE              = "RBS";
   public static final String PARTICLE                        = "RP";
   public static final String SYMBOL                          = "SYM";

   public static final String WH_DETERMINER                   = "WDT";
   public static final String WH_PRONOUN                      = "WP";
   public static final String WH_POSSESSIVE_PRONOUN           = "WP$";
   public static final String WH_ADVERB                       = "WRB";

   public static final String HASH                            = "#";
   public static final String DASH                            = "-";
   public static final String DOLLAR                          = "$";
   public static final String DOUBLE_QUOTES                   = "\"";
   public static final String OPEN_PARENTHESIS                = "(";
   public static final String CLOSE_PARENTHESIS               = ")";

   public static final String FULLSTOP                        = ".";
   public static final String COLON                           = ":";
   public static final String SEMICOLON                       = ";";
   public static final String SINGLE_QUOTE                    = "'";
   public static final String SPACE                           = " ";
   public static final String EMPTY_STRING                    = "";
   public static final String NOT_AVAILABLE                   = "N/A";
   public static final String VALUE_ALL                       = "ALL";

   public static final String ACTION                          = "ACT";
   public static final String STATISTICS                      = "STATISTICS";
   public static final String COMPARATIVE_STATISTICS          = "ComparativeStatistics";
   public static final String CHART                           = "CHRT";
   public static final String DISTRIBUTION                    = "DSTR";
   public static final String SHOW                            = "SHOW";
   public static final String COMPARE                         = "COMPARE";
   public static final String LISTING                         = "List";
   public static final String GRAPH                           = "Graph";
   public static final String BARCHART                        = "BarChart";
   public static final String PIECHART                        = "PieChart";
   public static final String LINECHART                       = "LineChart";
   public static final String CROSSTAB                        = "Crosstab";
   public static final String AVERAGE                         = "AVERAGE";
   public static final String AVERAGE_SHORT                   = "AVG";
   public static final String MEAN                            = "MEAN";
   public static final String MEDIAN                          = "MEDIAN";
   public static final String SUM                             = "SUM";
   public static final String TOTAL                           = "TOTAL";
   public static final String NUMBER_OF                       = "NUMBER OF";
   public static final String COUNT                           = "COUNT";
   public static final String BEST                            = "BEST";
   public static final String WORST                           = "WORST";
   public static final String MAXIMUM                         = "MAXIMUM";
   public static final String MINIMUM                         = "MINIMUM";
   public static final String FREQUENCY                       = "FREQUENCY";
   public static final String RATIO                           = "RATIO";
   public static final String PERCENT                         = "PERCENT";
   public static final String QUARTILES                       = "QUARTILES";
   public static final String DECILES                         = "DECILES";
   public static final String VIGINTILES                      = "VIGINTILES";
   public static final String QUINTILES                       = "QUINTILES";
   public static final String QUINDECILE                      = "QUINDECILE";
   public static final String TRIDECILE                       = "TRIDECILE";
   public static final String BIQUINTILE                      = "BIQUINTILE";
   public static final String SEMIQUINTILE                    = "SEMIQUINTILE";

   public static final String DIGIT                           = "DIGIT";
   public static final String CHAR                            = "CHARACTER";

   public static final String JANUARY                         = "JANUARY";
   public static final String FEBRUARY                        = "FEBRUARY";
   public static final String MARCH                           = "MARCH";
   public static final String APRIL                           = "APRIL";
   public static final String MAY                             = "MAY";
   public static final String JUNE                            = "JUNE";
   public static final String JULY                            = "JULY";
   public static final String AUGUST                          = "AUGUST";
   public static final String SEPTEMBER                       = "SEPTEMBER";
   public static final String OCTOBER                         = "OCTOBER";
   public static final String NOVEMBER                        = "NOVEMBER";
   public static final String DECEMBER                        = "DECEMBER";

   public static final String QUARTER                         = "QUARTER";
   public static final String QUARTER1                        = "QUARTER1";
   public static final String QUARTER2                        = "QUARTER2";
   public static final String QUARTER3                        = "QUARTER3";
   public static final String QUARTER4                        = "QUARTER4";

   public static final String JANUARY_SHORT                   = "JAN";
   public static final String FEBRUARY_SHORT                  = "FEB";
   public static final String MARCH_SHORT                     = "MAR";
   public static final String APRIL_SHORT                     = "APR";
   public static final String MAY_SHORT                       = "MAY";
   public static final String JUNE_SHORT                      = "JUN";
   public static final String JULY_SHORT                      = "JUL";
   public static final String AUGUST_SHORT                    = "AUG";
   public static final String SEPTEMBER_SHORT                 = "SEP";
   public static final String OCTOBER_SHORT                   = "OCT";
   public static final String NOVEMBER_SHORT                  = "NOV";
   public static final String DECEMBER_SHORT                  = "DEC";

   public static final String MONTH                           = "MONTH";
   public static final String YEAR                            = "YEAR";
   public static final String YEARS                           = "YEARS";

   public static final String NULL                            = "NULL";
   public static final String ALL                             = "ALL";
   public static final String STATISTICS_ASSOCIATED           = "StatAssociated";
   public static final String ECONOMIC_DATA                   = "EconomicData";

   // Type Name Constants
   public static final String DEFAULT_VERTICAL_NAME           = "Others";
   public static final String TIME_FRAME_TYPE                 = "TimeFrame";
   public static final String LOCATION_TYPE                   = "Location";
   public static final String VALUE_TYPE                      = "Value";
   public static final String UNIT_TYPE                       = "Unit";
   public static final String UNIT_SYMBOL_TYPE                = "UnitSymbol";
   public static final String UNIT_SCALE_TYPE                 = "UnitScale";
   public static final String BY_CONJUNCTION_TYPE             = "ByConjunction";
   public static final String CONJUNCTION_TYPE                = "Conjunction";
   public static final String ADJECTIVE_TYPE                  = "Adjective";
   public static final String ONTO_ENTITY_TYPE                = "OntoEntity";
   public static final String NAME_TYPE                       = "Name";
   public static final String MEASURABLE_ENTITY_TYPE          = "MeasurableEntity";
   public static final String MEASURES_PROFILE_TYPE           = "MeasuresProfile";
   public static final String COORDINATING_CONCJUNCTION_TYPE  = "CoordinatingConjunction";
   public static final String PREPOSITION_TYPE                = "Preposition";
   public static final String COMPARATIVE_STATISTICS_TYPE     = "ComparativeStatistics";
   public static final String OPERATOR_TYPE                   = "Operator";
   public static final String COMPARATIVE_INFORMATION_TYPE    = "ComparativeInformation";
   public static final String WEEK_TYPE                       = "Week";
   public static final String NUMBER_TYPE                     = "Number";
   public static final String PUNCTUATION_TYPE                = "Punctuation";
   public static final String VALUE_PREPOSITION_TYPE          = "ValuePreposition";
   public static final String TIME_QUALIFIER                  = "TimeQualifier";
   public static final String TIME_PREPOSITION                = "TimePreposition";
   public static final String SCALING_FACTOR_CONCEPT_NAME     = "ScalingFactor";

   // Type BED ID Constants
   public static final Long   VALUE_TYPE_BED_ID               = 153L;
   public static final Long   LOCATION_TYPE_BED_ID            = 301L;

   // type id constants
   public static final Long   COUNTRY_TYPE_ID                 = 302L;
   public static final Long   STATE_TYPE_ID                   = 303L;
   public static final Long   CITY_TYPE_ID                    = 304L;

   // Model Group Id Constants
   public static final Long   BASE_MODEL_GROUP_ID             = 1L;
   // TODO: NK: Need to get if from the configuration service
   public static final Long   LOCATION_MODEL_GROUP_ID         = 2L;

   // Base Concept Constants
   public static final String CURRENCY_CONCEPT                = "Currency";
   public static final String VOLUME_CONCEPT                  = "Volume";
   public static final String POWER_CONCEPT                   = "Power";
   public static final String DISTANCE_CONCEPT                = "Distance";
   public static final String WEIGHT_CONCEPT                  = "Weight";
   public static final String CURRENCY_SYMBOL_CONCEPT         = "CurrencySymbol";
   public static final String VOLUME_SYMBOL_CONCEPT           = "VolumeSymbol";
   public static final String POWER_SYMBOL_CONCEPT            = "PowerSymbol";
   public static final String DISTANCE_SYMBOL_CONCEPT         = "DistanceSymbol";
   public static final String WEIGHT_SYMBOL_CONCEPT           = "WeightSymbol";

   public static final String TIME_FRAME_CONVERTABLE_RELATION = "isConvertableTo";

   // Property constants
   public static final String PARENT_PROPERTY                 = "parentResource";
   public static final String TIME_FRAME_PROPERTY             = "hasTimeFrame";
   public static final String IS_COMPOSED_OF_PROPERTY         = "isComposedOf";
   public static final String IS_TRANSFORMABLE_TO_PROPERTY    = "isTransformableTo";
   public static final String VALUE_PROPERTY                  = "hasValue";

   private ExecueConstants () {
   }

   public static final String RANGE_DENOTER                                     = " -- ";
   public static final String WORD_BOUNDARY_DENOTER                             = " @ ";

   public static final String ONTOLOGY_FILE_URI                                 = "ontology-file-uri";
   public static final String REPLACEMENT_RULES_FILE_LOCATION                   = "replacement-rules-file";
   public static final String TEST_INPUT_DIRECTORY                              = "test-input-directory";
   public static final String TAGGER_DATA_FILE_LOCATION                         = "tagger-data-file";
   public static final String INSTANCE_FILE_LOCATION                            = "instance-file";
   public static final String DOMAIN_INSTANCE_FILE_LOCATION                     = "domain-instance-file";
   public static final String PARALLEL_WORDS_FILE_LOCATION                      = "parallel-words-file";
   public static final String DIRTY_FLAGS_FILE_LOCATION                         = "dirty-falgs-file";
   public static final String NUM_PASSES                                        = "number-of-passes";
   public static final String TIMEFRAME_RULES_FILE_LOCATION                     = "timeframe-rules-file";
   public static final String COMPARATIVE_RULES_FILE_LOCATION                   = "comparative-rules-file";
   public static final String EXECUE_TAGS_FILE_LOCATION                         = "execue-tags-file";
   public static final String PERSONAL_PROFILE_FILE                             = "personal-profile-file";
   public static final String DISPLAY_NAMES_FILE_LOCATION                       = "display-names-file";
   public static final String DISPLAY_TYPES_FILE_LOCATION                       = "display-types-file";
   public static final String STATS_FILE_LOCATION                               = "stats-file";
   public static final String BUSINESS_RULES_FILE_LOCATION                      = "business-rules-file";
   public static final String CONCEPT_ATTRIBUTE_ASSOCIATION_RULES_FILE_LOCATION = "concept-attribute-association-rules-file";
   public static final String SECONDARY_WORDS_FILE_LOCATION                     = "secondary-words-file";
   public static final String ENUMERATION_LIST_FILE_LOCATION                    = "enumeration-list-file";
   public static final String LOOKUP_SUPERCLASSES                               = "lookup-super-classes";
   public static final String MAX_NUMBER_OF_DBS_IN_RESULT                       = "max-number-of-dbs-in-result";
   public static final String SCHEMA_DISGUISE_MAP_CSV_LOCATION                  = "schema-disguise-map-csv-location";

   public static final String DATA_CONTEXT_PROPERTIES_FILE_LOCATION             = "data-context-properties-file";
   public static final String DATA_CONTEXT_NAMES                                = "data-context-names";
   public static final String SCHEMA_MAPPING_FILE_KEY_SUFFIX                    = "-schema-mapping-file";
   public static final String SCHEMA_JOINS_FILE_KEY_SUFFIX                      = "-schema-joins-file";
   public static final String DEFAULTS_FILE                                     = "defaults-file";
   public static final String QUERIES_FILE                                      = "queries-file";

   public static final String TAG_ASSOCIATED_CONEPT_NAMES_KEY                   = "tag-associated-concept-names";

   public static final String ENCRYPT_CONFIG_FLAG                               = "encryptConfig";

   public static final String PARALLEL_WORDS_RELOAD_FLAG                        = "parallelwordsdirty";
   public static final String LINE_SEPARATOR                                    = System.getProperty("line.separator");

   // Type of Where Clause Values

   public static final String RANGE                                             = "Range";
   public static final String LIST                                              = "List";
   public static final String SIMPLE_VALUE                                      = "SimpleValue";

   // Column Types

   public static final String RANGE_HEIRARCHY                                   = "RH";
   public static final String SIMPLE_HEIRARCHY                                  = "SH";
   public static final String RANGE_DIMENSION                                   = "RL";
   public static final String LOOKUP_DIMENSION                                  = "SL";
   public static final String PRIMARY_KEY                                       = "PK";
   public static final String FOREIGN_KEY                                       = "FK";
   public static final String FACT                                              = "FACT";
   public static final String DIMENSION                                         = "DIMENSION";
   public static final String MEASURE                                           = "MEASURE";

   // Database Types

   public static final String UNKNOWN_DB_TYPE                                   = "Unknown";
   public static final String RELATIONAL_DB                                     = "Relational";
   public static final String CUBE                                              = "Cube";
   public static final String N_WAY_CUBE                                        = "N-Way";
   public static final String COHORT_CUBE                                       = "Cohort";
   public static final String COHORT_SUPERSET_CUBE                              = "Cohort SuperSet";
   public static final String COHORT_NWAY_CUBE                                  = "Cohort Nway";
   public static final String SUPERSET_CUBE                                     = "SuperSet";
   public static final String DATAMART                                          = "Datamart";

   // Processor Pass Names

   public static final String INSTANCE_PASS                                     = "Instance";
   public static final String DOMAIN_INSTANCE_PASS                              = "DomainInstance";
   public static final String TIME_INSTANCE_PASS                                = "TimeInstance";
   public static final String ONTOLOGY_PASS                                     = "Ontology";

   public static final String TAB                                               = "  ";
   public static final String TAB2                                              = TAB + TAB;
   public static final String TAB3                                              = TAB2 + TAB;
   public static final String TAB4                                              = TAB2 + TAB2;

   // Custom Tags

   public static final String PROFILE                                           = "Profile";
   public static final String INSTANCE_PROFILE                                  = "IP";
   public static final String CONCEPT_PROFILE                                   = "CP";
   public static final String TAGS_PROFILE                                      = "TP";
   public static final String CONCEPT_SERIES                                    = "ConceptSeries";

   public static final String LOW                                               = "LOW";
   public static final String HI                                                = "HI";
   public static final String HIGH                                              = "HIGH";

   public static final String getEntitiesByConceptQuery                         = "getEntitiesByConceptQuery";
   public static final String getDatabasesByConceptQuery                        = "getDatabasesByConceptQuery";
   public static final String getColumnsByConceptQuery                          = "getColumnsByConceptQuery";
   public static final String getTableNameForDatabaseByID                       = "getTableNameForDatabaseByID";
   public static final String getRangeMembersForConceptQuery                    = "getRangeMembersForConceptQuery";
   public static final String getRangeMembersByConceptLowerLimitQuery           = "getRangeMembersByConceptLowerLimitQuery";
   public static final String getRangeMembersByConceptUpperLimitQuery           = "getRangeMembersByConceptUpperLimitQuery";
   public static final String getRangeMembersByConceptQuery                     = "getRangeMembersByConceptQuery";
   public static final String getRangeMembersByConceptAllRangesQuery            = "getRangeMembersByConceptAllRangesQuery";
   public static final String getLookupMembersForConceptQuery                   = "getLookupMembersForConceptQuery";
   public static final String getLookupMembersForConceptQueryByInstanceID       = "getLookupMembersForConceptQueryByInstanceID";
   public static final String getColumnTypeByIDQuery                            = "getColumnTypeByIDQuery";
   public static final String getDBIDforDBE                                     = "getDBIDforDBE";
   public static final String getTableAliases                                   = "getTableAliases";
   public static final String getDatabaseType                                   = "getDatabaseType";
   public static final String getQualifiedColumnNameForDatabase                 = "getQualifiedColumnNameForDatabase";
   public static final String getQualifiedColumnNameForDBE                      = "getQualifiedColumnNameForDBE";
   public static final String getDatasources                                    = "getDatasources";
   public static final String getMemberLookupValue                              = "getMemberLookupValue";
   public static final String getMemberLookupDescription                        = "getMemberLookupDescription";
   public static final String getRequiredColumnsForDB                           = "getRequiredColumnsForDB";
   public static final String getDimensionsForDB                                = "getDimensionsForDB";
   public static final String getPrimaryKeyColumnsForTable                      = "getPrimaryKeyColumnsForTable";
   public static final String getDBEsForDBColumn                                = "getDBEsForDBColumn";
   public static final String getTableType                                      = "getTableType";
   public static final String getDisplayValue                                   = "getDisplayValue";
   public static final String getConceptDisplayNamesForColumn                   = "getConceptDisplayNamesForColumn";
   public static final String getConceptNamesForColumn                          = "getConceptNamesForColumn";
   public static final String getMemberDescriptionForConcept                    = "getMemberDescriptionForConcept";

   // Ontological Concepts

   public static final String BY_VARIABLE                                       = "ByVariable";
   public static final String GROUP_VARIABLE                                    = "GroupVariable";
   public static final String DEFAULT                                           = "default";
   public static final String RELATIVE                                          = "Relative";
   public static final String ORDER_BY                                          = "OrderBy";
   public static final String COMPARATIVE_LIMIT                                 = "ComparativeLimit";
   public static final String MAIN_WORD                                         = "MainWord";

   public static final String MATCH_TRESHOLD                                    = "Match_Threshold";
   public static final String PARTIAL_ANSWER_SELECTION_THRESHOLD                = "partialAnswerSelectionThreshlold";
   public static final String SNOWFLAKE_PERCENT_CONFIDENCE_TRESHOLD             = "Snowflake_Percent_Threshold";
   public static final String SNOWFLAKE_PERCENTILE_CONFIDENCE_TRESHOLD          = "Snowflake_Percentile_Threshold";
   public static final String NUMBER_OF_SELECTED_SNOWFLAKES                     = "Number_Snowflakes_Selected";

   public static final String UNSUPPRTED_OPERATOR                               = "unsupportedOperMsg";
   public static final String UNSUPPRTED_STATISTIC                              = "unsupportedStatMsg";

   public static final String QUERY_TYPE                                        = "QueryType";
   public static final String TOP_BOTTOM                                        = "TOP-BOTTOM";
   public static final String TCV                                               = "TCV";
   public static final String COHORT                                            = "COHORT";
   public static final String TOP                                               = "TOP";
   public static final String BOTTOM                                            = "BOTTOM";

   public static final String MAPPED                                            = "MAPPED";

   public static final String BY                                                = "BY";

   public static final String CURRENT                                           = "CURRENT";

   public static final Map    TFM_MONTH                                         = new HashMap();
   public static final Map    TFM_QUARTER                                       = new HashMap();
   public static final Map    TFM_YEAR                                          = new HashMap();

   public static final Map    RELATIVE_TFM_MONTH                                = new HashMap();
   public static final Map    RELATIVE_TFM_QUARTER                              = new HashMap();
   public static final Map    RELATIVE_TFM_YEAR                                 = new HashMap();

   public static final List   TFM_FISCAL_MONTH_SYNONYMS                         = new ArrayList();
   public static final List   TFM_FISCAL_QUARTER_SYNONYMS                       = new ArrayList();
   public static final List   TFM_FISCAL_YEAR_SYNONYMS                          = new ArrayList();

   public static final Map    ALIAS_HELPER                                      = new HashMap();
   public static final Map    TABLE_ALIAS_MAP                                   = new HashMap();

   public static final Map    PASS_ID_MAP                                       = new HashMap();
   public static final Map    disguiseMap                                       = new HashMap();

   public static final String HYDB_USERNAME                                     = "hyDB-username";
   public static final String HYDB_PASSWORD                                     = "hyDB-password";
   public static final String NLP_USERNAME                                      = "nlpDB-username";
   public static final String NLP_PASSWORD                                      = "nlpDB-password";

   public static final List   TFM_MONTH_SYNONYMS                                = new ArrayList();
   public static final List   TFM_QUARTER_SYNONYMS                              = new ArrayList();
   public static final List   TFM_YEAR_SYNONYMS                                 = new ArrayList();

   public static final String QUARTZ_CONFIG_FILE_LOCATION                       = "quartz-config-file";

   public static final String NLP_MODULE_NAME_FOR_CONTENT_REFRESH               = "NLP_DYNAMIC_CONTENT";
   public static final String USER_DEFINED_RANGES_FOR_CONTENT_REFRESH           = "USER_DEFINED_RANGES";

   public static final String MEANING_BASED_RESULTS                             = "MEANING_BASED_RESULTS";
   public static final String KEYWORD_BASED_RESULTS                             = "KEYWORD_BASED_RESULTS";
   public static final String VERTICAL_FINANCE                                  = "Finance";

   public static final int    SUBJECTINSTANCE                                   = 1;
   public static final int    OBJECTINSTANCE                                    = 2;
   public static final int    RELATION                                          = 3;
   public static final int    SUBJECTINSTANCE_RELATION                          = 4;
   public static final int    RELATION_OBJECTINSTANCE                           = 5;
   public static final int    SUBJECTINSTANCE_OBJECTINSTANCE                    = 6;
   public static final int    SUBJECTINSTANCE_RELATION_OBJECTINSTANCE           = 7;
   public static final int    SUBJECTINSTANCE_RELATION_OBJECTCONCEPT            = 8;
   public static final int    SUBJECTCONCEPT_RELATION_OBJECTINSTANCE            = 9;
   public static final int    SUBJECTCONCEPT_RELATION_OBJECTCONCEPT             = 10;
   public static final int    SUBJECTCONCEPT_RELATION                           = 11;
   public static final int    SUBJECTCONCEPT_OBJECTINSTANCE                     = 12;
   public static final int    SUBJECTINSTANCE_OBJECTCONCEPT                     = 13;
   public static final int    RELATION_OBJECTCONCEPT                            = 14;
   public static final int    SUBJECTCONCEPT_OBJECTCONCEPT                      = 15;
   public static final int    SUBJECTCONCEPT                                    = 16;
   public static final int    OBJECTCONCEPT                                     = 17;

   // Realized Type constants
   public static final String VALUE_PREPOSITION_AFTER                           = "after";
   public static final String VALUE_PREPOSITION_BEFORE                          = "before";
   public static final String VALUE_PREPOSITION_TILL                            = "till";
   public static final String VALUE_PREPOSITION_SINCE                           = "since";

   // System level roles which should not be altered for name
   public static final String ROLE_ADMIN                                        = "ROLE_ADMIN";
   public static final String ROLE_USER                                         = "ROLE_USER";
   public static final String ROLE_GUEST                                        = "ROLE_GUEST";
   public static final String ROLE_PUBLISHER                                    = "ROLE_PUBLISHER";
   public static final String ROLE_ADV_PUBLISHER                                = "ROLE_ADV_PUBLISHER";

   static {
      TFM_QUARTER.put("DEFAULT", "4");
      for (int i = 1; i <= 4; i++) {
         String qtr = String.valueOf(i);
         TFM_QUARTER.put(new Integer(i), qtr);
         TFM_QUARTER.put("Q" + i, qtr);
         TFM_QUARTER.put("QTR" + i, qtr);
         TFM_QUARTER.put("QUART" + i, qtr);
         TFM_QUARTER.put("QUARTER" + i, qtr);
      }

      TFM_YEAR.put("DEFAULT", "2003");
      for (int i = 1990; i <= 2020; i++) {
         String yr = String.valueOf(i);
         TFM_YEAR.put(new Integer(i), yr);
         TFM_YEAR.put("YEAR" + i, yr);
         TFM_YEAR.put("YR" + i, yr);

         TFM_YEAR.put(new Integer(String.valueOf(i).substring(2, 4)), yr);
      }

      String[] months = { "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER",
            "OCTOBER", "NOVEMBER", "DECEMBER" };

      for (int i = 0; i < months.length; i++) {
         String month = months[i];
         String code = String.valueOf(i + 1);
         if (code.trim().length() == 1) {
            code = "0" + code;
         }
         TFM_MONTH.put(month, code);
         TFM_MONTH.put(month.substring(0, 3), code);
         TFM_MONTH.put(String.valueOf(i + 1), code);
      }
      // for (int i = 1; i <= 12; i++) {
      // String code = String.valueOf(i + 1);
      // if (code.trim().length() == 1)
      // code = "0" + code;
      //
      // RELATIVE_TFM_MONTH.put(String.valueOf(i), code);
      // RELATIVE_TFM_MONTH.put("M" + code, code);
      // }
      //
      // int startFiscalYr = 100;
      // for (int i = 1; i <= 120; i++) {
      // int qtrNum = ((i - 1) % 4) + 1;
      // if (qtrNum == 1)
      // startFiscalYr = startFiscalYr + 1;
      //
      // String qtr = startFiscalYr + "" + qtrNum;
      //
      // RELATIVE_TFM_QUARTER.put(new Integer(i), qtr);
      //
      // RELATIVE_TFM_QUARTER.put("Q" + i, qtr);
      // RELATIVE_TFM_QUARTER.put("QTR" + i, qtr);
      // RELATIVE_TFM_QUARTER.put("QUART" + i, qtr);
      // RELATIVE_TFM_QUARTER.put("QUARTER" + i, qtr);
      // }
      //
      // for (int i = 1; i <= 24; i++) {
      // String str = String.valueOf(i);
      // String yr = String.valueOf(100 + i);
      // RELATIVE_TFM_YEAR.put(new Integer(i), yr);
      //
      // RELATIVE_TFM_YEAR.put("YEAR" + i, yr);
      // RELATIVE_TFM_YEAR.put("YR" + i, yr);
      // RELATIVE_TFM_YEAR.put("YEAR" + str, yr);
      // RELATIVE_TFM_YEAR.put("YR" + str, yr);
      //
      // RELATIVE_TFM_YEAR.put(new Integer(str), yr);
      // }

      TFM_MONTH_SYNONYMS.add("month");
      TFM_MONTH_SYNONYMS.add("months");
      TFM_MONTH_SYNONYMS.add("mon");

      TFM_FISCAL_MONTH_SYNONYMS.add("fiscalmonth");
      TFM_FISCAL_MONTH_SYNONYMS.add("fiscalmonths");
      TFM_FISCAL_MONTH_SYNONYMS.add("fiscalmon");

      TFM_QUARTER_SYNONYMS.add("quarters");
      TFM_QUARTER_SYNONYMS.add("quarter");
      TFM_QUARTER_SYNONYMS.add("quart");
      TFM_QUARTER_SYNONYMS.add("quarts");
      TFM_QUARTER_SYNONYMS.add("qtrs");
      TFM_QUARTER_SYNONYMS.add("qtr");

      TFM_FISCAL_QUARTER_SYNONYMS.add("fiscalquarters");
      TFM_FISCAL_QUARTER_SYNONYMS.add("fiscalquarter");
      TFM_FISCAL_QUARTER_SYNONYMS.add("fiscalquart");
      TFM_FISCAL_QUARTER_SYNONYMS.add("fiscalquarts");
      TFM_FISCAL_QUARTER_SYNONYMS.add("fiscalqtrs");
      TFM_FISCAL_QUARTER_SYNONYMS.add("fiscalqtr");

      TFM_YEAR_SYNONYMS.add("years");
      TFM_YEAR_SYNONYMS.add("year");
      TFM_YEAR_SYNONYMS.add("yrs");
      TFM_YEAR_SYNONYMS.add("yr");

      TFM_FISCAL_YEAR_SYNONYMS.add("fiscalyears");
      TFM_FISCAL_YEAR_SYNONYMS.add("fiscalyear");
      TFM_FISCAL_YEAR_SYNONYMS.add("fiscalyrs");
      TFM_FISCAL_YEAR_SYNONYMS.add("fiscalyr");
   }
}