<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet" type="text/css">
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel="stylesheet">
<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/jquery.execue.js"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/jquery.layout.js"></script>
<script type="text/javascript" src="../js/execue.commons.js"></script>
<script type="text/javascript" src="../js/thickbox.js"></script>
<script type="text/javascript" src="../js/jquery.execue.htmlResponse.js"></script>
<script type="text/javascript" src="../js/jquery.execue.searchRecordsComponent.js"></script>
<script type="text/javascript">
var assetId;
var maxDimensions;
var maxMeasures;
$(document).ready(function() {
	// populate js variables for getting other portions of the screen
	assetId = '<s:property value="applicationContext.assetId"/>';
	maxDimensions = '<s:property value="maxDimensions"/>';
	maxMeasures = '<s:property value="maxMeasures"/>';  
	$("#container").hide();
	if(assetId){
		$("#assetId").val(assetId);  
		$("#container").show();
		$("#targetAsset_name").bind("blur", function() { doValidation($("#targetAsset_name")); });
	}
});
// user navigational methods start
function createMart() {
	var validationFlag = doValidation($("#targetAsset_name"));
	if(validationFlag){
		var $existingConcepts = "";
		var selectedFilteredMConcepts = $("#selectedProminentMeasures").find("option");
		var selectedFilteredDConcepts = $("#selectedProminentDimensions").find("option");
		if(selectedFilteredDConcepts.length == 0 || selectedFilteredMConcepts.length == 0){
			alert("Please select atleast one Prominent Dimension & Measure");
		} else {
			$.each(selectedFilteredMConcepts, function (k, item) {
				$existingConcepts += "&selectedProminentMeasures["+k+"].name="+$(this).text();		
			});
			$.each(selectedFilteredDConcepts, function (k, item) {
				$existingConcepts += "&selectedProminentDimensions["+k+"].name="+$(this).text();	
			});
			var params = $("#createMartForm").serialize() + $existingConcepts;
			$("#createMartForm").attr("action", "..swi/createMart.action?" + params);
			$("#createMartForm").submit();
			//$.post("..swi/createMart.action?" + params);
		}
	}
}
// user navigational methods end
// user interaction methods begin
function moveDimensionConcept() {
	var eligibleFilteredConcepts = $("#eligibleProminentDimensions").find("option:selected");
	var selectedFilteredConcepts = $("#selectedProminentDimensions").find("option");
	var msg = "The dimension list can't exceed " + maxDimensions;
	if(eligibleFilteredConcepts.length == 0) {
		alert("Please select some dimension!");
	} else if (selectedFilteredConcepts.length >= maxDimensions) {
		alert(msg);
	} else if (eligibleFilteredConcepts.length > maxDimensions) {
		alert(msg);
	} else if (eligibleFilteredConcepts.length + selectedFilteredConcepts.length > maxDimensions) {
		alert(msg);
	} else {
		$.each(eligibleFilteredConcepts, function(k, item) {
			var id = $(this).attr('value');
			var name = $(this).text();
			var $option = '<option value="'+id+'">'+name+'</option>';
			$("#selectedProminentDimensions").append($option);
			$("#eligibleProminentDimensions option[value='"+id+"']").remove();
		});	
	}
}

function removeDimensionConcept() {
	var selectedFilteredConcepts = $("#selectedProminentDimensions").find("option:selected");
	$.each(selectedFilteredConcepts, function(k, item) {
		var id = $(this).attr('value');
		var name = $(this).text();
		var $option = '<option value="'+id+'">'+name+'</option>';
		$("#eligibleProminentDimensions").append($option);
		$("#selectedProminentDimensions option[value='"+id+"']").remove();
	});
}

function moveMeasureConcept() {
	var eligibleFilteredConcepts = $("#eligibleProminentMeasures").find("option:selected");
	var selectedFilteredConcepts = $("#selectedProminentMeasures").find("option");
	var msg = "The measure list can't exceed " + maxMeasures;
	if(eligibleFilteredConcepts.length == 0) {
		alert("Please select some measure!");
	} else if (selectedFilteredConcepts.length >= maxMeasures) {
		alert(msg);
	} else if (eligibleFilteredConcepts.length > maxMeasures) {
		alert(msg);
	} else if (eligibleFilteredConcepts.length + selectedFilteredConcepts.length > maxMeasures) {
		alert(msg);
	} else {
		$.each(eligibleFilteredConcepts, function(k, item) {
			var id = $(this).attr('value');
			var name = $(this).text();
			var $option = '<option value="'+id+'">'+name+'</option>';
			$("#selectedProminentMeasures").append($option);
			$("#eligibleProminentMeasures option[value='"+id+"']").remove();
		});
	}
}

function removeMeasureConcept() {
	var selectedFilteredConcepts = $("#selectedProminentMeasures").find("option:selected");
	$.each(selectedFilteredConcepts, function(k, item) {
		var id = $(this).attr('value');
		var name = $(this).text();
		var $option = '<option value="'+id+'">'+name+'</option>';
		$("#eligibleProminentMeasures").append($option);
		$("#selectedProminentMeasures option[value='"+id+"']").remove();
	});
}

function doValidation(obj){
	var top=obj.position().top;
	var left=obj.position().left;
	var stringEmpty=checkEmptyString(obj);
				$("#assetNameMessage").css("top",top-8+"px");
				$("#assetNameMessage").css("left",left+obj.width()+10+"px");
	if(stringEmpty){$("#assetNameMessage").text("Dataset Name "+stringEmptyMessage).show(); 
				setFocus(obj);
				return false; }
	var splCharExist=checkSpecialChar(obj);
			if(splCharExist){				
				$("#assetNameMessage").text(specialCharMessage).show();
				setFocus(obj);
				return false;

			}else{
				$("#assetNameMessage").hide();
			} 
			return true;
}
// user interaction methods end
</script>