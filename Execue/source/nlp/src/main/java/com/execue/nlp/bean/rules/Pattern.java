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


/*
 * Created on Aug 21, 2008
 */
package com.execue.nlp.bean.rules;

import java.util.List;

import com.execue.core.common.bean.algorithm.BaseBean;

/**
 * @author kaliki
 */
public class Pattern extends BaseBean {

   private String       id;
   private String       regex;
   private List<String> superConcepts;
   private List<String> nlpTags;
   private List<String> excludeNlpTags;
   private String       patternType;
   private List<String> excludeSuperConcepts;
   private boolean      fixedPart = false;

   public String getId () {
      return id;
   }

   public void setId (String id) {
      this.id = id;
   }

   public String getRegex () {
      return regex;
   }

   public void setRegex (String regex) {
      this.regex = regex;
   }

   public List<String> getSuperConcepts () {
      return superConcepts;
   }

   public void setSuperConcepts (List<String> superConcepts) {
      this.superConcepts = superConcepts;
   }

   public boolean isFixedPart () {
      return fixedPart;
   }

   public void setFixedPart (boolean fixedPart) {
      this.fixedPart = fixedPart;
   }

   public List<String> getNlpTags () {
      return nlpTags;
   }

   public void setNlpTags (List<String> nlpTags) {
      this.nlpTags = nlpTags;
   }

   public List<String> getExcludeNlpTags () {
      return excludeNlpTags;
   }

   public void setExcludeNlpTags (List<String> excludeNlpTags) {
      this.excludeNlpTags = excludeNlpTags;
   }

   public List<String> getExcludeSuperConcepts () {
      return excludeSuperConcepts;
   }

   public void setExcludeSuperConcepts (List<String> excludeSuperConcepts) {
      this.excludeSuperConcepts = excludeSuperConcepts;
   }

   public String getPatternType () {
      return patternType;
   }

   public void setPatternType (String patternType) {
      this.patternType = patternType;
   }
}
