<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/loose.dtd">
<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
   <%@page import="com.execue.web.core.util.ExecueWebConstants"%>
    <c:set var="basePath" value="<%=request.getContextPath()%>" />
    <c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />
<HTML xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<title>semantifi | Sign in</title>
<META content="text/html; charset=iso-8859-1" http-equiv=Content-Type>
<META name=GENERATOR content="MSHTML 8.00.6001.18702">
<link href="css/common/qiStyle_new.css" rel="stylesheet" type="text/css" />
<link id="page_favicon" href="favicon.ico" rel="icon"
	type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />

<script language="JavaScript" src="<c:out value="${basePath}"/>/js/common/jquery.js"></script>


<script>

 $(document).ready(function () {
$ht=screen.height;
$htSearchDiv=($ht-170)-($ht/6);
$("#tdHeight").css("height",$htSearchDiv+"px");
$("#username").focus();
							 });

</script>
<style>
.login_bg {
	background-image: url(<c:out value="${basePath}"/>/images/main/login_r2_c1.jpg);
}
</style>
<style>
.login_bg {
	background-image: url(<c:out value="${basePath}"/>/images/main/login_r2_c1.jpg);
}
a.breadCrumbLink,a{
color:#3C71A1; 
}
</style>
<style>
.top-bg {
	background-image: url(<c:out value="${basePath}"/>/images/main/results_header_bg.jpg);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}
.technology-1 {
	background-image: url(<c:out value="${basePath}"/>/images/quinnox/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
	height:52px;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #ffffff;
}
input[type='text'],input[type='password'],textarea,.inputText{
border:1px solid #CCC;	
}

ul li{
line-height:22px;	
}
#footernew{
width:100%;
position:absolute;
bottom:0px;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</HEAD>
<BODY>

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
                            <td width="56%" height="20" align="center"><!-- sub links starts -->
                              &nbsp;</td>
                          
                                    
                                    
                                   
                            
                           
                                    <td width="30%" align="center"><div id="showWelcome" style="padding-right:20px;color:#333;padding-top:0px;padding-right:18px;font-size:14px;font-family:'Trebuchet MS', Arial, Helvetica, sans-serif;text-align:right;width:500px;margin:auto;">
   </div></td>
                          <td width="7%"><!--<a href="feedback.jsp"><s:text name="execue.Feedback.link" /></a>--></td><!--td width="1%">|</td> 
                            <td width="6%" align="right" style="white-space:nowrap;" >
                            
                            <span id="showLoginLink" style="padding-left:3px;" ><a href="javascript:;" class="links_sem3"
   id="loginId"  ><s:text name="execue.login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img src="images/loaderTrans.gif" ></span>
                            
                            </td-->
                           
                          </tr>
                        </table>
                        
                        
                        </td>
                    </tr>
                </table>
                
                </div>


</td>
</tr>
  
  
  <tr >
    <td class="technology-1" height="42" >
    <div style="margin:auto;width:91%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    
    <td width="92%" align="left"> <!--jsp:include page="../../../freeFormSearch.jsp"  flush="true" /--></td>
  </tr>
</table>
</div>

      </td>
  </tr>
  
  <tr >
    <td  bgcolor="#FFFFFF" >
    
     <div style="width:90%;margin:auto;">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr >
		<td height="25"  class="normalText" background="images/main/bc-bg.jpg" >
		&nbsp;
		</td>
	</tr>
	
</table>

</div>
  
      </td>
  </tr>
</table>


<DIV id=content>

<table width="100%" border="0" cellspacing="0" cellpadding="0" height="680"> 
  <!--tr>
    <td height="60" valign="middle" class="fontRed" style="padding-left:50px;"><c:if test="${not empty param.login_error}">
	<s:text name="execue.login.failure"/><br>
	<s:text name="execue.login.failure.reason"/> <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />. 
</c:if></td>
  </tr-->
  <tr>
    <td valign="top" bgcolor="#FFFFFF">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="32%"><DIV id=feedbackIntro style="background-color: #E0E0E0; border: 1px #DFE8FD solid; width: 78%; padding-left: 20px; min-height: 280px; height:auto;float: right; margin-top: 5px; padding-right: 20px;">
			<s:text name="execue.signup.benefits.text" />
			</DIV></td>
    <td width="62%">
    
    <DIV  style="background-color:#FFFFFF;;border:1px #DFE8FD solid;width:100%;padding-left:20px;height:280px;float:left;margin-top:5px;margin-left:8px;">
    <table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
	  <td align="center">
			

			<table width="100%" border="0" cellspacing="0" cellpadding="0"
					height="60%">
					<tr>
						<td  valign="middle" align="left" class="fontRed" style="padding-top:10px;"><c:if
							test="${not empty param.login_error}">
							<s:text name="execue.login.failure" />
							<br>
							<s:text name="execue.login.failure.reason" />
							<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />. 
   
    </c:if></td>
					</tr>
					<tr>
						<td align="left" valign="top">
						<table width="439" border="0" align="left" cellpadding="0"
							cellspacing="0">
							<tr>
								<td width="42%" height="40" valign="middle"><span
								style="color: #036; font-size: 18px; padding-left: 12px; height: 40px;"><s:text name="execue.Signin.heading" /></span></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td valign="top" bgcolor="#FFFFFF">
						<form action="<c:url value='/j_spring_security_check'/>"
							method="post" name="Login" id="Login">
						<div
							style="width: 90%; margin: auto; padding: 3px;background-color:#FFF;">
						<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
						<div id="actionMessage" style="color: green"><s:actionmessage /></div>
						<div background-color:#FFFfff;>
                        <table width="439" border="0" align="center" cellpadding="0"
							cellspacing="0" bgcolor="#FFFfff">
							<!-- fwtable fwsrc="login.png" fwpage="Page 1" fwbase="login.jpg" fwstyle="Dreamweaver" fwdocid = "1028016350" fwnested="1" -->
							<tr>
								<td height="156" bgcolor="#FFFfff">

								<table cellspacing="0" cellpadding="2" width="70%"
									align="center" border="0" bgcolor="#FFFfff" >
									
									<tr>
										<td width="14%" class="Arial11White">&nbsp;</td>
										<td width="86%" align="left" class="Arial11Blue"><s:text
											name="execue.user.username" /></td>
									</tr>
									<tr>
										<td class="Arial11Blue">&nbsp;</td>
										<td align="left"><input name='j_username'
											value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'
											class="inputText" id="username" size="33" maxlength="100" /></td>
									</tr>
									<tr>
										<td class="Arial11Blue" maxlength="25" size="25">&nbsp;</td>
										<td align="left" class="Arial11Blue"><s:text
											name="execue.user.password" /></td>
									</tr>
									<tr>
										<td class="Arial11Blue" maxlength="25" size="25">&nbsp;</td>
										<td align="left"><input name="j_password" type="password"
											class="inputText" id="password" size="33" /></td>
									</tr>
									<tr>
										<td>&nbsp;</td>
										<td height="30" align="left"><label> <input
											type="image" name="submit" id="submit"
											src="<c:out value="${basePath}"/>/images/main/login_submit.gif"> <a
											href="<c:out value="${basePath}"/>/BiSignup.jsp"></a></label></td>
									</tr>
									<!-- <tr>
										<td>&nbsp;</td>
										<td height="30" align="left" class="Arial11Blue"><s:text name="execue.donthaveId.text" />    <a href="biSignUp.action"><s:text name="execue.SignUp.link" /></a>&nbsp;&nbsp;&nbsp;<a
											href="forgotUserPassword.action"><s:text name="execue.ForgotPassword.text" /></a></td>
									</tr>-->
								</table>

								</td>
							</tr>
							<tr>
								<td bgcolor="#FFFFFF">&nbsp;</td>
							</tr>
						</table>
                        </div>
						</div>
						</form>
						</td>
					</tr>
					<tr>
						<td height="110" valign="top">&nbsp;</td>
					</tr>
				</table>

			
		</td>
	</tr>
</table>

</div>
</td>
    <td width="6%">&nbsp;</td>
  </tr>
</table>
</td>
  </tr>
</table>



</DIV>

<DIV id=footernew>
<table align="left" border="0" cellpadding="0" cellspacing="0"
	width="100%">
	
	<tr>
		<td colspan="3"><jsp:include page="views/main/footer-search.jsp" flush="true" /></td>
		
	</tr>

</table>
</DIV>

<div id="hiddenPane"
   style="position: absolute; width: 350px; height: 200px; z-index: 2; display: none;">
      <table border="0" cellpadding="0" cellspacing="0" width="310" align="left" >
        <!-- fwtable fwsrc="boxTemplate.png" fwpage="Page 1" fwbase="boxTemplate.gif" fwstyle="Dreamweaver" fwdocid = "901881679" fwnested="0" -->
        
<tr>
          <td width="15"><img name="boxTemplate_r1_c1"
     src="images/main/boxTemplate_r1_c1.gif" width="15" height="40"
     border="0" id="boxTemplate_r1_c1" alt="" /></td>
      <td background="images/main/boxTemplate_r1_c2.gif"><img
     name="boxTemplate_r1_c2" src="images/main/boxTemplate_r1_c2.gif"
     width="279" height="40" border="0" id="boxTemplate_r1_c2" alt=""/></td>
          <td width="15"><img
     name="boxTemplate_r1_c3" src="images/main/boxTemplate_r1_c3.gif"
     width="15" height="40" border="0" id="boxTemplate_r1_c3"
     alt="Close" title="Close"/></td>
          <td width="13"><img src="images/main/spacer.gif" width="1" height="15"
     border="0" alt="" /></td>
        </tr>
        <tr>
          <td background="images/main/boxTemplate_r2_c1.gif"><img
     name="boxTemplate_r2_c1" src="images/main/boxTemplate_r2_c1.gif"
     width="15" height="10" border="0" id="boxTemplate_r2_c1" alt="" /></td>
          <td width="1045" valign="top" bgcolor="#FFFFFF" >
            <div id="hiddenPaneContent"></div>
          </td>
          <td background="images/main/boxTemplate_r2_c3.gif" ><img
     name="boxTemplate_r2_c3" src="images/main/boxTemplate_r2_c3.gif"
     width="15" height="1" border="0" id="boxTemplate_r2_c3" alt="" /></td>
          <td><img src="images/main/spacer.gif" width="1" height="10"
     border="0" alt="" /></td>
        </tr>
        <tr>
          <td><img name="boxTemplate_r3_c1"
     src="images/main/boxTemplate_r3_c1.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c1" alt="" /></td>
          <td background="images/main/boxTemplate_r3_c2.gif" valign="top"><img
     name="boxTemplate_r3_c2" src="images/main/boxTemplate_r3_c2.gif"
     width="25" height="13" border="0" id="boxTemplate_r3_c2" alt="" /></td>
          <td><img name="boxTemplate_r3_c3"
     src="images/main/boxTemplate_r3_c3.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c3" alt="" /></td>
          <td><img src="images/main/spacer.gif" width="1" height="13"
     border="0" alt="" /></td>
        </tr>
  </table>
</div>
</BODY>
</HTML>
<link rel="stylesheet" href="css/common/roundedSearch.css" type="text/css" />

<script>
$(document).ready(function(){
						  if($(".technology-1").length==2){
							 //window.location="<c:out value="${basePath}"/>/login.jsp"
							// $("#execueBody").html($("#dynamicPane").html());
							window.location.reload();
							  }
		$("form#publisher #textfield").focus();
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
		$("#hiddenPane").css("left",Pane_Left-240); 
		$("#hiddenPane").css("top",Pane_Top+23); 
		$("#hiddenPane").fadeIn("slow");
		$("#hiddenPaneContent").css("width",290+"px");
		$("#hiddenPaneContent").css("height",130+"px");
		$("#showLoginLink").show();
		$("#hiddenPane").css("height","130px");
		$("#userName").select().focus();
	});
});
							 
$("#hiddenPane a#closeButtonId").click(function(){

		$("#hiddenPane").fadeOut("slow");
		$('#shimmer').remove();
        
	});				   
						   });

$("#button2").click(function(){
			$("form#publisher input[type='text']").val("");			
			$("form#publisher textarea").val("");
			$("form#publisher #textfield").focus();
							 });

showLoginInfo('<security:authentication property="principal.fullName"/>');
  showPublisherInfo('<security:authentication property="principal.admin"/>','<security:authentication property="principal.publisher"/>');
  function showLoginInfo(name) {
		name = name.replace(/^\s+|\s+$/g,"");
		if(name && name.length > 0) {
			$("#showWelcome").empty().append('<s:text name="execue.login.welcome.message"/> ' + name);
			$("#loginId").hide();
			$("#logoutId").show();
			$("#showBookmarksSearchLink").show();
			$("#moredata").hide();
		}	
	}

		function showPublisherInfo(admin,publisher) {
			if(admin=="true" ||publisher=="true"){
			$("#publisherTdSeperator").show();
			$("#publisherTd").show();
			$("#seperatorId").show();	
			$("#adminId").attr("href","<c:out value='${adminPath}' />swi/showSearchAppsDashboard.action");
			}
		}
</script>
