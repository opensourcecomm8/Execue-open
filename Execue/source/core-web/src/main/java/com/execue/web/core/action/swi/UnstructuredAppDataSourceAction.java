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

import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.DataSource;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.swi.IUnstructuredAppDataSourceServiceHandler;

/**
 * @author jitendra
 */
public class UnstructuredAppDataSourceAction extends SWIPaginationAction {

   private List<Application>                        applications;
   private List<DataSource>                         unstructuredDataSources;
   private List<DataSource>                         unstructuredContentAggregators;
   private List<DataSource>                         unstructuredContentAggregatorsForApp;
   private DataSource                               unstructuredDataSourceForApp;
   private List<DataSource>                         unstructuredSOLRDataSources;
   private DataSource                               unstructuredSOLRDataForApp;

   private Long                                     applicationId;
   private List<Long>                               selectedUnstructuredContentAggregatorsForApp;
   private Long                                     selectedUnstructuredDataSourceForApp;
   private Long                                     selectedUnstructuredSOLRDataSourceForApp;

   private IUnstructuredAppDataSourceServiceHandler unstructuredAppDataSourceServiceHandler;

   private final Logger                             log = Logger.getLogger(UnstructuredAppDataSourceAction.class);

   // methods

   @Override
   public String processPage () throws ExeCueException {
      setApplications(getUnstructuredAppDataSourceServiceHandler().getUnstructuredApplications());
      return SUCCESS;
   }

   public String showUnstructureAppDatasources () {
      try {
         populateList();
      } catch (HandlerException e) {
         log.error(e);
      }

      return SUCCESS;
   }

   public String saveUnstructuredDatasourceAndContentAggregators () {
      try {
         getUnstructuredAppDataSourceServiceHandler().saveUnstructuredContentAggregators(applicationId,
                  selectedUnstructuredDataSourceForApp, selectedUnstructuredSOLRDataSourceForApp,
                  selectedUnstructuredContentAggregatorsForApp);
         addActionMessage(getText("unstructued.app.content.aggregatior.save.success"));
         populateList();
      } catch (HandlerException e) {
         log.error(e);
         addActionError(getText("unstructued.app.content.aggregatior.save.failed"));
      }
      return SUCCESS;
   }

   private void populateList () throws HandlerException {
      setUnstructuredDataSources(getUnstructuredAppDataSourceServiceHandler().getUnstructureDataSources());
      setUnstructuredSOLRDataSources(getUnstructuredAppDataSourceServiceHandler().getUnstructureSOLRDataSources());
      setUnstructuredDataSourceForApp(getUnstructuredAppDataSourceServiceHandler().UnstructuredDataSourceByAppId(
               applicationId));
      setUnstructuredSOLRDataForApp(getUnstructuredAppDataSourceServiceHandler()
               .getUnstructuredWHSolrDataSourceByAppId(applicationId));
      setUnstructuredContentAggregatorsForApp(getUnstructuredAppDataSourceServiceHandler()
               .getContentAggregatorDataSourcesByAppId(applicationId));

      List<DataSource> allContentAggregatorDataSources = getUnstructuredAppDataSourceServiceHandler()
               .getContentAggregatorDataSources();
      if (!ExecueCoreUtil.isCollectionEmpty(unstructuredContentAggregatorsForApp)) {
         filterExistingContentAggregators(allContentAggregatorDataSources);
      } else {
         setUnstructuredContentAggregators(allContentAggregatorDataSources);
      }
   }

   private void filterExistingContentAggregators (List<DataSource> allContentAggregatorDataSources) {
      unstructuredContentAggregators = new ArrayList<DataSource>();
      allContentAggregatorDataSources.removeAll(unstructuredContentAggregatorsForApp);
      unstructuredContentAggregators = allContentAggregatorDataSources;
      //      if (!ExecueCoreUtil.isCollectionEmpty(allContentAggregatorDataSources)) {
      //         for (DataSource dataSource : allContentAggregatorDataSources) {
      //            DataSource matchedDataSource = getMatchedDataSource(dataSource);
      //            if (matchedDataSource == null) {
      //               unstructuredContentAggregators.add(dataSource);
      //            }
      //
      //         }
      //      }
   }

   //   private DataSource getMatchedDataSource (DataSource dataSource) {
   //      DataSource matchedDataSource = null;
   //      for (DataSource appDataSource : unstructuredContentAggregatorsForApp) {
   //         if (dataSource.getId().equals(appDataSource.getId())) {
   //            matchedDataSource = appDataSource;
   //            break;
   //         }
   //      }
   //      return matchedDataSource;
   //   }

   /**
    * @return the applications
    */
   public List<Application> getApplications () {
      return applications;
   }

   /**
    * @param applications
    *           the applications to set
    */
   public void setApplications (List<Application> applications) {
      this.applications = applications;
   }

   /**
    * @return the unstructuredDataSources
    */
   public List<DataSource> getUnstructuredDataSources () {
      return unstructuredDataSources;
   }

   /**
    * @param unstructuredDataSources
    *           the unstructuredDataSources to set
    */
   public void setUnstructuredDataSources (List<DataSource> unstructuredDataSources) {
      this.unstructuredDataSources = unstructuredDataSources;
   }

   /**
    * @return the unstructuredContentAggregators
    */
   public List<DataSource> getUnstructuredContentAggregators () {
      return unstructuredContentAggregators;
   }

   /**
    * @param unstructuredContentAggregators
    *           the unstructuredContentAggregators to set
    */
   public void setUnstructuredContentAggregators (List<DataSource> unstructuredContentAggregators) {
      this.unstructuredContentAggregators = unstructuredContentAggregators;
   }

   /**
    * @return the unstructuredContentAggregatorsForApp
    */
   public List<DataSource> getUnstructuredContentAggregatorsForApp () {
      return unstructuredContentAggregatorsForApp;
   }

   /**
    * @param unstructuredContentAggregatorsForApp
    *           the unstructuredContentAggregatorsForApp to set
    */
   public void setUnstructuredContentAggregatorsForApp (List<DataSource> unstructuredContentAggregatorsForApp) {
      this.unstructuredContentAggregatorsForApp = unstructuredContentAggregatorsForApp;
   }

   /**
    * @return the unstructuredDataSourceForApp
    */
   public DataSource getUnstructuredDataSourceForApp () {
      return unstructuredDataSourceForApp;
   }

   /**
    * @param unstructuredDataSourceForApp
    *           the unstructuredDataSourceForApp to set
    */
   public void setUnstructuredDataSourceForApp (DataSource unstructuredDataSourceForApp) {
      this.unstructuredDataSourceForApp = unstructuredDataSourceForApp;
   }

   /**
    * @return the applicationId
    */
   public Long getApplicationId () {
      return applicationId;
   }

   /**
    * @param applicationId
    *           the applicationId to set
    */
   public void setApplicationId (Long applicationId) {
      this.applicationId = applicationId;
   }

   /**
    * @return the selectedUnstructuredContentAggregatorsForApp
    */
   public List<Long> getSelectedUnstructuredContentAggregatorsForApp () {
      return selectedUnstructuredContentAggregatorsForApp;
   }

   /**
    * @param selectedUnstructuredContentAggregatorsForApp
    *           the selectedUnstructuredContentAggregatorsForApp to set
    */
   public void setSelectedUnstructuredContentAggregatorsForApp (List<Long> selectedUnstructuredContentAggregatorsForApp) {
      this.selectedUnstructuredContentAggregatorsForApp = selectedUnstructuredContentAggregatorsForApp;
   }

   /**
    * @return the selectedUnstructuredDataSourceForApp
    */
   public Long getSelectedUnstructuredDataSourceForApp () {
      return selectedUnstructuredDataSourceForApp;
   }

   /**
    * @param selectedUnstructuredDataSourceForApp
    *           the selectedUnstructuredDataSourceForApp to set
    */
   public void setSelectedUnstructuredDataSourceForApp (Long selectedUnstructuredDataSourceForApp) {
      this.selectedUnstructuredDataSourceForApp = selectedUnstructuredDataSourceForApp;
   }

   /**
    * @return the unstructuredAppDataSourceServiceHandler
    */
   public IUnstructuredAppDataSourceServiceHandler getUnstructuredAppDataSourceServiceHandler () {
      return unstructuredAppDataSourceServiceHandler;
   }

   /**
    * @param unstructuredAppDataSourceServiceHandler
    *           the unstructuredAppDataSourceServiceHandler to set
    */
   public void setUnstructuredAppDataSourceServiceHandler (
            IUnstructuredAppDataSourceServiceHandler unstructuredAppDataSourceServiceHandler) {
      this.unstructuredAppDataSourceServiceHandler = unstructuredAppDataSourceServiceHandler;
   }

   /**
    * @return the unstructuredSOLRDataSources
    */
   public List<DataSource> getUnstructuredSOLRDataSources () {
      return unstructuredSOLRDataSources;
   }

   /**
    * @param unstructuredSOLRDataSources
    *           the unstructuredSOLRDataSources to set
    */
   public void setUnstructuredSOLRDataSources (List<DataSource> unstructuredSOLRDataSources) {
      this.unstructuredSOLRDataSources = unstructuredSOLRDataSources;
   }

   /**
    * @return the selectedUnstructuredSOLRDataSourceForApp
    */
   public Long getSelectedUnstructuredSOLRDataSourceForApp () {
      return selectedUnstructuredSOLRDataSourceForApp;
   }

   /**
    * @param selectedUnstructuredSOLRDataSourceForApp
    *           the selectedUnstructuredSOLRDataSourceForApp to set
    */
   public void setSelectedUnstructuredSOLRDataSourceForApp (Long selectedUnstructuredSOLRDataSourceForApp) {
      this.selectedUnstructuredSOLRDataSourceForApp = selectedUnstructuredSOLRDataSourceForApp;
   }

   /**
    * @return the unstructuredSOLRDataForApp
    */
   public DataSource getUnstructuredSOLRDataForApp () {
      return unstructuredSOLRDataForApp;
   }

   /**
    * @param unstructuredSOLRDataForApp
    *           the unstructuredSOLRDataForApp to set
    */
   public void setUnstructuredSOLRDataForApp (DataSource unstructuredSOLRDataForApp) {
      this.unstructuredSOLRDataForApp = unstructuredSOLRDataForApp;
   }

   // getter/setters

}
