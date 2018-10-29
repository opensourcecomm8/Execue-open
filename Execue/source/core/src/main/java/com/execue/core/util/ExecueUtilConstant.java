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


package com.execue.core.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public interface ExecueUtilConstant {

   public static final SimpleDateFormat twoDigitYearSimpleDateFormat        = new SimpleDateFormat("yy");

   public static final SimpleDateFormat fourDigitYearSimpleDateFormat       = new SimpleDateFormat("yyyy");

   public static final int              DEFAULT_NAME_LENGTH                 = 35;

   public static List<String>           specialChars                        = new ArrayList<String>();

   public static final String           VERB_BASE_FORM                      = "VB";

   public static final String           VERB_PAST_TENSE                     = "VBD";

   public static final String           VERB_PRESENT_PARTICIPLE             = "VBG";

   public static final String           VERB_PAST_PARTICIPLE                = "VBN";

   public static final String           VERB_NON_3D_PERSON_PRESENT_SINGULAR = "VBP";

   public static final String           VERB_3D_PERSON_PRESENT_SINGULAR     = "VBZ";

   public static final String           GREATER_THAN_SYM                    = ">";

   public static final String           GREATER_THAN                        = "greaterthan";

   public static final String           MORE_THAN                           = "morethan";

   public static final String           GREATER_THAN_SHORT                  = "gt";

   public static final String           LESS_THAN_SYM                       = "<";

   public static final String           LESS_THAN                           = "lessthan";

   public static final String           LESS_THAN_SHORT                     = "lt";

   public static final String           EQUIVALENCE_SYM                     = "==";

   public static final String           EQUAL_TO_SYM                        = "=";

   public static final String           EQUAL_TO_SHORT                      = "eq";

   public static final String           EQUAL_TO                            = "equalto";

   public static final String           LESS_THAN_EQUAL_TO_SYM              = "<=";

   public static final String           LESS_THAN_EQUAL_TO                  = "lessthanequalto";

   public static final String           LESS_THAN_EQUAL_TO_SHORT            = "le";

   public static final String           GREATER_THAN_EQUAL_TO_SYM           = ">=";

   public static final String           GREATER_THAN_EQUAL_TO               = "greaterthanequalto";

   public static final String           GREATER_THAN_EQUAL_TO_SHORT         = "ge";

   public static final String           SUBORDINATING_CONJUNCTION           = "IN";

   public static final String           COORDINATING_CONJUNCTION            = "CC";

   public static final String           DETERMINER                          = "DT";

   public static final String           MODAL_AUXILIARY                     = "MD";

   public static final String           INTERJECTION                        = "UH";

   public static final String           TO                                  = "TO";

   public static final String           BETWEEN                             = "BETN";

   public static final String           COMMA                               = ",";

   public static final String           UNDER_SCORE                         = "_";
}
