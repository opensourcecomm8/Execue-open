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


package com.execue.handler.qi;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.UserInput;
import com.execue.core.common.util.ExeCueXMLUtils;
import com.execue.core.exception.HandlerRequestTransformException;
import com.execue.handler.IHandlerRequest;
import com.execue.handler.bean.UIUserInput;

/**
 * @author kaliki
 * @since 4.0
 */

public class QueryInterfaceHandlerReqeust implements IHandlerRequest {

   Logger logger = Logger.getLogger(QueryInterfaceHandlerReqeust.class);

   public Object transformRequest (Object o) throws HandlerRequestTransformException {
      UIUserInput userRequest = (UIUserInput) o;
      String xmlRequest = userRequest.getRequest();
      UserInput userInput = generateUserInput(userRequest);
      logger.info("xml Request received [ " + xmlRequest + " ] ");
      userInput.setQueryForm(ExeCueXMLUtils.parseXML(xmlRequest));
      return userInput;
   }

   private UserInput generateUserInput (UIUserInput userRequest) {
      UserInput userInput = new UserInput();
      userInput.setAssetId(userRequest.getAssetId());
      userInput.setBusinessQueryId(userRequest.getBusinessQueryId());
      userInput.setRequest(userRequest.getRequest());
      userInput.setRequestedPage(userRequest.getRequestedPage());
      userInput.setUserQueryId(userRequest.getUserQueryId());
      userInput.setPageSize(userRequest.getPageSize());
      return userInput;
   }

}