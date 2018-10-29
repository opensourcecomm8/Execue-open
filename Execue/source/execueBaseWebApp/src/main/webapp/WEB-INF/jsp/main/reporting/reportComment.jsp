<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<%@page import="com.execue.core.common.type.CheckType"%>
<!--              user  comments starts            -->
<style>
#reportComment input[type='text'],textarea{
border:1px solid #BBBBBB;
}
.comments{
font-size: 12px; font-weight: bold; padding-left: 5px;
border-bottom:1px solid #BBBBBB;
background-color:#E0E0E0;
height:25px;
text-align:left;
}
#disclaimer{
margin-top:5px;	
padding-left:0px;
}
#signInForComments td{
	line-height:22px;
	text-align:left;
	
}
#reportComment td{
	text-align:left;
}
</style>
<div style="width: 100%; margin: auto; background-color: #FFF;margin-top:3px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0" style="min-width:150px;"
	align="center">
	<tr>
		<td height="25"><s:form name="reportComment" id="reportComment">
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
            
            <tr>
				  <td width="4%">&nbsp;</td>
					<td 
						class="comments">Leave a Comment
					<span
						style="font-family: 'Trebuchet MS', Arial, Helvetica, sans-serif; font-size: 10px;">>></span>
			  </tr>
              
              <tr><td >&nbsp;</td><td >
              
              
              <div id="reportCommentsUI" style="display:none; line-height:20px;" >
              <table  border="0" cellspacing="0" cellpadding="0">
				
				
				
				<tr>
					
					<td ><textarea name="reportComment.comment" id="commentsText"
						style="width: 99%; height: 105px;overflow:auto; " cols="94"
						rows="4"></textarea>
                        <input type="hidden" name="reportComment.userName" readonly="readonly" id="userNameForComments" style="width: 130px;"  value="<security:authentication property="principal.fullName"/>"  >
                        
                        </td>
				</tr>
				<tr>
					
					<td >
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							 <!-- 
							<td width="4%">Public</td>
							<td width="34%" height="30" valign="middle"><label>
								 </td> -->
							<td width="100%" align="left" >
							<input type="hidden" name="reportComment.isPublic"
								id="checkbox" value="<%=CheckType.YES %>"> 
							
							<input type="button"	class="buttonSize130"
								id="submitReportComment" value="Submit Comment"
								width="65" height="26" onClick="saveReportComment();"
								style="cursor: pointer"></td>
						</tr>
					</table>
					</td>
				</tr>
                
                </table>
                </div>
                <div id="signInForComments" style="display:none;" >
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr><td><span id="showLoginLinkComments"><a id="loginIdComments" class="links_sem3" href="javascript:;">Sign In</a></span> <span id="loadingShowLoginCommentsLink"
							style="display: none;padding-left:10px;padding-right:10px;"><img src="images/main/loaderTrans.gif"></span> to leave a comment</td></tr>
                  <tr>
                    <td height="20" align="left" class="Arial11Blue">Don't have a Login Id?  <a href="biSignUp.action">Sign Up</a></td>
                  </tr>
                 
                </table>

                </div>
                </td></tr>
               
				<!--         posting         -->
				<c:if test="${not empty reportComments}">
				<tr>
					<td>&nbsp;</td>
					<td 
						class="comments">Related Comments
					<span
						style="font-family: 'Lucida Sans Unicode', 'Lucida Grande', sans-serif; font-size: 15px;">»</span></td>
				</tr>
				</c:if>
				<s:iterator value="reportComments" id="reportComment">

					<tr>
						<td>&nbsp;</td>
						<td >

						<div class="post" style="padding: 3px;padding-left:0px;">Posted by: <b><s:property
							value="%{#reportComment.userName}" /></b></div>
						<div style="padding: 3px;padding-left:0px;">Date: <s:property
							value="%{#reportComment.createdDate}" /></div>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td style="padding: 3px;" ><s:property
							value="%{#reportComment.comment}" /></td>
					</tr>

				</s:iterator>
				<!--         posting ends        -->
				
				
			</div>

			</table>
			<input type="hidden" name="reportComment.queryId"
				value="<%=request.getAttribute("reportComment.queryId")%>" />
			<input type="hidden" name="reportComment.queryHash"
				value="<%=request.getParameter("reportComment.queryHash")%>" />
			<input type="hidden" name="reportComment.assetId"
				value="<%=request.getAttribute("reportComment.assetId")%>" />
		</s:form></td>
	</tr>
	
</table>

</div>



<input type="hidden" id="loginUserId"
	value="<security:authentication property="principal.fullName"/>" />
<!--              user  comments ends            -->
<script>
var loginUserId='<security:authentication property="principal.username"/>';
//alert("loginUserId::"+loginUserId);
if(loginUserId=="guest"){
	$("#reportCommentsUI").hide();
	$("#signInForComments").show();
	
}else{
	$("#reportCommentsUI").show();
	$("#signInForComments").hide();
}
 var commentDefaultText="Please enter comment";
$(function(){
		  
		   $("#commentsText").val(commentDefaultText);
				 $("a#loginIdComments").click(function(){
				Pane_Left=$(this).position().left;
				Pane_Top=$(this).position().top;
				if($("#PivotTable").html()!=null){
					sim(Pane_Left,Pane_Top);	
					}
				$("#showLoginLinkComments").hide(); 	
				$("#loadingShowLoginCommentsLink").show(); 							   
					$.get("ajaxlogin.jsp", {ajax: 'true', noRedirect : 'true'}, function(data) { 
							$("#loadingShowLoginCommentsLink").hide(); 
					
							$("#hiddenPaneContent").empty(); 
							$("#hiddenPaneContent").append(data);
							$("#hiddenPane").css("left",Pane_Left-240); 
							$("#hiddenPane").css("top",Pane_Top-23); 
							$("#hiddenPane").fadeIn("slow");
							$("#hiddenPaneContent").css("width",290+"px");
							$("#hiddenPaneContent").css("height",160+"px");
							$("#showLoginLinkComments").show();
							$("#hiddenPane").css("height","160px");
							$("#userName").select().focus();
						});
				});
				 
			$("#commentsText").bind("click",function(){
			if($.trim($("#commentsText").val())==commentDefaultText) {	
			$("#commentsText").val("");
			}
		 });
			
			$("#commentsText").bind("focus",function(){
			if($.trim($("#commentsText").val())==commentDefaultText) {	
			$("#commentsText").val("");
			}
		 });
			
			$("#commentsText").bind("blur",function(){
			if($.trim($("#commentsText").val())=="") {	
			$("#commentsText").val(commentDefaultText).select();
			}
		 });
});
function saveReportComment(){
	if(($.trim($("#commentsText").val())=="")||($.trim($("#commentsText").val())==commentDefaultText) ){$("#commentsText").val(commentDefaultText);$("#commentsText").select(); return;}
   var reportUserComment=$('#reportComment').serialize(); 
   $("#reportComments").empty();	   
   $.get("reporting/saveReportComment.action",reportUserComment,function(data){          
	    getReportComments();	    
   });
}
</script>