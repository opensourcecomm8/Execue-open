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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * This bean represents the optimalDSet object
 * 
 * @author Vishay
 * @version 1.0
 * @since 27/06/09
 */
public class OptimalDSet {

   private static Logger               log = Logger.getLogger(OptimalDSet.class);
   protected String                    name;
   protected Set<OptimalDSetDimension> dimensions;
   protected String                    dimensionNames;
   protected double                    usagePercentage;

   public OptimalDSet () {

   }

   public OptimalDSet (String name, Collection<OptimalDSetDimension> dimensions, double usagePercentage) {
      this.name = name;
      this.dimensions = new HashSet<OptimalDSetDimension>();
      this.dimensions.addAll(dimensions);
      this.usagePercentage = usagePercentage;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public Set<OptimalDSetDimension> getDimensions () {
      return dimensions;
   }

   public void setDimensions (Collection<OptimalDSetDimension> dimensions) {
      this.dimensions = new HashSet<OptimalDSetDimension>();
      this.dimensions.addAll(dimensions);
   }

   public void setDimensions (Set<OptimalDSetDimension> dimensions) {
      this.dimensions = dimensions;
   }

   public double getUsagePercentage () {
      return usagePercentage;
   }

   public void setUsagePercentage (double usagePercentage) {
      this.usagePercentage = usagePercentage;
   }

   public String getDimensionNames () {
      return dimensionNames;
   }

   public void setDimensionNames (String dimensionNames) {
      this.dimensionNames = dimensionNames;
   }

   public double getCost () {
      double cost = 1.0;
      for (OptimalDSetDimension dimension : dimensions) {
         cost *= dimension.getNoOfMembers();
      }
      return cost;
   }

   public boolean isSubSet (OptimalDSet set) {
      if ((this.dimensions != null) && (set != null) && (set.getDimensions() != null)
               && (this.dimensions.size() > set.getDimensions().size())) {
         boolean isSubSet = this.dimensions.containsAll(set.getDimensions());
         log.debug("returning " + isSubSet + " for " + this + "," + set);
         return isSubSet;

      }
      return false;
   }

   @Override
   public String toString () {
      StringBuffer buff = new StringBuffer();
      buff.append("name =" + name == null ? "" : name).append(",");
      buff.append("usagePercentage =" + usagePercentage).append(",");
      if (this.dimensions != null) {
         buff.append("dimensions ={");
         for (Iterator<OptimalDSetDimension> iter = this.dimensions.iterator(); iter.hasNext();) {
            buff.append(iter.next().getName());
            if (iter.hasNext()) {
               buff.append(",");
            }
         }
         buff.append("}");
         buff.append("\n");

      }
      return buff.toString();

   }

   @Override
   public boolean equals (Object object) {
      OptimalDSet dset = (OptimalDSet) object;
      if (dset.getDimensions().size() != this.getDimensions().size()) {
         return false;
      }
      Set<OptimalDSetDimension> dimensions = dset.getDimensions();
      return this.dimensions.containsAll(dimensions);
   }

   @Override
   public int hashCode () {
      return 31 + this.getDimensions().hashCode();
   }

}
