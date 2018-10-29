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


package com.execue.qi;

import com.execue.core.common.bean.QueryForm;
import com.execue.core.common.bean.qi.QIBusinessTerm;
import com.execue.core.common.bean.qi.QICondition;
import com.execue.core.common.bean.qi.QIConditionLHS;
import com.execue.core.common.bean.qi.QIConditionRHS;
import com.execue.core.common.bean.qi.QIOrderBy;
import com.execue.core.common.bean.qi.QISelect;
import com.execue.core.common.bean.qi.QISubQuery;
import com.execue.core.common.bean.qi.QIValue;
import com.execue.core.exception.HandlerRequestTransformException;
import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author jitendra
 *
 */
public abstract class QueryInterfaceCommonBaseTest extends QueryInterfaceBaseTest {

   public QueryForm generatQueryFormFromXML (String xmlRequest) throws HandlerRequestTransformException {
      QueryForm queryForm = null;
      XStream xstream = new XStream();
      xstream.alias("qf", QueryForm.class);
      xstream.alias("select", QISelect.class);
      xstream.alias("term", QIBusinessTerm.class);
      xstream.alias("condition", QICondition.class);
      xstream.alias("lhsBusinessTerm", QIConditionLHS.class);
      xstream.alias("rhsValue", QIConditionRHS.class);
      xstream.alias("qiValue", QIValue.class);
      xstream.alias("query", QISubQuery.class);
      xstream.alias("value", String.class);
      xstream.alias("orderBy", QIOrderBy.class);
      try {
         queryForm = (QueryForm) xstream.fromXML(xmlRequest);
      } catch (Exception e) {
         e.printStackTrace();
      }
      return queryForm;
   }

}
