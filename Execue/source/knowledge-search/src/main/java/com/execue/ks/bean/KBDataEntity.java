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

import com.execue.core.common.bean.kb.DataEntity;

/**
 * @author Abhijit
 * @since Jul 7, 2009 : 4:19:15 PM
 */
public class KBDataEntity extends DataEntity {

   private Long   contentID;
   private String contentSource;

   /**
    * @return the contentID
    */
   public Long getContentID () {
      return contentID;
   }

   /**
    * @param contentID
    *           the contentID to set
    */
   public void setContentID (Long contentID) {
      this.contentID = contentID;
   }

   /**
    * @return the contentSource
    */
   public String getContentSource () {
      return contentSource;
   }

   /**
    * @param contentSource
    *           the contentSource to set
    */
   public void setContentSource (String contentSource) {
      this.contentSource = contentSource;
   }

}
