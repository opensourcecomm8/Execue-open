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


/**
 * 
 */
package com.execue.sforce.access;

import com.execue.sforce.bean.SforceUserInfo;
import com.execue.sforce.bean.SforceUserProfileInfo;
import com.execue.sforce.exception.SforceException;


/**
 * This interface is for the BiFactor Access from salesforce 
 * 
 * @author Nitesh
 *
 */
public interface ISforceAccessService {
   
   public SforceUserInfo getSforceUserInfo(String partnerSessionId, String partnerSessionURL) throws SforceException;   
   public SforceUserProfileInfo getSforceUserProfileInfo(String partnerSessionId, String partnerSessionURL, String userId) throws SforceException;   
   
}
