<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="TabbedPanelsContent" style="height: 220px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<div id="tableGridTitle" style="width: 918px;">
		<table width="100%" border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td height="28" width="20%" bgcolor="#EEEEEE"><strong>Job
				</strong></td>
				<td width="20%" align="left" bgcolor="#EEEEEE"><strong><s:text
					name="execue.global.status" /></strong></td>
				<td width="15%" bgcolor="#EEEEEE"><strong>Requested
				Date</strong></td>
				<td width="15%" bgcolor="#EEEEEE"><strong>Requested By</strong></td>

			</tr>
		</table>
		</div>
		<div style="height: 249px; overflow: auto; overflow-x: hidden">

		<table width="100%" border="0" cellpadding="2" cellspacing="1"
			id="tableGridMemberInfo">
			<s:iterator value="jobs">
				<tr>
					<td height="28" width="20%"><s:property value="id" /></td>					
					<td width="20%" colspan="3" align="center"><a class="links"
						href="../swi/getJobStatus.action?jobRequest.id=<s:property value='id' />&jobRequest.jobType=<s:property value='jobType' />"><s:property value='jobStatus' /></a></td>
					<td width="15%" bgcolor="#ffffff"><s:property
						value="requestedDate" /></td>
					<td width="15%" bgcolor="#ffffff"><s:property value="userName" /></td>

				</tr>
			</s:iterator>
		</table>


		</div>
		</td>
	</tr>
	<tr>
		<td>
		<table align="center">
			<tr>
				<td colspan="1" height="40" valign="bottom"><span
					class="fieldNames"> <input type="button" class="buttonSize108"
													value="<s:text name='execue.console.consoleHome.button' />" name="imageField"
					onclick="javascript:showConsoleHome();" /> </span></td>
			</tr>
		</table>

		</td>
	</tr>

</table>
</div>