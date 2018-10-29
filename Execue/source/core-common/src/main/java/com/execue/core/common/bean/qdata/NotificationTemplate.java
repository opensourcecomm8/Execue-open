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

import com.execue.core.common.type.NotificationType;
import com.execue.core.common.type.TemplateType;

public class NotificationTemplate implements Serializable {

   private Long             id;
   private NotificationType notificationType;
   private TemplateType     templateType;
   private String           template;
   private String           paramNames;
   private transient String message;
   private transient String errorMessage;

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   public NotificationType getNotificationType () {
      return notificationType;
   }

   public void setNotificationType (NotificationType notificationType) {
      this.notificationType = notificationType;
   }

   public TemplateType getTemplateType () {
      return templateType;
   }

   public void setTemplateType (TemplateType templateType) {
      this.templateType = templateType;
   }

   public String getTemplate () {
      return template;
   }

   public void setTemplate (String template) {
      this.template = template;
   }

   public String getParamNames () {
      return paramNames;
   }

   public void setParamNames (String paramNames) {
      this.paramNames = paramNames;
   }

   public String getMessage () {
      return message;
   }

   public void setMessage (String message) {
      this.message = message;
   }

   public String getErrorMessage () {
      return errorMessage;
   }

   public void setErrorMessage (String errorMessage) {
      this.errorMessage = errorMessage;
   }

}
