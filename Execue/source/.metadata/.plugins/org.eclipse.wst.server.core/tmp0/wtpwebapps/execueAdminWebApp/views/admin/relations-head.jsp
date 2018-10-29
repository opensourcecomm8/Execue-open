<%@page import="com.execue.core.common.type.PaginationType"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<LINK href="../css/admin/cssPopupMenu.css" rel=stylesheet>
<script src="../js/common/jquery.js" language="JavaScript" /></script>
<script src="../js/admin/menun.js" language="JavaScript" /></script>
<script src="../js/common/mm_menu.js" language="JavaScript" /></script>
<script src="../js/common/jquery.ui.all.js" type="text/javascript" /></script>
<script src="../js/common/jquery.layout.js" type="text/javascript" /></script>
<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript" /></script>
<script src="../js/admin/script.js" type="text/javascript"
	language="javascript" /></script>
<script src="../js/admin/jquery.execue.js" type="text/javascript"
	language="javascript"></script>
    
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>
    
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

	 showDetails("swi/createNewRelation.action","dynamicPane","post","","addNewRelation");

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
	var relationId=$("#currRelationId").val()
	if(!relationId){		
		document.relationForm.reset();	
	}else{
		getRelation(relationId);
	}	
} 

function deleteRelationHeirarchy(relationId){ 
	var answer = confirm("Are you sure?")
    if (answer){ 
        $("#deleteButton").hide();
		$("#deleteButtonLoader").show();
		var url ="deleteRelationHeirarchy.action?relation.id="+relationId;
		 $.post(url,function(data){
              if(data.status){
                showRelations();
                $("#deleteButton").show();
		        $("#deleteButtonLoader").hide();
		         $("#dynamicPane").empty();			    
		        $("#actionMessage").empty().append(data.message);
			    setTimeout(function(){$("#actionMessage").hide();}, 3000);              
              }else{              
                  $.each(data.errorMessages, function () {
				     $("#errorMessage").append("<li>"+this+"<\li>");
					 $("#deleteButton").show();
		             $("#deleteButtonLoader").hide();
				   });
              }	
		 });	
    }
	return false; 
}




</script>
