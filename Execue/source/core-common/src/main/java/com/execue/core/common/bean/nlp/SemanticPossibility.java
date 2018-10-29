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


package com.execue.core.common.bean.nlp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.WeightInformation;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.graph.Graph;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;

public class SemanticPossibility {

   protected Long                               id;
   protected Graph                              reducedForm;
   protected Map<Integer, WordRecognitionState> wordRecognitionStates;
   protected List<String>                       queryWords;
   private Model                                model;
   private Application                          application;
   private Double                               possiblityWeight             = 0D;
   private Double                               appWeight                    = 0D;
   private Double                               standarizedApplicationWeight = 0D;
   private Double                               standarizedPossiblityWeight  = 0D;

   private WeightInformation                    weightInformation;
   private WeightInformation                    weightInformationForExplicitEntities;
   private WeightInformation                    weightInformationForAssociations;
   private WeightInformation                    weightInformationForFrmeworks;

   // is true if the possibility is scoped by a vertical or app
   private boolean                              scoped                       = false;
   private Set<IGraphComponent>                 removedVertex;

   public SemanticPossibility () {
      this.reducedForm = new Graph();
   }

   public Graph getReducedForm () {
      return reducedForm;
   }

   public void setReducedForm (Graph reducedForm) {
      this.reducedForm = reducedForm;
   }

   public List<String> getQueryWords () {
      return queryWords;
   }

   public void setQueryWords (List<String> queryWords) {
      this.queryWords = queryWords;
   }

   /**
    * @return the wordRecognitionStates
    */
   public Map<Integer, WordRecognitionState> getWordRecognitionStates () {
      return wordRecognitionStates;
   }

   /**
    * @param wordRecognitionStates
    *           the wordRecognitionStates to set
    */
   public void setWordRecognitionStates (Map<Integer, WordRecognitionState> wordRecognitionStates) {
      this.wordRecognitionStates = wordRecognitionStates;
   }

   // Utility Methods

   public List<IGraphComponent> getAllGraphComponents () {
      List<IGraphComponent> allComponents = new ArrayList<IGraphComponent>(1);
      if (this.reducedForm.getVertices() != null) {
         allComponents.addAll(this.reducedForm.getVertices().values());
      }
      if (this.reducedForm.getEdges() != null) {
         allComponents.addAll(this.reducedForm.getEdges().values());
      }
      return allComponents;
   }

   public Collection<IGraphComponent> getRootVertices () {
      List<IGraphPath> paths = reducedForm.getPaths();
      if (paths == null || paths.isEmpty()) {
         if (reducedForm.getVertices() != null) {
            return reducedForm.getVertices().values();
         } else {
            return new ArrayList<IGraphComponent>(1);
         }
      }

      Collection<IGraphComponent> endVertices = new HashSet<IGraphComponent>(1);
      Collection<IGraphComponent> startVertices = new HashSet<IGraphComponent>(1);

      for (IGraphPath path : paths) {
         endVertices.add(path.getEndVertex());
         startVertices.add(path.getStartVertex());
      }
      startVertices.removeAll(endVertices);

      Map<String, IGraphComponent> vertices = reducedForm.getVertices();
      for (IGraphComponent gc : vertices.values()) {
         boolean found = false;
         for (IGraphPath path : paths) {
            if (path.contains(gc)) {
               found = true;
            }
         }
         if (!found) {
            startVertices.add(gc);
         }
      }
      return startVertices;
   }

   public String getDisplayString () {
      StringBuffer sb = new StringBuffer();
      Collection<IGraphComponent> rootVertices = getRootVertices();
      if (rootVertices != null) {
         for (IGraphComponent root : rootVertices) {
            List<IGraphComponent> visitedNodes = new ArrayList<IGraphComponent>(1);
            sb.append(reducedForm.depthFirstRoute(visitedNodes, root, "")).append("\n");
         }
      }
      sb.append(", For model ");
      sb.append(model == null ? "" : model.getName());
      return sb.toString();
      // StringBuffer sb = new StringBuffer();
      // Collection<IGraphComponent> rootVertices = getRootVertices();
      // if (rootVertices != null) {
      // for (IGraphComponent root : rootVertices) {
      // sb.append(root.print("")).append("\n");
      // }
      // }
      // sb.append(", For model ");
      // sb.append(model == null ? "" : model.getName());
      // return sb.toString();
   }

   /**
    * @return
    */
   public String getCompareString () {
      StringBuffer sb = new StringBuffer();
      Collection<IGraphComponent> rootVertices = getRootVertices();
      if (rootVertices != null) {
         for (IGraphComponent root : rootVertices) {
            sb.append(reducedForm.depthFirstRoute(new ArrayList<IGraphComponent>(1), root, ""));
         }
      }
      return sb.toString();
   }

   public void addQueryWord (String word) {
      if (this.queryWords == null) {
         this.queryWords = new ArrayList<String>();
      }
      this.queryWords.add(word);
   }

   public Model getModel () {
      return model;
   }

   public void setModel (Model model) {
      this.model = model;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public Double getPossiblityWeight () {
      return possiblityWeight;
   }

   public void setPossiblityWeight (Double possiblityWeight) {
      this.possiblityWeight = possiblityWeight;
   }

   public Double getStandarizedApplicationWeight () {
      return standarizedApplicationWeight;
   }

   public void setStandarizedApplicationWeight (Double standarizedApplicationWeight) {
      this.standarizedApplicationWeight = standarizedApplicationWeight;
   }

   public Double getStandarizedPossiblityWeight () {
      return standarizedPossiblityWeight;
   }

   public void setStandarizedPossiblityWeight (Double standarizedPossiblityWeight) {
      this.standarizedPossiblityWeight = standarizedPossiblityWeight;
   }

   /**
    * @return the appWeight
    */
   public Double getAppWeight () {
      return appWeight;
   }

   /**
    * @param appWeight
    *           the appWeight to set
    */
   public void setAppWeight (Double appWeight) {
      this.appWeight = appWeight;
   }

   /**
    * @return the weightInformation
    */
   public WeightInformation getWeightInformation () {
      return weightInformation;
   }

   /**
    * @param weightInformation
    *           the weightInformation to set
    */
   public void setWeightInformation (WeightInformation weightInformation) {
      this.weightInformation = weightInformation;
   }

   public Application getApplication () {
      return application;
   }

   public void setApplication (Application application) {
      this.application = application;
   }

   public WeightInformation getWeightInformationForExplicitEntities () {
      return weightInformationForExplicitEntities;
   }

   public void setWeightInformationForExplicitEntities (WeightInformation weightInformationForExplicitEntities) {
      this.weightInformationForExplicitEntities = weightInformationForExplicitEntities;
   }

   public boolean isScoped () {
      return scoped;
   }

   public void setScoped (boolean scoped) {
      this.scoped = scoped;
   }

   /**
    * @return the removedVertex
    */
   public Set<IGraphComponent> getRemovedVertex () {
      if (removedVertex == null) {
         removedVertex = new HashSet<IGraphComponent>(1);
      }
      return removedVertex;
   }

   /**
    * @param removedVertex
    *           the removedVertex to set
    */
   public void setRemovedVertex (Set<IGraphComponent> removedVertex) {
      this.removedVertex = removedVertex;
   }

   public WeightInformation getWeightInformationForAssociations () {
      return weightInformationForAssociations;
   }

   public void setWeightInformationForAssociations (WeightInformation weightInformationForAssociations) {
      this.weightInformationForAssociations = weightInformationForAssociations;
   }

   public WeightInformation getWeightInformationForFrmeworks () {
      return weightInformationForFrmeworks;
   }

   public void setWeightInformationForFrmeworks (WeightInformation weightInformationForFrmeworks) {
      this.weightInformationForFrmeworks = weightInformationForFrmeworks;
   }
}
