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

import com.execue.core.common.type.BusinessEntityType;

/**
 * @author kaliki
 */
public class QIBusinessTerm implements IQIComponent {

   // TODO : -VG- need to get the ded id from query form itself
   private String             name;
   private String             displayName;
   private BusinessEntityType type;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public BusinessEntityType getType () {
      return type;
   }

   public void setType (BusinessEntityType type) {
      this.type = type;
   }

   public String getDisplayName () {
      return displayName;
   }

   public void setDisplayName (String displayName) {
      this.displayName = displayName;
   }
}
