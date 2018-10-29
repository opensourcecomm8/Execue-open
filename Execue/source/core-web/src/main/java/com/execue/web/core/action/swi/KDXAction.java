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
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.entity.RIOntoTerm;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.Stat;
import com.execue.core.common.bean.entity.comparator.ConceptDisplaNameComparator;
import com.execue.core.common.type.PaginationType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.ExeCueExceptionCodes;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIParallelWord;
import com.execue.handler.bean.UIStatus;
import com.execue.swi.exception.SWIExceptionCodes;

public class KDXAction extends SWIAction implements RequestAware {

   private static Logger        log                       = Logger.getLogger(KDXAction.class);
   private static final long    serialVersionUID          = 1L;
   private static final String  ADD                       = "add";
   private static final String  UPDATE                    = "update";
   private Concept              concept;
   private Instance             instance;
   private Relation             relation;
   private List<Concept>        concepts;
   private List<Instance>       instances;
   private String               mode;
   private Set<Stat>            stats;
   private List<Long>           statList;
   private List<Long>           displayTypeList;
   private String               dataFormat;
   private String               unit;
   private KeyWord              keyWord;
   private List<UIParallelWord> uiParallelWords;
   private List<RIOntoTerm>     riOntoTerms;
   private List<SFLTerm>        sflTerms;
   private PaginationType       paginationType;
   private int                  requestedPage;
   private Page                 pageDetail;
   private Map<Object, Object>  httpRequest;
   // TODO:-JT- need to read from the configuration.
   private static final String  PAGINATION                = "pagination";
   private static final String  PAGINATION_TYPE           = "paginationType";
   private static final String  BUSINESSTERMS             = "businessTerms";
   private static final String  BUSINESSTERMS_SUBLIST     = "businessTermsSubList";
   private static final String  START_WITH                = "startWith";
   private static final String  CONTAINS                  = "contains";
   public static final int      PAGESIZE                  = 11;
   public static final int      NUMBER_OF_LINKS           = 4;
   public static final int      NUMBER_OF_LINKS_INSTANCES = 15;
   public static final int      DEFAULT_REQUESTED_PAGE    = 1;
   private String               searchString;
   private String               searchType                = "ALL";
   private UIStatus             uiStatus;

   public List<Long> getDisplayTypeList () {
      return displayTypeList;
   }

   public void setDisplayTypeList (List<Long> displayTypeList) {
      this.displayTypeList = displayTypeList;
   }

   public List<Long> getStatList () {
      return statList;
   }

   public void setStatList (List<Long> statList) {
      this.statList = statList;
   }

   public Set<Stat> getStats () {
      return stats;
   }

   public void setStats (Set<Stat> stats) {
      this.stats = stats;
   }

   public String getMode () {
      return mode;
   }

   public void setMode (String mode) {
      this.mode = mode;
   }

   public Concept getConcept () {
      return concept;
   }

   public void setConcept (Concept concept) {
      this.concept = concept;
   }

   public Instance getInstance () {
      return instance;
   }

   public void setInstance (Instance instance) {
      this.instance = instance;
   }

   public List<Concept> getConcepts () {
      return concepts;
   }

   public void setConcepts (List<Concept> concepts) {
      this.concepts = concepts;
   }

   public List<Instance> getInstances () {
      return instances;
   }

   public void setInstances (List<Instance> instances) {
      this.instances = instances;
   }

   // Action Methods

   public String retrieveInstance () {
      try {
         if (log.isDebugEnabled())
            log.debug("In get Instance, retrieving concept data " + concept);

         log.debug("In get Instance, retrieving instance data " + instance.getId());
         setConcept(getKdxServiceHandler().getConcept(concept.getId()));
         setInstance(getKdxServiceHandler().getInstance(instance.getId()));

         setMode(UPDATE);
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String retrieveRelation () {
      try {
         if (log.isDebugEnabled())

            log.debug("In get Relation, retrieving relation data " + relation.getId());
         setRelation(getKdxServiceHandler().getRelation(relation.getId()));
         setMode(UPDATE);
      } catch (ExeCueException exeCueException) {
         log.error(exeCueException, exeCueException);
         return ERROR;
      }
      return SUCCESS;
   }

   public String createInstance () {
      try {
         if (log.isDebugEnabled())
            log.debug("Instance is " + null);
         conceptDetails();
         if (mode != null) {
            if (mode.equalsIgnoreCase(ADD)) {
               getKdxServiceHandler().createInstance(getApplicationContext().getAppId(),
                        getApplicationContext().getModelId(), getApplicationContext().getAppSourceType(),
                        getConcept().getId(), instance);
               addActionMessage(getText("execue.instance.create.success"));
            } else if (mode.equalsIgnoreCase(UPDATE)) {
               getKdxServiceHandler().updateInstance(getApplicationContext().getAppId(),
                        getApplicationContext().getModelId(), getApplicationContext().getAppSourceType(),
                        getConcept().getId(), instance);
               addActionMessage(getText("execue.instance.update.success"));
            }
         }
      } catch (ExeCueException execueException) {
         if (execueException.getCode() == SWIExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionError(getText("execue.global.exist.message", new String[] { instance.getDisplayName() }));
         } else if (execueException.getCode() == SWIExceptionCodes.RESERVE_WORD_MATCH) {
            addActionError(getText("execue.kdx.reservedword.message", new String[] { instance.getDisplayName() }));
         } else if (mode.equalsIgnoreCase(ADD)) {
            setMessage(getText("execue.instance.create.failure"));
            addActionError(getText("execue.instance.create.failure"));
         } else if (mode.equalsIgnoreCase(UPDATE)) {
            setMessage(getText("execue.instance.update.failure"));
            addActionError(getText("execue.instance.update.failure"));
         } else {
            addActionError(getText("execue.global.error", execueException.getMessage()));
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteInstanceHeirarchy () {
      Long modelId = getApplicationContext().getModelId();
      uiStatus = new UIStatus();
      try {
         getKdxServiceHandler().deleteInstanceHeirarchy(modelId, concept.getId(), instance.getId());
         uiStatus.setMessage(getText("execue.relation.delete.success"));
      } catch (ExeCueException execueException) {
         uiStatus.setStatus(false);
         uiStatus.addErrorMessage("Error: " + execueException.getMessage());
         if (log.isDebugEnabled()) {
            log.debug(execueException.getMessage());
         }
      }
      return SUCCESS;
   }

   public String deleteRelationHeirarchy () {
      Long modelId = getApplicationContext().getModelId();
      uiStatus = new UIStatus();
      try {
         getKdxServiceHandler().deleteRelationHeirarchy(modelId, relation.getId());
         uiStatus.setMessage(getText("execue.instance.delete.success"));
      } catch (ExeCueException execueException) {
         uiStatus.setStatus(false);
         uiStatus.addErrorMessage("Error: " + execueException.getMessage());
         if (log.isDebugEnabled()) {
            log.debug(execueException.getMessage());
         }
      }
      return SUCCESS;
   }

   // public String showInstances () {
   //      log.debug("In show instances method");
   //      try {
   //         if (httpRequest != null) {
   //            pageDetail = new Page(Long.valueOf(DEFAULT_REQUESTED_PAGE), Long.valueOf(PAGESIZE));
   //            pageDetail.setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS_INSTANCES));
   //            setPaginationType(PaginationType.Instances);
   //            setConcept(getKdxServiceHandler().getConcept(concept.getId()));
   //            setInstances(getKdxServiceHandler().getInstancesByPage(getApplicationContext().getModelId(),
   //                     concept.getId(), pageDetail));
   //            httpRequest.put(PAGINATION, pageDetail);
   //            httpRequest.put(PAGINATION_TYPE, PaginationType.Instances);
   //         }
   //         if (getInstances().size() > 0) {
   //            return SUCCESS;
   //         }
   //         return "create";
   //      } catch (ExeCueException execueException) {
   //         log.error(execueException, execueException);
   //         return ERROR;
   //      }
   //   }
   //
   //   public String showSubInstances () {
   //      try {
   //         Long reqPageNo = Long.valueOf(getRequestedPage());
   //         pageDetail.setRequestedPage(reqPageNo);
   //         if (httpRequest != null) {
   //            httpRequest.put(PAGINATION, pageDetail);
   //            httpRequest.put(PAGINATION_TYPE, PaginationType.Instances);
   //         }
   //         setConcept(getKdxServiceHandler().getConcept(concept.getId()));
   //         setInstances(getKdxServiceHandler().getInstancesByPage(getApplicationContext().getModelId(), concept.getId(),
   //                  pageDetail));
   //      } catch (ExeCueException e) {
   //         e.printStackTrace();
   //      }
   //      return SUCCESS;
   //   }

   public String showHierarchy () {

      return SUCCESS;
   }

   public String showRelation () {

      return SUCCESS;
   }

   public String input () {
      try {
         if (mode == null) {
            setConcepts(getKdxServiceHandler().getConcepts(getApplicationContext().getModelId()));
            preparePaginationForBTs();
            Collections.sort(getConcepts(), new ConceptDisplaNameComparator());
         }
         if (concept != null) {
            if (concept.getId() == null) {
               setStats(getKdxServiceHandler().getAllStats());
            }
            if (concept.getId() != null) {
               log.debug("getting instance data " + mode);
               if (mode.equalsIgnoreCase(ADD))
                  setConcept(getKdxServiceHandler().getConcept(concept.getId()));
               return INPUT;
            }
            concept = null;
         } else {
            setStats(getKdxServiceHandler().getAllStats());
         }
      } catch (ExeCueException execueException) {
         log.error(execueException, execueException);
         return ERROR;
      }
      return INPUT;
   }

   private void preparePaginationForBTs () {
      pageDetail = new Page(Long.valueOf(DEFAULT_REQUESTED_PAGE), Long.valueOf(PAGESIZE));
      pageDetail.setNumberOfLinks(Long.valueOf(4));
      setPaginationType(PaginationType.Concepts);
      if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
         if (getHttpSession() != null) {
            getHttpSession().put(BUSINESSTERMS, concepts);
         }
         int size = concepts.size();
         int tempSize = (size <= PAGESIZE) ? size : PAGESIZE;
         concepts = concepts.subList(0, tempSize);
         pageDetail.setRecordCount(Long.valueOf(size));
         if (httpRequest != null) {
            httpRequest.put(PAGINATION, pageDetail);
            httpRequest.put(PAGINATION_TYPE, PaginationType.Concepts);
         }

      }
      log.info("displaying initial sublist");
   }

   @SuppressWarnings ("unchecked")
   public String getSubBusinessTerms () {
      int fromIndex = 1;
      int toIndex = 1;
      int reqPageNo = getRequestedPage();
      if (pageDetail != null) {
         pageDetail.setRequestedPage(Long.valueOf(getRequestedPage()));
      }
      List<Concept> conceptList = new ArrayList<Concept>();
      if (getHttpSession() != null) {
         conceptList = (getHttpSession().get(BUSINESSTERMS_SUBLIST) != null) ? (List<Concept>) getHttpSession().get(
                  BUSINESSTERMS_SUBLIST) : (List<Concept>) getHttpSession().get(BUSINESSTERMS);
      }
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
      if (httpRequest != null) {
         httpRequest.put(PAGINATION, pageDetail);
         httpRequest.put(PAGINATION_TYPE, paginationType);
      }
      log.info("Getting Columns SubList from -> " + fromIndex + " to " + toIndex);
      concepts = conceptList.subList(fromIndex, toIndex);
      return SUCCESS;

   }

   public String getBusinessTermsBySearchString () {
      if (getHttpSession() != null) {
         List<Concept> businessTermsSublist = new ArrayList<Concept>();
         List<Concept> concepts = (List<Concept>) getHttpSession().get(BUSINESSTERMS);
         if (ExecueCoreUtil.isCollectionNotEmpty(concepts)) {
            if (START_WITH.equalsIgnoreCase(searchType)) {
               for (Concept concept : concepts) {
                  if (concept.getDisplayName().toLowerCase().startsWith(searchString.toLowerCase())) {
                     businessTermsSublist.add(concept);
                  }
               }
            } else if (CONTAINS.equalsIgnoreCase(searchType)) {
               for (Concept concept : concepts) {
                  if (concept.getDisplayName().toLowerCase().contains(searchString.toLowerCase())) {
                     businessTermsSublist.add(concept);
                  }
               }
            } else {
               businessTermsSublist.addAll(concepts);
            }

         }
         setConcepts(businessTermsSublist);
         preparePaginationForBTSubList(businessTermsSublist);
      }

      return SUCCESS;
   }

   private void preparePaginationForBTSubList (List<Concept> businessTermsSubList) {
      pageDetail = new Page(Long.valueOf(DEFAULT_REQUESTED_PAGE), Long.valueOf(PAGESIZE));
      pageDetail.setNumberOfLinks(Long.valueOf(4));
      setPaginationType(PaginationType.Concepts);
      List<Concept> businessTermsToBePaginated = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTermsSubList)) {
         if (getHttpSession() != null) {
            if (getHttpSession().get(BUSINESSTERMS_SUBLIST) != null) {
               getHttpSession().remove(BUSINESSTERMS_SUBLIST);
            }
            getHttpSession().put(BUSINESSTERMS_SUBLIST, businessTermsSubList);
            businessTermsToBePaginated = (List<Concept>) getHttpSession().get(BUSINESSTERMS_SUBLIST);
         }
         int size = businessTermsToBePaginated.size();
         int tempSize = (size <= PAGESIZE) ? size : PAGESIZE;
         concepts = businessTermsToBePaginated.subList(0, tempSize);
         pageDetail.setRecordCount(Long.valueOf(size));
         if (httpRequest != null) {
            httpRequest.put(PAGINATION, pageDetail);
            httpRequest.put(PAGINATION_TYPE, PaginationType.Concepts);
         }

      }
      log.info("displaying initial sublist");

   }

   private void conceptDetails () throws ExeCueException {
      Concept populatedConcept = getKdxServiceHandler().getConcept(concept.getId());
      setConcept(populatedConcept);
      setStats(getKdxServiceHandler().getAllStats());
   }

   public String getKDXWords () {
      try {
         keyWord = getPreferencesServiceHandler().getKeyWord(keyWord.getWord(), getApplicationContext().getModelId());
         if (keyWord != null) {
            uiParallelWords = getPreferencesServiceHandler().getParallelWordsById(keyWord.getId());
            sflTerms = getKdxServiceHandler().getSFLTermsByKeyWord(keyWord.getWord());
            riOntoTerms = getPreferencesServiceHandler().getRIOntoTermsByKeyWord(keyWord.getWord());
         }

      } catch (ExeCueException exeCueException) {
         setErrorMessage(getText("execue.errors.general"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String createRelation () {
      try {
         if (log.isDebugEnabled())
            log.debug("Relation is " + null);
         if (relation != null) {
            if (relation.getId() != null) {
               getKdxServiceHandler().updateRelation(getApplicationContext().getModelId(), relation);
               addActionMessage(getText("execue.relation.update.success"));
            } else {
               getKdxServiceHandler().createRelation(getApplicationContext().getModelId(), relation);
               addActionMessage(getText("execue.relation.create.success"));

            }
         }
      } catch (ExeCueException execueException) {
         if (execueException.getCode() == ExeCueExceptionCodes.ENTITY_ALREADY_EXISTS) {
            addActionError(getText("execue.global.exist.message", new String[] { relation.getDisplayName() }));
         } else {
            addActionError(getText("execue.global.error", execueException.getMessage()));
         }
         return ERROR;
      }
      return SUCCESS;
   }

   public String getDataFormat () {
      return dataFormat;
   }

   public void setDataFormat (String dataFormat) {
      this.dataFormat = dataFormat;
   }

   public String getUnit () {
      return unit;
   }

   public void setUnit (String unit) {
      this.unit = unit;
   }

   public KeyWord getKeyWord () {
      return keyWord;
   }

   public void setKeyWord (KeyWord keyWord) {
      this.keyWord = keyWord;
   }

   public List<UIParallelWord> getUiParallelWords () {
      return uiParallelWords;
   }

   public void setUiParallelWords (List<UIParallelWord> uiParallelWords) {
      this.uiParallelWords = uiParallelWords;
   }

   public List<SFLTerm> getSflTerms () {
      return sflTerms;
   }

   public void setSflTerms (List<SFLTerm> sflTerms) {
      this.sflTerms = sflTerms;
   }

   public List<RIOntoTerm> getRiOntoTerms () {
      return riOntoTerms;
   }

   public void setRiOntoTerms (List<RIOntoTerm> riOntoTerms) {
      this.riOntoTerms = riOntoTerms;
   }

   public Page getPageDetail () {
      return pageDetail;
   }

   public void setPageDetail (Page pageDetail) {
      this.pageDetail = pageDetail;
   }

   public Map<Object, Object> getHttpRequest () {
      return httpRequest;
   }

   public void setRequest (Map request) {
      this.httpRequest = request;

   }

   public String getSearchString () {
      return searchString;
   }

   public void setSearchString (String searchString) {
      this.searchString = searchString;
   }

   public String getSearchType () {
      return searchType;
   }

   public void setSearchType (String searchType) {
      this.searchType = searchType;
   }

   public void setPaginationType (PaginationType paginationType) {
      this.paginationType = paginationType;
   }

   public void setRequestedPage (int requestedPage) {
      this.requestedPage = requestedPage;
   }

   public PaginationType getPaginationType () {
      return paginationType;
   }

   public int getRequestedPage () {
      return requestedPage;
   }

   public Relation getRelation () {
      return relation;
   }

   public void setRelation (Relation relation) {
      this.relation = relation;
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

}
