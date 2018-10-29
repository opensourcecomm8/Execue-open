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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.type.CheckType;
import com.execue.core.exception.swi.KDXException;
import com.execue.core.exception.swi.PreferencesException;
import com.execue.util.ExecueConstants;

public class PreferencesServiceTest extends ExeCueBaseTest {

   private static final Logger logger = Logger.getLogger(PreferencesServiceTest.class);

   @Before
   public void setup () {
      baseTestSetup();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void testGetRangeDetails () {
      try {
         List<Range> ranges = getPreferencesService().getExistingRangesForConcept(23L);
         for (Range range : ranges) {
            logger.debug(range.getName());
            Set<RangeDetail> rangeDetails = range.getRangeDetails();
            for (RangeDetail rangeDetail : rangeDetails) {
               logger.debug(rangeDetail.getLowerLimit());
               logger.debug(rangeDetail.getUpperLimit());
            }
         }
      } catch (PreferencesException prefExp) {
         Assert.fail(prefExp.getMessage());
      }
   }

   // @Test
   public void testgetExistingRangesForConcept () {
      long conceptId = 3022L;
      try {
         List<Range> ranges = getPreferencesService().getExistingRangesForConcept(conceptId);
         for (Range range : ranges) {
            logger.debug(range.getName());
            logger.debug(range.getDescription());
            logger.debug(range.getConcept().getName());
            Set<RangeDetail> rangeDetails = range.getRangeDetails();
            for (RangeDetail rangeDetail : rangeDetails) {
               logger.debug(rangeDetail.getRange().getName());
               logger.debug(rangeDetail.getOrder());
               logger.debug(rangeDetail.getValue());
            }
         }
      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetSuggestedRangeForConcept () {
      long modelId = 100L;
      long assetId = 11L;
      long conceptId = 3022L;
      try {
         Range range = getPreferencesService().getSuggestedRangeForConcept(conceptId, modelId, assetId);
         logger.debug(range.getName());
      } catch (PreferencesException e) {
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testGetRanges () {
      List<Range> ranges = new ArrayList<Range>();
      try {
         ranges = getPreferencesService().getRanges();
         logger.debug("range list size");
         for (Range range : ranges) {
            logger.debug(range.getName());
            logger.debug(range.getConcept());
            logger.debug(range.getDescription());
            logger.debug(range.getRangeDetails().size());
         }

      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConceptProfiles () {
      Long modelId = 100L;
      try {
         List<ConceptProfile> conceptProfiles = getPreferencesService().getConceptProfiles(modelId);
         logger.debug(conceptProfiles.size());
      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetInstanceProfiles () {
      long modelId = 100L;
      try {
         List<InstanceProfile> insProfile = getPreferencesService().getInstanceProfiles(modelId);
         for (InstanceProfile instanceProfile : insProfile) {
            logger.debug(instanceProfile.getName());
         }
      } catch (PreferencesException e) {
         e.printStackTrace();
      }

   }

   // @Test
   public void testGetConceptsForConceptProfiles () {
      long modelId = 100L;
      try {
         List<Concept> concepts = getPreferencesService().getConceptsForConceptProfiles(modelId);
         for (Concept concept : concepts) {
            logger.debug(concept.getId() + " " + concept.getName() + " " + concept.getDisplayName());
         }
      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConceptsForInstanceProfiles () {
      long modelId = 100L;
      try {
         List<Concept> concepts = getPreferencesService().getConceptsForInstanceProfiles(modelId);
         for (Concept concept : concepts) {
            logger.debug(concept.getId() + " " + concept.getName() + " " + concept.getDisplayName());
         }
      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetConceptsForRanges () {
      long modelId = 100L;
      try {
         List<Concept> concepts = getPreferencesService().getConceptsForRanges(modelId);
         for (Concept concept : concepts) {
            logger.debug(concept.getId() + " " + concept.getName() + " " + concept.getDisplayName());
         }
      } catch (PreferencesException e) {
      }
   }

   @Test
   public void testCreateProfile () {
      Long modelId = 101L;

      try {
         Set<Concept> concepts = new HashSet<Concept>();
         Concept concept1 = getKDXRetrievalService().getConceptByName(modelId, "NetIncome");
         Concept concept2 = getKDXRetrievalService().getConceptByName(modelId, "OtherIncome");
         Concept concept3 = getKDXRetrievalService().getConceptByName(modelId, "NetSales");
         concepts.add(concept1);
         concepts.add(concept2);
         concepts.add(concept3);

         ConceptProfile conceptProfile = new ConceptProfile();
         conceptProfile.setName("Sales Profile");
         conceptProfile.setDisplayName("Sales Profile");
         conceptProfile.setConcepts(concepts);
         Type type = getKDXRetrievalService().getTypeByName(ExecueConstants.MEASURES_PROFILE_TYPE);
         getPreferencesService().createProfile(conceptProfile, type, modelId, CheckType.NO);
      } catch (PreferencesException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      } catch (KDXException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testUpdateProfile () {
      Long modelId = 101L;
      try {
         List<ConceptProfile> conceptProfiles = getPreferencesService().getConceptProfiles(modelId);
         ConceptProfile conProfile = conceptProfiles.get(0);
         Set<Concept> concepts = new HashSet<Concept>();
         Concept concept1 = new Concept();
         concept1.setId(105L);
         concept1.setName("Value");
         concept1.setDisplayName("Value");
         concepts.add(concept1);
         conProfile.setConcepts(concepts);
         getPreferencesService().updateProfile(conProfile, modelId);
      } catch (PreferencesException e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }
   }

   // @Test
   public void testDeleteProfile () {
      Long modelId = 100L;
      try {
         List<ConceptProfile> conceptProfiles = getPreferencesService().getConceptProfiles(modelId);
         ConceptProfile conProfile = conceptProfiles.get(0);
         logger.debug(conProfile.getId());
         getPreferencesService().deleteProfile(conProfile, modelId);
      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testDeleteProfiles () {
      Long modelId = 100L;
      try {
         List<ConceptProfile> conceptProfiles = getPreferencesService().getConceptProfiles(modelId);
         for (ConceptProfile conceptProfile : conceptProfiles) {
            logger.debug(conceptProfile.getId());
            getPreferencesService().deleteProfile(conceptProfile, modelId);
         }

      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateRange () {
      long modelId = 100L;
      List<Concept> concepts;
      try {
         concepts = getPreferencesService().getConceptsForRanges(modelId);
         String rangeName = "Range  " + concepts.get(0).getName();
         Range range = new Range();
         range.setConcept(concepts.get(0));
         logger.debug(concepts.get(0).getId());
         range.setName(rangeName);
         Set<RangeDetail> rangeDetails = new HashSet<RangeDetail>();
         RangeDetail rangDetail = new RangeDetail();
         rangDetail.setDescription("Low to 7167");
         rangDetail.setUpperLimit(7167D);
         rangDetail.setOrder("0");
         rangDetail.setValue("0");
         rangeDetails.add(rangDetail);

         RangeDetail rangDetail1 = new RangeDetail();
         rangDetail1.setDescription("7167 to 14333");
         rangDetail1.setLowerLimit(7167D);
         rangDetail1.setUpperLimit(14333D);
         rangDetail1.setOrder("1");
         rangDetail1.setValue("1");
         rangeDetails.add(rangDetail1);

         RangeDetail rangDetail2 = new RangeDetail();
         rangDetail2.setDescription("14333 to High");
         rangDetail2.setLowerLimit(14333D);
         rangDetail2.setOrder("2");
         rangDetail2.setValue("2");
         rangeDetails.add(rangDetail2);
         range.setRangeDetails(rangeDetails);
         getPreferencesService().createRange(range, range.getConcept().getId());
      } catch (PreferencesException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetKeyWord () {
      Long modelId = 100L;
      String keyWordName = "Balance";
      try {
         KeyWord keyword = getPreferencesService().getKeyWord(keyWordName, modelId);
         logger.debug("keyword id --" + keyword.getId());
      } catch (KDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetAllKeywords () {
      Long modelId = 100L;
      try {
         List<KeyWord> keywords = getPreferencesService().getAllKeyWords(modelId);
         for (KeyWord keyWord : keywords) {
            logger.debug("Keyword name --" + keyWord.getWord());
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testDoesKeywordExist () {
      Long modelId = 100L;
      try {
         boolean status = getPreferencesService().doesKeyWordExist(modelId, "Balance");
         logger.debug("Does keyword exist --" + status);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }
}