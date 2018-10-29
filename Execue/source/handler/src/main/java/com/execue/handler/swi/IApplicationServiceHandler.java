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


package com.execue.handler.swi;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.ApplicationDetail;
import com.execue.core.common.bean.entity.ApplicationExample;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.UnstructuredApplicationDetail;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.bean.entity.VerticalAppExample;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIApplicationExample;
import com.execue.handler.bean.UIApplicationInfo;
import com.execue.handler.bean.UIApplicationModelInfo;
import com.execue.swi.exception.KDXException;

public interface IApplicationServiceHandler {

   public List<UIApplicationModelInfo> getAllApplications () throws HandlerException;

   public List<UIApplicationModelInfo> getAllApplicationsOrderedByName () throws HandlerException;

   public void createApplication (Application application) throws HandlerException;

   public void updateApplication (Application application) throws HandlerException;

   public Application getApplicationById (Long applicationId) throws HandlerException;

   public List<Model> getModelsByApplicationId (Long applicationId) throws HandlerException;

   public List<UIApplicationModelInfo> getApplications () throws KDXException;

   public Long deleteApplication (Long applicationId) throws KDXException;

   public void absorbApplicationImage (Long applicationId, File imageFile, String sourceDataFileName, String imageType)
            throws ExeCueException;

   public ApplicationDetail getApplicationDetail (Long applicationId, Long appImageId) throws KDXException;

   public List<UIApplicationInfo> getUIApplicationInfo () throws KDXException;

   public List<UIApplicationInfo> getApplicationsByPage (Page pageDetail) throws KDXException;

   public Long getAllExistingApplicationsCount () throws HandlerException;

   public Application getApplicationWithExample (Long applicationId) throws HandlerException;

   public ApplicationExample createApplicationExample (ApplicationExample applicationExample) throws HandlerException;

   public void updateApplicationExample (ApplicationExample applicationExample) throws HandlerException;

   public void deleteApplicationExample (ApplicationExample applicationExample) throws HandlerException;

   public List<UIApplicationExample> getAllAppExampleForApplication (Long appId) throws HandlerException;

   public UIApplicationInfo getApplicationDetailByAppId (Long appId) throws HandlerException;

   public List<UIApplicationInfo> getVerticalApplicationsByRank (Long verticalId) throws HandlerException;

   public List<UIApplicationInfo> getApplicationsByRank () throws HandlerException;

   public boolean isApplicationExist (String applicationName) throws HandlerException;

   public Map<String, List<VerticalAppExample>> getVerticalAppExamples () throws HandlerException;

   public List<Vertical> getAppVerticals (Long applicationId) throws HandlerException;

   public Application getApplicationByName (String applicationName) throws HandlerException;

   public String isUnstructuredAppVisibleToUser ();

   public boolean isAdvancedPublisher ();

   public UnstructuredApplicationDetail getUnstructuredApplicationDetailByApplicationId (Long applicationId)
            throws HandlerException;

   public List<UIApplicationModelInfo> getAllActiveStructuredCommunityApplicationsIncludingUserApps () throws HandlerException;

   public List<UIApplicationInfo> getAllCommunityApplicationsIncludingUserApps() throws HandlerException;

}
