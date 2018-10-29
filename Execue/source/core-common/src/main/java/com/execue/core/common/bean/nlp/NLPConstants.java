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


package com.execue.core.common.bean.nlp;

import com.execue.core.configuration.SystemConstants;

public interface NLPConstants extends SystemConstants {

   /* NLP Tag Constants */
   String NLP_TAG_ONTO_CONCEPT_PROFILE                    = "OCP";
   String NLP_TAG_ONTO_INSTANCE_PROFILE                   = "OIP";
   String NLP_TAG_ONTO_CONCEPT                            = "OC";
   String NLP_TAG_ONTO_INSTANCE                           = "OI";
   String NLP_TAG_ONTO_TYPE                               = "OT";
   String NLP_TAG_ONTO_TYPE_INSTANCE                      = "OTI";
   String NLP_TAG_ONTO_TYPE_REGEX_INSTANCE                = "OTRI";
   String NLP_TAG_ONTO_REALIZED_TYPE                      = "ORT";
   String NLP_TAG_ONTO_REALIZED_TYPE_INSTANCE             = "ORTI";
   String NLP_TAG_ONTO_PROPERTY                           = "OP";
   String NLP_TAG_ONTO_CONCEPT_INSTANCE                   = "OCI";
   String NLP_TAG_ONTO_INSTANCE_STAT                      = "STAT";
   String NLP_TAG_PRALLEL_WORD                            = "PW";
   String NLP_TAG_SFL                                     = "SFL";
   String NLP_TAG_POS_SUBORDINATING_CONJUNCTION           = "IN";
   String NLP_TAG_POS_COORDINATING_CONJUNCTION            = "CC";
   String NLP_TAG_POS_CARDINAL_NUMBER                     = "CD";
   String NLP_TAG_POS_ADJECTIVE                           = "JJ";
   String NLP_TAG_POS_ADJECTIVE_COMPARATIVE               = "JJR";
   String NLP_TAG_POS_ADJECTIVE_SUPERLATIVE               = "JJS";
   String NLP_TAG_POS_DETERMINER                          = "DT";
   String NLP_TAG_POS_BY                                  = "BY";
   String NLP_TAG_POS_EXISTENTIAL_THERE                   = "EX";
   String NLP_TAG_POS_FOREIGN_WORD                        = "FW";
   String NLP_TAG_POS_LIST_ITEM_MAKER                     = "LS";
   String NLP_TAG_POS_MODAL_AUXILIARY                     = "MD";
   String NLP_TAG_POS_SINGULAR_NOUN                       = "NN";
   String NLP_TAG_POS_NOUN                                = "NN";
   String NLP_TAG_POS_SINGULAR_PROPER_NOUN                = "NNP";
   String NLP_TAG_POS_PLURAL_PROPER_NOUN                  = "NNPS";
   String NLP_TAG_POS_PLURAL_NOUN                         = "NNS";
   String NLP_TAG_POS_WORD                                = "WRD";
   String NLP_TAG_POS_PREDETRMINER                        = "PDT";
   String NLP_TAG_POS_POSSESSIVE_ENDING                   = "POS";
   String NLP_TAG_POS_PERSONAL_PRONOUN                    = "PRP";
   String NLP_TAG_POS_POSSESSSIVE_PRONOUN                 = "PRP$";
   String NLP_TAG_POS_ADVERB                              = "RB";
   String NLP_TAG_POS_ADVERB_COMPARATIVE                  = "RBR";
   String NLP_TAG_POS_ADVERB_SUPERLATIVE                  = "RBS";
   String NLP_TAG_POS_PARTICLE                            = "RP";
   String NLP_TAG_POS_SYMBOL                              = "SYM";
   String NLP_TAG_POS_COMMA                               = ",";
   String NLP_TAG_POS_TO                                  = "TO";
   String NLP_TAG_POS_INTERJECTION                        = "UH";
   String NLP_TAG_POS_VERB_BASE_FORM                      = "VB";
   String NLP_TAG_POS_VERB_PAST_TENSE                     = "VBD";
   String NLP_TAG_POS_VERB_PRESENT_PARTICIPLE             = "VBG";
   String NLP_TAG_POS_VERB_PAST_PARTICIPLE                = "VBN";
   String NLP_TAG_POS_VERB_NON_3D_PERSON_PRESENT_SINGULAR = "VBP";
   String NLP_TAG_POS_VERB_3D_PERSON_PRESENT_SINGULAR     = "VBZ";
   String NLP_TAG_POS_WH_DETERMINER                       = "WDT";
   String NLP_TAG_POS_WH_PRONOUN                          = "WP";
   String NLP_TAG_POS_WH_POSSESSIVE_PRONOUN               = "WP$";
   String NLP_TAG_POS_WH_ADVERB                           = "WRB";

   /* Constants for base cloud names */
   String FROM_TO_TIME_RANGE_CLOUD                        = "FromToTimeRange";
   String FROM_TO_UNIT_RANGE_CLOUD                        = "FromToUnitRange";
   String BETWEEN_AND_TIME_RANGE_CLOUD                    = "TimeRange";
   String BETWEEN_AND_UNIT_RANGE_CLOUD                    = "UnitRange";
   String RELATIVE_YEAR_CLOUD                             = "RelativeYear";
   String RELATIVE_MONTH_CLOUD                            = "RelativeMonth";
   String RELATIVE_QUARTER_CLOUD                          = "RelativeQuarter";
   String RELATIVE_DAY_CLOUD                              = "RelativeDay";
   String RELATIVE_WITH_OPERATOR_CLOUD                    = "RelativeTFOperator";
   String RELATIVE_TIME_CLOUD                             = "RelativeTFTime";
   String RELATIVE_WEEK_CLOUD                             = "RelativeWeek";
   String YEAR_CLOUD                                      = "Year";
   String RELATIVE_WEEKDAY_CLOUD                          = "RelativeWeekday";
   String WEEK_DAY_TF_CLOUD                               = "WeekdayTimeFrame";
   // TODO: NK: Need to get it from the configuration later
   String LOCATION_CLOUD                                  = "Location";
   String RELATIVE_FOR_QUANTITATIVE_CLOUD                 = "relativeForQuantitative";
   String VALUE_WITH_PREPOSITION_CLOUD                    = "ValueWithPreposition";
   String BETWEEN_AND_VALUE_RANGE_CLOUD                   = "BetweenAndValue";
   String FROM_TO_VALUE_RANGE_CLOUD                       = "FromToValue";

   /* Framework Clouds */
   String TOP_BOTTOM_CLOUD                                = "TopBottom";
   String GROUP_BY_CLOUD                                  = "Group-By";
   String GROUP_BY1_CLOUD                                 = "Group-By1";

   /* NLP double constants */
   double MIN_QUALITY                                     = 0.0;
   double MAX_QUALITY                                     = 1;
   double MIN_WEIGHT                                      = 0.0;
   double MAX_WEIGHT                                      = 10.0;

   /* Other NLP String Constants */
   String NLP_RECOGNITION_SEMANTIC_SCOPING                = "runSemanticScoping";
   String NLP_RECOGNITION_FIND_MEANING                    = "runMeaningFinder";
   String NLP_RECOGNITION_ENHANCE_MEANING                 = "runMeaningEnhancer";
   String NLP_INSTANCE_NAME_TODAY                         = "Today";
   String NLP_INSTANCE_NAME_TOMORROW                      = "Tomorrow";
   String NLP_INSTANCE_NAME_YESTERDAY                     = "Yesterday";
   String NLP_INSTANCE_NAME_DAY_BEFORE_YESTERDAY          = "DayBeforeYesterday";
   String NLP_INSTANCE_NAME_DAY_AFTER_TOMORROW            = "DayAfterTomorrow";

}