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

import java.util.ArrayList;
import java.util.List;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Vishay
 */

/*
 * #DISTANCE# --> a decimal number representing how much is the max distance. In this case it used as exclusive upper
 * bound in range condition #LONGITUDE_FIELD_NAME# --> field name of longitude in solr In this case "long_d"
 * #LATITUDE_FIELD_NAME# --> field name of latitude in solr In this case "lat_d" #LONGITUDE# --> a decimal number
 * representing longitude Example : -36.0042 #LATITUDE# --> a decimal number representing latitude Example : 72.41672
 * #LIST_OF_LOCATIONS# --> a semi-colon separated string of comma separated longitude and lantitude values in decimals
 * Example : -36.0042,72.41672;-98.6574,12.8723 <single-location-strightline-distance-condition>{!frange u=#DISTANCE#
 * ignoreu=true}sum(pow(sub(#LONGITUDE#,#LONGITUDE_FIELD_NAME#),2),pow(sub(#LATITUDE#,#LATITUDE_FIELD_NAME#),2))<single-location-strightline-distance-condition>
 * <multiple-location-strightline-distance-condition>{!frange u=#DISTANCE#
 * ignoreu=true}minStLnDist(#LONGITUDE_FIELD_NAME#,#LATITUDE_FIELD_NAME#,"#LIST_OF_LOCATIONS#")<multiple-location-strightline-distance-condition>
 */
public class SolrDistanceConditionEntity {

   private Double                 distance;
   private String                 solrLongitudefieldName;
   private String                 solrLatitudefieldName;
   private List<SolrLocationInfo> locationInfoList;

   public SolrDistanceConditionEntity (Double distance, String solrLongitudefieldName, String solrLatitudefieldName,
            List<SolrLocationInfo> locationInfoList) {
      super();
      this.distance = distance;
      this.solrLongitudefieldName = solrLongitudefieldName;
      this.solrLatitudefieldName = solrLatitudefieldName;
      this.locationInfoList = locationInfoList;
   }

   public SolrDistanceConditionEntity () {
      super();
   }

   public Double getDistance () {
      return distance;
   }

   public void setDistance (Double distance) {
      this.distance = distance;
   }

   public String getSolrLatitudefieldName () {
      return solrLatitudefieldName;
   }

   public void setSolrLatitudefieldName (String solrLatitudefieldName) {
      this.solrLatitudefieldName = solrLatitudefieldName;
   }

   public String getSolrLongitudefieldName () {
      return solrLongitudefieldName;
   }

   public void setSolrLongitudefieldName (String solrLongitudefieldName) {
      this.solrLongitudefieldName = solrLongitudefieldName;
   }

   public void addSolrLocationInfo (SolrLocationInfo locationInfo) {
      if (ExecueCoreUtil.isCollectionEmpty(locationInfoList)) {
         locationInfoList = new ArrayList<SolrLocationInfo>();
      }
      locationInfoList.add(locationInfo);
   }

   public List<SolrLocationInfo> getLocationInfoList () {
      return locationInfoList;
   }

   public void setLocationInfoList (List<SolrLocationInfo> locationInfoList) {
      this.locationInfoList = locationInfoList;
   }
}
