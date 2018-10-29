<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<style>
#tableGridMemberInfo td{
padding-top:0px;

}
</style>
<div id="errorMessage"><s:fielderror /> <s:actionmessage /></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
										
										<div style="height: 302px; overflow: auto; overflow-x: hidden">
										<table width="100%" border="0" cellpadding="0" cellspacing="1"
											id="tableGridMemberInfo">
                                            
                                            <tr id="tableGridTitle">
												<td width="150" height="25" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.name" /></strong></td>
												<td width="100" bgcolor="#EEEEEE"><strong><s:text
													name="execue.asset.ws.asset.label.data-source" /></strong></td>
												<td width="50" bgcolor="#EEEEEE"><strong><s:text
													name="execue.asset.ws.asset.label.type" /></strong></td>
												<td width="150" bgcolor="#EEEEEE"><strong><s:text
													name="execue.asset.ws.asset.label.description" /></strong></td>
												<td width="48" align="center" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.edit" /></strong></td>
												<td width="48" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.enabled" /></strong></td>
                                                <td width="150" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.delete" /></strong></td>
											</tr>

											<s:iterator value="assets" status="even_odd" id="asset">
												<tr>
													<td><s:property value="displayName" /></td>
													<td><s:property value="dataSource.name" /></td>
													<td><s:property value="type" /></td>
													<td><s:property value="description" /></td>
													<td align="center"><a
														href="showAsset.action?asset.id=<s:property value='id' />&asset.name=<s:property value='name' />"
														class="links" id="1"><img src="../images/editIcon.gif"
														alt="Edit" height="17" border="0" title="Edit" /></a></td>
													<td align="center"><s:if
														test='%{#asset.status.value == "A"}'>
														<div id="showStatus<s:property value="id"/>Link"><a
															href="javascript:showAssetStatus('<s:property value="id"/>');">
														<img src="../images/enabledIcon.gif" alt="Enabled"
															 height="17" border="0" title="Enabled" /></a></div>
														<div id="loadingShowStatus<s:property value="id"/>Link"
															style="display: none;"><img
															src="../images/loadingWhite.gif" width="20" height="20"></div>
													</s:if> <s:else>
														<div id="showStatus<s:property value="id"/>Link"><a
															href="javascript:showAssetStatus('<s:property value="id"/>');">
														<img src="../images/disabledIcon.gif" alt="Disabled"
															 height="17" border="0" title="Disabled" /></a></div>
														<div id="loadingShowStatus<s:property value="id"/>Link"
															style="display: none;"><img
															src="../images/loadingWhite.gif" width="20" height="20"></div>
													</s:else></td>
                                                    <td>
                                                    <div id="showDelete<s:property value="id"/>Link"><a
                                                    href="javascript:deleteDataset(<s:property value="id"/>);"><img
                                                    src="../images/disabledIcon.gif" alt="Delete" width="25"
                                                    height="20" border="0" title="Delete"/></a></div>
													<div id="loadingShowDelete<s:property value="id"/>Link"
														style="display: none;"><img
														src="../images/loadingWhite.gif" width="20" height="20"></div>                                                    
                                                    <div id="showStatus<s:property value="id"/>" style="display:none;float:left;min-width:190px;width:auto;margin-left:10px;white-space:nowrap;margin-top:10px;"></div>
                                                    </td>
												</tr>
											</s:iterator>
										</table>
										</div>
										</td>
									</tr>

								</table>
                                <div id="paginationDiv2" style="float:right;" ><pg:page targetPane="dynamicPaneAssets" targetURL="viewSubAssetList.action"/></div>