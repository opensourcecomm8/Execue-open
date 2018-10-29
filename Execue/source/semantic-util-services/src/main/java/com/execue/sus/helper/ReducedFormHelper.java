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
package com.execue.sus.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.INormalizedData;
import com.execue.core.common.bean.entity.Conversion;
import com.execue.core.common.bean.entity.NormalizedDataType;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.ListNormalizedData;
import com.execue.core.common.bean.nlp.NormalizedDataEntity;
import com.execue.core.common.bean.nlp.RangeNormalizedData;
import com.execue.core.common.bean.nlp.RelativeTimeNormalizedData;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.nlp.TimeFrameNormalizedData;
import com.execue.core.common.bean.nlp.UnitNormalizedData;
import com.execue.core.common.bean.nlp.ValueRealizationNormalizedData;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DateQualifier;
import com.execue.core.common.type.OperatorType;
import com.execue.core.common.util.NLPUtils;
import com.execue.core.util.ExecueStringUtil;
import com.execue.platform.swi.conversion.unitscale.ITypeConvertor;
import com.execue.platform.swi.conversion.unitscale.TypeConvertorFactory;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IConversionService;

/**
 * @author Nitesh
 *
 */
public class ReducedFormHelper {

   private Logger               logger = Logger.getLogger(RFXServiceHelper.class);
   private TypeConvertorFactory typeConvertorFactory;
   private IConversionService   conversionService;

   public Double getValueFromRangeNormalizedData (INormalizedData normalizedData) {
      if (normalizedData == null) {
         return null;
      }
      if (normalizedData.getNormalizedDataType() == NormalizedDataType.VALUE_NORMALIZED_DATA) {
         if (((ValueRealizationNormalizedData) normalizedData).getNumber() != null) {
            ValueRealizationNormalizedData valueNormalizedData = (ValueRealizationNormalizedData) normalizedData;
            String number = valueNormalizedData.getNumber().getValue();
            if (valueNormalizedData.getUnitScale() != null) {
               String convertedValue = applyBaseUnitConversion(valueNormalizedData.getUnitScale().getDisplayValue(),
                        number, valueNormalizedData.getUnitScale().getValueBedId());
               if (convertedValue == null) {
                  return null;
               }
               return Double.parseDouble(convertedValue);
            } else {
               return Double.parseDouble(number);
            }
         }
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.UNIT_NORMALIZED_DATA) {
         if (((UnitNormalizedData) normalizedData).getNumber() != null) {
            UnitNormalizedData valueNormalizedData = (UnitNormalizedData) normalizedData;
            String number = valueNormalizedData.getNumber().getValue();
            if (valueNormalizedData.getUnitScale() != null) {
               String convertedValue = applyBaseUnitConversion(valueNormalizedData.getUnitScale().getDisplayValue(),
                        number, valueNormalizedData.getUnitScale().getValueBedId());
               if (convertedValue == null) {
                  return null;
               }
               return Double.parseDouble(convertedValue);
            } else {
               return Double.parseDouble(number);
            }
         }
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
         // TODO: NK: Currently considering only year in time frame
         if (((TimeFrameNormalizedData) normalizedData).getYear() != null) {
            return Double.parseDouble(((TimeFrameNormalizedData) normalizedData).getYear().getValue());
         }
      }
      return null;
   }

   public static boolean isLocation (DomainRecognition domainRecognition) {
      INormalizedData normalizedData = domainRecognition.getNormalizedData();
      if (normalizedData != null) {
         if (normalizedData.getNormalizedDataType() == NormalizedDataType.LOCATION_NORMALIZED_DATA) {
            return true;
         } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
            List<NormalizedDataEntity> normalizedDataEntities = listNormalizedData.getNormalizedDataEntities();
            for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
               if (normalizedDataEntity.getNormalizedData() != null
                        && normalizedDataEntity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LOCATION_NORMALIZED_DATA) {
                  return true;
               }
            }
         }
      }
      return false;
   }

   public Object[] getOperatorAndValue (INormalizedData normalizedData) {
      Object[] value = new Object[2];
      if (normalizedData.getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA) {
         RangeNormalizedData rangeNormalizedData = (RangeNormalizedData) normalizedData;
         Double startValue = getValueFromRangeNormalizedData(rangeNormalizedData.getStart().getNormalizedData());
         Double endValue = getValueFromRangeNormalizedData(rangeNormalizedData.getEnd().getNormalizedData());

         if (startValue != null && endValue != null) {
            value = new Object[4];
            value[0] = OperatorType.GREATER_THAN_EQUALS.getValue();
            value[1] = startValue;
            value[2] = OperatorType.LESS_THAN_EQUALS.getValue();
            value[3] = endValue;
         }
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.VALUE_NORMALIZED_DATA) {
         value[0] = getOperatorFromValueNormalizedData((ValueRealizationNormalizedData) normalizedData);
         if (((ValueRealizationNormalizedData) normalizedData).getNumber() != null) {
            ValueRealizationNormalizedData opValueNormalizedData = (ValueRealizationNormalizedData) normalizedData;
            String number = opValueNormalizedData.getNumber().getValue();
            if (opValueNormalizedData.getUnitScale() != null) {
               String convertedValue = applyBaseUnitConversion(opValueNormalizedData.getUnitScale().getDisplayValue(),
                        number, opValueNormalizedData.getUnitScale().getValueBedId());
               if (convertedValue == null) {
                  return value;
               }
               value[1] = Double.parseDouble(convertedValue);
            } else {
               value[1] = Double.parseDouble(number);
            }
         }
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
         NormalizedDataEntity valuePreposition = ((TimeFrameNormalizedData) normalizedData).getValuePreposition();
         if (valuePreposition == null) {
            value[0] = OperatorType.EQUALS.getValue();
         } else {
            value[0] = valuePreposition.getDisplaySymbol();
         }
         if (((TimeFrameNormalizedData) normalizedData).getYear() != null) {
            value[1] = Double.parseDouble(((TimeFrameNormalizedData) normalizedData).getYear().getValue());
         }
      } else if (normalizedData.getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA) {
         RelativeTimeNormalizedData relativeNormalizedData = (RelativeTimeNormalizedData) normalizedData;
         if (relativeNormalizedData.getRelativeDateQualifier() == DateQualifier.YEAR) {
            value = new Object[4];
            NormalizedDataEntity numberNormalizedDataEntity = relativeNormalizedData.getNumber();
            String number;
            if (numberNormalizedDataEntity != null) {
               number = numberNormalizedDataEntity.getValue();
            } else {
               number = "1";
            }
            if (relativeNormalizedData.getAdjective() != null) {
               if (relativeNormalizedData.getAdjective().getDisplayValue().equalsIgnoreCase("last")) {
                  // NOTE: IF Last is specified, we will handled it as range
                  // TODO -NA- Add call to conversion Service after the API is modified
                  // NOTE: operator are not considered in the final query match, hence for
                  // now reducing last no. of years by 1 before getting the start year
                  value[0] = OperatorType.GREATER_THAN.getValue();
                  Calendar cl = Calendar.getInstance();
                  Integer endYear = cl.get(Calendar.YEAR);
                  Integer startYear = endYear - (Double.valueOf(number).intValue() - 1);
                  value[1] = startYear.doubleValue();
                  value[2] = OperatorType.LESS_THAN_EQUALS.getValue();
                  value[3] = endYear.doubleValue();
               }
            } else if (relativeNormalizedData.getOperator() != null) {
               value[0] = getOperatorForRelativeNormalizedData(relativeNormalizedData.getOperator()).getValue();
               Calendar cl = Calendar.getInstance();
               Integer year = cl.get(Calendar.YEAR);
               year = year - Double.valueOf(number).intValue();
               value[1] = year.doubleValue();
            }
         }
      }
      return value;
   }

   private OperatorType getOperatorForRelativeNormalizedData (NormalizedDataEntity operator) {
      String qualifierVal = operator.getDisplayValue();
      if (qualifierVal.equals(OperatorType.LESS_THAN.getValue())) {
         return OperatorType.GREATER_THAN;
      } else if (qualifierVal.equals(OperatorType.LESS_THAN_EQUALS.getValue())) {
         return OperatorType.GREATER_THAN_EQUALS;
      } else if (qualifierVal.equals(OperatorType.GREATER_THAN.getValue())) {
         return OperatorType.LESS_THAN_EQUALS;
      } else if (qualifierVal.equals(OperatorType.GREATER_THAN_EQUALS.getValue())) {
         return OperatorType.LESS_THAN_EQUALS;
      } else {
         return OperatorType.EQUALS;
      }

   }

   private String getOperatorFromValueNormalizedData (ValueRealizationNormalizedData normalizedData) {
      if (normalizedData.getOperator() != null) {
         String displaySymbol = normalizedData.getOperator().getDisplaySymbol();
         if (displaySymbol != null) {
            return OperatorType.getOperatorType(displaySymbol).getValue();
         }
      } else {
         if (normalizedData.getValuePreposition() != null) {
            NormalizedDataEntity valuePreposition = normalizedData.getValuePreposition();
            String displaySymbol = valuePreposition.getDisplaySymbol();
            if (displaySymbol != null) {
               return OperatorType.getOperatorType(displaySymbol).getValue();
            }
         }
      }
      return OperatorType.EQUALS.getValue();
   }

   /**
    * @param unitScaleValue
    * @param number
    * @param unitScaleBedId
    * @return the converted value
    */
   public String applyBaseUnitConversion (String unitScaleValue, String number, Long unitScaleBedId) {
      String value = null;
      try {
         Conversion sourceConversion = getUnitScaleConversion(unitScaleBedId);
         if (sourceConversion == null) {
            if (logger.isDebugEnabled()) {
               logger.debug("couldn't found the default conversion for the unit value: " + unitScaleValue);
            }
            return value;
         }
         // TODO: NK: Currently converting to the base conversion i.e ONES
         Conversion targetConversion = getConversionService().getBaseConversion(sourceConversion.getType());
         ITypeConvertor numberTypeConvertor = getTypeConvertorFactory().getTypeConvertor(ConversionType.NUMBER);
         value = numberTypeConvertor.convert(sourceConversion, targetConversion, number);
      } catch (SWIException e) {
         logger.error(e.getMessage());
         e.printStackTrace();
      }
      return value;
   }

   public Conversion getUnitScaleConversion (Long scaleBedId) throws SWIException {
      // TODO -NA- HACK Hardcoding the TypeBeId of Number As Value Realization.
      Long typeBedId = 157L;
      Conversion conversion = getConversionService().getConversionByConceptAndInstanceBedId(typeBedId, scaleBedId);
      return conversion;

   }

   private Conversion getUnitSymbolConversion (String displayValue) throws SWIException {
      List<Conversion> conversions = null;
      conversions = getConversionService().getConversionsByType(ConversionType.CURRENCY);
      Conversion unitScaleConversion = null;
      if (conversions != null) {
         for (Conversion conversion : conversions) {
            if (conversion.getUnit().equalsIgnoreCase(displayValue)
                     || conversion.getUnit().equalsIgnoreCase(displayValue)) {
               unitScaleConversion = conversion;
               break;
            }
         }
      }
      return unitScaleConversion;
   }

   public static SemanticPossibility mergeSemanticPossibilities (List<SemanticPossibility> semanticPossibilities) {
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return null;
      }
      int size = semanticPossibilities.size();
      SemanticPossibility mergedPossibility = semanticPossibilities.get(0);
      if (semanticPossibilities.size() == 1) {
         return mergedPossibility;
      }
      Graph reducedFormGraph = new Graph();
      Map<String, IGraphPath> graphPathMap = new HashMap<String, IGraphPath>(1);
      Set<Long> tripleBeds = new HashSet<Long>(1);

      for (SemanticPossibility semanticPossibility : semanticPossibilities) {
         Graph currentReducedFormGraph = semanticPossibility.getReducedForm();
         List<IGraphPath> graphPaths = new ArrayList<IGraphPath>(1);
         if (!CollectionUtils.isEmpty(currentReducedFormGraph.getPaths())) {
            graphPaths.addAll(currentReducedFormGraph.getPaths());
         }

         for (IGraphPath path : graphPaths) {
            GraphPath graphPath = (GraphPath) path;
            if (graphPathMap.containsKey(graphPath.toString())) {
               continue;
            }
            graphPathMap.put(graphPath.toString(), graphPath);
            List<IGraphComponent> mergedComps = new ArrayList<IGraphComponent>(1);
            int pathLength = graphPath.getPathLength();
            int start = 0;
            for (int i = 3; i <= pathLength;) {
               List<IGraphComponent> components = graphPath.getPartialPath(start, i);

               DomainRecognition source = (DomainRecognition) components.get(0);
               if (reducedFormGraph.getAlreadyAddedNode(source) != null) {
                  source = (DomainRecognition) reducedFormGraph.getAlreadyAddedNode(source);
               }
               DomainRecognition relation = (DomainRecognition) components.get(1);
               if (reducedFormGraph.getAlreadyAddedNode(relation) != null) {
                  relation = (DomainRecognition) reducedFormGraph.getAlreadyAddedNode(relation);
               }
               DomainRecognition destination = (DomainRecognition) components.get(2);
               if (reducedFormGraph.getAlreadyAddedNode(destination) != null) {
                  destination = (DomainRecognition) reducedFormGraph.getAlreadyAddedNode(destination);
               }
               if (!mergedComps.contains(source)) {
                  mergedComps.add(source);
               }
               mergedComps.add(relation);
               mergedComps.add(destination);
               tripleBeds.add(source.getEntityBeId());
               tripleBeds.add(relation.getEntityBeId());
               tripleBeds.add(destination.getEntityBeId());
               reducedFormGraph.addGraphComponent(source, null);
               reducedFormGraph.addGraphComponent(relation, source);
               reducedFormGraph.addGraphComponent(destination, relation);

               start = start + 2;
               i = i + 2;
            }
            graphPath.setPath(mergedComps);
            reducedFormGraph.addPath(graphPath);
         }

         Collection<IGraphComponent> rootVertices = semanticPossibility.getRootVertices();
         for (IGraphComponent rv : rootVertices) {
            DomainRecognition entity = (DomainRecognition) rv;
            // Skip if the concept is already part of triple concept beds
            if (tripleBeds.contains(entity.getEntityBeId()) && entity.getNormalizedData() == null) {
               continue;
            }
            // TODO: Should check if we can fit into existing graph path
            reducedFormGraph.addGraphComponent(entity, null);
         }
      }

      mergedPossibility.setReducedForm(reducedFormGraph);
      return mergedPossibility;
   }

   /**
    * Method to optimize the grapth while removing the extra recognitions and paths for single instance concepts.
    * 
    * @param semanticPossibility
    * @param conceptsConsideredForSingleInstance
    * @param requiredInstanceRecQuality
    */
   public void optimizeGraph (SemanticPossibility semanticPossibility, Set<String> conceptsConsideredForSingleInstance,
            double requiredInstanceRecQuality) {
      Graph reducedForm = semanticPossibility.getReducedForm();
      Set<IGraphComponent> removedVertex = new HashSet<IGraphComponent>(1);

      for (String conceptName : conceptsConsideredForSingleInstance) {

         // Prepare the connected, unconnected vertices information each time as they might get updated in each
         // iteration
         Collection<IGraphComponent> connectedVertices = NLPUtils.getConnectedVertices(reducedForm);
         Collection<IGraphComponent> unconnectedVertices = NLPUtils.getUnconnectedVertices(reducedForm);
         Map<String, List<IGraphComponent>> connectedVerticesByConceptNameMap = getGraphComponentsByConceptNameMap(connectedVertices);
         Map<String, List<IGraphComponent>> unconnectedVerticesByConceptNameMap = getGraphComponentsByConceptNameMap(unconnectedVertices);

         // Populate paths which attach instances of single instance concept and populate paths which attach single
         // instance concept entity.
         List<IGraphPath> singleInstanceToInstancePaths = new ArrayList<IGraphPath>(1);
         List<IGraphPath> singleInstancePaths = new ArrayList<IGraphPath>(1);
         List<IGraphPath> singleInstanceConceptPaths = new ArrayList<IGraphPath>(1);
         Set<Long> existingPathConceptIds = new HashSet<Long>(1);
         populatePathsForSingleInstanceConcepts(reducedForm, conceptName, singleInstancePaths,
                  singleInstanceConceptPaths, existingPathConceptIds, singleInstanceToInstancePaths);

         // populate single instance recognition which are not connected with the given concept
         List<IGraphComponent> unconnectedVerticesOfConcept = unconnectedVerticesByConceptNameMap.get(conceptName
                  .toLowerCase());
         List<IGraphComponent> singleInstanceUnconnectedRecognitions = new ArrayList<IGraphComponent>(1);
         List<IGraphComponent> singleInstanceUnconnectedFirstSentenceRecognitions = new ArrayList<IGraphComponent>(1);
         populateUnconnectedSingleInstanceRecognitions(reducedForm, unconnectedVerticesOfConcept,
                  singleInstanceUnconnectedRecognitions, singleInstanceUnconnectedFirstSentenceRecognitions,
                  requiredInstanceRecQuality);

         // populate single instance recognition which are connected with the given concept.
         List<IGraphComponent> connectedVerticesOfConcept = connectedVerticesByConceptNameMap.get(conceptName
                  .toLowerCase());
         List<IGraphComponent> singleInstanceConnectedFirstSentenceRecognitions = new ArrayList<IGraphComponent>(1);
         populateConnectedSingleInstanceRecognitions(reducedForm, connectedVerticesOfConcept,
                  singleInstanceConnectedFirstSentenceRecognitions, requiredInstanceRecQuality);

         // First priority is the title of the article i.e. first sentence(possibility) unconnected good quality
         // recognitions
         if (!CollectionUtils.isEmpty(singleInstanceUnconnectedFirstSentenceRecognitions)
                  && CollectionUtils.isEmpty(singleInstanceConnectedFirstSentenceRecognitions)) {
            // Remove all the unconnected recognitions for the single Instance Concept if a Path already exists
            removeUnconnectedSingleInstanceRecognitions(reducedForm, removedVertex,
                     singleInstanceUnconnectedRecognitions);

            // Remove paths which attach single instance concept entity
            removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstanceConceptPaths, null,
                     conceptName);

            // Remove instance to concept can be removed i.e chevy-isrealizedAs-Impala is
            // found then make-isRelaizedAs-Impala can be removed where model is current concept.
            removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstancePaths, null, conceptName);

            List<IGraphComponent> usedVertices = new ArrayList<IGraphComponent>(1);

            if (singleInstanceUnconnectedFirstSentenceRecognitions.size() > 1) {
               NLPUtils.sortComponentsBySentenceIdAndQuality(singleInstanceUnconnectedFirstSentenceRecognitions);
               DomainRecognition domainRecognition = (DomainRecognition) singleInstanceUnconnectedFirstSentenceRecognitions
                        .get(0);
               for (IGraphComponent graphComponent : singleInstanceUnconnectedFirstSentenceRecognitions) {
                  DomainRecognition recognition = (DomainRecognition) graphComponent;
                  if (!domainRecognition.equals(recognition)) {
                     removedVertex.add(recognition);
                     reducedForm.removeVertex(recognition);
                  }
               }
            }

            // Method to remove the extra single Instance to Instance Paths and the unused vertex they connect
            removeInstanceToInstancePaths(reducedForm, removedVertex, singleInstanceToInstancePaths,
                     existingPathConceptIds, usedVertices, conceptName);

         } else if (singleInstancePaths.size() > 0 || singleInstanceToInstancePaths.size() > 0) {
            // Remove all the unconnected recognitions for the single Instance Concept if a path already exists
            removeUnconnectedSingleInstanceRecognitions(reducedForm, removedVertex,
                     singleInstanceUnconnectedRecognitions);
            removeUnconnectedSingleInstanceRecognitions(reducedForm, removedVertex,
                     singleInstanceUnconnectedFirstSentenceRecognitions);

            if (singleInstanceToInstancePaths.size() > 0) {
               // sort the path by proximity of sentence first, then quality, then paths start and end proximity
               NLPUtils.sortPathsByProximity(singleInstanceToInstancePaths);
               IGraphPath graphPath = singleInstanceToInstancePaths.get(0);
               // remove paths which attach single Instance Concept entity if the corresponding single Instance Path is
               // present.
               removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstanceConceptPaths, graphPath,
                        conceptName);
               // if instance to instance paths are found instance to concept can be removed i.e
               // chevy-isrealizedAs-Impala is
               // found then make-isRelaizedAs-Impala can be removed where model is current concept.
               removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstancePaths, graphPath,
                        conceptName);
               List<IGraphComponent> usedVertices = new ArrayList<IGraphComponent>(1);
               usedVertices.add(graphPath.getStartVertex());
               usedVertices.add(graphPath.getEndVertex());
               // Method to remove the extra single Instance Paths and the unused Vertice they conncet
               removeExtraPathsAndRecognition(reducedForm, removedVertex, singleInstanceToInstancePaths,
                        existingPathConceptIds, graphPath, usedVertices, conceptName);
            } else {
               // sort the path by proximity of sentence first, then quality, then paths start and end proximity
               NLPUtils.sortPathsByProximity(singleInstancePaths);
               IGraphPath graphPath = singleInstancePaths.get(0);
               removePathsForSingleInstanceConcepts(reducedForm, removedVertex, singleInstanceConceptPaths, graphPath,
                        conceptName);

               List<IGraphComponent> usedVertices = new ArrayList<IGraphComponent>(1);
               usedVertices.add(graphPath.getStartVertex());
               usedVertices.add(graphPath.getEndVertex());
               // Method to remove the extra single Instance Paths and the unused vertex they connect
               removeExtraPathsAndRecognition(reducedForm, removedVertex, singleInstancePaths, existingPathConceptIds,
                        graphPath, usedVertices, conceptName);
            }
         } else if (!CollectionUtils.isEmpty(singleInstanceUnconnectedRecognitions)
                  && singleInstanceUnconnectedRecognitions.size() > 1) {
            singleInstanceUnconnectedRecognitions.addAll(singleInstanceUnconnectedFirstSentenceRecognitions);
            NLPUtils.sortComponentsBySentenceIdAndQuality(singleInstanceUnconnectedRecognitions);
            DomainRecognition domainRecognition = getBestRecognitionBasedOnQualityThreshold(
                     singleInstanceUnconnectedRecognitions, requiredInstanceRecQuality);
            for (IGraphComponent graphComponent : singleInstanceUnconnectedRecognitions) {
               DomainRecognition recognition = (DomainRecognition) graphComponent;
               if (domainRecognition == null || !domainRecognition.equals(recognition)) {
                  removedVertex.add(recognition);
                  reducedForm.removeVertex(recognition);
               }
            }
         }
      }
      semanticPossibility.setRemovedVertex(removedVertex);
   }

   private DomainRecognition getBestRecognitionBasedOnQualityThreshold (
            List<IGraphComponent> singleInstanceUnconnectedRecognitions, double requiredInstanceRecQuality) {
      DomainRecognition rec = null;
      for (IGraphComponent graphComponent : singleInstanceUnconnectedRecognitions) {
         if (((DomainRecognition) graphComponent).getWeightInformation().getRecognitionQuality() >= requiredInstanceRecQuality) {
            rec = (DomainRecognition) graphComponent;
            break;
         }
      }
      return rec;
   }

   private Map<String, List<IGraphComponent>> getGraphComponentsByConceptNameMap (
            Collection<IGraphComponent> unconnectedVertices) {
      Map<String, List<IGraphComponent>> unconnectedVerticesByConceptNameMap = new HashMap<String, List<IGraphComponent>>(
               1);

      if (CollectionUtils.isEmpty(unconnectedVertices)) {
         return unconnectedVerticesByConceptNameMap;
      }

      for (IGraphComponent graphComponent : unconnectedVertices) {
         DomainRecognition rec = (DomainRecognition) graphComponent;
         if (rec.getConceptName() == null) {
            continue;
         }
         String conceptName = rec.getConceptName().toLowerCase();
         List<IGraphComponent> list = unconnectedVerticesByConceptNameMap.get(conceptName);
         if (list == null) {
            list = new ArrayList<IGraphComponent>(1);
            unconnectedVerticesByConceptNameMap.put(conceptName, list);
         }
         list.add(rec);
      }

      return unconnectedVerticesByConceptNameMap;
   }

   /**
    * @param reducedForm
    * @param removedVertex
    * @param singleInstanceRecognitions
    */
   private void removeUnconnectedSingleInstanceRecognitions (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphComponent> singleInstanceRecognitions) {
      for (IGraphComponent singleInstanceRec : singleInstanceRecognitions) {
         removedVertex.add(singleInstanceRec);
         reducedForm.removeVertex(singleInstanceRec);
      }
   }

   /**
    * @param reducedForm
    * @param removedVertex
    * @param singleInstanceConceptPaths
    * @param conceptName
    * @param graphPathToBeRetain
    */
   private void removePathsForSingleInstanceConcepts (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphPath> singleInstanceConceptPaths, IGraphPath graphPathToBeRetain, String conceptName) {
      for (IGraphPath graphPath1 : singleInstanceConceptPaths) {
         DomainRecognition endVertex = (DomainRecognition) graphPath1.getEndVertex();
         DomainRecognition startVertex = (DomainRecognition) graphPath1.getStartVertex();
         if (graphPathToBeRetain != null) {
            DomainRecognition endVertexSelected = (DomainRecognition) graphPathToBeRetain.getEndVertex();
            DomainRecognition startVertexSelected = (DomainRecognition) graphPathToBeRetain.getStartVertex();
            if (startVertexSelected.getConceptName().equalsIgnoreCase(conceptName)
                     && startVertexSelected.equals(endVertex)) {
               continue;
            } else if (endVertexSelected.getConceptName().equalsIgnoreCase(conceptName)
                     && endVertexSelected.equals(startVertex)) {
               continue;
            }
         }
         reducedForm.removePath(graphPath1);
         int pathLength = graphPath1.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath1.getPartialPath(start, i);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            removedVertex.add(destination);
            reducedForm.removeVertex(destination);
            start = start + 2;
            i = i + 2;
         }
      }
   }

   private void removeInstanceToInstancePaths (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphPath> singleInstanceToInstancePaths, Set<Long> existingPathConceptIds,
            List<IGraphComponent> usedVertices, String conceptName) {
      if (CollectionUtils.isEmpty(singleInstanceToInstancePaths)) {
         return;
      }

      for (IGraphPath graphPath1 : singleInstanceToInstancePaths) {
         int startIndex = 0;
         int pathLength = graphPath1.getPathLength();
         for (int endIndex = 3; endIndex <= pathLength;) {
            List<IGraphComponent> components = graphPath1.getPartialPath(startIndex, endIndex);
            DomainRecognition startVertex = (DomainRecognition) components.get(0);
            DomainRecognition endVertex = (DomainRecognition) components.get(2);

            if (!usedVertices.contains(endVertex)) {
               removedVertex.add(endVertex);
               reducedForm.removeVertex(endVertex);
            }
            if (!usedVertices.contains(startVertex) && existingPathConceptIds.contains(startVertex.getConceptBEDId())) {
               removedVertex.add(startVertex);
               reducedForm.removeVertex(startVertex);
            }
            startIndex = startIndex + 2;
            endIndex = endIndex + 2;
         }
         reducedForm.removePath(graphPath1);
      }
   }

   /**
    * Method to remove the extra recognition and paths for the single instance concepts.
    * 
    * @param reducedForm
    * @param removedVertex
    * @param singleInstanceToInstancePaths
    * @param existingPathConceptIds
    * @param graphPathToRetain
    * @param usedVertices
    * @param conceptName
    */
   private void removeExtraPathsAndRecognition (Graph reducedForm, Set<IGraphComponent> removedVertex,
            List<IGraphPath> singleInstanceToInstancePaths, Set<Long> existingPathConceptIds,
            IGraphPath graphPathToRetain, List<IGraphComponent> usedVertices, String conceptName) {
      if (CollectionUtils.isEmpty(singleInstanceToInstancePaths) || graphPathToRetain == null) {
         return;
      }
      List<List<IGraphComponent>> pathsToRetain = new ArrayList<List<IGraphComponent>>(1);
      int pathToRetainStartIndex = 0;
      int pathToRetainLength = graphPathToRetain.getPathLength();

      // Add the partial paths to paths to retain list
      for (int pathToRetainEndIndex = 3; pathToRetainEndIndex <= pathToRetainLength;) {
         List<IGraphComponent> components = graphPathToRetain.getPartialPath(pathToRetainStartIndex,
                  pathToRetainEndIndex);
         pathsToRetain.add(components);
         pathToRetainStartIndex = pathToRetainStartIndex + 2;
         pathToRetainEndIndex = pathToRetainEndIndex + 2;
      }

      for (List<IGraphComponent> pathToRetain : pathsToRetain) {
         DomainRecognition pathToRetainStartVertex = (DomainRecognition) pathToRetain.get(0);
         DomainRecognition pathToRetainEndVertex = (DomainRecognition) pathToRetain.get(2);
         for (IGraphPath graphPath1 : singleInstanceToInstancePaths) {
            // If path to retain and current path is same, then continue
            if (graphPath1.equals(graphPathToRetain) || graphPath1.intersectsWith(graphPathToRetain)) {
               continue;
            }

            reducedForm.removePath(graphPath1);

            int startIndex = 0;
            int pathLength = graphPath1.getPathLength();
            for (int endIndex = 3; endIndex <= pathLength;) {
               List<IGraphComponent> components = graphPath1.getPartialPath(startIndex, endIndex);
               DomainRecognition startVertex = (DomainRecognition) components.get(0);
               DomainRecognition endVertex = (DomainRecognition) components.get(2);
               startIndex = startIndex + 2;
               endIndex = endIndex + 2;

               // If start and end vertex are not of same concept, then continue
               if (!startVertex.getConceptName().equalsIgnoreCase(pathToRetainStartVertex.getConceptName())
                        || !endVertex.getConceptName().equalsIgnoreCase(pathToRetainEndVertex.getConceptName())) {
                  continue;
               }

               // If start vertex is the current single instance but start vertex is same as the selected end vertex
               if (pathToRetainStartVertex.getConceptName().equalsIgnoreCase(conceptName)
                        && pathToRetainStartVertex.equals(endVertex)) {
                  continue;
               } else if (pathToRetainEndVertex.getConceptName().equalsIgnoreCase(conceptName)
                        && pathToRetainEndVertex.equals(startVertex)) {
                  continue;
               }

               if (!usedVertices.contains(endVertex)) {
                  removedVertex.add(endVertex);
                  reducedForm.removeVertex(endVertex);
               }

               if (!usedVertices.contains(startVertex)
                        && existingPathConceptIds.contains(startVertex.getConceptBEDId())) {
                  removedVertex.add(startVertex);
                  reducedForm.removeVertex(startVertex);
               }
            }
         }
      }
   }

   /**
    * @param reducedForm
    * @param singleInstanceUnconnectedRecognitions
    * @param singleInstanceUnconnectedFirstSentenceRecognitions
    * @param requiredInstanceRecQuality
    * @param singleInstanceConnectedFirstSentenceRecognitions2
    */
   private void populateUnconnectedSingleInstanceRecognitions (Graph reducedForm,
            List<IGraphComponent> unconnectedVertices, List<IGraphComponent> singleInstanceUnconnectedRecognitions,
            List<IGraphComponent> singleInstanceUnconnectedFirstSentenceRecognitions, double requiredInstanceRecQuality) {
      if (CollectionUtils.isEmpty(unconnectedVertices)) {
         return;
      }
      for (IGraphComponent gc : unconnectedVertices) {
         DomainRecognition rec = (DomainRecognition) gc;
         if (rec.getDefaultInstanceValue() == null) {
            continue;
         }
         INormalizedData normalizedData = rec.getNormalizedData();
         if (normalizedData != null
                  && normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            updateAndMakeSingleInstance(rec, reducedForm);
         }

         if (rec.getSentenceId().equals(1)
                  && rec.getWeightInformation().getRecognitionQuality() >= requiredInstanceRecQuality) {
            singleInstanceUnconnectedFirstSentenceRecognitions.add(rec);
         } else {
            singleInstanceUnconnectedRecognitions.add(rec);
         }
      }
   }

   /**
    * @param reducedForm
    * @param connectedVertices
    * @param singleInstanceConnectedFirstSentenceRecognitions
    * @param requiredInstanceRecQuality
    */
   private void populateConnectedSingleInstanceRecognitions (Graph reducedForm,
            List<IGraphComponent> connectedVertices,
            List<IGraphComponent> singleInstanceConnectedFirstSentenceRecognitions, double requiredInstanceRecQuality) {
      if (CollectionUtils.isEmpty(connectedVertices)) {
         return;
      }
      for (IGraphComponent gc : connectedVertices) {
         DomainRecognition rec = (DomainRecognition) gc;
         if (rec.getDefaultInstanceValue() == null
                  || rec.getWeightInformation().getRecognitionQuality() < requiredInstanceRecQuality) {
            continue;
         }
         if (rec.getSentenceId().equals(1)) {
            singleInstanceConnectedFirstSentenceRecognitions.add(rec);
         }
      }
   }

   /**
    * @param reducedForm
    * @param conceptName
    * @param singleInstancePaths
    * @param singleInstanceConceptPaths
    * @param existingPathConceptIds
    * @param singleInstanceToInstancePaths
    */
   private void populatePathsForSingleInstanceConcepts (Graph reducedForm, String conceptName,
            List<IGraphPath> singleInstancePaths, List<IGraphPath> singleInstanceConceptPaths,
            Set<Long> existingPathConceptIds, List<IGraphPath> singleInstanceToInstancePaths) {
      List<IGraphPath> paths = reducedForm.getPaths();
      for (IGraphPath graphPath : paths) {
         GraphPath path = (GraphPath) graphPath;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);
            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            if (source.getConceptBEDId() != null) {
               existingPathConceptIds.add(source.getConceptBEDId());
            }
            if (destination.getConceptBEDId() != null) {
               existingPathConceptIds.add(destination.getConceptBEDId());
            }
            INormalizedData normalizedData = destination.getNormalizedData();
            if (conceptName.equals(destination.getConceptName()) && normalizedData != null
                     && normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               updateAndMakeSingleInstance(destination, reducedForm);
            }
            normalizedData = source.getNormalizedData();
            if (conceptName.equals(source.getConceptName()) && normalizedData != null
                     && normalizedData.getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               updateAndMakeSingleInstance(source, reducedForm);
            }
            if (conceptName.equalsIgnoreCase(source.getConceptName()) && source.getDefaultInstanceBedId() != null
                     && destination.getDefaultInstanceBedId() != null) {
               singleInstanceToInstancePaths.add(path);
            } else if (conceptName.equals(source.getConceptName()) && source.getDefaultInstanceBedId() != null) {
               singleInstancePaths.add(path);
            } else if (conceptName.equals(source.getConceptName())) {
               singleInstanceConceptPaths.add(path);
            } else if (conceptName.equalsIgnoreCase(destination.getConceptName())
                     && destination.getDefaultInstanceBedId() != null && source.getDefaultInstanceBedId() != null) {
               singleInstanceToInstancePaths.add(path);
            } else if (conceptName.equals(destination.getConceptName())
                     && destination.getDefaultInstanceBedId() != null) {
               singleInstancePaths.add(path);
            } else if (conceptName.equals(destination.getConceptName())) {
               singleInstanceConceptPaths.add(path);
            }
            start = start + 2;
            i = i + 2;
         }
      }
   }

   private void updateAndMakeSingleInstance (DomainRecognition domainRec, Graph reducedForm) {
      INormalizedData normalizedData = domainRec.getNormalizedData();
      if (normalizedData == null || normalizedData.getNormalizedDataType() != NormalizedDataType.LIST_NORMALIZED_DATA) {
         return;
      }
      String key = getBusinessEntityName(domainRec) + domainRec.getConceptName() + "-" + domainRec.getPosition();
      ListNormalizedData listNormalizedData = (ListNormalizedData) normalizedData;
      NormalizedDataEntity bestEntity = chooseBestNormalizedEntity(listNormalizedData.getNormalizedDataEntities());
      domainRec.setNormalizedData(bestEntity.getNormalizedData());
      InstanceInformation instanceInformation = new InstanceInformation();
      instanceInformation.setInstanceBedId(bestEntity.getValueBedId());
      instanceInformation.setInstanceDisplayName(bestEntity.getDisplayValue());
      instanceInformation.setInstanceValue(bestEntity.getValue());
      domainRec.getInstanceInformations().clear();
      domainRec.getInstanceInformations().add(instanceInformation);
      String newKey = getBusinessEntityName(domainRec) + domainRec.getConceptName() + "-" + domainRec.getPosition();
      reducedForm.getVertices().remove(key);
      reducedForm.getVertices().put(newKey, domainRec);

   }

   /**
    * @param recognition
    * @return
    */
   private String getBusinessEntityName (DomainRecognition recognition) {
      String businessEntityName = "";
      if (recognition.getDefaultInstanceValue() != null) {
         int i = 0;
         for (String name : recognition.getInstanceNames()) {
            if (i > 0) {
               businessEntityName = businessEntityName + ",";
            }
            businessEntityName = businessEntityName + name;
            i++;
         }
      } else {
         businessEntityName = recognition.getBusinessEntityName();
      }
      return businessEntityName;
   }

   /**
    * @param normalizedDataEntities
    * @return
    */
   public static NormalizedDataEntity chooseBestNormalizedEntity (List<NormalizedDataEntity> normalizedDataEntities) {
      if (CollectionUtils.isEmpty(normalizedDataEntities)) {
         return null;
      }
      double quality = 0;
      Collections.sort(normalizedDataEntities, new Comparator<NormalizedDataEntity>() {

         public int compare (NormalizedDataEntity p1, NormalizedDataEntity p2) {
            if (p1.getWeightInformation().getRecognitionQuality() > p2.getWeightInformation().getRecognitionQuality()) {
               return -1;
            } else if (p1.getWeightInformation().getRecognitionQuality() == p2.getWeightInformation()
                     .getRecognitionQuality()) {
               return 0;
            } else {
               return 1;
            }
         }
      });
      List<NormalizedDataEntity> bestRecognitions = new ArrayList<NormalizedDataEntity>(1);
      for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
         if (quality == 0) {
            quality = normalizedDataEntity.getWeightInformation().getRecognitionQuality();
            bestRecognitions.add(normalizedDataEntity);
         } else if (quality == normalizedDataEntity.getWeightInformation().getRecognitionQuality()) {
            bestRecognitions.add(normalizedDataEntity);
         } else {
            break;
         }
      }
      NormalizedDataEntity bestEntity = bestRecognitions.get(0);
      return bestEntity;
   }

   public static Set<String> getRemovedVertices (List<SemanticPossibility> semanticPossibilities) {

      Set<String> removedVertices = new HashSet<String>(1);
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return removedVertices;
      }
      for (SemanticPossibility semanticPossibility : semanticPossibilities) {
         Set<IGraphComponent> removedVertex = semanticPossibility.getRemovedVertex();
         for (IGraphComponent graphComponent : removedVertex) {
            DomainRecognition domainRecognition = (DomainRecognition) graphComponent;
            removedVertices.addAll(ExecueStringUtil.getAsList(domainRecognition.getKeyWordMatchName().toLowerCase()));
         }
      }
      return removedVertices;
   }

   public static Integer getEntityCount (SemanticPossibility semanticPossibility) {
      Graph reducedFormGraph = semanticPossibility.getReducedForm();
      List<IGraphPath> graphPaths = new ArrayList<IGraphPath>(1);
      if (!CollectionUtils.isEmpty(reducedFormGraph.getPaths())) {
         graphPaths.addAll(reducedFormGraph.getPaths());
      }
      Set<Long> ids = new HashSet<Long>(1);
      for (IGraphPath path : graphPaths) {
         GraphPath graphPath = (GraphPath) path;
         int pathLength = graphPath.getPathLength();
         int start = 0;
         for (int i = 3; i <= pathLength;) {
            List<IGraphComponent> components = graphPath.getPartialPath(start, i);

            DomainRecognition source = (DomainRecognition) components.get(0);
            DomainRecognition relation = (DomainRecognition) components.get(1);
            DomainRecognition destination = (DomainRecognition) components.get(2);
            if (source.getNormalizedData() != null
                     && source.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               ListNormalizedData normalizedData = (ListNormalizedData) source.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
               for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                  if (normalizedDataEntity.getValueBedId() != null) {
                     ids.add(normalizedDataEntity.getValueBedId());
                  }
               }
            } else {
               ids.add(source.getEntityBeId());
            }
            ids.add(relation.getEntityBeId());
            if (destination.getNormalizedData() != null
                     && destination.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
               ListNormalizedData normalizedData = (ListNormalizedData) destination.getNormalizedData();
               List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
               for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
                  if (normalizedDataEntity.getValueBedId() != null) {
                     ids.add(normalizedDataEntity.getValueBedId());
                  }
               }
            } else {
               ids.add(destination.getEntityBeId());
            }
            start = start + 2;
            i = i + 2;
         }
      }
      Collection<IGraphComponent> rootVertices = semanticPossibility.getRootVertices();
      for (IGraphComponent rv : rootVertices) {
         DomainRecognition entity = (DomainRecognition) rv;
         if (entity.getNormalizedData() != null
                  && entity.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            ListNormalizedData normalizedData = (ListNormalizedData) entity.getNormalizedData();
            List<NormalizedDataEntity> normalizedDataEntities = normalizedData.getNormalizedDataEntities();
            for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
               if (normalizedDataEntity.getValueBedId() != null) {
                  ids.add(normalizedDataEntity.getValueBedId());
               }
            }
         } else {
            ids.add(entity.getEntityBeId());
         }
      }
      return ids.size();
   }

   public String getKeyWordMatchText (SemanticPossibility semanticPossibility, Map<Long, Integer> conceptPriorityMap) {

      StringBuilder sb = new StringBuilder(1);
      List<IGraphComponent> allGraphComponents = semanticPossibility.getAllGraphComponents();
      if (CollectionUtils.isEmpty(allGraphComponents)) {
         return sb.toString();
      }

      Set<String> nonPriorityWords = new TreeSet<String>();
      Map<Integer, String> priorityWordsMap = new TreeMap<Integer, String>();
      for (IGraphComponent graphComponent : allGraphComponents) {
         DomainRecognition domainRecognition = (DomainRecognition) graphComponent;
         if (conceptPriorityMap.containsKey(domainRecognition.getConceptBEDId())) {
            if (isValidValue(domainRecognition)) {
               if (domainRecognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
                  Object[] operatorAndValue = getOperatorAndValue(domainRecognition.getNormalizedData());
                  priorityWordsMap.put(conceptPriorityMap.get(domainRecognition.getConceptBEDId()), domainRecognition
                           .getConceptName()
                           + operatorAndValue[1].toString());
               }
            } else {
               priorityWordsMap.put(conceptPriorityMap.get(domainRecognition.getConceptBEDId()), domainRecognition
                        .getKeyWordMatchName());
            }
         } else {
            if (isValidValue(domainRecognition)) {
               if (domainRecognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA) {
                  Object[] operatorAndValue = getOperatorAndValue(domainRecognition.getNormalizedData());
                  if (operatorAndValue != null && operatorAndValue[1] != null) {
                     nonPriorityWords.add(domainRecognition.getConceptName() + operatorAndValue[1].toString());
                  }
               }
            } else {
               // Non Priority words will be in alphabetical order
               nonPriorityWords.add(domainRecognition.getKeyWordMatchName());
            }
         }
      }
      Object[] priorityWords = priorityWordsMap.values().toArray();
      CollectionUtils.reverseArray(priorityWords);
      String priorityWordsString = StringUtils.join(priorityWords, " ").trim();
      String nonPriorityWordString = StringUtils.join(nonPriorityWords, " ").trim();
      sb.append(priorityWordsString).append(" ").append(nonPriorityWordString);
      return sb.toString().trim();
   }

   public static boolean isValidValue (DomainRecognition recognition) {
      INormalizedData normalizedData = recognition.getNormalizedData();
      if (normalizedData != null) {
         if (isValue(normalizedData)) {
            return true;
         } else if (recognition.getNormalizedData().getNormalizedDataType() == NormalizedDataType.LIST_NORMALIZED_DATA) {
            ListNormalizedData listNormalizedData = (ListNormalizedData) recognition.getNormalizedData();
            List<NormalizedDataEntity> normalizedDataEntities = listNormalizedData.getNormalizedDataEntities();
            NormalizedDataEntity normalizedDataEntity = normalizedDataEntities.get(0);
            INormalizedData firstNormalizedData = normalizedDataEntity.getNormalizedData();
            return isValue(firstNormalizedData);
         }
      }
      return false;
   }

   public static boolean isValue (INormalizedData normalizedData) {
      if (normalizedData == null) {
         return false;
      }
      return normalizedData.getNormalizedDataType() == NormalizedDataType.TIME_FRAME_NORMALIZED_DATA
               || normalizedData.getNormalizedDataType() == NormalizedDataType.VALUE_NORMALIZED_DATA
               || normalizedData.getNormalizedDataType() == NormalizedDataType.RANGE_NORMALIZED_DATA
               || normalizedData.getNormalizedDataType() == NormalizedDataType.RELATIVE_TIME_NORMALIZED_DATA;
   }

   /**
    * @return the typeConvertorFactory
    */
   public TypeConvertorFactory getTypeConvertorFactory () {
      return typeConvertorFactory;
   }

   /**
    * @param typeConvertorFactory
    *           the typeConvertorFactory to set
    */
   public void setTypeConvertorFactory (TypeConvertorFactory typeConvertorFactory) {
      this.typeConvertorFactory = typeConvertorFactory;
   }

   /**
    * @return the conversionService
    */
   public IConversionService getConversionService () {
      return conversionService;
   }

   /**
    * @param conversionService
    *           the conversionService to set
    */
   public void setConversionService (IConversionService conversionService) {
      this.conversionService = conversionService;
   }
}