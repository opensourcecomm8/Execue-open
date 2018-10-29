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


package com.execue.web.core.action.swi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.ac.CubeCreationContext;
import com.execue.core.common.bean.entity.Asset;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.type.ColumnType;
import com.execue.core.common.type.JobStatus;
import com.execue.core.common.type.JobType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.handler.bean.UIAsset;
import com.execue.handler.bean.UICubeCreation;
import com.execue.handler.bean.UICubeRequestDataInfo;
import com.execue.handler.bean.UIInstanceMember;
import com.execue.handler.bean.UIJobHistory;
import com.execue.handler.bean.UIJobRequestStatus;
import com.execue.handler.bean.UIRangeDetail;
import com.execue.handler.bean.UIStatus;
import com.execue.handler.comparator.UIRangeDetailOrderComparator;
import com.thoughtworks.xstream.XStream;

@SuppressWarnings ("serial")
public class CubeManagementAction extends SWIPaginationAction {

   private static final Logger    log                  = Logger.getLogger(CubeManagementAction.class);
   private static final String    UI_CUBE_CREATION_KEY = "uiCubeCreation";

   private Asset                  baseAsset;
   private Asset                  targetAsset;
   private List<Asset>            assets;
   private Asset                  asset;
   private Concept                concept;
   private Range                  range;
   private List<Concept>          concepts;
   private List<UIInstanceMember> instanceMembrs;
   private UICubeCreation         localUICubeCreation;
   private List<Long>             dimensionIndexesForDeletion;
   private List<Long>             rangeIndexesForDeletion;
   private List<UIRangeDetail>    rangeDetailList;
   private String                 dimensionType;
   private int                    selectedIndex;
   private List<String>           defaultStats;
   private List<String>           defaultDimensions;
   private UIJobRequestStatus     cubeJobRequestStatus;
   private List<UIJobHistory>     cubeJobHistory;
   private UICubeRequestDataInfo  cubeRequestDataInfo;
   private List<UIAsset>          cubes;
   private CubeCreationContext    cubeCreationContext;
   private Long                   jobRequestId;
   // this flag means if request is for cube refresh(then this should be populated with existing cube assetId)
   private Long                   existingAssetId;
   public static final int        PAGE_SIZE            = 9;
   public static final int        NUMBER_OF_LINKS      = 4;
   private String                 sourceName           = "cubeRequest";
   private UIStatus               uiStatus;

   public String input () {
      try {
         if (log.isDebugEnabled()) {
            log.debug("Input method called");
         }
         if (getHttpSession() != null && getHttpSession().get(UI_CUBE_CREATION_KEY) != null) {
            getHttpSession().remove(UI_CUBE_CREATION_KEY);
         }
         assets = getSdxServiceHandler().getAllParentAssets(getApplicationContext().getAppId());
         Long sessionAssetId = getApplicationContext().getAssetId();
         if (sessionAssetId != null) {
            for (Asset localAsset : assets) {
               if (localAsset.getId().longValue() == sessionAssetId.longValue()) {
                  asset = localAsset;
                  break;
               }
            }
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   @Override
   public String processPage () throws ExeCueException {
      try {
         getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
         getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
         if (getApplicationContext().getAssetId() != null) {
            baseAsset = getCubeManagementServiceHandler().getAssetById(getApplicationContext().getAssetId());
            List<Concept> dimensions = getCubeManagementServiceHandler().getEligibleConceptsOfAssetForCubeByPage(
                     getBaseAsset().getId(), getPageDetail());
            List<Concept> eligibleDimensions = new ArrayList<Concept>();
            for (Concept concept : dimensions) {
               ColumnType columnType = getCubeManagementServiceHandler().getConceptKDXType(
                        getApplicationContext().getModelId(), concept.getId(), baseAsset.getId());
               if (ColumnType.MEASURE.equals(columnType)) {
                  eligibleDimensions.add(concept);
               } else {
                  if (getCubeManagementServiceHandler().getMappedInstanceCount(getApplicationContext().getModelId(),
                           concept.getId(), baseAsset.getId()) > 0) {
                     eligibleDimensions.add(concept);
                  }
               }
            }

            // set the record count
            // getPageDetail().setRecordCount(Long.valueOf(eligibleDimensions.size()));
            // concepts = getProcessedResults(eligibleDimensions, getPageDetail());
            setConcepts(eligibleDimensions);
            // push the page object into request
            if (log.isDebugEnabled()) {
               log.debug(getPageDetail().toString());
            }
            getHttpRequest().put(PAGINATION, getPageDetail());
            log.debug("Concepts for cube creation request size is " + concepts.size());

            if (getHttpSession().get(UI_CUBE_CREATION_KEY) == null) {
               getHttpSession().put(UI_CUBE_CREATION_KEY, new UICubeCreation());
               ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).setBaseAsset(baseAsset);
            }
            setLocalUICubeCreation((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY));
         }

      } catch (Exception exception) {
         log.error(exception, exception);
         throw new ExeCueException(ExeCueExceptionCodes.DEFAULT_EXCEPTION_CODE, exception);
      }
      return SUCCESS;
   }

   public String showSelectedDimensions () {
      UICubeCreation uiCubeCreation = (UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY);
      if (uiCubeCreation != null && !uiCubeCreation.getBaseAsset().getId().equals(getApplicationContext().getAssetId())) {
         getHttpSession().remove(UI_CUBE_CREATION_KEY);
      }
      setLocalUICubeCreation((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY));
      return SUCCESS;
   }

   public String showDimensionDetails () {
      try {
         if (existsInSelectedDimensions()) {
            return showSelectedDimensionDetails();
         }
         baseAsset = getCubeManagementServiceHandler().getAssetById(baseAsset.getId());
         Long conceptBedId = getConcept().getBedId();
         concept = getCubeManagementServiceHandler().getConceptById(getConcept().getId());
         concept.setBedId(conceptBedId);
         instanceMembrs = getCubeManagementServiceHandler().getConceptDetails(getApplicationContext().getModelId(),
                  getConcept().getId(), getBaseAsset().getId());
         if (ExecueCoreUtil.isCollectionEmpty(instanceMembrs)) {
            range = getCubeManagementServiceHandler().getSuggestedRangesForConcept(
                     getApplicationContext().getModelId(), baseAsset.getId(), conceptBedId);
            setRangeDetailList(getPreferencesServiceHandler().transformRangeDetails(range.getRangeDetails()));
            Collections.sort(getRangeDetailList(), new UIRangeDetailOrderComparator());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String showSelectedDimensionDetails () {
      try {
         baseAsset = getCubeManagementServiceHandler().getAssetById(baseAsset.getId());
         if ("SL".equals(dimensionType)) {
            concept = ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).getSelectedConcepts().get(
                     selectedIndex);
            instanceMembrs = getCubeManagementServiceHandler().getConceptDetails(getApplicationContext().getModelId(),
                     getConcept().getId(), getBaseAsset().getId());
         } else {
            range = ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).getSelectedRanges()
                     .get(selectedIndex);
            concept = getCubeManagementServiceHandler().getConceptByBEDId(range.getConceptBedId());
            concept.setBedId(range.getConceptBedId());
            setRangeDetailList(getPreferencesServiceHandler().transformRangeDetails(range.getRangeDetails()));
            Collections.sort(getRangeDetailList(), new UIRangeDetailOrderComparator());
         }
      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String addDimensionToSelection () {
      try {
         Long bedId = getConcept().getBedId();
         concept = getCubeManagementServiceHandler().getConceptById(getConcept().getId());
         concept.setBedId(bedId);
         ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).addSelectedConcept(concept);
         setMessage(getText("execue.cube.dimension.addToSelection.success"));
      } catch (ExeCueException e) {
         e.printStackTrace();
         setMessage(getText("execue.cube.dimension.addToSelection.failure"));
      }
      return SUCCESS;
   }

   public String addRangeToSelection () {
      try {
         Long conceptBedId = getConcept().getBedId();
         concept = getCubeManagementServiceHandler().getConceptById(getConcept().getId());
         concept.setBedId(conceptBedId);
         range.setRangeDetails(getPreferencesServiceHandler().transformRangeDetails(getRangeDetailList()));
         range.setConceptBedId(conceptBedId);
         range.setName(concept.getDisplayName());

         if (range.getId() == null) {
            range.setId(new Long(((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).getSelectedRanges()
                     .size()));
            ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).addSelectedRange(range);
         } else {
            ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).getSelectedRanges().set(
                     range.getId().intValue(), range);
         }

         setMessage(getText("execue.cube.dimension.addToSelection.success"));
      } catch (ExeCueException e) {
         e.printStackTrace();
         setMessage(getText("execue.cube.dimension.addToSelection.failure"));
      }
      return SUCCESS;
   }

   public String deleteMembersAndRanges () {

      List<Integer> conceptIdsToBeDeleted = new ArrayList<Integer>();
      List<Integer> rangeCocneptIdsToBeDeleted = new ArrayList<Integer>();

      int index = 0;
      if (getDimensionIndexesForDeletion() != null) {
         for (Long conceptIdToBeDeleted : getDimensionIndexesForDeletion()) {
            index = 0;
            for (Concept selectedConcept : ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY))
                     .getSelectedConcepts()) {
               if (conceptIdToBeDeleted != null && conceptIdToBeDeleted.equals(selectedConcept.getId())) {
                  conceptIdsToBeDeleted.add(index);
               }
               index++;
            }
         }
      }

      if (getRangeIndexesForDeletion() != null) {
         for (Long rangeConceptToBeDeleted : getRangeIndexesForDeletion()) {
            index = 0;
            for (Range selectedRange : ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY))
                     .getSelectedRanges()) {
               if (rangeConceptToBeDeleted != null && rangeConceptToBeDeleted.equals(selectedRange.getConceptBedId())) {
                  rangeCocneptIdsToBeDeleted.add(index);
               }
               index++;
            }
         }
         adjustRangeIds();
      }

      int length = 0;
      if (conceptIdsToBeDeleted.size() != 0) {
         length = conceptIdsToBeDeleted.size() - 1;
         for (int deleteIndex = length; deleteIndex >= 0; deleteIndex--) {
            ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).deleteSelectedConcept(conceptIdsToBeDeleted
                     .get(deleteIndex));
         }
      }
      if (rangeCocneptIdsToBeDeleted.size() != 0) {
         length = rangeCocneptIdsToBeDeleted.size() - 1;
         for (int deleteIndex = length; deleteIndex >= 0; deleteIndex--) {
            ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY))
                     .deleteSelectedRange(rangeCocneptIdsToBeDeleted.get(deleteIndex));
         }
      }
      setMessage(getText("execue.cube.dimension.deleteFromSelection.success"));

      return SUCCESS;
   }

   public String createCube () {
      try {
         if (log.isDebugEnabled()) {
            log.debug("Create cube method called");
            log.debug("Name: " + targetAsset.getName());
            log.debug("Description: " + targetAsset.getDescription());
            log.debug("DisplayName: " + targetAsset.getDisplayName());
            log.debug("model id: " + getApplicationContext().getModelId());
         }
         targetAsset.setName(ExecueStringUtil.getNormalizedName(ExecueCoreUtil.generateAlphanumericString(targetAsset
                  .getDisplayName())));
         if (getSdxServiceHandler().isAssetExists(getApplicationContext().getAppId(), targetAsset.getName())) {
            uiStatus = new UIStatus();
            uiStatus.addErrorMessage(getText("execue.global.exist.message", new String[] { targetAsset.getName() }));
            uiStatus.setStatus(false);
            return ERROR;
         }
         ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).setTargetAsset(targetAsset);
         ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).setModelId(getApplicationContext().getModelId());
         uiStatus = getCubeManagementServiceHandler().createCube(
                  (UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY), getApplicationContext());

      } catch (ExeCueException e) {
         log.error(e);
         if (uiStatus == null) {
            uiStatus = new UIStatus();
         }
         uiStatus.addErrorMessage(getText("execue.global.error", new String[] { getText("execue.errors.general") }));
         uiStatus.setStatus(false);
         return ERROR;
      }
      return SUCCESS;
   }

   public String updateCube () {
      try {
         uiStatus = getCubeManagementServiceHandler().updateCube(getExistingAssetId(), getApplicationContext());
      } catch (ExeCueException e) {
         log.error(e);
         if (uiStatus == null) {
            uiStatus = new UIStatus();
         }
         uiStatus.addErrorMessage(getText("execue.global.error", new String[] { getText("execue.errors.general") }));
         uiStatus.setStatus(false);
         return ERROR;
      }
      return SUCCESS;
   }

   public String refreshCube () {
      try {
         uiStatus = getCubeManagementServiceHandler().refreshCube(getExistingAssetId(), getApplicationContext());
      } catch (ExeCueException e) {
         log.error(e);
         if (uiStatus == null) {
            uiStatus = new UIStatus();
         }
         uiStatus.addErrorMessage(getText("execue.global.error", new String[] { getText("execue.errors.general") }));
         uiStatus.setStatus(false);
         return ERROR;
      }
      return SUCCESS;
   }

   public String editCube () {
      try {
         if (getHttpSession() != null && getHttpSession().get(UI_CUBE_CREATION_KEY) != null) {
            getHttpSession().remove(UI_CUBE_CREATION_KEY);
         }
         cubeCreationContext = getCubeManagementServiceHandler().getCubeCreationContextByAssetId(getExistingAssetId());
         getApplicationContext().setAssetId(cubeCreationContext.getSourceAsset().getId());
         // getPageResults();
      } catch (Exception e) {
         e.printStackTrace();
      }

      return SUCCESS;
   }

   public String showSnapshot () {
      try {

         defaultDimensions = getCubeManagementServiceHandler().getDefaultDimensions(baseAsset.getId());
         defaultStats = getCubeManagementServiceHandler().getDefaultStats();
         if (log.isDebugEnabled()) {
            log.debug("defaultDimensions : " + defaultDimensions);
            log.debug("defaultStat : " + defaultStats);
         }

      } catch (ExeCueException e) {
         e.printStackTrace();
      }
      setLocalUICubeCreation((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY));
      return SUCCESS;
   }

   public String showCubeHistory () {

      try {
         if (JobStatus.SUCCESS.equals(getCubeJobRequestStatus().getJobStatus())
                  || JobStatus.FAILURE.equals(getCubeJobRequestStatus().getJobStatus())) {
            cubeJobHistory = getJobStatusServiceHandler().getJobHistoryOperationalStatus(
                     getCubeJobRequestStatus().getId());
         } else {
            cubeJobHistory = getJobStatusServiceHandler().getJobOperationalStatus(getCubeJobRequestStatus().getId());
         }
      } catch (ExeCueException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public String showCubeRequestStatus () {

      // populate the status of last cube request
      try {
         cubeJobRequestStatus = getJobStatusServiceHandler().getJobRequestStatus(JobType.CUBE_CREATION);
         if (cubeJobRequestStatus == null) {
            cubeJobRequestStatus = new UIJobRequestStatus();
            addActionMessage(getText("execue.cube.job.previous.request.empty"));
            return SUCCESS;
         }
         CubeCreationContext context = (CubeCreationContext) new XStream().fromXML(cubeJobRequestStatus
                  .getRequestData());
         cubeRequestDataInfo = new UICubeRequestDataInfo();
         cubeRequestDataInfo.setCubeName(context.getTargetAsset().getName());
         showCubeHistory();
         cubeRequestDataInfo.setCubeRequestedDimensions(context.getSimpleLookupDimensions());
         if (JobStatus.FAILURE.equals(cubeJobRequestStatus.getJobStatus())) {
            showCubeHistory();
         }

      } catch (ExeCueException e) {
         e.printStackTrace();
         return ERROR;
      }
      return SUCCESS;
   }

   public String showCubes () {
      try {
         setCubes(getCubeManagementServiceHandler().getAssetsByTypeForApplication(getApplicationContext().getAppId()));
      } catch (ExeCueException e) {
         setCubes(new ArrayList<UIAsset>());
         e.printStackTrace();
         return ERROR;
      }

      return SUCCESS;
   }

   // Private methods
   private void adjustRangeIds () {
      int index = 0;
      for (Range range : ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).getSelectedRanges()) {
         range.setId(new Long(index));
         index++;
      }
   }

   private boolean existsInSelectedDimensions () {
      boolean existsInSelection = false;

      int index = 0;
      for (Concept selectedConcept : ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY))
               .getSelectedConcepts()) {
         if (selectedConcept.getId().equals(getConcept().getId())) {
            dimensionType = "SL";
            selectedIndex = index;
            existsInSelection = true;
            break;
         }
         index++;
      }

      index = 0;
      for (Range selectedRange : ((UICubeCreation) getHttpSession().get(UI_CUBE_CREATION_KEY)).getSelectedRanges()) {

         if (selectedRange.getConceptBedId().equals(getConcept().getBedId())) {
            dimensionType = "RL";
            selectedIndex = index;
            existsInSelection = true;
            break;
         }
         index++;
      }

      return existsInSelection;
   }

   // Mutators
   public Range getRange () {
      return range;
   }

   public void setRange (Range range) {
      this.range = range;
   }

   public List<Concept> getConcepts () {
      return concepts;
   }

   public void setConcepts (List<Concept> concepts) {
      this.concepts = concepts;
   }

   public Concept getConcept () {
      return concept;
   }

   public void setConcept (Concept concept) {
      this.concept = concept;
   }

   public UICubeCreation getLocalUICubeCreation () {
      return localUICubeCreation;
   }

   public void setLocalUICubeCreation (UICubeCreation localUICubeCreation) {
      this.localUICubeCreation = localUICubeCreation;
   }

   public List<Long> getDimensionIndexesForDeletion () {
      return dimensionIndexesForDeletion;
   }

   public void setDimensionIndexesForDeletion (List<Long> dimensionIndexesForDeletion) {
      this.dimensionIndexesForDeletion = dimensionIndexesForDeletion;
   }

   public List<Long> getRangeIndexesForDeletion () {
      return rangeIndexesForDeletion;
   }

   public void setRangeIndexesForDeletion (List<Long> rangeIndexesForDeletion) {
      this.rangeIndexesForDeletion = rangeIndexesForDeletion;
   }

   public List<UIRangeDetail> getRangeDetailList () {
      return rangeDetailList;
   }

   public void setRangeDetailList (List<UIRangeDetail> rangeDetailList) {
      this.rangeDetailList = rangeDetailList;
   }

   public Asset getBaseAsset () {
      return baseAsset;
   }

   public void setBaseAsset (Asset baseAsset) {
      this.baseAsset = baseAsset;
   }

   public List<UIInstanceMember> getInstanceMembrs () {
      return instanceMembrs;
   }

   public void setInstanceMembrs (List<UIInstanceMember> instanceMembrs) {
      this.instanceMembrs = instanceMembrs;
   }

   public String getDimensionType () {
      return dimensionType;
   }

   public void setDimensionType (String dimensionType) {
      this.dimensionType = dimensionType;
   }

   public int getSelectedIndex () {
      return selectedIndex;
   }

   public void setSelectedIndex (int selectedIndex) {
      this.selectedIndex = selectedIndex;
   }

   public Asset getTargetAsset () {
      return targetAsset;
   }

   public void setTargetAsset (Asset targetAsset) {
      this.targetAsset = targetAsset;
   }

   public List<String> getDefaultStats () {
      return defaultStats;
   }

   public void setDefaultStats (List<String> defaultStats) {
      this.defaultStats = defaultStats;
   }

   public List<String> getDefaultDimensions () {
      return defaultDimensions;
   }

   public void setDefaultDimensions (List<String> defaultDimensions) {
      this.defaultDimensions = defaultDimensions;
   }

   public UIJobRequestStatus getCubeJobRequestStatus () {
      return cubeJobRequestStatus;
   }

   public void setCubeJobRequestStatus (UIJobRequestStatus cubeJobRequestStatus) {
      this.cubeJobRequestStatus = cubeJobRequestStatus;
   }

   public UICubeRequestDataInfo getCubeRequestDataInfo () {
      return cubeRequestDataInfo;
   }

   public void setCubeRequestDataInfo (UICubeRequestDataInfo cubeRequestDataInfo) {
      this.cubeRequestDataInfo = cubeRequestDataInfo;
   }

   public List<UIJobHistory> getCubeJobHistory () {
      return cubeJobHistory;
   }

   public void setCubeJobHistory (List<UIJobHistory> cubeJobHistory) {
      this.cubeJobHistory = cubeJobHistory;
   }

   /**
    * @return the cubes
    */
   public List<UIAsset> getCubes () {
      return cubes;
   }

   /**
    * @param cubes
    *           the cubes to set
    */
   public void setCubes (List<UIAsset> cubes) {
      this.cubes = cubes;
   }

   /**
    * @return the cubeCreationContext
    */
   public CubeCreationContext getCubeCreationContext () {
      return cubeCreationContext;
   }

   /**
    * @param cubeCreationContext
    *           the cubeCreationContext to set
    */
   public void setCubeCreationContext (CubeCreationContext cubeCreationContext) {
      this.cubeCreationContext = cubeCreationContext;
   }

   /**
    * @return the assets
    */
   public List<Asset> getAssets () {
      return assets;
   }

   /**
    * @param assets
    *           the assets to set
    */
   public void setAssets (List<Asset> assets) {
      this.assets = assets;
   }

   /**
    * @return the existingAssetId
    */
   public Long getExistingAssetId () {
      return existingAssetId;
   }

   /**
    * @param existingAssetId
    *           the existingAssetId to set
    */
   public void setExistingAssetId (Long existingAssetId) {
      this.existingAssetId = existingAssetId;
   }

   /**
    * @return the sourceName
    */
   public String getSourceName () {
      return sourceName;
   }

   /**
    * @param sourceName
    *           the sourceName to set
    */
   public void setSourceName (String sourceName) {
      this.sourceName = sourceName;
   }

   /**
    * @return the jobRequestId
    */
   public Long getJobRequestId () {
      return jobRequestId;
   }

   /**
    * @param jobRequestId
    *           the jobRequestId to set
    */
   public void setJobRequestId (Long jobRequestId) {
      this.jobRequestId = jobRequestId;
   }

   /**
    * @return the uiStatus
    */
   public UIStatus getUiStatus () {
      return uiStatus;
   }

   /**
    * @param uiStatus the uiStatus to set
    */
   public void setUiStatus (UIStatus uiStatus) {
      this.uiStatus = uiStatus;
   }

   /**
    * @return the asset
    */
   public Asset getAsset () {
      return asset;
   }

   /**
    * @param asset the asset to set
    */
   public void setAsset (Asset asset) {
      this.asset = asset;
   }

}
