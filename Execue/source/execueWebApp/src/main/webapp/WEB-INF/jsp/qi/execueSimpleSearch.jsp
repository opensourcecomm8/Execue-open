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
<meta name="google-site-verification"
	content="v8Bb-udORUh5isQmoTktiyuz56ExzYLwd6L7w5aCVhw" />
<meta name="robots" content="index,follow" />
<meta name="keywords"
	content="Semantic search, data sets, demographic search, Census 2000, business search , finance, census 2000 search, government, economics, shopping, travel,  sec  filling, FDIC backs, US Case Shiller Housing Indices, Federal Budget Actual And Forecast,  US Economic Aid, Gross Job Flows By Sector, Earmarks, Federal IT Spending" />
<meta name="description"
	content="Semantifi is semantic search engine. Quickly find what you're searching on demography, finance, government, shopping, sports, travel. Shows automatic data summaries and visualizations to interpret database results and Index multi-terabyte data. Crawl any type of database to compose complex queries" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<link href="css/qiStyle_new.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="css/roundedSearch.css" type="text/css" />
<link rel="stylesheet" href="css/jquery.autocomplete.css"
	type="text/css" />
<script language="JavaScript" src="js/goog_analytics.js"></script>
<script language="JavaScript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.ui.all.js"></script>
<script type='text/javascript'
	src='<c:out value="${basePath}"/>/js/qinew/jquery.autocomplete.freeform.js'></script>
<meta http-equiv="X-UA-Compatible" content="IE=6;" />
<link href="css/semantifi3b/semantifi.css" rel="stylesheet"
	type="text/css" />
<link href="css/home.css" rel="stylesheet" type="text/css" />
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
		<td class="top-bg" align="right" style="padding-right: 20px;"><span
			id="showWelcome"
			style="padding-right: 20px; color: #333; padding-right: 18px; font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; text-align: right; width: 1000px; margin: auto;">
		</span><a href="account/showUserProfile.action"
			style="display: none; white-space: nowrap; color: blue;"
			id="changePassword">My Account</a></td>
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
				<td height="354" background="images/semantifi3b/centerImage.png"
					style="background-repeat: no-repeat">


				<table width="60%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="120" align="center" valign="top">&nbsp;</td>
					</tr>

					<tr>
						<td align="left" style="padding-left: 50px;">
						<table width="10%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td class="appsHeading">&nbsp;</td>
							</tr>

						</table>
						<a href="#"></a></td>
					</tr>
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
								<td><input id="metrics" tabindex="10" name="q" type="text"
									maxlength="500" class="inputBox" title="Enter your search term"
									value="" /></td>
								<td align="left">

								<div id="search_image"
									style="padding-top: 6px; margin-top: 1px;"><input
									id="searchBtnFreeForm" name="search" type="image"
									src="images/semantifi3b/semantifi-search2.png" /></div>

								</td>
								<td valign="bottom"><img src="images/beta.png" /><!--div style="padding:3px;color:#FFF;font-weight:bold;">BETA</div--></td>
							</tr>

							<tr>
								<td align="right" valign="middle"><span
									id="showBookmarksSearchLink" style="display: none;"><a
									href="javascript:;" class="advancedSearch"
									id="bookmarksSearchId" style="font-size: 7pt;">Saved
								Queries</a></span> <span id="loadingShowBookmarksSearchLink"
									style="display: none; padding-left: 13px; padding-top: 5px;"><img
									src="images/loaderTrans50.gif" width="35px" /></span> <span
									style="padding-left: 10px;"><a
									href="execueHome.action?type=QI" class="advancedSearch"
									title="Advanced Search" style="margin-right: 0px;">Advanced
								Search</a></span> </td>
								<td align="right" style="padding-right: 5px;">&nbsp;</td>
								<td></td>
							</tr>


						</table>
						<input type="hidden" name="requestBookMark" id="requestId" /><input
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
							id="waiting_img" src="images/Loader-main-page-3.gif" width="32"
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


								<table border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td align="left"><a  href="javascript:openWIndow('http://www.semantifi.com/SemantifiPortal.html');" 
											class="menus">About</a> <a id="noLink" href=""></a> &nbsp;
                                            <a href="swi/showSearchAppsDashboard.action"
											class="menus" >Publish Apps</a>
										&nbsp; <a href="javascript:openWIndow('http://www.semantifi.com/contact.html');"  class="menus">Contact</a>&nbsp;
										<span style="color: #FFF">|</span></td>
										<td width="70" align="left"><span class="menu-signin">
										<span id="showLoginLink" style="padding-left: 3px;"><a
											href="javascript:;" class="menus" id="loginId">Sign In</a> <a
											href="<c:url value='/j_spring_security_logout'/>"
											class="menus" id="logoutId"
											style="display: none; white-space: nowrap"><s:text
											name="execue.logout.link" /></a> </span> <span
											id="loadingShowLoginLink" style="display: none;"><img
											src="images/loaderTrans50.gif"></span></span></td>
									</tr>
								</table>


								</td>
								<td width="50" align="right"><!--span id="moredata" style="color:#FFF;font-size:12px;white-space:nowrap;">( <a href="biSignUp.action" class="linkWhite">Signup</a> to view 12 years of data )</span--></td>
							</tr>
						</table>



						</td>
					</tr>
					<tr>
						<td height="12" align="center">&nbsp;</td>
					</tr>
					<tr>
						<td height="10"></td>
					</tr>

					<tr>
						<td height="10"></td>
					</tr>


				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>





</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="#FFFFFF">

	<tr>
		<td class="center-bg1">
		<div id="center-content" style="height: auto;">
		<table width="1000" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="home-page-bg">
				<table width="98%" border="0" align="center" cellpadding="0"
					cellspacing="0" class="main-box">
					<tr>
						<td valign="top" bgcolor="#E9E6C3" class="box1"
							style="background: url('images/box1_bg.jpg') no-repeat;"><!-- Box1 Content -->

						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" style="margin-left: 10px; margin-top: 10px;">
							<tr>
								<td height="34" class="headding-apps"
									style="padding-bottom: 10px;">FEATURED APPS&#13;</td>
							</tr>
							<tr>
								<td><!--div style="overflow:auto;height:480px;width:313px;overflow-x:none;"-->

								<!------------------------ apps starts -------------------->
								<table width="100%" border="0" align="center" cellpadding="0"
									cellspacing="0" style="margin-top: 3px;">
									<s:iterator value="applicationList" id="applicationListId">
										<tr>
											<td>
											<table width="290" border="0" align="left" cellpadding="0"
												cellspacing="0" id="t1" class="appTable">
												<tr>
													<s:if test="applicationImageId != -1">
														<td width="90" align="left" valign="top"><s:if
															test="applicationName != 'Craigslist Auto'">
															<a href='' style="float: left"> <img
																src="qi/getImage.action?applicationId=<s:property value='applicationId'/>&appImageId=<s:property value='applicationImageId'/>"
																name="Image1" width="90" height="90" border="0"
																id="Image1" /></a>
														</s:if> <s:else>
															<a href='' style="float: left"><img
																src="qi/getImage.action?applicationId=<s:property value='applicationId'/>&appImageId=<s:property value='applicationImageId'/>"
																name="Image1" width="90" height="90" border="0"
																id="Image1" /></a>
														</s:else></td>
														<td width="200" valign="top" class="content-text" style="padding-left:12px;">

														<div><s:property value="completeAppDescription" />
														
														</div>
														</td>
													</s:if>
													<s:else>
														<TD class=content-text height=34><a
															href="<s:property value='applicationURL'/>"
															title="<s:property value='completeAppDescription'/>"
															alt="<s:property value='completeAppDescription'/>"
															style="float: left"><img
															src="<c:out value="${basePath}"/>/images/noImage-icon.gif"
															width="90" height="90" border="0" id="Image1"></img></a></TD>
													</s:else>
												</tr>
												<tr>

													<td colspan="2" align="left" style="padding-left: 11px;">
													<div class="appName"><s:property
														value="applicationName" /></div>
													</td>
												</tr>
												<tr>

													<td colspan="2" class="lineSeperator" align="center">&nbsp;</td>
												</tr>
											</table>
											</td>
										</tr>
									</s:iterator>
								</table>
								</td>
							</tr>
						</table>

						</td>
						<td valign="top" bgcolor="#E8E6C0" class="box2">


						<table width="98%" border="0" align="center" cellpadding="0"
							cellspacing="0">

							<tr>
								<td align="left" height="44">
								<div class="headding-apps" style="padding-top: 8px;">&nbsp;POPULAR
								SEARCHES...</div>
								</td>
							</tr>

							<tr>
								<td>
								<div id="queriesDiv" style="display: none"></div>

								<div id="t2app" class="appsExamplesLinks">
								<table width="95%" border="0" align="center" cellpadding="0"
									cellspacing="0">

									<tr>
										<td>
										<table width="100%" border="0" cellspacing="0"
											id="appsDetailsDiv" cellpadding="0">

											<s:iterator value="applicationList" id="applicationListId">
												<tr>
													<td>
													<div class="appName"
														style="padding-left: 9px; font-size: 14px;"><s:property
														value="applicationName" /></div>
													</td>
												</tr>
												<tr>
													<td width="61%" valign="middle">
													<TABLE border=0 class="exampleQueries" cellSpacing=0
														width="95%" align=center>
														<TBODY>
															<s:iterator value="appExamples" id="appExamplesListId">
																<TR>
																	<s:if
																		test="%{#appExamplesListId.queryName != #appExamplesListId.truncatedQueryName}">
																		<TD align=left><A style="CURSOR: pointer"
																			class=box2-link
																			title="<s:property value='queryName'/>"
																			alt="<s:property value='queryName'/>"><s:property
																			value="truncatedQueryName" />...</A></TD>
																	</s:if>
																	<s:else>
																		<TD align=left><A style="CURSOR: pointer"
																			class=box2-link
																			title="<s:property value='queryName'/>"
																			alt="<s:property value='queryName'/>"><s:property
																			value="truncatedQueryName" /></A></TD>
																	</s:else>
																</TR>
															</s:iterator>
														</TBODY>
													</TABLE>

													</td>
												</tr>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</s:iterator>
										</table>
										</td>
									</tr>
								</table>

								</div>

								</td>
							</tr>
						</table>
						</td>
						<td valign="top" bgcolor="#FFFFFF" class="box3"
							style="width: 296px;">
						<table width="95%" border="0" align="center" cellpadding="0"
							cellspacing="0">

							<tr>
							<tr>
								<td>
								<table width="95%" border="0" align="center" cellpadding="0"
									cellspacing="0">


									<tr>
										<td colspan="2" align="center">
										<div
											style="width: 255px; height: 282px; border: 1px solid #ccc; margin-top: 15px; background-color: #fff;">
										<div
											style="width: 250px; height: 20px; text-align: left; padding-left: 6px; padding-top: 3px; font-size: 10px">Advertisement</div>
										<div style="width: 250px; height: 260px;"><script
											type="text/javascript"><!--
google_ad_client = "pub-5075546646414660";
/* 250x250, created 7/13/10 */
google_ad_slot = "9664733940";
google_ad_width = 250;
google_ad_height = 250;
//-->
</script> <script type="text/javascript"
											src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script></div>
										</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left">&nbsp;</td>
									</tr>


									<tr>
										<td colspan="2" align="left">&nbsp;</td>
									</tr>

									<tr>
										<td colspan="2" align="left">&nbsp;</td>
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
			<tr>
				<td height="50" valign="top" class="home-page-bg-bottom">&nbsp;</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>
	<tr>
		<td><jsp:include page="../../../views/footer-search.jsp"
			flush="true" /></td>
	</tr>
	<tr>
		<td align="center"><img src="images/1pix.jpg" width="1"
			height="1" id="img1" /></td>
	</tr>
</table>
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
			src="images/closeUserInfo.png" border="0" /></a></td>
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
</script>
<script type="text/javascript" src="js/home.js"></script>