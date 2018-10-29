<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="f" %>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
    <%@page import="com.execue.web.core.util.ExecueWebConstants"%><HEAD>
<title><s:text name="execue.unstructured.results.title" /></title>
<META content="text/html; charset=iso-8859-1" http-equiv=Content-Type>
<META name=GENERATOR content="MSHTML 8.00.6001.18702">
<link href="<c:out value="${basePath}"/>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />

/css/common/qiStyle_new.css"
	rel="stylesheet" type="text/css" />
<link id="page_favicon" href="favicon.ico" rel="icon"
	type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />

<jsp:include page="../../../main/selectCss.jsp" />

<script language="JavaScript" src="<c:out value="${basePath}"/>/js/common/jquery.js"></script>
<link href="<c:out value="${basePath}"/>/css/main/jquery.alert.css" rel="stylesheet" type="text/css">

<script type="text/javascript" src="<c:out value="${basePath}"/>/js/main/jquery.alert.js"></script>
<script language="JavaScript" src="<c:out value="${basePath}"/>/js/main/qi/XMLWriter.js"></script>
<link href="<c:out value="${basePath}"/>/css/main/uss.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</HEAD>
<BODY>
<jsp:include page="../../../../../views/main/search/qi-head-simpleSearch.jsp"
	flush="true" />
<table width="100%" cellpadding="0" cellspacing="0" border="0">

	<tr>
		<td class="top-bg">

		<div style="margin: auto; width: 95%">

		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td width="8%">&nbsp;</td>
				<td width="92%" align="center">


				<table id="feedbackMenu" width="100%" border="0" cellspacing="0" cellpadding="3"
					align="right">
					<tr>
						<td width="56%" height="20" align="center"><!-- sub links starts -->
						&nbsp;</td>
						<td width="30%" align="center">
						<div id="showWelcome"
							style="padding-right: 20px; color: #333; padding-top: 0px; padding-right: 18px; font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; text-align: right; width: 500px; margin: auto;">
						</div>
						</td>
						<td width="3%" style="padding:0px 6px 0px 3px;"><a href="feedback.jsp"><s:text
							name="execue.Feedback.link" /></a></td>
						<td width="1%">|</td> 
                            <td width="2%" style="padding:0px 3px 0px 3px;white-space:nowrap;" align="right"  >
                            
                            <span id="showLoginLink" ><a href="javascript:;" class="links_sem3"
   id="loginId"  ><s:text name="execue.login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img src="images/main/loaderTrans.gif" ></span>
                            
                            </td>

					</tr>
				</table>


				</td>
			</tr>
		</table>

		</div>


		</td>
	</tr>


	<tr>
		<td bgcolor="#FFFFFF" height="42">
		<div class="qiHeaderDiv"  style="width:100%;height:56px;background:url(images/main/top-bg-inner-2.png) repeat-x;" >
                   <div  id="logoDiv" style="width:100%;padding-left:0px;padding-top:6px;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				
				<td width="92%" align="left"><jsp:include page="../../../../../freeFormUnStructuredSearch.jsp" flush="true" /></td>
			</tr>
		</table>
		</div>
</div>

		</td>
	</tr>

	<tr>
		<td bgcolor="#FFFFFF">

		<div style="margin-left: 10px;margin-right:10px;" class="outerDiv">
<form name="backToHome" method="post">

		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="25" class="normalText" background="images/main/bc-bg.jpg">
				<span style="padding-left: 1px; color: #333;">
               
				
				<span id="homeLinkSpan"><a href="#"  onClick="backToHomePage();"
					class="breadCrumbLink"><s:text name="execue.home.link" /></a><span
					class="textArrow"> >> </span> </span> <a href="javascript:history.back(-1);"
					class="breadCrumbLink"><s:text name="execue.unstructured" /></a> <span
					class="textArrow"> >> </span><span class="breadCrumbNoLink"><s:text
					name="execue.results" /></span> </span></td>
					
			</tr>

		</table>
<input type="hidden" name="requestString" value="" id="requestString"/> <input type="hidden" name="type" value="SI" id="typeId"/></form>

		</div>

		</td>
	</tr>
</table>


<div style="width: 100%; background-color: #FFF">
<DIV id=content style="margin-left: 10px;margin-right:10px;padding:0 0 0 0;width:97%;">

<table width="100%" border="0" cellspacing="0" cellpadding="0"
	height="680" >

	<tr>
		<td width="88%" style="width:870px;" valign="top">
    
<div id="dynamicPane" style="width:100%;min-height:500px;height:auto;display:none;">
		
		<jsp:include page="unstructuredSearchResults.jsp" flush="true" />

</div>


		
		&nbsp;

</div>
		</td>
		
	</tr>
</table>



</DIV>
</div>
<DIV id=footernew>
<table align="left" border="0" cellpadding="0" cellspacing="0"
	width="100%">

	<tr>
		<td colspan="3"><jsp:include page="../../../../../views/main/footer-search.jsp" flush="true" /></td>

	</tr>

</table>
</DIV>
		</div>
<div id="showBigImage"
	style="display: none; overflow: auto; width: auto;; min-height: 1px; height: auto; position: absolute; z-index: 10000; left: 0px; top: 0px; border: 3px soild #333;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td bgcolor="#666666" align="right" style="padding-right: 3px;"><a
			href="javascript:closeImage();"><img
			src="images/main/closeUserInfo.png" border="0" /></a></td>
	</tr>
	<tr>
		<td width="200" height="200" bgcolor="#FFFFFF" align="center"
			id="imageTd"
			style="border: 1px solid #D3E1F9; padding: 2px; background: #ffffff url(images/main/uss/Loader.gif) center center no-repeat">
		<div id="bigImg" style="display: none;"></div>
		</td>
	</tr>
</table>
<input type="hidden" id="requestedStringId" value="<s:property value='requestString'/>" />

</div>
<div
	style="height: 16px; padding-top: 2px; z-index: 20; position: absolute;"
	id="waitImage">
<div id="pleaseWaitDiv"
	style="display: none; margin: auto; width: 110px;"><img
	id="waiting_img" src="images/main/Loader-main-page-3.gif" width="22"
	height="22" /></div>
</div>

</BODY>
</HTML>

<script>
$view="listView"; //"<s:property value='resultViewType' />";
var qString='<s:property value="userQuery"  />';
var modelId='<s:property value="modelId" />';
var userQueryId='<s:property value="userQueryId" />';
var applicationId='<s:property value="applicationId" />';
var userQueryFeatureCount='<s:property value="userQueryFeatureCount" />';
var userQueryRecordCount='<s:property value="userQueryRecordCount" />';
var fromResemantification='<s:property value="fromResemantification" />';

var welcomemessage='<s:text name="execue.login.welcome.message"/>';
var fromSearchBar=true;
var selectedUdxCarsInfoSortType="";
var $requestString="";
showLoginInfo('<security:authentication property="principal.fullName"/>');
showPublisherInfo('<security:authentication property="principal.admin"/>','<security:authentication property="principal.publisher"/>');
var adminPath='<c:out value="${adminPath}"/>';
var facetsString="";
</script>
<script src="js/main/uss.js" ></script>
<script>

 function performResemantification(fromunrecogStatuts,instanceName){    
     var applicationId=$("#applicationId").val();
     var userQuery=qString; //$("#metrics").val();	
	 if(!fromunrecogStatuts){
		$("#metrics").val(instanceName) ;
		userQuery=instanceName;
	 }
	 $("#dynamicPane").show();
    // alert("userQuery:::::::::::::::::::::::::::::::before  calling performResemantification.action::::::::"+userQuery+"::::::"+applicationId);
      $.post("uss/performResemantification.action",{applicationId:applicationId,userQuery:userQuery} ,function(data){	    
	       $("#dynamicPane").empty().html(data);
	       
	   }); 
  }
  

function getResults(){ 

$("#dynamicPage").removeClass("dynamicPaneBgNoLoader").addClass("dynamicPaneBgLoader"); 
$("#dynamicPane").show();

 var hasImg = hasImageCheck();
 //alert(hasImg);
 //+"&userRequestZipCodes[0]="+$("#zip").val()
 var userQueryFeatureCount=$("#AFC").val();
 var userQueryRecordCount=$("#UQRC").val();
 if(userQueryFeatureCount==undefined){userQueryFeatureCount=0;}
  if(userQueryRecordCount==undefined){userQueryRecordCount=0;}

request="applicationId="+applicationId+"&userQuery="+qString+"&userQueryId="+userQueryId+"&userQueryFeatureCount="+userQueryFeatureCount+"&userQueryRecordCount="+userQueryRecordCount+"&selectedDefaultVicinityDistanceLimit=100&imagePresent="+hasImg+"&resultViewType=listView&keyWordBasedResults=NO&modelId="+modelId;

//alert(request);
processFacets(request);
$.get("<c:out value="${basePath}"/>/getUnstructuredSearchPageResult.action?"+request,{},function(data){
					   $("#dynamicPage").empty().html(data);
					   $("#dynamicPane").css("backgroundImage","none");
					   $("#dynamicPane").removeClass("dynamicPaneBgLoader").addClass("dynamicPaneBgNoLoader"); 
					   $("#dynamicPage").removeClass("dynamicPaneBgLoader").addClass("dynamicPaneBgNoLoader"); 
					   $("#dynamicPane").show();
					   $("#dynamicPaneLoader").hide();			   
					   reSemantificationProcess=false;
					   
					});	
}
$(document).ready(function(){ 
	//$("#metrics").val(qString);					  
	if(fromResemantification=="N"){					   
        getResults();	
		$("form#publisher #textfield").focus();
		 
							 
	$("#hiddenPane a#closeButtonId").click(function(){
	
			$("#hiddenPane").fadeOut("slow");
			$('#shimmer').remove();
	        
		});				   

		$("#zip").ForceNumericOnly();
		
		 $requestString=$("#requestedStringId").val();
         

		

		$(".exampleQueries a.box2-link").click(function(){ 
		$("#metrics").val($(this).attr("title")); 
			example=setTimeout(showHide,200);
			$("#searchBtnFreeForm").click();							  
  		});	
			
	}else{
		
		performResemantification(true);
	}//setCheckStatus();
		
});


function processView(sortParams){   
	    
			$("#dynamicPage").empty()
			$("#dynamicPage").removeClass('dynamicPaneBgNoLoader').addClass('dynamicPaneBgLoader'); 			
			var hasImg = hasImageCheck();
			var imagePresent="&imagePresent="+hasImg;
			var resultViewType = $("#resultViewTypeId").val();
			    resultViewType="&resultViewType="+resultViewType;
				modId="&modelId="+modelId;
               if(sortParams != ''){
				 request="applicationId="+applicationId+"&userQuery="+qString+"&userQueryId="+userQueryId+"&userQueryFeatureCount="+$("#AFC").val()+"&userQueryRecordCount="+$("#UQRC").val()+"&userRequestZipCodes[0]="+$("#zip").val()+"&selectedDefaultVicinityDistanceLimit="+$("#distance").val()+resultViewType+"&keyWordBasedResults=NO"+imagePresent+sortParams+modId;
				 // selectedDefaultVicinityDistanceLimit changed to distance
				}else{
				 request="applicationId="+applicationId+"&userQuery="+qString+"&userQueryId="+userQueryId+"&userQueryFeatureCount="+$("#AFC").val()+"&userQueryRecordCount="+$("#UQRC").val()+"&userRequestZipCodes[0]="+$("#zip").val()+"&selectedDefaultVicinityDistanceLimit="+$("#distance").val()+resultViewType+"&keyWordBasedResults=NO"+imagePresent+modId;
				}
				//alert(request);  // selectedDefaultVicinityDistanceLimit changed to distance
			$.get("<c:out value="${basePath}"/>/getUnstructuredSearchPageResult.action?"+request,{},function(data){ 																																																																									
				$("#dynamicPage").html(data).show();
				$("#dynamicPane").show();
				$("#dynamicPage").removeClass('dynamicPaneBgLoader').addClass('dynamicPaneBgNoLoader'); 
				//$("#pageHeader").show();
				$("#pageHeaderLoader").hide();
				$("#dynamicPaneLoader").hide();										
			});
	
}
function  backToUSSPage(){	  	
	 //TODO:- temporary changes just for nevigation, will changes once landing page will be finalized	
	// var applicationId = $("#applicationId").val();   
	 url="search_apps/Craigslist_Auto/1508";	
 	 gotoURL(url)
	 //window.location.href =back.action?xmlRequestString="+value	 
}
function  backToHomePage(){	  	
 //TODO:- temporary changes just for nevigation, will changes once landing page will be finalized	
	 url="execueHome.action?type=SI";
 	 gotoURL(url)
	 //window.location.href =back.action?xmlRequestString="+value	 
}
function gotoURL(url){
	 	 
 	 $('#requestString').val($("#metrics").val());
	 document.backToHome.method="post";	
	 document.backToHome.action=url;
	 document.backToHome.submit();
}
$.fn.fixBroken = function(){
			return this.each(function(){
			var tag = $(this);
			var same_img =$(this).attr("src"); 
			var aId=$("#applicationId").val();
			var alt_img ='images/main/noImage-'+aId+'.png';
				tag.error(function() {  // this adds the onerror event to images 
				//var cnt=tag.attr("count");	
				//var count=Number(cnt);
					//if(cnt==2){
					tag.attr("src",alt_img); // change the src attribute of the image
					//}else{
					//count++;
					//tag.attr("src",same_img);
					//tag.attr("count",count+"");
					//}
				return true;
				});
			});
	};
	function  backToCraigslistPage(){	  	
	 //TODO:- temporary changes just for nevigation, will changes once landing page will be finalized	
	// var applicationId = $("#applicationId").val();   
	 url="search_apps/Craigslist_Auto/1508";	
 	 gotoURL(url)
	 //window.location.href =back.action?xmlRequestString="+value	 
}
</script>