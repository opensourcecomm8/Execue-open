<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="50" align="center" cellpadding="10" cellspacing="0">
	<tr>
		<td>
		<div id="greyBorder1">
		<table width="50%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText" height="70"><s:text
					name="execue.correctmapping.main.discription" /></td>
			</tr>
			<tr>
				<td valign="top">

				<table width="200" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>


						<div id="container" style="width: 763px; height: 405px;">

						<div class="ui-layout-center"
							style="width: 750px; overflow: hidden;">
						<div id="dynamicPane">

						<div id="errorMessage"><s:fielderror /> <s:actionmessage />
						<s:actionerror /></div>

						<div class="innerPane" style="width: 99%"><s:form
							id="correctMppingForm" namespace="/swi"
							action="sheduleCorrectMappingMaintenanceJob" method="post"
							enctype="multipart/form-data">
							<table width="80%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td valign="top" align="center">
									<table width="10%" border="0" align="center" cellpadding="3"
										cellspacing="0">								
										<tr>
											<td class="fieldNames">
											<STRONG><s:text name="execue.maintenance.correctmapping.selectasset"/>: </STRONG></td>

										</tr>
										<tr>
											<td align="center"><select id="asset" name="assetIds"
												multiple="multiple"
												style='width: 190px; height: 108px; font-size: 12px; color: #525252; margin-top: 2px;'>
												<s:iterator id="correctMappingAssetId" value="assets"
													status="list">
													<option value="<s:property
														value="id" />"><s:property
														value="displayName" /></option>
												</s:iterator>
											</select></td>

										</tr>
									</table>
								<tr>
									<td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" align="center" height="40" valign="bottom"><span
										class="fieldNames"> <input type="submit" class="buttonSize108"
										name="imageField" id="scheduleJob"
										value="<s:text name='execue.global.scheduleJob' />" /> <input type="button" class="buttonSize51" onclick="hide();"
										name="imageField2" id="imageField2"
										value="<s:text name='execue.global.cancel' />" /> </span></td>
								</tr>
							</table>

							<input type=hidden name="modelId" id="modelId"
								value="<%=request.getAttribute("modelId") %>" />
							<input type=hidden name="applicationId" id="modelId"
								value="<%=request.getAttribute("applicationId") %>" />

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
		</div>
		</td>
	</tr>
</table>
