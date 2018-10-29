<%@page import="com.execue.core.common.type.PaginationType"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet>
<LINK href="../css/cssPopupMenu.css" rel=stylesheet>
<LINK href="../css/autoComplete.css" rel=stylesheet>
<script src="../js/jquery.js" language="JavaScript" /></script>
<script src="../js/menun.js" language="JavaScript" /></script>
<script src="../js/mm_menu.js" language="JavaScript" /></script>
<script src="../js/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/jquery.execue.js" type="text/javascript"
	language="javascript"></script>
<SCRIPT src="../js/qinew/jquery.autocomplete.js" type=text/javascript></SCRIPT>
<script type="text/javascript" src="../js/execue.commons.js"></script>
<script type="text/javascript"
	src="../js/jquery.execue.searchRecordsComponent.js"></script>
<script src="../js/jquery.execue.helpMessage.js" ></script>

<script>
var hideLoading=false;
var conceptId='';
var selectedConceptId="";
var advancedPathData="";
var CRCPathData="";
var selectTypeIdDefaultsVal="";
$(document).ready(function() {
showBusinessTerms();
//showSearch('divBTerms');
 searcTextFocusInitialise(); 
 $("#divBTerms").searchRecordsComponent({actionName:"swi/getBusinessTermsBySearchString.action",targetDivName:"dynamicPaneBTerms"});
 
 });

function showBusinessTerms(){
  showDetails("swi/getBusinessTerms.action","dynamicPaneBTerms","get");  
}
function createNewConcept() { 
	showPanel1();
	 $("#dynamicPanePathDefinition").empty();
	 $("#dynamicPaneAdvancedPathDefinition").empty();
	 $("#TabbedPanels1").show();
	 showDetails("swi/createNewConcept.action?mode=add","dynamicPane","post","","addNewConcept");
	 //TODO-we need to come up with proper solution
	 $("#dynamicPaneAdvancedConceptDetails").empty().append('Advance concept details are not available');	
 }

/*function createConcept() {
	 var conceptData = $("#conceptForm").serialize();
	 showDetails("swi/addConcept.action","dynamicPane","post",conceptData,"enableUpdateConcept","updateProcess",false);     
     showBusinessTerms();
  }*/
  
  function createUpdateInstance() {
  //	$("#updateProcess").show();
  //  $("#enableUpdateInstance").hide();
	var instanceData = $("#instanceForm").serialize();
	//  $.post("swi/addInstance.action", instanceData, function(data) {
	//  $("#enableUpdateInstance").show();
  	//  $("#updateProcess").hide();
   //   $("#dynamicPane").fadeIn("fast");
   //   $("#dynamicPane").empty(); 
    //  $("#dynamicPane").append(data);
   //   $("#dynamicPane").show();
   // });
	//$("#dynamicPane").getAjaxHtmlResponce("swi/addInstance.action","enableUpdateInstance","updateProcess",instanceData,"post",false);
	showDetails("swi/addInstance.action","dynamicPane","post",instanceData,"enableUpdateInstance","updateProcess");
  }
function getConcept(conceptID) {
	  $("#TabbedPanels1").show();
	  showPanel1();
	  showDetails("swi/conceptDetail.action?concept.id="+conceptID,"dynamicPane","post","",conceptID);
	  showDetails("swi/showAdvanceConceptDetails.action?concept.id="+conceptID,"dynamicPaneAdvancedConceptDetails","post","","");  	   
  }
  function noCursor(){$(this).trigger("blur");}
  
	function getInstancesByPage(conceptID) {
		/*$("#showInstance").hide(); 
  		$("#loadingShowInstanceLink").show();
    	$.post("swi/getInstances.action?concept.id="+conceptID, function(data) {
      		$("#dynamicPane").fadeIn("fast");
      		$("#dynamicPane").empty(); 
      		$("#dynamicPane").append(data);
      		$("#dynamicPane").show();*/
			showDetails("swi/getInstances.action?concept.id="+conceptID,"dynamicPane","post","","showInstance");
			// $("#dynamicPane").getAjaxHtmlResponce("swi/getInstances.action?concept.id="+conceptID,"showInstance","","","post");
       		//$("#cname").bind("focus", noCursor);   
        	//$("#iname").select().focus(); 
    	//});
		//getInstances(conceptID);
  	}
  
  function getInstances(conceptID) {
  /*$("#showInstance").hide(); 
  $("#loadingShowInstanceLink").show();
    $.post("swi/showInstances.action?concept.id="+conceptID+"&mode=add", function(data) {
      $("#dynamicPane").fadeIn("fast");
      $("#dynamicPane").empty(); 
      $("#dynamicPane").append(data);
      $("#dynamicPane").show();*/
	 showDetails("swi/showInstances.action?concept.id="+conceptID+"&mode=add","dynamicPane","post","","showInstance","loadingShowInstanceLink");
	//  $("#dynamicPane").getAjaxHtmlResponce("swi/showInstances.action?concept.id="+conceptID+"&mode=add","showInstance","loadingShowInstanceLink","","post");
       //$("#cname").bind("focus", noCursor);   
       // $("#iname").select().focus(); 
   // });
  }
  function addInstance(conceptID) {
   /* $.post("swi/createNewInstance.action?concept.id="+conceptID+"&mode=add", {}, function(data) {
      $("#dynamicPane").fadeIn("fast");
      $("#dynamicPane").empty(); 
      $("#dynamicPane").append(data);
      $("#dynamicPane").show();
    });*/
   showDetails("swi/createNewInstance.action?concept.id="+conceptID+"&mode=add","dynamicPane","post","","addNewInstance");
   //$("#dynamicPane").getAjaxHtmlResponce("swi/createNewInstance.action?concept.id="+conceptID+"&mode=add","addNewInstance","","","post");
  }
  
  function getInstance(conceptID, instanceID) {
     /* $.post("swi/instanceDetail.action?concept.id="+conceptID+"&instance.id="+instanceID, {}, function(data) {
      $("#dynamicPane").fadeIn("fast");
      $("#dynamicPane").empty(); 
      $("#dynamicPane").append(data);
      $("#dynamicPane").show();*/
	  showDetails("swi/instanceDetail.action?concept.id="+conceptID+"&instance.id="+instanceID,"dynamicPane","post","",instanceID);
	  //$("#dynamicPane").getAjaxHtmlResponce("swi/instanceDetail.action?concept.id="+conceptID+"&instance.id="+instanceID,instanceID,"","","post");

     // $("#cname").bind("focus", noCursor); 
     // $("#idname").bind("focus", noCursor);     
   // });
   } 
   
    function close() {
      $("#dynamicPane").empty();     
    }
	
 $option='';
 $optionUnit='';

 function changeFormat(conversionType){
  $options='';
    $isCurrencyOrNumber = false;
    $("#dataFormats").empty();
    $("#units").empty();   
    getFormatAndUnitValues(conversionType); 
  }
  function getFormatAndUnitValues(format){
   var format= format.toUpperCase();  
	$.getJSON("swi/qiConversion.action", {displayType : format}, function(data) {
		$option='';		
		if(data.qiConversion){
		if (data.qiConversion.dataFormats) {
			
			$.each(data.qiConversion.dataFormats, function(i, dataFormats) {
				if($("#dDataFormat").val()==dataFormats.name){			
				$option+="<option value='"+$("#dDataFormat").val()+"' selected>"+dataFormats.displayName+"</option>"
				}else{
					$option+="<option value='"+dataFormats.name+"'>"+dataFormats.displayName+"</option>"
				}				
			});		
			 $("#dataFormats").append($option);	
			 $("#dDataFormat").val($("#dataFormats").val())	;			 
	   }
	   if (data.qiConversion.units) {
	   $optionUnit=''
	   //	if(data.units.length>0){
			$.each(data.qiConversion.units, function(j, units) {
				if($("#dUnit").val()==units.name){
					$optionUnit+="<option value='"+$("#dUnit").val()+"' selected>"+units.displayName+"</option>"
				}else{
					$optionUnit+="<option value='"+units.name+"'>"+units.displayName+"</option>"
				}					
			});			
			$("#units").append($optionUnit);
			$("#dUnit").val($("#units").val());
		/*}else{
	 		$("#dUnit").val($("#units").val())
	 	}*/
	  }
	 }
	});	
}
function getFormatValue(frmt,fvalue){
	if(frmt=="Frmt"){				
		$("#dDataFormat").val(fvalue);
	}else{	
		$("#dUnit").val(fvalue);		
	}
}
function resetConcept(){
	hideLoading=true;
	if($("#mode").val()=="add"){		
		$("#dataFormats").empty();
		$("#units").empty();
		document.conceptForm.reset();	
	}else{
		conceptId=$("#currConceptId").val();
		getConcept(conceptId);
	}	
} 


var nameOrderBy="Desc";
var descOrderBy="Desc";
var order="Desc"
var sortField="displayName"
var sortDiv="sortByNameLink"
function sortByName(){
sortInstances("name")		
}
function sortInstances(orderBy){
var conceptId= $("#conceptId").val();
	actionName="getInstances.action";
	//$.post("swi/"+actionName+"?requestedPage=1",{sortField:"displayName", sortOrder:nameOrderBy, "concept.id":conceptId}, function(data) {
	//$("#dynamicPane").fadeIn("fast");
	//$("#dynamicPane").empty();
	//$("#dynamicPane").append(data);
	setOrderField(orderBy);
			//alert(sortField+"::::::::::"+order)
	//showDetails("swi/"+actionName+"?requestedPage=1","dynamicPane","post",{sortField:sortField, sortOrder:order, "concept.id":conceptId},sortDiv,null,false)
	$("#dynamicPane").getAjaxHtmlResponce("swi/"+actionName+"?requestedPage=1",sortDiv,"",{sortField:sortField, sortOrder:order, "concept.id":conceptId},"post",false);

	
}
function sortByDescr(){
	sortInstances("desc")	
	/*var conceptId= $("#conceptId").val();
	actionName="getInstances.action";
	//$.post("swi/"+actionName+"?requestedPage=1",{sortField:"description", sortOrder:descOrderBy, "concept.id":conceptId}, function(data) {
	//$("#dynamicPane").fadeIn("fast");
	//$("#dynamicPane").empty();
	//$("#dynamicPane").append(data); 
	showDetails("swi/"+actionName+"?requestedPage=1","dynamicPane","post",{sortField:"description", sortOrder:descOrderBy, "concept.id":conceptId},"sortByDescLink",null,false)
	
	if(descOrderBy=="Asc"){descOrderBy="Desc";}else{descOrderBy="Asc";}
	//	});*/
}

//function getShowAllDetails(){
		// $.get("swi/getBusinessTermsBySearchString.action",{}, function(data) {
     //  $("#dynamicPaneBTerms").empty();
     //  $("#dynamicPaneBTerms").append(data);
     //});//End of populate BusinessTerms
	//showDetails("swi/getBusinessTermsBySearchString.action","dynamicPaneBTerms","get");	
//}
//function getStartsWithStringDetails(searchString){
	// $.get("swi/getBusinessTermsBySearchString.action?searchString="+searchString+"&searchType=startWith",{}, function(data) {
     //  $("#dynamicPaneBTerms").empty();
     //  $("#dynamicPaneBTerms").append(data);
    // });//End of populate BusinessTerms
//showDetails("swi/getBusinessTermsBySearchString.action?searchString="+searchString+"&searchType=startWith","dynamicPaneBTerms","get");
//}

//function getContainsStringDetails(searchString){
	 //$.get("swi/getBusinessTermsBySearchString.action?searchString="+searchString+"&searchType=contains",{}, function(data) {
      // $("#dynamicPaneBTerms").empty();
      // $("#dynamicPaneBTerms").append(data);
    // });//End of populate BusinessTerms
//showDetails("swi/getBusinessTermsBySearchString.action?searchString="+searchString+"&searchType=contains","dynamicPaneBTerms","get");
//}

function getPaneSearchDetails(searchType,searchString){
	if(searchType=="ShowAll"){
	showDetails("swi/getBusinessTermsBySearchString.action","dynamicPaneBTerms","get");	
	 }else{
     showDetails("swi/getBusinessTermsBySearchString.action?searchString="+searchString+"&searchType="+searchType,"dynamicPaneBTerms","get");
	 }
}


  function checkAttributes(){
	 if( $("#selectAttributeId option").length==1){
		 $("#selectAttributesDiv").empty().append("Not Found");
	 }
  }
   function showSelectedTypePathDefinitions(advanced){	
   var selectedTypeDefinitions="selectedTypePathDefinitions"+advanced;
	 $.each($("input[name='"+selectedTypeDefinitions+"']"),function(k,v){
			var $bedId=$(this).attr("sourceconceptbedid"); 
			var $relationText=$(this).attr("relationname");  
		    var $relationbedId	=$(this).attr("relationBedId");
		    var $atributeVal=$(this).attr("attributebedid");
			var $atributeText=$(this).attr("attributeName");  
			var $attributeBedId	=$(this).attr("attributebedid");
			var $realizationConceptName=$(this).attr("destinationconceptname");  
			var $realizationConceptBedId=$(this).attr("destinationconceptbedid");  
			var $conName=$(this).attr("sourceconceptname");  
			var $inherent=$(this).attr("inherent"); 
				var $rowVals={
					conName:$conName,
					bedId:$bedId,
					relationText:$relationText,
					relationbedId:$relationbedId,
					atributeVal:$atributeVal,
					atributeText:$atributeText,
					attributeBedId	:$attributeBedId,
					inherent:$inherent
				};		
	 createAssociation($realizationConceptName,$realizationConceptBedId,advanced,$rowVals);
																	
		});
	
   }
  function showSelectedTypeBehaviors(advanced){
	  var  behaviours="";
	  if(advanced==""){
		behaviours  ="typeBehaviors";
	  }else{
		behaviours="extendedBehaviors";  
	  }
	 $.each($("input[name='"+behaviours+"']"),function(k,v){ 
				var selectedBehaviour=$(this).attr('desc').toUpperCase();	 
				var exists=false; 
				$.each($("#selectedTypeBehaviorsDiv"+advanced+" input[value='"+selectedBehaviour+"']"),function(){
					exists=true; 
				});
				if(exists){
					$(this).attr("checked","checked");
				}
				
		});
  }
   
 
function getRelization(advanced){
   var relationName= $("#selectAttributeId"+advanced+" option:selected").attr("relationName");
   var attributeName= $("#selectAttributeId"+advanced+" option:selected").attr("attributeName");   
        
     $("#relationText"+advanced).val(relationName);
     $("#relationText"+advanced).show();
     $("#relationHeading"+advanced).show();
     $("#addLink"+advanced).show();
     $("#realizedConceptsList"+advanced).empty();if(relationName==undefined){$("#addLink").hide();$("#realizedConceptsListDiv"+advanced).empty();return;}
     var entityType= $("#selectAttributeId"+advanced+" option:selected").attr("entityType"); 
      if(entityType!="REALIZED_TYPE"){
         var selectedAttributeTypeId= $("#selectAttributeId"+advanced+" option:selected").val();
         var $select="";  
         if(attributeName =='Value'){
            $select=$('<select name=""></select>');	
         }else{
            $select=$('<select name="" size="4" width="200px"  multiple="multiple"></select>');	
         }    
	   
		 $select.attr("id","realizationSelectedId"+advanced);
	     $.getJSON("swi/showRealizedConcepts.action?selectedAttributeTypeId="+selectedAttributeTypeId,function(data){	       		
	        $.each(data,function(k,v){
	           $option=$('<option></option>');
	           $option.text(v.displayName);
	           $option.val(v.bedId);
	           $select.append($option);
	        });
			$select.children('option').eq(0).attr("selected","selected");
	        $("#realizedConceptsListDiv"+advanced).empty().show().append($select);
			
	     });
	    
      }else{
		 $("#realizedConceptsListDiv"+advanced).empty(); 
	  }
}


function checkAssociationExists(realizationConceptBedId,relationText,atributeText,atributeId,advanced){
	var checkAssociationExist=true;
	var inptName="hiddenAssociationData";
	if(advanced!=""){inptName="hiddenAssociationData"+advanced;}
	$.each($("#relationDisplayDiv"+advanced+" input[name='"+inptName+"']"),function(k,v){ 
		$relationTextFromAssociation=$(this).attr("relationText");																		
		$realizationConceptIdFromAssociation=$(this).attr("realizationConceptBedId");	
		$attributeTextFromAssociation=$(this).attr("attributetext");	
		$attributeIdFromAssociation=$(this).attr("attributeBedId");	

		if(realizationConceptBedId!=""){ 
			//if((realizationConceptBedId==$realizationConceptIdFromAssociation) && (relationText==$relationTextFromAssociation)){
			if(advanced==""){ 
			  //alert("attributeTextFromAssociation::"+$attributeTextFromAssociation+" atributeText::"+atributeText);
			    if(atributeText=='Value'){
			     if($attributeTextFromAssociation && $attributeTextFromAssociation==atributeText){
			       var message="Association Exists.";
				  showValidationMessage(message,advanced);	
				  checkAssociationExist=false;
			     }			      
			    }else{
				  if((realizationConceptBedId==$realizationConceptIdFromAssociation) ){
				    var message="Association Exists.";
				    showValidationMessage(message,advanced);	
				   checkAssociationExist=false;
				 }
				}
			}else{ 			
				if((realizationConceptBedId==$realizationConceptIdFromAssociation) && (relationText==$relationTextFromAssociation)){
				var message="Association Exists.";
				showValidationMessage(message,advanced);	
				checkAssociationExist=false;
				}
			}
		}else{ 
		   if(advanced==""){
				if((atributeId==$attributeIdFromAssociation)){		
				var message="Association Exists.";
				showValidationMessage(message,advanced);			
				checkAssociationExist=false;
				}
		   } else{
				if((atributeText==$attributeTextFromAssociation)){		
				var message="Association Exists.";
				showValidationMessage(message,advanced);			
				checkAssociationExist=false;
				}
		   }
		}
	 });

	return checkAssociationExist;
}
function checkRelation(text){
var ret=true;
	  $.each($("ul#relationsCRC li"),function(k,v){ 
			if($(this).text()==text){
				ret=false;
				return ret;	
	  		}
		});
return ret;

}
function addRow(advanced){
    var association="is associated with";
    var $conName=$("#conceptNameId").val();  
    var $bedId=$("#businessEntityId").val(); 
	var $relationText=$("#relationText"+advanced).val();  
    var $relationbedId	=$("#selectAttributeId"+advanced+" option:selected").attr("relationBedId");
    var $atributeVal=$("#selectAttributeId"+advanced+" option:selected").val();
	var $atributeText=$("#selectAttributeId"+advanced+" option:selected").attr("attributeName");
	var $attributeBedId	=$("#selectAttributeId"+advanced+" option:selected").attr("attributeBedId");	
	var $inherent=$("#selectAttributeId"+advanced+" option:selected").attr("isInherent");	
	var $rowVals={
		conName:$conName,
		bedId:$bedId,
		relationText:$relationText,
		relationbedId:$relationbedId,
		atributeVal:$atributeVal,
		atributeText:$atributeText,
		attributeBedId	:$attributeBedId,
		relationBedId	:$relationbedId,
		inherent:$inherent
	};	
  if($relationText ==''){
     var message="Relation name is required.";
	 showValidationMessage(message,advanced);
	 return false;	
  }
  if(advanced=="CRC"){
	var v = checkRelation($relationText);
	if(!v){
		var message="<s:text name='execue.conceptAssociations.baserelationmessage' />";
	    showValidationMessage(message,advanced);	
	return false;
	}
  }
  if($("#realizedConceptsListDiv"+advanced).html()!=""){
	  if($("#realizationSelectedId"+advanced+" option:selected").length>0){
		$.each($("#realizationSelectedId"+advanced+" option:selected"),function(){
			var $realizationConceptBedId=$(this).val();
			if(advanced != ""){
			   $rowVals.atributeVal=$(this).attr("attributeBedId"); 
			}	 
			var checkAssociationExist=checkAssociationExists($realizationConceptBedId,$relationText,$atributeText,$attributeBedId,advanced);		
			if(checkAssociationExist){							
			    createAssociation($(this).text(),$realizationConceptBedId,advanced,$rowVals);			    
			    if(advanced !='Advanced' && $atributeText =='Value'){
				   var selectedRealizedValue=$("#realizationSelectedId option:selected").text();  
				   changeFormat(selectedRealizedValue);
			     }
			}			
		});
	  }else{
	    var message="Realizations not exist, Please add Realizations.";
	    showValidationMessage(message,advanced);		
	  }
  }else{
	  var checkAssociationExist=checkAssociationExists("",$relationText,$atributeText,$attributeBedId,advanced);
	  if(checkAssociationExist){
	    createAssociation("","",advanced,$rowVals);
	  }
  }
    /* alert(checkAssociationExist);
	 */
}
function createAssociation(realizationConceptName,realizationConceptBedId,advanced,rowVals){
	        var $rowOuterDiv=$('<div style="padding-left: 2px;width:100%"></div>');
			var $conceptNameDiv=$('<div style="float: left;padding: 3px;" ></div>');
			var $hasRelationDiv=$('<div style="float: left; padding: 3px;"></div>');
		    var $realizationDiv=$('<div style="float: left;padding: 3px;"  ></div>');
			var $deleteDiv=$('<div id="deleteDiv1" style="float: left;padding: 3px;"</div>');
		    var $deleteImg=$('<img height="16" border="0" title="Delete" alt="Delete" src="../images/disabledIcon.gif">');				
			var $deleteLink=$('<a href="#"></a>');

			var $conName=rowVals.conName;
		    var $bedId=rowVals.bedId;
			var $relationText=rowVals.relationText;//$("#relationText"+advanced).val();  
		    var $relationbedId	=rowVals.relationbedId;//$("#selectAttributeId"+advanced+" option:selected").attr("relationBedId");
		    var $atributeVal=rowVals.atributeVal;//$("#selectAttributeId"+advanced+" option:selected").val();
			var $atributeText=rowVals.atributeText;//$("#selectAttributeId"+advanced+" option:selected").attr("attributeName");
			var $attributeBedId	=rowVals.attributeBedId	;//$("#selectAttributeId"+advanced+" 
			var $inherent=rowVals.inherent;
			
			$deleteLink.click(function(){
									   $(this).parent('div').parent('div').next('img').remove();
									   $(this).parent('div').parent('div').remove();									
					            });
			var $hiddenField=$('<input  value=""  type="hidden"></input>');
					$deleteLink.append($deleteImg); $deleteDiv.append($deleteLink);
					$hiddenField.val($atributeVal);
					$hiddenField.attr("conceptName", $conName);
					$hiddenField.attr("conceptBedId",$bedId);
					$hiddenField.attr("relationText",$relationText);					
					$hiddenField.attr("realizationConceptBedId",realizationConceptBedId);
					$hiddenField.attr("realizationConceptName",realizationConceptName);		
					$hiddenField.attr("attributeText",$atributeText);
					$hiddenField.attr("attributeBedId",$attributeBedId);
					$hiddenField.attr("relationBedId",$relationbedId);					
					$hiddenField.attr("inherent",$inherent);			
					$hiddenField.attr("name","hiddenAssociationData"+advanced);
							
			$("#relationDisplayDiv"+advanced).prepend($rowOuterDiv).prepend("<img src='../images/space.jpg' width='500' height='2'  />");
			$rowOuterDiv.append($conceptNameDiv.append($conName));
			
			if(advanced!=""){$rowOuterDiv.append($hasRelationDiv.append($relationText));}	
			else {$rowOuterDiv.append($hasRelationDiv.append("is associated with"));	}
			
			if(realizationConceptName!=""){
			$rowOuterDiv.append($realizationDiv.append(realizationConceptName));
			
			 	if(($atributeText!="")&&($atributeText!=undefined)){
					$realizationDiv.append(" ("+$atributeText+")")
				}
			}else{
				$rowOuterDiv.append($realizationDiv.append($atributeText));
			}
			if($inherent!="YES"){
			$rowOuterDiv.append($deleteDiv);
			}
			$rowOuterDiv.append($hiddenField);
}

function checkForRequiredFields(){
var requiredFieldsExists=true;
	if($("#selectAttributeId option[isrequired='YES']").length>0){
			$.each($("#selectAttributeId option[isrequired='YES']"),function(){
				$attId=$(this).attr("attributebedid");
				if($("#relationDisplayDiv input[attributebedid='"+$attId+"']").length==0){
					requiredFieldsExists=false;
				}
				
			 });
	}else{
		requiredFieldsExists=false;
	}
return requiredFieldsExists;
}

function saveType(){ 
var valid=doValidation($("#concept"));
if(!valid){return false;}
	var requiredFieldsExists=checkForRequiredFields();
	//var requiredFieldsExists=true;
	    var conceptId=$("#selectedConceptId").val();
		var typeIdTobeSaved=$("#selectTypeId option:selected").attr("id");		
		var selectedDetailTypeId=getSelectedDetailTypeId();	
	    var conceptData = $("#conceptForm").serialize();
		$request=""
		$request+=conceptData;
		$request+="&selectedTypeId="+typeIdTobeSaved;
		$request+="&selectedConceptId="+conceptId;				
		if(selectedDetailTypeId){
		 $request+="&selectedDetailTypeId="+selectedDetailTypeId;
		}
		
		         /**  Data to be saved**/
				$.each($("#relationDisplayDiv input[name='hiddenAssociationData']"),function(k,v){		
					$request+="&selectedTypePathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("conceptBedId");						
					if($(this).attr("realizationConceptBedId")){
						$request+="&selectedTypePathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("realizationConceptBedId");
					}else{
						$request+="&selectedTypePathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("attributeBedId");
					}									
					$request+="&selectedTypePathDefinitions["+k+"].attributeBedId="+$(this).attr("attributeBedId");
					$request+="&selectedTypePathDefinitions["+k+"].relationBedId="+$(this).attr("relationBedId");			
				});
				   /**  Original Path definition**/
				$.each($("#selectedTypePathDefinitionsDiv input[name='selectedTypePathDefinitions']"),function(k,v){		
					$request+="&savedTypePathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("sourceConceptBedId");					
					$request+="&savedTypePathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("destinationConceptBedId");													
					$request+="&savedTypePathDefinitions["+k+"].attributeBedId="+$(this).attr("attributeBedId");	
					$request+="&savedTypePathDefinitions["+k+"].pathDefinitionId="+$(this).attr("pathDefinitionId");
					$request+="&savedTypePathDefinitions["+k+"].relationBedId="+$(this).attr("relationBedId");	
	
				});
				
		    if($("#selectAttributeId option[isrequired='YES']").length>0){
			if(!requiredFieldsExists){	
			    var message="Association with required attributes must exist.";
	            showValidationMessage(message,'');		
				return false;
			   }
		    }
		 /**  Behaviors to be saved **/
		$.each($("input[name='selectedTypeBehaviors']"),function(k,v){
		  $request+="&selectedTypeBehaviors["+k+"]="+$(this).val();
		});
		 /** Original Behaviors **/
		$.each($("input[name='selectedTypeBehaviors']"),function(k,v){
		  $request+="&savedTypeBehaviors["+k+"]="+$(this).val();
		});
		$("#submitButton").hide();
		$("#submitButtonLoader").show();
		//alert($request);		
     $.ajax({url:"swi/saveConceptTypeAssociation.action",
			    data:$request,
			    success:function(data){showSavedData(data)},
			    type:"post"
			   });
		function showSavedData(data){	
			 $("#validationMessage").empty();
			 $("#dynamicPane").empty().append(data);
			  savedDefaultType=typeIdTobeSaved;
			  checkToDisplayAdvancedMeaning(typeIdTobeSaved,$("#selectedConceptId").val());
			 $("#submitButton").show();
			 $("#submitButtonLoader").hide();
			 var requestdPage=$("#requestedPageHiddenField").val(); 
			
			 var searchStr=$("#search_string").val(); 
			  var searchTyp=$("#search_type").val(); 
			  var url="getBusinessTerms.action?searchString="+searchStr+"&searchType="+searchTyp;
			   if(requestdPage!=undefined){ url="getBusinessTerms.action?searchString="+searchStr+"&searchType="+searchTyp+"&requestedPage="+requestdPage; }

			 $.post(url,function(data1){
																																	   
					$("#dynamicPaneBTerms").empty().html(data1);	
					
					 $.each($("#dynamicPaneBTerms div.tableList a"),function(i,k){
					$temp=$(this);
					$temp.html(autoHighlight($temp.text(), searchStr));
					});
					 
				})
			 
			
			 
			 if(!conceptId){
			    showBusinessTerms();
			 }
		  }
       }
function showValidationMessage(message,advanced){
	$("#validationMessage"+advanced).empty().append(message);
   setTimeout(function(){$("#validationMessage"+advanced).slideUp().empty();},3000);
  $("#validationMessage"+advanced).css("color","red");
 $("#validationMessage"+advanced).slideDown();
}

function saveAdvanceConceptDetails(){ 
        var $request=""
	    $request = $("#advanceConceptDetails").serialize();		
       $.ajax({url:"swi/saveAdvanceConceptDetails.action",
			    data:$request,
			    success:function(data){showSavedData(data)},
			    type:"post"
			   });
		function showSavedData(data){
			 $("#dynamicPaneAdvancedConceptDetails").empty().append(data);
			 $("#submitButton").show();
			 $("#submitButtonLoader").hide();			
			 
		}
}
function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
	}
</script>
