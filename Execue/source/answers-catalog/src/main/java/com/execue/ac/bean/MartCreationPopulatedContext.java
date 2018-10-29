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


package com.execue.ac.bean;

import java.util.HashMap;
import java.util.Map;

import com.execue.core.common.bean.ac.MartCreationContext;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.security.User;

/**
 * This bean contains the populated context information for mart
 * 
 * @author Vishay
 * @version 1.0
 * @since 11/01/2011
 */
public class MartCreationPopulatedContext extends MartCreationContext {

   private Application                         application;
   private Model                               model;
   private User                                user;
   private SourceAssetMappingInfo              sourceAssetMappingInfo;
   private Map<String, SourceAssetMappingInfo> prominentDimensionCubeMap = new HashMap<String, SourceAssetMappingInfo>();

   public SourceAssetMappingInfo getSourceAssetMappingInfo () {
      return sourceAssetMappingInfo;
   }

   public void setSourceAssetMappingInfo (SourceAssetMappingInfo sourceAssetMappingInfo) {
      this.sourceAssetMappingInfo = sourceAssetMappingInfo;
   }

   public Map<String, SourceAssetMappingInfo> getProminentDimensionCubeMap () {
      return prominentDimensionCubeMap;
   }

   public void setProminentDimensionCubeMap (Map<String, SourceAssetMappingInfo> prominentDimensionCubeMap) {
      this.prominentDimensionCubeMap = prominentDimensionCubeMap;
   }

   public Application getApplication () {
      return application;
   }

   public void setApplication (Application application) {
      this.application = application;
   }

   public Model getModel () {
      return model;
   }

   public void setModel (Model model) {
      this.model = model;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }

}
