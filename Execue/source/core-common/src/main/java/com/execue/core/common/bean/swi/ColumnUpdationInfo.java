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


package com.execue.core.common.bean.swi;

import java.io.Serializable;

/**
 * This bean represents the colum updation level sync information
 * 
 * @author Vishay
 * @version 1.0
 * @since 20/08/09
 */
public class ColumnUpdationInfo implements Serializable {

   private AssetOperationColumn sourceColumn;
   private AssetOperationColumn swiColumn;
   private boolean              isDescriptionUpdated    = false;
   private boolean              isDataTypeUpdated       = false;
   private boolean              isPrecisionUpdated      = false;
   private boolean              isScaleUpdated          = false;
   private boolean              isRequiredUpdated       = false;
   private boolean              isDefaultValueUpdated   = false;
   private boolean              isConstraintInfoUpdated = false;

   public boolean isDescriptionUpdated () {
      return isDescriptionUpdated;
   }

   public void setDescriptionUpdated (boolean isDescriptionUpdated) {
      this.isDescriptionUpdated = isDescriptionUpdated;
   }

   public boolean isDataTypeUpdated () {
      return isDataTypeUpdated;
   }

   public void setDataTypeUpdated (boolean isDataTypeUpdated) {
      this.isDataTypeUpdated = isDataTypeUpdated;
   }

   public boolean isPrecisionUpdated () {
      return isPrecisionUpdated;
   }

   public void setPrecisionUpdated (boolean isPrecisionUpdated) {
      this.isPrecisionUpdated = isPrecisionUpdated;
   }

   public boolean isScaleUpdated () {
      return isScaleUpdated;
   }

   public void setScaleUpdated (boolean isScaleUpdated) {
      this.isScaleUpdated = isScaleUpdated;
   }

   public boolean isRequiredUpdated () {
      return isRequiredUpdated;
   }

   public void setRequiredUpdated (boolean isRequiredUpdated) {
      this.isRequiredUpdated = isRequiredUpdated;
   }

   public boolean isDefaultValueUpdated () {
      return isDefaultValueUpdated;
   }

   public void setDefaultValueUpdated (boolean isDefaultValueUpdated) {
      this.isDefaultValueUpdated = isDefaultValueUpdated;
   }

   public boolean isConstraintInfoUpdated () {
      return isConstraintInfoUpdated;
   }

   public void setConstraintInfoUpdated (boolean isConstraintInfoUpdated) {
      this.isConstraintInfoUpdated = isConstraintInfoUpdated;
   }

   
   public AssetOperationColumn getSourceColumn () {
      return sourceColumn;
   }

   
   public void setSourceColumn (AssetOperationColumn sourceColumn) {
      this.sourceColumn = sourceColumn;
   }

   
   public AssetOperationColumn getSwiColumn () {
      return swiColumn;
   }

   
   public void setSwiColumn (AssetOperationColumn swiColumn) {
      this.swiColumn = swiColumn;
   }
}
