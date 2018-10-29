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


package com.execue.web.sysconfig.bean;

import java.io.Serializable;
import java.util.List;

public class ReportPresentationConfigurationViewInfo implements Serializable {

   private List<ConfigurationProperty> readOnlyConfigurations;

   private ConfigurationProperty       axisLabelMaxLength;

   public List<ConfigurationProperty> getReadOnlyConfigurations () {
      return readOnlyConfigurations;
   }

   public void setReadOnlyConfigurations (List<ConfigurationProperty> readOnlyConfigurations) {
      this.readOnlyConfigurations = readOnlyConfigurations;
   }

   public ConfigurationProperty getAxisLabelMaxLength () {
      return axisLabelMaxLength;
   }

   public void setAxisLabelMaxLength (ConfigurationProperty axisLabelMaxLength) {
      this.axisLabelMaxLength = axisLabelMaxLength;
   }

}