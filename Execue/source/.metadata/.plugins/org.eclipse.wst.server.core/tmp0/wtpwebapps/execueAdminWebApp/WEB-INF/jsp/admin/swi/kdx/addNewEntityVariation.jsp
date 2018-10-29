<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="innerPane2" style="width: 99%;">
<div id="errorMessage" style="color: red"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green"><s:actionmessage /></div>
<s:form id="entityVariationDefinitionForm">
<table width="90%" border="0" align="center" cellpadding="5" cellspacing="0">
  <tr>
    <td colspan="2"><div id="errorMessage" STYLE="color: red" align="left"></div></td>
  </tr>
  <tr>
    <td colspan="2" >
      <table width="90%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td>
                 <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>                 
                    <td width="8%"  class="fieldNames" style="text-align:right"><s:text name='execue.entityVariations.entityName' /></td>
                    <td width="2%">:</td>
						<td width="68%"><s:textfield name="entityName"
							cssClass="textBoxDisabled" id="entityName" readonly="true" /></td>
                  </tr>
                  
                </table>
             </td>
      </tr>
      <tr><td>&nbsp;</td></tr>
            <tr>
        <td>
               <%int ct=0;%>
               <div style="margin-left:70px;">
                   <table width="60%" border="0" cellspacing="0" cellpadding="3" id="entityVariationTable">
                  <tr>
                    <td height="30" width="65%" align="left" style="text-align:left"   class="fieldNames"><strong><a href="#" id="addEntityVariation" ><s:text name='execue.entityVariations.addEntityVariation' /></a></strong></td><td width="35%">&nbsp;</td>	
                     <div id="pWords">
                    <s:iterator value="variations" status="ct">
                  	<tr id="Tr<s:property value='#ct.index'/>"> 
                     <td width="65%"><input style='width:180px;' maxlength="255" name="variations[<s:property value="#ct.index"/>]" type=text id="variations[<s:property value="#ct.index"/>]" value="<s:property/>"></td>
                    
                    <td width="35%" align="left"  class="fieldNames">
                    
                    <img class='delRow' onclick="deleteRow(<s:property value="#ct.index"/>)" id='<s:property value="#ct.index"/>' src='../images/admin/remove.jpg' alt='Remove this variation' title='Remove this variation' >
                    
                    </td>  
                  </tr>
                  <%ct=ct+1;%>
                  </s:iterator>
                  </div> 
                                               
              </table>
              </div>
              
              </td>
               </tr>
           <tr>      
        <td>        
          <div style="margin-left:70px;">        
            <div id="addNewParallWord">
               <span id="createEntityVariationsId"> 
               <s:if test="variations.size==0" >
               <input type="button" disabled="disabled"	class="buttonSize160" style="width:162px;"  value="<s:text name='execue.entityVariations.submit.button' />" id="submitEntityVariation" />
               </s:if>
               <s:else>
               <input type="button" 	class="buttonSize160" style="width:162px;"  value="<s:text name='execue.entityVariations.submit.button' />" id="submitEntityVariation" />
               </s:else>
               </span>
                </div> </div>
      
        </td>
         </tr>
        </table>
      </td>
  </tr>
</table>
<s:hidden id="entityBedId" name="entityBedId" />
<input type='hidden' value='<%=ct%>' id="count"/>
</s:form>
</div>
<script>
var existingVariation=0
$(document).ready(function() {
existingVariation=$("table#entityVariationTable input[type='text']").length;

if( $("table#entityVariationTable input[type='text']").length==0 && existingVariation==0 ){ 
			$("#submitEntityVariation").attr("disabled","disabled");
		}
		
$('#submitEntityVariation').click(function() {

 var status=true;
	$.each( $("table#entityVariationTable input[type='text']"),function(){
				if($.trim($(this).val())==""){
					status=false;
				}
			});
	if(status){
		$("#submitEntityVariation").removeClass("buttonSize160");	
 		$("#submitEntityVariation").addClass("buttonLoaderSize160");
 		var req="entityName="+$("#entityName").val();
 		req=req+"&entityBedId="+$("#entityBedId").val();
 		$.each( $("table#entityVariationTable input[type='text']"),function(k,v){
				req=req+"&variations["+k+"]="+$(this).val();
			});
			//alert(req);
		 $.post("swi/createEntityVariation.action", req, function(data) {        
				$("#dynamicPane").empty();
				$("#dynamicPane").fadeIn("fast");    
				$("#dynamicPane").append(data);
				$("#dynamicPane").show();
			});
	}else{
	alert("Values should not be empty");	
	}
});			


	
var cnt=parseInt($("#count").val());
$("#addEntityVariation").click(function(){
$("#submitEntityVariation").removeAttr("disabled");									
	$("#imageField").show();
	var trid="Tr"+cnt
	rowContent="<tr id='"+trid+"'><td width='65%' >"
			   +"<input style='width:180px;' name='variations["+cnt+"]' type='text' id='variations["+cnt+"]'></td><td width='35%' align='left'  class='fieldNames'><img class='delRow' onclick=deleteRow('"+cnt+"') id='"+cnt+"' src='../images/admin/remove.jpg' alt='Remove this variation' title='Remove this variation' ></td></tr>";									   
		$("#entityVariationTable").append(rowContent);							 
		cnt++;							  });	



});


//rowContent="<tr><td width='20%' align='right'  class='fieldNames'><input name='' type='checkbox' value='' checked ></td><td width='80%' ><input name='x' type='text' id='pWord'></td></tr>";

function deleteRow(id){  
   		//var id=$(this).attr("id"); 
		$("#Tr"+id).remove();
		
		if( $("table#entityVariationTable input[type='text']").length==0 && existingVariation==0 ){ 
			$("#submitEntityVariation").attr("disabled","disabled");
		}

	}	

		
</script>
