<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="user==null || user.id == null">
	<s:set name="mode" value="%{'add'}" />
</s:if>
<s:else>
	<s:set name="mode" value="%{'update'}" />
</s:else>

<div class="innerPane" style="width: 100%; height: auto;">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="89%" height="30" class="tableSubHeading" style="padding-left:20px;" align="left"><strong>
		<s:if test="#mode == 'add'">
			<s:text name="execue.user.pageHeading.add" />
		</s:if> <s:else>
			<s:text name="execue.user.pageHeading.update" />
		</s:else> </strong></td>
		<td width="11%" align="center" class="fieldNames">&nbsp;
		
		</td>
	</tr>
	<tr>
		<td colspan="3">
		<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
		<div id="actionMessage" style="color: green"><s:actionmessage /></div>
		</td>
	</tr>
	<tr>
		<td colspan="2"><s:form id="createUser" action="user!User">
			<table width="50%" border="0" align="left" cellpadding="2"
				cellspacing="0" style="margin-left:20px;">
				<tr>
					<td width="12%" class="fieldNames"><s:text
						name="execue.user.username" /><span class='fontRed'>*</span></td>
					<s:if test="#mode == 'add'">
						<td width="51%"><s:textfield name="user.username"
							cssClass="textBox" id="userName" /></td>
					</s:if>
					<s:else>
						<td width="37%"><s:textfield name="user.username"
							cssClass="textBoxDisabled" id="username" readonly="true" /></td>
					</s:else>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.user.password" /><span
						class='fontRed'>*</span></td>
					<s:if test="#mode == 'add'">
						<td><s:password name="user.password" cssClass="textBox"
							id="user.password" showPassword="true" /></td>
					</s:if>
					<s:else>
						<td><s:password name="user.password" value="*****"
							cssClass="textBox" id="user.password" showPassword="true" /></td>
					</s:else>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.user.confirmPassword" /><span
						class='fontRed'>*</span></td>
					<s:if test="#mode == 'add'">
						<td><s:password name="confirmPassword" cssClass="textBox"
							id="confirmPassword" showPassword="true">
						</s:password></td>
					</s:if>
					<s:else>
						<td><s:password name="confirmPassword" cssClass="textBox"
							value="*****" id="confirmPassword" showPassword="true">
						</s:password></td>
					</s:else>
				</tr>
				<!-- 
         <tr>
          <td class="fieldNames"><s:text name="user.changePassword"/></td>
          <td><s:textfield name="user.password" cssClass="textBox" id="user.changePassword"/></td>
        </tr>
         -->
				<tr>
					<td class="fieldNames"><s:text name="execue.user.status" /></td>
					<td><s:select name="user.status" id="statusType"
						list="userStatusTypes" /></td>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.user.firstName" /><span
						class='fontRed'>*</span></td>
					<td><s:textfield name="user.firstName" cssClass="textBox"
						id="user.firstName" /></td>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.user.lastName" /><span
						class='fontRed'>*</span></td>
					<td><s:textfield name="user.lastName" cssClass="textBox"
						id="user.lastName" /></td>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.user.emailId" /></td>
					<td><s:textfield name="user.emailId" cssClass="textBox"
						id="user.emailId" /></td>
				</tr>
				<tr>
					
					<td height="40" colspan="2" align="left"><s:if test="#mode == 'add'">
						<input type="submit" class="buttonSize108" name="imageField" id="imageField"
							value="<s:text name='execue.user.createUser.button' />" />
						<input type="button" class="buttonSize108"  name="imageField2" id="imageField2"
							value="<s:text name='execue.global.clearAllFields.button' />" />
					</s:if> <s:else>
						<input type="submit" class="buttonSize108" name="imageField" id="imageField"
							value="<s:text name='execue.user.updateUser.button' />" />
					</s:else></td>
				</tr>
			</table>
		  <s:hidden name="user.id" cssClass="textBox" id="user.id" />
		  <s:hidden name="user.address1" cssClass="textBox" id="user.address1" />
		  <s:hidden name="user.address2" cssClass="textBox" id="user.address2" />
		  <s:hidden name="user.city" cssClass="textBox" id="user.city" />
		  <s:hidden name="user.state" cssClass="textBox" id="user.state" />
		  <s:hidden name="user.zip" cssClass="textBox" id="user.zip" />
		  <s:hidden name="user.country" cssClass="textBox" id="user.country" />
		  <s:hidden name="user.encryptedKey" cssClass="textBox" id="user.encryptedKey" />
		  <s:hidden name="originalPassword" cssClass="textBox"
				id="originalPassword" />

		</s:form></td>
	</tr>
</table>
</div>

<script>
$("#closeInnerPaneLink").show();
$(document).ready(function() {
//("#addNewUserLink").hide();

$("#username").css("width","152px");
	$("#statusType").css("width","154px");
$("#closeInnerPane").click(function(){
    $("#dynamicPane").createInput('<s:url action="listUsers"/>',"closeInnerPaneLink","loadingCloseInnerPaneLink"); //passing URL, user clicked link, loader div
});
$("#imageField").click(function(){
	$("#dynamicPane").createInput('<s:url action="saveUser!save"/>',"","",$('#createUser').serialize()); //passing URL, user clicked link, loader div
     return false;
});
$("#imageField2").click(function(){	
	$("#createUser input[type='text'],#createUser input[type='password']").val("");
});
$("#userName").select().focus();
$("#username").bind("focus", noCursor);
function noCursor(){
 $(this).trigger("blur");}
});
</script>
