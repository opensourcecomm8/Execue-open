<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script>
	function showJobs(){
		var jobType = $("#jobType").val();		
		$.get("showJobsByType.action",{selectedJobType:jobType}, function(data){
			//var data=eval(data);	
			$(".TabbedPanelsContentGroup").html(""+data);
		});
	}
	
</script>


<div id="outerPane">
<table width="950" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="50" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.showRequestStatus.pageHeading" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder" style="height: 405px;">
		<table width="96%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText"><s:text
					name="execue.showRequestStatus.description" /></td>
			</tr>
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">

					<tr>
						<td>&nbsp;</td>
					</tr>
					<tr>
						<td height="300" align="center" valign="top">
						<div id="dynamicPane">

						<table width="100%" border="0" cellspacing="0" cellpadding="0">

							<tr>
								<td>
								<div class="TabbedPanelsContent" style="height: 220px;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td>
		<div id="tableGridTitle" style="width: 918px;">
		<table width="100%" border="0" cellpadding="2" cellspacing="0">
			<tr>
				<td height="28" width="20%" bgcolor="#EEEEEE"><strong>ApplicationId
				</strong></td>
				<td height="28" width="20%" bgcolor="#EEEEEE"><strong>DataSourceId
				</strong></td>
				<td height="15%" width="20% bgcolor="#EEEEEE"><strong>FileName</strong></td>
				<td width="20%" align="left" bgcolor="#EEEEEE"><strong><s:text
					name="execue.global.status" /></strong></td>
				<td width="15%" bgcolor="#EEEEEE"><strong>Current Operation
				</strong></td>
				

			</tr>
		</table>
		</div>
		<div style="height: 249px; overflow: auto; overflow-x: hidden">

		<table width="100%" border="0" cellpadding="2" cellspacing="1"
			id="tableGridMemberInfo">
			<s:iterator value="uiPublishedFileInfoList">
				<tr>
					<td height="28" width="20%"><s:property value="applicationId" /></td>
					<td height="28" width="20%"><s:property value="dataSourceId" /></td>
					<td height="15" width="20%"><s:property value="fileName" /></td>
					<td width="20%" colspan="3" align="center"><a class="links"
						href="../swi/getPublishJobStatus.action?jobRequest.id=<s:property value='jobRequestId' />&jobRequest.jobType=<s:property value='jobType' />"><s:property
						value='currentOperationStatus' /></a></td>
					<td width="15%" bgcolor="#EEEEEE"><s:property
						value="currentOperation" /></td>
	
				</tr>
				</s:iterator>
		</table>


		</div>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td>
		<table align="center">
			<tr>
				<td colspan="1" height="40" valign="bottom"><span
					class="fieldNames"> <img
					src="../images/admin/consoleHomeButton.jpg" name="imageField"
					onclick="javascript:showConsoleHome();" /> </span></td>
			</tr>
		</table>

		</td>
	</tr>

</table>
</div>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
					<tr>
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
</div>
