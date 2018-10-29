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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.core.configuration.IConfigurable;
import com.execue.core.constants.ExecueConstants;
import com.execue.core.exception.ConfigurationException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.swi.service.IKDXModelService;

public class LocationConfigurableService implements IConfigurable {

   private IKDXModelService                 kdxModelService;
   private IBaseKDXRetrievalService         baseKDXRetrievalService;
   private LocationConfigurationServiceImpl locationConfigurationService;

   @Override
   public void doConfigure () throws ConfigurationException {
      loadLocationInformation();
   }

   @Override
   public void reConfigure () throws ConfigurationException {
      doConfigure();
   }

   private void loadLocationInformation () {
      try {

         BusinessEntityDefinition locationTypeBed = getBaseKDXRetrievalService().getTypeBEDByName(
                  ExecueConstants.LOCATION_TYPE);
         BusinessEntityDefinition parentRelationBed = getBaseKDXRetrievalService().getRelationBEDByName(
                  ExecueConstants.PARENT_PROPERTY);
         BusinessEntityDefinition transformableRelationBed = getBaseKDXRetrievalService().getRelationBEDByName(
                  ExecueConstants.IS_TRANSFORMABLE_TO_PROPERTY);

         List<EntityTripleDefinition> entityTriples = getKdxModelService().getEntityTriplesForSourceAndRelation(
                  locationTypeBed.getId(), parentRelationBed.getId());
         List<Long> childBedIdList = new ArrayList<Long>();
         List<Long> childIdList = new ArrayList<Long>();
         Map<Long, Long> locationByChildTypeBedId = new HashMap<Long, Long>();
         for (EntityTripleDefinition entityTripleDefinition : entityTriples) {
            childBedIdList.add(entityTripleDefinition.getDestinationBusinessEntityDefinition().getId());
            childIdList.add(entityTripleDefinition.getDestinationBusinessEntityDefinition().getType().getId());
            locationByChildTypeBedId.put(entityTripleDefinition.getDestinationBusinessEntityDefinition().getId(),
                     locationTypeBed.getId());
         }
         //load location by child type bed
         getLocationConfigurationService().loadLocationByChildTypeBedId(locationByChildTypeBedId);
         //load child type by location
         Map<Long, List<Long>> childTypesByLocation = new HashMap<Long, List<Long>>();
         childTypesByLocation.put(locationTypeBed.getId(), childBedIdList);
         getLocationConfigurationService().loadChildTypesByLocation(childTypesByLocation);

         //load child type id by parent bedId
         Map<Long, List<Long>> childTypeIdsByParentBEDID = new HashMap<Long, List<Long>>();
         childTypeIdsByParentBEDID.put(locationTypeBed.getId(), childIdList);
         getLocationConfigurationService().loadChildTypeIdsByParentBEDID(childTypeIdsByParentBEDID);

         // load transformable location types 
         Map<Long, List<Long>> transformableLocationTypes = new HashMap<Long, List<Long>>();
         for (Long childBedId : childBedIdList) {
            List<EntityTripleDefinition> entityTripleList = getKdxModelService().getEntityTriplesForSourceAndRelation(
                     childBedId, transformableRelationBed.getId());
            List<Long> transformableList = new ArrayList<Long>();
            for (EntityTripleDefinition entityTripleDef : entityTripleList) {
               transformableList.add(entityTripleDef.getDestinationBusinessEntityDefinition().getId());
            }
            transformableLocationTypes.put(childBedId, transformableList);
         }
         getLocationConfigurationService().loadTransformableLocationTypes(transformableLocationTypes);
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   /**
    * @return the kdxModelService
    */
   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   /**
    * @param kdxModelService the kdxModelService to set
    */
   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   /**
    * @return the baseKDXRetrievalService
    */
   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   /**
    * @param baseKDXRetrievalService the baseKDXRetrievalService to set
    */
   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the locationConfigurationService
    */
   public LocationConfigurationServiceImpl getLocationConfigurationService () {
      return locationConfigurationService;
   }

   /**
    * @param locationConfigurationService the locationConfigurationService to set
    */
   public void setLocationConfigurationService (LocationConfigurationServiceImpl locationConfigurationService) {
      this.locationConfigurationService = locationConfigurationService;
   }

}
