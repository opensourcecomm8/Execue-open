<%@page import="com.execue.core.common.type.PaginationType"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<LINK href="../css/admin/cssPopupMenu.css" rel=stylesheet>
<LINK href="../css/admin/autoComplete.css" rel=stylesheet>
<script src="../js/common/jquery.js" language="JavaScript" /></script>
<script src="../js/menun.js" language="JavaScript" /></script>
<script src="../js/common/mm_menu.js" language="JavaScript" /></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/common/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/jquery.execue.js" type="text/javascript"
	language="javascript"></script>
<SCRIPT src="../js/common/jquery.autocomplete.js" type=text/javascript></SCRIPT>
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript"
	src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>

<script>

$(document).ready(function() {
$("#divBTerms").searchRecordsComponent({actionName:"swi/getAttributeRelationsBusinessTerms.action",targetDivName:"dynamicPaneAttributeRelationBTerms"});
 getAttributeRelationsBusinessTerms();
});


function getAttributeRelationsBusinessTerms() {
	  $.get("getAttributeRelationsBusinessTerms.action", function(data){
		   $("#dynamicPaneAttributeRelationBTerms").empty().append(data);
	  });
}
function getConcept(conceptId) {
	  $.get("showAttributeRelationDetails.action?conceptBedId="+conceptId, function(data){
		   $("#dynamicPane").empty().append(data);
	  });
}

function addRow() {
    var association = "is related with";
    var $conBedId = $("#conceptBedId").val();
    var $conName = $("#conceptRow1 a[id='"+$conBedId+"']").attr("attributeName");
	var $relationText = $("#relationType").val();
    var $relationbedId	= $("#relationType").attr('relationBedId');
	var $rowVals = {
		conName			: $conName,
		bedId			: $conBedId,
		attributeName	: '',
		attributeBedId	: '',
		relationText	: $relationText,
		relationbedId	: $relationbedId
	};
  
  if($("#realizedConceptsListDiv").html() != "") {
	  if($("#realizationSelectedId option:selected").length > 0){
		$.each($("#realizationSelectedId option:selected"), function() {
		   	$rowVals.attributeBedId = $(this).attr("attributebedid");
		   	$rowVals.attributeName = $(this).attr("attributeName");
			var checkAssociationExist = checkAssociationExists($rowVals);
			if(checkAssociationExist) {
				createAssociation($rowVals);
			}
		});
	  } else {
	    //var message="Realizations not exist, Please add Realizations.";
	    //showValidationMessage(message);
	  }
  }/* else {
	  var checkAssociationExist = checkAssociationExists("",$relationText,$atributeText,$attributeBedId,advanced);
	  if(checkAssociationExist){
	    createAssociation($rowVals);
	  }
  }*/
				
}

function createAssociation(rowVals) {
	var $rowOuterDiv = $('<div style="padding-left: 2px;width:100%"></div>');
	var $conceptNameDiv = $('<div style="float: left;padding: 3px;"></div>');
	var $hasRelationDiv = $('<div style="float: left;padding: 3px;"></div>');
	var $realizationDiv = $('<div style="float: left;padding: 3px;"></div>');
	var $deleteDiv = $('<div id="deleteDiv1" style="float: left;padding: 3px;"</div>');
	var $deleteImg = $('<img height="16" border="0" title="Delete" alt="Delete" src="../images/admin/disabledIcon.gif">');				
	var $deleteLink = $('<a href="#"></a>');
	$deleteLink.append($deleteImg);
	$deleteDiv.append($deleteLink);	
	
	var $conName = rowVals.conName;
	var $bedId = rowVals.bedId;
	var $relationText = rowVals.relationText;
	var $relationbedId = rowVals.relationbedId;
	var $attributeName = rowVals.attributeName;
	var $attributeBedId = rowVals.attributeBedId;

	$deleteLink.click(function(){
		$(this).parent('div').parent('div').next('img').remove();
		$(this).parent('div').parent('div').remove();
	});

	var $hiddenField = $('<input name="hiddenAssociationData" type="hidden"></input>');
	$hiddenField.attr("conceptName", $conName);
	$hiddenField.attr("conceptBedId", $bedId);
	$hiddenField.attr("relationText", $relationText);
	$hiddenField.attr("relationBedId", $relationbedId);
	$hiddenField.attr("attributeName", $attributeName);
	$hiddenField.attr("attributeBedId", $attributeBedId);

	$("#existingPathDefDiv").prepend($rowOuterDiv).prepend("<img src='../images/admin/space.jpg' width='500' height='2'  />");
	$rowOuterDiv.append($conceptNameDiv.append($conName));
	$rowOuterDiv.append($hasRelationDiv.append($relationText));
	$rowOuterDiv.append($realizationDiv.append($attributeName));	
	$rowOuterDiv.append($hiddenField);
	$rowOuterDiv.append($deleteDiv);
}

function showValidationMessage(message){
	$("#validationMessage").empty().append(message);
   	setTimeout(function(){$("#validationMessage").slideUp().empty();},3000);
  	$("#validationMessage").css("color","red");
 	$("#validationMessage").slideDown();
}

function checkAssociationExists(rowVals){
	var checkAssociationExist = true;
	var inptName = "hiddenAssociationData";
	var attrBedId = rowVals.attributeBedId;
		
	$.each($("#existingPathDefDiv input[name='"+inptName+"']"), function(k, v){
		var conceptbedid = $(this).attr("attributebedid");
		if(conceptbedid == attrBedId) {
			var message="Relation Already Exists for: "+rowVals.attributeName;
			showValidationMessage(message);	
			checkAssociationExist = false;
			// this is a break statement for jquery
			return;
		}
	 });	
	return checkAssociationExist;
}


function saveAdvanceDefinition() {
	var conBedId = $("#conceptBedId").val();
    var conName = $("#conceptRow1 a[id='"+conBedId+"']").text();
	var request = "conceptBedId=" + conBedId;
	request += "&conceptName=" + conName;
	$.each($("#existingPathDefDiv input[name='hiddenAssociationData']"), function(k, v) {
		request += "&selectedPathDefinitions["+k+"].sourceConceptBedId=" + $(this).attr("conceptbedid");
		request += "&selectedPathDefinitions["+k+"].sourceConceptName='" + $(this).attr("conceptname") + "'";
		request += "&selectedPathDefinitions["+k+"].destinationConceptBedId=" + $(this).attr("attributebedid");
		request += "&selectedPathDefinitions["+k+"].destinationConceptName='" + $(this).attr("attributename") + "'";
		request += "&selectedPathDefinitions["+k+"].relationBedId=" + $(this).attr("relationbedid");
		request += "&selectedPathDefinitions["+k+"].relationName='" + $(this).attr("relationtext") + "'";
	});
    /**  Original Path definition**/
	$.each($("#forDeletePathDefDiv input[name='hiddenAssociationDataForDelete']"), function(k,v) {
		request += "&existingPathDefinitions["+k+"].pathDefinitionId=" + $(this).attr("pathDefId");
		request += "&existingPathDefinitions["+k+"].entityTripleDefinitionId=" + $(this).attr("entityTripleDefId");
		request += "&existingPathDefinitions["+k+"].sourceConceptBedId=" + $(this).attr("conceptbedid");
		request += "&existingPathDefinitions["+k+"].sourceConceptName='" + $(this).attr("conceptname") + "'";
		request += "&existingPathDefinitions["+k+"].destinationConceptBedId=" + $(this).attr("attributebedid");
		request += "&existingPathDefinitions["+k+"].destinationConceptName='" + $(this).attr("attributename") + "'";
		request += "&existingPathDefinitions["+k+"].relationBedId=" + $(this).attr("relationbedid");
		request += "&existingPathDefinitions["+k+"].relationName='" + $(this).attr("relationtext") + "'";
	});
	$("#submitButton").hide();
	$("#submitButtonLoader").show();

	$.ajax({url:"updateAttributeRelations.action" ,
	    data: request,			    
		success: function(data) {showData(data);},
	    type: "post"
    });
	function showData(data){
		//$("#validationMessage").empty();		
		$("#dynamicPane").empty().append(data);			  
		//$("#submitButton").show();
		//$("#submitButtonLoader").hide();
	}
	
}
</script>
