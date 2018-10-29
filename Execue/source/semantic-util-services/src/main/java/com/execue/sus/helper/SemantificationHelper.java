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


package com.execue.sus.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.entity.unstructured.Feature;
import com.execue.core.common.bean.entity.unstructured.FeatureRange;
import com.execue.core.common.bean.entity.unstructured.FeatureValue;
import com.execue.core.common.bean.entity.unstructured.SemantifiedContentFeatureInformation;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.LocationNormalizedData;
import com.execue.core.common.bean.nlp.LocationNormalizedDataEntity;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RangeNormalizedData;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.sdata.location.LocationPointInfo;
import com.execue.core.common.bean.sdata.location.StateCity;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.LocationType;
import com.execue.core.common.type.NormalizedLocationType;
import com.execue.core.common.util.NLPUtils;
import com.execue.core.util.ExecueStringUtil;
import com.execue.sdata.exception.LocationException;
import com.execue.sdata.service.ILocationRetrievalService;
import com.execue.uswh.exception.UnstructuredWarehouseException;
import com.execue.uswh.helper.UnstructuredWarehouseHelper;
import com.execue.uswh.service.IUnstructuredWarehouseRetrievalService;

/**
 * @author abhijit
 * @since 3/25/11 : 2:38 PM
 */
public class SemantificationHelper {

   private ReducedFormHelper                      reducedFormHelper;
   private IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService;
   private ILocationRetrievalService              locationRetrievalService;
   private UnstructuredWarehouseHelper            unstructuredWarehouseHelper;

   public static String removeUnwantedCharacterFromDescription (String shorDescription, String regex) {
      // Trim the html chars from the title
      String shortDescription = NewsUtil.getTrimmedForHTMLTags(shorDescription);
      // Trim the non ascii chars from the title
      shortDescription = ExecueStringUtil.replaceMatchedRegexWithInputString(shortDescription,
               ExecueStringUtil.NON_ASCII_CHAR_MATCH_REGEX, "");
      // Trim the repeating special chars from the title
      shortDescription = ExecueStringUtil.replaceMatchedRegexWithInputString(shortDescription, regex, "");
      return shortDescription;
   }

   public List<SemantifiedContentFeatureInformation> getSemantifiedContentFeaturesInformation (
            SemanticPossibility semanticPossibility, boolean fromSemantification) throws UnstructuredWarehouseException {
      Map<Long, Set<Long>> featureValueBedIdsByFeatureBedIdMap = new HashMap<Long, Set<Long>>(1);
      Map<Long, List<Object[]>> featureOperatorValueByFeatureBedIdMap = new HashMap<Long, List<Object[]>>(1);

      parseReducedFormForValueAndNumber(semanticPossibility, featureValueBedIdsByFeatureBedIdMap,
               featureOperatorValueByFeatureBedIdMap, fromSemantification);

      Long applicationId = semanticPossibility.getApplication().getId();
      List<SemantifiedContentFeatureInformation> semantifiedContentFeaturesInfo = new ArrayList<SemantifiedContentFeatureInformation>(
               1);

      // Add the dummy feature content information
      semantifiedContentFeaturesInfo.add(getUnstructuredWarehouseHelper().getDummySemantifiedContentFeatureInfo(
               applicationId));

      // Add the range feature content information
      if (!MapUtils.isEmpty(featureOperatorValueByFeatureBedIdMap)) {
         List<FeatureRange> featureRanges = new ArrayList<FeatureRange>(1);
         Set<Long> featureBedIds = featureOperatorValueByFeatureBedIdMap.keySet();
         List<Feature> features = getUnstructuredWarehouseRetrievalService().getFeaturesByContextIdAndFeatureBedIds(
                  applicationId, featureBedIds);
         for (Feature featureNumber : features) {

            List<Object[]> featureOperatorValues = featureOperatorValueByFeatureBedIdMap.get(featureNumber
                     .getFeatureBedId());
            for (Object[] operatorValues : featureOperatorValues) {
               FeatureRange featureRange = new FeatureRange();
               featureRange.setFeatureId(featureNumber.getFeatureId());
               featureRange.setFeatureStartOperator((String) operatorValues[0]);
               featureRange.setStartValue((Double) operatorValues[1]);
               if (operatorValues.length > 2) {
                  featureRange.setFeatureEndOperator((String) operatorValues[2]);
                  featureRange.setEndValue((Double) operatorValues[3]);
                  featureRange.setRangeType(CheckType.YES);
               } else {
                  featureRange.setRangeType(CheckType.NO);
               }
               featureRanges.add(featureRange);
            }
         }
         semantifiedContentFeaturesInfo.addAll(getUnstructuredWarehouseHelper()
                  .populateSemantifiedContentRangeFeatureInformation(featureRanges, applicationId));
      }

      // Add the value feature content information
      if (!MapUtils.isEmpty(featureValueBedIdsByFeatureBedIdMap)) {
         Set<Long> featureBedIds = featureValueBedIdsByFeatureBedIdMap.keySet();
         List<Feature> features = getUnstructuredWarehouseRetrievalService().getFeaturesByContextIdAndFeatureBedIds(
                  applicationId, featureBedIds);
         Set<Long> featureValueBedIds = getValidFeatureValueBedIds(features, featureValueBedIdsByFeatureBedIdMap,
                  fromSemantification);
         List<FeatureValue> featureValues = getUnstructuredWarehouseRetrievalService().getFeatureValues(applicationId,
                  featureValueBedIds);
         semantifiedContentFeaturesInfo.addAll(getUnstructuredWarehouseHelper()
                  .populateSemantifiedContentValueFeatureInformation(featureValues, features, applicationId));
      }

      return semantifiedContentFeaturesInfo;
   }

   private Set<Long> getValidFeatureValueBedIds (List<Feature> featureStrings,
            Map<Long, Set<Long>> featureValueBedIdsByFeatureIdMap, boolean fromSemantification) {
      Set<Long> validFeatureValueBedIds = new HashSet<Long>();
      for (Feature feature : featureStrings) {
         Set<Long> featureValueBedIds = featureValueBedIdsByFeatureIdMap.get(feature.getFeatureBedId());
         if (featureValueBedIds.size() == 1) {
            validFeatureValueBedIds.addAll(featureValueBedIds);
            continue;
         }
         // Pick only one feature value bed id, if the feature is single valued and is from semantification
         if (feature.getMultiValued() == CheckType.NO && fromSemantification) {
            validFeatureValueBedIds.add(featureValueBedIds.iterator().next());
            continue;
         }
         validFeatureValueBedIds.addAll(featureValueBedIds);
      }
      return validFeatureValueBedIds;
   }

   public List<LocationPointInfo> getLocationPointInformations (SemanticPossibility semanticPossibility,
            boolean returnBestLocationInfo) throws LocationException {
      List<LocationPointInfo> locationPointInfos = new ArrayList<LocationPointInfo>();
      if (semanticPossibility == null) {
         return locationPointInfos;
      }
      Map<DomainRecognition, IGraphPath> pathsByLocationDomainRecognition = getLocationDomainRecognitions(semanticPossibility);

      List<DomainRecognition> locationDomainRecs = new ArrayList<DomainRecognition>();
      locationDomainRecs.addAll(pathsByLocationDomainRecognition.keySet());
      if (CollectionUtils.isEmpty(locationDomainRecs)) {
         return locationPointInfos;
      }

      // If only one location recognition exists, then just validate it and get the location point information 
      if (locationDomainRecs.size() == 1 || !returnBestLocationInfo) {
         for (DomainRecognition domainRecognition : locationDomainRecs) {
            locationPointInfos.addAll(getValidLocationInfos(domainRecognition, semanticPossibility,
                     pathsByLocationDomainRecognition, returnBestLocationInfo));
         }
         return locationPointInfos;
      }

      // Group the domain recognitions by sentence id
      TreeMap<Integer, List<DomainRecognition>> sortedDomainRecBySentencedId = new TreeMap<Integer, List<DomainRecognition>>();
      for (DomainRecognition domainRecognition : locationDomainRecs) {
         Integer sentenceId = domainRecognition.getSentenceId();
         //Default the sentence id to zero, if not available
         if (sentenceId == null) {
            sentenceId = 0;
         }
         List<DomainRecognition> domainRecognitions = sortedDomainRecBySentencedId.get(sentenceId);
         if (domainRecognitions == null) {
            domainRecognitions = new ArrayList<DomainRecognition>();
            sortedDomainRecBySentencedId.put(sentenceId, domainRecognitions);
         }
         domainRecognitions.add(domainRecognition);
      }

      if (sortedDomainRecBySentencedId.size() == 1) {
         Entry<Integer, List<DomainRecognition>> domainRecEntry = sortedDomainRecBySentencedId.firstEntry();
         List<DomainRecognition> domainRecs = domainRecEntry.getValue();
         DomainRecognition bestRecognition = NLPUtils.chooseBestDomainRecognitionByQuality(domainRecs);
         updateReducedForm(semanticPossibility, pathsByLocationDomainRecognition, domainRecs, bestRecognition);
         locationPointInfos = getValidLocationInfos(bestRecognition, semanticPossibility,
                  pathsByLocationDomainRecognition, returnBestLocationInfo);
         return locationPointInfos;
      }

      // Source location domain recognitions
      List<DomainRecognition> locationRecsInSource = sortedDomainRecBySentencedId.get(1);

      // Title location domain recognitions
      List<DomainRecognition> locationRecsInTitle = sortedDomainRecBySentencedId.get(2);

      // Process all the location domain recs from the title 
      if (!CollectionUtils.isEmpty(locationRecsInTitle)) {
         DomainRecognition bestRecognitionInTitle = NLPUtils.chooseBestDomainRecognitionByQuality(locationRecsInTitle);
         DomainRecognition bestRecognitionInSource = NLPUtils
                  .chooseBestDomainRecognitionByQuality(locationRecsInSource);

         updateReducedForm(semanticPossibility, pathsByLocationDomainRecognition, locationRecsInTitle,
                  bestRecognitionInTitle);
         updateReducedForm(semanticPossibility, pathsByLocationDomainRecognition, locationRecsInSource,
                  bestRecognitionInSource);

         locationPointInfos = getValidLocationInfos(semanticPossibility, pathsByLocationDomainRecognition,
                  bestRecognitionInTitle, bestRecognitionInSource, returnBestLocationInfo);
      }
      return locationPointInfos;
   }

   /**
    * @param semanticPossibility
    * @param pathsByLocationDomainRecognition 
    * @param domainRecognitions
    * @param bestRecognition
    */
   private void updateReducedForm (SemanticPossibility semanticPossibility,
            Map<DomainRecognition, IGraphPath> pathsByLocationDomainRecognition,
            List<DomainRecognition> domainRecognitions, DomainRecognition bestRecognition) {
      Graph reducedForm = semanticPossibility.getReducedForm();
      for (DomainRecognition domainRecognition : domainRecognitions) {
         if (domainRecognition != bestRecognition) {
            IGraphPath graphPath = pathsByLocationDomainRecognition.get(domainRecognition);
            if (graphPath != null) {
               reducedForm.removePath(graphPath);
            }
            reducedForm.removeVertex(domainRecognition);

         }
      }
   }

   private List<LocationPointInfo> getValidLocationInfos (SemanticPossibility semanticPossibility,
            Map<DomainRecognition, IGraphPath> pathsByLocationDomainRecognition,
            DomainRecognition bestRecognitionInTitle, DomainRecognition bestRecognitionInSource,
            boolean returnBestLocationInfo) throws LocationException {
      List<LocationPointInfo> validLocationInfos = new ArrayList<LocationPointInfo>();

      // Populate the cities and states in title
      INormalizedData normalizedDataInTitle = bestRecognitionInTitle.getNormalizedData();
      List<NormalizedDataEntity> citiesInTitle = new ArrayList<NormalizedDataEntity>();
      List<NormalizedDataEntity> statesInTitle = new ArrayList<NormalizedDataEntity>();
      populateCityStatesNormalizedDataEntities(normalizedDataInTitle, citiesInTitle, statesInTitle);

      // Populate the cities and states in source
      INormalizedData normalizedDataInSource = bestRecognitionInSource.getNormalizedData();
      List<NormalizedDataEntity> citiesInSource = new ArrayList<NormalizedDataEntity>();
      List<NormalizedDataEntity> statesInSource = new ArrayList<NormalizedDataEntity>();
      populateCityStatesNormalizedDataEntities(normalizedDataInSource, citiesInSource, statesInSource);

      if (!CollectionUtils.isEmpty(citiesInTitle) && !CollectionUtils.isEmpty(statesInTitle)) {
         // Get the valid location point info's for the cities and states present in the title
         validLocationInfos = getValidLocationInfos(citiesInTitle, statesInTitle);

         if (!CollectionUtils.isEmpty(validLocationInfos)) {
            updateLocationInfoInGraph(semanticPossibility, bestRecognitionInTitle, bestRecognitionInSource,
                     pathsByLocationDomainRecognition, citiesInTitle, statesInTitle, returnBestLocationInfo,
                     validLocationInfos);
         } else {
            // If valid location point info not found, then check with the cities in title and states in source
            validLocationInfos = getValidLocationInfos(citiesInTitle, statesInSource);
         }

         if (!CollectionUtils.isEmpty(validLocationInfos)) {
            updateLocationInfoInGraph(semanticPossibility, bestRecognitionInTitle, bestRecognitionInSource,
                     pathsByLocationDomainRecognition, citiesInTitle, statesInSource, returnBestLocationInfo,
                     validLocationInfos);
         } else {
            // If still valid location point info not found, then check with the cities in source and states in source
            validLocationInfos = getValidLocationInfos(citiesInSource, statesInSource);
            updateLocationInfoInGraph(semanticPossibility, bestRecognitionInSource, bestRecognitionInTitle,
                     pathsByLocationDomainRecognition, citiesInSource, statesInSource, returnBestLocationInfo,
                     validLocationInfos);
         }

      } else if (CollectionUtils.isEmpty(statesInTitle)) {
         // If only city is present in the title and not state, then pick the state from the source
         validLocationInfos = getValidLocationInfos(citiesInTitle, statesInSource);

         if (!CollectionUtils.isEmpty(validLocationInfos)) {
            updateLocationInfoInGraph(semanticPossibility, bestRecognitionInTitle, bestRecognitionInSource,
                     pathsByLocationDomainRecognition, citiesInTitle, statesInSource, returnBestLocationInfo,
                     validLocationInfos);
         } else {
            // If still valid location point info not found, then check with the cities and states in source
            validLocationInfos = getValidLocationInfos(citiesInSource, statesInSource);
            updateLocationInfoInGraph(semanticPossibility, bestRecognitionInSource, bestRecognitionInTitle,
                     pathsByLocationDomainRecognition, citiesInSource, statesInSource, returnBestLocationInfo,
                     validLocationInfos);
         }
      } else {
         validLocationInfos = getValidLocationInfos(citiesInSource, statesInSource);
         updateLocationInfoInGraph(semanticPossibility, bestRecognitionInSource, bestRecognitionInTitle,
                  pathsByLocationDomainRecognition, citiesInSource, statesInSource, returnBestLocationInfo,
                  validLocationInfos);
      }
      return validLocationInfos;
   }

   /**
    * @param semanticPossibility
    * @param recognitionToBeUpdated
    * @param recognitionToBeRemoved
    * @param pathsByLocationDomainRecognition
    * @param validCities
    * @param validStates
    * @param validLocationInfos 
    */
   private void updateLocationInfoInGraph (SemanticPossibility semanticPossibility,
            DomainRecognition recognitionToBeUpdated, DomainRecognition recognitionToBeRemoved,
            Map<DomainRecognition, IGraphPath> pathsByLocationDomainRecognition,
            List<NormalizedDataEntity> validCities, List<NormalizedDataEntity> validStates,
            boolean returnBestLocationInfo, List<LocationPointInfo> validLocationInfos) {

      IGraphPath graphPath = pathsByLocationDomainRecognition.get(recognitionToBeUpdated);
      if (graphPath != null) {
         // If recognition is in path, then first remove and then update and add it back to the graph
         semanticPossibility.getReducedForm().removeVertex(recognitionToBeUpdated);
         semanticPossibility.getReducedForm().removePath(graphPath);
         List<IGraphComponent> fullPath = graphPath.getFullPath();
         IGraphComponent graphComponentToBeAdded = null;
         for (IGraphComponent graphComponent : fullPath) {
            if (graphComponent == recognitionToBeUpdated) {
               graphComponentToBeAdded = graphComponent;
               updateGraphComponent(graphComponent, validCities, validStates, returnBestLocationInfo,
                        validLocationInfos);
               break;
            }
         }
         if (graphComponentToBeAdded != null) {
            semanticPossibility.getReducedForm().addPath(graphPath);
            semanticPossibility.getReducedForm().addGraphComponent(graphComponentToBeAdded, null);
         }
      } else {
         // If recognition is not in path, then we can directly update the recognition
         Map<String, IGraphComponent> vertices = semanticPossibility.getReducedForm().getVertices();
         for (Entry<String, IGraphComponent> entry : vertices.entrySet()) {
            IGraphComponent graphComponent = entry.getValue();
            if (graphComponent == recognitionToBeUpdated) {
               updateGraphComponent(graphComponent, validCities, validStates, returnBestLocationInfo,
                        validLocationInfos);
               break;
            }
         }
      }

      // Remove the recognition to be removed
      if (recognitionToBeRemoved != null) {
         graphPath = pathsByLocationDomainRecognition.get(recognitionToBeRemoved);
         if (graphPath != null) {
            semanticPossibility.getReducedForm().removePath(graphPath);
         }
         semanticPossibility.getReducedForm().removeVertex(recognitionToBeRemoved);
      }
   }

   private void updateGraphComponent (IGraphComponent graphComponent, List<NormalizedDataEntity> validCities,
            List<NormalizedDataEntity> validStates, boolean returnBestLocationInfo,
            List<LocationPointInfo> validLocationInfos) {
      DomainRecognition domainRecognition = (DomainRecognition) graphComponent;
      INormalizedData normalizedData = domainRecognition.getNormalizedData();
      if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
         if (returnBestLocationInfo) {
            // Override the list normalized data with the valid location normalized data 
            LocationNormalizedData locationNormalizedData = getValidLocationNormalizedData(listNormalizedData,
                     validLocationInfos.get(0));
            domainRecognition.setNormalizedData(locationNormalizedData);
            updateInstanceInfo(domainRecognition, locationNormalizedData, validCities, validStates);
         } else {
            List<NormalizedDataEntity> normalizedDataEntities = listNormalizedData.getNormalizedDataEntities();
            for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
               INormalizedData normalizedData2 = normalizedDataEntity.getNormalizedData();
               LocationNormalizedData locationNormalizedData = (LocationNormalizedData) normalizedData2;
               updateInstanceInfo(domainRecognition, locationNormalizedData, validCities, validStates);
            }
         }
      } else {
         LocationNormalizedData locationNormalizedData = (LocationNormalizedData) normalizedData;
         updateInstanceInfo(domainRecognition, locationNormalizedData, validCities, validStates);
      }
   }

   private LocationNormalizedData getValidLocationNormalizedData (ListNormalizedData listNormalizedData,
            LocationPointInfo locationPointInfo) {
      List<NormalizedDataEntity> normalizedDataEntities = listNormalizedData.getNormalizedDataEntities();
      Long locationBedId = locationPointInfo.getLocationBedId();
      LocationNormalizedData validLocationNormalizedData = null;
      for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
         LocationNormalizedData locationNormalizedData = (LocationNormalizedData) normalizedDataEntity
                  .getNormalizedData();
         List<NormalizedDataEntity> cities = locationNormalizedData.getCities();
         for (NormalizedDataEntity normalizedDataEntity2 : cities) {
            if (normalizedDataEntity2.getValueBedId().equals(locationBedId)) {
               validLocationNormalizedData = locationNormalizedData;
               break;
            }
         }
      }
      return validLocationNormalizedData;
   }

   private void updateInstanceInfo (DomainRecognition domainRecognition, LocationNormalizedData locationNormalizedData,
            List<NormalizedDataEntity> validCities, List<NormalizedDataEntity> validStates) {
      locationNormalizedData.getCities().retainAll(validCities);
      locationNormalizedData.getStates().retainAll(validStates);

      List<InstanceInformation> instanceInformations = domainRecognition.getInstanceInformations();
      Set<InstanceInformation> validInstanceInformations = new HashSet<InstanceInformation>();
      for (InstanceInformation instanceInformation : instanceInformations) {
         instanceInformation.setInstanceValue(locationNormalizedData.getValue());
         instanceInformation.setInstanceDisplayName(locationNormalizedData.getDisplayValue());
      }
      validInstanceInformations.addAll(domainRecognition.getInstanceInformations());
      domainRecognition.setInstanceInformations(new ArrayList<InstanceInformation>(validInstanceInformations));
   }

   private void populateCityStatesNormalizedDataEntities (INormalizedData normalizedData,
            List<NormalizedDataEntity> cities, List<NormalizedDataEntity> states) {
      if (normalizedData.getNormalizedDataType() == NormalizedDataType.LOCATION_NORMALIZED_DATA) {
         LocationNormalizedData titleLocationNormalizedData = (LocationNormalizedData) normalizedData;
         cities.addAll(titleLocationNormalizedData.getCities());
         states.addAll(titleLocationNormalizedData.getStates());
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
         List<NormalizedDataEntity> normalizedDataEntities = listNormalizedData.getNormalizedDataEntities();
         for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
            LocationNormalizedData locationNormalizedData = (LocationNormalizedData) normalizedDataEntity
                     .getNormalizedData();
            if (locationNormalizedData.getLocationType() == LocationType.CITY) {
               cities.addAll(locationNormalizedData.getCities());
               states.addAll(locationNormalizedData.getStates());
            } else if (locationNormalizedData.getLocationType() == LocationType.STATE) {
               states.addAll(locationNormalizedData.getStates());
            }
         }
      }
   }

   private List<LocationPointInfo> getValidLocationInfos (DomainRecognition locationRecognition,
            SemanticPossibility semanticPossibility,
            Map<DomainRecognition, IGraphPath> pathsByLocationDomainRecognition, boolean returnBestLocationInfo)
            throws LocationException {
      List<LocationPointInfo> validLocationInfos = new ArrayList<LocationPointInfo>();
      INormalizedData normalizedData = locationRecognition.getNormalizedData();
      List<NormalizedDataEntity> cities = new ArrayList<NormalizedDataEntity>();
      List<NormalizedDataEntity> states = new ArrayList<NormalizedDataEntity>();
      populateCityStatesNormalizedDataEntities(normalizedData, cities, states);
      if (CollectionUtils.isEmpty(states)) {
         validLocationInfos = getLocationInfos(cities);
      } else {
         validLocationInfos = getValidLocationInfos(cities, states);
         updateLocationInfoInGraph(semanticPossibility, locationRecognition, null, pathsByLocationDomainRecognition,
                  cities, states, returnBestLocationInfo, validLocationInfos);
      }
      return validLocationInfos;
   }

   private List<LocationPointInfo> getLocationInfos (List<NormalizedDataEntity> cities) throws LocationException {
      List<LocationPointInfo> locationPoints = new ArrayList<LocationPointInfo>();
      if (CollectionUtils.isEmpty(cities)) {
         return locationPoints;
      }
      Map<Long, NormalizedDataEntity> dataEntityByLocationBedId = getNormalizedDataEntityByLocationBedId(cities);
      List<Long> cityBedIds = new ArrayList<Long>(dataEntityByLocationBedId.keySet());
      locationPoints = getLocationRetrievalService().getLocationPointsByBedIds(cityBedIds,
               NormalizedLocationType.BED_BASED);
      return locationPoints;
   }

   private List<LocationPointInfo> getValidLocationInfos (List<NormalizedDataEntity> cities,
            List<NormalizedDataEntity> states) throws LocationException {
      List<LocationPointInfo> locationPoints = new ArrayList<LocationPointInfo>();
      Map<Long, NormalizedDataEntity> cityDataEntityByLocationBedId = getNormalizedDataEntityByLocationBedId(cities);
      List<Long> cityBedIds = new ArrayList<Long>(cityDataEntityByLocationBedId.keySet());
      Map<Long, NormalizedDataEntity> stateDataEntityByLocationBedId = getNormalizedDataEntityByLocationBedId(states);
      List<Long> stateBedIds = new ArrayList<Long>(stateDataEntityByLocationBedId.keySet());
      List<StateCity> stateCities = getLocationRetrievalService()
               .getValidStateCityCombinations(stateBedIds, cityBedIds);
      if (CollectionUtils.isEmpty(stateCities)) {
         return locationPoints;
      }
      Map<Long, StateCity> stateCitiesByCityBedId = getCityStateByCityBedId(stateCities);
      removeInvalidCityStates(cities, states, cityDataEntityByLocationBedId, stateDataEntityByLocationBedId,
               stateCities);
      cityBedIds = new ArrayList<Long>(stateCitiesByCityBedId.keySet());
      locationPoints = getLocationRetrievalService().getLocationPointsByBedIds(cityBedIds,
               NormalizedLocationType.BED_BASED);
      return locationPoints;
   }

   private void removeInvalidCityStates (List<NormalizedDataEntity> cities, List<NormalizedDataEntity> states,
            Map<Long, NormalizedDataEntity> cityDataEntityByLocationBedId,
            Map<Long, NormalizedDataEntity> stateDataEntityByLocationBedId, List<StateCity> stateCities) {
      if (CollectionUtils.isEmpty(stateCities)) {
         return;
      }

      Set<NormalizedDataEntity> validCities = new HashSet<NormalizedDataEntity>();
      Set<NormalizedDataEntity> validStates = new HashSet<NormalizedDataEntity>();
      for (StateCity stateCity : stateCities) {
         Long cityId = stateCity.getCityId();
         NormalizedDataEntity cityEntity = cityDataEntityByLocationBedId.get(cityId);
         validCities.add(cityEntity);
         Long stateId = stateCity.getStateId();
         NormalizedDataEntity stateEntity = stateDataEntityByLocationBedId.get(stateId);
         validStates.add(stateEntity);
      }
      cities.clear();
      states.clear();
      cities.addAll(validCities);
      states.addAll(validStates);

   }

   private Map<Long, StateCity> getCityStateByCityBedId (List<StateCity> stateCities) {
      Map<Long, StateCity> stateCityByCityBedId = new HashMap<Long, StateCity>();
      if (CollectionUtils.isEmpty(stateCities)) {
         return stateCityByCityBedId;
      }
      for (StateCity stateCity : stateCities) {
         stateCityByCityBedId.put(stateCity.getCityId(), stateCity);
      }
      return stateCityByCityBedId;
   }

   /**
    * @param locationDomainRecs
    * @param semanticPossibility
    */
   private Map<DomainRecognition, IGraphPath> getLocationDomainRecognitions (SemanticPossibility semanticPossibility) {
      List<DomainRecognition> locationDomainRecs = new ArrayList<DomainRecognition>();
      Map<DomainRecognition, IGraphPath> locationPathsByRecognition = new HashMap<DomainRecognition, IGraphPath>();
      Graph reducedFormGraph = semanticPossibility.getReducedForm();
      List<IGraphPath> graphPaths = new ArrayList<IGraphPath>(1);
      if (!CollectionUtils.isEmpty(reducedFormGraph.getPaths())) {
         graphPaths.addAll(reducedFormGraph.getPaths());
      }

      for (IGraphPath path : graphPaths) {
         GraphPath graphPath = (GraphPath) path;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);
            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            if (ReducedFormHelper.isLocation(source)) {
               locationDomainRecs.add(source);
               locationPathsByRecognition.put(source, path);
            }
            if (ReducedFormHelper.isLocation(destination)) {
               locationDomainRecs.add(destination);
               locationPathsByRecognition.put(destination, path);
            }
            start = start + 2;
            i = i + 2;
         }
      }
      Collection<IGraphComponent> rootVertices = semanticPossibility.getRootVertices();
      for (IGraphComponent rv : rootVertices) {
         DomainRecognition entity = (DomainRecognition) rv;
         if (ReducedFormHelper.isLocation(entity)) {
            locationDomainRecs.add(entity);
            locationPathsByRecognition.put(entity, null);
         }
      }
      return locationPathsByRecognition;
   }

   /**
    * @param semanticPossibility
    * @param featureValueBedIdsByFeatureBedIdMap
    * @param featureOperatorValueByFeatureBedIdMap
    * @param fromSemantification 
    */
   private void parseReducedFormForValueAndNumber (SemanticPossibility semanticPossibility,
            Map<Long, Set<Long>> featureValueBedIdsByFeatureBedIdMap,
            Map<Long, List<Object[]>> featureOperatorValueByFeatureBedIdMap, boolean fromSemantification) {

      Graph reducedFormGraph = semanticPossibility.getReducedForm();
      List<IGraphPath> graphPaths = new ArrayList<IGraphPath>(1);
      if (!CollectionUtils.isEmpty(reducedFormGraph.getPaths())) {
         graphPaths.addAll(reducedFormGraph.getPaths());
      }

      for (IGraphPath path : graphPaths) {
         GraphPath graphPath = (GraphPath) path;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);

            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition destination = (DomainRecognition) components.get(2);

            // Check if source is location and for semantification, then handle the location information 
            if (ReducedFormHelper.isLocation(source)) {
               if (fromSemantification) {
                  handleLocationInfo(featureValueBedIdsByFeatureBedIdMap, source);
               }
            } else if (source.getDefaultInstanceBedId() != null) {
               Set<Long> featureValueBedIds = getExistingFeatureValueBedIds(featureValueBedIdsByFeatureBedIdMap, source
                        .getConceptBEDId());
               for (InstanceInformation instanceInformation : source.getInstanceInformations()) {
                  featureValueBedIds.add(instanceInformation.getInstanceBedId());
               }
            } else if (source.getNormalizedData() != null
                     && source.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               Set<Long> featureValueBedIds = getExistingFeatureValueBedIds(featureValueBedIdsByFeatureBedIdMap, source
                        .getConceptBEDId());
               ListNormalizedData normalizedData = (ListNormalizedData) source.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
               for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                  if (normalizedDataEntity.getValueBedId() != null) {
                     featureValueBedIds.add(normalizedDataEntity.getValueBedId());
                  }
               }
            }
            // Check if destination is location and for semantification, then handle the location information
            if (ReducedFormHelper.isLocation(destination)) {
               if (fromSemantification) {
                  handleLocationInfo(featureValueBedIdsByFeatureBedIdMap, destination);
               }
            } else if (ReducedFormHelper.isValidValue(destination)) {
               handleRFXValue(featureOperatorValueByFeatureBedIdMap, source, destination);
            } else if (destination.getDefaultInstanceBedId() != null) {
               if (!StringUtils.isEmpty(destination.getConceptDisplayName())) {
                  Set<Long> featureValueBedIds = getExistingFeatureValueBedIds(featureValueBedIdsByFeatureBedIdMap,
                           destination.getConceptBEDId());
                  for (InstanceInformation instanceInformation : destination.getInstanceInformations()) {
                     featureValueBedIds.add(instanceInformation.getInstanceBedId());
                  }
               }
            } else if (destination.getNormalizedData() != null
                     && destination.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               Set<Long> featureValueBedIds = getExistingFeatureValueBedIds(featureValueBedIdsByFeatureBedIdMap,
                        destination.getConceptBEDId());
               ListNormalizedData normalizedData = (ListNormalizedData) destination.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
               for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                  if (normalizedDataEntity.getValueBedId() != null) {
                     featureValueBedIds.add(normalizedDataEntity.getValueBedId());
                  }
               }
            }
            start = start + 2;
            i = i + 2;
         }
      }
      Collection<IGraphComponent> rootVertices = semanticPossibility.getRootVertices();
      for (IGraphComponent rv : rootVertices) {
         DomainRecognition entity = (DomainRecognition) rv;
         // Check if entity is location and for semantification, then handle the location information
         if (ReducedFormHelper.isLocation(entity)) {
            if (fromSemantification) {
               handleLocationInfo(featureValueBedIdsByFeatureBedIdMap, entity);
            }
         } else if (!StringUtils.isEmpty(entity.getConceptDisplayName()) && entity.getDefaultInstanceBedId() != null) {
            Set<Long> featureValueBedIds = getExistingFeatureValueBedIds(featureValueBedIdsByFeatureBedIdMap, entity
                     .getConceptBEDId());
            for (InstanceInformation instanceInformation : entity.getInstanceInformations()) {
               featureValueBedIds.add(instanceInformation.getInstanceBedId());
            }
         } else if (ReducedFormHelper.isValidValue(entity)
                  && (entity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA || entity
                           .getNormalizedData().getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA)) {
            // Right now only considering the time frame which can come as unconnected in the user query if it is
            // separated by
            // more proximity from the central concept
            handleRFXValue(featureOperatorValueByFeatureBedIdMap, null, entity);
         }
      }
   }

   /**
    * @param featureValueBedIdsByFeatureBedIdMap
    * @param locationRecognition
    */
   private void handleLocationInfo (Map<Long, Set<Long>> featureValueBedIdsByFeatureBedIdMap,
            DomainRecognition locationRecognition) {
      // TODO: We are only getting cities and states location bed ids for now
      INormalizedData normalizedData = locationRecognition.getNormalizedData();
      List<NormalizedDataEntity> cities = new ArrayList<NormalizedDataEntity>();
      List<NormalizedDataEntity> states = new ArrayList<NormalizedDataEntity>();
      populateCityStatesNormalizedDataEntities(normalizedData, cities, states);

      Map<Long, Set<Long>> citiesByCityBedId = getLocationInfoMap(cities);
      featureValueBedIdsByFeatureBedIdMap.putAll(citiesByCityBedId);

      Map<Long, Set<Long>> statesByStateBedId = getLocationInfoMap(states);
      featureValueBedIdsByFeatureBedIdMap.putAll(statesByStateBedId);

   }

   private Map<Long, Set<Long>> getLocationInfoMap (List<NormalizedDataEntity> cities) {
      Map<Long, Set<Long>> locationsBylocationConceptBedId = new HashMap<Long, Set<Long>>();

      for (NormalizedDataEntity normalizedDataEntity : cities) {
         LocationNormalizedDataEntity locationNormalizedDataEntity = (LocationNormalizedDataEntity) normalizedDataEntity;
         Map<Long, Long> instanceByConceptBedId = locationNormalizedDataEntity.getInstanceByConceptBedId();
         for (Entry<Long, Long> entry : instanceByConceptBedId.entrySet()) {
            Set<Long> set = locationsBylocationConceptBedId.get(entry.getKey());
            if (CollectionUtils.isEmpty(set)) {
               set = new HashSet<Long>();
               locationsBylocationConceptBedId.put(entry.getKey(), set);
            }
            set.add(entry.getValue());
         }
      }
      return locationsBylocationConceptBedId;
   }

   private Set<Long> getAllLocationBedIds (DomainRecognition locationRecognition) {
      // TODO: We are only getting cities and states location bed ids for now
      INormalizedData normalizedData = locationRecognition.getNormalizedData();
      List<NormalizedDataEntity> cities = new ArrayList<NormalizedDataEntity>();
      List<NormalizedDataEntity> states = new ArrayList<NormalizedDataEntity>();
      populateCityStatesNormalizedDataEntities(normalizedData, cities, states);
      Set<Long> locationBedIds = getLocationBedIds(cities);
      return null;
   }

   private Set<Long> getLocationBedIds (List<NormalizedDataEntity> cities) {
      for (NormalizedDataEntity normalizedDataEntity : cities) {
         LocationNormalizedDataEntity locationNormalizedDataEntity = (LocationNormalizedDataEntity) normalizedDataEntity;
         locationNormalizedDataEntity.getInstanceByConceptBedId();
      }
      return null;
   }

   private Set<Long> getExistingFeatureValueBedIds (Map<Long, Set<Long>> featureValueBedIdsByFeatureIdMap,
            Long conceptBEDId) {
      Set<Long> featureValueBedIds = featureValueBedIdsByFeatureIdMap.get(conceptBEDId);
      if (featureValueBedIds == null) {
         featureValueBedIds = new HashSet<Long>();
         featureValueBedIdsByFeatureIdMap.put(conceptBEDId, featureValueBedIds);
      }
      return featureValueBedIds;
   }

   private Map<Long, NormalizedDataEntity> getNormalizedDataEntityByLocationBedId (
            List<NormalizedDataEntity> locationEntities) {
      Map<Long, NormalizedDataEntity> dataEntityByLocationBedId = new HashMap<Long, NormalizedDataEntity>();
      for (NormalizedDataEntity normalizedDataEntity : locationEntities) {
         dataEntityByLocationBedId.put(normalizedDataEntity.getValueBedId(), normalizedDataEntity);
      }
      return dataEntityByLocationBedId;
   }

   /**
    * @param featureOperatorValueMap
    * @param source
    * @param destination
    */
   private void handleRFXValue (Map<Long, List<Object[]>> featureOperatorValueMap, DomainRecognition source,
            DomainRecognition destination) {
      INormalizedData normalizedData = destination.getNormalizedData();
      if (normalizedData.getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         Object[] operatorValue = getReducedFormHelper().getOperatorAndValue(normalizedData);
         RangeNormalizedData rangeNormalizedData = (RangeNormalizedData) normalizedData;
         updateFeatureOperatorValueMap(rangeNormalizedData.getStart().getNormalizedData(), featureOperatorValueMap,
                  destination, source, operatorValue);
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
         // Currently we are only handling TF in case of list
         ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
         List<NormalizedDataEntity> normalizedDataEntities = listNormalizedData.getNormalizedDataEntities();
         NormalizedDataEntity normalizedDataEntity = normalizedDataEntities.get(0);
         if (normalizedDataEntity.getNormalizedData() != null
                  && normalizedDataEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
            for (NormalizedDataEntity entity : normalizedDataEntities) {
               Object[] operatorValue = getReducedFormHelper().getOperatorAndValue(entity.getNormalizedData());
               if (operatorValue[0] != null && operatorValue[1] != null) {
                  List<Object[]> featureValues = featureOperatorValueMap.get(destination.getConceptBEDId());
                  if (featureValues == null) {
                     featureValues = new ArrayList<Object[]>(1);
                     featureOperatorValueMap.put(destination.getConceptBEDId(), featureValues);
                  }
                  featureValues.add(operatorValue);
               }
            }
         }
      } else {
         Object[] operatorValue = getReducedFormHelper().getOperatorAndValue(normalizedData);
         updateFeatureOperatorValueMap(normalizedData, featureOperatorValueMap, destination, source, operatorValue);
      }
   }

   private void updateFeatureOperatorValueMap (INormalizedData normalizedData,
            Map<Long, List<Object[]>> featureOperatorValueMap, DomainRecognition destination, DomainRecognition source,
            Object[] operatorValue) {
      if (operatorValue[0] != null && operatorValue[1] != null) {
         if (normalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA
                  || normalizedData.getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
            List<Object[]> featureValues = featureOperatorValueMap.get(destination.getConceptBEDId());
            if (featureValues == null) {
               featureValues = new ArrayList<Object[]>(1);
               featureOperatorValueMap.put(destination.getConceptBEDId(), featureValues);
            }
            featureValues.add(operatorValue);
         } else {
            List<Object[]> featureValues = featureOperatorValueMap.get(source.getConceptBEDId());
            if (featureValues == null) {
               featureValues = new ArrayList<Object[]>(1);
               featureOperatorValueMap.put(source.getConceptBEDId(), featureValues);
            }
            featureValues.add(operatorValue);
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

   /**
    * @return the locationRetrievalService
    */
   public ILocationRetrievalService getLocationRetrievalService () {
      return locationRetrievalService;
   }

   /**
    * @param locationRetrievalService the locationRetrievalService to set
    */
   public void setLocationRetrievalService (ILocationRetrievalService locationRetrievalService) {
      this.locationRetrievalService = locationRetrievalService;
   }

   /**
    * @return the unstructuredWarehouseRetrievalService
    */
   public IUnstructuredWarehouseRetrievalService getUnstructuredWarehouseRetrievalService () {
      return unstructuredWarehouseRetrievalService;
   }

   /**
    * @param unstructuredWarehouseRetrievalService the unstructuredWarehouseRetrievalService to set
    */
   public void setUnstructuredWarehouseRetrievalService (
            IUnstructuredWarehouseRetrievalService unstructuredWarehouseRetrievalService) {
      this.unstructuredWarehouseRetrievalService = unstructuredWarehouseRetrievalService;
   }

   public UnstructuredWarehouseHelper getUnstructuredWarehouseHelper () {
      return unstructuredWarehouseHelper;
   }

   public void setUnstructuredWarehouseHelper (UnstructuredWarehouseHelper unstructuredWarehouseHelper) {
      this.unstructuredWarehouseHelper = unstructuredWarehouseHelper;
   }

}
