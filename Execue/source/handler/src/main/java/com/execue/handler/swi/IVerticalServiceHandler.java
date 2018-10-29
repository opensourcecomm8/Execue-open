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

import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.exception.HandlerException;
import com.execue.handler.bean.UIApplicationInfo;

public interface IVerticalServiceHandler {

   public void createVertical (Vertical vertical) throws HandlerException;

   public void updateVertical (Vertical vertical) throws HandlerException;

   public void deleteVertical (Long verticalId) throws HandlerException;

   public List<Vertical> getAllVerticals () throws HandlerException;

   public List<Vertical> getAllVisibleVerticals () throws HandlerException;

   public Vertical getVertical (Long verticalId) throws HandlerException;

   public List<UIApplicationInfo> getApplications (Long verticalId) throws HandlerException;

   public List<Long> getVerticalIds (Long applicationId) throws HandlerException;

   public void createVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws HandlerException;

   public void deleteVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws HandlerException;

   public List<Long> getVerticalExcludedApps (List<Long> applicationIds, Long verticalId) throws HandlerException;

   public void addToDefaultVerticalAppAssociation (List<Long> applicationIds) throws HandlerException;

   public void moveAppsAcrossVertical (List<Long> applicationIds, Long verticalId) throws HandlerException;

}
