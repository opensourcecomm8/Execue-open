<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet type="text/css">

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/jquery.js" language="JavaScript"></script>
<script src="../js/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script src="../js/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/jquery.layout.js" type="text/javascript"></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript"></script>
<script src="../js/searchList.js" type="text/javascript"
	language="javascript" /></script>



<script type="text/javascript">
var paginationType="availableAssets";
$(document).ready(function() { 
	//showSearch('divGrain');
    showAssets();
});

function showAssets(){ 
    //$.get("assetListForGrain.action", {paginationType:paginationType},function(data) {
    $.get("assetListForGrain.action", function(data) {
        $("#dynamicPaneAssets").empty();
        $("#dynamicPaneAssets").append(data);
    });//End of populate Assets
}

function getAsset(assetId,displayName) {
    $("#loadingShowGrainLink"+assetId).show();
    $("#showGrainAssetLink"+assetId).hide();
    

    $.get("swi/showAssetGrain.action", {"asset.id" : assetId ,"asset.displayName" : displayName}, function(data) {
        $("#dynamicPane").fadeIn("fast");
        $("#dynamicPane").empty(); 
        $("#loadingShowGrainLink"+assetId).hide(); 
        $("#showGrainAssetLink"+assetId).show();         
        $("#dynamicPane").append(data);
        $("#dynamicPane").show();
        $("#resetAssetGrainsLoader").hide();               
    });
}  

/*
 * Populate the default population and deafult distribution select options 
 *   from the selected options of eligible grain
 * Maintain the previous selection from default population and deafult distribution
 *   if the previously selected item still exists in the newly populated options
 */  
function populateDefaultSelectOptionsByGrainSelection(){  
    
    dPopulationSelectedValue = $('#dPopulation :selected').val();
    dDistributionSelectedValue = $('#dDistribution :selected').val();
    
    $("#dDistribution").empty();
  	$("#dPopulation").empty();
    
    $options='';
    $tempOption = '';  
    $('#mappingId :selected').each(function(i, selected){ 
        grainType = $(selected).text();
  	    mappingID = $(selected).val();
        if (mappingID == dPopulationSelectedValue) {
            $tempOption = "<option selected value='"+mappingID+"'>"+grainType+"</option>";
        } else {
            $tempOption = "<option value='"+mappingID+"'>"+grainType+"</option>";
        }
         
  	    $options += $tempOption  	   
  	});
    $("#dPopulation").append($options);
    
    $options='';
    $('#mappingId :selected').each(function(i, selected){ 
        grainType = $(selected).text();
        mappingID = $(selected).val();
        if (mappingID == dDistributionSelectedValue) {
            $tempOption = "<option selected value='"+mappingID+"'>"+grainType+"</option>";
        } else {
            $tempOption = "<option value='"+mappingID+"'>"+grainType+"</option>";
        }
         
        $options += $tempOption        
    });
    $("#dDistribution").append($options);
    
} 

function resetAssetGrainInfo(){
	var assetId=$("#assetId").val();
	var displayName=$("#displayName").val();
	resetGrain(assetId,displayName);
}
function resetGrain(assetId,displayName) {
     $("#resetAssetGrainsLoader").show();
     $("#resetAssetGrains").hide();    
    $.get("swi/showAssetGrain.action", {"asset.id" : assetId ,"asset.displayName" : displayName}, function(data) {
        $("#dynamicPane").fadeIn("fast");
        $("#dynamicPane").empty(); 
        $("#dynamicPane").append(data);
        $("#dynamicPane").show(); 
        $("#resetAssetGrainsLoader").hide(); 
        $("#resetAssetGrains").show();                    
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
  searchString=$("#searchText").val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText").focus();
	}else{
	$.get("swi/showSubAllAssets.action",{}, function(data) {
       $("#dynamicPaneAssets").empty();
       $("#dynamicPaneAssets").append(data);
     });//End of populate BusinessTerms
	}  
}

function startsWithString(searchBox){

  searchString=$("#searchText").val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText").focus();
	}else{
	$.get("swi/showSubAllAssets.action?searchString="+searchString+"&searchType=startWith",{}, function(data) {
       $("#dynamicPaneAssets").empty();
       $("#dynamicPaneAssets").append(data);
     });//End of populate BusinessTerms
	}  
}

function containsString(searchBox){

searchString=$("#searchText").val().toLowerCase();	
 if(searchString=="search"){
    alert("please enter search string");
	$("#searchText").focus();
	}else{
	$.get("swi/showSubAllAssets.action?searchString="+searchString+"&searchType=contains",{}, function(data) {
       $("#dynamicPaneAssets").empty();
       $("#dynamicPaneAssets").append(data);
     });//End of populate BusinessTerms
	}  
}

function subList(index_id){

requestedPage=index_id;
var actionName="";
	
		
			actionName="showSubAllAssets.action";
			$.post("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
		     $("#dynamicPaneAssets").empty();
             $("#dynamicPaneAssets").append(data);
		    var len1,len2=0;
		   
		   // $("#columnListForm .col").width(130);	
		   // $("#columnIno").show();
		   

		  });
			
	   
	 
}
</script>