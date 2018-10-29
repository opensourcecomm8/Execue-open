<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

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
		<div id="greyBorder" style="height:380px">
		<table width="96%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText"><s:text
					name="execue.showRequestStatus.description" /></td>
			</tr>
			<tr>
				<td valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
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
								<div class="TabbedPanelsContentGroup">
								<div class="TabbedPanelsContent" style="height: 270px;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
										<div id="tableGridTitle" style="width: 918px;">
										<table width="100%" border="0" cellpadding="2" cellspacing="0">
											<tr>
											<td height="28" width="22%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.showRequestStatus.jobType" /></strong></td>
												<td height="28" width="20%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.showRequestStatus.appName" /></strong></td>
												<td width="16%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.showRequestStatus.datasetName" /></strong></td>
												<td width="12%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.showRequestStatus.fileName" /></strong></td>
												<td width="16%" align="left" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.status" /></strong></td>
												<td width="20%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.remarks" /></strong></td>
											</tr>

										</table>
										</div>
										<div style="height: 219px; overflow: auto; overflow-x: hidden">

										<table width="100%" border="0" cellpadding="2" cellspacing="1"
											id="tableGridMemberInfo">
											<s:iterator value="jobsRequestStatus" status = "inst" id="requestStatus">
												<tr>
												<td height="28" width="16%"><s:property
														value="jobType" /></td>
													<td height="16" width="20%"><s:property
														value="appName" /></td>
													<td width="16%"><s:property value="dataSetName" /></td>
													<td width="16"><s:property value="fileName" /></td>
													<s:if	test='#requestStatus.jobType.toString() == "PUBLISHER_DATA_ABSORPTION"'>  
													<td width="16%" align="center"><A
														href="../publisher/showUploadStatus.action?jobRequest.id=<s:property value='id'/>"><s:property
														value='jobStatus' /></A></td>
													</s:if>	
													<s:elseif	test='#requestStatus.jobType.toString() == "PUBLISHER_DATA_EVALUATION"'>  
													<td width="16%" align="center"><A
														href="../publisher/showUploadStatus.action?jobRequest.id=<s:property value='id'/>"><s:property
														value='jobStatus' /></A></td>
													</s:elseif>	
													<s:else>
													<td width="16%" align="center"><s:property
														value='jobStatus' /></td>
													</s:else>
													<td width="16%" bgcolor="#EEEEEE"><s:property
														value="remark" /></td>
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
													class="fieldNames"><input type="button" class="buttonSize108"
													value="<s:text name='execue.console.consoleHome.button' />" name="imageField"
													onclick="javascript:showConsoleHome();" /> </span></td>
											</tr>
										</table>

										</td>
									</tr>

								</table>
								</div>
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
