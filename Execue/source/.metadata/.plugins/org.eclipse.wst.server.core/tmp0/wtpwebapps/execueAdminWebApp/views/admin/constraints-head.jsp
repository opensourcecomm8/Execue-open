<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<LINK href="../css/common/roundedSearch.css" rel=stylesheet type="text/css">
<link href="../css/common/ui-layout-styles.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css" media="screen" />

<script language="JavaScript" src="../js/common/jquery.js"></script>
<script language="JavaScript" src="../js/admin/jquery.execue.js"></script>
<script language="JavaScript" src="../js/admin/menun.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script>

var jsAssetId;
var NoOfRows=0;

$(document).ready(function() { 
   jsAssetId = '<s:property value="applicationContext.assetId"/>';      
    if (jsAssetId) {
       $("#assetId").val(jsAssetId);
        showAssetTables();
		$("#container").show();
		$("#dottedLine").show();
    }/* else {
        tb_show("Select Dataset","swi/showAllAssets.action?sourceName=constraints&keepThis=true&TB_iframe=true&height=50&width=50 class=thickbox title=Select Asset");
    }*/
});

function showAssetTables(){
    $.get("swi/showConstraintTables.action?asset.id="+jsAssetId, {}, function(data) {
        $("#dynamicPaneAssetTables").empty();
        $("#dynamicPaneAssetTables").append(data);
    });//End of populate BusinessTerms
}

function getColomns(tableId){
    $.get("swi/showConstraintTables.action?asset.id="+jsAssetId, {}, function(data) {
        $("#dynamicPaneAssetTables").empty();
        $("#dynamicPaneAssetTables").append(data);
    });//End of populate BusinessTerms
}

function getConstraintsForTable(tableId) {
    $.get("swi/showTableConstraints.action",{"asset.id" : jsAssetId ,"tableConstraintsInfo.table.id" : tableId }, function(data) {
        $("#dynamicPane").fadeIn("fast");
        $("#dynamicPane").empty(); 
        $("#dynamicPane").append(data);
        $("#dynamicPane").show();
       // if(tableId != null){
       // registerAddReferenceClick();
       // }
       
       
       
    });
}

function getTableColumns(tableId){
 $.getJSON("swi/showRefTableColums.action",{"tableId" : tableId },function(data) {      
        if (data) {
         $("#refColomnList"+NoOfRows).empty();
        $select="<select name='refColumns' id='refColumns' MULTIPLE SIZE=3>";
        $option='';
        $.each(data, function(i, refTableColums) {         
          $option+="<option  value='"+refTableColums.id+"'>"+refTableColums.name+" </option>";        
		});	
		  $("#refColomnList"+NoOfRows).append($select+$option+"</select>");		
        }
    });

}

function registerAddReferenceClick() {
     NoOfRows++;    
    var tableId=$("#tableId").val();
    var assetId=$("#assetId").val();
    
   $.getJSON("swi/showPrimaryKeyTablesForForiegnKey.action",{"asset.id" : assetId,"tableId" : tableId  },function(data) {
     if (data) {
      $("#tableList").empty();
     $select="<select name='fKeyrefTable' id='fKeyrefTable' onchange='getTableColumns(this.value)'>";
     $option='';
     	$option+="<option value=''></option>";   	
        $.each(data, function(i, foriegnKeyTables) {
           	$option+="<option value='"+foriegnKeyTables.id+"'>"+foriegnKeyTables.name+"</option>";   				
			});
			 $("#tableList"+NoOfRows).append($select+$option+"</select>");	
        }      
    });
    $.getJSON("swi/showRefTableColums.action",{"tableId" : tableId },function(data) {
        if (data) {
        $("#colomnList").empty();
        $fkId="fKeyColumns"+NoOfRows;           
        $select="<select name='fKeyColumns' id="+$fkId+" MULTIPLE SIZE=3>";
        $option='';
        $.each(data, function(i, refTableColums) {         
          $option+="<option  value='"+refTableColums.id+"'>"+refTableColums.name+" </option>";        
		});	
		  $("#colomnList"+NoOfRows).append($select+$option+"</select>");		
        }
    });
    $fkname="fkeyName"+NoOfRows ;
        $rowContent="<tr id='tr"+NoOfRows+"' style='display:none;'>"
          + "<td><a href='javascript:deleteReference("+NoOfRows+");'>"
          + "<img src='../images/admin/disabledIcon.gif' alt='Remove Constraint' width='25' height='20' border='0' /></a></td>"
          + "<td><a href='javascript:editReference("+NoOfRows+");' class='links' id='3'>"
          +" <img src='../images/admin/editIcon.gif' alt='Modify Constraint' width='25' height='20' border='0' /></a></td>"
          +"<td><label><input name='fkeyName' type='text' id="+$fkname+" size='15'></label></td>"
          +"<td> <div id='colomnList"+NoOfRows+"'></div>"
          +"</td>"
          +"<td> <div id='tableList"+NoOfRows+"'></div>"
          +"</td><td>"
          +"<div id='refColomnList"+NoOfRows+"'></div>"
          +"</td>"
          +"<td>"
          +"<select name='foreignKeyIds' id='foreignKeyIds'"+NoOfRows+" multiple='true'>"
          +"</td>"
          +"</tr>";
        
        $("table#references").append($rowContent);
        $("#tr"+ NoOfRows).show();    
        $("name#foreignKeyIds").hide(); 
}

/*function deleteReference(x){
    $("#tr"+x).remove();   
}*/
function showSearch(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables").hide("");
  $("#"+divName+obj+"#roundedSearch").fadeIn("slow");
  
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
</script>
