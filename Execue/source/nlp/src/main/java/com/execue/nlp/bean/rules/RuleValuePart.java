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


package com.execue.nlp.bean.rules;

import com.execue.core.common.bean.algorithm.BaseBean;

/**
 * @author Abhijit
 * @since Sep 4, 2008 - 5:10:25 PM
 */
public abstract class RuleValuePart extends BaseBean {

   public static final String SEPARATOR  = "SEPARATOR";
   public static final String MULTI_PART = "MULTI-PART";
   public static final String FUNCTION   = "FUNCTION";
   public static final String REGULAR    = "REGULAR";

   private String             partID;

   public String getPartID () {
      return partID;
   }

   public void setPartID (String partID) {
      this.partID = partID;
   }

   public abstract String getType ();
}
