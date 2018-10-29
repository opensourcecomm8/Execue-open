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
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.type.ConstraintType;
import com.execue.swi.exception.SDXException;
import com.execue.swi.exception.SWIException;
import com.execue.swi.SWIBaseTest;

public class ConstraintsServiceTest extends SWIBaseTest {

   private static final Logger logger = Logger.getLogger(ConstraintsServiceTest.class);

   @BeforeClass
   public static void setUp () {
      baseSetup();
   }

   @Test
   public void testProcessor () {

      // test for orphan tables
      // Create constraints(Primary Key, Foreign key)
      // updateConstraints
      // retrieve constraints
      // deleteConstraints

      // Prepare Constraints when the warehouse doesnot have constraints
      getOrphanTables(1L);
      updateConstraints();
      deleteConstraints(1L);

   }

   private void deleteConstraints (Long assetId) {
      boolean operationSuccedded = true;
      try {
         getSDXDeletionService().deleteConstraints(assetId);
      } catch (SDXException e) {
         operationSuccedded = false;
      }
      Assert.assertTrue("Failed to delete Constraints ", operationSuccedded);
   }

   private void updateConstraints () {
      boolean operationSucceeded = true;
      List<Constraint> constraints = new ArrayList<Constraint>();
      try {
         Constraint primaryKeyConstraint = getSdxRetrievalService().getConstraintByName("Account_AccId");
         primaryKeyConstraint.setName("VNTG_DT");
         primaryKeyConstraint.setType(ConstraintType.PRIMARY_KEY);
         Colum colum = getSdxRetrievalService().getColumnById(3L);
         Set<Colum> columSet = new HashSet<Colum>();
         columSet.add(colum);
         primaryKeyConstraint.setConstraintColums(columSet);
         constraints.add(primaryKeyConstraint);
         getSdxManagementService().updateConstraints(constraints);
      } catch (SWIException e) {
         operationSucceeded = false;
      }
      Assert.assertTrue("Failed to update Constraints ", operationSucceeded);
   }

   private void getOrphanTables (Long assetId) {
      boolean operationSucceeded = true;
      List<Tabl> tables = null;
      try {
         tables = getSdxRetrievalService().getOrphanTables(assetId);
         for (Tabl tabl : tables) {
            logger.debug("Table Name " + tabl.getName());
         }
      } catch (SDXException e) {
         operationSucceeded = false;
      }
      Assert.assertTrue("Failed to getOrphanTables ", operationSucceeded);
   }

   private List<Constraint> prepareConstraints () {
      List<Constraint> constraints = new ArrayList<Constraint>();
      try {
         Constraint primaryKeyConstraint = new Constraint();
         Constraint foreignKeyConstraint = new Constraint();
         primaryKeyConstraint.setName("Account_AccId");
         primaryKeyConstraint.setType(ConstraintType.PRIMARY_KEY);
         Colum colum = new Colum();
         colum.setName("ACCOUNT_ID");
         colum.setId(1L);
         Set<Colum> columSet = new HashSet<Colum>();
         columSet.add(colum);
         primaryKeyConstraint.setConstraintColums(columSet);

         foreignKeyConstraint.setName("FKEY_ACCID");
         foreignKeyConstraint.setType(ConstraintType.FOREIGN_KEY);
         Tabl referenceTable = new Tabl();
         referenceTable.setId(1L);
         Colum referenceColum = new Colum();
         referenceColum.setId(1L);
         foreignKeyConstraint.setReferenceTable(referenceTable);
         foreignKeyConstraint.setReferenceColumn(referenceColum);
         Colum colum1 = getSdxRetrievalService().getColumnById(6L);
         Set<Colum> columSet1 = new HashSet<Colum>();
         columSet1.add(colum1);
         foreignKeyConstraint.setConstraintColums(columSet1);
         constraints.add(primaryKeyConstraint);
         constraints.add(foreignKeyConstraint);
      } catch (SWIException e) {
         constraints = null;
      }
      return constraints;
   }

}
