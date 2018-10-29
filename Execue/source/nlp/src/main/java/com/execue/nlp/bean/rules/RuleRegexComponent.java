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

import com.execue.core.common.bean.algorithm.BaseBean;

public class RuleRegexComponent extends BaseBean {

   private static final long serialVersionUID = 2989752206873142744L;

   private String            name;
   private String            id;
   private List<String>      concepts;
   private List<String>      nlpTagTypes;
   private String            expression;

   /**
    * @return Returns the concepts.
    */
   public List<String> getConcepts () {
      return concepts;
   }

   /**
    * @param concepts
    *           The concepts to set.
    */
   public void setConcepts (List<String> concepts) {
      this.concepts = concepts;
   }

   /**
    * @return Returns the expression.
    */
   public String getExpression () {
      return expression;
   }

   /**
    * @param expression
    *           The expression to set.
    */
   public void setExpression (String expression) {
      this.expression = expression;
   }

   /**
    * @return Returns the id.
    */
   public String getId () {
      return id;
   }

   /**
    * @param id
    *           The id to set.
    */
   public void setId (String id) {
      this.id = id;
   }

   /**
    * @return Returns the name.
    */
   public String getName () {
      return name;
   }

   /**
    * @param name
    *           The name to set.
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return Returns the nlpTagTypes.
    */
   public List<String> getNlpTagTypes () {
      return nlpTagTypes;
   }

   /**
    * @param nlpTagTypes
    *           The nlpTagTypes to set.
    */
   public void setNlpTagTypes (List<String> nlpTagTypes) {
      this.nlpTagTypes = nlpTagTypes;
   }
}
