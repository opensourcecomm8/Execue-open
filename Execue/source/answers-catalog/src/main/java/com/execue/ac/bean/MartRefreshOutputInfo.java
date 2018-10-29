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
 * This bean contains the information from each step of cube refresh process so that other steps can use it.
 * 
 * @author Vishay
 * @version 1.0
 * @since 17/02/2011
 */
public class MartRefreshOutputInfo {

   private MartCreationOutputInfo martCreationOutputInfo;
   private boolean                refreshSuccessful = false;

   public boolean isRefreshSuccessful () {
      return refreshSuccessful;
   }

   public void setRefreshSuccessful (boolean refreshSuccessful) {
      this.refreshSuccessful = refreshSuccessful;
   }

   public MartCreationOutputInfo getMartCreationOutputInfo () {
      return martCreationOutputInfo;
   }

   public void setMartCreationOutputInfo (MartCreationOutputInfo martCreationOutputInfo) {
      this.martCreationOutputInfo = martCreationOutputInfo;
   }

}
