(function($){
   
$.fn.searchRecordsComponent = function(options){
var $containsString="Contains";
var $startsWithString="Starts With";
var $showAllString="Show All";
return this.each(function(){
						   
						   
var $me = undefined;
var $ImageSearch=$('<img src=../images/searchEnd.gif name=ImageSearch2 border=0 />');
var $roundedSearch=$('<DIV></DIV>').addClass("searchBoxStyle");;
var $startSearch=$('<div class=searchStart></div>');
var $input=$('<INPUT class=searchField type=search value=Search>');
var $searchIcon1=$('<div class=searchEnd ></div>');
var $ImageSearchLink=$('<a href="#"></a>');
var $ul=$("<ul></ul>");
var $span=$("<span class=nav></span>");
var $timer=null;

var $liContains=$("<li></li>");
var $liStartsWith=$("<li></li>");
var $liShowAll=$("<li></li>");
var $searchString="";
var enterTextMessage="please enter search string";

	$liContains.addClass("listBgColor").append($containsString).click(processSearchContains);
    $liStartsWith.addClass("listBgColor").append($startsWithString).click(processSearchStartsWith);
    $liShowAll.addClass("listBgColor").append($showAllString).click(processSearchShowAll);
	$input.blur(inputBlurValidate).focus(inputFocusValidate).bind('keypress', function(e) {
					if (e.keyCode == 13) {
						if(!checkEmptyString()){ 
						processSearchContains($liContains);
						return false;
						}
					}
					else if(e.keyCode==9){
						$ul.show();
					}
					
					else if(e.keyCode==8){
						var displayString=$(this).val();
						if(displayString.indexOf(enterTextMessage)>-1){
												  $(this).val("");
												  return;
												  }
						//$("#searchBtn").trigger('click');
					}

				}).bind("click",function(){$(this).val("");});
	
var	$opts = $.extend({rowType:" table tr "},options);
	
var	$actionName=$opts.actionName;
var $targetDivName=$opts.targetDivName;
// if value is present, means read that element's value as boolean to interrupt the searching
var $userInterrupt = "";
if($opts.userInterrupt){
	$userInterrupt = $opts.userInterrupt;
}
var $leftSize = 0;
var $topSize = 0;
if($opts.leftSize > 0){
	$leftSize = $opts.leftSize; 
} else {
	$leftSize = 103;
}
if($opts.topSize > 0){
	$topSize = $opts.topSize; 
} else {
	$topSize = 22;
}
	//alert($opts.actionName);alert($opts.targetDivName);
   $me = $(this);
  	$me.append($roundedSearch);	
	$ImageSearchLink.append($ImageSearch);	
																		
	$roundedSearch.append($startSearch);
	$roundedSearch.append($input);
	$roundedSearch.append($searchIcon1);
	
	$span.append($ImageSearchLink);
	$span.append($ul);
	$searchIcon1.append($span);	
	
	$ul.append($liContains);
	$ul.append($liStartsWith);
	$ul.append($liShowAll);
	$ul.css("left",($me.position().left+$leftSize)+"px");
	$ul.css("top",($me.position().top+$topSize)+"px");
	//$me.append($ul);
	$ImageSearchLink.bind("mousemove",function(){	
											   clearTimeout($timer);
			$ul.show();			
		 }).bind("mouseout",function(){
			 
		slideUpUl()
		 }).bind("click",function(){
		processSearchContains($liStartsWith);
		 });
	

	$ul.find('li').mousemove(function(){
				clearTimeout($timer);
					$ul.show()
				$(this).removeClass("listBgColor").addClass("listBgColorHover");	   
		}).mouseout(function(){
	
				$(this).removeClass("listBgColorHover").addClass("listBgColor");	  
				slideUpUl();
		 });
	function slideUpUl(){
		$timer=setTimeout(function(){$ul.hide();},2000);
	}
	
	function processSearchContains(){
	
	operator="&";
		if(!checkEmptyString()){
			if($actionName!=""){
				if($actionName.indexOf("?")==-1){ operator="?";}
			getSearchResults($actionName+operator+"searchString="+$searchString+"&searchType=contains","get",false);
			$("#search_string").val($searchString);
			$("#search_type").val("contains");
			highLightText();
		   }else{
			 
			 
			searchInListShowAll();
			 
			searchInList("contains");
		   }
		}
	}
	
	
	function processSearchStartsWith(){
	searchString=$input.val().toLowerCase();	
	operator="&";
		if(!checkEmptyString()){
			if($actionName!=""){
				if($actionName.indexOf("?")==-1){operator="?";}
				getSearchResults($actionName+operator+"searchString="+$searchString+"&searchType=startWith","get",false);
				$("#search_string").val($searchString);
				$("#search_type").val("startWith");
				highLightText();
			} else {
				searchInListShowAll();			 
				searchInList("startWith");
			}
		}
	}
	
	function processSearchShowAll(){
		if($actionName!=""){	
		getSearchResults($actionName,"get",false);
		$("#search_string").val("");
		$("#search_type").val("");
		removeHighLight();
		}else{
			searchInListShowAll();
		}
	}
	function getElement(obj){
		$temp=undefined;
	if($opts.divType!="span")	{										  
			$temp=obj.find("a");
			}else{
			$temp=obj.find("span."+$opts.divClass);	
			}
			return $temp;
	}
	
	function searchInList(searchType){
	checkFor=0;
		    $.each($("#"+$targetDivName+" "+$opts.rowType),function(i,k){
			$temp=getElement($(this)); 
			if(searchType=="contains"){if($temp.text().toLowerCase().indexOf($searchString)==-1){$(this).hide();}}
			if(searchType=="startWith"){if($temp.text().toLowerCase().indexOf($searchString)!=0){$(this).hide();}}
			$temp.html(autoHighlight($temp.text(), $searchString));
		     });
		
     }
	 
	 function searchInListShowAll(){
		 $ul.hide();
		 $.each($("#"+$targetDivName+" "+$opts.rowType),function(i,k){
				$temp=getElement($(this));									   
				$(this).show();			
				$temp.html($temp.text());
			}); 
	 }
	
	
	
	function highLightText(){
		removeHighLight();
		$.each($("#"+$targetDivName+" div.tableList "+$opts.rowType),function(i,k){
			$temp=getElement($(this));
			$temp.html(autoHighlight($temp.text(), $searchString));
			});
	}
	
	function removeHighLight(){
		$.each($("#"+$targetDivName+" div.tableList "+$opts.rowType),function(i,k){
		    $temp=getElement($(this));								   		
			$temp.html($temp.text());
		   });
	}
	
	function checkEmptyString(){
	$searchString=$input.val().toLowerCase();	
	
	if(($searchString=="search")||($searchString=="")||($searchString==enterTextMessage)){
		$input.val(enterTextMessage);
		$input.focus();
		$ul.hide();
		processSearchShowAll($liShowAll)
		return true;
		}else{
			return false;
		}
	}
	
	function getSearchResults(actionName,method,async){  // used to send action name , div name and get the details 
		var validate = _validateUserNav();
		if(validate){
			$.ajax({url:actionName,
				success:displayData,//function(data){appendToDiv(divName,data,linkId,linkLoaderId);},
			    type:method,
				async:async,
				error:error,
				timeout:10000
			});
		}
	}
	function error(){
		$targetDivName.append("unable to process request");
	}
	function displayData(data){
		$("#"+$targetDivName).empty().append(data);
		$ul.hide();
		//alert(data);
	}
	function inputBlurValidate(){
		$sString=$(this).val().toLowerCase();
		if($sString==""){
			$(this).val("Search")
		}
	}
	
	function inputFocusValidate(){
		$sString=$(this).val().toLowerCase();
		if($sString=="search"){
			$(this).val("");
		}
	}
	
	function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
	}
	
	function _validateUserNav() {
		var msg = "You have unsaved changes, proceed any way?";		
		if($userInterrupt.length > 0){
			// developer requested for interuption
			// the devloper is supposed to update this element based on the custom requirement as boolean value 
			var showConfirm  = $("#"+$userInterrupt).val();
			if(showConfirm == "true") {
				return confirm(msg);
			} else {
				return true;
			}
		} else {
			return true;
		}
	}

});

};
})(jQuery);// $ is the main jQuery object, we can attach a global function to it