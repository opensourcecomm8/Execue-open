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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.AssetProviderType;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIStatus;
import com.execue.swi.exception.SWIExceptionCodes;

public class DataSourceAction extends SWIAction {

   private static final Logger log             = Logger.getLogger(DataSourceAction.class);

   private DataSource          dataSource;
   private List<DataSource>    dataSources;
   private String              dataSourceName;
   private String              portStr;

   private String              paginationType;
   public static final int     PAGESIZE        = 8;
   public static final int     numberOfLinks   = 4;
   private String              requestedPage;
   private UIStatus            uiStatus;
   private CheckType           encryptPassword = CheckType.NO;

   public DataSource getDataSource () {
      return dataSource;
   }

   public void setDataSource (DataSource dataSource) {
      this.dataSource = dataSource;
   }

   public List<DataSource> getDataSources () {
      return dataSources;
   }

   public void setDataSources (List<DataSource> dataSources) {
      this.dataSources = dataSources;
   }

   // Action Methods
   public String input () {

      if (log.isDebugEnabled()) {
         log.debug("Came to input flow");
      }
      if (dataSource != null && dataSource.getName() != null) {
         if (log.isDebugEnabled()) {
            log.debug("Data Source Name : " + dataSource.getName());
         }
         try {
            dataSource = getSdxServiceHandler().getDataSource(dataSource.getName());
            setPortStr();
            if (log.isDebugEnabled()) {
               log.debug("JNDI Name : " + dataSource.getJndiName());
            }
         } catch (ExeCueException e) {
            return ERROR;
         }
      } else if (dataSource == null) {
         dataSource = new DataSource();
      }
      return SUCCESS;
   }

   public String create () {
      try {
         setDataSourcePort();
         if (!validateDataSourceInput()) {
            addActionError(getText("execue.dataSource.invalid.input"));
            return ERROR;
         }
         if (dataSource.getId() != null) {
            return update();
         }
         getSdxServiceHandler().createDataSource(dataSource);
         setPortStr();
         //Dummy password to display in UI
         setDummyPassword();
         addActionMessage(getText("execue.dataSource.create.success"));
      } catch (ExeCueException exeCueException) {
         //Dummy password to display in UI
         setDummyPassword();
         if (exeCueException.getCode() == SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionError(exeCueException.getMessage());
            return SUCCESS;
         }
         if (exeCueException.getMessage().equalsIgnoreCase(getText("execue.datasource.invalid"))) {
            addActionError(getText("execue.dataSource.create.invalid.failure"));
         } else {
            addActionError(getText("execue.dataSource.create.failure"));
         }

         return ERROR;
      }
      return SUCCESS;
   }

   public String update () {
      if (dataSource != null) {
         if (log.isDebugEnabled()) {
            log.debug("Display Name : " + dataSource.getDisplayName());
            log.debug("Descriotion : " + dataSource.getDescription());
            log.debug("JNDI Name : " + dataSource.getJndiName());
            log.debug("Conenction Type = : " + dataSource.getConnectionType());
            log.debug("Data Source Locatin : " + dataSource.getLocation());
            log.debug("Port : " + dataSource.getPort());
            log.debug("User Name : " + dataSource.getUserName());
            log.debug("Password : " + dataSource.getPassword());
            log.debug("Provider Type : " + dataSource.getProviderType());
            log.debug("Schema Name : " + dataSource.getSchemaName());
         }
      }
      try {
         setDataSourcePort();
         if (CheckType.YES.equals(encryptPassword)) {
            setDataSource(getSdxServiceHandler().encryptDataSourceCredential(getDataSource()));
            addActionMessage(getText("execue.dataSource.encrypt.credential.success"));
         } else {
            getSdxServiceHandler().updateDataSource(getDataSource());
            addActionMessage(getText("execue.dataSource.update.success"));
         }
         setPortStr();
         //Dummy password to display in UI
         setDummyPassword();

      } catch (ExeCueException exeCueException) {
         //Dummy password to display in UI
         setDummyPassword();
         if (exeCueException.getCode() == 900018) {
            addActionMessage(exeCueException.getMessage());
            return SUCCESS;
         }
         if (exeCueException.getMessage().equalsIgnoreCase(getText("execue.datasource.invalid"))) {
            addActionError(getText("execue.dataSource.update.invalid.failure"));
         } else {
            addActionError(getText("execue.dataSource.update.failure"));
         }

         return ERROR;
      }
      return SUCCESS;
   }

   public String list () {
      try {
         dataSources = getSdxServiceHandler().getDisplayableDataSources();
         if (paginationType != null && paginationType.equalsIgnoreCase("dataSourcesForPagination")) {
            paginationForDataSources();
         }
      } catch (ExeCueException exeCueException) {
         return ERROR;
      }
      return SUCCESS;
   }

   private void paginationForDataSources () {
      if (requestedPage == null)
         requestedPage = "1";
      getHttpSession().put("DATASOURCESPAGINATION", dataSources);

      int tempSize = 0;
      if (dataSources.size() <= PAGESIZE)
         tempSize = dataSources.size();
      else
         tempSize = PAGESIZE;
      log.info("displaying initial sublist");
      dataSources = dataSources.subList(0, tempSize);
   }

   @SuppressWarnings ("unchecked")
   public String showSubDataSourcesDetails () {

      int reqPageNo = Integer.parseInt(getRequestedPage());

      int fromIndex = 1;
      int toIndex = 1;

      if (paginationType != null && paginationType.equalsIgnoreCase("dataSourcesForPagination")) {
         List<DataSource> dsList = (List<DataSource>) getHttpSession().get("DATASOURCESPAGINATION");
         int tempTotCount = (dsList.size() / PAGESIZE);
         int rmndr = dsList.size() % PAGESIZE;
         if (rmndr != 0) {
            tempTotCount++;
         }

         if (reqPageNo <= tempTotCount) {
            fromIndex = ((reqPageNo - 1) * PAGESIZE);
            toIndex = reqPageNo * PAGESIZE;
            if (toIndex > dsList.size())
               toIndex = (dsList.size());
         }

         log.info("Getting Columns SubList from -> " + fromIndex + " to " + toIndex);
         dataSources = dsList.subList(fromIndex, toIndex);
      }
      return SUCCESS;
   }

   public String delete () {
      //NOTE-JT- This old method , we will delete this method later
      try {
         dataSource = getSdxServiceHandler().getDataSource(dataSourceName);
         getSdxServiceHandler().deleteDataSource(dataSource);
         setPortStr();
      } catch (ExeCueException exeCueException) {
         addActionError(getText("execue.dataSource.delete.failure"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteDatasource () {
      uiStatus = new UIStatus();
      try {
         //dataSource = getSdxServiceHandler().getDataSource(dataSourceName);
         getSdxServiceHandler().deleteDataSource(dataSource);
         uiStatus.setStatus(true);
         uiStatus.setMessage(getText("execue.dataSource.delete.success"));
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException);
         uiStatus.setStatus(false);
         if (exeCueException.getCode() == 900018) {
            uiStatus.addErrorMessage(getText("execue.dataSource.isInUse.delete.failure"));
         } else {
            uiStatus.addErrorMessage(getText("execue.errors.general"));
         }

      }
      return SUCCESS;
   }

   public String getDataSourceByName () {
      try {
         dataSource = getSdxServiceHandler().getDataSource(dataSource.getName());
         setPortStr();
      } catch (ExeCueException exeCueException) {
         return ERROR;
      }
      return SUCCESS;
   }

   private void setDataSourcePort () {
      if (!StringUtils.isBlank(portStr)) {
         dataSource.setPort(Integer.parseInt(portStr));
      }
   }

   private void setPortStr () {
      portStr = (dataSource.getPort() != 0) ? String.valueOf(dataSource.getPort()) : "";
   }

   private boolean validateDataSourceInput () {
      boolean validInput = true;
      if (ExecueCoreUtil.isEmpty(dataSource.getDisplayName()) || ExecueCoreUtil.isEmpty(dataSource.getLocation())
               || ExecueCoreUtil.isEmpty(dataSource.getUserName()) || ExecueCoreUtil.isEmpty(dataSource.getPassword())
               || ExecueCoreUtil.isEmpty(dataSource.getSchemaName()) || dataSource.getPort() < 0) {
         validInput = false;

      }
      if ((AssetProviderType.Teradata.equals(dataSource.getProviderType())
               || AssetProviderType.SAS_SHARENET.equals(dataSource.getProviderType()) || AssetProviderType.SAS_WORKSPACE
               .equals(dataSource.getProviderType()))
               && ExecueCoreUtil.isEmpty(dataSource.getOwner())) {
         validInput = false;
      }
      return validInput;
   }

   private void setDummyPassword () {
      dataSource.setPassword("*****");
   }

   public String getDataSourceName () {
      return dataSourceName;
   }

   public void setDataSourceName (String dataSourceName) {
      this.dataSourceName = dataSourceName;
   }

   public String getPortStr () {
      return portStr;
   }

   public void setPortStr (String portStr) {
      this.portStr = portStr;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   public String getPaginationType () {
      return paginationType;
   }

   public void setPaginationType (String paginationType) {
      this.paginationType = paginationType;
   }

   /**
    * @return the uiStatus
    */
   public UIStatus getUiStatus () {
      return uiStatus;
   }

   /**
    * @param uiStatus the uiStatus to set
    */
   public void setUiStatus (UIStatus uiStatus) {
      this.uiStatus = uiStatus;
   }

   /**
    * @return the encryptPassword
    */
   public CheckType getEncryptPassword () {
      return encryptPassword;
   }

   /**
    * @param encryptPassword the encryptPassword to set
    */
   public void setEncryptPassword (CheckType encryptPassword) {
      this.encryptPassword = encryptPassword;
   }

}
