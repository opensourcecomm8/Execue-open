<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet" type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel="stylesheet">
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/common/jquery.layout.js"></script>
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.htmlResponse.js"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>
<script type="text/javascript">
var assetId;
var maxDimensions;
var maxMeasures;
$(document).ready(function() {
	// populate js variables for getting other portions of the screen
	//assetId = '<s:property value="applicationContext.assetId"/>';
	assetId= $("#selectAssetId option:selected").val(); 
	maxDimensions = '<s:property value="maxDimensions"/>';
	maxMeasures = '<s:property value="maxMeasures"/>';  
	$("#container").hide();
	if(assetId>0){
		$("#assetId").val(assetId);  
		$("#container").show();
		$("#targetAsset_name").bind("blur", function() { doValidation($("#targetAsset_name")); });
	}
	showTargetAsset();
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
			//$("#createMartForm").attr("action", "..swi/createMart.action?" + params);
			//$("#createMartForm").submit();
			 $.post("swi/createMart.action",params,function(data) {
			    /*$("#targetAssetDynaminPane").fadeIn("fast");
			    $("#targetAssetDynaminPane").empty();
			    $("#targetAssetDynaminPane").append(data);			 	   
			    $('#createMartButtonId').removeAttr('onclick');
			    $("#targetAssetDynaminPane").show();*/
			     if(data.status){
			       $("#errorMessageId").empty();
				    $("#actionMessageId").empty().append("Mart creation request is in process. Please check the <a href='getJobStatus.action?jobRequest.id="+data.jobRequestId+"'>Status </a>");	
				    $('#createMartButtonId').removeAttr('onclick'); 
				  }else{
				    showErrorMessages(data.errorMessages);
				  }
				  $("#targetAssetDynaminPane").show();
	      }); 
		}
	}
}
function showErrorMessages(errorMessages){
    $.each(errorMessages, function () {
			$("#errorMessageId").empty().append("<li>"+this+"<\li>");	    
      });
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
	 if(stringEmpty){
	       $("#assetNameMessage").text("Dataset Name "+stringEmptyMessage).show(); 
				setFocus(obj);
		   return false; 
	  }
				
	    /*var splCharExist=checkSpecialChar(obj);
			if(splCharExist){				
				$("#assetNameMessage").text(specialCharMessage).show();
				setFocus(obj);
				return false;

			}else{
				$("#assetNameMessage").hide();
			} */
	return true;
}

function showTargetAsset() {
  $.get("swi/showMartTargetAsset.action",{},function(data) {
    $("#targetAssetDynaminPane").fadeIn("fast");
    $("#targetAssetDynaminPane").empty();
    $("#targetAssetDynaminPane").append(data);
    $("#targetAssetDynaminPane").show();
  });
  }
// user interaction methods end
</script>