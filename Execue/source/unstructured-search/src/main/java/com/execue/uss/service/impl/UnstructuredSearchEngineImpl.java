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


package com.execue.uss.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.InstancePathDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.kb.DataConnectionEntity;
import com.execue.core.common.bean.qdata.UnStructuredIndex;
import com.execue.core.common.type.ConnectionEndPointType;
import com.execue.qdata.exception.UDXException;
import com.execue.qdata.service.IUDXService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXRetrievalService;
import com.execue.swi.service.IPathDefinitionRetrievalService;
import com.execue.uss.bean.ResultDataConnectionEntity;
import com.execue.uss.bean.ResultDataEntity;
import com.execue.uss.bean.UnstructuredSearchResult;
import com.execue.uss.exception.USSException;
import com.execue.uss.service.IUnstructuredSearchEngine;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 4:06:08 PM
 */
public class UnstructuredSearchEngineImpl implements IUnstructuredSearchEngine {

   private IUDXService                     udxService;
   private IPathDefinitionRetrievalService pathDefinitionRetrievalService;
   private IKDXRetrievalService            kdxRetrievalService;

   public IUDXService getUdxService () {
      return udxService;
   }

   public void setUdxService (IUDXService udxService) {
      this.udxService = udxService;
   }

   // Logic
   public UnstructuredSearchResult search (List<InstancePathDefinition> instancePaths) throws USSException {
      UnstructuredSearchResult searchResult = new UnstructuredSearchResult();
      if (!instancePaths.isEmpty()) {
         Map<Long, ResultDataEntity> sourceEntities = new HashMap<Long, ResultDataEntity>();
         Map<Long, ResultDataEntity> destinationEntities = new HashMap<Long, ResultDataEntity>();

         List<Long> bedIDs = new ArrayList<Long>();
         List<Long> rfIds = new ArrayList<Long>();

         for (InstancePathDefinition requestedPath : instancePaths) {
            ResultDataEntity sourceEntity = new ResultDataEntity();
            sourceEntity.setId(requestedPath.getSourceDEDId());
            sourceEntity.setType(ConnectionEndPointType.SUBJECT);
            sourceEntities.put(sourceEntity.getId(), sourceEntity);

            ResultDataEntity destEntity = new ResultDataEntity();
            destEntity.setId(requestedPath.getDestinationDEDId());
            destEntity.setType(ConnectionEndPointType.OBJECT);
            destinationEntities.put(destEntity.getId(), destEntity);
         }

         boolean sourceStart = sourceEntities.size() < destinationEntities.size();

         bedIDs.addAll(sourceEntities.keySet());
         bedIDs.addAll(destinationEntities.keySet());

         try {
            Map<Long, String> displayNames = kdxRetrievalService.getDisplayNamesByBEDIds(bedIDs);
            // method call udxService.getRFIdsMapByDEDIds(bedIDs) does not exist 
            // because the bean and table structure has been changed.  
            Map<Long, List<Long>> rfIDMap = null;
            for (Map.Entry<Long, List<Long>> entry : rfIDMap.entrySet()) {
               rfIds.addAll(entry.getValue());
            }
            Map<Long, List<UnStructuredIndex>> udxIDMap = udxService.getUDXMapByRFIds(rfIds);
            for (InstancePathDefinition requestedPath : instancePaths) {
               DataConnectionEntity connectionEntity = new ResultDataConnectionEntity();
               connectionEntity.setEntityPathID(requestedPath.getPathDefinitionId());
               Relation rel = getPathDefinitionRetrievalService().getDirectPathDefRelationByPathDefId(
                        requestedPath.getPathDefinitionId());
               connectionEntity.setId(rel.getId());
               connectionEntity.setName(rel.getName());

               ResultDataEntity destEnt = destinationEntities.get(requestedPath.getDestinationDEDId());
               ResultDataEntity sourceEnt = sourceEntities.get(requestedPath.getSourceDEDId());

               destEnt.setName(displayNames.get(destEnt.getId()));
               sourceEnt.setName(displayNames.get(sourceEnt.getId()));

               List<Long> destRFIdList = rfIDMap.get(destEnt.getId());
               if (destRFIdList != null) {
                  for (long rfId : destRFIdList) {
                     List<UnStructuredIndex> udxList = udxIDMap.get(rfId);
                     if (udxList != null)
                        destEnt.addUnstructuredInfos(udxList);
                  }
               }
               List<Long> sourceRFIdList = rfIDMap.get(sourceEnt.getId());
               if (sourceRFIdList != null) {
                  for (long rfId : sourceRFIdList) {
                     List<UnStructuredIndex> udxList = udxIDMap.get(rfId);
                     if (udxList != null)
                        sourceEnt.addUnstructuredInfos(udxList);
                  }
               }

               if (!sourceStart) {
                  if (destEnt.hasConnection(connectionEntity.getEntityPathID())) {
                     connectionEntity = destEnt.getConnection(connectionEntity.getEntityPathID());
                  }
                  connectionEntity.addDataItemForInstancePathID(requestedPath.getId(), sourceEnt);
                  destEnt.addConnection(connectionEntity);
               } else {
                  if (sourceEnt.hasConnection(connectionEntity.getEntityPathID())) {
                     connectionEntity = sourceEnt.getConnection(connectionEntity.getEntityPathID());
                  }
                  connectionEntity.addDataItemForInstancePathID(requestedPath.getId(), destEnt);
                  sourceEnt.addConnection(connectionEntity);
               }
            }
            if (!sourceStart) {
               searchResult.setEntityMap(destinationEntities);
            } else {
               searchResult.setEntityMap(sourceEntities);
            }
         } catch (SWIException e) {
            throw new USSException(e.getCode(), e);
         } catch (UDXException e) {
            throw new USSException(e.getCode(), e);
         }
      }
      return searchResult;
   }

   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public IPathDefinitionRetrievalService getPathDefinitionRetrievalService () {
      return pathDefinitionRetrievalService;
   }

   public void setPathDefinitionRetrievalService (IPathDefinitionRetrievalService pathDefinitionRetrievalService) {
      this.pathDefinitionRetrievalService = pathDefinitionRetrievalService;
   }
}
