(function($){
   
$.fn.createInput = function(inputURL,linkDiv,loaderDiv,params){
 $inputURL=inputURL;
 $me = undefined;
 $linkDiv = linkDiv;
 $loaderDiv = loaderDiv;
 $params=params;

 return this.each(function(){
   $me = $(this);
	if($linkDiv!=""){ 
	 $("#"+$linkDiv).hide();
	 $("#"+$loaderDiv).show();
	}
   $.post($inputURL, $params, function(data) { 
     Add(data);
	 
	 
    });
   
		function Add(mydata){

		$me.empty();
		 $me.append(mydata);
		 $me.fadeIn("fast");    
		 $me.show();
		/* if($("#errorMessage")){
		 var message=$("#errorMessage").html().toString();	
				var x=message.replace(/<BR>/gi,",");
				$("#errorMessage").html(x);
		 }*/
		 if($linkDiv!=""){
		$("#"+$linkDiv).show();
		 $("#"+$loaderDiv).hide();
		 }
		}
		
});

};
})(jQuery);// $ is the main jQuery object, we can attach a global function to it

