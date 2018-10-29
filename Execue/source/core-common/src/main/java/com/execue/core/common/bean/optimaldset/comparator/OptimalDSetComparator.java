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


package com.execue.core.common.bean.optimaldset.comparator;

import java.util.Comparator;

import com.execue.core.common.bean.optimaldset.OptimalDSet;

public class OptimalDSetComparator implements Comparator<OptimalDSet> {
	
	    boolean compareUsagePercentage = false;
	    boolean compareCost = false;
	    
	    public OptimalDSetComparator(boolean usage, boolean compareCost){
	    	this.compareUsagePercentage=usage;
	    	this.compareCost=compareCost;
	    	
	    }
	    public int compare(OptimalDSet set1, OptimalDSet set2){
	    	if(compareUsagePercentage)
	    		return compareUsage(set1,set2);
	    	else
	    		return compareCost(set1,set2);
	    }
	

		protected int compareCost(OptimalDSet set1, OptimalDSet set2){
			if (set1.getCost() == set2.getCost()) {
				return 0;
			}
			return set1.getCost() > set2.getCost() ? -1 : 1;
			
		}
		protected int compareUsage(OptimalDSet set1, OptimalDSet set2) {
			
			if (set1.getUsagePercentage() == set2.getUsagePercentage()) {
				return 0;
			}
			return set1.getUsagePercentage() > set2.getUsagePercentage() ? -1 : 1;
		}
		
	
	

}
