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


package com.execue.ac.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

import com.execue.ac.bean.BasicSamplingAlgorithmInput;
import com.execue.ac.bean.BasicSamplingAlgorithmStaticInput;
import com.execue.ac.bean.MeasureStatInfo;
import com.execue.ac.exception.SamplingAlgorithmException;
import com.execue.ac.service.IBasicSampleSizeCalculatorService;

/**
 * This service contains the sampling algorithm.It tells the slice_count to be picked for each island.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class BasicSampleSizeCalculatorServiceImpl implements IBasicSampleSizeCalculatorService {

   // Coefficients in rational approximations
   private static final double[] firstCoefInRationalApprox  = { -3.969683028665376E+01, 2.209460984245205E+02,
            -2.759285104469687E+02, 1.383577518672690E+02, -3.066479806614716E+01, 2.506628277459239E+00 };
   private static final double[] secondCoefInRationalApprox = { -5.447609879822406E+01, 1.615858368580409E+02,
            -1.556989798598866E+02, 6.680131188771972E+01, -1.328068155288572E+01 };
   private static final double[] thirdCoefInRationalApprox  = { -7.784894002430293E-03, -3.223964580411365E-01,
            -2.400758277161838E+00, -2.549732539343734E+00, 4.374664141464968E+00, 2.938163982698783E+00 };
   private static final double[] fourthCoefInRationalApprox = { 7.784695709041462E-03, 3.224671290700398E-01,
            2.445134137142996E+00, 3.754408661907416E+00   };

   public static void main (String[] args) {

      BasicSamplingAlgorithmStaticInput input = new BasicSamplingAlgorithmStaticInput();
      // input.setPLow(0.02425d);
      // input.setPHigh(1 - 0.02425d);
      double confidenceLevel = 1.00d;
      double prob = (1 - confidenceLevel) / 2;
      double zvalue = getZValue(input, prob);
      System.out.println("100 : " + zvalue + "Abs : " + Math.abs(Precision.round(zvalue, 3)));

      confidenceLevel = 0.992;
      prob = (1 - confidenceLevel) / 2;
      zvalue = getZValue(input, prob);
      System.out.println("99.2 : " + zvalue + "Abs : " + Math.abs(Precision.round(zvalue, 3)));

      confidenceLevel = 0.95d;
      prob = (1 - confidenceLevel) / 2;
      zvalue = getZValue(input, prob);
      System.out.println("95 : " + zvalue + "Abs : " + Math.abs(Precision.round(zvalue, 3)));
   }

   private static double getZValue (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput, double probablity) {

      double zValue = 0;
      double quotient = 0;
      double rational = 0;

      // if (probablity < samplingAlgorithmStaticInput.getPLow()) {// Rational approximation for lower region
      //
      // quotient = Math.sqrt(-2 * Math.log(probablity));
      //
      // zValue = (((((thirdCoefInRationalApprox[0] * quotient + thirdCoefInRationalApprox[1]) * quotient +
      // thirdCoefInRationalApprox[2])
      // * quotient + thirdCoefInRationalApprox[3])
      // * quotient + thirdCoefInRationalApprox[4])
      // * quotient + thirdCoefInRationalApprox[5])
      // / ((((fourthCoefInRationalApprox[0] * quotient + fourthCoefInRationalApprox[1]) * quotient +
      // fourthCoefInRationalApprox[2])
      // * quotient + fourthCoefInRationalApprox[3])
      // * quotient + 1);
      //
      // } else if (samplingAlgorithmStaticInput.getPHigh() > probablity) {// Rational approximation for upper region
      //
      // quotient = Math.sqrt(-2 * Math.log(1 - probablity));
      //
      // zValue = -(((((thirdCoefInRationalApprox[0] * quotient + thirdCoefInRationalApprox[1]) * quotient +
      // thirdCoefInRationalApprox[2])
      // * quotient + thirdCoefInRationalApprox[3])
      // * quotient + thirdCoefInRationalApprox[4])
      // * quotient + thirdCoefInRationalApprox[5])
      // / ((((fourthCoefInRationalApprox[0] * quotient + fourthCoefInRationalApprox[1]) * quotient +
      // fourthCoefInRationalApprox[2])
      // * quotient + fourthCoefInRationalApprox[3])
      // * quotient + 1);
      //
      // } else {// Rational approximation for central region

      quotient = probablity - 0.5;

      rational = quotient * quotient;

      zValue = (((((firstCoefInRationalApprox[0] * rational + firstCoefInRationalApprox[1]) * rational + firstCoefInRationalApprox[2])
               * rational + firstCoefInRationalApprox[3])
               * rational + firstCoefInRationalApprox[4])
               * rational + firstCoefInRationalApprox[5])
               * quotient
               / (((((secondCoefInRationalApprox[0] * rational + secondCoefInRationalApprox[1]) * rational + secondCoefInRationalApprox[2])
                        * rational + secondCoefInRationalApprox[3])
                        * rational + secondCoefInRationalApprox[4])
                        * rational + 1);

      // }

      return zValue;
   }

   private double getSampleSize (BasicSamplingAlgorithmStaticInput sampleAlgorithmInput, double population,
            double stdDev, double mean) {

      double sampleSize = 0;

      // Get the confidence interval the sample is going to represent for the given mean
      double samplingError = mean * sampleAlgorithmInput.getErrorRate();

      // Get the critical value
      double zValue = getZValue(sampleAlgorithmInput, (1 - sampleAlgorithmInput.getConfidenceLevel()) / 2);

      // Get the sample size for infinite population
      double stepOneResult = zValue * stdDev / samplingError;
      double stepTwoResult = stepOneResult * stepOneResult;

      // Adjust the sample size to the actual population
      sampleSize = stepTwoResult * population / (stepTwoResult + population - 1);

      // Rule 0: If sample size is greater than population, set the sample size to
      // population
      if (sampleSize > population) {
         sampleSize = population;
      }

      /*
       * if (log.isDebugEnabled()) { log.debug("population : "+population); log.debug("std dev : "+stdDev);
       * log.debug("mean : "+mean); log.debug("sampleSize : "+sampleSize); }
       */

      return sampleSize;
   }

   private long getSliceCount (BasicSamplingAlgorithmStaticInput sampleAlgorithmInput, double population,
            double sampleSize) {
      long sliceCount = 0;
      double tempSliceCount = 0;

      if (sampleAlgorithmInput.isFunctionUseAlone()) {
         sampleSize = getDefaultSampleSize(sampleAlgorithmInput, population);
      }

      if (sampleSize > population) {
         sampleSize = population;
      }

      tempSliceCount = ((sampleSize / population)) / sampleAlgorithmInput.getSlicePercentage();
      sliceCount = (long) Math.ceil(tempSliceCount);

      // Rules for avoiding mis-calculations
      // TODO: -RG- Documentation required

      // Rule 2: If Population is less than minimum default population, pull of
      // all the slices
      if (population <= sampleAlgorithmInput.getMinPopulation()) {
         sliceCount = sampleAlgorithmInput.getNumberOfSlices();
      }

      // ** This rule should be the last one to be executed **
      // Rule 1: If the slice count is Zero, pull off default minimum number of
      // slices
      if (sliceCount < sampleAlgorithmInput.getDefaultNumberOfSlices()) {
         sliceCount = sampleAlgorithmInput.getDefaultNumberOfSlices();
      }

      /*
       * if (log.isDebugEnabled()) { log.debug("population : "+population); log.debug("sampleSize : "+sampleSize);
       * log.debug("sliceCount : "+sliceCount); }
       */
      return sliceCount;
   }

   private long getGeometricMeanBasedSliceCount (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            double population, List sampleSizes) {
      return getSliceCount(samplingAlgorithmStaticInput, population, getSampleSizesGeometricMean(
               samplingAlgorithmStaticInput, population, sampleSizes));
   }

   private long getMaxBasedSliceCount (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            double population, List sampleSizes) {
      return getSliceCount(samplingAlgorithmStaticInput, population, getMaxSampleSize(samplingAlgorithmStaticInput,
               population, sampleSizes));
   }

   private double getMaxSampleSize (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput, double population,
            List sampleSizes) {
      double sampleSize = 0;
      double tempSize = 0;
      Iterator sizes = sampleSizes.iterator();
      while (sizes.hasNext()) {
         tempSize = ((Double) sizes.next()).doubleValue();
         if (isSampleSizeConsiderable(samplingAlgorithmStaticInput, population, tempSize) && tempSize > sampleSize) {
            sampleSize = tempSize;
         }
      }
      return sampleSize;
   }

   private double getSampleSizesGeometricMean (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            double population, List sampleSizes) {
      int sizeCount = 0;
      double sizesMultiplied = 1;
      double sampleSize = 0;
      double tempSize = 0.0;
      double tempSampleSize = 0.0;
      Iterator sizes = sampleSizes.iterator();
      while (sizes.hasNext()) {
         tempSize = ((Double) sizes.next()).doubleValue();
         if (isSampleSizeConsiderable(samplingAlgorithmStaticInput, population, tempSize)) {
            sizesMultiplied = sizesMultiplied * tempSize;
            sizeCount = sizeCount + 1;
         }
      }
      tempSampleSize = getDefaultSampleSize(samplingAlgorithmStaticInput, population);
      if (sizeCount == 0) {
         if (samplingAlgorithmStaticInput.isFloorSettingRequired()) {
            sampleSize = tempSampleSize * samplingAlgorithmStaticInput.getFloorMultiplicationFactor();
         } else {
            sampleSize = tempSampleSize;
         }
      } else {
         double oneOverSizeCount = (double) ((double) 1 / (double) sizeCount);
         sampleSize = Math.pow(sizesMultiplied, oneOverSizeCount);
         if (samplingAlgorithmStaticInput.isFloorSettingRequired()) {
            if (sampleSize < tempSampleSize) {
               sampleSize = tempSampleSize * samplingAlgorithmStaticInput.getFloorMultiplicationFactor();
            }
         }
      }
      return sampleSize;
   }

   private boolean isSampleSizeConsiderable (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            double population, double sampleSize) {
      boolean isConsiderable = false;
      // Rule 4: If the sample size is zero or less do not consider it for any
      // calculations
      // Rule 5: If sample size is more than defined percentage of Population,
      // then do not consider it for any calculations
      if ((long) sampleSize > 0
               && sampleSize < (population * samplingAlgorithmStaticInput.getMaxSamplePercentageOfPopulation())) {
         isConsiderable = true;
      }
      return isConsiderable;
   }

   private double getDefaultSampleSize (BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            double population) {
      double sampleSize = 0;
      if (population > 0) {
         double naturalLogOfPopulation = Math.log((double) population);
         sampleSize = (samplingAlgorithmStaticInput.getLdConstantOne() * naturalLogOfPopulation)
                  - samplingAlgorithmStaticInput.getLdConstantTwo();
      }
      if (sampleSize < 0) {
         sampleSize = 0;
      }
      return sampleSize;
   }

   public Long populateSliceCount (BasicSamplingAlgorithmInput samplingAlgorithmInput,
            BasicSamplingAlgorithmStaticInput samplingAlgorithmStaticInput) throws SamplingAlgorithmException {
      List<Double> sampleSizes = new ArrayList<Double>();
      for (MeasureStatInfo measureStatInfo : samplingAlgorithmInput.getMeasureStatInfo()) {
         sampleSizes.add(getSampleSize(samplingAlgorithmStaticInput, samplingAlgorithmInput.getPopulation(),
                  measureStatInfo.getStddevValue(), measureStatInfo.getMeanValue()));
      }
      return getGeometricMeanBasedSliceCount(samplingAlgorithmStaticInput, samplingAlgorithmInput.getPopulation(),
               sampleSizes);
   }

}
