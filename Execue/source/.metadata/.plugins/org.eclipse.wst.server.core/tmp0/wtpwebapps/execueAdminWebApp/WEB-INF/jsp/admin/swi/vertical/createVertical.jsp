<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#actionMessage ul {
	margin: 0px;
	padding: 0px;
	margin-bottom: 5px;
}
</style>

<div id="errorMessage"
	style="color: red; padding-top: 10px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage"
	style="color: green; padding-top: 10px; padding-left: 20px;"><s:actionmessage /></div>
<div id="statusMessage" style="display: none; padding-left: 20px;"></div>
<div id="validationMessage" style="display: none; padding-left: 20px;"></div>
<form action="#" method="post" id="verticalForm"><s:if
	test="vertical.id!=''">
	<div style="float: right; width: 200px;"><a id="association"
		type="button" href="#"
		onClick='javascript:getVerticalAssociation(<s:property value="selectedVerticalId"/>);'><s:text
		name="execue.vertical.createAssociation" /></a></div>
</s:if>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0" style="margin-left: 20px;">
	<tr>
		<td>
		<table width="50%" border="0" cellspacing="3" cellpadding="0">
			<tr>
				<td height="35" colspan="2"><strong> <s:if
					test="vertical.id!=''">
					<s:text name="execue.vertical.updateVertical" />
				</s:if> <s:else>
					<s:text name="execue.vertical.createVertical" />
				</s:else></strong></td>
			</tr>
			<tr>
				<td>Name</td>
				<td><s:textfield name="vertical.name" /></td>
			</tr>
			<tr>
				<td>Description</td>
				<td><s:textfield name="vertical.description" /></td>
			</tr>
			<tr>
				<td>Importance</td>
				<td><s:select list="hundredList" name="vertical.importance" /><span style="padding-left:4px;"><div id="messageDiv" style="display: none; z-index: inherit; position: absolute; width: 300px; height: 30px; overflow-y: auto; min-height: 30px; border: 2px solid rgb(204, 204, 204); background-color: rgb(207, 221, 221); color: rgb(0, 0, 0); white-space: normal; padding: 5px; " ><div style="margin-right: 20px;"><s:text name="execue.vertical.importance" /></div></div> <a id="qMark" href="#">?</a>
                
                
                
                </span></td>
			</tr>
			<tr>
				<td>URL</td>
				<td><s:textfield name="vertical.url" /></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="35" colspan="2" align="left">
		<div style="padding-left: 2px;"><s:if test="vertical.id!=''">
			<input id="submitButton" type="button" class="singleButton"
				onClick="javascript:addVertical();" value="Update" />
			<input id="submitButtonLoader" type="button" style="display: none"
				onClick="javascript:addVertical();" class="singleButtonLoader"
				disabled="disabled" value="Submit" />
			<a href="#" style="margin-left: 20px;" id="resetButton"
				onClick="javascript:resetUpdateForm();"> Reset</a>
			<img id="resetButtonLoader" style="display: none; margin-left: 20px;"
				src="../images/loadingBlue.gif" />&nbsp;&nbsp;
	        <a id="delete" href="#"
				onClick='javascript:return deleteVertical(<s:property value='vertical.id' />);'>Delete</a>&nbsp;&nbsp;
        </s:if> <s:else>
			<input id="submitButton" type="button" class="singleButton"
				onclick="javascript:return addVertical();" value="Create" />
			<input id="submitButtonLoader" type="button" style="display: none"
				class="singleButtonLoader" disabled="disabled" value="Submit" />
			<a href="#" style="margin-left: 20px;" id="resetButton"
				onClick="javascript:resetForm();"> Reset</a>
			<img id="resetButtonLoader" style="display: none; margin-left: 20px;"
				src="../images/loadingBlue.gif" />&nbsp;&nbsp;
        </s:else></div>
		</td>
	</tr>
</table>
<input name="vertical.id" id="verticalId" type="hidden"
	value="<s:property value='vertical.id' />" /> <input
	name="vertical.popularity" type="hidden"
	value="<s:property value='vertical.popularity' />" /> <input
	name="selectedVerticalId" type="hidden"
	value='<s:property value="selectedVerticalId"/>' /></form>
<div
	style="white-space: nowrap; position: absolute; z-index: 100; padding: 4px; border: 1px solid rgb(204, 204, 204); background-color: rgb(255, 255, 255); color: rgb(0, 102, 0); top: 257.75px; left: 756.733px; display: none;"
	id="verticalNameMessage">Vertical Name Required</div>

<script>
var timer="";
$(function(){
		   var left=$("#qMark").position().left;
		   var top=$("#qMark").position().top;
		   $("#messageDiv").css("left",left+20+"px");
		   $("#messageDiv").css("top",top-20+"px");
	$("#vertical_name").bind("blur",function(e){
		doValidation($(this));
     });
	
	$("#qMark").bind("mouseover",function(){
			clearTimeout(timer);
			$("#messageDiv").show();							  
	 });
	
	$("#qMark").bind("mouseout",function(){
			timer=setTimeout('$("#messageDiv").hide();',1000);							  
	 });
	
});

function resetForm(){
	createNewVertical();
}

function resetUpdateForm(){
	getVerticalDetails($("#verticalId").val());	
}
</script>