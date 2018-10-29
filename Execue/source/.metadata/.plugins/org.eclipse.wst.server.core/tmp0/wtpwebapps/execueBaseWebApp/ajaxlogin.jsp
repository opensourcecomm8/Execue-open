<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
    <c:set var="basePath" value="<%=request.getContextPath()%>" />
    
<%@page import="com.execue.web.core.util.ExecueWebConstants"%>
<c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />
<script type="text/javascript">
  function processLogin () { 
	    $data = $('#s_form').serialize()+"&ajax=true";
	    <%if (request.getParameter("noRedirect") != null) {%>
	    	$data = $data +"&noRedirect=true"; 
	    <%}%> 
	  	$.post("<c:out value="${basePath}"/>/<c:url value='j_spring_security_check'/>",$data ,function (data) {
	  			if (data.errMsg) {
				
	  				$('#errorMessage').html(data.errMsg);
	  				$('#errorDiv').show();
					$("#hiddenPane").css("height","190px");
					$("#hiddenPaneContent").css("height","auto");
					$("form#s_form input#userName").select().focus();   
					return false;
	  			}else {
	  				populateLoginInfo(data);
	  				showApplications();
	  				/*if (data.url) {
		  				$('#hiddenPane').hide();
		  			}*/
		  			$('#shimmer').remove();		
					if(data.url!=="" ){
							$.get(data.url ,function (data1) {
									//$('#loginDiv').empty().append(data);
									$("#hiddenPane").hide();
									
									$("#hiddenPaneContent").empty().append(data1); 
									//$("#hiddenPane").fadeIn("slow"); 
									$("#hiddenPane").show(); 
									$("ul#browser li ul").hide();
									$("form#form1 input#bookmarkName").focus();
										if(data1.indexOf("###searchBookMarksBox####")>-1){
										$("#hiddenPaneContent").css("width",320+"px");
										}
								  
								  $.each($("li span.file"),function(){	
									  var type=$("#type").val();			                			                	                
									  if(type && type=="QI"){
											 if($(this).find('.dType').text()!='Q'){
												$(this).find('.dVal').attr("title","Not applicable for this page");
												$(this).find('.dVal').css("color","#DFDFDF");
												$(this).find('.dVal').unbind("click");
											  }
										  }else{
											 if($(this).find('.dType').text()!='S'){
												$(this).find('.dVal').attr("title","Not applicable for this page");
												$(this).find('.dVal').css("color","#DFDFDF");
												$(this).find('.dVal').unbind("click");
												 }	
										 }
								});
								  
							});
					}
		  		}
	  	},"json");
	  	return false;
  }
  function keyPressFun(e){	
		 if(e.keyCode==13 || e.which==13){		 	
		 	$("#login_submit").focus();	 	
		 	processLogin ();		 	
		 }
  }
  function populateLoginInfo(data) {
  	showLoginInfo(data.name);
	showPublisherInfo(data.admin);	
	//showPublisherUser(data.isPublisher);
	$('#hiddenPane').hide();
  }
  function showLoginInfo(name) {
		if(name) {
			$("#showWelcome").empty().append('<s:text name="execue.login.welcome.message"/> ' + name);
			$("#loginId").hide();
			$("#logoutId").show();
			$("#changePassword").show();
			$("#showBookmarksSearchLink").show();			
			$("#moredata").hide();	
			$("#reportCommentsUI").show();
			$("#signInForComments").hide();
			$("#userNameForComments").val(name);
			 $("#publishAppId").hide();
			  $("#publisherConsoleId").show();
		}	
	}
	
	function showPublisherInfo(publisher) {
			if(publisher){
			$("#publisherTdSeperator").show();
			$("#publisherTd").show();
			$("#seperatorId").show();				
			$("#adminId").attr("href","<c:out value='${adminPath}' />swi/showSearchAppsDashboard.action");
			}
		}
	function showPublisherUser(isPublisher) {	
	     if("YES"==isPublisher){
	          $("#publishAppId").hide();
			  $("#publisherConsoleId").show();
	       }else{
	          $("#publishAppId").show();
			  $("#publisherConsoleId").hide();
	       }
	}	

$(document).ready(function(){
						$("form#s_form input#userName").select().focus();   
						   });
</script>
<div id="loginDiv" style="width:300px;"><span style="padding-left:7px;"><strong><s:text name="execue.login.heading"></s:text></strong></span>
<div id='errorDiv' style="display: none;width:300px;color:#F00;font-size:8pt;font-weight:normal;" >
<s:text name="execue.login.failure"/><br>
<s:text name="execue.login.failure.reason"/><span id="errorMessage"></span>. </div>
<table width="10%" border="0" cellspacing="0" cellpadding="3">
  <tr>
    <td><form id="s_form" action="<c:out value="${basePath}"/>/<c:url value='/j_spring_security_check'/>"
	method="POST" onkeypress="keyPressFun(event);">
<table width="350">
	<tr>
		<td width="20%"><s:text name="execue.user.username"/></td>
		<td width="80%" align="left"><input   type='text' name='j_username' id="userName"
			value='<c:if test="${not empty param.login_error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>'  style="width:140px;"
            /></td>
	</tr>
	<tr>
		<td><s:text name="execue.user.password"/></td>
		<td align="left"><input  type='password' id="password" name='j_password' style="width:140px;"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td align="left"><table width="10%" border="0" cellspacing="0" cellpadding="0" align="left">
  <tr>
    <td><img name="button" id="login_submit" tabindex="12" style="cursor:pointer;"
			 value="Submit" onclick="processLogin()" src="<c:out value="${basePath}"/>/images/main/submitButton.jpg" /></td>
    <td style="padding-left:3px;"><img src="<c:out value="${basePath}"/>/images/main/cancelButton.jpg" style="cursor:pointer;"
				name="closeButtonId"  value="Cancel" id="closeButtonId" onclick="hide();" /></td>
  </tr>
</table>
</td>
	</tr>

      


</table>

</form></td>
  </tr>
</table>

</div>
<script>
$("form#s_form input#userName").select().focus();   
function hide(){

	$('#hiddenPane').fadeOut('slow');
	$('#shimmer').remove();
}

/*$("#userName").blur(function(){
						$("#userName").focus();	 
							 });

$("#password").blur(function(){
						$("#login_submit").focus();	 
							 });*/
</script>