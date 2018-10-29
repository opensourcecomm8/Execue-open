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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.RequestAware;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.entity.KeyWord;
import com.execue.core.common.bean.governor.BusinessEntityInfo;
import com.execue.core.common.type.CheckType;
import com.execue.core.common.type.PaginationType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIInvalidParallelWord;
import com.execue.handler.bean.UIParallelWord;
import com.execue.handler.bean.UIParallelWordValidationResult;

public class ParallelWordsAction extends SWIAction implements RequestAware {

   private static final long        serialVersionUID          = 1L;
   private static final Logger      log                       = Logger.getLogger(ParallelWordsAction.class);
   private KeyWord                  keyWord;
   private List<UIParallelWord>     uiParallelWords;
   private List<KeyWord>            keyWords;
   private String                   keyWordName;
   private List<BusinessEntityInfo> businessTerms;
   private List<Long>               keyWordIdsForDeletion;
   private String                   mode;
   public static final int          numberOfLinks             = 4;

   private Page                     pageDetail;
   private PaginationType           paginationType;
   private int                      requestedPage;
   private Map<Object, Object>      httpRequest;

   private static final String      PAGINATION                = "pagination";
   private static final String      PAGINATION_TYPE           = "paginationType";
   private static final String      BUSINESSTERMS_PW          = "businessTerms";
   private static final String      BUSINESSTERMS_PW_SUBLIST  = "businessTermsSubList";
   public static final int          PAGESIZE                  = 9;
   public static final int          NUMBER_OF_LINKS           = 4;
   public static final int          NUMBER_OF_LINKS_INSTANCES = 15;
   public static final int          DEFAULT_REQUESTED_PAGE    = 1;
   private String                   searchString;
   private String                   searchType                = "ALL";
   private InputStream              inputStream;

   public InputStream getInputStream () {
      return inputStream;
   }

   public void setInputStream (InputStream inputStream) {
      this.inputStream = inputStream;
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

   public List<KeyWord> getKeyWords () {
      return keyWords;
   }

   public void setKeyWords (List<KeyWord> keyWords) {
      this.keyWords = keyWords;
   }

   // Action Methods
   public String getAllKeyWords () {
      try {

         keyWords = getPreferencesServiceHandler().getAllExistingKeywords(getApplicationContext().getModelId());
      } catch (ExeCueException e) {
         e.printStackTrace();
         setErrorMessage(getText("execue.errors.general"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String input () {
      if (log.isDebugEnabled()) {
         log.debug("Came to input flow");
      }
      if (keyWord != null && keyWord.getWord() != null) {
         if (log.isDebugEnabled()) {
            log.debug("key word Name : " + keyWord.getWord());
         }
      }
      //      try {
      //         businessTerms = getPreferencesServiceHandler()
      //                  .getAllBusinessTermEntities(getApplicationContext().getModelId());
      //         preparePaginationForBTs();
      //         if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
      //            Collections.sort(businessTerms, new BusinessEntityInfoDisplayNameComparator());
      //         }
      //
      //      } catch (ExeCueException e) {
      //         e.printStackTrace();
      //      }
      return SUCCESS;
   }

   private void preparePaginationForBTs () {
      pageDetail = new Page(Long.valueOf(DEFAULT_REQUESTED_PAGE), Long.valueOf(PAGESIZE));
      pageDetail.setNumberOfLinks(Long.valueOf(4));
      setPaginationType(PaginationType.BusinessTermsForParallelWords);
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
         if (getHttpSession() != null) {
            getHttpSession().put(BUSINESSTERMS_PW, businessTerms);
         }
         int size = businessTerms.size();
         int tempSize = (size <= PAGESIZE) ? size : PAGESIZE;
         businessTerms = businessTerms.subList(0, tempSize);
         pageDetail.setRecordCount(Long.valueOf(size));
         if (httpRequest != null) {
            httpRequest.put(PAGINATION, pageDetail);
            httpRequest.put(PAGINATION_TYPE, PaginationType.BusinessTermsForParallelWords);
         }

      }
      log.info("displaying initial sublist");
   }

   @SuppressWarnings ("unchecked")
   public String showSubBTforParallelWords () {
      int fromIndex = 1;
      int toIndex = 1;
      int reqPageNo = getRequestedPage();
      if (pageDetail != null) {
         pageDetail.setRequestedPage(Long.valueOf(getRequestedPage()));
      }
      List<BusinessEntityInfo> btList = new ArrayList<BusinessEntityInfo>();
      if (getHttpSession() != null) {
         btList = (getHttpSession().get(BUSINESSTERMS_PW_SUBLIST) != null) ? (List<BusinessEntityInfo>) getHttpSession()
                  .get(BUSINESSTERMS_PW_SUBLIST)
                  : (List<BusinessEntityInfo>) getHttpSession().get(BUSINESSTERMS_PW);
      }
      int tempTotCount = (btList.size() / PAGESIZE);
      int rmndr = btList.size() % PAGESIZE;
      if (rmndr != 0) {
         tempTotCount++;
      }
      if (reqPageNo <= tempTotCount) {
         fromIndex = ((reqPageNo - 1) * PAGESIZE);
         toIndex = reqPageNo * PAGESIZE;
         if (toIndex > btList.size())
            toIndex = (btList.size());
      }
      if (httpRequest != null) {
         httpRequest.put(PAGINATION, pageDetail);
         httpRequest.put(PAGINATION_TYPE, paginationType);
      }
      log.info("Getting Columns SubList from -> " + fromIndex + " to " + toIndex);
      businessTerms = btList.subList(fromIndex, toIndex);
      return SUCCESS;

   }

   public String getBusinessTermsForPWsBySearchString () {
      if (getHttpSession() != null) {
         List<BusinessEntityInfo> businessTermsSublist = new ArrayList<BusinessEntityInfo>();
         List<BusinessEntityInfo> businessTerms = (List<BusinessEntityInfo>) getHttpSession().get(BUSINESSTERMS_PW);
         if (ExecueCoreUtil.isCollectionNotEmpty(businessTerms)) {
            if ("startWith".equalsIgnoreCase(searchType)) {
               for (BusinessEntityInfo bt : businessTerms) {
                  if (bt.getBusinessEntityTermDisplayName().toLowerCase().startsWith(searchString.toLowerCase())) {
                     businessTermsSublist.add(bt);
                  }
               }
            } else if ("contains".equalsIgnoreCase(searchType)) {
               for (BusinessEntityInfo bt : businessTerms) {
                  if (bt.getBusinessEntityTermDisplayName().toLowerCase().contains(searchString.toLowerCase())) {
                     businessTermsSublist.add(bt);
                  }
               }
            } else {
               businessTermsSublist.addAll(businessTerms);
            }

         }
         setBusinessTerms(businessTermsSublist);
         preparePaginationForBTSubList(businessTermsSublist);
      }

      return SUCCESS;
   }

   private void preparePaginationForBTSubList (List<BusinessEntityInfo> businessTermsSubList) {
      pageDetail = new Page(Long.valueOf(DEFAULT_REQUESTED_PAGE), Long.valueOf(PAGESIZE));
      pageDetail.setNumberOfLinks(Long.valueOf(4));
      setPaginationType(PaginationType.BusinessTermsForParallelWords);
      List<BusinessEntityInfo> businessTermsToBePaginated = null;
      if (ExecueCoreUtil.isCollectionNotEmpty(businessTermsSubList)) {
         if (getHttpSession() != null) {
            if (getHttpSession().get(BUSINESSTERMS_PW_SUBLIST) != null) {
               getHttpSession().remove(BUSINESSTERMS_PW_SUBLIST);
            }
            getHttpSession().put(BUSINESSTERMS_PW_SUBLIST, businessTermsSubList);
            businessTermsToBePaginated = (List<BusinessEntityInfo>) getHttpSession().get(BUSINESSTERMS_PW_SUBLIST);
         }
         int size = businessTermsToBePaginated.size();
         int tempSize = (size <= PAGESIZE) ? size : PAGESIZE;
         businessTerms = businessTermsToBePaginated.subList(0, tempSize);
         pageDetail.setRecordCount(Long.valueOf(size));
         if (httpRequest != null) {
            httpRequest.put(PAGINATION, pageDetail);
            httpRequest.put(PAGINATION_TYPE, PaginationType.BusinessTermsForParallelWords);
         }

      }
      log.info("displaying initial sublist");

   }

   public String getKeyword () {
      try {
         if (mode != null) {
            if (mode.equalsIgnoreCase("UPDATE")) {
               addActionMessage(getText("execue.parallelwords.updation.success"));
            } else if (mode.equalsIgnoreCase("ADD")) {
               addActionMessage(getText("execue.parallelwords.creation.success"));
            }
         }
         keyWord = getPreferencesServiceHandler().getKeyWord(keyWordName, getApplicationContext().getModelId());
         if (keyWord != null) {
            uiParallelWords = getPreferencesServiceHandler().getParallelWordsById(keyWord.getId());
         } else {
            keyWord = new KeyWord();
            keyWord.setWord(keyWordName);
         }
      } catch (ExeCueException exeCueException) {
         setErrorMessage(getText("execue.errors.general"));
         return ERROR;
      }
      return SUCCESS;
   }

   public String deleteKeyWords () {
      try {
         List<KeyWord> keyWordsToBeDeleted = extractKeyWordsToBeDeleted();
         log.debug("Keywords to be deleted ::" + keyWordsToBeDeleted.size());
         if (ExecueCoreUtil.isCollectionNotEmpty(keyWordsToBeDeleted)) {
            getPreferencesServiceHandler().deleteKeyWords(keyWordsToBeDeleted);
            setMessage(getText("execue.keywords.delete.success"));
         }
      } catch (HandlerException handlerException) {
         setErrorMessage(getText("execue.errors.general"));
      }
      return SUCCESS;
   }

   private List<KeyWord> extractKeyWordsToBeDeleted () {
      List<KeyWord> KeyWordsTobeDeleted = new ArrayList<KeyWord>();
      if (keyWordIdsForDeletion != null) {
         for (Long keyWordId : keyWordIdsForDeletion) {
            if (keyWordId != null) {
               KeyWord keyWord = new KeyWord();
               keyWord.setId(keyWordId);
               KeyWordsTobeDeleted.add(keyWord);
            }
         }
      }
      return KeyWordsTobeDeleted;
   }

   private boolean validateInput () {
      boolean status = false;
      if (keyWord == null || ExecueCoreUtil.isEmpty(keyWord.getWord())) {
         addActionMessage(getText("execue.keyword.name.required"));
         status = true;

      } else if (keyWord.getWord().length() > 255) {
         addActionMessage(getText("execue.keyword.name.length.mismatch"));
         status = true;
      }
      if (uiParallelWords != null) {
         List<UIParallelWord> tempUIParallelWords = new ArrayList<UIParallelWord>();
         for (UIParallelWord uiParallelWord : uiParallelWords) {
            if (CheckType.YES.getValue().toString().equals(uiParallelWord.getCheckedState())) {
               if (uiParallelWord == null || ExecueCoreUtil.isEmpty(uiParallelWord.getParallelWord())) {
                  addActionMessage(getText("execue.parallelword.name.required"));
                  status = true;
               } else if (uiParallelWord.getParallelWord().length() > 255) {
                  addActionMessage(getText("execue.parallelword.name.length.mismatch"));
                  status = true;
               }

            }
            uiParallelWord.setIsInvalidParallelWord("YES");
            tempUIParallelWords.add(uiParallelWord);
         }
         uiParallelWords.clear();
         uiParallelWords.addAll(tempUIParallelWords);
      }
      return status;
   }

   public String createUpdateKeyWordAndParallelWord () {
      if (validateInput()) {
         return ERROR;
      }

      if (keyWord != null) {
         try {
            if (keyWord.getId() != null) {
               mode = "UPDATE";
            } else {
               mode = "ADD";
            }

            UIParallelWordValidationResult parallelWordValidationResult = getPreferencesServiceHandler()
                     .createUpdateKeywordParallelWords(getApplicationContext().getModelId(), keyWord, uiParallelWords);
            if (parallelWordValidationResult != null) {
               if (parallelWordValidationResult.getDuplicateKeyWord() != null) {
                  addActionMessage(getText("execue.global.exist.message", new String[] { parallelWordValidationResult
                           .getDuplicateKeyWord() }));
               }
               if (parallelWordValidationResult.getKeyWord() != null) {
                  addActionMessage(getText("execue.invalid.keyword", new String[] { parallelWordValidationResult
                           .getKeyWord() }));
               }
               if (ExecueCoreUtil.isCollectionNotEmpty(parallelWordValidationResult.getParallelWords())) {
                  for (UIInvalidParallelWord parallelWord : parallelWordValidationResult.getParallelWords()) {
                     if (parallelWord.isSystemVariable()) {
                        addActionMessage(getText("execue.invalid.systemVariable.parallelWord",
                                 new String[] { parallelWord.getName() }));
                     }
                     if (parallelWord.isExactBusinessTerm()) {
                        addActionMessage(getText("execue.invalid.parallelWord", new String[] { parallelWord.getName() }));
                     }
                     if (parallelWord.isDuplicate()) {
                        addActionMessage(getText("execue.invalid.duplicate.parallelWord", new String[] { parallelWord
                                 .getName() }));
                     }

                  }
               }
               return ERROR;
            }
         } catch (Exception exception) {
            log.error(exception);
            addActionMessage(getText("execue.errors.general"));
            return ERROR;
         }
      }
      return SUCCESS;

   }

   public String getKeyWordName () {
      return keyWordName;
   }

   public void setKeyWordName (String keyWordName) {
      this.keyWordName = keyWordName;
   }

   /**
    * @return the businessTerms
    */
   public List<BusinessEntityInfo> getBusinessTerms () {
      return businessTerms;
   }

   /**
    * @param businessTerms
    *           the businessTerms to set
    */
   public void setBusinessTerms (List<BusinessEntityInfo> businessTerms) {
      this.businessTerms = businessTerms;
   }

   /**
    * @return the keyWordIdsForDeletion
    */
   public List<Long> getKeyWordIdsForDeletion () {
      return keyWordIdsForDeletion;
   }

   /**
    * @param keyWordIdsForDeletion
    *           the keyWordIdsForDeletion to set
    */
   public void setKeyWordIdsForDeletion (List<Long> keyWordIdsForDeletion) {
      this.keyWordIdsForDeletion = keyWordIdsForDeletion;
   }

   /**
    * @return the mode
    */
   public String getMode () {
      return mode;
   }

   /**
    * @param mode
    *           the mode to set
    */
   public void setMode (String mode) {
      this.mode = mode;
   }

   public PaginationType getPaginationType () {
      return paginationType;
   }

   public void setPaginationType (PaginationType paginationType) {
      this.paginationType = paginationType;
   }

   public int getRequestedPage () {
      return requestedPage;
   }

   public void setRequestedPage (int requestedPage) {
      this.requestedPage = requestedPage;
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

}
