<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link id="page_favicon" href="favicon.ico" rel="icon" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
#metrics{
width:600px;
height:18px;
padding-top:3px;
}
#queries td{
text-align:left;
font-size:9pt;
}
#queries td a{
color:#333;
}
a#closeButtonLink{
cursor:pointer;	
}
a#closeButtonLink img{
cursor:pointer;	
padding-bottom:6px;
}
a.links_white,a#adminId,a#logoutId{
    text-decoration:none;
    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;
font-size:14px; 
} 
a#logoutId{
color:#FF9505;
}
a.links_white:hover,a#adminId:hover,a#logoutId:hover{
    text-decoration:underline;
    font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;
font-size:14px; 
}   
</style>
 <c:set var="basePath" value="<%=request.getContextPath()%>" />
  
<div id="fSearch" style="margin:auto;width:100%">

		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			
			<tr>
			  <td valign="top">
				<form action='<c:out value="${basePath}"/>/semanticSearch.action' onSubmit="return submitDataFreeForm(false)" id="searchFormId"
					accept-charset="utf-8" method="post">
                    
                    <input type="hidden" name="request" id="request" />
                    <input type="hidden" name="type" id="type" value="SI"/>
					<input type="hidden" name="disableCache" id="disableCache" value="false"/>
					<s:hidden name="applicationId"/>
                    <s:hidden name="verticalId"/>
				<table width="96%" border="0" align="left" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="32" valign="top">

						
               
               <!-------------------------------------------------------------------------------->
               
               
               <table id="freeformOuterTable" border="0" align="left" cellpadding="0" cellspacing="0">
 
  <tr  >
    <td height="10" valign="middle" colspan="3"><img id="ig" src="<c:out value="${basePath}"/>/images/main/1pix.gif" width="1" height="10" /></td>
    
  </tr>
  <tr  >
   <s:set name="metric" value="%{''}" />
   <s:if test="request!=null && type=='SI'" >
   <s:set name="metric" value="request" />
   </s:if>
    <td  height="25" valign="middle"><div style="padding-left:6px;margin-top:0px;"><input id="metrics" type="text" maxlength="500"	 value="<s:property value='#metric' />" size="" style="border:none;font-size:13px;font-family:Arial;color:#525252;padding-top:4px;padding-left:3px;height:21px;background: url(<c:out value="${basePath}"/>/images/main/text-bg.jpg)"  /></div></td>
    <td width="22" align="left" valign="middle"><div id="search_image" style="padding-left:10px;padding-right:3px;margin-top:0px;">
    
    <input type="image" 
							id="searchBtnFreeForm" src="<c:out value="${basePath}"/>/images/quinnox/search.png" alt="Search" title="Search" tabindex="-1"  class="imgNoBorder1" /></div></td>
    <td width="22" align="left" valign="middle">
    <!--
    <s:if test="result.cacheResultPresent" ><input type="image" 
							id="searchBtnFreeForm" src="images/refresh.png" onclick="refreshClicked();" alt="Refresh" title="Refresh" tabindex="-1"  class="imgNoBorder1" /></s:if>
                            -->
                            </td>
  </tr>
  <tr>
    <td  align="right" valign="top">
    
    
    
    <table width="100%" border="0" cellspacing="0" cellpadding="0" style="margin-bottom:4px;">
      <tr>
        
        
        <td width="72%" align="left">&nbsp;</td>
        <td width="7%"  align="left"><div id="showBookmarksSearchLink" style="padding-right:15px;"  ><a href="javascript:;" class="links_white" id="bookmarksSearchId" style="font-size:7pt;"><s:text name="execue.freeform.SavedQueries.link" /></a></div>
          <div id="loadingShowBookmarksSearchLink" style="display: none;padding-left:33px;padding-right:33px;"><img
									src="<c:out value="${basePath}"/>/images/main/loaderTrans.gif" /></div></td>
       <!--   <td width="7%" align="left" style="padding-right:15px;"><div id="showExamplesLink"  ><a href="javascript:;" class="links_white" id="examplesId-freeform" style="font-size:7pt;"><s:text name="execue.freeform.examples.link" /></a></div></td>-->
                                    
         <td id="advancedSearchTd" width="7%" align="right" style="padding-right:15px;" ><a href="<c:out value="${basePath}"/>/execueHome.action?type=QI" class="links_white" style="font-size:7pt;padding-right:4px;" ><s:text name="execue.freeform.advancedSearch.link"  /></a></td>
         
         <td width="7%" align="right"><a href="http://wiki.semantifi.com/index.php/SearchTips" class="links_white" target="_blank" style="font-size:7pt;padding-right:4px;" ><s:text name="execue.freeform.searchTips.link" /></a></td>
         
      </tr>
    </table>
    
    
    
    </td><td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
               </table>
               
               
              <!-------------------------------------------------------------------------------->
               
          
						</td>
					</tr>
					
					
					<tr>
						<td  align="left" valign="top"><!-- div id="pleaseWaitDiv" style="display:none;margin:auto;width:133px;margin-top:5px;margin-bottom:5px;"><img  id="waiting_img" src="images/main/wait.gif" width="133" height="11" /></div--></td>
					</tr>
					<tr>
					<tr>
						<td align="left"><input type="hidden" name="request" id="requestId" /><input type="hidden" value="<%=com.execue.core.common.type.BookmarkType.SEARCH_INTERFACE%>" id="bookmarkType" /></td>
					</tr>

				</table>
				</form>
				</td>
			</tr>
		</table>

</div>
<div id="queries" style="border-right:1px solid #000;border-bottom:1px solid #000;border-top:1px solid #CCC;border-left:1px solid #CCC;position: absolute; top: 0px; left: 0px;height: auto; width:auto; overflow: auto;background-color:#ffffff;display:none;z-index:10;">
	<table cellpadding="3">
	<tr><td align="left" style="border-bottom:#CCC thin dashed;"><span style="padding-left:2px;color:#666;float:left;text-align:left;" ><b>General Questions</b></span></td></tr>		
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query1"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query2"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query3"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query4"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query5"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query6"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query7"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query8"/></s:a>
			</td>
		</tr>
		<tr>
			<td>
			<s:a href="#"><s:text name="execue.semantify.query9"/></s:a>
			</td>
		</tr>	
  </table>
</div>
<div id="hiddenPane"
	style="position: absolute; width: 350px; height: 350px; z-index: 1000; display: none;">
<table border="0" cellpadding="0" cellspacing="0" width="310" align="left" >
        <!-- fwtable fwsrc="boxTemplate.png" fwpage="Page 1" fwbase="boxTemplate.gif" fwstyle="Dreamweaver" fwdocid = "901881679" fwnested="0" -->
        
        <tr>
          <td width="15"><img name="boxTemplate_r1_c1"
     src="<c:out value="${basePath}"/>/images/quinnox/boxTemplate_r1_c1.gif" width="15" height="40"
     border="0" id="boxTemplate_r1_c1" alt="" /></td>
          <td background="<c:out value="${basePath}"/>/images/quinnox/boxTemplate_r1_c2.gif" align="right"></td>
          <td width="15"><img
     name="boxTemplate_r1_c3" src="<c:out value="${basePath}"/>/images/quinnox/boxTemplate_r1_c3.gif"
     width="15" height="40" border="0" id="boxTemplate_r1_c3"
     alt="Close" title="Close"/></td>
          <td width="13"><img src="<c:out value="${basePath}"/>/images/main/spacer.gif" width="1" height="40"
     border="0" alt="" /></td>
          </tr>
        <tr>
          <td background="<c:out value="${basePath}"/>/images/main/boxTemplate_r2_c1.gif"><img
     name="boxTemplate_r2_c1" src="<c:out value="${basePath}"/>/images/main/boxTemplate_r2_c1.gif"
     width="15" height="10" border="0" id="boxTemplate_r2_c1" alt="" /></td>
          <td width="1045" valign="top" bgcolor="#FFFFFF" >
            <div id="hiddenPaneContent"></div>
            </td>
          <td background="<c:out value="${basePath}"/>/images/main/boxTemplate_r2_c3.gif" ><img
     name="boxTemplate_r2_c3" src="<c:out value="${basePath}"/>/images/main/boxTemplate_r2_c3.gif"
     width="15" height="10" border="0" id="boxTemplate_r2_c3" alt="" /></td>
          <td><img src="<c:out value="${basePath}"/>/images/main/spacer.gif" width="1" height="10"
     border="0" alt="" /></td>
          </tr>
        <tr>
          <td><img name="boxTemplate_r3_c1"
     src="<c:out value="${basePath}"/>/images/main/boxTemplate_r3_c1.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c1" alt="" /></td>
          <td background="<c:out value="${basePath}"/>/images/main/boxTemplate_r3_c2.gif" valign="top"><img
     name="boxTemplate_r3_c2" src="<c:out value="${basePath}"/>/images/main/boxTemplate_r3_c2.gif"
     width="25" height="13" border="0" id="boxTemplate_r3_c2" alt="" /></td>
          <td><img name="boxTemplate_r3_c3"
     src="<c:out value="${basePath}"/>/images/main/boxTemplate_r3_c3.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c3" alt="" /></td>
          <td><img src="<c:out value="${basePath}"/>/images/main/spacer.gif" width="1" height="13"
     border="0" alt="" /></td>
          </tr>
        </table>
</div>
 <div  id="underProcess" style="margin:auto;">
                  <div style="padding-left:30px;margin:auto;width:280px;text-align:center;padding-top:10px;"><s:text
					name="execue.cancel.request" /></div>
                  <div style="margin:auto;float:left;display:inline;padding:10px;"><div style="padding-left:110px;float:left;display:inline;padding-right:5px;"><input id="processYes" type="button" width="70" style="width:70px;" value="Yes"/></div><div style=";float:left;display:inline;"><input type="button" id="processNo" style="width:70px;"  value="No"/></div></div>
                  </div> 
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