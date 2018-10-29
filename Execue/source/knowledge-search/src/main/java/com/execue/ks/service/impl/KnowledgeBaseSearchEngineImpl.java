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


package com.execue.ks.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.InstanceTripleDefinition;
import com.execue.core.common.bean.entity.KnowledgeSearchResultItem;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphComponentType;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.kb.DataConnectionEntity;
import com.execue.core.common.bean.kb.DataEntity;
import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.qdata.RFXValue;
import com.execue.core.common.bean.qdata.RIInstanceTripleDefinition;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.bean.qdata.ReducedFormIndex;
import com.execue.core.common.bean.qdata.UserQueryReducedFormIndex;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConnectionEndPointType;
import com.execue.core.common.type.PathProcessingType;
import com.execue.core.common.type.RFXType;
import com.execue.core.common.type.RFXValueType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.common.type.SearchType;
import com.execue.core.exception.UidException;
import com.execue.ks.bean.KBConnectionEntity;
import com.execue.ks.bean.KBDataEntity;
import com.execue.ks.bean.KBSearchResult;
import com.execue.ks.exception.KnowledgeSearchException;
import com.execue.ks.service.IKnowledgeBaseSearchEngine;
import com.execue.ks.service.UserQueryRFXFactory;
import com.execue.platform.IUidService;
import com.execue.qdata.exception.RFXException;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IRFXService;
import com.execue.qdata.service.IUDXService;
import com.execue.sus.helper.RFXServiceHelper;
import com.execue.sus.helper.ReducedFormHelper;
import com.execue.sus.helper.SemanticUtil;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.execue.util.MathUtil;

/**
 * @author Abhijit
 * @since Jul 4, 2009 : 10:56:41 AM
 */
public class KnowledgeBaseSearchEngineImpl implements IKnowledgeBaseSearchEngine {

   private static Logger                   logger = Logger.getLogger(KnowledgeBaseSearchEngineImpl.class);

   private IPathDefinitionRetrievalService pathDefinitionRetrievalService;
   private IUidService                     rfIdGenerationService;
   private IUDXService                     udxService;
   private IRFXService                     rfxService;
   private RFXServiceHelper                rfxServiceHelper;

   // Logic Methods

   /**
    * Returns a Structured Object as result of Knowledge Search on passed Semantic Possibility Object
    * 
    * @param possibility
    *           Object representing a possible meaning of User Query
    * @return KBSearchResult object Representing the outcome of Knowledge Search for missing information on passed
    *         meaning
    * @throws KnowledgeSearchException
    */
   public KBSearchResult searchAndOrganize (SemanticPossibility possibility) throws KnowledgeSearchException {
      return getInferredDomainEntities(possibility);
   }

   /**
    * Returns List of Instance Path Definition that represents knowledge ibtained based on passed meaning of User query
    * 
    * @param possibility
    *           Object representing a possible meaning of User Query
    * @return List of Instance Path Definition objects representing the knowledge obtained based on passed meaning of
    *         User Query
    * @throws KnowledgeSearchException
    */
   public List<InstancePathDefinition> search (SemanticPossibility possibility) throws KnowledgeSearchException {
      Graph rf = possibility.getReducedForm();
      List<IGraphPath> paths = rf.getPaths();
      List<InstancePathDefinition> instancePathDefinitions = new ArrayList<InstancePathDefinition>();
      Map<IGraphPath, List<InstancePathDefinition>> instancePathDefinitionsMap = new HashMap<IGraphPath, List<InstancePathDefinition>>();
      Map<IGraphPath, List<InstancePathDefinition>> finalInstancePathDefinitionsMap = new HashMap<IGraphPath, List<InstancePathDefinition>>();

      if (paths != null) {
         for (IGraphPath path : paths) {
            DomainRecognition source = (DomainRecognition) path.getStartVertex();
            DomainRecognition destination = (DomainRecognition) path.getEndVertex();
            DomainRecognition relation = (DomainRecognition) path.getConnectors().get(0);
            try {
               Long pathID = getPathDefinitionRetrievalService().getDirectPathDefinitionIdByETDNames(
                        source.getConceptName(), relation.getConceptName(), destination.getConceptName());
               if (pathID != null) {
                  boolean isSrcInstance = NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(source.getNlpTag());
                  boolean isDestInstance = NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(destination.getNlpTag());

                  // Get the Instance Path Definitions and missing Instance Domain Entities for the same
                  // based on information available in User Query
                  if (!isSrcInstance && isDestInstance) {
                     // If destination instance DED ID is known and Path ID is known then get the source instance DED
                     // IDs
                     List<InstancePathDefinition> tempDefs = getPathDefinitionRetrievalService()
                              .getInstancePathDefinitionsByDestinationAndPath(destination.getConceptBEDId(), pathID);
                     instancePathDefinitionsMap.put(path, tempDefs);
                  } else if (isSrcInstance && !isDestInstance) {
                     // If source instance DED ID is known and Path ID is known then get the destination instance DED
                     // IDs
                     List<InstancePathDefinition> tempDefs = getPathDefinitionRetrievalService()
                              .getInstancePathDefinitionsBySourceAndPath(source.getConceptBEDId(), pathID);
                     instancePathDefinitionsMap.put(path, tempDefs);
                  } else if (!isSrcInstance && !isDestInstance) {
                     // If only Path ID is known then get the source instance DED ID and the destination instance DED
                     // IDs
                     List<InstancePathDefinition> tempDefs = getPathDefinitionRetrievalService()
                              .getInstancePathDefinitionsByPath(pathID);
                     instancePathDefinitionsMap.put(path, tempDefs);
                  }
               }
            } catch (SWIException e) {
               throw new KnowledgeSearchException(e.getCode(), e);
            }
         }
      }
      // If there is only one Graph Path then return the Instance Path definitions for it as Output
      // If there are more than one Paths then we need to do extra processing for Filtering them
      if (paths != null && paths.size() == 1) {
         for (Map.Entry<IGraphPath, List<InstancePathDefinition>> entry : instancePathDefinitionsMap.entrySet()) {
            instancePathDefinitions = entry.getValue();
         }
      } else if (paths != null && paths.size() > 1) {
         // If more than one Graph Paths are there then first get all the Root Vertices
         // For each Root vertex, reach till end of its Leafs and reach back to restrict the
         // Instance Path Definitions for the starting Path
         Collection<IGraphComponent> rootComponents = rf.getRootVertices();
         for (IGraphComponent graphRootComponent : rootComponents) {
            // finalInstancePathDefinitionsMap = filterInstancePaths(rf.getPaths(), instancePathDefinitionsMap,
            // graphRootComponent);
            instancePathDefinitions.addAll(filterPathsByChildren(rf, instancePathDefinitionsMap, graphRootComponent));
         }
         logger.debug("");
      }
      return instancePathDefinitions;
   }

   private Map<IGraphPath, List<InstancePathDefinition>> filterInstancePaths (List<IGraphPath> rdPaths,
            Map<IGraphPath, List<InstancePathDefinition>> instancePathDefinitionsMap, IGraphComponent gc) {
      // This returns the final List of Instance Path Definitions corresponding to the Paths
      // starting with Graph Componenet passed as parameter
      // For a leaf graph componenet the return value is null
      Map<IGraphPath, List<InstancePathDefinition>> instPathDefMap = new HashMap<IGraphPath, List<InstancePathDefinition>>();
      // Iterate Over all Paths of this Reduced form
      for (IGraphPath rdPath : rdPaths) {
         List<InstancePathDefinition> tempInstancePaths = new ArrayList<InstancePathDefinition>();
         // Check if the Path starts with the Passed IGraphComponent
         if (rdPath.getStartVertex() == gc) {
            // Get the Instance Path Definitions for this Path
            List<InstancePathDefinition> curInstancePathDefs = instancePathDefinitionsMap.get(rdPath);
            // Get the Instance Path Definitions for the Children Path
            Map<IGraphPath, List<InstancePathDefinition>> tempInstancePathDefMap = filterInstancePaths(rdPaths,
                     instancePathDefinitionsMap, rdPath.getEndVertex());
            // If paths from children are NULL i.e. child is a leaf then set the current paths as final paths
            // Else if children have returned some paths then as the End point of this path is
            // starting point of next one, the Instance Path definitions for this path also need to have the destination
            // Instance Entity as the source instance entity of the children paths
            if (tempInstancePathDefMap != null) {
               instPathDefMap.putAll(tempInstancePathDefMap);
               if (curInstancePathDefs != null) {
                  List<InstancePathDefinition> tempInstancePathDefs = null;
                  for (Map.Entry<IGraphPath, List<InstancePathDefinition>> entry : tempInstancePathDefMap.entrySet()) {
                     IGraphPath tempPath = entry.getKey();
                     if (rdPath.getEndVertex().equals(tempPath.getStartVertex())) {
                        tempInstancePathDefs = entry.getValue();
                        break;
                     }
                  }
                  if (tempInstancePathDefs != null) {
                     for (InstancePathDefinition pathToFilter : curInstancePathDefs) {
                        for (InstancePathDefinition pathToCompare : tempInstancePathDefs) {
                           if (pathToFilter.getDestinationDEDId().equals(pathToCompare.getSourceDEDId())) {
                              tempInstancePaths.add(pathToFilter);
                           }
                        }
                     }
                  } else {
                     tempInstancePaths = curInstancePathDefs;
                  }
               }
               instPathDefMap.put(rdPath, tempInstancePaths);
            }
         }
      }
      return instPathDefMap;
   }

   private List<InstancePathDefinition> filterPathsByChildren (Graph rf,
            Map<IGraphPath, List<InstancePathDefinition>> instancePathDefinitionsMap, IGraphComponent gc) {
      // This returns the final List of Instance Path Definitions corresponding to the Paths
      // starting with Graph Componenet passed as parameter
      // For a leaf graph componenet the return value is null
      Set<InstancePathDefinition> returnInstancePaths = new HashSet<InstancePathDefinition>();
      List<IGraphPath> rdPaths = rf.getPaths();
      Map<IGraphPath, List<InstancePathDefinition>> instPathDefMap = new HashMap<IGraphPath, List<InstancePathDefinition>>();
      // Flag to denote if the passed Graph Components is a leaf Component
      boolean isLeaf = true;
      // Iterate Over all Paths of this Reduced form
      for (IGraphPath rdPath : rdPaths) {
         List<InstancePathDefinition> tempInstancePaths = new ArrayList<InstancePathDefinition>();
         // Check if the Path starts with the Passed IGraphComponent
         if (rdPath.getStartVertex() == gc) {
            // As a path exists with this
            isLeaf = false;
            // Get the Instance Path Definitions for this Path
            List<InstancePathDefinition> curInstancePathDefs = instancePathDefinitionsMap.get(rdPath);
            // Get the Instance Path Definitions for the Children Path
            List<InstancePathDefinition> tempInstancePathDefs = filterPathsByChildren(rf, instancePathDefinitionsMap,
                     rdPath.getEndVertex());
            // If paths from children are NULL i.e. child is a leaf then set the current paths as final paths
            // Else if children have returned some paths then as the End point of this path is
            // starting point of next one, the Instance Path definitions for this path also need to have the destination
            // Instance Entity as the source instance entity of the children paths
            if (tempInstancePathDefs == null && curInstancePathDefs != null) {
               tempInstancePaths.addAll(curInstancePathDefs);
            } else if (curInstancePathDefs != null) {
               for (InstancePathDefinition pathToFilter : curInstancePathDefs) {
                  for (InstancePathDefinition pathToCompare : tempInstancePathDefs) {
                     if (pathToFilter.getDestinationDEDId().equals(pathToCompare.getSourceDEDId())) {
                        tempInstancePaths.add(pathToFilter);
                        tempInstancePaths.add(pathToCompare);
                     }
                  }
               }
            }
            instPathDefMap.put(rdPath, tempInstancePaths);
         }
      }
      if (isLeaf) {
         return null;
      }
      // As all the paths starting from the passed Graph Component should start from the Same Entity
      // all the instance paths also need to have same source instance entities
      for (Map.Entry<IGraphPath, List<InstancePathDefinition>> entry : instPathDefMap.entrySet()) {
         if (returnInstancePaths.isEmpty()) {
            returnInstancePaths.addAll(entry.getValue());
         } else {
            List<InstancePathDefinition> revisedPaths = new ArrayList<InstancePathDefinition>();
            for (InstancePathDefinition currentPath : returnInstancePaths) {
               for (InstancePathDefinition path : entry.getValue()) {
                  if (currentPath.getSourceDEDId().equals(path.getSourceDEDId())
                           || currentPath.getSourceDEDId().equals(path.getDestinationDEDId())
                           || currentPath.getDestinationDEDId().equals(path.getSourceDEDId())) {
                     revisedPaths.add(currentPath);
                     revisedPaths.add(path);
                  }
               }
            }
            returnInstancePaths.addAll(revisedPaths);
         }
      }
      return new ArrayList<InstancePathDefinition>(returnInstancePaths);
   }

   public KBSearchResult getInferredDomainEntities (SemanticPossibility possibility) throws KnowledgeSearchException {
      Graph rf = possibility.getReducedForm();
      List<IGraphPath> paths = rf.getPaths();
      KBSearchResult searchResult = new KBSearchResult();
      if (paths != null) {
         for (IGraphPath path : paths) {
            DomainRecognition source = (DomainRecognition) path.getStartVertex();
            DomainRecognition destination = (DomainRecognition) path.getEndVertex();
            DomainRecognition relation = (DomainRecognition) path.getConnectors().get(0);
            try {
               Long pathID = getPathDefinitionRetrievalService().getDirectPathDefinitionIdByETDNames(
                        source.getConceptName(), relation.getConceptName(), destination.getConceptName());
               if (pathID != null) {
                  boolean isSrcInstance = NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(source.getNlpTag());
                  boolean isDestInstance = NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(destination.getNlpTag());

                  if (!isSrcInstance && isDestInstance) {
                     List<InstancePathDefinition> instancePaths = getPathDefinitionRetrievalService()
                              .getInstancePathDefinitionsByDestinationAndPath(destination.getConceptBEDId(), pathID);
                     if (!instancePaths.isEmpty()) {
                        KBDataEntity result = new KBDataEntity();
                        if (searchResult.containsDataEntity(destination.getConceptBEDId())) {
                           result = searchResult.getDataEntity(destination.getConceptBEDId());
                        }
                        result.setId(destination.getConceptBEDId());
                        result.setType(ConnectionEndPointType.OBJECT);
                        result.setName(destination.getBusinessEntityName());

                        DataConnectionEntity resultLine = new DataConnectionEntity();
                        // resultLine.setId(relation.getDomainEntityID());
                        resultLine.setName(relation.getBusinessEntityName());
                        resultLine.setEntityPathID(pathID);

                        for (InstancePathDefinition requestedPath : instancePaths) {
                           DataEntity item = new DataEntity();
                           item.setId(requestedPath.getSourceDEDId());
                           resultLine.addDataItemForInstancePathID(requestedPath.getId(), item);
                        }
                        result.addConnection(resultLine);
                        searchResult.addDataEntity(result);
                     }
                  }
               }
            } catch (SWIException e) {
               throw new KnowledgeSearchException(e.getCode(), e);
            }
         }
      }
      return searchResult;
   }

   /**
    * Returns object representing rows of User query RFX as a result of Knowledge Search based on Passed Semantic
    * Possibility
    * 
    * @param possibility
    *           Object representing a possible meaning of User Query
    * @return object representing rows of User query RFX
    */
   public Collection<UserQueryReducedFormIndex> searchToRFX (SemanticPossibility possibility) {
      Map<String, UserQueryReducedFormIndex> userQueryRFX = new HashMap<String, UserQueryReducedFormIndex>(1);
      Graph rf = possibility.getReducedForm();
      List<IGraphPath> paths = rf.getPaths();
      if (paths != null) {
         for (IGraphPath path : paths) {
            DomainRecognition source = (DomainRecognition) path.getStartVertex();
            DomainRecognition destination = (DomainRecognition) path.getEndVertex();
            DomainRecognition relation = (DomainRecognition) path.getConnectors().get(0);
            try {
               Long pathID = getPathDefinitionRetrievalService().getDirectPathDefinitionIdByETDNames(
                        source.getConceptName(), relation.getConceptName(), destination.getConceptName());
               if (pathID != null) {
                  boolean isSrcInstance = NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(source.getNlpTag());
                  boolean isDestInstance = NLPConstants.NLP_TAG_ONTO_INSTANCE.equals(destination.getNlpTag());

                  if (!isSrcInstance && isDestInstance) {
                     userQueryRFX.put(destination.getConceptBEDId() + "-DE", UserQueryRFXFactory.getuserQueryRFXForDE(
                              1, destination.getConceptBEDId(), CheckType.NO, 2));
                     userQueryRFX.put(pathID + "-CT", UserQueryRFXFactory.getuserQueryRFXForConceptTriple(1, pathID,
                              CheckType.NO, 1));
                     List<InstancePathDefinition> instancePaths = getPathDefinitionRetrievalService()
                              .getInstancePathDefinitionsByDestinationAndPath(destination.getConceptBEDId(), pathID);
                     if (!instancePaths.isEmpty()) {
                        for (InstancePathDefinition instancePathDef : instancePaths) {
                           userQueryRFX.put(instancePathDef.getSourceDEDId() + "-DE", UserQueryRFXFactory
                                    .getuserQueryRFXForDE(1, instancePathDef.getSourceDEDId(), CheckType.YES, 0));
                        }
                     }
                  }
               }
            } catch (SWIException e) {
               e.printStackTrace();
            }
         }
      }
      return userQueryRFX.values();
   }

   public Map<Long, Set<Long>> searchRelatedInstances (SemanticPossibility semanticPossibility)
            throws KnowledgeSearchException {
      List<SemanticPossibility> semanticPossibilities = new ArrayList<SemanticPossibility>(1);
      semanticPossibilities.add(semanticPossibility);
      return searchRelatedInstances(semanticPossibilities);
   }

   public Map<Long, Set<Long>> searchRelatedInstances (Collection<SemanticPossibility> semanticPossibilities)
            throws KnowledgeSearchException {
      Map<Long, Set<Long>> relatedBedsByAppId = new HashMap<Long, Set<Long>>(1);
      if (CollectionUtils.isEmpty(semanticPossibilities)) {
         return relatedBedsByAppId;
      }
      Map<Long, Set<Long>> instanceIdsByAppId = new HashMap<Long, Set<Long>>(1);
      for (SemanticPossibility semanticPossibility : semanticPossibilities) {
         Graph reducedFormGraph = semanticPossibility.getReducedForm();
         Collection<IGraphComponent> rootVertices = semanticPossibility.getRootVertices();
         Long appId = semanticPossibility.getApplication().getId();
         for (IGraphComponent rv : rootVertices) {
            List<IGraphComponent> components = (List<IGraphComponent>) reducedFormGraph.getDepthFirstRoute(rv);
            for (IGraphComponent component : components) {
               if (component.getType() == GraphComponentType.Vertex) {
                  DomainRecognition recognition = (DomainRecognition) component;
                  if (recognition.getDefaultInstanceBedId() != null) {
                     Set<Long> instanceIds = instanceIdsByAppId.get(appId);
                     if (instanceIds == null) {
                        instanceIds = new HashSet<Long>(1);
                        instanceIdsByAppId.put(appId, instanceIds);
                     }
                     for (InstanceInformation instanceInformation : recognition.getInstanceInformations()) {
                        instanceIds.add(instanceInformation.getInstanceBedId());
                     }
                  }
               }
            }
         }
      }
      if (instanceIdsByAppId.isEmpty()) {
         return relatedBedsByAppId;
      }
      try {
         // Get the related Instance By SourceBeId and DestBeIds
         for (Entry<Long, Set<Long>> entry : instanceIdsByAppId.entrySet()) {
            Long appId = entry.getKey();
            Set<Long> instanceIds = entry.getValue();
            List<Long> relatedSrcBeIds = getPathDefinitionRetrievalService().getInstanceTripleSrcIdsByDestBeIds(
                     instanceIds);
            List<Long> relatedDestBeIds = getPathDefinitionRetrievalService().getInstanceTripleDestIdsBySrcBeIds(
                     instanceIds);
            if (CollectionUtils.isEmpty(relatedSrcBeIds) && CollectionUtils.isEmpty(relatedDestBeIds)) {
               continue;
            }
            Set<Long> relatedBeds = relatedBedsByAppId.get(appId);
            if (relatedBeds == null) {
               relatedBeds = new HashSet<Long>(1);
               relatedBedsByAppId.put(appId, relatedBeds);
            }
            relatedBeds.addAll(relatedSrcBeIds);
            relatedBeds.addAll(relatedDestBeIds);
         }
      } catch (SWIException e) {
         throw new KnowledgeSearchException(e.getCode(), e);
      }
      return relatedBedsByAppId;
   }

   public Map<SemanticPossibility, List<RIInstanceTripleDefinition>> knowledgeSearch (
            List<SemanticPossibility> reducedFormPossibilities, SearchType searchType) throws KnowledgeSearchException {
      try {
         Map<SemanticPossibility, List<RIInstanceTripleDefinition>> riInstancePossiblityMap = new HashMap<SemanticPossibility, List<RIInstanceTripleDefinition>>(
                  1);

         if (CollectionUtils.isEmpty(reducedFormPossibilities)) {
            return riInstancePossiblityMap;
         }

         List<SemanticPossibility> semanticPossibilities = new ArrayList<SemanticPossibility>(1);
         Map<Double, List<SemanticPossibility>> possibilityWeightMap = new HashMap<Double, List<SemanticPossibility>>(1);

         for (SemanticPossibility semanticPossibility : reducedFormPossibilities) {
            processForPathTypeValidation(semanticPossibility, searchType);

            // Get the rfx variation
            Long reducedFormId = getRfIdGenerationService().getNextId();
            // TODO: NK: Should later check if we need to pass the correct user query id, currently this method itself
            // if not used
            Set<ReducedFormIndex> rfxSet = new HashSet<ReducedFormIndex>(1);
            Set<RFXValue> rfxValueSet = new HashSet<RFXValue>();
            getRfxServiceHelper().populateRFXAndRFXValueFromSemanticPossibility(semanticPossibility, rfxSet,
                     rfxValueSet, reducedFormId, -1L, false, RFXType.RFX_KNOWLEDGE_SEARCH,
                     RFXValueType.RFX_VALUE_USER_QUERY, new HashSet<String>(1), new HashSet<String>(1),
                     new HashMap<Long, Integer>(1), new HashMap<String, String>(1));
            if (CollectionUtils.isEmpty(rfxSet)) {
               continue;
            }
            List<ReducedFormIndex> rfxList = new ArrayList<ReducedFormIndex>(rfxSet);
            // getRfxService().storeRFX(rfxList);

            Set<RIUserQuery> riUserQueriesSet = new HashSet<RIUserQuery>(1);

            // Generate the ri_usery_query variation based on the rfx
            Integer entityCount = ReducedFormHelper.getEntityCount(semanticPossibility);
            for (ReducedFormIndex reducedFormIndex : rfxList) {
               Map<RFXVariationSubType, Double> rankingWeightsMap = getRfxService()
                        .getRankingWeightsMapForContentVariationSubType(reducedFormIndex.getRfxVariationSubType());
               riUserQueriesSet.addAll(getRfxServiceHelper().getRIUserQueryEntries(reducedFormIndex, rankingWeightsMap,
                        new HashMap<Long, Integer>(), searchType, reducedFormId, entityCount, false));

            }

            getUdxService().saveUserQueryReverseIndex(new ArrayList<RIUserQuery>(riUserQueriesSet));

            List<RIInstanceTripleDefinition> riInstanceTriples = getUdxService().getRIInstanceTriplesForUserQuery(
                     reducedFormId);
            // cache the ri instance triples
            riInstancePossiblityMap.put(semanticPossibility, riInstanceTriples);

            if (!CollectionUtils.isEmpty(riInstanceTriples)) {
               List<SemanticPossibility> list = possibilityWeightMap.get(riInstanceTriples.get(0).getMatchWeight());
               if (list == null) {
                  list = new ArrayList<SemanticPossibility>(1);
                  possibilityWeightMap.put(riInstanceTriples.get(0).getMatchWeight(), list);
               }
               list.add(semanticPossibility);
            }
         }
         List<Double> values = new ArrayList<Double>(possibilityWeightMap.keySet());
         List<Double> liberalTopCluster = MathUtil.getTopCluster(values);
         for (Double double1 : liberalTopCluster) {
            semanticPossibilities.addAll(possibilityWeightMap.get(double1));
         }

         Map<SemanticPossibility, List<RIInstanceTripleDefinition>> filteredRIInstancePossiblityMap = new HashMap<SemanticPossibility, List<RIInstanceTripleDefinition>>(
                  1);
         for (SemanticPossibility semanticPossibility : semanticPossibilities) {
            List<RIInstanceTripleDefinition> list = riInstancePossiblityMap.get(semanticPossibility);
            filteredRIInstancePossiblityMap.put(semanticPossibility, list);
         }
         return filteredRIInstancePossiblityMap;

      } catch (SWIException e) {
         throw new KnowledgeSearchException(e.getCode(), e);
      } catch (UidException e) {
         throw new KnowledgeSearchException(e.getCode(), e);
      } catch (UDXException e) {
         throw new KnowledgeSearchException(e.getCode(), e);
      } catch (RFXException e) {
         throw new KnowledgeSearchException(e.getCode(), e);
      }
   }

   public Map<Long, KnowledgeSearchResultItem> populateKnowledgeSearchResultsMap (
            List<RIInstanceTripleDefinition> riInstanceTriples, Set<Long> existingUserQueryBedIds) {
      Map<Long, KnowledgeSearchResultItem> knowledgeSearchResultList = new HashMap<Long, KnowledgeSearchResultItem>(1);

      if (CollectionUtils.isEmpty(riInstanceTriples)) {
         return knowledgeSearchResultList;
      }
      // TODO Massage data and create return object i.e KnowledgeSearchResultItem
      for (RIInstanceTripleDefinition riInstanceTriple : riInstanceTriples) {

         /*
          * System.out.println("Triple Info: "+ riInstanceTiple.getBeId1Name() + " - " + riInstanceTiple.getBeId2Name() + " - " +
          * riInstanceTiple.getBeId3Name()); System.out.println("Search Type: "+ riInstanceTiple.getSearchType() + "
          * Variation Sub Type: " + riInstanceTiple.getVariationSubType()); System.out.println("Weight: "+
          * riInstanceTiple.getMatchWeight());
          */
         if (knowledgeSearchResultList.containsKey(riInstanceTriple.getInstanceTripeId())
                  && existingUserQueryBedIds.contains(riInstanceTriple.getRelationBEID())) {
            KnowledgeSearchResultItem knowledgeSearchResultItem = knowledgeSearchResultList.get(riInstanceTriple
                     .getInstanceTripeId());
            // TODO -NA- its kind of a hack as of now we are comparing source id and dest id of both the
            // ri_instanceTriple entries to make sure these are actually different.
            // if (!riInstanceTiple.getSourceBEID().equals(knowledgeSearchResultItem.getSourceBEID())
            // || !riInstanceTiple.getDestinationBEID().equals(knowledgeSearchResultItem.getDestinationBEID()))
            {

               knowledgeSearchResultItem.setMatchWeight(knowledgeSearchResultItem.getMatchWeight()
                        + riInstanceTriple.getMatchWeight());
               knowledgeSearchResultItem.setSearchType(riInstanceTriple.getSearchType());
               knowledgeSearchResultList.put(riInstanceTriple.getInstanceTripeId(), knowledgeSearchResultItem);
               continue;
            }
         }
         // Create New KnowledgeSearchResultItem
         KnowledgeSearchResultItem itd = new KnowledgeSearchResultItem();
         itd.setInstanceTripleID(riInstanceTriple.getInstanceTripeId());
         // Set IDs and Names for the actual instance triple
         itd.setSourceInstanceName(riInstanceTriple.getBeId1Name());
         itd.setRelationName(riInstanceTriple.getBeId2Name());
         itd.setDestinationInstanceName(riInstanceTriple.getBeId3Name());
         itd.setSourceBEID(riInstanceTriple.getSourceBEID());
         itd.setRelationBEID(riInstanceTriple.getRelationBEID());
         itd.setDestinationBEID(riInstanceTriple.getDestinationBEID());
         // Set other information
         itd.setMatchWeight(riInstanceTriple.getMatchWeight());
         itd.setMatchedVariationSubType(riInstanceTriple.getVariationSubType());
         itd.setSearchType(riInstanceTriple.getSearchType());
         itd.setContentSource(riInstanceTriple.getContentSource());
         knowledgeSearchResultList.put(itd.getInstanceTripleID(), itd);
      }
      return knowledgeSearchResultList;
   }

   public Map<Long, KnowledgeSearchResultItem> getKnowledgeSearchResultItemMap (
            SemanticPossibility reducedFormPossibility, List<RIInstanceTripleDefinition> riInstanceTriples) {
      Map<Long, KnowledgeSearchResultItem> knowledgeSearchResultItemsMap = new HashMap<Long, KnowledgeSearchResultItem>(
               1);
      if (CollectionUtils.isEmpty(riInstanceTriples)) {
         return knowledgeSearchResultItemsMap;
      }
      Set<Long> existingUserQueryBedIds = SemanticUtil.getAllBedIds(reducedFormPossibility);
      knowledgeSearchResultItemsMap = populateKnowledgeSearchResultsMap(riInstanceTriples, existingUserQueryBedIds);
      if (logger.isDebugEnabled()) {
         StringBuilder sb = new StringBuilder(1);
         sb.append("\nSemantic Possibility: ").append(reducedFormPossibility.getId()).append("\n").append(
                  reducedFormPossibility.getDisplayString());

         for (KnowledgeSearchResultItem knowledgeSearchResultItem : knowledgeSearchResultItemsMap.values()) {
            sb.append("\nTriple Info: ").append(knowledgeSearchResultItem.getDisplayString());
         }
         sb.append("\nTotal Triple Infos: ").append(knowledgeSearchResultItemsMap.values().size());
         logger.debug(sb.toString());
      }
      return knowledgeSearchResultItemsMap;
   }

   /**
    * @param reducedFormPossibility
    * @param searchType
    * @throws SWIException
    */
   private void processForPathTypeValidation (SemanticPossibility reducedFormPossibility, SearchType searchType)
            throws SWIException {
      List<IGraphPath> paths = reducedFormPossibility.getReducedForm().getPaths();
      if (CollectionUtils.isEmpty(paths)) {
         return;
      }
      for (IGraphPath graphPath : paths) {
         GraphPath path = (GraphPath) graphPath;
         List<IGraphComponent> components = path.getPath();
         if (components.size() == 3) {
            DomainRecognition src = (DomainRecognition) components.get(0);
            // If source is implicit rec(central concept), then set the processing type as implicit triple
            if (src.getPosition() < 0) {
               path.setPathProcessingType(PathProcessingType.IMPLICIT_PATH);
               continue;
            }
            DomainRecognition relation = (DomainRecognition) components.get(1);
            DomainRecognition dest = (DomainRecognition) components.get(2);
            if (src.getDefaultInstanceBedId() != null && dest.getDefaultInstanceBedId() != null) {
               // Its an Instance Triple.
               InstanceTripleDefinition instanceTripleDefinition = getPathDefinitionRetrievalService()
                        .getInstanceTripleDefinition(src.getDefaultInstanceBedId(), relation.getRelationBEDId(),
                                 dest.getDefaultInstanceBedId());
               if (instanceTripleDefinition == null) {
                  if (searchType == SearchType.ENTITY_SEARCH) {
                     path.setPathProcessingType(PathProcessingType.CUT);
                  } else {
                     path.setPathProcessingType(PathProcessingType.SKIP);
                  }
               } else {
                  path.setPathProcessingType(PathProcessingType.CONSIDER);
               }
            }
         }
      }
   }

   public KBSearchResult getKnowledgeSearchResult (Map<Long, KnowledgeSearchResultItem> instanceTriples)
            throws KnowledgeSearchException {
      KBSearchResult searchResult = new KBSearchResult();
      if (!instanceTriples.isEmpty()) {
         Map<Long, KBDataEntity> sourceEntities = new HashMap<Long, KBDataEntity>();
         Map<Long, KBDataEntity> destinationEntities = new HashMap<Long, KBDataEntity>();

         for (KnowledgeSearchResultItem requestedPath : instanceTriples.values()) {
            int variationSubtype = requestedPath.getMatchedVariationSubType();
            if (!sourceEntities.containsKey(requestedPath.getSourceBEID())) {
               KBDataEntity sourceEntity = new KBDataEntity();
               sourceEntity.setId(requestedPath.getSourceBEID());
               sourceEntity.setName(requestedPath.getSourceInstanceName());
               sourceEntity.setType(ConnectionEndPointType.SUBJECT);
               sourceEntities.put(sourceEntity.getId(), sourceEntity);
            }
            if (!destinationEntities.containsKey(requestedPath.getDestinationBEID())) {
               KBDataEntity destEntity = new KBDataEntity();
               destEntity.setId(requestedPath.getDestinationBEID());
               destEntity.setName(requestedPath.getDestinationInstanceName());
               destEntity.setType(ConnectionEndPointType.OBJECT);
               destinationEntities.put(destEntity.getId(), destEntity);
            }
         }
         boolean sourceStart = sourceEntities.size() < destinationEntities.size();
         for (KnowledgeSearchResultItem riInstanceTriple : instanceTriples.values()) {
            int variationSubtype = riInstanceTriple.getMatchedVariationSubType();
            DataConnectionEntity connectionEntity = new KBConnectionEntity();
            connectionEntity.setEntityPathID(riInstanceTriple.getInstanceTripleID());
            connectionEntity.setName(riInstanceTriple.getRelationName());
            connectionEntity.setId(riInstanceTriple.getRelationBEID());

            KBDataEntity destEnt = destinationEntities.get(riInstanceTriple.getDestinationBEID());
            destEnt.setContentSource(riInstanceTriple.getContentSource());
            KBDataEntity sourceEnt = sourceEntities.get(riInstanceTriple.getSourceBEID());
            sourceEnt.setContentSource(riInstanceTriple.getContentSource());

            if (!sourceStart) {
               if (destEnt.hasConnection(connectionEntity.getId())) {
                  connectionEntity = destEnt.getConnection(connectionEntity.getId());
               }
               connectionEntity.addDataItemForInstancePathID(riInstanceTriple.getInstanceTripleID(), sourceEnt);
               destEnt.addConnection(connectionEntity);
            } else {
               if (sourceEnt.hasConnection(connectionEntity.getId())) {
                  connectionEntity = sourceEnt.getConnection(connectionEntity.getId());
               }
               connectionEntity.addDataItemForInstancePathID(riInstanceTriple.getInstanceTripleID(), destEnt);
               sourceEnt.addConnection(connectionEntity);
            }
         }
         if (!sourceStart) {
            searchResult.setEntityMap(destinationEntities);
         } else {
            searchResult.setEntityMap(sourceEntities);
         }
      }
      return searchResult;
   }

   /**
    * @return the udxService
    */
   public IUDXService getUdxService () {
      return udxService;
   }

   /**
    * @param udxService
    *           the udxService to set
    */
   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }

   /**
    * @return the rfxServiceHelper
    */
   public RFXServiceHelper getRfxServiceHelper () {
      return rfxServiceHelper;
   }

   /**
    * @param rfxServiceHelper
    *           the rfxServiceHelper to set
    */
   public void setRfxServiceHelper (RFXServiceHelper rfxServiceHelper) {
      this.rfxServiceHelper = rfxServiceHelper;
   }

   /**
    * @return the rfxService
    */
   public IRFXService getRfxService () {
      return rfxService;
   }

   /**
    * @param rfxService
    *           the rfxService to set
    */
   public void setRfxService (IRFXService rfxService) {
      this.rfxService = rfxService;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }

   public IUidService getRfIdGenerationService () {
      return rfIdGenerationService;
   }

   public void setRfIdGenerationService (IUidService rfIdGenerationService) {
      this.rfIdGenerationService = rfIdGenerationService;
   }
}