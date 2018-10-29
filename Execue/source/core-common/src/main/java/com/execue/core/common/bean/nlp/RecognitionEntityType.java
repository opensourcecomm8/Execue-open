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


package com.execue.core.common.bean.nlp;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.execue.core.configuration.EnumLookupHelper;
import com.execue.core.type.IBaseEnumType;

/**
 * User: Abhijit Date: Jul 30, 2008 Time: 3:45:30 PM
 */
public enum RecognitionEntityType implements IBaseEnumType {

   CONCEPT_ENTITY ("com.execue.nlp.bean.entity.ConceptEntity"), INSTANCE_ENTITY (
            "com.execue.nlp.bean.entity.InstanceEntity"), PROPERTY_ENTITY ("com.execue.nlp.bean.entity.PropertyEntity"), SFL_ENTITY (
            "com.execue.nlp.bean.entity.SFLEntity"), PW_ENTITY ("com.execue.nlp.bean.entity.PWEntity"), CANDIDATE_ENTITY (
            "com.execue.nlp.bean.entity.CandidateEntity"), CONCEPT_PROFILE_ENTITY (
            "com.execue.nlp.bean.entity.ConceptProfileEntity"), INSTANCE_PROFILE_ENTITY (
            "com.execue.nlp.bean.entity.InstanceProfileEntity"), GENERAL_ENTITY (
            "com.execue.nlp.bean.entity.RecognitionEntity"), TYPE_ENTITY ("com.execue.nlp.bean.entity.TypeEntity"), INFLECTION_ENTITY (
            "com.execue.nlp.bean.entity.InflectionEntity");

   private String                                          value;
   private static final Map<String, RecognitionEntityType> reverseLookupMap = new HashMap<String, RecognitionEntityType>();
   private static String                                   name             = RecognitionEntityType.class
                                                                                     .getSimpleName();

   static {
      for (RecognitionEntityType entityType : EnumSet.allOf(RecognitionEntityType.class)) {
         reverseLookupMap.put(entityType.value, entityType);
      }
   }

   RecognitionEntityType (String className) {
      this.value = className;
   }

   public String getValue () {
      return this.value;
   }

   public static RecognitionEntityType getRecognitionEntityType (String value) {
      return reverseLookupMap.get(value);
   }

   public String getDescription () {
      return EnumLookupHelper.getEnumLookupDescription(name, String.valueOf(this.value));
   }
}
