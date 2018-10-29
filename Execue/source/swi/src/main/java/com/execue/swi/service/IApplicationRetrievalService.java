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


package com.execue.swi.service;

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.ApplicationOperation;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.bean.entity.VerticalAppWeight;
import com.execue.core.common.bean.entity.VerticalEntityBasedRedirection;
import com.execue.core.common.bean.entity.wrapper.AppDashBoardInfo;
import com.execue.core.common.type.AppSourceType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.FacetNatureType;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.swi.exception.KDXException;
import com.execue.swi.exception.SWIException;

public interface IApplicationRetrievalService {

   public Application getApplicationById (Long applicationId) throws KDXException;

   public List<Application> getApplications (Long userId) throws KDXException;

   public List<Application> getAllApplications () throws KDXException;

   public List<Application> getAllActiveApplications () throws KDXException;

   public List<Application> getAllActiveStructuredApplications () throws KDXException;

   public List<Application> getApplicationsByType (AppSourceType appSourceType) throws KDXException;

   public Application getApplicationByName (String applicationName) throws KDXException;

   public ApplicationDetail getApplicationDetail (Long applicationId, Long appImageId) throws KDXException;

   public List<Application> getApplicationsByPage (Page pageDetail) throws KDXException;

   public List<Application> getApplicationsByImageId (Long imageId) throws KDXException;

   public Long getAllExistingApplicationsCount () throws KDXException;

   public Application getApplicationWithExample (Long applicationId) throws KDXException;

   public List<ApplicationExample> getAllAppExampleForApplication (Long appId) throws KDXException;

   public List<Application> getVerticalApplicationsByRank (Long varticalId, Long limit) throws SWIException;

   public List<Application> getApplicationsByRank (Long limit) throws KDXException;

   public List<Application> getAllApplicationsOrderedByName () throws KDXException;

   public boolean isModelEvaluationRequiredByAppSourceType (Long applicationId) throws KDXException;

   public boolean isUnstructuredWHManagementRequiredByAppSourceType (Long applicationId) throws KDXException;

   public FacetNatureType getFacetNatureByApplicationId (Long applicationId) throws KDXException;

   public CheckType getLocationFromContentByApplicationId (Long applicationId) throws KDXException;

   public UnstructuredApplicationDetail getUnstructuredApplicationDetailByApplicationId (Long applicationId)
            throws KDXException;

   public List<Vertical> getAppVerticals (Long applicationId) throws KDXException;

   public List<Application> getApplicationsByVerticalId (Long verticalId) throws KDXException;

   public List<String> getApplicationNamesForVertical (String verticalName) throws KDXException;

   public VerticalAppWeight getVerticalAppWeightByApplicationId (Long id) throws KDXException;

   public List<ApplicationOperation> getApplicationOperationsByApplicationId (Long applicationId) throws KDXException;

   public List<VerticalEntityBasedRedirection> getVerticalRedirectionEntitiesByApplicationId (Long id)
            throws KDXException;

   public List<VerticalAppExample> getVerticalAppExamplesByApplicationId (Long applicationId) throws KDXException;

   public List<Application> getApplicationsForVertical (String verticalName) throws KDXException;

   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail, boolean advancedMenu) throws KDXException;

   public List<AppDashBoardInfo> getAppDashBoardInfosByPage (Page pageDetail) throws KDXException;

   public ApplicationDetail getImageByAppliationImageId (Long applicationId, Long imageId) throws KDXException;

   public ApplicationDetail getImageByApplicationId (Long applicationId) throws KDXException;

   public List<Application> getApplicationsByModelId (Long modelId) throws KDXException;

   public boolean isApplicationExist (String applicationName) throws KDXException;

   public Application getApplicationByModelId (Long modelId) throws KDXException;

   public Application getApplicationByKey (String applicationKey) throws KDXException;

   public List<Application> getAllActiveStructuredApplicationsByPublishMode (PublishAssetMode publishMode)
            throws KDXException;

   public List<Application> getAllActiveStructuredEligibleApplicationsByUserId (Long userId,
            boolean skipOtherCommunityApps) throws KDXException;

   public List<Application> getAllEligibleApplicationsByUserId (Long userId, boolean skipOtherCommunityApps) throws KDXException;

   public List<Application> getApplicationsForByPublishModeOrderByRank (PublishAssetMode publishMode)
            throws KDXException;

}
