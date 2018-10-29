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


package com.execue.nlp.bean.rules.timeframe;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.algorithm.BaseBean;

public class StaticTimeFormula extends BaseBean {

   private String              name;
   private List<String>        synonyms;
   private Map<String, String> unitValueMap;

   /**
    * @return Returns the name.
    */
   public String getName () {
      return name;
   }

   /**
    * @param name
    *           The name to set.
    */
   public void setName (String name) {
      this.name = name;
   }

   /**
    * @return Returns the synonyms.
    */
   public List<String> getSynonyms () {
      return synonyms;
   }

   /**
    * @param synonyms
    *           The synonyms to set.
    */
   public void setSynonyms (List<String> synonyms) {
      this.synonyms = synonyms;
   }

   /**
    * @return Returns the unitValueMap.
    */
   public Map<String, String> getUnitValueMap () {
      return unitValueMap;
   }

   /**
    * @param unitValueMap
    *           The unitValueMap to set.
    */
   public void setUnitValueMap (Map<String, String> unitValueMap) {
      this.unitValueMap = unitValueMap;
   }

   public boolean nameMatches (String inName) {
      boolean matches = false;
      if (inName.equals(getName())) {
         matches = true;
      } else if (getSynonyms().contains(inName)) {
         matches = true;
      }
      return matches;
   }

   public String getUnitValue (String unitName) {
      String unitValue = null;
      Object ref = getUnitValueMap().get(unitName.toUpperCase());
      if (ref != null) {
         unitValue = (String) ref;
      }
      return unitValue;
   }
}
