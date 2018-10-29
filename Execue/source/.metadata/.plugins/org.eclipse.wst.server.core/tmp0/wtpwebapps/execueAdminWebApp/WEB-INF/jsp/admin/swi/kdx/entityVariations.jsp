<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name='execue.entityVariations.heading' /></td>
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
				<td valign="top" class="descriptionText"><s:text
					name="execue.entityVariations.description" /> <div style="float:right;" ><s:text name="execue.keywords.businessTerm"/> : <s:select list="businessEntityTermTypes"  listValue="description" id="businessEntityTermTypesId"></s:select></div></td>
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
						<div class="ui-layout-west"
							style="overflow-x: hidden; width: 220px; padding-left: 0px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" class="tableHeading">
								<table width="100%" border="0" cellspacing="0" cellpadding="3">
									<tr>
										<td width="80%" valign="bottom" class="tableSubHeading"><s:text
											name='execue.keywords.BusinessTerms.heading' />
										</td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td>
								<div class="tableBorder"
									style="padding-top: 5px; height: 305px; width: 235px; border: none;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td height="30" align="left" valign="top">
										<div id="divBT"></div>
										</td>
									</tr>
									<tr>
										<td>
										<div id="dynamicPaneBTerms" style="width:190px;"></div>
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>

						</div>
						<div class="ui-layout-center" style="width: 500px;">
						<div id="dynamicPane"></div>
						</div>

						
						</div>
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