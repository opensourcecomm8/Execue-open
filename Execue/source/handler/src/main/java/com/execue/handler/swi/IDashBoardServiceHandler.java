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

import java.util.List;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.IHandler;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.handler.bean.grid.IGridBean;

public interface IDashBoardServiceHandler extends IHandler {

   public List<UIPublishedFileInfo> getPublishedFileInfo (Long applicationId) throws ExeCueException;

   public List<DataSource> getDataSources () throws ExeCueException;

   public List<Application> getApplications () throws ExeCueException;

   public List<Asset> getAssetsByApplicationId (Long applicationId) throws ExeCueException;

   public List<Model> getModelsByApplicationId (Long applicationId) throws ExeCueException;

   public List<Concept> getConceptsByPopularity (Long modelId) throws ExeCueException;

   public List<IGridBean> getApplicationsByPage (Page pageDetails) throws ExeCueException;

   public List<IGridBean> getApplicationsByPage (Page pageDetails, boolean advancedMenu) throws ExeCueException;

}
