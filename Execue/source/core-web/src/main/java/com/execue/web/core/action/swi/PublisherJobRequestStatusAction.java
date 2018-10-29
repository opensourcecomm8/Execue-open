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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.PublishedFileInfo;
import com.execue.core.exception.ExeCueException;
import com.execue.handler.bean.UIPublishedFileInfo;
import com.execue.qdata.dataaccess.IJobDataAccessManager;
import com.execue.qdata.exception.QueryDataException;
import com.execue.swi.dataaccess.IPublishedFileDataAccessManager;

public class PublisherJobRequestStatusAction extends SWIAction {

   private static final long               serialVersionUID = 132252941747834476L;

   private List<UIPublishedFileInfo>       uiPublishedFileInfoList;
   private static final Logger             log              = Logger.getLogger(PublisherJobRequestStatusAction.class);

   private IPublishedFileDataAccessManager publishedFileDataAccessManager;
   private IJobDataAccessManager           jobDataAccessManager;

   public IJobDataAccessManager getJobDataAccessManager () {
      return jobDataAccessManager;
   }

   public void setJobDataAccessManager (IJobDataAccessManager jobDataAccessManager) {
      this.jobDataAccessManager = jobDataAccessManager;
   }

   public IPublishedFileDataAccessManager getPublishedFileDataAccessManager () {
      return publishedFileDataAccessManager;
   }

   public void setPublishedFileDataAccessManager (IPublishedFileDataAccessManager publishedFileDataAccessManager) {
      this.publishedFileDataAccessManager = publishedFileDataAccessManager;
   }

   public String input () {
      try {
         Long applicationId = getApplicationContext().getAppId();
         List<PublishedFileInfo> publishedFileInfoList = getPublishedFileDataAccessManager()
                  .getPublishedFileInfoByApplicationId(applicationId);
         uiPublishedFileInfoList = transformPublishedFileInfoListToUIPublishedFileInfoList(publishedFileInfoList);

      } catch (ExeCueException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public List<UIPublishedFileInfo> getUiPublishedFileInfoList () {
      return uiPublishedFileInfoList;
   }

   public void setUiPublishedFileInfoList (List<UIPublishedFileInfo> uiPublishedFileInfoList) {
      this.uiPublishedFileInfoList = uiPublishedFileInfoList;
   }

   private List<UIPublishedFileInfo> transformPublishedFileInfoListToUIPublishedFileInfoList (
            List<PublishedFileInfo> publisherFileInfoList) {
      List<UIPublishedFileInfo> uiPublisherFileInfoList = new ArrayList<UIPublishedFileInfo>();
      try {
         for (PublishedFileInfo publishedFileInfo : publisherFileInfoList) {
            UIPublishedFileInfo uiPublisherFileInfo = new UIPublishedFileInfo();
            uiPublisherFileInfo.setApplicationId(publishedFileInfo.getApplicationId());
            uiPublisherFileInfo.setCurrentOperation(publishedFileInfo.getCurrentOperation());
            uiPublisherFileInfo.setCurrentOperationStatus(publishedFileInfo.getCurrentOperationStatus());
            uiPublisherFileInfo.setDataSourceId(publishedFileInfo.getDatasourceId());
            uiPublisherFileInfo.setFileName(publishedFileInfo.getOriginalFileName());
            Long jobRequestId = publishedFileInfo.getAbsorbtionJobRequestId();
            if (jobRequestId == null) {
               jobRequestId = publishedFileInfo.getEvaluationJobRequestId();
            }
            uiPublisherFileInfo.setJobRequestId(jobRequestId);
            uiPublisherFileInfo.setJobType(getJobDataAccessManager().getJobTypeByJobRequestId(jobRequestId));
            uiPublisherFileInfo.setOriginalFileName(publishedFileInfo.getOriginalFileName());
            uiPublisherFileInfoList.add(uiPublisherFileInfo);
         }
      } catch (QueryDataException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return uiPublisherFileInfoList;
   }

}
