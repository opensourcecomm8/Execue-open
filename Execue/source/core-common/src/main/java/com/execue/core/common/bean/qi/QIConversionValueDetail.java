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

/**
 * @author Jtiwari
 * @since 30/07/2009
 */

public class QIConversionValueDetail {

   private String       sampleValue;
   private QIConversion qiConversion;

   public String getSampleValue () {
      return sampleValue;
   }

   public void setSampleValue (String sampleValue) {
      this.sampleValue = sampleValue;
   }

   public QIConversion getQiConversion () {
      return qiConversion;
   }

   public void setQiConversion (QIConversion qiConversion) {
      this.qiConversion = qiConversion;
   }

}
