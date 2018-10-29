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


package com.execue.platform.test.unstructured;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.ExecueFacet;
import com.execue.core.common.bean.FacetQueryInput;
import com.execue.core.common.bean.LocationDetail;
import com.execue.core.common.bean.entity.unstructured.UserQueryFeatureInformation;
import com.execue.core.common.type.CheckType;
import com.execue.platform.exception.PlatformException;
import com.execue.platform.test.PlatformCommonBaseTest;

/**
 * @author Vishay
 */
public class UnstructuredFacetRetrievalServiceTest extends PlatformCommonBaseTest {

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @AfterClass
   public static void tearDown () {
      baseTeardown();
   }

   @Test
   public void testRetrieveFacets () throws PlatformException {
      List<ExecueFacet> facets = getUnstructuredFacetRetrievalService().retrieveFacets(110L, buildFacetQueryInput());
      System.out.println(facets.size());
   }

   private FacetQueryInput buildFacetQueryInput () {
      FacetQueryInput facetQueryInput = new FacetQueryInput();
      facetQueryInput.setDistance(100d);
      facetQueryInput.setImagePresent(CheckType.NO);
      facetQueryInput.addLocationDetail(new LocationDetail(-118.24277496d, 35d));
      // facetQueryInput.addLocationDetail(new LocationDetail(20d, 35d));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(10L, "Single cyl", "Single cyl"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(10L, "UNKNOWN_VALUE", "UNKNOWN_VALUE"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(11L, "Power Windows", "Power Windows"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(11L, "UNKNOWN_VALUE", "UNKNOWN_VALUE"));
      // facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(12L, "UNKNOWN_VALUE", "UNKNOWN_VALUE"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(18L, "-1", "-1"));
      facetQueryInput.addQueryCondition(buildUserQueryFeatureInformation(18L, "100", "100000"));
      return facetQueryInput;
   }

   private UserQueryFeatureInformation buildUserQueryFeatureInformation (Long featureId, String startValue,
            String endValue) {
      UserQueryFeatureInformation featureInformation = new UserQueryFeatureInformation();
      featureInformation.setFeatureId(featureId);
      featureInformation.setStartValue(startValue);
      featureInformation.setEndValue(endValue);
      return featureInformation;
   }
}
