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


package com.execue.core.common.bean.publisher;

import java.util.List;

/**
 * This bean represents the DbTable data normalized information
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class DBTableDataNormalizedInfo extends DBTableNormalizedInfo {

   private List<List<Object>> normalizedDataPoints;

   public List<List<Object>> getNormalizedDataPoints () {
      return normalizedDataPoints;
   }

   public void setNormalizedDataPoints (List<List<Object>> normalizedDataPoints) {
      this.normalizedDataPoints = normalizedDataPoints;
   }

}