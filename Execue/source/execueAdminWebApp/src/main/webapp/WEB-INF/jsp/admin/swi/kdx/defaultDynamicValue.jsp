<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="hiddenPane" style="position: absolute; width: 350px; height: 350px; z-index: 100000; display: none;">
<table border="0" cellpadding="0" cellspacing="0" width="310" align="left">
	<tr>
		<td width="15"><img name="boxTemplate_r1_c1" src="../images/admin/boxTemplate_r1_c1.gif" width="15" height="40"
			border="0" id="boxTemplate_r1_c1" alt="" /></td>
		<td background="../images/admin/boxTemplate_r1_c2.gif">
		<div id="boxTitleDiv" style="height: 25px; font-weight: bold; color: #FFF; font-size: 12px;"></div>
		</td>
		<td width="15"><a href="javascript:closeBox();" id="closeButtonId"><img name="boxTemplate_r1_c3"
			src="../images/admin/boxTemplate_r1_c3_1.gif" width="15" height="40" border="0" id="boxTemplate_r1_c3" alt="Close"
			title="Close" /></a></td>
		<td width="13"><img src="../images/admin/spacer.gif" width="1" height="40" border="0" alt="" /></td>
	</tr>
	<tr>
		<td background="../images/admin/boxTemplate_r2_c1.gif"><img name="boxTemplate_r2_c1"
			src="../images/admin/boxTemplate_r2_c1.gif" width="15" height="10" border="0" id="boxTemplate_r2_c1" alt="" /></td>
		<td width="1045" valign="top" bgcolor="#FFFFFF">
		<div id="hiddenPaneContent" style="background-color: #FFF"></div>
		</td>
		<td background="../images/admin/boxTemplate_r2_c3.gif"><img name="boxTemplate_r2_c3"
			src="../images/admin/boxTemplate_r2_c3.gif" width="15" height="10" border="0" id="boxTemplate_r2_c3" alt="" /></td>
		<td><img src="../images/admin/spacer.gif" width="1" height="10" border="0" alt="" /></td>
	</tr>
	<tr>
		<td><img name="boxTemplate_r3_c1" src="../images/admin/boxTemplate_r3_c1.gif" width="15" height="13" border="0"
			id="boxTemplate_r3_c1" alt="" /></td>
		<td background="../images/admin/boxTemplate_r3_c2.gif" valign="top"><img name="boxTemplate_r3_c2"
			src="../images/admin/boxTemplate_r3_c2.gif" width="25" height="13" border="0" id="boxTemplate_r3_c2" alt="" /></td>
		<td><img name="boxTemplate_r3_c3" src="../images/admin/boxTemplate_r3_c3.gif" width="15" height="13" border="0"
			id="boxTemplate_r3_c3" alt="" /></td>
		<td><img src="../images/admin/spacer.gif" width="1" height="13" border="0" alt="" /></td>
	</tr>
</table>
</div>
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="execue.ddv.header" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td>
				<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td height="20" align="left" width="100%" valign="middle">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<!--  <tr>
								<td height="23" width="100%" align="left">
								<div id="showDasetCollectionLink"><a href="../swi/showAssets.action" class="arrowBg"><s:text
									name="execue.showAssets.showAssets.linkName" /></a></div>
								</td>
							</tr> -->
							<tr>
								<td height="23" width="100%"><jsp:include page='/views/admin/showSelectAsset.jsp' flush="true" /></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td valign="top" class="descriptionText" width="70%"><s:text name="execue.ddv.bodyText" /></td>
					</tr>
					<tr>
				</table>
				</td>
			</tr>
			<td valign="top">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td>
					<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
						<tr>
							<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img src="../images/admin/blueLine.jpg"
								width="10" height="1" /></td>
						</tr>
						<tr>
							<td width="19%" valign="top">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="27"><span class="tableSubHeading"><s:text name='execue.businessterms.concepts.subheading' /></span></td>
								</tr>
								<tr>
									<td align="left">
									<div class="tableBorder"
										style="padding-top: 5px; height: 344px; width: auto; margin-bottom: 5px; border: none;">
									<table width="99%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td>
											<table width="100%" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td>
													<table width="100%" border="0" cellspacing="0" cellpadding="2">
														<s:iterator value="uiConcepts" id="concept" status="count">
															<tr id="conceptRow1">
																<td width="1%" class="dotBullet">&nbsp;</td>
																<td width="99%" class="fieldNames">
																<div id="showConcept1Link"><a
																	href="javascript:getConcept('<s:property value="%{#concept.bedId}"/>');" class="links"
																	id='<s:property value="%{#concept.bedId}"/>' attributeName='<s:property value="%{#concept.name}" />'><s:property
																	value="%{#concept.displayName}" /></a></div>
																<div id="loadingShowConcept1Link" style="display: none;"><img src="../images/admin/loadingBlue.gif"
																	width="25" height="25" /></div>
																</td>
															</tr>
														</s:iterator>
													</table>
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
							<td width="3%" height="350" align="center" class="blueLineVeritical">&nbsp;</td>
							<td width="78%" valign="top">
							<table border="0" cellpadding="0" cellspacing="0">
								<tr>
									<td height="350" align="left" valign="top">
									<div id="dynamicPane" style="width: 720px"></div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img src="../images/admin/blueLine.jpg"
								width="10" height="1" /></td>
						</tr>
						<tr>
							<td colspan="3">&nbsp;</td>
						</tr>
					</table>
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
<script>
	$("#srcName").attr('value', 'ddv');
</script>