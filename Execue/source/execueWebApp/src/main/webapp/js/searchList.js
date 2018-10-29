// JavaScript Document

var searchBox="";
var space=" ";
var findString="a";
var searchIterateThrough="table#searchList";
var tr="tr";
function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
			}
			
function showAll(searchBox){
	
	if(searchBox==1){searchBox="";}
	if(tr==""){searchBox="";}
$.each($(searchIterateThrough+searchBox+space+tr),function(i,k){
	$temp=$(this).find(findString);									   
	$(this).show();			
	$temp.html($temp.text());
});

}
function startsWithString(searchBox){
	if(searchBox==1){searchBox="";}
	showAll(searchBox);
	searchString=$("#searchText"+searchBox).val().toLowerCase();
	if(tr==""){searchBox="";}
$.each($(searchIterateThrough+searchBox+space+tr),function(i,k){
	$temp=$(this).find(findString);
	//alert($temp);
	if($temp.text().toLowerCase().indexOf(searchString)!=0){$(this).hide();}
	$temp.html(autoHighlight($temp.text(), searchString));
});
}

function containsString(searchBox){
	if(searchBox==1){searchBox="";}
	showAll(searchBox);
	searchString=$("#searchText"+searchBox).val().toLowerCase();
	 if(tr==""){searchBox="";}
$.each($(searchIterateThrough+searchBox+space+tr),function(i,k){
	$temp=$(this).find(findString);
	if($temp.text().toLowerCase().indexOf(searchString)==-1){$(this).hide();}
	$temp.html(autoHighlight($temp.text(), searchString));
});
}

function initSearchList(){
	autoHighlight();
	showAll();
	startsWithString();
	containsString();
}
