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


package com.execue.platform.impl;

import org.apache.commons.lang.RandomStringUtils;

import com.execue.core.exception.UidException;
import com.execue.platform.IUidService;

/**
 * Provides an id value to use as Knowledge Id.
 * 
 * @author execue
 */
public class KnowledgeIdGenerationServiceImpl implements IUidService {

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.IUidService#getNextId()
    */
   public long getNextId () throws UidException {
      return Long.parseLong(System.currentTimeMillis() + "" + RandomStringUtils.randomNumeric(3));
   }
}
