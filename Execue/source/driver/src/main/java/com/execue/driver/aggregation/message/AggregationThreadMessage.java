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


package com.execue.driver.aggregation.message;

import com.execue.core.common.bean.AggregationMessage;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.message.AbstractThreadMessage;
import com.execue.message.exception.MessageException;

public class AggregationThreadMessage extends AbstractThreadMessage {

   AggregationMessageProcessor messageProcessor;

   public void setMessageProcessor (AggregationMessageProcessor messageProcessor) {
      this.messageProcessor = messageProcessor;
   }

   @Override
   public void process (Object o) throws MessageException {
      try {
         messageProcessor.processMessage((AggregationMessage) o);
      } catch (MessageException me) {
         throw new MessageException(me.getCode(), me.getCause());
      } catch (Exception e) {
         e.printStackTrace();
         throw new MessageException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, "Error from AggregationThreadMessage",
                  e.getCause());
      }
   }
}