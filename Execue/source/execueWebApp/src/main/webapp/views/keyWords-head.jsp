<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/thickbox.css" type="text/css"
	media="screen" />
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel="stylesheet" type="text/css" />
<LINK href="../css/cssPopupMenu.css" rel="stylesheet" type="text/css" />

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/jquery.execue.js"></script>
<script language="JavaScript" src="../js/menun.js"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/jquery.layout2.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/thickbox.js"></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript" /></script>
<script type="text/javascript"
	src="../js/jquery.execue.searchRecordsComponent.js"></script>

<script type="text/javascript">
var keyWord="";
var txId="";
var myLayout;
var paginationType="businessTermsForPW";
var searchTypeClicked="";
$(document).ready(function() {
  myLayout = $('#container').layout({
    west__showOverflowOnHover: true
	//,	west__fxSettings_open: { easing: "easeOutBounce", duration: 750 }
  });  
  getExistingKeywords();
 showBusinessTerms('');
 showKeyword('');

/* $('#searchText2').focus(function(){
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
  });*/
  var businessTerm= $("#businessEntityTermTypesId").val();
  $("#divBT").searchRecordsComponent({actionName:"swi/getBusinessTermsForPWsBySearchString.action?businessEntityTermType="+businessTerm,targetDivName:"dynamicPaneBTerms"});
  
  
 
  
});


function showBusinessTerms(businessTerm){
  businessTerm= $("#businessEntityTermTypesId").val();
  $.get("swi/showBTforParallelWords.action?businessEntityTermType="+businessTerm, function(data) {
      $("#dynamicPaneBTerms").empty();
      $("#dynamicPaneBTerms").append(data);
    });//End of populate BusinessTerms
}


function updateParallelWords(){
$("#updateParallelWords").hide();
$("#updateParallelWordsProcess").show();
 $.post("swi/createKeyWordParallelWord.action", $('#keyWordDefinitionForm').serialize(), function(data) {        
		$("#dynamicPane").empty();
		$("#dynamicPane").fadeIn("fast");    
		$("#dynamicPane").append(data);
		$("#dynamicPane").show();
		$("#errorMessage").append('<s:fielderror/>');
		$("#errorMessage").append('<s:actionmessage/>');
		$("#updateParallelWords").show();
        $("#updateParallelWordsProcess").hide();		
	});
 getExistingKeywords();	

}
function createParallelWords(){
$("#createParallelWords").hide();
$("#createParallelWordsProcess").show();
 $.post("swi/createKeyWordParallelWord.action", $('#keyWordDefinitionForm').serialize(), function(data) {        
		$("#dynamicPane").empty();
		$("#dynamicPane").fadeIn("fast");    
		$("#dynamicPane").append(data);
		$("#dynamicPane").show();
		$("#errorMessage").append('<s:fielderror/>');
		$("#errorMessage").append('<s:actionmessage/>');
		$("#createParallelWords").show();
        $("#createParallelWordsProcess").hide();		
	});
 getExistingKeywords();	

}


function getAllParallelwords(){
	
	keyWord=$("#keyWord").val();
	params = "keyWord.word="+keyWord;
	$.post("swi/getParallelWords.action",params, function(data) {	
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    if(parseInt($("#count").val())==0){
    	$("#imageField").hide();
    }
  });
 }
 
 function getExistingKeywords(){
  $.post("swi/showExistingKeyWords.action", {}, function(data) {
    $("#rightPane").empty();
    $("#rightPane").append(data);
    $("#rightPane").show();
  });
}
 function getKeywordAndParallelWords(){
  $.post("swi/showExistingKeyWords.action", {}, function(data) {
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
  });
}

function showKeyword(keywordName) {
  $.post("swi/showKeyWord.action",{keyWordName:keywordName}, function(data) {    
    $("#dynamicPane").empty();
    $("#dynamicPane").append(data);
    $("#dynamicPane").show();
    $("#imageField").hide();
  });
}
function checkedKeyWord(){
	 var checkd=false;	
     checkd= $("#keyWordForm input:checkbox").is(':checked');     
     if(checkd){
	    $("#enableDeleteSelect").show();
	    $("#disableDeleteSelect").hide();
	 }else{
	    $("#enableDeleteSelect").hide();
	    $("#disableDeleteSelect").show();
	}
}
function deleteKeywords() {
  var keywords = $("#keyWordForm").serialize();
 // alert(keywords);
  $("#deleteProcess").show();
  $("#enableDeleteSelect").hide();
  $.getJSON("swi/deleteKeyWords.action", keywords ,function(data) {         
    getExistingKeywords();
    $("#deleteProcess").hide();
  	$("#disableDeleteSelect").show();
    $("#dynamicPane").fadeIn("fast");
    $("#dynamicPane").empty();
    $("#dynamicPane").append("<div id='errorMessage' STYLE='color: red' align='left'>"+data+"</div>");
    $("#dynamicPane").show();
  });
  
}

function subList(index_id){

requestedPage=index_id;
var actionName="";
	
		
			actionName="showSubBTforParallelWords.action";
			 var pageSize=$("#pageSizeConcepts").val();
			   var recordCount=$("#recordCountConcepts").val();
			   var pageCount=$("#pageCountConcepts").val(); 
			   var noOfLinks=$("#noOfLinksConcepts").val();   
			   var paginationType=$("#paginationTypeConcepts").val();
				actionName="swi/showSubBTforParallelWords.action";
				$.post(actionName,{
						"pageDetail.pageSize" : pageSize,
						"pageDetail.recordCount" : recordCount,
						"pageDetail.pageCount" : pageCount,
						"pageDetail.numberOfLinks" : noOfLinks,
						paginationType : paginationType,			
						requestedPage :index_id}, function(data) {
				   $("#dynamicPaneBTerms").empty();
				   $("#dynamicPaneBTerms").append(data);
				   var len1,len2=0;	
			  }); 
			
	   
	 
}

function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
			}
</script>
