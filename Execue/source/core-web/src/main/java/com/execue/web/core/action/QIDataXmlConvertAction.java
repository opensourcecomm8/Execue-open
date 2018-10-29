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


package com.execue.web.core.action;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.UserInput;
import com.execue.core.exception.HandlerRequestTransformException;
import com.execue.handler.bean.UIUserInput;
import com.execue.handler.qi.QueryInterfaceHandlerReqeust;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ActionSupport;

public class QIDataXmlConvertAction extends ActionSupport {

   private static final Logger          logger = Logger.getLogger(QIDataXmlConvertAction.class);

   private String                       requestXML;
   private QueryInterfaceHandlerReqeust queryInterfaceRequestXmlTransform;
   private String                       qiJSONData;

   public String getJsonString () {

      if (logger.isDebugEnabled()) {
         logger.debug("requestXML:" + requestXML);
      }
      if (requestXML != null) {
         try {
            UIUserInput userRequest = new UIUserInput();
            userRequest.setRequest(getRequestXML());
            UserInput userInput = (UserInput) queryInterfaceRequestXmlTransform.transformRequest(userRequest);
            QueryForm queryForm = userInput.getQueryForm();
            qiJSONData = JSONUtil.serialize(queryForm);
         } catch (HandlerRequestTransformException e) {
            e.printStackTrace();
         } catch (JSONException e) {
            e.printStackTrace();
         }
      }
      return SUCCESS;

   }

   public String getRequestXML () {
      return requestXML;
   }

   public void setRequestXML (String requestXML) {
      this.requestXML = requestXML;
   }

   public QueryInterfaceHandlerReqeust getQueryInterfaceRequestXmlTransform () {
      return queryInterfaceRequestXmlTransform;
   }

   public void setQueryInterfaceRequestXmlTransform (QueryInterfaceHandlerReqeust queryInterfaceRequestXmlTransform) {
      this.queryInterfaceRequestXmlTransform = queryInterfaceRequestXmlTransform;
   }

   public String getQiJSONData () {
      return qiJSONData;
   }

   public void setQiJSONData (String qiJSONData) {
      this.qiJSONData = qiJSONData;
   }
}
