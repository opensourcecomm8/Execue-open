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


/**
 *
 */
package com.execue.nlp.bean.entity;

/**
 * @author Nihar
 */
public class ClusterInformation {

   @Override
   protected Object clone () throws CloneNotSupportedException {
      ClusterInformation toBeClonedClusterInformation = this;
      ClusterInformation clonedClusterInformation = new ClusterInformation();
      clonedClusterInformation.setClusterEndPosition(toBeClonedClusterInformation.getClusterEndPosition());
      clonedClusterInformation.setClusterId(toBeClonedClusterInformation.getClusterId());
      clonedClusterInformation.setClusterStartPosition(toBeClonedClusterInformation.getClusterStartPosition());
      return clonedClusterInformation;
   }

   private Integer clusterId;
   private Integer clusterStartPosition;
   private Integer clusterEndPosition;

   /**
    * @return the clusterId
    */
   public Integer getClusterId () {
      return clusterId;
   }

   /**
    * @param clusterId
    *           the clusterId to set
    */
   public void setClusterId (Integer clusterId) {
      this.clusterId = clusterId;
   }

   /**
    * @return the clusterStartPosition
    */
   public Integer getClusterStartPosition () {
      return clusterStartPosition;
   }

   /**
    * @param clusterStartPosition
    *           the clusterStartPosition to set
    */
   public void setClusterStartPosition (Integer clusterStartPosition) {
      this.clusterStartPosition = clusterStartPosition;
   }

   /**
    * @return the clusterEndPosition
    */
   public Integer getClusterEndPosition () {
      return clusterEndPosition;
   }

   /**
    * @param clusterEndPosition
    *           the clusterEndPosition to set
    */
   public void setClusterEndPosition (Integer clusterEndPosition) {
      this.clusterEndPosition = clusterEndPosition;
   }

}
