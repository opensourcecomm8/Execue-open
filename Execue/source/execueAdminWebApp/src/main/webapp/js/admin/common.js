jQuery.extend(
        jQuery.expr[':'], { 
                Contains : "jQuery(a).text().toLowerCase().indexOf(m[3].toLowerCase())>=0" 
});


function filterListBySearch(searchEleName, parentClass, childClass ) {
 //add index column with all content.
 $("#"+searchEleName).keyup(function(){
   	//var s = $(this).val().toLowerCase().split(" ");
   	var s = $(this).val();
   	//show all rows.
   	$("."+parentClass+":hidden").show();  
	$("."+parentClass+" ."+childClass+":not(:Contains('" + s + "'))").parent().hide();
   	$.each($("."+parentClass+" ."+childClass+":Contains('" + s + "')"),function (i , el) {
   			$(el).html(highlightText($(el).html(), s));
   	});
 });//key up.
}
function highlightText(text, search) {
	text= text.replace(/<span class="highlight">/ig,'');
	text = text.replace(/<\/span>/ig,'');
	var re = new RegExp('('+search+')','gi');
	text = text.replace(re,'<span class="highlight">$1</span>');
	return text;
}