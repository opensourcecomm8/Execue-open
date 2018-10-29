
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="execue.jobstatus.main.heading"/> </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="96%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText">
				<table>
					<tr>
						<td><s:text name="execue.jobstatus.main.description"/></td>
						<td>
					</tr>
					<tr>
						<td valign="top">

						<table width="400" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>


								<div id="container"
									style="width: 950px; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
								<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->

								<!-- 
            <div class="ui-layout-west" style="overflow-x: hidden; width: 10px">
            <div id="dynamicPaneWest">
            
            </div>
            
            </div>
             -->
								<div class="ui-layout-center"
									style="width: 800px; overflow: hidden; margin: auto; padding-top: 20px">
								<div id="dynamicPane">

								<div id="errorMessage"><s:fielderror /> <s:actionmessage />
								<s:actionerror /></div>

								<div class="innerPane" style="width: 99%"><s:form
									namespace="/swi" action="showJobStatus" method="post">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="51%">
											<table width="90%" border="0" align="center" cellpadding="3"
												cellspacing="0">
												<tr>
													<td class="fieldNames"><strong><s:text
														name="execue.upload.main.request.status" /></strong></td>
													<td class="fieldNames"><strong><s:property
														value="jobRequest.jobStatus" /></strong></td>
												</tr>
											</table>
											</td>
										</tr>
										<tr>
											<td height="10">&nbsp;</td>
										</tr>
										<tr>
											<td width="49%" valign="top">
											<div class="statusPane"
												style="overflow: auto; width: 90% px; height: 200px; border: 1px solid #333333">
											<table width="90%" border="0" align="center" cellpadding="3"
												cellspacing="0">
												<tr>
													<td class="fieldNames"><strong><s:text
														name="execue.upload.main.request.stage" /></strong></td>
													<td class="fieldNames"><strong><s:text
														name="execue.upload.main.request.stage.status" /></strong></td>
													<td class="fieldNames"><strong><s:text
														name="execue.upload.main.request.stage.statusDetail" /></strong></td>
												</tr>
												<s:iterator value="jobStatusSteps" status="even_odd"
													id="jobStatusStep">
													<tr>
														<td class="fieldNames"><s:property
															value="operationalStage" /></td>
														<td class="fieldNames"><s:property value="jobStatus" /></td>
														<td width="25%" class="fieldNames"><s:property
															value="statusDetail" /></td>
													</tr>
												</s:iterator>
											</table>
											</div>
											</td>
										</tr>
										<tr>
											<td colspan="1">&nbsp;</td>
										</tr>
										<tr>
											<s:if test='%{jobRequest.jobStatus.value == 4}'>
												<td colspan="1" align="center" height="40" valign="bottom">
												<span class="fieldNames"> <input type="button" class="buttonSize108"
													value="<s:text name='execue.console.consoleHome.button' />" name="imageField"
													onclick="javascript:showConsoleHome();" /> </span></td>
											</s:if>
											<s:else>
												<td colspan="1" align="center" height="40" valign="bottom">
												<span class="fieldNames"> <input type="submit"
													class="buttonSize108"
													name="imageField" id="imageField"
													value="<s:text name='execue.global.refreshStatus.button' />" /> </span> <span
													class="fieldNames"> <img
													src="../images/admin/maintenanceHomeButton.jpg" name="imageField"
													onclick="javascript:showMaintenanceHome();" /> </span></td>
											</s:else>
										</tr>
									</table>
									<s:hidden id="modelId" name="modelId" />
									<s:hidden id="applicationId" name="applicationId" />
									<s:hidden name="jobRequest.id" id="jobRequestId" />
									<s:hidden name="asset.id" id="assetId" />
									<s:hidden name="asset.name" id="assetName" />
								</s:form></div>
								</div>
								</div>
								<!-- 
            <div class="ui-layout-east" style="overflow-x: hidden;width:10px">
            </div>
            --></div>
								&nbsp;</td>
							</tr>
						</table>
						</td>
					</tr>
				</table>

				</td>
			</tr>
		</table>

		<script>
function showMaintenanceHome(){      
	   var modelId= $("#modelId").val();
	   var applicationId=$("#applicationId").val(); 	
	   document.location = "showMaintenanceHome.action?modelId="+modelId+"&applicationId="+applicationId;
}

</script>