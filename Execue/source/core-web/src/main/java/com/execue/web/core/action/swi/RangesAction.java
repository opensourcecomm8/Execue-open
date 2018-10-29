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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Range;
import com.execue.core.common.bean.entity.RangeDetail;
import com.execue.core.exception.ExeCueException;
import com.execue.core.util.ExeCueXMLUtils;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIRangeDetail;
import com.execue.handler.comparator.UIRangeDetailOrderComparator;

public class RangesAction extends SWIAction {

   /**
    * 
    */
   private static final long   serialVersionUID = -3610457086437134399L;
   private static final Logger log              = Logger.getLogger(RangesAction.class);
   private Long                rangeId;
   private Range               range;
   private List<Range>         userDefinedRanges;
   private List<Concept>       concepts;
   private Long                conceptID;
   private Long                conceptBedId;
   private String              conceptName;
   private List<UIRangeDetail> rangeDetailList;
   private RangeStatus         rangeStatus;
   private List<Long>          rangeIdsForDeletion;
   private String              paginationType;
   public static final int     PAGESIZE         = 9;
   public static final int     numberOfLinks    = 4;
   private String              requestedPage;

   public List<Long> getRangeIdsForDeletion () {
      return rangeIdsForDeletion;
   }

   public void setRangeIdsForDeletion (List<Long> rangeIdsForDeletion) {
      this.rangeIdsForDeletion = rangeIdsForDeletion;
   }

   public RangeStatus getRangeStatus () {
      return rangeStatus;
   }

   public void setRangeStatus (RangeStatus rangeStatus) {
      this.rangeStatus = rangeStatus;
   }

   public List<UIRangeDetail> getRangeDetailList () {
      return rangeDetailList;
   }

   public void setRangeDetailList (List<UIRangeDetail> rangeDetailList) {
      this.rangeDetailList = rangeDetailList;
   }

   public String getConceptName () {
      return conceptName;
   }

   public void setConceptName (String conceptName) {
      this.conceptName = conceptName;
   }

   public Long getConceptID () {
      return conceptID;
   }

   public void setConceptID (Long conceptID) {
      this.conceptID = conceptID;
   }

   public List<Concept> getConcepts () {
      return concepts;
   }

   public void setConcepts (List<Concept> concepts) {
      this.concepts = concepts;
   }

   public Long getRangeId () {
      return rangeId;
   }

   public void setRangeId (Long rangeId) {
      this.rangeId = rangeId;
   }

   public Range getRange () {
      return range;
   }

   public void setRange (Range range) {
      this.range = range;
   }

   public List<Range> getUserDefinedRanges () {
      return userDefinedRanges;
   }

   public void setUserDefinedRanges (List<Range> userDefinedRanges) {
      this.userDefinedRanges = userDefinedRanges;
   }

   // Action Methods
   public String input () {
      if (log.isDebugEnabled()) {
         log.debug("input method called");
      }
      try {
         // setExistingRanges(getPreferencesServiceHandler().getRanges());
         setConcepts(getPreferencesServiceHandler().getConceptsForRanges(getApplicationContext().getModelId()));
         log.debug("Concepts size is " + concepts.size());

         if (paginationType != null && paginationType.equalsIgnoreCase("conceptsForRanges")) {
            paginationForConcepts();
         }

      } catch (ExeCueException execueException) {
         log.error(execueException, execueException);
         if (log.isDebugEnabled()) {
            log.debug("returning error");
         }
         return ERROR;
      }
      if (log.isDebugEnabled()) {
         log.debug("returning success");
      }
      return SUCCESS;
   }

   private void paginationForConcepts () {
      if (requestedPage == null)
         requestedPage = "1";
      getHttpSession().put("CONCEPTSFORRANGES", concepts);

      int tempSize = 0;
      if (concepts.size() <= PAGESIZE)
         tempSize = concepts.size();
      else
         tempSize = PAGESIZE;
      log.info("displaying initial sublist");
      concepts = concepts.subList(0, tempSize);
   }

   @SuppressWarnings ("unchecked")
   public String showSubConceptsforRanges () {

      int reqPageNo = Integer.parseInt(getRequestedPage());

      int fromIndex = 1;
      int toIndex = 1;

      if (paginationType != null && paginationType.equalsIgnoreCase("conceptsForRanges")) {
         List<Concept> conceptList = (List<Concept>) getHttpSession().get("CONCEPTSFORRANGES");

         int tempTotCount = (conceptList.size() / PAGESIZE);
         int rmndr = conceptList.size() % PAGESIZE;
         if (rmndr != 0) {
            tempTotCount++;
         }

         if (reqPageNo <= tempTotCount) {
            fromIndex = ((reqPageNo - 1) * PAGESIZE);
            toIndex = reqPageNo * PAGESIZE;
            if (toIndex > conceptList.size())
               toIndex = (conceptList.size());
         }

         log.info("Getting Columns SubList from -> " + fromIndex + " to " + toIndex);
         concepts = conceptList.subList(fromIndex, toIndex);
      }
      return SUCCESS;
   }

   public String retrieveRangeForConcept () {

      if (log.isDebugEnabled()) {
         log.debug("Getting ranges for concept ");
      }
      /*
       * Concept concept = new Concept(); concept.setName(conceptName); setDummyData(); getRange().setConcept(concept);
       */
      try {
         range = getPreferencesServiceHandler().getSuggestedRangesForConceptByApplication(
                  getApplicationContext().getAppId(), getApplicationContext().getModelId(), conceptBedId);
         setRangeDetailList(getPreferencesServiceHandler().transformRangeDetails(range.getRangeDetails()));
         Collections.sort(getRangeDetailList(), new UIRangeDetailOrderComparator());
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }

      return SUCCESS;
   }

   public String userDefinedRanges () {
      try {
         if (log.isDebugEnabled()) {
            log.debug("Getting ranges for concept ");
         }
         setUserDefinedRanges(getPreferencesServiceHandler().getRanges(getApplicationContext().getModelId()));
         if (log.isDebugEnabled()) {
            log.debug("range size is " + getUserDefinedRanges().size());
         }
         return SUCCESS;
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
   }

   public String updateRange () {
      try {
         if (log.isDebugEnabled()) {
            log.debug("In update, Range " + ExeCueXMLUtils.getXMLStringFromObject(range));
            log.debug("In update, Range Details " + ExeCueXMLUtils.getXMLStringFromObject(rangeDetailList));
            Collections.sort(rangeDetailList, new UIRangeDetailOrderComparator());
            for (UIRangeDetail uiRangeDetail : rangeDetailList) {
               log.debug("Order:" + uiRangeDetail.getOrder());
               log.debug("Llimit:" + uiRangeDetail.getLowerLimit());
               log.debug("ULimit:" + uiRangeDetail.getUpperLimit());
            }
         }
         setRangeStatus(new RangeStatus());

         if (!validateDataIntegrity(rangeDetailList)) {
            getRangeStatus().setStatus("error");
            getRangeStatus().setMessage(getText("execue.range.rangeDetail.failure"));
         } else if (!validateLimits(rangeDetailList)) {
            getRangeStatus().setStatus("error");
            getRangeStatus().setMessage(getText("execue.range.LLimitULimit.failure"));
         } else if (!validateRangeName(range)) {
            getRangeStatus().setStatus("error");
            getRangeStatus().setMessage(getText("execue.range.name.required"));
         } else if (!validateRangeLength(range)) {
            getRangeStatus().setStatus("error");
            getRangeStatus().setMessage(getText("execue.range.name.length.mismatch"));
         } else {

            Set<RangeDetail> rangeDetails = getPreferencesServiceHandler().transformRangeDetails(getRangeDetailList());
            range.setRangeDetails(rangeDetails);
            if (range.getId() == null) {
               getPreferencesServiceHandler().createRange(getApplicationContext().getModelId(), range);
               getRangeStatus().setStatus("success");
               getRangeStatus().setMessage(getText("execue.range.create.success"));
               getRangeStatus().setRangeId(range.getId());
            } else {
               getPreferencesServiceHandler().updateRange(getApplicationContext().getModelId(), range);
               getRangeStatus().setStatus("success");
               getRangeStatus().setMessage(getText("execue.range.update.success"));
               getRangeStatus().setRangeId(range.getId());
            }
         }
      } catch (ExeCueException exeCueException) {
         getRangeStatus().setStatus("error");
         if (exeCueException.getCode() == 900018) {
            getRangeStatus().setMessage(exeCueException.getMessage());
         } else {
            log.error(exeCueException, exeCueException);
            if (log.isDebugEnabled()) {
               log.debug("returning error");
            }
            getRangeStatus().setMessage(getText("execue.range.create.success"));
         }
      }
      return SUCCESS;
   }

   private boolean validateLimits (List<UIRangeDetail> rangeDetailList) {
      boolean isValidLimits = true;
      int index = 0;
      for (UIRangeDetail uiRangeDetail : rangeDetailList) {
         if (index != 0 && index != rangeDetailList.size() - 1) {
            if (Double.parseDouble(uiRangeDetail.getLowerLimit()) > Double.parseDouble(uiRangeDetail.getUpperLimit())) {
               isValidLimits = false;
               break;
            }
         }
         index++;
      }
      return isValidLimits;
   }

   private boolean validateRangeName (Range range) {
      boolean rangeNameExists = true;
      if (ExecueCoreUtil.isEmpty(range.getName()))
         rangeNameExists = false;
      return rangeNameExists;
   }

   private boolean validateRangeLength (Range range) {
      boolean validRangeLength = true;
      if (range.getName().length() > 255 || range.getName().length() < 5) {
         validRangeLength = false;
      }
      return validRangeLength;
   }

   private boolean validateDataIntegrity (List<UIRangeDetail> rangeDetailList) {
      boolean isRangeDetailDataValid = true;
      try {
         int index = 0;
         for (UIRangeDetail uiRangeDetail : rangeDetailList) {
            if (index == 0) {
               Double.parseDouble(uiRangeDetail.getUpperLimit());
            } else if (index == rangeDetailList.size() - 1) {
               Double.parseDouble(uiRangeDetail.getLowerLimit());
            } else {
               Double.parseDouble(uiRangeDetail.getLowerLimit());
               Double.parseDouble(uiRangeDetail.getUpperLimit());
            }
            index++;
         }
      } catch (NumberFormatException numberFormatException) {
         isRangeDetailDataValid = false;
      }
      return isRangeDetailDataValid;
   }

   public String deleteRanges () {
      setRangeStatus(new RangeStatus());

      if (log.isDebugEnabled()) {
         log.debug("Ranges to be delted count  ");
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(rangeIdsForDeletion)) {
         try {
            getPreferencesServiceHandler().deleteRanges(rangeIdsForDeletion);
            getRangeStatus().setStatus("success");
            getRangeStatus().setMessage(getText("execue.range.delete.success"));
         } catch (ExeCueException execueException) {
            getRangeStatus().setStatus("error");
            log.error(execueException);
            getRangeStatus().setMessage(getText("execue.range.delete.failure"));
         }
      } else {
         getRangeStatus().setStatus("error");
         getRangeStatus().setMessage(getText("execue.range.delete.notSelected"));
      }
      return SUCCESS;
   }

   public String getRangeById () {
      try {
         range = getPreferencesServiceHandler().getRangeById(rangeId);
         setConceptName(range.getConceptDisplayName());
         setRangeDetailList(getPreferencesServiceHandler().transformRangeDetails(range.getRangeDetails()));
         Collections.sort(rangeDetailList, new UIRangeDetailOrderComparator());

      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String getPaginationType () {
      return paginationType;
   }

   public void setPaginationType (String paginationType) {
      this.paginationType = paginationType;
   }

   public String getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (String requestedPage) {
      this.requestedPage = requestedPage;
   }

   /**
    * @return the conceptBedId
    */
   public Long getConceptBedId () {
      return conceptBedId;
   }

   /**
    * @param conceptBedId the conceptBedId to set
    */
   public void setConceptBedId (Long conceptBedId) {
      this.conceptBedId = conceptBedId;
   }

}
