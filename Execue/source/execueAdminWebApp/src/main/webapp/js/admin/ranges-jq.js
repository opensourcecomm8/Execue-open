// JavaScript Document

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
function processAddImageClick(e){ 
										  
		var rowId=$(this).parents().parents("tr:first").attr("id");	
		var nextRowId=Number(rowId)+1;
		var $tr=$("<tr></tr>"); 
		var lowVal=$("tr#"+nextRowId).find("input").eq(0).val(); 
		var highVal=$("tr#"+nextRowId).find("input").eq(1).val(); 
		var finalHighVal=(Number(lowVal)+Number(highVal))/2; 
		finalHighVal=roundNumber(finalHighVal,2);//alert(finalHighVal);
		var $low=$("<input type='text' readonly='readonly' value='' />"); $low.val(lowVal);
		var $high=$("<input type='text' value='' />"); $high.val(finalHighVal);  
			$("tr#"+nextRowId).find("input").eq(0).val(finalHighVal); 
			$("tr#"+nextRowId).find("input").eq(2).val(finalHighVal+" to "+$("tr#"+nextRowId).find("input").eq(1).val());  	
		var $desc=$("<input type='text' rangeId=''  value='' />"); $desc.val(lowVal+" to "+finalHighVal);
		var $addImage=$("<img style='display:none;' class='addRow'  title='Add range below' alt='Add range below' src='../images/admin/plus.gif' />");
		$addImage.bind("click",processAddImageClick);
		var $delRowImage=$("<img style='display:none;'   title='Remove this range' alt='Remove this range' src='../images/admin/remove.jpg' class='delRow' />");
		
		var $td0=$("<td ></td>"); $tr.append($td0);  $td0.append($low);
		var $td1=$("<td></td>"); $tr.append($td1);  $td1.append($high);
		var $td2=$("<td></td>"); $tr.append($td2);  $td2.append($desc);
		var $td3=$("<td></td>"); $tr.append($td3);  $td3.append($addImage);
		var $td4=$("<td></td>"); $tr.append($td4);
		$td4.append($delRowImage);
		$tr.insertAfter($("tr#"+rowId));
		reNameTrIds();
		e.stopPropagation();
	 }
$(function(){
	$("#from").ForceNumericOnly();
	$("#to").ForceNumericOnly();
	
	$("img.addRow").bind("click",processAddImageClick);
	   
});
function roundNumber(num, dec) {
	var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
	return result;
}
function performDelete(){ 
			var $delTrId = 	$(this).parents().parents("tr:first").attr("id");	
			var delTrIdPre=Number($delTrId)-1;
			var delTrIdNext=Number($delTrId)+1;
			//alert("delTrIdNext::"+delTrIdNext);
			//alert("delTrIdPre::"+delTrIdPre);
			var val=$("tr#"+delTrIdPre).find("input").eq(1).val();
			$("tr#"+delTrIdNext).find("input").eq(0).val(val);
			$("tr#"+delTrIdNext).find("input").eq(2).val($("tr#"+delTrIdNext).find("input").eq(0).val()+" to "+$("tr#"+delTrIdNext).find("input").eq(1).val());
			$(this).parents().parents("tr:first").remove();
			
					var totalRows=$("#rangesTable tr").length;  
					$.each($("#rangesTable tr"),function(k1,v1){ 
						$(this).attr("id" , k1); 
						
						//var $del=$("tr#"+k).find("img.delRow");
						//var $add=$("tr#"+k).find("img.addRow");
						//if(k1!=0 || k1!=totalRows){ $del.show();  }
		
						//if(k1!=(totalRows-2)&& k1!=(totalRows-1)){  $add.show();  }else{ $add.hide();}
					});
					stopPropagation();
		 }
function reNameTrIds(){ 
	var totalRows=$("#rangesTable tr").length; 
	$.each($("#rangesTable tr"),function(k,v){
		$(this).attr("id" , k);
		var rId=$(this).parents().parents("tr:first").attr("id");
		
		var $del=$("tr#"+k).find("img.delRow");
		var $add=$("tr#"+k).find("img.addRow");
		$del.click(performDelete);
		if(k!=0 && k!=(totalRows-2)&& k!=(totalRows-1)){ $del.show();  }
		
		if(k!=(totalRows-2)&& k!=(totalRows-1)){  $add.show();  }else{ $add.hide();}
		
		$(this).find("input").eq(1).bind("blur",function(){// alert($(this).val());
			var rId=$(this).parents().parents("tr:first").attr("id"); //alert(rId);
			var nextRId=Number(rId)+1;
			//if( Number($(this).val()) < Number($("tr#"+nextRId).find("input").eq(1).val())){
			var lowerLimitInput_next=$("tr#"+nextRId).find("input").eq(0);
			var upperLimitInput_next=$("tr#"+nextRId).find("input").eq(1);
			var descInput_next=$("tr#"+nextRId).find("input").eq(2);
			
			var lowerLimitInput_current=$("tr#"+rId).find("input").eq(0);
			var upperLimitInput_current=$("tr#"+rId).find("input").eq(1);
			var descInput_current=$("tr#"+rId).find("input").eq(2);
			
			lowerLimitInput_next.val($(this).val()); 
			
			$("tr#"+rId).find("input").eq(2).val($("tr#"+rId).find("input").eq(0).val()+" to "+$(this).val());  	
			
			descInput_next.val($(this).val()+" to "+upperLimitInput_next.val()); 
			
			
			/*}else{
				
				$("#myAlert").empty().html("Range1 value should be less than next range value").show();
				$("#myAlert").css("top",($(this).position().top)+"px");
				$("#myAlert").css("left",($(this).position().left+65)+"px");
				$(this).value("").select().focus();
				setTimeout(function(){$(this).focus();},1);

			
			}*/
			
		});
		
		
	 });
}

function createRangeTable(){
	var from=$("#from").val();
	var to=$("#to").val();
	if (Number(from) >= Number(to)){
	$("#myAlert").empty().html("Min value should be less than Max value").show();
	$("#myAlert").css("top",($("#create").position().top)+"px");
	$("#myAlert").css("left",($("#create").position().left+65)+"px");
	setTimeout(function(){$("#myAlert").hide();},3000);			
	}else{
		
		$("tr#0").find("input").eq(0).val("low");  $("tr#0").find("input").eq(1).val(from); $("tr#0").find("input").eq(2).val("low to "+from);
		$("tr#1").find("input").eq(0).val(from);  $("tr#1").find("input").eq(1).val(to); $("tr#1").find("input").eq(2).val(from+" to "+to);
		$("tr#2").find("input").eq(0).val(to);  $("tr#2").find("input").eq(1).val("High"); $("tr#2").find("input").eq(2).val(to+" to High");

	
    $("#rangesTable").show();
	reNameTrIds()
	}
	
}