<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page contentType="text/html; charset=ISO-8859-1;"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<style>
html,body {
	font-family: Arial;
}

table td {
	font-size: 9pt;
}

body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	height:101%;
}
html{
height:101%;	
}

a {
	font-size: 10pt;
}

.textArrow {
	font-family: "Trebuchet MS", Arial, Helvetica, sans-serif;
	font-size: 11px;
	padding-left: 3px;
}

.subHeading {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-weight: bold;
	padding: 3px;
	color: #333;
}

.request {
	color: #666
}

.source {
	color: #666;
}

.top-bg {
	background-image: url(images/semantifi3b/header_top.png);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}

.technology-1 {
	background-image: url(images/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
	height: 51px;
}

.breadCrumbLink,.breadCrumbNoLink,.linkDescription {
	font-size: 8pt;
}

#disclaimerDetails {
	position: absolute;
	width: 800px;
	height: 450px;
	z-index: 1001;
	border: 1px #CCC solid;
	display: none;
	background-color: #FFF;
}

#disclaimerDetailsCloseDiv {
	width: 750px;
	float: right;
	padding: 10px;
}

#disclaimerDetailsCloseDiv a {
	float: right;
}

#disclaimerDetailsContent {
	overflow: auto;
	padding: 20px;
	width: 750px;
	height: 440px;
}

a.greenLink {
	color: green;
	font-size: 9pt;
	text-decoration: none;
}

.htmlThumbnail {
	height: 61px;
	width: 80px;
	border-width: 0px;
}

.chartThumbnail {
	height: 55px;
	width: 75px;
	margin-top: 2px;
}

.overlayBox {
	height: 62px;
	width: 82px;
	position: absolute;
	margin-left: -77px;
}

.post {
	background: url(images/gdot.gif) repeat-x bottom;
}
#charts,#LineChart,#LineChart img{

z-index:0;
}
#charts{
/*position:absolute;*/

/*width:700px;*/
width:100%;
overflow:auto;
z-index:100000;

padding:10px;
padding-left:0px;
text-align:center;
margin-top:0px;
}
.whiteFont {
color:#FFFFFF;
font-size:13px;
font-weight:bold;
padding-left:10px;
}
#charts div{
float:left;	
}
.disclaimerClass{
	
}
.advt{
border-top:1px solid #EAEAEA;width:100%;margin-left:5px;
font-size:12px;
font-weight:bold;
padding-top:5px;

}
</style>

<script language="JavaScript" src="js/jquery.js"></script>
<script>
$(document).ready(function () {
	var type = 'SI';	
	if(type == '<%=request.getParameter("type")%>')
    	$("#metrics").val('<%=request.getParameter("requestedString")%>');	
    
    $.each($(".inactive"),function(index,data){
    	if(index == 0){
    		$(this).attr('class','active');
    	}else{
    	}
    });
	
	//alert($("#middleTable").width);
	var posLeftMiddleTable=$("#bCrumb").position().left;
	var posLeftPageHeader=$("#pageHeaderDiv").position().left;
	var topMenuDivWidth=$("#topMenuDiv").width();
	//alert(topMenuDivWidth);
	if(posLeftMiddleTable!=0){
	$("#middleTable").css("marginLeft",posLeftMiddleTable+"px");
	$("#bCrumb").css("marginLeft",posLeftMiddleTable+"px");
	$("#pageHeaderDiv").css("marginLeft",posLeftPageHeader+"px");
	$("#topMenuDiv").css("marginLeft",posLeftPageHeader+"px");
	$("#topMenuDiv").css("width",topMenuDivWidth+"px");
	}
	
});
function  backToHomePage(){
	 var xmlValue = '<%=request.getParameter("requestedString")%>';		 	 
	 var type = '<%=request.getParameter("type")%>';
	 var applicationId = '<%=request.getParameter("applicationId")%>';	 
	 var url="";		
	 if(xmlValue.indexOf('{__spring_security_filterSecurityInterceptor_filterApplied=true')>-1){xmlValue="";}
		  if(type=="QI"){
		   url="execueHome.action?type=QI";
		  }else{
		   url="execueHome.action?type=SI";
		  }
	
	 $('#requestString').val(xmlValue);
	 $('#typeId').val(type);	
	 document.backToHome.method="post";	
	 document.backToHome.action=url;
	 document.backToHome.submit();
}
function chartView(chartName){
   $("#disclaimerDetails").hide();
   $(".active").attr('class','inactive');
   $("#"+chartName).attr('class','active');
   if(chartName == 'DetailGrid' && $("#"+chartName).html() == ""){
   	$("#"+chartName).html('<div id="pleaseWaitDiv" style="margin: 100px 200px; width: 110px;"><img id="waiting_img" src="images/Loader-main-page-3.gif" width="32" height="32" /></div>');
   	$.get("viewDetailReport.action?agQueryIdList=<%=request.getParameter("agQueryIdList")%>&source=Zacks Fundamentals", function (data) {$("#"+chartName).html(data);});
   }
}
function showAll(iconEl)
{
   $(".inactive").attr('class','active');
}

if($("#IconView img").length<2){$("#showAllTd").hide();}
var assetDetailId=0;
$(document).ready(function(){
var $disclaimerSummary=	$.trim($("#disclaimerSummary").text());
if($disclaimerSummary!=""){$("#disclimerTr").show();}
var windowUrl = window.location;
$.each($('.sharing-button'), function(){
	var shareUrl = $(this).attr("addthis:url");
	$(this).attr("addthis:url",windowUrl + shareUrl);
});
addthis.button('.sharing-button');
var assetId='<s:property value="assetId"/>';
$("a#disclaimerDetailsClose").hide();
      getDisclaimerSummary(assetId);
	  
var left=(screen.width)-(screen.width/2)-250;
var top=(screen.height)-(screen.height/2)-350;
$("#disclaimerClose").click(function(){
$("#disclaimerInfoDynamicDivOuter").hide();
});
		$("a#disclaimerMore").click(function(){	
		    //hideAll();	
		    //$(".active").attr('class','inactive');
		if(assetDetailId !='0'){
	 		$.getJSON("qi/getExtendedDisclaimer.action",{"uiAssetDetail.assetDetailId" : assetDetailId},function(data){																							
				//$("#disclaimerDetailsContent").html(data.extendedDisclaimer)
				//$("#disclaimerDetails").show().css("left",$("#bCrumb").position().left+152+"px");
				//$("#disclaimerDetails").css("top",$("#bCrumb").position().top+160+"px");	
				$("#disclaimerInfoDynamicDivOuter").show();
					$("#disclaimerInfoDynamicDiv").empty().show().html(data.extendedDisclaimer);
					
					$("#disclaimerInfoDynamicDivOuter").css("left",left+"px");
					$("#disclaimerInfoDynamicDivOuter").css("top",top+"px");
					//$("#lightBoxHeading").text("User Profile");
			});
		 }
											 
	 });
						
	$("a#disclaimerDetailsClose").click(function(){
		$("#disclaimerDetails").fadeOut("fast"); 
	});
 
  getReportComments();
});
function getDisclaimerSummary(assetId){
   $.getJSON("qi/getShortDisclaimer.action",{assetId:assetId},function(data){ 
   assetDetailId=data.assetDetailId;
	   if(data.shortDisclaimer){
	     $("#disclaimerSummary").html(data.shortDisclaimer);
	     $("#disclaimerMore").show();
	    
	   }  
   });
}
function getReportComments(){
	var queryId='<s:property value="queryId"/>';
	var requestedString='<%=request.getParameter("requestedString")%>';
	var assetId='<s:property value="assetId"/>';

   $("#reportComments").empty();
   $.get("reporting/showReportComments.action",{"reportComment.queryHash":requestedString,
                                                "reportComment.assetId":assetId,
                                                "reportComment.queryId":queryId},function(data){    
	     $("#reportComments").append(data);	    
   });
}



</script>
<meta name="robots" content="index,follow" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>
        <%=request.getParameter("requestedString")%>
</title>
<script type="text/javascript"
	src="http://s7.addthis.com/js/250/addthis_widget.js?pub=xa-4a9febfe48560f52"></script>
<script type="text/javascript">
var addthis_config = {
  ui_click: true
}
</script>
<link href="css/reporting/view.css" rel="stylesheet" type="text/css" />
</head>
<jsp:include page="../../../views/search/qi-head-simpleSearch.jsp" flush="true" />
<body style="background-image: none; background-color: #ffffff;">
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr>
    <td height="50" valign="top">
   <!--div id="disclaimerDetails">
<div id="disclaimerDetailsCloseDiv"><a href="#"
	id="disclaimerDetailsClose">Close</a></div>
<div id="disclaimerDetailsContent"></div>
</div-->
<div id="disclaimerInfoDynamicDivOuter" style="display:none;z-index:1000000;width:470px;height:335px;position:absolute;left:150px;top:150px;background-color:#ffffff;padding:5px;" >
<table width="100%" border="0" cellspacing="0" cellpadding="0" id="disclaimerTable">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td background="images/user_tab.png" width="151" height="22" valign="bottom">
    
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left"> <span id="lightBoxHeading" class="whiteFont" >Disclaimer</span></td>
    <td align="right" valign="bottom" style="padding-right:5px;"><a href="#"  id="disclaimerClose" ><img src="images/closeUserInfo.png" border="0"  /></a></td>
  </tr>
</table>

   
    
    </td>
    <td align="right" valign="bottom">&nbsp;</td>
  </tr>
</table>
</td>
  </tr>
</table>

<table width="96%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><div style="border:5px solid #666666;background-color:#ffffff;width:98%;height:300px;">
    <div id="disclaimerInfoDynamicDiv" style="border: 1px solid rgb(204, 204, 204); margin: 12px 0px 5px 12px; padding: 3px; overflow: scroll; width: 410px; height: 265px;" >
    
    </div>
    </div></td>
  </tr>
</table>

</div>
<!--  disclimer close -->
<div class="mainDiv" style="margin-left: 0px;">
<table width="100%" cellpadding="0" cellspacing="0" border="0"
	bgcolor="#FFFFFF">
	<tr>
		<td class="top-bg">
		<div id="topMenuDiv" style="margin-left: 20px;margin-right:30px;padding-left:30px; width: 95%">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="100%" align="center">
				<table width="100%" border="0" cellspacing="0" cellpadding="3"
					align="right">
					<tr>
						<td width="41%" height="20" align="right"><!-- sub links starts -->
						<div
							style="height: 16px; padding-top: 2px; z-index: 20; position: absolute;"
							id="waitImage">
						<div id="pleaseWaitDiv"
							style="display: none; margin: auto; width: 110px;"><img
							id="waiting_img" src="images/Loader-main-page-3.gif" width="22"
							height="22" /></div>
						</div>
						</td>
						<td width="40%" align="right">
						<div id="showWelcome"
							style="padding-right: 20px; color: #333; padding-top: 0px; font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; text-align: right; width: 200px; float: right;">
						</div>
						</td>
						<td width="9%" align="right"><a href="feedback.jsp"><s:text
							name="execue.feedback.link" /></a></td>
						<td width="1%" id="publisherTdSeperator" style="display: none">|</td>
						<td width="5%" align="center" id="publisherTd"
							style="display: none">
						<div id="showAdminLink" ><a href="<s:text name="execue.global.publishApps.link"/>"
							id="adminId"><s:text name="execue.publisher.link" /></a></div>
						<div id="loadingShowLoginLink1" style="display: none;"><img
							src="images/loaderTrans.gif"></div>
						</td>
						<td width="1%">|</td>
						<td width="6%" align="left"><span id="showLoginLink"
							style="padding-left: 3px;"><a href="javascript:;"
							class="links_sem3" id="loginId"><s:text name="execue.login.link" /></a>
						<a href="<c:url value='/j_spring_security_logout'/>"
							class="links_sem3" id="logoutId" style="display: none;"><s:text
							name="execue.logout.link" /></a></span> <span id="loadingShowLoginLink"
							style="display: none;"><img src="images/loaderTrans.gif"></span>
						</td>
						<td width="1%"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td class="technology-1"  height="51">
		<div id="pageHeaderDiv" style="margin-left: 20px;margin-right:30px;padding-left:30px; width: 95%">
		<table  border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="120" valign="top">
				<div style="padding-top: 10px; padding-left: 0px; padding-right: 0px; padding-bottom: 1px;width:120px;"><a
					href="index.jsp"><img name="index_r1_c1"
					src="images/inner-page-logo.png" border="0" id="index_r1_c1" alt="" /></a></div>
				</td>
				<td width="99%" align="left"><jsp:include page="../../../freeFormSearch.jsp" flush="true" /></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>

<div style="width: 94%; margin-left: 25px;margin-right:30px;padding-left:30px; background-color: #FFF;"
	id="bCrumb">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="25" colspan="2" align="center"
			background="images/bc-bg.jpg" valign="top">
		<form name="backToHome" method="post">
		<table width="100%">
			<tr>
				<td height="25" align="left" valign="top"><script
					type="text/javascript">
					    if(history.length > 1){
					    	window.document.write('<span style="padding-left:0px;color:#333;font-size:14px;"> <a href="#" class="breadCrumbLink" id="backTohome" onclick="backToHomePage();">Home</a> <Span class="textArrow"> >> </Span> <a class="breadCrumbLink" href="javascript:history.back(-1);" style = "margin-left: 7px;">Results</a> <Span class="textArrow"> >> </Span> <span class="breadCrumbNoLink">Reports</span></span></td><td align="right" valign="top" >&nbsp;');
					    }
    				</script></td>
			</tr>
		</table>
		<s:hidden id="requestString" name="requestString" /> <s:hidden
			id="typeId" name="type" /></form>
		</td>
	</tr>
</table>
</div>  
 </td> 
  </tr>  
 <tr>
 <td valign="top" style="padding-bottom:5px;">  
<div style="width: 94%; margin-left: 20px;margin-right:30px;padding-left:30px; background-color: #FFF;" >
<table width="98%" border="0" cellspacing="0" cellpadding="0" align="left" height="100%" id="middleTable">
  <tr><td>
  	<div id="errorMessage" style="color: #FF0000"><s:actionerror /><s:fielderror /></div>
  </td>
  </tr>
  <tr>
    <td width="62%" valign="top" style="border-right:solid 1px #EAEAEA;padding-right:10px;min-width:702px;" >
    
    <!-- report content starts -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top">
    
    <div style="padding-bottom: 4px;float:right;"><!-- AddThis Button BEGIN -->
						<div id="shareButton"><a class="addthis_button"
							href="http://www.addthis.com/bookmark.php?v=250&amp;pub=xa-4a9febfe48560f52"><img
							src="images/share1.jpg" alt="Bookmark and Share"
							style="border: 0" /></a></div>
						<!-- AddThis Button END --></div>
                        
<table width="auto" border="0" cellspacing="0" cellpadding="0"
	align="left">
	<tr>
		<td>
		<table align="center">
			<tr>
				<td align="center">
				<div id="IconView"><s:if test="reportListWrapper != null">
					<s:iterator value="reportListWrapper.htmlReports" id="thumbnails">
						<s:if
							test="#thumbnails.id != 'DetailGrid' && #thumbnails.id != 'DetailGroupTable'">
							<div style="height: 60px; width: 80px; float: left;"><s:property
								value="#thumbnails.thumbnail" escape="html" /></div>
						</s:if>
					</s:iterator>
					<s:iterator value="reportListWrapper.chartFxReports"
						id="chartThumbnails">
						<div
							style="height: 60px; width: 80px; float: left; margin-left: 2px;"><s:property
							value="#chartThumbnails.thumbnail" escape="html" /></div>
					</s:iterator>
					<s:iterator value="reportListWrapper.htmlReports" id="thumbnails">
						<s:if
							test="#thumbnails.id == 'DetailGrid' || #thumbnails.id == 'DetailGroupTable'">
							<div
								style="height: 60px; width: 80px; float: left; margin-left: 4px;"><s:property
								value="#thumbnails.thumbnail" escape="html" /></div>
						</s:if>
					</s:iterator>
				</s:if></div>
				
			
						
				</td>
			</tr>
			<tr>
				<td align="center">
				<table width="auto" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" width="60" valign="top" height="30"
							style="padding-top: 6px;" id="showAllTd"><font face="arial"><a
							onclick="showAll(this);"
							style="margin-bottom: 22px; cursor: pointer; text-decoration: underline;">Show
						all</a></font></td>
						
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table></td>
  </tr>
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0" align="left">
	<tr>
		<td height="300" align="left" valign="top"><s:if
			test="reportListWrapper != null">
			<div id="grids"  style="margin-top: 5px;float:left;width:99%;"><jsp:include
				page="grids.jsp" flush="true" /></div>

			<div id="charts"  style="margin-top: 5px;"><jsp:include
				page="charts.jsp" flush="true" /></div>
		</s:if></td>
	</tr>
</table></td>
  </tr>
  

	<tr id="disclimerTr" style="display:none;">
					
					<td class="disclaimerClass" ><span ><b><s:text name='execue.global.Disclaimer' ></s:text></b> <span
						style="font-family: Arial; font-size: 15px;">&raquo;</span> </span><span id="disclaimer"><span id="disclaimerSummary"></span><a
			href="#" id="disclaimerMore" style="display: none;" class="greenLink">
		...More</a></span></td>
				</tr>
	<!-- Related search result commented for now -->
 <!-- <tr>
    <td style="padding-top:10px;">
     <c:if test="${!empty relatedAssetResults}">
      <table width="100%" border="0" cellspacing="0" cellpadding="0" >
         <tr>
             <td> <strong> Related Search Results</strong></td>
          </tr>           
          <tr>
           <td >
            <div style="overflow:auto;min-height:10px;max-height:150px;overflow-x:hidden;width:100%;">
             <table width="100%"   style="margin-bottom:20px;">
              <s:iterator value="relatedAssetResults">
				  <tr>				
				   <td>
					<table border="0"  cellpadding="0"
						cellspacing="0">
						<tr>
												
								<td valign="bottom" style="padding-bottom: 1px;">
								    <a href="reportView.action?agQueryIdList=<s:property value='aggregatedQueryIdsList'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><s:property value="reportHeader"/>(<s:property value="applicationName"/>)</a>
							  </td>							
							  <c:set var="chartsExist" value="false"/>
							  <c:set var="gridsExist" value="false"/>
							   <s:iterator value="reportGroupList" id="reportGroupListObj">
								  <s:if test="%{#reportGroupListObj.groupType == 2}">
									 <c:set var="chartsExist" value="true"/>
								  </s:if>
								   <s:if test="%{#reportGroupListObj.groupType == 1}">
									 <c:set var="gridsExist" value="true"/>
								   </s:if>
								</s:iterator>
								<s:set name="csvExist" value="%{'false'}" />
								<s:set name="pivotExist" value="%{'false'}" />												
								<s:iterator value="reportGroupList" id="reportGroupListObj">
									<td valign="bottom">
									<s:if test="%{#reportGroupListObj.reportTypes[0]==99}">
										<s:if test="#csvExist=='true'">					
											<a href="showCSV.action?agQueryIdList=<s:property value='aggregateQueryId[1]'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
										</s:if>
										<s:else>
											<a href="showCSV.action?agQueryIdList=<s:property value='aggregateQueryId[0]'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
										</s:else>
										<s:set name="csvExist" value="%{'true'}" />	
									</s:if> 
									<s:elseif test="%{#reportGroupListObj.reportTypes[0]==4}">														
										<s:if test="#pivotExist=='true'">	
											<a href="showPivot.action?agQueryIdList=<s:property value='aggregateQueryId[1]'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
										</s:if>
										<s:else>														   
											<a href="showPivot.action?agQueryIdList=<s:property value='aggregateQueryId[0]'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
										</s:else>
										<s:set name="pivotExist" value="%{'true'}" />	
									</s:elseif> 
									<s:elseif test="%{#reportGroupListObj.reportTypes[0]==20}">
									   <a href="showDetailReport.action?agQueryIdList=<s:property value='aggregatedQueryIdsList'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
				                   </s:elseif>
									<s:elseif test="%{#reportGroupListObj.reportTypes[0]==98}">
									    <a href="reportView.action?agQueryIdList=<s:property value='aggregatedQueryIdsList'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><s:property value="reportHeader"/>(<s:property value="applicationName"/>)</a>
									</s:elseif>
									<s:elseif test="%{#reportGroupListObj.reportTypes[0]==22}">
									  <a href="showDetailReport.action?agQueryIdList=<s:property value='aggregatedQueryIdsList'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
									</s:elseif>
									<s:else>
										<c:choose>
											<c:when test="${gridsExist && chartsExist}">
												<s:if test="%{#reportGroupListObj.groupType == 2}">
													 <a href="reportView.action?agQueryIdList=<s:property value='aggregatedQueryIdsList'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
												</s:if>
											</c:when>
											<c:when test="${gridsExist && !chartsExist}">
												<s:if test="%{#reportGroupListObj.groupType == 1}">
													<a href="reportView.action?agQueryIdList=<s:property value='aggregatedQueryIdsList'/>&type=SI&title=<s:property value='reportHeader'/>&source=<s:property value='description'/>&requestedString=<s:property value='reportHeader'/>&relatedUserQueryIds=<s:property value="relatedUserQueryIds"/>"><img src='<s:property value="imageUrl" />' alt=""width="18" height="18" hspace="4" border=0></a>
												</s:if>
											</c:when>
										</c:choose>
									</s:else></td>
								</s:iterator>
								<td>
									<a class="addthis_button"
									href="http://www.addthis.com/bookmark.php?v=250&amp;pub=xa-4a9febfe48560f52"
									addthis:url='?agQueryIdList=<s:property value="agQueryIdList"/>'
									addthis:title='<s:property value="reportHeader"/>'><img
									src="images/share1.jpg" alt="Bookmark and Share"
									style="border: 0" /></a>
								</td>
										</tr>
									</table>
								</td>
							</tr>
						</s:iterator>
                      </table>
                      </div>
                      
						</td>
                      </tr>
                     
                    </table>
				</c:if>
                </td>
                 
              </tr>-->
              	<!-- Related search result commented for now -->
</table>






<!--  report content ends -->
    
    
    
    
    </td>
    <td width="38%" valign="top" align="center" >
    <table width="100%" border="0" cellspacing="0" cellpadding="0" >
  
  <tr>
    <td><!-- report comments starts -->
    <div id="reportComments" style="margin-top: 0px;margin-left:1px;"></div>

<!-- report comments ends --></td>
  </tr>
  
  <tr>
    <td align="left" style="padding-left:15px;padding-top:10px;padding-bottom:10px;"><meta name="google-site-verification" content="v8Bb-udORUh5isQmoTktiyuz56ExzYLwd6L7w5aCVhw" />
<div  class="advt">Advertisement</div>
<script type="text/javascript"><!--
google_ad_client = "pub-5075546646414660";
/* 300x250, created 7/22/10 */
google_ad_slot = "2148010613";
google_ad_width = 300;
google_ad_height = 250;
//-->
</script>
<script type="text/javascript"
src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script></td>
  </tr>
  
</table>

    
    
    
    
    
    </td>
  </tr>
</table>
</div>
 </td> </tr>  
 <tr><td valign="bottom" height="30">  

<jsp:include page="../../../views/footer-search.jsp" />
    
    
    </td>
  </tr>
</table>

</body>
</html>