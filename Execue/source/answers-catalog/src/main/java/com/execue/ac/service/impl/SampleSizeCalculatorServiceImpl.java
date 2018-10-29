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
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.ac.bean.ConceptColumnMapping;
import com.execue.ac.bean.MartFractionalDataSetTempTableStructure;
import com.execue.ac.bean.MeasureStatInfo;
import com.execue.ac.bean.SamplingAlgorithmInput;
import com.execue.ac.bean.SamplingAlgorithmStaticInput;
import com.execue.ac.bean.SourceAssetMappingInfo;
import com.execue.ac.exception.AnswersCatalogExceptionCodes;
import com.execue.ac.exception.SamplingAlgorithmException;
import com.execue.ac.service.ISampleSizeCalculatorService;
import com.execue.ac.util.AnswersCatalogUtil;
import com.execue.ac.util.SampleSizeFormulaUtil;
import com.execue.core.common.bean.ExeCueResultSet;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.querygen.ConditionEntity;
import com.execue.core.common.bean.querygen.Query;
import com.execue.core.common.bean.querygen.QueryGenerationInput;
import com.execue.core.common.bean.querygen.QueryGenerationOutput;
import com.execue.core.common.bean.querygen.QueryRepresentation;
import com.execue.core.common.bean.querygen.SelectEntity;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.StatType;
import com.execue.core.common.util.ExecueBeanCloneUtil;
import com.execue.core.common.util.ExecueBeanManagementUtil;
import com.execue.dataaccess.clientsource.IClientSourceDAO;
import com.execue.querygen.service.IQueryGenerationService;
import com.execue.querygen.service.QueryGenerationServiceFactory;


/**
 * This service contains the sampling algorithm.
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class SampleSizeCalculatorServiceImpl implements ISampleSizeCalculatorService {

   private static final Logger log = Logger.getLogger(SampleSizeCalculatorServiceImpl.class);
   private static final double LOWEST_COMPLIANT_MEAN_VALUE = 0.001d; // TODO make this configurable
   private static final double LOWEST_RANGE_RATIO_FOR_CLUSTERS = 0.01d; // TODO make this configurable
   private static final double LOWEST_COMPLIANT_STD_DEV_FOR_CLUSTERS = 0.01d; // TODO make this configurable
   
   private IClientSourceDAO    clientSourceDAO;

   @Override
   public Double calculateSampleSizePercentage (SamplingAlgorithmInput samplingAlgorithmInput,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput) throws SamplingAlgorithmException {
      List<Double> sampleSizes = new ArrayList<Double>();
      SourceAssetMappingInfo sourceAssetMappingInfo = samplingAlgorithmInput.getMartCreationInputInfo()
               .getMartCreationPopulatedContext().getSourceAssetMappingInfo();
      List<ConceptColumnMapping> populatedProminentMeasures = sourceAssetMappingInfo.getPopulatedProminentMeasures();
      // calculate sample size for each metric within the island using different strategies.
      // take the max of the sample size, before that convert them to percentage to the total population
      Long totalPopulation = samplingAlgorithmInput.getPopulation();
      int index = 0;
      for (MeasureStatInfo measureStatInfo : samplingAlgorithmInput.getMeasureStatInfo()) {
         ConceptColumnMapping matchedMeasureConceptColumnMapping = populatedProminentMeasures.get(index++);
         if (log.isDebugEnabled()) {
            log.debug("Concept Name : " + matchedMeasureConceptColumnMapping.getConcept().getName());
         }
         Double sampleSize = 0d;
         switch (matchedMeasureConceptColumnMapping.getConcept().getDataSamplingStrategy()) {
            case MEAN:
               sampleSize = calculateSampleSizeUsingMeanSamplingStrategy(samplingAlgorithmStaticInput, measureStatInfo
                        .getStddevValue(), measureStatInfo.getMeanValue());
               break;
            case PROPORTION:
               sampleSize = calculateSampleSizeUsingProportionSamplingStrategy(samplingAlgorithmStaticInput,
                        measureStatInfo.getMeanValue(), totalPopulation);
               break;
            case SUB_GROUPING:
               sampleSize = calculateSampleSizeUsingSubGroupingSamplingStrategy(samplingAlgorithmInput,
                        samplingAlgorithmStaticInput, measureStatInfo, matchedMeasureConceptColumnMapping);
               break;
            case CATEGORICAL:
               sampleSize = calculateSampleSizeUsingCategoricalSamplingStrategy();
               break;
         }
         if (log.isDebugEnabled()) {
            log.debug("Sample Size Value : " + sampleSize);
         }
         sampleSizes.add(calculateSampleSizePercentage(sampleSize, totalPopulation, samplingAlgorithmStaticInput));
      }
      if (log.isDebugEnabled()) {
         log.debug("Sample Size %s : " + sampleSizes);
      }
      Double maxSampleSizePercentage = pickMaxSampleSizePercentage(sampleSizes);
      if (log.isDebugEnabled()) {
         log.debug("Maximum Sample Size % : " + maxSampleSizePercentage);
      }
      return maxSampleSizePercentage;
   }

   private Double calculateSampleSizePercentage (Double sampleSize, Long totalPopulation,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput) {
      Double sampleSizePercentage = sampleSize / totalPopulation * 100;
      sampleSizePercentage = alterSampleSizeForOverAndUnderLimit(sampleSizePercentage, samplingAlgorithmStaticInput);
      return sampleSizePercentage;

   }

   private Double alterSampleSizeForOverAndUnderLimit (Double sampleSizePercentage,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput) {
      Double altertedSampleSizePercentage = sampleSizePercentage;
      if (sampleSizePercentage == 0) {
         altertedSampleSizePercentage = samplingAlgorithmStaticInput.getMinSamplePercentageOfPopulation();
      } else if (sampleSizePercentage > samplingAlgorithmStaticInput.getMaxSamplePercentageOfPopulationAllowed()) {
         altertedSampleSizePercentage = samplingAlgorithmStaticInput.getMaxSamplePercentageOfPopulationAllowed();
      }
      return altertedSampleSizePercentage;
   }

   private Double pickMaxSampleSizePercentage (List<Double> sampleSizePercentages) {
      Collections.sort(sampleSizePercentages);
      return sampleSizePercentages.get(sampleSizePercentages.size() - 1);
   }

   private Double calculateSampleSizeUsingMeanSamplingStrategy (
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput, Double stddevValue, Double meanValue) {
      return SampleSizeFormulaUtil.calculateSampleSizeUsingMeanSamplingStrategy(samplingAlgorithmStaticInput
               .getZValue(), samplingAlgorithmStaticInput.getErrorRateValue(), stddevValue, meanValue);
   }
   
   private Double calculateSampleSizeUsingSubgroupingSamplingStrategy (
           SamplingAlgorithmStaticInput samplingAlgorithmStaticInput, SubGroupStatInfo subGroupStatInfo) {
     double sampleSize = SampleSizeFormulaUtil.calculateSampleSizeUsingMeanSamplingStrategy(samplingAlgorithmStaticInput
              .getZValue(), samplingAlgorithmStaticInput.getErrorRateValue(), subGroupStatInfo.getStddevValue(), subGroupStatInfo.getMeanValue());
     if(sampleSize > (double)subGroupStatInfo.getCountValue()) {
    	 //TODO replace this logic with population adjustment formula, until then using the current sub group count as the sample size
    	 sampleSize = (double)subGroupStatInfo.getCountValue();
     }
     return sampleSize;
  }
   
   private Double calculateDefaultSampleSizeForSubgroupSamplingStrategy (
           SamplingAlgorithmStaticInput samplingAlgorithmStaticInput, Long population, int numberOfSubGroups) {
     double sampleSize;
         //calculate the default sample size of applying this formula
		//default sample size for sub group = MaxSamplePercentageOfPopulationAllowed * population of the group) / Number of Subgroups
		sampleSize = (samplingAlgorithmStaticInput.getMaxSamplePercentageOfPopulationAllowed()*population)/(100*numberOfSubGroups);
     return sampleSize;
  }

   private Double calculateSampleSizeUsingProportionSamplingStrategy (
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput, Double meanValue, Long totalPopulation) {
      return SampleSizeFormulaUtil.calculateSampleSizeUsingProportionSamplingStrategy(samplingAlgorithmStaticInput
               .getZValue(), samplingAlgorithmStaticInput.getErrorRateValue(), meanValue, totalPopulation);
   }

   private Double calculateSampleSizeUsingCategoricalSamplingStrategy () {
      return 0d;
   }

   // create max of 16 groups by checking if every group is valid or not.
   // try to merge them on conditions
   // once done, go for mean for each of the subgroup left and then all up all the sample sizes
   private Double calculateSampleSizeUsingSubGroupingSamplingStrategy (SamplingAlgorithmInput samplingAlgorithmInput,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput, MeasureStatInfo measureStatInfo,
            ConceptColumnMapping matchedMeasureConceptColumnMapping) throws SamplingAlgorithmException {
      Double sampleSize = 0d;
      try {
         // build the conditions needed for each subgrouping query
         List<ConditionEntity> dimensionAndDistributionConditionEntities = prepareDimensionAndDistributionConditionEntities(samplingAlgorithmInput);
         // get first group as list
         List<SubGroupStatInfo> subGroupStatInfoList = buildSubGroupStatInfo(measureStatInfo, samplingAlgorithmInput
                  .getPopulation());
         int level = 1;
         // 5 levels means max of 16 subgroups
         while (level++ < 5) {
            if (log.isDebugEnabled()) {
               log.debug("Level " + (level - 1) + " : Sub Groups ["+subGroupStatInfoList.size()+"] : " + subGroupStatInfoList);
            }
            // get the max of two subgroups based on the split
            // if both the new subgriups are invalid, we should get the original group itself
            List<SubGroupStatInfo> splittedSubGroups = splitSubGroups(subGroupStatInfoList, samplingAlgorithmInput,
                     samplingAlgorithmStaticInput, matchedMeasureConceptColumnMapping,
                     dimensionAndDistributionConditionEntities);
            if (log.isDebugEnabled()) {
               log.debug("Level " + (level - 1) + " : Splitted Groups ["+splittedSubGroups.size()+"] : " + splittedSubGroups);
            }
            // if previous group list and this list are same, it means we cannot split any further
            if (isSubGroupStatInfoListsSame(subGroupStatInfoList, splittedSubGroups)) {
               if (log.isDebugEnabled()) {
                  log.debug("Level " + (level - 1) + " : Previous and Current Groups are same");
               }
               break;
            }
            subGroupStatInfoList = splittedSubGroups;
         }
         if (log.isDebugEnabled()) {
            log.debug("Leftout Sub Groups ["+subGroupStatInfoList.size()+"] : " + subGroupStatInfoList);
         }
         // merge the subgroups left
         if (subGroupStatInfoList.size() > 1) {
            subGroupStatInfoList = mergeSubGroups(samplingAlgorithmInput.getMartCreationInputInfo()
                     .getMartCreationPopulatedContext().getSourceAsset(), matchedMeasureConceptColumnMapping,
                     dimensionAndDistributionConditionEntities, samplingAlgorithmStaticInput, subGroupStatInfoList);
            if (log.isDebugEnabled()) {
               log.debug("Merged Sub Groups ["+subGroupStatInfoList.size()+"] : " + subGroupStatInfoList);
            }
         }
         // calculate sample size per group and add all the sample sizes
         for (SubGroupStatInfo subGroupStatInfo : subGroupStatInfoList) {
        	 
        	if(isMinimumCompliantValidDistribution(measureStatInfo, subGroupStatInfo)) {
        		sampleSize += calculateSampleSizeUsingSubgroupingSamplingStrategy(samplingAlgorithmStaticInput, subGroupStatInfo);
        		
        	} else {
        		sampleSize += calculateDefaultSampleSizeForSubgroupSamplingStrategy(samplingAlgorithmStaticInput,subGroupStatInfo.getCountValue(),subGroupStatInfoList.size() );
        		if (log.isDebugEnabled()) {
        			log.debug("Applied Default Sample Size ..");
        		}
        	}
    		if (log.isDebugEnabled()) {
    			log.debug("Cumulative Sample Size : "+sampleSize);
    		}
         }
      } catch (Exception e) {
         throw new SamplingAlgorithmException(AnswersCatalogExceptionCodes.DEFAULT_ANSWERS_CATALOG_EXCEPTION_CODE, e);
      }
      return sampleSize;
   }
   
   //under this method, add all boundary and exception conditions for handling population distributions that are not normal
   //case 1) Mean value is very very low 
   //case 2) Range value of the sub group is < 1% of the total range of the variable
   //add more..
   //
   private boolean isMinimumCompliantValidDistribution(MeasureStatInfo measureStatInfo, SubGroupStatInfo subGroupStatInfo) {
	   double measure_range   = measureStatInfo.getMaxValue() - measureStatInfo.getMinValue();
	   double sub_group_range = subGroupStatInfo.getMaxValue() - subGroupStatInfo.getMinValue();
	   if(Math.abs(subGroupStatInfo.getMeanValue()) < LOWEST_COMPLIANT_MEAN_VALUE) {
		   return false;
	   } 
	   
	   double sub_group_range_ratio = (sub_group_range/measure_range);
	   
	   if(Math.abs(sub_group_range_ratio) < LOWEST_RANGE_RATIO_FOR_CLUSTERS && Math.abs(subGroupStatInfo.getStddevValue()) < LOWEST_COMPLIANT_STD_DEV_FOR_CLUSTERS) {
		   return false;
	   }
	   return true;
   }

   private boolean isSubGroupStatInfoListsSame (List<SubGroupStatInfo> subGroupsList1,
            List<SubGroupStatInfo> subGroupsList2) {
      return subGroupsList1.size() == subGroupsList2.size() && subGroupsList1.containsAll(subGroupsList2);
   }

   // merge only the continous ones and wherever merge happens continue from that point again for merging
   private List<SubGroupStatInfo> mergeSubGroups (Asset sourceAsset,
            ConceptColumnMapping matchedMeasureConceptColumnMapping,
            List<ConditionEntity> dimensionAndDistributionConditionEntities,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput, List<SubGroupStatInfo> subGroups)
            throws Exception {
      List<SubGroupStatInfo> mergedSubGroups = new ArrayList<SubGroupStatInfo>();
      mergedSubGroups.addAll(subGroups);
      int startIndex = 0;
      Integer mergedIndex = null;
      do {
         // get the first index of the merged subgroups continuous
         mergedIndex = populateMergedIndex(startIndex, subGroups, samplingAlgorithmStaticInput, sourceAsset,
                  matchedMeasureConceptColumnMapping, dimensionAndDistributionConditionEntities);
         if (mergedIndex != null) {
            // create a merged subgroup out of it
            SubGroupStatInfo mergedSubGroup = buildMergedSubGroup(sourceAsset, matchedMeasureConceptColumnMapping,
                     dimensionAndDistributionConditionEntities, subGroups.get(mergedIndex), subGroups
                              .get(mergedIndex + 1));
            // start from this group next time
            startIndex = mergedIndex;
            // remove two subgroups
            subGroups.remove(mergedIndex.intValue());
            subGroups.remove(mergedIndex.intValue());
            // add at this location new merged subgroup
            subGroups.add(mergedIndex, mergedSubGroup);
         }
      } while (mergedIndex != null);
      // keep on executing till nothing can be merged.

      return mergedSubGroups;
   }

   private Integer populateMergedIndex (int startIndex, List<SubGroupStatInfo> subGroups,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput, Asset sourceAsset,
            ConceptColumnMapping matchedMeasureConceptColumnMapping,
            List<ConditionEntity> dimensionAndDistributionConditionEntities) throws Exception {
      Integer mergedIndex = null;
      // check which two consecutive groups can be merged together
      for (int index = startIndex; index < subGroups.size() - 1; index++) {
         //if (canBeMerged(subGroups.get(index), subGroups.get(index + 1), samplingAlgorithmStaticInput)) {
         // NOTE: -RG- Above method is not as per the document
         if (canBeMergedAsPerDoc(subGroups.get(index), subGroups.get(index + 1), samplingAlgorithmStaticInput,
                  sourceAsset, matchedMeasureConceptColumnMapping, dimensionAndDistributionConditionEntities)) {
            mergedIndex = index;
            break;
         }
      }
      return mergedIndex;
   }

   private SubGroupStatInfo buildMergedSubGroup (Asset sourceAsset,
            ConceptColumnMapping matchedMeasureConceptColumnMapping,
            List<ConditionEntity> dimensionAndDistributionConditionEntities, SubGroupStatInfo subGroupStatInfo1,
            SubGroupStatInfo subGroupStatInfo2) throws Exception {
      return buildSubGroup(sourceAsset, matchedMeasureConceptColumnMapping, dimensionAndDistributionConditionEntities,
               subGroupStatInfo1.getMinValue(), subGroupStatInfo2.getMaxValue(), true, true);

   }

   /**
    * This is not true to the document, so kept un-used
    * 
    * @param subGroupStatInfo1
    * @param subGroupStatInfo2
    * @param samplingAlgorithmStaticInput
    * @return
    */
   private boolean canBeMerged (SubGroupStatInfo subGroupStatInfo1, SubGroupStatInfo subGroupStatInfo2,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput) {
      Double firstGroupVarianceValue = Math.abs(subGroupStatInfo1.getVarianceValue());
      Double secondGroupVarianceValue = Math.abs(subGroupStatInfo2.getVarianceValue());
      // if the diff between two groups stddev is less than defined they can be merged
      return firstGroupVarianceValue - secondGroupVarianceValue < samplingAlgorithmStaticInput
               .getSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups()
               || secondGroupVarianceValue - firstGroupVarianceValue < samplingAlgorithmStaticInput
                        .getSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups();
   }

   private boolean canBeMergedAsPerDoc (SubGroupStatInfo firstSubGroupStatInfo,
            SubGroupStatInfo secondSubGroupStatInfo, SamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            Asset sourceAsset, ConceptColumnMapping matchedMeasureConceptColumnMapping,
            List<ConditionEntity> dimensionAndDistributionConditionEntities) throws Exception {
      boolean canBeMerged = false;
      SubGroupStatInfo tempMergedSubGroup = buildMergedSubGroup(sourceAsset, matchedMeasureConceptColumnMapping,
               dimensionAndDistributionConditionEntities, firstSubGroupStatInfo, secondSubGroupStatInfo);
      if (log.isDebugEnabled()) {
         log.debug("Temporarily Merged Sub Group : " + tempMergedSubGroup);
      }
      if (tempMergedSubGroup.getVarianceValue() < samplingAlgorithmStaticInput
               .getSubGroupingMaxCoefficientOfVarianceAllowedBetweenTwoGroups()) {
         canBeMerged = true;
      }
      if (log.isDebugEnabled()) {
         log.debug("Sub Group Merge Status : " + canBeMerged);
      }
      return canBeMerged;
   }

   private List<SubGroupStatInfo> buildSubGroupStatInfo (MeasureStatInfo measureStatInfo, Long totalPopulation) {
      Double varianceValue = SampleSizeFormulaUtil.calculateVariance(measureStatInfo.getStddevValue(), measureStatInfo
               .getMeanValue());
      List<SubGroupStatInfo> subGroups = new ArrayList<SubGroupStatInfo>();
      subGroups.add(new SubGroupStatInfo(measureStatInfo.getMinValue(), measureStatInfo.getMaxValue(), totalPopulation,
               measureStatInfo.getMeanValue(), measureStatInfo.getStddevValue(), varianceValue));
      return subGroups;
   }

   private List<ConditionEntity> prepareDimensionAndDistributionConditionEntities (
            SamplingAlgorithmInput samplingAlgorithmInput) {
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      List<String> distributionValues = samplingAlgorithmInput.getDistributionValues();
      String dimensionValue = samplingAlgorithmInput.getDimensionValue();
      MartFractionalDataSetTempTableStructure fractionalDataSetTempTable = samplingAlgorithmInput
               .getFractionalDataSetTempTable();
      SelectEntity dimensionSelectEntity = fractionalDataSetTempTable.getDimensionSelectEntity();
      ConditionEntity dimensionConditionEntity = AnswersCatalogUtil.prepareConditionEntity(dimensionSelectEntity
               .getTableColumn().getTable(), dimensionSelectEntity.getTableColumn().getColumn(), dimensionValue);
      conditionEntities.add(dimensionConditionEntity);
      List<ConditionEntity> distributionConditionEntities = new ArrayList<ConditionEntity>();
      int index = 0;
      for (SelectEntity distributionSelectEntity : fractionalDataSetTempTable.getDistributionSelectEntities()) {
         distributionConditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(distributionSelectEntity
                  .getTableColumn().getTable(), distributionSelectEntity.getTableColumn().getColumn(),
                  distributionValues.get(index++)));
      }
      conditionEntities.addAll(distributionConditionEntities);
      return conditionEntities;
   }

   private IQueryGenerationService getQueryGenerationService (AssetProviderType providerType) {
      return QueryGenerationServiceFactory.getInstance().getQueryGenerationService(providerType);
   }

   private boolean isSubGroupValid (SubGroupStatInfo subGroupStatInfo, Long totalPopulation,
            SamplingAlgorithmStaticInput samplingAlgorithmStaticInput) {
      // rule 1. if population is less than required population
      // rule 2. if mean is zero
      // rule 3. if coefficient of variance is less than 0.3
      // if any of the above rules are true, then it means it is not a valid group
      boolean isSubGroupValid = true;
      Long subGroupPopulation = subGroupStatInfo.getCountValue();
      Double subGroupPopulationPercentage = subGroupPopulation / totalPopulation.doubleValue() * 100.0;
      if (subGroupPopulationPercentage < samplingAlgorithmStaticInput.getSubGroupingMinPopulationPercentageRequired()
               || subGroupStatInfo.getMeanValue() == 0
               || Math.abs(subGroupStatInfo.getVarianceValue()) < samplingAlgorithmStaticInput
                        .getSubGroupingMaxCoefficientOfVarianceAllowed()) {
         isSubGroupValid = false;
      }
      if (log.isDebugEnabled()) {
         log.debug("Sub Group Population against Total Population ["+totalPopulation+"] : " + subGroupPopulationPercentage);
         log.debug("Validity of the Sub Group : " + isSubGroupValid);
      }
      return isSubGroupValid;
   }

   private List<SubGroupStatInfo> splitSubGroups (List<SubGroupStatInfo> subGroups,
            SamplingAlgorithmInput samplingAlgorithmInput, SamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            ConceptColumnMapping matchedMeasureConceptColumnMapping,
            List<ConditionEntity> dimensionAndDistributionConditionEntities) throws Exception {
      List<SubGroupStatInfo> splittedSubGroups = new ArrayList<SubGroupStatInfo>();
      for (SubGroupStatInfo subGroupStatInfo : subGroups) {
         // if splitting further is not allowed then keep the same sub group as is in the list
         if (!subGroupStatInfo.isSubGroupingAllowed()) {
            splittedSubGroups.add(subGroupStatInfo);
         } else {
            // for each of the group get the subgroups
            List<SubGroupStatInfo> subGroupsList = getSubGroups(subGroupStatInfo, samplingAlgorithmInput,
                     samplingAlgorithmStaticInput, matchedMeasureConceptColumnMapping,
                     dimensionAndDistributionConditionEntities);
            splittedSubGroups.addAll(subGroupsList);
         }
      }
      return splittedSubGroups;
   }

   private List<SubGroupStatInfo> getSubGroups (SubGroupStatInfo subGroupStatInfo,
            SamplingAlgorithmInput samplingAlgorithmInput, SamplingAlgorithmStaticInput samplingAlgorithmStaticInput,
            ConceptColumnMapping matchedMeasureConceptColumnMapping,
            List<ConditionEntity> dimensionAndDistributionConditionEntities) throws Exception {
      Asset sourceAsset = samplingAlgorithmInput.getMartCreationInputInfo().getMartCreationPopulatedContext()
               .getSourceAsset();
      SubGroupStatInfo firstSubGroup = buildSubGroup(sourceAsset, matchedMeasureConceptColumnMapping,
               dimensionAndDistributionConditionEntities, subGroupStatInfo.getMinValue(), subGroupStatInfo
                        .getMeanValue(), true, true);
      SubGroupStatInfo secondSubGroup = buildSubGroup(sourceAsset, matchedMeasureConceptColumnMapping,
               dimensionAndDistributionConditionEntities, subGroupStatInfo.getMeanValue(), subGroupStatInfo
                        .getMaxValue(), false, true);
      List<SubGroupStatInfo> subGroups = new ArrayList<SubGroupStatInfo>();
      if (!isSubGroupValid(firstSubGroup, samplingAlgorithmInput.getPopulation(), samplingAlgorithmStaticInput)) {
         firstSubGroup.setSubGroupingAllowed(false);
      }
      if (!isSubGroupValid(secondSubGroup, samplingAlgorithmInput.getPopulation(), samplingAlgorithmStaticInput)) {
         secondSubGroup.setSubGroupingAllowed(false);
      }
      subGroups.add(firstSubGroup);
      subGroups.add(secondSubGroup);
      if (log.isDebugEnabled()) {
         log.debug("First Sub Group : " + firstSubGroup + ", Further Sub Groups Allowed : "+firstSubGroup.isSubGroupingAllowed());
         log.debug("Second Sub Group : " + secondSubGroup + ", Further Sub Groups Allowed : "+secondSubGroup.isSubGroupingAllowed());
      }
      
      return subGroups;
   }

   private SubGroupStatInfo buildSubGroup (Asset sourceAsset, ConceptColumnMapping matchedMeasureConceptColumnMapping,
            List<ConditionEntity> dimensionAndDistributionConditionEntities, Double minValue, Double maxValue,
            boolean minValueInclusive, boolean maxValueInclusive) throws Exception {
      SelectEntity dummySelectEntity = ExecueBeanManagementUtil
               .prepareDummySelectEntity(matchedMeasureConceptColumnMapping.getQueryTableColumn());
      List<SelectEntity> selectEntities = new ArrayList<SelectEntity>();
      selectEntities.add(prepareStatBasedSelectEntity(dummySelectEntity, StatType.STDDEV));
      selectEntities.add(prepareStatBasedSelectEntity(dummySelectEntity, StatType.AVERAGE));
      selectEntities.add(prepareStatBasedSelectEntity(dummySelectEntity, StatType.MINIMUM));
      selectEntities.add(prepareStatBasedSelectEntity(dummySelectEntity, StatType.MAXIMUM));
      selectEntities.add(prepareStatBasedSelectEntity(dummySelectEntity, StatType.COUNT));
      List<ConditionEntity> conditionEntities = new ArrayList<ConditionEntity>();
      conditionEntities.addAll(dimensionAndDistributionConditionEntities);
      OperatorType minValueOperatorType = OperatorType.GREATER_THAN;
      OperatorType maxValueOperatorType = OperatorType.LESS_THAN;
      if (minValueInclusive) {
         minValueOperatorType = OperatorType.GREATER_THAN_EQUALS;
      }
      if (maxValueInclusive) {
         maxValueOperatorType = OperatorType.LESS_THAN_EQUALS;
      }
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(dummySelectEntity.getTableColumn().getTable(),
               dummySelectEntity.getTableColumn().getColumn(), minValue.toString(), minValueOperatorType));
      conditionEntities.add(AnswersCatalogUtil.prepareConditionEntity(dummySelectEntity.getTableColumn().getTable(),
               dummySelectEntity.getTableColumn().getColumn(), maxValue.toString(), maxValueOperatorType));
      Query query = new Query();
      query.setSelectEntities(selectEntities);
      query.setWhereEntities(conditionEntities);
      List<Query> queries = new ArrayList<Query>();
      queries.add(query);
      QueryGenerationInput queryGenerationInput = new QueryGenerationInput();
      queryGenerationInput.setInputQueries(queries);
      queryGenerationInput.setTargetAsset(sourceAsset);
      IQueryGenerationService queryGenerationService = getQueryGenerationService(sourceAsset.getDataSource()
               .getProviderType());
      QueryGenerationOutput queryGenerationOutput = queryGenerationService.generateQuery(queryGenerationInput);
      QueryRepresentation queryRepresentation = queryGenerationService.extractQueryRepresentation(sourceAsset,
               queryGenerationOutput.getResultQuery());
      ExeCueResultSet resultSet = getClientSourceDAO().executeQuery(sourceAsset.getDataSource(), query,
               queryRepresentation.getQueryString());
      // move to record
      resultSet.next();
      int resultSetIndex = 0;
      Double stddevValue = resultSet.getDouble(resultSetIndex++);
      if (stddevValue == null) {
         stddevValue = 0D;
      }
      Double meanValue = resultSet.getDouble(resultSetIndex++);
      if (meanValue == null) {
         meanValue = 0D;
      }
      Double minimumValue = resultSet.getDouble(resultSetIndex++);
      if (minimumValue == null) {
         minimumValue = 0D;
      }
      Double maximumValue = resultSet.getDouble(resultSetIndex++);
      if (maximumValue == null) {
         maximumValue = 0D;
      }
      Long countValue = resultSet.getLong(resultSetIndex++);
      if (countValue == null) {
         countValue = 0L;
      }
      Double varianceValue = SampleSizeFormulaUtil.calculateVariance(stddevValue, meanValue);

      return new SubGroupStatInfo(minimumValue, maximumValue, countValue, meanValue, stddevValue, varianceValue);
   }

   private SelectEntity prepareStatBasedSelectEntity (SelectEntity selectEntity, StatType statType)
            throws CloneNotSupportedException {
      SelectEntity clonedSelectEntity = ExecueBeanCloneUtil.cloneSelectEntity(selectEntity);
      clonedSelectEntity.setFunctionName(statType);
      clonedSelectEntity
               .setAlias(clonedSelectEntity.getTableColumn().getColumn().getColumnName() + statType.getValue());
      return clonedSelectEntity;
   }

   class SubGroupStatInfo {

      private Double  minValue;
      private Double  maxValue;
      private Long    countValue;
      private Double  meanValue;
      private Double  stddevValue;
      private Double  varianceValue;
      private boolean subGroupingAllowed = true;

      public SubGroupStatInfo (Double minValue, Double maxValue, Long countValue, Double meanValue, Double stddevValue,
               Double varianceValue) {
         super();
         this.minValue = minValue;
         this.maxValue = maxValue;
         this.countValue = countValue;
         this.meanValue = meanValue;
         this.stddevValue = stddevValue;
         this.varianceValue = varianceValue;
      }

      public Long getCountValue () {
         return countValue;
      }

      public void setCountValue (Long countValue) {
         this.countValue = countValue;
      }

      public Double getMeanValue () {
         return meanValue;
      }

      public void setMeanValue (Double meanValue) {
         this.meanValue = meanValue;
      }

      public Double getStddevValue () {
         return stddevValue;
      }

      public void setStddevValue (Double stddevValue) {
         this.stddevValue = stddevValue;
      }

      public Double getVarianceValue () {
         return varianceValue;
      }

      public void setVarianceValue (Double varianceValue) {
         this.varianceValue = varianceValue;
      }

      public Double getMinValue () {
         return minValue;
      }

      public void setMinValue (Double minValue) {
         this.minValue = minValue;
      }

      public Double getMaxValue () {
         return maxValue;
      }

      public void setMaxValue (Double maxValue) {
         this.maxValue = maxValue;
      }

      public boolean isSubGroupingAllowed () {
         return subGroupingAllowed;
      }

      public void setSubGroupingAllowed (boolean subGroupingAllowed) {
         this.subGroupingAllowed = subGroupingAllowed;
      }

      @Override
      public boolean equals (Object obj) {
         return this.toString().equalsIgnoreCase(((SubGroupStatInfo) obj).toString());
      }

      @Override
      public String toString () {
         return "Min : " + minValue + ",Max : " + maxValue + ",Count : " + countValue + ",Mean : " + meanValue
                  + ",Stddev : " + stddevValue + ", Variance : " + varianceValue;
      }

      @Override
      public int hashCode () {
         return this.toString().hashCode();
      }

   }

   public IClientSourceDAO getClientSourceDAO () {
      return clientSourceDAO;
   }

   public void setClientSourceDAO (IClientSourceDAO clientSourceDAO) {
      this.clientSourceDAO = clientSourceDAO;
   }
}
