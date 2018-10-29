<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security' uri='http://www.springframework.org/security/tags'%>
 <script language="JavaScript" src="../js/goog_analytics.js"></script>

<style>
.login_bg {
	background-image: url(../images/login_r2_c1.jpg);
}

.top-bg {
	background-image: url(../images/results_header_bg.jpg);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}
.technology-1 {
	background-image: url(../images/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
	height:52px;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;

}

.welcome_user_type{
color:#333;	
}
.tableTitles td{
padding-left:2px;
height:25px;
}
</style>


      
      
      
     <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" valign="middle" bgcolor="#487CAD" class="top-bg"><a href="../index.html"></a></td>
    <td width="82%" class="top-bg"><div id="topMenuContent">
    
    <table border="0" align="right" cellpadding="2" cellspacing="0" >
      <tr>
        <td height="28" style="color:#333;font-size:11px;"><div id="showWelcome">
   
        </div>
        </td>
        <td><a href="../SemantifiPortal.html" class="toplink">About</a>&nbsp;</td><td><a href="../qi/browseApps.action" class="toplink">Browse all Apps</a>&nbsp;</td><td> <a id="publishAppsLinks" href="../publishApps.jsp" class="toplink">Publish Apps</a>&nbsp;</td><td><a href="http://wiki.semantifi.com/index.php/App_Ideas" target="_blank" class="toplink">App Ideas</a>&nbsp;</td><td> <a href="../semantifi-for-enterprise.html" class="toplink">Semantifi for Enterprise</a>&nbsp;</td><td> <a href="http://wiki.semantifi.com/index.php/Main_Page" class="toplink">Wiki</a>&nbsp;</td><td> <a href="http://blog.semantifi.com/" class="toplink">Blog</a>&nbsp;</td><td> <a href="../contact.html" class="toplink">Contact</a>&nbsp;<!--a href="login.jsp" class="toplink">Sign In</a--></td><td  align="right"><div id="logoutId" style="margin-top:0px;"><span style="padding-left:10px;"><a href="<c:url value='/j_spring_security_logout'/>" class="toplink"  tabindex="-1">SIGN OUT</a></span></div></td>
      </tr>
    </table>
    
    
    
    </div></td>
  </tr>
  </table></td>
  </tr>
  <tr>
    <td valign="top">
  
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2" align="center" valign="top"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="innerPages-bgColor">
          <tr>
            <td height="50" colspan="2" align="center" valign="top" bgcolor="#FFFFFF" class="technology-1">
            <table width="916" border="0" align="center" cellpadding="0" cellspacing="0" id="logoTable">
              <tr>
                <td width="20%" height="50" align="left" valign="middle"><a href="../index.html"><img src="../images/semantifi3bnew/inner-page-logo.png" width="112" height="30" vspace="10" border="0" /></a></td>
                <td width="3%" valign="middle">&nbsp;</td>
                <td align="right" valign="middle" class="white18">A Marketplace of <span class="orange18"> Search Apps </span>for a community of <span class="orange18"> Publishers and Consumers</span></td>
                <td width="1%" align="right" valign="middle" class="white18">&nbsp;</td>
              </tr>
            </table></td>
          </tr>
          
     
     </table>
     </td>
     </tr>     
    </table>
  
    </td>
  </tr>
  
</table>