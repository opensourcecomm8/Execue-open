<%@ page language="java"%>
<%@page import="com.execue.web.core.util.ExecueWebConstants"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@page import="com.execue.core.common.bean.Pagination"%>

<c:set var="basePath" value="<%=request.getContextPath()%>" />
<c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />

<style type="text/css">
a.toplink {
	font-family: "trebuchet ms";
	font-size: 12px;
	font-weight: normal;
	color: #900;
	text-transform: uppercase;
	background-repeat: no-repeat;
	background-position: left;
	text-indent: 5px;
	float: left;
}
a.toplink:hover {
	font-family: "trebuchet ms";
	font-size: 12px;
	font-weight: normal;
	color: #900;
	text-transform: uppercase;
	text-decoration: none;
	text-indent: 5;
}
</style>

<table border="0" align="right" cellpadding="2" cellspacing="0" >
      <tr>
        <td height="28"><span id="showWelcome" style="padding-right:20px;color:#333;padding-right:18px;font-family:'Trebuchet MS', Arial, Helvetica, sans-serif;text-align:right;width:1000px;margin:auto;">
   </span>&nbsp;</td>
        <td><a href="SemantifiPortal.html" class="toplink">About</a>&nbsp;</td><td><a href="qi/browseApps.action" class="toplink">Browse all Apps</a>&nbsp;</td><td> <a href="publishApps.jsp" id="publishAppsLinks" class="toplink">Publish Apps</a>&nbsp;</td><td><a href="http://wiki.semantifi.com/index.php/App_Ideas" target="_blank" class="toplink">App Ideas</a>&nbsp;</td><td> <a href="semantifi-for-enterprise.html" class="toplink">Semantifi for Enterprise</a>&nbsp;</td><td>&nbsp;<a href="http://wiki.semantifi.com/index.php/Main_Page" class="toplink">Wiki</a>&nbsp;</td><td> <a href="http://blog.semantifi.com/" class="toplink">Blog</a>&nbsp;</td><td> <a href="contact.html" class="toplink">Contact</a>&nbsp;<!--a href="login.jsp" class="toplink">Sign In</a--></td><td width="auto" align="left"><span id="logInLink"></span>&#13;</td>
      </tr>
    </table>
    <script>
	
	$.get("loginCheck.jsp",{},function(data){
	$("#logInLink").html(data);
	var dat=$("#logoutId").attr("signout");
	if(dat=="true"){
		$("#signUpAppsTr").show();
		$("#publishAppsLinks").attr("href","<c:out value='${adminPath}' />swi/showSearchAppsDashboard.action");
		}
	});
	</script>