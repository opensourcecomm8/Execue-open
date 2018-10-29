<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="role==null || role.id == null">
	<s:set name="mode" value="%{'add'}" />
</s:if>
<s:else>
	<s:set name="mode" value="%{'update'}" />
</s:else>

<div class="innerPane" style="width: 100%;">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="97%" height="30" class="tableSubHeading" style="padding-left:20px;"><strong>
		<s:if test="#mode == 'add'">
			<s:text name="execue.role.pageHeading.add" />
		</s:if> <s:else>
			<s:text name="execue.role.pageHeading.update" />
		</s:else> </strong></td>
		<td width="11%" align="center" class="fieldNames">&nbsp;
		
		</td>
	</tr>
	<tr>
		<td colspan="3">
		<div id="errorMessage" style="color: red"><s:fielderror /> <s:actionerror /></div>
		<div id="actionMessage" style="color: red"><s:actionmessage /></div>
		</td>
	</tr>
	<tr>
		<td colspan="2"><s:form id="createRole"
			action="role!SecurityRole">
			<table width="50%" border="0" align="left" cellpadding="2"
				cellspacing="0" style="margin-left:20px">
				<tr>
					<td width="23%" class="fieldNames"><s:text name="execue.role.name" /><span class='fontRed'>*</span></td>
					<s:if test="#mode=='add'">
						<td width="77%"><s:textfield name="role.name"
							cssClass="textBox" id="roleName" /></td>
					</s:if>
					<s:else>
						<td width="77%"><s:textfield name="role.name"
							cssClass="textBoxDisabled" id="rolename" readonly="true" /></td>
					</s:else>

				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.role.description" /><span class='fontRed'>*</span></td>
					<td><s:textfield name="role.description" cssClass="textBox"
						id="role.description" /></td>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.role.status" /></td>
					<td><s:select name="role.status" id="statusType"
						list="statusTypes" /></td>
				</tr>
				<tr>
					
					<td height="40" colspan="2" align="left"><s:if test="#mode=='add'">
						<input type="submit" class="buttonSize108" name="imageField" id="imageField"
							value="<s:text name='execue.role.createRole.button' />" />
					</s:if> <s:else>
						<input type="submit" class="buttonSize108" name="imageField" id="imageField"
							value="<s:text name='execue.role.updateRole.button' />" />
					</s:else> <s:if test="#mode=='add'">
						<!-- input type="image" name="imageField2" id="imageField2"
						src="../images/resetButton.gif" /-->
						<input type="button"	class="buttonSize90" name="imageField2" id="imageField2"
							value="<s:text name='execue.global.resetButton.name' />" />
					</s:if></td>
				</tr>
			</table>
			<s:hidden name="role.id" cssClass="textBox" id="role.id" />
		</s:form></td>
	</tr>
</table>
</div>

<script>
$("#closeInnerPaneLink").show();
$("#addNewRoleLink").hide();
$(document).ready(function() { 
	/*$("#closeInnerPane").click(function(){									
		$("#closeInnerPaneLink").hide(); 	
		$("#loadingCloseInnerPaneLink").show(); 
		$.get('<s:url action="showRoles"/>', function(data) { 									
			$("#loadingCloseInnerPaneLink").hide(); 									
			$("#closeInnerPaneLink").show(); 
			$("#dynamicPane").empty(); 
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();  
		});
	});*/
	$("#rolename").css("width","152px");
	$("#statusType").css("width","154px");
	$("#closeInnerPane").click(function(){
	  $("#dynamicPane").createInput('<s:url action="listRoles"/>',"closeInnerPane","loadingCloseInnerPaneLink"); //passing URL, user clicked link, loader div
	});
	$("#imageField").click(function(){
		/*$("#loadingCloseInnerPaneLink").show(); 
		 var obj = $('#createGroup').serialize()
		$.get('<s:url action="role!save"/>', $('#createRole').serialize(), function(data) { 										
			$("#loadingCloseInnerPaneLink").hide(); 									
			$("#closeInnerPaneLink").show(); 	
			$("#dynamicPane").empty(); 
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();  
		});
	    return false;*/
	    $("#dynamicPane").createInput('<s:url action="role!save"/>',"","",$('#createRole').serialize()); //passing URL, user clicked link, loader div
         return false;
	});
	$("#imageField2").click(function(){	
		$("#createRole input[type='text'],#createRole input[type='password']").val("");
	});
	$('#roleName').select().focus();
	$("#rolename").bind("focus", noCursor);
		function noCursor(){$(this).trigger("blur");}
});

</script>
