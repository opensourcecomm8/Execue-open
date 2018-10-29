<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<s:form id="createCubeForm" >
	<table width="80%" border="0" align="center" cellpadding="2"
		cellspacing="0">
		<tr>
			<td colspan="3">
			<div id="errorMessageId" style="color: red">
			  <s:iterator value="uiStatus.errorMessages">
			   <li><s:property/></li>
			  </s:iterator>
			</div>
			 <div id="actionMessageId" style="color: green;white-space: nowrap"></div>			
			</td>
		</tr>
	
		<tr>
			<td width="32%" class="fieldNames"><s:text name="execue.global.name"></s:text><span class='fontRed'>*</span></td>
			<td width="68%"><s:textfield name="targetAsset.displayName" id="targetAssetNameId"
				cssClass="textBox" /></td>
		</tr>
	<!-- <tr>
			<td class="fieldNames"><s:text name="execue.global.displayName"></s:text><span class='fontRed'>*</span></td>
			<td><s:textfield name="targetAsset.displayName" id="targetAssetDispNameId"
				cssClass="textBox" /></td>
		</tr>-->
		<tr>
			<td class="fieldNames"><s:text name="execue.global.description"></s:text></td>
			<td><s:textarea name="targetAsset.description" id="targetAssetDesId" cssStyle="width:150px;" />
				<s:hidden name="existingAssetId"/></td>					 		
		</tr>		
		<tr>
			<td></td>
			<td><input type="button" class="buttonSize160" onclick="javascript:createCube();"   id="createCubeButton" value="Create Cube" />			
			</td>
		</tr>
	</table>
</s:form>
<script type="text/javascript">
$("#createCube_targetAsset_name").focus();
function createCube(){
 var name=$("#targetAssetNameId").val();
 var dispName=$("#targetAssetDispNameId").val();
 //var des=$("#targetAssetDesId").val();
  if(name=="" || dispName==""){
     $("#errorMessageId").empty().append("Please enter required fields");
      return false;
  }else{
	$("#createCubeButton").removeClass("buttonSize160");		
	$("#createCubeButton").addClass("buttonLoaderSize160"); 
	  $.post("swi/createCube.action",$('#createCubeForm').serialize(),function(data) {
	    /*$("#targetAssetDynaminPane").fadeIn("fast");
	    $("#targetAssetDynaminPane").empty();
	    $('#createCubeButton').removeAttr('onclick');      
	    $("#targetAssetDynaminPane").append(data);*/	    
	    if(data.status){
	        $("#errorMessageId").empty();
			$("#actionMessageId").empty().append("Cube creation request is in process. Please check the <a href='getJobStatus.action?jobRequest.id="+data.jobRequestId+"'>Status </a>");	
		    $('#createCubeButton').removeAttr('onclick');      
		}else{
		    showErrorMessages(data.errorMessages);
		}
	    $("#createCubeButton").addClass("buttonSize160");		
	    $("#createCubeButton").removeClass("buttonLoaderSize160"); 	    
	    $("#targetAssetDynaminPane").show();
	  }); 
  }
}

function showErrorMessages(errorMessages){
    $.each(errorMessages, function () {
			$("#errorMessageId").empty().append("<li>"+this+"<\li>");	    
      });
}
</script>

