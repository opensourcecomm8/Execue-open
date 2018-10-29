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


package com.execue.das.solr.bean;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.das.solr.type.SolrFieldType;
import com.execue.das.solr.type.SolrOperatorType;

/**
 * @author Vishay
 */
public class SolrConditionEntity {

   private String                    facetField;
   private SolrFieldType             fieldType;
   private SolrOperatorType          operatorType;
   private String                    rhsValue;
   private List<String>              rhsValues;
   private SolrRangeEntity           range;
   private boolean                   isCompositeCondition;
   private List<SolrConditionEntity> subConditions;

   public String getFacetField () {
      return facetField;
   }

   public void setFacetField (String facetField) {
      this.facetField = facetField;
   }

   public SolrFieldType getFieldType () {
      return fieldType;
   }

   public void setFieldType (SolrFieldType fieldType) {
      this.fieldType = fieldType;
   }

   public SolrOperatorType getOperatorType () {
      return operatorType;
   }

   public void setOperatorType (SolrOperatorType operatorType) {
      this.operatorType = operatorType;
   }

   public String getRhsValue () {
      return rhsValue;
   }

   public void setRhsValue (String rhsValue) {
      this.rhsValue = rhsValue;
   }

   public void addRhsValue (String value) {
      if (ExecueCoreUtil.isCollectionEmpty(rhsValues)) {
         rhsValues = new ArrayList<String>();
      }
      rhsValues.add(value);
   }

   public List<String> getRhsValues () {
      return rhsValues;
   }

   public void setRhsValues (List<String> rhsValues) {
      this.rhsValues = rhsValues;
   }

   public SolrRangeEntity getRange () {
      return range;
   }

   public void setRange (SolrRangeEntity range) {
      this.range = range;
   }

   public boolean isCompositeCondition () {
      return isCompositeCondition;
   }

   public void setCompositeCondition (boolean isCompositeCondition) {
      this.isCompositeCondition = isCompositeCondition;
   }

   public void addSubCondition (SolrConditionEntity conditionEntity) {
      if (ExecueCoreUtil.isCollectionEmpty(subConditions)) {
         subConditions = new ArrayList<SolrConditionEntity>();
      }
      subConditions.add(conditionEntity);
   }

   public List<SolrConditionEntity> getSubConditions () {
      return subConditions;
   }

   public void setSubConditions (List<SolrConditionEntity> subConditions) {
      this.subConditions = subConditions;
   }
}
