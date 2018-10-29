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


package com.execue.platform.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.SearchFilter;
import com.execue.core.common.bean.SemanticSuggestTerm;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.type.ModelCategoryType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.common.type.SearchFilterType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.exception.DataAccessExceptionCodes;
import com.execue.platform.ISemanticSuggestService;
import com.execue.platform.exception.PlatformException;
import com.execue.swi.dataaccess.ISemanticSuggestDataAccessManager;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IApplicationRetrievalService;

/**
 * @author John Mallavalli
 */
public class SemanticSuggestServiceImpl implements ISemanticSuggestService {

   private static final Logger               logger = Logger.getLogger(SemanticSuggestServiceImpl.class);
   private ISemanticSuggestDataAccessManager semanticSuggestDataAccessManager;
   private IApplicationRetrievalService      applicationRetrievalService;

   public ISemanticSuggestDataAccessManager getSemanticSuggestDataAccessManager () {
      return semanticSuggestDataAccessManager;
   }

   public void setSemanticSuggestDataAccessManager (ISemanticSuggestDataAccessManager semanticSuggestDataAccessManager) {
      this.semanticSuggestDataAccessManager = semanticSuggestDataAccessManager;
   }

   public List<SemanticSuggestTerm> getSuggestTerms (String searchString, SearchFilter searchFilter, Long userId)
            throws PlatformException {
      List<SemanticSuggestTerm> semanticSuggestTerms = new ArrayList<SemanticSuggestTerm>();
      try {
         // semanticSuggestTerms.addAll(getConceptProfilesBySearchString(searchString));
         // semanticSuggestTerms.addAll(getInstanceProfilesBySearchString(searchString));
         // semanticSuggestTerms.addAll(getConceptsBySearchString(searchString));
         // semanticSuggestTerms.addAll(getInstancesBySearchString(searchString));
        //NOTE-JT- Changed the logic to get the modelGroupIds for branch

         List<Long> modelGroupIds = getAuthorizedModelGroupIdsForAutoSuggestions(userId, searchFilter, false);
         if (logger.isDebugEnabled()) {
            logger.debug("The authorized model group ids are " + modelGroupIds);
         }
         if (ExecueCoreUtil.isCollectionEmpty(modelGroupIds)) {
            return semanticSuggestTerms;
         }
         semanticSuggestTerms
                  .addAll(prepareConceptProfileWithAppNameBasedSuggestTermList(semanticSuggestDataAccessManager
                           .getConceptProfilesWithAppNameBySearchString(searchString, modelGroupIds)));
         semanticSuggestTerms
                  .addAll(prepareInstanceProfileWithAppNameBasedSuggestTermList(semanticSuggestDataAccessManager
                           .getInstanceProfilesWithAppNameBySearchString(searchString, modelGroupIds)));
         semanticSuggestTerms.addAll(prepareConceptWithAppNameBasedSuggestTermList(semanticSuggestDataAccessManager
                  .getConceptsWithAppNameBySearchString(searchString, modelGroupIds)));
         semanticSuggestTerms.addAll(prepareInstanceWithAppNameBasedSuggestTermList(semanticSuggestDataAccessManager
                  .getInstancesWithAppNameBySearchString(searchString, modelGroupIds)));
      } catch (SWIException e) {
         throw new PlatformException(e.getCode(), e);
      }
      return semanticSuggestTerms;
   }

   private List<Long> getAuthorizedModelGroupIdsForAutoSuggestions (Long userId, SearchFilter searchFilter, boolean skipOtherCommunityApps)
            throws PlatformException, SWIException {
      List<Long> authorizedModelGroupIds = null;
      List<Application> applications = null;
      if (userId !=null &&  userId > 0) {
         applications = getApplicationRetrievalService().getAllEligibleApplicationsByUserId(userId, skipOtherCommunityApps);
      } else {
         applications = getApplicationRetrievalService().getApplicationsForByPublishModeOrderByRank(
                  PublishAssetMode.COMMUNITY);
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(applications)) {
         if (SearchFilterType.GENERAL != searchFilter.getSearchFilterType()) {
            List<Application> filteredApplications = new ArrayList<Application>();
            if (SearchFilterType.VERTICAL.equals(searchFilter.getSearchFilterType())) {
               Long verticalId = searchFilter.getId();
               filteredApplications =getApplicationRetrievalService().getApplicationsByVerticalId(verticalId);
            } else {
               Long applicationId = searchFilter.getId();
               Application application = getApplicationRetrievalService().getApplicationById(applicationId);
               filteredApplications.add(application);
            }
            if (ExecueCoreUtil.isCollectionNotEmpty(filteredApplications)) {
               applications = filterVerticalApps(applications, filteredApplications);
            }
         } 
         authorizedModelGroupIds = getSemanticSuggestDataAccessManager().getAuthorizedModelGroupIdsForApplications(applications);
      }

      return authorizedModelGroupIds;
   }

   private List<Application> filterVerticalApps (List<Application> allApplications, List<Application> verticalApps) {
      List<Application> filteredApps = new ArrayList<Application>();
      for (Application application : allApplications) {
         if (isApplicationExists(application, verticalApps)) {
            filteredApps.add(application);
         }
      }
      return filteredApps;
   }

   private boolean isApplicationExists (Application application, List<Application> verticalApplications) {
      boolean isApplicationExists = false;
      for (Application verticalApplication : verticalApplications) {
         if (application.getId().equals(verticalApplication.getId())) {
            isApplicationExists = true;
            break;
         }
      }
      return isApplicationExists;
   }

  

   private List<SemanticSuggestTerm> prepareConceptProfileWithAppNameBasedSuggestTermList (
            List<BusinessEntityDefinition> beds) throws SWIException {
      List<SemanticSuggestTerm> suggestTerms = new ArrayList<SemanticSuggestTerm>();
      for (BusinessEntityDefinition bed : beds) {
         ConceptProfile conceptProfile = bed.getConceptProfile();
         SemanticSuggestTerm suggestTerm = new SemanticSuggestTerm();
         StringBuilder name = new StringBuilder();
         name.append(conceptProfile.getDisplayName());
         String appName = semanticSuggestDataAccessManager.getApplicationNameByModelGroup(bed.getModelGroup());
         if (appName != null) {
            name.append(" (").append(appName).append(")");
         }
         suggestTerm.setName(name.toString());
         suggestTerm.setDisplayName(conceptProfile.getDisplayName());
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SemanticSuggestTerm> prepareInstanceWithAppNameBasedSuggestTermList (List<BusinessEntityDefinition> beds)
            throws SWIException {
      List<SemanticSuggestTerm> suggestTerms = new ArrayList<SemanticSuggestTerm>();
      for (BusinessEntityDefinition bed : beds) {
         Instance instance = bed.getInstance();
         SemanticSuggestTerm suggestTerm = new SemanticSuggestTerm();
         String displayName = instance.getDisplayName();
         // TODO: -JVK- the abbreviations are commented out since the <dispname> (abbr) format is resulting in regex
         // syntax pattern exception - discuss and fix
         // if (ExecueCoreUtil.isNotEmpty(instance.getAbbreviatedName())) {
         // displayName += " (" + instance.getAbbreviatedName() + ")";
         // }
         StringBuilder name = new StringBuilder();
         name.append(instance.getDisplayName());
         String appName = semanticSuggestDataAccessManager.getApplicationNameByModelGroup(bed.getModelGroup());
         if (appName != null) {
            name.append(" (").append(appName).append(")");
         }
         suggestTerm.setName(name.toString());
         suggestTerm.setDisplayName(displayName);
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SemanticSuggestTerm> prepareInstanceProfileWithAppNameBasedSuggestTermList (
            List<BusinessEntityDefinition> beds) throws SWIException {
      List<SemanticSuggestTerm> suggestTerms = new ArrayList<SemanticSuggestTerm>();
      for (BusinessEntityDefinition bed : beds) {
         InstanceProfile instanceProfile = bed.getInstanceProfile();
         SemanticSuggestTerm suggestTerm = new SemanticSuggestTerm();
         StringBuilder name = new StringBuilder();
         name.append(instanceProfile.getDisplayName());
         String appName = semanticSuggestDataAccessManager.getApplicationNameByModelGroup(bed.getModelGroup());
         if (appName != null) {
            name.append(" (").append(appName).append(")");
         }
         suggestTerm.setName(name.toString());
         suggestTerm.setDisplayName(instanceProfile.getDisplayName());
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   private List<SemanticSuggestTerm> prepareConceptWithAppNameBasedSuggestTermList (List<BusinessEntityDefinition> beds)
            throws SWIException {
      List<SemanticSuggestTerm> suggestTerms = new ArrayList<SemanticSuggestTerm>();
      for (BusinessEntityDefinition bed : beds) {
         Concept concept = bed.getConcept();
         SemanticSuggestTerm suggestTerm = new SemanticSuggestTerm();
         StringBuilder name = new StringBuilder();
         name.append(concept.getDisplayName());
         String appName = semanticSuggestDataAccessManager.getApplicationNameByModelGroup(bed.getModelGroup());
         if (appName != null) {
            name.append(" (").append(appName).append(")");
         }
         suggestTerm.setName(name.toString());
         suggestTerm.setDisplayName(concept.getDisplayName());
         suggestTerms.add(suggestTerm);
      }
      return suggestTerms;
   }

   public IApplicationRetrievalService getApplicationRetrievalService () {
      return applicationRetrievalService;
   }

   public void setApplicationRetrievalService (IApplicationRetrievalService applicationRetrievalService) {
      this.applicationRetrievalService = applicationRetrievalService;
   }
}
