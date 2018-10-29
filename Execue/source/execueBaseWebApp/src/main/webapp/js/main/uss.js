

var locationSuggestTerm="";
var userQueryFeatureCount=""
var userQueryRecordCount="";

function closeImage(){
$("#showBigImage").hide();	
$("#bigImg").hide();
}




function substringShortDesc(){
$.each($("a.reportHeaderLink,a.titleLinkGrid"),function(){
		var $text=$.trim($(this).text());
		$(this).attr("title",$text);
		$(this).attr("alt",$text);
		$(this).text($text.substring(0,14));
		$(this).append("...");
		
		});	
}
function substringShortDescForList(){
$.each($("a.headerLink,a.titleLink"),function(){
		var $text=$.trim($(this).text());
		//$text=removeSpecialChar($text);
		$(this).attr("title",$text);
		var $lth=$text.length;
		$(this).attr("alt",$text);
		//$(this).text($text.substring(0,90));
		//if($lth>90){
		//$(this).append("...");
		//}
		});	
}	



$("#button2").click(function(){
			$("form#publisher input[type='text']").val("");			
			$("form#publisher textarea").val("");
			$("form#publisher #textfield").focus();
							 });

$("#makesSeeMoreLink,#featuresSeeMoreLink,#citiesSeeMoreLink,#modelsSeeMoreLink").live("click",function(){ 
				$name=	$(this).attr("name");																		 
				if($(this).text()=="More..."){
				$("."+$name+"MoreTr").show();
				$(this).text("Hide...")
				}
				else{
				$("."+$name+"MoreTr").hide();
				$(this).text("More...")
				}
			});


  function showLoginInfo(name) {
		name = name.replace(/^\s+|\s+$/g,"");
		if(name && name.length > 0) {
			$("#showWelcome").empty().append(welcomemessage + name);
			$("#loginId").hide();
			$("#logoutId").show();
			$("#showBookmarksSearchLink").show();
			$("#moredata").hide();
		}	
	}

		function showPublisherInfo(admin,publisher) {
			if(admin=="true" ||publisher=="true"){
			$("#publisherTdSeperator").show();
			$("#publisherTd").show();
			$("#adminId").attr("href",adminPath+"swi/showSearchAppsDashboard.action");
			}
		}
		
		
		function substringShortDesc(){
$.each($("a.reportHeaderLink"),function(){
		var $text=$.trim($(this).text());
		$(this).attr("title",$text);
		$(this).attr("alt",$text);
		$(this).text($text.substring(0,14));
		$(this).append("...");
		
		});	
}




function relatedSearch(query) {
	oldQuery=$("#metrics").val();
	$("#metrics").val(query);
	$("#searchBtnFreeForm").click();
	return ;
}

function toggleFeatures(id){ 
		var $id=id;  
		$td=$("#feature"+$id).parents("td:first");
		
		if($td.next("td").children("div").css("display")=="none"){
		
		$td.next("td").children("div").slideDown();
		//$("#featuredRangeDiv"+$id+" input[type='checkbox']").attr("checked",true);
		}else{
		$td.next("td").children("div").slideUp();	
		}
	  }
 function processFeatureCheckBoxClicked(){  
		  var $id=$(this).val();
		  if($(this).is(':checked')){													 
					
					//$td=$(this).parents("td:first");
					//$td.next("td").children("table").slideDown();
					$td=$("#feature"+$id).parents("td:first");
		
					if($td.next("td").children("div").css("display")=="none"){
					toggleFeatures($id);
					}
					$("#featuredRangeDiv"+$id+" input[type='checkbox']").attr("checked",true);
					
					//}
				}else{
					//$td=$(this).parents("td:first");
					//$td.next("td").children("table").slideUp();
					$("#featuredRangeDiv"+$id+" input[type='checkbox']").attr("checked",false);
				}
	  }
	  
	  
	  function processFeatureValuesRangesClicked(){
		$tableId=$(this).parents("table:first").attr("id"); 
		$featureId=$(this).parents("table:first").parents("td:first").find("a:first").attr("name");
		//alert($tableId+"::"+$featureId);
		checkFeatureClicked($tableId,$featureId);
	  }
	  function checkFeatureClicked($tableId,$featureId){
		  if($("#"+$tableId+" input[type='checkbox']:checked").length>0){
		$("#"+$featureId).attr("checked",true);
		}else{
		$("#"+$featureId).attr("checked",false);	
		}
	  }
	  
	  function processFeatureValuesRangesLinkClicked(name){
		 var $name=name;//$(this).attr("name"); 
		 if($("#"+$name).attr("checked")){
		 $("#"+$name).attr("checked",false);
		 }else{
			$("#"+$name).attr("checked",true); 
		 }
		 
		 $tableId=$("#"+$name).parents("table:first").attr("id"); 
		$featureId=$("#"+$name).parents("table:first").parents("td:first").find("a:first").attr("name");
		//alert($tableId+"::"+$featureId);
		checkFeatureClicked($tableId,$featureId);
		
	  }
	  
	  function setCheckStatus(){
		changeCheckedStatus("city",$cities,"City");
									changeCheckedStatus("make",$makes,"Make");
									changeCheckedStatus("model",$models,"Model");
									changeCheckedStatus("feature",$features,"Feature");
									changeCheckedStatus("featureRange",$featureRanges,"Feature Range");
									changeCheckedStatus("featureValue",$featureValues,"Feature Value");
									var selectText=$("#selectedOptions").html();
									var commaIndex=selectText.lastIndexOf(",");
									$("#selectedOptions").html(selectText.substring(0,commaIndex-1));
									var $temp=$("#selectedOptions").text();
									$temp=$temp.replace(/,  \)/gi,")");
									$("#selectedOptions").empty().text($temp);
									if($("#selectedOptions").text()=="" ){
										$("#selectedOptions").text("None");
									}  
	  }
	
	  function changeCheckedStatus(name,val,actualName){

		  if(val.length>0){
		  	$("#selectedOptions").append("<b>"+actualName+"</b> > ");
		  }
		  if(name == "make"){
		  	$("table#advancedOption #makes option").each(function(k,v){
		  		$me = $(this);
		  		for(i=0; i < val.length; i++){
		  			var $c = val[i];
		  			if($c == $me.val()){
		  				$me.attr("selected",true);
		  				$textName = $me.text().split("(");
		  				$("#selectedOptions").append($textName[0]);
		  			}
		  		}
		  	});
		  }
		  if(name!="featureRange" && name!="featureValue"){
			for(i=0;i< val.length;i++){
				var $c=name+val[i]; 
				$("table#advancedOption input[id='"+$c+"']").attr("checked",true) ;
				var tr=$("table#advancedOption input[id='"+$c+"']").parents("tr:first");
				var $table=$("table#advancedOption input[id='"+$c+"']").parents('table:first');
				tr.removeAttr("class"); 
				tr.remove();
				$table.prepend(tr);
				tr.show();
				//var chbox=tr.children().find('input');
				//chbox.click(function(){processClickAction();});
				//alert("name::"+name);
				if(name!="feature"){
				$("#selectedOptions").append($("table#advancedOption input[id='"+$c+"']").attr("nameVal"));
				
				}else{
					//var $span=$("<span ></span>");
					//$span.attr("name",$c);
					$("#selectedOptions").append($("table#advancedOption input[id='"+$c+"']").attr("nameVal")+" ( <span name='"+$c+"' ></span> )");
					//$("#selectedOptions").append($span);
				}
				$("#selectedOptions").append(" , ");
		  	}
	  	} else { 
	  		for(i=0;i<val.length;i++){
	  			var featureNextNumber=0;
				var featureNext=0;
				var featureNextParentId=0;
				if((i+1)<val.length){featureNextNumber=i+1;  featureNext=val[featureNextNumber].split(":"); featureNextParentId= featureNext[0];}
				var feature = val[i].split(":"); //alert(feature);
	  			var featureParentId = feature[0];
	  			var featureSelectedId = feature[1];
	  			var $selectedItem = $("table#advancedOption table#featuredRangeDiv"+featureParentId+" input[id="+name+featureSelectedId+"]");
	  			$selectedItem.attr("checked",true);
	  			var tr= $selectedItem.parents("tr:first");
				var $table= $selectedItem.parents('table:first');
				tr.removeAttr("class"); 
				tr.remove();
				$table.prepend(tr);
				tr.show();
				var $selectedOptionsSpan="feature"+featureParentId; 
	  			$("span[name='"+$selectedOptionsSpan+"']").append($selectedItem.attr("nameval"));
				
				if(featureParentId==featureNextParentId){
	  			$("span[name='"+$selectedOptionsSpan+"']").append(" , ");
				}
	  		}
		}
	  }
	  
	  
	  function displayDataInDiv(data){
		  $("#searchBoxResultsDynamicDiv").empty().html(data);
		  $("#searchBoxResultsDiv").show();
	  }
     
		jQuery.fn.ForceNumericOnly = function()
		{
			return this.each(function()
			{
				$(this).keydown(function(e)
				{
					var key = e.charCode || e.keyCode || 0;
					// allow backspace, tab, delete, arrows, numbers and keypad numbers ONLY
					return (
						key == 8 || 
						key == 9 ||
						key == 46 ||
						(key >= 37 && key <= 40) ||
						(key >= 48 && key <= 57) ||
						(key >= 96 && key <= 105));
				})
			})
		};


function processClickAction(from){  
		fromSearchBar=false;
		var  $reqData="";	
		   locationSuggestTerm = returnLocationSuggestTerm();
			if(from=="fromFacets"){
				 var locSuggest=$.trim($("#locationId").val());
					  if(locSuggest!=""){							
							$reqData=facetsString+"&"+locationSuggestTerm;
					  }else{
							$reqData=facetsString;
					  }
			}else{				
				$reqData=locationSuggestTerm;				
			}		
			
			var selectedDefaultVicinityDistanceLimit=$("#selectedDVDLimitFaceted").val();
			  selectedDVDL="&selectedDefaultVicinityDistanceLimit="+selectedDefaultVicinityDistanceLimit;		
			
			var hasImg = hasImageCheck();
			hasImg="&imagePresent="+hasImg;
			$("#dynamicPage").empty();
			$("#dynamicPane").css("background","url(images/main/uss/Loader.gif) center center no-repeat");		
			$reqData=$reqData+"&applicationId="+$("#applicationId").val();
			var resultViewType = $("#resultViewTypeId").val();
			resultViewType="&resultViewType="+resultViewType;
			$("#dynamicPane").empty();
			//$("#dynamicPaneLoader").show();
			$("#dynamicPane").removeClass("dynamicPaneBgNoLoader").addClass("dynamicPaneBgLoader").show(); 
			var url="";
			 $reqData=$reqData+selectedDVDL+hasImg+selectedUdxCarsInfoSortType+resultViewType+"&modelId="+modelId;	
			if(from=="fromFacets"){
			 url="unstructuredSearchByFacet.action?"+$reqData;
			}else{
				//alert(userQueryFeatureCount+" vbvcbcvb"+userQueryRecordCount);
			     $reqData=$reqData+"&userQueryId="+userQueryId+"&userQueryFeatureCount="+ userQueryFeatureCount+"&userQueryRecordCount="+userQueryRecordCount;
				 url="updateUnstructuredSearchOptions.action?"+$reqData;
			}
			//alert(from+" :::  "+$reqData);
			$("#dynamicPane").removeClass("dynamicPaneBgNoLoader").addClass("dynamicPaneBgLoader"); 
			$.post(url,function(data){
						$("#dynamicPane").empty().html(data);
					   $("#dynamicPane").css("backgroundImage","none");
					   $("#dynamicPane").removeClass("dynamicPaneBgLoader").addClass("dynamicPaneBgNoLoader"); 
					   $("#dynamicPane").show();
					   $("#dynamicPaneLoader").hide();	
						getResults();	
			 		});  
			
	  }
	  
function showMore(divName, id){
	$("#"+divName).slideDown(); 
	$("#"+id+" img").attr("src","images/main/less-2.png");
	$("#"+id).attr("href","javascript:hideMoreLink('"+divName+"','"+id+"');");
}
function hideMoreLink(divName, id){
	$("#"+divName).slideUp(); 
	$("#"+id+" img").attr("src","images/main/more-2.png");
	$("#"+id).attr("href","javascript:showMore('"+divName+"','"+id+"');");
	
}
function addNoImage(aId){
$.each($("img.listimage"),function(k,v){
		 if($(this).attr("src")==""){ 
			$(this).attr("src","images/main/noImage-"+aId+".png");
			$(this).css({"width":"90px","height":"60px","cursor":"default"});
		 }
		  
		});	

}
function addNoImageGrid(aId){ 
$.each($("img.gridimage"),function(k,v){
		 if($(this).attr("src")=="images/main/noImage-"+aId+".png" || $(this).attr("src")==""){ 
			$(this).remove();
			//$(this).css({"width":"60px","height":"40px","cursor":"default"});
		 }
		});	
}
function enableImageClick(){
$("img.listimage").click(function(){
			var aId=$("#applicationId").val();
			if($(this).attr("src")!="images/main/noImage-"+aId+".png"){ 
			$img=$(this);
			//$img.removeAttr("width").removeAttr("height").css("cursor","default");
			$("#bigImg").empty().append($img.clone().removeAttr("width").removeAttr("height").css("cursor","default")).show();
			$("#showBigImage").show();
			
			var $left=$("#imageViewU").position().left;
			var $top=$("#imageViewU").position().top;
			$("#showBigImage").css("left",$left+"px");
			$("#showBigImage").css("top",$top+"px");
			

		
					$("#bigImg").fadeIn();
					var w=$("#bigImg img").width();
					if(w>736){$("#bigImg img").width(736)}

			}
			
		});
			
}
function processFacets(request){
	
	//alert("processFacets::"+request);
	$("#facetsPane").removeClass("dynamicPaneBgNoLoader").addClass("dynamicPaneBgLoader"); 
	$.get("getExecueFacets.action?"+request,function(data){ 
 $("#facetsPane").removeClass("dynamicPaneBgLoader").addClass("dynamicPaneBgNoLoader"); //alert(data);
									  $("#facetsPane").empty().html(data);
								
									  //$("#dynamicPane").css("backgroundImage","none");
									/*  $("#selectedOptions").empty();
									  $cities.length=0;
									  $models.length=0;
									  $features.length=0;
									  $makes.length=0;
									  $featureValues.length=0;
									  //$featureRanges.length=0;
									  $.each($("input[name='cities']:checked"),function(k,v){
										$cities[$cities.length]=$(this).val();										
										 });
									  if($("select[name='makes']").val()!='0'){
										$makes[$makes.length]=$("select[name='makes']").val();										
									  }
									  $.each($("input[name='models']:checked"),function(k,v){
										$models[$models.length]=$(this).val();										
										 });
									   $.each($("input[name='features']:checked"),function(k,v){
										$features[$features.length]=$(this).val();										
										 });
									   $.each($("input[name='featureValues']:checked"),function(k,v){
									   $featureValues[$featureValues.length]=$(this).attr("featureid")+":"+$(this).val();
									   });
									  /* $.each($("input[name='featureRanges']:checked"),function(k,v){
									   $featureRanges[$featureRanges.length]=$(this).attr("featureid")+":"+$(this).val();
									   });*/
									//setCheckStatus(); 
									//if(makesSeeMoreLink=="Hide..."){$(".makesMoreTr").show();}
									
									 
			 		});  
	
}

function returnLocationSuggestTerm(){
	
	return "locationSuggestTerm.locationBedId="+$("#locationBedId").val()+"&locationSuggestTerm.latitude="+$("#latitude").val()+"&locationSuggestTerm.longitude="+$("#longitude").val()+"&locationSuggestTerm.id="+$("#locationId").val()+"&locationSuggestTerm.displayName="+escape($("#location").val());
}
function hasImageCheck(){ 
var hasImage=$("#imagePresent").is(":checked");
	if(hasImage){
	return "true";
	}else{
	return "false";	
	}	
}