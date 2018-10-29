<%@ taglib prefix="s" uri="/struts-tags"%>
<div style="width:239px;float:left;"><strong><s:text name="execue.unstructured.add.knowledge"></s:text> </strong></div><div id="closeKpopup" style="cursor:pointer;float:left"><img src="images/main/closeButton.png" border="0" /></div>
<div id="errorMessageDiv" style="display:none;width:100%;padding:3px;"></div>
<div id="messageDiv" style="display:none;width:100%;padding:3px;"></div>
<table width="100%" border="0" bgcolor="#FFFFFF" cellspacing="0" style="margin-top:10px;" cellpadding="3">
     
    <tr>
    <td style="white-space:nowrap"><s:text name="execue.unstructured.appName"></s:text> </td>
    <td><s:property value="applicationName" /></td>
  </tr>
  <tr>
    <td width="17%" ><s:text name="execue.unstructured.instance"></s:text> </td>
    
    <td width="83%">
      <input type="text" name="instanceName" readonly="readonly"  id="instanceNameId" value="">
   </td>
  </tr>
  
  <tr>
    <td><s:text name="execue.unstructured.concept"></s:text> </td>
    <td><input type="text" name="concept" style="border:1px solid #CCC;" class="ac_input" id="concept"></td>
  </tr>
  <s:if test="resemanticationCheckBoxVisible">
   <tr>
    <td><s:text name="execue.unstructured.resemantifi"></s:text> </td>
    <td><input type="checkbox" name="resemanticationCheckBox" class="" checked="checked" id="resemanticationCheckBox"></td>
   </tr>
  </s:if>
  <s:else>
  <input type="checkbox" name="resemanticationCheckBox" style="display:none" class="" checked="checked" id="resemanticationCheckBox">
  </s:else>
 
  
  <tr>
    <td>&nbsp;</td>
    <td><label>
    
          <input type="hidden" style="border: 1px solid #CCCCCC;" name="conceptId" id="conceptId">
          <input type="hidden" name="unstructuredApplicationId" id="unstructuredApplicationId" value="<s:property value='applicationId' />">
          <input type="hidden" name="unstructuredModelId" id="unstructuredModelId" value="<s:property value='modelId' />">
      <img src="images/main/add.png" name="addButton" id="submitDynamicKnowledgeId" style="cursor:pointer" >
    </label></td>
  </tr>
</table>
<div id="showInstancesDiv" style="display:none;"></div>
<script>

$(document).ready(function() {
			
		//Submit Dynamic Knowledge							  
											  
		$("#submitDynamicKnowledgeId").click(function(){
				var instanceName=$.trim($("#instanceNameId").val());	
				var applicationId=$("#unstructuredApplicationId").val();	
				 modelId=$("#unstructuredModelId").val();	
				var conceptId=$("#conceptId").val();
				var runResemantification=$("#resemanticationCheckBox").is(":checked");
				//alert(runResemantification);	
				
				$("div.ac_results").remove();
						if(instanceName!=""){
							if(conceptId!="" && $.trim($("#concept").val())!=""){
								//alert(instanceName+"::"+applicationId+"::"+modelId+":::"+conceptId);
							$.post("uss/createInstance.action",{instanceName:instanceName,applicationId:applicationId,modelId:modelId,conceptId:conceptId} ,function(data){
							   if(data.status){
							      if(runResemantification){
								    if(page!="results"){
								        performResemantification(fromunrecog,instanceName);
									}else{
								        submitforResemantification();
									}
								}else{
								   $("#messageDiv").empty().html("Knowledge added successfully.").slideDown();
								   setTimeout('$("#errorMessageDiv").slideUp()',10000);	
								}								 
							  	fromunrecog=false;
							   }else{
								//alert(data.errorMessages[0]);
								$("#errorMessageDiv").empty().html(data.errorMessages[0]).slideDown();
								setTimeout('$("#errorMessageDiv").slideUp()',10000);	
							   }
							}); 
							}else{
							 $("#featuresAlert").empty().html("Please enter concept name").show();
						     $("#featuresAlert").css("top",$top_v+160+"px");
						     $("#featuresAlert").css("left",($left_v+165)+"px");
						     setTimeout('$("#featuresAlert").hide()',3000);	
							}
						
						}else{
							$("#featuresAlert").empty().html("Please enter instance name").show();
					        $("#featuresAlert").css("top",$top_v+130+"px");
					        $("#featuresAlert").css("left",($left_v+165)+"px");
					        setTimeout('$("#featuresAlert").hide()',3000);	
						}
			});	
		
					  
			 addAutocomplete();
                      $("#showAdd").hide();	
					 if(fromunrecog){
						  $("input#instanceNameId").attr("readonly",true);
					  $("input#instanceNameId").val(inst);
					  
					  }else{
						$("input#instanceNameId").val("");  
						$("input#instanceNameId").attr("readonly",false);
					  } 
					  
			$("#concept").blur(function(){
							if(!autoSuggestClicked){ $(this).val("");}			
										});
			
});

function addAutocomplete(){	
modelId=$("#unstructuredModelId").val();
			$("#concept").autocompleteKbase("uss/suggestConcepts.action", {
								multiple: false,
								mustMatch: true,
								selectFirst : false,
								autoFill: false,
								matchContains: false,
								modelId:modelId
								//width:604
								}).result(function(event, data, formatted) {
											//$unitsInput.hide();
											$data = data;
											//$unitLink.show();
											//alert($data.displayName);
											//alert($data.id);
											$("div.ac_results").hide();
											$("#conceptId").val($data.id);
											//alert(modelId+":::::::"+$data.id);
											$.post("uss/showInstances.action",{modelId:modelId,conceptId:$data.id},function(instdata){
													var d1=$("<div style='float:left;padding:0px 10px 10px 10px;width:100%'></div>");
													var ul=$("<ul></ul>");
													$("#showInstancesDiv").empty().append(d1.append('<strong>Examples :</strong>')).append(ul);															
												     $.each(instdata,function(k,v){
															var l1=$("<li style='list-style-type:disc;float:left;width:100px;'></li>"); 
															ul.append(l1.append(v.displayName))	;		 
													 });
													 $("#showInstancesDiv").slideUp("slow");
													 $("#showInstancesDiv").slideDown("slow");
											});
											
										});
			}
</script>