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
 * This bean contains the information from each step of cube creation so that other steps can use it.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class CubeCreationOutputInfo {

   private CubeCreationInputInfo     cubeCreationInputInfo;
   private CubePreFactTableStructure cubePreFactTableStructure;
   private CubeFactTableStructure    cubeFactTableStructure;
   private CubeLookupTableStructure  cubeLookupTableStructure;
   private boolean                   creationSuccessful = false;

   public CubeCreationInputInfo getCubeCreationInputInfo () {
      return cubeCreationInputInfo;
   }

   public void setCubeCreationInputInfo (CubeCreationInputInfo cubeCreationInputInfo) {
      this.cubeCreationInputInfo = cubeCreationInputInfo;
   }

   public boolean isCreationSuccessful () {
      return creationSuccessful;
   }

   public void setCreationSuccessful (boolean creationSuccessful) {
      this.creationSuccessful = creationSuccessful;
   }

   public CubePreFactTableStructure getCubePreFactTableStructure () {
      return cubePreFactTableStructure;
   }

   public void setCubePreFactTableStructure (CubePreFactTableStructure cubePreFactTableStructure) {
      this.cubePreFactTableStructure = cubePreFactTableStructure;
   }

   public CubeFactTableStructure getCubeFactTableStructure () {
      return cubeFactTableStructure;
   }

   public void setCubeFactTableStructure (CubeFactTableStructure cubeFactTableStructure) {
      this.cubeFactTableStructure = cubeFactTableStructure;
   }

   public CubeLookupTableStructure getCubeLookupTableStructure () {
      return cubeLookupTableStructure;
   }

   public void setCubeLookupTableStructure (CubeLookupTableStructure cubeLookupTableStructure) {
      this.cubeLookupTableStructure = cubeLookupTableStructure;
   }

}
