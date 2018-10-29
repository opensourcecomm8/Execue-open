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


package com.execue.util;

import java.util.List;

import org.junit.Test;

public class MathUtilTest {

	@Test
	public void testUnweighted() {
		double[] input = new double[] {60,60,60,60,90,90,100,100};
		double p = 50;
		List<PercentileItem> out = MathUtil.getPercentileValues(input, p, false /*isWeighted*/, true /*isTop*/);
		
		print("Unweighted", p, out);
	}
	
	@Test
	public void testWeightedRepeatedValues() {
		double[] input = new double[] {60, 60,60,60,100,100,100,100};
		double p = 90;
		List<PercentileItem> out = MathUtil.getPercentileValues(input, p, true /*isWeighted*/, true /*isTop*/);
		
		print("WeightedRepeatedValues", p, out);

	}
	
	@Test
	public void testWeightedFewValues() {
		double[] input = new double[] {60, 90};
		double p = 90;
		List<PercentileItem> out = MathUtil.getPercentileValues(input, p, true /*isWeighted*/, true /*isTop*/);
		
		print("WeightedFewValues", p, out);

	}
	
	@Test
	public void testWeightedManyValues() {
		double[] input = new double[] {10,20,30,40,50,60,70,80,90,100};
		double p = 90;
		List<PercentileItem> out = MathUtil.getPercentileValues(input, p, true /*isWeighted*/, true /*isTop*/);
		
		print("WeightedManyValues",p, out);

	}
	
	@Test
	public void testShree1Values() {
		double[] input = new double[] {15,25,35};
		double p = 90;
		List<PercentileItem> out = MathUtil.getPercentileValues(input, p, true /*isWeighted*/, true /*isTop*/);
		
		print("Shree1",p, out);

	}
	
	void print(String testcase, double p, List<PercentileItem> list) {
		System.out.print(testcase+" above "+p+" percentile: ");
		for (PercentileItem x:list) {
			System.out.print(x.value+" ("+x.getPercentile()+"%) | ");
		}
		System.out.println();
	}

}
