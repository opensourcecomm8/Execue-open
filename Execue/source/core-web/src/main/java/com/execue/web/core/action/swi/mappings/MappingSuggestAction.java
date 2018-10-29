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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.mapping.MappableBusinessTerm;
import com.execue.handler.bean.mapping.MappableInstanceTerm;
import com.execue.handler.bean.mapping.TermInfo;
import com.execue.web.core.action.swi.SWIAction;

public class MappingSuggestAction extends SWIAction {

   private List<TermInfo>       terms;
   private MappableBusinessTerm mappableTerm;
   private MappableInstanceTerm mappableInstanceTerm;
   private String               search;
   private Long                 conBedId;
   private Long                 pageNo    = 1L;
   private Long                 PAGE_SIZE = 30L;

   public String suggestConcepts () {
      try {
         terms = getMappingServiceHandler().suggestConcepts(getApplicationContext().getModelId(), search);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      if (terms == null) {
         terms = new ArrayList<TermInfo>();
      }
      return SUCCESS;
   }

   public String showConcepts () {
      try {
         mappableTerm = getMappingServiceHandler().showConcepts(getApplicationContext().getModelId());
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String suggestInstances () {
      try {
         terms = getMappingServiceHandler().suggestInstances(getApplicationContext().getModelId(), conBedId, search);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      if (terms == null) {
         terms = new ArrayList<TermInfo>();
      }
      return SUCCESS;
   }

   public String showInstances () {
      try {
         // mappableInstanceTerm = getMappingServiceHandler()
         // .showInstances(getApplicationContext().getModelId(), conBedId);

         mappableInstanceTerm = getMappingServiceHandler().retrieveInstancesByPage(conBedId, pageNo, PAGE_SIZE);
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getSearch () {
      return search;
   }

   public void setSearch (String search) {
      this.search = search;
   }

   public List<TermInfo> getTerms () {
      return terms;
   }

   public void setTerms (List<TermInfo> terms) {
      this.terms = terms;
   }

   public MappableBusinessTerm getMappableTerm () {
      return mappableTerm;
   }

   public void setMappableTerm (MappableBusinessTerm mappableTerm) {
      this.mappableTerm = mappableTerm;
   }

   public MappableInstanceTerm getMappableInstanceTerm () {
      return mappableInstanceTerm;
   }

   public void setMappableInstanceTerm (MappableInstanceTerm mappableInstanceTerm) {
      this.mappableInstanceTerm = mappableInstanceTerm;
   }

   public Long getConBedId () {
      return this.conBedId;
   }

   public void setConBedId (Long conBedId) {
      this.conBedId = conBedId;
   }

   public Long getPageNo () {
      return pageNo;
   }

   public void setPageNo (Long pageNo) {
      this.pageNo = pageNo;
   }
}