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
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.web.core.action.swi.AssetGrainAction;
import com.execue.web.core.action.swi.CubeManagementAction;
import com.execue.web.core.action.swi.DataSourceAction;
import com.execue.web.core.action.swi.KDXAction;
import com.execue.web.core.action.swi.ParallelWordsAction;
import com.execue.web.core.action.swi.ProfilesAction;
import com.execue.web.core.action.swi.PublishedFileAbsorptionAction;
import com.execue.web.core.action.swi.RangesAction;
import com.execue.web.core.action.swi.SDXAction;
import com.execue.web.core.action.swi.security.UsersAction;

public class PaginationResultsImpl implements Tag, Serializable {

   /**
    * @author Ganesh Tarala
    */
   private static final long   serialVersionUID = 1L;
   private static final Logger log              = Logger.getLogger(PaginationResultsImpl.class);
   private PageContext         pageContext;
   private Tag                 tag;
   private int                 requestedPage;
   private int                 pageSize         = 15;
   private int                 pageCount;
   private String              baseURL;
   private HttpServletRequest  httpRequest;
   private List<Object>        dataList;
   private HttpSession         session;
   private int                 startFrom        = 1;                                            // init value for links
   private int                 numberOfLinks    = 10;                                           // number of links on
   // the page
   private int                 tempHolder;
   private int                 pageType         = 1;
   private String              listType;

   @SuppressWarnings ("unchecked")
   public void init () {
      session = (HttpSession) pageContext.getSession();
      httpRequest = (HttpServletRequest) pageContext.getRequest();
      String reqPage = httpRequest.getParameter("requestedPage");
      listType = httpRequest.getParameter("paginationType");
      int totalCnt = 0;

      if (listType != null && listType.equalsIgnoreCase("column")) {
         pageSize = SDXAction.COLUMN_MEMBER_PAGESIZE;
         numberOfLinks = SDXAction.numberOfLinks;
         dataList = (List<Object>) session.getAttribute("COLUMNLIST");
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("member")) {
         dataList = (List<Object>) session.getAttribute("MEMBERLIST");
         pageSize = SDXAction.COLUMN_MEMBER_PAGESIZE;
         numberOfLinks = SDXAction.numberOfLinks;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("businessTerms")) {
         pageSize = KDXAction.PAGESIZE;
         numberOfLinks = KDXAction.NUMBER_OF_LINKS;
         dataList = (List<Object>) session.getAttribute("BUSINESSTERMS");
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("availableAssets")) {
         pageSize = AssetGrainAction.PAGESIZE;
         numberOfLinks = AssetGrainAction.numberOfLinks;
         dataList = (List<Object>) session.getAttribute("ASSETSFORPAGINATION");
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("sourceTables")) {
         pageSize = SDXAction.PAGESIZEFORSOURCETABLES;
         numberOfLinks = SDXAction.numberOfLinksForSourceTables;
         dataList = (List<Object>) session.getAttribute("SOURCETABLESFORPAGINATION");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("businessTermsForPW")) {
         pageSize = ParallelWordsAction.PAGESIZE;
         numberOfLinks = ParallelWordsAction.numberOfLinks;
         dataList = (List<Object>) session.getAttribute("BUSINESSTERMSFORPW");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("conceptsForRanges")) {
         pageSize = RangesAction.PAGESIZE;
         numberOfLinks = RangesAction.numberOfLinks;
         dataList = (List<Object>) session.getAttribute("CONCEPTSFORRANGES");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("conceptsForProfiles")) {
         pageSize = ProfilesAction.PAGESIZE;
         numberOfLinks = ProfilesAction.numberOfLinks;
         dataList = (List<Object>) session.getAttribute("CONCEPTSFORPROFILES");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("conceptsForCube")) {
         pageSize = CubeManagementAction.PAGE_SIZE;
         numberOfLinks = CubeManagementAction.NUMBER_OF_LINKS;
         dataList = (List<Object>) session.getAttribute("CONCEPTSFORCUBE");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("applicationsPagination")) {
         pageSize = 10;
         numberOfLinks = 4;
         dataList = (List<Object>) session.getAttribute("APPLICATIONSPAGINATION");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } /*
                                * else if (listType != null && listType.equalsIgnoreCase("instancesForPagination")) { pageSize =
                                * KDXAction.PAGESIZEINSTANCES; numberOfLinks = KDXAction.numberOfLinksInstances; dataList = (List<Object>)
                                * session.getAttribute("INSTANCESFORPAGINATION"); pageType = 2; if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
                                * totalCnt = dataList.size(); else totalCnt = 1; }
                                */else if (listType != null && listType.equalsIgnoreCase("dataSourcesForPagination")) {
         pageSize = DataSourceAction.PAGESIZE;
         numberOfLinks = DataSourceAction.numberOfLinks;
         dataList = (List<Object>) session.getAttribute("DATASOURCESPAGINATION");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else if (listType != null && listType.equalsIgnoreCase("usersPagination")) {
         pageSize = UsersAction.PAGESIZE;
         numberOfLinks = UsersAction.numberOfLinks;
         dataList = (List<Object>) session.getAttribute("USERSFORPAGINATION");
         pageType = 2;
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList))
            totalCnt = dataList.size();
         else
            totalCnt = 1;
      } else {
         pageSize = PublishedFileAbsorptionAction.PAGE_SIZE;
         dataList = (List<Object>) session.getAttribute("EVALUATED_COLUMN_LIST");
         if (ExecueCoreUtil.isCollectionNotEmpty(dataList)) {
            totalCnt = dataList.size();
         } else {
            totalCnt = 1;
         }
      }

      // read from constant files
      // TODO:read from constants file
      int pageCnt = totalCnt / pageSize;
      int rmndr = totalCnt % pageSize;
      if (rmndr != 0) {
         pageCnt++;
      }
      this.pageCount = pageCnt;
      if (ExecueCoreUtil.isEmpty(reqPage)) {
         requestedPage = Integer.parseInt("1");
      } else {
         requestedPage = Integer.parseInt(reqPage);
      }
   }

   public int doEndTag () throws JspException {
      return EVAL_PAGE;
   }

   public int doStartTag () throws JspException {
      try {
         init();

         pageContext.getOut().write("<div>");

         if (pageCount > 1) {
            pageContext.getOut().write("<table align='left' border='0' cellpadding='0' width='auto'><tr>");
            if (requestedPage == 1) {

               pageContext.getOut().write(
                        "<td width='14'><IMG border='0' src='../images/previmgnew2_disabled.jpg'></td>");

               pageContext.getOut().write(
                        "<td width='13'><IMG border='0' src='../images/previmgnew_disabled2.jpg'></td>");

            }
            if (requestedPage > 1) {

               pageContext
                        .getOut()
                        .write(
                                 "<td width='10px'><a href='#' id='' onclick='subList("
                                          + 1
                                          + ","
                                          + pageType
                                          + ")'><IMG border='0' title='First Page' alt='First Page' src='../images/previmgnew2.jpg'></a></td>");

               pageContext
                        .getOut()
                        .write(
                                 "<td width='10px'><a href='#' id='' onclick='subList("
                                          + (requestedPage - 1)
                                          + ","
                                          + pageType
                                          + ")'><IMG border='0' title='Previous Page' alt='Previous Page' src='../images/previmgnew.jpg'> </a></td>");

            }

            if (requestedPage % numberOfLinks == 1) {
               startFrom = requestedPage;
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
                     pageContext.getOut().write("<td width='10px'>");
                     pageContext.getOut().write("&nbsp;" + pageIndex + "&nbsp;");
                     pageContext.getOut().write("</td>");
                  } else {
                     pageContext.getOut().write("<td width='10px'>");
                     pageContext.getOut().write(
                              "<a href='#' id='' onclick='subList(" + pageIndex + "," + pageType + ")'>" + pageIndex
                                       + "</a>");
                     pageContext.getOut().write("</td>");
                  }
               }
            } else {
               int count = 0;
               for (int pageIndex = startFrom; pageIndex <= pageCount; pageIndex++) {
                  count++;
                  if (count <= numberOfLinks) {

                     if (requestedPage == pageIndex) {
                        pageContext.getOut().write("<td width='8'>");
                        pageContext.getOut().write("&nbsp;" + pageIndex + "&nbsp;");
                        pageContext.getOut().write("</td width='8'>");
                     } else {
                        pageContext.getOut().write("<td width='8'>");
                        pageContext.getOut().write(
                                 "<a href='#' id='' onclick='subList(" + pageIndex + "," + pageType + ")'>" + pageIndex
                                          + "</a>");
                        pageContext.getOut().write("</td>");
                     }
                  }
               }
            }

            if (requestedPage < pageCount) {

               pageContext
                        .getOut()
                        .write(
                                 "<td width='8'><a href='#' id='' onclick='subList("
                                          + (requestedPage + 1)
                                          + ","
                                          + pageType
                                          + ")'><IMG border='0' title='Next Page' alt='Next Page' src='../images/nxtimgnew.jpg'> </a></td>");

               pageContext
                        .getOut()
                        .write(
                                 "<td width='8'><a href='#' id='' onclick='subList("
                                          + pageCount
                                          + ","
                                          + pageType
                                          + ")'><IMG border='0' title='Last Page' alt='Last Page' src='../images/nxtimgnew2.jpg'> </a></td>");

            }
            if (requestedPage == pageCount) {

               pageContext.getOut().write(
                        "<td width='14'><IMG border='0' src='../images/nxtimgnew_disabled2.jpg'></td>");

               pageContext.getOut().write(
                        "<td width='13'><IMG border='0' src='../images/nxtimgnew2_disabled2.jpg'></td>");

            }
            pageContext.getOut().write(" </tr></table>");
         }
         if (pageCount > 1) {
            pageContext.getOut().write(
                     "<table align='left'><tr><td style='padding-left:5px;height:25px;'>Page</td><td>" + requestedPage
                              + "</td><td>of</td><td>" + pageCount + "</td></tr></table>");
         } else {

            pageContext.getOut().write(
                     "<table align='left'><tr><td style='padding-left:5px;height:25px;'>Page</td><td> 1 </td><td>of</td><td>"
                              + pageCount + "</td></tr></table>");

         }

         pageContext.getOut().write("</div>");
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return SKIP_BODY;
   }

   public Tag getParent () {
      return getTag();
   }

   public void release () {
      pageContext = null;
      tag = null;
   }

   public void setPageContext (PageContext arg0) {
      pageContext = arg0;
   }

   public void setParent (Tag arg0) {
      setTag(arg0);
   }

   public Tag getTag () {
      return tag;
   }

   public void setTag (Tag tag) {
      this.tag = tag;
   }

   public int getPageSize () {
      return pageSize;
   }

   public void setPageSize (int pageSize) {
      this.pageSize = pageSize;
   }

   public int getPageCount () {
      return pageCount;
   }

   public void setPageCount (int pageCount) {
      this.pageCount = pageCount;
   }

   public String getBaseURL () {
      return baseURL;
   }

   public void setBaseURL (String baseURL) {
      this.baseURL = baseURL;
   }

   public int getStartFrom () {
      return startFrom;
   }

   public void setStartFrom (int startFrom) {
      this.startFrom = startFrom;
   }

   public int getNumberOfLinks () {
      return numberOfLinks;
   }

   public void setNumberOfLinks (int numberOfLinks) {
      this.numberOfLinks = numberOfLinks;
   }

   public int getTempHolder () {
      return tempHolder;
   }

   public void setTempHolder (int tempHolder) {
      this.tempHolder = tempHolder;
   }

   public HttpServletRequest getHttpRequest () {
      return httpRequest;
   }

   public void setHttpRequest (HttpServletRequest httpRequest) {
      this.httpRequest = httpRequest;
   }
}
