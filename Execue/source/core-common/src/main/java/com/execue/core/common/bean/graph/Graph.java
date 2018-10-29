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


package com.execue.core.common.bean.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Abhijit
 * @since Sep 15, 2008 - 12:13:07 PM
 */
public class Graph {

   private static Logger                log = Logger.getLogger(Graph.class);

   private Map<String, IGraphComponent> vertices;
   private Map<String, IGraphComponent> edges;
   private List<IGraphPath>             paths;

   public Map<String, IGraphComponent> getVertices () {
      if (ExecueCoreUtil.isMapEmpty(vertices)) {
         vertices = new HashMap<String, IGraphComponent>(1);
      }
      return vertices;
   }

   public Map<String, IGraphComponent> getEdges () {
      if (ExecueCoreUtil.isMapEmpty(edges)) {
         edges = new HashMap<String, IGraphComponent>(1);
      }
      return edges;
   }

   public List<IGraphPath> getPaths () {
      if (ExecueCoreUtil.isCollectionEmpty(paths)) {
         paths = new ArrayList<IGraphPath>();
      }
      return paths;
   }

   // Utility Methods

   public void addGraphComponent (IGraphComponent graphComponent, IGraphComponent prevNode) {

      if (graphComponent.getType() == GraphComponentType.Vertex) {
         DomainRecognition recognition = (DomainRecognition) graphComponent;
         String businessEntityName = getBusinessEntityName(recognition);
         String key = businessEntityName + (recognition).getConceptName() + "-" + (recognition).getPosition();
         if (prevNode != null && prevNode.getType() == GraphComponentType.Vertex) {
            // TODO Throw Exception - Component cant be added
            log.error("Can not add graph Component");
            return;
         }
         if (vertices == null) {
            vertices = new HashMap<String, IGraphComponent>(1);
         }
         if (!vertices.containsKey(key)) {
            vertices.put(key, graphComponent);
            if (prevNode != null) {
               prevNode.addNextGraphComponent(graphComponent);
            }
         } else if (prevNode != null) {
            prevNode.addNextGraphComponent(vertices.get(key));
         }

      } else if (graphComponent.getType() == GraphComponentType.Edge) {
         if (prevNode != null && prevNode.getType() == GraphComponentType.Edge) {
            // TODO Throw Exception - Component cant be added
            log.error("Can not add graph Component");
            return;
         }
         if (edges == null) {
            edges = new HashMap<String, IGraphComponent>(1);
         }
         String businessEntityName = getBusinessEntityName((DomainRecognition) graphComponent);
         // if (!edges.containsKey(gc.getBusinessEntityName())) {
         edges.put(businessEntityName, graphComponent);
         if (prevNode != null) {
            prevNode.addNextGraphComponent(graphComponent);
         }
         // }
         /*
          * else if (prevNode != null) {
          * vertices.get(prevNode.getBusinessEntityName()).addNextGraphComponent(edges.get(gc.getBusinessEntityName())); }
          */
      }
   }

   /**
    * @param recognition
    * @return
    */
   private String getBusinessEntityName (DomainRecognition recognition) {
      String businessEntityName = "";
      if (!CollectionUtils.isEmpty(recognition.getInstanceInformations())) {
         businessEntityName = ExecueCoreUtil.joinCollection(recognition.getInstanceNames());
      } else {
         businessEntityName = recognition.getBusinessEntityName();
      }
      return businessEntityName;
   }

   public void removeVertex (IGraphComponent vertex) {
      String businessEntityName = getBusinessEntityName((DomainRecognition) vertex);
      String key = businessEntityName + ((DomainRecognition) vertex).getConceptName() + "-"
               + ((DomainRecognition) vertex).getPosition();
      this.getVertices().remove(key);
   }

   public void addPath (IGraphPath path) {
      if (this.paths == null) {
         paths = new ArrayList<IGraphPath>(1);
      }
      paths.add(path);
   }

   public void removePath (IGraphPath path) {
      if (this.paths != null) {
         paths.remove(path);
         for (int i = 0; i < path.getFullPath().size() - 1; i++) {
            IGraphComponent pathComponent = path.getPathComponent(i);
            IGraphComponent nextPathComponent = null;
            if (path.getPathLength() > i + 1) {
               nextPathComponent = path.getPathComponent(i + 1);
            }
            if (pathComponent.hasNextGraphComponents()
                     && pathComponent.getNextGraphComponents().contains(nextPathComponent)) {
               if (nextPathComponent.getType() == GraphComponentType.Edge) {
                  if (nextPathComponent.getNextGraphComponents().size() == 1) {

                     pathComponent.getNextGraphComponents().remove(nextPathComponent);
                  }
               } else {
                  while (pathComponent.getNextGraphComponents().contains(nextPathComponent)) {
                     pathComponent.getNextGraphComponents().remove(nextPathComponent);
                  }
               }
            }
         }
      }
   }

   /**
    * Get the Graph Components in Depth First manner. Will get the Start vertex two times if its the start of more thn
    * one path but its importan so that as we can figure out the relation with this.
    * 
    * @param gc
    * @return
    */
   public Collection<IGraphComponent> getDepthFirstRoute (IGraphComponent gc) {
      List<IGraphComponent> components = new ArrayList<IGraphComponent>(5);
      boolean foundPath = false;
      if (gc.getType().equals(GraphComponentType.Vertex)) {
         if (paths != null) {
            for (IGraphPath path : paths) {
               if (path.getStartVertex().equals(gc)) {
                  components.add(gc);
                  components.addAll(path.getConnectors());
                  components.addAll(getDepthFirstRoute(path.getEndVertex()));
                  foundPath = true;
               }
            }
         }
         if (!foundPath) {
            components.add(gc);
         }
      }
      return components;
   }

   public Collection<Collection<Collection<IGraphComponent>>> getDepthFirstRoutePaths (List<IGraphPath> graphPaths,
            IGraphComponent gc) {
      List<Collection<Collection<IGraphComponent>>> listRoutePaths = new ArrayList<Collection<Collection<IGraphComponent>>>();
      for (IGraphPath path : graphPaths) {
         if (path.getStartVertex().equals(gc)) {
            List<IGraphComponent> components = new ArrayList<IGraphComponent>(5);
            components.add(gc);
            components.addAll(path.getConnectors());
            components.add(path.getEndVertex());
            Collection<Collection<IGraphComponent>> componentPaths = getDepthFirstRoutePaths(path.getEndVertex());
            componentPaths.add(components);
            listRoutePaths.add(componentPaths);
         }
      }
      return listRoutePaths;
   }

   public Collection<Collection<IGraphComponent>> getDepthFirstRoutePaths (IGraphComponent gc) {
      List<Collection<IGraphComponent>> componentPaths = new ArrayList<Collection<IGraphComponent>>();
      boolean foundPath = false;
      if (gc.getType().equals(GraphComponentType.Vertex)) {
         if (paths != null) {
            for (IGraphPath path : paths) {
               if (path.getStartVertex().equals(gc)) {
                  List<IGraphComponent> components = new ArrayList<IGraphComponent>(5);
                  components.add(gc);
                  components.addAll(path.getConnectors());
                  components.add(path.getEndVertex());
                  componentPaths.addAll(getDepthFirstRoutePaths(path.getEndVertex()));
                  componentPaths.add(components);
                  foundPath = true;
               }
            }
         }
         if (!foundPath && isRootVertex(gc)) {
            List<IGraphComponent> components = new ArrayList<IGraphComponent>(5);
            components.add(gc);
            componentPaths.add(components);
         }
      }
      return componentPaths;
   }

   public String depthFirstRoute (List<IGraphComponent> visitedNodes, IGraphComponent gc, String space) {
      StringBuffer sb = new StringBuffer();
      boolean foundPath = false;
      if (gc.getType().equals(GraphComponentType.Vertex)) {
         if (paths != null) {
            for (IGraphPath path : paths) {
               if (path.getStartVertex().equals(gc)) {
                  if (!visitedNodes.contains(gc)) {
                     sb.append(space).append(path.getStartVertex().getRecognitionDisplayName()).append("\n");
                     visitedNodes.add(gc);
                  }
                  String tab = "\t";
                  for (IGraphComponent connector : path.getConnectors()) {
                     sb.append(space).append(tab).append(connector.getRecognitionDisplayName()).append("\n");
                     tab = tab + "\t";
                  }

                  sb.append(space).append(depthFirstRoute(visitedNodes, path.getEndVertex(), space + tab)).append("\n");
                  foundPath = true;
               }
            }
         }
         if (!foundPath) {
            sb.append(space).append(gc.getRecognitionDisplayName());
            visitedNodes.add(gc);
         }
      } else {
         log.error("You can not start at an Non-vertex Component.");
      }
      return sb.toString();
   }

   public Collection<IGraphComponent> getRootVertices () {
      Set<IGraphComponent> startVertices = new HashSet<IGraphComponent>(1);
      Set<IGraphComponent> endVertices = new HashSet<IGraphComponent>(1);

      for (IGraphPath path : paths) {
         startVertices.add(path.getStartVertex());
         endVertices.add(path.getEndVertex());
      }

      startVertices.removeAll(endVertices);

      return startVertices;
   }

   public boolean isRootVertex (IGraphComponent gc) {
      Set<IGraphComponent> startVertices = new HashSet<IGraphComponent>(1);
      Set<IGraphComponent> endVertices = new HashSet<IGraphComponent>(1);

      for (IGraphPath path : paths) {
         startVertices.add(path.getStartVertex());
         endVertices.add(path.getEndVertex());
      }

      startVertices.removeAll(endVertices);

      return startVertices.contains(gc);
   }

   public IGraphComponent getAlreadyAddedNode (IGraphComponent gc) {
      DomainRecognition newRec = (DomainRecognition) gc;
      String businessEntityName = getBusinessEntityName((DomainRecognition) gc);
      if (gc.getType() == GraphComponentType.Vertex) {
         for (IGraphComponent graphComponent : this.getVertices().values()) {
            DomainRecognition addedRec = (DomainRecognition) graphComponent;
            String addedBusinessEntityName = getBusinessEntityName(addedRec);
            String addedKey = addedRec.getTypeName() + addedRec.getConceptName() + addedBusinessEntityName;
            addedKey = StringUtils.remove(addedKey, " ");
            String newKey = newRec.getTypeName() + newRec.getConceptName() + businessEntityName;
            newKey = StringUtils.remove(newKey, " ");
            if (addedKey.equals(newKey)) {
               return addedRec;
            }
         }

      } else {
         if (edges == null) {
            return null;
         }
         return edges.get(businessEntityName);
      }
      return null;
   }

   public Collection<IGraphComponent> getLeafVertices () {
      Set<IGraphComponent> startVertices = new HashSet<IGraphComponent>(1);
      Set<IGraphComponent> endVertices = new HashSet<IGraphComponent>(1);

      for (IGraphPath path : paths) {
         startVertices.add(path.getStartVertex());
         endVertices.add(path.getEndVertex());
      }

      endVertices.removeAll(startVertices);

      return endVertices;
   }
}
