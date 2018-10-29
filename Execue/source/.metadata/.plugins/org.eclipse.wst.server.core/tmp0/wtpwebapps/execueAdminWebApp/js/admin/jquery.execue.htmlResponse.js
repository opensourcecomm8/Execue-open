(function($){
   
$.fn.getAjaxHtmlResponce = function(inputURL,linkDiv,loaderDiv,params,method,async){
 $inputURL=inputURL;
 $me = undefined;
 $linkDiv = linkDiv;
 $loaderDiv = loaderDiv;
 $params=params;
 $async=async;
 $method=method;
 $img="<div id='loaderDiv'><img src='../images/admin/loading.gif' width='20' height='20' /></div>";

 return this.each(function(){
   $me = $(this);
  if($async==undefined){$async=true; }
 
	if($linkDiv!=""){ 
	 showLoader();
	 setTimeout(hideLoader,9000);
	}
   
   $.ajax({url:$inputURL,
			    data:$params,
				success:Add,
			    type:$method,
				async: $async,
				error:error
			   });
   
		 function error(){
			$me.empty().append("Unable to process your request"); 
		 }
		function Add(mydata){
		$me.empty();
		 $me.append(mydata);
		 $me.fadeIn("fast");    
		 $me.show();
		 
		 if($linkDiv!=""){
     		hideLoader();
		 }
		 
		}
		
		function showLoader(){
		$("#"+$linkDiv).hide();
		if(!$loaderDiv){
		$("#"+$linkDiv).after($img);
		}else{
	    $("#"+$loaderDiv).show();
		}
	
	}
	
	function hideLoader(){
		$("#"+$linkDiv).show();
		if(!$loaderDiv){
	    $("#loaderDiv").remove();
		}else{
	    $("#"+$loaderDiv).hide();
		}
	
	}
		
});

};
})(jQuery);// $ is the main jQuery object, we can attach a global function to it

