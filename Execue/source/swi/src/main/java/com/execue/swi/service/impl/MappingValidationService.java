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


package com.execue.swi.service.impl;

import com.execue.core.common.bean.mapping.MemberMapping;
import com.execue.swi.exception.MappingException;
import com.execue.swi.service.IMappingValidationService;

/**
 * @author John Mallavalli
 */
public class MappingValidationService implements IMappingValidationService {

   public String validateMemberMapping (MemberMapping saveMapping) throws MappingException {
      // member mapping object will have the mapping type(Existing | Suggested), the BED inside the mapping will have
      // the updated display name of the instance set into the instance object found in the BED

      // Rule : No two member mappings can have the same instance(check via display name)
      String statusMessage = "SUCCESS";
      return statusMessage;
   }
}
