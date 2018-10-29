<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="execue.asset.detail.assetdetail"/></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="100%" border="0" align="center" cellpadding="0" style="margin-bottom:15px;"
			cellspacing="0">
			<tr>
				<td colspan="3" valign="top" class="descriptionText" style="padding-left:10px;"><s:text
					name="execue.showAssetDetails.description" /></td>
			</tr>
			<tr>
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="22%" valign="top">



				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<!-- tr>
                      <td height="40"><div id="addNewConceptLink" ><a href="javascript:createNewConcept();" class="arrowBg" id="addNewConcept">Add New Concept</a></div>
                        <div id="loadingAddNewConceptLink" style="display:none;"><img src="../images/admin/loadingBlue.gif" width="25" height="25" /></div></td>
             </tr-->

					<tr>

						<td align="center" valign="top" style="padding-left:10px;">
						<table width="99%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" align="left"><span class="tableSubHeading"><s:text
									name="execue.asset.grain.available.assets" /></span></td>
							</tr>
							<tr>
								<td>
								<div class="tableBorder"
									style="padding-top: 5px; height: 310px; width: 200px; margin-bottom: 5px;margin-right:5px;border:none;">

								<table width="99%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" valign="top">
										<div id="divGrain">
										<DIV id=roundedSearch >
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText type=search
											value=Search>
										<div class=searchEnd id=searchIcon><a href="#"><img
											src="../images/admin/searchEnd.gif" name="Image2a" border="0"
											id="Image2a"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2a')"
											onMouseOut="MM_startTimeout();" /></a></div>
										</DIV>

										<!--div id="searchTables"><a
											href="javascript:showSearch('divGrain');" id="searchLink" class="links"><s:text
											name="global.search" /></a></div-->
										</div>
										</td>
									</tr>
									<tr>
									<tr>
										<td>
										<table width="100%" border="0" cellspacing="0" cellpadding="0">
											<tr>
												<td>
												<div id="dynamicPaneAssets" ></div>
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
						<td width="3%" height="310" align="center"
							class="blueLineVeritical">&nbsp;</td>
						<td width="75%">
						<table border="0" cellpadding="0" cellspacing="0">
							<tr>
								<td>&nbsp;</td>
							</tr>
							<tr>
								<td height="340" align="center" valign="top">
								<div id="dynamicPane" style="width: 670px"></div>
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
		</table>
		</div>
         </td>
         </tr>
         </table>