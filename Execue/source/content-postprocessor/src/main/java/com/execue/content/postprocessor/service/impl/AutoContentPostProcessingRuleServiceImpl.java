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


/**
 * 
 */
package com.execue.content.postprocessor.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.nlp.ValueRealizationNormalizedData;
import com.execue.sus.helper.ReducedFormHelper;

/**
 * @author Nitesh
 *
 */
public class AutoContentPostProcessingRuleServiceImpl extends BaseContentPostProcessingRuleServiceImpl {

   private ReducedFormHelper reducedFormHelper;
   private static final int  MIN_PRICE_OF_CAR                        = 500;
   private static final int  THRESHOLD_MODEL_YEAR                    = 2008;
   private static final int  MIN_MILEAGE_BEFORE_THRESHOLD_MODEL_YEAR = 500;

   /* (non-Javadoc)
    * @see com.execue.content.postprocessor.service.impl.BaseContentPostProcessingRuleServiceImpl#processRules(com.execue.core.common.bean.nlp.SemanticPossibility)
    */
   @Override
   public void processRules (SemanticPossibility semanticPossibility) {
      super.processRules(semanticPossibility);
      Graph reducedForm = semanticPossibility.getReducedForm();
      List<IGraphPath> paths = reducedForm.getPaths();
      IGraphPath pricePath = null;
      IGraphPath mileagePath = null;
      IGraphPath modelYearPath = null;
      for (IGraphPath graphPath : paths) {
         GraphPath path = (GraphPath) graphPath;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);
            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition destination = (DomainRecognition) components.get(2);

            if (source.getConceptDisplayName().equalsIgnoreCase("SalePrice")) {
               pricePath = path;
            } else if (source.getConceptDisplayName().equalsIgnoreCase("Mileage")) {
               mileagePath = path;
            } else if (destination.getConceptDisplayName().equalsIgnoreCase("ModelYear")
                     && !StringUtils.isEmpty(destination.getDefaultInstanceDisplayName())) {
               modelYearPath = path;
            }
            start = start + 2;
            i = i + 2;
         }
      }

      // Rule 1. If MODEL YEAR is coming as relative then remove it
      if (modelYearPath != null) {
         DomainRecognition year = (DomainRecognition) modelYearPath.getEndVertex();
         if (year.getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
            reducedForm.removePath(modelYearPath);
            reducedForm.removeVertex(year);
         }
      }

      // Rule 2. if (Model Year < MODEL_YEAR_THRESHOLD and mileage on car < 500 miles) set miles = -1 (unknown)
      if (modelYearPath != null && mileagePath != null) {
         int thresholdModelYear = THRESHOLD_MODEL_YEAR;
         int minMilesBeforeModelYear = MIN_MILEAGE_BEFORE_THRESHOLD_MODEL_YEAR;
         DomainRecognition year = (DomainRecognition) modelYearPath.getEndVertex();
         // Check if it is time frame normalized date and the year is not null
         if (year.getNormalizedData().getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA
                  && ((TimeFrameNormalizedData) year.getNormalizedData()).getYear() != null) {
            TimeFrameNormalizedData normalizedData = (TimeFrameNormalizedData) year.getNormalizedData();
            Object[] timeOPValue = getReducedFormHelper().getOperatorAndValue(normalizedData);
            Double currentYearNumber = (Double) timeOPValue[1];
            if (thresholdModelYear > currentYearNumber) {
               DomainRecognition value = (DomainRecognition) mileagePath.getEndVertex();
               if (value.getNormalizedData() != null
                        && value.getNormalizedData().getNormalizedDataType() == NormalizedDataType.VALUE_NORMALIZED_DATA) {
                  ValueRealizationNormalizedData valueRealizationNormalizedData = (ValueRealizationNormalizedData) value
                           .getNormalizedData();
                  Object[] operatorAndValue = getReducedFormHelper()
                           .getOperatorAndValue(valueRealizationNormalizedData);
                  if (operatorAndValue != null && operatorAndValue.length == 2) {
                     double mileageNumber = (Double) operatorAndValue[1];
                     if (valueRealizationNormalizedData.getUnitScale() == null
                              && mileageNumber < minMilesBeforeModelYear) {
                        reducedForm.removePath(mileagePath);
                        reducedForm.removeVertex(value);
                     }
                  }
               }
            }
         }
      }
      // Rule 3. price on car < minPriceOfCar set price = -1 (unknown)
      if (pricePath != null) {
         int minPriceOfCar = MIN_PRICE_OF_CAR;

         DomainRecognition value = (DomainRecognition) pricePath.getEndVertex();
         if (value.getNormalizedData() != null
                  && value.getNormalizedData().getNormalizedDataType() == NormalizedDataType.VALUE_NORMALIZED_DATA) {
            ValueRealizationNormalizedData valueRealizationNormalizedData = (ValueRealizationNormalizedData) value
                     .getNormalizedData();
            Object[] operatorAndValue = getReducedFormHelper().getOperatorAndValue(valueRealizationNormalizedData);
            if (operatorAndValue != null && operatorAndValue.length == 2) {
               double priceNumber = (Double) operatorAndValue[1];
               if (valueRealizationNormalizedData.getUnitScale() == null && priceNumber < minPriceOfCar) {
                  reducedForm.removePath(pricePath);
                  reducedForm.removeVertex(value);
               }
            }
         }
      }
   }

   /**
    * @return the reducedFormHelper
    */
   public ReducedFormHelper getReducedFormHelper () {
      return reducedFormHelper;
   }

   /**
    * @param reducedFormHelper the reducedFormHelper to set
    */
   public void setReducedFormHelper (ReducedFormHelper reducedFormHelper) {
      this.reducedFormHelper = reducedFormHelper;
   }
}
