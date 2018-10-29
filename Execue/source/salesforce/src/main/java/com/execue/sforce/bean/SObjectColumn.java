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


package com.execue.sforce.bean;

/**
 * This bean reprsents the Sobject column. Digits denote the length when soap type is integer, length denotes when soap
 * type is string or id. if soap type is double then precision and scale denotes the length
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/08/09
 */
public class SObjectColumn {

   private String columnName;
   private String soapType;
   private String digits;
   private String length;
   private String precision;
   private String scale;
   private String nullable;
   private String unique;

   public String getColumnName () {
      return columnName;
   }

   public void setColumnName (String columnName) {
      this.columnName = columnName;
   }

   public String getSoapType () {
      return soapType;
   }

   public void setSoapType (String soapType) {
      this.soapType = soapType;
   }

   public String getLength () {
      return length;
   }

   public void setLength (String length) {
      this.length = length;
   }

   public String getPrecision () {
      return precision;
   }

   public void setPrecision (String precision) {
      this.precision = precision;
   }

   public String getScale () {
      return scale;
   }

   public void setScale (String scale) {
      this.scale = scale;
   }

   public String getNullable () {
      return nullable;
   }

   public void setNullable (String nullable) {
      this.nullable = nullable;
   }

   public String getDigits () {
      return digits;
   }

   public void setDigits (String digits) {
      this.digits = digits;
   }

   
   public String getUnique () {
      return unique;
   }

   
   public void setUnique (String unique) {
      this.unique = unique;
   }
}
