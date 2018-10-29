<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr height="40px">
		<td height="20" align="left" valign="top">
		<div id="errorMessage" style="color: red"><s:fielderror /> <s:actionerror /></div>
		<div id="actionMessage" style="color: green"><s:actionmessage /></div>
		</td>
	</tr>

	<tr>
		<td>

		<table width="100%" border="0" cellspacing="0" cellpadding="0"
			align="center">
			<tr>
				<td>

				<div
					style="margin-left: 2px; margin-top: 1px; height: 300px; overflow: hidden; overflow-x: hidden;">
				<form id="AssetGridFormId">
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					id="assetGridInfo">

					<tr height="100px">
						<td><STRONG> <s:text name="execue.hierarchy.level.name"/></STRONG><s:text name="execue.global.mandatory"></s:text></td>
						  <td> <s:textfield name="hierarchy.name" id="hierarchyNameId" cssClass="textBox"
							maxlength="255" />
						   <s:hidden name="hierarchy.id" id="hierarchyId"></s:hidden>
					    </td>
					</tr>					
					<tr>
						<td width="20%">						 
						<div style="height:20px;"><strong><s:text name="execue.hierarchy.available.demensions"></s:text> </strong></div> 
						<select id="dimensions" name="dimensions"
							style="height: 150px; width: 250px;" multiple="multiple">
							<s:iterator value="dimensions">
								<option value='<s:property value="id"/>' bedId='<s:property value="bedId"/>'><s:property
									value="displayName" /></option>
							</s:iterator>
						</select></td>
						<td align="center"><a href="javascript:moveRight();"><img
							height="22" border="0" title="Move to right" alt="Move to right"
							src="../images/moveRightButton2.gif"></a><br/><a
							href="javascript:moveLeft();"><img height="22"
							style="margin-top: 10px;" border="0" title="Move to left"
							alt="Move to left" src="../images/moveLeftButton.gif"></a></td>
						<td width="40%">
						 <div style="height:20px;"><strong><s:text name="execue.hierarchy.hierarchy.definitions"></s:text></strong></div> 						
						<select id="selectedHierarchyDefinitions"
							name="selectedHierarchyDefinitions"
							style="height: 150px; width: 250px;" multiple="multiple">
							<s:iterator value="existingHierarchyDefinitions">
								<option value='<s:property value="id"/>' bedId='<s:property value="bedId"/>' > ${displayName}</option>
							</s:iterator>
						</select></td>
					</tr>
				</table>
				</form>
				</div>
				</td>
			</tr>
			<tr>
				<td height="35" colspan="3" align="left"><input type="button"
					class="singleButton" name="imageField" id="submitButton"
					value="Confirm" onclick="javascript:saveHierarchyDefinitions();" />
				<input id="submitButtonLoader" type="button" style="display: none"
					class="singleButtonLoader" disabled="disabled" value="Confirm" />
				<input type="button" class="singleButton" name="imageField2"
					id="imageField2" value="Reset" onclick="javascript:reset();" />
			    <s:if test="hierarchy.id != null">
				<input type="button" class="singleButton" name="imageField3"
					id="deleteButton" value="Delete" onclick="javascript:deleteHierarchy();" />
			    <input id="deleteButtonLoader" type="button" style="display: none"
					class="singleButtonLoader" disabled="disabled" value="Delete" />
				</s:if>
					</td>
			</tr>
		</table>

		</td>
	</tr>
</table>


<script>
	$(document).ready(function() {	 
	 $("#hierarchyNameId").focus(); 
	});
 
highLightText(1);
</script>