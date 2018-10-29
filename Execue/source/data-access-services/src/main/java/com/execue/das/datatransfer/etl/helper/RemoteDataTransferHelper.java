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


package com.execue.das.datatransfer.etl.helper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.common.type.ConnectionType;
import com.execue.core.common.type.SuccessFailureType;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.das.dataaccess.ISystemDataAccessService;
import com.execue.das.datatransfer.etl.bean.DataTransferInput;
import com.execue.das.datatransfer.etl.bean.DataTransferQuery;
import com.execue.das.datatransfer.etl.bean.ETLInput;
import com.execue.das.datatransfer.etl.exception.RemoteDataTransferException;
import com.execue.das.exception.DataAccessServicesExceptionCodes;
import com.execue.dataaccess.configuration.IDataAccessConfigurationService;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.util.service.IDataAccessUtilService;
import com.execue.util.cryptography.exception.CryptographyException;

public class RemoteDataTransferHelper {

   private ISystemDataAccessService        systemDataAccessService;
   private IDataAccessConfigurationService dataAccessConfigurationService;
   private ICoreConfigurationService       coreConfigurationService;
   private IDataAccessUtilService          dataAccessUtilService;
   private Logger                          logger = Logger.getLogger(RemoteDataTransferHelper.class);

   //   private Document                        prequeriesDoc;
   //   private List<String>                    remoteConnection = new ArrayList<String>();
   //   private List<String>                    localConnection  = new ArrayList<String>();

   /**
    * Prepares ETLInput used by ETL process
    * 
    * @param dataTransferQuery
    * @param datTransferInput
    * @return
    */
   public ETLInput prepareETLInputFromRemoteQuery (DataTransferQuery dataTransferQuery,
            DataTransferInput dataTransferInput) throws RemoteDataTransferException {
      Map<String, Object> sourceConnectionPropsMap = prepareMap(dataTransferInput.getSourceDataSource());
      Map<String, Object> targetConnectionPropsMap = prepareMap(dataTransferInput.getTargetDataSource());
      ETLInput etlInput = new ETLInput();
      etlInput.setTargetConnectionPropsMap(targetConnectionPropsMap);
      etlInput.setSourceConnectionPropsMap(sourceConnectionPropsMap);
      etlInput.setTargetInsert(dataTransferQuery.getTargetInsertStatement());
      etlInput.setSourceQuery(dataTransferQuery.getSourceSelectQuery());
      return etlInput;
   }

   /**
    * Prepares a map that contain db connection parameters
    * 
    * @param remoteAsset
    * @return
    */
   private Map<String, Object> prepareMap (DataSource dataSource) throws RemoteDataTransferException {
      Map<String, Object> connectionPropsMap = null;

      if (ConnectionType.JNDI.equals(dataSource.getConnectionType())) {
         connectionPropsMap = prepareMapForExternalJNDIBasedDataSource(dataSource);
      } else if (!getDataAccessConfigurationService().isSetupForBatchProcessOnly()
               && getCoreConfigurationService().isForcePooledDataSourcesViaJNDI()) {
         connectionPropsMap = prepareMapForJNDIBasedDataSource(dataSource);
      } else {
         connectionPropsMap = prepareMapForPropertiesBasedDataSource(dataSource);
      }

      return connectionPropsMap;
   }

   private Map<String, Object> prepareMapForExternalJNDIBasedDataSource (DataSource dataSource) {
      // TODO : -RG- Not Yet implemented
      // Needs some more details
      // Refer : http://scriptella.javaforge.com/docs/api/scriptella/driver/jndi/package-summary.html#package_description

      Map<String, Object> connectionPropsMap = new HashMap<String, Object>();
      connectionPropsMap.put("type", "jndi");
      connectionPropsMap.put("driver", "jndi");
      connectionPropsMap.put("url", dataSource.getJndiProviderUrl());

      return connectionPropsMap;
   }

   private Map<String, Object> prepareMapForPropertiesBasedDataSource (DataSource dataSource)
            throws RemoteDataTransferException {
      Map<String, Object> connectionPropsMap = new HashMap<String, Object>();
      connectionPropsMap.put("type", "properties");
      String driver = getDataAccessConfigurationService().getDriver(dataSource);
      connectionPropsMap.put("driver", driver);
      String connectionURL = getDataAccessConfigurationService().getConnectionURL(dataSource);
      connectionPropsMap.put("url", connectionURL);
      connectionPropsMap.put("user", dataSource.getUserName());
      try {
         String decryptedPassword = getDataAccessUtilService().getDatasourcePassword(dataSource);
         connectionPropsMap.put("password", decryptedPassword);
      } catch (CryptographyException cryptographyException) {
         if (logger.isDebugEnabled()) {
            logger.debug("Error: Could not decrypt password");
         }
         logger.error(cryptographyException);
         throw new RemoteDataTransferException(ExeCueExceptionCodes.DECYPTION_PROCESS_FAILED, cryptographyException);
      }
      return connectionPropsMap;
   }

   /**
    * Refer : http://scriptella.javaforge.com/docs/api/scriptella/driver/jndi/package-summary.html#package_description
    * 
    * @return
    * @throws RemoteDataTransferException
    */
   private Map<String, Object> prepareMapForJNDIBasedDataSource (DataSource dataSource)
            throws RemoteDataTransferException {
      Map<String, Object> connectionPropsMap = new HashMap<String, Object>();

      connectionPropsMap.put("type", "custom-jndi");
      connectionPropsMap.put("driver", "jndi");
      connectionPropsMap.put("url", getDataAccessConfigurationService().prepareCustomJNDIDataSourceName(
               dataSource.getName()));
      connectionPropsMap.put("initial-context-environment-properties", getCoreConfigurationService()
               .getInitialContextEnvironment());
      return connectionPropsMap;
   }

   /**
    * @param localAsset -
    *           on which the rollback operation has to be performed
    * @param rollBackStatement -
    *           the SQL statement that has to be executed
    * @return
    */
   public SuccessFailureType rollBackTransfer (DataSource targetDataSource, String rollBackStatement)
            throws RemoteDataTransferException {

      SuccessFailureType status = SuccessFailureType.FAILURE; // failure
      try {
         getSystemDataAccessService().executeDDLStatement(targetDataSource.getName(), rollBackStatement);
      } catch (DataAccessException e) {
         throw new RemoteDataTransferException(
                  DataAccessServicesExceptionCodes.DEFAULT_REMOTE_DATA_TRANSFER_EXCEPTION_CODE, "DataAccessException",
                  e);
      }
      status = SuccessFailureType.SUCCESS;

      return status;
   }

   /**
    * To check whether the table exists or not. If not create a new one.
    * 
    * @param dataTransferInput
    * @throws SQLException
    * @throws SQLException
    * @throws RemoteDataTransferExceptionException
    */

   public void createTargetTable (DataTransferInput dataTransferInput) throws RemoteDataTransferException {
      List<DataTransferQuery> list = new ArrayList<DataTransferQuery>();
      list = dataTransferInput.getDataTransferQueries();
      for (int listCount = 0; listCount < list.size(); listCount++) {
         DataTransferQuery dataTransferQuery = list.get(listCount);
         createTargetTable(dataTransferInput.getTargetDataSource(), dataTransferQuery.getTargetTable(),
                  dataTransferQuery.getTargetCreateStatement());
      }
   }

   public void createTargetTable (DataSource targetDataSource, String targetTableName, String targetTableCreateStatement)
            throws RemoteDataTransferException {
      synchronized (this) {
         try {
            if (!getSystemDataAccessService().isTableExists(targetDataSource, targetTableName)) {
               getSystemDataAccessService().executeDDLStatement(targetDataSource.getName(), targetTableCreateStatement);
            }
         } catch (DataAccessException exception) {
            throw new RemoteDataTransferException(
                     DataAccessServicesExceptionCodes.DEFAULT_REMOTE_DATA_TRANSFER_EXCEPTION_CODE, exception);
         }
      }
   }

   /*
      *//**
                                                                          * Returns the connection Object to check for table existence
                                                                          * 
                                                                          * @param asset
                                                                          * @return
                                                                          * @throws RemoteDataTransferException
                                                                          */
   /*
   private Connection getConnections (DataSource dataSource) throws RemoteDataTransferException {
   Connection con;
   String driver = getDataAccessConfigurationService().getDriver(dataSource);
   String url = getDataAccessConfigurationService().getConnectionURL(dataSource);
   String userName = dataSource.getUserName();
   String password = dataSource.getPassword();

   try {
      try {

         Class.forName(driver).newInstance();
      } catch (InstantiationException e1) {

         e1.printStackTrace();
      } catch (IllegalAccessException e1) {

         e1.printStackTrace();
      }

      try {
         con = DriverManager.getConnection(url, userName, password);

      } catch (SQLException e) {
         e.printStackTrace();
         throw new RemoteDataTransferException(
                  DataAccessServicesExceptionCodes.DEFAULT_REMOTE_DATA_TRANSFER_EXCEPTION_CODE, "SQLException", e);
      }
   } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new RemoteDataTransferException(
               DataAccessServicesExceptionCodes.DEFAULT_REMOTE_DATA_TRANSFER_EXCEPTION_CODE,
               "ClassNotFoundException", e);

   } catch (NullPointerException e) {
      e.printStackTrace();
      throw new RemoteDataTransferException(
               DataAccessServicesExceptionCodes.DEFAULT_REMOTE_DATA_TRANSFER_EXCEPTION_CODE,
               "ClassNotFoundException", e);
   }
   return con;

   }

   private DataTransferInput prepareDataTransferInput (String fileName) {

   List<String> queryList = new ArrayList<String>();
   List<String> selectStatements = new ArrayList<String>();
   List<String> createStatements = new ArrayList<String>();
   List<String> inserStatements = new ArrayList<String>();
   List<String> rollbackStatements = new ArrayList<String>();

   DataTransferInput dataTransferInput = new DataTransferInput();

   ExecueConfiguration execueConfiguration;

   try {
      List<String> filenames = new ArrayList<String>();
      filenames.add(fileName);
      execueConfiguration = new ExecueConfiguration(filenames);
      String remoteDriver = execueConfiguration
               .getProperty("answers-catalog.remote-asset.connection-properties.properties.driver");
      String remoteURL = execueConfiguration
               .getProperty("answers-catalog.remote-asset.connection-properties.properties.url");

      Map<String, String> remoteMap = extractURL(remoteURL, remoteDriver);
      DataSource remoteDataSource = new DataSource();
      remoteDataSource.setLocation(remoteMap.get("location"));

      remoteDataSource.setPort(Integer.parseInt(remoteMap.get("port")));

      remoteDataSource.setSchemaName(remoteMap.get("database"));
      remoteDataSource.setUserName(execueConfiguration
               .getProperty("answers-catalog.remote-asset.connection-properties.properties.userid"));
      remoteDataSource.setPassword(execueConfiguration
               .getProperty("answers-catalog.remote-asset.connection-properties.properties.password"));

      // for Local Datasource

      String localDriver = execueConfiguration
               .getProperty("answers-catalog.local-asset.connection-properties.properties.driver");
      String localURL = execueConfiguration
               .getProperty("answers-catalog.local-asset.connection-properties.properties.url");

      Map<String, String> localMap = extractURL(localURL, localDriver);
      DataSource localDataSource = new DataSource();
      localDataSource.setLocation(localMap.get("location"));
      localDataSource.setPort(Integer.parseInt(localMap.get("port")));
      localDataSource.setSchemaName(localMap.get("database"));
      localDataSource.setUserName(execueConfiguration
               .getProperty("answers-catalog.local-asset.connection-properties.properties.userid"));
      localDataSource.setPassword(execueConfiguration
               .getProperty("answers-catalog.local-asset.connection-properties.properties.password"));

      
       * queryList = execueConfiguration.getXMLConfiguration().getList(
       * "answers-catalog.remote-data-queries.query.id"); XMLConfiguration xmlConfiguration = execueConfiguration
       * .getXMLConfiguration();
       
      XMLConfiguration xmlConfiguration = null; // this entire method is no longer used
      // storing this for future DOM parsing example purpose

      ConfigurationNode rootNode = xmlConfiguration.getRootNode();
      ConfigurationNode queriesNode = rootNode.getChild(0).getChild(2);
      for (int selectCount = 0; selectCount < queriesNode.getChildren().size(); selectCount++) {
         selectStatements.add(queriesNode.getChild(selectCount).getChild(1).getValue().toString());
      }

      for (int i = 0; i < queriesNode.getChildren().size(); i++) {
         createStatements.add(queriesNode.getChild(i).getChild(2).getChild(0).getValue().toString());
      }
      for (int i = 0; i < queriesNode.getChildren().size(); i++) {
         inserStatements.add(queriesNode.getChild(i).getChild(2).getChild(1).getValue().toString());
      }

      for (int i = 0; i < queriesNode.getChildren().size(); i++) {
         rollbackStatements.add(queriesNode.getChild(i).getChild(2).getChild(2).getValue().toString());
      }

      List<DataTransferQuery> remoteQueryList = new ArrayList<DataTransferQuery>();
      for (int i = 0; i < queryList.size(); i++) {
         DataTransferQuery remoteQuery = new DataTransferQuery();
         remoteQuery.setId(Long.parseLong(queryList.get(i)));
         remoteQuery.setSourceSelectQuery(selectStatements.get(i));
         remoteQuery.setTargetCreateStatement(createStatements.get(i));
         remoteQuery.setTargetInsertStatement(inserStatements.get(i));
         remoteQuery.setTargetRollbackStatement(rollbackStatements.get(i));
         remoteQueryList.add(remoteQuery);
      }

      dataTransferInput.setTargetDataSource(localDataSource);
      dataTransferInput.setSourceDataSource(remoteDataSource);
      dataTransferInput.setDataTransferQueries(remoteQueryList);

   } catch (ConfigurationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
   }
   return dataTransferInput;
   }

   private void getConnectionParameters (String parameter) {
   Element studentlist = prequeriesDoc.getDocumentElement();
   NodeList students = studentlist.getElementsByTagName("properties");

   Node e;

   for (int i = 0; i < students.getLength(); i++) {
      e = students.item(i);
      if (e.hasChildNodes()) {
         NodeList nl = e.getChildNodes();
         for (int j = 0; j < nl.getLength(); j++) {
            String nodeName = nl.item(j).getNodeName();
            String value = nl.item(j).getTextContent();
            if (nodeName.equals(parameter)) {
               if (e.getParentNode().getParentNode().getNodeName().equals("remote-asset")) {
                  remoteConnection.add(value);
               } else if (e.getParentNode().getParentNode().getNodeName().equals("local-asset")) {
                  localConnection.add(value);
               }

            }
         }
      }
   }

   }

   private void getQueryParameters (String parameter, List<String> listType) {
   Element studentlist = prequeriesDoc.getDocumentElement();
   NodeList students = studentlist.getElementsByTagName("query");

   Node e;

   for (int i = 0; i < students.getLength(); i++) {
      e = students.item(i);
      if (e.hasChildNodes()) {
         NodeList nl = e.getChildNodes();
         for (int j = 0; j < nl.getLength(); j++) {
            String nodeName = nl.item(j).getNodeName();
            String value = nl.item(j).getTextContent();
            if (nodeName.equals(parameter)) {
               listType.add(value);
            }
         }
      }
   }
   }

   private void getQueries () {
   List<String> queryList = new ArrayList<String>();
   NodeList list;
   list = prequeriesDoc.getElementsByTagName("query");

   Element queryElement = null;
   for (int i = 0; i < list.getLength(); i++) {
      queryElement = (Element) list.item(i);
      String str = getSimpleElementText(queryElement, "id");
      queryList.add(str);

   }

   }

   */// NOTE : -RG- Commneted as nto in use  
   /*   
      public void getQuery (String type, List<String> listType) {
         NodeList nl = prequeriesDoc.getElementsByTagName(type);

         for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            listType.add(n.getTextContent());

         }

      }

      public String getSimpleElementText (Element node, String name) {
         Element namenl = getFirstElement(node, name);
         Node textnode = namenl.getFirstChild();

         if (textnode instanceof Text) {
            return textnode.getNodeValue();
         } else {
            throw new RuntimeException("No text" + name);
         }
      }

      public Element getFirstElement (Element element, String name) {
         NodeList nl = element.getElementsByTagName(name);

         if (nl.getLength() < 1) {
            throw new RuntimeException("Element: " + element + " does not contain: " + name);
         }
         return (Element) nl.item(0);
      }

      public Map<String, String> extractURL (String url, String driver) {

         Map<String, String> urlMap = new HashMap<String, String>();
         StringTokenizer token = new StringTokenizer(url, ":");
         String location = null;
         String port = null;
         String database = null;
         int locationToken;
         int portToken;
         int databaseToken;
         int totalTokens = token.countTokens();
         if (driver.contains("oracle")) {

            databaseToken = token.countTokens() - 1;
            portToken = token.countTokens() - 2;
            locationToken = token.countTokens() - 3;
            for (int cnt = 0; cnt < totalTokens; cnt++) {
               if (cnt == locationToken) {
                  location = token.nextToken().substring(1);
                  continue;
               } else if (cnt == portToken) {
                  port = token.nextToken();
                  continue;
               } else if (cnt == databaseToken) {
                  database = token.nextToken();
                  continue;
               }
               token.nextToken();

            }
         } else if (driver.contains("mysql")) {
            String tokens[] = null;
            databaseToken = token.countTokens() - 1;
            portToken = token.countTokens() - 1;
            locationToken = token.countTokens() - 2;
            String urlTokens[] = null;

            for (int i = 0; i < totalTokens; i++) {
               if (i == locationToken) {
                  location = token.nextToken().substring(2);
                  continue;
               } else if (i == portToken || i == databaseToken) {
                  urlTokens = token.nextToken().split("/");
                  continue;
               }
               token.nextToken();

            }
            port = urlTokens[0];

            database = urlTokens[1];

         }
         urlMap.put("location", location);
         urlMap.put("port", port);
         urlMap.put("database", database);

         return urlMap;
      }
      */// NOTE : -RG- Commented as not in use
   public ISystemDataAccessService getSystemDataAccessService () {
      return systemDataAccessService;
   }

   public void setSystemDataAccessService (ISystemDataAccessService systemDataAccessService) {
      this.systemDataAccessService = systemDataAccessService;
   }

   /**
    * @return the dataAccessConfigurationService
    */
   public IDataAccessConfigurationService getDataAccessConfigurationService () {
      return dataAccessConfigurationService;
   }

   /**
    * @param dataAccessConfigurationService
    *           the dataAccessConfigurationService to set
    */
   public void setDataAccessConfigurationService (IDataAccessConfigurationService dataAccessConfigurationService) {
      this.dataAccessConfigurationService = dataAccessConfigurationService;
   }

   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }

   /**
    * @return the dataAccessUtilService
    */
   public IDataAccessUtilService getDataAccessUtilService () {
      return dataAccessUtilService;
   }

   /**
    * @param dataAccessUtilService the dataAccessUtilService to set
    */
   public void setDataAccessUtilService (IDataAccessUtilService dataAccessUtilService) {
      this.dataAccessUtilService = dataAccessUtilService;
   }
}