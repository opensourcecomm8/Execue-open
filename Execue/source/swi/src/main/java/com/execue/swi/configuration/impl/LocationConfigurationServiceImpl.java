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


package com.execue.swi.configuration.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.configuration.IConfiguration;
import com.execue.swi.configuration.ILocationConfigurationService;

/**
 * Class to load the location related configurations.
 * 
 * @author Prasanna.
 * @since 07/21/2011
 */
public class LocationConfigurationServiceImpl implements ILocationConfigurationService {

   private Map<Long, List<Long>> childTypesByLocation       = new HashMap<Long, List<Long>>();
   private Map<Long, Long>       locationByChildTypeBedId   = new HashMap<Long, Long>();
   private Map<Long, List<Long>> transformableLocationTypes = new HashMap<Long, List<Long>>();
   private Map<Long, List<Long>> childTypeIdsByParentBEDID  = new HashMap<Long, List<Long>>();

   private IConfiguration        locationConfiguration;

   public boolean isLocationType (Long sharedModelParentBedId) {
      return childTypesByLocation.containsKey(sharedModelParentBedId);
   }

   public boolean isChildOfLocationType (Long sharedModelChildBedId) {
      return locationByChildTypeBedId.containsKey(sharedModelChildBedId);
   }

   public List<Long> getChildBedIdsByParentBedId (Long locationBedId) {
      return childTypesByLocation.get(locationBedId);
   }

   public Long getParentBedIdForChildBedId (Long childBedId) {
      return locationByChildTypeBedId.get(childBedId);
   }

   @Override
   public List<Long> getTransformableLocationTypesByChildBedId (Long sharedModelChildBedId) {
      return transformableLocationTypes.get(sharedModelChildBedId);
   }

   public void loadTransformableLocationTypes (Map<Long, List<Long>> transformableLocationTypes) {
      this.transformableLocationTypes = transformableLocationTypes;
   }

   public void loadChildTypesByLocation (Map<Long, List<Long>> childTypesByLocation) {
      this.childTypesByLocation = childTypesByLocation;
   }

   public void loadLocationByChildTypeBedId (Map<Long, Long> locationByChildTypeBedId) {
      this.locationByChildTypeBedId = locationByChildTypeBedId;
   }

   public IConfiguration getLocationConfiguration () {
      return locationConfiguration;
   }

   public void setLocationConfiguration (IConfiguration locationConfiguration) {
      this.locationConfiguration = locationConfiguration;
   }

   public void loadChildTypeIdsByParentBEDID (Map<Long, List<Long>> childTypeIdsByParentBEDID) {
      this.childTypeIdsByParentBEDID = childTypeIdsByParentBEDID;
   }

   @Override
   public List<Long> getChildIdsByParentBedId (Long parentBedId) {
      return childTypeIdsByParentBEDID.get(parentBedId);
   }

}