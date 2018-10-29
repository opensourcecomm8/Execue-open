// JavaScript Document
var searchTypeClicked="";
var specialCharMessage="Please enter only letters , numeric characters";
var stringEmptyMessage="Required";
$img="<div id='loaderDiv'><img src='../images/admin/loading.gif' width='20' height='20' /></div>";
	function showDetails(actionName,divName,method,params,linkId,linkLoaderId,async){  // used to send action name , div name and get the details 
	if(async==undefined){async=true; }
	    showLoader(linkId,linkLoaderId)
		$.ajax({url:actionName,
			    data:params,
				success:function(data){appendToDiv(divName,data,linkId,linkLoaderId);},
			    type:method,
				async: async
			   });
		/*if(method=="get"){
		   $.get(actionName,params, function(data) {
				//alert("get:"+data);
			  appendToDiv(divName,data,linkId)
			});
		}else{
			 $.post(actionName,params, function(data) {
				//alert("post:"+actionName+":"+data);
			  appendToDiv(divName,data,linkId,linkLoaderId)
			});
		}*/
	}
	function showLoader(linkId,linkLoaderId){
		if(linkId!=""){
		$("#"+linkId).hide();
		if(!linkLoaderId){
		$("#"+linkId).after($img);
		}else{
	    $("#"+linkLoaderId).show();
		}
		}
	}
	function hideLoader(linkId,linkLoaderId){
		if(linkId!=""){
		$("#"+linkId).show();
		if(!linkLoaderId){
	    $("#loaderDiv").remove();
		}else{
	    $("#"+linkLoaderId).hide();
		}
		}
	
	}
	function appendToDiv(divName,data,linkId,linkLoaderId){
		  //$("#"+divName).empty();
		  $("#"+divName).empty().append(data);
		  if(linkId!=""){
		  hideLoader(linkId,linkLoaderId);
		  }
	}
	
	function deleteRecord(deleteMessage,actionName,divName,callType,method){
		//alert(actionName+"::"+callType);
		confirmed = confirm(deleteMessage);
		if (confirmed) {
			if(callType=="ajaxCall"){
			showDetails(actionName,divName,method)	
			}else{
			document.location=actionName;
			}
		}
	}
	function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
	}
	
	
  function showAll(searchBox){
	  if(searchBox==2){
		
			$.each($("table#searchList tr"),function(i,k){
				$temp=$(this).find("a");									   
				$(this).show();			
				$temp.html($temp.text());
			});
		
		}else{
	  processSearch(searchBox,"ShowAll"); 
	  searchTypeClicked="all";
		}
  }

function processSearch(searchBox,searchType){
if(searchBox==1){searchBox="";}	
searchString=$("#searchText"+searchBox).val().toLowerCase();	
  if(searchString=="search"){
    alert("please enter search string");
	$("#searchText"+searchBox).focus();
	}else{
	searchTypeClicked=searchType;	
	getPaneSearchDetails(searchType,searchString);
	}  	
}

function startsWithString(searchBox){
	if(searchBox==2){		
			showAll(searchBox);
			searchInList("startWith");
	}else{
		processSearch(searchBox,"startWith");
		searchTypeClicked="startWith";
	}
}

function containsString(searchBox){
	if(searchBox==2){	
		    showAll(searchBox);
			searchInList("contains");
			
	}else{
		processSearch(searchBox,"contains"); 
		searchTypeClicked="contains";
	}
}
function searchInList(searchType){
	checkFor=0;
	
	       searchString=$("#searchText2").val().toLowerCase();
			 if(searchString=="search"){
				alert("please enter search string");
				$("#searchText2").val("").focus();
				}else{
		    $.each($("table#searchList tr"),function(i,k){
			$temp=$(this).find("a");
			if(searchType=="contains"){if($temp.text().toLowerCase().indexOf(searchString)==-1){$(this).hide();}}
			if(searchType=="startWith"){if($temp.text().toLowerCase().indexOf(searchString)!=0){$(this).hide();}}
			$temp.html(autoHighlight($temp.text(), searchString));
		     });
		}
}
function searcTextFocusInitialise(){
$('#searchText,#searchText2').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText,#searchText2').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
}

function setOrderField(orderBy){
if(orderBy=="name"){
		if(nameOrderBy=="Asc"){nameOrderBy="Desc";}else{nameOrderBy="Asc";}	
		order=nameOrderBy;
		sortField="displayName";
		sortDiv="sortByNameLink";
		}else{
			if(descOrderBy=="Asc"){descOrderBy="Desc";}else{descOrderBy="Asc";}
			order=descOrderBy;
			sortField="description";
			sortDiv="sortByDescLink";
			}	
			//alert(sortField+"::::::::::"+order)
}
function highLightText(textboxId){
	if(textboxId==1){textboxId="";}
if(searchTypeClicked=="contains"){
highLightTextSubFunction(textboxId,"contains");
 }
 if(searchTypeClicked=="startWith"){
highLightTextSubFunction(textboxId,"startWith");
 }
 if(searchTypeClicked=="all"){ 
 highLightTextSubFunction(textboxId,"all");
 }	
}
function highLightTextSubFunction(textboxId,schType){
 $.each($("table#searchList"+textboxId+" tr"),function(i,k){
		$temp=$(this).find("a");
		if(schType!="all"){
		$temp.html(autoHighlight($temp.text(), searchString));
		}else{
		$temp.html($temp.text());	
		}
		});	
}

function checkSpecialChar(obj){
var checkString = obj.val();
var splCharExist=false;
	if (checkString != "") {
		if ( /[^A-Za-z0-9_\s]/.test(checkString)) {
			//alert("Please enter only letter and numeric characters");
		   splCharExist=true;
		   
		}
	}
return splCharExist;
}

function checkEmptyString(obj){
	var checkString = $.trim(obj.val());
	var stringEmpty=false;
	if (checkString == "") {
		stringEmpty=true;
	}
	return stringEmpty;
}
function setFocus(obj){
obj.select().focus();
tempField = obj;
setTimeout("tempField.select().focus();",1);	
}