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


package com.execue.core.common.bean.entity;

/**
 * @author anujit
 * @since Jan 26, 2010 - 11:11:26 AM
 */

public class InstanceTripleData {

   protected Long   instanceTripleID;
   protected String sourceInstanceName;
   protected String sourceConceptName;
   protected String relationName;
   protected String destinationInstanceName;
   protected String destinationConceptName;
   protected Long   sourceBEID;
   protected Long   relationBEID;
   protected Long   destinationBEID;

   public Long getInstanceTripleID () {
      return instanceTripleID;
   }

   public void setInstanceTripleID (Long instanceTripleID) {
      this.instanceTripleID = instanceTripleID;
   }

   public String getSourceInstanceName () {
      return sourceInstanceName;
   }

   public void setSourceInstanceName (String sourceInstanceName) {
      this.sourceInstanceName = sourceInstanceName;
   }

   public String getRelationName () {
      return relationName;
   }

   public void setRelationName (String relationName) {
      this.relationName = relationName;
   }

   public String getDestinationInstanceName () {
      return destinationInstanceName;
   }

   public void setDestinationInstanceName (String destinationInstanceName) {
      this.destinationInstanceName = destinationInstanceName;
   }

   public Long getSourceBEID () {
      return sourceBEID;
   }

   public void setSourceBEID (Long sourceBEID) {
      this.sourceBEID = sourceBEID;
   }

   public Long getRelationBEID () {
      return relationBEID;
   }

   public void setRelationBEID (Long relationBEID) {
      this.relationBEID = relationBEID;
   }

   public Long getDestinationBEID () {
      return destinationBEID;
   }

   public void setDestinationBEID (Long destinationBEID) {
      this.destinationBEID = destinationBEID;
   }

   /**
    * @return the sourceConceptName
    */
   public String getSourceConceptName () {
      return sourceConceptName;
   }

   /**
    * @param sourceConceptName
    *           the sourceConceptName to set
    */
   public void setSourceConceptName (String sourceConceptName) {
      this.sourceConceptName = sourceConceptName;
   }

   /**
    * @return the destinationConceptName
    */
   public String getDestinationConceptName () {
      return destinationConceptName;
   }

   /**
    * @param destinationConceptName
    *           the destinationConceptName to set
    */
   public void setDestinationConceptName (String destinationConceptName) {
      this.destinationConceptName = destinationConceptName;
   }
}
