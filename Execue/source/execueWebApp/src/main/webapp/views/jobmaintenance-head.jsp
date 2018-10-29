<link href="../css/styles.css" rel="stylesheet" type="text/css">
<link href="../css/ui-layout-styles.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/thickbox.css" type="text/css"
	media="screen" />

<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/jquery.execue.js"></script>
<script language="JavaScript" src="../js/menun.js"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/jquery.layout.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/thickbox.js"></script>

<script type="text/javascript">

function showFileOntology(){
	$.post("swi/showUploadFileOntology.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}
function showCorrectMapping(){
	$.post("swi/showCorrectMappingMaintenance.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}
function showPopularityHit(){
	$.post("swi/showPopularityHitMaintenance.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showRIOntoPopularityHit(){
	$.post("swi/showRIOntoTermPopularityHitMaintenance.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showPopularityCollection(){
	$.post("swi/showPopularityCollectionMaintenance.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showPopularityDispersion(){
	$.post("swi/showPopularityDispersionMaintenance.action", function(data) {
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showSFLTokenWeight(){
	$.post("swi/showSFLTermTokenWeightMaintenance.action", function(data) {
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showRIOntoTermAbsorption(){
    $.post("swi/showRIOntoTermAbsorption.action", function(data) {
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showSnowFlakeAbsorption(){
    $.post("swi/showSnowFlakeAbsorption.action", function(data) {
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showIndexFormManagement(){
    $.post("swi/showIndexFormManagement.action", function(data) {
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showSFLWeightBySecWord(){
    $.post("swi/showSFLWeightUpdationBySecWord.action", function(data) {
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showConceptTypeAssociation(){
	$.post("swi/showConceptTypeAssociationMaintenance.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

function showMemberSynchronization(){
	$.post("swi/showMemberSynchronization.action", function(data) {	   
    $("#dynamicPaneOntology").empty();    
    $("#dynamicPaneOntology").append(data);
    $("#dynamicPaneOntology").show();  
    });  
}

</script>

