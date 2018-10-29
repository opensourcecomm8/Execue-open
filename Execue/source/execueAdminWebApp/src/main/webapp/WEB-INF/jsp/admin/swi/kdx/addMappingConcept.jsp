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
<div class="innerPane2" style="width: 230px; height: auto">
<div id="errorMessage"
	style="color: red; padding-top: 10px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage"
	style="color: green; padding-top: 10px; padding-left: 20px;"><s:actionmessage /></div>
<div id="statusMessage" style="display: none; padding-left: 20px;"></div>
<div id="validationMessage" style="display: none; padding-left: 20px;"></div>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0" style="margin-left: 20px;">
	<tr>
		<s:if test="concept.id != null">
			<td width="60%" height="25" class="fieldNames"><strong><s:text
				name="execue.concept.updateViewConcept" /></strong></td>
		</s:if>
		<s:else>
			<td width="60%" height="25" class="fieldNames"><strong><s:text
				name="execue.concept.addNewConcept" /></strong></td>
		</s:else>
		<td width="40%" align="center" class="fieldNames"><s:if
			test="concept.id != null">
			<div id="showInstancesLink"><a
				href="javascript:getInstancesByPage('<s:property value="concept.id"/>');"
				id="showInstance"><s:text name='execue.instance.instance' /></a></div>
			<div id="loadingShowInstanceLink" style="display: none;"><img
				src="../images/admin/loading.gif" width="20" height="20"></div>
		</s:if></td>
	</tr>

	<tr>
		<td colspan="2">
		<table width="100%" border="0" align="center" cellpadding="2"
			cellspacing="0">
			<tr>
				<td colspan="2">
				<div id="message" STYLE="color: red" align="left"></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
				<form id="conceptForm">
				<table width="100%" cellspacing="0" cellspacing="0" border="0">
					<tr>
						<td width="15%" class="fieldNames"><s:text name="execue.global.name" /><s:text
							name="execue.global.mandatory"></s:text></td>
						<td width="85%"><s:textfield name="concept.displayName"
							cssClass="textBox" id="concept" maxlength="255"/>
							<a href="#" id="helpLink" title="Help" alt="Help">?</a>
						<div id="conceptNameMessage"
							style="display: none; white-space: nowrap; position: absolute; z-index: 100; padding: 4px; border: 1px solid #ccc; background-color: #FFF; color: red;"></div>
						
						</td>
					</tr>
					<tr>
						<td class="fieldNames"><s:text name="execue.global.description" /></td>
						<td><s:textfield name="concept.description"
							cssClass="textBox" id="description" maxlength="255"/></td>
						<td><s:hidden name="concept.id" id="conceptUpdateId"/></td>
					</tr>

				</table>
				</form>
				</td>
			</tr>
			<tr>
				<td align="left" colspan="2">
				<div style="padding-left: 2px;"><input id="submitButton"
					type="button" class="singleButton" value="Submit"
					onClick="javascript:createConcept()" /> <input
					id="submitButtonLoader" type="button" style="display: none"
					class="singleButtonLoader" disabled="disabled" value="Submit" /> <a
					href="#" style="margin-left: 20px;" id="resetButton"
					onClick="javascript:resetConcept()"> Reset </a> <img
					id="resetButtonLoader" style="display: none; margin-left: 20px;"
					src="../images/admin/loadingBlue.gif" /></div>
				</td>
			</tr>
		</table>
	</tr>

</table>
</div>

<script>
  $(document).ready(function() {
    $("#helpLink").helpMessage({messageKey:"execue.concept.help.name"}); 
	var cName=$("#concept").val();
		$("#conceptNameHeadingDiv").html(cName);
	    $("#concept").bind("blur",function(e){
		doValidation($(this));
     });
   setFocus($("#concept"));
  });

  function doValidation(obj){
	var top=obj.position().top;
	var left=obj.position().left;
	var stringEmpty=checkEmptyString(obj);
				$("#conceptNameMessage").css("top",top-30+"px");
				$("#conceptNameMessage").css("left",left+obj.width()-100+"px");
	if(stringEmpty){
	     $("#conceptNameMessage").text("Concept Name "+stringEmptyMessage).show(); 
				setFocus(obj);
				return false; 
	  }
	var splCharExist=checkSpecialChar(obj);
			if(splCharExist){				
				$("#conceptNameMessage").text(specialCharMessage).show();
				setFocus(obj);
				return false;

			}else{
				$("#conceptNameMessage").hide();
			} 
	return true;
  }
  



</script>

