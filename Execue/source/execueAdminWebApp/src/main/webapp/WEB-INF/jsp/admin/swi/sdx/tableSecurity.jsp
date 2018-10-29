<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="execue.table.security.level"/></td>
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
				<td valign="top" class="descriptionText" width="70%"><s:text name="execue.table.security.description"/></td>
			</tr>
			<tr>
				<td valign="top" class="descriptionText">
				<table width="300" border="0" cellspacing="0" cellpadding="0"
					align="left">
					<tr>
						<td height="35"><span style="white-space: nowrap"><s:text name="execue.authorized.asset.security.level"/> </span></td>
						<td align="center" style="padding-left: 3px;">
						<select id="authorizedAssetsForRole" name="selectedAssetId"
							style="width: 120px;">
						</select></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td valign="top">

				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>
						<div id="container"
							style="width: 100%; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
						<div class="ui-layout-west"
							style="overflow-x: hidden; width: 220px; padding-left: 0px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" class="tableHeading">
								<table width="100%" border="0" cellspacing="0" cellpadding="3">
									<tr>
										<td width="80%" valign="bottom" class="tableSubHeading"><s:text name="execue.dataset.security.roles"></s:text> </td>
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
										<div id="divRoleSearch"></div>
										</td>
									</tr>
									<tr>
										<td>
										<div id="roleDynamicPane"></div>
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>
						</div>
						<div class="ui-layout-center" style="width: 700px;">
						     <div id="dynamicPane"></div>
						</div>

						<div class="ui-layout-east"
							style="overflow-x: hidden; padding-left: 0px;">
						<div id="rightPane"></div>
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
<s:hidden name="" value="Table" id="contextId"></s:hidden>
</body>
</html>