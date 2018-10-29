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


package com.execue.das.solr.service;

/**
 * @author Vishay
 */
public interface ISolrConstants {

   public static final String INCLUSIVE_RANGE_START     = "[";
   public static final String INCLUSIVE_RANGE_END       = "]";
   public static final String EXCLUSIVE_RANGE_START     = "{";
   public static final String EXCLUSIVE_RANGE_END       = "}";
   public static final String BRACE_START               = "(";
   public static final String BRACE_END                 = ")";
   public static final String INFINITY_RANGE_SYMBOL     = "*";
   public static final String RANGE_DEFINITION_OPERATOR = "TO";
   public static final String SPACE                     = " ";
   public static final String COLON                     = ":";
   public static final String COMMA                     = ",";
   public static final String SEMICOLON                 = ";";
   public static final String DOUBLE_QUOTE              = "\"";
   public static final String OR_OPERATOR               = "OR";
   public static final String AND_OPERATOR              = "AND";
}
