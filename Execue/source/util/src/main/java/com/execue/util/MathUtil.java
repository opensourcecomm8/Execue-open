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


package com.execue.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.execue.core.util.ExecueCoreUtil;

public class MathUtil {

   // ruthless filtering of input values, the best make though this filter
   private static double       RUTHLESS_CV_VALUE = 0.03;                            // TODO make this a configurable
   // value

   // liberal filtering
   private static double       LIBERAL_CV_VALUE  = 0.30;                            // TODO make this a configurable

   // midway filtering
   private static double       MIDWAY_CV_VALUE   = 0.15;                            // TODO make this a configurable
   // value

   private static final Logger log               = Logger.getLogger(MathUtil.class);

   private MathUtil () {

   }

   private static double normalize (double x) {
      double minimum_sigma = -3.0;
      double maximum_sigma = 3.0;
      double scale_range = maximum_sigma - minimum_sigma;

      if (x < minimum_sigma) {
         x = minimum_sigma;
      }
      if (x > maximum_sigma) {
         x = maximum_sigma;
      }

      double new_x = (x - minimum_sigma) / scale_range;
      return new_x;
   }

   public static List<Double> getStandardizedValues (List<Double> values) {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (Double value : values) {
         stats.addValue(value);
      }
      double mean = stats.getMean();
      double sd = stats.getStandardDeviation();
      log.debug("Mean: " + mean + " SD: " + sd);
      List<Double> standardValues = new ArrayList<Double>();
      // sd could be zero for all equal values.
      if (sd == 0.0) {
         for (Double value : values) {
            standardValues.add(1d);
         }
      }
      if (ExecueCoreUtil.isCollectionEmpty(standardValues)) {
         for (int j = 0; j < values.size(); j++) {
            // compute the standard values
            double new_x = (values.get(j) - mean) / sd;
            standardValues.add(normalize(new_x));
         }
      }
      String key = "value";
      int index = 1;
      Map<String, Double> valuesMap = new HashMap<String, Double>();
      for (Double standardValue : standardValues) {
         valuesMap.put(key + index++, standardValue);
      }
      Map<String, Double> relativePercentageMap = getRelativePercentageMap(valuesMap);
      List<Double> percentileStandarizedValues = new ArrayList<Double>();
      for (int intitalIndex = 1; intitalIndex < index; intitalIndex++) {
         percentileStandarizedValues.add(relativePercentageMap.get(key + intitalIndex));
      }
      return percentileStandarizedValues;
   }

   /**
    * returns top/bottom p percentile of N values
    */
   public static List<PercentileItem> getPercentileValues (double[] values, double percentile, boolean isWeighted,
            boolean isTop) {
      assert isTop == true;

      List<PercentileItem> retList = new ArrayList<PercentileItem>(0);

      if (values.length == 0) {
         return retList;
      }

      retList = getPercentile(values, percentile, isWeighted);

      return retList;
   }

   private static List<PercentileItem> getPercentile (double[] values, double percentile, boolean isWeighted) {
      // double[] opValues = {};
      // Arrays.sort(values);
      List<PercentileItem> pList = new ArrayList<PercentileItem>();
      List<PercentileItem> opList = new ArrayList<PercentileItem>();
      double sumOfAll = getSum(values, values.length);
      for (int i = 0; i < values.length; i++) {
         if (isWeighted) {
            double sumOfN = getSum(values, i + 1);
            double percentileOfN = 100 / sumOfAll * (sumOfN - values[i] / 2);
            PercentileItem pObject = new PercentileItem();
            pObject.setPercentile(percentileOfN);
            pObject.setValue(values[i]);
            pList.add(pObject);

         } else {
            double percentileOfN = 100 / values.length * (i + 1 - 0.5);
            PercentileItem pObject = new PercentileItem();
            pObject.setPercentile(percentileOfN);
            pObject.setValue(values[i]);
            pList.add(pObject);
         }
      }

      // Comparing percentiles with ones in the List
      // and returning the ones with more than the percentile
      // asked for

      for (int i = pList.size() - 1; i >= 0; i--) {
         if (pList.get(i).getPercentile() > percentile) {
            opList.add(pList.get(i));
         }
      }

      if (opList.isEmpty()) {
         // top item
         opList.add(pList.get(pList.size() - 1));
      }

      return opList;
   }

   private static double getSum (double[] values, int position) {

      double sum = 0.0;
      for (int i = 0; i < position; i++) {

         sum = sum + values[i];
      }

      return sum;
   }

   public static double getStandardDeviation (List<Double> values) {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (Double value : values) {
         stats.addValue(value);
      }
      return stats.getStandardDeviation();

   }

   public static double getMean (List<Double> values) {
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (Double value : values) {
         stats.addValue(value);
      }
      return stats.getMean();

   }

   // public static Map<Integer, List<Double>> distributeValuesInBands(List<Double> values){ return null;}

   public static Map<Integer, List<Double>> distributeValuesInBands (List<Double> values) {

      if (CollectionUtils.isEmpty(values)) {
         return null;
      }
      Map<Integer, List<Double>> valuesMapByBand = new HashMap<Integer, List<Double>>();
      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (Double value : values) {
         stats.addValue(value);
      }
      double mean = stats.getMean();
      double sd = stats.getStandardDeviation();
      double lowest = stats.getMin();
      double max = stats.getMax();
      double cv = sd / mean;
      log.debug("Mean: " + mean + " CV: " + cv + " SD: " + sd + " : Min: " + lowest + " Max: " + max);
      int j = -3;
      Collections.sort(values);
      for (int i = 0; i < 8; i++) {
         double maxForBand = mean + j * sd;
         if (i == 7) {
            maxForBand = max;
         }
         List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         valuesMapByBand.put(i + 1, rangeValues);
         lowest = maxForBand;
         // after gettting the elments increase the lowest to current max.
         // if i == 7 then ,maxFor band would be max.
         j++;
      }

      return valuesMapByBand;
   }

   // returns only top one cluster
   // uses ruthless cv values, only the best make it
   public static List<Double> getTopCluster (List<Double> values) {
      if (log.isDebugEnabled()) {
         log.debug("input=" + values);
      }
      if (CollectionUtils.isEmpty(values)) {
         return new ArrayList<Double>(1);
      }
      Map<Integer, List<Double>> out = MathUtil.linearDistribution(values, true, RUTHLESS_CV_VALUE);
      // Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(values);
      for (int i = 1; i < out.size() + 1; i++) {
         // List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         List<Double> cluster = out.get(i);
         if (cluster != null) {
            // log.debug(cluster);
         }
      }

      return out.get(out.size());
   }

   // returns only top one cluster
   // uses ruthless cv values, only the best make it
   public static List<Double> getBottomCluster (List<Double> values) {
      if (log.isDebugEnabled()) {
         log.debug("input=" + values);
      }
      if (CollectionUtils.isEmpty(values)) {
         return new ArrayList<Double>(1);
      }
      Map<Integer, List<Double>> out = MathUtil.linearDistribution(values, false, RUTHLESS_CV_VALUE);
      // Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(values);
      for (int i = 1; i < out.size() + 1; i++) {
         // List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         List<Double> cluster = out.get(i);
         if (cluster != null) {
            log.debug(cluster);
         }
      }

      return out.get(1);
   }

   // returns only top one cluster
   // liberal filtering uses a high CV value
   public static List<Double> getLiberalTopCluster (List<Double> values) {
      if (log.isDebugEnabled()) {
         log.debug("input=" + values);
      }
      if (CollectionUtils.isEmpty(values)) {
         return new ArrayList<Double>(1);
      }
      Map<Integer, List<Double>> out = MathUtil.linearDistribution(values, true, LIBERAL_CV_VALUE);
      // Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(values);
      for (int i = 1; i < out.size() + 1; i++) {
         // List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         List<Double> cluster = out.get(i);
         if (cluster != null) {
            if (log.isDebugEnabled()) {
               log.debug(cluster);
            }
         }
      }

      return out.get(out.size());
   }

   // returns only top one cluster
   // liberal filtering uses a high CV value
   public static List<Double> getLiberalBottomCluster (List<Double> values) {
      if (log.isDebugEnabled()) {
         log.debug("input=" + values);
      }
      if (CollectionUtils.isEmpty(values)) {
         return new ArrayList<Double>(1);
      }
      Map<Integer, List<Double>> out = MathUtil.linearDistribution(values, false, LIBERAL_CV_VALUE);
      // Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(values);
      for (int i = 1; i < out.size() + 1; i++) {
         // List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         List<Double> cluster = out.get(i);
         if (cluster != null) {
            log.debug(cluster);
         }
      }

      return out.get(1);
   }

   // returns only top one cluster
   // Midway filtering uses a Midway CV value
   public static List<Double> getMidwayTopCluster (List<Double> values) {
      if (log.isDebugEnabled()) {
         log.debug("input=" + values);
      }
      if (CollectionUtils.isEmpty(values)) {
         return new ArrayList<Double>(1);
      }
      Map<Integer, List<Double>> out = MathUtil.linearDistribution(values, true, MIDWAY_CV_VALUE);
      // Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(values);
      for (int i = 1; i < out.size() + 1; i++) {
         // List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         List<Double> cluster = out.get(i);
         if (cluster != null) {
            if (log.isDebugEnabled()) {
               log.debug(cluster);
            }
         }
      }

      return out.get(out.size());
   }

   // gets you all the clusters
   // you can pick up ton n out of these clusters.

   public static Map<Integer, List<Double>> getAllClusters (List<Double> values) {
      Map<Integer, List<Double>> out = MathUtil.linearDistribution(values, false, RUTHLESS_CV_VALUE);
      Map<Integer, List<Double>> returnValues = new HashMap<Integer, List<Double>>();
      int j = 0;
      for (int i = 1; i < out.size() + 1; i++) {
         // List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         List<Double> cluster = out.get(i);
         if (cluster != null) {
            log.debug(cluster);
            returnValues.put(j, cluster);
            j++;
         }
      }

      return returnValues;
   }

   public static Map<Integer, List<Double>> linearDistribution (List<Double> values, boolean top, double cv_value) {

      if (CollectionUtils.isEmpty(values)) {
         return null;
      }

      DescriptiveStatistics stats = new DescriptiveStatistics();

      for (Double value : values) {
         stats.addValue(value);
      }

      // double mean = stats.getMean();
      // double sd = stats.getStandardDeviation();
      // double lowest = stats.getMin();
      // double max = stats.getMax();
      // double cv = sd/mean;

      Collections.sort(values);

      // add the default lowest value as zero if
      // if(values.get(0) > 0.0) {
      // List<Double> newvalues = new ArrayList<Double>();
      // newvalues.add(0, new Double("0.0"));
      // newvalues.addAll(values);
      // values = newvalues;
      // }

      // set initial bands with d = 0
      int i = 1;
      Map<Integer, List<Double>> valuesMapByBand = new HashMap<Integer, List<Double>>();

      for (int j = 0; j < values.size(); j++) {
         List<Double> rangeValues = new ArrayList<Double>();
         rangeValues.add(values.get(j));
         while (j + 1 < values.size() && values.get(j).compareTo(values.get(j + 1)) == 0) {
            rangeValues.add(values.get(j));
            j++;
         }
         valuesMapByBand.put(i++, rangeValues);
      }

      // print(valuesMapByBand);

      // Map<Integer, List<Double>> valuesMapByBand = null;
      // for(int d = 1; d < max_distance; d++){
      // int d=1;
      // updateClusters(valuesMapByBand, d, sd);

      if (top) {
         getTopClusterWithCV(valuesMapByBand, cv_value);
      } else {
         updateClustersWithCV(valuesMapByBand, cv_value);
      }

      return valuesMapByBand;
   }

   private static void getTopClusterWithCV (Map<Integer, List<Double>> valuesMapByBand, double cv_value) {

      // Map<Integer, List<Double>> nextValuesMapByBand = valuesMapByBand;
      for (int i = valuesMapByBand.size(); i > 0; i--) {
         List<Double> rangeValues = valuesMapByBand.get(i);

         if (rangeValues == null) {
            continue;
         }
         if (rangeValues != null && !rangeValues.isEmpty() && i - 1 > 0) {
            // combine the lists now
            List<Double> nextRangeValues = valuesMapByBand.get(i - 1);
            if (nextRangeValues != null && !nextRangeValues.isEmpty()) {
               DescriptiveStatistics stats1 = new DescriptiveStatistics();

               // we consider only distinct values in each band
               // TreeSet<Double> rangeSet = new TreeSet<Double>();
               // rangeSet.addAll(rangeValues);
               for (Double value : rangeValues) {
                  stats1.addValue(value);
               }
               double mean1 = stats1.getMean();

               DescriptiveStatistics stats2 = new DescriptiveStatistics();
               // TreeSet<Double> nextRangeSet = new TreeSet<Double>();
               // nextRangeSet.addAll(nextRangeValues);
               for (Double value : nextRangeValues) {
                  stats2.addValue(value);
               }
               double mean2 = stats2.getMean();

               DescriptiveStatistics stats = new DescriptiveStatistics();
               stats.addValue(mean1);
               stats.addValue(mean2);

               double mean = stats.getMean();
               double sd = stats.getStandardDeviation();
               double lowest = stats.getMin();
               double max = stats.getMax();
               double cv = sd / mean;
               if (log.isDebugEnabled()) {
                  log.debug("loop=" + i + " Mean: " + mean + " CV: " + cv + " Min: " + lowest + " Max: " + max);
               }
               if (cv < cv_value) {
                  // combine the lists
                  List<Double> combinedValues = new ArrayList<Double>();
                  combinedValues.addAll(rangeValues);
                  combinedValues.addAll(nextRangeValues);
                  valuesMapByBand.remove(i);
                  valuesMapByBand.put(i - 1, combinedValues);
               } else {
                  return;
               }

            }
         }
      }
      // return nextValuesMapByBand;
   }

   private static void updateClustersWithCV (Map<Integer, List<Double>> valuesMapByBand, double cv_value) {

      // Map<Integer, List<Double>> nextValuesMapByBand = valuesMapByBand;
      // for(int i = 1; i < valuesMapByBand.size()+1; i++){
      for (int i = valuesMapByBand.size(); i > 0; i--) {
         List<Double> rangeValues = valuesMapByBand.get(i);

         if (rangeValues == null) {
            continue;
         }
         // if(rangeValues != null && !rangeValues.isEmpty() && i+1 < valuesMapByBand.size()+1) {
         if (rangeValues != null && !rangeValues.isEmpty() && i - 1 > 0) {
            // combine the lists now
            List<Double> nextRangeValues = valuesMapByBand.get(i - 1);
            if (nextRangeValues != null && !nextRangeValues.isEmpty()) {

               // DescriptiveStatistics stats = new DescriptiveStatistics();
               // for(Double value : rangeValues){
               // stats.addValue(value);
               // }
               // for(Double value : nextRangeValues){
               // stats.addValue(value);
               // }

               DescriptiveStatistics stats1 = new DescriptiveStatistics();

               // we consider only distinct values in each band
               // TreeSet<Double> rangeSet = new TreeSet<Double>();
               // rangeSet.addAll(rangeValues);
               for (Double value : rangeValues) {
                  stats1.addValue(value);
               }
               double mean1 = stats1.getMean();

               DescriptiveStatistics stats2 = new DescriptiveStatistics();
               // TreeSet<Double> nextRangeSet = new TreeSet<Double>();
               // nextRangeSet.addAll(nextRangeValues);
               for (Double value : nextRangeValues) {
                  stats2.addValue(value);
               }
               double mean2 = stats2.getMean();

               DescriptiveStatistics stats = new DescriptiveStatistics();
               stats.addValue(mean1);
               stats.addValue(mean2);

               double mean = stats.getMean();
               double sd = stats.getStandardDeviation();
               double lowest = stats.getMin();
               double max = stats.getMax();
               double cv = sd / mean;

               if (log.isDebugEnabled()) {
                  log.debug("loop=" + i + " Mean: " + mean + " CV: " + cv + " Min: " + lowest + " Max: " + max);
               }
               if (cv < cv_value) {
                  // combine the lists
                  List<Double> combinedValues = new ArrayList<Double>();
                  combinedValues.addAll(rangeValues);
                  combinedValues.addAll(nextRangeValues);
                  // valuesMapByBand.put(i, null);
                  // valuesMapByBand.put(i+1, combinedValues);
                  valuesMapByBand.put(i, null);
                  valuesMapByBand.put(i - 1, combinedValues);

               }

            }
         }
      }

   }

   private static void getTopClusterWithCVSD (Map<Integer, List<Double>> valuesMapByBand, double overallsd) {

      // Map<Integer, List<Double>> nextValuesMapByBand = valuesMapByBand;
      for (int i = valuesMapByBand.size(); i > 0; i--) {
         List<Double> rangeValues = valuesMapByBand.get(i);

         if (rangeValues == null) {
            continue;
         }
         if (rangeValues != null && !rangeValues.isEmpty() && i - 1 > 0) {
            // combine the lists now
            List<Double> nextRangeValues = valuesMapByBand.get(i - 1);
            if (nextRangeValues != null && !nextRangeValues.isEmpty()) {
               DescriptiveStatistics stats = new DescriptiveStatistics();
               for (Double value : rangeValues) {
                  stats.addValue(value);
               }
               for (Double value : nextRangeValues) {
                  stats.addValue(value);
               }

               double mean = stats.getMean();
               double sd = stats.getStandardDeviation();
               double lowest = stats.getMin();
               double max = stats.getMax();
               double cv = sd / mean;
               double sdratio = sd / overallsd;
               log.debug("loop=" + i + " Mean: " + mean + " CV: " + cv + " sd: " + sd + " sdratio: " + sdratio
                        + " Min: " + lowest + " Max: " + max);
               if (sdratio < cv && cv < 1.0 && sdratio < 0.20 || sdratio < 0.10 && cv < 1.0 || cv < 0.10
                        && sdratio < 1.0 || cv < 0.05) {

                  // combine the lists
                  List<Double> combinedValues = new ArrayList<Double>();
                  combinedValues.addAll(rangeValues);
                  combinedValues.addAll(nextRangeValues);
                  valuesMapByBand.remove(i);
                  valuesMapByBand.put(i - 1, combinedValues);

               } else {
                  return;
               }

            }
         }
      }
      // return nextValuesMapByBand;
   }

   private static void updateClustersWithCVSD (Map<Integer, List<Double>> valuesMapByBand, int d, double overallsd) {

      // Map<Integer, List<Double>> nextValuesMapByBand = valuesMapByBand;
      for (int i = 1; i < valuesMapByBand.size() + 1; i++) {
         List<Double> rangeValues = valuesMapByBand.get(i);

         if (rangeValues == null) {
            continue;
         }
         if (rangeValues != null && !rangeValues.isEmpty() && i + 1 < valuesMapByBand.size() + 1) {
            // combine the lists now
            List<Double> nextRangeValues = valuesMapByBand.get(i + 1);
            if (nextRangeValues != null && !nextRangeValues.isEmpty()) {
               DescriptiveStatistics stats = new DescriptiveStatistics();
               for (Double value : rangeValues) {
                  stats.addValue(value);
               }
               for (Double value : nextRangeValues) {
                  stats.addValue(value);
               }

               double mean = stats.getMean();
               double sd = stats.getStandardDeviation();
               double lowest = stats.getMin();
               double max = stats.getMax();
               double cv = sd / mean;
               double sdratio = sd / overallsd;
               log.debug("iteration=" + d + " loop=" + i + " Mean: " + mean + " CV: " + cv + " sd: " + sd
                        + " sdratio: " + sdratio + " Min: " + lowest + " Max: " + max);

               if (sdratio < cv && cv < 1.0 && sdratio < 0.20 || sdratio < 0.10 && cv < 1.0 || cv < 0.10
                        && sdratio < 1.0 || cv < 0.05) {
                  // if(sdratio < 0.20) {
                  // combine the lists
                  List<Double> combinedValues = new ArrayList<Double>();
                  combinedValues.addAll(rangeValues);
                  combinedValues.addAll(nextRangeValues);
                  valuesMapByBand.put(i, null);
                  valuesMapByBand.put(i + 1, combinedValues);

               }

            }
         }
      }
      // return nextValuesMapByBand;
   }

   private static List<Double> filterValuesInRange (double lowest, double maxForBand, List<Double> values) {
      List<Double> rangeList = new ArrayList<Double>();
      for (Double value : values) {
         if (value >= lowest && value < maxForBand) {
            rangeList.add(value);
         }
      }
      return rangeList;

   }

   private static void print (Map<Integer, List<Double>> valuesMapByBand) {
      log.debug("Clusters : ");
      for (int i = 1; i < valuesMapByBand.size() + 1; i++) {
         // List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
         List<Double> values = valuesMapByBand.get(i);
         log.debug(i + ": " + values);
      }
   }

   // public static Map<Integer, List<Double>> distributeByExeCueApproach (List<Double> values) {
   // if (CollectionUtils.isEmpty(values)) {
   // return null;
   // }
   // Map<Integer, List<Double>> valuesMapByBand = new HashMap<Integer, List<Double>>();
   // Collections.sort(values);
   //
   // List<Double> differences = new ArrayList<Double>();
   // for (int i = 0 ; i < values.size()-1 ; i++) {
   // Double thisVal = values.get(i);
   // Double nextVal = values.get(i+1);
   // differences.add(nextVal-thisVal);
   // }
   //
   // DescriptiveStatistics stats = new DescriptiveStatistics();
   // for (Double difference : differences) {
   // stats.addValue(difference);
   // }
   // double sd = stats.getStandardDeviation();
   //
   // List<Double> differenceFromSD = new ArrayList<Double>();
   // for (Double difference : differences) {
   // differenceFromSD.add(Math.abs(sd-difference));
   // }
   //
   // Double minVal = Collections.min(differenceFromSD);
   //
   // int thisCluster = 1;
   // int nextCluster = thisCluster + 1;
   //
   // List<Double> thisVals = new ArrayList<Double>();
   // thisVals.add(values.get(0));
   // valuesMapByBand.put(thisCluster, thisVals);
   // for (int i = 0 ; i < values.size()-1 ; i++) {
   // Double thisVal = values.get(i);
   // Double nextVal = values.get(i+1);
   //
   // if(nextVal-thisVal >= minVal) {
   // List<Double> nextVals = new ArrayList<Double>();
   // nextVals.add(nextVal);
   // valuesMapByBand.put(nextCluster, nextVals);
   // thisCluster = nextCluster;
   // nextCluster = thisCluster + 1;
   // } else {
   // thisVals = valuesMapByBand.get(thisCluster);
   // thisVals.add(nextVal);
   // }
   // }
   //
   // return valuesMapByBand;
   // }

   public static Map<Integer, List<Double>> distributeByExeCueApproach (List<Double> values) {
      if (CollectionUtils.isEmpty(values)) {
         return null;
      }
      Map<Integer, List<Double>> valuesMapByBand = new HashMap<Integer, List<Double>>();
      Collections.sort(values);

      List<Double> differences = new ArrayList<Double>();
      for (int i = 0; i < values.size() - 1; i++) {
         Double thisVal = values.get(i);
         Double nextVal = values.get(i + 1);
         differences.add(nextVal - thisVal);
      }

      DescriptiveStatistics stats = new DescriptiveStatistics();
      for (Double difference : differences) {
         stats.addValue(difference);
      }
      double sd = stats.getStandardDeviation();

      List<Double> differenceFromSD = new ArrayList<Double>();
      for (Double difference : differences) {
         differenceFromSD.add(Math.abs(sd - difference));
      }

      Double diffSDMinVal = Collections.min(differenceFromSD);
      Double diffMinVal = Collections.min(differences);
      while (diffSDMinVal < diffMinVal) {
         differenceFromSD.remove(diffSDMinVal);
         diffSDMinVal = Collections.min(differenceFromSD);
      }
      Double minVal = diffSDMinVal;

      int thisCluster = 1;
      int nextCluster = thisCluster + 1;

      List<Double> thisVals = new ArrayList<Double>();
      thisVals.add(values.get(0));
      valuesMapByBand.put(thisCluster, thisVals);
      for (int i = 0; i < values.size() - 1; i++) {
         Double thisVal = values.get(i);
         Double nextVal = values.get(i + 1);

         if (nextVal - thisVal >= minVal) {
            List<Double> nextVals = new ArrayList<Double>();
            nextVals.add(nextVal);
            valuesMapByBand.put(nextCluster, nextVals);
            thisCluster = nextCluster;
            nextCluster = thisCluster + 1;
         } else {
            thisVals = valuesMapByBand.get(thisCluster);
            thisVals.add(nextVal);
         }
      }

      return valuesMapByBand;
   }

   public static <K, V> Map<K, Double> getRelativePercentageMap (Map<K, Double> percentageMap) {
      Map<K, Double> percentileMap = new HashMap<K, Double>(1);
      if (percentageMap.isEmpty()) {
         return percentileMap;
      }
      Object[] vals = percentageMap.values().toArray();
      Map<Double, Double> percentiles = getRelativePercentageMap(vals);

      for (K key : percentageMap.keySet()) {
         percentileMap.put(key, percentiles.get(percentageMap.get(key)));
      }
      return percentileMap;
   }

   public static Map<Double, Double> getRelativePercentageMap (Object[] values) {
      double[] vals = new double[values.length];
      for (int i = 0; i < values.length; i++) {
         if (values[i] instanceof Double) {
            vals[i] = (Double) values[i];
         } else {
            // TODO throw ExecueNLPException
         }
      }
      return getRelativePercentageMap(vals);
   }

   public static Map<Double, Double> getRelativePercentageMap (double[] values) {
      double[] unsorted = new double[values.length];
      Map<Double, Double> percentiles = new HashMap<Double, Double>(1);

      System.arraycopy(values, 0, unsorted, 0, values.length);
      Arrays.sort(values);

      double max = values[values.length - 1];

      // Calculating the Percentiles

      for (double anUnsorted : unsorted) {
         String value_key = Double.toString(anUnsorted);
         double relativePercentage = anUnsorted / max * 100;
         percentiles.put(new Double(value_key), relativePercentage);
      }
      return percentiles;
   }

   // public static void main (String[] args) {
   // double[] weightList = new double[] { 99.0, 91 };
   // List<Double> list = new ArrayList<Double>(1);
   // for (double weight : weightList) {
   // list.add(weight);
   // }
   // System.out.println(list.size());
   // List<Double> liberalTopCluster = getMidwayTopCluster(list);
   // System.out.println(liberalTopCluster.size() + " " + liberalTopCluster);
   //
   // }
   public static double getCovariance (double[] xArray, double[] yArray) {
      double covarianceValue = 0;
      Covariance covariance = new Covariance();
      covarianceValue = covariance.covariance(xArray, yArray);
      return covarianceValue;
   }

   public static double getCorrelation (double[] xArray, double[] yArray) {
      double CorrelationValue = 0;
      PearsonsCorrelation pearsonsCorrelation = new PearsonsCorrelation();
      CorrelationValue = pearsonsCorrelation.correlation(xArray, yArray);
      return CorrelationValue;
   }
}
