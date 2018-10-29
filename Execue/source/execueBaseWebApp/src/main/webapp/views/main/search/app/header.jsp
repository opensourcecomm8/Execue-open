<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@page import="com.execue.web.core.util.ExecueWebConstants"%>

<c:set var="basePath" value="<%=request.getContextPath()%>" />
<c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />

<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<script type="text/javascript"
	src="http://s7.addthis.com/js/250/addthis_widget.js?pub=xa-4a9febfe48560f52"></script>
	<script type="text/javascript">
var addthis_config = {
  ui_click: true
}
</script>
    <script>
	$(document).ready(function(){
		var windowUrl = window.location;
		$.each($('.sharing-button'), function(){
		 var shareUrl = $(this).attr("addthis:url");
		 $(this).attr("addthis:url",windowUrl + shareUrl);
		});
		//addthis.button('.sharing-button');
		$(function(){
			$("#metrics").blur();
			});
	});	
</script>
<style>
.no-active{
color:#3C71A1;	
}
.arrowOrange{
color:#F60;
font-size:10px;
padding-right:2px;
}
</style>

<!-- AddThis Button BEGIN -->
						<!--<div id="shareButton" style="position:absolute;z-index:100000;">
                        <a class="addthis_button"
       href="javascript:;"><img
       src="<c:out value='${basePath}'/>/images/main/share1.png" alt="Bookmark and Share"
       style="border: 0" /></a>
       
                       
                            
                            </div>-->
						<!-- AddThis Button END -->
<link href="css/main/quinnox.css" rel="stylesheet" type="text/css" />                       
<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
  <TBODY>
  <TR>
    <TD style="PADDING-RIGHT: 20px" class=top-bg align=right>
    
    
    <table border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                        <td align="left">
                                            <span id="showWelcomeOuterDiv">
    <SPAN 
      style="TEXT-ALIGN: right; MARGIN: auto; WIDTH: 1000px; PADDING-RIGHT: 18px; FONT-FAMILY: 'Trebuchet MS', Arial, Helvetica, sans-serif; COLOR: #333;font-size:14px;" 
      id=showWelcome></SPAN>
      </span></td>
                                       <td>
                                       <a style="color:#3C71A1;" href="<c:out value="${basePath}" />/publishApps.jsp"
                                            class="menus" >Publish Apps</a>
                                     </td>
                              
                                            
                                            <td>&nbsp;| &nbsp;</td> 
                                           
                                       <td>
                                       <a style="color:#3C71A1;" href="<c:out value="${basePath}" />/index.jsp"
                                            class="menus" >Home</a>
                                     </td>
                                     
                                     <td>&nbsp;| &nbsp;</td> 
                                        <td width="70" align="left"><span class="menu-signin">
                                        <span id="showLoginLink" style="padding-left: 3px;"><a
                                            href="javascript:;" class="menus" id="loginId">Sign In</a> <a
                                            href="<c:url value='/j_spring_security_logout'/>"
                                            class="menus" style="color:#FF9505;" id="logoutId"
                                            style="display: none; white-space: nowrap"><s:text
                                            name="execue.logout.link" /></a> </span> <span
                                            id="loadingShowLoginLink" style="display: none;"><img
                                            src="images/main/loaderTrans50.gif"></span></span></td>
                                    </tr>
                                </table>
                                
    
      
      
      
      </TD></TR>
  <TR>
    <TD class=center-bg-app vAlign=top align=middle id="outerTd"">
      
      
      <TABLE border=0 cellSpacing=0 cellPadding=0 width=730 align=center>
        <TBODY>
        <TR>
          <TD style="BACKGROUND-REPEAT: no-repeat" id="topTd" vAlign=top 
          background="">
            <TABLE border=0 cellSpacing=0 cellPadding=0 width="95%">
              <TBODY>
              <TR>
                <TD height=12></TD></TR></TBODY></TABLE>
            <TABLE border=0 cellSpacing=0 cellPadding=0 width="60%" 
align=center>
              <TBODY>
              <TR>
                <TD height=20 align=right>&nbsp;</TD></TR>
              <TR>
                <TD style="PADDING-LEFT: 30px" align=left>
                  <FORM accept-charset=utf-8 
                  onsubmit="return submitDataFreeForm('app')" method="post"  id="searchFormId"
                  action="<c:out value='${basePath}'/>/semanticSearch.action"><INPUT id="request" type=hidden 
                  name="request" /><!--INPUT id="appNameForURL" value='${fn:replace(uiApplicationInfo.applicationName," ","_")}' type="hidden" name="appNameForURL"/--> 
                  <INPUT id="type" value=SI type=hidden name="type" />
                  <TABLE border=0 cellSpacing=0 cellPadding=0 width=100 
                  align=left>
                    <TBODY>
                    <TR>
                      <TD style="padding-bottom:3px;">&nbsp;
                     

                        </TD>
                      <TD align=left>&nbsp;</TD>
                      <TD vAlign=bottom>&nbsp;</TD>
                    </TR>
                    <TR><!--td><label>
                      <select name="select" id="selectApp" style="height:38px;font-size:20px;color:#525252;margin-top:2px;" >
                        <option value="../" selected="selected">I2App</option>
                        <option value="http://localhost:8080/GovernmentSpending/">GovernmentSpending</option>
                      </select>
                    </label></td-->
                      <TD><INPUT id=metrics class=inputBox 
                        title="Enter your search term" tabIndex="10" 
                        value="" 
                        type="text"></TD>
                      <TD align=left>
                        <DIV style="MARGIN-TOP: 1px; PADDING-TOP: 3px;margin-left:6px;" 
                        id=search_image><INPUT id="searchBtnFreeForm" 
                        src="<c:out value="${basePath}" />/images/quinnox/semantifi-search2.png" 
                        type="image"></DIV></TD>
                     <!--  <TD vAlign=bottom><IMG 
                        src="<c:out value="${basePath}" />/images/main/beta.png"><div style="padding:3px;color:#FFF;font-weight:bold;">BETA</div></TD--></TR>
                    <TR>
                      <TD  align=right>
                      
                      <table width="100%" ><tr><td style="color:#fff;text-align:left;">
                                Semantic integration, reporting & search 
                                </td><td align="right" valign="middle">
                      
                      <span id="advancedMenu"><SPAN 
                        style="DISPLAY: none" id=showBookmarksSearchLink><A  
                        style="" id=bookmarksSearchId  title="Saved Queries" alt="Saved Queries" 
                        class=advancedSearch href="javascript:;">Saved Queries</A></SPAN> <SPAN 
                        style="PADDING-LEFT: 13px; DISPLAY: none; PADDING-TOP: 5px" 
                        id=loadingShowBookmarksSearchLink><IMG src="<c:out value="${basePath}" />/images/main/loaderTrans50.gif" 
                        width=35></SPAN> 
                        <s:if test='uiApplicationInfo.sourceType.value=="S"'>
                          <SPAN style="PADDING-LEFT: 10px" id="advancedSearchSpan"><A 
                        style="MARGIN-RIGHT: 0px" class=advancedSearch 
                        title="Advanced Search" alt="Advanced Search" 
                        href="<c:out value="${basePath}" />/execueHome.action?type=QI&applicationId=<s:property value='applicationId' />">Advanced 
                        Search</A></SPAN> 
                        </s:if>
                        
                        <!-- >SPAN style="PADDING-LEFT: 10px"><A 
                        class=advancedSearch title="Search Tips" 
                        href="http://wiki.semantifi.com/index.php/Search_Tips" 
                        target=_blank>Search Tips</A></SPAN-->
                        
                        </span> 
                        
                        </td></tr></table>
                        </TD>
                      <TD style="PADDING-RIGHT: 5px" align=right>&nbsp;</TD>
                      <TD></TD></TR><!--tr>
                    <td  align="center" valign="top" colspan="2" >
                        <div style="height:16px;padding-top:10px;" id="waitImage"><div id="pleaseWaitDiv" style="display: none; margin: auto; width: 133px;">
								<img id="waiting_img" src="SemantifiFinance_files/wait1c.gif" width="133" height="11" />
							</div>
                        </div>
                      </td>
                     
                    </tr--></TBODY></TABLE>
                    <INPUT id="bookmarkType" value="SEARCH_INTERFACE" type="hidden">                      
                  <input type="hidden" name="verticalId" id="vertical" value="<s:property value='verticalId' />"/>
                  <input type="hidden" name="applicationId" id="applicationId" value="<s:property value='applicationId' />"/>
              </FORM></TD></TR></TBODY></TABLE>
              <INPUT id="requestString" name="requestString" value="" type="hidden">
            <DIV 
            style="Z-INDEX: 2; BORDER-BOTTOM: #666 1px solid; POSITION: absolute; BORDER-LEFT: #fff 1px solid; PADDING-BOTTOM: 10px; BACKGROUND-COLOR: #ffffff; PADDING-LEFT: 10px; WIDTH: 300px; PADDING-RIGHT: 10px; DISPLAY: none; HEIGHT: 130px; BORDER-TOP: #fff 1px solid; BORDER-RIGHT: #666 1px solid; PADDING-TOP: 10px" 
            id=hiddenPane><div id="closeLinkDiv"
					style="width: 10px; float: right; cursor: pointer; color: #666; font-size: 11px; position: absolute; left: 300px;">X</div>
            <DIV id=hiddenPaneContent></DIV></DIV>
            <TABLE border=0 cellSpacing=0 id="menuTable" cellPadding=0 width="100%">
              <TBODY>
              <TR>
                <TD height=15 align=middle>
                  <DIV id=underProcess style="display:none;">
                  <DIV 
                  style="PADDING-BOTTOM: 10px; PADDING-LEFT: 10px; PADDING-RIGHT: 10px; PADDING-TOP: 10px">Request 
                  being processed. Do you want to cancel?</DIV>
                  <DIV style="PADDING-LEFT: 30px"><SPAN 
                  style="PADDING-BOTTOM: 5px; PADDING-LEFT: 5px; PADDING-RIGHT: 5px; PADDING-TOP: 5px"><INPUT style="WIDTH: 70px" id=processYes value=Yes width=70 type=button></SPAN><SPAN 
                  style="PADDING-BOTTOM: 5px; PADDING-LEFT: 5px; PADDING-RIGHT: 5px; PADDING-TOP: 5px"><INPUT style="WIDTH: 70px" id=processNo value=No type=button></SPAN></DIV></DIV></TD></TR>
              <TR>
                <TD align=left >
                  &nbsp;
                  </TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE>

<script>
$(function(){
           var defaultSearchBarText='<s:text name="execue.qi.search.all.published.apps"/>';
           var appSearchBarText='<s:text name="execue.qi.search.selected.published.apps"/>';           
		   var $appId=$("#applicationId").val(); 		  
		   if($appId=="1508"){
		   $action=$("#searchFormId").attr("action");
		   $action=$action.replace("semanticSearch","craigslistSearch");
		   $("#searchFormId").attr("action",$action);
		   }
varticalId="<s:property value='verticalId' />";
$("#shareButton").css("left",$("#metrics").position().left+780+"px");
$("#shareButton").css("top",$("#metrics").position().top+85+"px");
	
	if(varticalId=="1001"){
	$("#finance").attr("src","<c:out value="${basePath}" />/images/main/apps-heading-finance-active.png" );
	
	}
	else if(varticalId=="1002"){
	$("#govt").attr("src","<c:out value="${basePath}" />/images/main/apps-heading-govt-active.png" );
	
	}
	else if(varticalId=="1003"){
	$("#others").attr("src","<c:out value="${basePath}" />/images/main/apps-heading-others-active.png" );
	
	}

$("#closeLinkDiv").click(function(){
			$("#hiddenPane").hide();				  
								  });

$(".exampleQueries a.box2-link").click(function(){
		$("#metrics").val($(this).attr("title"));
			example=setTimeout(showHide,200);
			$("#searchBtnFreeForm").click();							  
  });
$(".exampleQueriesApp a.box2-link").click(function(){
		$("#metrics").val($(this).attr("title"));
			example=setTimeout(showHide,200);
			$("#searchBtnFreeForm").click();							  
  });
$(".exampleConcepts a.box2-link, a.instance-link,a.profile-link").click(function(){ 
		//str="Ask simple questions. Search datasets on the Web ..."
		var currentVal=$("#metrics").val(); 
		if(currentVal.indexOf(defaultSearchBarText)>-1 || currentVal.indexOf(appSearchBarText)>-1){currentVal="";}
		var newVal=	currentVal+" "+$(this).text();													  
		$("#metrics").val(newVal);
			//example=setTimeout(showHide,200);
			//$("#searchBtnFreeForm").click();							  
  });

});

function showMore(){
    $(".moreNewsSummary").slideDown();
    $(".moreNewsTitle").slideDown();  
	$("#showMoreLink1").attr("href","javascript:hideMoreLink();"); 
	$("#showMoreLink1 img").attr("src","<c:out value="${basePath}"/>/images/main/less-2.png");
	//$("#"+id+" img").attr("src","<c:out value="${basePath}"/>/images/main/app/less-2.png");
	//$("#"+id).attr("href","javascript:hideMoreLink('"+divName1+"','"+divName2+"','"+id+"');");
}
function hideMoreLink(){
    $(".moreNewsSummary").slideUp();
    $(".moreNewsTitle").slideUp();
	$("#showMoreLink1").attr("href","javascript:showMore();"); 
	$("#showMoreLink1 img").attr("src","<c:out value="${basePath}"/>/images/main/more-2.png");
	//$("#"+id+" img").attr("src","<c:out value="${basePath}"/>/images/main/app/more-2.png");
	//$("#"+id).attr("href","javascript:showMore('"+divName1+"','"+divName2+"','"+id+"');");	
}

var popupTimer;
var jsonVerticals;
$("#verticalPopup").live("mouseover",function(){
	clearTimeout(popupTimer);
});

$("#verticalPopup").live("mouseout",function(){
	popupTimer=setTimeout(function(){$("#verticalPopup").hide();  },2000);
});

function showVerticalPopup() { 
	if(popupTimer){
		clearTimeout(popupTimer);
	}
	
	var $left=$("#verticalMoreImg").position().left;
	$("#verticalPopup").css("left",$left-90+"px");
	$("#verticalPopup").show();
	if(!jsonVerticals) {
		getVerticals();
	}
	$("#verticalMoreImg").attr("src","<c:out value="${basePath}"/>/images/main/moreButtonHover.png");
}

function hideVerticalPopup() {
	$("#verticalMoreImg").attr("src","<c:out value="${basePath}"/>/images/main/moreButton.png"); 
	popupTimer=setTimeout(function(){$("#verticalPopup").hide();}, 3000);
} 


function getVerticals() {
	if($("#otherVerticals table[id='cat_menu'] td a").length == 0){
		jsonVerticals = $.get("<c:out value="${basePath}"/>/querySuggest/showVisibleVerticals.action", function(data) {
		    var data=eval(data);
		     $.each(data, function(i, item) {
		    	prepareTable(item);
		    });
		    $("#otherVerticals table[id='cat_menu'] td a").bind("click", function() {
				$("#searchString").val(str);
				$(this).blur();
				window.location="<c:out value="${basePath}"/>/vertical/verticalHome.action?verticalId="+$(this).attr("id");
			});
	  	});
  	}
}

function prepareTable(item) {
	var openTag = '<tr><td> <span class="arrowOrange" > > </span> <a href="#" id=' + item.id + ' name=' + item.name + ' class="no-active">' + item.name + '</a></td></tr>';
	$("#otherVerticals table[id='cat_menu']").append(openTag);
}

</script>
 <DIV 
                  style="Z-INDEX: 20; POSITION: absolute; HEIGHT: 16px; PADDING-TOP: 7px" 
                  id=waitImage>
                  <DIV style="MARGIN: auto; WIDTH: 133px; DISPLAY: none" 
                  id=pleaseWaitDiv><IMG id=waiting_img  src="<c:out value="${basePath}" />/images/main/Loader-main-page-3.gif" width="32" height="32"> </DIV></DIV>

   <script>
$(function(){
$("a").live("click",function(){
var id=$(this).attr("id"); 
var imgIndex=-1;

if($(this).find("img").attr("src")!=undefined){
imgSrc=$(this).find("img").attr("src"); //alert(imgSrc.indexOf("CSVFILE"));
imgIndex=imgSrc.indexOf("CSVFILE");
}
if(id!="showAllA" && id!="loginId" && id!="bookmarksSearchId" && id!="closeButtonLink" && id!="bookmarksIdResults" && id!="at15sptx" && imgIndex==-1){
$("#showLoaderPopup").show();
}
});
});
</script>              