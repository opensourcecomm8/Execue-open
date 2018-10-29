<%@ taglib prefix="s" uri="/struts-tags"%>
<link href="../css/common/styles.css" rel="stylesheet" type="text/css">
<LINK href="../css/common/roundedSearch.css" rel="stylesheet">
<LINK href="../css/admin/cssPopupMenu.css" rel=stylesheet>
<link href="../css/admin/treeDrag.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="../css/admin/thickbox.css" type="text/css" media="screen" />
<script type="text/javascript" src="../js/common/jquery.js"></script>

<script language="JavaScript" src="../js/admin//menun.js"></script>
<script language="JavaScript" src="../js/common/mm_menu.js"></script>
<script type="text/javascript" src="../js/common/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/admin/thickbox.js"></script>
<script src="../js/admin/searchList.js" type="text/javascript" language="javascript" /></script>
<script type="text/javascript" src="../js/admin/jquery.execue.searchRecordsComponent.js"></script>
<script type="text/javascript">

  var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
  var jsAssetId;
  var joinTypesSelectString;

  $(document).ready(function () {
   // jsAssetId = $("#assetId").val();
    jsAssetId = '<s:property value="applicationContext.assetId"/>';  
	$("#container").hide();
    if(jsAssetId){
      getAssetsJoins();
	  $("#container").show();
    }   
    createSelect("INNER JOIN");
	
	
	$("#divJoins").searchRecordsComponent({actionName:"",targetDivName:"divJoinsTable",divType:"span",divClass:"tableHolder"});
	
  });
  function createSelect(x){
	 
	   joinTypesSelectString = "";
		
	  $.each($("input.jType"), function(index, item) {
		optval=$(item).attr("typeAttr");
		opttext=$(item).val();					
		if(x==optval){
			joinTypesSelectString = joinTypesSelectString + "<option value='"+optval+"' selected>"+opttext+"</option>"; }
		else{
          joinTypesSelectString = joinTypesSelectString + "<option value='"+optval+"'>"+opttext+"</option>";
																 }
    });
  }
  /* to get the existing joins for the defined asset */
  function getAssetsJoins(){
      $.get("swi/showExistingJoins.action?assetId="+jsAssetId, {}, function(data) {
      $("#existingJoinsPane").empty();
      $("#existingJoinsPane").append(data);
    });//End of populate existing joins
  }
  
</script>

<SCRIPT type=text/javascript>
$(document).ready(function(){ 	
  
/*  $('#searchIcon').click(function() {
    $('#search').submit();
  });*/

 
});
</SCRIPT>


<SCRIPT type=text/javascript>

var rightColumn=null;
var leftColumn=null;
var rightTable=null;
var leftTable=null;
var hitFlag=false;
var tableId="";
var tableDisplayName="";
var leftTableDisplayName = "";
var rightTableDisplayName = "";
var leftColumnTempSug=null;
var rightColumntempSug=null;
$suggestedJoinsArray={};
$suggestedJoinsArrayArray=[]; // place holder for suggested or existing joins 
var fromJoinDifinitions=false;
$tableListClicked=false;
listItems="<select name='join'>"+ joinTypesSelectString +"</select>";
chkBox="<input name=checkbox4 type=checkbox id=checkbox4 checked=checked />";
spacer=" &nbsp;&nbsp;&nbsp;&nbsp;     ";

$joinsArray={};
$joinsArrayArray=[];

$(document).ready(function() {  

  $('div.rightTableHolder').droppable(
    { accept    : '.tablesList',
      hoverClass: 'dropareahoverRight',
      drop      : function(dropped)
      {
	      hitFlag=true;
        	rightTable=tableId;
        	rightTableDisplayName = tableDisplayName;
	if($tableListClicked){
   populateRightTable();
   
	}
   
	}
	
    }
  );
    
  $('div.leftTableHolder').droppable(
    { accept     : '.tablesList',
      hoverClass : 'dropareahoverLeft',
      drop       : function(dropped)
      {
      	 hitFlag=true;
		 leftTable=tableId;
		 leftTableDisplayName = tableDisplayName;
		 
	if($tableListClicked){
	populateLeftTable();
	 }
	 }
       
    }
  );
  
$('div.tablesList').draggable({
							  helper: 'clone', start: function()
				{
					$tableListClicked=true;
    				tableId=$(this).attr("id");
    				tableDisplayName= $(this).attr("displayName");

				}
							  
							  });
    

  });
function showSuggested(){
	if( $("#suggestedJoinsShowingDiv").css("display")=="none" ){
	$("#suggestedJoinsShowingDiv").slideDown();
	$("#suggestHeading").hide();
	}
}
function showJoinsSuggested(){
suggestedId=0;
slectId=0;
var suggestedCount=0;
//var jsAssetId = $("#assetId").val();
  if((leftTable!=null) && (rightTable!=null)){
    
    $("#dynamicPane").empty();
      $("#dynamicPane").fadeIn("fast");
      $("#dynamicPane").show();
      $("#container").css("overflow","hidden");
      $("#container").css("height","auto");

      $("#seperatorLeft").css("height",500);

      $("#seperatorRight").css("height",500);
      // $("#createNewJoins").show();
      $("#autoSuggestedJoins").show();
      
      $.getJSON("swi/showExistingJoinDefinitions.action?assetId="+jsAssetId+"&sourceTableName="+leftTable+"&destTableName="+rightTable,function (jsonData) {
      
      if(jsonData.data!==null && jsonData.length>0){            	
      	  showSaveJoinsImage();
      	  $("#enabledSaveJoin").show();
		  $("#disabledSaveJoin").hide();
		  
		  $existJ=$('<div style="width:100%;" id="exitstingJoinsShowingDiv"></div>');
          //$sugHeading=('<div id="suggestHeading" style="width:100%;padding:5px 0px;" ><a href="javascript:showHideSuggested();">Show Suggested Joins</a></div>');
          $suggestedJ=('<div style="width:100%;display:none;" id="suggestedJoinsShowingDiv"></div>');
										
		  $("#dynamicPane").append($existJ);
		 // $("#dynamicPane").append($sugHeading);
		  $("#dynamicPane").append($suggestedJ);
	      $.each(jsonData, function(item) {	
	     // alert("uiJoinDefInfo["+suggestedId+"].isSuggestedJoin"+this.suggestedJoin);
	      	 createSelect(this.type);
	      chkBox="<input  type='checkbox' name='uiJoinDefInfo["+suggestedId+"].checkedState' value='Yes' id='uiJoinDefInfo["+suggestedId+"].checkedState'"+" "+this.checkedState+" onclick='checkSavingJoins();'/>";
	      chkBox=chkBox+"<input type='hidden' id='uiJoinDefInfo["+suggestedId+"].lhsColumn' value='"+this.lhsColumn+"' name='uiJoinDefInfo["+suggestedId+"].lhsColumn' />";
	      chkBox=chkBox+"<input type='hidden' id='uiJoinDefInfo["+suggestedId+"].rhsColumn' value='"+this.rhsColumn+"' name='uiJoinDefInfo["+suggestedId+"].rhsColumn' />";
	      var checkSuggest='';
	      if(this.suggestedJoin){
	      		checkSuggest="*";
				$("#suggestHeading").show();
	      	}
	      listItems="<select name='uiJoinDefInfo["+slectId+"].type' id='uiJoinDefInfo["+slectId+"].type'>"+joinTypesSelectString+"</select>";
  
	      //rowContent="<div class='rowContentClass' style='padding-left:20px'><table width='450'><tr><td>"+checkSuggest+"</td><td width='150'>"+this.lhsColumn+"</td><td></td><td>"+listItems+"</td><td width='150'><span style='padding-left:5px;'>"+this.rhsColumn+"</span></td><td width='40'>"+chkBox+"</td></tr></table></div> ";
		   if(this.suggestedJoin){
	       rowContent="<div class='rowContentClass' suggested='Yes' style='padding-left:20px;padding-right:20px;'><table  width='450px' style='table-layout:fixed'> <col width='10px'><col width='120px'><col width='120px'><col width='120px'><col width='10px'><tr><td>"+checkSuggest+"</td>"
	      											+"<td style='word-wrap: break-word;'>"+this.lhsColumn+"</td>"
	      											+"<td >"+listItems+"</td>"
	      											+"<td style='word-wrap: break-word;'><span style='padding-left:5px;'>"+this.rhsColumn+"</span></td>"
	      											+"<td>"+chkBox+"</td></tr>"
	      											+"</table></div> ";
		   }else{
			   
			  rowContent="<div class='rowContentClass'  suggested='No' style='padding-left:20px;padding-right:20px'><table  width='450px' style='table-layout:fixed'> <col width='10px'><col width='120px'><col width='120px'><col width='120px'><col width='10px'><tr><td>"+checkSuggest+"</td>"
	      											+"<td style='word-wrap: break-word;'>"+this.lhsColumn+"</td>"
	      											+"<td >"+listItems+"</td>"
	      											+"<td style='word-wrap: break-word;'><span style='padding-left:5px;'>"+this.rhsColumn+"</span></td>"
	      											+"<td>"+chkBox+"</td></tr>"
	      											+"</table></div> "; 
		   }
	            // alert("this.leftColumnName:"+this.lhsColumn);
				 $suggestedJoinsArray["leftColumnName"]=this.lhsColumn;
				 // alert("this.rightColumnName:"+this.rhsColumn);
				 $suggestedJoinsArray["rightColumnName"]=this.rhsColumn;						 
				 $suggestedJoinsArrayArray.push($suggestedJoinsArray);
				 $suggestedJoinsArray={};	
	            //left column list creation
	              
				   if(!this.suggestedJoin){
				  $("#exitstingJoinsShowingDiv").append(rowContent);
				   }else{
				  $("#suggestedJoinsShowingDiv").append(rowContent);
				  suggestedCount++;
				   }
	              suggestedId++;
	              slectId++;
	       });
    	} if(suggestedCount==0){$("#suggestHeading").hide();}     
   });
 }	

}

function hideSaveJoinsImage(){
	$("#autoSuggestedId").hide();
 	$("#saveSelectedJoinId").hide();
 	$("#disabledSaveJoin").hide();
}
function showSaveJoinsImage(){
	$("#autoSuggestedId").show();
 	$("#saveSelectedJoinId").show();
}
function showSearch(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables").hide("");
  $("#"+divName+obj+"#roundedSearch").fadeIn("slow");
  $("div#searchIcon img#Image2a").bind("mouseover",function(){findString="a"; })
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
  $("div#searchIcon1 img#Image2").bind("mouseover",function(){findString="span.tableHolder"; })
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

</SCRIPT>
<script>
	function saveSelectedJoins(){
	saveJoinsDefinition();
	/*	var  x = 0;	
	var values = new Array();	
	
	for(i=0;i<document.getElementsByTagName("input").length;i++)
	{
		if(document.getElementsByTagName("input")[i].type == "checkbox")
		{					
			if(document.getElementsByTagName("input")[i].checked == true)
			{
				var str=document.getElementsByTagName("input")[i].id;
				//var str=document.getElementsByTagName("input")[i].val();
				var subStr=str.substring(0,16);//"cb_user_created_"
				var subStrSugg=str.substring(0,13);//"cb_suggested_"						
				if(subStr=="cb_user_created_" ){					
					var joinType=$('#select_user_created_'+str.substring(16)+' :selected').val();												
					values[x++]=joinType+"$"+$("#"+str).val();
				}else if(subStrSugg=="cb_suggested_"){					
					var joinType=$('#select_suggested_'+str.substring(13)+' :selected').val();											
					values[x++]=joinType+"$"+$("#"+str).val();
				}						
			}
		}				
  /* Create new joins  */
/*	if(values.length>0){
	     $("#message").empty();
         //assetId = $("#assetId").val()
		$.getJSON("swi/createJoins.action", {joinColumInfo:values,assetId:jsAssetId,sourceTableName:leftTable,destTableName:rightTable}, function(data) {					
			$("#message").append(data);
	         getAssetsJoins();
		});
	  createDefaultVals();
	}*/
}
function showJoindefinitions(x,y){
$("#message").empty();
$("#suggestHeading").show();
createDefaultVals();
leftTable=x;
rightTable=y; 
hitFlag=true;
fromJoinDifinitions=true;
populateLeftTable();
populateRightTable();
$("#leftTableName").html(x);
$("#rightTableName").html(y);
$("#saveJoinLoader").hide();
}
function createDefaultVals(){
    $joinsArray={};
    $joinsArrayArray.length=0;// clear the user created array length
    $suggestedJoinsArrayArray.length=0;// clear the existing joins array length
    $suggestedJoinsArray={};
	$("#joinsContent").empty();
	$("#resultsList").hide();
	$("#resultsListHeading").hide();
}
function populateLeftTable(){  
  $("#message").empty();
   hideSaveJoinsImage();
   $("#autoSuggestedJoins").hide();
	if(hitFlag){ 
	$tableListClicked=false;
	   hideSaveJoinsImage();      
      $("#sourceTableName").val(leftTable);         
      createDefaultVals();
    //  if(leftTable==rightTable){alert("this table already selected");return false;}
      //alert("leftTable::"+tableId);
      $("#leftColumnList").empty();
      
      $.getJSON("swi/showTableColumnsForJoin.action?assetId="+jsAssetId+"&tableName="+leftTable,function (jsonData) {
      	
        $.each(jsonData, function(item) {
          //alert(this.name+"::"+this.id); 
          colContent="<LI class=leftColumn id="+this.columnName+" ><IMG class=folderImage src=../images/admin/columnIcon.jpg><SPAN class=leftColumnHolder>"+this.columnName+"</SPAN> ";
          //left column list creation
          $("#leftColumnList").append(colContent);
        });
        //left column draggable 
        $('li.leftColumn').draggable({ helper: 'clone',start:function(){ leftColumn=this.id; } });
        // left column draggable end
        
      });
      
      $("#columnsRowLeft").show();
      $("#leftTableName").empty();
      $("#leftTableName").append(""+leftTableDisplayName+"");
      $(".leftTableHolder").css("height",20);
     
	  if(!fromJoinDifinitions){	  	
		   showJoinsSuggested();
      hitFlag=false;
	  }
    }
}
function populateRightTable(){ 
 hideSaveJoinsImage();
 $("#autoSuggestedJoins").hide();
 $("#message").empty();
 if(hitFlag){ 
 $tableListClicked=false;
 	hideSaveJoinsImage();                 
    $("#destTableName").val(rightTable);
    createDefaultVals();
   // if(leftTable==rightTable){alert("this table already selected"); return false;}
    $("#rightColumnList").empty();
    //alert(rightTable);
     $.getJSON("swi/showTableColumnsForJoin.action?assetId="+jsAssetId+"&tableName="+rightTable,function (jsonData) {
           
            $.each(jsonData, function(item) {
          //alert(this.name+"::"+this.id); 
      colContent="<LI class=rightColumn ><IMG class=folderImage src=../images/admin/columnIcon.jpg><SPAN class=rightColumnHolder id="+this.columnName+">"+this.columnName+"</SPAN> ";
          $("#rightColumnList").append(colContent);
          });
        
        //////////////////////////////////////////////////////////////////////////
             $('span.rightColumnHolder').droppable(
            {
			  accept    : '.leftColumn',
			  hoverClass: 'dropOver',
			  tolerance	: 'pointer',
              drop      : function(dropped)
              {
                
              	hitFlag=true;
                if(hitFlag){                     
                         
          rightColumn=this.id; 
             
          //alert("leftColumn::"+leftColumn+"::rightColumn::"+rightColumn);
          var joinRowText=leftColumn+spacer+listItems+spacer+rightColumn;
          obj=checkDuplicates(leftColumn,rightColumn);
          if(!obj){
          
          $joinsArray["leftColumnName"]=leftColumn;
          $joinsArray["rightColumnName"]=rightColumn;
          $joinsArray["checkedState"]="checked";
	      $joinsArrayArray.push($joinsArray);
	      $joinsArray={};
          
         
         createJoin();
        $("#resultsList").show();
        $("#resultsListHeading").show();
        $("#autoSuggestedJoins").show();
          }
          hitFlag=false;
          }
              }
            });
        /////////////////////////////////////////////////////////////////////////
     
        /////////////////////////////////////////////////////////////////////////
        });
    $("#columnsRowRight").show();
   // $("#columnsRowRightImage").hide();
    $(".rightTableHolder").css("height",20);
    $("#rightTableName").empty();
    $("#rightTableName").append(" "+rightTableDisplayName+" ");
     showJoinsSuggested();
      
      
    hitFlag=false;
	fromJoinDifinitions=false;
    }	
}

 function createJoin(){
  	var leftvalue=null;
	var rightvalue=null;
	$("#enabledSaveJoin").show();
	$("#disabledSaveJoin").hide();
    $("#joinsContent").empty();
    $("#autoSuggestedId").show();
    showSaveJoinsImage();
	createSelect("INNER JOIN");
    var joinsContent = "<div class='rowContentClass' id='jrow'><table width='480' border='0' cellpadding='2'>";
    for(i=0;i<$joinsArrayArray.length;i++){
    	temp=$joinsArrayArray[i];
        count=0;
		tempContent="";
        tempContent="<tr><td width='20' border='1'><a class='del' href='#' id='"+i+"'><img src='../images/admin/remove.jpg' border='0' alt='delete' title='delete'/></a></td>";
        for(key in temp){
          listItems="<td width='120'  ><select name='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].type' id='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].type'>"+joinTypesSelectString+"</select></td>";
          if(key=="leftColumnName"){
          	leftvalue=temp[key];          	
          	tempContent+="<td width='150'>"+temp[key]+"</td>"+listItems;
          }
          if(key=="rightColumnName"){
          	rightvalue=temp[key];          	
          	tempContent+="<td width='150' <span style='padding-left:5px;'>"+temp[key]+"</span></td>";
          }
          if(key=="checkedState"){
          	chkBox="<td width='40'><input name='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].checkedState' type='checkbox' value='Yes' id='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].checkedState'"+" "+temp[key]+" onclick='checkSavingJoins();'/></td>";
          	chkBox=chkBox+"<input type='hidden' id='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].lhsColumn' value='"+leftvalue+"' name='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].lhsColumn' />";
          	chkBox=chkBox+"<input type='hidden' id='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].rhsColumn' value='"+rightvalue+"' name='uiJoinDefInfo["+(i+parseInt($suggestedJoinsArrayArray.length))+"].rhsColumn' />";
             tempContent+=chkBox+"</tr>";
          }
        }
		joinsContent+=tempContent;
	}
   joinsContent+="</table></div>";
      $("#joinsContent").append(joinsContent);
      if($suggestedJoinsArrayArray.length == 0){    		
    		$("#autoSuggestedId").hide();
    	}  
    $("a.del").click(function(){    	
        $joinsArrayArray.splice(this.id,1);
        createJoin();
     if($joinsArrayArray.length==0){        	
        	$("#resultsListHeading").hide();
        	 createJoin();	
       } 
       if($suggestedJoinsArrayArray.length==0 && $joinsArrayArray.length==0){      
       	$("#autoSuggestedJoins").hide();
        	 createJoin();
       }       
	});
	checkSavingJoins();
    
  }
  
  function checkDuplicates(leftColumn,rightColumn){
    for(i=0;i<$joinsArrayArray.length;i++){
      temp=$joinsArrayArray[i];
      for(key in temp){
        if(key=="leftColumnName")
          leftColumnTemp=temp[key];
  
        if(key=="rightColumnName")
          rightColumnTemp=temp[key];
      
      }
      if((leftColumnTemp==leftColumn)&&(rightColumnTemp==rightColumn)){
        alert("join Exist");
        return true
      }
    }
     for(var j=0;j<$suggestedJoinsArrayArray.length;j++){
		var tempSug=$suggestedJoinsArrayArray[j];
		for(key in tempSug){
			if(key=="leftColumnName")
				leftColumnTempSug=tempSug[key];
				//alert("leftColumnTempSug"+leftColumnTempSug);
				if(key=="rightColumnName")
					rightColumntempSug=tempSug[key];
					//alert("rightColumntempSug  "+rightColumntempSug);	
				} 	
				if((leftColumnTempSug==leftColumn) && (rightColumntempSug==rightColumn)){
					alert("join Exist");
					return true
				}
		}
    return false;
  }
  function deleteUIAssetJoins(){
  $("#deleteProcess").show();
  $("#enableDeleteJoin").hide();
 // var uiAssetJoins ="";
 	var uiAssetJoins = $("#existingJoins").serialize();
  	//alert("uiAssetJoins"+uiAssetJoins);  	
     uiAssetJoins = uiAssetJoins + "&" + "assetId="+jsAssetId;
	 
	/* $.each($("#existingJoins input"),function(i,v){
				
		 $idName=$(this).attr("id");
			     $chkVal=$(this).val();
				 $t=true;
				 if(($(this).attr("type")=="checkbox") && !($(this).is(':checked'))){
				 $t=false;
				 uiAssetJoins = uiAssetJoins+$idName+"=No&";
			  }
			  if($t){
				uiAssetJoins = uiAssetJoins+$idName+"="+$chkVal+"&";
				
			  }
			// }
	  });
        
		 uiAssetJoins = uiAssetJoins+"&assetId="+jsAssetId;
      // alert("uiAssetJoins:"+uiAssetJoins);*/
	   
	   
      $("#message").empty();
		$.getJSON("swi/deleteJoins.action", uiAssetJoins, function(data) {				
			  $("#message").append(data);
		      getAssetsJoins();	
		      //$("#disableDeleteJoin").show(); 
			});
			$("#resultsListHeading").hide();
			$("#autoSuggestedJoins").hide();
			$("#rightTableName").empty().append("Right Table");
			$("#leftTableName").empty().append("Left Table");			
			$("#leftColumnList").empty();
			$("#rightColumnList").empty();	
			$("#columnsRowRight").hide();
			$("#columnsRowLeft").hide();
			$(".leftTableHolder").css("height",180);
			$(".rightTableHolder").css("height",180);
			rightTable=null;
			leftTable=null;
			hideSaveJoinsImage();		
			createDefaultVals();	
  }
 /* function deleteSelectedJoins(){
	var  x = 0;	
	var deleteSelectedJoins = new Array();
	for(i=0;i<document.getElementsByTagName("input").length;i++)
	{		
		if(document.getElementsByTagName("input")[i].type == "checkbox")
		{		
			if(document.getElementsByTagName("input")[i].checked == true)
			{	
				var strValue=document.getElementsByTagName("input")[i].id;				
				if(strValue!=null && strValue.substring(0,8)=="existing"){
					var existingJoin=strValue.substring(8,strValue.length);
					//alert("strVAlue"+existingJoin);
					deleteSelectedJoins[x++]=existingJoin;	
				}		    
			}
		}				
	}*/
	/* Delete joins*/
	/*if(deleteSelectedJoins.length>0){
	    $("#message").empty();
		$.getJSON("swi/deleteJoins.action", {deleteJoinsInfo:deleteSelectedJoins,assetId:jsAssetId}, function(data) {				
			  $("#message").append(data);
		      getAssetsJoins();	 
			});
			createDefaultVals();
	}
}*/
 /* Create new joins  */
 function checkSavingJoins(){
 	if(!$(".ui-layout-center input:checkbox").is(":checked")){
 		$("#enabledSaveJoin").hide();
		$("#disabledSaveJoin").show();
	}else{
		$("#enabledSaveJoin").show();
		$("#disabledSaveJoin").hide();
	}
 }
function saveJoinsDefinition() {
  $("#saveJoinLoader").show();
  $("#enabledSaveJoin").hide();
var joinDefinitions ="";
	//alert("values:::"+ $("#joinDefinitions").serialize());
   // var joinDefinitions = $("#joinDefinitions").serialize();
   // joinDefinitions = joinDefinitions + "&" + $("#requiredValues").serialize();


 $checkedJoin="";

	  $.each($(".ui-layout-center input,.ui-layout-center select"),function(i,v){
			
		  
			//if($(this).attr("id").split(".")[0]== $checkedJoin){
				
				 $idName=$(this).attr("id");
			     $chkVal=$(this).val();
				 $t=true;
				 if(($(this).attr("type")=="checkbox") && !($(this).is(':checked'))){
				 $t=false;				 
				 joinDefinitions = joinDefinitions+$idName+"=No&";
			  }
			  if($t){			  
				joinDefinitions = joinDefinitions+$idName+"="+$chkVal+"&";
				
			  }
			  
			// }
	  });  
	//  alert(joinDefinitions);   
	 
	  var joinCheck =  checkDuplicateJoinCreation(); 
	  if(!joinCheck){  
		 joinDefinitions = joinDefinitions+"destTableName="+$("#destTableName").val()+"&sourceTableName="+$("#sourceTableName").val()+"&assetId="+jsAssetId; 
      //$("#results").empty();
      //$("#results").text(str);       
      $("#message").empty();
		$.post("swi/createJoins.action", joinDefinitions ,function(data) {					
			$("#message").append(data);
	         getAssetsJoins();
	         
			  $("#saveJoinLoader").hide();
             $("#enabledSaveJoin").show();
		},"json"); 
		
		
}else{
$("#saveJoinLoader").hide();
  $("#enabledSaveJoin").show();	
}
  
	  //createDefaultVals();

    }
   function showData(data){ alert(data);
		$("#message").append(data);
	         getAssetsJoins();
	         
			  $("#saveJoinLoader").hide();
             $("#enabledSaveJoin").show();
		 
		} 
    
  function checkDuplicateJoinCreation(){
  
   for(i=0;i<$suggestedJoinsArrayArray.length;i++){
 	 var temp = $suggestedJoinsArrayArray[i];
  	var jType=document.getElementById("uiJoinDefInfo["+i+"].type");
  	var joinType=jType.options[jType.selectedIndex].value;
  	var leftColumn;
  	var rightColumn;
  	var joinCount =0;
  	for(key in temp){
		 if(key=="leftColumnName")
         leftColumn = temp[key];
        if(key=="rightColumnName")
         rightcolumn = temp[key];
    }
    for(j=0;j<$suggestedJoinsArrayArray.length;j++){
      	var innerLoopTemp = $suggestedJoinsArrayArray[j];
      	var jTypeInner=document.getElementById("uiJoinDefInfo["+j+"].type");
      	 var checkedState = document.getElementById("uiJoinDefInfo["+j+"].checkedState").checked;
      var joinTypeInner=jTypeInner.options[jTypeInner.selectedIndex].value;
 		var leftColumnInner;
 		 var rightColumnInner;
 		 	for(innerKey in innerLoopTemp){
		 		if(innerKey=="leftColumnName")
         			leftColumnInner = innerLoopTemp[innerKey];
        		if(innerKey=="rightColumnName")
         			rightColumnInner = innerLoopTemp[innerKey];
         	}		
         		if((leftColumnInner==leftColumn) && (rightColumnInner==rightcolumn) && (joinTypeInner==joinType) && (checkedState)){
         		   joinCount++;
         		}  
            		   	
      }  
      if(joinCount>1){
        alert('Duplicate Join Exists');
        return true;
      }
      }
    return false;  
 }   
 /* enable disable delete join button */
  function checkedJoin(){
	  var checkd=false;
	//  if($.browser.msie){
     checkd= $("#existingJoins input:checkbox").is(':checked');
	//  }else{
   //  checkd= $("#existingJoins input[type='checkbox']").is(':checked');
	//  }

     if(checkd){
	    $("#enableDeleteJoin").show();
	    $("#disableDeleteJoin").hide();
	    }
	    else{
	    $("#enableDeleteJoin").hide();
	    $("#disableDeleteJoin").show();
	    }
	 }
</script>

<SCRIPT language=JavaScript type=text/javascript>
  var client_id = 1;
</SCRIPT>

<SCRIPT language=JavaScript src="" type=text/javascript>
</SCRIPT>

<NOSCRIPT>
<P><IMG height=1 alt="" src="" width=1></P>
</NOSCRIPT>
