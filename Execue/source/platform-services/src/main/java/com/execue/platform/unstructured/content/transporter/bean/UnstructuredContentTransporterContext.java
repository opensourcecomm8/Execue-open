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


package com.execue.platform.unstructured.content.transporter.bean;

import java.io.Serializable;

import com.execue.core.common.bean.entity.DataSource;

public class UnstructuredContentTransporterContext implements Serializable {

   private DataSource targetWareHouseDataSource;
   private DataSource sourceContentDataSource;
   private String     contentTempTableName;

   // Variables populated by transporter while and after transportation
   private long       numberOfRecordsTransported;

   public DataSource getTargetWareHouseDataSource () {
      return targetWareHouseDataSource;
   }

   public void setTargetWareHouseDataSource (DataSource targetWareHouseDataSource) {
      this.targetWareHouseDataSource = targetWareHouseDataSource;
   }

   public DataSource getSourceContentDataSource () {
      return sourceContentDataSource;
   }

   public void setSourceContentDataSource (DataSource sourceContentDataSource) {
      this.sourceContentDataSource = sourceContentDataSource;
   }

   public String getContentTempTableName () {
      return contentTempTableName;
   }

   public void setContentTempTableName (String contentTempTableName) {
      this.contentTempTableName = contentTempTableName;
   }

   public long getNumberOfRecordsTransported () {
      return numberOfRecordsTransported;
   }

   public void setNumberOfRecordsTransported (long numberOfRecordsTransported) {
      this.numberOfRecordsTransported = numberOfRecordsTransported;
   }

}
