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


package com.execue.ks.bean;

import com.execue.core.common.bean.kb.DataConnectionEntity;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 4:19:58 PM
 */
public class KBConnectionEntity extends DataConnectionEntity {
   private String url;

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
