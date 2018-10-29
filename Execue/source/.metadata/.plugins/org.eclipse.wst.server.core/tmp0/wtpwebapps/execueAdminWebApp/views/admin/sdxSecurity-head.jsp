<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/admin/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css" />
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<link href="../css/common/ui-layout-styles.css" rel="stylesheet"
	type="text/css" />
<link href="../css/admin/styleToolTip.css" rel="stylesheet"
	type="text/css" />
<LINK href="../css/common/roundedSearch.css" rel=stylesheet>
<LINK href="../css/admin/cssPopupMenu.css" rel=stylesheet>
<LINK href="../css/admin/autoComplete.css" rel=stylesheet>
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
<SCRIPT src="../js/common/jquery.autocomplete.js" type=text/javascript></SCRIPT>
<script type="text/javascript" src="../js/admin/execue.commons.js"></script>
<script type="text/javascript"
	src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>
<script src="../js/admin/jquery.execue.helpMessage.js"></script>
<script>
$(document).ready(function() { 	
  var applicationId=   $("#selectAppId option:selected").val();
   var context=$("#contextId").val();   
   showRoles();
   searcTextFocusInitialise(); 
     $("#divRoleSearch").searchRecordsComponent({actionName:"swi/getAssetsForApplication.action",targetDivName:"datasetCollectionDynamicPane"});
    function showRoles(applicationId){
	   showDetails("showSecurityRoles.action","roleDynamicPane","get");  
	}
	
	 $("#selectTableId").change(function(){
	   var selectedAssetId =   $("#authorizedAssetsForRole option:selected").val(); 
	   var selectedRoleName =   $("#selectedRoleId").val();
	    if(context== 'Column'){
	       getColumnsForSelectedTable(selectedAssetId,selectedRoleName,$(this).val());	    
	    }else if(context== 'Member'){
	      getMembersForSelectedTable(selectedAssetId,selectedRoleName,$(this).val())
	    }
	 
	 });
	 
	  $("#authorizedAssetsForRole").change(function(){
	    var selectedAssetId =   $("#authorizedAssetsForRole option:selected").val(); 
	    var selectedRoleName =   $("#selectedRoleId").val();	   
	    if(context== 'Column'){
	       showTablesAndColumnForAsset(selectedAssetId,selectedRoleName);
	    } else if(context== 'Member'){ 	      
	        showLookupTablesAndMembersForSelectedAsset(selectedAssetId,selectedRoleName);	     
	    }else {
	       getTablesForSelectedAsset(selectedAssetId,selectedRoleName);
	    }
	 
	 });
  
});

function showAssets(roleId){
   var applicationId=   $("#selectAppId option:selected").val();   
	$("#dynamicPane").empty();		
	  if(applicationId > 0){	          
	       showDetails("swi/getAssetsForApplication.action","dynamicPane","get",{selectedApplicationId:applicationId,selectedRoleName:roleId},"showRoleDiv"+roleId+"Link","loadingShowRoleDiv"+roleId+"Link"); 
	   }
 }
 
 function showMembers(roleName){ 
   $.getJSON("swi/showPemittedAssetForRole.action",  {selectedRoleName:roleName} ,function(data) { 
                  $("#authorizedAssetsForRole").empty();
                   if(data.length>0){
	                   $.each(data,function(i,v){                                      
				   		 $("#authorizedAssetsForRole").append("<option value='"+v.id+"'>"+v.name+"</option>"); 				   		     
	                   });
                   }else{
                        $("#authorizedAssetsForRole").append("<option value='0' title='No Authorized dataset avilable for selected Role'>Dataset not available</option>");   
                   }
                var selectedAssetId =   $("#authorizedAssetsForRole option:selected").val();   
               showLookupTablesAndMembersForSelectedAsset(selectedAssetId,roleName);    
			             
      });      
 
 } 
 
 
 function showLookupTablesAndMembersForSelectedAsset(selectedAssetId,roleName){
              $.getJSON("swi/showLookupTablesForAsset.action",  {selectedAssetId:selectedAssetId,selectedRoleName:roleName} ,function(data) { 
			                $("#selectTableId").empty();
			                 if(data.length>0){
				                 $.each(data,function(i,v){                                      
							   		  $("#selectTableId").append("<option value='"+v.id+"'>"+v.name+"</option>"); 
				                  });
			                  }
			               var selectedTableId =   $("#selectTableId option:selected").val();
                           getMembersForSelectedTable(selectedAssetId,roleName,selectedTableId);  
			      });    


}
function getMembersForSelectedTable(selectedAssetId,roleName,selectedTableId){  
	$("#dynamicPane").empty();		
	  if(selectedAssetId > 0){	          
	       showDetails("swi/getMembersForSelectedTable.action","dynamicPane","get",{selectedAssetId:selectedAssetId,selectedRoleName:roleName,selectedTableId:selectedTableId},"showRoleDiv"+roleName+"Link","loadingShowRoleDiv"+roleName+"Link"); 
	   }
}
 




function getExistingObjectIds(queryString,context){ 
      $.each($("#permitted"+context+"IdsDiv input[name=permitted"+context+"Ids]"),function(k,v){  
       queryString= queryString+"&permitted"+context+"Ids["+k+"]="+$(this).val();    
     });      
    return queryString;  
}

function getSelectedObjectIds(queryString,context){     
    $('#'+context+'GridFormId input:checkbox:checked').each(function(k,v) {  
	   queryString= queryString+"&selected"+context+"Ids["+k+"]="+$(this).val(); 
     });   
  return queryString;  
}


function isExists(selectedEntity,context){
       var isExists=false;
       $.each($("#permitted"+context+"IdsDiv input[name=permitted"+context+"Ids]"),function(){	
          //alert("selectedEntity:-"+selectedEntity+" this:-"+$(this).val());
		    if(selectedEntity == $(this).val()){
		       isExists=true;
		       return;		      
			}
		});
	return isExists;
}



function isModified(context){
    var isChanged=false; 
    var selectedlength=$('#'+context+'GridFormId input:checkbox:checked').length;
    var existinglength=$("#permitted"+context+"IdsDiv input[name=permitted"+context+"Ids]").length;
     //alert("selectedlength:-"+selectedlength+" existinglength:-"+existinglength);
    $('#'+context+'GridFormId input:checkbox:checked').each(function(k,v) { 	  	      
       if(!isExists($(this).val(),context) || selectedlength != existinglength){
          isChanged=true;
          return;
       }		     
     }); 
     return isChanged;
}

function isChanngesSaved(obj){
     var context=$("#contextId").val(); 
     var isChanged= isModified(context);
     if(isChanged){
            var confirmChanges= confirm("Your changes are not saved.\n\nDiscard your changes?");           
			if(!confirmChanges){
			   return false;
			}  
	   }
   return true;
  
}


function showTables(roleName){
    $.getJSON("swi/showPemittedAssetForRole.action",  {selectedRoleName:roleName} ,function(data) { 
                  $("#authorizedAssetsForRole").empty();
                   if(data.length>0){
	                   $.each(data,function(i,v){                                      
				   		 $("#authorizedAssetsForRole").append("<option value='"+v.id+"'>"+v.name+"</option>"); 
	                   });
                   }else{
                        $("#authorizedAssetsForRole").append("<option value='0' title='No Authorized dataset avilable for selected Role'>Dataset not available</option>");   
                   }
             var selectedAssetId =   $("#authorizedAssetsForRole option:selected").val(); 
             getTablesForSelectedAsset(selectedAssetId,roleName);            
      });
  
	
}

function getTablesForSelectedAsset(selectedAssetId,roleName){  
	$("#dynamicPane").empty();		
	  if(selectedAssetId > 0){	          
	       showDetails("swi/getTablesForSelectedAsset.action","dynamicPane","get",{selectedAssetId:selectedAssetId,selectedRoleName:roleName},"showRoleDiv"+roleName+"Link","loadingShowRoleDiv"+roleName+"Link"); 
	   }
}

function showColumns(roleName){
      $.getJSON("swi/showPemittedAssetForRole.action",  {selectedRoleName:roleName} ,function(data) { 
                  $("#authorizedAssetsForRole").empty();
                   if(data.length>0){
	                   $.each(data,function(i,v){                                      
				   		 $("#authorizedAssetsForRole").append("<option value='"+v.id+"'>"+v.name+"</option>"); 				   		     
	                   });
                   }else{
                        $("#authorizedAssetsForRole").append("<option value='0' title='No Authorized dataset avilable for selected Role'>Dataset not available</option>");   
                   }
                var selectedAssetId =   $("#authorizedAssetsForRole option:selected").val();   
               showTablesAndColumnForAsset(selectedAssetId,roleName);    
			             
      });      
      
        
}

function showTablesAndColumnForAsset(selectedAssetId,roleName){
   $.getJSON("swi/showTablesForAsset.action",  {selectedAssetId:selectedAssetId,selectedRoleName:roleName} ,function(data) { 
			                $("#selectTableId").empty();
			                 if(data.length>0){
				                 $.each(data,function(i,v){                                      
							   		  $("#selectTableId").append("<option value='"+v.id+"'>"+v.name+"</option>"); 
				                  });
			                  }
			               var selectedTableId =   $("#selectTableId option:selected").val();
                           getColumnsForSelectedTable(selectedAssetId,roleName,selectedTableId);  
			      });    


}
function getColumnsForSelectedTable(selectedAssetId,roleName,selectedTableId){  
	$("#dynamicPane").empty();		
	  if(selectedAssetId > 0){	          
	       showDetails("swi/getColumnsForSelectedTable.action","dynamicPane","get",{selectedAssetId:selectedAssetId,selectedRoleName:roleName,selectedTableId:selectedTableId},"showRoleDiv"+roleName+"Link","loadingShowRoleDiv"+roleName+"Link"); 
	   }
}



function saveObjectSecurity(context){
    var url="";
    if(context=='Asset'){
	   url='swi/addSecurityPermissionForAsset.action';
	  }else if(context=='Table'){
	     url='swi/addSecurityPermissionForTable.action';
	  }else if(context=='Column'){
	     url='swi/addSecurityPermissionForColumn.action';
	  }else if(context=='Member'){
	     url='swi/addSecurityPermissionForMember.action';
	  }      
  var length=$('#'+context+'GridFormId input:checkbox:checked').length;  
   $("#actionMessage").empty();           
   if(length==0){  
      $("#errorMessage").empty().append("Please select aleast one Table");
      setTimeout(function(){ $("#errorMessage").empty(); },3000);      
      return;  
   }
   if(isModified(context)){ 
		  var selectedRoleName=$("#selectedRoleId").val();
		  var queryString="selectedRoleName="+selectedRoleName;
		  queryString=getSelectedObjectIds(queryString,context);
		  queryString=getExistingObjectIds(queryString,context);
		  var applicationId=$("#selectAppId option:selected").val();
		  var selectedAssetId = $("#authorizedAssetsForRole option:selected").val(); 
		  var selectedTableId = $("#selectTableId option:selected").val();  		
	      if(applicationId && applicationId > 0){
	           queryString=queryString+"&selectedApplicationId="+applicationId;
	        } 
	       if(selectedAssetId && selectedAssetId > 0){
	           queryString=queryString+"&selectedAssetId="+selectedAssetId;
	        }
	        if(selectedTableId && selectedTableId > 0){
	           queryString=queryString+"&selectedTableId="+selectedTableId;
	        } 
	        
	      $("#submitButton").hide();
	      $("#submitButtonLoader").show();    
		  $.post(url,queryString ,function(data) { 			
			     $("#dynamicPane").empty().append(data);
			      $("#submitButton").show();
	              $("#submitButtonLoader").hide();  
			   
			  });	
  }else{
    $("#errorMessage").empty().append("No changes found");
    setTimeout(function(){ $("#errorMessage").empty(); },3000);         
  }
}

function reset(){
     var roleName=$("#selectedRoleId").val();
     var selectedApplicationId=$("#selectedApplicationId").val(); 
      var context=$("#contextId").val();  
	  if(context=='Asset'){
	    showAssets(roleName);
	  }else if(context=='Table'){
	    showTables(roleName);
	  }else if(context=='Column'){
	    showColumns(roleName);
	  }else if(context=='Member'){
	    showMembers(roleName);
	  }     
}

</script>
