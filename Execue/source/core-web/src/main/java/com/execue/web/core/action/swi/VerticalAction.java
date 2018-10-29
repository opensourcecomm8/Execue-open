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
import java.util.List;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.PageSort;
import com.execue.core.common.bean.entity.Vertical;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.exception.ExeCueException;
import com.execue.core.exception.HandlerException;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.handler.bean.UIApplicationInfo;
import com.execue.handler.swi.IApplicationServiceHandler;
import com.execue.handler.swi.IVerticalServiceHandler;

public class VerticalAction extends SWIPaginationAction {

   private static final long          serialVersionUID = 1L;
   private static final Logger        log              = Logger.getLogger(VerticalAction.class);
   private List<Vertical>             verticals;
   private List<Vertical>             filteredVerticals;
   private List<UIApplicationInfo>    filteredApplications;
   private List<UIApplicationInfo>    existingApplications;
   private List<Long>                 movedApplications;
   private List<Long>                 applicationIds;
   private IVerticalServiceHandler    verticalServiceHandler;
   private IApplicationServiceHandler applicationServiceHandler;
   private Long                       selectedVerticalId;
   private Long                       filteredVerticalId;
   private Vertical                   vertical;
   private String                     selectedVerticalName;
   private String                     verticalSelectName;

   private static final int           PAGE_SIZE        = 15;
   private static final int           NUMBER_OF_LINKS  = 5;

   public String getAllVerticals () {
      try {
         setVerticals(getVerticalServiceHandler().getAllVerticals());
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getVerticalAppAssociationDetails () {
      // get all the applications for the verticals selected from the left nav
      try {
         List<Vertical> filteredVerticalList = getFilterVerticals(getSelectedVerticalId());
         setFilteredVerticals(filteredVerticalList);
         /*
          * setExistingApplications(getVerticalServiceHandler().getApplications(getSelectedVerticalId())); if
          * (ExecueCoreUtil.isCollectionNotEmpty(filteredVerticalList)) { // get the top list item and show the filtered apps
          * for the left multi-select if (getFilteredVerticalId() != null) {
          * setFilteredApplications(getVerticalServiceHandler().getApplications(getFilteredVerticalId())); } else {
          * setFilteredApplications(getVerticalServiceHandler().getApplications(
          * getFilteredVerticals().get(0).getId())); } }
          */
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String getVerticalApplications () {
      try {
         setExistingApplications(getVerticalServiceHandler().getApplications(getSelectedVerticalId()));
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String createOrUpdateVertical () {
      // create/update vertical
      try {
         getVerticalServiceHandler().createVertical(getVertical());
         setSelectedVerticalId(getVertical().getId());
      } catch (HandlerException e) {
         addActionError(e.getMessage());
         return ERROR;
      }
      addActionMessage("Updated successfully");
      return SUCCESS;
   }

   public String getVerticalDetails () {
      try {
         setVertical(getVerticalServiceHandler().getVertical(getSelectedVerticalId()));
      } catch (HandlerException e) {
         e.printStackTrace();
      }
      return SUCCESS;
   }

   public String deleteVertical () {
      // delete a vertical after warning for "existing apps will be associated to Others"
      try {
         getVerticalServiceHandler().deleteVertical(getSelectedVerticalId());
      } catch (HandlerException e) {
         addActionError("Error Deleting vertical :" + e.getMessage());
         return ERROR;
      }
      addActionMessage("Deleted successfully");
      return SUCCESS;
   }

   public String updateVerticalAppAssociation () {
      // update application based on the vertical group they are selected under.
      try {
         getVerticalServiceHandler().createVerticalAppAssociation(getApplicationIds(), getSelectedVerticalId());

         if (ExecueCoreUtil.isCollectionNotEmpty(getMovedApplications())) {
            getVerticalServiceHandler().moveAppsAcrossVertical(getMovedApplications(), getFilteredVerticalId());
         }
      } catch (HandlerException e) {
         addActionError("Unable to process your request currently " + e.getMessage());
         return ERROR;
      }
      addActionMessage("Updated Sucessfully");
      return SUCCESS;
   }

   public String deleteVerticalAppAssociation () {
      // update application based on the vertical group they are selected under.
      try {
         getVerticalServiceHandler().deleteVerticalAppAssociation(getApplicationIds(), getSelectedVerticalId());
      } catch (HandlerException e) {
         addActionError("Unable to process your request currently " + e.getMessage());
         return ERROR;
      }
      addActionMessage("Removed Sucessfully");
      return SUCCESS;
   }

   @SuppressWarnings("unchecked")
   @Override
   public String processPage () throws ExeCueException {
      getPageDetail().setPageSize(Long.valueOf(PAGE_SIZE));
      getPageDetail().setNumberOfLinks(Long.valueOf(NUMBER_OF_LINKS));
      Page page = getPageDetail();
      List<PageSearch> searchList = page.getSearchList();
      PageSearch pSearch = new PageSearch();
      if (ExecueCoreUtil.isListElementsEmpty(searchList)) {
         searchList = new ArrayList<PageSearch>();
      } else {
         // necessory for the base level query to always sort on application name, as for this screen
         // sorting on other columns is not required.
         for (PageSearch pageSearchItem : searchList) {
            if (pageSearchItem.getField().equals("displayName")) {
               pageSearchItem.setField("name");
            }
         }
      }
      // necessory for the base level query to always group on vertical name, as for this screen
      // other grouping filter is not required.
      pSearch.setField("vertical");
      pSearch.setType(PageSearchType.EQUALS);
      pSearch.setString(selectedVerticalName);
      searchList.add(pSearch);
      page.setSearchList(searchList);

      if (ExecueCoreUtil.isCollectionEmpty(page.getSortList())) {
         PageSort pageSort = new PageSort();
         pageSort.setField("name");
         pageSort.setOrder("asc");
         List<PageSort> pageSortList = new ArrayList<PageSort>();
         pageSortList.add(pageSort);
         page.setSortList(pageSortList);
      }
      setExistingApplications(getApplicationServiceHandler().getApplicationsByPage(getPageDetail()));
      getHttpRequest().put(PAGINATION, getPageDetail());
      return SUCCESS;
   }

   private List<Vertical> getFilterVerticals (Long seletedVerticalId) throws HandlerException {
      List<Vertical> originalList = getVerticalServiceHandler().getAllVerticals();
      List<Vertical> filteredList = new ArrayList<Vertical>(originalList);
      for (Vertical item : originalList) {
         if (item.getId().equals(seletedVerticalId)) {
            filteredList.remove(item);
         }
      }
      return filteredList;
   }

   public List<Vertical> getVerticals () {
      return verticals;
   }

   public void setVerticals (List<Vertical> verticals) {
      this.verticals = verticals;
   }

   public List<UIApplicationInfo> getExistingApplications () {
      return existingApplications;
   }

   public void setExistingApplications (List<UIApplicationInfo> existingApplications) {
      this.existingApplications = existingApplications;
   }

   public Long getSelectedVerticalId () {
      return selectedVerticalId;
   }

   public void setSelectedVerticalId (Long selectedVerticalId) {
      this.selectedVerticalId = selectedVerticalId;
   }

   public Long getFilteredVerticalId () {
      return filteredVerticalId;
   }

   public void setFilteredVerticalId (Long filteredVerticalId) {
      this.filteredVerticalId = filteredVerticalId;
   }

   public IVerticalServiceHandler getVerticalServiceHandler () {
      return verticalServiceHandler;
   }

   public void setVerticalServiceHandler (IVerticalServiceHandler verticalServiceHandler) {
      this.verticalServiceHandler = verticalServiceHandler;
   }

   public List<Vertical> getFilteredVerticals () {
      return filteredVerticals;
   }

   public void setFilteredVerticals (List<Vertical> filteredVerticals) {
      this.filteredVerticals = filteredVerticals;
   }

   public List<UIApplicationInfo> getFilteredApplications () {
      return filteredApplications;
   }

   public void setFilteredApplications (List<UIApplicationInfo> filteredApplications) {
      this.filteredApplications = filteredApplications;
   }

   public Vertical getVertical () {
      return vertical;
   }

   public void setVertical (Vertical vertical) {
      this.vertical = vertical;
   }

   public List<Long> getMovedApplications () {
      return movedApplications;
   }

   public void setMovedApplications (List<Long> movedApplications) {
      this.movedApplications = movedApplications;
   }

   public List<Long> getApplicationIds () {
      return applicationIds;
   }

   public void setApplicationIds (List<Long> applicationIds) {
      this.applicationIds = applicationIds;
   }

   public List<Integer> getHundredList () {
      List<Integer> hundredList = new ArrayList<Integer>();
      for (int i = 1; i < 100; i++) {
         hundredList.add(i);
      }
      return hundredList;
   }

   public IApplicationServiceHandler getApplicationServiceHandler () {
      return applicationServiceHandler;
   }

   public void setApplicationServiceHandler (IApplicationServiceHandler applicationServiceHandler) {
      this.applicationServiceHandler = applicationServiceHandler;
   }

   public String getSelectedVerticalName () {
      return selectedVerticalName;
   }

   public void setSelectedVerticalName (String selectedVerticalName) {
      this.selectedVerticalName = selectedVerticalName;
   }

   public String getVerticalSelectName () {
      return verticalSelectName;
   }

   public void setVerticalSelectName (String verticalSelectName) {
      this.verticalSelectName = verticalSelectName;
   }

}
