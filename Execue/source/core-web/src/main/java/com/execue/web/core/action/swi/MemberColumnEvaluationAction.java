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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.util.ExecueBeanUtil;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIMember;
import com.execue.handler.bean.UITable;
import com.execue.handler.bean.grid.IGridBean;
import com.execue.handler.swi.IPublishedFileColumnEvaluationServiceHandler;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class MemberColumnEvaluationAction extends PaginationGridAction {

   private static final Logger                          log              = Logger
                                                                                  .getLogger(MemberColumnEvaluationAction.class);

   private static final long                            serialVersionUID = 1L;
   private Long                                         assetId;
   private Long                                         tableId;
   private List<UITable>                                columnList;
   private String                                       evaluatedMemberList;
   private String                                       updateValidationErrorMessage;
   private IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler;

   public String getMemberEvaluationList () {
      try {
         setColumnList(getPublishedFileColumnEvaluationServiceHandler().getAssetLookupTables(getAssetId()));
      } catch (ExeCueException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   @Override
   protected List<? extends IGridBean> processPageGrid () {
      List<UIMember> memberEvalList = new ArrayList<UIMember>();
      try {
         memberEvalList = getPublishedFileColumnEvaluationServiceHandler().getAssetTableMembersByPage(getAssetId(),
                  getTableId(), getPageDetail());
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return memberEvalList;
   }

   @SuppressWarnings ("unchecked")
   public String updateEvaluatedMembers () {
      try {
         ArrayList<HashMap> jsonObjectArray = (ArrayList<HashMap>) JSONUtil.deserialize(getEvaluatedMemberList());
         List<UIMember> uiMemberList = new ArrayList<UIMember>();
         for (HashMap map : jsonObjectArray) {
            UIMember uiMember = new UIMember();
            uiMember.setId(Long.parseLong((String) map.get("id")));
            uiMember.setName((String) map.get("name"));
            uiMember.setDescription((String) map.get("description"));
            uiMember.setInstanceExist(ExecueBeanUtil.getCorrespondingBooleanValue((String) map.get("instanceExist")));
            String parentConceptId = (String) map.get("parentConceptId");
            if (ExecueCoreUtil.isNotEmpty(parentConceptId) && !"null".equalsIgnoreCase(parentConceptId)) {
               uiMember.setParentConceptId(Long.parseLong(parentConceptId));
            }
            String optionalMemberId = (String) map.get("optionalMemberId");
            if (ExecueCoreUtil.isNotEmpty(optionalMemberId) && !"null".equalsIgnoreCase(optionalMemberId)) {
               uiMember.setOptionalMemberId(Long.parseLong(optionalMemberId));
            }
            uiMemberList.add(uiMember);
         }
         getPublishedFileColumnEvaluationServiceHandler().updateAssetTableMembers(getApplicationContext().getModelId(),
                  uiMemberList);
         setUpdateValidationErrorMessage("" + true);
      } catch (JSONException e) {
         e.printStackTrace();
         setUpdateValidationErrorMessage("" + false);
         return ERROR;
      } catch (ExeCueException e) {
         setUpdateValidationErrorMessage("" + false);
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public Long getAssetId () {
      return assetId;
   }

   public void setAssetId (Long assetId) {
      this.assetId = assetId;
   }

   public Long getTableId () {
      return tableId;
   }

   public void setTableId (Long tableId) {
      this.tableId = tableId;
   }

   public List<UITable> getColumnList () {
      return columnList;
   }

   public void setColumnList (List<UITable> columnList) {
      this.columnList = columnList;
   }

   public String getEvaluatedMemberList () {
      return evaluatedMemberList;
   }

   public void setEvaluatedMemberList (String evaluatedMemberList) {
      this.evaluatedMemberList = evaluatedMemberList;
   }

   public String getUpdateValidationErrorMessage () {
      return updateValidationErrorMessage;
   }

   public void setUpdateValidationErrorMessage (String updateValidationErrorMessage) {
      this.updateValidationErrorMessage = updateValidationErrorMessage;
   }

   public IPublishedFileColumnEvaluationServiceHandler getPublishedFileColumnEvaluationServiceHandler () {
      return publishedFileColumnEvaluationServiceHandler;
   }

   public void setPublishedFileColumnEvaluationServiceHandler (
            IPublishedFileColumnEvaluationServiceHandler publishedFileColumnEvaluationServiceHandler) {
      this.publishedFileColumnEvaluationServiceHandler = publishedFileColumnEvaluationServiceHandler;
   }

}
