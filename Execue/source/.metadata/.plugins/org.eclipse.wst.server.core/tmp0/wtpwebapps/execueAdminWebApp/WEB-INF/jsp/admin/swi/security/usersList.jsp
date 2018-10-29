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
				
				<div style="margin-left:2px;margin-top:1px;height: auto; overflow: hidden; overflow-x: hidden;">
				<table width="100%" border="0" cellpadding="0" cellspacing="1"
					id="tableGridMemberInfo">
                    
                    <tr id="tableGridTitle">
						<td width="17%" height="28" bgcolor="#EEEEEE" align="left"><strong><s:text
							name="execue.user.username" /></strong></td>
						<td width="14%" bgcolor="#EEEEEE" align="left"><strong><s:text
							name="execue.user.firstName" /></strong></td>
						<td width="15%" bgcolor="#EEEEEE" align="left"><strong><s:text
							name="execue.user.lastName" /></strong></td>
						<td width="25%" bgcolor="#EEEEEE" align="left"><strong><s:text
							name="execue.user.emailId" /></strong></td>
						
						<td width="10%" bgcolor="#EEEEEE">
                       
                       <strong> <s:text name="execue.user.groups" /></strong></td>
                            <td width="8%"  bgcolor="#EEEEEE" align="left"><strong>Edit</strong></td>
						<td width="12%" bgcolor="#EEEEEE" align="left"><strong>Enable/Disable</strong></td>
                        
					</tr>
                    
					<s:iterator value="users" id="user">
						<tr>
							<td width="15%"><s:property value="username" /></td>
							<td width="15%"><s:property value="firstName" /></td>
							<td width="15%"><s:property value="lastName" /></td>
							<td width="26%">&nbsp;<s:property value="emailId" /></td>							
							<td width="10%" bgcolor="#FFFFFF">
							<div id="showGroups<s:property value="id"/>Link"><a
								href="javascript:showGroups('<s:property value="id"/>');"
								class="links" id='<s:property value="id"/>'><img
								src="../images/admin/groupsIcon.gif" alt="show userGroups" width="20"
								height="18" border="0" title="show userGroups" /></a></div>
							<div id="loadingShowGroups<s:property value="id"/>Link"
								style="display: none;"><img
								src="../images/admin/loadingWhite.gif"  height="20"></div>
							</td>

							<td width="8%" align="center">
							<div id="showEdit<s:property value="id"/>Link"><a
								href="javascript:editUser('<s:property value="id"/>');"
								class="links" id='<s:property value="id"/>'><img
								src="../images/admin/editIcon.gif" alt="Edit Record" 
								height="16" border="0" title="Edit Record" /></a></div>
							<div id="loadingShowEdit<s:property value="id"/>Link"
								style="display: none;"><img
								src="../images/admin/loadingWhite.gif"  height="16"></div>
							</td>
							<!--td width="46"><s:if
								test="%{#status == com.execue.core.common.bean.security.UserStatus.ACTIVE}">
								<a href="javascript:showUserStatus('<s:property value="id"/>');">
								<img src="../images/admin/enabledIcon.gif" alt="Enabled" width="25"
									height="20" border="0" title="Enabled" /></a>
							</s:if> <s:else>
								<a href="javascript:showUserStatus('<s:property value="id"/>');">
								<img src="../images/admin/disabledIcon.gif" alt="Disabled" width="25"
									height="20" border="0" title="Disabled" />
							</s:else></td-->
							<td width="12%"><s:if
								test='%{#user.status.value == "A"}'>
								<div id="showStatus<s:property value="id"/>Link">
								<a href="javascript:showUserStatus('<s:property value="id"/>');">
								<img src="../images/admin/enabledIcon.gif" alt="Enabled" 
									height="16" border="0" title="Enabled" /></a></div>
									<div id="loadingShowStatus<s:property value="id"/>Link"
								style="display: none;"><img
								src="../images/admin/loadingWhite.gif"  height="20"></div>
							</s:if> <s:else>
								<div id="showStatus<s:property value="id"/>Link">
								<a href="javascript:showUserStatus('<s:property value="id"/>');">
								<img src="../images/admin/disabledIcon.gif" alt="Disabled" 
									height="16" border="0" title="Disabled" /></a></div>
								<div id="loadingShowStatus<s:property value="id"/>Link"
								style="display: none;"><img
								src="../images/admin/loadingWhite.gif" width="25" height="25"></div>	
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
     <tr><td><div id="paginationDiv2" style="float:right;" ><pg:paginateResults/></div></td></tr>
</table>
<script>

	function showGroups(userId){	
		params = "user.id="+userId;
		$("#dynamicPane").createInput('<s:url action="showUserGroups"/>',"showGroups"+userId+"Link","loadingShowGroups"+userId+"Link",params); //passing URL, user clicked link, loader div
    }                
	$("#addNewUser").click(function(){
		$("#addNewUserLink").hide();
		
		$("#dynamicPane").createInput('<s:url action="saveUser"/>',"addNewUser","loadingAddNewUserLink"); //passing URL, user clicked link, loader div
		
	});	
	
	function editUser(userId){
	
/*	$("#showEdit"+obj+"Link").hide(); 	
	$("#loadingShowEdit"+obj+"Link").show(); 	
	params = "user.id="+userId;						   
	$.get('<s:url action="user!edit"/>', params, function(data) { 
	$("#loadingShowEdit"+obj+"Link").hide(); 
	$("#dynamicPane").empty(); 
	$("#dynamicPane").append(data);
	$("#dynamicPane").show(); 
	//$("#showEdit"+obj+"Link").show();
	
	});*/
	params = "user.id="+userId;	
	$("#addNewUserLink").hide();
	$("#dynamicPane").createInput('<s:url action="saveUser"/>',"showEdit"+userId+"Link","loadingShowEdit"+userId+"Link",params); //passing URL, user clicked link, loader div
	}
	function showUserStatus(userId){
		 var flag= confirm("Do you want change the status?");
		 if (flag){
		    params = "user.id="+userId;	
			var $img=$("#showStatus"+userId+"Link").find("img"); 
			$img.attr("src","../images/admin/loadingBlue.gif");
		   $.get('updateUserStatus.action',{'user.id':userId,'requestedPage':requestedPage,'paginationType':'usersPagination','dataSource.name':'dummy'},function(data){ 
			
					 $.get('showUsersSubList.action',{'requestedPage':requestedPage,'paginationType':'usersPagination','dataSource.name':'dummy'},function(data1){ 
					$("#dynamicPane").empty().html(data1);																																																																				
																																																																																																																														
																																																															            });
		});	    
		 }
	}
	
	
</script>
