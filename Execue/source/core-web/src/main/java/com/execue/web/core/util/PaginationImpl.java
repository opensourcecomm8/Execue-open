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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.execue.core.common.bean.Pagination;
import com.execue.core.configuration.ICoreConfigurationService;
import com.execue.core.util.ExecueCoreUtil;

public class PaginationImpl implements Tag, Serializable {

   /**
    * @author Ganesh Tarala
    */
   private static final long         serialVersionUID = 1L;
   private PageContext               pageContext;
   private Tag                       tag;
   private Logger                    logger           = Logger.getLogger(PaginationImpl.class);
   private String                    requestedPage;
   private String                    pageSize;
   private String                    pageCount;                                                // totalAssetResultsCount
   private String                    baseURL;
   private String                    resultsPerPage;
   private HttpSession               httpSession;
   private HttpServletRequest        requestObj;
   private Pagination                pagination;
   private ICoreConfigurationService coreConfigurationService;
   private String                    resultsPaginationFlag;

   public void init () {
      WebApplicationContext springWebContext = WebApplicationContextUtils.getRequiredWebApplicationContext(pageContext
               .getServletContext());
      coreConfigurationService = (ICoreConfigurationService) springWebContext.getBean("coreConfigurationService");
      requestObj = (HttpServletRequest) pageContext.getRequest();
      pagination = (Pagination) requestObj.getAttribute("PAGINATION");
      resultsPaginationFlag = (String) requestObj.getAttribute("RESULTS_PAGINATION_FLAG");
      httpSession = (HttpSession) pageContext.getSession();
      if (resultsPaginationFlag != null && resultsPaginationFlag.equalsIgnoreCase("simpleReport")) {
         resultsPerPage = (String) httpSession.getAttribute("USERPAGESIZE");
      } else if (resultsPaginationFlag != null && resultsPaginationFlag.equalsIgnoreCase("detailReport")) {
         resultsPerPage = (String) httpSession.getAttribute("USER_DETAIL_PAGESIZE");
      }
      if (pagination != null) {
         requestedPage = pagination.getRequestedPage();
         baseURL = pagination.getBaseURL();

         if (ExecueCoreUtil.isEmpty(resultsPerPage)) {
            resultsPerPage = Integer.toString(getCoreConfigurationService().getResultsPageSize());
         }

         pageCount = pagination.getPageCount();
      }

      logger.info("Values :: requestedPage " + requestedPage + " totalResultCount " + pageCount + " resultsPerPage "
               + resultsPerPage);

      if (ExecueCoreUtil.isEmpty(requestedPage)) {
         requestedPage = "1";
      }

      if (ExecueCoreUtil.isEmpty(resultsPerPage)) {
         resultsPerPage = Integer.toString(getCoreConfigurationService().getResultsPageSize());
      }

      /* if (ExecueCoreUtil.isEmpty(pageSize)) {
          this.pageSize = "0";
       }*/
      if (ExecueCoreUtil.isEmpty(pageCount)) {
         this.pageCount = "0";
      }
   }

   public int doEndTag () throws JspException {
      try {

      } catch (Exception e) {
         throw new JspException(e.getCause());
      }
      return EVAL_PAGE;
   }

   public int doStartTag () throws JspException {
      try {
         init();
         int iPageCount = Integer.parseInt(pageCount);
         int iRequestedPage = Integer.parseInt(requestedPage);
         int noOfLinks = iPageCount / Integer.parseInt(resultsPerPage);
         int rmndr = iPageCount % Integer.parseInt(resultsPerPage);
         if (rmndr != 0) {
            noOfLinks++;
         }
         if (noOfLinks > 1) {
            pageContext.getOut().write("<table><tr>");

            if (iRequestedPage > 1) {
               pageContext.getOut().write(
                        "<td width='20'><a class='pageLinkPrevImage' href='" + baseURL + "&requestedPage="
                                 + (iRequestedPage - 1) + "&resultsPerPage=" + resultsPerPage
                                 + "'><IMG border='0' src='./images/previmgnew.png'></a>" + "</td>");
            }
            pageContext.getOut().write("<td width='20'>");
            for (int pageIndex = 1; pageIndex <= noOfLinks; pageIndex++) {
               if (iRequestedPage == pageIndex) {
                  pageContext.getOut().write("<td width='20'><span class='pageNoLink'>" + pageIndex + "</span></td>");
               } else {
                  pageContext.getOut().write(
                           " <td width='20'><a  class='pageLink' href='" + baseURL + "&requestedPage=" + pageIndex
                                    + "&resultsPerPage=" + resultsPerPage + "'>" + pageIndex + "</a></td> ");
               }
            }
            pageContext.getOut().write("</td>");
            if (iRequestedPage < noOfLinks) {
               pageContext.getOut().write(
                        " <td width='20'><a class='pageLinkNextImage' href='" + baseURL + "&requestedPage="
                                 + (iRequestedPage + 1) + "&resultsPerPage=" + resultsPerPage + "'>"
                                 + "<IMG border='0' src='./images/nxtimgnew.png'></a>" + " </td>");
            }
            pageContext.getOut().write(" </tr><tr><td colspan='3'></td></tr></table>");
         }
      } catch (IOException e) {
         throw new JspException(e.getCause());
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

   /*/ public String getBaseURL () {
       return baseURL;
    }

    public void setBaseURL (String baseURL) {
       this.baseURL = baseURL;
    }

    public String getRequestedPage () {
       return requestedPage;
    }

    public void setRequestedPage (String requestedPage) {
       this.requestedPage = requestedPage;
    }

   /*  public String getPageSize () {
       return pageSize;
    }

    public void setPageSize (String pageSize) {
       this.pageSize = pageSize;
    }*/

   public String getPageCount () {
      return pageCount;
   }

   public void setPageCount (String pageCount) {
      this.pageCount = pageCount;
   }

   public Pagination getPagination () {
      return pagination;
   }

   public void setPagination (Pagination pagination) {
      this.pagination = pagination;
   }

   /* public String getResultsPerPage () {
       return resultsPerPage;
    }

    public void setResultsPerPage (String resultsPerPage) {
       this.resultsPerPage = resultsPerPage;
    }*/

   public HttpServletRequest getRequestObj () {
      return requestObj;
   }

   public void setRequestObj (HttpServletRequest requestObj) {
      this.requestObj = requestObj;
   }

   public HttpSession getHttpSession () {
      return httpSession;
   }

   public void setHttpSession (HttpSession httpSession) {
      this.httpSession = httpSession;
   }

   /**
    * @return the coreConfigurationService
    */
   public ICoreConfigurationService getCoreConfigurationService () {
      return coreConfigurationService;
   }

   /**
    * @param coreConfigurationService the coreConfigurationService to set
    */
   public void setCoreConfigurationService (ICoreConfigurationService coreConfigurationService) {
      this.coreConfigurationService = coreConfigurationService;
   }
}
