<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">

	<tr>
		<td height="40"><strong><s:text
			name="execue.advOptions.userRequest" /></strong></td>
	</tr>
	<tr>
		<td colspan="3">
		<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
		<div id="actionMessage" style="color: green"><s:actionmessage /></div>
		</td>
	</tr>
	<tr>
		<s:if test="userRequests.size>0">
			<td>
			<div
				style="margin-left: 2px; margin-top: 1px; overflow: auto; width: 800px; height: 300px;">
			<s:form id="userRequestForm">
				<table width="100%" border="0" cellspacing="1" cellpadding="4"
					id="tableGridMemberInfo">
					<tr id="tableGridTitle">
						<td width="11%"><s:text name="execue.advOptions.userName" /></td>
						<td width="16%"><s:text name="execue.advOptions.emailId" /></td>
						<td width="13%"><s:text name="execue.advOptions.userComments" /></td>
						<td width="16%"><s:text name="execue.advOptions.comment" /></td>
						<s:if test="!acceptRejectType">
							<td width="4%">&nbsp;</td>
						</s:if>
					</tr>
					<s:iterator value="userRequests" id="userRequestsId">
						<tr>
							<td><s:property value="userFullName" /></td>
							<td><s:property value="emailId" /></td>
							<td><textarea cols="22" disabled="disabled"><s:property
								value="notes" /></textarea></td>
							<td><label> <s:if test="!acceptRejectType">
								<textarea textAreaId="<s:property value='id' />" cols="22"></textarea>
							</s:if> <s:else>
								<textarea value="" cols="22" disabled="disabled"><s:property
									value="adminComment" /></textarea>
							</s:else> </label></td>
							<s:if test="!acceptRejectType">
								<td><label> <input type="checkbox"
									name="userRequest.acceptRejectRequest"
									id="<s:property value="id" />" value="YES" /> </label></td>
							</s:if>
						</tr>
					</s:iterator>
				</table>
			</s:form></div>
			<s:if test="!acceptRejectType">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td width="7%"><input type="button" id="acceptButtonId"
							value="Accept" /></td>
						<td width="43%" align="left"><input type="button"
							id="rejectButtonId" value="Reject" /></td>
						<td width="33%" align="center">&nbsp;</td>
						<td width="17%" align="right">&nbsp;</td>
					</tr>
				</table>
			</s:if></td>
		</s:if>
		<s:else>
			<table width="100%" border="0" cellspacing="0" cellpadding="0">
				<tr>
					<td width="7%"><s:text name="execue.advOptions.nodata" /></td>
				</tr>
			</table>
		</s:else>
	</tr>
	<tr>
		<td height="40">&nbsp;</td>
	</tr>
</table>
<script>
 $("#acceptButtonId").click(function(){
   updateUserRequests("YES");
});

$("#rejectButtonId").click(function(){
   updateUserRequests("NO");
});
</script>
