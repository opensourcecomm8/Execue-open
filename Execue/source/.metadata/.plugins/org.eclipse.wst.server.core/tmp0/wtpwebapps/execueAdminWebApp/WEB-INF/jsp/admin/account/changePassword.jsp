<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
    
<s:form id="changePasswordForm">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td width="62%">
			<DIV
				style="background-color: #FFFFFF; width: 98%; padding-left: 0px; height: 320px; float: left; margin-top: 5px; margin-left: 8px;"
				id="signUpDiv">
			<table width="100%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td align="left">
					<table width="60%" border="0" cellspacing="0" cellpadding="2">
						<tr>
							<td colspan="3" align="left">
							<div id="errorMessage"
								style="color: red; padding-top: 10px; font-size: 10px;"><s:actionerror /><s:fielderror /></div>
							<div id="actionMessage" style="color: green"><s:actionmessage /></div>
							</td>
						</tr>
						<tr>
							<td height="34" colspan="4" align="left"><span
								style="color: #000000; font-size: 13px; padding-left: 12px;font-weight:bold; height: 40px;"><s:text
								name="execue.changepass.breadcrumb" /></span></td>
						</tr>
						<tr id="userNameTr">
							<td align="left">&nbsp;</td>
							<td align="left" class="fieldNames">User Id &nbsp;</td>
							<td align="left"><s:textfield name="user.username"
								readonly="true" id="displayUserId" /></td>
							<td width="1%">&nbsp;</td>
						</tr>
						<tr id="oldPassTr">
							<td align="left">&nbsp;</td>
							<td align="left" class="fieldNames"><s:text
								name="execue.Oldpassword.text" />&nbsp;<span class='fontRed'>*</span></td>
							<td align="left"><s:password name="oldPassword"
								cssClass="textBox" id="oldPassword" showPassword="false" /></td>
							<td width="1%">&nbsp;</td>
						</tr>


						<tr>
							<td align="left">&nbsp;</td>
							<td align="left" class="fieldNames"><s:text
								name="execue.Newpassword.text" />&nbsp;<span class='fontRed'>*</span></td>
							<td align="left"><s:password name="user.password"
								cssClass="textBox" id="user.password" showPassword="false" /></td>
							<td width="1%">&nbsp;</td>
						</tr>
						<tr>
							<td align="left">&nbsp;</td>
							<td align="left" class="fieldNames"><s:text
								name="execue.user.confirmPassword" />&nbsp;<span class='fontRed'>*</span></td>
							<td align="left"><s:password name="confirmPassword"
								cssClass="textBox" id="confirmPassword" showPassword="false" /></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td align="left"><input class="buttonSize51" type="button"
								id="button" value="<s:text name='execue.global.submit.button'/>" onclick="changePassword();" ></td>
							<td>&nbsp;</td>
						</tr>
						<tr>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
							<td align="left">&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</div>
			</td>
		</tr>
	</table>
</s:form>
<input type="hidden"  id="currentUserId"
		value="<security:authentication property="principal.user.username"/>" />
<script>
var currentUserId=$("#currentUserId").val();
 $("#displayUserId").val(currentUserId);
</script>




