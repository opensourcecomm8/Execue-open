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


package com.execue.web.core.action.swi.mappings;

import java.util.List;

import com.execue.core.common.util.MappingEntityType;
import com.execue.core.configuration.ExecueConfiguration;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.mapping.AssetColumnMember;
import com.execue.handler.bean.mapping.InstanceMapping;
import com.execue.handler.bean.mapping.SaveMapping;
import com.execue.web.core.action.swi.SWIAction;

@SuppressWarnings ("unchecked")
public class InstanceMappingsAction extends SWIAction {

   private static final long   serialVersionUID = 1L;
   private Long                assetId;
   private Long                selColAedId;
   private Long                selConBedId;
   private AssetColumnMember   assetColumnMember;
   private InstanceMapping     instanceMapping;
   private List<SaveMapping>   saveMappings;
   private ExecueConfiguration swiConfiguration;
   private int                 pageNo           = 1;
   private Long                PAGE_SIZE        = 30L;
   private String              mappingFilter;
   private boolean             saveAll;

   public boolean isSaveAll () {
      return saveAll;
   }

   public void setSaveAll (boolean saveAll) {
      this.saveAll = saveAll;
   }

   public String showPage () {
      displayInstanceMappings();
      return SUCCESS;
   }

   public String showExistingInstanceMappings () {
      // try {
      // instanceMapping = getMappingServiceHandler().retrieveMemberMappingsByPage(selColAedId, pageNo,
      // MappingEntityType.EXISTING);
      // } catch (ExeCueException e) {
      // e.printStackTrace();
      // }
      try {
         if (mappingFilter != null) {
            if (!(mappingFilter.equals(MappingEntityType.SUGGESTED) || mappingFilter.equals(MappingEntityType.EXISTING))) {
               // the 'Show All' option is handled here
               mappingFilter = null;
            }
         }
         instanceMapping = getMappingServiceHandler().retrieveMemberMappingsByPage(selColAedId, pageNo, mappingFilter);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   /**
    * This method displays the instance mappings based on the type of mappings
    */
   public String displayInstanceMappings () {
      try {
         if (mappingFilter != null) {
            if (!(mappingFilter.equals(MappingEntityType.SUGGESTED) || mappingFilter.equals(MappingEntityType.EXISTING))) {
               // the 'Show All' option is handled here
               mappingFilter = null;
            }
         }
         instanceMapping = getMappingServiceHandler().retrieveMemberMappingsByPage(selColAedId, pageNo, mappingFilter);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String suggestInstanceMappings () throws ExeCueException {
      Long totalUnmappedInstances = getMappingServiceHandler().getUnmappedMemberCount(selColAedId);
      Long maxAllowedSuggestions = getMappingServiceHandler().getMaxAllowedSuggestions();
      if (totalUnmappedInstances > maxAllowedSuggestions) {
         instanceMapping = new InstanceMapping();
         if (getSdxServiceHandler().isColumnUnderBatchProcess(selColAedId)) {
            instanceMapping.setMsg(getText("execue.jon.instance.mapping.suggestion.under.process"));
         } else {
            Long jobRequestId = getMappingServiceHandler().suggestMemberMappingsByJob(
                     getApplicationContext().getModelId(), getApplicationContext().getAssetId(), selColAedId,
                     selConBedId);
            instanceMapping.setMsg(getText("execue.jon.instance.mapping.success"));
            instanceMapping.setJobRequestId(jobRequestId);
         }
         // instanceMapping.setMsgType("S");
      } else {
         instanceMapping = getMappingServiceHandler().suggestMemberMappings(getApplicationContext().getModelId(),
                  selColAedId, selConBedId);
      }
      return SUCCESS;
   }

   public String storeInstanceMappings () throws ExeCueException {
      String msg = getText("execue.mapping.confirm.success");
      String msgType = "S";
      boolean scheduleJob = false;

      if (saveAll) {
         Long totalSuggestionInstances = getMappingServiceHandler().getInstanceMappingSuggestionDetailsCount(
                  selColAedId);
         Long maxAllowedSuggestions = getMappingServiceHandler().getMaxAllowedSuggestions();
         if (totalSuggestionInstances > maxAllowedSuggestions) {
            scheduleJob = true;
            Long jobRequestId = getMappingServiceHandler().saveMemberMappingsByJob(
                     getApplicationContext().getModelId(), getApplicationContext().getAssetId(), selColAedId,
                     selConBedId);
            instanceMapping = new InstanceMapping();
            instanceMapping.setMsg(getText("execue.jon.instance.mapping.success"));
            instanceMapping.setJobRequestId(jobRequestId);
            instanceMapping.setMsgType("S");
         }
      }
      if (!scheduleJob) {
         // first validate the mappings to be saved and then proceed
         instanceMapping = getMappingServiceHandler().validateInstanceMappings(selConBedId, saveMappings);

         if ("S".equals(instanceMapping.getMsgType())) {
            // save the mappings
            instanceMapping = getMappingServiceHandler().saveMemberMappings(pageNo,
                     getApplicationContext().getModelId(), selColAedId, selConBedId, instanceMapping, saveAll);
         }
         if ("E".equals(instanceMapping.getMsgType())) {
            msg = "Error occurred while saving the mappings";
            msgType = "E";
         }
         instanceMapping.setMsg(msg);
         instanceMapping.setMsgType(msgType);
      }
      return SUCCESS;
   }

   public String retrieveColumnMembers () {
      try {
         // assetColumnMember = getMappingServiceHandler().getColumnMembers(getSelColAedId());
         assetColumnMember = getMappingServiceHandler().getColumnMembersByPage(getSelColAedId(), new Long(pageNo),
                  PAGE_SIZE);

      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String deleteMapping () {
      return SUCCESS;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public List<SaveMapping> getSaveMapping () {
      return saveMappings;
   }

   public void setSaveMapping (List<SaveMapping> saveMappings) {
      this.saveMappings = saveMappings;
   }

   public InstanceMapping getInstanceMapping () {
      return instanceMapping;
   }

   public void setInstanceMapping (InstanceMapping instanceMapping) {
      this.instanceMapping = instanceMapping;
   }

   public Long getSelColAedId () {
      return selColAedId;
   }

   public void setSelColAedId (Long selColAedId) {
      this.selColAedId = selColAedId;
   }

   public Long getSelConBedId () {
      return selConBedId;
   }

   public void setSelConBedId (Long selConBedId) {
      this.selConBedId = selConBedId;
   }

   public AssetColumnMember getAssetColumnMember () {
      return assetColumnMember;
   }

   public void setAssetColumnMember (AssetColumnMember assetColumnMember) {
      this.assetColumnMember = assetColumnMember;
   }

   public List<SaveMapping> getSaveMappings () {
      return saveMappings;
   }

   public void setSaveMappings (List<SaveMapping> saveMappings) {
      this.saveMappings = saveMappings;
   }

   public ExecueConfiguration getSwiConfiguration () {
      return swiConfiguration;
   }

   public void setSwiConfiguration (ExecueConfiguration swiConfiguration) {
      this.swiConfiguration = swiConfiguration;
   }

   public int getPageNo () {
      return pageNo;
   }

   public void setPageNo (int pageNo) {
      this.pageNo = pageNo;
   }

   public String getMappingFilter () {
      return mappingFilter;
   }

   public void setMappingFilter (String mappingFilter) {
      this.mappingFilter = mappingFilter;
   }

   // new set of methods
   // This method will be called for viewing the mappings
   public String retrieveMappings () {
      // UI will set the mappingFilter value
      if (mappingFilter != null) {
         if (!(mappingFilter.equals(MappingEntityType.SUGGESTED) || mappingFilter.equals(MappingEntityType.EXISTING))) {
            // this means 'Show All'
            mappingFilter = null;
         }
      }
      // call the handler's method
      return SUCCESS;
   }

   // This method will validate the user's changes
   // public String validateMappings () {
   // // call the corresponding handler method
   // String statusMessage = SUCCESS;
   // try {
   // statusMessage = getMappingServiceHandler().validateInstanceMappings(selConBedId, saveMappings);
   // } catch (ExeCueException e) {
   // e.printStackTrace();
   // }
   // return statusMessage;
   // }

   // This method will save the mappings
   // public String saveMappings () {
   // try {
   // boolean saveAllFlag = false;
   // if (saveAll.equals("true")) {
   // saveAllFlag = true;
   // }
   // instanceMapping = getMappingServiceHandler().saveMemberMappings(pageNo, getApplicationContext().getModelId(),
   // selColAedId, selConBedId, saveMappings, saveAllFlag);
   // } catch (ExeCueException e) {
   // e.printStackTrace();
   // }
   // return SUCCESS;
   // }
}