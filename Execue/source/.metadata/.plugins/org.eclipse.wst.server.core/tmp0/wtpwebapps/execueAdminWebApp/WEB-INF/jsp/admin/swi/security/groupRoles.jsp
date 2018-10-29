<%@taglib uri="/struts-tags" prefix="s"%>
<table width="100%">
<tr>
	<td height="25"  >
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><s:text	name="execue.group.name" /> : <s:property value="group.name" /></td>
    <td align="right">&nbsp;</td>
  </tr>
</table>
</td>
</tr>
<tr><td>
<form id="assignRolesForm">
<table width="30%" border="0" align="center" cellpadding="3"
	cellspacing="0">

<tr>
		<td width="41%" align="right" class="fieldNames">&nbsp;</td>
		<td width="3%">&nbsp;</td>
		<!-- s:select name="group.id" id="group.id" list="groups" listKey="id" listValue="name" id='groupId'/-->
	</tr>

	<tr>
		<td width="45%" align="center"><strong><span class="cellTExtCenter">
	    <s:text
			name="execue.role.pageHeading" />
	  </span></strong></td>
		<td width="8%" align="center">&nbsp;</td>
    <td width="47%" align="center"><strong><span class="cellTExtCenter">
      <s:text
			name="execue.roles.assigned" />
  </span></strong></td>
		<td width="11%" align="center" class="fieldNames">&nbsp;
		
		</td>
	</tr>

	
	<tr>
	<tr>
		<td align="center"><s:select id="Left" list="roles" listKey="id"
			listValue="name" size="7" multiple="true" /></td>
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
					alt="Move All to left" border="0" title="Move All to left" /></a></td>
			</tr>
		</table>
		</td>
		<td align="center"><s:select name="selectedRoles"
			list="group.roles" listKey="id" listValue="name" size="7"
			multiple="true" id="Right" />
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
</table>

<s:hidden name="group.id" cssClass="textBox" id="groupId" /></form></td></tr>
<tr>
	<td height="35" colspan="3" align="center"><input type="button" class="buttonSize108"
		name="imageField" id="imageField"
		value="<s:text name='execue.role.assignRoles' />" /> <input type="button" class="buttonSize108" name="imageField2"
		id="imageField2" value="<s:text name='execue.global.resetButton.name' />" /></td>
</tr>
</table>
<script>
$("#closeInnerPaneLink").show();
$("#addNewGroupLink").hide();

/*$("#group.id").change(function(){
	params = "group.id="+$("#group.id").val();
	alert(params);
	$.get('<s:url action="showUserGroups"/>', params, function(data) {
		$("#innerPane").empty();
		$("#innerPane").fadeIn("fast");    
		$("#innerPane").append(data);
		$("#innerPane").show();  
	}); 
});*/
$("#closeInnerPane").click(function(){
	  $("#dynamicPane").createInput('<s:url action="listGroups"/>',"closeInnerPane","loadingCloseInnerPaneLink"); //passing URL, user clicked link, loader div
	});
$("a").click(function(){
//alert("in herf");
var inputControl=this.id;//$("id").val();
Move(inputControl);

});
function Move(inputControl)
{  
//alert("in move");
  var from, to;
  var bAll = false;
  var left = document.getElementById("Left");
  var right = document.getElementById("Right"); 
  //alert("right:"+right); 
  switch (inputControl)
  {
  case '<<':
    bAll = true;
    // Fall through
  case '<':
    from = right; to = left;
    break;
  case '>>':
    bAll = true;
    $("select[name='selectedRoles'] option").attr("selected" ,"true"); 	
    // Fall through
  case '>':
    from = left; to = right;
    $("select[name='selectedRoles'] option").attr("selected" ,"true"); 	
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
  $().ready(function(){
  	$("select[name='selectedRoles'] option").attr("selected" ,"true"); 	
  });
  $("#imageField").click(function(){ 
  $("select[name='selectedRoles'] option").attr("selected" ,"true"); 	
  	 var right = document.getElementById("Right");
  	 count=0;
  	 var groups = new Array(right.length);  
  	 if(right.length!=0 ){	  
	  	/* for(var i=0;i<right.length;i++){
	  	 	if(right.options[i].selected){
		  	 	groups[i]=right.options[i].text;
		  	 	//alert("value"+groups[i]);	
		  	 	count++;
		  	 }		  	 
		 }
		 if (count==0){
		  	 	alert("Select at least one role From Assigned Roles List:");
		            return false;  
		  	 } */
  	 }else{  	 	        	        
            alert("Assigned Roles list should not empty:");
            return false;          
       }  	
		$.post('<s:url action="assignGroupRoles"/>', $('#assignRolesForm').serialize(), function(data) {
			$("#dynamicPane").empty();
			$("#dynamicPane").fadeIn("fast");    
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();  
		});  
	
	});
	$("#imageField2").click(function(){			
		params = "group.id="+$("#groupId").val();
		$("#dynamicPane").createInput('<s:url action="showGroupRoles"/>',"","",params); 
	
});
</script>
