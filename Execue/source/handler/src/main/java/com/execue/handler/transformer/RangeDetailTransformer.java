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

import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIRangeDetail;

public class RangeDetailTransformer {

   // TODO: -RG- should be taken from configuration
   public static final String LOWEST_LOWER_LIMIT  = "Low";
   public static final String HIGHEST_UPPER_LIMIT = "High";

   public void transformToUIObject (RangeDetail fromObject, UIRangeDetail toObject) {
      toObject.setId(fromObject.getId());
      toObject.setValue(fromObject.getValue());
      toObject.setDescription(fromObject.getDescription());
      if (fromObject.getLowerLimit() == null) {
         toObject.setLowerLimit(LOWEST_LOWER_LIMIT);
      } else {
         toObject.setLowerLimit(ExecueCoreUtil.getSmoothenedNumberDescription(ExecueCoreUtil.getSmoothenedNumber(fromObject
                  .getLowerLimit())));
      }
      if (fromObject.getUpperLimit() == null) {
         toObject.setUpperLimit(HIGHEST_UPPER_LIMIT);
      } else {
         toObject.setUpperLimit(ExecueCoreUtil.getSmoothenedNumberDescription(ExecueCoreUtil.getSmoothenedNumber(fromObject
                  .getUpperLimit())));
      }
      toObject.setOrder(fromObject.getOrder().toString());
   }

   public void transformToDomainObject (UIRangeDetail fromObject, RangeDetail toObject) {
      toObject.setId(fromObject.getId());
      toObject.setValue(fromObject.getValue());
      toObject.setDescription(fromObject.getDescription());
      if (LOWEST_LOWER_LIMIT.equalsIgnoreCase(fromObject.getLowerLimit())) {
         toObject.setLowerLimit(null);
      } else {
         toObject.setLowerLimit(new Double(fromObject.getLowerLimit()));
      }
      if (HIGHEST_UPPER_LIMIT.equalsIgnoreCase(fromObject.getUpperLimit())) {
         toObject.setUpperLimit(null);
      } else {
         toObject.setUpperLimit(new Double(fromObject.getUpperLimit()));
      }
      toObject.setOrder(Integer.parseInt(fromObject.getOrder()));
   }

}
