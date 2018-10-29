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


package com.execue.core.common.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.execue.core.common.bean.entity.unstructured.FeatureRange;

public class ExeCueRangeUtilTest {

   @Test
   public void testFindMatchingFeatureRangesForBetweenCondition () {
      Double startValue = null;
      Double endValue = null;

      List<FeatureRange> featureRanges = new ArrayList<FeatureRange>();

      FeatureRange featureRange = new FeatureRange();
      featureRange.setRangeName("Un Known");
      featureRange.setRangeOrder(1);
      featureRange.setStartValue(-1d);
      featureRange.setEndValue(-1d);
      featureRanges.add(featureRange);

      featureRange = new FeatureRange();
      featureRange.setRangeName("Low");
      featureRange.setRangeOrder(2);
      featureRange.setStartValue(0d);
      featureRange.setEndValue(100d);
      featureRanges.add(featureRange);

      featureRange = new FeatureRange();
      featureRange.setRangeName("Medium");
      featureRange.setRangeOrder(3);
      featureRange.setStartValue(101d);
      featureRange.setEndValue(200d);
      featureRanges.add(featureRange);

      featureRange = new FeatureRange();
      featureRange.setRangeName("High");
      featureRange.setRangeOrder(4);
      featureRange.setStartValue(201d);
      featureRange.setEndValue(300d);
      featureRanges.add(featureRange);

      List<FeatureRange> matchedFeatureRanges = null;

      startValue = null;
      endValue = 100d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("ERROR: Start Null : " + range.getRangeName());
      }

      startValue = 200d;
      endValue = null;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("ERROR: End Null : " + range.getRangeName());
      }

      startValue = 200d;
      endValue = 220d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(null, startValue, endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("ERROR: Ranges Null : " + range.getRangeName());
      }

      startValue = 200d;
      endValue = 220d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(
               new ArrayList<FeatureRange>(), startValue, endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("ERROR: Ranges Empty : " + range.getRangeName());
      }

      startValue = 100d;
      endValue = 101d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("100 to 101 : " + range.getRangeName());
      }

      startValue = 201d;
      endValue = 300d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("201 to 300 : " + range.getRangeName());
      }

      startValue = 0d;
      endValue = 100d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("0 to 100 : " + range.getRangeName());
      }

      startValue = -2d;
      endValue = 120d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("-2 to 120 : " + range.getRangeName());
      }

      startValue = -2d;
      endValue = -1d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("-2 to -1 : " + range.getRangeName());
      }

      startValue = 130d;
      endValue = 450d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("130 to 450 : " + range.getRangeName());
      }

      startValue = 300d;
      endValue = 450d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("300 to 450 : " + range.getRangeName());
      }

      startValue = -2d;
      endValue = 450d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("-2 to 450 : " + range.getRangeName());
      }

      startValue = 320d;
      endValue = 420d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("ERROR: 320 to 420 : " + range.getRangeName());
      }

      startValue = -5d;
      endValue = -2d;
      matchedFeatureRanges = ExecueRangeUtil.findMatchingFeatureRangesForBetweenCondition(featureRanges, startValue,
               endValue);
      for (FeatureRange range : matchedFeatureRanges) {
         System.out.println("ERROR: -5 to -2 : " + range.getRangeName());
      }
   }
}
