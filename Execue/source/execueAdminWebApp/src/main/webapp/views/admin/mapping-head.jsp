<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script language="JavaScript" src="../js/common/mm_menu.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" type="text/javascript"
	src="../js/admin/mapping.js"></script>
<SCRIPT src="../js/common/jquery.autocomplete.js" type=text/javascript></SCRIPT>
<SCRIPT src="../js/admin/common.js" type=text/javascript></SCRIPT>
<SCRIPT src="../js/admin/execue.commons.js" type=text/javascript></SCRIPT>
<script src="../js/admin/searchList.js" type="text/javascript" language="javascript" /></script>
<script type="text/javascript" src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>
<script src="../js/admin/jquery.execue.helpMessage.js" ></script>
<LINK href="../css/admin/cssPopupMenu.css" rel=stylesheet>
<link href="../css/admin/treeDrag.css" rel="stylesheet" type="text/css">
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<!--link href="css/ui-layout-styles.css" rel="stylesheet" type="text/css"-->
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<style type="text/css">
.ac_input {
	width: 200px;
}

.ac_results {
	width: 200px;
	background: #eee;
	cursor: pointer;
	position: absolute;
	left: 0;
	font-size: 90%;
	z-index: 101;
		
}

.ac_results ul {
	width: 194px;
	list-style-position: outside;
	list-style: none;
	padding: 0;
	margin: 0;
	border: 1px solid #000;
	background-color:#FFF;
	overflow: scroll;
	height:100px;
}


.ac_results iframe {
	display: none; /*sorry for IE5*/
	display /**/: block; /*sorry for IE5*/
	position: absolute;
	top: 0;
	left: 0;
	z-index: -1;
	filter: mask();
	width: 3000px;
	height: 3000px;
}

.ac_results li {
	width: 100%;
	padding: 3px 0px 3px;
}

.ac_results a {
	width: 100%;
}

.ac_loading {
	background: url('../images/admin/loaderTrans.gif') right center no-repeat;
}

.ac_over {
	background: #8CA0FD;
}
</style>
<script type="text/javascript">
var mapInstanceClicked=false;
	var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
//searchIterateThrough="li.concept";
//findString="span.conceptHolder";
tr="";
	$(document).ready(function () {
		//myLayout = $('#container').layout({
			// enable showOverflow on west-pane so popups will overlap north pane
		//	west__showOverflowOnHover: true

		//,	west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
		//});
		
		var jsAssetId = '<s:property value="applicationContext.assetId"/>';   
		$('#checkAllTables').click(function(){ 
		  $("li.mytable INPUT[type='checkbox']").attr('checked', $('#checkAllTables').is(':checked'));   
		  $("li.mytable INPUT[type='checkbox']").trigger("click");
		  $("li.mytable INPUT[type='checkbox']").attr('checked', $('#checkAllTables').is(':checked'));   
	   });
		$("#container").hide();
		$("#divConceptsSearch").searchRecordsComponent({actionName:"",targetDivName:"businessTermDiv",divType:"span",divClass:"conceptHolder",rowType:"li"});
		$("#divAssetTablesSearch").searchRecordsComponent({actionName:"",targetDivName:"tableColumnsDiv",divType:"span",divClass:"tableHolder",rowType:"li.mytable"});
		$("#divColumnMembersSearch").searchRecordsComponent({actionName:"",targetDivName:"colMemsDiv",divType:"span",divClass:"columnHolder",rowType:"li"});
		$("#divInstancesSearch").searchRecordsComponent({actionName:"",targetDivName:"instancesDiv",divType:"span",divClass:"conceptHolder",rowType:"li"});
	   if(jsAssetId){
	    $('#assetId').val(jsAssetId);
	      initializeMappings();   
		  $("#loadingATLink").show(); 	
		  $("#loadingBTLink").show();
		   $("#loadingConceptMappingsLink").show(); 
		  setTimeout(hideConceptMappingsLoaders,10000);
		 
		  $("#container").show();
		
	   }
 	});
	function hideConceptMappingsLoaders(){
		 $("#loadingConceptMappingsLink").hide(); 
	}
	
	</script>
<SCRIPT type=text/javascript>
$(document).ready(function(){

  doDrag();
$('#searchIcon').click(function() {
	$('#search').submit();
});

$('#searchText').focus(function(){
	if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){	//test to see if the search field value is empty or 'search' on focus, then remove our holding text
		$(this).attr('value', '');
	}
	$(this).css('color', '#000');
	
});
$('#searchText').blur(function(){	//test to see if the value of our search field is empty, then fill with our holding text
	if($(this).attr('value') == ''){
		$(this).attr('value', 'Search');
		$(this).css('color', '#777');
	}
});

});

function showSearch(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables").hide("");
  $("#"+divName+obj+"#roundedSearch").fadeIn("slow");
  
  
 
$("div#searchIcon img#Image3").bind("mouseover",function(){ 
 searchIterateThrough="li.mytable,li.column";
findString="span.tableHolder"; })

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
  
  $("div#searchIcon img#Image4").bind("mouseover",function(){ 
 searchIterateThrough="li.concept";
findString="span.conceptHolder"; })
  
  
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

function showSearchIns(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTablesIns").hide();
  $("#"+divName+obj+"#roundedSearchIns").fadeIn("slow");
  
  $("div#searchIconIns img#Image4Ins").bind("mouseover",function(){ 
 searchIterateThrough="li.concept";
findString="span.conceptHolder"; })
  
  $('#searchText3').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText3').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
}

function showSearchCol(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTablesCol").hide();
  $("#"+divName+obj+"#roundedSearchCol").fadeIn("slow");
  
  $("div#searchIcon img#Image4Col").bind("mouseover",function(){ 
 searchIterateThrough="li.column";
findString="span.columnHolder"; })
  
  $('#searchText4').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText4').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
}
</SCRIPT>
<SCRIPT type=text/javascript>
var rightColumn="";
var leftColumn="";
var hitFlagColumn=false;
var hitFlagConcept=false;
seperator=":";
spacer="&nbsp;";
$mapsArray={};
$mapsArrayArray=[];
maptype="";
$(document).ready(
				 
	function()
	{	 		  
		$("#suggestConcepts").bind("click",function(){ 
			var status=checkforMapsToSave(); 				 
			$("#suggestConceptsLink").hide(); 	
			$("#loadingSuggestConceptsLink").show();
			
			showMapsSuggested();
		});
		$("#suggestInstances").click(function(){
			$("#suggestInstancesLink").hide(); 	
			$("#loadingSuggestInstancesLink").show();
			
			showMapsInstancesSuggested();
		});
		//filterListBySearch("searchText2",'concept','conceptHolder');	
		
		$("li.TabbedPanelsTab").click(function(){
				$(".ac_results").hide();							   
			//initialiseConceptsInstances();								   
				$tabClicked=$(this).attr("tabindex");							   
				if($tabClicked==0)	{
					
					$("#btDiv").empty().append("Business Terms");
					$("#atDiv").empty().append("Dataset Tables");
					
					$("#tableColumnsDiv").show();
					$("#columnMembersDiv").hide();
					
					$("#businessTermDiv").show();
					$("#instancesDiv").hide();
					$current="concept";
					showHidePanes();
					$editFlag=true;
					}
					
				if($tabClicked==1)	{
					
						$("#btDiv").empty().append("Instances");
						$("#atDiv").empty().append("Column Members");
						
						$("#tableColumnsDiv").hide();
					$("#columnMembersDiv").show();
					
					$("#businessTermDiv").hide();
					$("#instancesDiv").show();
					$current="instance";
					showHidePanes();
					$editFlag=true;
					
				}
			});
		

	});

//$("#dynamicPane").createInput("suggestMaps.htm","","","");
	
function checkConcepts(){
$("div#conceptsSuggestedDiv  INPUT[type='checkbox']").attr('checked', $('#checkAllConceptSuggestions').is(':checked'));
  checkedMap();
  if(!$('#checkAllConceptSuggestions').is(':checked')){
  disableCinfirmConceptMapping();
  }
}
function checkInstancesMapping(){

$("div#instanceMapsSelected  INPUT[type='checkbox']").attr('checked', $('#checkAllInstanceMappings').is(':checked')); 
 checkedInstanceMap();
 
 if(!$('#checkAllInstanceMappings').is(':checked')){
  disableConfirmInstanceMapping();
  }
}

</SCRIPT>


<script>
var TabbedPanels1=undefined;
$("document").ready(function(){
Pane_Left=0;
Pane_Top=0;

TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
//$("#conceptsPane").createInput("mapConcepts.htm");
	$("#hiddenPane a#closeButtonId").click(function(){
	$addConceptFlag=false;
	$("#hiddenPane").fadeOut("slow");						   
	});
		
 });
function mapInstances(id){
$("#instancesPane").createInput("mapInstances.html","showInstances"+id+"Link","loadingShowInstances"+id+"Link"); //passing URL, user clicked link, loader div
TabbedPanels1.showPanel(1);
$("#instanceMappingId").show();

}

function checkedMap(){

     var checkd= $("#mapsSelected input[type='checkbox']").is(':checked');
    // alert(checkd);

				if(checkd){    
				    enableCinfirmConceptMapping();
				 }else{
				  disableCinfirmConceptMapping();
			
					}
}
		function enableCinfirmConceptMapping(){
					$("#enableConfirmMappings").show();
				    $("#disableConfirmMappings").hide();	
		}
		function disableCinfirmConceptMapping(){
					 $("#enableConfirmMappings").hide();
				    $("#disableConfirmMappings").show();
		}
		function disableConfirmInstanceMapping(){
			  $("#enableInstanceConfirmMappings").hide();
			  $("#disableInstanceConfirmMappings").show();
		}
		function enableConfirmInstanceMapping(){
			  $("#enableInstanceConfirmMappings").show();
			  $("#disableInstanceConfirmMappings").hide();
		}
function checkedInstanceMap(){
     var checkd= $("#instanceMapsSelected input[type='checkbox']").is(':checked');
    // alert(checkd);
   			if(checkd){    
				    enableConfirmInstanceMapping();
				 }else{
				  disableConfirmInstanceMapping();
			
					}
}
function showConceptsTextBox(x){
$text=$("a#"+x).html();	
var input = $('<input type="text"></input>');
				input.bind('click', function(e) {
					//e.stopPropagation();
				});
				$("#dynamicTextBox").empty();
				$("#dynamicTextBox").append(input);
				$("#dynamicTextBox").css("left",$("a#"+x).position().left+"px");
				$("#dynamicTextBox").css("top",$("a#"+x).position().top+"px");
				$("#dynamicTextBox input").val($text);
		//$("a#"+x).append(input);		
}

function doDrag(){
 $("#boxTemplate_r1_c2,#boxTemplate_r1_c1").mousedown(function(){
    $('div#hiddenPane').draggable({ containment: "#execueBody", scroll: false });
   });
    $("#boxTemplate_r1_c2,#boxTemplate_r1_c1").mouseup(function(){
    $('div#hiddenPane').draggable('destroy');
   });
  }
//formats
$option='';
 $optionUnit='';
 $selectedFromats = [];
 function changeFormat(){
  $selectedFromats.length=0;
  $options='';
  $("#dataFormats").empty();
  $("#units").empty();
  	$('#displayTypeList :selected').each(function(i, selected){   	
  	    formatType = $(selected).text();
  	   // formatID = $(selected).val();  	    
  	    $selectedFromats[i]=formatType;  	      	   
  	});
  	
  	$.each($selectedFromats,function(i,text){
  		 getFormatAndUnitValues(text);
  	});  	
  }
  function getFormatAndUnitValues(format){
	$.getJSON("swi/qiConversion.action", {displayType : format}, function(data) {
		$option='';	
		if(data.qiConversion){		
		if (data.qiConversion.dataFormats) {			
			$.each(data.qiConversion.dataFormats, function(i, dataFormats) {
				if($("#dDataFormat").val()==dataFormats.displayName){			
				$option+="<option value='"+$("#dDataFormat").val()+"' selected>"+$("#dDataFormat").val()+"</option>"
				}else{
					$option+="<option value='"+dataFormats.displayName+"'>"+dataFormats.displayName+"</option>"
				}				
			});		
			 $("#dataFormats").append($option);	
			 $("#dDataFormat").val($("#dataFormats").val())	;			 
	   }
	   if (data.qiConversion.units) {
	   $optionUnit=''
	   	//if(data.units.length>0){
			$.each(data.qiConversion.units, function(j, units) {
				if($("#dUnit").val()==units.displayName){
					$optionUnit+="<option value='"+$("#dUnit").val()+"' selected>"+$("#dUnit").val()+"</option>"
				}else{
					$optionUnit+="<option value='"+units.displayName+"'>"+units.displayName+"</option>"
				}					
			});			
			$("#units").append($optionUnit);
			$("#dUnit").val($("#units").val());
		/*}else{
	 		$("#dUnit").val($("#units").val())
	 	}*/
	 }
	}
	});	
}
function getFormatValue(frmt,fvalue){
	if(frmt=="Frmt"){				
		$("#dDataFormat").val(fvalue);
	}else{	
		$("#dUnit").val(fvalue);		
	}
} 

//
// initialize
suggestConceptMappingURL = '<s:url action="suggestConceptMappings" includeParams="none"/>';
btListURL = '<s:url action="showMappableConcepts" includeParams="none"/>';
suggestConceptURL ='<s:url action="suggestConcepts" includeParams="none"/>';
existingConceptMappingURL = '<s:url action="showExistingConceptMappings" includeParams="none" />';
editConceptURL = '<s:url action="showConcept" includeParams="none" />';
newConceptURL = '<s:url action="showConcept" includeParams="none" />';
saveConceptURL = '<s:url action="saveConcept" includeParams="none" />';
tableColumnsURL = '<s:url action="showAssetTables" includeParams="none" />';
storeConceptMappingURL = '<s:url action="storeConceptMappings" includeParams="none" />';
existingInstanceMappingURL = '<s:url action="showExistingInstanceMappings" includeParams="none" />';
suggestInstanceMappingURL = '<s:url action="suggestInstanceMappings" includeParams="none" />';
storeInstanceMappingURL = '<s:url action="storeInstanceMappings" includeParams="none" />';
suggestInstanceURL ='<s:url action="suggestInstances" includeParams="none" />';
instanceListURL = '<s:url action="showMappableInstances" includeParams="none" />';
columnMembersURL = '<s:url action="showColumnMembers" includeParams="none" />';
newInstanceURL = '<s:url action="createNewInstance" includeParams="none" />';
editInstanceURL = '<s:url action="instanceDetail" includeParams="none" />';
saveInstanceURL = '<s:url action="saveInstance" includeParams="none"  />';
conceptShowPageURL='<s:url action="showConceptPage" includeParams="none" />';
instanceShowPageURL='<s:url action="showInstancePage" includeParams="none" />';
displayConceptMappingsURL='<s:url action="displayConceptMappings" includeParams="none" />';
displayInstanceMappingsURL='<s:url action="displayInstanceMappings" includeParams="none" />';
</script>
