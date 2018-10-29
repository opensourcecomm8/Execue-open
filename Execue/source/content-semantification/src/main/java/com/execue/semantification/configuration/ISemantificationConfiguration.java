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


package com.execue.semantification.configuration;

import java.util.List;
import java.util.Map;

import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.NewsCategory;

/**
 * @author abhijit
 * @since 3/16/11 : 1:30 PM
 */
public interface ISemantificationConfiguration {

   public void populateInvalidArticleContents ();

   public void populateInvalidArticleTexts ();

   // *******Configuration properties related methods*************

   public Boolean isSemantificationLoadOnStartUp ();

   public Integer getSemantificationDefaultUserPreferredZipCode ();

   public Integer getSemantificationDefaultVicinityDistanceLimit ();

   public List<String> getSemantificationDefaultVicinityDistanceLimits ();

   public Integer getSemantificationListPageSize ();

   public Integer getSemantificationGridPageSize ();

   public Integer getSemantificationNumberOfLinks ();

   public Boolean isSemantificationPopulateRfx ();

   public Boolean isSemantificationPopulateUdxKeyword ();

   public Integer getSemantificationMaxLimitForShortDistanceQuery ();

   public List<String> getInvalidContentList ();

   public Integer getSemantificationArticleBatchsize ();

   public Integer getSemantificationArticleCleanupDays ();

   public Integer getSemantificationArticleSentenceTokenLimit ();

   public Map<Long, Map<Long, FeatureValueType>> getExistingFeatureByFeatureTypeMapByAppId ();

   public Map<NewsCategory, Long> getApplicationCategoryMap ();

   public Map<NewsCategory, Long> getVerticalCategoryMap ();

   public Long getMinImageSize ();

   public Long getMaxImageSize ();

   public Long getMaxImageCount ();

   public List<String> getBadImageURLDomainNames ();

   public int getBadImageURLCleanupBatchSize ();

}
