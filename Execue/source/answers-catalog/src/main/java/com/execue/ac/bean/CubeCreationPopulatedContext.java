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


package com.execue.ac.bean;

import java.util.List;

import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.Application;
import com.execue.core.common.bean.entity.Model;
import com.execue.core.common.bean.security.User;

/**
 * This bean contains the populated context information for cube
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeCreationPopulatedContext extends CubeCreationContext {

   private Application                application;
   private Model                      model;
   private User                       user;
   private List<ConceptColumnMapping> populatedFrequencyMeasures;
   private List<ConceptColumnMapping> populatedSimpleLookupDimensions;
   private List<ConceptColumnMapping> populatedRangeLookupDimensions;
   private List<ConceptColumnMapping> populatedMeasures;

   public List<ConceptColumnMapping> getPopulatedFrequencyMeasures () {
      return populatedFrequencyMeasures;
   }

   public void setPopulatedFrequencyMeasures (List<ConceptColumnMapping> populatedFrequencyMeasures) {
      this.populatedFrequencyMeasures = populatedFrequencyMeasures;
   }

   public List<ConceptColumnMapping> getPopulatedSimpleLookupDimensions () {
      return populatedSimpleLookupDimensions;
   }

   public void setPopulatedSimpleLookupDimensions (List<ConceptColumnMapping> populatedSimpleLookupDimensions) {
      this.populatedSimpleLookupDimensions = populatedSimpleLookupDimensions;
   }

   public List<ConceptColumnMapping> getPopulatedRangeLookupDimensions () {
      return populatedRangeLookupDimensions;
   }

   public void setPopulatedRangeLookupDimensions (List<ConceptColumnMapping> populatedRangeLookupDimensions) {
      this.populatedRangeLookupDimensions = populatedRangeLookupDimensions;
   }

   public List<ConceptColumnMapping> getPopulatedMeasures () {
      return populatedMeasures;
   }

   public void setPopulatedMeasures (List<ConceptColumnMapping> populatedMeasures) {
      this.populatedMeasures = populatedMeasures;
   }

   public Application getApplication () {
      return application;
   }

   public void setApplication (Application application) {
      this.application = application;
   }

   public Model getModel () {
      return model;
   }

   public void setModel (Model model) {
      this.model = model;
   }

   public User getUser () {
      return user;
   }

   public void setUser (User user) {
      this.user = user;
   }
}
