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

import com.execue.core.common.type.OrderLimitEntityType;

/**
 * @author John Mallavalli
 * @since 4.0
 */
public class QITopBottom {

   private QIBusinessTerm       term;
   private OrderLimitEntityType type;
   private String               value;

   public QIBusinessTerm getTerm () {
      return term;
   }

   public void setTerm (QIBusinessTerm term) {
      this.term = term;
   }

   public String getValue () {
      return value;
   }

   public void setValue (String value) {
      this.value = value;
   }

   public OrderLimitEntityType getType () {
      return type;
   }

   public void setType (OrderLimitEntityType type) {
      this.type = type;
   }
}