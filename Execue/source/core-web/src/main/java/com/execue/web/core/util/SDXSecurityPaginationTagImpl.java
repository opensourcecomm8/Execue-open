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


package com.execue.web.core.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.PageSort;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.web.core.action.swi.SWIPaginationAction;

public class SDXSecurityPaginationTagImpl extends PaginationTagImpl {

   private Logger logger = Logger.getLogger(SDXSecurityPaginationTagImpl.class);

   public int doStartTag () throws JspException {
      try {
         int pageCount = 0;
         int requestedPage = 1;
         int numberOfLinks = 10;
         int startFrom = 1;
         int tempHolder = 0;
         setHttpRequest((HttpServletRequest) getPageContext().getRequest());
         Page page = (Page) getHttpRequest().getAttribute(PAGINATION);
         String contextPath = getHttpRequest().getContextPath();
         if (page != null) {
            requestedPage = page.getRequestedPage().intValue();
            pageCount = page.getPageCount().intValue();
            numberOfLinks = page.getNumberOfLinks().intValue();
            List<PageSearch> searchList = page.getSearchList();
            List<PageSort> sortList = page.getSortList();

            PageSearch search = null;
            PageSort sort = null;
            String sortField = null;
            String sortOrder = null;
            String searchString = null;
            String searchType = null;
            if (ExecueCoreUtil.isCollectionNotEmpty(searchList)) {
               search = searchList.get(0);
               searchString = search.getString();
               // TODO: -KA- REMOVE ENUM TRANSLATION FROM HERE, CURRENTLY NOT DONE AS GRID AND NORMAL (TABLE) PAGINATION
               // HAVE DIFFERENT VALUES.
               PageSearchType pageSearchType = search.getType();

               if (PageSearchType.STARTS_WITH == pageSearchType) {
                  searchType = SWIPaginationAction.SEARCH_TYPE_STARTWITH;
               } else if (PageSearchType.CONTAINS == pageSearchType) {
                  searchType = SWIPaginationAction.SEARCH_TYPE_CONTAINS;
               } else if (PageSearchType.BY_APP_NAME == pageSearchType) {
                  searchType = SWIPaginationAction.SEARCH_TYPE_BY_APP_NAME;
               } else if (PageSearchType.BY_PUBLISHER_NAME == pageSearchType) {
                  searchType = SWIPaginationAction.SEARCH_TYPE_BY_PUBLISHER_NAME;
               } else if (PageSearchType.VERTICAL == pageSearchType) {
                  searchType = SWIPaginationAction.SEARCH_TYPE_VERTICAL;
               }

            }
            if (ExecueCoreUtil.isCollectionNotEmpty(sortList)) {
               sort = sortList.get(0);
               sortField = sort.getField();
               sortOrder = sort.getOrder();
            }
            boolean addSort = false;
            if (ExecueCoreUtil.isNotEmpty(sortField) && !sortField.equals("null")) {
               addSort = true;
            }
            // Populate the url with the sort and the search information
            if (getTargetURL().contains("?")) {
               setTargetURL(getTargetURL() + "&searchString=");
            } else {
               setTargetURL(getTargetURL() + "?searchString=");
            }
            if (addSort) {
               setTargetURL(getTargetURL() + searchString + "&searchType=" + searchType + "&sortField=" + sortField
                        + "&sortOrder=" + sortOrder + "&requestedPage=");
            } else {
               setTargetURL(getTargetURL() + searchString + "&searchType=" + searchType + "&requestedPage=");
            }
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Target URL :" + getTargetURL());
            logger.debug("Target Pane :" + getTargetPane());
         }
         getPageContext().getOut().write("<div>");
         if (pageCount > 1) {
            getPageContext().getOut().write(
                     "<input type='hidden' id='requestedPageHiddenField' value='" + requestedPage
                              + "' /><table align='left' border='0' cellpadding='0' width='auto'><tr>");
            if (requestedPage == 1) {// 1
               getPageContext().getOut().write(
                        "<td width='14'><IMG border='0' style='padding-top:3px;' src='" + contextPath
                                 + "/images/previmgnew2_disabled.jpg'></td>");
               getPageContext().getOut()
                        .write(
                                 "<td width='13'><IMG border='0' src='" + contextPath
                                          + "/images/previmgnew_disabled.jpg'></td>");
            }
            if (requestedPage > 1) {// 2
               getPageContext()
                        .getOut()
                        .write(
                                 "<td width='10'><a href='javascript:loadPage"
                                          + getTargetPane()
                                          + "(\""
                                          + getTargetURL()
                                          + "1\")' onclick='return isChanngesSaved(this)'><IMG border='0' style='padding-top:2px;' title='First Page' alt='First Page' src='"
                                          + contextPath + "/images/previmgnew2.jpg'></a></td>");
               getPageContext()
                        .getOut()
                        .write(
                                 "<td width='10'><a href='javascript:loadPage"
                                          + getTargetPane()
                                          + "(\""
                                          + getTargetURL()
                                          + (requestedPage - 1)
                                          + "\")' onclick='return isChanngesSaved(this)' ><IMG border='0' style='padding-top:3px;' title='Previous Page' alt='Previous Page' src='"
                                          + contextPath + "/images/previmgnew.jpg'> </a></td>");
            }
            startFrom = requestedPage;
            if (requestedPage % numberOfLinks == 1) {
               tempHolder = startFrom;
            } else if (requestedPage == pageCount) {// for last page condition
               int addLink = 0;

               if (requestedPage % numberOfLinks != 0) {
                  addLink = numberOfLinks - (requestedPage % numberOfLinks);
               }
               startFrom = requestedPage - (numberOfLinks) + 1 + (addLink);
            }
            if (requestedPage < tempHolder) {
               int count = numberOfLinks;
               for (int pageIndex = tempHolder - numberOfLinks; pageIndex < tempHolder; pageIndex++) {
                  count--;
                  if (requestedPage == pageIndex) {
                     getPageContext().getOut().write("<td width='10'>&nbsp;" + pageIndex + "&nbsp;</td>");
                  } else {
                     getPageContext().getOut().write(
                              "<td width='10'><a href='javascript:loadPage" + getTargetPane() + "(\"" + getTargetURL()
                                       + pageIndex + "\")' onclick='return isChanngesSaved(this)' >" + pageIndex
                                       + "</a></td>");
                  }
               }
            } else {
               int count = 0;
               for (int pageIndex = startFrom; pageIndex <= pageCount; pageIndex++) {
                  count++;
                  if (count <= numberOfLinks) {
                     if (requestedPage == pageIndex) {
                        getPageContext().getOut().write("<td width='10'>&nbsp;" + pageIndex + "&nbsp;</td width='8'>");
                     } else {
                        getPageContext().getOut().write(
                                 "<td width='10'><a href='javascript:loadPage" + getTargetPane() + "(\""
                                          + getTargetURL() + pageIndex + "\")' onclick='return isChanngesSaved(this)'>"
                                          + pageIndex + "</a></td>");
                     }
                  }
               }
               if (requestedPage == pageCount) {// for last page condition

                  getPageContext().getOut().write(
                           "<td width='14'><IMG border='0'  src='" + contextPath
                                    + "/images/nxtimgnew_disabled.jpg'></td>");
                  getPageContext().getOut().write(
                           "<td width='13'><IMG border='0'  src='" + contextPath
                                    + "/images/nxtimgnew2_disabled.jpg'></td>");

               }

            }
            // Next Images button
            if (requestedPage < pageCount) {
               getPageContext()
                        .getOut()
                        .write(
                                 "<td width='8'><a href='javascript:loadPage"
                                          + getTargetPane()
                                          + "(\""
                                          + getTargetURL()
                                          + (requestedPage + 1)
                                          + "\")' onclick='return isChanngesSaved(this)' ><IMG border='0' title='Next Page' alt='Next Page' style='padding-top:3px;' src='"
                                          + contextPath + "/images/nxtimgnew.jpg'> </a></td>");
               getPageContext()
                        .getOut()
                        .write(
                                 "<td width='8'><a href='javascript:loadPage"
                                          + getTargetPane()
                                          + "(\""
                                          + getTargetURL()
                                          + pageCount
                                          + "\")'  onclick='return isChanngesSaved(this)'  ><IMG border='0' title='Last Page' alt='Last Page' style='padding-top:3px;' src='"
                                          + contextPath + "/images/nxtimgnew2.jpg'> </a></td>");
            }
            getPageContext().getOut().write(" </tr></table>");
         }
         // Code to output the footer of the pagination section (Page n of N)

         if (pageCount >= 1) {

            getPageContext().getOut().write(
                     "<table align='left'><tr><td style='padding-left:5px;height:25px;'>Page</td><td>" + requestedPage
                              + "</td><td>of</td><td>" + pageCount + "</td></tr></table>");
         } else if (pageCount == 0) {
            getPageContext()
                     .getOut()
                     .write(
                              "<table align='left'><tr><td style='padding-left:5px;height:25px;'>No Records Available</td></tr></table>");
         }

         getPageContext().getOut().write("</div>");
         // script for handling the AJAX call
         getPageContext()
                  .getOut()
                  .write(
                           "<script>function loadPage"
                                    + getTargetPane()
                                    + "(url){ $(\"#"
                                    + getTargetPane()
                                    + "\").removeClass('dynamicPaneBgNoLoader').addClass('dynamicPaneBgLoader'); if(url.indexOf('udxCarInfoSortType')>-1){ url=url.replace(/##/gi,\'%23%23'); } $.post(url,{}, function(data) {$(\"#"
                                    + getTargetPane()
                                    + "\").empty().append(data); $(\"#"
                                    + getTargetPane()
                                    + "\").removeClass('dynamicPaneBgLoader').addClass('dynamicPaneBgNoLoader');});}</script>");
      } catch (IOException e) {
         e.printStackTrace();
         throw new JspException(e.getMessage(), e);
      }
      return SKIP_BODY;
   }

}
