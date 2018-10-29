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
import java.util.List;

import com.execue.ac.bean.SlicingAlgorithmStaticInput;
import com.execue.ac.service.IMartTotalSlicesCalculatorService;
import com.execue.ac.exception.AnswersCatalogException;

/**
 * This service will populate the number of slices to be divided based on total records in population table.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartTotalSlicesCalculatorServiceImpl implements IMartTotalSlicesCalculatorService {

   /**
    * This method runs the heuristics to calculate the numberOfSlices and if it falls out of range then we take default
    * from configuration.
    * 
    * @param slicingAlgorithmStaticInput
    * @param totalPopulationRecordCount
    * @return numberOfSlices
    * @throws AnswersCatalogException
    */
   public Integer calculateTotalSlices (SlicingAlgorithmStaticInput slicingAlgorithmStaticInput,
            Long totalPopulationRecordCount) throws AnswersCatalogException {
      List<String> slicingEligiblityCriteriaRecords = slicingAlgorithmStaticInput.getSlicingEligiblityCriteriaRecords();
      List<String> slicingEligiblityCriteriaPercentages = slicingAlgorithmStaticInput
               .getSlicingEligiblityCriteriaPercentages();
      List<Long> slicingEligiblityLongCriteriaRecords = new ArrayList<Long>();
      // based on the record percentage information, calculate the percentage and check for the range of the percentage
      // defined.
      for (String recordCount : slicingEligiblityCriteriaRecords) {
         slicingEligiblityLongCriteriaRecords.add(Long.valueOf(recordCount).longValue());
      }
      List<Double> slicingEligiblityLongCriteriaPercentages = new ArrayList<Double>();
      for (String percentage : slicingEligiblityCriteriaPercentages) {
         slicingEligiblityLongCriteriaPercentages.add(Double.valueOf(percentage).doubleValue());
      }
      int counter = 0;
      Double percentage = null;
      for (Long recordCount : slicingEligiblityLongCriteriaRecords) {
         if (totalPopulationRecordCount < recordCount) {
            percentage = slicingEligiblityLongCriteriaPercentages.get(counter);
            break;
         }
         counter++;
      }
      if (percentage == null) {
         percentage = slicingEligiblityLongCriteriaPercentages.get(slicingEligiblityLongCriteriaPercentages.size() - 1);
      }
      Integer calculatedNumberOfSlices = Double.valueOf((percentage / 100.00) * totalPopulationRecordCount).intValue();
      Integer minSlices = slicingAlgorithmStaticInput.getMinSlices();
      Integer maxSlices = slicingAlgorithmStaticInput.getMaxSlices();
      Integer finalSlices = null;
      // if calculated slices are out of range take avg slices from configuration.
      if (calculatedNumberOfSlices >= minSlices && calculatedNumberOfSlices <= maxSlices) {
         finalSlices = calculatedNumberOfSlices;
      } else {
         Integer avgSlices = slicingAlgorithmStaticInput.getAvgSlices();
         finalSlices = avgSlices;
      }
      return finalSlices;
   }

}
