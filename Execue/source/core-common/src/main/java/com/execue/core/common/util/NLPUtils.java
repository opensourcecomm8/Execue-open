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
package com.execue.core.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.GraphComponentType;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.DomainRecognition;

/**
 * @author Nitesh
 */
public class NLPUtils {

   public static void sortComponentsBySentenceIdAndQuality (List<IGraphComponent> singleInstanceRecognitions) {
      Collections.sort(singleInstanceRecognitions, new Comparator<IGraphComponent>() {

         public int compare (IGraphComponent graphComponent1, IGraphComponent graphComponent2) {
            DomainRecognition recognition1 = (DomainRecognition) graphComponent1;
            DomainRecognition recognition2 = (DomainRecognition) graphComponent2;
            if (recognition1.getSentenceId() != null && recognition2.getSentenceId() != null) {
               if (recognition1.getSentenceId() > recognition2.getSentenceId()) {
                  return 1;
               }
               if (recognition1.getSentenceId() < recognition2.getSentenceId()) {
                  return -1;
               }
            }
            if (recognition1.getWeightInformation().getRecognitionQuality() > recognition1.getWeightInformation()
                     .getRecognitionQuality()) {
               return -1;
            } else if (recognition1.getWeightInformation().getRecognitionQuality() < recognition1
                     .getWeightInformation().getRecognitionQuality()) {
               return 1;
            } else {
               return recognition1.getPosition() - recognition2.getPosition();
            }
         }
      });
   }

   public static void sortPathsByProximity (List<IGraphPath> singleInstancePaths) {
      Collections.sort(singleInstancePaths, new Comparator<IGraphPath>() {

         public int compare (IGraphPath p1, IGraphPath p2) {
            GraphPath path1 = (GraphPath) p1;
            GraphPath path2 = (GraphPath) p2;

            // sort by sentence id
            if (path1.getSentenceId() != null && path2.getSentenceId() != null) {
               if (path1.getSentenceId() > path2.getSentenceId()) {
                  return 1;
               }
               if (path1.getSentenceId() < path2.getSentenceId()) {
                  return -1;
               }
            }

            // sort by rec quality
            double qualityPath1 = path1.getStartVertex().getWeightInformation().getRecognitionQuality()
                     + path1.getEndVertex().getWeightInformation().getRecognitionQuality();
            double qualityPath2 = path2.getStartVertex().getWeightInformation().getRecognitionQuality()
                     + path2.getEndVertex().getWeightInformation().getRecognitionQuality();

            if (qualityPath1 < qualityPath2) {
               return 1;
            } else if (qualityPath1 > qualityPath2) {
               return -1;
            }

            // sort by proximity
            int proxmityPath1 = path1.getPositionDiffBetweenSourceAndDestination();
            int proxmityPath2 = path2.getPositionDiffBetweenSourceAndDestination();
            if (proxmityPath1 >= proxmityPath2) {
               return 1;
            }
            return -1;
         }
      });
   }

   /**
    * @param graphPath
    * @return the list of IGraphComponent of type vertex in the given path
    */
   public static List<IGraphComponent> getVertices (IGraphPath graphPath) {
      List<IGraphComponent> vertices = new ArrayList<IGraphComponent>(1);
      List<IGraphComponent> pathComponents = graphPath.getFullPath();
      if (CollectionUtils.isEmpty(pathComponents)) {
         return vertices;
      }

      for (IGraphComponent pathComponent : pathComponents) {
         if (pathComponent.getType() == GraphComponentType.Vertex) {
            vertices.add(pathComponent);
         }
      }
      return vertices;
   }

   /**
    * @param reducedForm
    * @return the unique collection set of connected vertices
    */
   public static Collection<IGraphComponent> getConnectedVertices (Graph reducedForm) {
      Set<IGraphComponent> connectedVertices = new HashSet<IGraphComponent>(1);
      List<IGraphPath> paths = reducedForm.getPaths();
      if (CollectionUtils.isEmpty(paths)) {
         return connectedVertices;
      }
      for (IGraphPath path : paths) {
         connectedVertices.addAll(getVertices(path));
      }

      return connectedVertices;
   }

   /**
    * @param reducedForm
    * @return the collection list of unconnected vertices
    */
   public static Collection<IGraphComponent> getUnconnectedVertices (Graph reducedForm) {
      List<IGraphComponent> unconnectedVertices = new ArrayList<IGraphComponent>(1);
      Map<String, IGraphComponent> vertices = reducedForm.getVertices();
      if (CollectionUtils.isEmpty(vertices)) {
         return unconnectedVertices;
      }
      unconnectedVertices.addAll(vertices.values());

      if (CollectionUtils.isEmpty(reducedForm.getPaths())) {
         return unconnectedVertices;
      }

      Collection<IGraphComponent> connectedVertices = getConnectedVertices(reducedForm);
      unconnectedVertices.removeAll(connectedVertices);
      return unconnectedVertices;
   }

   public static DomainRecognition chooseBestDomainRecognitionByQuality (List<DomainRecognition> domainRecognitions) {
      TreeMap<Double, List<DomainRecognition>> domainRecsByWeight = new TreeMap<Double, List<DomainRecognition>>();
      for (DomainRecognition domainRecognition : domainRecognitions) {
         Double recognitionQuality = domainRecognition.getWeightInformation().getRecognitionQuality();
         List<DomainRecognition> existingDomainRecs = domainRecsByWeight.get(recognitionQuality);
         if (existingDomainRecs == null) {
            existingDomainRecs = new ArrayList<DomainRecognition>();
            domainRecsByWeight.put(recognitionQuality, existingDomainRecs);
         }
         existingDomainRecs.add(domainRecognition);
      }
      //TODO: NK: Need to check if we still want to filter if there are more than one top quality domain recs
      DomainRecognition bestDomainRecognition = domainRecsByWeight.lastEntry().getValue().get(0);
      return bestDomainRecognition;
   }

   public static Set<String> getSingleValueRecognitionNames (Graph reducedForm) {
      Set<String> singleValuedRecognitionNames = new HashSet<String>();
      Collection<IGraphComponent> vertices = getConnectedVertices(reducedForm);
      vertices.addAll(getUnconnectedVertices(reducedForm));
      for (IGraphComponent graphComponent : vertices) {
         DomainRecognition domainRecognition = (DomainRecognition) graphComponent;
         if (!domainRecognition.isMutliValuedRecognition()) {
            if (!StringUtils.isEmpty(domainRecognition.getConceptName())) {
               singleValuedRecognitionNames.add(domainRecognition.getConceptName());
            }
         }
      }
      return singleValuedRecognitionNames;
   }
}