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


package com.execue.handler;

import org.apache.log4j.Logger;

import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.exception.HandlerException;
import com.execue.core.exception.HandlerRequestTransformException;
import com.execue.core.exception.HandlerResponseTransformException;
import com.execue.handler.bean.Response;
import com.execue.security.UserContextService;

/**
 * @author kaliki
 * @since 4.0
 */
public abstract class BaseHandler extends UserContextService implements IRequestResponseHandler {

   private static final Logger logger = Logger.getLogger(BaseHandler.class);
   private IHandlerRequest     handlerRequest;
   private IHandlerResponse    handlerResponse;

   public IHandlerRequest getHandlerRequest () {
      return handlerRequest;
   }

   public void setHandlerRequest (IHandlerRequest handlerRequest) {
      this.handlerRequest = handlerRequest;
   }

   public IHandlerResponse getHandlerResponse () {
      return handlerResponse;
   }

   public void setHandlerResponse (IHandlerResponse handlerResponse) {
      this.handlerResponse = handlerResponse;
   }

   public Response processRequest (Object request) throws HandlerException {
      Response response = new Response();
      try {
         Object inputBean = handlerRequest.transformRequest(request);
         Object outputBean = process(inputBean);
         Object responseObject = handlerResponse.transformResponse(outputBean);
         response.setObject(responseObject);
      } catch (HandlerRequestTransformException handlerRequestTransformException) {
         logger.error("HandlerRequestTransformException in BaseHandler", handlerRequestTransformException);
         logger.error("Actual Error : [" + handlerRequestTransformException.getCode() + "] "
                  + handlerRequestTransformException.getMessage());
         logger.error("Cause : " + handlerRequestTransformException.getCause());
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, handlerRequestTransformException
                  .getCause());
      } catch (HandlerResponseTransformException handlerResponseTransformException) {
         logger.error("HandlerRequestTransformException in BaseHandler", handlerResponseTransformException);
         logger.error("Actual Error : [" + handlerResponseTransformException.getCode() + "] "
                  + handlerResponseTransformException.getMessage());
         logger.error("Cause : " + handlerResponseTransformException.getCause());
         throw new HandlerException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, handlerResponseTransformException
                  .getCause());
      }
      return response;
   }

   public abstract Object process (Object inputBean) throws HandlerException;
}
