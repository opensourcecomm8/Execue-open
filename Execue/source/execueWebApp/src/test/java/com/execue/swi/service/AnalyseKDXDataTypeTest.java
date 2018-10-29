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

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.execue.core.ExeCueBaseTest;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.AssetOperationInfo;
import com.execue.core.common.bean.entity.Colum;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Constraint;
import com.execue.core.common.bean.entity.DefaultDynamicValue;
import com.execue.core.common.bean.entity.DefaultMetric;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.JobRequest;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.Mapping;
import com.execue.core.common.bean.entity.Membr;
import com.execue.core.common.bean.entity.ModelGroup;
import com.execue.core.common.bean.entity.ModelGroupMapping;
import com.execue.core.common.bean.entity.ParallelWord;
import com.execue.core.common.bean.entity.Profile;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.Tabl;
import com.execue.core.common.bean.entity.Type;
import com.execue.core.common.bean.entity.wrapper.TableInfo;
import com.execue.core.common.bean.governor.LightAssetEntityDefinitionInfo;
import com.execue.core.common.bean.qdata.QueryCacheResultInfo;
import com.execue.core.common.bean.security.User;
import com.execue.core.common.bean.swi.ApplicationDeletionContext;
import com.execue.core.common.bean.swi.AssetAnalysisReport;
import com.execue.core.common.bean.swi.SFLTermTokenWeightContext;
import com.execue.core.common.type.AssetOperationType;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.ConstraintType;
import com.execue.core.common.type.DynamicValueQualifierType;
import com.execue.core.common.type.JobType;
import com.execue.core.common.type.OperationEnum;
import com.execue.core.common.type.PublishAssetMode;
import com.execue.core.exception.HandlerException;
import com.execue.core.exception.dataaccess.DataAccessException;
import com.execue.core.exception.qdata.QueryDataException;
import com.execue.core.exception.swi.AssetAnalysisException;
import com.execue.core.exception.swi.AssetSynchronizationException;
import com.execue.core.exception.swi.KDXException;
import com.execue.core.exception.swi.MappingException;
import com.execue.core.exception.swi.PreferencesException;
import com.execue.core.exception.swi.SDXException;
import com.execue.core.exception.swi.SQLGenerationException;
import com.execue.core.exception.swi.SWIException;
import com.execue.core.util.ExecueDateTimeUtils;
import com.execue.handler.bean.UIConcept;
import com.execue.util.ExecueConstants;
import com.thoughtworks.xstream.XStream;

public class AnalyseKDXDataTypeTest extends ExeCueBaseTest {

   private static Random random;

   @Before
   public void setup () {
      baseTestSetup();
      random = new Random();
   }

   @After
   public void tearDown () {
      baseTestTeardown();
   }

   // @Test
   public void testAnalyseKDxDataType () {
      try {
         getSDXService().analyseKDXDataType(13L);
      } catch (SDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testExecueDMLStatement () {
      String dmlStatement = "insert into campaign(StartDate) values(?)";
      try {
         List<Object> data = new ArrayList<Object>();
         data.add(ExecueDateTimeUtils.getSQLTimeFromString("2003-01-24T07:10:05"));
         data.add(Types.DATE);
         getGenericJDBCService().executeManipulationStatement("sforce", dmlStatement, null, null);
      } catch (SQLGenerationException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetCentralDistributionConcepts () {
      try {
         List<Mapping> centralConcepts = getMappingService().getGrainConcepts(1L, 11L);
         List<Mapping> distributionConcepts = getMappingService().getDistributionConcepts(1L, 11L);
         System.out.println("Central" + centralConcepts.size());
         System.out.println("Distribution" + distributionConcepts.size());
         for (Mapping mapping : centralConcepts) {
            System.out.println(mapping.getBusinessEntityDefinition().getConcept().getDisplayName());
            System.out.println(mapping.getAssetEntityDefinition().getTabl().getName());
            System.out.println(mapping.getAssetEntityDefinition().getColum().getName());
         }
         for (Mapping mapping : distributionConcepts) {
            System.out.println(mapping.getBusinessEntityDefinition().getConcept().getDisplayName());
            System.out.println(mapping.getAssetEntityDefinition().getTabl().getName());
            System.out.println(mapping.getAssetEntityDefinition().getColum().getName());
         }

      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetMembers () {
      Colum column = new Colum();
      column.setId(1091L);
      try {
         List<Membr> columnMembers = getSDXService().getColumnMembers(column);
         System.out.println(columnMembers.size());
      } catch (SDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   /**
    * 
    */
   // @Test
   public void testAssetSynchInfo () {
      // Test for AssetOperationInfo
      AssetOperationInfo assetSynchronizationInfo = createAssetSynchronizationInfo();
      testGetAssetSynchData(assetSynchronizationInfo);
      testDeleteAssetSynchronizationInfo(assetSynchronizationInfo);
   }

   private void testGetAssetSynchData (AssetOperationInfo assetOperationInfo) {
      String syncData = "";
      try {
         syncData = getSDXService().getAssetOperationData(assetOperationInfo.getId());
         syncData = getSDXService().getAssetOperationDataByAssetId(assetOperationInfo.getAssetId(),
                  assetOperationInfo.getAssetOperationType());
      } catch (SDXException e) {
         e.printStackTrace();
         syncData = null;
      }
      Assert.assertTrue("Failed to get the asset operation data ", (syncData != null));
   }

   private void testDeleteAssetSynchronizationInfo (AssetOperationInfo assetOperationInfo) {
      boolean isDeleted = false;
      try {
         getSDXService().deleteAssetOperation(assetOperationInfo);
         isDeleted = true;
      } catch (SDXException e) {
         e.printStackTrace();

      }
      Assert.assertTrue("Failed to delete asset synchronization info ", isDeleted);
   }

   private AssetOperationInfo createAssetSynchronizationInfo () {
      AssetOperationInfo assetOperationInfo = getSampleAssetSynchronizationInfo(random.nextInt(),
               AssetOperationType.ASSET_ANALYSIS);
      boolean isCreated = false;
      try {
         getSDXService().createAssetOperation(assetOperationInfo);
         isCreated = true;
      } catch (SDXException e) {
         e.printStackTrace();
      }
      Assert.assertTrue("Failed to create asset operation info ", isCreated);
      return assetOperationInfo;
   }

   public AssetOperationInfo getSampleAssetSynchronizationInfo (long assetId, AssetOperationType assetOperationType) {
      AssetOperationInfo assetOperationInfo = new AssetOperationInfo();
      assetOperationInfo.setAssetId(assetId);
      assetOperationInfo.setAssetOperationData("abc");
      assetOperationInfo.setChangeFound(CheckType.YES);
      assetOperationInfo.setOperation(OperationEnum.JOB);
      assetOperationInfo.setStatus(CheckType.NO);
      assetOperationInfo.setCompletionDate(new Date());
      assetOperationInfo.setStartDate(new Date());
      assetOperationInfo.setAssetOperationType(assetOperationType);
      return assetOperationInfo;
   }

   // @Test
   public void testGetLatestInstance () {
      try {
         Long modelId = 100L;
         Concept concept = new Concept();
         concept.setId(104L);
         Instance latestInstanceInserted = getKDXRetrievalService().getLatestInstanceInserted(modelId, concept.getId());
         System.out.println(latestInstanceInserted.getName());
      } catch (KDXException kdxException) {
         kdxException.printStackTrace();
      }
   }

   // @Test
   public void testDefaultDynamicValue () {
      System.out.println("here");
      DefaultDynamicValue defaultDynamicValue = getLookupService().getDefaultDynamicValue(101L, 2L,
               DynamicValueQualifierType.FIRST);

      if (defaultDynamicValue != null) {
         System.out.println("forumla:::" + defaultDynamicValue.getFormula());
      }
   }

   // @Test
   public void testKDXMaintenanceService () {
      try {
         SFLTermTokenWeightContext sflTermTokenWeightContext = new SFLTermTokenWeightContext();
         JobRequest jobRequest = new JobRequest();
         jobRequest.setId(15L);
         jobRequest.setUserId(1L);
         jobRequest.setJobType(JobType.SFL_TERM_TOKEN_WEIGHT_UPDATION);
         sflTermTokenWeightContext.setJobRequest(jobRequest);
         // getKDXMaintenanceService().updateSFLTermTokensWeightOnHits(sflTermTokenWeightContext);
         getUpdateSFLTermTokenWeightJobService().scheduleUpdateSFLTermTokenWeightJob(sflTermTokenWeightContext);
      } catch (KDXException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testCreateConstraints () {
      try {
         List<Constraint> constraints = new ArrayList<Constraint>();
         Constraint constraint = getConstraint(10L, 1001L);
         Constraint constraint1 = getConstraint(11L, 1002L);
         constraints.add(constraint);
         constraints.add(constraint1);
         getSDXService().createConstraints(constraints);
      } catch (SDXException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }

   private Constraint getConstraint (Long constraintId, Long columnId) {
      Constraint constraint = new Constraint();
      constraint.setConstraintId(constraintId);
      constraint.setName("First");
      constraint.setType(ConstraintType.PRIMARY_KEY);
      Set<Colum> columns = new HashSet<Colum>();
      Colum colum = null;
      try {
         colum = getSDXService().getColumnById(columnId);
      } catch (SDXException e) {
         e.printStackTrace();
      }
      columns.add(colum);
      constraint.setConstraintColums(columns);
      return constraint;
   }

   // @Test
   public void testGetParallelWords () throws KDXException {
      List<ParallelWord> parallelWordsForKeyWord = getPreferenceService().getParallelWordsForKeyWord(1L);
      for (ParallelWord parallelWord : parallelWordsForKeyWord) {
         System.out.println(parallelWord.getParallelWord());
      }
   }

   // @Test
   public void testCreateKeyWord () throws KDXException {
      KeyWord keyWord = new KeyWord();
      keyWord.setWord("Sachin");
      User user = new User();
      user.setId(1L);
      // keyWord.setUser(user);
      // getPreferenceService().createKeyWord(keyWord);
   }

   // @Test
   public void testCreateParallelWord () throws KDXException {
      ParallelWord parallelWord = new ParallelWord();
      parallelWord.setParallelWord("Master");
      KeyWord keyWord = getPreferenceService().getKeyWord(1L);
      parallelWord.setKeyWord(keyWord);
      getPreferenceService().createParallelWord(parallelWord);
   }

   // @Test
   public void testUpdateParallelWord () throws KDXException {
      ParallelWord parallelWord = getPreferenceService().getParallelWord(5002L);
      parallelWord.setSuffixSpace(true);
      getPreferenceService().updateParallelWord(parallelWord);
   }

   // @Test
   public void testDeleteParallelWord () throws KDXException {
      getPreferenceService().deleteParallelWord(getPreferenceService().getParallelWord(5004L));
   }

   // @Test
   public void testGetKeyWordByName () throws KDXException {
      KeyWord keyWord = getPreferenceService().getKeyWord(1L);
      System.out.println(keyWord.getWord());
   }

   // @Test
   public void testIsExactBusinessTerm () throws KDXException {
      Long modelId = 100L;
      Long dedId = getKDXRetrievalService().isExactBusinessTerm(modelId, "Computer and Technology");
      System.out.println("dedId:::" + dedId);
   }

   // @Test
   public void testIsPartOfBisinessTermForKeyWord () throws KDXException {
      Long modelId = 100L;
      boolean dedId = getKDXRetrievalService().isPartOfBusinessTerm(modelId, "Retail");
      System.out.println("dedId:::" + dedId);
   }

   // @Test
   public void testGetAssetEntitiesForBusinessEntityTerm () throws KDXException {
      try {
         List<LightAssetEntityDefinitionInfo> assetEntities = getMappingService().getAssetEntitiesForAllAssets(10001L,
                  101L);
         System.out.println("Size " + assetEntities.size());
      } catch (MappingException e) {
         e.printStackTrace();
      }
   }

   // @Test
   public void testGetNonBaseModelGroup () throws KDXException, PreferencesException {
      List<ModelGroup> modelGroups = getKDXRetrievalService().getNonBaseModelGroupsByModelId(311L);
      for (ModelGroup modelGroup : modelGroups) {
         System.out.println("modelGroupId " + modelGroup.getId() + "  name of mg--" + modelGroup.getName());
      }
      List<Profile> conceptProfiles = getPreferenceService().getConceptProfiles(modelGroups);
      for (Profile profile : conceptProfiles) {
         System.out.println("Profile Name----" + profile.getName());
      }

      List<Concept> concepts = getKDXRetrievalService().getConcepts(modelGroups);
      System.out.println(concepts.size());
      for (Concept concept : concepts) {
         System.out.println(concept.getName() + "concept ID --" + concept.getId());
      }
      //
      // List<Relation> relaList = getBusinessEntityDefinitionDAO().getRelationsForModelGroup(modelGroups);
      //
      // for (Relation relation : relaList) {
      // System.out.println("name of the relation " + relation.getName() + "id---" + relation.getId());
      //
      // }
   }

   // @Test
   public void testPopulateModelGroupMapping () throws KDXException {
      List<ModelGroupMapping> modelGroupMappings = getKDXRetrievalService().getPopulatedModelGroupMapping(311L);
      System.out.println(modelGroupMappings.size());
      for (ModelGroupMapping modelGroupMapping : modelGroupMappings) {
         System.out.println("model name--" + modelGroupMapping.getModel().getName());
         System.out.println("mg name----" + modelGroupMapping.getModelGroup().getName());
      }
   }

   // @Test
   public void testDeleteApplicationHierarchy () throws KDXException {
      Long applicationId = 419L;
      Long userId = 1L;
      ApplicationDeletionContext ctx = new ApplicationDeletionContext();
      ctx.setApplicationId(applicationId);
      ctx.setUserId(userId);
      getApplicationService().deleteApplicationHierarchy(ctx);
   }

   // @Test
   public void testGetQueryCacheResults () throws QueryDataException {
      Long userQueryId = 1L; // eg. for query "Amazon Sales";
      List<QueryCacheResultInfo> queryCacheResults = getQueryDataService().getQueryCacheResults(1L, 101L);
      System.out.println(queryCacheResults.size());
   }

   // @Test
   public void testGetRiOntoTermsForInstanceInsideConcept () throws KDXException {
      List<RIOntoTerm> terms = getKDXDataAccessManager().getTermsByLookupWordAndEntityTypeInGroupForInstance(
               "california", BusinessEntityType.CONCEPT_LOOKUP_INSTANCE, 110L, 10011L);
      System.out.println(terms.size());
   }

   // @Test
   public void testCreateDefaultMetrics () throws MappingException {
      List<DefaultMetric> defaultMetrics = new ArrayList<DefaultMetric>();
      DefaultMetric defaultMetric1 = getDefaultMetric(1L, 10001L, 100L);
      DefaultMetric defaultMetric2 = getDefaultMetric(1L, 10002L, 200L);
      DefaultMetric defaultMetric3 = getDefaultMetric(6L, 10003L, 150L);
      defaultMetrics.add(defaultMetric1);
      defaultMetrics.add(defaultMetric2);
      defaultMetrics.add(defaultMetric3);
      getMappingService().saveUpdateDefaultMetrics(defaultMetrics);
   }

   // @Test
   public void testDeleteDefaultMetrics () throws MappingException {
      getMappingService().deleteDefaultMetrics(1L);
   }

   // @Test
   public void testGetFactTables () throws SDXException {
      List<Tabl> factTables = getSDXService().getFactTables(1L);
      for (Tabl tabl : factTables) {
         System.out.println(tabl.getName());
         System.out.println(tabl.getId());
      }
      Asset asset = getSDXService().getAsset(1L);
      List<TableInfo> allTables = getSDXService().getAssetTables(asset);
      for (TableInfo tabl : allTables) {
         System.out.println(tabl.getTable().getName());
      }
      System.out.println(factTables.size());
   }

   // @Test
   public void testGetPossibleDefaultMetrics () throws MappingException {
      List<DefaultMetric> possibleDefaultMetrics = getMappingService().getPossibleDefaultMetrics(1L, 6L, 2);
      System.out.println(possibleDefaultMetrics.size());
      for (DefaultMetric defaultMetric : possibleDefaultMetrics) {
         System.out.print(defaultMetric.getTableId() + "\t");
         System.out.print(defaultMetric.getMappingId() + "\t");
         System.out.println(defaultMetric.getPopularity());
      }
   }

   // @Test
   public void testGetExistingDefaultMetrics () throws SWIException {
      List<Long> tableIds = new ArrayList<Long>();
      tableIds.add(7L);
      tableIds.add(10L);
      List<Mapping> existingDefaultMetrics = getMappingService().getExistingDefaultMetrics(tableIds, 2);
      System.out.println(existingDefaultMetrics.size());
      for (Mapping mapping : existingDefaultMetrics) {
         System.out.print(mapping.getId() + "\t");
         System.out.print(mapping.getAssetEntityDefinition().getAsset().getName() + "\t");
         System.out.print(mapping.getAssetEntityDefinition().getTabl().getName() + "\t");
         System.out.print(mapping.getAssetEntityDefinition().getColum().getName() + "\t");
         System.out.println(mapping.getBusinessEntityDefinition().getConcept().getName());
      }
   }

   // @Test
   public void testGetFactTablesForBEDIdsByNonPrimaryMapping () throws MappingException {
      List<Long> bedIds = new ArrayList<Long>();
      bedIds.add(10001L);
      Long assetId = 1L;
      List<Long> distinctFactTables = getMappingService().getDistinctFactTablesForBEDIdsByNonPrimaryMappings(bedIds,
               assetId);
      System.out.println(distinctFactTables.size());
      for (Long tableId : distinctFactTables) {
         System.out.println(tableId);
      }
   }

   // @Test
   public void testGetAllPossibleDefaultMetrics () throws SWIException {
      Long assetId = 1L;
      Long tableId = 6L;
      List<DefaultMetric> defaultMetrics = getMappingService().getAllPossibleDefaultMetrics(assetId, tableId);
      for (DefaultMetric defaultMetric : defaultMetrics) {
         System.out.println("MappingId ::" + defaultMetric.getMappingId());
         System.out.println("Popularity::" + defaultMetric.getPopularity());
         System.out.println("Column Name " + defaultMetric.getColumnName());
         System.out.println("Concept Name " + defaultMetric.getConceptName());
         System.out.println("DM Id" + defaultMetric.getId());
      }
   }

   // @Test
   public void testGetTablesInJoins () throws DataAccessException {
      List<Long> tablesParticipatingInJoins = getJoinDAO().getTablesParticipatingInJoins(7048L);
      System.out.println(tablesParticipatingInJoins.size());

   }

   // @Test
   public void testGetColumnsWihoutKDXType () throws DataAccessException {
      List<Long> columnsWithoutKDXDataType = getAssetEntityDefinitionDAO().getColumnsWithoutKDXDataType(7048L, 101L);
      System.out.println(columnsWithoutKDXDataType.size());
   }

   // @Test
   public void testGetPopulatedColumns () throws DataAccessException {
      List<Long> columnIds = new ArrayList<Long>();
      columnIds.add(15075L);
      columnIds.add(15077L);
      columnIds.add(15079L);
      List<Colum> populatedColumns = getAssetEntityDefinitionDAO().getPopulatedColumns(columnIds);
      for (Colum colum : populatedColumns) {
         System.out.println(colum.getName());
      }
   }

   // @Test
   public void testGetPopulatedMembers () throws DataAccessException {
      List<Long> memberIds = new ArrayList<Long>();
      memberIds.add(416045L);
      memberIds.add(416046L);
      memberIds.add(416050L);
      List<Membr> populatedColumns = getAssetEntityDefinitionDAO().getPopulatedMembers(memberIds);
      for (Membr membr : populatedColumns) {
         System.out.println(membr.getLookupDescription());
      }
   }

   // @Test
   public void testGetMetricsPerAssetPerQuery () throws DataAccessException, MappingException {
      List<Mapping> assetDefaultMetrics = getMappingService().getAssetDefaultMetrics(1L, 3);
      for (Mapping mapping : assetDefaultMetrics) {
         System.out.print(mapping.getId() + "\t");
         System.out.print(mapping.getAssetEntityDefinition().getAsset().getName() + "\t");
         System.out.print(mapping.getAssetEntityDefinition().getTabl().getName() + "\t");
         System.out.print(mapping.getAssetEntityDefinition().getColum().getName() + "\t");
         System.out.println(mapping.getBusinessEntityDefinition().getConcept().getName());
      }
   }

   // @Test
   public void testTypeByName () throws HandlerException {
      Type type = getTypeConceptAssociationServiceHandler().getTypeByName(ExecueConstants.ONTO_ENTITY_TYPE);
      System.out.println(type.getId());
      System.out.println(type.getName());
   }

   // @Test
   public void testGetAllTypes () throws HandlerException {
      List<Type> allTypes = getTypeConceptAssociationServiceHandler().getAllTypes();
      for (Type type : allTypes) {
         System.out.println(type.getId());
         System.out.println(type.getName());
      }
   }

   // @Test
   public void testGetRealizedConceptsForType () throws HandlerException {
      Type type = getTypeConceptAssociationServiceHandler().getTypeByName(ExecueConstants.TIME_FRAME_TYPE);
      List<UIConcept> uiConcepts = getTypeConceptAssociationServiceHandler().getRealizedConceptsForType(type.getId(),
               101L);
      for (UIConcept uiConcept : uiConcepts) {
         System.out.println(uiConcept.getDisplayName());
      }
   }

   // @Test
   public void testGetAttributesToBeRealizedForType () throws HandlerException {
      Type measureType = getTypeConceptAssociationServiceHandler()
               .getTypeByName(ExecueConstants.MEASURABLE_ENTITY_TYPE);
      List<Type> types = getTypeConceptAssociationServiceHandler()
               .getAttributesToBeRealizedForType(measureType.getId());
      for (Type type : types) {
         System.out.println(type.getId());
         System.out.println(type.getName());
      }
   }

   // @Test
   public void testPopulateAssetAnalysisReport () throws AssetAnalysisException {
      boolean reportFound = getAssetAnalysisService().populateAssetAnalysisReport(7052L, PublishAssetMode.LOCAL);
      System.out.println(reportFound);
   }

   @Test
   public void fetchAssetAnalysisReport () throws AssetAnalysisException, IOException {
      AssetAnalysisReport assetAnalysisReport = getAssetAnalysisService().fetchAssetAnalysisReport(7052L);
      System.out.println(assetAnalysisReport.getAssetAnalysisReportInfoList().size());
      FileWriter file = new FileWriter("c:\\temp.txt");
      String data = new XStream().toXML(assetAnalysisReport);
      file.append(data);
      file.close();
   }

   // @Test
   public void testPopulateAssetSyncInfo () throws AssetSynchronizationException {
      boolean changeFound = getAssetSyncPopulateService().populateAssetSyncInfo(3036L);
      System.out.println(changeFound);
   }

   // @Test
   public void testAbsorbAssetSyncInfo () throws AssetSynchronizationException {
      boolean absorbAssetSyncInfo = getAssetSyncAbsorptionService().absorbAssetSyncInfo(3036L, 422L);
      System.out.println(absorbAssetSyncInfo);
   }

}
