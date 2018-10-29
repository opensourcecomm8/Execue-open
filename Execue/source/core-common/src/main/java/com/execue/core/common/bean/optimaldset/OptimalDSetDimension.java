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


package com.execue.core.common.bean.optimaldset;

/**
 * This bean represents the OptimalDSet dimension
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/06/09
 */
public class OptimalDSetDimension {

   protected String name;
   protected long   noOfMembers;

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public long getNoOfMembers () {
      return noOfMembers;
   }

   public void setNoOfMembers (long noOfMembers) {
      this.noOfMembers = noOfMembers;
   }

   @Override
   public boolean equals (Object object) {
      OptimalDSetDimension dimension = (OptimalDSetDimension) object;
      return noOfMembers == dimension.noOfMembers && name.equals(dimension.name);
   }

   @Override
   public int hashCode () {
      return 31 + name.hashCode() + ((Long) noOfMembers).hashCode();
   }

}
