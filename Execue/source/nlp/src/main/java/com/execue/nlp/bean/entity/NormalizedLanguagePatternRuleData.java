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


package com.execue.nlp.bean.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.AbstractNormalizedData;
import com.execue.core.common.bean.entity.NormalizedDataType;

/**
 * @author Nitesh
 */
public class NormalizedLanguagePatternRuleData extends AbstractNormalizedData {

   private String                                            value;
   private Map<List<Integer>, LanguagePatternRecognizedPart> recognizedDisplayNameMap;

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getValue()
    */
   public String getValue () {
      return value;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getType()
    */
   public String getType () {
      return null;
   }

   /*
    * (non-Javadoc)
    * 
    * @see com.execue.core.common.bean.INormalizedData#getTypeBedId()
    */
   public Long getTypeBedId () {
      return null;
   }

   /**
    * @param value
    *           the value to set
    */
   public void setValue (String value) {
      this.value = value;
   }

   public Map<List<Integer>, LanguagePatternRecognizedPart> getRecognizedDisplayNameMap () {
      if (recognizedDisplayNameMap == null) {
         recognizedDisplayNameMap = new HashMap<List<Integer>, LanguagePatternRecognizedPart>();
      }
      return recognizedDisplayNameMap;
   }

   public void setRecognizedDisplayNameMap (Map<List<Integer>, LanguagePatternRecognizedPart> recognizedDisplayNameMap) {
      this.recognizedDisplayNameMap = recognizedDisplayNameMap;
   }

   public NormalizedDataType getNormalizedDataType () {
      // TODO Auto-generated method stub
      return null;
   }

   public String getDisplayValue () {
      // TODO Auto-generated method stub
      return null;
   }
}
