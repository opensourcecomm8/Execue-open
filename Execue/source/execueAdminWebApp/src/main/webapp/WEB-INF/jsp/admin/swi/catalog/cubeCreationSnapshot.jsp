<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground">
				<strong><s:text name="execue.cubeCreation.snapshot"/></strong></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="96%" border="0" align="center" cellpadding="0"
			cellspacing="0" >
			<!-- tr>
				<td valign="top" class="descriptionText"><s:text
					name="execue.cubeCreation.dimension.selection.description" /></td>
			</tr-->
			<tr>
				<td valign="top">

				<table width="400" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="right" height="25">
						<div id="backLink"><a href="javascript:back();"><s:text name="execue.global.link.back"></s:text> </a></div>
						<div id="loadingBackLink" style="display: none;"><img
							src="../images/admin/loading.gif" width="20" height="20"></div>
						</td>
					</tr>
					<tr>
						<td>
						<div id="container"
							style="width: 950px; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<table width="100%" height="400">
							<tr>
								<td colspan="2"><strong><s:text name="execue.cubeCreation.snapshot"/></strong></td>
								<td width="2%" align="center" class="blueLineVeritical"
									rowspan="2">&nbsp;</td>
								<td colspan="2"><strong><s:text name="execue.cubeCreation.snapshot.targetasset"/></>:</strong></td>
							</tr>
							<tr>
								<td colspan="2" valign="top">
								<div style="overflow: auto; height: 340px; width: 460px; left: 50px">
								 <table width="80%" align="center">
									<tr>
										<td width="48%"><strong><s:text name="execue.cubeCreation.snapshot.asset"/></>:</strong></td>
										<td><s:property
											value="localUICubeCreation.baseAsset.name" /></td>
									</tr>
									<tr>
										<td colspan='2'>
										<table width="100%">
											<tr>
												<td width="48%"><strong><s:text name="execue.cube.selected.simple.lookup"></s:text> :</strong></td>
												<td width="46%"></td>
											</tr>
											<s:iterator value="localUICubeCreation.selectedConcepts">
												<tr>
													<td></td>
													<td><s:property value="displayName" /></td>
													<td width="6%"></td>
												</tr>
											</s:iterator>
										</table>
										</td>
									</tr>
									<tr>
										<td colspan='2'>
										<table width="100%">
											<tr>
												<td width="48%"><strong><s:text name="execue.cube.selected.range.lookup"></s:text> :</strong></td>
												<td width="46%"></td>
											</tr>
											<s:iterator value="localUICubeCreation.selectedRanges">
												<tr>
													<td></td>
													<td><s:property value="name" /></td>
													<td width="6%"></td>
												</tr>
											</s:iterator>
										</table>
										</td>
									</tr>
									<tr>
										<td colspan='2'>
										<table width="100%">
											<tr>
												<td width="48%"><strong><s:text name="execue.cube.default.dimensions"></s:text> :</strong></td>
												<td width="46%"></td>
											</tr>
											<s:iterator value="defaultDimensions">
												<tr>
													<td></td>
													<td><s:property /></td>
													<td width="6%"></td>
												</tr>
											</s:iterator>
										</table>
										</td>
									</tr>
									<tr>
										<td colspan='2'>
										<table width="100%">
											<tr>
												<td width="48%"><strong><s:text name="execue.cube.default.stats"></s:text> :</strong></td>
												<td width="46%"></td>
											</tr>
											<s:iterator value="defaultStats">
												<tr>
													<td></td>
													<td><s:property /></td>
													<td width="6%"></td>
												</tr>
											</s:iterator>
										</table>
										</td>
									</tr>
								</table>
								</div>
								</td>
								<td width="50%" colspan="2" valign="top">
								<div id="targetAssetDynaminPane"></div>
								</td>
							</tr>
						</table>
						</div>
						</td>
					</tr>
					<tr>
						<td align="right" height="25">&nbsp;</td>
					</tr>
				</table>
				<!-- <div style="float: right;"><input type="button"
					value="Create Cube" onclick="location.href='createCube.action'" /></div>-->
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>


