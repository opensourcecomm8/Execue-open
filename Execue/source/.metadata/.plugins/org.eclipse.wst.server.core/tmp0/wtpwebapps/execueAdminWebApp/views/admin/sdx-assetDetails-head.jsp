<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet type="text/css">

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/common/jquery.js" language="JavaScript"></script>
<script src="../js/admin/menun.js" language="JavaScript"></script>
<script src="../js/common/mm_menu.js" language="JavaScript"></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/common/jquery.layout.js" type="text/javascript"></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript"></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>


<script type="text/javascript">
var paginationType="availableAssets";
$(document).ready(function() { 
	//showSearch('divGrain');
     showAssets();
});

function showAssets(){
    //$.get("assetListForAssetDetails.action", {paginationType:paginationType}, function(data) {
    $.get("assetListForAssetDetails.action", {}, function(data) {
     $("#dynamicPaneAssets").empty();
     $("#dynamicPaneAssets").append(data);
    });//End of populate Assets
}

function getAssetDetail(assetId,displayName,discription){
 $("#loadingShowAssetLink"+assetId).show();
 $("#showAssetLink"+assetId).hide();
 $.get("swi/showAssetDetailInfo.action", {assetId : assetId ,assetDisplayName : displayName,assetDescription : discription}, function(data) {
        $("#dynamicPane").fadeIn("fast");
        $("#dynamicPane").empty(); 
        $("#loadingShowAssetLink"+assetId).hide(); 
        $("#showAssetLink"+assetId).show();         
        $("#dynamicPane").append(data);
        $("#dynamicPane").show();                       
    });
}



/*
 * Populate the default population and deafult distribution select options 
 *   from the selected options of eligible grain
 * Maintain the previous selection from default population and deafult distribution
 *   if the previously selected item still exists in the newly populated options
 */  


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
	$.get("swi/assetSubListForAssetDetails.action",{}, function(data) {
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
	$.get("swi/assetSubListForAssetDetails.action?searchString="+searchString+"&searchType=startWith",{}, function(data) {
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
	$.get("swi/assetSubListForAssetDetails.action?searchString="+searchString+"&searchType=contains",{}, function(data) {
       $("#dynamicPaneAssets").empty();
       $("#dynamicPaneAssets").append(data);
     });//End of populate BusinessTerms
	}  
}

function subList(index_id){

requestedPage=index_id;
var actionName="";
	
		
			actionName="assetSubListForAssetDetails.action";
			$.post("swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
		     $("#dynamicPaneAssets").empty();
             $("#dynamicPaneAssets").append(data);
		    var len1,len2=0;
		   
		   // $("#columnListForm .col").width(130);	
		   // $("#columnIno").show();
		   

		  });
			
	   
	 
}

</script>
