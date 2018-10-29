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


package com.execue.core.common.bean.pseudolang;

import java.util.List;

import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.OrderEntityType;

/**
 * Pseudo class representing the BuinessTerm for Pseudo language statement creation purpose<br/> orderType and rowCount
 * are significant only when used as order clause in NormalizedPseudoQuery<br/>
 * 
 * @author execue
 * @since 4.0
 * @version 1.0
 */
public class PseudoEntity {

   // for comparison purposes - value comes from Business term
   private long            id;

   // display name of the Business Term
   private String          entityDescription;

   // is requested by user
   private boolean         fromUser;

   List<PseudoStat>        stats;

   // Ascending or Descending
   private OrderEntityType orderType;

   // restricted number of rows from the result
   private int             rowCount;

   // flag to be set when the entity comes from the cohort query
   private boolean         fromCohort;

   private String          profileName;

   private boolean         partOfProfile;

   private Long            profileBedId;

   private ColumnType      kdxType = ColumnType.NULL;

   public ColumnType getKdxType () {
      return kdxType;
   }

   public void setKdxType (ColumnType kdxType) {
      this.kdxType = kdxType;
   }

   public Long getProfileBedId () {
      return profileBedId;
   }

   public void setProfileBedId (Long profileBedId) {
      this.profileBedId = profileBedId;
   }

   public String getProfileName () {
      return profileName;
   }

   public void setProfileName (String profileName) {
      this.profileName = profileName;
   }

   public boolean isPartOfProfile () {
      return partOfProfile;
   }

   public void setPartOfProfile (boolean partOfProfile) {
      this.partOfProfile = partOfProfile;
   }

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   public String getEntityDescription () {
      return entityDescription;
   }

   public void setEntityDescription (String entityDescription) {
      this.entityDescription = entityDescription;
   }

   public boolean isFromUser () {
      return fromUser;
   }

   public void setFromUser (boolean fromUser) {
      this.fromUser = fromUser;
   }

   public List<PseudoStat> getStats () {
      return stats;
   }

   public void setStats (List<PseudoStat> stats) {
      this.stats = stats;
   }

   public OrderEntityType getOrderType () {
      return orderType;
   }

   public void setOrderType (OrderEntityType orderType) {
      this.orderType = orderType;
   }

   public int getRowCount () {
      return rowCount;
   }

   public void setRowCount (int rowCount) {
      this.rowCount = rowCount;
   }

   public boolean isFromCohort () {
      return fromCohort;
   }

   public void setFromCohort (boolean fromCohort) {
      this.fromCohort = fromCohort;
   }
}
