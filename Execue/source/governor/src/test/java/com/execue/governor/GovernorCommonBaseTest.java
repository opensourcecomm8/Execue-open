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


package com.execue.governor;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.BusinessCondition;
import com.execue.core.common.bean.BusinessQuery;
import com.execue.core.common.bean.BusinessStat;
import com.execue.core.common.bean.BusinessTerm;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.governor.BusinessEntityTerm;
import com.execue.core.common.bean.querygen.QueryValue;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;

public abstract class GovernorCommonBaseTest extends GovernorBaseTest {

   private BusinessTerm getSampleConceptBusinessTerm (String conceptName, String displayName) {
      BusinessTerm businessTerm = new BusinessTerm();
      BusinessEntityTerm businessEntityTerm = new BusinessEntityTerm();
      Concept concept = new Concept();
      concept.setName(conceptName);
      concept.setDisplayName(displayName);
      businessEntityTerm.setBusinessEntity(concept);
      businessEntityTerm.setBusinessEntityType(BusinessEntityType.CONCEPT);

      businessTerm.setBusinessEntityTerm(businessEntityTerm);
      return businessTerm;
   }

   public BusinessQuery prepareBusinessQuery () {
      BusinessQuery businessQuery = new BusinessQuery();
      List<BusinessTerm> metrics = new ArrayList<BusinessTerm>();
      BusinessTerm businessTerm = getSampleConceptBusinessTerm("NetIncome", "Net Income");
      BusinessStat businessStat = new BusinessStat();
      businessStat.setRequestedByUser(true);
      businessTerm.setBusinessStat(businessStat);

      businessTerm.setAlternateEntity(false);
      metrics.add(businessTerm);
      businessQuery.setMetrics(metrics);

      List<BusinessCondition> conditions = new ArrayList<BusinessCondition>();
      BusinessCondition businessCondition = new BusinessCondition();
      businessCondition.setLhsBusinessTerm(getSampleConceptBusinessTerm("NetBasicEPS", "Net Basic EPS"));

      businessCondition.setOperator(OperatorType.GREATER_THAN);

      businessCondition.setOperandType(OperandType.VALUE);
      List<QueryValue> queryValues = new ArrayList<QueryValue>();
      QueryValue queryValue = new QueryValue();
      queryValue.setDataType(DataType.STRING);
      queryValue.setValue("1000");

      queryValues.add(queryValue);
      businessCondition.setRhsValues(queryValues);
      conditions.add(businessCondition);

      businessQuery.setConditions(conditions);
      return businessQuery;
   }

}
