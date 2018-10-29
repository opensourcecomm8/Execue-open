<meta http-equiv="refresh" content="10" />
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="Cube Creation Status" /></td>
			</tr>
			<!--tr>
				<td>
					<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
			<div id="actionMessage" style="color: red"><s:actionmessage /></div>
				</td>
			</tr-->
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText">
				<table>
					<tr>
						<td><s:text
							name="execue.CubeCreation.status.description" /></td>
						<td></td>
					</tr>
					<tr>
						<td valign="top">

						<table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>


								<div id="container"
									style="width: 100%; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
								<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->

								<!-- 
            <div class="ui-layout-west" style="overflow-x: hidden; width: 10px">
            <div id="dynamicPaneWest">
            
            </div>
            
            </div>
             -->
								<div class="ui-layout-center"
									style="width: 940px; overflow: hidden;">
								<div id="dynamicPane">

								<div id="errorMessage"><s:fielderror /> <s:actionmessage />
								<s:actionerror /></div>

								<div class="innerPane" style="width: 99%"><s:form
									namespace="/swi" action="showCubeStatus" method="post">
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="51%">

											<table width="90%" border="0" align="center" cellpadding="3"
												cellspacing="0">
												<tr>
													<td class="fieldNames"><strong><s:text
														name="execue.cube.request.name" /></strong></td>
													<td class="fieldNames"><s:property
														value="cubeRequestDataInfo.cubeName" /></td>
												</tr>
												<!--
                    <tr>
                      <td class="fieldNames"><s:text name="upload.main.asset.name" /></td>
                      <td><s:property value="asset.displayName" /></td>
                    </tr>
                    <tr>
                      <td class="fieldNames"><s:text name="upload.main.asset.description" /></td>
                      <td><s:property value="asset.description" /></td>
                    </tr>
                    -->
												<tr>
													<td class="fieldNames"><strong><s:text
														name="cube.request.execution.status" /></strong></td>
													<td class="fieldNames"><strong><s:property
														value="cubeJobRequestStatus.jobStatus" /></strong></td>
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
												style="overflow: auto; width: 90% px; height: 200px; border: 1px solid #333333;margin-left:20px;">
											<table width="90%" border="0" align="center" cellpadding="3"
												cellspacing="0">
												<tr>
													<td class="fieldNames"><strong><s:text
														name="execue.cube.request.operation.stage" /></strong></td>
													<td class="fieldNames"><strong><s:text
														name="execue.cube.request.operation.status" /></strong></td>
													<td class="fieldNames"><strong><s:text
														name="execue.cube.request.operation.status.detail" /></strong></td>
												</tr>
												 <s:set name="jobRequestStatus" value="%{#cubeJobRequestStatus.jobStatus.value}"/>
												 
													<s:iterator value="cubeJobHistory" status="even_odd"
															id="cubeJobHistory">
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
										<!--tr>
                  <td colspan="1">&nbsp;</td>
                </tr-->
										<tr>
											<s:if test='%{cubeJobRequestStatus.jobStatus.value == 3}'>
												<td colspan="1" align="center" height="40" valign="bottom">
												<span class="fieldNames"> <img
													src="../images/viewDatasetDetailsButton.jpg"
													name="imageField" onclick="javascript:showAssetScreen();" />
												</span></td>
											</s:if>
											<s:elseif test='%{cubeJobRequestStatus.jobStatus.value == 4}'>
												<td colspan="1" align="center" height="40" valign="bottom">
												<span class="fieldNames"> <input type="button" class="buttonSize108"
													value="<s:text name='execue.console.consoleHome.button' />" name="imageField"
													onclick="javascript:showConsoleHome();" /> </span></td>
											</s:elseif>
											<s:else>
												<td colspan="1" align="center" height="40" valign="bottom">
												<span class="fieldNames"> <input type="submit" class="buttonSize108"
													name="imageField" id="imageField"
													value="<s:text name='execue.global.refreshStatus.button' />" /> </span></td>
											</s:else>
										</tr>
									</table>
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