<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td width="49%" height="50" class="headding-apps"><s:property
			value="uiApplicationInfo.applicationName" /></td>
	</tr>
	<tr>
		<td valign="top">

		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing="0" id="appsDetailsDiv"
					cellpadding="0">
					<tr>
						<td width="39%">
						<TABLE border=0 cellSpacing=0 cellPadding=0 width="96%" align=left>
							<TBODY>

								<TR>
									<s:if test="uiApplicationInfo.applicationImageId != -1">
										<TD class=content-text height=34><img
											src="<c:out value="${basePath}"/>/qi/getImage.action?applicationId=<s:property value='uiApplicationInfo.applicationId'/>&appImageId=<s:property value='uiApplicationInfo.applicationImageId'/>"
											 border="0" width="90" /></TD>
									</s:if>
									<s:else>
										<TD class=content-text height=34><img
											src="<c:out value="${basePath}"/>/images/main/noImage-icon.gif"  border="0"
											width="90"></img></TD>

									</s:else>
								</TR>
								<TR>
									<TD class=content-text height=34>
									<div style="float: left"><s:property
										value="uiApplicationInfo.applicationDescription" /></div>
									</TD>
								</TR>

							</TBODY>
						</TABLE>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<TR>
				<TD colspan="2" align=left vAlign=top>&nbsp;</TD>
			</TR>
		  <TR>
			  <TD colspan="2" align=left vAlign=top>&nbsp;</TD>
			</TR>
			<tr>
				<td align="center">

				<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%" align=center id="datasetsProfiles">
					<TBODY>
						<s:if test="assets.size>1">
							<TR>
								<TD width="800%" height=30 colspan="2" align=left
									class="headding-apps">Datasets</TD>
							</TR>
							<TR>
								<TD colspan="2" align=left vAlign=top>
								<ul>
									<s:iterator value="assets">
										<li>
										<s:if test='status.value=="A"'>
										  <span  style="color:#3C71A1;line-height:19px;text-decoration:none;font-size: 9pt;" title="Active Dataset Collection" ><s:property value="displayName" /></span>
										</s:if>
										<s:else>
										  <span  style="color:#3C71A1;line-height:19px;text-decoration:none;font-size: 9pt;" title="Inactive Dataset Collection"><s:property value="displayName" /></span>
										</s:else>										
										</li>
									</s:iterator>
								</ul>
								</TD>
							</TR>
                            <TR>
							<TD colspan="2" align=left vAlign=top>&nbsp;</TD>
						</TR>
						</s:if>
                        
						<s:if test="conceptProfiles.size > 0 || instanceProfiles.size >0" >
						<TR>
							<TD colspan="2"  height="25" align=left vAlign=top><span
								class="headding-apps"
								style="PADDING-TOP: 12px; padding-right: 10px;">Profiles</span></TD>
						</TR>
                        </s:if>
						<TR>
							<TD colspan="2" align=left vAlign=top> 
							<TABLE border=0 cellSpacing=0 width="95%" align=center>
								<TBODY>
									<s:iterator value="conceptProfiles">
										<TR>
											<TD align=left>&nbsp;</TD>
											<TD align=left><a class="profile-link" style="color:#3C71A1;cursor: pointer;line-height:19px;text-decoration:none;" href="#" ><s:property value="displayName" /></a></TD>
										</TR>
									</s:iterator>
									<s:iterator value="instanceProfiles">
										<TR>
											<TD align=left>&nbsp;</TD>
											<TD align=left><a class="profile-link"  href="#" style="color:#3C71A1;cursor: pointer;line-height:19px;text-decoration:none;"><s:property value="displayName" /></a></TD>
										</TR>
									</s:iterator>
								</TBODY>
							</TABLE>
							</TD>
						</TR>

					</TBODY>
				</TABLE>


				</td>
			</tr>

			<!--/td>
			<td>
            
            
			<table width="98%" border="0" align="center" cellpadding="0"
				cellspacing="0">
				<tr>
					<td valign="top">
					<div id="queriesDiv"></div>
					<div id="t1app" class="appsExamplesLinks">
					<div class="querieshome">
					<table width="100%" border="0" align="center" cellpadding="0"
						cellspacing="0">
						<tr>
							<td>
							<table width="100%" border="0" align="center" cellspacing="0"
								id="earmarksAppExamples">
								<s:iterator value="uiApplicationInfo.appExamples">
									<tr>
										<td align="left"><a style="cursor: pointer;"
											class="box2-link"><s:property value="queryName" /> </a></td>
									</tr>
								</s:iterator>
							</table>
							</td>
						</tr>
					</table>
					</div>
					</div>
					</td>
				</tr>
			</table>
            
            
			</td>
			</tr-->
		</table>
  </tr>
</table>