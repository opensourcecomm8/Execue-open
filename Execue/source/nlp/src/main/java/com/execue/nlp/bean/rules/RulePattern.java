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


package com.execue.nlp.bean.rules;

import java.util.List;
import java.util.Map;

public class RulePattern extends Pattern {

   private static final long  serialVersionUID       = 5140736388431425483L;

   public static final String REGULAR                = "REGULAR";
   public static final String RECURSIVE              = "RECURSIVE";
   public static final String ALLOWED                = "ALLOWED";
   public static final String NOT_ALLOWED            = "NOT ALLOWED";
   private String             defaultValue;
   private boolean            required               = true;
   private boolean            compareWithNextPattern = false;

   private int                minOccurances          = 2;
   private int                maxOccurances          = 99;

   private Map                patternIdMap;                                 // patternId and pattern
   private List               patternList;

   /**
    * @return the maxOccurances
    */
   public int getMaxOccurances () {
      return maxOccurances;
   }

   /**
    * @param maxOccurances
    *           the maxOccurances to set
    */
   public void setMaxOccurances (int maxOccurances) {
      this.maxOccurances = maxOccurances;
   }

   /**
    * @return the minOccurances
    */
   public int getMinOccurances () {
      return minOccurances;
   }

   /**
    * @param minOccurances
    *           the minOccurances to set
    */
   public void setMinOccurances (int minOccurances) {
      this.minOccurances = minOccurances;
   }

   /**
    * @return the patternIdMap
    */
   public Map getPatternIdMap () {
      return patternIdMap;
   }

   /**
    * @param patternIdMap
    *           the patternIdMap to set
    */
   public void setPatternIdMap (Map patternIdMap) {
      this.patternIdMap = patternIdMap;
   }

   /**
    * @return the required
    */
   public boolean isRequired () {
      return required;
   }

   /**
    * @param required
    *           the required to set
    */
   public void setRequired (boolean required) {
      this.required = required;
   }

   /**
    * @return the compareWithNextPattern
    */
   public boolean isCompareWithNextPattern () {
      return compareWithNextPattern;
   }

   /**
    * @param compareWithNextPattern
    *           the compareWithNextPattern to set
    */
   public void setCompareWithNextPattern (boolean compareWithNextPattern) {
      this.compareWithNextPattern = compareWithNextPattern;
   }

   /**
    * @return the patternList
    */
   public List getPatternList () {
      return patternList;
   }

   /**
    * @param patternList
    *           the patternList to set
    */
   public void setPatternList (List patternList) {
      this.patternList = patternList;
   }

   public String getDefaultValue () {
      return defaultValue;
   }

   public void setDefaultValue (String defaultValue) {
      this.defaultValue = defaultValue;
   }
}
