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


package com.execue.platform.audittrail.impl;

import java.util.Date;
import java.util.List;

import com.execue.audittrail.exception.AuditTrailException;
import com.execue.audittrail.service.IAuditTrailService;
import com.execue.core.common.bean.audittrail.UserAccessAuditInput;
import com.execue.core.common.bean.audittrail.UserSearchAuditInput;
import com.execue.core.common.bean.audittrail.UserSearchAuditOutput;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.audittrail.AnonymousUser;
import com.execue.core.common.bean.entity.audittrail.UserAccessAudit;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.type.AuditLogType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ExeCueSystemType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.platform.audittrail.IAuditTrailWrapperService;
import com.execue.platform.exception.PlatformException;
import com.execue.qdata.exception.QueryDataException;
import com.execue.qdata.service.IQueryDataService;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.ISDXRetrievalService;
import com.execue.swi.service.IUserManagementService;

/**
 * Platform Wrapper service for AuditTrail service
 * @author Jitendra
 *
 */
public class AuditTrailWrapperServiceImpl implements IAuditTrailWrapperService {

   private IAuditTrailService        auditTrailService;
   private IUserManagementService    userManagementService;
   private IQueryDataService         queryDataService;
   private ISDXRetrievalService      sdxRetrievalService;
   private ICoreConfigurationService coreConfigurationService;

   @Override
   public List<UserAccessAudit> populateUserAccessAudit (UserAccessAuditInput input) throws PlatformException {
      List<UserAccessAudit> userAccessAuditList = null;
      try {
         userAccessAuditList = getAuditTrailService().populateUserAccessAudit(input);
         if (ExecueCoreUtil.isCollectionNotEmpty(userAccessAuditList)) {
            for (UserAccessAudit userAccessAudit : userAccessAuditList) {
               if (CheckType.YES.equals(userAccessAudit.getAnonymousUser())) {
                  userAccessAudit.setAnonymUser(populateAnonymousUser(userAccessAudit.getUserId()));
               } else {
                  userAccessAudit.setUser(populateUser(userAccessAudit.getUserId()));
               }
            }
         }
      } catch (AuditTrailException e) {
         e.printStackTrace();
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         e.printStackTrace();
         throw new PlatformException(e.getCode(), e);
      }
      return userAccessAuditList;
   }

   @Override
   public List<UserSearchAuditOutput> populateUserSearchAudit (UserSearchAuditInput input) throws PlatformException {
      List<UserSearchAuditOutput> userSearchAuditList = null;
      try {
         userSearchAuditList = getQueryDataService().populateUserSearchAudit(input);
         if (ExecueCoreUtil.isCollectionNotEmpty(userSearchAuditList)) {
            for (UserSearchAuditOutput userSearchAuditOutput : userSearchAuditList) {
               if (CheckType.YES.equals(userSearchAuditOutput.getAnonymousUser())) {
                  userSearchAuditOutput.setAnonymUser(populateAnonymousUser(userSearchAuditOutput.getUserId()));
               } else {
                  userSearchAuditOutput.setUser(populateUser(userSearchAuditOutput.getUserId()));
               }
               Asset asset = getSdxRetrievalService().getAssetById(userSearchAuditOutput.getAssetId());
               if (asset != null) {
                  userSearchAuditOutput.setAssetName(asset.getDisplayName());
               }
            }
         }
      } catch (QueryDataException e) {
         e.printStackTrace();
         throw new PlatformException(e.getCode(), e);
      } catch (AuditTrailException e) {
         e.printStackTrace();
         throw new PlatformException(e.getCode(), e);
      } catch (SWIException e) {
         e.printStackTrace();
         throw new PlatformException(e.getCode(), e);
      }
      return userSearchAuditList;
   }

   @Override
   public void prepareAndPersistUserAccessAuditInfo (User user, AuditLogType auditLogType, String ipLocation)
            throws PlatformException {
      UserAccessAudit userAccessAudit = new UserAccessAudit();
      userAccessAudit.setUserId(user.getId());
      userAccessAudit.setIpLocation(ipLocation);
      userAccessAudit.setAuditLogType(auditLogType);
      userAccessAudit.setAccessedTime(new Date());
      userAccessAudit.setAccessedSystemType(ExeCueSystemType.getType(getCoreConfigurationService()
               .getRunningApplicationTypeValue()));
      if (user.getId().intValue() == -1) {
         userAccessAudit.setAnonymousUser(CheckType.YES);
      } else {
         userAccessAudit.setAnonymousUser(CheckType.NO);
      }
      userAccessAudit.setComments("semantifi user access audit log");
      try {
         getAuditTrailService().createUserAccessAudit(userAccessAudit);
      } catch (AuditTrailException e) {
         e.printStackTrace();
         throw new PlatformException(e.getCode(), e);
      }
   }

   private User populateUser (Long userId) throws SWIException {
      return getUserManagementService().getUserById(userId);
   }

   private AnonymousUser populateAnonymousUser (Long userId) throws AuditTrailException {
      return getAuditTrailService().getAnonymousUserById(userId);
   }

   /**
    * @return the auditTrailService
    */
   public IAuditTrailService getAuditTrailService () {
      return auditTrailService;
   }

   /**
    * @param auditTrailService the auditTrailService to set
    */
   public void setAuditTrailService (IAuditTrailService auditTrailService) {
      this.auditTrailService = auditTrailService;
   }

   /**
    * @return the userManagementService
    */
   public IUserManagementService getUserManagementService () {
      return userManagementService;
   }

   /**
    * @param userManagementService the userManagementService to set
    */
   public void setUserManagementService (IUserManagementService userManagementService) {
      this.userManagementService = userManagementService;
   }

   /**
    * @return the queryDataService
    */
   public IQueryDataService getQueryDataService () {
      return queryDataService;
   }

   /**
    * @param queryDataService the queryDataService to set
    */
   public void setQueryDataService (IQueryDataService queryDataService) {
      this.queryDataService = queryDataService;
   }

   /**
    * @return the sdxRetrievalService
    */
   public ISDXRetrievalService getSdxRetrievalService () {
      return sdxRetrievalService;
   }

   /**
    * @param sdxRetrievalService the sdxRetrievalService to set
    */
   public void setSdxRetrievalService (ISDXRetrievalService sdxRetrievalService) {
      this.sdxRetrievalService = sdxRetrievalService;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

}
