<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>

<table width="480px" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="20" align="center" valign="top">
		<div id="errorMessage" style="color: red"><s:fielderror /> <s:actionerror /></div>
		<div id="actionMessage" style="color: green"><s:actionmessage /></div>
		</td>
	</tr>
	
	<tr>
		<td>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
			<tr>
				<td>
				
				<div style="margin-left:2px;margin-top:1px;height: 300px; overflow: hidden; overflow-x: hidden;">
				<form id="AssetGridFormId">	
				  <table width="100%" border="0" cellpadding="0" cellspacing="1"
					id="assetGridInfo">
						  
					<tr>					  
						<s:iterator value="assets" status="even_odd">
							<td>							
							       <s:if test="hasAclPermission.value=='Y'">
									  <input name="assets" desc="" type="checkbox" checked="checked" value='<s:property value="id"/>'/>
									</s:if>
									<s:else>
									   <input name="assets" desc="" type="checkbox" value='<s:property value="id"/>'/>
									</s:else>
							</td>
							<td><s:property value="name" /></td>
                             <s:if test="(#even_odd.index+1)%4==0">
                                  <tr></tr>
                            </s:if>
						</s:iterator>
					</tr>
				</table>
				</form>
				</div>
				 <div id="permittedAssetIdsDiv">                
               			 <s:iterator value="permittedAssetIds" status="even_odd">
							<input name="permittedAssetIds" type="hidden" value='<s:property/>' />
						</s:iterator>
                </div>
				</td>
			</tr>
            <tr>
				<td height="35" colspan="3" align="left">
				
				<input type="button" class="buttonSize108"
					name="imageField" id="submitButton"
					value="Confirm" onclick="javascript:saveObjectSecurity('Asset');" /> 
					<input id="submitButtonLoader" type="button" style="display: none"
					  class="singleButtonLoader" disabled="disabled" value="Confirm" />
					<input type="button" class="buttonSize108" name="imageField2"
					id="imageField2" value="Reset" onclick="javascript:reset();" />
					<div id="paginationDiv2" style="float:right;" ><pg:sdxSecurityPage targetURL="getAssetsForApplication.action?selectedApplicationId=${selectedApplicationId}&selectedRoleName=${selectedRoleName}" targetPane="dynamicPane"/></div></td>
			</tr>
		</table>
		
		</td>
	</tr>
  <s:hidden name="selectedRoleName" id="selectedRoleId"></s:hidden> 
  <s:hidden name="selectedApplicationId" id="selectedApplicationId"></s:hidden> 
</table>


<script>
highLightText(1);
</script>