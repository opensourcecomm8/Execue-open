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

import java.util.List;

import com.execue.core.common.bean.algorithm.BaseBean;

public class RulePlaceValue extends BaseBean {

   /**
    *
    */
   private static final long           serialVersionUID = 1597709856908201728L;

   private List<RuleValueFunctionPart> valuePartList;
   private List<String>                connectorList;

   /**
    * @return Returns the connectorList.
    */
   public List<String> getConnectorList () {
      return connectorList;
   }

   /**
    * @param connectorList
    *           The connectorList to set.
    */
   public void setConnectorList (List<String> connectorList) {
      this.connectorList = connectorList;
   }

   /**
    * @return Returns the valuePartList.
    */
   public List<RuleValueFunctionPart> getValuePartList () {
      return valuePartList;
   }

   /**
    * @param valuePartList
    *           The valuePartList to set.
    */
   public void setValuePartList (List<RuleValueFunctionPart> valuePartList) {
      this.valuePartList = valuePartList;
   }
}
