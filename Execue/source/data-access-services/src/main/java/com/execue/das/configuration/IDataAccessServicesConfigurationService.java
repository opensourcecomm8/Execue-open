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


package com.execue.das.configuration;

import com.execue.das.solr.bean.SolrDistanceConfigurationInfo;
import com.execue.das.solr.bean.SolrFacetFieldEntity;
import com.execue.das.solr.bean.SolrFacetQueryConstantParameters;
import com.execue.das.solr.bean.SolrFieldConfigurationInfo;
import com.execue.das.solr.bean.SolrStaticFieldsConfigurationInfo;

/**
 * @author Vishay
 */
public interface IDataAccessServicesConfigurationService {

   public String getRemoteIPLocationDataSource ();

   public String getCityCenterZipCodeDataSource ();

   public SolrFacetFieldEntity getSolrStaticFieldDocumentId ();

   public SolrFacetFieldEntity getSolrStaticFieldContextId ();

   public SolrFacetFieldEntity getSolrStaticFieldLocationId ();

   public SolrFacetFieldEntity getSolrStaticFieldLatitude ();

   public SolrFacetFieldEntity getSolrStaticFieldLongitude ();

   public SolrFacetFieldEntity getSolrStaticFieldImagePresent ();

   public SolrFacetFieldEntity getSolrStaticFieldProcessingState ();

   public SolrFacetFieldEntity getSolrStaticFieldContentDate ();

   public SolrStaticFieldsConfigurationInfo getSolrStaticFieldsConfigurationInfo ();

   public Integer getSolrFacetQueryParameterTotalRows ();

   public boolean isSolrFacetQueryParameterFacetsEnabled ();

   public String getSolrFacetQueryParameterFacetMethodParamName ();

   public String getSolrFacetQueryParameterFacetMethodParamValue ();

   public boolean isSolrFacetQueryParameterSortByCountEnabled ();

   public Integer getSolrFacetQueryParameterFetchFacetsWithMinCount ();

   public Integer getSolrFacetQueryParameterFacetLimit ();

   public SolrFacetQueryConstantParameters getSolrFacetQueryConstantParameters ();

   public String getSolrSingleLocationDistanceFormula ();

   public String getSolrMultipleLocationDistanceFormula ();

   public String getSolrDistanceReplaceToken ();

   public String getSolrLongitudeReplaceToken ();

   public String getSolrLatitudeReplaceToken ();

   public String getSolrMultipleLongitudeLatitudeReplaceToken ();

   public String getSolrLongitudeFieldReplaceToken ();

   public String getSolrLatitudeFieldReplaceToken ();

   public SolrDistanceConfigurationInfo getSolrDistanceConfigurationInfo ();

   public String getSolrNumberColumnFieldSuffix ();

   public String getSolrLongColumnFieldSuffix ();

   public String getSolrStringColumnFieldSuffix ();

   public String getSolrDateColumnFieldSuffix ();

   public String getSolrMultiValuedSeperatorColumnFieldSuffix ();

   public String getSolrMultiValuedFeatureSeperator ();

   public String getSolrDocumentIdFieldSeperator ();

   public SolrFieldConfigurationInfo getSolrFieldConfigurationInfo ();

   public String getSolrDefaultQuery ();

}
