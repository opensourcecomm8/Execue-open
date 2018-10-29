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

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.Path;
import com.execue.core.common.bean.entity.PathDefinition;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.exception.swi.SWIException;

/**
 * This class contains the DAO operations for Path, PathDefintion and EntityTrippleDefinition object
 * 
 * @author Vishay
 * @version 1.0
 * @since 01/07/09
 */
public class PathDefinitionServiceTest extends ExeCueBaseTest {

   private static final Logger log = Logger.getLogger(PathDefinitionServiceTest.class);

   @Before
   public void setUp () throws Exception {
      baseTestSetup();
   }

   @After
   public void tearDown () throws Exception {
      baseTestTeardown();
   }

   // @Test
   public void testGetPathDefinition () {
      BusinessEntityDefinition source = new BusinessEntityDefinition();
      BusinessEntityDefinition target = new BusinessEntityDefinition();
      source.setId(1L);
      target.setId(2L);
      try {
         PathDefinition pathDefinition = getPathDefinitionService().getPathDefinition(source, target);
         System.out.println(pathDefinition.getId());
         Collection<Path> paths = pathDefinition.getPaths();
         for (Path path : paths) {
            System.out.println(path.getEntityTripleDefinition().getId());
         }
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetPathDefinitionByID () {
      try {
         List<PathDefinition> pathDefinitions = getPathDefinitionService().getPathDefinitions(1012L, 1001L);
         for (PathDefinition pathDefinition : pathDefinitions) {
            System.out.println(pathDefinition.getId());
            Collection<Path> paths = pathDefinition.getPaths();
            for (Path path : paths) {
               System.out.println(path.getEntityTripleDefinition().getId());
            }
         }
      } catch (SWIException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetSourceDEDIds () {
      try {
         List<Long> sourceDEDIds = getPathDefinitionService().getSourceBEDIds(4044L, 3316L);
         if (log.isDebugEnabled()) {
            for (Long id : sourceDEDIds) {
               log.debug("Source DED Id : " + id);
            }
         }
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
      }
   }

   // @Test
   public void testGetPathDefinitionIds () {
      try {
         List<Long> pathDefIds = getPathDefinitionService().getPathDefinitionIds(1012L, 1001L);
         if (log.isDebugEnabled()) {
            for (Long id : pathDefIds) {
               log.debug("Path Def Id : " + id);
            }
         }
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
      }
   }

   // @Test
   public void testGetDirectPathDefinitionByETDIds () {
      try {
         PathDefinition pathDefinition = getPathDefinitionService()
                  .getDirectPathDefinitionByETDIds(1174L, 1866L, 1149L);
         if (log.isDebugEnabled()) {
            log.debug("Path Definition Id : " + pathDefinition.getId());
         }
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
      }
   }

   // @Test
   public void testGetDirectPathDefinitionDetailsByETDIds () {
      try {
         PathDefinition pathDefinition = getPathDefinitionService().getDirectPathDefinitionDetailsByETDIds(1174L,
                  1866L, 1149L);
         if (log.isDebugEnabled()) {
            log.debug("Path Definition Id : " + pathDefinition.getId());
            for (Path path : pathDefinition.getPaths()) {
               log.debug("Path Id : " + path.getId());
               log.debug("ETD Id : " + path.getEntityTripleDefinition().getId());
            }
         }
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
      }
   }

   // @Test
   public void testGetDirectPathDefinitionIdByETDNames () {
      try {
         Long pathDefinitionId = getPathDefinitionService().getDirectPathDefinitionIdByETDNames("Beverage",
                  "manufacturer", "Organisation");
         if (log.isDebugEnabled()) {
            log.debug("Path Definition Id : " + pathDefinitionId);
         }
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
      }
   }

   // @Test
   public void testGetDirectPathDefRelationByPathDefId () {
      try {
         Relation relation = getPathDefinitionService().getDirectPathDefRelationByPathDefId(1001L);
         if (log.isDebugEnabled()) {
            log.debug("relation : " + relation.getName());
         }
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
      }
   }

   // @Test
   public void testMarkCentralConceptPaths () {
      try {
         Long modelId = 101L;
         getPathDefinitionService().markCentralConceptPaths(modelId);
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
         Assert.fail(swiException.getMessage());
      }
   }

   // @Test
   public void testDeleteDirectPath () {
      try {
         Long directPathDefinitonId = 44456L;
         PathDefinition pathDefinition = getPathDefinitionService().getPathDefinitionById(directPathDefinitonId);
         getPathDefinitionService().deleteDirectPath(pathDefinition);
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
         Assert.fail(swiException.getMessage());
      }
   }

   @Test
   public void testDeleteAllPathDefinitionsForCloud () {
      try {
         Long cloudId = 1004L;
         Long startime = System.currentTimeMillis();
         getPathDefinitionService().deleteAllPathDefinitionsForCloud(cloudId);
         Long endTime = System.currentTimeMillis();
         System.out.println("Total Time(Ms): " + (endTime - startime));
      } catch (SWIException swiException) {
         log.error(swiException, swiException);
         Assert.fail(swiException.getMessage());
      }
   }
}
