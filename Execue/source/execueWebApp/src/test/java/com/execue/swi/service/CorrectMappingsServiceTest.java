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


package com.execue.swi.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.swi.CorrectMappingsMaintenanceContext;
import com.execue.core.exception.swi.KDXException;
import com.execue.core.exception.swi.MappingException;

public class CorrectMappingsServiceTest extends ExeCueBaseTest {

	private static final Logger logger = Logger
			.getLogger(MappingServiceTest.class);

	@BeforeClass
	public static void setUp() {
		baseTestSetup();
	}

	@Test
	public void testCorrectMappingsForApplication() {
		CorrectMappingsMaintenanceContext correctMappingsMaintenanceContext = new CorrectMappingsMaintenanceContext();
		correctMappingsMaintenanceContext.setApplicationId(110L);
		try {
			getCorrectMappingService().correctMappings(
					correctMappingsMaintenanceContext);
		} catch (MappingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCorrectMappingsForSpecifiedAssetsInApplication() {
		CorrectMappingsMaintenanceContext correctMappingsMaintenanceContext = new CorrectMappingsMaintenanceContext();
		correctMappingsMaintenanceContext.setApplicationId(17L);
		try {
			List<Long> assetIds = new ArrayList<Long>();
			assetIds.add(101L);
			assetIds.add(102L);
			correctMappingsMaintenanceContext.setAssetIds(assetIds);
			getCorrectMappingService().correctMappings(
					correctMappingsMaintenanceContext);

		} catch (MappingException e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDown() {
		baseTestTeardown();
	}
}
