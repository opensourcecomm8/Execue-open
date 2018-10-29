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
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name='Hierarchy Definitions' /></td>
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
				<td colspan="3" height="40" valign="top" class="descriptionText"><s:text name="execue.hierarchy.description"/></td>
			</tr>

			<tr>
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="19%" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					
					<tr>
						<td height="25"><a
							href="#" class="arrowBg"
							id="addNewHierarchyDefinition"><s:text name="execue.hierarchy.link.new"></s:text> </a> </td>
					</tr>
					<tr>
						<td height="27"><span class="tableSubHeading"> <s:text name="execue.hierarchy.list"></s:text> </span></td>
					</tr>

					<tr>
						<td align="left">
						<div class="tableBorder"
							style="padding-top: 5px; height: 344px; width: auto; margin-bottom: 5px; border: none;">
						<table width="99%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" align="left" valign="top">
								<div id="divHierarchyDefinition">
								</div>
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
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td colspan="3">&nbsp;</td>
			</tr>

		</table>
		</div>
		</td>
	</tr>

</table>
</body>
</html>