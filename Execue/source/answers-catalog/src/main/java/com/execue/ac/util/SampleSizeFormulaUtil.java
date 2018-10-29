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


package com.execue.ac.util;

import org.apache.log4j.Logger;

/**
 * @author Vishay
 */
public class SampleSizeFormulaUtil {

   private static final Logger   log                        = Logger.getLogger(SampleSizeFormulaUtil.class);

   // Coefficients in rational approximations
   private static final double[] firstCoefInRationalApprox  = { -3.969683028665376E+01, 2.209460984245205E+02,
            -2.759285104469687E+02, 1.383577518672690E+02, -3.066479806614716E+01, 2.506628277459239E+00 };
   private static final double[] secondCoefInRationalApprox = { -5.447609879822406E+01, 1.615858368580409E+02,
            -1.556989798598866E+02, 6.680131188771972E+01, -1.328068155288572E+01 };

   public static Double calculateZValue (Double confidenceLevelAsValue) {
      Double probablity = (1 - confidenceLevelAsValue) / 2;
      Double quotient = probablity - 0.5;
      Double rational = quotient * quotient;
      double zValue = (((((firstCoefInRationalApprox[0] * rational + firstCoefInRationalApprox[1]) * rational + firstCoefInRationalApprox[2])
               * rational + firstCoefInRationalApprox[3])
               * rational + firstCoefInRationalApprox[4])
               * rational + firstCoefInRationalApprox[5])
               * quotient
               / (((((secondCoefInRationalApprox[0] * rational + secondCoefInRationalApprox[1]) * rational + secondCoefInRationalApprox[2])
                        * rational + secondCoefInRationalApprox[3])
                        * rational + secondCoefInRationalApprox[4])
                        * rational + 1);
      return Math.abs(zValue);
   }

   //Method to calculate sample sizes based design notes from
   //look at last example of http://www.itl.nist.gov/div898/handbook/ppc/section3/ppc333.htm
   //read "Sample size calculation Final3. doc" has this formula under Section 1a)
   //the variable e was mentioned as confidence interval, which can be derived from mean and error rate
   public static Double calculateSampleSizeUsingMeanSamplingStrategy (Double zValue, Double errorRateAsValue,
            Double stddevValue, Double meanValue) {
      if (meanValue == 0) {
         return 0d;
      }

      // Get the confidence interval the sample is going to represent for the given mean
      double samplingError = meanValue * errorRateAsValue;

      // formula is  square of (z * stddev)/samplingError
      Double sampleSizeForMeanStrategy = Math.pow(zValue * stddevValue / samplingError, 2);

      if (log.isDebugEnabled()) {
         log.debug("Sample Size : " + sampleSizeForMeanStrategy + " [zValue : " + zValue + ", errorRateValue : "
                  + errorRateAsValue + ", Mean : " + meanValue + ", Std Dev : " + stddevValue + "]");
      }

      return sampleSizeForMeanStrategy;
   }

   public static Double calculateSampleSizeUsingProportionSamplingStrategy (Double zValue, Double errorRateAsValue,
            Double meanValue, Long totalPopulation) {
      // formula is (z square * mean * (1-mean))/errorrate square
      // adjustment formula
      // calculatedSampleSize/(1+calculatedSampleSize-1/totalPopulation)
      Double sampleSizeForProportionSamplingStrategy = Math.pow(zValue, 2) * meanValue * (1 - meanValue)
               / Math.pow(errorRateAsValue, 2);
      // adjust the sample size if population is known
      if (totalPopulation != null && totalPopulation != 0) {
         sampleSizeForProportionSamplingStrategy = sampleSizeForProportionSamplingStrategy
                  / (1 + (sampleSizeForProportionSamplingStrategy - 1) / totalPopulation);
      }
      return sampleSizeForProportionSamplingStrategy;
   }

   public static Double calculateVariance (Double stddevValue, Double meanValue) {
      // stddev value divided by abs value of standard mean
      Double variance = 0d;
      if (meanValue != 0) {
         variance = stddevValue / Math.abs(meanValue);
      }
      return variance;
   }
}