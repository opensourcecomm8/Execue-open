<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<style>
#tableGridMemberInfo td{
padding-top:0px;

}
</style>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
										
										<div
											style="height: 305px; overflow: auto; overflow-x: hidden; width: 950px;">
										<table width="100%" border="0" cellpadding="0" cellspacing="1"
											id="tableGridMemberInfo">
                                            <tr id="tableGridTitle">
												<td width="20%" height="28" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.name" /></strong></td>
												<td width="50%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.description" /></strong></td>
												<td width="14%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.defineDataSource.providerType" /></strong></td>
												<td width="8%" align="left" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.edit" /></strong></td>
												<td width="8%" bgcolor="#EEEEEE"><strong><s:text
													name="execue.global.delete" /></strong></td>
											</tr>

											<s:iterator value="dataSources" status="even_odd"
												id="dataSource">
												<tr>
													<td width="20%"><s:property value="name" /></td>
													<td width="50%"><s:property value="description" /></td>
													<td width="14%"><s:property value="providerType" /></td>
													<td width="8%" align="center"><a
														href="<s:url action="showDataSource">
            										<s:param name="dataSource.name" value="name" />
        										</s:url>"
														class="links" id="1"><img src="../images/admin/editIcon.gif"
														alt="Edit"  height="15" border="0" title="Edit" /></a></td>




													<td width="8%" align="center"><a
														href="javascript:showDataSourceDeletionConfirmation('<s:property value="name" />');"><img
														src="../images/admin/disabledIcon.gif" alt="Disabled" width="25"
														height="20" border="0" title="Delete" /></a></td>
												</tr>
											</s:iterator>
										</table>
										</div>
										</td>
									</tr>

								</table>
                                
                                 <div id="paginationDiv2" style="float:right;" ><pg:page targetPane="dynamicPaneDataSources" targetURL="showSubDataSources.action"/></div>