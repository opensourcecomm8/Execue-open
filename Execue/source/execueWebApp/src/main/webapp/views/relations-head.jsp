<%@page import="com.execue.core.common.type.PaginationType"%>
<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/roundedSearch.css" rel=stylesheet>
<LINK href="../css/cssPopupMenu.css" rel=stylesheet>
<script src="../js/jquery.js" language="JavaScript" /></script>
<script src="../js/menun.js" language="JavaScript" /></script>
<script src="../js/mm_menu.js" language="JavaScript" /></script>
<script src="../js/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/jquery.execue.js" type="text/javascript"
	language="javascript"></script>
    
<script type="text/javascript" src="../js/execue.commons.js"></script>
<script type="text/javascript" src="../js/jquery.execue.searchRecordsComponent.js"></script>
    
<script>
var hideLoading=false;
var conceptId='';
$(document).ready(function() {
showRelations();
searcTextFocusInitialise(); 
 $("#divRelationTerms").searchRecordsComponent({actionName:"swi/getRelationsBySearchString.action",targetDivName:"dynamicPaneRelations"});
 });	

function showRelations(){

showDetails("swi/getRelations.action","dynamicPaneRelations","get");  

}


function createNewRelation() {	

	 showDetails("swi/createNewRelation.action?mode=add","dynamicPane","post","","addNewRelation");

 }
function createRelation() {
	//$("#updateProcess").show();
   // $("#enableUpdateRelation").hide();
	var relationData = $("#relationForm").serialize();
	///  $.post("swi/addRelation.action", relationData, function(data) {
	//  $("#enableUpdateRelation").show();
  	//  $("#updateProcess").hide();
    //  $("#dynamicPane").fadeIn("fast");
    //  $("#dynamicPane").empty(); 
    //  $("#dynamicPane").append(data);
      
    // $("#dynamicPane").show();
	 showDetails("swi/addRelation.action","dynamicPane","post",relationData,"enableUpdateRelation","updateProcess",false);
      showRelations();
    //});
  } 


  
  
function getRelation(relationID) {

	  showDetails("swi/relationDetail.action?relation.id="+relationID,"dynamicPane","post","",relationID);	

    		  
  }
  
  
  
 

function resetRelation(){
	hideLoading=true;
	if($("#mode").val()=="add"){		
		document.relationForm.reset();	
	}else{
		conceptId=$("#currRelationId").val();
		getRelation(relationId);
	}	
} 






</script>
