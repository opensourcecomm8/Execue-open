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

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.swi.exception.SWIException;

public interface IVerticalService extends ISWIService {

   public Vertical getVerticalByName (String name) throws SWIException;

   public void createVertical (Vertical vertical) throws SWIException;

   public void updateVertical (Vertical vertical) throws SWIException;

   public void deleteVertical (Long verticalId) throws SWIException;

   public List<Vertical> getAllVerticals () throws SWIException;

   public List<Vertical> getAllVisibleVerticals () throws SWIException;

   public Vertical getVertical (Long verticalId) throws SWIException;

   public List<Application> getApplications (Long verticalId) throws SWIException;

   public List<Long> getVerticalIds (Long applicationId) throws SWIException;

   public List<Long> getVerticalExcludedApps (List<Long> applicationIds, Long verticalId) throws SWIException;

   public void createVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws SWIException;

   public void deleteVerticalAppAssociation (List<Long> applicationIds, Long verticalId) throws SWIException;

   public void addToDefaultVerticalAppAssociation (List<Long> applicationIds, Long defaultVerticalId)
            throws SWIException;

   public void populateVerticalPopularity () throws SWIException;

   public List<Long> getAllVerticalIds () throws SWIException;
}
