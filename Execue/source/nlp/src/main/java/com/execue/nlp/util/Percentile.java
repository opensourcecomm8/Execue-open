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


package com.execue.nlp.util;

/**
 * @author dasariv
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Percentile {

   public static double[] percentiles (Object[] values) {
      double[] vals = new double[values.length];
      for (int i = 0; i < values.length; i++) {
         if (values[i] instanceof Double)
            vals[i] = (Double) values[i];
         else {
            // TODO throw ExecueNLPException
         }
      }
      return percentiles(vals);
   }

   public static double[] percentiles (double[] values) {
      double[] unsorted = new double[values.length];
      double[] percentiles = new double[values.length];

      System.arraycopy(values, 0, unsorted, 0, values.length);
      Arrays.sort(values);

      Map<String, Integer> perMap = getRanksMap(values);

      // Calculating the Percentiles

      for (int x = 0; x < unsorted.length; x++) {
         String value_key = Double.toString(unsorted[x]);
         percentiles[x] = (100 / unsorted.length) * ((perMap.get(value_key)) - 0.5);
      }
      return percentiles;
   }

   public static Map<Double, Double> getPercentileMap (Object[] values) {
      double[] vals = new double[values.length];
      for (int i = 0; i < values.length; i++) {
         if (values[i] instanceof Double)
            vals[i] = (Double) values[i];
         else {
            // TODO throw ExecueNLPException
         }
      }
      return getPercentileMap(vals);
   }

   public static Map<Double, Double> getRelativePercentageMap (Object[] values) {
      double[] vals = new double[values.length];
      for (int i = 0; i < values.length; i++) {
         if (values[i] instanceof Double)
            vals[i] = (Double) values[i];
         else {
            // TODO throw ExecueNLPException
         }
      }
      return getRelativePercentageMap(vals);
   }

   public static Map<Double, Double> getPercentileMap (double[] values) {
      double[] unsorted = new double[values.length];
      Map<Double, Double> percentiles = new HashMap<Double, Double>(1);

      System.arraycopy(values, 0, unsorted, 0, values.length);
      Arrays.sort(values);

      Map<String, Integer> perMap = getRanksMap(values);

      // Calculating the Percentiles

      for (double anUnsorted : unsorted) {
         String value_key = Double.toString(anUnsorted);
         double percentileVal = (100 / unsorted.length) * (perMap.get(value_key) - 0.5);
         percentiles.put(new Double(value_key), percentileVal);
      }
      return percentiles;
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
         double relativePercentage = (anUnsorted / max) * 100;
         percentiles.put(new Double(value_key), relativePercentage);
      }
      return percentiles;
   }

   private static Map<String, Integer> getRanksMap (double[] values) {
      Map<String, Integer> perMap = new HashMap<String, Integer>();

      // Putting the ranks in a Hashmap

      int rank = values.length;
      for (int i = values.length - 1; i >= 0; i--) {
         String value_key = Double.toString(values[i]);
         if (!perMap.containsKey(value_key)) {
            perMap.put(value_key, rank--);
         } else {
            // Same values get same rank but then next smallest value will have rank lower by the number of same values
            // Example - in an array (90,90,80) rank for both 90s would be 3
            // but for 80 it would be lowered by number of same values i.e. as we have two 90s
            // rank of 80 would be (3-2) i.e. 1

            rank--;
         }
      }
      return perMap;
   }
}