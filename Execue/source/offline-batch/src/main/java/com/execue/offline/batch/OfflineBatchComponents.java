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


package com.execue.offline.batch;

import com.execue.offline.batch.configuration.IOfflineBatchConfigurationService;
import com.execue.offline.batch.shareddata.location.helper.ILocationDataInputSourceWrapper;
import com.execue.platform.shareddata.location.ILocationDataPopulationService;
import com.execue.platform.unstructured.content.transporter.IUnstructuredTargetBasedContentTransporter;
import com.execue.semantification.batch.service.IGenericArticleBadImageURLCleanupService;
import com.execue.semantification.batch.service.IGenericArticleRuntimeTablesCleanupService;
import com.execue.semantification.batch.service.IGenericArticleSemantificationService;
import com.execue.semantification.batch.service.IGenericOldArticlesCleanupService;
import com.execue.semantification.service.ISemantificationJobService;

public class OfflineBatchComponents extends OfflineBaseBatch {

   public IGenericArticleSemantificationService getGenericArticleSemantificationService () {
      return (IGenericArticleSemantificationService) getContext().getBean("genericArticleSemantificationService");
   }

   public ISemantificationJobService getSemantificationJobService () {
      return (ISemantificationJobService) getContext().getBean("semantificationJobService");
   }

   public ILocationDataPopulationService getLocationDataPopulationService () {
      return (ILocationDataPopulationService) getContext().getBean("locationDataPopulationService");
   }

   public IUnstructuredTargetBasedContentTransporter getUnstructuredTargetBasedContentTransporter () {
      return (IUnstructuredTargetBasedContentTransporter) getContext().getBean(
               "unstructuredTargetBasedContentTransporter");
   }

   public ILocationDataInputSourceWrapper getLocationDataInputSourceWrapper () {
      // return (ILocationDataInputSourceWrapper) getContext().getBean("locationDataInputSourceCensusWrapper");
      return (ILocationDataInputSourceWrapper) getContext().getBean("locationDataInputSourceZipCodeDBWrapper");
   }

   public IOfflineBatchConfigurationService getOfflineBatchConfigurationService () {
      return (IOfflineBatchConfigurationService) getContext().getBean("offlineBatchConfigurationService");
   }

   public IGenericArticleBadImageURLCleanupService getGenericArticleBadImageURLCleanupService () {
      return (IGenericArticleBadImageURLCleanupService) getContext().getBean("genericArticleBadImageURLCleanupService");
   }

   public IGenericArticleRuntimeTablesCleanupService getGenericArticleRuntimeTablesCleanupService () {
      return (IGenericArticleRuntimeTablesCleanupService) getContext().getBean(
               "genericArticleRuntimeTablesCleanupService");
   }

   public IGenericOldArticlesCleanupService getGenericOldArticlesCleanupService () {
      return (IGenericOldArticlesCleanupService) getContext().getBean("genericOldArticlesCleanupService");
   }
}
