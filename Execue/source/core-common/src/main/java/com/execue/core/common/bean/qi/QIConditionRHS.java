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

import java.util.List;

/**
 * @author kaliki
 * @since 4.0
 */
public class QIConditionRHS {

   private QISubQuery           query;
   private List<QIBusinessTerm> terms;
   private List<QIValue>         values;

   public QISubQuery getQuery () {
      return query;
   }

   public void setQuery (QISubQuery query) {
      this.query = query;
   }

   public List<QIValue> getValues () {
      return values;
   }

   public void setValues (List<QIValue> values) {
      this.values = values;
   }

   public List<QIBusinessTerm> getTerms () {
      return terms;
   }

   public void setTerms (List<QIBusinessTerm> terms) {
      this.terms = terms;
   }

}
