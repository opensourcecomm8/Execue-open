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


package com.execue.core.common.bean.pseudolang;

import java.util.List;

import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.OperandType;
import com.execue.core.common.type.OperatorType;

/**
 * Pseudo condition entity representing Business Condition Entity
 * 
 * @author execue
 * @since 4.0
 * @version 1.0
 */
public class PseudoConditionEntity {

   // private static final Logger log = Logger.getLogger(PseudoConditionEntity.class);

   private PseudoEntity                lhsEntity;
   private OperatorType                operator;
   private OperandType                 operandType;
   private List<String>                values;
   private List<PseudoEntity>          entities;
   private NormalizedPseudoQuery       pseudoQuery;
   private PseudoStat                  pseudoStat;
   private boolean                     subCondition;
   private List<PseudoConditionEntity> subConditions;
   private boolean                     timeFrame;
   private DateQualifier               dateQualifier;
   private String                      dateFormat;

   public boolean isTimeFrame () {
      return timeFrame;
   }

   public void setTimeFrame (boolean timeFrame) {
      this.timeFrame = timeFrame;
   }

   public DateQualifier getDateQualifier () {
      return dateQualifier;
   }

   public void setDateQualifier (DateQualifier dateQualifier) {
      this.dateQualifier = dateQualifier;
   }

   public String getDateFormat () {
      return dateFormat;
   }

   public void setDateFormat (String dateFormat) {
      this.dateFormat = dateFormat;
   }

   public boolean isSubCondition () {
      return subCondition;
   }

   public void setSubCondition (boolean subCondition) {
      this.subCondition = subCondition;
   }

   public PseudoEntity getLhsEntity () {
      return lhsEntity;
   }

   public void setLhsEntity (PseudoEntity lhsEntity) {
      this.lhsEntity = lhsEntity;
   }

   public OperatorType getOperator () {
      return operator;
   }

   public void setOperator (OperatorType operator) {
      this.operator = operator;
   }

   public OperandType getOperandType () {
      return operandType;
   }

   public void setOperandType (OperandType operandType) {
      this.operandType = operandType;
   }

   public List<String> getValues () {
      return values;
   }

   public void setValues (List<String> values) {
      this.values = values;
   }

   public List<PseudoEntity> getEntities () {
      return entities;
   }

   public void setEntities (List<PseudoEntity> entities) {
      this.entities = entities;
   }

   public NormalizedPseudoQuery getPseudoQuery () {
      return pseudoQuery;
   }

   public void setPseudoQuery (NormalizedPseudoQuery pseudoQuery) {
      this.pseudoQuery = pseudoQuery;
   }

   public PseudoStat getPseudoStat () {
      return pseudoStat;
   }

   public void setPseudoStat (PseudoStat pseudoStat) {
      this.pseudoStat = pseudoStat;
   }

   public List<PseudoConditionEntity> getSubConditions () {
      return subConditions;
   }

   public void setSubConditions (List<PseudoConditionEntity> subConditions) {
      this.subConditions = subConditions;
   }
}