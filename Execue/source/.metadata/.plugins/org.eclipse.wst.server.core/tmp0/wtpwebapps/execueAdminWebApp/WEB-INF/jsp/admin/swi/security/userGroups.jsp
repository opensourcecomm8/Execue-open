<%@taglib uri="/struts-tags" prefix="s"%>
<table width="100%" border="0" cellspacing="0" cellpadding="2">
<tr>
		<td >
		<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
		<div id="actionMessage" style="color: green"><s:actionmessage /></div>
		</td>
	</tr>
</table>

<table width="100%">
<tr>
	<td height="25"  ><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><s:text
			name="execue.user.username" />  : <s:property value="user.firstName" />&nbsp;&nbsp;<s:property value="user.lastName" /></td>
    <td align="right">&nbsp;</td>
  </tr>
</table>
</td>
</tr>
<tr><td>

<form id="userGroupForm">
<table width="30%" border="0" align="center" cellpadding="3"
	cellspacing="0">
    
    <tr>
		<td width="41%" align="right" class="fieldNames">&nbsp;</td>
		<td width="3%">&nbsp;</td>
		<!-- td><s:property value="user.lastName" /></td-->
		<!--s:select name="user.id" value="user.id" id="user.id" list="users"
			listKey="id" listValue="firstName" id='userId'/-->
	</tr>
    
    
	<tr>
		<td width="45%" align="center"><strong><span class="cellTExtCenter">
		  <s:text
			name="execue.group.pageHeading" />
		</span></strong></td>
		<td width="8%" align="center">&nbsp;</td>
		<td width="47%" align="center"><strong><span class="cellTExtCenter">
		  <s:text
			name="execue.groups.assigned" /> 
		  </span></strong></td>
		<td width="11%" align="center" class="fieldNames">&nbsp;
		
		</td>
	</tr>

	
	<tr>
	<tr>
		<td align="center"><s:select list="groups" listKey="id"
			listValue="name" size="7" id="Left" multiple="true" /></td>
		<td align="center">
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td><a href="#" id="&gt;&gt"><img
					src="../images/admin/rightAllArrowButton.gif" width="29" height="22"
					alt="Move All to right" border="0" title="Move All to right" /></a></td>
			</tr>
			<tr>
				<td><a href="#" id="&gt"><img
					src="../images/admin/rightArrowButton.gif" width="29" height="22"
					alt="Move to right" border="0" title="Move to right" /></a></td>
			</tr>
			<tr>
				<td><a href="#" id="&lt"><img
					src="../images/admin/leftArrowButton.gif" width="29" height="22"
					alt="Move to left" border="0" title="Move to left" /></a></td>
			</tr>
			<tr>
				<td><a href="#" id="&lt;&lt"><img
					src="../images/leftAllArrowButton.gif" width="29" height="22"
					alt="Move All to left" border="0" title="Move All to left " /></a></td>
			</tr>
		</table>
		</td>
		<td align="center"><s:select name="selectedGroups"
			list="user.groups" listKey="id" listValue="name" size="7" id="Right"
			multiple="true" /></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>

</table>
<s:hidden name="user.id" cssClass="textBox" id="userId" /></form></td></tr>
<tr>
	<td height="35" colspan="3" align="center"><input type="button" class="buttonSize108"
		name="imageField" id="imageField"
		value="<s:text name='execue.group.assignGroup' />" /> <input type="button" class="buttonSize90" name="imageField2"
		id="imageField2" value="<s:text name='execue.global.resetButton.name' />" /></td>
</tr>
</table>
<script>


/*$("#userID").change(function(){
obj=$("#userID").val();
	$.get('<s:url action="showUserGroups"/>', {userId:obj}, function(data) {
$("#innerPane").empty();
$("#innerPane").fadeIn("fast");    
$("#innerPane").append(data);
$("#innerPane").show();  
}); 
});*/
$("#closeInnerPaneLink").show();
$("#addNewUserLink").hide();
$().ready(function(){
  	$("select[name='selectedGroups'] option").attr("selected" ,"true"); 	
  });
$("#closeInnerPane").click(function(){
	  $("#dynamicPane").createInput('<s:url action="listUsers"/>',"closeInnerPane","loadingCloseInnerPaneLink"); //passing URL, user clicked link, loader div
	});
$("a").click(function(){
var inputControl=this.id;//$("id").val();
Move(inputControl);
});
function Move(inputControl)
{ 
  var from, to;
  var bAll = false;
  var left = document.getElementById("Left");
  var right = document.getElementById("Right"); 
 
  switch (inputControl)
  {
  case '<<':
    bAll = true; 
  case '<':
    from = right; to = left;
    break;
  case '>>':
    bAll = true;
    $("select[name='selectedGroups'] option").attr("selected" ,"true"); 
    // Fall through
  case '>':
    from = left; to = right;
    $("select[name='selectedGroups'] option").attr("selected" ,"true"); 
    break;
  default:
   // alert("Check your HTML!");
  }
  for (var i = from.length - 1; i >= 0; i--)
  {
    var o = from.options[i];
    if (bAll || o.selected)
    {
      from.remove(i);
      try
      {
        to.add(o, null);  
        
      }
      catch (e)
      {
        to.add(o); 
      }
    }
  }
  }
  
$("#imageField").click(function(){
$("select[name='selectedGroups'] option").attr("selected" ,"true");
  	 var right = document.getElementById("Right");
  	 count=0;
  	 var groups = new Array(right.length);  
  	 if(right.length!=0){	  
	  	/* for(var i=0;i<right.length;i++){
	  	 	if(right.options[i].selected){
		  	 	groups[i]=right.options[i].text;
		  	 	//alert("value"+groups[i]);	
		  	 	count++;
	  	 	}
	  	 }
	  	 alert( "length of group:"+groups.length);
	  	 if (count==0){
			 alert("Select at least one group From Assigned Gropus List:");
			 return false;  
		 }  */
  	 }else{  	 	        	        
            alert("Assigned Gropus list should not empty:");
            return false;          
     } 
	$.post('<s:url action="assignGroups"/>', $('#userGroupForm').serialize(), function(data) {
		$("#dynamicPane").empty();
		$("#dynamicPane").fadeIn("fast");    
		$("#dynamicPane").append(data);
		$("#dynamicPane").show();  
	});   
});
$("#imageField2").click(function(){	
	params = "user.id="+$("#userId").val();	
	$("#dynamicPane").createInput('<s:url action="showUserGroups"/>',"","",params); //passing URL, user clicked link, loader div
});

</script>
