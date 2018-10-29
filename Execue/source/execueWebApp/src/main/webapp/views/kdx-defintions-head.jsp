<%@page import="com.execue.core.common.type.PaginationType"%>
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
<SCRIPT src="../js/jquery.autocomplete.js" type=text/javascript></SCRIPT>
<script type="text/javascript" src="../js/execue.commons.js"></script>
<script type="text/javascript"
	src="../js/jquery.execue.searchRecordsComponent.js"></script>

<script>
var hideLoading=false;
var conceptId='';
var selectedConceptId="";
var advancedPathData="";
$(document).ready(function() {
showBusinessTerms();
 searcTextFocusInitialise();
 $("#addNewConcept").hide();
 $("#divBTerms").searchRecordsComponent({actionName:"swi/getBusinessTermsBySearchString.action",targetDivName:"dynamicPaneBTerms"});
  
 });

function showBusinessTerms(){
  showDetails("swi/getBusinessTerms.action","dynamicPaneBTerms","get");  
}
function getConcept(conceptID) {
	  $("#TabbedPanels1").show();	 	
	  showDetails("swi/showHierarchy.action?concept.id="+conceptID,"dynamicPane","post","",conceptID);
	  showDetails("swi/showRelation.action?concept.id="+conceptID,"dynamicPaneRelations","post","","");	   
  }

 function showExistingPathDefinitions(advanced){ 
     var selectedPathDefinitions="selectedPathDefinitions"+advanced;
	 $.each($("input[name='"+selectedPathDefinitions+"']"),function(k,v){
	        var $sourceConceptName=$(this).attr("sourceConceptName");
	        var $sourceConceptBedId=$(this).attr("sourceConceptBedId");    
			var $relationText=$(this).attr("relationName");  
		    var $relationBedId	=$(this).attr("relationBedId");	
			var $destinationConceptName=$(this).attr("destinationConceptName");  
			var $destinationConceptBedId=$(this).attr("destinationConceptBedId"); 
			var $entityTripleDefinitionId=$(this).attr("entityTripleDefinitionId"); 
			var $source=$(this).attr("source"); 
			 
			var $rowVals={
				sourceConceptName:$sourceConceptName,
				sourceConceptBedId:$sourceConceptBedId,
				relationName:$relationText,
				relationBedId:$relationBedId,
				entityTripleDefinitionId:$entityTripleDefinitionId,
				source:$source
			};		
	      createAssociation($destinationConceptBedId,$destinationConceptName,advanced,$rowVals);
																	
		});
	
   }
 function checkAssociationExists(selectedConcept,advanced,relationName){
	var checkAssociationExist=true;
	var inptName="hiddenAssociationData"+advanced; 

	$.each($("#assoiationDisplayDiv"+advanced+" input[name='"+inptName+"']"),function(k,v){ 
		
		$selectedConceptExistingAssociation=$(this).attr("selectedconceptbedid");	
		$relationNameFromAssociation=$(this).attr("relationText");	
			//if((realizationConceptBedId==$realizationConceptIdFromAssociation) && (relationText==$relationTextFromAssociation)){
			 if(advanced=="Hierarchy"){
				if(selectedConcept==$selectedConceptExistingAssociation){
				var message="Association Exists.";
				showValidationMessage(message,advanced);	
				checkAssociationExist=false;
				}

			}else{
				if((selectedConcept==$selectedConceptExistingAssociation)&&($relationNameFromAssociation==relationName)){
				var message="Association Exists.";
				showValidationMessage(message,advanced);	
				checkAssociationExist=false;
				}
				
			}
			
		
		
	 });

	return checkAssociationExist;
} 
 function addRow(advanced){	
	var relationName=""
	var relationBedId="";
	var hierarchyVal="";
	if(advanced=="Hierarchy"){
	 //NOTE:- ralationName and relationBedId may be hiearchy name and bedId as well as relation name and bedId
		relationName=$("#selectHierarchy option:selected").text();
		relationBedId=$("#selectHierarchy option:selected").attr("hierarchybedid");
		relationBedId=relationBedId.substr(0,relationBedId.indexOf('#'));
		hierarchyVal=$("#selectHierarchy option:selected").val();
	    
    }else{
	   relationName=$("#relationText").val();	
	   if($.trim(relationName)==""){showValidationMessage("Relation should not be empty",advanced); $("#relationText").focus(); return ;}
	}	
    var sourceConceptBedId=$("#businessEntityId").val(); 
	var sourceConceptName=$("#conceptName").val(); 
	  var sourceConceptBedId=$("#businessEntityId").val(); 
	var $rowVals={
		sourceConceptName:sourceConceptName,
		sourceConceptBedId:sourceConceptBedId,
		relationBedId:relationBedId,
		hierarchyVal:hierarchyVal,
		relationName:relationName
	};	
  if($("#conceptsFor"+advanced+" option:selected").length>0){
		$.each($("#conceptsFor"+advanced+" option:selected"),function(){
			var $selectedConceptBedId=$(this).val();
			if(advanced=="Hierarchy"){
			var checkAssociationExist=checkAssociationExists($selectedConceptBedId,advanced); 
			}else{
			var checkAssociationExist=checkAssociationExists($selectedConceptBedId,advanced,relationName); 	
			}
			if(checkAssociationExist){		
			    createAssociation($selectedConceptBedId,$(this).text(),advanced,$rowVals);
			}			
		});
		
		/*$.each($("#conceptsFor"+advanced+" input:checked"),function(){ 
			var $selectedConceptForHierarchy=$(this).val();
			 
			var checkAssociationExist=checkAssociationExists($selectedConceptForHierarchy,advanced); 
			if(checkAssociationExist){		
			    //createAssociation($selectedConceptForHierarchy,$(this).text(),advanced,$rowVals);
				createAssociation($selectedConceptForHierarchy,$(this).attr("label"),advanced,$rowVals);
			}			
		});*/		
  }else{
    var message="";
	if(advanced=="Hierarchy"){message="please select Concepts for Hierarchy.";}
	else{message="please select Concepts for Relation.";}
    showValidationMessage(message,advanced);		
  }	
}
function createAssociation(selectedConceptBedId,selectedConceptName,advanced,rowVals){ 
    var $rowOuterDiv=$('<div style="padding-left: 2px;width:100%"></div>');
	var $conceptNameDiv=$('<div style="float: left;padding: 3px;" ></div>');
	var $hasRelationDiv=$('<div style="float: left; padding: 3px;"></div>');
    var $selectedConceptDiv=$('<div style="float: left;padding: 3px;"  ></div>');
	var $deleteDiv=$('<div id="deleteDiv1" style="float: left;padding: 3px;"</div>');
    var $deleteImg=$('<img height="16" border="0" title="Delete" alt="Delete" src="../images/disabledIcon.gif">');				
	var $deleteLink=$('<a href="#"></a>');
    
	var $source=rowVals.source;
	var $sourceConceptName=rowVals.sourceConceptName;
	var $sourceConceptBedId=rowVals.sourceConceptBedId;
	var $relationBedId=rowVals.relationBedId;
	var $hierarchyVal=rowVals.hierarchyVal;
	var $relationText=rowVals.relationName;
	var $selectedConceptBedId=selectedConceptBedId;
	var $selectedConceptName=selectedConceptName;
	var $entityTripleDefinitionId=rowVals.entityTripleDefinitionId;
	if(advanced=="Hierarchy"){
	var hierarchy="";
	if($hierarchyVal==undefined){$hierarchyVal=$("#selectHierarchy option:selected").val();}
	if($hierarchyVal=="ADD_PARENT"){hierarchy="Child";}
	else if($hierarchyVal=="ADD_CHILD"){hierarchy="Parent";}
	else if($hierarchyVal=="ADD_PART"){hierarchy="Group";}
	else if($hierarchyVal=="ADD_GROUP"){hierarchy="Part";}
	$relationText="is "+hierarchy+" of";
	}
	$deleteLink.click(function(){
				 $(this).parent('div').parent('div').next('img').remove();
				 $(this).parent('div').parent('div').remove();
							
	});
	var $hiddenField=$('<input  value=""  type="hidden"></input>');
			$deleteLink.append($deleteImg); 
			
			if($source!="NO"){
			$deleteDiv.append($deleteLink);
			}
			
			$hiddenField.val("");
			$hiddenField.attr("sourceConceptName", $sourceConceptName);
			$hiddenField.attr("sourceConceptBedId", $sourceConceptBedId);
			$hiddenField.attr("relationBedId", $relationBedId);
			$hiddenField.attr("relationText", $relationText);
			$hiddenField.attr("selectedConceptBedId",$selectedConceptBedId);
			$hiddenField.attr("selectedConceptName",$selectedConceptName);	
			$hiddenField.attr("entityTripleDefinitionId",$entityTripleDefinitionId);
			$hiddenField.attr("name","hiddenAssociationData"+advanced);
	        $("#assoiationDisplayDiv"+advanced).prepend($rowOuterDiv).prepend("<img src='../images/space.jpg' width='500' height='2'  />");
	        $rowOuterDiv.append($conceptNameDiv.append($sourceConceptName));
	
	        $rowOuterDiv.append($hasRelationDiv.append($relationText));
	
	 
	$rowOuterDiv.append($selectedConceptDiv.append($selectedConceptName));
	$rowOuterDiv.append($hiddenField).append($deleteDiv);
}

function showValidationMessage(message,advanced){
	  $("#validationMessage"+advanced).empty().append(message);
	  setTimeout(function(){$("#validationMessage"+advanced).slideUp().empty();},3000);
	  $("#validationMessage"+advanced).css("color","red");
	  $("#validationMessage"+advanced).slideDown();  
}

function saveHierarchy(){ 
       /**  Data to be saved**/
	   if($("#assoiationDisplayDivHierarchy input[name='hiddenAssociationDataHierarchy']").length==0 && $("#selectedPathDefinitionsHierarchyDiv input[name='selectedPathDefinitionsHierarchy']").length==0){showValidationMessage("No Hierarchy Exist","Hierarchy"); return;}
	   var selectHierarchyType=$("#selectHierarchy option:selected").val();
	   var conceptBedId=$("#businessEntityId").val();
		$request=""
		$request+="&concept.id="+conceptBedId;	
		$request+="&selectedHierarchyType="+selectHierarchyType;	
		$.each($("#assoiationDisplayDivHierarchy input[name='hiddenAssociationDataHierarchy']"),function(k,v){
			$request+="&selectedHierarchyPathDefinitions["+k+"].sourceConceptName="+$(this).attr("sourceConceptName");				
			$request+="&selectedHierarchyPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("sourceConceptBedId");
			$request+="&selectedHierarchyPathDefinitions["+k+"].relationBedId="+$(this).attr("relationBedId");
			$request+="&selectedHierarchyPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("selectedConceptBedid");
			$request+="&selectedHierarchyPathDefinitions["+k+"].destinationConceptName="+$(this).attr("selectedConceptName");
		});		    
		    /**  Original Path definition**/
		      //TODO- need to see whether this section is required or not.
		$.each($("#selectedPathDefinitionsHierarchyDiv input[name='selectedPathDefinitionsHierarchy']"),function(k,v){		
			$request+="&savedHierarchyPathDefinitions["+k+"].entityTripleDefinitionId="+$(this).attr("entityTripleDefinitionId");
			$request+="&savedHierarchyPathDefinitions["+k+"].sourceConceptName="+$(this).attr("sourceConceptName");				
			$request+="&savedHierarchyPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("sourceConceptBedId");
			$request+="&savedHierarchyPathDefinitions["+k+"].relationName="+$(this).attr("relationName");
			$request+="&savedHierarchyPathDefinitions["+k+"].relationBedId="+$(this).attr("relationBedId");
			$request+="&savedHierarchyPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("destinationConceptBedId");
			$request+="&savedHierarchyPathDefinitions["+k+"].destinationConceptName="+$(this).attr("destinationConceptName");
			
		});     
	
		   
			$("#submitButtonAdvanced").hide();
			$("#submitButtonLoaderAdvanced").show();
			alert($request);
		$("#hierarchyDefinitionDiv").hide();
		$("#hierarchyDefinitionDivLoader").show(); 
	   $.ajax({url:"saveHierarchyPathDefinitions.action",
			    data:$request,			    
				success:function(data){showData(data);},
			    type:"post"
			   });
		function showData(data){
		        $("#validationMessageAdvanced").empty(); advancedPathData=data;
		        $("#hierarchyDefinitionDiv").empty().append(data);
				 $("#hierarchyDefinitionDivLoader").hide();
				$("#hierarchyDefinitionDiv").fadeIn('fast');
			    $("#submitButtonAdvanced").show();
			    $("#submitButtonLoaderAdvanced").hide();
			}
	
}
// Hiearchy save End	
function saveRelation(){ 
       /**  Data to be saved**/
	   
	   if($("#assoiationDisplayDivRelation input[name='hiddenAssociationDataRelation']").length==0 && $("#selectedPathDefinitionsRelationDiv input[name='selectedPathDefinitionsRelation']").length==0){showValidationMessage("No Relation Exist","Relation"); return;}
	   
	       var conceptBedId=$("#businessEntityId").val();
		   $request=""
		   $request+="&concept.id="+conceptBedId;	  
			$.each($("#assoiationDisplayDivRelation input[name='hiddenAssociationDataRelation']"),function(k,v){
			$request+="&selectedRelationPathDefinitions["+k+"].sourceConceptName="+$(this).attr("sourceConceptName");				
			$request+="&selectedRelationPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("sourceConceptBedId");
			$request+="&selectedRelationPathDefinitions["+k+"].relationtext="+$(this).attr("relationtext");
			$request+="&selectedRelationPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("selectedConceptBedid");
			$request+="&selectedRelationPathDefinitions["+k+"].destinationConceptName="+$(this).attr("selectedConceptName");
		});		    
		    /**  Original Path definition**/
		    //TODO- need to see whether this section is required or not.
		$.each($("#selectedPathDefinitionsRelationDiv input[name='selectedPathDefinitionsRelation']"),function(k,v){		
			$request+="&savedRelationPathDefinitions["+k+"].entitytripledefinitionid="+$(this).attr("entityTripleDefinitionId");
			$request+="&savedRelationPathDefinitions["+k+"].sourceConceptName="+$(this).attr("sourceConceptName");				
			$request+="&savedRelationPathDefinitions["+k+"].sourceConceptBedId="+$(this).attr("sourceConceptBedId");
			$request+="&savedRelationPathDefinitions["+k+"].relationName="+$(this).attr("relationName");
			$request+="&savedRelationPathDefinitions["+k+"].destinationConceptBedId="+$(this).attr("destinationConceptBedId");
			$request+="&savedRelationPathDefinitions["+k+"].destinationConceptName="+$(this).attr("destinationConceptName");
			
		});
		   
			$("#submitButtonAdvanced").hide();
			$("#submitButtonLoaderAdvanced").show();
			alert($request);

	   $.ajax({url:"saveRelationPathDefinitions.action",
			    data:$request,			    
				success:function(data){showData(data);},
			    type:"post"
			   });
		function showData(data){
		        $("#validationMessageAdvanced").empty(); advancedPathData=data;
		        $("#dynamicPaneRelations").empty().append(data);			  
			    $("#submitButtonAdvanced").show();
			    $("#submitButtonLoaderAdvanced").hide();
			}
	
}
</script>
