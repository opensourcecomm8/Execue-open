<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<LINK href="../css/admin/cssPopupMenu.css" rel="stylesheet" type="text/css" />
<script src="../js/common/jquery.js" language="JavaScript" /></script>
<script src="../js/admin/menun.js" language="JavaScript" /></script>
<script src="../js/common/mm_menu.js" language="JavaScript" /></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/admin/jquery.layout2.js" type="text/javascript" /></script>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/jquery.execue.js" type="text/javascript"
	language="javascript"></script>
<script src="../js/admin/searchList.js" type="text/javascript" language="javascript" /></script>
<script type="text/javascript" src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>
<script src="../js/admin/jquery.execue.helpMessage.js" ></script>
 
<script type="text/javascript">
var myLayout;
var paginationType="conceptsForRanges";
$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
	//,	west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
  });
  showConcepts();
  getUserRanges();
  
  $("#divRanges").searchRecordsComponent({actionName:"swi/showSubConceptsforRanges.action",targetDivName:"dynamicPaneConcepts"});
});
$tmpRangeStatus="";
$tempRangeConceptStatus="";


function showConcepts(){
  //$.get("swi/showConceptsforRanges.action?paginationType="+paginationType, function(data) {
  $.get("swi/showConceptsforRanges.action", function(data) {
      $("#dynamicPaneConcepts").empty();
      $("#dynamicPaneConcepts").append(data);
    });//End of populate BusinessTerms
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


function getRangesForConcept(conceptID,conceptBedId, conceptName) {
	if($tempRangeConceptStatus!=conceptID){
		var rangeConept="#showConcept";
		var loadingRangeConcept="#loadingShowConcept";		
		$(rangeConept+conceptID+"Link").hide();
		$(loadingRangeConcept+conceptID+"Link").show();	
  	$.post("swi/retrieveRange.action",{conceptID:conceptID,conceptBedId:conceptBedId,conceptName:conceptName}, function(data) {
  		$(loadingRangeConcept+conceptID+"Link").hide();
	    $("#dynamicPane").empty();
	    $("#dynamicPane").fadeIn("fast");
	    $("#dynamicPane").append(data);
	    $("#dynamicPane").show();
	    $(rangeConept+conceptID+"Link").show();
  });
  $tempRangeConceptStatus=conceptID;
  $tmpRangeStatus="";
  }
}

var inputVal="Description";
function getRangeById(rangeID){
	if($tmpRangeStatus!=rangeID){
		var userRange="#showUserRange";
		var loadingUserRange="#loadingShowUserRange";
		$(userRange+rangeID+"Link").hide();
		$(loadingUserRange+rangeID+"Link").show();	
  	$.post("swi/retrieveRangeById.action?rangeId="+rangeID, {}, function(data) {
	  	$(loadingUserRange+rangeID+"Link").hide();
	    $("#dynamicPane").empty();
	    $("#dynamicPane").fadeIn("fast");
	    $("#dynamicPane").append(data);
	    $("#dynamicPane").show();
	    $(userRange+rangeID+"Link").show();
		

		$("input[desc='Yes']").live("click",function(){
											if(	$(this).val()==inputVal){
												$(this).val("");  
											}
												  });
		$("input[desc='Yes']").live("blur",function(){
													if(	$(this).val()==""){
												$(this).val(inputVal);  
																					}
												  });
		
  });
  $tmpRangeStatus=rangeID;
  }
}

function getUserRanges(){
  $.post("swi/userRanges.action", {}, function(data) {
    $("#rightPane").empty();
    $("#rightPane").append(data);
    $("#rightPane").show();
  });
}
    
function updateRange() {
 // var rangeDefinition = $("#rangeDefinitionForm").serialize();
 var rangeDefinition ="";
 rangeDefinition = "range.name="+$("#name").val();
 rangeDefinition=rangeDefinition+"&range.id="+$("#rangeDefinitionForm_range_id").val();
 rangeDefinition=rangeDefinition+"&range.conceptBedId="+$("#rangeDefinitionForm_range_conceptBedId").val();
 
 var checkedStatus=$("#statusCheckboxId").is(":checked");
 if(checkedStatus){
  rangeDefinition=rangeDefinition+"&range.enabled=true";
  }else{
  rangeDefinition=rangeDefinition+"&range.enabled=false";
  }
 	
	$.each($("#rangesTable tr"),function(k,v){

		var rId=$(this).attr("id");
		var lowerLimitInput_current=$("tr#"+rId).find("input").eq(0).val();;
		var upperLimitInput_current=$("tr#"+rId).find("input").eq(1).val();;
		var descInput_current=$("tr#"+rId).find("input").eq(2).val();
		var rangeId=$("tr#"+rId).find("input").eq(2).attr("rangeId");
		
		rangeDefinition=rangeDefinition+"&rangeDetailList["+k+"].lowerLimit="+lowerLimitInput_current;
		rangeDefinition=rangeDefinition+"&rangeDetailList["+k+"].upperLimit="+upperLimitInput_current;
		rangeDefinition=rangeDefinition+"&rangeDetailList["+k+"].description="+descInput_current;
		
		rangeDefinition=rangeDefinition+"&rangeDetailList["+k+"].id="+rangeId;
		rangeDefinition=rangeDefinition+"&rangeDetailList["+k+"].order="+k;
		rangeDefinition=rangeDefinition+"&rangeDetailList["+k+"].value="+k;
		
	 });
 // alert(rangeDefinition);
  	$("#updateProcess").show();
    $("#enableUpdateRange").hide();
  $.getJSON("swi/updateRange.action", rangeDefinition ,function(data) {         
    if (data.status == 'success' && $("#rangeDefinitionForm_range_id").val() == '') {
      // getRangeById(data.rangeId);
      // $("#rangeDefinitionForm_range_id").val(data.rangeId);
      $("#enableUpdateRange").show();
  	  $("#updateProcess").hide();
      $("#dynamicPane").fadeIn("fast");     
      $("#dynamicPane").empty();
      $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+data.message+"</div>");
      $("#dynamicPane").show();
    } else {
    	$("#enableUpdateRange").show();
  	  $("#updateProcess").hide();
      $("#errorMessage").empty();
      $("#errorMessage").append(data.message);
    }
    getUserRanges();
    $tmpRangeStatus="";
   $tempRangeConceptStatus="";
  });
}
    
function deleteRange() {
var answer = confirm("Are you sure?")
   if (answer){ 
     $request=""; 
     $('#userDefinedRangesForm input:checkbox:checked').each(function(k,v) {      
       $request+="&rangeIdsForDeletion["+k+"]="+$(this).val();                 
     });
	  $("#deleteProcess").show();
	  $("#enableDeleteSelect").hide();
	  $.getJSON("swi/deleteRanges.action",  $request ,function(data) {         
	    
	    $("#deleteProcess").hide();
	  	$("#disableDeleteSelect").show();
	    $("#dynamicPane").fadeIn("fast");
	    $("#dynamicPane").empty();
	    $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+data.message+"</div>");
	    $("#dynamicPane").show();
	    getUserRanges();
	  });
	   $tmpRangeStatus="";
	   $tempRangeConceptStatus="";
   }
   return false;
}
function checkedRanges(){
	 var checkd=false;	
     checkd= $("#userDefinedRangesForm input:checkbox").is(':checked');
     if(checkd){
	    $("#enableDeleteSelect").show();
	    $("#disableDeleteSelect").hide();
	 }else{
	    $("#enableDeleteSelect").hide();
	    $("#disableDeleteSelect").show();
	}
}



function subList(index_id){

requestedPage=index_id;
var actionName="";
	
		
			actionName="showSubConceptsforRanges.action";
			$.post("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
		     $("#dynamicPaneConcepts").empty();
             $("#dynamicPaneConcepts").append(data);
		    var len1,len2=0;
		   
		   // $("#columnListForm .col").width(130);	
		   // $("#columnIno").show();
		   

		  });
			
	   
	 
}
</script>
