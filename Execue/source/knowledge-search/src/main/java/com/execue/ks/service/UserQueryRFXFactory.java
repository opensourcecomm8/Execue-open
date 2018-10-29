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


package com.execue.ks.service;

import com.execue.core.common.bean.qdata.UserQueryReducedFormIndex;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.RFXEntityType;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 12:45:46 PM
 */
public class UserQueryRFXFactory {

   public static UserQueryReducedFormIndex getuserQueryRFXForDE (long queryRFID, long deID, CheckType inferred,
            long order) {
      UserQueryReducedFormIndex userQueryRFX = new UserQueryReducedFormIndex();
      userQueryRFX.setRfId(queryRFID);
      userQueryRFX.setRfxEntityType(RFXEntityType.BUSINESS_ENTITY_DEFINITION);
      userQueryRFX.setOrder(order);
      return userQueryRFX;
   }

   public static UserQueryReducedFormIndex getuserQueryRFXForConceptTriple (long queryRFID, long ctID,
            CheckType inferred, long order) {
      UserQueryReducedFormIndex userQueryRFX = new UserQueryReducedFormIndex();
      userQueryRFX.setRfxEntityType(RFXEntityType.CONCEPT_TRIPLE);
      userQueryRFX.setOrder(order);
      return userQueryRFX;
   }
}
