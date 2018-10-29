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


package com.execue.semantification.configuration.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.type.FeatureValueType;
import com.execue.core.common.type.NewsCategory;
import com.execue.core.configuration.IConfiguration;
import com.execue.semantification.configuration.ISemantificationConfiguration;

/**
 * @author abhijit
 * @since 3/16/11 : 1:29 PM
 */
public class SemantificationConfigurationImpl implements ISemantificationConfiguration {

   // ****************** Constants for the configuration properties starts*******************************

   private static final String                    SEMANTIFICATION_LOAD_ON_START_UP                   = "loadOnStartUp";
   private static final String                    SEMANTIFICATION_DEFAULT_USER_PREFERRED_ZIP_CODE    = "defaultUserPreferredZipCode";
   private static final String                    SEMANTIFICATION_RESULTS_PAGE_SIZE_FOR_LIST_VIEW    = "listPageSize";
   private static final String                    SEMANTIFICATION_RESULTS_PAGE_SIZE_FOR_GRID_VIEW    = "gridPageSize";
   private static final String                    SEMANTIFICATION_NUMBER_OF_LINKS                    = "numberOfLinks";
   private static final String                    SEMANTIFICATION_DEFAULT_VICINITY_DISTANCE_LIMIT    = "defaultVicinityDistanceLimit";
   private static final String                    SEMANTIFICATION_DEFAULT_VICINITY_DISTANCE_LIMITS   = "defaultVicinityDistanceLimits";
   private static final String                    SEMANTICATION_POPULATE_RFX                         = "populateRfx";
   private static final String                    SEMANTICATION_POPULATE_UDX_KEYWORD                 = "populateUdxKeyword";
   private static final String                    SEMANTIFICATION_ARTICLE_SENTENCE_TOKEN_LIMIT       = "sentenceTokenLimit";
   private static final String                    SEMANTIFICATION_ARTICLE_INVALID_CONTENTS           = "InvalidContents.InvalidContent";
   private static final String                    SEMANTICATION_INVALID_ARTICLE_TEXTS                = "InvalidArticleTexts.InvalidArticleText";
   private static final String                    SEMANTIFICATION_ARTICLE_BATCH_SIZE                 = "batchsize";
   private static final String                    SEMANTIFICATION_ARTICLE_CLEANUP_DAYS               = "articleCleanupDays";
   private static final String                    SEMANTIFICATION_MAX_LIMIT_FOR_SHORT_DISTANCE_QUERY = "maxLimitForShortDistanceQuery";
   private static final String                    MIN_IMAGE_SIZE                                     = "minImageSize";
   private static final String                    MAX_IMAGE_SIZE                                     = "maxImageSize";
   private static final String                    MAX_IMAGE_COUNT                                    = "maxImageCount";
   private static final String                    BAD_IMAGE_URL_CLEANUP_BATCH_SIZE                   = "image-batch.bad-image-urls.batch-size";
   private static final String                    BAD_IMAGE_URL_DOMAIN_NAMES                         = "image-batch.bad-image-urls.domain-name";
   private static final String                    SEMANTIFIED_CONTENT_KEYWORD_MATCH_CLEANUP_TIME     = "semantified-content-keyword-match-cleanup-time-ms";
   private static final String                    USER_QUERY_FEATURE_INFO_CLEANUP_TIME               = "user-query-feature-info-cleanup-time-ms";
   private static final String                    USER_QUERY_LOCATION_INFO_CLEANUP_TIME              = "user-query-location-info-cleanup-time-ms";

   // ****************************** Field declarations starts **********************************************

   private IConfiguration                         semantificationConfiguration;

   private Map<Long, Map<Long, FeatureValueType>> existingFeatureByFeatureTypeMapByAppId             = new HashMap<Long, Map<Long, FeatureValueType>>(
                                                                                                              1);
   private Map<NewsCategory, Long>                applicationCategoryMap                             = new HashMap<NewsCategory, Long>(
                                                                                                              1);
   private Map<NewsCategory, Long>                verticalCategoryMap                                = new HashMap<NewsCategory, Long>(
                                                                                                              1);
   private List<String>                           invalidContentList                                 = new ArrayList<String>(
                                                                                                              1);
   private List<String>                           invalidArticleTexts                                = new ArrayList<String>(
                                                                                                              1);

   // ************************** Method implementations for configuration properties********************

   @Override
   public Integer getSemantificationArticleCleanupDays () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_ARTICLE_CLEANUP_DAYS);
   }

   @Override
   public Integer getSemantificationArticleBatchsize () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_ARTICLE_BATCH_SIZE);
   }

   @Override
   public Integer getSemantificationDefaultUserPreferredZipCode () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_DEFAULT_USER_PREFERRED_ZIP_CODE);
   }

   @Override
   public Integer getSemantificationDefaultVicinityDistanceLimit () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_DEFAULT_VICINITY_DISTANCE_LIMIT);
   }

   @Override
   public List<String> getSemantificationDefaultVicinityDistanceLimits () {
      return getSemantificationConfiguration().getList(SEMANTIFICATION_DEFAULT_VICINITY_DISTANCE_LIMITS);
   }

   @Override
   public Integer getSemantificationGridPageSize () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_RESULTS_PAGE_SIZE_FOR_GRID_VIEW);
   }

   @Override
   public Integer getSemantificationListPageSize () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_RESULTS_PAGE_SIZE_FOR_LIST_VIEW);
   }

   @Override
   public Integer getSemantificationMaxLimitForShortDistanceQuery () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_MAX_LIMIT_FOR_SHORT_DISTANCE_QUERY);
   }

   @Override
   public Integer getSemantificationNumberOfLinks () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_NUMBER_OF_LINKS);
   }

   @Override
   public Integer getSemantificationArticleSentenceTokenLimit () {
      return getSemantificationConfiguration().getInt(SEMANTIFICATION_ARTICLE_SENTENCE_TOKEN_LIMIT);
   }

   @Override
   public Boolean isSemantificationLoadOnStartUp () {
      return getSemantificationConfiguration().getBoolean(SEMANTIFICATION_LOAD_ON_START_UP);
   }

   @Override
   public Boolean isSemantificationPopulateRfx () {
      return getSemantificationConfiguration().getBoolean(SEMANTICATION_POPULATE_RFX);
   }

   @Override
   public Boolean isSemantificationPopulateUdxKeyword () {
      return getSemantificationConfiguration().getBoolean(SEMANTICATION_POPULATE_UDX_KEYWORD);
   }

   public IConfiguration getSemantificationConfiguration () {
      return semantificationConfiguration;
   }

   public void setSemantificationConfiguration (IConfiguration semantificationConfiguration) {
      this.semantificationConfiguration = semantificationConfiguration;
   }

   @Override
   public void populateInvalidArticleTexts () {
      invalidArticleTexts.addAll(getSemantificationConfiguration().getList(SEMANTICATION_INVALID_ARTICLE_TEXTS));
   }

   @Override
   public void populateInvalidArticleContents () {
      invalidContentList.addAll(getSemantificationConfiguration().getList(SEMANTIFICATION_ARTICLE_INVALID_CONTENTS));
   }

   // ********************** Getters and Setters methods****************************************************

   /**
    * @return the existingFeatureByFeatureTypeMapByAppId
    */
   public Map<Long, Map<Long, FeatureValueType>> getExistingFeatureByFeatureTypeMapByAppId () {
      return existingFeatureByFeatureTypeMapByAppId;
   }

   /**
    * @param existingFeatureByFeatureTypeMapByAppId
    *           the existingFeatureByFeatureTypeMapByAppId to set
    */
   public void setExistingFeatureByFeatureTypeMapByAppId (
            Map<Long, Map<Long, FeatureValueType>> existingFeatureByFeatureTypeMapByAppId) {
      this.existingFeatureByFeatureTypeMapByAppId = existingFeatureByFeatureTypeMapByAppId;
   }

   /**
    * @return the applicationCategoryMap
    */
   public Map<NewsCategory, Long> getApplicationCategoryMap () {
      return applicationCategoryMap;
   }

   /**
    * @return the verticalCategoryMap
    */
   public Map<NewsCategory, Long> getVerticalCategoryMap () {
      return verticalCategoryMap;
   }

   /**
    * @return the invalidContentList
    */
   public List<String> getInvalidContentList () {
      return invalidContentList;
   }

   /**
    * @return the invalidArticleTexts
    */
   public List<String> getInvalidArticleTexts () {
      return invalidArticleTexts;
   }

   @Override
   public Long getMaxImageCount () {
      return getSemantificationConfiguration().getLong(MAX_IMAGE_COUNT);
   }

   @Override
   public Long getMinImageSize () {
      return getSemantificationConfiguration().getLong(MIN_IMAGE_SIZE);
   }

   @Override
   public Long getMaxImageSize () {
      return getSemantificationConfiguration().getLong(MAX_IMAGE_SIZE);
   }

   @Override
   public List<String> getBadImageURLDomainNames () {
      return getSemantificationConfiguration().getList(BAD_IMAGE_URL_DOMAIN_NAMES);
   }

   @Override
   public int getBadImageURLCleanupBatchSize () {
      return getSemantificationConfiguration().getInt(BAD_IMAGE_URL_CLEANUP_BATCH_SIZE);
   }

   public Long getSemantifiedContentKeywordMatchExecutionTime () {
      return getSemantificationConfiguration().getLong(SEMANTIFIED_CONTENT_KEYWORD_MATCH_CLEANUP_TIME);
   }

   public Long getUserQueryFeatureInfoExecutionTime () {
      return getSemantificationConfiguration().getLong(USER_QUERY_FEATURE_INFO_CLEANUP_TIME);
   }

   public Long getUserQueryLocationInfoExecutionTime () {
      return getSemantificationConfiguration().getLong(USER_QUERY_LOCATION_INFO_CLEANUP_TIME);
   }
}
