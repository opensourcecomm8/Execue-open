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


package com.execue.core.common.bean.qi;

import java.io.Serializable;
import java.util.List;

import com.execue.core.common.bean.qi.suggest.SuggestionUnit;

/**
 * @author Jtiwari
 * @since 03/07/09
 */
public class QIConversion implements Serializable {

   private List<SuggestionUnit> dataFormats;
   private List<SuggestionUnit> units;

   public List<SuggestionUnit> getDataFormats () {
      return dataFormats;
   }

   public void setDataFormats (List<SuggestionUnit> dataFormats) {
      this.dataFormats = dataFormats;
   }

   public List<SuggestionUnit> getUnits () {
      return units;
   }

   public void setUnits (List<SuggestionUnit> units) {
      this.units = units;
   }

}
