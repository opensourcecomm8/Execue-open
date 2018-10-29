<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<link rel="stylesheet" href="css/jquery.treeview.css" type="text/css"
	media="screen" />
<style>
#createNewFolder,#bookmarkID,#closeButtonId {
	cursor: pointer;
}

#bookMarkSearchResult a {
	color: #333;
	text-decoration: none;
}

#bookMarkSearchResult a:hover {
	color: #333;
}
.snapBit {

cursor:pointer;
width:auto;
float:left;
}

</style>
<script type="text/javascript" src="<c:out value="${basePath}"/>/js/jquery.treeview.js"></script>
<script type="text/javascript" src="<c:out value="${basePath}"/>/js/jquery.cookie.js"></script>
<script type="text/javascript" src="<c:out value="${basePath}"/>/js/bookmark.js"></script>
<%
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>

<div style="width: 320px; height: 250px;">

<table width="320" border="0" cellspacing="0" cellpadding="3">

	<!-- tr>
			<td width="19" class="fieldText"><s:text name="execue.bookmark.folder" /></td>
			<td width="187"><s:select name="folders" id="folders"
				list="folderList" listKey="id" listValue="name" value="108" /></td>
			</tr-->
	<tr>
		<td valign="top" align="left">
		<div id="searchTables" class="searchText"><s:text name="execue.bookmark.search" /></div>
		<input type="hidden" value="###searchBookMarksBox####" /></td>
		<td align="left">
		<DIV id=roundedSearch style="width: 200px;">
		<div class=searchStart></div>
		<INPUT class=searchField id=searchText type=searchvalue=Search>
		<div class=searchEnd id=searchIcon><a href="#"><img
			src="<c:out value="${basePath}"/>/images/searchEndIndex.gif" name="Image2" border="0" id="Image2"
			alt="Search Bookmark" title="Search Bookmark" /></a></div>
		</DIV>
		</td>
	</tr>
	<tr>
		<td colspan="3" id="bookMarkSearchResultRow" style="display: none;">
		<div id="bookMarkSearchResult" style="float: left"></div>
		<div id="bookMarkSearchResultLink"
			style='display: none; padding-top: 5px; float: right; padding-right: 25px;'>
		<img src="<c:out value="${basePath}"/>/images/showFoldersButton.jpg" value="Show Folders"
			style="cursor: pointer;" /> <img src="<c:out value="${basePath}"/>/images/cancelButton.jpg"
			name="closeButtonId" value="Cancel" id="closeButtonId"
			onclick="javascript:$('#hiddenPane').fadeOut('slow');" /></div>
		</td>
	</tr>
	<tr>
	</tr>
	<tr id="bookmarksList">

		<td colspan="3" align="left">
		<table border="0" cellpadding="0" cellspacing="0" align="left">
			<tr>
				<td align="left">
				<div id="fid">
				<div id="scrollable"
					style="width: 310px; height: 185px; border: # #CCC solid 1px; background-color: #FFF; float: left;">
				<ul id="browser" class="filetree">
					<s:iterator value="folderList">
						<li><span class="folder">
						<div >
                            
                            <span id="folder_<s:property value='id'/>" class="folderKlass"
							style="text-align: left;width:20px;white-space:nowrap;"><s:property value="name" /></span>
                            
                             <span> <a title="Delete Folder" alt="Delete Folder" style="cursor:pointer;"   onclick="return deleteSavedFolder('folder_<s:property value="id"/>');" href="#"><img src="<c:out value="${basePath}"/>/images/deleteFolder.gif" border="0" /></a> </span> 
                            
                            </div>

						</span>
						<ul>
							<s:iterator value="uiBookmarks" id="uiBookmarkIds">
								<li><span class="file"
									appId="<s:property value='applicationId'/>">
								<div class="dType" style="display: none"><s:property
									value="type.value" /></div>
								
                                <div >
                                    
                                    <span class="dVal" id="<s:property value='id'/>"
									style="cursor: pointer;width:10px;width:20px;white-space:nowrap;" title="Click" alt="Click"
									bookmarkValue='<s:property value="value"/>'
									onclick="getBookmarkVal(this.id,'<s:property value="type"/>');"
									class="bookmarkKlass" style="text-align:left;"><s:property
									value="name" /></span>
                                    
                                    <span> <a title="Delete Query" alt="Delete Query" style="cursor:pointer;"   onclick="return deleteSavedQuery('<s:property value="id"/>');" href="#"><img src="<c:out value="${basePath}"/>/images/deleteButt2.gif" border="0" /></a> </span>
                                    
                                    </div>
								</span></li>

							</s:iterator>
						</ul>
						</li>
					</s:iterator>
				</ul>
				</div>
				</div>
				</td>
			</tr>
			<tr id="closeSearchBookmarks">
				<td>
				<table width="10%" border="0" cellspacing="0" cellpadding="5"
					align="right">
					<tr>
						<td><img src="<c:out value="${basePath}"/>/images/closeNewButton.jpg" name="closeButtonId"
							value="Close" id="closeButtonId"
							onclick="javascript:$('#hiddenPane').fadeOut('slow');" /></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<script>


function deleteSavedQuery(id){ 
var st=confirm("Are you sure you want to delete saved query ?");
	if(st){ 
	url = "bookmark/deleteBookmarkFromSavedQueries.action?bookmarkId=" + id;
	
	$.get(url, function(data) {
						
			if(data.indexOf("Bookmark deleted successfully")>-1){ 
			$("span#"+id).parents("li:first").remove();	
			}
	});
	}
}

function deleteSavedFolder(id){ 
var foId = id.substring(7);
	var st=confirm("Are you sure you want to delete saved Folder and its contents ?");
	if(st){ 
	url = "bookmark/deleteFolderFromSavedQueries.action?folderId=" + foId;
	
	$.get(url, function(data) { 
			if(data.indexOf("Folder deleted successfully")>-1){ 
			$("span#"+id).parents("li:first").remove();	
			}
	});
	}
	
}
$("#searchText").bind('click', function(e) {
	$("#searchText").val('');
});
$("#searchText").bind('keypress', function(e) {
		if (e.keyCode == 13 || e.which == 13) {
			$("#searchIcon").trigger('click');
			return false;
		}
	});
$("#hiddenPaneContent").css("height","250px");
$("#bookMarkSearchResultLink").click(function(){
	$("#closeSearchBookmarks").show();										  
$("#fid").show();
$("#bookMarkSearchResultRow").hide();
 $("#bookMarkSearchResult").hide();
 $("#bookMarkSearchResultLink").hide();
});
 $("#searchIcon").click(function(){	 
	$("#closeSearchBookmarks").hide();
    $("#bookMarkSearchResult").empty();
	//$("#bookMarkSearchResult").append("<div style='width:280px;border-top:#ccc dashed 1px;height:10px;'></div>");
	var searchString =  $("#searchText").val();
	var bookmarkType=$("#bookmarkType").val();	
	var count=1;
  	 $.getJSON("bookmark/searchBookmarks.action?search="+searchString+"&bookmarkType="+bookmarkType,function (jsonData) {
  	 $("#fid").hide();
	  var type = typeof jsonData;
	  if(type=="string"){
	    $("#bookMarkSearchResult").append(jsonData);
	  }else{	      
		    $.each(jsonData, function(i,item) {	
			  var x=autoHighlight(item.name, searchString)
		      var bookmarkVal="<div><span>"+count+".</span><a id='"+item.id+"' href='#' bookmarkValue='"+item.value+"' onClick=javascript:getBookmarkVal(this.id,'"+bookmarkType+"') title='Click' alt='Click'>"+x+"</a></div>";		  		 	 
		 	  var summary="<div id='summary'>"+item.summary+"</div>";
		 	 if(item.summary != null){
		 	   bookmarkVal=bookmarkVal+summary;
		 	 }
			  $("#bookMarkSearchResult").append(bookmarkVal);
			     count=count+1;
		    });
	      }
	     $("#bookMarkSearchResult").show();
	     $("#bookMarkSearchResultLink").show();
		 $("#bookMarkSearchResultRow").show();
	});
  });
  function getBookmarkVal(bookmarkID,bType){
	var bookmarkType=$("#bookmarkType").val();	
	var bookmarkValue=$("#"+bookmarkID).attr("bookmarkValue");
	
	if(bookmarkType != bType){	 
	  return;
	}	
	if(bType=='<%=com.execue.core.common.type.BookmarkType.SEARCH_INTERFACE%>'){	 		
	    $("#metrics").empty();
	    $("#metrics").val(bookmarkValue);	 
	}else{	 
	  	$.getJSON("bookmark/retrieveBookmark.action",{bookmarkValue : bookmarkValue},function (data){	   
	   // Select Matric
	   $("#clearPage").trigger("click");
	   SelectMatric.clearAll();	   
	   if(data.selects){
		    $.each(data.selects, function(i,selects){		    
		      getSeletMetric(selects);		    			
		   });
	    }
	   // Condition Matric
	   ConditionMetric.clearAll();	   
	   if(data.conditions){
		    $.each(data.conditions,function(i,conditions){
			  getConditionMetric(conditions);
		    });
	   }
	 //populations
	 PopulationMetric.clearAll();	  
	  if(data.populations){
	  	$.each(data.populations,function(i,populations){
		  getPopulations(populations);
		 });
	  }
	   //summarizations
	    GroupByMetric.clearAll();	   
	   if(data.summarizations){
	   	  $.each(data.summarizations,function(i,summarizations){
		    getSummarizations(summarizations);
		   });
	   }
	  //TopBottom
	  //TopBottomMetric.clearAll();			
		if(data.topBottom){
			TopBottomMetric.add({name: data.topBottom.term.name, displayName : data.topBottom.term.name, type: data.topBottom.term.type});				
			TopBottomMetric.add({name: data.topBottom.type, displayName :data.topBottom.type,type: 'operator'});
			TopBottomMetric.add({name: data.topBottom.value, displayName :data.topBottom.value,type: 'VALUE'});
		    TopBottomMetric.addBitsToArray();
		}
	   //cohort conditions
	   CohortConditionMetric.clearAll();	 
	  if(data.cohort.conditions){
		    $.each(data.cohort.conditions,function(i,conditions){
		      getCohortConditionMetric(conditions);
	       });
	  }
	   //cohort summarizations
	   CohortGroupByMetric.clearAll();	   
	   if(data.cohort.summarizations){
		   $.each(data.cohort.summarizations,function(i,summarizations){
		     getCohortGroupByMetric(summarizations);
	      });
	   }
	 });
	}

	 setTimeout('$("#hiddenPane").hide()', 300)
   }
   
   
   function autoHighlight(html, highlight) {
				return html.replace(new RegExp(highlight, 'gi'),
						function(match) {
							return '<b>' + match + '</b>';
						});
			}
</script>

