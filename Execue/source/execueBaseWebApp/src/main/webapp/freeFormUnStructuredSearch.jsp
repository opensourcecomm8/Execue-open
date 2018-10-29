<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link id="page_favicon" href="favicon.ico" rel="icon" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
 
    <c:set var="basePath" value="<%=request.getContextPath()%>" />
<link href="<c:out value="${basePath}"/>/css/common/qiStyle_new.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="<c:out value="${basePath}"/>/css/common/roundedSearch.css" type="text/css" />
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
</style>
 <c:set var="basePath" value="<%=request.getContextPath()%>" />
<div style="margin:auto;width:100%">

		<table border="0" cellpadding="0" cellspacing="0" width="100%">
			
			<tr>
			  <td valign="top">
				<form action='<c:out value="${basePath}"/>/unstructuredSearch.action' onSubmit="return submitDataUnstructuredFreeForm(true)" id="searchFormId"
					accept-charset="utf-8" method="post">
                    
                    <input type="hidden" name="userQuery" id="request" />
                    <input type="hidden" name="type" id="type" value="SI"/>
					<input type="hidden" name="disableCache" id="disableCache" value="false"/>
                    <input type="hidden" name="isFromFacatedSearch" id="isFromFacatedSearch" value="YES" />
                    <input type="hidden" name="selectedDefaultVicinityDistanceLimit" id="selectedDVDLimit" value="" />
                    <input type="hidden" name="imagePresent" id="imgPresent" value="" />
                    <input type="hidden" name="showClicked" id="showClicked" value="No" />                                          
					<s:hidden name="applicationId" />
                    <s:hidden name="verticalId"/>
                    <s:hidden name="applicationName"/>
                    <s:hidden name="modelId"/>
                    <input type="hidden" name="resultViewType" id="resultViewTypeId" value="<s:property value='resultViewType' />" />
                    
                    <input type="hidden" name="locationSuggestTerm.locationBedId" id="locationSuggestTerm_locationBedId" value="" />   
                    <input type="hidden" name="locationSuggestTerm.latitude" id="locationSuggestTerm_latitude" value="" /> 
                    <input type="hidden" name="locationSuggestTerm.longitude" id="locationSuggestTerm_longitude" value="" /> 
                    <input type="hidden" name="locationSuggestTerm.id" id="locationSuggestTerm_id" value="" /> 
                    <input type="hidden" name="locationSuggestTerm.displayName" id="locationSuggestTerm_displayName" value="" />                     
                    
				<table width="96%" border="0" align="left" cellpadding="0"
					cellspacing="0">
					<tr>
						<td height="32" valign="top">

						
               
               <!-------------------------------------------------------------------------------->
               
               <table width="1000" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="631" valign="top"><table width="631" border="0" align="left" cellpadding="0" cellspacing="0">
 
 
  <tr  >
   
    <td  height="25" valign="middle">
    
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><div style="padding-top:3px;padding-left:4px;padding-right:0px;padding-bottom:1px;"><a href="index.jsp"><img name="index_r1_c1" src="images/main/inner-page-logo.png" border="0"
					id="index_r1_c1" alt="" /></a></div></td>
    <td> <div style="padding-left:6px;margin-top:0px;">
    
    <input id="metrics" type="text" maxlength="500"	 value="<s:property value='userQuery' />" size="" style="border:none;font-size:13px;font-family:Arial;color:#525252;padding-top:4px;padding-left:3px;height:21px;background: url(<c:out value="${basePath}"/>/images/main/text-bg.jpg) no-repeat;"  />
    
    </div></td>
  </tr>
</table>

   
    
    </td>
    <td width="22" align="left" valign="middle"><div id="search_image" style="padding-left:15px;padding-right:3px;margin-top:3px;">
    
     <input type="image" id="searchBtnFreeForm" src="<c:out value="${basePath}"/>/images/main/search.png" alt="Search" title="Search" tabindex="-1"  class="imgNoBorder1" /></div></td>
    <td width="22" align="left" valign="middle">
    <!--
    <s:if test="result.cacheResultPresent" ><input type="image" 
							id="searchBtnFreeForm" src="images/refresh.png" onclick="refreshClicked();" alt="Refresh" title="Refresh" tabindex="-1"  class="imgNoBorder1" /></s:if>
                            -->
                          </td>
  </tr>
  <tr>
    <td  align="right" valign="top">
    
    
    
    <table width="100%" border="0" cellspacing="0" cellpadding="0" id="advancedMenu">
      <tr>
        
        <td width="90%" align="left">&nbsp;</td>
        
        <!--td width="12%" style="padding-left:5px;"  align="left"><div id="showBookmarksSearchLink"  ><a href="javascript:;" class="links_green" id="bookmarksSearchId" style=""><s:text name="execue.freeform.SavedQueries.link" /></a></div>
          <div id="loadingShowBookmarksSearchLink" style="display: none;padding-left:33px;padding-right:33px;"><img
									src="<c:out value="${basePath}"/>/images/main/loaderTrans.gif" /></div></td-->
        <td width="5%" valign="top" align="left"><div id="showExamplesLink"  ><a href="javascript:;" class="links_white" id="examplesId-freeform" style=""><s:text name="execue.freeform.examples.link" /></a></div></td>
                                    
        
         
         <td width="5%" valign="top" align="right" style="padding-left:10px;"><a href="http://wiki.semantifi.com/index.php/SearchTips" class="links_white" target="_blank" style="padding-right:4px;" ><s:text name="execue.freeform.searchTips.link" /></a></td>
         
      </tr>
    </table>
    
    
    
    </td><td>&nbsp;</td>
    <td>&nbsp;</td>
    </tr>
               </table></td>
    <td align="center" style="padding-top:0px;color:#FFF;">Didn't find what you are looking for ? <a class="teach" id="teachSystemLink" href="javascript:;">Teach system</a></td>
  </tr>
</table>

               
               
               
              <!-------------------------------------------------------------------------------->
               
          
						</td>
					</tr>
					
					
					<tr>
						<td  align="left" valign="top">
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
     src="<c:out value="${basePath}"/>/images/main/boxTemplate_r1_c1.gif" width="15" height="40"
     border="0" id="boxTemplate_r1_c1" alt="" /></td>
          <td background="<c:out value="${basePath}"/>/images/main/boxTemplate_r1_c2.gif" align="right"><a id="closeButtonLink" alt="Close" title="Close" href="#"><img
     name="boxTemplate_r1_c2" src="<c:out value="${basePath}"/>/images/main/cButton.png" border="0" id="boxTemplate_r1_c2" /></a></td>
          <td width="15"><img
     name="boxTemplate_r1_c3" src="<c:out value="${basePath}"/>/images/main/boxTemplate_r1_c3.gif"
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

<script type="text/javascript">


$(function(){
	/*$("#closeButtonLink").mouseover(function(){
		$(this).find("img").attr("src","<c:out value="${basePath}"/>/images/main/cButtonHover.png");								 
	});
	$("#closeButtonLink").mouseout(function(){
		$(this).find("img").attr("src","<c:out value="${basePath}"/>/images/main/cButton.png");								 
	});*/
	$("#closeButtonLink").click(function(){
		$(this).blur();
		$("#hiddenPane").fadeOut();								 
	});
});
</script>