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


package com.execue.core.common.bean.nlp;

import java.util.ArrayList;
import java.util.List;

import com.execue.core.common.bean.AbstractNormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;

/**
 * @author Nitesh
 */
public class ListNormalizedData extends AbstractNormalizedData {

   List<NormalizedDataEntity> normalizedDataEntities;
   String                     type;
   Long                       typeBedId;

   /**
    * @return the NormalizedDataType.VALUE_CLOUD_TYPE
    */
   public NormalizedDataType getNormalizedDataType () {
      return NormalizedDataType.LIST_NORMALIZED_DATA;
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      ListNormalizedData clonedTFNormalizedData = (ListNormalizedData) super.clone();
      clonedTFNormalizedData.setType(type);
      clonedTFNormalizedData.setTypeBedId(typeBedId);
      List<NormalizedDataEntity> clonedNormalizedDataEntities = new ArrayList<NormalizedDataEntity>();
      List<NormalizedDataEntity> normalizedDataEntities = getNormalizedDataEntities();
      for (NormalizedDataEntity normalizedDataEntity : normalizedDataEntities) {
         clonedNormalizedDataEntities.add((NormalizedDataEntity) normalizedDataEntity.clone());
      }
      clonedTFNormalizedData.setNormalizedDataEntities(clonedNormalizedDataEntities);
      return clonedTFNormalizedData;
   }

   /*
    * public void OPValueNormalizedData () { }
    */
   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   @Override
   public String toString () {
      return getValue();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#hashCode()
    */
   @Override
   public int hashCode () {
      return toString().hashCode();
   }

   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#equals(java.lang.Object)
    */
   @Override
   public boolean equals (Object obj) {
      return (obj instanceof ListNormalizedData || obj instanceof String) && this.toString().equals(obj.toString());
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   public String getType () {
      return type;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */

   public Long getTypeBedId () {
      return typeBedId;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   public String getValue () {
      // TODO:NK: get the list of normalized data entities value
      return "";
   }

   /**
    * @param type
    *           the type to set
    */
   public void setType (String type) {
      this.type = type;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.nlp.bean.entity.UnitNormalizedData#getDisplayValue()
    */
   public String getDisplayValue () {
      // TODO:NK: display the list of normalized data entities value
      return "";
   }

   /**
    * @return the normalizedDataEntities
    */
   public List<NormalizedDataEntity> getNormalizedDataEntities () {
      if (this.normalizedDataEntities == null) {
         this.normalizedDataEntities = new ArrayList<NormalizedDataEntity>(1);
      }
      return normalizedDataEntities;
   }

   /**
    * @param normalizedDataEntities
    *           the normalizedDataEntities to set
    */
   public void setNormalizedDataEntities (List<NormalizedDataEntity> normalizedDataEntities) {
      this.normalizedDataEntities = normalizedDataEntities;
   }

   /**
    * @param typeBedId
    *           the typeBedId to set
    */
   public void setTypeBedId (Long typeBedId) {
      this.typeBedId = typeBedId;
   }
}
