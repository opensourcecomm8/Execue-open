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


package com.execue.core.common.bean.entity;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.execue.core.common.type.AssociationType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.EntityTripleDefinitionType;
import com.execue.core.common.type.HierarchyType;
import com.execue.core.common.type.PathSelectionType;

public class PathDefinition implements java.io.Serializable {

   private Long                       id;

   private BusinessEntityDefinition   sourceBusinessEntityDefinition;
   private BusinessEntityDefinition   destinationBusinessEntityDefinition;

   private int                        pathLength;
   private int                        priority;

   private EntityTripleDefinitionType type;
   private HierarchyType              hierarchyType;
   private PathSelectionType          pathSelectionType = PathSelectionType.NORMAL_PATH;

   private CheckType                  centralConceptType;

   private Long                       cloudId;

   private Set<Path>                  paths;

   private Set<Rule>                  pathRules;
   private AssociationType            associationType   = AssociationType.RELATION_ASSOCIATION;

   /**
    * @return the associationType
    */
   public AssociationType getAssociationType () {
      return associationType;
   }

   /**
    * @param associationType
    *           the associationType to set
    */
   public void setAssociationType (AssociationType associationType) {
      this.associationType = associationType;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public int getPathLength () {
      return pathLength;
   }

   public void setPathLength (int pathLength) {
      this.pathLength = pathLength;
   }

   public int getPriority () {
      return priority;
   }

   public void setPriority (int priority) {
      this.priority = priority;
   }

   public Set<Path> getPaths () {
      return paths;
   }

   public void setPaths (Set<Path> paths) {
      SortedSet<Path> sortedPaths = new TreeSet<Path>();
      sortedPaths.addAll(paths);
      this.paths = sortedPaths;
   }

   public EntityTripleDefinitionType getType () {
      return type;
   }

   public void setType (EntityTripleDefinitionType type) {
      this.type = type;
   }

   public HierarchyType getHierarchyType () {
      return hierarchyType;
   }

   public void setHierarchyType (HierarchyType parentChildType) {
      this.hierarchyType = parentChildType;
   }

   public BusinessEntityDefinition getSourceBusinessEntityDefinition () {
      return sourceBusinessEntityDefinition;
   }

   public void setSourceBusinessEntityDefinition (BusinessEntityDefinition sourceBusinessEntityDefinition) {
      this.sourceBusinessEntityDefinition = sourceBusinessEntityDefinition;
   }

   public BusinessEntityDefinition getDestinationBusinessEntityDefinition () {
      return destinationBusinessEntityDefinition;
   }

   public void setDestinationBusinessEntityDefinition (BusinessEntityDefinition destinationBusinessEntityDefinition) {
      this.destinationBusinessEntityDefinition = destinationBusinessEntityDefinition;
   }

   /**
    * @return the centralConceptType
    */
   public CheckType getCentralConceptType () {
      return centralConceptType;
   }

   /**
    * @param centralConceptType
    *           the centralConceptType to set
    */
   public void setCentralConceptType (CheckType centralConceptType) {
      this.centralConceptType = centralConceptType;
   }

   /**
    * @return the pathRules
    */
   public Set<Rule> getPathRules () {
      return pathRules;
   }

   /**
    * @param pathRules
    *           the pathRules to set
    */
   public void setPathRules (Set<Rule> pathRules) {
      this.pathRules = pathRules;
   }

   public Long getCloudId () {
      return cloudId;
   }

   public void setCloudId (Long cloudId) {
      this.cloudId = cloudId;
   }

   /**
    * @return the pathSelectionType
    */
   public PathSelectionType getPathSelectionType () {
      return pathSelectionType;
   }

   /**
    * @param pathSelectionType
    *           the pathSelectionType to set
    */
   public void setPathSelectionType (PathSelectionType pathSelectionType) {
      this.pathSelectionType = pathSelectionType;
   }

}
