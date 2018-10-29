<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<style>
a.links{
padding-top:0px;
padding-bottom:0px;
}

</style>

<div class="tableList"
											style="height: 225px; width: 190px; margin: auto; margin-bottom: 5px; margin-left: 0px;overflow-y:hidden;">
										<table width="100%" border="0" cellspacing="0" cellpadding="0" id="searchList2">
											<tr id="rangeRow1" class="rowSelected">
												<s:iterator value="concepts" status="inst" id="dimension">
													<tr id="conceptRow1">
														<td width="1%" class="dotBullet">&nbsp;</td>
														<td width="99%" class="fieldNames">
														<div id="showConcept<s:property value="id"/>Link"><a
															href="javascript:showDimensionDetails('<s:property value="id"/>','<s:property value="bedId"/>');"
															class="links" id=<s:property value="%{#concept.id}"/>)><s:property
															value="displayName" /></a></div>
														<div id="loadingShowConcept<s:property value="id"/>Link" style="display: none;"><img
															src="../images/loadingBlue.gif" width="25" height="25" /></div>
														</td>
													</tr>
												</s:iterator>
										</table>
										</div>
                                        <s:set name="url" id="url" value="%{'showConceptsForCube.action?baseAsset.id='+baseAsset.id}"/>
                                        <s:hidden id="baseAssetId" name="baseAsset.id" value="%{baseAsset.id}" />
                                         <div id="paginationDiv2" style="margin-top:10px;margin-bottom:5px;" ><pg:page targetPane="dynamicPaneCubeConcepts" targetURL="${url}"/></div>