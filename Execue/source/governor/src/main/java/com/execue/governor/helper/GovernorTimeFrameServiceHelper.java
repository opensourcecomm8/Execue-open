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


package com.execue.governor.helper;

import com.execue.core.common.bean.governor.StructuredCondition;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.governor.service.IGovernorTimeFrameConstants;
import com.execue.util.conversion.timeframe.helper.TimeFrameHandlerServiceHelper;

/**
 * This class is helper class to governor time frame handler service and contains methods which are pure math
 * operations(mostly date operations)
 * 
 * @author Vishay
 * @version 1.0
 * @since 05/09/2010
 */
public class GovernorTimeFrameServiceHelper extends TimeFrameHandlerServiceHelper implements
         IGovernorTimeFrameConstants {

   public static StructuredCondition copyStructuredCondition (StructuredCondition structuredCondition) {
      StructuredCondition copiedStructuredCondition = new StructuredCondition();
      copiedStructuredCondition.setLhsBusinessAssetTerm(structuredCondition.getLhsBusinessAssetTerm());
      copiedStructuredCondition.setConversionId(structuredCondition.getConversionId());
      copiedStructuredCondition.setTargetConversionUnit(structuredCondition.getTargetConversionUnit());
      copiedStructuredCondition.setOriginalLhsBusinessTerm(structuredCondition.getOriginalLhsBusinessTerm());
      copiedStructuredCondition.setOperator(structuredCondition.getOperator());
      copiedStructuredCondition.setOperandType(structuredCondition.getOperandType());
      return copiedStructuredCondition;
   }

   public static DynamicValueQualifierType normalizeDynamicValueQualifierType (
            DynamicValueQualifierType dynamicValueQualifierType) {
      DynamicValueQualifierType normalizedDynamicValueQualifierType = null;
      if (DynamicValueQualifierType.FIRST.equals(dynamicValueQualifierType)) {
         normalizedDynamicValueQualifierType = DynamicValueQualifierType.FIRST;
      } else if (DynamicValueQualifierType.LAST.equals(dynamicValueQualifierType)
               || DynamicValueQualifierType.NEXT.equals(dynamicValueQualifierType)) {
         normalizedDynamicValueQualifierType = DynamicValueQualifierType.LAST;
      }
      return normalizedDynamicValueQualifierType;
   }
}
