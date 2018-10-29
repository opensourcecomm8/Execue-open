<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>semantifi | meaning based search | business intelligence |
search databases | search data sets | bi tools</title>
</head>
<table width="100%" border="0" cellspacing="5" cellpadding="5"
	height="98%" bgcolor="#FFFFFF">
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground">Message
				Box</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td height="50%" valign="top"
			style="border-top: 1px dashed #CCC; border-bottom: 1px dashed #CCC;">
		<div style="width: 100%; height: 355px; overflow: auto;">
		<table width="100%" cellspacing="1" cellpadding="0" border="0"
			id="tableGridMemberInfo">
			<tbody>
				<tr class="tableGridTitle">
					<!-- <td height="25" width="17%"><strong>Id</strong></td>-->
					<td height="25" width="17%"><strong> <s:text
						name="execue.messagebox.main.date" /></strong></td>
					<td width="17%"><strong><s:text
						name="execue.messagebox.main.category" /></strong></td>
					<td width="17%"><strong><s:text
						name="execue.messagebox.main.type" /></strong></td>
					<td width="49%"><strong><s:text
						name="execue.messagebox.main.subject" /></strong></td>
				</tr>
				<s:iterator value="notificationInfo" id="notifications"
					status="even_odd">
					<tr class="bgColorWhite">
						<!-- <td width="20%"><s:property value="id" /></td>-->
						<td width="20%"><s:property value="createdDate" /></td>
						<td width="34%"><s:property
							value="notificationType.description" /></td>
						<td width="10%"><s:property value="category.description" /></td>
						<td width="10%"><a href="#"
							onclick="javascript:showMessage(<s:property value='id' />,'<s:property value='notificationType' />');"><s:property
							value="subject" /></a></td>
					</tr>

				</s:iterator>
		</table>
		</div>
		</td>

	</tr>
	<tr>
		<td>
		<table align="center">
			<tr>
				<td><s:set name="url" id="url"
					value="%{'showMessageBox.action'}"></s:set>
				<div id="paginationDiv2"><pg:page targetURL="${url}"
					targetPane="dynamicPane" /></div>
				</td>
			</tr>
		</table>

		</td>
	</tr>
	<tr>
		<td height="50%" valign="top" style="border-bottom: 1px dashed #CCC;">
		<div id="showMessageDiv"
			style="width: 100%; height: 300px; overflow: auto;"></div>

		</td>
	</tr>
</table>
</html>

