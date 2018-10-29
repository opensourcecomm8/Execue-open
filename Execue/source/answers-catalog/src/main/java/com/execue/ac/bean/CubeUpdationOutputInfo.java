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

/**
 * This bean contains the information from each step of cube updation so that other steps can use it.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeUpdationOutputInfo {

   private CubeUpdationInputInfo           cubeUpdationInputInfo;
   private CubeUpdatePreFactTableStructure cubeUpdatePreFactTableStructure;
   private CubeFactTableStructure          cubeFactTableStructure;
   private boolean                         updationSuccessful = false;
   private String                          failureReason;

   public CubeUpdationInputInfo getCubeUpdationInputInfo () {
      return cubeUpdationInputInfo;
   }

   public void setCubeUpdationInputInfo (CubeUpdationInputInfo cubeUpdationInputInfo) {
      this.cubeUpdationInputInfo = cubeUpdationInputInfo;
   }

   public boolean isUpdationSuccessful () {
      return updationSuccessful;
   }

   public void setUpdationSuccessful (boolean updationSuccessful) {
      this.updationSuccessful = updationSuccessful;
   }

   public String getFailureReason () {
      return failureReason;
   }

   public void setFailureReason (String failureReason) {
      this.failureReason = failureReason;
   }

   public CubeUpdatePreFactTableStructure getCubeUpdatePreFactTableStructure () {
      return cubeUpdatePreFactTableStructure;
   }

   public void setCubeUpdatePreFactTableStructure (CubeUpdatePreFactTableStructure cubeUpdatePreFactTableStructure) {
      this.cubeUpdatePreFactTableStructure = cubeUpdatePreFactTableStructure;
   }

   public CubeFactTableStructure getCubeFactTableStructure () {
      return cubeFactTableStructure;
   }

   public void setCubeFactTableStructure (CubeFactTableStructure cubeFactTableStructure) {
      this.cubeFactTableStructure = cubeFactTableStructure;
   }

}
