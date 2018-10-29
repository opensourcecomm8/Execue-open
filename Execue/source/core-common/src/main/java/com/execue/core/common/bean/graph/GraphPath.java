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
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.type.PathProcessingType;

/**
 * @author Abhijit
 * @since Sep 26, 2008 : 1:41:42 PM
 */
public class GraphPath implements IGraphPath {

   private static Logger           log = Logger.getLogger(GraphPath.class);
   protected List<IGraphComponent> path;
   private PathProcessingType      pathProcessingType;
   private Integer                 sentenceId;
   private boolean                 validateInstanceTriple;

   /**
    * @return the validateInstanceTriple
    */
   public boolean isValidateInstanceTriple () {
      return validateInstanceTriple;
   }

   /**
    * @param validateInstanceTriple
    *           the validateInstanceTriple to set
    */
   public void setValidateInstanceTriple (boolean validateInstanceTriple) {
      this.validateInstanceTriple = validateInstanceTriple;
   }

   @Override
   public String toString () {
      StringBuilder sb = new StringBuilder();
      if (CollectionUtils.isEmpty(path)) {
         return sb.toString();
      }
      for (IGraphComponent graphComponent : path) {
         DomainRecognition domainRecognition = (DomainRecognition) graphComponent;
         sb.append(domainRecognition.getEntityBeId());
      }
      return sb.toString();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof GraphPath || obj instanceof String) && this.toString().equalsIgnoreCase(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   public List<IGraphComponent> getPath () {
      return path;
   }

   public void setPath (List<IGraphComponent> path) {
      this.path = path;
   }

   // Utility Methods

   public List<IGraphComponent> getFullPath () {
      return getPath();
   }

   public IGraphComponent getStartVertex () {
      return path.get(0);
   }

   public IGraphComponent getEndVertex () {
      return path.get(path.size() - 1);
   }

   public IGraphComponent getPathComponent (int index) {
      return path.get(index);
   }

   public List<IGraphComponent> getConnectors () {
      return path.subList(1, path.size() - 1);
   }

   public int getPathLength () {
      return path.size();
   }

   public IGraphPath subPath (int start, int end) {
      GraphPath tempPath = new GraphPath();
      List<IGraphComponent> partialList = path.subList(start, end + 1);
      tempPath.setPath(partialList);
      return tempPath;
   }

   public List<IGraphComponent> getPartialPath (int start, int end) {
      return path.subList(start, end);
   }

   public boolean contains (IGraphComponent gc) {
      return path.contains(gc);
   }

   public void addPathComponent (IGraphComponent gc) {
      if (path == null) {
         path = new ArrayList<IGraphComponent>(1);
      } else {
         IGraphComponent prevComponent = path.get(path.size() - 1);
         if (prevComponent.equals(gc)) {
            return;
         }
         if (prevComponent.getType().equals(GraphComponentType.Vertex) && !gc.getType().equals(GraphComponentType.Edge)
                  || prevComponent.getType().equals(GraphComponentType.Edge)
                  && !gc.getType().equals(GraphComponentType.Vertex)) {
            // TODO Throw Exception - Component cant be added
            log.error("Can not add path Component");
            return;
         }
      }
      path.add(gc);
   }

   /**
    * @return the pathProcessingType
    */
   public PathProcessingType getPathProcessingType () {
      return pathProcessingType;
   }

   /**
    * @param pathProcessingType
    *           the pathProcessingType to set
    */
   public void setPathProcessingType (PathProcessingType pathProcessingType) {
      this.pathProcessingType = pathProcessingType;
   }

   public int getMaxTokenPosition () {

      int maxPosition = 0;
      if (CollectionUtils.isEmpty(path)) {
         return maxPosition;
      }

      for (IGraphComponent graphComponent : path) {
         DomainRecognition dr = (DomainRecognition) graphComponent;
         if (maxPosition < dr.getPosition()) {
            maxPosition = dr.getPosition();
         }
      }
      return maxPosition;
   }

   public int getPositionDiffBetweenSourceAndDestination () {

      int posDiff = 0;
      if (CollectionUtils.isEmpty(path)) {
         return posDiff;
      }
      DomainRecognition startVertex = (DomainRecognition) getStartVertex();
      DomainRecognition endVertex = (DomainRecognition) getEndVertex();
      posDiff = endVertex.getPosition() - startVertex.getPosition();
      if (posDiff < 0) {
         posDiff = posDiff * -1;
      }
      return posDiff;
   }

   /**
    * @return the sentenceId
    */
   public Integer getSentenceId () {
      return sentenceId;
   }

   /**
    * @param sentenceId
    *           the sentenceId to set
    */
   public void setSentenceId (Integer sentenceId) {
      this.sentenceId = sentenceId;
   }

   public boolean intersectsWith (IGraphPath graphPath) {
      IGraphComponent startVertex = this.getStartVertex();
      IGraphComponent endVertex = this.getEndVertex();
      IGraphComponent pathStartVertex = graphPath.getStartVertex();
      IGraphComponent pathEndVertex = graphPath.getEndVertex();
      if (pathEndVertex.equals(startVertex) || pathStartVertex.equals(endVertex)) {
         return true;
      }
      return false;
   }
}
