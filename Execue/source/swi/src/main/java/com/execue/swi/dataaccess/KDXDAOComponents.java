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


package com.execue.swi.dataaccess;

import com.execue.dataaccess.swi.dao.IApplicationDAO;
import com.execue.dataaccess.swi.dao.IBaseBusinessEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IBusinessEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IBusinessEntityMaintenanceDAO;
import com.execue.dataaccess.swi.dao.IBusinessEntityVariationDAO;
import com.execue.dataaccess.swi.dao.ICloudDAO;
import com.execue.dataaccess.swi.dao.IConceptDAO;
import com.execue.dataaccess.swi.dao.IContentCleanupPatternDAO;
import com.execue.dataaccess.swi.dao.IDefaultDynamicValueDAO;
import com.execue.dataaccess.swi.dao.IEntityAttributeDAO;
import com.execue.dataaccess.swi.dao.IEntityBehaviorDAO;
import com.execue.dataaccess.swi.dao.IHierarchyDAO;
import com.execue.dataaccess.swi.dao.IInstanceDAO;
import com.execue.dataaccess.swi.dao.IMappingDAO;
import com.execue.dataaccess.swi.dao.IModelDAO;
import com.execue.dataaccess.swi.dao.IOntoReverseIndexDAO;
import com.execue.dataaccess.swi.dao.IParallelWordDAO;
import com.execue.dataaccess.swi.dao.IPathDefinitionDAO;
import com.execue.dataaccess.swi.dao.IPopularityHitDAO;
import com.execue.dataaccess.swi.dao.IProfileDAO;
import com.execue.dataaccess.swi.dao.IRISharedUserModelMappingDAO;
import com.execue.dataaccess.swi.dao.IRelationDAO;
import com.execue.dataaccess.swi.dao.ISFLWordDAO;
import com.execue.dataaccess.swi.dao.IStatDAO;
import com.execue.dataaccess.swi.dao.ITypeDAO;
import com.execue.dataaccess.swi.dao.IUnstructuredApplicationDetailDAO;
import com.execue.dataaccess.swi.dao.IVerticalDAO;

public abstract class KDXDAOComponents {

   private IBusinessEntityDefinitionDAO      businessEntityDefinitionDAO;
   private IBaseBusinessEntityDefinitionDAO  baseBusinessEntityDefinitionDAO;
   private ISFLWordDAO                       sflWordDAO;
   private IParallelWordDAO                  parallelWordDAO;
   private IOntoReverseIndexDAO              ontoReverseIndexDAO;
   private IPathDefinitionDAO                pathDefinitionDAO;
   private ICloudDAO                         cloudDAO;
   private ITypeDAO                          typeDAO;
   private IBusinessEntityVariationDAO       businessEntityVariationDAO;
   private IContentCleanupPatternDAO         contentCleanupPatternDAO;
   private IUnstructuredApplicationDetailDAO unstructuredApplicationDetailDAO;
   private IRISharedUserModelMappingDAO      riSharedUserModelMappingDAO;
   private IConceptDAO                       conceptDAO;
   private IInstanceDAO                      instanceDAO;
   private IEntityBehaviorDAO                entityBehaviorDAO;
   private IEntityAttributeDAO               entityAttributeDAO;
   private IRelationDAO                      relationDAO;
   private IApplicationDAO                   applicationDAO;
   private IVerticalDAO                      verticalDAO;
   private IModelDAO                         modelDAO;
   private IProfileDAO                       profileDAO;
   private IDefaultDynamicValueDAO           defaultDynamicValueDAO;
   private IStatDAO                          statDAO;
   private IBusinessEntityMaintenanceDAO     businessEntityMaintenanceDAO;
   private IPopularityHitDAO                 popularityHitDAO;
   private IMappingDAO                       mappingDAO;
   private IHierarchyDAO                     hierarchyDAO;

   public IHierarchyDAO getHierarchyDAO () {
      return hierarchyDAO;
   }

   public void setHierarchyDAO (IHierarchyDAO hierarchyDAO) {
      this.hierarchyDAO = hierarchyDAO;
   }

   public ITypeDAO getTypeDAO () {
      return typeDAO;
   }

   public void setTypeDAO (ITypeDAO typeDAO) {
      this.typeDAO = typeDAO;
   }

   public ICloudDAO getCloudDAO () {
      return cloudDAO;
   }

   public void setCloudDAO (ICloudDAO cloudDAO) {
      this.cloudDAO = cloudDAO;
   }

   public IBusinessEntityDefinitionDAO getBusinessEntityDefinitionDAO () {
      return businessEntityDefinitionDAO;
   }

   public void setBusinessEntityDefinitionDAO (IBusinessEntityDefinitionDAO businessEntityDefinitionDAO) {
      this.businessEntityDefinitionDAO = businessEntityDefinitionDAO;
   }

   public ISFLWordDAO getSflWordDAO () {
      return sflWordDAO;
   }

   public void setSflWordDAO (ISFLWordDAO sflWordDAO) {
      this.sflWordDAO = sflWordDAO;
   }

   public IParallelWordDAO getParallelWordDAO () {
      return parallelWordDAO;
   }

   public void setParallelWordDAO (IParallelWordDAO parallelWordDAO) {
      this.parallelWordDAO = parallelWordDAO;
   }

   public IOntoReverseIndexDAO getOntoReverseIndexDAO () {
      return ontoReverseIndexDAO;
   }

   public void setOntoReverseIndexDAO (IOntoReverseIndexDAO ontoReverseIndexDAO) {
      this.ontoReverseIndexDAO = ontoReverseIndexDAO;
   }

   /**
    * @return the pathDefinitionDAO
    */
   public IPathDefinitionDAO getPathDefinitionDAO () {
      return pathDefinitionDAO;
   }

   /**
    * @param pathDefinitionDAO
    *           the pathDefinitionDAO to set
    */
   public void setPathDefinitionDAO (IPathDefinitionDAO pathDefinitionDAO) {
      this.pathDefinitionDAO = pathDefinitionDAO;
   }

   public IBaseBusinessEntityDefinitionDAO getBaseBusinessEntityDefinitionDAO () {
      return baseBusinessEntityDefinitionDAO;
   }

   public void setBaseBusinessEntityDefinitionDAO (IBaseBusinessEntityDefinitionDAO baseBusinessEntityDefinitionDAO) {
      this.baseBusinessEntityDefinitionDAO = baseBusinessEntityDefinitionDAO;
   }

   public IBusinessEntityVariationDAO getBusinessEntityVariationDAO () {
      return businessEntityVariationDAO;
   }

   public void setBusinessEntityVariationDAO (IBusinessEntityVariationDAO businessEntityVariationDAO) {
      this.businessEntityVariationDAO = businessEntityVariationDAO;
   }

   public IContentCleanupPatternDAO getContentCleanupPatternDAO () {
      return contentCleanupPatternDAO;
   }

   public void setContentCleanupPatternDAO (IContentCleanupPatternDAO contentCleanupPatternDAO) {
      this.contentCleanupPatternDAO = contentCleanupPatternDAO;
   }

   /**
    * @return the unstructuredApplicationDetailDAO
    */
   public IUnstructuredApplicationDetailDAO getUnstructuredApplicationDetailDAO () {
      return unstructuredApplicationDetailDAO;
   }

   /**
    * @param unstructuredApplicationDetailDAO
    *           the unstructuredApplicationDetailDAO to set
    */
   public void setUnstructuredApplicationDetailDAO (IUnstructuredApplicationDetailDAO unstructuredApplicationDetailDAO) {
      this.unstructuredApplicationDetailDAO = unstructuredApplicationDetailDAO;
   }

   public IRISharedUserModelMappingDAO getRiSharedUserModelMappingDAO () {
      return riSharedUserModelMappingDAO;
   }

   public void setRiSharedUserModelMappingDAO (IRISharedUserModelMappingDAO riSharedUserModelMappingDAO) {
      this.riSharedUserModelMappingDAO = riSharedUserModelMappingDAO;
   }

   /**
    * @return the conceptDAO
    */
   public IConceptDAO getConceptDAO () {
      return conceptDAO;
   }

   /**
    * @param conceptDAO
    *           the conceptDAO to set
    */
   public void setConceptDAO (IConceptDAO conceptDAO) {
      this.conceptDAO = conceptDAO;
   }

   /**
    * @return the instanceDAO
    */
   public IInstanceDAO getInstanceDAO () {
      return instanceDAO;
   }

   /**
    * @param instanceDAO
    *           the instanceDAO to set
    */
   public void setInstanceDAO (IInstanceDAO instanceDAO) {
      this.instanceDAO = instanceDAO;
   }

   /**
    * @return the entityBehaviorDAO
    */
   public IEntityBehaviorDAO getEntityBehaviorDAO () {
      return entityBehaviorDAO;
   }

   /**
    * @param entityBehaviorDAO
    *           the entityBehaviorDAO to set
    */
   public void setEntityBehaviorDAO (IEntityBehaviorDAO entityBehaviorDAO) {
      this.entityBehaviorDAO = entityBehaviorDAO;
   }

   /**
    * @return the entityAttributeDAO
    */
   public IEntityAttributeDAO getEntityAttributeDAO () {
      return entityAttributeDAO;
   }

   /**
    * @param entityAttributeDAO
    *           the entityAttributeDAO to set
    */
   public void setEntityAttributeDAO (IEntityAttributeDAO entityAttributeDAO) {
      this.entityAttributeDAO = entityAttributeDAO;
   }

   /**
    * @return the relationDAO
    */
   public IRelationDAO getRelationDAO () {
      return relationDAO;
   }

   /**
    * @param relationDAO
    *           the relationDAO to set
    */
   public void setRelationDAO (IRelationDAO relationDAO) {
      this.relationDAO = relationDAO;
   }

   /**
    * @return the applicationDAO
    */
   public IApplicationDAO getApplicationDAO () {
      return applicationDAO;
   }

   /**
    * @param applicationDAO
    *           the applicationDAO to set
    */
   public void setApplicationDAO (IApplicationDAO applicationDAO) {
      this.applicationDAO = applicationDAO;
   }

   /**
    * @return the verticalDAO
    */
   public IVerticalDAO getVerticalDAO () {
      return verticalDAO;
   }

   /**
    * @param verticalDAO
    *           the verticalDAO to set
    */
   public void setVerticalDAO (IVerticalDAO verticalDAO) {
      this.verticalDAO = verticalDAO;
   }

   /**
    * @return the modelDAO
    */
   public IModelDAO getModelDAO () {
      return modelDAO;
   }

   /**
    * @param modelDAO
    *           the modelDAO to set
    */
   public void setModelDAO (IModelDAO modelDAO) {
      this.modelDAO = modelDAO;
   }

   /**
    * @return the profileDAO
    */
   public IProfileDAO getProfileDAO () {
      return profileDAO;
   }

   /**
    * @param profileDAO
    *           the profileDAO to set
    */
   public void setProfileDAO (IProfileDAO profileDAO) {
      this.profileDAO = profileDAO;
   }

   /**
    * @return the defaultDynamicValueDAO
    */
   public IDefaultDynamicValueDAO getDefaultDynamicValueDAO () {
      return defaultDynamicValueDAO;
   }

   /**
    * @param defaultDynamicValueDAO
    *           the defaultDynamicValueDAO to set
    */
   public void setDefaultDynamicValueDAO (IDefaultDynamicValueDAO defaultDynamicValueDAO) {
      this.defaultDynamicValueDAO = defaultDynamicValueDAO;
   }

   /**
    * @return the statDAO
    */
   public IStatDAO getStatDAO () {
      return statDAO;
   }

   /**
    * @param statDAO
    *           the statDAO to set
    */
   public void setStatDAO (IStatDAO statDAO) {
      this.statDAO = statDAO;
   }

   /**
    * @return the businessEntityMaintenanceDAO
    */
   public IBusinessEntityMaintenanceDAO getBusinessEntityMaintenanceDAO () {
      return businessEntityMaintenanceDAO;
   }

   /**
    * @param businessEntityMaintenanceDAO
    *           the businessEntityMaintenanceDAO to set
    */
   public void setBusinessEntityMaintenanceDAO (IBusinessEntityMaintenanceDAO businessEntityMaintenanceDAO) {
      this.businessEntityMaintenanceDAO = businessEntityMaintenanceDAO;
   }

   /**
    * @return the popularityHitDAO
    */
   public IPopularityHitDAO getPopularityHitDAO () {
      return popularityHitDAO;
   }

   /**
    * @param popularityHitDAO
    *           the popularityHitDAO to set
    */
   public void setPopularityHitDAO (IPopularityHitDAO popularityHitDAO) {
      this.popularityHitDAO = popularityHitDAO;
   }

   /**
    * @return the mappingDAO
    */
   public IMappingDAO getMappingDAO () {
      return mappingDAO;
   }

   /**
    * @param mappingDAO
    *           the mappingDAO to set
    */
   public void setMappingDAO (IMappingDAO mappingDAO) {
      this.mappingDAO = mappingDAO;
   }
}
