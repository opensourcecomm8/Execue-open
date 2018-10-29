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


package com.execue.message;

import com.execue.core.common.type.MessageStatusType;
import com.execue.message.bean.MessageInfo;
import com.execue.message.bean.MessageInput;
import com.execue.message.exception.MessageException;

public abstract class AbstractSequenceMessage implements IMessage {

   public abstract void process (Object o) throws MessageException;

   public MessageInfo processMessage (MessageInput input) throws MessageException {
      process(input.getObject());
      MessageInfo message = new MessageInfo();
      message.setId(0);
      message.setStatus(MessageStatusType.COMPLETED);
      return message;
   }
}
