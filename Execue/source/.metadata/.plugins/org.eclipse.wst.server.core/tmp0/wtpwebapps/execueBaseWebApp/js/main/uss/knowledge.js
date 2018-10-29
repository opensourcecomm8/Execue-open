var inst="";
var autoSuggestClicked=false;
var $left_v=0;
var $top_v=0;
var fromunrecog=false;
var uApp=null;
$(document).ready(function() { 
   function getSelected() {
	  if(window.getSelection) { return window.getSelection(); }
	  else if(
	      document.getSelection) { return document.getSelection(); 
	   } else {
	    var selection = document.selection && document.selection.createRange();
	    if(selection.text) { 
	       return selection.text; 
	    }
	    return false;
	  }
	  return false;
}
  $('.fullunrecog').bind("mouseup mouseout",function() {  
				uApp=$(this).next(".uAppVals"); 
				if(page=="results"){
				uApp=$(this).next(".uAppVals"); 
				
				}
				 fromunrecog=true;
				oldQuery=$("#metrics").val();		
   				 var selection = getSelected();
    				if(selection && (selection = new String(selection).replace(/^\s+|\s+$/g,''))) {
					$left_v=$(this).position().left;
					$top_v=$(this).position().top-50;
					$("#showAdd").css("left",$left_v+"px");
					$("#showAdd").css("top",$top_v+"px");
					$("#showAdd").show();
					var instance=$(this).text();
					inst=instance;
    			}
  			}); 			
			$(document).mousedown(function(){							   
						$("#showAdd").fadeOut("slow");				  
			});
			
			
			 $("#showAdd,#teachSystemLink").click(function(){
			 	var applicationName=$("#applicationName").val();	
			   var applicationId=$("#applicationId").val();
			   if(page=="results"){
				 applicationName=uApp.attr("uAppName"); 
				applicationId=uApp.attr("uAppId"); 
			   }
				var modelId=$("#modelId").val();
			 	if(!modelId){
			 		modelId=-1;			 		
			 	}
				 Pane_Left=$(this).position().left;
					 Pane_Top=$(this).position().top;
					if($(this).attr("id")=="teachSystemLink"){Pane_Left=Pane_Left-100;} 
					 $left_v=Pane_Left;
					$top_v=Pane_Top;
					$("#showKnowledgePopup").css("left",Pane_Left-120+"px"); 
					  $("#showKnowledgePopup").css("top",Pane_Top+70+"px"); 
					$("#showKnowledgePopup").empty().css("background","#ffffff url(images/main/loaderTrans.gif) no-repeat center center").show();
					
					//alert(applicationName+":::"+applicationId+":::"+modelId);
			 	  $.post("showDynamicKnowledgeInput.action",{applicationName:applicationName,applicationId:applicationId,modelId:modelId},function(data){
																																				   
			 	    setTimeout(function(){$("#showKnowledgePopup").empty().html(data).show();},1000);
			 	    
			 	    
			 	    
					  
					  
					  $("#concept").val("");
					  $("#showKnowledgePopup").show();
					 
					  
			 	  });
					
				});
			 
			 $("#closeKpopup").live("click",function(){
					fromunrecog=false;						  
					$("#concept").removeAttr("autocomplete");
					$("div.ac_results").remove();
					$("#concept").val("");
					 $("#showInstancesDiv").empty().slideUp();
						$("#showKnowledgePopup").fadeOut();			  
				});
});

