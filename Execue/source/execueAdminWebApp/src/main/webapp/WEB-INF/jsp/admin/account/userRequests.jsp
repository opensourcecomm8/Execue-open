
<!-- This is page is body part of the tiles -->
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="left" cellpadding="0"
	cellspacing="0">
	<tr>
		<td valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.user.manageRequests" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td align="center">
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText" align="left">

				<table width="100%" border="0" cellspacing="0" cellpadding="0">


					<tr>
						<td height="25"><s:text
							name="execue.users.manageRequestsDescription" /></td>

					</tr>
					<tr>
						<td height="25"><!-- select name="requestType">
                      <option value="1">Advanced Options</option>
                      <option value="2">Demo Requests</option>
                    </select>--> <s:select list="userRequestTypes"
							id="userRequestTypeId" name="userRequestType" listValue="description">
						</s:select></td>

					</tr>

				</table>


				</td>
			</tr>
			<tr>
				<td valign="top" background="../images/blueLine.jpg"><img
					src="../images/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="72%">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">

					<tr>
						<td height="300" width="16%" align="center" valign="top">
						<div id="leftDynamicPane">
						<table width="100%" border="0" cellspacing="4" cellpadding="0">
							<tr>
								<td><a class="links" href="#" id="newRequestId"><s:text name="execue.advOptions.new"/></a></td>
							</tr>
							<tr>
								<td><a class="links" href="#" id="acceptedRequestId"><s:text name="execue.advOptions.accepted"/></a></td>
							</tr>
							<tr>
								<td><a class="links" href="#" id="rejectedRequestId"><s:text name="execue.advOptions.rejected"/></a></td>
							</tr>
						</table>

						</div>
						</td>
						<td height="300" align="center" width="3%"
							class="blueLineVeritical">&nbsp;</td>
						<td height="300" width="81%" align="center" valign="top">
						<div id="dynamicPane"></div>
						</td>
					</tr>
					<tr>
						<td>&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
<script>

$("#acceptedRequestId").click(function(){
   showUserRequests("YES");
});
$("#rejectedRequestId").click(function(){
   showUserRequests("NO");
});
$("#newRequestId").click(function(){
   showUserRequests("");
});
$("#userRequestTypeId").change(function(){
  $("#dynamicPane").empty();
});

  function showUserRequests(acceptRejectType){  
     var userRequestType=$("#userRequestTypeId option:selected").val();
     if(acceptRejectType != ''){     
        showDetails("showUserRequests.action?userRequestType="+userRequestType+"&acceptRejectType="+acceptRejectType,"dynamicPane","get");
     }else{
         showDetails("showUserRequests.action?userRequestType="+userRequestType,"dynamicPane","get");
     }    
  }
  function updateUserRequests(acceptRejectRequest){ 
      var $userRequestType=$("#userRequestTypeId option:selected").val(); 
      var $request="";
      var length= $("#userRequestForm input[type='checkbox']:checked").length;
	    if(length==0){
	      $("#errorMessage").empty().append("Please select atleast one record");
	      return false;
	    }
		$.each($("#userRequestForm input[type='checkbox']:checked"),function(k,v){
		var $id=$(this).attr("id");
		var $comments=$("textarea[textAreaId='"+$id+"']").val();
		var $val=acceptRejectRequest;	
		$request=$request+"&userRequestType="+$userRequestType+"&acceptRejectType="+$val+"&userRequests["+k+"].id="+$id+"&userRequests["+k+"].adminComment="+$comments+"&userRequests["+k+"].acceptRejectRequest="+$val;
	});
	 $.ajax({url:"updateUserRequests.action",
			    data:$request,
			    success:function(data){$("#dynamicPane").empty().html(data);},
			    type:"post"
		}); 
  }
</script>