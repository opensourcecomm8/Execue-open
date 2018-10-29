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
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.Page;
import com.execue.core.common.bean.PageSearch;
import com.execue.core.common.bean.PageSort;
import com.execue.core.common.type.PageSearchType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.web.core.action.swi.SWIPaginationAction;

/**
 * @author John Mallavalli
 */
public class PaginationTagImpl implements Tag, Serializable {

   private Logger                logger     = Logger.getLogger(PaginationTagImpl.class);
   private Tag                   tag;
   private String                targetURL;
   private String                targetPane;
   private PageContext           pageContext;
   private HttpServletRequest    httpRequest;
   protected static final String PAGINATION = "PAGINATION";

   /**
    * This tag need to get 2 attributes:<BR>
    * 1. targetURL - this should contain the target action built as an URL along with the other necessary parameters
    * required by the action class<BR>
    * 2. targetPane - the div id which should receive the AJAX response<BR>
    */
   public int doStartTag () throws JspException {
      try {
         // TODO: -JVK- populate the below required values from the request
         int pageCount = 0;
         int requestedPage = 1;
         int numberOfLinks = 10;
         int startFrom = 1;
         int tempHolder = 0;
         httpRequest = (HttpServletRequest) pageContext.getRequest();
         Page page = (Page) httpRequest.getAttribute(PAGINATION);
         String contextPath = httpRequest.getContextPath();
         if (page != null) {
            requestedPage = page.getRequestedPage().intValue();
            pageCount = page.getPageCount().intValue();
            numberOfLinks = page.getNumberOfLinks().intValue();
            List<PageSearch> searchList = page.getSearchList();
            List<PageSort> sortList = page.getSortList();
            // TODO: -JM- Currently there will be only one search object, change later if there are multiple searches
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
            if (targetURL.contains("?")) {
               targetURL += "&searchString=";
            } else {
               targetURL += "?searchString=";
            }
            if (addSort) {
               targetURL += searchString + "&searchType=" + searchType + "&sortField=" + sortField + "&sortOrder="
                        + sortOrder + "&requestedPage=";
            } else {
               targetURL += searchString + "&searchType=" + searchType + "&requestedPage=";
            }
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Target URL :" + targetURL);
            logger.debug("Target Pane :" + targetPane);
         }
         pageContext.getOut().write("<div>");
         if (pageCount > 1) {
            pageContext.getOut().write(
                     "<input type='hidden' id='requestedPageHiddenField' value='" + requestedPage
                              + "' /><table align='left' border='0' cellpadding='0' width='auto'><tr>");
            if (requestedPage == 1) {// 1
               pageContext.getOut().write(
                        "<td width='14'><IMG border='0' style='padding-top:3px;' src='" + contextPath
                                 + "/images/previmgnew2_disabled.jpg'></td>");
               pageContext.getOut()
                        .write(
                                 "<td width='13'><IMG border='0' src='" + contextPath
                                          + "/images/previmgnew_disabled.jpg'></td>");
            }
            if (requestedPage > 1) {// 2
               pageContext
                        .getOut()
                        .write(
                                 "<td width='10'><a href='javascript:loadPage"
                                          + targetPane
                                          + "(\""
                                          + targetURL
                                          + "1\")'><IMG border='0' style='padding-top:2px;' title='First Page' alt='First Page' src='"
                                          + contextPath + "/images/previmgnew2.jpg'></a></td>");
               pageContext
                        .getOut()
                        .write(
                                 "<td width='10'><a href='javascript:loadPage"
                                          + targetPane
                                          + "(\""
                                          + targetURL
                                          + (requestedPage - 1)
                                          + "\")'><IMG border='0' style='padding-top:3px;' title='Previous Page' alt='Previous Page' src='"
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
                     pageContext.getOut().write("<td width='10'>&nbsp;" + pageIndex + "&nbsp;</td>");
                  } else {
                     pageContext.getOut().write(
                              "<td width='10'><a href='javascript:loadPage" + targetPane + "(\"" + targetURL
                                       + pageIndex + "\")'>" + pageIndex + "</a></td>");
                  }
               }
            } else {
               int count = 0;
               for (int pageIndex = startFrom; pageIndex <= pageCount; pageIndex++) {
                  count++;
                  if (count <= numberOfLinks) {
                     if (requestedPage == pageIndex) {
                        pageContext.getOut().write("<td width='10'>&nbsp;" + pageIndex + "&nbsp;</td width='8'>");
                     } else {
                        pageContext.getOut().write(
                                 "<td width='10'><a href='javascript:loadPage" + targetPane + "(\"" + targetURL
                                          + pageIndex + "\")'>" + pageIndex + "</a></td>");
                     }
                  }
               }
               if (requestedPage == pageCount) {// for last page condition

                  pageContext.getOut().write(
                           "<td width='14'><IMG border='0'  src='" + contextPath
                                    + "/images/nxtimgnew_disabled.jpg'></td>");
                  pageContext.getOut().write(
                           "<td width='13'><IMG border='0'  src='" + contextPath
                                    + "/images/nxtimgnew2_disabled.jpg'></td>");

               }

            }
            // Next Images button
            if (requestedPage < pageCount) {
               pageContext
                        .getOut()
                        .write(
                                 "<td width='8'><a href='javascript:loadPage"
                                          + targetPane
                                          + "(\""
                                          + targetURL
                                          + (requestedPage + 1)
                                          + "\")'><IMG border='0' title='Next Page' alt='Next Page' style='padding-top:3px;' src='"
                                          + contextPath + "/images/nxtimgnew.jpg'> </a></td>");
               pageContext
                        .getOut()
                        .write(
                                 "<td width='8'><a href='javascript:loadPage"
                                          + targetPane
                                          + "(\""
                                          + targetURL
                                          + pageCount
                                          + "\")'><IMG border='0' title='Last Page' alt='Last Page' style='padding-top:3px;' src='"
                                          + contextPath + "/images/nxtimgnew2.jpg'> </a></td>");
            }
            pageContext.getOut().write(" </tr></table>");
         }
         // Code to output the footer of the pagination section (Page n of N)

         if (pageCount >= 1) {

            pageContext.getOut().write(
                     "<table align='left'><tr><td style='padding-left:5px;height:25px;'>Page</td><td>" + requestedPage
                              + "</td><td>of</td><td>" + pageCount + "</td></tr></table>");
         } else if (pageCount == 0) {
            pageContext
                     .getOut()
                     .write(
                              "<table align='left'><tr><td style='padding-left:5px;height:25px;'>No Records Available</td></tr></table>");
         }

         pageContext.getOut().write("</div>");
         // script for handling the AJAX call
         pageContext
                  .getOut()
                  .write(
                           "<script>function loadPage"
                                    + targetPane
                                    + "(url){ $(\"#"
                                    + targetPane
                                    + "\").removeClass('dynamicPaneBgNoLoader').addClass('dynamicPaneBgLoader'); if(url.indexOf('udxCarInfoSortType')>-1){ url=url.replace(/##/gi,\'%23%23'); } $.post(url,{}, function(data) {$(\"#"
                                    + targetPane
                                    + "\").empty().append(data); $(\"#"
                                    + targetPane
                                    + "\").removeClass('dynamicPaneBgLoader').addClass('dynamicPaneBgNoLoader');});}</script>");
      } catch (IOException e) {
         e.printStackTrace();
         throw new JspException(e.getMessage(), e);
      }
      return SKIP_BODY;
   }

   public int doEndTag () {
      return EVAL_PAGE;
   }

   public Tag getParent () {
      // TODO Auto-generated method stub
      return null;
   }

   public void release () {
      pageContext = null;
      tag = null;
   }

   public void setPageContext (PageContext pageContext) {
      this.pageContext = pageContext;
   }

   public void setParent (Tag parent) {
      this.tag = parent;
   }

   public Tag getTag () {
      return tag;
   }

   public void setTag (Tag tag) {
      this.tag = tag;
   }

   public HttpServletRequest getHttpRequest () {
      return httpRequest;
   }

   public void setHttpRequest (HttpServletRequest httpRequest) {
      this.httpRequest = httpRequest;
   }

   public PageContext getPageContext () {
      return pageContext;
   }

   public String getTargetURL () {
      return targetURL;
   }

   public void setTargetURL (String targetURL) {
      this.targetURL = targetURL;
   }

   public String getTargetPane () {
      return targetPane;
   }

   public void setTargetPane (String targetPane) {
      this.targetPane = targetPane;
   }
}
