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
package com.execue.core.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.comparator.FeatureRangeOrderComparator;
import com.execue.core.common.bean.entity.comparator.FeatureRangeOrderInverseComparator;
import com.execue.core.common.bean.entity.unstructured.FeatureRange;
import com.execue.core.common.type.OperatorType;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 */
public class ExecueRangeUtil {

   /**
    * This method will return the list of unique bands for the list of query values depends on operator asked
    * 
    * @param values
    * @param operator
    * @param ranges
    * @return matchedRangeBands
    */
   public static List<RangeDetail> findMatchingRangeDetails (List<Double> values, OperatorType operator,
            List<RangeDetail> ranges) {
      List<RangeDetail> matchedRangeDetails = new ArrayList<RangeDetail>();
      Set<String> matchedRangeBands = new HashSet<String>();
      switch (operator) {
         case EQUALS:
            for (RangeDetail rangeDetail : ranges) {
               if (isRangeValueExists(values.get(0), rangeDetail)) {
                  matchedRangeBands.add(rangeDetail.getValue());
                  break;
               }
            }
            break;
         case NOT_EQUALS:
            for (RangeDetail rangeDetail : ranges) {
               if (!isRangeValueExists(values.get(0), rangeDetail)) {
                  matchedRangeBands.add(rangeDetail.getValue());
               }
            }
            break;
         case GREATER_THAN:
            matchedRangeBands.addAll(getGreaterThanRangeBands(values.get(0), ranges));
            break;
         case LESS_THAN:
            matchedRangeBands.addAll(getLessThanRangeBands(values.get(0), ranges));
            break;
         case GREATER_THAN_EQUALS:
            for (RangeDetail rangeDetail : ranges) {
               if (isRangeValueExists(values.get(0), rangeDetail)) {
                  matchedRangeBands.add(rangeDetail.getValue());
                  break;
               }
            }
            matchedRangeBands.addAll(getGreaterThanRangeBands(values.get(0), ranges));
            break;
         case LESS_THAN_EQUALS:
            for (RangeDetail rangeDetail : ranges) {
               if (isRangeValueExists(values.get(0), rangeDetail)) {
                  matchedRangeBands.add(rangeDetail.getValue());
                  break;
               }
            }
            matchedRangeBands.addAll(getLessThanRangeBands(values.get(0), ranges));
            break;
         case BETWEEN:
            Set<String> greaterThanRangeBands = getGreaterThanRangeBands(values.get(0), ranges);
            Set<String> lessThanRangeBands = getLessThanRangeBands(values.get(1), ranges);
            greaterThanRangeBands.retainAll(lessThanRangeBands);
            matchedRangeBands.addAll(greaterThanRangeBands);
            break;
         case IN:
            for (Double value : values) {
               for (RangeDetail rangeDetail : ranges) {
                  if (isRangeValueExists(value, rangeDetail)) {
                     matchedRangeBands.add(rangeDetail.getValue());
                     break;
                  }
               }
            }
            break;
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(matchedRangeBands)) {
         for (RangeDetail rangeDetail : ranges) {
            if (matchedRangeBands.contains(rangeDetail.getValue())) {
               matchedRangeDetails.add(rangeDetail);
            }
         }
      }
      return matchedRangeDetails;
   }

   /**
    * This method will return the matched bands greater then the value asked by user.
    * 
    * @param rangeValue
    * @param ranges
    * @return matchedRangeBands
    */
   private static Set<String> getGreaterThanRangeBands (double rangeValue, List<RangeDetail> ranges) {
      Set<String> matchedRangeBands = new HashSet<String>();
      int index = 0;
      for (RangeDetail rangeDetail : ranges) {
         if (isRangeValueExists(rangeValue, rangeDetail)) {
            break;
         }
         index++;
      }
      List<RangeDetail> greaterRanges = ranges.subList(index, ranges.size());
      for (RangeDetail rangeDetail : greaterRanges) {
         matchedRangeBands.add(rangeDetail.getValue());
      }
      return matchedRangeBands;
   }

   /**
    * This method will return the matched bands smaller then the value asked by user.
    * 
    * @param rangeValue
    * @param ranges
    * @return matchedRangeBands
    */
   private static Set<String> getLessThanRangeBands (double rangeValue, List<RangeDetail> ranges) {
      Set<String> matchedRangeBands = new HashSet<String>();
      int index = 0;
      for (RangeDetail rangeDetail : ranges) {
         if (isRangeValueExists(rangeValue, rangeDetail)) {
            break;
         }
         index++;
      }
      List<RangeDetail> lessRanges = ranges.subList(0, index + 1);
      for (RangeDetail rangeDetail : lessRanges) {
         matchedRangeBands.add(rangeDetail.getValue());
      }
      return matchedRangeBands;
   }

   /**
    * This method will find if rangeValue falls in the rangeDetail
    * 
    * @param rangeValue
    * @param rangeDetail
    * @return boolean value
    */
   private static boolean isRangeValueExists (double rangeValue, RangeDetail rangeDetail) {
      boolean isExists = false;
      Double lowerLimit = rangeDetail.getLowerLimit();
      Double upperLimit = rangeDetail.getUpperLimit();
      if (lowerLimit != null && upperLimit != null) {
         if (rangeValue >= lowerLimit.doubleValue() && rangeValue < upperLimit.doubleValue()) {
            isExists = true;
         }
      } else if (lowerLimit == null && upperLimit != null) {
         if (rangeValue < upperLimit.doubleValue()) {
            isExists = true;
         }
      } else if (lowerLimit != null && upperLimit == null) {
         if (rangeValue >= lowerLimit.doubleValue()) {
            isExists = true;
         }
      }
      return isExists;
   }

   /**
    * This method is specific to handling of finding matching ranges on set of Feature Ranges 
    *    and against a BETWEEN-AND based condition
    * 
    * startValue has to be in one of the range bands, if found then that is the starting band
    *    if no range band is found, then see if startValue is less than start value of lowest range band
    *    if so, then consider the lowest band as the starting band
    * 
    * endValue has to be in one of the range bands, if found then that is the ending band
    *    if no range band is found, then see if endValue is greater than start value of lowest range band
    *    if so, then consider the Highest band as the ending band
    *    
    * @param featureRanges
    * @param startValue
    * @param endValue
    * @return
    */
   public static List<FeatureRange> findMatchingFeatureRangesForBetweenCondition (List<FeatureRange> featureRanges,
            Double startValue, Double endValue) {
      List<FeatureRange> matchedFeatureRanges = new ArrayList<FeatureRange>();
      if (startValue == null || endValue == null || ExecueCoreUtil.isCollectionEmpty(featureRanges)) {
         return matchedFeatureRanges;
      }
      Integer startFeatureRangeOrder = null;
      Collections.sort(featureRanges, new FeatureRangeOrderComparator());
      for (FeatureRange featureRange : featureRanges) {
         if (startValue.doubleValue() >= featureRange.getStartValue().doubleValue()
                  && startValue.doubleValue() <= featureRange.getEndValue().doubleValue()) {
            startFeatureRangeOrder = featureRange.getRangeOrder();
            break;
         }
      }
      if (startFeatureRangeOrder == null) {
         if (startValue.doubleValue() < featureRanges.get(0).getStartValue().doubleValue()) {
            startFeatureRangeOrder = featureRanges.get(0).getRangeOrder();
         } else {
            return matchedFeatureRanges;
         }
      }

      Integer endFeatureRangeOrder = null;
      Collections.sort(featureRanges, new FeatureRangeOrderInverseComparator());
      for (FeatureRange featureRange : featureRanges) {
         if (featureRange.getStartValue().doubleValue() <= endValue.doubleValue()
                  && featureRange.getEndValue().doubleValue() >= endValue.doubleValue()) {
            endFeatureRangeOrder = featureRange.getRangeOrder();
            break;
         }
      }
      if (endFeatureRangeOrder == null) {
         if (endValue.doubleValue() > featureRanges.get(featureRanges.size() - 1).getStartValue().doubleValue()) {
            endFeatureRangeOrder = featureRanges.get(0).getRangeOrder();
         } else {
            return matchedFeatureRanges;
         }
      }
      Collections.sort(featureRanges, new FeatureRangeOrderComparator());
      for (FeatureRange featureRange : featureRanges) {
         if (featureRange.getRangeOrder().intValue() >= startFeatureRangeOrder.intValue()
                  && featureRange.getRangeOrder().intValue() <= endFeatureRangeOrder.intValue()) {
            matchedFeatureRanges.add(featureRange);
         }
      }
      return matchedFeatureRanges;
   }

}
