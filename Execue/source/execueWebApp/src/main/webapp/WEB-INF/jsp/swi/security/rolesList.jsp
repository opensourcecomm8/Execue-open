<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="20" align="center" valign="top">
		<div id="errorMessage" style="color: red"><s:fielderror /> <s:actionerror /></div>
		<div id="actionMessage" style="color: red"><s:actionmessage /></div>
		</td>
	</tr>
	
	<tr>
		<td>
		
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				
			<div style="margin-left:2px;margin-top:1px;height: auto; ">
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					id="tableGridMemberInfo">
                    <tr id="tableGridTitle">
						<td width="32%" height="28" bgcolor="#EEEEEE" align="left"><strong>Name</strong></td>
						<td width="46%" bgcolor="#EEEEEE" align="left"><strong>Description</strong></td>
						<td width="8%"  bgcolor="#EEEEEE" align="left"><strong>Edit</strong></td>
						<td width="13%" bgcolor="#EEEEEE" align="left"><strong>Enable/Disable</strong></td>
					</tr>
					<s:iterator value="roles" id="role">
						<tr>
							<td width="33%" ><s:property value="name" /></td>
							<td width="47%"><s:property value="description" /></td>
							<td width="8%" align="center">
							<div id="showEdit<s:property value="id"/>Link"><a
								href="javascript:editRole('<s:property value="id"/>');"
								class="links" id='<s:property value="id"/>'><img
								src="../images/editIcon.gif" alt="Edit Record" 
								height="16" border="0" title="Edit Record" /></a></div>
							<div id="loadingShowEdit<s:property value="id"/>Link"
								style="display: none;"><img
								src="../images/loadingWhite.gif" width="16" height="16"></div>
							</td>
							<td width="12%"><!--<s:if test="%{#status == com.execue.core.common.bean.StatusEnum.ACTIVE}">
									<img src="../images/enabledIcon.gif" alt="Enabled" width="25"
										height="20" border="0" />
								</s:if>
								<s:else>
									<img src="../images/disabledIcon.gif" alt="Disabled" width="25"
										height="20" border="0" />
								</s:else>--> <s:if test="%{#role.systemRole.value=='Y'}">
								<div id="showDelete<s:property value="id"/>Link"><a
									href="javascript:deleteRole('<s:property value="id"/>');"
									class="links" id='<s:property value="id"/>'><img
									src="../images/disabledIcon.gif" alt="Delete Record" 
									height="16" border="0" title="Delete Record" /></a></div>
								<div id="loadingShowDelete<s:property value="id"/>Link"
									style="display: none;"><img
									src="../images/loadingWhite.gif" width="16" ></div>
							</s:if><s:else>&nbsp;</s:else></td>
						</tr>
					</s:iterator>
				</table>
				</div>
				</td>
			</tr>

		</table>
		
		</td>
	</tr>
     <tr><td>&nbsp;</td></tr>
</table>
<script>
$("#closeInnerPaneLink").hide();
$("#addNewRoleLink").show();
	$("#addNewRole").click(function(){
		$("#addNewRoleLink").hide(); 	
		$("#loadingAddNewRoleLink").show(); 							   
		$.get('<s:url action="role"/>', {}, function(data) { 
			$("#loadingAddNewRoleLink").hide(); 
			$("#dynamicPane").empty(); 
			$("#dynamicPane").append(data);
			$("#dynamicPane").show(); 
			$("#addNewUserLink").show();
		});
	});
	function editRole(roleId){
		params = "role.id="+roleId;	
		$("#dynamicPane").createInput('<s:url action="role"/>',"showEdit"+roleId+"Link","loadingShowEdit"+roleId+"Link",params); 
	}
	function deleteRole(roleId){
		 var flag= confirm("Do you want to delete role?");
		 if (flag == true){
		    params = "role.id="+roleId;	
		    $("#dynamicPane").createInput('<s:url action="deleteRole"/>',"showDelete"+roleId+"Link","loadingShowDelete"+roleId+"Link",params);	    
		 }
		}
	
	</script>
