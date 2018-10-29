<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet" type="text/css">
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel="stylesheet">
<LINK href="../css/cssPopupMenu.css" rel="stylesheet" type="text/css">

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/jquery.js" language="JavaScript"></script>
<script src="../js/ranges.js" language="JavaScript"></script>
<script src="../js/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script src="../js/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/jquery.layout2.js" type="text/javascript"></script>
<script src="../js/jquery.execue.js" type="text/javascript"></script>
<script src="../js/script.js" type="text/javascript" language="javascript"></script>
<script src="../js/searchList.js" type="text/javascript" language="javascript" /></script>
<script type="text/javascript" src="../js/jquery.execue.searchRecordsComponent.js"></script>
<script type="text/javascript">
var myLayout;
var assetId;
var paginationType="conceptsForCube";
$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  //, west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
  });
  
  // populate js variables for getting other portions of the screen
  //assetId = $("#assetId").val();  
   assetId = '<s:property value="applicationContext.assetId"/>';  
    $("#container").hide();
	   $("#nextButton").hide();
   if(assetId){
      $("#assetId").val(assetId);  
       showConcepts();
       getSelectedDimensions();
	   $("#container").show();
       $("#nextButton").show();
   }
   
    $("#divDimensions").searchRecordsComponent({actionName:"swi/showConceptsForCube.action?baseAsset.id="+assetId,targetDivName:"dynamicPaneCubeConcepts"});
});
function showConcepts(){
  //$.get("swi/showConceptsForCube.action?baseAsset.id="+assetId+"&paginationType="+paginationType, {}, function(data) {
  $.get("swi/showConceptsForCube.action?baseAsset.id="+assetId, {}, function(data) {
      $("#dynamicPaneCubeConcepts").empty();
      $("#dynamicPaneCubeConcepts").append(data);
    });//End of populate BusinessTerms
}
function getSelectedDimensions() {
   //alert(assetId);
  $.post("swi/showSelectedDimensions.action?baseAsset.id="+assetId, {}, function(data) {
    
    $("#rightPane").fadeIn("fast");
    $("#rightPane").empty();
    $("#rightPane").append(data);
    $("#rightPane").show();
    
  });
}

function showSelectedDimensionDetails(dimensionType, index) {
  //alert(assetId +' - '+ dimensionType +' - '+ index);
  var showSelectedConcept = "#showSelectedConcept";
   var loadingShowSelectedConcept = "#loadingShowSelectedConcept";
   
   $(showSelectedConcept+index+"Link").hide();
   $(loadingShowSelectedConcept+index+"Link").show();   
  $.post("swi/showSelectedDimensionDetails.action?baseAsset.id="+assetId+"&dimensionType="+dimensionType+"&selectedIndex="+index, {}, function(data) {
    $(loadingShowSelectedConcept+index+"Link").hide(); 
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $(showSelectedConcept+index+"Link").show();
    
  });
}

function showDimensionDetails(conceptId,conceptBedId) {
  //alert(assetId);
   var showConcept = "#showConcept";
   var loadingShowConcept = "#loadingShowConcept";
   $(showConcept+conceptId+"Link").hide();
   $(loadingShowConcept+conceptId+"Link").show();
   $.post("swi/showDimensionDetails.action?baseAsset.id="+assetId+"&concept.id="+conceptId+"&concept.bedId="+conceptBedId, function(data) {
  
    $(loadingShowConcept+conceptId+"Link").hide();
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $(showConcept+conceptId+"Link").show(); 
    
  });
}

function updateRange() {
  var rangeDefinition = $("#rangeDefinitionForm").serialize();
  var tempdata="";
  var rangeId=$.trim($("#rangeId").val());
  //alert(rangeDefinition);
  $.getJSON("swi/addRangeDimension.action", rangeDefinition ,function(data) {         
    
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    if(parseInt(rangeId)>=0) {
    tempdata=data;
    }else if(rangeId==""){
    	tempdata="Selected dimensions added successfully";
    }
    $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+tempdata+"</div>");
    $("#dynamicPane").show();
    
    getSelectedDimensions();
  });
}

function addDimension() {
  var dimensionDefinition = $("#dimensionDefinitionForm").serialize();
  //alert(dimensionDefinition);
  $.getJSON("swi/addDimension.action", dimensionDefinition ,function(data) {
        
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+data+"</div>");
    $("#dynamicPane").show();
    
    getSelectedDimensions();
  }); 
}
function checkedCube(){
	 var checkd=false;	
     checkd= $("#selectedDimensionsForm input:checkbox").is(':checked');
     if(checkd){
	    $("#enableDeleteCube").show();
	    $("#disableDeleteCube").hide();
	 }
	    else{
	    $("#enableDeleteCube").hide();
	    $("#disableDeleteCube").show();
	    }
}
function deleteSelectedDimensions() {
  var selectedDimensions = $("#selectedDimensionsForm").serialize();
  //alert(selectedDimensions);
   $("#deleteProcess").show();
  $("#enableDeleteCube").hide();
  $.getJSON("swi/deleteSelectedDimensions.action", selectedDimensions ,function(data) {
  
    $("#deleteProcess").hide();
  	$("#disableDeleteCube").show();    
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+data+"</div>");
    $("#dynamicPane").show();
    
    getSelectedDimensions();
  });
}


function showSearch(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables").hide("");
  $("#"+divName+obj+"#roundedSearch").fadeIn("slow");
  
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
  
}
function showSearch2(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables2").hide();
  $("#"+divName+obj+"#roundedSearch2").fadeIn("slow");
  
  $('#searchText2').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText2').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
}

function showAll(searchBox){
  searchString=$("#searchText"+searchBox).val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	var baseAssetId = $("#baseAssetId").val();
	$.get("swi/showSubConceptsForCube.action?baseAsset.id="+baseAssetId,{}, function(data) {
       $("#dynamicPaneCubeConcepts").empty();
       $("#dynamicPaneCubeConcepts").append(data);
     });//End of populate BusinessTerms
	}  
}

function startsWithString(searchBox){

  searchString=$("#searchText"+searchBox).val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	var baseAssetId = $("#baseAssetId").val();
	$.get("swi/showSubConceptsForCube.action?baseAsset.id="+baseAssetId+"&searchString="+searchString+"&searchType=startWith",{}, function(data) {
       $("#dynamicPaneCubeConcepts").empty();
       $("#dynamicPaneCubeConcepts").append(data);
     });//End of populate BusinessTerms
	}  
}

function containsString(searchBox){

searchString=$("#searchText"+searchBox).val().toLowerCase();	
 if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	var baseAssetId = $("#baseAssetId").val();
	$.get("swi/showSubConceptsForCube.action?baseAsset.id="+baseAssetId+"&searchString="+searchString+"&searchType=contains",{}, function(data) {
       $("#dynamicPaneCubeConcepts").empty();
       $("#dynamicPaneCubeConcepts").append(data);
     });//End of populate BusinessTerms
	}  
}

function subList(index_id){

requestedPage=index_id;
var actionName="";
	
		
			actionName="showSubConceptsForCube.action";
			$.post("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
		     $("#dynamicPaneCubeConcepts").empty();
             $("#dynamicPaneCubeConcepts").append(data);
		    var len1,len2=0;
		   
		   // $("#columnListForm .col").width(130);	
		   // $("#columnIno").show();
		   

		  });
			
	   
	 
}

</script>