<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="execue.defaultMatric.main.metrics"></s:text> </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="98%" border="0" cellspacing="0" cellpadding="0"
					align="center">
					<tr>
						<td height="25"><jsp:include
							page='/views/admin/showSelectAsset.jsp' flush="true" /></td>
					</tr>
					<tr>
						<td colspan="3" valign="top" class="descriptionText"><s:text
					name="execue.defaultMetric.main.description" /></td>

					</tr>
				</table>
				</td>
			<tr>
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="25%" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td align="center" valign="top" width="20%">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30"><span class="tableSubHeading"
									style="padding-left: 2px;"><s:text
									name="execue.asset.grain.available.assets" /></span></td>
							</tr>
							<tr>
								<td>
								<div class="tableBorder"
									style="padding-top: 5px; height: auto; width: 200px; margin-bottom: 5px; border: none;">

								<table width="99%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" valign="top">
										<div id="divGrain">
										<div id="divASearchAssetTables"></DIV>
										</td>
									</tr>
									<tr>
									<tr>
										<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
												<div id="dynamicPaneAssetTables"></div>
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
						</td>
						<td width="3%" height="auto" align="center"
							class="blueLineVeritical">&nbsp;</td>
						<td width="77%">
						<table border="0" cellpadding="0" cellspacing="0">

							<tr>
								<td height="335" align="left" valign="top">
								<div id="dynamicPaneDefaultMetrics" style="width: 670px"></div>
								</td>
							</tr>
							<tr>
								<td>&nbsp;</td>
							</tr>
						</table>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td colspan="3" valign="top">&nbsp;</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>