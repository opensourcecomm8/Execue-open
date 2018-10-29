<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<LINK href="../css/admin/cssPopupMenu.css" rel=stylesheet>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script src="../js/common/jquery.js" language="JavaScript"></script>
<script src="../js/admin/menun.js" language="JavaScript"></script>
<script src="../js/common/mm_menu.js" language="JavaScript"></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript"></script>
<script src="../js/admin/jquery.layout2.js" type="text/javascript"></script>
<script src="../js/admin/searchList.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript"></script>
    
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>

<style>
.nolink {
	text-decoration: none;
	color: gray;
	cursor: text;
	a: active :                   gray
}
</style>
<script type="text/javascript"><!--

var TabbedPanels1;

var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method

var assetId;
var assetName;
var tblName;
var virtualFlag=false;
var virtualSaved=false;
//reset variables in pagination
var resetPageNo=1;
var resetFlag=false;
//alert variable in pagination
var normalFlow=false;
var sameData=true;
var confirmChanges=true;
var collistWidth=490;
var paginationType="";
var requestedPage="";

/*function showSearch(divName){
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
  
 
  
}
function showSearch2(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables2").hide();
  $("#"+divName+obj+"#roundedSearch2").fadeIn("slow");
   $('#searchText2').val("");
  $('#searchText2').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
 
}*/



$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
  });
  
  //Tabbed Panels should only be defined when the corresponding div is loaded
  TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");	
  // populate js variables for getting other portions of the screen
  assetId = $("#assetId").val();
  assetName = $("#assetName").val();
  var assetOrgin = $("#assetOrigin").val();
  // get the asset screen
  getAssetDefinition();
 // showSearch2('divJoins','span.tableHolder');
 // showSearch('divExistingJoins','span.tableHolder');
  // if asset is an existing asset then get left panel and right panel data as well
  if (assetId != '') {
    
    getAssetTables();
	getSourceTables();
	$("#advancedOptionsDiv").show();
	$("#divSearchSourceTables").searchRecordsComponent({actionName:"../swi/showSubSourceTables.action?asset.id="+assetId+"&asset.name="+assetName,targetDivName:"sourceTableList"});
	$("#divAssetTableList").searchRecordsComponent({actionName:"",targetDivName:"assetTableList"});
	
	
	$(document).ajaxError(function(e,xhr,settings) {
		$("#assetTableList").text("unable to process");
		$("#loadingATLink").hide();
		//alert(settings.url);
		

});
	//$("#showDasetCollectionLink").show();
  }else{
	/*	$(".ui-layout-west,.ui-layout-east,.ui-layout-resizer-west,.ui-layout-resizer-east").hide();
		$(".ui-layout-center").width(900);
		$(".ui-layout-center").css("left","10px");
		$(".ui-layout-center").css("float","left");*/
		//$("#showDasetCollectionLink").hide();
		$("#divJoins").hide();
		$("#divExistingJoins").hide();
		$("#disableDeleteAsset").hide();
	}
	
   if($("#selectAssetId option").length>2){
	$("#showDasetCollectionLink").show();	
	}else{
	$("#showDasetCollectionLink").hide();
	}
	
	searcTextFocusInitialise();
 //$(tableValueChange); // for change of data in column info tab
  /* $('#searchText2').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
   $('#searchText2,#searchText').click(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == 'Search'){
      $(this).attr('value', '');
      $(this).css('color', '#777');
    }
  });
  
  $('#searchText2').keydown(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
     showAll(2);
    }
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
  });*/
  
});

function getAssetDefinition() {
  $.post("../swi/getAssetDefinition.action?asset.id="+assetId+"&asset.name="+assetName, {}, function(data) {
   // $("#updateProcess").hide();
  // $("#enableUpdateAsset").show();
    $("#assetIno").fadeIn("fast");
    $("#assetIno").empty();
    $("#assetIno").append(data);
    $("#assetIno").show();
    
  });
}

function createUpdateAsset(operation) {
$('.links').bind('click', disableLink).addClass('nolink');
   $("#updateProcess").show();
   $("#enableUpdateAsset").hide();
   $("#enableCreateAsset").hide();     
  var  checkd= $("#assetDefinitionForm input[id=queryExecutionAllowedId]:checkbox").is(':checked');    
  var assetData = $("#assetDefinitionForm").serialize();
  if(!checkd){
    assetData=assetData+"&asset.queryExecutionAllowed=NO";
  }else{   
     assetData=assetData+"&asset.queryExecutionAllowed=YES";
  }    
  $.post("../swi/createUpdateAsset.action", assetData, function(data) {
    $("#updateProcess").hide();
   	$("#enableUpdateAsset").show();
    $("#assetIno").fadeIn("fast");
    $("#assetIno").empty();
    $("#assetIno").append(data);
    $("#assetIno").show();
    if(!checkd){
       $("#assetDefinitionForm input[id=queryExecutionAllowedId]:checkbox").attr('checked', false);
    }
    if (operation == 'create' && $("#assetIdFromAssetDefinition").val() != '') { 
    	//$("#updateProcess").show();
      $("#enableCreateAsset").hide();
      $("#assetId").val($("#assetIdFromAssetDefinition").val());
      $("#assetName").val($("#assetNameFromAssetDefinition").val());
      assetId = $("#assetId").val();
      assetName = $("#assetName").val();
      // alert(assetId + " - " + assetName);
      if (operation == 'create') {
        // alert('calling get source tables');
        getSourceTables();
		$(".ui-layout-west,.ui-layout-east,.ui-layout-resizer-west,.ui-layout-resizer-east").show();
		$(".ui-layout-center").width(498);
		$(".ui-layout-center").css("left","226px");
		$(".ui-layout-center").css("float","left");
		
      }
      // $("#sourceDataSourceDescriptionDisplay").val($("sourceDataSourceDescription").val());
      // $("#sourceDataSourceNameDisplay").val($("sourceDataSourceName").val());
    }
    $('.links').unbind('click', disableLink).removeClass('nolink');
  });
  
}

function analyzeActivateAsset () {
  var assetData = $("#assetDefinitionForm").serialize();  
  $.post("../swi/analyzeActivateAsset.action", assetData, function(data) {
    virtualSaved=true;
    $("#assetIno").fadeIn("fast");
    $("#assetIno").empty();
    $("#assetIno").append(data);
    $("#assetIno").show();
  });
}
function setLeftTop(iput,messageDiv,l,t){
	left=$("#"+iput).position().left;
	top=$("#"+iput).position().top;
	left=left+l;
	top=top+t;
	$("#"+messageDiv).css("left",left);
	$("#"+messageDiv).css("top",top);

}
timer="";
function createUpdateTable(operation) {
  
 $('.links').bind('click', disableLink).addClass('nolink');
	 var lookType=$("#tableLookupType").val();   
     var tName=$.trim($("#tableNameFromTableDefinition").val());
     var vCol=$("#tableLookupValueColumn option:selected").text();
     var dCol=$("#tableLookupDescColumn option:selected").text(); 

     var lCol=$("#tableLowerLimitColumn option:selected").text(); 
     var uCol=$("#tableUpperLimitColumn option:selected").text(); 
	
	if($("#virtualCB").val()=="YES"){	
		if(tName==""){
			setLeftTop("tableNameFromTableDefinition","errorMessageDiv",0,-30);
		  $("#errorMessageDiv").html("Name Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableNameFromTableDefinition").focus();
		  return false;
		}
		
		if(vCol=="None"){
			setLeftTop("tableLookupValueColumn","errorMessageDiv",0,-30);
		  $("#errorMessageDiv").html("Value Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableLookupValueColumn").focus();
		  return false;
		}
		if(dCol=="None"){
			setLeftTop("tableLookupDescColumn","errorMessageDiv",0,30);
		  $("#errorMessageDiv").html("Description Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableLookupDescColumn").focus();
		  return false;
		}	
	 
	   var tableName=$("#tableName").val();
	   var lookType=$("#tableLookupType").val();      
	   
	  if(!tableName){    
	    $("#displayError").empty();     
	    $("#displayError").append("Table name is required");
	    return;
	  }
	  
	  if(lookType=="None"){
	  $("#displayError").empty();
	    $("#displayError").html("Please select valid lookup type");
	    return;
	  } 
	   //errorMessage
	}
	if(lookType=="SIMPLE_LOOKUP"){
		if(vCol=="None"){
			setLeftTop("tableLookupValueColumn","errorMessageDiv",0,-30);
		  $("#errorMessageDiv").html("Value Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableLookupValueColumn").focus();
		  return false;
		}
		if(dCol=="None"){
			setLeftTop("tableLookupDescColumn","errorMessageDiv",0,30);
		  $("#errorMessageDiv").html("Description Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableLookupDescColumn").focus();
		  return false;
		}
	}
	if(lookType=="RANGE_LOOKUP"){
		if(vCol=="None"){
			setLeftTop("tableLookupValueColumn","errorMessageDiv",0,-30);
		  $("#errorMessageDiv").html("Value Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableLookupValueColumn").focus();
		  return false;
		}
		if(dCol=="None"){
			setLeftTop("tableLookupDescColumn","errorMessageDiv",0,30);
		  $("#errorMessageDiv").html("Description Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableLookupDescColumn").focus();
		  return false;
		}
		if(lCol=="None"){
			setLeftTop("tableLowerLimitColumn","errorMessageDiv",0,30);
		  $("#errorMessageDiv").html("Lower Limit Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableLowerLimitColumn").focus();
		  return false;
		}
		if(uCol=="None"){
			setLeftTop("tableUpperLimitColumn","errorMessageDiv",0,30);
		  $("#errorMessageDiv").html("Upper Limit Column Required").show();
		  timer=setTimeout('$("#errorMessageDiv").hide()',3000);
		  $("#tableUpperLimitColumn").focus();
		  return false;
		}
	}
  var tableData = $("#tableDefinitionForm").serialize(); 
  //alert(tableData); 
  $("#createUpdateTablProcess").show();
  $("#enableCreateTable").hide();
  $("#enableUpdateTable").hide();
  if (operation == 'create') {  		
  		$("#enableUpdateColumn").hide();
  		$("#createUpdateTablProcess").show();
    	$("#enableCreateTable").hide();
    tableData = tableData + "&" + $("#columnListForm").serialize();    
  }
  
  // alert(tableData);
 $.post("../swi/createUpdateTable.action?asset.id="+assetId+"&asset.name="+assetName, tableData, function(data) {
  		virtualSaved=true;
    $("#createUpdateTablProcess").hide();
   	$("#enableUpdateTable").show();
    $("#tableIno").fadeIn("fast");
    $("#tableIno").empty();
    $("#tableIno").append(data);
    $("#tableIno").show();
    
    if ($("#tableIdFromTableDefinition").val() != '') {
      if (operation == 'create') {      	
        getAssetTables();
        showColumnInfo($("#tableName").val(),$("#virtualCB").val());
      }
      if ($("#tableLookupType").val() != 'None') {
        showMemberInfo($("#tableName").val(),$("#virtualCB").val());
      }
    }
     $('.links').unbind('click', disableLink).removeClass('nolink');
    showHideLookupBlock();
  });
}

//disabling links 
function disableLink(e) {
//console.log("Block URL!");
    e.preventDefault();
    return false;
}


function getSourceTables() {
$("#loadingSTLink").show();
paginationType="sourceTables";
 $.post("../swi/getSourceTables.action?asset.id="+assetId+"&paginationType="+paginationType+"&asset.name="+assetName, function(data) {
    $("#enableUpdateAsset").show();
    $("#updateProcess").hide();
    $("#sourceTableList").fadeIn("fast");
    $("#sourceTableList").empty();
    $("#sourceTableList").append(data);
    $("#sourceTableList").show();
    $("#loadingSTLink").hide();
  });
 // $("#sourceTableList").getHtmlResponce("../swi/getSourceTables.action?asset.id="+assetId+"&paginationType="+paginationType+"&asset.name="+assetName,"sourceTableList","loadingSTLink","","get",false);
}

function getAssetTables() {
$("#loadingATLink").show();
 $.post("../swi/getAssetTables.action?asset.id="+assetId+"&asset.name="+assetName, function(data) {    
    $("#assetTableList").fadeIn("fast");
    $("#assetTableList").empty();
    $("#assetTableList").append(data);
    $("#assetTableList").show();
    $("#loadingATLink").hide();
  });
 //$("#assetTableList").getHtmlResponce("../swi/getAssetTables.action?asset.id="+assetId+"&asset.name="+assetName,"assetTableList","loadingATLink","","get",false);
}

function checkedAsset(){	
	  var checkd=false;	
     checkd= $("#assetTablesForm input:checkbox").is(':checked');
     if(checkd){
	    $("#enableDeleteAsset").show();
	    $("#disableDeleteAsset").hide();
	 }
	    else{
	    $("#enableDeleteAsset").hide();
	    $("#disableDeleteAsset").show();
	    }
	 }
	 
	 var tmpClckStatus="";
function getTableInfo(tableName, description, owner, source,eligibleDefaultMetric) {

	resetPageNo=1;//pagination
	$('.actionMessage').empty();
	if(tmpClckStatus!=tableName){
	  virtualSaved=false;
	  
	  var showTable = "#showTable";
	  var loadingShowTable = "#loadingShowTable";
	  virtualFlag=false;
	  if (source == 'asset') {  
	    showTable = "#showAssetTable";
	    loadingShowTable = "#loadingShowAssetTable";
	  }
	  $("#typeRows").show();
	  $(showTable+tableName+"Link").hide();
	  $(loadingShowTable+tableName+"Link").show();
	  
	  $.post("../swi/getTableInfo.action?asset.id="+assetId+"&asset.name="+assetName+"&table.name="+tableName+"&table.description="+description+"&table.owner="+owner +"&table.virtual=NO"+"&table.eligibleDefaultMetric="+eligibleDefaultMetric, function(data) {     
	    $(loadingShowTable+tableName+"Link").hide();       
	    $("#tableIno").fadeIn("fast");
	    $("#tableIno").empty();
	    $("#tableIno").append(data);
	    $("#tableIno").show();    
	    TabbedPanels1.showPanel(1);
	    // $("#tableLookupType").change(showHideLookupBlock); 
	    showHideLookupBlock();
	    showColumnInfo(tableName,"NO");    
	    if ($("#tableLookupType").val() != 'None') {
	    $("#tableLookupType").attr("alt",$("#tableLookupType :selected").text());
	    $("#tableLookupType").attr("title",$("#tableLookupType :selected").text());	    
	      showMemberInfo(tableName,"NO");
	    } else {
	      $("#memberIno").fadeIn("fast");
	      $("#memberIno").empty();
	      $("#memberIno").show();
	    }	    
	    $(showTable+tableName+"Link").show(); 	
		tmpClckStatus=tableName;
	  });  
	  
	}
}
function getVirtualTableInfo(tableName, description, owner, source){
 var showTable = "#showTable"; 
 virtualSaved=false;
 $("#typeRows").show();
if (source == 'asset') {
    showTable = "#showAssetTable";
    loadingShowTable = "#loadingShowAssetTable";
  }
  $.post("../swi/getTableInfo.action?asset.id="+assetId+"&asset.name="+assetName+"&table.name="+tableName+"&table.description="+description+"&table.owner="+owner+"&table.virtual=YES", function(data) {
    $("#tableIno").fadeIn("fast");
    $("#tableIno").empty();
    $("#tableIno").append(data);
    $("#tableIno").show();    
    TabbedPanels1.showPanel(1); 
    $("#tableLookupType").find("option[index='1']").attr("selected","selected");  
    showHideLookupBlock();
    showColumnInfo(tableName,"YES");    
    if ($("#tableLookupType").val() != 'None') {      
      showMemberInfo(tableName,"YES");
    } else {
      $("#memberIno").fadeIn("fast");
      $("#memberIno").empty();
      $("#memberIno").show();
    }	
  });
}
function virtualCBCheck(){
	// $("#virtualCB").bind("click",function(){	
	var description=$("#tableDescription").val();
	var owner=	$("#tableOwner").val();			  
	if($("#virtualCB").attr("checked")){
			virtualFlag=true;
			var tableName=$("#tableName").val();
			tmpClckStatus=tableName;
			getVirtualTableInfo(tableName, description, owner, 'source');								   
	       		
		}else{
			tmpClckStatus="";
			virtualFlag=false;
			var actualTableName=$("#virtualTableActualName").val();
			var defaultMetricValue=$("#defaultMetricValue").val();		
			getTableInfo(actualTableName, description, owner, 'asset',defaultMetricValue);
			
		}
				
		//	});
}
function showColumnInfo(tableName,virtualTable) {
	tblName=tableName;
	paginationType="column";

	  $.post("../swi/getColumnsInfo.action?asset.id="+assetId+"&asset.name="+assetName+"&table.name="+tableName+"&table.virtual="+virtualTable+"&paginationType="+paginationType, function(data) {
	    $("#columnIno").fadeIn("fast");
	    $("#columnIno").empty();
	    $("#columnIno").append(data);    
	    var len1,len2=0;
	 
	    $("#columnListForm .col").width(130);	
	    $("#columnIno").show();
	   
	    if($("#tblId").val()=='updateTable'){
	    	$("#enableUpdateColumn").show();
	    	$("#updateColumnProcess").hide();    	
	    }else{    	
	    	$("#updateColumnProcess").show();
	  		$("#enableUpdateColumn").hide();
	    }
	    	$("#restColumnProcess").hide();
	    	$("#enableRestColumn").show();    	
	  });
	  //}
}

function showMemberInfo(tableName,virtualTable) {
paginationType="member";
  $.post("../swi/getMembersInfo.action?asset.id="+assetId+"&asset.name="+assetName+"&table.name="+tableName+"&table.virtual="+virtualTable+"&paginationType="+paginationType, function(data) {  	
    $("#memberIno").fadeIn("fast");
    $("#memberIno").empty();
    $("#memberIno").append(data);
    $("#memberIno").show();
    if($("#tblId").val()=='updateTable'){    
    	$("#enableUpdateMember").show();
    	$("#updateMemberProcess").hide();
    }else{
    	$("#enableUpdateMember").hide();
    	$("#updateMemberProcess").show();    	
    }
  });
}

function updateColumns() {
 $('.links').bind('click', disableLink).addClass('nolink');
paginationType="column";
	var columnData = $("#columnListForm").serialize() + "&" +$("#tableDefinitionForm").serialize();
	$("#enableUpdateColumn").hide();
  	$("#updateColumnProcessLoader").show();
 
  $.post("../swi/updateColumnInfo.action?asset.id="+assetId+"&asset.name="+assetName+"&paginationType="+paginationType+"&requestedPage="+requestedPage, columnData, function(data) {  

  	$("#enableUpdateColumn").show();
  	$("#updateColumnProcessLoader").hide();
  	$("#restColumnProcess").hide();  	
    $("#columnIno").fadeIn("fast");
    $("#columnIno").empty();   
    $("#columnIno").append(data);
    /*for(i=0;i<$("#ccolName").val();i++){
    var ftype=$("#cols"+i+"conversionType :selected").text();
    	if(ftype!=null){
    		changeFormat(i,ftype);
    	}
    }*/
    /*changeFormat($indx,$frmType);
    $("#cols"+$indx+"dataFormat1 :selected").text($("#cols"+$indx+"DataFormat").text());
    $("#cols"+$indx+"Unit :selected").text($("#cols"+$indx+"Unit").text());*/
     $('.links').unbind('click', disableLink).removeClass('nolink');
    $("#columnIno").show();
  });
  	 $("#enableRestColumn").show(); 	   	 
 	 return;
}

function updateMembers() {
paginationType="member";
 $('.links').bind('click', disableLink).addClass('nolink');
	var memberData = $("#memberListForm").serialize() + "&" +$("#tableDefinitionForm").serialize();;
	var indicatorString="";
	$.each($("input.indicator"),function(){
		var chkd=$(this).attr("checked");
		if(!chkd){
			var name=$(this).attr("name");
			var val=$(this).attr("value");
			indicatorString="&"+name+"="+val;
			memberData=memberData+indicatorString;
	  }
	})
	
	//alert(memberData);
  	$("#updateMemberProcess").show();
  	$("#enableUpdateMember").hide();
  $.post("../swi/updateMemberInfo.action?asset.id="+assetId+"&asset.name="+assetName+"&paginationType="+paginationType+"&requestedPage="+requestedPage, memberData, function(data) {
  	$("#enableUpdateMember").show();
  	$("#updateMemberProcess").hide();
    $("#memberIno").fadeIn("fast");
    $("#memberIno").empty();
    $("#memberIno").append(data);
    $("#memberIno").show();
    $('.links').unbind('click', disableLink).removeClass('nolink');
  });
  
}

function deleteTables() {
	 $("#deleteProcess").show();
  $("#enableDeleteAsset").hide();
  var assetTablesData = $("#assetTablesForm").serialize();
  $.post("../swi/deleteSelectedTables.action?asset.id="+assetId+"&asset.name="+assetName, assetTablesData, function(data) {
  	$("#deleteProcess").hide();
  	$("#disableDeleteAsset").show();
    $("#assetTableList").fadeIn("fast");
    $("#assetTableList").empty();
    $("#assetTableList").append(data);
    $("#assetTableList").show();
   TabbedPanels1.showPanel(0);
   tmpClckStatus="";
   virtualFlag=false;
   $("#tableIno").empty();
   $("#memberIno").empty();
   $("#columnIno").empty();
  });
}

function hideRows(){
$("#lookupValueColRow").hide();	
$("#lookupDescColRow").hide();
$("#lookupLowerLimitColRow").hide();
$("#lookupUpperLimitColRow").hide();
$("#lookupParentTablesRow").hide();
}

function showHideLookupBlock (){  
  $("#typeRows").show();
  var output = $("#tableLookupType option:selected").val();
	$("#tableLookupType").attr("alt",$("#tableLookupType :selected").text());
	$("#tableLookupType").attr("title",$("#tableLookupType :selected").text());
  if(output == "None"  || output == "RANGEHIERARCHICAL_LOOKUP"){
	  hideRows();
    $("#lookupValueColText").hide();
    $("#lookupValueColValue").hide();
    $("#lookupDescColText").hide();
    $("#lookupDescColValue").hide();
     
    $("#lookupLowerLimitColText").hide();
    $("#lookupLowerLimitColValue").hide();
    $("#lookupUpperLimitColText").hide();
    $("#lookupUpperLimitColValue").hide();
	
	$("#lookupParentTablesText").hide();
    $("#lookupParentTablesValue").hide();
    //$("#dummyLookupBlock").hide();
  } else {
    $("#lookupValueColText").show();
    $("#lookupValueColValue").show();
    $("#lookupDescColText").show();
    $("#lookupDescColValue").show();
	
	$("#lookupValueColRow").show();	
    $("#lookupDescColRow").show();
	showTooltip(output);
    if (output == "RANGE_LOOKUP") {
      $("#lookupLowerLimitColText").show();
      $("#lookupLowerLimitColValue").show();
      $("#lookupUpperLimitColText").show();
      $("#lookupUpperLimitColValue").show();
	  $("#lookupLowerLimitColRow").show();
      $("#lookupUpperLimitColRow").show();
	  $("#lookupParentTablesRow").hide();
	  $("#lookupParentTablesText").hide();
    $("#lookupParentTablesValue").hide();
    }else if(output =="SIMPLEHIERARCHICAL_LOOKUP"){
	  $("#lookupParentTablesText").show();
      $("#lookupParentTablesValue").show();	
	  $("#lookupLowerLimitColText").hide();
      $("#lookupLowerLimitColValue").hide();
      $("#lookupUpperLimitColText").hide();
      $("#lookupUpperLimitColValue").hide();	  
	  $("#lookupLowerLimitColRow").hide();
      $("#lookupUpperLimitColRow").hide();
	  $("#lookupParentTablesRow").show();
	}else {
	  $("#lookupParentTablesText").hide();
      $("#lookupParentTablesValue").hide();
      $("#lookupLowerLimitColText").hide();
      $("#lookupLowerLimitColValue").hide();
      $("#lookupUpperLimitColText").hide();
      $("#lookupUpperLimitColValue").hide();
    }
  }
  if(output == "None" ){
  	 $("#defaultMetricText").show();
  	 $("#defaultMetricValue").show();
  	 $("#indicatorRow").hide();
  	 $("#indicatorcb").attr("value","NO");
  }else{
   $("#defaultMetricText").hide();
   $("#defaultMetricValue").hide();
   $("#indicatorRow").show();
   $("#indicatorcb").attr("value","YES");
  }
}
function showTooltip(output){
	if(output != "None"  || output != "RANGEHIERARCHICAL_LOOKUP"){
		$("#tableLookupValueColumn").attr("alt",$("#tableLookupValueColumn :selected").text());
		$("#tableLookupValueColumn").attr("title",$("#tableLookupValueColumn :selected").text());	
		$("#tableLookupDescColumn").attr("alt",$("#tableLookupDescColumn :selected").text());	
		$("#tableLookupDescColumn").attr("title",$("#tableLookupDescColumn :selected").text());
		$("#tableLookupValueColumn").bind("change",function(){showTooltip(output);});		
		$("#tableLookupDescColumn").bind("change",function(){showTooltip(output);});
		if(output == "RANGE_LOOKUP"){
			$("#tableLowerLimitColumn").attr("alt",$("#tableLowerLimitColumn :selected").text());
			$("#tableLowerLimitColumn").attr("title",$("#tableLowerLimitColumn :selected").text());	
			$("#tableUpperLimitColumn").attr("alt",$("#tableUpperLimitColumn :selected").text());	
			$("#tableUpperLimitColumn").attr("title",$("#tableUpperLimitColumn :selected").text());
			$("#tableLowerLimitColumn").bind("change",function(){showTooltip(output);});		
			$("#tableUpperLimitColumn").bind("change",function(){showTooltip(output);});
		}else if("SIMPLEHIERARCHICAL_LOOKUP"){
			$("#tableLookupParentDescColumn").attr("alt",$("#tableLookupParentDescColumn :selected").text());	
			$("#tableLookupParentDescColumn").attr("title",$("#tableLookupParentDescColumn :selected").text());
			$("#tableLookupParentDescColumn").bind("change",function(){showTooltip(output);});			
		}
	}
	
}
function defaultFormatUnits(index){
	$("#"+index).empty();
 		$("#unit"+index).empty();
 		$("#selectForamt"+index).hide();
 		$("#selectUnit"+index).hide();
		$formatSelection = $('<select class="format" id=subListForColumnInfo'+index+'dataFormat1></select>');	
		$unitSelection = $('<select class="unit" id=subListForColumnInfo'+index+'unit1></select>');	  	
		$option='';
		$optionUnit=''	
		$("#subListForColumnInfo"+index+"dataFormat1").empty();
		$("#subListForColumnInfo"+index+"unit1").empty();			
}
//$indx=''
//$frmType=''
//$frmVal=''
//$uintVal=''
 $formatSelection=undefined;
 $unitSelection=undefined;
function changeFormat(index,format){

//$indx=index;
//$frmType=format;
 $option='';
 $optionUnit=''; 
 	if(format=="NULL"){
 		defaultFormatUnits(index);
 		$("#"+index).append($formatSelection);
		$("#"+index).show();					
		$("#subListForColumnInfo"+index+"DataFormat").val($("#subListForColumnInfo"+index+"dataFormat1 :selected").text()); 		
 		$("#unit"+index).append($unitSelection);
		$("#unit"+index).show();					
		$("#subListForColumnInfo"+index+"Unit").val($("#subListForColumnInfo"+index+"unit1 :selected").text());	
 	}else{ 	
	 $.getJSON("../swi/qiConversion.action", {displayType : format}, function(data) {
		defaultFormatUnits(index);
		if(data.qiConversion){	
			if (data.qiConversion.dataFormats) {				
				$.each(data.qiConversion.dataFormats, function(i, dataFormats) {		
					if($("#subListForColumnInfo"+index+"DataFormat").val()==dataFormats.displayName){			
					$option+="<option value='"+$("#subListForColumnInfo"+index+"DataFormat").val()+"' selected>"+$("#subListForColumnInfo"+index+"DataFormat").val()+"</option>"
					}else{
						$option+="<option value='"+dataFormats.displayName+"'>"+dataFormats.displayName+"</option>"
					}									
				});						
				$formatSelection.append($option);	
				$formatSelection.bind('change',function(){						
						getFormatValue(index,"Format");
				 });
				$formatSelection.css("width","140px");
				 $("#"+index).append($formatSelection);
				 $("#"+index).show();					
				 $("#subListForColumnInfo"+index+"DataFormat").val($("#subListForColumnInfo"+index+"dataFormat1 :selected").text());	
		   }
		   if (data.qiConversion.units) {		 	   
				$.each(data.qiConversion.units, function(j, units) {					
					if($("#subListForColumnInfo"+index+"Unit").val()==units.displayName){
						$optionUnit+="<option value='"+units.displayName+"' selected>"+units.displayName+"</option>"
					}else{
						$optionUnit+="<option value='"+units.displayName+"'>"+units.displayName+"</option>"
					}										
				});	
				$unitSelection.append($optionUnit);	
				$unitSelection.bind('change',function(){						
						getFormatValue(index,"Unit");
				 });					
				 $("#unit"+index).append($unitSelection);
				 $("#unit"+index).show();					
				 $("#subListForColumnInfo"+index+"Unit").val($("#subListForColumnInfo"+index+"unit1 :selected").text());							
		 }
	  }
	});	
	}
}
function changedFormatsAndUnits(indx){
	var ftype = $("#subListForColumnInfo"+indx+"conversionType :selected").val();
	if(ftype != "NULL"){
		$("#subListForColumnInfo1"+indx+"DataFormat").hide();
		$("#subListForColumnInfo1"+indx+"Unit").hide();
		$("#"+indx).hide();	
		$("#unit"+indx).hide();
		$("#selectForamt"+indx).hide();
		$("#selectUnit"+indx).hide();
		changeFormat(indx,ftype);
	}else{
	alert("select conversion type");	
	}
} 
function getFormatValue(indx,Frmt){	
	if(Frmt=="Format"){	
		$("#subListForColumnInfo"+indx+"DataFormat").empty();	
		$("#subListForColumnInfo"+indx+"DataFormat").val($("#subListForColumnInfo"+indx+"dataFormat1 :selected").text());		
	}else{		
		$("#subListForColumnInfo"+indx+"Unit").empty();			
		$("#subListForColumnInfo"+indx+"Unit").val($("#subListForColumnInfo"+indx+"unit1 :selected").text());
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

function resetTablInfo(){
	if(virtualFlag){
		var description=$("#tableDescription").val();
		var owner=	$("#tableOwner").val();			 
		tmpClckStatus="";
		virtualFlag=false;
		var actualTableName=$("#virtualTableActualName").val();
		var defaultMetricValue=$("#defaultMetricValue").val();	
		getTableInfo(actualTableName, description, owner, 'asset',defaultMetricValue);
	}else{
		//$("#virtualTableRowId").hide();
		document.tableDefinitionForm.reset();
		showHideLookupBlock ();
	}
}
function restMemberInfo(){
	$("#enableRestMember").hide();
	$("#restMemberProcess").show();
	document.memberListForm.reset();
	$("#enableRestMember").show();
	$("#restMemberProcess").hide();
	sameData=true;
}

/*
function showAll(searchBox){
	
	if(searchBox==2){
		
			$.each($("table#searchList tr"),function(i,k){
				$temp=$(this).find("a");									   
				$(this).show();			
				$temp.html($temp.text());
			});
		
		}else{
	

  searchString=$("#searchText").val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	var assetId = $("#assetId").val();
	var assetName = $("#assetName").val();
	 $.get("../swi/showSubSourceTables.action?asset.id="+assetId+"&asset.name="+assetName,{}, function(data) {
       $("#sourceTableList").empty();
       $("#sourceTableList").append(data);
     });//End of populate BusinessTerms
	}
		}
}*/

function getPaneSearchDetails(searchType,searchString){
	var assetId = $("#assetId").val();
	var assetName = $("#assetName").val();
	
	if(searchType=="ShowAll"){
	showDetails("../swi/showSubSourceTables.action?asset.id="+assetId+"&asset.name="+assetName,"sourceTableList","get");	
	 }else{
     showDetails("../swi/showSubSourceTables.action?asset.id="+assetId+"&asset.name="+assetName+"&searchString="+searchString+"&searchType="+searchType,"sourceTableList","get");
	 }
}
/*
function startsWithString(searchBox){
	
	if(searchBox==2){
		
			showAll(searchBox);
				searchString=$("#searchText2").val().toLowerCase();
				if(searchString=="search"){
				alert("please enter search string");
				$("#searchText2").val("").focus();
				}else{
			$.each($("table#searchList tr"),function(i,k){
				$temp=$(this).find(findString);
				//alert($temp);
				if($temp.text().toLowerCase().indexOf(searchString)!=0){$(this).hide();}
				$temp.html(autoHighlight($temp.text(), searchString));
			});
			}
		}else{

  searchString=$("#searchText").val().toLowerCase();	
  if(searchString=="search"||searchString ==""){
    alert("please enter search string");
	$("#searchText").val("").focus();
	}else{
	 var assetId = $("#assetId").val();
	 var assetName = $("#assetName").val();
	 $.get("../swi/showSubSourceTables.action?asset.id="+assetId+"&asset.name="+assetName+"&searchString="+searchString+"&searchType=startWith",{}, function(data) {
       $("#sourceTableList").empty();
       $("#sourceTableList").append(data);
     });//End of populate BusinessTerms
	}  
	
		}
}

function containsString(searchBox){
	if(searchBox==2){
		
		    showAll(searchBox);
			searchString=$("#searchText2").val().toLowerCase();
			 if(searchString=="search"){
				alert("please enter search string");
				$("#searchText2").val("").focus();
				}else{
		    $.each($("table#searchList tr"),function(i,k){
			$temp=$(this).find("a");
			if($temp.text().toLowerCase().indexOf(searchString)==-1){$(this).hide();}
			$temp.html(autoHighlight($temp.text(), searchString));
		     });
				}
		}else{
	
	searchString=$("#searchText").val().toLowerCase();	
 if(searchString=="search"||searchString ==""){
    alert("please enter search string");
	$("#searchText").focus();
	}else{
	 var assetId = $("#assetId").val();
	 var assetName = $("#assetName").val();
	 $.get("../swi/showSubSourceTables.action?asset.id="+assetId+"&asset.name="+assetName+"&searchString="+searchString+"&searchType=contains",{}, function(data) {
       $("#sourceTableList").empty();
       $("#sourceTableList").append(data);
     });//End of populate BusinessTerms
     
	}  
	}
}

*/
function subList(index_id,type){
requestedPage=index_id;
pageType=Number(type);
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
		//finding type
		
		if(pageType==2){
		       paginationType="sourceTables";
				actionName="showSubSourceTables.action";
				$.post("../swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType, function(data) {
				$("#sourceTableList").fadeIn("fast");
				$("#sourceTableList").empty();
				$("#sourceTableList").append(data);    
				var len1,len2=0;
		
				} );
	}
	
		if(pageType==1){
			if(TabbedPanels1.currentTabIndex==2){			
				paginationType="column";
				actionName="getSubColumnsInfo.action";
				$.post("../swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType+"&searchType="+$("#columnSearchTypeId").val()+"&searchString="+$("#columnSearchStringId").val(), function(data) {
				$("#columnIno").fadeIn("fast");
				$("#columnIno").empty();
				$("#columnIno").append(data);    
				var len1,len2=0;
			   
				$("#columnListForm .col").width(130);	
				$("#columnIno").show();
			   
				if($("#tblId").val()=='updateTable'){		    	
					$("#enableUpdateColumn").show();
					$("#updateColumnProcess").hide();  	
				}else{    	
					$("#enableUpdateColumn").hide();
					$("#updateColumnProcess").show();
				}
					$("#enableRestColumn").show();
					$("#restColumnProcess").hide(); 
			  });
				
			}else if(TabbedPanels1.currentTabIndex==3){
				paginationType="member";
				actionName="getSubMembersInfo.action";
				$.post("../swi/"+actionName+"?requestedPage="+index_id+"&paginationType="+paginationType+"&searchType="+$("#memberSearchTypeId").val()+"&searchString="+$("#memberSearchStringId").val(), function(data) {
				$("#memberIno").fadeIn("fast");
				$("#memberIno").empty();
				$("#memberIno").append(data);    
				var len1,len2=0;
			   
				$("#memberListForm .col").width(130);	
				$("#memberIno").show();
			   
				if($("#tblId").val()=='updateTable'){		    	
					$("#enableUpdateMember").show();
					$("#updateMemberProcess").hide();  	
				}else{    	
					$("#enableUpdateMember").hide();
					$("#updateMemberProcess").show();
				}
					$("#enableRestMember").show();
					$("#restMemberProcess").hide(); 
			  });
			}
		  }else{
				
			 return;
		  }
	}
	
	
	
}

var assetId;
var  assetName;
var assetDesc;
var screenWidth=screen.width;
var screenHeight=screen.height;
var boxLeft=screenWidth-(screenWidth/2)-200;
var boxTop=screenHeight-(screenHeight/2)-400;
function showAssetsGrain(){
assetId=$("#selectAssetId option:selected").val();
assetName=$("#selectAssetId option:selected").text();
getAssetGrain(assetId,assetName);
$("#hiddenPaneContent").width(400);
}
function showAssetDetail(){
assetId=$("#selectAssetId option:selected").val();
assetName=$("#selectAssetId option:selected").text();
assetDesc=$("#selectAssetId option:selected").attr("desc");
getAssetDetail(assetId,assetName,assetDesc);
$("#hiddenPaneContent").width(400);	
}

function closeBox(){
	$("#hiddenPane").hide();	
}
function getAssetGrain(assetId,displayName) {
    $("#assetGrainLink").hide();
    $("#assetGrainLinkLoader").show();
    

    $.get("../swi/showAssetGrain.action", {"asset.id" : assetId ,"asset.displayName" : displayName}, function(data) {
        $("#hiddenPane").fadeIn("fast").css("left",boxLeft+"px").css("top",boxTop+"px");

       $("#assetGrainLink").show();  
	   $("#assetGrainLinkLoader").hide();
        $("#hiddenPaneContent").empty().append(data);
		$("#boxTitleDiv").empty().append("<s:text name='execue.asset.grain.heading'/>");
        //$("#dynamicPane").show();
        //$("#resetAssetGrainsLoader").hide();               
    });
}  

function getAssetDetail(assetId,displayName,discription){
 
 $("#assetDetailsLink").hide();
 $("#assetDetailsLinkLoader").show();
 $.get("../swi/showAssetDetailInfo.action", {assetId : assetId ,assetDisplayName : displayName,assetDescription : discription}, function(data) {
        $("#hiddenPane").fadeIn("fast").css("left",boxLeft+"px").css("top",boxTop+"px");;;
       $("#assetDetailsLinkLoader").hide(); 
	   $("#assetDetailsLink").show();
       $("#hiddenPaneContent").empty().append(data);
	   $("#boxTitleDiv").empty().append("<s:text name='execue.asset.detail.assetdetail' />");
       // $("#dynamicPane").show();                       
    });
}

--></script>