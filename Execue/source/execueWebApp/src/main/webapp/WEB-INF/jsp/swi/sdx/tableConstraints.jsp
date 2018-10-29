<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script>  
function getReferenceColums(refId,ref){
	$.getJSON("swi/showRefTableColums.action",{"tableId" : ref },function(data) {
		$option='';	
		if (data) {
		$("#refColId"+refId).empty();			
			$.each(data, function(i, refTableColums) {
					$option+="<option value='"+refTableColums.name+"'>"+refTableColums.name+"</option>"								
			});		
			
			 //$("#dDataFormat").val($("#dataFormats").val())	;			 
	   }	   
			 $("#refColId"+refId).append($option);	
	   });
}
function create(){
  //$("#dynamicPane").createInput('<s:url action="saveConstraint!saveConstraint"/>',"","",$('#constraintForm').serialize());
  $("#updateConstraints").hide();
  $("#updateConstraintsLoader").show();
    $.post("swi/saveConstraint.action", $('#constraintForm').serialize(), function(data) {
		$("#dynamicPane").empty();
		$("#dynamicPane").fadeIn("fast");    
		$("#dynamicPane").append(data);
		$("#dynamicPane").show();
		showAssetTables();		
		$("#errorMessage").append('<s:fielderror/>');
		$("#errorMessage").append('<s:actionmessage/>');
		 $("#updateConstraintsLoader").hide();
		 $("#updateConstraints").show();
  		
	}); 
}$("#primaryKeyIds option").attr("selected","selected");
$("#primaryKeyIds").hide();
$("#foreignKeyIds option").attr("selected","selected");
$("#foreignKeyIds").hide();




function deleteReference(inds){
var flag=false;
var right;
var fkeyIds;
var fkeyConstraintId;
var constraintId=$("#foreignKeyIds"+inds+" option:selected");
if(constraintId.val()!=0 && typeof constraintId.val() != "undefined"){
	flag=true;	
}
if(!flag){
	$("#tr"+inds).remove();
}else{	
fkeyIds = document.getElementById("foreignKeyIds"+inds);	
    //var conFk = new Array(right.length);  
    var $fkeyConstraintIds=[];   
   if(fkeyIds.length!=0){
	   for(var i=0;i<fkeyIds.length;i++){
	   if(fkeyIds.options[i].selected){
	    	$fkeyConstraintIds[i]=fkeyIds.options[i].value;
		}  
   }
   
}
fkeyConstraintId=fkeyIds.options[0].value;
//var fkeyids=$("#fkeyColumns option:selected").val();
	right = document.getElementById("fkeyColumns"+inds);	
    //var conFk = new Array(right.length);  
    var $fkeyConstraintColumnIds=[];   
   if(right.length!=0){
	   for(var i=0;i<right.length;i++){
	   if(right.options[i].selected){
	    	$fkeyConstraintColumnIds[i]=right.options[i].value;
		}  
   }
   
 }
var assetId=$("#assetId").val();
var tableId=$("#tableId").val(); 
url="swi/deleteConstraint.action?fkeyConstraintId="+fkeyConstraintId+"&asset.id="+assetId+"&tableConstraintsInfo.table.id="+tableId+"&fkeyConstraintColumnIds="+$fkeyConstraintColumnIds;
$.get(url ,function(data) {      
     	$("#dynamicPane").empty();
		$("#dynamicPane").fadeIn("fast");    
		$("#dynamicPane").append(data);
		$("#dynamicPane").show();
    });
 }
}
</script>
<div class="innerPane2" style="width: 670px; height: auto">
<div id="errorMessage" style="color: red"><s:fielderror /> <s:actionerror /></div>
<div id="actionMessage" style="color: red"><s:actionmessage /></div>
<table width="97%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>


		<td width="96%" height="25" class="fieldNames"><s:text
			name="execue.constraints.ws.table" />&nbsp;&nbsp;<strong><s:property
			value="tableConstraintsInfo.table.displayName" /></strong></td>
	</tr>

	<tr>
		<td width="80%" height="25" class="fieldNames"><strong><s:text
			name="execue.constraints.ws.primary-key" /></strong></td>
	</tr>

	<tr>
		<td><s:form namespace="/swi" id="constraintForm" method="POST">
			<table width="100%" border="0" align="center" cellpadding="2"
				cellspacing="0">
				<tr>
					<td colspan="2">
					<div id="message" STYLE="color: red" align="left"></div>
					</td>
				</tr>


				<tr>
					<td width="22%" class="fieldNames"><s:text
						name="execue.constraints.ws.constraint.name" /></td>
					<td width="78%" align="left"><label><s:textfield
						name="execue.tableConstraintsInfo.pkConstraint.name" /></label></td>
				</tr>

				<tr>
					<td valign="top" class="fieldNames"><s:text
						name="execue.constraints.ws.constraint.columns" /></td>
					<td><!-- select tag Start --> <s:select
						name="primaryKeyColumnIds" list="tableConstraintsInfo.columns"
						listKey="id" listValue="name" multiple="true" size="3"
						value="%{tableConstraintsInfo.pkConstraint.constraintColums.{id}}"
						id="constraints" /> <!-- select tag End --></td>
						<s:set name="pkeyIds" value="tableConstraintsInfo.pkConstraint.constraintId"/>
						<s:if test="%{#pkeyIds==null}" >
						<s:select name="primaryKeyIds" id="primaryKeyIds" list="{}"/>
						</s:if>
						<s:else>
						<td>
						<s:select name="primaryKeyIds" id="primaryKeyIds" list="tableConstraintsInfo.pkConstraint.constraintId" multiple="true" 
						/>
						</td>
						</s:else>
				</tr>
				<tr>
					<td class="fieldNames">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" class="fieldNames"><!-- references starts -->
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="25" class="fieldNames"><strong><s:text
								name="execue.constraints.ws.references" /> ( </strong><a href="#"
								id="addReference" onclick="registerAddReferenceClick();"><s:text
								name="execue.constraints.ws.references.add" /></a><strong> )</strong></td>
						</tr>
						<tr>
							<td>
							<table width="100%" border="0" cellspacing="1" cellpadding="1"
								class="tableGridMemberInfo" id="references">
								<tr>
									<td width="4%">&nbsp;</td>
									<td width="4%">&nbsp;</td>
									<td width="23%"><s:text
										name="execue.constraints.ws.constraint.name" /></td>
									<td width="23%"><s:text
										name="execue.constraints.ws.constraint.columns" /></td>
									<td width="23%"><s:text
										name="execue.constraints.ws.constraint.reference.table" /></td>
									<td width="23%"><s:text
										name="execue.constraints.ws.constraint.reference.columns" /></td>
								</tr>
								<!-- Loop Start -->
								<%int cnt=1; %>
								<%int count=0; %>
								
								<s:iterator value="tableConstraintsInfo.fkConstraints"
									status="ct" id="fkConstraints">
									
									<s:set name="fKeyRefTab" value="referenceTable"></s:set>
									<tr id="tr1" style="">
										<td><a href="javascript:deleteReference('<%=cnt%>');"><img 
											src="../images/disabledIcon.gif" alt="Remove Constraint"
											border="0" width="25" height="20"></a></td>
										<td><a href="javascript:editReference('1')" class="links"
											id="3"> <img src="../images/editIcon.gif"
											alt="Modify Constraint" border="0" width="25" height="20"></a></td>
										<td><s:property value="name" id="fkeyName%{#ct.index+1}"/></td>
										<td><s:select name="fkeyColumns" id="fkeyColumns%{#ct.index+1}"
											list="tableConstraintsInfo.columns" listKey="id"
											listValue="name" multiple="true" size="3"
											value="%{constraintColums.{id}}" /></td>
										<td><s:select name="fKeyrefTable" list="foriegnKeyTables"
											listKey="id" listValue="name" value="referenceTable.{id}"
											id="refTableId%{#ct.index}"
											onchange="getReferenceColums('%{#ct.index}',this.value);" />
										</td>
										
									
				<td><s:select name="refColumns" list="referenceColumns"
											listKey="id" listValue="name" id="refColId%{#ct.index}"
											multiple="true" size="3" /></td>
				<td class="constraints">
						<s:select name="foreignKeyIds" id="foreignKeyIds%{#ct.index+1}"  list="tableConstraintsInfo.fkConstraints[#ct.index].constraintId" multiple="true" 
						/>
					
						</td>
					<script>
               getReferenceColums('<%=count%>',$("#refTableId"+<%=count%>).val());
               
                </script>	
			<% cnt+=1;%>
			<% count+=1;%>
									</tr>

								</s:iterator>
								<!-- Loop End -->
							</table>
							</td>
						</tr>
					</table>
					<!-- references end --></td>
				</tr>
				<tr>
					<td class="fieldNames">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td colspan="2" class="fieldNames"><!-- reference by starts -->
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="25">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td height="25" class="fieldNames"><strong><s:text
										name="execue.constraints.ws.referenced-by" /></strong></td>
								</tr>
								<tr>
									<td>
									<table width="100%" border="0" cellspacing="1" cellpadding="1"
										class="tableGridMemberInfo">
										<tr>
											<td width="33%"><s:text
												name="execue.constraints.ws.constraint.name" /></td>
											<td width="33%"><s:text
												name="execue.constraints.ws.constraint.reference.table" /></td>
											<td width="33%"><s:text
												name="execue.constraints.ws.constraint.reference.columns" /></td>
										</tr>
										<!-- Loop Start -->
										<s:iterator value="tableConstraintsInfo.pkReferences"
											id="pkReferences">
											<tr>
												<td width="33%"><s:property value="name" /></td>
												<td width="33%"><s:property value="referenceTable.name" /></td>
												<td width="33%"><s:select list="referenceColumns"
													listKey="name" listValue="name" /></td>
											</tr>
										</s:iterator>
										<!-- Loop End -->
									</table>
									</td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					<!-- reference by ends --></td>
				</tr>
				<tr>
					<td class="fieldNames">&nbsp;</td>
					<td>&nbsp;</td>
				</tr>
				<tr>
					<td height="40"><input type="button" id="updateConstraints" 	class="buttonSize160" style="width:162px;" value="<s:text name='execue.tableConstraints.updateConstraints' />"
						alt="Update Concept" onClick="javascript:create()" />
                        
                        <input id="updateConstraintsLoader" type="button" class="buttonLoaderSize160" disabled="disabled"  style="display:none;"
								value="<s:text name='Update Constraints' />" />
                        
                        </td>

				</tr>
			</table>
			<s:hidden id="tableId" name="tableConstraintsInfo.table.id" />
			<s:hidden id="assetId" name="asset.id" />
			<input type="hidden" value='<%=cnt %>' id="count" name="count">
		</s:form></td>
	</tr>
</table>
</div>
<script>
selectAll();
function selectAll(){
$.each($("td.constraints select"),function(){
  x= $(this).attr("id");
$("#"+x+" option").attr("selected","selected");
    $(this).hide();   
   })
       }
</script>
