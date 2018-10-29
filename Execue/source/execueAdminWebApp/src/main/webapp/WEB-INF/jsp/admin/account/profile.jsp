<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<STYLE type=text/css>
input[type='text'],input[type='textarea'],select,.textBox {
	width: 180px;
}
</STYLE>

<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%" align=left>
	<TBODY>
		<TR>
			<TD height=30 vAlign=bottom>
			<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
					<TR>
						<TD class=titleWithBackground height=29><s:text
							name="execue.account.profile.pageHeading" /></TD>
					</TR>
				</TBODY>
			</TABLE>
			</TD>
		</TR>
		<TR>
			<TD>
			<DIV style="PADDING-LEFT: 3px; HEIGHT: 400px" id=greyBorder>
			<TABLE border=0 cellSpacing=0 cellPadding=0 width="98%" align=center>
				<TBODY>
					<TR>
						<TD class=descriptionText vAlign=top>
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>

								<td width="100%" align="left" height="25"><a id="myLink"
									class="arrowBg" href="javascript:showChangePass();"><s:text
									name="execue.account.profile.changePasswordLink" /></a></td>
							</tr>

							<tr>
								<td width="100%" height="25"><s:text
									name="execue.account.profile.description" /></td>

							</tr>
						</table>



						</TD>
					</TR>
					<TR>
						<TD>
						<DIV style="COLOR: green" id=message align=center></DIV>
						</TD>
					</TR>
					<TR>
						<TD vAlign=top>
						<TABLE width="100%" border=0 cellSpacing=0 cellPadding=0
							align=left>
							<TBODY>
								<TR>
									<TD>
									<DIV
										style="MARGIN: auto; WIDTH: 100%; HEIGHT: 350px; BORDER-TOP: #ccc 1px dashed"
										id=container>
									<DIV style="WIDTH: 100%; FLOAT: left" class=ui-layout-center>
									<table width="100%" border="0" cellspacing="0" cellpadding="0">
										<tr>
											<td width="100%" valign="top">
											<div
												style="background-color: F3F4FE; margin-bottom: 5px; height: 370px;">
											<div id="dynamicPane"></div>
											</div>
											</td>
										</tr>
									</table>
									</DIV>
									</DIV>
									</TD>
								</TR>
							</TBODY>
						</TABLE>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
			</DIV>
			</TD>
		</TR>
	</TBODY>
</TABLE>

