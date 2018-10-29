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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;



public class MathUtilClusterTest {

	@Test
	public void testPossibilitySFLTestValues() {
		Double[] input = new Double[]{14.385, 14.385, 16.6375, 16.6375};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void testPossibility12Values() {
		Double[] input = new Double[]{1.0, 1.1};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void testPossibility13Values() {
		Double[] input = new Double[]{1.0, 1.2};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void testPossibility110Values() {
		Double[] input = new Double[]{1.0, 1.3};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void testPossibility1100Values() {
		Double[] input = new Double[]{1.0, 1.4};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void testPossibility11000Values() {
		Double[] input = new Double[]{1.0, 1.5};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void testPossibilityTwoValues() {
		Double[] input = new Double[]{5.325, 5.325, 5.325, 7.125};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		runAll(inputlist);
	}
	
	@Test
	public void testPossibilityValues() {
		Double[] input = new Double[]{12.66, 12.77, 12.88};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		runAll(inputlist);
	}
	
	@Test
	public void testOneSFLValues() {
		Double[] input = new Double[]{33.33};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		runAll(inputlist);
	}
	
	@Test
	public void test1SFLVales() {
		Double[] input = new Double[]{45.0,90.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void test2SFLVales() {
		Double[] input = new Double[]{95.0,97.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void test3SFLVales() {
		Double[] input = new Double[]{15.0,15.0,17.0};

		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void test4SFLVales() {
		Double[] input = new Double[]{50.0, 50.0, 25.0, 20.0, 33.0, 65.0, 34.0, 33.0, 33.0, 34.0, 34.0, 20.0, 34.0, 33.0, 50.0, 70.0, 33.0, 20.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void test5SFLVales() {
		Double[] input = new Double[]{34.0, 33.0, 33.0, 20.0, 34.0, 25.0, 34.0, 33.0, 50.0, 20.0, 34.0, 50.0, 20.0, 65.0, 50.0, 70.0, 33.0, 33.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		//double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);
	}
	
	@Test
	public void testFiveVales() {
		Double[] input = new Double[]{20.0,30.0,40.0,1000.0,1100.0,1200.0};

		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		run(inputlist);

	}
	
	@Test
	public void testTenVales() {
		Double[] input = new Double[]{1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0};
		//Double[] input = new Double[]{3.0,4.0,6.0,10.0,15.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		run(inputlist);

	}
	
	@Test
	public void testTwoVales() {
		//Double[] input = new Double[]{1.0,1.0,2.0,3.0,3.0,4.0,7.0,9.0,10.0,10.0,14.0};
		Double[] input = new Double[]{3.0,4.0,6.0,10.0,15.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeByExeCueApproach(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		run(inputlist);

	}
	
	@Test
	public void testDistances() {
		//Double[] input = new Double[]{1.0,1.0,2.0,3.0,3.0,4.0,7.0,9.0,10.0,10.0,14.0};
		Double[] input = new Double[]{1.0,10.0,20.0,22.0,25.0,35.0,38.0,42.0,49.0,50.0,51.0,99.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeValuesInBands(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		//print(inputlist, out);
		
		run(inputlist);

	}
	
	@Test
	public void testUnweighted() {
		Double[] input = new Double[]{60.0,60.0,60.0,60.0,90.0,90.0,100.0,100.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 50;
		//Map<Integer, List<Double>> out = MathUtil.distributeValuesInBands(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		//print(inputlist, out);
		
		run(inputlist);

	}
	
	@Test
	public void testWeightedRepeatedValues() {
		Double[] input = new Double[] {60.0, 60.0,60.0,60.0,100.0,100.0,100.0,100.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 90;
		//Map<Integer, List<Double>> out = MathUtil.distributeValuesInBands(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		//print(inputlist, out);
		
		run(inputlist);
	}
	
	@Test
	public void testWeightedFewValues() {
		Double[] input = new Double[] {60.0, 90.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 90;
		//Map<Integer, List<Double>> out = MathUtil.distributeValuesInBands(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		//print(inputlist, out);
		
		run(inputlist);
	}
	
	@Test
	public void testWeightedManyValues() {
		Double[] input = new Double[] {10.0,20.0,30.0,40.0,50.0,60.0,70.0,80.0,90.0,100.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 90;
		//Map<Integer, List<Double>> out = MathUtil.distributeValuesInBands(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		//print(inputlist,out);
		run(inputlist);

	}
	
	@Test
	public void testShree1Values() {
		Double[] input = new Double[]  {15.0,25.0,35.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 90;
		//Map<Integer, List<Double>> out = MathUtil.distributeValuesInBands(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		//print(inputlist, out);

		run(inputlist);

	}
	
	@Test
	public void testShree2Values() {
		Double[] input = new Double[]  {8.0,10.0,12.0,34.0,35.0};
		List<Double> inputlist = (List<Double>) Arrays.asList(input);
		double p = 90;
		//Map<Integer, List<Double>> out = MathUtil.distributeValuesInBands(inputlist);
		//Map<Integer, List<Double>> out = MathUtil.linearDistribution(inputlist);
		//print(inputlist,out);

		run(inputlist);

	}
	
	void print(List<Double> input, Map<Integer, List<Double>> valuesMapByBand) {
		System.out.println("Top tier of : "+input.toString());
		  for(int i = 1; i < valuesMapByBand.size()+1; i++){
		      //List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
		      List<Double> values = valuesMapByBand.get(i);
		      if(values != null) {
		    	  System.out.println(values);
		      }
		  }
		System.out.println();
	}

	void run(List<Double> inputlist) {
		System.out.println("Input: "+inputlist);
		List<Double> out = MathUtil.getTopCluster(inputlist);
		System.out.println("Liberal Top:");
		System.out.println(out);
		System.out.println("=================================================================");
	}
	
	void runLiberal(List<Double> inputlist) {
		System.out.println("Input: "+inputlist);
		List<Double> out = MathUtil.getLiberalTopCluster(inputlist);
		System.out.println("Liberal Top:");
		System.out.println(out);
		System.out.println("=================================================================");
	}
	
	void runAll(List<Double> inputlist) {
		System.out.println("Input: "+inputlist);
		Map<Integer, List<Double>> out = MathUtil.getAllClusters(inputlist);
		  for(int i = 0; i < out.size(); i++){
		      //List<Double> rangeValues = filterValuesInRange(lowest, maxForBand, values);
		      List<Double> cluster = out.get(i);
		      //if(cluster != null) {
		    	System.out.println(cluster);
		  		System.out.println("=================================================================");
		      //}
		  }
	}
}
