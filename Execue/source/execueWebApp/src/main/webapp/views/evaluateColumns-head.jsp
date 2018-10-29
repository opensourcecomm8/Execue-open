<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="../css/redmond/jquery-ui-1.7.1.custom.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="../css/ui.jqgrid.css" />
<style type="text">
   html, body {
   margin: 0;   /* Remove body margin/padding */
   padding: 0;
   overflow: hidden; /* Remove scroll bars on browser window */
   font-size: 75%;
   }
</style>
<LINK href="../css/roundedSearch.css" rel=stylesheet>

<script src="../js/jquery.js" language="JavaScript" /></script>
<script src="../js/jquery.ui.all.js" language="JavaScript" /></script>
<script src="../js/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/script.js" type="text/javascript"
 language="javascript" /></script>
<script src="../js/searchList.js" type="text/javascript"
 language="javascript" /></script>
<script src="../js/jquery.execue.js" language="JavaScript" /></script>
<script type="text/javascript" src="../js/jquery.execue.jobStatus.js"></script>
<!-- JQ GRID INCLUDES -->
<script src="../js/i18n/grid.locale-en.js" type="text/javascript"></script>
<script src="../js/jquery.jqGrid.min.js" type="text/javascript"></script>
<script src="../js/jquery.json.min.js" type="text/javascript"></script>
<script src="../js/jquery.execue.UIPublishedFileTable.js" type="text/javascript"></script>
<script type="text/javascript">



var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
/*//pagination 
var resetPageNo=1;
var normalFlow=false;
var sameData=true;
var confirmChanges=true;
var requestedPage="";
var collistWidth=490; 
var resetFlag=false;
var $formatSelection=undefined;
var $unitSelection=undefined;

$(document).ready(function() {
 
   var  tableId='<c:out value="${publishedFileTableId}"/>';    
   if(tableId){
    showEvaluatedColumns(tableId);
   }
});*/

/* -- The below methods have been taken over by jqGrid implementation --
function updateEvaluateColumnTable(){ 
//alert($('#evaluateColumnForm').serialize()); 
$("#confirmButtonLoader").show();
$("#confirmButton").hide();
      $.post("publisher/updateEvaluatedColumns.action?requestedPage="+requestedPage,$('#evaluateColumnForm').serialize(),function(data) {
		   $("#dynamicPane").empty();
			$("#dynamicPane").fadeIn("fast");    
			$("#dynamicPane").append(data);
			
			$("#dynamicPane").show();	
	});
 
}
function subList(index_id){
var fileTableInfoId=$("#fileTableInfoId").val();
requestedPage=index_id;
var actionName="";
	if(!sameData){
		if(resetFlag){ //avoiding alert when we hit reset button
			normalFlow=true;
		}else{
			confirmChanges= confirm("Your changes are not saved.\n\nDiscard your changes?");
			if(!confirmChanges){return false;}
		}
	}else {
		normalFlow=true;
	}
	if(confirmChanges || normalFlow){
	resetPageNo=index_id; //on resetting user stays in current page		
			actionName="getEvaluatedSubColumnsInfo.action";
			$.post("publisher/"+actionName+"?requestedPage="+index_id+"&fileTableInfoId="+fileTableInfoId, function(data) {
		     $("#dynamicPane").empty();
			$("#dynamicPane").fadeIn("fast");    
			$("#dynamicPane").append(data);
			$("#dynamicPane").show();
			});	   
	  }else{
	 	 return;
	  }
}
function resetFields(){
    //resetFlag=true;//used for pagination
	$("#enableRestColumn").hide();
	$("#restColumnProcess").show();
	showColumnInfo(tblName,"NO");
	//resetFlag=false;
	sameData=true;
}
function showSearch(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables").hide("");
  $("#"+divName+obj+"#roundedSearch").fadeIn("slow");
  $('#searchText').val("");
   $('#searchText').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
  });
  $('#searchText').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
  $('#searchText').keydown(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
     showAll();
    }
  });
}*/

/** --This is depricated now. Has a new method with different implemention related to ids but not logic--
 * correct the data precision if any data type is changed to a char field.
 * length shoudl become the sum of precison, scale and finally incremented by 1 to take care of "."
 * the above logic should only be applied if the scale is more than 0
 *
function correctDataLengthFields(index) {
  $evalDataType = $("#subListForEvaluatedColumsInfo"+index+"DataType").val();
  $finalPrecision = 0;
  $evalScale = $("#subListForEvaluatedColumsInfo"+index+"Scale").val();
  $evalPrecision = $("#subListForEvaluatedColumsInfo"+index+"Precition").val();
  if ($evalDataType == 'NUMBER') {
  if ($evalScale == 0) {
   $("#subListForEvaluatedColumsInfo"+index+"Scale").val(2);
   $evalScale = 2; 
  }
    $finalPrecision = eval($evalPrecision*1) + eval($evalScale*1) + eval(1*1);      
   $("#subListForEvaluatedColumsInfo"+index+"Precition").val($finalPrecision);
  }
    
  if ($evalDataType == 'CHARACTER' || $evalDataType == 'STRING') {
    if ($evalScale > 0) {      
      $finalPrecision = eval($evalPrecision*1) + eval($evalScale*1) + eval(1*1);
      $("#subListForEvaluatedColumsInfo"+index+"Scale").val(0);
      $("#subListForEvaluatedColumsInfo"+index+"Precition").val($finalPrecision);
    }
  }
  
   if($evalDataType=='DATE' ||$evalDataType=='DATETIME'||$evalDataType=='TIME'){
      $("#subListForEvaluatedColumsInfo"+index+"Precition").attr("readonly", true);
      $("#subListForEvaluatedColumsInfo"+index+"Scale").attr("readonly", true);
  }else{
     $("#subListForEvaluatedColumsInfo"+index+"Precition").attr("readonly", false);
     $("#subListForEvaluatedColumsInfo"+index+"Scale").attr("readonly", false);
  }
}// End of correctDataLengthFields(index) method;
// no more to be used, has a new duplicate method as populateFormatAndUnit
function getCorrespondingFormatsAndUnits(index,unitType){
 $formatSelection = $('<select id="subListForEvaluatedColumsInfo'+index+'Format" name="subListForEvaluatedColumsInfo['+index+'].format"></select>');	
 $unitSelection = $('<select id="subListForEvaluatedColumsInfo'+index+'Unit" name="subListForEvaluatedColumsInfo['+index+'].unit"></select>');	
 $option='';
 $optionUnit=''; 
if(unitType=='NULL'){
  $unitSelection.append($optionUnit);
  $formatSelection.append($option);	
  $("#format"+index).empty().append($formatSelection);
  $("#unit"+index).empty().append($unitSelection);  
}else{
	 $.getJSON("../swi/qiConversion.action", {displayType : unitType}, function(data) {  
	    if(data.qiConversion){    
				if (data.qiConversion.dataFormats) {											
					$.each(data.qiConversion.dataFormats, function(i, dataFormats) {
							$option+="<option value='"+dataFormats.name+"'>"+dataFormats.displayName+"</option>"											
					});						
					$formatSelection.append($option);
					 $("#format"+index).empty();
					 $("#format"+index).append($formatSelection);					
					 $("#format"+index).show();					
			   }
			   if (data.qiConversion.units) {		 	   
					$.each(data.qiConversion.units, function(j, units) {
							$optionUnit+="<option value='"+units.name+"'>"+units.displayName+"</option>"													
					});	
					$unitSelection.append($optionUnit);	
					 $("#unit"+index).empty();								
					 $("#unit"+index).append($unitSelection);
					 $("#unit"+index).show();	
			     }
		  }
	   
	 });
  }
}
// not to be used any more. This work is done by the include tag in main jsp page.
function showEvaluatedColumns(fileTableInfoId){
$("#loadingShowPublisheFiledLink"+fileTableInfoId).show();
 $.post("publisher/getEvaluatedColumns.action?fileTableInfoId="+fileTableInfoId,function(data) {
		   $("#dynamicPane").empty();
			$("#dynamicPane").fadeIn("fast");
			$("#dynamicPane").append(data);
			$("#loadingShowPublisheFiledLink"+fileTableInfoId).hide();
			$("#absorbEvaluatedDataLink").show();
			$("#dynamicPane").show();	
	});
}*/

function submitForAbsorption () {
    var fileInfoId = $("#fileInfoId").val();
    var jobRequestId = $("#jobRequestId").val();
   	//var confirmed = confirm("Do you want to absorb the Dataset?");
	//if(confirmed){
   	absorbSource(jobRequestId);
   	//}
}

function absorbSource(jobRequestId){
	 var jobRequestId = $("#jobRequestId").val();
	 $("#disableGrid").fadeIn();
	 $("#submitDiv td[id='pager_left']").hide();
	 $("#submitDiv td[id='pager_right']").hide();
	 $("#absorbtionDiv").empty();
	 $("#absorbtionDiv").jobStatus({
	 	requestJobURL : "../publisher/invokeAbsorbEvaluatedData.action",
	 	postDataForJobParam : function () {
	 			var data = {};
	 			data.jobRequestId = jobRequestId;	 			
	 			return data;
	 			},
	 	postCall : function(jobid,status) {updateAbsorbtionInfo(jobid,status)}
	 });
} 

function updateAbsorbtionInfo(jobid,status) {
	// specifc code for showing specific
	if(status=="SUCCESS") {
		/*$pubDiv = $("#absorbtionDiv");
		$pubDiv.show();
		$pubDiv.empty();
		var $inp = $("<input type='button' value='Dashboard Screen' >").addClass("singleButton")
					.click(function(){ window.location="../swi/showAssetsDashboard.action";	});
		$pubDiv.append($inp);*/
		$("#submitDiv td[id='pager_right'] div[class='ui-pg-div']").remove();
		var $absorbButton = $("#submitDiv td[id='pager_right'] td[id='absorbButton']");
		var $spanButton = $("<span>").addClass("ui-icon ui-icon-arrowreturnthick-1-e");
		//$absorbButton.empty().append($spanButton).append("Publish Dataset")
					//.click(function(){ window.location = "../swi/showPublishDatasets.action"; });
		$("#submitDiv td[id='pager_right']").show();
		$("#disableGrid span").html("Process Completed");
		$processType=$("#publishedFileInfo_publisherProcessType").val();
		if($processType=="SIMPLIFIED_PUBLISHER_PROCESS"){
		window.location = "../swi/showPublishDatasets.action"; 
		}else{
		window.location = "../swi/showMappings.action"; 	
		}
	} else {
		$("#disableGrid").fadeOut();
	 	$("#submitDiv td[id='pager_left']").show();
	 	$("#submitDiv td[id='pager_right']").show();
	}
}
/* deprecated because of the use of a new implementation for the same
 * this is beacuase of the jqGrid implemented for the new screen.
function handleCorrespondingDefaultMetricAndGrain(index,kdxType){
  if(kdxType=='MEASURE'){
    $("#subListForEvaluatedColumsInfo"+index+"defaultMetric").removeAttr("disabled"); 
    $("#subListForEvaluatedColumsInfo"+index+"granularity option[value='NA']").attr('selected', 'selected');   
    $("#subListForEvaluatedColumsInfo"+index+"granularity").attr("disabled",true);  
  }else if(kdxType=='DIMENSION' || kdxType=='ID'){
     $("#subListForEvaluatedColumsInfo"+index+"granularity").removeAttr("disabled");
     $("#subListForEvaluatedColumsInfo"+index+"defaultMetric").attr("disabled","disabled");
     $("#subListForEvaluatedColumsInfo"+index+"defaultMetric").attr('checked', false);    
  }else{
     $("#subListForEvaluatedColumsInfo"+index+"defaultMetric").attr("disabled","disabled"); 
     $("#subListForEvaluatedColumsInfo"+index+"granularity").attr("disabled",true); 
     $("#subListForEvaluatedColumsInfo"+index+"granularity option[value='NA']").attr('selected', 'selected');
     $("#subListForEvaluatedColumsInfo"+index+"defaultMetric").attr('checked', false);   
  }
}*/
</script>