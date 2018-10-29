<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>    
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet" type="text/css">
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link href="../css/admin/styleToolTip.css" rel="stylesheet" type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel="stylesheet">
<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/common/jquery.layout.js"></script>
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

</body>
</html>


<script>
	$(document).ready(function() {	 
	   showHierarchies(); 
	   $("#addNewHierarchyDefinition").click(function(){
	       showDetails("showNewHierarchyDefinition.action","dynamicPane","get");  
	   }); 	   
	});

   function showHierarchies(){       	
	   showDetails("showHierarchies.action","divHierarchyDefinition","get");  
	}
	function showHierarchyDefinitions(id,name){
	   showDetails("getHierarchyDefinitions.action?hierarchy.id="+id+"&hierarchy.name="+name,"dynamicPane","get",{},"showHierarchiesDiv"+id+"Link","loadingShowHierarchiesDiv"+id+"Link");  
	}
  function moveRight() {   
	var selectedLHSDimensions = $("#dimensions").find("option:selected");	
	if(selectedLHSDimensions.length == 0) {
		 $("#errorMessage").empty().append("Please select atleast one Dimension");
	}else {
		$.each(selectedLHSDimensions, function(k, item) {
			var id = $(this).attr('value');
			var name = $(this).text();	
			var bedId =	$(this).attr('bedId');	
			var rhsDimensionLength = $("#selectedHierarchyDefinitions").find("option").length;	
			// add hierarchy level		
			var $option = "";
			 var space="";
			if(rhsDimensionLength>0){			 
				for(i=1; i<= rhsDimensionLength; i++){				   
				  	space= '&nbsp;&nbsp;&nbsp;'+space;
				}				
			  $option='<option value="'+id+'" bedId="'+bedId+'">'+space+name+'</option>';
			  $("#selectedHierarchyDefinitions").append($option);
			}else{			 
			   $option='<option value="'+id+'" bedId="'+bedId+'">'+name+'</option>';
			   $("#selectedHierarchyDefinitions").append($option);
			}
			$("#dimensions option[value='"+id+"']").remove();
			adjustHierarchyLevelAfterRemove();
		});
	}
}

function moveLeft() {
	var selectedRHSDimensions = $("#selectedHierarchyDefinitions").find("option:selected");	
	moveToLeft(selectedRHSDimensions);
}

function moveToLeft(selectedRHSDimensions){
  $.each(selectedRHSDimensions, function(k, item) {
		var id = $(this).attr('value');
		var name = $(this).text();
		var bedId = $(this).attr('bedId');		
		var $option = '<option value="'+id+'" bedId="'+bedId+'">'+$.trim(name)+'</option>';		
		$("#dimensions").append($option);
		sortDimensions();
		$("#selectedHierarchyDefinitions option[value='"+id+"']").remove();	
		adjustHierarchyLevelAfterRemove();	
	});
}

function sortDimensions(){
    var lhsDimensions = $("#dimensions").find("option");		 
		lhsDimensions.sort(function(a, b) {
		   var compA = $(a).text().toUpperCase();
		   var compB = $(b).text().toUpperCase();
		   return (compA < compB) ? -1 : (compA > compB) ? 1 : 0;
		})
		$.each(lhsDimensions, function(idx, itm) { 
		    $("#dimensions").append(itm); 
		});
}

function adjustHierarchyLevelAfterRemove(){
    var selectedRHSDimensions = $("#selectedHierarchyDefinitions").find("option");  
	$.each(selectedRHSDimensions, function(k, item) {	  
	    var id = $(this).attr('value');
	    var name = $.trim($(this).text());
	    var bedId =	$(this).attr('bedId');	
	    $("#selectedHierarchyDefinitions option[value='"+id+"']").remove();	 	     
	    var space=""; 
        for(i=1; i<=k; i++){
            space= '&nbsp;&nbsp;&nbsp;'+space;
        } 
	    var $option='<option value="'+id+'" bedId="'+bedId+'">'+space+$.trim(name)+'</option>';
	    $("#selectedHierarchyDefinitions").append($option);
	});
}

function saveHierarchyDefinitions(){
   var hierarchyName = $("#hierarchyNameId").val();
   var hierarchyId = $("#hierarchyId").val();
   var selectedRHSDimensions = $("#selectedHierarchyDefinitions").find("option");
   $("#actionMessage").empty();
	
	if($.trim(hierarchyName)=="") {
		$("#errorMessage").empty().append("Hierarchy name is required");
		  setTimeout(function(){ $("#errorMessage").empty(); },3000);   
		return;
	}
	var splCharExist=checkSpecialChar($("#hierarchyNameId"));
	if(splCharExist){
	  	$("#errorMessage").empty().append("Please enter only letter and numeric characters");
		  setTimeout(function(){ $("#errorMessage").empty(); },3000);   
		  return;
	}
	
	 if(selectedRHSDimensions.length <=1) {
       $("#errorMessage").empty().append("Please select atleast two dimension");
        setTimeout(function(){ $("#errorMessage").empty(); },3000);   
		return;
	}
	
	 var maxHierarchyDefinitionSize = $("#maxHierarchyDefinitionSizeId").val();
	 if(selectedRHSDimensions.length > maxHierarchyDefinitionSize){
	     $("#errorMessage").empty().append("Hierarchy definition can not have more than "+maxHierarchyDefinitionSize+" dimensions");
        setTimeout(function(){ $("#errorMessage").empty(); },3000);   
		return;
	 }
	
   
   var $request="hierarchy.name="+hierarchyName+"&hierarchy.id="+hierarchyId;  
	$.each(selectedRHSDimensions, function(k, item) { 
	    var bedId =	$(this).attr('bedId');
	  	$request=$request+"&selectedHierarchyDefinitions["+k+"]="+bedId;
	});	
      $("#submitButton").hide();
      $("#submitButtonLoader").show();    
	  $.post("saveHierarchyDefinitions.action",$request ,function(data) {
	        if(typeof data =='object'){
	           $("#errorMessage").empty().append(data.errorMessages[0]);
	           setTimeout(function(){ $("#errorMessage").empty(); },3000);   
	        }else{
		       $("#dynamicPane").empty().append(data);
		         showHierarchies();
		        setTimeout(function(){ $("#actionMessage").empty(); },3000); 
		     
		     }
		     $("#submitButton").show();
             $("#submitButtonLoader").hide();  
		});	

}

function deleteHierarchy(){
  var hierarchyId=$("#hierarchyId").val();
  if(hierarchyId){
       var confirmChanges= confirm("Do you want to delete hierarchy definition?");           
			if(confirmChanges){			     
			     $("#deleteButton").hide();
			      $("#deleteButtonLoader").show();    
				  $.post("deleteHierarchyDefinitions.action?hierarchy.id="+hierarchyId,function(data) {
				        if(data.status){				          
				            $("#dynamicPane").empty().append('<div id="actionMessage" style="color: green">'+data.message+'</div>');         
				            showHierarchies();
				             setTimeout(function(){ $("#actionMessage").empty(); },3000);     
				        }else{
				           $("#errorMessage").empty().append(data.errorMessages[0]);
				             setTimeout(function(){ $("#errorMessage").empty(); },3000);     
				        }
					     $("#deleteButton").show();
			             $("#deleteButtonLoader").hide();  
					});				  
			}  
   
  }
}

function reset(){
   var hierarchyId=$("#hierarchyId").val();
   var hierarchyName= $("#showHierarchiesDiv"+hierarchyId+"Link").text();   
  if(hierarchyId){
     showHierarchyDefinitions(hierarchyId, hierarchyName);
  }else{
    $("#hierarchyNameId").val(null);
   	var selectedRHSDimensions = $("#selectedHierarchyDefinitions").find("option");	
   	moveToLeft(selectedRHSDimensions);
  }
}

</script>