<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css"
	media="screen" />

<script src="../js/admin/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/common/jquery.layout.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>

<script type="text/javascript">

function showFileOntology(obj){
	/*$("#"+obj.id).css("background-image","url(../images/admin/loaderAT.gif)");
	$.post("swi/showUploadFileOntology.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
	$("#"+obj.id).css("background-image","none");
    });  */
	
	doPost("swi/showUploadFileOntology.action",obj);
}
function showCorrectMapping(obj){
	
	doPost("swi/showCorrectMappingMaintenance.action",obj);
}
function showPopularityHit(obj){
	
	doPost("swi/showPopularityHitMaintenance.action",obj);
}

function showRIOntoPopularityHit(obj){
	
	doPost("swi/showRIOntoTermPopularityHitMaintenance.action",obj);
}

function showPopularityCollection(obj){
	
	doPost("swi/showPopularityCollectionMaintenance.action",obj);
}

function showPopularityDispersion(obj){
	
	doPost("swi/showPopularityDispersionMaintenance.action",obj);
}

function showSFLTokenWeight(obj){
	
	doPost("swi/showSFLTermTokenWeightMaintenance.action",obj);
}


function showIndexFormManagement(obj){
	
	doPost("swi/showIndexFormManagement.action",obj);
}

function showSFLWeightBySecWord(obj){
	
	doPost("swi/showSFLWeightUpdationBySecWord.action",obj);
}

function showConceptTypeAssociation(obj){
	//$("#"+obj.id).css("background-image","url(../images/admin/loaderAT.gif)");
	
	doPost("swi/showConceptTypeAssociationMaintenance.action",obj);
}

function showSDXSynchronization(obj){
	//$("#"+obj.id).css("background-image","url(../images/admin/loaderAT.gif)");
	doPost("swi/showSDXSynchronization.action",obj);
   
}

function showOptimalDSet(obj){
	//$("#"+obj.id).css("background-image","url(../images/admin/loaderAT.gif)");
	doPost("swi/showOptimalDSet.action",obj);
   
}

function doPost(url,obj){
$("#"+obj.id).css("background-image","url(../images/admin/loaderAT.gif)");	
$.ajax({url:url, success:function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show(); 
	$("#"+obj.id).css("background-image","none");
    },error:function(){ 
	$("#dynamicPaneOntology").empty();    
	alert("error occured");
	$("#"+obj.id).css("background-image","none");
	} }); 	
}

</script>

