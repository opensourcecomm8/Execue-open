<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#actionMessage ul {
	margin: 0px;
	padding: 0px;
	margin-bottom: 5px;
}
</style>

<div id="errorMessage"
	style="color: red; padding-top: 5px; padding-left: 20px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage"
	style="color: green; padding-top: 0px; padding-left: 20px;"><s:actionmessage /></div>
<div id="validationMessage" style="display: none; padding-left: 20px;"></div>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="Mart Creation Request" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td><jsp:include page='/views/admin/showSelectAsset.jsp' flush="true" /></td>
					</tr>
					<tr>
						<td width="82%" style="padding-top: 5px; padding-bottom: 5px;">
						<table width="98%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td width="1000%"><s:text name="execue.cubeCreation.dimension.selection.description" /></td>
							</tr>
						</table>
						</td>
						<td width="18%" style="padding-left: 10px;" align="right">&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td valign="top">
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" id="contentTable">
					<tr>
						<td>
						<div
							style="white-space: nowrap; position: absolute; z-index: 100; padding: 4px; border: 1px solid rgb(204, 204, 204); background-color: rgb(255, 255, 255); color: red; top: 257.75px; left: 756.733px; display: none;"
							id="assetNameMessage">Target Name Required</div>
						<div id="container" style="width: 100%; height: 400px; margin: auto; border-top: #CCC dashed 1px;">
						<div id="contentDiv" style="margin-left: 2px;">
						<form id="createMartForm" method="post">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
                              <tr>
                                <td height="30" style="border-right:1px dashed #ccc;" class="tableSubHeading">Enter Dataset Details</td>
                                <td height="30" style="padding-left:10px;" class="tableSubHeading">Select Prominent Dimensions and Measures</td>
                              </tr>
                              <tr>
                                <td width="35%" valign="top" style="border-right:1px dashed #ccc;border-bottom:1px dashed #ccc;padding-right:10px;">
                                <div id="targetAssetDynaminPane"></div>
                               </td>
                                <td style="padding-left:10px;padding-bottom:10px;border-bottom:1px dashed #ccc;">
                                
                                
                                <table width="70%" border="0" cellspacing="2" cellpadding="2">
							
							<tr>
								<td><strong>Prominent Dimensions:</strong></td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td width="20%"><select id="eligibleProminentDimensions" style="height: 100px; width: 250px;"
									multiple="multiple">
									<s:iterator value="eligibleProminentDimensions">
										<option value='<s:property value="id"/>'><s:property value="name" /></option>
									</s:iterator>
								</select></td>
								<td width="10%"><a href="javascript:moveDimensionConcept();"><img height="22" border="0"
									title="Move to right" alt="Move to right" src="../images/moveRightButton2.gif"></a> <a
									href="javascript:removeDimensionConcept();"><img height="22" style="margin-top: 10px;" border="0"
									title="Move to left" alt="Move to left" src="../images/moveLeftButton.gif"></a></td>
								<td width="40%"><select id="selectedProminentDimensions" name="selectedProminentDimensions" style="height: 100px; width: 250px;"
									multiple="multiple">
									<s:iterator value="selectedProminentDimensions">
										<option value='<s:property value="id"/>'><s:property value="name" /></option>
									</s:iterator>
								</select></td>
							</tr>
							<tr>
								<td><strong>Prominent Measures:</strong></td>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td width="20%"><select id="eligibleProminentMeasures" style="height: 150px; width: 250px;"
									multiple="multiple">
									<s:iterator value="eligibleProminentMeasures">
										<option value='<s:property value="id"/>'><s:property value="name" /></option>
									</s:iterator>
								</select></td>
								<td width="10%"><a href="javascript:moveMeasureConcept();"><img height="22" border="0"
									title="Move to right" alt="Move to right" src="../images/moveRightButton2.gif"></a> <a
									href="javascript:removeMeasureConcept();"><img height="22" style="margin-top: 10px;" border="0"
									title="Move to left" alt="Move to left" src="../images/moveLeftButton.gif"></a></td>
								<td width="40%"><select id="selectedProminentMeasures" name="selectedProminentMeasures" style="height: 150px; width: 250px;"
									multiple="multiple">
									<s:iterator value="selectedProminentMeasures">
										<option value='<s:property value="id"/>'><s:property value="name" /></option>
									</s:iterator>
								</select></td>
							</tr>
							
						</table></td>
                              </tr>
                              <tr>
                                <td valign="top" >&nbsp;</td>
                                <td style="padding-left:10px;"><input type="button" value="Submit" id="createMartButtonId" class="buttonSize51" style="margin-top: 10px;"
									onclick="javascript: return createMart();" /></td>
                              </tr>
                            </table>

						</form>
						</div>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
<s:hidden id="assetId" name="baseAsset.id" />
<script>
	$("#srcName").attr('value', 'mart');
</script>