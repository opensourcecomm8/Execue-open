<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@page import="com.execue.core.common.bean.Pagination"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<%@page import="com.execue.web.core.util.ExecueWebConstants"%>
<c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />
<%
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>
<title>Semantifi|semantic search engine|semantic search|A portal
to publish and search datasets|search apps|enterprise search and
analytics</title>
<jsp:include page="../selectCss.jsp" flush="true" />
<meta name="robots" content="index,follow" />
<meta name="keywords"
	content="Semantic search, data sets, demographic search, Census 2000, business search , finance, census 2000 search, government, economics, shopping, travel,  sec  filling, FDIC backs, US Case Shiller Housing Indices, Federal Budget Actual And Forecast,  US Economic Aid, Gross Job Flows By Sector, Earmarks, Federal IT Spending" />
<meta name="description"
	content="Semantifi is semantic search engine. Quickly find what you're searching on demography, finance, government, shopping, sports, travel. Shows automatic data summaries and visualizations to interpret database results and Index multi-terabyte data. Crawl any type of database to compose complex queries" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href="css/common/qiStyle_new.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="css/common/roundedSearch.css" type="text/css" />
<link rel="stylesheet" href="css/main/jquery.autocomplete.css"
	type="text/css" />

<script language="JavaScript" src="js/common/jquery.js"></script>
<script type="text/javascript" src="js/common/jquery.ui.all.js"></script>
<script type='text/javascript'
	src='<c:out value="${basePath}"/>/js/main/qi/jquery.autocomplete.freeform.js'></script>
<meta http-equiv="X-UA-Compatible" content="IE=6;" />

<!-- link href="css/main/semantifi.css" rel="stylesheet" type="text/css" /-->
<link href="css/main/quinnox.css" rel="stylesheet" type="text/css" />

<link href="css/main/home.css" rel="stylesheet" type="text/css" />
<link id="page_favicon" href="favicon.ico" rel="icon"
	type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
</head>
<%
   String baseURL = "";
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

   Pagination pagination = (Pagination) request.getAttribute("PAGINATION");
   String requestedPage = "";
   String totalRecrods = "";
   if (pagination != null) {
      baseURL = pagination.getBaseURL();
      requestedPage = pagination.getRequestedPage();
      totalRecrods = pagination.getPageCount();
   }
%>

<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td class="top-bg" align="right" style="padding-right: 20px;">
		<table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td align="left">
                                        
                                        
                                            <span
            id="showWelcome"
            style="padding-right: 20px; color: #333;font-size:14px; padding-right: 18px; font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; text-align: right;  margin: auto;">
        </span>
            </td>
                                        <td> <a style="color:#3C71A1;" href="<c:out value='${adminPath}' />swi/showSearchAppsDashboard.action"
                                            class="menus" >Publish Apps</a></td>
                              <td>&nbsp;| &nbsp;</td> 
                                        <td width="70" align="left"><span class="menu-signin">
                                        <span id="showLoginLink" style="padding-left: 3px;color:#FF9505;"><a
                                            href="javascript:;" class="menus" id="loginId">Sign In</a> <a
                                            href="<c:url value='/j_spring_security_logout'/>"
                                            class="menus" style="color:#FF9505;" id="logoutId"
                                            style="display: none; white-space: nowrap"><s:text
                                            name="execue.logout.link" /></a> </span> <span
                                            id="loadingShowLoginLink" style="display: none;"><img
                                            src="images/main/loaderTrans50.gif"></span></span></td>
                                    </tr>
                                </table>
		
		</td>
	</tr>
	<tr>
		<td align="center" class="center-bg">
		<div id="innovaterAward">

		<div style="margin-top: 5px;"></div>

		<div style="margin-top: 5px;"></div>

		</div>


		<div id="govExpo"><!--div style="margin-top:5px;"><a   href="http://www.gov2expo.com/gov2expo2010"  target="_blank" title="Gov Expo"><img src="images/gov20.jpg" border="0" /></a></div-->

		<div style="margin-top: 5px;"></div>

		<div style="margin-top: 5px;"></div>

		</div>
		<table width="850" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td height="210" class="centerImage">


				<table width="60%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					
					<tr>
						<td align="left" style="padding-left: 50px;">

						<form action='semanticSearch.action'
							onSubmit="return submitDataFreeForm(true)" accept-charset="utf-8"
							method="post"><input type="hidden" name="request"
							id="request" /><input type="hidden" name="type" id="type"
							value="SI" />
						<table border="0" align="left" cellpadding="0" cellspacing="0"
							width="auto">

							<tr>
								<td><input id="metrics" tabindex="10"  type="text"
									maxlength="500" class="inputBox" title="Enter your search term"
									value="" /></td>
								<td align="left">

								<div id="search_image"
									style="padding-top: 6px; margin-top: 1px;margin-left:6px"><input
									id="searchBtnFreeForm"  type="image"
									src="images/quinnox/semantifi-search2.png" /></div>

								 </td>
								<td valign="bottom"><img src="images/quinnox/beta.png" /></td>
							</tr>

							<tr>
								<td align="right" valign="middle">
								
								<table width="100%" ><tr><td style="color:#fff;text-align:left;">
								Semantic integration, reporting & search 
								</td><td align="right" valign="middle">
								
								<span
                                    id="showBookmarksSearchLink" style="display: none;"><a
                                    href="javascript:;" class="advancedSearch" title="Saved Queries" alt="Saved Queries" 
                                    id="bookmarksSearchId" style="">Saved
                                Queries</a></span> <span id="loadingShowBookmarksSearchLink"
                                    style="display: none; padding-left: 13px; padding-top: 5px;"><img
                                    src="images/main/loaderTrans50.gif" width="35px" /></span> <span
                                    style="padding-left: 10px;"><a
                                    href="execueHome.action?type=QI" class="advancedSearch"
                                    title="Advanced Search" alt="Advanced Search" style="margin-right: 0px;">Advanced
                                Search</a></span> 
								</td></tr></table>
								
								
								</td>
								<td align="right" style="padding-right: 5px;">&nbsp;</td>
								<td></td>
							</tr>


						</table>
						<!-- <input type="hidden" name="requestBookMark" id="requestId" />--><input
							type="hidden"
							value="<%=com.execue.core.common.type.BookmarkType.SEARCH_INTERFACE%>"
							id="bookmarkType" /></form>

						</td>
					</tr>
				</table>
				<div id="hiddenPane"
					style="position: absolute; border-bottom: #666 solid 1px; border-right: #666 solid 1px; border-top: #fff solid 1px; border-left: #fff solid 1px; width: 350px; height: 200px; z-index: 2; display: none; padding: 5px 10px 10px 10px; background-color: #ffffff; width: 300px; height: 130px;">
				<div id="closeLinkDiv"
					style="width: 10px; float: right; cursor: pointer; color: #666; font-size: 11px; position: absolute; left: 300px;">X</div>
				<div id="hiddenPaneContent"></div>
				</div>

				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="33" align="center">
						<div
							style="height: 16px;width:32px; padding-top: 7px; z-index: 20; position: absolute;"
							id="waitImage">
						<div id="pleaseWaitDiv"
							style="display: none; margin: auto; width: 32px;"><img
							id="waiting_img" src="images/main/Loader-main-page-3.gif" width="32"
							height="32" /></div>
						</div>

						<div id="underProcess">
						<div style="padding: 10px;"><s:text
							name="execue.cancel.request" /></div>
						<div style="padding-left: 30px;"><span style="padding: 5px;"><input
							id="processYes" type="button" width="70" style="width: 70px;"
							value="Yes" /></span><span style="padding: 5px;"><input
							type="button" id="processNo" style="width: 70px;" value="No" /></span></div>
						</div>

						</td>
					</tr>
					<tr>
						<td align="left" valign="bottom">&nbsp;</td>
					</tr>
					<tr>
						<td align="center">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>

								<td align="center" style="padding-left: 60px;"><!--span class="advancedSearch" style="font-size:11px;"><img src="images/findOut.png"  /></span-->


								


								</td>
								<td width="50" align="right"><!--span id="moredata" style="color:#FFF;font-size:12px;white-space:nowrap;">( <a href="biSignUp.action" class="linkWhite">Signup</a> to view 12 years of data )</span--></td>
							</tr>
						</table>



						</td>
					</tr>
					

				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>





</table>
  <div id="applicationListDiv">
 
   </div>	

<div id="showBigImage"
	style="display: none; overflow: auto; width: auto;; min-height: 1px; height: auto; position: absolute; z-index: 10000; left: 0px; top: 0px; border: 1px solid #fff;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">

	<tr>
		<td bgcolor="#000000"><object width="640" height="390">
			<param name="movie"
				value="http://www.youtube.com/v/8rB5a7zIah4&hl=en_US&feature=player_embedded&version=3"></param>
			<param name="allowFullScreen" value="true"></param>
			<param name="allowScriptAccess" value="always"></param>
			<embed
				src="http://www.youtube.com/v/8rB5a7zIah4&hl=en_US&feature=player_embedded&version=3"
				type="application/x-shockwave-flash" allowfullscreen="true"
				allowScriptAccess="always" width="640" height="390"></embed></object></td>
	</tr>
	<tr>
		<td bgcolor="#000000" align="right"
			style="padding-right: 3px; padding-top: 3px; background: url(images/video_bg.png) no-repeat; height: 31px;"><a
			href="javascript:closeImage();"><img
			src="images/main/closeUserInfo.png" border="0" /></a></td>
	</tr>
</table>
</div>

</body>
</html>

<script>
var appCount='<s:text name="execue.appCount" />';
var applicationId='<s:property value="applicationId"/>';
var url="";
var welcomeMessage='<s:text name="execue.login.welcome.message"/> ';
var currentPosition="t1";
var data='<s:property value="requestString"/>';
var type = '<s:property value="type"/>'; 
var isPublisher='<security:authentication property="principal.user.isPublisher"/>'; 
var loginUserId='<security:authentication property="principal.username"/>';
var fullName='<security:authentication property="principal.fullName"/>';
var pricipalAdmin='<security:authentication property="principal.admin"/>';
var principalPublisher='<security:authentication property="principal.publisher"/>';
var basePath='<c:out value="${basePath}"/>/';
var adminPath='<c:out value="${adminPath}"/>';
showApplications();
$(function(){
$("#metrics").blur();
});

</script>
<!--<script type="text/javascript" src="js/main/home.js"></script>-->