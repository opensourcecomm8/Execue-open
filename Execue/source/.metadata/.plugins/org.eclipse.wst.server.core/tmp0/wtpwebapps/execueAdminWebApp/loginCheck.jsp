<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.execue.core.common.bean.Pagination"%>
<%
   String baseURL="";	
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
   Pagination pagination=(Pagination)request.getAttribute("PAGINATION");
	if(pagination!=null){
	 baseURL=pagination.getBaseURL();}
%>
<style>
a.menus{
color:#039;	
}
</style>
<script language="JavaScript" src="js/common/jquery.js"></script>
<span > 
                <span id="showLoginLink" style="padding-left:0px;"><a href="javascript:;" class="toplink"
   id="loginId" >Sign In</a> 
  <a href="<c:url value='/j_spring_security_logout'/>" class="toplink"
   id="logoutId" signout="true" style="display: none;white-space:nowrap"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img
   src="images/main/loaderTrans50.gif" ></span></span>
  <div id="hiddenPane"
   style="position: absolute;border-bottom:#666 solid 1px;border-right:#666 solid 1px;border-top:#fff solid 1px;border-left:#fff solid 1px; width: 350px; height: 200px; z-index: 2; display: none;padding:10px;background-color:#ffffff;width:300px;height:130px;right:50px;top:30px;">
     <div id="hiddenPaneContent" ></div>
      </div>  
   <script>
   showLoginInfo('<security:authentication property="principal.fullName"/>');
 // showPublisherInfo('<security:authentication property="principal.admin"/>','<security:authentication property="principal.publisher"/>');
  $("#loginId").hide();
  function showLoginInfo(name) {
		name = name.replace(/^\s+|\s+$/g,"");
		if(name && name.length > 0) {
			$("#showWelcome").empty().append('<s:text name="execue.login.welcome.message"/> ' + name);
			$("#loginId").remove();
			$("#logoutId").show();
			$("#moredata").hide();
			
		}	else{$("#logoutId").remove();}
	}
	
	$("a#loginId").click(function(){
Pane_Left=$(this).position().left;
Pane_Top=$(this).position().top;
if($("#PivotTable").html()!=null){
	sim(Pane_Left,Pane_Top);	
	}
$("#showLoginLink").hide(); 	
$("#loadingShowLoginLink").show(); 							   
$.get("ajaxlogin.jsp", {ajax: 'true', noRedirect : 'true'}, function(data) { 
		$("#loadingShowLoginLink").hide(); 

		$("#hiddenPaneContent").empty(); 
		$("#hiddenPaneContent").append(data);
		//$("#hiddenPane").css("left",Pane_Left-270); 
		//$("#hiddenPane").css("top",Pane_Top+23); 
		$("#hiddenPane").fadeIn("slow");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#hiddenPaneContent").css("height",130+"px");
		$("#showLoginLink").show();
		$("#hiddenPane").css("height","130px");
		$("#userName").select().focus();
	});
});
   </script>