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


package com.execue.handler.transformer;

import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.CheckType;
import com.execue.handler.bean.UIColumn;
import com.execue.handler.bean.UITable;

public class TableTransformer {

   public void transformToUIObject (Tabl fromObject, UITable toObject) {
      toObject.setName(fromObject.getName());
      toObject.setId(fromObject.getId());
      toObject.setDescription(fromObject.getDescription());
      toObject.setOwner(fromObject.getOwner());
      toObject.setActualName(fromObject.getActualName());
      toObject.setEligibleSystemDefaultMetric(fromObject.getEligibleDefaultMetric());
      toObject.setVirtual(fromObject.getVirtual().getValue());
      toObject.setDisplayName(fromObject.getDisplayName());
      toObject.setLookupColumnName(fromObject.getLookupValueColumn());
   }

   public void transformToDomainObject (UITable fromObject, Tabl toObject) {
      toObject.setName(fromObject.getName());
      toObject.setDisplayName(fromObject.getDisplayName());
      toObject.setId(fromObject.getId());
      toObject.setOwner(fromObject.getOwner());
      toObject.setDescription(fromObject.getDescription());
      toObject.setActualName(fromObject.getActualName());
      toObject.setVirtual(CheckType.getType(fromObject.getVirtual()));
      toObject.setEligibleDefaultMetric(fromObject.getEligibleSystemDefaultMetric());
   }

   public void transformToUIObject (Colum fromObject, UIColumn toObject) {
      toObject.setName(fromObject.getName());
      toObject.setId(fromObject.getId());
      toObject.setDescription(fromObject.getDescription());
      toObject.setDataType(fromObject.getDataType());
   }

   public void transformToDomainObject (UIColumn fromObject, Colum toObject) {
      toObject.setName(fromObject.getName());
      toObject.setId(fromObject.getId());
      toObject.setDescription(fromObject.getDescription());
      toObject.setDataType(fromObject.getDataType());
   }
}
