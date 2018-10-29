<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd"><head>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<meta name="robots" content="index,follow" />
<style>
html,body{
font-family:Arial;

}
table td{
	font-size: 9pt;
}
td.top-bg a,#adminId{
	color:#3C71A1;  
	font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;
	font-size:14px;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
a{
font-size:10pt;	
}
.textArrow{
font-family:"Trebuchet MS", Arial, Helvetica, sans-serif;
font-size:11px;
padding-left:3px;
}
.subHeading{
font-family:Arial, Helvetica, sans-serif;
font-size:12px;
font-weight:bold;
padding:3px;
color:#F2F2F2;
}

.request{
color:#FFF;

}
.source{
	color:#FFF;
}

.top-bg {
	background-image: url(images/main/header_top.png);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}
.technology-1 {
    background-image: url(images/quinnox/top-bg-inner-2.png);
    background-repeat: repeat-x;
    background-position: left top;
    height:51px;
}
.breadCrumbLink,.breadCrumbNoLink,.linkDescription{
   font-size:8pt;	
   color:#3C71A1;
}
</style>


<html>
   <head>
       <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
      <title>
Semantifi Reports
</title>
       <link rel="stylesheet" type="text/css" href="./common/scripts/ext-2.2.1/resources/css/ext-all.css" />
       <script type="text/javascript" src="./common/scripts/ext-2.2.1/adapter/ext/ext-base.js"></script>
       <script type="text/javascript" src="./common/scripts/ext-2.2.1/ext-all.js"></script>	
       <script type="text/javascript" src="./js/main/reporting/reporting.js"></script>  
       <link href="css/main/reporting/view.css" rel="stylesheet" type="text/css" />              
       </head>
       <script language="JavaScript" src="js/common/jquery.js"></script>
	   <script>
		$(document).ready(function () {
			var type = 'SI';	
			if(type == '<%=request.getParameter("type")%>')
		    	$("#metrics").val('<%=request.getParameter("requestedString")%>');	
		});
		function  backToHomePage(){
			 var xmlValue = '<%=request.getParameter("requestedString")%>';		 	 
			 var type = '<%=request.getParameter("type")%>';
			  var applicationId = '<%=request.getParameter("applicationId")%>';	 	 
			 var url="";	
			 if(type=="QI"){
			   url="returnToHome.action?type=QI";
			 }else{
			   url="returnToHome.action?type=SI";
			    if(applicationId && applicationId != -1){
	                url="appIndex.action?type=SI&applicationId="+applicationId;
	           }
			 }
			 $('#requestString').val(xmlValue);
			 $('#typeId').val(type);	
			 document.backToHome.method="post";	
			 document.backToHome.action=url;
			 document.backToHome.submit();
		}
	   </script>
	   <jsp:include page="../../../../views/main/search/qi-head-simpleSearch.jsp"  flush="true" />
       <body style="background-color:#FFF; background-image:none;">
       <div class="mainDiv" style="margin-left: 0px;">
               
       <table width="100%" cellpadding="0" cellspacing="0" border="0" >
  <tr>
    <td class="top-bg">
    
    
    
    <div style="margin:auto;width:91%">
                
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="8%">&nbsp;</td>
                      <td width="92%" align="center">
                      
                      
                      <table width="100%" border="0" cellspacing="0" cellpadding="3" align="right">
                          <tr>
                            <td width="52%" height="20" align="center"><!-- sub links starts -->
                             <div style="height:16px;padding-top:2px;z-index:20;position:absolute;" id="waitImage"><div id="pleaseWaitDiv" style="display: none; margin: auto; width: 110px;">
								<img id="waiting_img" src="images/main/Loader-main-page-3.gif" width="22" height="22" />
							</div>
                        </div></td>
        
                                    <td width="30%" align="right"><div id="showWelcome" style="padding-right:20px;font-size:14px;color:#333;padding-top:0px;font-family:'Trebuchet MS', Arial, Helvetica, sans-serif;text-align:right;width:500px;float:right;">
   </div></td>
                          
                          
                            <!--  <td width="1%" id="publisherTdSeperator" style="display:none">|</td>-->
                          <td width="5%" align="center" id="publisherTd" style="display:none">
						<div id="showAdminLink"><a href="swi/showConsole.action"
							id="adminId"><s:text name="execue.publisher.link" /></a></div>
						<div id="loadingShowLoginLink1" style="display: none;"><img
							src="images/main/loaderTrans.gif"></div>
						</td>
                          
                          
                            <td width="1%" id="seperatorId" style="display:none">|</td> 
                            <td width="5%" align="right" >
                            
                            <span id="showLoginLink" style="padding-left:3px;"><a href="javascript:;" class="links_sem3"
   id="loginId" ><s:text name="execue.login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img src="images/main/loaderTrans.gif" ></span>
                            
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
   <tr >
    <td class="technology-1" height="51" >
    <div style="margin:auto;width:91%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
   
    <td width="92%" align="left"> <jsp:include page="../../../../freeFormSearch.jsp"  flush="true" /></td>
  </tr>
</table>
</div>

      </td>
  </tr>
  
</table> 
    <div style="width:90%;margin:auto;">      
	<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
    <tr>
    <td height="25" colspan="2" align="center" background="images/main/bc-bg.jpg" valign="top" >
   	<form name="backToHome" method="post">
    <table width="100%"><tr><td height="25" align="left" valign="top" >
    <span style="padding-left:0px;color:#333;font-size:14px;"> <a href="#" id="backTohome" onclick="backToHomePage();" class="breadCrumbLink">Home</a> <Span class="textArrow"> >> </Span> <a class="breadCrumbLink" href="javascript:history.back(-1);" style = "margin-left: 7px;">Results</a> <Span class="textArrow"> >> </Span> <span class="breadCrumbNoLink">Reports</span></span></td><td align="right" valign="top" ><div style="padding-bottom:4px;">
            <script type="text/javascript">
	         var addthis_config = {
	           services_compact: 'facebook, linkedin, twitter, more',
	           services_exclude: 'print'
	         }
	   </script>
            <!-- AddThis Button BEGIN -->
            <!-- <div id="shareButton"><a class="addthis_button"
	href="http://www.addthis.com/bookmark.php?v=250&amp;pub=xa-4a9febfe48560f52"><img src="images/main/share1.jpg" alt="Bookmark and Share" style="border:0"/></a></div>
            <script
	type="text/javascript"
	src="http://s7.addthis.com/js/250/addthis_widget.js?pub=xa-4a9febfe48560f52"></script>-->
            <!-- AddThis Button END -->
          </div>
          
          </td></tr></table>
          <s:hidden id="requestString" name="requestString"/>
		  <s:hidden id="typeId" name="type"/>
          </form>
          </td>
    </tr>
  <tr>
    <td height="686" valign="top">
	
 <div id="PivotTable" align="center" style="border:#CCC solid 1px;width:764px;margin:auto;margin-top:20px;">
 <APPLET  code="com.execue.web.applet.presentation.pivot.WebPivot" codebase="./jide"  archive="webPivot.jar,jide-grids.jar,jide-pivot.jar,jide-common.jar,jide-components.jar,jide-data.jar" width=764 height=504>
 	<PARAM name="Message" value="<s:property value="xmlReportData" />"/>
 </APPLET>
</div>
</td></tr></table>
</div>
 <jsp:include page="../../../../views/main/footer-search.jsp" flush="true" />
</body>
</html>
