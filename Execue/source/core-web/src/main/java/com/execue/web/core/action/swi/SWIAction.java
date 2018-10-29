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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.security.core.GrantedAuthority;

import com.execue.core.common.bean.ApplicationContext;
import com.execue.core.common.bean.StatusEnum;
import com.execue.core.common.type.AssetOwnerType;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.AssetSubType;
import com.execue.core.common.type.AssetType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.ConnectionType;
import com.execue.core.common.type.ConversionType;
import com.execue.core.common.type.DataType;
import com.execue.core.common.type.GranularityType;
import com.execue.core.common.type.JoinType;
import com.execue.core.common.type.LookupType;
import com.execue.core.configuration.IConfiguration;
import com.execue.handler.swi.IAssetDetailServiceHandler;
import com.execue.handler.swi.ICubeManagementServiceHandler;
import com.execue.handler.swi.IJobStatusServiceHandler;
import com.execue.handler.swi.IKDXServiceHandler;
import com.execue.handler.swi.IPreferencesServiceHandler;
import com.execue.handler.swi.ISDXServiceHandler;
import com.execue.handler.swi.IUploadServiceHandler;
import com.execue.handler.swi.constraints.IConstraintServiceHandler;
import com.execue.handler.swi.joins.IJoinServiceHandler;
import com.execue.handler.swi.mappings.IMappingServiceHandler;
import com.execue.security.util.ExecueSecurityUitl;
import com.execue.web.core.bean.MenuSelection;
import com.execue.web.core.helper.MenuHelper;
import com.opensymphony.xwork2.ActionSupport;

public abstract class SWIAction extends ActionSupport implements RequestAware, SessionAware {

   // Status constants
   /*
    * public static final String SUCCESS = "success"; public static final String INPUT = "input"; public static final
    * String ERROR = "error";
    */

   // static
   protected static String               MAPPING               = "mapping";
   protected static String               ASSET                 = "asset";
   protected static String               ASSET_NEW             = "asset-new";
   protected static String               JOIN                  = "join";
   protected static String               CONSTRAINTS           = "constraints";
   protected static String               CUBE_REQUEST          = "cubeRequest";
   protected static String               SUBMIT                = "submit";
   protected static String               DEFAULT_METRICS       = "defaultMetrics";
   protected static String               DEFAULT_DYNAMIC_VALUE = "ddv";
   protected static String               MART_CREATION         = "mart";

   private ISDXServiceHandler            sdxServiceHandler;
   private IKDXServiceHandler            kdxServiceHandler;
   private IMappingServiceHandler        mappingServiceHandler;
   private IJoinServiceHandler           joinServiceHandler;
   private IPreferencesServiceHandler    preferencesServiceHandler;
   private ICubeManagementServiceHandler cubeManagementServiceHandler;
   private IJobStatusServiceHandler      jobStatusServiceHandler;
   private IConstraintServiceHandler     constraintServiceHandler;
   private IUploadServiceHandler         uploadServiceHandler;
   private IAssetDetailServiceHandler    assetDetailServiceHandler;
   private IConfiguration                configuration;
   private String                        errorMessage;
   private String                        message;
   private String                        status;
   private Map                           httpSession;
   private Map                           httpRequest;
   private String                        sourceURL;
   private String                        helpMessage;

   public IPreferencesServiceHandler getPreferencesServiceHandler () {
      return preferencesServiceHandler;
   }

   public void setPreferencesServiceHandler (IPreferencesServiceHandler preferencesServiceHandler) {
      this.preferencesServiceHandler = preferencesServiceHandler;
   }

   public ISDXServiceHandler getSdxServiceHandler () {
      return sdxServiceHandler;
   }

   public void setSdxServiceHandler (ISDXServiceHandler sdxServiceHandler) {
      this.sdxServiceHandler = sdxServiceHandler;
   }

   public IKDXServiceHandler getKdxServiceHandler () {
      return kdxServiceHandler;
   }

   public void setKdxServiceHandler (IKDXServiceHandler kdxServiceHandler) {
      this.kdxServiceHandler = kdxServiceHandler;
   }

   public void setMappingServiceHandler (IMappingServiceHandler mappingServiceHandler) {
      this.mappingServiceHandler = mappingServiceHandler;
   }

   public void setJoinServiceHandler (IJoinServiceHandler joinServiceHandler) {
      this.joinServiceHandler = joinServiceHandler;
   }

   public IMappingServiceHandler getMappingServiceHandler () {
      return mappingServiceHandler;
   }

   public IJoinServiceHandler getJoinServiceHandler () {
      return joinServiceHandler;
   }

   public List<ConnectionType> getConnectionTypes () {
      return Arrays.asList(ConnectionType.values());
   }

   public List<AssetProviderType> getAssetProviderTypes () {
      List<AssetProviderType> assetProviderTypes = new ArrayList<AssetProviderType>();
      assetProviderTypes.add(AssetProviderType.MySql);
      assetProviderTypes.add(AssetProviderType.Oracle);
      assetProviderTypes.add(AssetProviderType.MSSql);
      assetProviderTypes.add(AssetProviderType.DERBY);
      assetProviderTypes.add(AssetProviderType.POSTGRES);
      assetProviderTypes.add(AssetProviderType.DB2);
      assetProviderTypes.add(AssetProviderType.Teradata);
      assetProviderTypes.add(AssetProviderType.SAS_SHARENET);
      assetProviderTypes.add(AssetProviderType.SAS_WORKSPACE);
      return assetProviderTypes;
   }

   public List<AssetType> getAssetTypes () {
      return Arrays.asList(AssetType.values());
   }

   public List<AssetSubType> getAssetSubTypes () {
      return Arrays.asList(AssetSubType.values());
   }

   public List<AssetOwnerType> getAssetOwnerTypes () {
      return Arrays.asList(AssetOwnerType.values());
   }

   public List<JoinType> getJoinTypes () {
      return Arrays.asList(JoinType.values());
   }

   public List<LookupType> getLookupTypes () {
      return Arrays.asList(LookupType.values());
   }

   public List<LookupType> getLookupTypesForTableTab () {
      List<LookupType> lookupTypesForTableTab = new ArrayList<LookupType>();
      lookupTypesForTableTab.add(LookupType.None);
      lookupTypesForTableTab.add(LookupType.SIMPLE_LOOKUP);
      lookupTypesForTableTab.add(LookupType.RANGE_LOOKUP);
      return lookupTypesForTableTab;
   }

   public List<CheckType> getCheckTypes () {
      return Arrays.asList(CheckType.values());
   }

   public List<DataType> getDataTypes () {
      return Arrays.asList(DataType.values());
   }

   public List<ColumnType> getColumnTypes () {
      List<ColumnType> columnTypes = Arrays.asList(ColumnType.values());
      return columnTypes;
   }

   public List<ColumnType> getColumnTypesForFactTableColumnsTab () {
      List<ColumnType> columnTypesForFactTableColumnsTab = new ArrayList<ColumnType>();
      columnTypesForFactTableColumnsTab.add(ColumnType.NULL);
      columnTypesForFactTableColumnsTab.add(ColumnType.MEASURE);
      columnTypesForFactTableColumnsTab.add(ColumnType.DIMENSION);
      columnTypesForFactTableColumnsTab.add(ColumnType.ID);
      return columnTypesForFactTableColumnsTab;
   }

   public List<ColumnType> getColumnTypesForLookupTableColumnsTab () {
      List<ColumnType> columnTypesForLookupTableColumnsTab = new ArrayList<ColumnType>();
      columnTypesForLookupTableColumnsTab.add(ColumnType.NULL);
      columnTypesForLookupTableColumnsTab.add(ColumnType.SIMPLE_LOOKUP);
      columnTypesForLookupTableColumnsTab.add(ColumnType.RANGE_LOOKUP);
      return columnTypesForLookupTableColumnsTab;
   }

   public List<GranularityType> getGranularityTypes () {
      List<GranularityType> granularityTypes = Arrays.asList(GranularityType.values());
      return granularityTypes;
   }

   public List<StatusEnum> getStatuses () {
      List<StatusEnum> statuses = new ArrayList<StatusEnum>();
      statuses.add(StatusEnum.ACTIVE);
      statuses.add(StatusEnum.INACTIVE);
      return statuses;
   }

   public List<ConversionType> getConversionTypes () {
      List<ConversionType> displayConversionTypes = new ArrayList<ConversionType>();
      List<ConversionType> conversionTypes = Arrays.asList(ConversionType.values());
      for (ConversionType conversionType : conversionTypes) {
         if (!ConversionType.DEFAULT.equals(conversionType) && !ConversionType.NULL.equals(conversionType)) {
            displayConversionTypes.add(conversionType);
         }
      }
      displayConversionTypes.add(0, ConversionType.NULL);
      return displayConversionTypes;
   }

   public List<String> getUserRoles () {
      List<String> userRoles = new ArrayList<String>();
      try {
         List<GrantedAuthority> grantedAuthorities = ExecueSecurityUitl.getAuthoritiesFromContext();
         for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            userRoles.add(grantedAuthority.getAuthority());
         }
      } catch (Exception exception) {
         // TODO : -RG- ETE : Eating the exception, check this
         exception.printStackTrace();
      }
      return userRoles;
   }

   public String getHelpMessages () {
      helpMessage = getText(helpMessage);
      return SUCCESS;
   }

   public String getErrorMessage () {
      return errorMessage;
   }

   public void setErrorMessage (String errorMessage) {
      this.errorMessage = errorMessage;
   }

   public String getMessage () {
      return message;
   }

   public void setMessage (String message) {
      this.message = message;
   }

   public String getStatus () {
      return status;
   }

   public void setStatus (String status) {
      this.status = status;
   }

   public IJobStatusServiceHandler getJobStatusServiceHandler () {
      return jobStatusServiceHandler;
   }

   public void setJobStatusServiceHandler (IJobStatusServiceHandler jobStatusServiceHandler) {
      this.jobStatusServiceHandler = jobStatusServiceHandler;
   }

   public IConstraintServiceHandler getConstraintServiceHandler () {
      return constraintServiceHandler;
   }

   public void setConstraintServiceHandler (IConstraintServiceHandler constraintServiceHandler) {
      this.constraintServiceHandler = constraintServiceHandler;
   }

   public IUploadServiceHandler getUploadServiceHandler () {
      return uploadServiceHandler;
   }

   public void setUploadServiceHandler (IUploadServiceHandler uploadServiceHandler) {
      this.uploadServiceHandler = uploadServiceHandler;
   }

   public ApplicationContext getApplicationContext () {
      return (ApplicationContext) httpSession.get("APPLICATION");
   }

   public void setApplicationContext (ApplicationContext applicationContext) {
      this.httpSession.put("APPLICATION", applicationContext);
   }

   public MenuSelection getMenuSelection () {
      return MenuHelper.getMenuSelection(ServletActionContext.getRequest().getSession());
   }

   public void setSession (Map session) {
      this.httpSession = session;
   }

   public Map getHttpSession () {
      return this.httpSession;
   }

   public void setRequest (Map request) {
      this.httpRequest = request;
   }

   public Map getHttpRequest () {
      return httpRequest;
   }

   public IAssetDetailServiceHandler getAssetDetailServiceHandler () {
      return assetDetailServiceHandler;
   }

   public void setAssetDetailServiceHandler (IAssetDetailServiceHandler assetDetailServiceHandler) {
      this.assetDetailServiceHandler = assetDetailServiceHandler;
   }

   public IConfiguration getConfiguration () {
      return configuration;
   }

   public void setConfiguration (IConfiguration configuration) {
      this.configuration = configuration;
   }

   public String getSourceURL () {
      return sourceURL;
   }

   public void setSourceURL (String sourceURL) {
      this.sourceURL = sourceURL;
   }

   public String getHelpMessage () {
      return helpMessage;
   }

   public void setHelpMessage (String helpMessage) {
      this.helpMessage = helpMessage;
   }

   public ICubeManagementServiceHandler getCubeManagementServiceHandler () {
      return cubeManagementServiceHandler;
   }

   public void setCubeManagementServiceHandler (ICubeManagementServiceHandler cubeManagementServiceHandler) {
      this.cubeManagementServiceHandler = cubeManagementServiceHandler;
   }
}
