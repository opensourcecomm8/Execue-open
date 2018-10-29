<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
 <div class="tableList" style="height: 240px; width: 190px; margin-left: 0px; margin-bottom: 5px;overflow:hidden;overflow-x:scroll;overflow-y:hidden;">
<table width="100%" border="0" cellspacing="0"
													cellpadding="1">
													<s:iterator value="assets" status="inst" id="asset">
														<tr id="assetRow1">
															<td width="1%" class="dotBullet">&nbsp;</td>
															<td width="99%" class="fieldNames">
															<div id="showAssetLink<s:property value="%{#asset.id}"/>"><a
																href="javascript:getAssetDetail(<s:property value="%{#asset.id}"/>,'<s:property value="%{#asset.displayName}"/>','<s:property value="%{#asset.description}"/>');"
																class="links" id=<s:property value="%{#asset.id}"/>)><s:property
																value="%{#asset.displayName}" /></a></div>
															<div
																id="loadingShowAssetLink<s:property value="%{#asset.id}"/>"
																style="display: none;"><img
																src="../images/loadingBlue.gif" width="20" height="20" /></div>
															</td>
														</tr>
													</s:iterator>
												</table>
</div>
<div id="paginationDiv2" style="margin-top:10px;margin-bottom:5px;" ><pg:page targetPane="dynamicPaneAssets" targetURL="getSubListForAssetDetail.action"/></div>

