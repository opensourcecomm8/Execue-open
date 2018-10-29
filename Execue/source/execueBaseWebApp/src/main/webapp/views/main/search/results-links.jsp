<%@ page language="java" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<style>
.technology-1 {
    background-image: url(images/quinnox/top-bg-inner-2.png);
    background-repeat: repeat-x;
    background-position: left top;
    height: 51px;
}
td.top-bg a,#adminId{
color:#3C71A1;  
font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif;
font-size:14px;
    }
</style>

<table width="100%" border="0" cellspacing="0" cellpadding="0" >
              <tr>
                <td class="top-bg">
                
                <div style="margin:auto;width:91%">
                
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                    
                      <td width="100%" align="center">
                      
                      
                      <table width="100%" border="0" cellspacing="0" cellpadding="3" align="right">
                          <tr>
                            <td width="61%" height="20" align="right"><!-- sub links starts -->
                              <!--div id="pleaseWaitDiv" style="display: none; margin: auto; width: 133px; margin-top: 1px; margin-bottom: 0px;height:10px;float:right;"> <img id="waiting_img" src="images/wait1e.gif"  /> </div-->
                              <div style="height:16px;padding-top:2px;z-index:20;position:absolute;" id="waitImage"><div id="pleaseWaitDiv" style="display: none; margin: auto; width: 110px;">
								<img id="waiting_img" src="images/main/Loader-main-page-3.gif" width="22" height="22" />
							</div>
                        </div>
                              </td>                          
                                    <td width="20%" align="right"><div id="showWelcome" style="padding-right: 20px; color: #333; padding-top: 0px; padding-right: 18px;font-size:14px; font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; text-align: right; width: 500px; margin: auto;">
   </div></td>
                          
                          
                         <!--  <td width="1%" id="publisherTdSeperator" style="display:none">|</td>-->
                          <td width="5%" align="center" id="publisherTd" style="display:none">
						<div id="showAdminLink"><a href="<s:text name="execue.global.publishApps.link"/>"
							id="adminId"><s:text name="execue.publisher.link" /></a></div>
						<div id="loadingShowLoginLink1" style="display: none;"><img
							src="images/main/loaderTrans.gif"></div>
						</td>
						
                          
                          
                          <td width="1%" id="seperatorId" style="display:none">|</td> 
                            <td width="6%" align="left" >
                            
                            <span id="showLoginLink" style="padding-left:3px;"><a href="javascript:;" class="links_sem3"
   id="loginId" ><s:text name="execue.login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img
   src="images/main/loaderTrans.gif" ></span>
                            
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
                <td align="center" class="technology-1"  ><div class="qiHeaderDiv" style="margin-top:0px;" >
                   <div id="searchBar">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    
    <td width="92%" align="left"> <jsp:include page="../../../freeFormSearch.jsp"  flush="true" /></td>
  </tr>
</table>
</div>
                  </div></td>
              </tr>
            </table>
            
            

<!--form>
<table width="98%" border="0" cellspacing="0" cellpadding="3">
	<tr>
	  <td width="77%">&nbsp;</td>
		<td width="6%" align="center">&nbsp;</td>
		<td width="1%" class="fieldNames">&nbsp;</td>
		<td width="6%">
		&nbsp;
		</td>
        <td width="1%" class="fieldNames">&nbsp;</td>
		<td width="4%">
		<div id="back"><a href="javascript:;" class="links"
			id="backTohome" onclick="backToHome();">Back</a></div>
		</td-->
		
	<!--/tr>
</table>
</form-->
