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


/**
 * 
 */
package com.execue.core.extension.hibernate.dialect;

import org.hibernate.dialect.Oracle10gDialect;

/**
 * @author Nitesh
 *
 */
public class ExeCueOracle10gDialect extends Oracle10gDialect {

   /* (non-Javadoc)
    * @see org.hibernate.dialect.Oracle8iDialect#getCrossJoinSeparator()
    */
   @Override
   public String getCrossJoinSeparator () {
      // NOTE: We do no need this method here, as its already taken care in above dialect. 
      // Just for consistency over-riding the method because in dialect of some other db vendors 
      // it is returning "Cross Join"
      return ", ";
   }
}