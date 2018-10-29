<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="pg" uri="/WEB-INF/tlds/pagination.tld"%>
<html>
<head>
<link href="css/reporting/view.css" rel="stylesheet" type="text/css" />
<link href="css/pagination.css" rel="stylesheet" type="text/css" />
<style>
html,body {
	font-family: Arial;
}

table td {
	font-size: 9pt;
}

.pagination {
	text-align: center;
	font-size: 10pt;
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
	background-image: url(images/header_top.png);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}
.technology-1 {
	background-image: url(images/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
	height:51px;
}
.breadCrumbLink,.breadCrumbNoLink{
font-size:8pt;	
}

</style>
<title>
Semantifi Reports
</title>
</head>

<script language="JavaScript" src="js/jquery.js"></script>
<script>
$(document).ready(function () {
	var type = 'SI';	
	if(type == '<%=request.getParameter("type")%>')
    	$("#metrics").val('<%=request.getParameter("requestedString")%>');	
    	
    showRecordsDiv();
    // loading detail grid
	chartView("DetailReport");
});
function  backToHomePage(){
	 var xmlValue = '<%=request.getParameter("requestedString")%>';		 	 
	 var type = '<%=request.getParameter("type")%>';	 

	var url="";	
	 if(type=="QI"){
	   url="returnToHome.action?type=QI";
	 }else{
	   url="returnToHome.action?type=SI";
	 }
	 $('#requestString').val(xmlValue);
	 $('#typeId').val(type);	
	 document.backToHome.method="post";	
	 document.backToHome.action=url;
	 document.backToHome.submit();
}
function chartView(chartName){
   $("#"+chartName).html('<div id="pleaseWaitDiv" style="margin: 100px 200px; width: 110px;"><img id="waiting_img" src="images/Loader-main-page-3.gif" width="32" height="32" /></div>');
   $.get("viewDetailReport.action?agQueryIdList=<%=request.getParameter("agQueryIdList")%>&source=Zacks Fundamentals", function (data) {$("#"+chartName).html(data);});
   
}
</script>
<jsp:include page="../../../views/search/qi-head-simpleSearch.jsp"  flush="true" />
<body style="background-image:none; background-color:#FFF;">

    
    <table width="100%" cellpadding="0" cellspacing="0" border="0" >
  <tr>
    <td align="center" class="top-bg">
    
    <div style="margin:auto;width:91%">
                
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                      <td width="8%">&nbsp;</td>
                      <td width="92%" align="center">
                      
                      
                      <table width="100%" border="0" cellspacing="0" cellpadding="3" align="right">
                          <tr>
                            <td width="52%" height="20" align="center"><!-- sub links starts -->
                             <div style="height:16px;padding-top:2px;z-index:20;position:absolute;" id="waitImage"><div id="pleaseWaitDiv" style="display: none; margin: auto; width: 110px;">
								<img id="waiting_img" src="images/Loader-main-page-3.gif" width="22" height="22" />
							</div>
                        </div></td>
                          
                                    
                                    
                                   
                            
                           
                                    <td width="30%" align="right"><div id="showWelcome" style="padding-right:20px;color:#333;padding-top:0px;font-family:'Trebuchet MS', Arial, Helvetica, sans-serif;text-align:right;width:300px;float:right;">
   </div></td>
                           <td width="9%" align="right"><a href="feedback.jsp"><s:text name="execue.feedback.link" /></a></td>
                          
                          <td width="1%" id="publisherTdSeperator" style="display:none">|</td>
                          <td width="5%" align="center" id="publisherTd" style="display:none">
						<div id="showAdminLink"><a href="swi/showConsole.action"
							id="adminId"><s:text name="execue.publisher.link" /></a></div>
						<div id="loadingShowLoginLink1" style="display: none;"><img
							src="images/loaderTrans.gif"></div>
						</td>
                          
                          
                          <td width="1%">|</td> 
                            <td width="5%" align="right" >
                            
                            <span id="showLoginLink" style="padding-left:3px;"><a href="javascript:;" class="links_sem3"
   id="loginId" ><s:text name="execue.login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img src="images/loaderTrans.gif" ></span>
                            
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
    <td width="8%" valign="top"><div style="padding-top:10px;padding-left:0px;padding-right:0px;padding-bottom:1px;"><a href="index.jsp"><img name="index_r1_c1" src="images/inner-page-logo.png" border="0"
					id="index_r1_c1" alt="" /></a></div></td>
    <td width="92%" align="left"> <jsp:include page="../../../freeFormSearch.jsp"  flush="true" /></td>
  </tr>
</table>
</div>

      </td>
  </tr>
 
</table>
    
  <div style="width:90%;margin:auto;">  
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
    <tr>
    <td height="25" colspan="2" align="center" background="images/bc-bg.jpg" valign="top" >
   	<form name="backToHome" method="post">
    <table width="100%"><tr><td height="25" align="left" valign="top" >
    <span style="padding-left:0px;color:#333;font-size:14px;"> <a href="#" id="backTohome" onclick="backToHomePage();" class="breadCrumbLink">Home</a> <Span class="textArrow"> >> </Span> <a class="breadCrumbLink" href="javascript:history.back(-1);" style = "margin-left: 7px;">Results</a> <Span class="textArrow"> >> </Span> <span class="breadCrumbNoLink">Reports</span></span></td><td align="right" valign="top" >&nbsp;
          
          </td></tr></table>
          <s:hidden id="requestString" name="requestString"/>
		  <s:hidden id="typeId" name="type"/>
          </form>
          </td>
    </tr>
</table>
</div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="650" valign="top">

<table align="center">
	<tr>
		<td valign="top" align="center">
        
        <table width="auto" border="0" cellspacing="0" cellpadding="4">
  <tr>
    
    <td align="center" width="60"  valign="top" height="30" style="padding-top:6px;"> 
        <a href='showCSV.action?agQueryIdList=<s:property value="agQueryId"/>&title=<%=request.getParameter("title")%>
								&source=<%=request.getParameter("source")%>
								'>Download Excel</a></td>
    <td align="left" width="54"><div style="padding-bottom:4px;">
            <script type="text/javascript">
	         var addthis_config = {
	           services_compact: 'facebook, linkedin, twitter, more',
	           services_exclude: 'print'
	         }
	   </script>
            <!-- AddThis Button BEGIN -->
            <div id="shareButton"><a class="addthis_button"
	href="http://www.addthis.com/bookmark.php?v=250&amp;pub=xa-4a9febfe48560f52"><img src="images/share1.jpg"  alt="Bookmark and Share" style="border:0"/></a></div>
            <script
	type="text/javascript"
	src="http://s7.addthis.com/js/250/addthis_widget.js?pub=xa-4a9febfe48560f52"></script>
            <!-- AddThis Button END -->
          </div></td>
  </tr>
</table>
                                
                                
                                </td>
	</tr>
	<tr><td height="auto" valign="top" align="left"><div id="recordsDiv" /></td></tr>
	<tr>
		<td height="auto" valign="top">
			<div id="detailReport"><!--<s:property value="htmlOutput" id="htmlOutput" escape="html" />--><div id="DetailReport"></div></div>
		</td>
	</tr>
</table>

</td>
  </tr>
</table>
		
 		<!-- Start Pagination Code -->

        <div
			style="margin: auto; margin-top: 10px; margin-bottom: 10px; width: 90%">
		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			<tr>
				<td height="30" background="images/results_bg.jpg">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>


						<td width="3%">&nbsp;</td>
						<td width="80%"></td>
						<td width="4%">
						<s:select cssClass="textBox1" id="resultsPerPage"
							name="resultsPerPage"
							onchange="javascript:setDetailedResultsPerPage(this.value)"
							list="#{'50':'50','75':'75','100':'100'}" />

						</td>
						<td width="10%"
							style="color: #3F5356; white-space: nowrap; padding-left: 5px; padding-right: 15px;">results
						per page</td>
						<td width="1%" style="color: #3F5356">
						<div align="center"
							style="white-space: nowrap; padding-top: 1px; padding-right: 10px;">
						<span style='font-size: 10pt'><pg:paginate /></span></div>
						</td>
					</tr>
				</table>

				</td>
			</tr>

		</table>
		</div>

		<!-- End Pagination Code -->
		 <jsp:include page="../../../views/footer-search.jsp" flush="true" />
</body>
</html>