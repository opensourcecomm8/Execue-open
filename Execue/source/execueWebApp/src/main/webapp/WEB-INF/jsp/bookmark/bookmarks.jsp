<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<!--script language="JavaScript" src="../js/jquery.js"></script-->
<link rel="stylesheet" href="<c:out value="${basePath}"/>/css/jquery.treeview.css" type="text/css"
	media="screen" />
    
    <style>
	#createNewFolder,#bookmarkID,#closeButtonId{
	cursor:pointer;	
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
   int ct=1;
%>

<div style="width: 200px; height: 60px;"><s:form id="form1"
 method="post">
	<table width="100%" border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td colspan="3" align="left"><strong><s:text
				name="execue.bookmark.pageHeading" /></strong></td>
		</tr>
		<!--tr>
			<td colspan="3"><span id="errorMessage" style="color: red"><s:fielderror /><s:actionerror /></span>
			<span id="actionMessage" style="color: red"><s:actionmessage /></span>
			<div style="display: none" id="fNameRequired"><s:text
				name="execue.bookmark.foldername.required" /></div>
			<div style="display: none" id="bNameRequired"><s:text
				name="execue.bookmark.name.required" /></div-->
			<!-- span id="message" style="color: red"></span--><!--/td>
		</tr-->
		<tr>
			<td width="19" class="fieldNames" align="left"><s:text name="execue.global.name" /> :</td>
			<td colspan="2"><s:textfield name="bookmark.name"
				cssClass="textBox" id="bookmarkName" tabindex="0" /></td>
		</tr>
		<tr>
			<td width="19" class="fieldNames" align="left"><s:text name="execue.bookmark.folder" /> :</td>
			<td width="30"><s:select name="folders" id="folders"
				list="folderList" listKey="id" listValue="name"/></td>
			<td width="197"><input type=button value=""
				onclick="showFolder();" class="openBookmarksButton"
				id="openBookmarksB">
			<div id="tooltip"><s:text name="execue.ShowBookmarkFolders.heading" /></div>
			</td>
		</tr>
		<tr>
		</tr>
		<tr id="bookmarksList" style="display: none;"DispList">
			<td>&nbsp;</td>
			<td colspan="2">
			<table border="0" cellspacing="0" cellpadding="0" >
				<tr>
					<td>
					<div id="fid">
					<div id="scrollable">
					<ul id="browser" class="filetree">
						<s:iterator value="folderList">
							<input type="hidden" id="lastFid<%=ct%>" name="fname" value="<s:property value="id"/>"/>
							<li><span class="folder">
							<div id="folder_<s:property value='id'/>"
								onclick="setColorForFolder(this.id);" class="folderKlass"><s:property
								value="name" /></div>							
							</span>
							<ul>
							 
								<s:iterator value="uiBookmarks">
									<li>									
									<span class="file">
									    <div id="bookmark_<s:property value='id'/>"
										style="cursor: pointer;"
										onclick="setColorForBookmark(this.id);"  class="bookmarkKlass">
										<s:property value="name" /></div>
									</span></li>
								</s:iterator>
								
							</ul>
							</li>
							<%ct++;%>														
						</s:iterator>
					</ul>
					</div>
					</div>
					</td>
				</tr>
				<tr>
					<td colspan="2">
					<table width="100%">
						<tr>
							<td width="50%">
							<s:a href="#" id="createNewFolder">
								<!--s:text name="bookmark.link.folder" /-->
                                <img src="<c:out value="${basePath}"/>/images/newFolderButton.jpg" border="0"   />
							</s:a>
							<div style="display: none" id="saveFolderDiv"><s:text
								name="execue.bookmark.link.savefolder" /></div>
							<div style="display: none" id="createFolderDiv"><s:text
								name="execue.bookmark.link.folder" /></div>
							</td>
							<!-- td width="50%">
							<p><s:a href="#" id="deleteBookmarkId">
								<s:text name="execue.global.delete" />
							</s:a></p>
							</td-->
						<tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
			<td colspan="2" class="fieldText"><table width="10%" border="0" cellspacing="0" cellpadding="0" align="right">
  <tr>
    <td><img  src="<c:out value="${basePath}"/>/images/doneButton.jpg" name="createBookmark" id="bookmarkID" style="width:57px;margin-right:5px" /></td>
    <td><img src="<c:out value="${basePath}"/>/images/closeNewButton.jpg"  name="closeButtonId"  value="Cancel" id="closeButtonId" onclick="javascript:$('#hiddenPane').fadeOut('slow');" /></td>
  </tr>
</table>
</td>
		</tr>
		
	</table>
	<input type="hidden" id="count" value="<%=ct%>"/>
</s:form></div>
<script type="text/javascript">
var data='<c:out value="${folderList}"/>';
if(data.length==2){
	$("#openBookmarksB").removeClass("openBookmarksButton");
	$("#openBookmarksB").addClass("closeBookmarksButton");
	$("#tooltip").text("Hide Bookmark Folders");
	$("#deleteBookmarkId").show()
	$("#bookmarksList").show();
/*	$("#hiddenPaneContent").animate({
		height : "290px",
		width : "200px"
	});*/
$("#hiddenPaneContent").height(290);
	bmflag=true;
	if($("li span.actionMessage").html()==null){
	  createFolder();
	  $("#txtId").select().focus(); 

	}
}	
$("form#form1 input#bookmarkName").focus();
$(document).ready(function(){
$("form#form1 input#bookmarkName").focus();
});
$("#bookmarksList").hide();
showFolder();
$("#openBookmarksB").removeClass("closeBookmarksButton");
		$("#openBookmarksB").addClass("openBookmarksButton");
		$("#tooltip").text("Show Bookmark Folders");
		// $("#DispList").hide();
		$("#samplebutton").hide()
		$("#deleteBookmarkId").hide();
		$("#bookmarksList").hide();
		/*$("#hiddenPaneContent").animate({
			height : "100px",
			width : "300px"
		});*/
		$("#hiddenPaneContent").height(100);
		bmflag = false;
</script>

