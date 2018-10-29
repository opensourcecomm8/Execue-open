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

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.mapping.EntityStatus;
import com.execue.web.core.action.swi.KDXAction;

public class KDXMappingAction extends KDXAction {

   private EntityStatus entityStatus;

   public String saveConcept () {
      String retValue = persistConcept();
      entityStatus = new EntityStatus();
      if (ERROR.equals(retValue)) {
         entityStatus.setMsg("Unable to process");
         entityStatus.setMsgType("E");
      } else {
         try {
            BusinessEntityDefinition dedef = getKdxServiceHandler().getBusinessEntityDefinitionByIds(
                     getApplicationContext().getModelId(), getConcept().getId(), null);
            entityStatus.setBedId(dedef.getId());
         } catch (Exception e) {
            e.printStackTrace();
         }
         entityStatus.setDispName(getConcept().getDisplayName());
         entityStatus.setType("E");
         entityStatus.setId(getConcept().getId());
      }
      return SUCCESS;
   }

   public String saveInstance () {
      String retValue = createInstance();
      entityStatus = new EntityStatus();
      if (ERROR.equals(retValue)) {
         entityStatus.setMsg("Unable to process");
         entityStatus.setMsgType("E");
      } else {
         try {
            BusinessEntityDefinition dedef = getKdxServiceHandler().getBusinessEntityDefinitionByIds(
                     getApplicationContext().getModelId(), getConcept().getId(), getInstance().getId());
            entityStatus.setBedId(dedef.getId());
         } catch (Exception e) {
            e.printStackTrace();
         }
         entityStatus.setDispName(getInstance().getDisplayName());
         entityStatus.setType("E");
         entityStatus.setId(getInstance().getId());
      }
      return SUCCESS;
   }

   public String showMappingConcept () {
      try {
         if (getConcept() != null && getConcept().getId() != null) {
            setConcept(getKdxServiceHandler().getConcept(getConcept().getId()));
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String persistConcept () {
      Long modelId = getApplicationContext().getModelId();
      try {
         if (getConcept().getId() != null) {
            Concept swiConcept = getKdxServiceHandler().getConcept(getConcept().getId());
            swiConcept.setDisplayName(getConcept().getDisplayName());
            swiConcept.setDescription(getConcept().getDescription());
            getKdxServiceHandler().updateConcept(modelId, swiConcept);
            addActionMessage(getText("execue.global.update.success", new String[] { getConcept().getName() }));
         } else {
            getKdxServiceHandler().createConcept(modelId, getConcept());
            addActionMessage(getText("execue.global.insert.success", new String[] { getConcept().getName() }));
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public EntityStatus getEntityStatus () {
      return entityStatus;
   }

   public void setEntityStatus (EntityStatus entityStatus) {
      this.entityStatus = entityStatus;
   }
}
