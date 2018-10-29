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
<script type="text/javascript"
    src="../js/jquery.execue.searchRecordsComponent.js"></script>

<script>
var hideLoading=false;
var conceptId='';
$(document).ready(function() {
//showBusinessTerms();
//showSearch('divBTerms');
// searcTextFocusInitialise(); 
// $("#divBTerms").searchRecordsComponent({actionName:"swi/getBusinessTermsBySearchString.action",targetDivName:"dynamicPaneBTerms"});
 
});


function getConceptsAndAttributesTobeRealizedForType(){
    var id=$("#selectedType").val();
    var name=$("#selectedType option:selected").text();

            $("#eligibleConcepts").empty();
            $("#typesToBeRealized").empty();
            $("#realizedConcepts").empty();
        $.getJSON("populateEligibleConcepts.action",{"selectedType.id":id,"selectedType.name":name},function(data){
            $.each(data,function(k,v){
            var $option=$("<option></option>");
            $option.attr("conceptId",v.id);
            $option.text(v.displayName);
            
            if(v.existing){
            $option.attr("selected",true);
            }
            $("#eligibleConcepts").append($option);
            });
        });



        $.getJSON("populateTypesToBeRealized.action",{"selectedType.id":id,"selectedType.name":name},function(data){
        
        
                    $.each(data,function(k,v){
                    var $option=$("<option></option>");
                    $option.attr("value",v.id);
                    $option.text(v.displayName);
                    $("#typesToBeRealized").append($option);
                    });
        });
}

function getRealizedConceptsForAttribute(){


    var id=$("#typesToBeRealized option:selected").val();
        var name=$("#typesToBeRealized option:selected").text();
                $("#realizedConcepts").empty();
            $.getJSON("populateRealizedConcepts.action",{"selectedTypeToBeRealized.id":id,"selectedTypeToBeRealized.name":name},function(data){
                $.each(data,function(k,v){
                var $option=$("<option></option>");
                $option.attr("conceptId",v.id);
                $option.text(v.displayName);
    
                $("#realizedConcepts").append($option);
                });
            });
}

function saveTypeForConcepts(){

var pars="";
//selected type params
if($("#selectedType").val()!=undefined){
pars+= "&selectedType.id="+$("#selectedType").val();
pars+= "&selectedType.name="+$("#selectedType option:selected").text();
}
//selectedTypeToBeRealized params
if($("#typesToBeRealized option:selected").val()!=undefined){
pars+= "&selectedTypeToBeRealized.id="+$("#typesToBeRealized option:selected").val();
pars+= "&selectedTypeToBeRealized.name="+$("#typesToBeRealized option:selected").text();
}
$.each($("#eligibleConcepts option:selected"),function(i,v){

pars+="&eligibleConcepts["+i+"].name="+$(this).text();
pars+="&eligibleConcepts["+i+"].id="+$(this).attr("conceptid");

});

$.each($("#realizedConcepts option:selected"),function(i,v){
pars+="&realizedConcepts["+i+"].name="+$(this).text();
pars+="&realizedConcepts["+i+"].id="+$(this).attr("conceptid");
});


$("#saveButton").hide();
$("#saveButtonLoader").show();
$.post("saveConceptType.action",pars,function(data){
$("#dynamicPane").empty().html(data);
$("#saveButton").show();
$("#saveButtonLoader").hide();
});
}
</script>
