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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.comparator.ConceptDisplaNameComparator;
import com.execue.core.common.bean.entity.comparator.InstanceDisplaNameComparator;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ProfileType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;

public class ProfilesAction extends SWIAction {

   private static final Logger   log           = Logger.getLogger(ProfilesAction.class);

   private List<Concept>         baseProfileConcepts;
   private List<ConceptProfile>  conceptProfiles;
   private List<InstanceProfile> instanceProfiles;
   private List<Concept>         domainProfileConcepts;
   private List<Instance>        conceptInstances;
   private Long                  profileId;
   private ConceptProfile        conceptProfile;
   private InstanceProfile       instanceProfile;
   private String                profileType;
   private Long                  conceptId;
   private List<Concept>         conceptProfileConcepts;
   private List<Instance>        instanceProfileInstances;
   private List<Concept>         newConceptProfileConcepts;
   private List<Instance>        newInstanceProfileInstances;
   private ProfileStatus         profileStatus;
   private String                paginationType;
   public static final int       PAGESIZE      = 9;
   public static final int       numberOfLinks = 4;
   private String                requestedPage;
   private CheckType             hybridProfile = CheckType.NO;
   private String                searchString;
   private String                searchType    = "showAll";
   private static final String   START_WITH    = "startWith";
   private static final String   CONTAINS      = "contains";

   public ProfileStatus getProfileStatus () {
      return profileStatus;
   }

   public void setProfileStatus (ProfileStatus profileStatus) {
      this.profileStatus = profileStatus;
   }

   public List<Concept> getNewConceptProfileConcepts () {
      return newConceptProfileConcepts;
   }

   public void setNewConceptProfileConcepts (List<Concept> newConceptProfileConcepts) {
      this.newConceptProfileConcepts = newConceptProfileConcepts;
   }

   public List<Instance> getNewInstanceProfileInstances () {
      return newInstanceProfileInstances;
   }

   public void setNewInstanceProfileInstances (List<Instance> newInstanceProfileInstances) {
      this.newInstanceProfileInstances = newInstanceProfileInstances;
   }

   public List<Instance> getConceptInstances () {
      return conceptInstances;
   }

   public void setConceptInstances (List<Instance> conceptInstances) {
      this.conceptInstances = conceptInstances;
   }

   public ConceptProfile getConceptProfile () {
      return conceptProfile;
   }

   public void setConceptProfile (ConceptProfile conceptProfile) {
      this.conceptProfile = conceptProfile;
   }

   public InstanceProfile getInstanceProfile () {
      return instanceProfile;
   }

   public void setInstanceProfile (InstanceProfile instanceProfile) {
      this.instanceProfile = instanceProfile;
   }

   public String getProfileType () {
      return profileType;
   }

   public void setProfileType (String profileType) {
      this.profileType = profileType;
   }

   public Long getProfileId () {
      return profileId;
   }

   public void setProfileId (Long profileId) {
      this.profileId = profileId;
   }

   public List<Concept> getDomainProfileConcepts () {
      return domainProfileConcepts;
   }

   public void setDomainProfileConcepts (List<Concept> domainProfileConcepts) {
      this.domainProfileConcepts = domainProfileConcepts;
   }

   public List<Concept> getBaseProfileConcepts () {
      return baseProfileConcepts;
   }

   public void setBaseProfileConcepts (List<Concept> baseProfileConcepts) {
      this.baseProfileConcepts = baseProfileConcepts;
   }

   public List<ConceptProfile> getConceptProfiles () {
      return conceptProfiles;
   }

   public void setConceptProfiles (List<ConceptProfile> conceptProfiles) {
      this.conceptProfiles = conceptProfiles;
   }

   public List<InstanceProfile> getInstanceProfiles () {
      return instanceProfiles;
   }

   public void setInstanceProfiles (List<InstanceProfile> instanceProfiles) {
      this.instanceProfiles = instanceProfiles;
   }

   public Long getConceptId () {
      return conceptId;
   }

   public void setConceptId (Long conceptId) {
      this.conceptId = conceptId;
   }

   public List<Concept> getConceptProfileConcepts () {
      return conceptProfileConcepts;
   }

   public void setConceptProfileConcepts (List<Concept> conceptProfileConcepts) {
      this.conceptProfileConcepts = conceptProfileConcepts;
   }

   public List<Instance> getInstanceProfileInstances () {
      return instanceProfileInstances;
   }

   public void setInstanceProfileInstances (List<Instance> instanceProfileInstances) {
      this.instanceProfileInstances = instanceProfileInstances;
   }

   // Action Methods
   public String input () {
      if (log.isDebugEnabled()) {
         log.debug("input method called");
      }
      try {
         setBaseProfileConcepts(getPreferencesServiceHandler().getBaseProfiles(getApplicationContext().getModelId()));
         if (paginationType != null && paginationType.equalsIgnoreCase("conceptsForProfiles")) {
            paginationForProfileConcepts();
         }
      } catch (ExeCueException execueException) {
         log.error(execueException, execueException);
         if (log.isDebugEnabled()) {
            log.debug("returning error");
         }
         return ERROR;
      }
      if (log.isDebugEnabled()) {
         log.debug("returning success");
      }
      return SUCCESS;
   }

   @SuppressWarnings ("unchecked")
   public String showSubConceptsforProfiles () {
      int reqPageNo = 1;
      if (getRequestedPage() != null) {
         reqPageNo = Integer.parseInt(getRequestedPage());
      }
      int fromIndex = 1;
      int toIndex = 1;
      if (paginationType != null && paginationType.equalsIgnoreCase("conceptsForProfiles")) {
         List<Concept> conceptList = (List<Concept>) getHttpSession().get("CONCEPTSFORPROFILES");
         if (searchString != null) {
            conceptList = searchRecordsByFilterCriteria(conceptList);
         }
         if (ExecueCoreUtil.isCollectionNotEmpty(conceptList)) {
            int tempTotCount = (conceptList.size() / PAGESIZE);
            int rmndr = conceptList.size() % PAGESIZE;
            if (rmndr != 0) {
               tempTotCount++;
            }
            if (reqPageNo <= tempTotCount) {
               fromIndex = ((reqPageNo - 1) * PAGESIZE);
               toIndex = reqPageNo * PAGESIZE;
               if (toIndex > conceptList.size())
                  toIndex = (conceptList.size());
            }
            log.info("Getting Columns SubList from -> " + fromIndex + " to " + toIndex);

            baseProfileConcepts = conceptList.subList(fromIndex, toIndex);
         }

      }

      return SUCCESS;
   }

   private List<Concept> searchRecordsByFilterCriteria (List<Concept> conceptList) {
      List<Concept> concepts = new ArrayList<Concept>();
      if (searchType.equals(START_WITH)) {
         for (Concept concept : conceptList) {
            String cDispName = concept.getDisplayName();
            if (cDispName.toLowerCase().startsWith(searchString.toLowerCase())) {
               concepts.add(concept);
            }
         }
      } else if (searchType.equals(CONTAINS)) {
         for (Concept concept : conceptList) {
            String cDispName = concept.getDisplayName();
            if (cDispName.toLowerCase().contains(searchString.toLowerCase())) {
               concepts.add(concept);
            }
         }

      }
      return concepts;
   }

   public String getAllConceptProfiles () {
      try {
         List<Concept> allProfiles = getPreferencesServiceHandler().getConceptsForConceptProfiles(
                  getApplicationContext().getModelId(), hybridProfile);
         log.debug("Total profile size : " + allProfiles.size());
         setDomainProfileConcepts(allProfiles);
         return SUCCESS;
      } catch (ExeCueException execueException) {
         // to do handle exception
         execueException.printStackTrace();
         return SUCCESS;
      }
   }

   // Action Methods
   public String getAllBaseProfiles () {
      try {
         List<Concept> allProfiles = getPreferencesServiceHandler().getBaseProfiles(
                  getApplicationContext().getModelId());
         log.debug("Total profile size : " + allProfiles.size());
         setBaseProfileConcepts(allProfiles);
         return SUCCESS;
      } catch (ExeCueException execueException) {
         // to do handle exception
         execueException.printStackTrace();
         return SUCCESS;
      }
   }

   // Action Methods
   public String getAllUserProfiles () {
      if (log.isDebugEnabled()) {
         log.debug("Getting user profiles");
      }
      try {
         List<ConceptProfile> conceptProfiles = getPreferencesServiceHandler().getConceptProfiles(
                  getApplicationContext().getModelId());
         log.debug("Concept profile size : " + conceptProfiles.size());
         setConceptProfiles(conceptProfiles);

         List<InstanceProfile> instanceProfiles = getPreferencesServiceHandler().getInstanceProfiles(
                  getApplicationContext().getModelId());
         log.debug("Instance profile size : " + instanceProfiles.size());
         setInstanceProfiles(instanceProfiles);

      } catch (ExeCueException execueException) {
         log.error(execueException);
         if (log.isDebugEnabled()) {
            log.debug("returning error");
         }
         return ERROR;
      }
      if (log.isDebugEnabled()) {
         log.debug("returning success");
      }
      return SUCCESS;
   }

   public String createProfile () {
      if (log.isDebugEnabled()) {
         log.debug("profileType : " + profileType);
      }
      if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(ProfileType.getProfileType(profileType))) {
         if (log.isDebugEnabled()) {
            log.debug("Inastance Profile Display Name : " + instanceProfile.getDisplayName());
            log.debug("Inastance Profile Description  : " + instanceProfile.getDescription());
            log.debug("Inastance Profile Concept Id   : " + instanceProfile.getConcept().getId());
            if (instanceProfileInstances != null) {
               log.debug("Inastance Profile Instance Count : " + instanceProfileInstances.size());
            }
         } else {
            if (log.isDebugEnabled()) {
               log.debug("Concept Profile Display Name : " + conceptProfile.getDisplayName());
               log.debug("Concept Profile Description  : " + conceptProfile.getDescription());
               if (conceptProfileConcepts != null) {
                  log.debug("Concept Profile Instance Count : " + conceptProfileConcepts.size());
               }
            }
         }
      }
      try {
         setProfileStatus(new ProfileStatus());
         if (ProfileType.CONCEPT_LOOKUP_INSTANCE.equals(ProfileType.getProfileType(profileType))) {
            if (instanceProfile.getId() != null) {
               setMessage(updateInstanceProfile());
            } else {
               log.debug("createInstanceProfile Called");
               setMessage(createInstanceProfile());
            }
         } else {
            if (conceptProfile.getId() != null) {
               setMessage(updateConceptProfile());
            } else {
               setMessage(createConceptProfile());
            }
         }

      } catch (ExeCueException execueException) {
         getProfileStatus().setStatus("error");
         if (execueException.getCode() == 900018) {
            getProfileStatus().setMessage(getText("execue.profile.exist.message"));
         } else if (execueException.getCode() == ExeCueExceptionCodes.RESERVE_WORD_MATCH) {
            getProfileStatus().setMessage(getText("execue.kdx.rwmatchforprofile.message"));
         } else {
            log.error(execueException);
            if (log.isDebugEnabled()) {
               log.debug("returning error");
            }
            getProfileStatus().setMessage(getText("execue.profile.create.faliure"));
         }
      }
      return SUCCESS;
   }

   private void populateInstanceProfile () throws ExeCueException {
      Concept concept = getPreferencesServiceHandler().getConceptById(instanceProfile.getConcept().getId());
      instanceProfile.setConcept(concept);
      Set<Instance> instances = new HashSet<Instance>();
      if (instanceProfileInstances != null) {
         for (Instance instance : instanceProfileInstances) {
            if (instance != null) {
               instances.add(instance);
            }
         }
      }
      if (newInstanceProfileInstances != null) {
         for (Instance instance : newInstanceProfileInstances) {
            if (instance != null) {
               instances.add(instance);
            }
         }
      }
      instanceProfile.setInstances(instances);
   }

   private void populateConceptProfile () {
      Set<Concept> concepts = new HashSet<Concept>();
      if (conceptProfileConcepts != null) {
         for (Concept concept : conceptProfileConcepts) {
            if (concept != null) {
               concepts.add(concept);
            }
         }
      }
      if (newConceptProfileConcepts != null) {
         for (Concept concept : newConceptProfileConcepts) {
            if (concept != null) {
               concepts.add(concept);
            }
         }
      }
      conceptProfile.setConcepts(concepts);
   }

   private String createInstanceProfile () throws ExeCueException {
      populateInstanceProfile();

      if (log.isDebugEnabled()) {
         log.debug(ExeCueXMLUtils.getXMLStringFromObject(instanceProfile));
      }
      if (instanceProfile.getInstances().size() > 1 && instanceProfile.getDisplayName() != null
               && instanceProfile.getDisplayName().trim() != "") {
         getPreferencesServiceHandler().createProfile(getApplicationContext().getModelId(), instanceProfile,
                  hybridProfile);
         if (log.isDebugEnabled()) {
            log.debug("Id after creation : " + instanceProfile.getId());
         }
         getProfileStatus().setInstanceProfileId(instanceProfile.getId());
         getProfileStatus().setInstanceProfileName(instanceProfile.getName());
         getProfileStatus().setStatus("success");
         getProfileStatus().setMessage(getText("execue.profile.create.success"));
         return getProfileStatus().getMessage();
      } else {
         if (instanceProfile.getDisplayName() == null || instanceProfile.getDisplayName().trim() == "") {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(
                     getText("execue.errors.required", new String[] { getText("execue.profiles.name.label") }));
         } else {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(getText("execue.profile.select.definitions"));
         }
         return getProfileStatus().getMessage();
      }

   }

   private String updateInstanceProfile () throws ExeCueException {
      populateInstanceProfile();
      if (instanceProfile.getInstances().size() > 1 && instanceProfile.getDisplayName() != null
               && instanceProfile.getDisplayName().trim() != "") {
         if (log.isDebugEnabled()) {
            log.debug(ExeCueXMLUtils.getXMLStringFromObject(instanceProfile));
         }
         getPreferencesServiceHandler().updateProfile(getApplicationContext().getModelId(), instanceProfile);
         getProfileStatus().setInstanceProfileId(instanceProfile.getId());
         getProfileStatus().setInstanceProfileName(instanceProfile.getName());
         getProfileStatus().setStatus("success");
         getProfileStatus().setMessage(getText("execue.profile.update.success"));
         return getProfileStatus().getMessage();
      } else {
         if (instanceProfile.getDisplayName() == null || instanceProfile.getDisplayName().trim() == "") {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(
                     getText("execue.errors.required", new String[] { getText("execue.profiles.name.label") }));
         } else {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(getText("execue.profile.select.definitions"));
         }
         return getProfileStatus().getMessage();
      }

   }

   private void paginationForProfileConcepts () {
      if (requestedPage == null)
         requestedPage = "1";
      getHttpSession().put("CONCEPTSFORPROFILES", baseProfileConcepts);

      int tempSize = 0;
      if (baseProfileConcepts.size() <= PAGESIZE)
         tempSize = baseProfileConcepts.size();
      else
         tempSize = PAGESIZE;
      log.info("displaying initial sublist");
      baseProfileConcepts = baseProfileConcepts.subList(0, tempSize);
   }

   private String createConceptProfile () throws ExeCueException {
      populateConceptProfile();
      if (log.isDebugEnabled()) {
         log.debug("XMLStringFromObject:" + ExeCueXMLUtils.getXMLStringFromObject(conceptProfile));
      }
      if (conceptProfile.getConcepts().size() > 1 && conceptProfile.getDisplayName() != null
               && conceptProfile.getDisplayName().trim() != "") {
         getPreferencesServiceHandler().createProfile(getApplicationContext().getModelId(), conceptProfile,
                  hybridProfile);
         if (log.isDebugEnabled()) {
            log.debug("Id after creation : " + conceptProfile.getId());
         }
         getProfileStatus().setConceptProfileId(conceptProfile.getId());
         getProfileStatus().setConceptProfileName(conceptProfile.getName());
         getProfileStatus().setStatus("success");
         getProfileStatus().setMessage(getText("execue.profile.create.success"));
         return getProfileStatus().getMessage();
      } else {
         if (conceptProfile.getDisplayName() == null || conceptProfile.getDisplayName().trim() == "") {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(
                     getText("execue.errors.required", new String[] { getText("execue.profiles.name.label") }));
         } else {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(getText("execue.profile.select.definitions"));
         }
         return getProfileStatus().getMessage();
      }
   }

   private String updateConceptProfile () throws ExeCueException {
      populateConceptProfile();
      if (log.isDebugEnabled()) {
         log.debug(ExeCueXMLUtils.getXMLStringFromObject(conceptProfile));
      }
      if (conceptProfile.getConcepts().size() > 1 && conceptProfile.getDisplayName() != null
               && conceptProfile.getDisplayName().trim() != "") {
         getPreferencesServiceHandler().updateProfile(getApplicationContext().getModelId(), conceptProfile);
         getProfileStatus().setConceptProfileId(conceptProfile.getId());
         getProfileStatus().setConceptProfileName(conceptProfile.getName());
         getProfileStatus().setStatus("success");
         getProfileStatus().setMessage(getText("execue.profile.update.success"));
         return getProfileStatus().getMessage();
      } else {
         if (conceptProfile.getDisplayName() == null || conceptProfile.getDisplayName().trim() == "") {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(
                     getText("execue.errors.required", new String[] { getText("execue.profiles.name.label") }));
         } else {
            getProfileStatus().setStatus("error");
            getProfileStatus().setMessage(getText("execue.profile.select.definitions"));
         }
         return getProfileStatus().getMessage();
      }
   }

   public String getProfileDefinition () {
      try {
         Profile profile = getPreferencesServiceHandler()
                  .getProfile(profileId, ProfileType.getProfileType(profileType));
         log.debug("enabled:::::" + profile.isEnabled());

         if (ProfileType.CONCEPT.equals(profile.getType())) {
            setConceptProfile((ConceptProfile) profile);
            setProfileType(ProfileType.CONCEPT.getValue());
            hybridProfile = getPreferencesServiceHandler().isConceptProfileHybridProfileType(
                     getApplicationContext().getModelId(), (ConceptProfile) profile);
            setDomainProfileConcepts(getPreferencesServiceHandler().getConceptsForConceptProfiles(
                     getApplicationContext().getModelId(), hybridProfile));
            conceptProfileConcepts = new ArrayList<Concept>(getConceptProfile().getConcepts());
            Collections.sort(conceptProfileConcepts, new ConceptDisplaNameComparator());
            setDomainProfileConcepts(removeConceptsFromDomainProfile(getDomainProfileConcepts(), conceptProfileConcepts));
         } else {
            setInstanceProfile((InstanceProfile) profile);
            setProfileType(ProfileType.CONCEPT_LOOKUP_INSTANCE.getValue());
            setConceptInstances(getPreferencesServiceHandler().getInstancesForConcept(
                     getApplicationContext().getModelId(), getInstanceProfile().getConcept().getId()));
            instanceProfileInstances = new ArrayList<Instance>(getInstanceProfile().getInstances());
            Collections.sort(instanceProfileInstances, new InstanceDisplaNameComparator());
            setConceptInstances(removeInstancesFromConceptInstances(getConceptInstances(), instanceProfileInstances));
         }
      } catch (ExeCueException execueException) {
         log.error(execueException);
         if (log.isDebugEnabled()) {
            log.debug("returning error");
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String getNewProfileDefinition () {
      try {
         if (ProfileType.CONCEPT.equals(ProfileType.getProfileType(profileType))) {
            setDomainProfileConcepts(getPreferencesServiceHandler().getConceptsForConceptProfiles(
                     getApplicationContext().getModelId(), hybridProfile));
            conceptProfile = new ConceptProfile();
            conceptProfile.setEnabled(true);
         } else {
            Concept concept = getPreferencesServiceHandler().getConceptById(conceptId);
            setConceptInstances(getPreferencesServiceHandler().getInstancesForConcept(
                     getApplicationContext().getModelId(), concept.getId()));
            instanceProfile = new InstanceProfile();
            instanceProfile.setEnabled(true);
            instanceProfile.setConcept(concept);
         }
      } catch (ExeCueException execueException) {
         log.error(execueException);
         if (log.isDebugEnabled()) {
            log.debug("returning error");
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteProfiles () {
      setProfileStatus(new ProfileStatus());
      List<Profile> profilesToBeDeleted = extractProfilesToBeDeleted();
      if (log.isDebugEnabled()) {
         log.debug("Count of profiles to be deleted : " + profilesToBeDeleted.size());
      }
      if (profilesToBeDeleted.size() > 0) {
         try {
            getPreferencesServiceHandler().deleteProfiles(getApplicationContext().getModelId(), profilesToBeDeleted);
            getProfileStatus().setStatus("success");
            getProfileStatus().setMessage(getText("execue.profile.delete.success"));
         } catch (ExeCueException execueException) {
            getProfileStatus().setStatus("error");
            log.error(execueException);
            getProfileStatus().setMessage(getText("execue.profile.delete.failure"));
         }
      } else {
         getProfileStatus().setStatus("error");
         getProfileStatus().setMessage(getText("execue.profile.delete.notSelected"));
      }
      return SUCCESS;
   }

   private List<Concept> removeConceptsFromDomainProfile (List<Concept> domainProfileConcepts,
            List<Concept> conceptProfileConcepts) {
      List<Concept> baseConcepts = new ArrayList<Concept>();
      List<Long> conceptProfileConceptIds = new ArrayList<Long>();
      for (Concept concept : conceptProfileConcepts) {
         conceptProfileConceptIds.add(concept.getId());
      }
      for (Concept concept : domainProfileConcepts) {
         if (!conceptProfileConceptIds.contains(concept.getId())) {
            baseConcepts.add(concept);
         }
      }
      return baseConcepts;
   }

   private List<Instance> removeInstancesFromConceptInstances (List<Instance> conceptInstances,
            List<Instance> instanceProfileInstances) {
      List<Instance> baseConcepts = new ArrayList<Instance>();
      List<Long> instanceProfileInstanceIds = new ArrayList<Long>();
      for (Instance instance : instanceProfileInstances) {
         instanceProfileInstanceIds.add(instance.getId());
      }
      for (Instance instance : conceptInstances) {
         if (!instanceProfileInstanceIds.contains(instance.getId())) {
            baseConcepts.add(instance);
         }
      }
      return baseConcepts;
   }

   private List<Profile> extractProfilesToBeDeleted () {
      List<Profile> profilesForDeletion = new ArrayList<Profile>();
      if (conceptProfiles != null) {
         for (ConceptProfile conceptProfile : conceptProfiles) {
            if (conceptProfile != null) {
               profilesForDeletion.add(conceptProfile);
            }
         }
      }
      if (instanceProfiles != null) {
         for (InstanceProfile instanceProfile : instanceProfiles) {
            if (instanceProfile != null) {
               profilesForDeletion.add(instanceProfile);
            }
         }
      }
      return profilesForDeletion;
   }

   public String getPaginationType () {
      return paginationType;
   }

   public void setPaginationType (String paginationType) {
      this.paginationType = paginationType;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public CheckType getHybridProfile () {
      return hybridProfile;
   }

   public void setHybridProfile (CheckType hybridProfile) {
      this.hybridProfile = hybridProfile;
   }

   public String getSearchString () {
      return searchString;
   }

   public void setSearchString (String searchString) {
      this.searchString = searchString;
   }

   public String getSearchType () {
      return searchType;
   }

   public void setSearchType (String searchType) {
      this.searchType = searchType;
   }
}
