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


package com.execue.core.common.bean.qi.suggest;

import com.execue.core.common.type.SuggestTermType;

/**
 * @author kaliki
 * @since 4.0
 */
public class SuggestTerm {

   private long            id;
   private String          name;
   private String          displayName;
   private SuggestTermType type;

   public long getId () {
      return id;
   }

   public void setId (long id) {
      this.id = id;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }

   public SuggestTermType getType () {
      return type;
   }

   public void setType (SuggestTermType type) {
      this.type = type;
   }
}