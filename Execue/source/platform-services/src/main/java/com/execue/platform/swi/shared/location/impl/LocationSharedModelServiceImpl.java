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


package com.execue.platform.swi.shared.location.impl;

import java.util.List;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.SharedTypeModelMappingType;
import com.execue.core.constants.ExecueConstants;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.swi.shared.impl.SharedModelServiceImpl;
import com.execue.sdata.exception.SharedModelException;
import com.execue.swi.exception.KDXException;

public class LocationSharedModelServiceImpl extends SharedModelServiceImpl {

   public ModelGroup getModelGroup () throws SharedModelException {
      try {
         return getKdxRetrievalService().getModelGroupByTypeBedId(SharedTypeModelMappingType.LOCATION.getValue());
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   @Override
   protected Model getModel () throws SharedModelException {
      try {
         return getKdxRetrievalService().getModelByName(SharedTypeModelMappingType.LOCATION.name());
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   public List<Long> findMatchingCityInstance (String instanceValue) throws SharedModelException {
      try {
         return findMatchingTypeInstance(instanceValue, ExecueConstants.CITY_TYPE_ID);
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   public List<Long> findMatchingStateInstance (String instanceValue) throws SharedModelException {
      try {
         return findMatchingTypeInstance(instanceValue, ExecueConstants.STATE_TYPE_ID);
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   public List<Long> findMatchingCountryInstance (String instanceValue) throws SharedModelException {
      try {
         return findMatchingTypeInstance(instanceValue, ExecueConstants.COUNTRY_TYPE_ID);
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   private List<Long> findMatchingTypeInstance (String instanceValue, Long typeId) throws KDXException,
            SharedModelException {
      BusinessEntityDefinition typeBed = getKdxRetrievalService().getTypeBedByTypeID(typeId);
      BusinessEntityType instanceEntityType = null;
      if (BusinessEntityType.TYPE.equals(typeBed.getEntityType())) {
         instanceEntityType = BusinessEntityType.TYPE_LOOKUP_INSTANCE;
      } else if (BusinessEntityType.REALIZED_TYPE.equals(typeBed.getEntityType())) {
         instanceEntityType = BusinessEntityType.REALIZED_TYPE_LOOKUP_INSTANCE;
      }
      return getKdxRetrievalService().findMatchingTypeInstanceIncludingVariations(getModelGroup().getId(), typeId,
               instanceValue, instanceEntityType);
   }

   private Long createRealTimeTypeInstance (Long typeId, String instanceDisplayName) throws KDXException,
            PlatformException, SharedModelException {
      Instance instance = new Instance();
      instance.setDisplayName(instanceDisplayName);
      ModelGroup baseGroup = getBaseKDXRetrievalService().getBaseGroup();
      return getRealTimeBusinessEntityWrapperService().createTypeInstance(getModel().getId(), baseGroup.getId(),
               typeId, instance);
   }

   public Long createRealTimeCityInstance (String instanceValue) throws SharedModelException {
      try {
         return createRealTimeTypeInstance(ExecueConstants.CITY_TYPE_ID, instanceValue);
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      } catch (PlatformException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   public Long createRealTimeStateInstance (String instanceValue) throws SharedModelException {
      try {
         return createRealTimeTypeInstance(ExecueConstants.STATE_TYPE_ID, instanceValue);
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      } catch (PlatformException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }

   public Long createRealTimeCountryInstance (String instanceValue) throws SharedModelException {
      try {
         return createRealTimeTypeInstance(ExecueConstants.COUNTRY_TYPE_ID, instanceValue);
      } catch (KDXException e) {
         throw new SharedModelException(e.getCode(), e);
      } catch (PlatformException e) {
         throw new SharedModelException(e.getCode(), e);
      }
   }
}
