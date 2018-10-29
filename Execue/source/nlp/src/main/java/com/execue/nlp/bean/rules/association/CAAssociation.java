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
package com.execue.nlp.bean.rules.association;

import java.util.List;

import com.execue.core.common.bean.algorithm.BaseBean;
import com.execue.nlp.bean.rules.RuleRegexComponent;
import com.execue.nlp.rule.IWeightAssignmentRule;

/**
 * @author kaliki
 */
public class CAAssociation extends BaseBean {

   private List<RuleRegexComponent>    regexComponents;
   private List<IWeightAssignmentRule> associationWeights;
   private List<CAAssociationInfo>     associationInfos;

   public List<RuleRegexComponent> getRegexComponents () {
      return regexComponents;
   }

   public void setRegexComponents (List<RuleRegexComponent> regexComponents) {
      this.regexComponents = regexComponents;
   }

   public List<IWeightAssignmentRule> getAssociationWeights () {
      return associationWeights;
   }

   public void setAssociationWeights (List<IWeightAssignmentRule> associationWeights) {
      this.associationWeights = associationWeights;
   }

   public List<CAAssociationInfo> getAssociationInfos () {
      return associationInfos;
   }

   public void setAssociationInfos (List<CAAssociationInfo> associationInfos) {
      this.associationInfos = associationInfos;
   }
}
