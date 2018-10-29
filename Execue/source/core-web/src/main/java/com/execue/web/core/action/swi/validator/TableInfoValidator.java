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


package com.execue.web.core.action.swi.validator;

import com.execue.core.common.type.LookupType;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class TableInfoValidator extends FieldValidatorSupport {

   private static final String NONE              = "None";
   private String              lookupValueColumn = "";
   private String              lookupDescColumn  = "";

   public void validate (Object object) throws ValidationException {
      String fieldName = getFieldName();
      if (fieldName.equalsIgnoreCase("table.lookupType")) {
         LookupType lookUpType = (LookupType) getFieldValue(fieldName, object);

         if (LookupType.RANGE_LOOKUP.equals(lookUpType)) {
            checkLookUpData(object);
            String lowerLimitColumn = (String) getFieldValue("table.lowerLimitColumn", object);
            String upperLimitColumn = (String) getFieldValue("table.upperLimitColumn", object);
            if (lowerLimitColumn.equalsIgnoreCase(NONE) || upperLimitColumn.equalsIgnoreCase(NONE)) {
               addFieldError("execue.table.lowerLimitColumn", object);
               setMessageKey("execue.table.lowerUpperLimitColumn.notselected");
            }

         } else if (LookupType.SIMPLE_LOOKUP.equals(lookUpType)) {
            checkLookUpData(object);
         }

      }

   }

   private void checkLookUpData (Object object) throws ValidationException {
      lookupValueColumn = (String) getFieldValue("table.lookupValueColumn", object);
      lookupDescColumn = (String) getFieldValue("table.lookupDescColumn", object);

      if (lookupValueColumn.equalsIgnoreCase(NONE) || lookupDescColumn.equalsIgnoreCase(NONE)) {
         addFieldError("execue.table.lookupValueColumn", object);
         setMessageKey("execue.table.lookupValueDescColumn.notselected");
      }

   }

}
