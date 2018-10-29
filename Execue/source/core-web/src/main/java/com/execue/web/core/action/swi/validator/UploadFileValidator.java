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

import java.io.File;

import com.execue.core.common.type.CheckType;
import com.execue.core.util.ExecueCoreUtil;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class UploadFileValidator extends FieldValidatorSupport {

   public void validate (Object object) throws ValidationException {
      String fieldName = getFieldName();
      if (fieldName.equals("absorbDataset")) {
         CheckType absorbDataset = (CheckType) getFieldValue(fieldName, object);
         String fileName = (String) getFieldValue("fileName", object);
         String fileDescription = (String) getFieldValue("fileDescription", object);
         String sourceURL = (String) getFieldValue("sourceURL", object);
         File sourceData = (File) getFieldValue("sourceData", object);
         if (CheckType.YES.equals(absorbDataset)) {
            if (sourceData == null) {
               addFieldError("sourceData", object);
               setMessageKey("execue.upload.main.file.selection.required");
            }
         } else {
            if (sourceData == null && ExecueCoreUtil.isEmpty(sourceURL)) {
               addFieldError("sourceURL", object);
               setMessageKey("execue.upload.main.file.required");
            }
         }
         if (ExecueCoreUtil.isEmpty(fileName)) {
            addFieldError("fileName", object);
            setMessageKey("execue.upload.main.file.name.required");
         }
         if (ExecueCoreUtil.isEmpty(fileDescription)) {
            addFieldError("fileDescription", object);
            setMessageKey("execue.upload.main.file.description.required");
         }
      }

   }
}
