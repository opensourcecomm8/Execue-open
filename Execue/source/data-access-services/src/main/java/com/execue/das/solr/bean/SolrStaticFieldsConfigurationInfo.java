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


package com.execue.das.solr.bean;

/**
 * @author Vishay
 */
public class SolrStaticFieldsConfigurationInfo {

   private SolrFacetFieldEntity documentIdField;
   private SolrFacetFieldEntity contextIdField;
   private SolrFacetFieldEntity locationIdField;
   private SolrFacetFieldEntity imagePresentField;
   private SolrFacetFieldEntity latitudeField;
   private SolrFacetFieldEntity longitudeField;
   private SolrFacetFieldEntity contentDateField;
   private SolrFacetFieldEntity processingStateField;

   public SolrFacetFieldEntity getDocumentIdField () {
      return documentIdField;
   }

   public void setDocumentIdField (SolrFacetFieldEntity documentIdField) {
      this.documentIdField = documentIdField;
   }

   public SolrFacetFieldEntity getContextIdField () {
      return contextIdField;
   }

   public void setContextIdField (SolrFacetFieldEntity contextIdField) {
      this.contextIdField = contextIdField;
   }

   public SolrFacetFieldEntity getLocationIdField () {
      return locationIdField;
   }

   public void setLocationIdField (SolrFacetFieldEntity locationIdField) {
      this.locationIdField = locationIdField;
   }

   public SolrFacetFieldEntity getImagePresentField () {
      return imagePresentField;
   }

   public void setImagePresentField (SolrFacetFieldEntity imagePresentField) {
      this.imagePresentField = imagePresentField;
   }

   public SolrFacetFieldEntity getLatitudeField () {
      return latitudeField;
   }

   public void setLatitudeField (SolrFacetFieldEntity latitudeField) {
      this.latitudeField = latitudeField;
   }

   public SolrFacetFieldEntity getLongitudeField () {
      return longitudeField;
   }

   public void setLongitudeField (SolrFacetFieldEntity longitudeField) {
      this.longitudeField = longitudeField;
   }

   public SolrFacetFieldEntity getContentDateField () {
      return contentDateField;
   }

   public void setContentDateField (SolrFacetFieldEntity contentDateField) {
      this.contentDateField = contentDateField;
   }

   public SolrFacetFieldEntity getProcessingStateField () {
      return processingStateField;
   }

   public void setProcessingStateField (SolrFacetFieldEntity processingStateField) {
      this.processingStateField = processingStateField;
   }

}
