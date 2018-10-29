<style>
.appTable td{
font-size:12px;	
}
.appTableHeading{
	color:#900;
	font-size:12px;
}
a.learnMoreLink{
white-space:nowrap;
color:#00F;
}
a.learnMoreLink:visited{
color:#00F;	
}
a.learnMoreLink:hover{
color:#00F;	
text-decoration:none;
}
</style>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2" align="center" valign="top"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" >
          <tr>
            <td height="470" colspan="2" align="left" valign="top">
              
              <table width="100%" border="0" align="left" cellpadding="1" cellspacing="0">
                            
                           
                <tr>
                  <td colspan="2" align="left" valign="top" >                
                    <table width="100%" border="0" align="left" cellpadding="1" cellspacing="0" id="t1" class="appTable">                
                      <tr>
                        <td width="15%" align="left" height="25" class="pagingTop">&nbsp;</td>
                        <td width="16%"  class="pagingTop" style="padding-left:3px;"><span class="appTableHeading"><s:text name='App Name'/></span></td>
                        <td width="53%" class="pagingTop" style="text-align:left;padding-left:10px;"><span class="appTableHeading"><s:text name='Description'/></span></td> 
                        <td width="16%"  class="pagingTop" style="padding-left:30px;"><span class="appTableHeading"><s:text name='Publisher'/></span></td>                     
                        </tr>                  
                      <s:iterator value="applicationsList" status="appStatus" >                 
                        
                            <tr>
                              <s:if test="applicationImageId != -1">
                                <td width="15%"  align="left" class="pagingTop" height="85"><a href='<c:url value="/appIndex.action?applicationId=${applicationId}&application.name=${fn:replace(applicationName,' ','_')}"/>' style="float:left">
                                <div class="imgOuter" style="background:url(../images/loadingBlue.gif) no-repeat center center;width:70px;height:70px;"><img src="qi/getImage.action?applicationId=<s:property value='applicationId'/>&appImageId=<s:property value='applicationImageId'/>"  border="0" width="70" /></div>
                                </a></td>
                                </s:if>   
                              <s:else>
                                <td width="15%"  align="left"  class="pagingTop"  height="85"><a href='<c:url value="/appIndex.action?applicationId=${applicationId}&application.name=${fn:replace(applicationName,' ','_')}"/>' style="float:left">
                                <div class="imgOuter" style="background:url(../images/loadingBlue.gif) no-repeat center center;width:70px;height:70px;">
                                <img src="../images/noImage-icon.gif" border="0" width="70" ></img>
                                </div>
                                </a></td>
                                </s:else>  
                              <td width="16%" valign="top" class="pagingTop" name="appNameTd"><s:property value="applicationName"/></td>
                              <td width="53%" valign="top"  class="pagingTop">
                                      <table width="95%" border="0" align="center" cellspacing="0"  cellpadding="0">
                                          <tr>
                                            <td style="text-align:justify;"> <s:property value="applicationDescription"/> <a href="<s:property value='applicationURL'/>" onclick="this.blur();" target="_blank" class="learnMoreLink"  ><s:text name='execue.diplayAppApplication.app.learnMore'/></a></td>
                                          </tr>
                                         
                                        </table>

                              </td>
                              <td width="16%" valign="top" name="publisherNameTd" style="padding-left:30px;" class="pagingTop"><s:property value="publisherName"/></td>
                              </tr>                 
                           
                                       
                        </s:iterator>
                        <tr><td colspan="3">
                        <s:if test="applicationsList.size==0">
                        <div style="width:100%;padding:10px;text-align:center;">No data found</div>
                           </s:if>
                         </td>  </tr>
                      </table>                 
                    </td>
                  </tr>      
              
                </table>
              
              </td>
          </tr>
     <tr><td>
     
      <table  align="center" width="40%">
	   <tr>
	      <td align="center" style="padding-bottom:20px;">
	        <s:set name="url" id="url" value="%{'qi/displayApplications.action'}"></s:set>
			<div id="paginationDiv2" style="float:inherit;"><pg:page targetURL="${url}" targetPane="dynamicPane" /></div>
	      </td>
		</tr>
	 </table>
     </td></tr>
     </table>
     </td>
     </tr>     
    </table>

   <script>
   $(function(){ 
				  if($("#searchType").val()=="byAppName"){
					highLightTextBrowseAllApps("appNameTd"); 
				  }
				  if($("#searchType").val()=="byPublisherName"){
					highLightTextBrowseAllApps("publisherNameTd");  
				  }
				  $(".imgOuter").css("background","none");
			  });
   </script>