<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#dynamicPane select {
	width: 160px;
}
</style>
<!-- table width="99%" border="0" cellspacing="0" cellpadding="3"
		align="center" ><tr><td align="right"><div id="closeUserInfo" style="width:40px;height:20px;background-color:#fff;cursor:pointer;padding-right:5px;color:#0000FF;">Close</div></td></tr></table-->
<div style="width:91%;height:260px;border:solid 1px #ccc;margin:10px;padding:10px;">
	<table width="80%" border="0" cellspacing="0" cellpadding="4"
		align="center" id="accountTable" style="white-space:nowrap;margin-top:20px;">
		<!-- tr>
			<td colspan="3" align="left"> 
			<div id="errorMessage"
				   style="color: red; padding: 3px; font-size: 10px;"><s:actionerror /><s:fielderror /></div>
			 
			<div id="actionMessage" style="color: green"><s:actionmessage /></div>
			 </td>
		</tr-->
		<tr>
			<td class="label"><s:text name="execue.account.profile.username" /></td>
			<td> : </td>
			<td><s:property value="user.username"/></td>
		</tr>

		<tr>
			<td width="30%" class="label"><s:text name="execue.user.firstName" /></td>
			<td width="2%"> : </td>
			<td width="68%"><s:property value="user.firstName"/></td>
		</tr>
		<tr>
			<td class="label"><s:text name="execue.user.lastName" /></td>
			<td> : </td>
			<td><s:property value="user.lastName"/></td>
		</tr>

		<tr>
			<td valign="top" class="label"><s:text name="execue.user.label.address1" /></td>
			<td> : </td>
			<td><s:property value="user.address1"/></td>
		</tr>
		<tr>
			<td valign="top" class="label"><s:text name="execue.user.label.address2" /></td>
			<td> : </td>
			<td><s:property value="user.address2"/></td>
		</tr>
		<tr>
			<td class="label"><s:text name="execue.user.label.city" /></td>
			<td> : </td>
			<td><s:property value="user.city"/></td>
		</tr>
		<tr>
			<td class="label"><s:text name="execue.user.label.state" /></td>
			<td> : </td>
			<td><s:property value="user.state"/></td>
		</tr>
		<tr>
			<td class="label"><s:text name="execue.user.label.zip" /></td>
			<td> : </td>
			<td><s:property value="user.zip"/></td>
		</tr>
		<tr>
			<td class="label"><s:text name="execue.user.label.country" /></td>
			<td> : </td>
			<td><s:property value="user.country"/></td>
		</tr>

<tr>
			<td>&nbsp;</td>
			<td colspan="2">&nbsp;</td>
		</tr>
	</table>
	</div>
<script>
$(document).ready(function(){
$("#closeUserInfo").click(function(){
$("#userInfoDynamicDivOuter").hide();
});
});
</script>