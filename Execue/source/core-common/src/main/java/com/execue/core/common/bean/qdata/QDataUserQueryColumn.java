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


package com.execue.core.common.bean.qdata;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.type.OperatorType;
import com.execue.core.common.type.QuerySectionType;

/**
 * This bean represents the query column asked by the user
 * 
 * @author Vishay
 * @version 1.0
 * @since 25/06/09
 */
public class QDataUserQueryColumn implements Serializable {

   private Long             id;
   private String           name;
   private String           stat;
   private QuerySectionType querySection;
   private OperatorType     operator;
   // TODO : -VG- this has to be multiple values
   private String           value;
   private Long             businessEntityId;
   private QDataUserQuery   userQuery;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getStat () {
      return stat;
   }

   public void setStat (String stat) {
      this.stat = stat;
   }

   public String getValue () {
      //TODO: -RG- This is a temporary fix.
      //Storage of value should be decided after the query cache search
      //algorithm is finalized
      if (!StringUtils.isBlank(value) && value.length() > 255) {
         return value.substring(0,255);
      }
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public QDataUserQuery getUserQuery () {
      return userQuery;
   }

   public void setUserQuery (QDataUserQuery userQuery) {
      this.userQuery = userQuery;
   }

   public QuerySectionType getQuerySection () {
      return querySection;
   }

   public void setQuerySection (QuerySectionType querySection) {
      this.querySection = querySection;
   }

   public OperatorType getOperator () {
      return operator;
   }

   public void setOperator (OperatorType operator) {
      this.operator = operator;
   }

   public Long getBusinessEntityId () {
      return businessEntityId;
   }

   public void setBusinessEntityId (Long businessEntityId) {
      this.businessEntityId = businessEntityId;
   }
}
