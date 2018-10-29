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

import org.apache.commons.lang.StringUtils;

import com.execue.core.common.type.ConnectionType;
import com.opensymphony.xwork2.validator.ValidationException;
import com.opensymphony.xwork2.validator.validators.FieldValidatorSupport;

public class DataSourceValidator extends FieldValidatorSupport {

   public void validate (Object object) throws ValidationException {
      String fieldName = getFieldName();
      if (fieldName.equals("dataSource.connectionType")) {
         ConnectionType connectionType = (ConnectionType) getFieldValue(fieldName, object);

         if (ConnectionType.JNDI.equals(connectionType)) {
            String jndiFactory = (String) getFieldValue("dataSource.jndiConnectionFactory", object);
            String jndiURL = (String) getFieldValue("dataSource.jndiProviderUrl", object);
            String jndiName = (String) getFieldValue("dataSource.jndiName", object);
            String userName = (String) getFieldValue("dataSource.userName", object);
            // String password = (String) getFieldValue("dataSource.password", object);
            if (jndiFactory == null) {
               addFieldError("execue.dataSource.jndiConnectionFactory", object);
               setMessageKey("execue.dataSource.jndiConnectionFactory.empty");
            }

            if (jndiURL == null) {
               addFieldError("execue.dataSource.jndiProviderUrl", object);
               setMessageKey("execue.dataSource.jndiProviderUrl.empty");
            }
            if (jndiName == null) {

               addFieldError("execue.dataSource.jndiName", object);
               setMessageKey("execue.dataSource.jndiName.empty");
            }
            if (userName == null) {
               addFieldError("execue.dataSource.userName", object);
               setMessageKey("execue.dataSource.userName.empty");
            }
            // Password may be blank
            /*
             * if (password == null) { addFieldError("dataSource.password", object);
             * setMessageKey("dataSource.password.empty"); }
             */
         }
         if (ConnectionType.PROPERTIES.equals(connectionType)) {
            String location = (String) getFieldValue("dataSource.location", object);
            String schemaName = (String) getFieldValue("dataSource.schemaName", object);
            String userName = (String) getFieldValue("dataSource.userName", object);
            // String password = (String) getFieldValue("dataSource.password", object);
            // int port = (Integer) getFieldValue("dataSource.port", object);
            int port = 0;
            String portStr = (String) getFieldValue("portStr", object);
            if (!StringUtils.isBlank(portStr)) {
               port = Integer.parseInt(portStr);
            }
            if (location == null) {
               addFieldError("execue.dataSource.location", object);
               setMessageKey("execue.dataSource.location.empty");

            }
            if (port <= 0) {
               addFieldError("execue.dataSource.port", object);
               setMessageKey("execue.dataSource.port.invalid");
            }
            if (schemaName == null) {
               addFieldError("execue.dataSource.schemaName", object);
               setMessageKey("execue.dataSource.schemaName.empty");
            }
            if (userName == null) {
               addFieldError("execue.dataSource.userName", object);
               setMessageKey("execue.dataSource.userName.empty");
            }
            // Password may be blank
            /*
             * if (password == null) { addFieldError("dataSource.password", object);
             * setMessageKey("dataSource.password.empty"); }
             */

         }
      }
   }

}
