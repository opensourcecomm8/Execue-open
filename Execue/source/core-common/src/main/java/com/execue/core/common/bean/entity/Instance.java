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


package com.execue.core.common.bean.entity;

import java.io.Serializable;
import java.util.Set;
import java.util.regex.Pattern;

import com.execue.core.common.bean.IBusinessEntity;

/**
 * This class represnts the Instance object.
 * 
 * @author Vishay
 * @version 1.0
 * @since 12/01/09
 */
public class Instance implements IBusinessEntity, Serializable {

   private static final long             serialVersionUID = 1L;
   private Long                          id;
   private String                        name;
   private Concept                       parentConcept;
   private String                        description;
   private String                        displayName;
   private Set<BusinessEntityDefinition> businessEntityDefinitions;
   private String                        expression;
   private String                        abbreviatedName;

   private transient Pattern             compiledRegexPattern;

   @Override
   public int hashCode () {
      return 100;
   }

   @Override
   public boolean equals (Object obj) {
      if (obj instanceof Instance) {
         return this.id.equals(((Instance) obj).getId());
      }
      return false;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDescription () {
      return description;
   }

   public void setDescription (String description) {
      this.description = description;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public Concept getParentConcept () {
      return parentConcept;
   }

   public void setParentConcept (Concept parentConcept) {
      this.parentConcept = parentConcept;
   }

   public String getExpression () {
      return expression;
   }

   public void setExpression (String expression) {
      this.expression = expression;
   }

   public Pattern getCompiledRegexPattern () {
      return compiledRegexPattern;
   }

   public void setCompiledRegexPattern (Pattern compiledRegexPattern) {
      this.compiledRegexPattern = compiledRegexPattern;
   }

   public String getAbbreviatedName () {
      return abbreviatedName;
   }

   public void setAbbreviatedName (String abbreviatedName) {
      this.abbreviatedName = abbreviatedName;
   }

   public Set<BusinessEntityDefinition> getBusinessEntityDefinitions () {
      return businessEntityDefinitions;
   }

   public void setBusinessEntityDefinitions (Set<BusinessEntityDefinition> businessEntityDefinitions) {
      this.businessEntityDefinitions = businessEntityDefinitions;
   }
}