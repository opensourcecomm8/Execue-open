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


package com.execue.swi.validation.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Hierarchy;
import com.execue.core.common.bean.entity.HierarchyDetail;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.comparator.ConceptIDComparator;
import com.execue.core.common.bean.entity.comparator.InstanceIDComparator;
import com.execue.core.common.type.ProfileType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.dataaccess.swi.dao.IAssetDAO;
import com.execue.dataaccess.swi.dao.IAssetEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IBusinessEntityDefinitionDAO;
import com.execue.dataaccess.swi.dao.IInstanceDAO;
import com.execue.dataaccess.swi.dao.IProfileDAO;
import com.execue.dataaccess.swi.dao.IRangeDAO;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.validation.ISWIValidator;

public class SWIValidatorImpl implements ISWIValidator {

   private IBusinessEntityDefinitionDAO businessEntityDefinitionDAO;
   private IAssetEntityDefinitionDAO    assetEntityDefinitionDAO;
   private IKDXRetrievalService         kdxRetrievalService;
   private IProfileDAO                  profileDAO;
   private IRangeDAO                    rangeDAO;
   private IInstanceDAO                 instanceDAO;
   private IAssetDAO                    assetDAO;
   private Logger                       log = Logger.getLogger(SWIValidatorImpl.class);

   public IBusinessEntityDefinitionDAO getBusinessEntityDefinitionDAO () {
      return businessEntityDefinitionDAO;
   }

   public void setBusinessEntityDefinitionDAO (IBusinessEntityDefinitionDAO businessEntityDefinitionDAO) {
      this.businessEntityDefinitionDAO = businessEntityDefinitionDAO;
   }

   public IAssetEntityDefinitionDAO getAssetEntityDefinitionDAO () {
      return assetEntityDefinitionDAO;
   }

   public void setAssetEntityDefinitionDAO (IAssetEntityDefinitionDAO assetEntityDefinitionDAO) {
      this.assetEntityDefinitionDAO = assetEntityDefinitionDAO;
   }

   public <DomainObject extends Serializable> boolean objectExists (String name, Class<DomainObject> clazz) {
      try {
         businessEntityDefinitionDAO.getByName(name, clazz);
      } catch (DataAccessException dataAccessException) {
         if (dataAccessException.getCode() == DataAccessExceptionCodes.ENTITY_NOT_FOUND_CODE)
            return false;
      }
      return true;
   }

   public <DomainObject extends Serializable> DomainObject objectExistsById (Long id, Class<DomainObject> clazz) {
      try {
         return businessEntityDefinitionDAO.getById(id, clazz);
      } catch (DataAccessException dataAccessException) {

      }
      return null;
   }

   public boolean isConceptProfileExists (Profile profile, Long modelId) {
      boolean isExists = false;
      try {
         // get all the concept profiles from system
         List<ConceptProfile> conceptProfiles = getProfileDAO().getConceptProfiles(modelId);
         // check in all the profiles except that one in which the id is same as the profile we are looking for
         for (ConceptProfile conceptProfile : conceptProfiles) {
            if (profile.getId() == null && conceptProfile.getName().equalsIgnoreCase(profile.getName())) {
               isExists = true;
               break;
            }
            if (profile.getId() != null && conceptProfile.getId().longValue() != profile.getId().longValue()
                     && conceptProfile.getName().equalsIgnoreCase(profile.getName())) {
               isExists = true;
               break;
            }
         }
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
      }
      return isExists;
   }

   public boolean isInstanceProfileExists (Profile profile, Long modelId) {
      boolean isExists = false;
      try {
         // get all the instance profiles from system
         List<InstanceProfile> instanceProfiles = getProfileDAO().getInstanceProfiles(modelId);
         // check in all the profiles except that one in which the id is same as the profile we are looking for
         for (InstanceProfile instanceProfile : instanceProfiles) {
            if (profile.getId() == null && instanceProfile.getName().equalsIgnoreCase(profile.getName())) {
               isExists = true;
               break;
            }
            if (profile.getId() != null && instanceProfile.getId().longValue() != profile.getId().longValue()
                     && instanceProfile.getName().equalsIgnoreCase(profile.getName())) {
               isExists = true;
               break;
            }
         }
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
      }
      return isExists;
   }

   public boolean profileExists (Long modelId, Profile profile, Long parentBedId) throws KDXException {
      boolean memberExists = false;
      try {
         if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(profile.getType())) {
            InstanceProfile instanceProfile = (InstanceProfile) profile;
            Instance instanceProfileByInstanceName = getKdxRetrievalService().getInstanceByInstanceName(parentBedId,
                     instanceProfile.getName());
            Concept conceptByProfileName = getKdxRetrievalService()
                     .getConceptByName(modelId, instanceProfile.getName());
            if (isInstanceProfileExists(instanceProfile, modelId) || isConceptProfileExists(profile, modelId)
                     || instanceProfileByInstanceName != null || conceptByProfileName != null) {
               return true;
            }
            List<Instance> instanceListFromReq = new ArrayList<Instance>(instanceProfile.getInstances());
            List<InstanceProfile> instanceProfileList = new ArrayList<InstanceProfile>(getProfileDAO()
                     .getInstanceProfiles(modelId));
            Collections.sort(instanceListFromReq, new InstanceIDComparator());
            // Collections.sort(instanceProfileList, new InstanceIDComparator());

            outer: for (InstanceProfile instProf : instanceProfileList) {
               log.debug("Instance Profile Name : " + instProf.getName());
               if (instanceProfile.getId() != null
                        && instProf.getId().longValue() == instanceProfile.getId().longValue()) {
                  continue outer;
               }
               List<Instance> instanceList = new ArrayList<Instance>(instProf.getInstances());
               Collections.sort(instanceList, new InstanceIDComparator());
               log.debug("Size of list from req " + instanceListFromReq.size());
               log.debug("Size of list from DB " + instanceList.size());

               if (instanceListFromReq.size() == instanceList.size()) {
                  boolean isInstanceMemberExist = true;
                  for (int i = 0; i < instanceList.size(); i++) {
                     log.debug("List Size Same ");
                     if (instanceListFromReq.get(i).getId().compareTo(instanceList.get(i).getId()) != 0) {
                        isInstanceMemberExist = false;
                        break;
                     }
                  }
                  memberExists = isInstanceMemberExist;
               }
               if (memberExists) {
                  break outer;
               }
            }
         }

         if (ProfileType.CONCEPT.equals(profile.getType())) {
            ConceptProfile conceptProfile = (ConceptProfile) profile;
            Concept conceptByProfileName = getKdxRetrievalService().getConceptByName(modelId, conceptProfile.getName());
            if (isConceptProfileExists(conceptProfile, modelId) || isInstanceProfileExists(profile, modelId)
                     || conceptByProfileName != null) {
               return true;
            }
            List<Concept> conceptListFromReq = new ArrayList<Concept>(conceptProfile.getConcepts());
            List<ConceptProfile> conceptProfileList = new ArrayList<ConceptProfile>(getProfileDAO().getConceptProfiles(
                     modelId));
            Collections.sort(conceptListFromReq, new ConceptIDComparator());
            outer: for (ConceptProfile conceptProf : conceptProfileList) {
               if (conceptProfile.getId() != null
                        && conceptProf.getId().longValue() == conceptProfile.getId().longValue()) {
                  continue outer;
               }
               List<Concept> conceptList = new ArrayList<Concept>(conceptProf.getConcepts());
               Collections.sort(conceptList, new ConceptIDComparator());

               if (conceptListFromReq.size() == conceptList.size()) {
                  boolean isConceptMemberExists = true;
                  for (int i = 0; i < conceptList.size(); i++) {
                     if (conceptListFromReq.get(i).getId().compareTo(conceptList.get(i).getId()) != 0) {
                        isConceptMemberExists = false;
                        break;
                     }
                  }
                  memberExists = isConceptMemberExists;
               }
               if (memberExists) {
                  break outer;
               }
            }
         }
      } catch (DataAccessException dataAccessException) {
         dataAccessException.printStackTrace();
      }

      log.debug("Member Exists is " + memberExists);
      return memberExists;
   }

   // public <DomainObject extends Serializable> boolean instanceExistsForConcept (Domain domain, Concept concept,
   // String name) {
   // try {
   // List<Instance> instances = domainEntityDefinitionDAO.getInstances(domain, concept);
   // for (Instance instance : instances) {
   // if (instance.getDisplayName().equalsIgnoreCase(name)) {
   // return true;
   // }
   // }
   //
   // } catch (DataAccessException dataAccessException) {
   // if (log.isDebugEnabled())
   // log.debug("Exception code is " + dataAccessException.getCode());
   // }
   // return false;
   // }
   public <DomainObject extends Serializable> boolean instanceExistsForConcept (Long modelId, Long conceptId,
            String instanceDisplayName) {
      try {
         List<Instance> instances = getInstanceDAO().getInstances(modelId, conceptId);
         for (Instance instance : instances) {
            if (instance.getDisplayName().equalsIgnoreCase(instanceDisplayName)) {
               return true;
            }
         }
      } catch (DataAccessException dataAccessException) {
         if (log.isDebugEnabled())
            log.debug("Exception code is " + dataAccessException.getCode());
      }
      return false;
   }

   public <DomainObject extends Serializable> boolean instanceExistsForType (Long modelId, Long typeId,
            String instanceDisplayName) {
      try {
         List<Instance> instances = getInstanceDAO().getTypeInstances(modelId, typeId);
         for (Instance instance : instances) {
            if (instance.getDisplayName().equalsIgnoreCase(instanceDisplayName)) {
               return true;
            }
         }
      } catch (DataAccessException dataAccessException) {
         if (log.isDebugEnabled())
            log.debug("Exception code is " + dataAccessException.getCode());
      }
      return false;
   }

   public boolean dataSourceAlreadyExists (DataSource dataSource) {
      try {
         DataSource existingDataSource = assetEntityDefinitionDAO.getByName(dataSource.getName(), DataSource.class,
                  false);
         if (existingDataSource.getId() != dataSource.getId() && dataSource.getId() != null) {
            return false;
         }
      } catch (DataAccessException dae) {
         if (dae.getCode() == DataAccessExceptionCodes.ENTITY_NOT_FOUND_CODE) {
            return false;
         }
      }
      return true;
   }

   public boolean assetsExistForDataSource (Long dataSourceId) {
      try {
         List<Asset> assets = getAssetDAO().getAssetsForDataSource(dataSourceId);
         if (assets.size() > 0) {
            return true;
         }
         return false;
      } catch (DataAccessException dae) {
         return false;
      }
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public <DomainObject extends Serializable> Range rangeExists (Long modelId, String rangeName) {
      try {
         return getRangeDAO().getRangeByNameForModel(modelId, rangeName);
      } catch (DataAccessException e) {
         e.printStackTrace();
         return null;
      }
   }

   public boolean hierarchyExists (Long modelId, Hierarchy hierarchy) {
      boolean hierarchyExists = false;
      try {
         Long hierarchyId = hierarchy.getId();
         Set<HierarchyDetail> hierarchyDetails = hierarchy.getHierarchyDetails();
         List<Hierarchy> swiHierarchies = getKdxRetrievalService().getHierarchiesByModelId(modelId);
         if (hierarchyId != null) {
            if (ExecueCoreUtil.isCollectionNotEmpty(swiHierarchies)) {
               for (Hierarchy swiHierarchy : swiHierarchies) {
                  Set<HierarchyDetail> swiHierarchyDetails = swiHierarchy.getHierarchyDetails();
                  boolean isSameEntity = swiHierarchy.getId().longValue() == hierarchyId.longValue();
                  if (!isSameEntity
                           && ((swiHierarchy.getName().equalsIgnoreCase(hierarchy.getName().trim())) || (swiHierarchyDetails
                                    .size() == hierarchyDetails.size() && swiHierarchyDetails
                                    .containsAll(hierarchyDetails)))) {
                     hierarchyExists = true;
                     break;
                  }
               }
            }

         } else {
            if (ExecueCoreUtil.isCollectionNotEmpty(swiHierarchies)) {
               for (Hierarchy swiHierarchy : swiHierarchies) {
                  Set<HierarchyDetail> swiHierarchyDetails = swiHierarchy.getHierarchyDetails();
                  if ((swiHierarchy.getName().equalsIgnoreCase(hierarchy.getName().trim()))
                           || (swiHierarchyDetails.size() == hierarchyDetails.size() && swiHierarchyDetails
                                    .containsAll(hierarchyDetails))) {
                     hierarchyExists = true;
                     break;
                  }
               }
            }
         }

      } catch (KDXException e) {
         e.printStackTrace();

      }
      return hierarchyExists;
   }

   public IProfileDAO getProfileDAO () {
      return profileDAO;
   }

   public void setProfileDAO (IProfileDAO profileDAO) {
      this.profileDAO = profileDAO;
   }

   public IRangeDAO getRangeDAO () {
      return rangeDAO;
   }

   public void setRangeDAO (IRangeDAO rangeDAO) {
      this.rangeDAO = rangeDAO;
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

   public IAssetDAO getAssetDAO () {
      return assetDAO;
   }

   public void setAssetDAO (IAssetDAO assetDAO) {
      this.assetDAO = assetDAO;
   }

}