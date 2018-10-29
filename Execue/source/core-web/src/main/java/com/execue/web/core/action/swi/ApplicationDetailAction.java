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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIApplicationInfo;
import com.execue.handler.bean.UIConcept;
import com.execue.handler.swi.IApplicationServiceHandler;

/**
 * Action class represents the application specific information
 * 
 * @author jitendra
 */

public class ApplicationDetailAction extends SWIAction {

   private static final long          serialVersionUID               = 1L;
   private static final Logger        log                            = Logger.getLogger(ApplicationDetailAction.class);
   private UIApplicationInfo          uiApplicationInfo;
   private List<Asset>                assets;
   private List<ConceptProfile>       conceptProfiles;
   private List<InstanceProfile>      instanceProfiles;
   private Long                       applicationId;
   private IApplicationServiceHandler applicationServiceHandler;
   private List<UIConcept>            uiConcepts;
   private String                     requestString;
   private String                     type;
   private List<Vertical>             verticals;
   private String                     isUnstructuredAppVisibleToUser = "true";

   public String showApplicationDetail () {
      try {
         setIsUnstructuredAppVisibleToUser(getApplicationServiceHandler().isUnstructuredAppVisibleToUser());
         setUiApplicationInfo(getApplicationServiceHandler().getApplicationDetailByAppId(getApplicationId()));
         // get the verticals.
         setVerticals(getApplicationServiceHandler().getAppVerticals(getApplicationId()));
         // set assets
         setAssets(getSdxServiceHandler().getAllAssets(getApplicationId()));
         // set concepts/Instance
         setUiConcepts(getKdxServiceHandler().getBusinessTermsByPopularity(getUiApplicationInfo().getModelId()));
         // set Concept Profiles
         setConceptProfiles(getPreferencesServiceHandler().getConceptProfiles(getUiApplicationInfo().getModelId()));
         // set Instance Profiles
         setInstanceProfiles(getPreferencesServiceHandler().getInstanceProfiles(getUiApplicationInfo().getModelId()));
         // set default
         if (ExecueCoreUtil.isCollectionEmpty(assets)) {
            setAssets(new ArrayList<Asset>());
         }
         if (ExecueCoreUtil.isCollectionEmpty(getInstanceProfiles())) {
            setInstanceProfiles(new ArrayList<InstanceProfile>());
         }
         if (ExecueCoreUtil.isCollectionEmpty(getConceptProfiles())) {
            setConceptProfiles(new ArrayList<ConceptProfile>());
         }

         // if (getUiApplicationInfo().getApplicationName().equalsIgnoreCase("craigslist")) {
         // return "craigslist";
         // }

      } catch (HandlerException e) {
         e.printStackTrace();
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public UIApplicationInfo getUiApplicationInfo () {
      return uiApplicationInfo;
   }

   public void setUiApplicationInfo (UIApplicationInfo uiApplicationInfo) {
      this.uiApplicationInfo = uiApplicationInfo;
   }

   public List<Asset> getAssets () {
      return assets;
   }

   public void setAssets (List<Asset> assets) {
      this.assets = assets;
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

   public Long getApplicationId () {
      return applicationId;
   }

   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   public List<UIConcept> getUiConcepts () {
      return uiConcepts;
   }

   public void setUiConcepts (List<UIConcept> uiConcepts) {
      this.uiConcepts = uiConcepts;
   }

   public String getRequestString () {
      return requestString;
   }

   public void setRequestString (String requestString) {
      this.requestString = requestString;
   }

   public String getType () {
      return type;
   }

   public void setType (String type) {
      this.type = type;
   }

   public List<Vertical> getVerticals () {
      return verticals;
   }

   public void setVerticals (List<Vertical> verticals) {
      this.verticals = verticals;
   }

   public String getIsUnstructuredAppVisibleToUser () {
      return isUnstructuredAppVisibleToUser;
   }

   public void setIsUnstructuredAppVisibleToUser (String isUnstructuredAppVisibleToUser) {
      this.isUnstructuredAppVisibleToUser = isUnstructuredAppVisibleToUser;
   }

}
