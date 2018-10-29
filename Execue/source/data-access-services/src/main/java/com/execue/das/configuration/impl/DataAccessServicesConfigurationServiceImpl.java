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


package com.execue.das.configuration.impl;

import com.execue.core.configuration.IConfiguration;
import com.execue.das.configuration.IDataAccessServicesConfigurationService;
import com.execue.das.solr.bean.SolrDistanceConfigurationInfo;
import com.execue.das.solr.bean.SolrFacetFieldEntity;
import com.execue.das.solr.bean.SolrFacetQueryConstantParameters;
import com.execue.das.solr.bean.SolrFieldConfigurationInfo;
import com.execue.das.solr.bean.SolrStaticFieldsConfigurationInfo;
import com.execue.das.solr.type.SolrFieldType;

/**
 * @author Vishay
 */
public class DataAccessServicesConfigurationServiceImpl implements IDataAccessServicesConfigurationService {

   private IConfiguration      dataAccessServicesConfiguration;
   private static final String REMOTE_IP_LOCATION_DATA_SOURCE_KEY                         = "dataaccess-services.static-values.remote-ip-location-data-source";
   private static final String CITY_CENTER_ZIPCODE_DATA_SOURCE_KEY                        = "dataaccess-services.static-values.city-center-zipcode-data-source";

   private static final String SOLR_STATIC_FIELD_DOCUMENT_ID_NAME_KEY                     = "dataaccess-services.static-values.solr.static-fields.field.document-id-name";
   private static final String SOLR_STATIC_FIELD_DOCUMENT_ID_TYPE_KEY                     = "dataaccess-services.static-values.solr.static-fields.field.document-id-type";
   private static final String SOLR_STATIC_FIELD_CONTEXT_ID_NAME_KEY                      = "dataaccess-services.static-values.solr.static-fields.field.context-id-name";
   private static final String SOLR_STATIC_FIELD_CONTEXT_ID_TYPE_KEY                      = "dataaccess-services.static-values.solr.static-fields.field.context-id-type";
   private static final String SOLR_STATIC_FIELD_LOCATION_ID_NAME_KEY                     = "dataaccess-services.static-values.solr.static-fields.field.location-id-name";
   private static final String SOLR_STATIC_FIELD_LOCATION_ID_TYPE_KEY                     = "dataaccess-services.static-values.solr.static-fields.field.location-id-type";
   private static final String SOLR_STATIC_FIELD_LATITUDE_NAME_KEY                        = "dataaccess-services.static-values.solr.static-fields.field.latitude-name";
   private static final String SOLR_STATIC_FIELD_LATITUDE_TYPE_KEY                        = "dataaccess-services.static-values.solr.static-fields.field.latitude-type";
   private static final String SOLR_STATIC_FIELD_LONGITUDE_NAME_KEY                       = "dataaccess-services.static-values.solr.static-fields.field.longitude-name";
   private static final String SOLR_STATIC_FIELD_LONGITUDE_TYPE_KEY                       = "dataaccess-services.static-values.solr.static-fields.field.longitude-type";
   private static final String SOLR_STATIC_FIELD_IMAGE_PRESENT_NAME_KEY                   = "dataaccess-services.static-values.solr.static-fields.field.image-present-name";
   private static final String SOLR_STATIC_FIELD_IMAGE_PRESENT_TYPE_KEY                   = "dataaccess-services.static-values.solr.static-fields.field.image-present-type";
   private static final String SOLR_STATIC_FIELD_PROCESSING_STATE_NAME_KEY                = "dataaccess-services.static-values.solr.static-fields.field.processing-state-name";
   private static final String SOLR_STATIC_FIELD_PROCESSING_STATE_TYPE_KEY                = "dataaccess-services.static-values.solr.static-fields.field.processing-state-type";
   private static final String SOLR_STATIC_FIELD_CONTENT_DATE_NAME_KEY                    = "dataaccess-services.static-values.solr.static-fields.field.content-date-name";
   private static final String SOLR_STATIC_FIELD_CONTENT_DATE_TYPE_KEY                    = "dataaccess-services.static-values.solr.static-fields.field.content-date-type";

   private static final String SOLR_FACET_QUERY_PARAMETER_TOTAL_ROWS_KEY                  = "dataaccess-services.static-values.solr.facet.query-parameters.total-rows";
   private static final String SOLR_FACET_QUERY_PARAMETER_FACET_KEY                       = "dataaccess-services.static-values.solr.facet.query-parameters.facet";
   private static final String SOLR_FACET_QUERY_PARAMETER_FACET_METHOD_PARAM_NAME_KEY     = "dataaccess-services.static-values.solr.facet.query-parameters.facet-method-param-name";
   private static final String SOLR_FACET_QUERY_PARAMETER_FACET_METHOD_PARAM_VALUE_KEY    = "dataaccess-services.static-values.solr.facet.query-parameters.facet-method-param-value";
   private static final String SOLR_FACET_QUERY_PARAMETER_FACET_SORT_BY_COUNT_KEY         = "dataaccess-services.static-values.solr.facet.query-parameters.facet-sort-by-count";
   private static final String SOLR_FACET_QUERY_PARAMETER_FETCH_FACETS_WITH_MIN_COUNT_KEY = "dataaccess-services.static-values.solr.facet.query-parameters.fetch-facets-with-min-count";
   private static final String SOLR_FACET_QUERY_PARAMETER_FACET_LIMIT_KEY                 = "dataaccess-services.static-values.solr.facet.query-parameters.facet-limit";

   private static final String SOLR_SINGLE_LOCATION_DISTANCE_FORMULA_KEY                  = "dataaccess-services.static-values.solr.distance.single-location-distance-formula";
   private static final String SOLR_MULTIPLE_LOCATION_DISTANCE_FORMULA_KEY                = "dataaccess-services.static-values.solr.distance.multiple-location-distance-formula";
   private static final String SOLR_DISTANCE_REPLACE_TOKEN_KEY                            = "dataaccess-services.static-values.solr.distance.distance-replace-token";
   private static final String SOLR_LONGITUDE_REPLACE_TOKEN_KEY                           = "dataaccess-services.static-values.solr.distance.longitude-replace-token";
   private static final String SOLR_LATITUDE_REPLACE_TOKEN_KEY                            = "dataaccess-services.static-values.solr.distance.latitude-replace-token";
   private static final String SOLR_MULTIPLE_LONGITUDE_LATITUDE_REPLACE_TOKEN_KEY         = "dataaccess-services.static-values.solr.distance.multiple-longitude-latitude-replace-token";
   private static final String SOLR_LONGITUDE_FIELD_REPLACE_TOKEN_KEY                     = "dataaccess-services.static-values.solr.distance.longitude-field-replace-token";
   private static final String SOLR_LATITUDE_FIELD_REPLACE_TOKEN_KEY                      = "dataaccess-services.static-values.solr.distance.latitude-field-replace-token";

   private static final String SOLR_LONG_COLUMN_FIELD_SUFFIX_KEY                          = "dataaccess-services.static-values.solr.field-info.long-column-solr-field-suffix";
   private static final String SOLR_NUMBER_COLUMN_FIELD_SUFFIX_KEY                        = "dataaccess-services.static-values.solr.field-info.number-column-solr-field-suffix";
   private static final String SOLR_STRING_COLUMN_FIELD_SUFFIX_KEY                        = "dataaccess-services.static-values.solr.field-info.string-column-solr-field-suffix";
   private static final String SOLR_DATE_COLUMN_FIELD_SUFFIX_KEY                          = "dataaccess-services.static-values.solr.field-info.date-column-solr-field-suffix";
   private static final String SOLR_MULTI_VALUED_SEPERATOR_COLUMN_SUFFIX_KEY              = "dataaccess-services.static-values.solr.field-info.multivalued-seperator-column-solr-field-suffix";
   private static final String SOLR_MULTI_VALUED_FEATURE_SEPERATOR_KEY                    = "dataaccess-services.static-values.solr.field-info.multi-valued-feature-seperator";
   private static final String SOLR_DOCUMENT_ID_FIELD_SEPERATOR_KEY                       = "dataaccess-services.static-values.solr.field-info.document-id-field-seperator";

   private static final String SOLR_DEFAULT_QUERY_KEY                                     = "dataaccess-services.static-values.solr.default-query";

   public IConfiguration getDataAccessServicesConfiguration () {
      return dataAccessServicesConfiguration;
   }

   public void setDataAccessServicesConfiguration (IConfiguration dataAccessServicesConfiguration) {
      this.dataAccessServicesConfiguration = dataAccessServicesConfiguration;
   }

   @Override
   public String getCityCenterZipCodeDataSource () {
      return getDataAccessServicesConfiguration().getProperty(CITY_CENTER_ZIPCODE_DATA_SOURCE_KEY);
   }

   @Override
   public String getRemoteIPLocationDataSource () {
      return getDataAccessServicesConfiguration().getProperty(REMOTE_IP_LOCATION_DATA_SOURCE_KEY);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldContextId () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_CONTEXT_ID_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_CONTEXT_ID_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldDocumentId () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_DOCUMENT_ID_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_DOCUMENT_ID_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldImagePresent () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_IMAGE_PRESENT_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_IMAGE_PRESENT_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldLatitude () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_LATITUDE_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_LATITUDE_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldLocationId () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_LOCATION_ID_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_LOCATION_ID_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldLongitude () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_LONGITUDE_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_LONGITUDE_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldContentDate () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_CONTENT_DATE_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_CONTENT_DATE_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   @Override
   public SolrFacetFieldEntity getSolrStaticFieldProcessingState () {
      String fieldName = getDataAccessServicesConfiguration().getProperty(SOLR_STATIC_FIELD_PROCESSING_STATE_NAME_KEY);
      SolrFieldType fieldType = SolrFieldType.valueOf(getDataAccessServicesConfiguration().getProperty(
               SOLR_STATIC_FIELD_PROCESSING_STATE_TYPE_KEY));
      return new SolrFacetFieldEntity(fieldName, fieldType);
   }

   public SolrStaticFieldsConfigurationInfo getSolrStaticFieldsConfigurationInfo () {
      SolrStaticFieldsConfigurationInfo solrStaticFieldsConfigurationInfo = new SolrStaticFieldsConfigurationInfo();
      solrStaticFieldsConfigurationInfo.setContextIdField(getSolrStaticFieldContextId());
      solrStaticFieldsConfigurationInfo.setDocumentIdField(getSolrStaticFieldDocumentId());
      solrStaticFieldsConfigurationInfo.setImagePresentField(getSolrStaticFieldImagePresent());
      solrStaticFieldsConfigurationInfo.setLatitudeField(getSolrStaticFieldLatitude());
      solrStaticFieldsConfigurationInfo.setLocationIdField(getSolrStaticFieldLocationId());
      solrStaticFieldsConfigurationInfo.setLongitudeField(getSolrStaticFieldLongitude());
      solrStaticFieldsConfigurationInfo.setProcessingStateField(getSolrStaticFieldProcessingState());
      solrStaticFieldsConfigurationInfo.setContentDateField(getSolrStaticFieldContentDate());
      return solrStaticFieldsConfigurationInfo;
   }

   public Integer getSolrFacetQueryParameterTotalRows () {
      return getDataAccessServicesConfiguration().getInt(SOLR_FACET_QUERY_PARAMETER_TOTAL_ROWS_KEY);
   }

   public boolean isSolrFacetQueryParameterFacetsEnabled () {
      return getDataAccessServicesConfiguration().getBoolean(SOLR_FACET_QUERY_PARAMETER_FACET_KEY);
   }

   public String getSolrFacetQueryParameterFacetMethodParamName () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_FACET_QUERY_PARAMETER_FACET_METHOD_PARAM_NAME_KEY);
   }

   public String getSolrFacetQueryParameterFacetMethodParamValue () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_FACET_QUERY_PARAMETER_FACET_METHOD_PARAM_VALUE_KEY);
   }

   public boolean isSolrFacetQueryParameterSortByCountEnabled () {
      return getDataAccessServicesConfiguration().getBoolean(SOLR_FACET_QUERY_PARAMETER_FACET_SORT_BY_COUNT_KEY);
   }

   public Integer getSolrFacetQueryParameterFetchFacetsWithMinCount () {
      return getDataAccessServicesConfiguration().getInt(SOLR_FACET_QUERY_PARAMETER_FETCH_FACETS_WITH_MIN_COUNT_KEY);
   }

   public Integer getSolrFacetQueryParameterFacetLimit () {
      return getDataAccessServicesConfiguration().getInt(SOLR_FACET_QUERY_PARAMETER_FACET_LIMIT_KEY);
   }

   @Override
   public SolrFacetQueryConstantParameters getSolrFacetQueryConstantParameters () {
      SolrFacetQueryConstantParameters solrFacetQueryConstantParameters = new SolrFacetQueryConstantParameters();
      solrFacetQueryConstantParameters.setTotalRows(getSolrFacetQueryParameterTotalRows());
      solrFacetQueryConstantParameters.setFacet(isSolrFacetQueryParameterFacetsEnabled());
      solrFacetQueryConstantParameters.setFacetMethodParamName(getSolrFacetQueryParameterFacetMethodParamName());
      solrFacetQueryConstantParameters.setFacetMethodParamValue(getSolrFacetQueryParameterFacetMethodParamValue());
      solrFacetQueryConstantParameters.setFacetSortByCount(isSolrFacetQueryParameterSortByCountEnabled());
      solrFacetQueryConstantParameters.setFetchFacetsWithMinCount(getSolrFacetQueryParameterFetchFacetsWithMinCount());
      solrFacetQueryConstantParameters.setFacetLimit(getSolrFacetQueryParameterFacetLimit());
      return solrFacetQueryConstantParameters;
   }

   @Override
   public String getSolrSingleLocationDistanceFormula () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_SINGLE_LOCATION_DISTANCE_FORMULA_KEY);
   }

   @Override
   public String getSolrMultipleLocationDistanceFormula () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_MULTIPLE_LOCATION_DISTANCE_FORMULA_KEY);
   }

   @Override
   public String getSolrDistanceReplaceToken () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_DISTANCE_REPLACE_TOKEN_KEY);
   }

   @Override
   public String getSolrLatitudeReplaceToken () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_LATITUDE_REPLACE_TOKEN_KEY);
   }

   @Override
   public String getSolrLongitudeReplaceToken () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_LONGITUDE_REPLACE_TOKEN_KEY);
   }

   @Override
   public String getSolrMultipleLongitudeLatitudeReplaceToken () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_MULTIPLE_LONGITUDE_LATITUDE_REPLACE_TOKEN_KEY);
   }

   @Override
   public String getSolrLatitudeFieldReplaceToken () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_LATITUDE_FIELD_REPLACE_TOKEN_KEY);
   }

   @Override
   public String getSolrLongitudeFieldReplaceToken () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_LONGITUDE_FIELD_REPLACE_TOKEN_KEY);
   }

   @Override
   public SolrDistanceConfigurationInfo getSolrDistanceConfigurationInfo () {
      SolrDistanceConfigurationInfo solrDistanceConfigurationInfo = new SolrDistanceConfigurationInfo();
      solrDistanceConfigurationInfo.setSingleLocationDistanceFormula(getSolrSingleLocationDistanceFormula());
      solrDistanceConfigurationInfo.setMultipleLocationDistanceFormula(getSolrMultipleLocationDistanceFormula());
      solrDistanceConfigurationInfo.setDistanceReplaceToken(getSolrDistanceReplaceToken());
      solrDistanceConfigurationInfo.setLongitudeReplaceToken(getSolrLongitudeReplaceToken());
      solrDistanceConfigurationInfo.setLatitudeReplaceToken(getSolrLatitudeReplaceToken());
      solrDistanceConfigurationInfo
               .setMutipleLontitudeLatitudeReplaceToken(getSolrMultipleLongitudeLatitudeReplaceToken());
      solrDistanceConfigurationInfo.setLongitudeFieldReplaceToken(getSolrLongitudeFieldReplaceToken());
      solrDistanceConfigurationInfo.setLatitudeFieldReplaceToken(getSolrLatitudeFieldReplaceToken());
      return solrDistanceConfigurationInfo;
   }

   @Override
   public String getSolrNumberColumnFieldSuffix () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_NUMBER_COLUMN_FIELD_SUFFIX_KEY);
   }

   @Override
   public String getSolrLongColumnFieldSuffix () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_LONG_COLUMN_FIELD_SUFFIX_KEY);
   }

   @Override
   public String getSolrStringColumnFieldSuffix () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_STRING_COLUMN_FIELD_SUFFIX_KEY);
   }

   @Override
   public String getSolrDateColumnFieldSuffix () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_DATE_COLUMN_FIELD_SUFFIX_KEY);
   }

   @Override
   public String getSolrMultiValuedFeatureSeperator () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_MULTI_VALUED_FEATURE_SEPERATOR_KEY);
   }

   @Override
   public String getSolrDocumentIdFieldSeperator () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_DOCUMENT_ID_FIELD_SEPERATOR_KEY);
   }

   @Override
   public String getSolrMultiValuedSeperatorColumnFieldSuffix () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_MULTI_VALUED_SEPERATOR_COLUMN_SUFFIX_KEY);
   }

   @Override
   public SolrFieldConfigurationInfo getSolrFieldConfigurationInfo () {
      SolrFieldConfigurationInfo solrFieldConfigurationInfo = new SolrFieldConfigurationInfo();
      solrFieldConfigurationInfo.setLongColumnSuffix(getSolrLongColumnFieldSuffix());
      solrFieldConfigurationInfo.setNumberColumnSuffix(getSolrNumberColumnFieldSuffix());
      solrFieldConfigurationInfo.setStringColumnSuffix(getSolrStringColumnFieldSuffix());
      solrFieldConfigurationInfo.setDateColumnSuffix(getSolrDateColumnFieldSuffix());
      solrFieldConfigurationInfo.setMultivaluedFeatureSeperator(getSolrMultiValuedFeatureSeperator());
      solrFieldConfigurationInfo.setMultivaluedSeperatorColumnSuffix(getSolrMultiValuedSeperatorColumnFieldSuffix());
      solrFieldConfigurationInfo.setDocumentFieldIdSeperator(getSolrDocumentIdFieldSeperator());
      return solrFieldConfigurationInfo;
   }

   @Override
   public String getSolrDefaultQuery () {
      return getDataAccessServicesConfiguration().getProperty(SOLR_DEFAULT_QUERY_KEY);
   }

}
