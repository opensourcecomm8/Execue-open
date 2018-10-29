<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:if test="group==null || group.id == null">
	<s:set name="mode" value="%{'add'}" />
</s:if>
<s:else>
	<s:set name="mode" value="%{'update'}" />
</s:else>

<div class="innerPane" style="width: 99%;">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td width="100%" height="30" class="tableSubHeading" style="padding-left:20px;"><strong>
		<s:if test="#mode == 'add'">
			<s:text name="execue.group.pageHeading.add" />
		</s:if> <s:else>
			<s:text name="execue.group.pageHeading.update" />
		</s:else> </strong></td>
		<td width="3%" align="center" class="fieldNames">&nbsp;
		
		</td>
	</tr>
	<tr>
		<td colspan="3" style="padding-left:20px;">
		<div id="errorMessage" style="color: red"><s:fielderror /> <s:actionerror /></div>
		<div id="actionMessage" style="color: red"><s:actionmessage /></div>
		</td>
	</tr>
	<tr>
		<td colspan="2"><s:form id="createGroup"
			action="group!SecurityGroup">
			<table width="50%" border="0" align="left" cellpadding="2"
				cellspacing="0" style="margin-left:20px;">
				<tr>
					<td width="23%" class="fieldNames"><s:text name="execue.group.name" /><span class='fontRed'>*</span></td>
					<s:if test="#mode=='add'">
						<td width="77%"><s:textfield name="group.name"
							cssClass="textBox" id="groupname" /></td>
					</s:if>
					<s:else>
						<td width="77%"><s:textfield name="group.name"
							cssClass="textBoxDisabled" id="group.name" readonly="true" id="gname"/></td>
					</s:else>

				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.group.description" /><span class='fontRed'>*</span></td>
					<td><s:textfield name="group.description" cssClass="textBox"
						id="group.description" /></td>
				</tr>
				<tr>
					<td class="fieldNames"><s:text name="execue.group.status" /></td>
					<td><s:select name="group.status" id="statusType"
						list="statusTypes" /></td>
				</tr>
				<tr>
					
					<td height="40" colspan="2" align="left"><s:if test="#mode=='add'">
						<input type="button" class="buttonSize108" name="imageField" id="imageField"
							value="<s:text name='execue.group.createGroup.button' />" />
					</s:if> <s:else>
						<input type="button" class="buttonSize108" name="imageField" id="imageField"
							value="<s:text name='execue.group.updateGroup.button' />" />
					</s:else> <s:if test="#mode=='add'">
						<!--input type="image" name="imageField2" id="imageField2"
							src="../images/resetButton.gif" /-->
						<input type="button" class="buttonSize90" name="imageField2" id="imageField2"
							value="<s:text name='execue.global.resetButton.name' />" />
					</s:if></td>
				</tr>
			</table>
			<s:hidden name="group.id" cssClass="textBox" id="group.id" />
		</s:form></td>
	</tr>
</table>
</div>

<script>
$("#closeInnerPaneLink").show();
$("#addNewGroupLink").hide();
$(document).ready(function() { 
	/*$("#closeInnerPane").click(function(){									
		$("#closeInnerPaneLink").hide(); 	
		$("#loadingCloseInnerPaneLink").show(); 
		$.get('<s:url action="showGroups"/>', function(data) { 									
			$("#loadingCloseInnerPaneLink").hide(); 									
			$("#closeInnerPaneLink").show(); 
			$("#dynamicPane").empty(); 
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();  
		});
	});*/
	var eMessage=$("#errorMessage").html();
	eMessage=eMessage.replace(/\./gi," ");
	$("#errorMessage").html(eMessage);
	$("#gname").css("width","152px");
	$("#statusType").css("width","154px");
	$("#closeInnerPane").click(function(){
	  $("#dynamicPane").createInput('<s:url action="listGroups"/>',"closeInnerPane","loadingCloseInnerPaneLink"); //passing URL, user clicked link, loader div
	});
	$("#imageField").click(function(){
	/*$("#loadingCloseInnerPaneLink").show(); 
	 var obj = $('#createGroup').serialize()
		$.get('<s:url action="group!save"/>', $('#createGroup').serialize(), function(data) { 										
			$("#loadingCloseInnerPaneLink").hide(); 									
			$("#closeInnerPaneLink").show(); 	
			$("#dynamicPane").empty(); 
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();  
		});*/
		$("#dynamicPane").createInput('<s:url action="saveGroup!save"/>',"","",$('#createGroup').serialize()); //passing URL, user clicked link, loader div
	    return false;
	});
	$("#imageField2").click(function(){		
		//$("#createGroup").find("input[type!='submit'], input[type!='reset']").val("");
		$("#createGroup input[type='text']").val("");
		
		$("#createGroup #groupname").focus();
	
	});
	$('#groupname').select().focus();
	$("#gname").bind("focus", noCursor);
		function noCursor(){$(this).trigger("blur");}
});
</script>
