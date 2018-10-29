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
						<td width="34%" height="28" bgcolor="#EEEEEE" align="left"><strong><s:text
							name="execue.group.name" /></strong></td>
						<td width="32%" bgcolor="#EEEEEE" align="left"><strong><s:text
							name="execue.group.description" /></strong>&nbsp;</td>
						
						<td width="12%" bgcolor="#EEEEEE" align="left"><strong><s:text
							name="execue.group.roles" /></strong></td>
                            <td width="8%" align="left" bgcolor="#EEEEEE" ><strong>Edit</strong></td>
						<td width="13%" bgcolor="#EEEEEE" align="left"><strong>Enable/Disable</strong></td>
					</tr>
                    
					<s:iterator value="groups" id="group">
						<tr>
							<td ><s:property value="name" /></td>
							<td ><s:property value="description" /></td>
							<td  bgcolor="#fffffff">

							<div id="showRoles<s:property value="id"/>Link"><a
								href="javascript:showRoles('<s:property value="id"/>');"
								class="links" id="<s:property value="id"/>"><img
								src="../images/admin/roleIcon.gif" alt="show groupRoles" 
								height="16" border="0" title="show groupRoles" /></a></div>
							<div id="loadingShowRoles<s:property value="id"/>Link"
								style="display: none;"><img
								src="../images/admin/loadingWhite.gif" width="16"></div>
							</td>
							<td  align="center">
							<div id="showEdit<s:property value="id"/>Link"><a
								href="javascript:editGroup('<s:property value="id"/>');"
								class="links" id='<s:property value="id"/>'><img
								src="../images/admin/editIcon.gif" alt="Edit Record" 
								height="16" border="0" title="Edit Record" /></a></div>
							<div id="loadingShowEdit<s:property value="id"/>Link"
								style="display: none;"><img
								src="../images/admin/loadingWhite.gif"  height="16"></div>
							</td>
							<td ><s:if test='#group.status.value == "A"'>
								<div id="showStatus<s:property value="id"/>Link"><a
									href="javascript:showGroupStatus('<s:property value="id"/>');">
								<img src="../images/admin/enabledIcon.gif" alt="Enabled"
									height="16" border="0" title="Enabled" /></a></div>
								<div id="loadingShowStatus<s:property value="id"/>Link"
									style="display: none;"><img
									src="../images/admin/loadingWhite.gif"  height="16"></div>
							</s:if> <s:else>
								<div id="showStatus<s:property value="id"/>Link"><a
									href="javascript:showGroupStatus('<s:property value="id"/>');">
								<img src="../images/admin/disabledIcon.gif" alt="Disabled"
									height="16" border="0" title="Disabled" /></a></div>
								<div id="loadingShowStatus<s:property value="id"/>Link"
									style="display: none;"><img
									src="../images/admin/loadingWhite.gif"  height="16"></div>
							</s:else></td>
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
$("#addNewGroupLink").show();
function showRoles(groupId){
	params = "group.id="+groupId;
	$("#dynamicPane").createInput('<s:url action="showGroupRoles"/>',"showRoles"+groupId+"Link","loadingShowRoles"+groupId+"Link",params); //passing URL, user clicked link, loader div
                
	}
	$("#addNewGroup").click(function(){
		$("#addNewGroupLink").hide(); 	
		$("#loadingAddNewGroupLink").show(); 							   
		$.get('<s:url action="saveGroup!input"/>', {}, function(data) { 
			$("#loadingAddNewGroupLink").hide(); 
			$("#dynamicPane").empty(); 
			$("#dynamicPane").append(data);
			$("#dynamicPane").show(); 
			//$("#addNewGroupLink").show();
		});
	});
	function editGroup(groupId){
		/*obj =groupId;
		$("#showEdit"+obj+"Link").hide(); 	
		$("#loadingShowEdit"+obj+"Link").show(); 	
		params = "group.id="+groupId;						   
		$.get('<s:url action="group"/>', params, function(data) { 
			$("#loadingShowEdit"+obj+"Link").hide(); 
			$("#dynamicPane").empty(); 
			$("#dynamicPane").append(data);
			$("#dynamicPane").show(); 
			//$("#showEdit"+obj+"Link").show();
		
		});*/
		params = "group.id="+groupId;
		$("#addNewGroupLink").hide(); 	
		$("#dynamicPane").createInput('<s:url action="saveGroup"/>',"showEdit"+groupId+"Link","loadingShowEdit"+groupId+"Link",params); //passing URL, user clicked link, loader div
		
	}
	function showGroupStatus(groupId){
		 var flag= confirm("Do you want change the status?");
		 if (flag){
		    params = "group.id="+groupId;		   
		   $("#dynamicPane").createInput('<s:url action="updateGroupStatus"/>',"showStatus"+groupId+"Link","loadingShowStatus"+groupId+"Link",params);	    
		 }
	}
	</script>
