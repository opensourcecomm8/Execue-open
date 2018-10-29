<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<style>
#tableGridMemberInfo td {
	padding-top: 0px;
}
</style>
<div id="errorMessage" style="color: red;padding-top:0px;"><s:actionerror /><s:fielderror /></div>
<div id="actionMessage" style="color: green;padding-top:0px;"><s:actionmessage /></div>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="25" class="fieldNames">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="75%"><s:text name="execue.concept.name" />
		: <s:label name="concept.name" id="name" /> <s:text name='execue.viewInstances.instances' /></td>
    <td width="25%" align="right"><!--div id="divSearchInstances" style="float:right;">
										<DIV id=roundedSearch2 style="display:none;width:190px;" >
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText2 type=search
											value=Search>
										<div class=searchEnd id=searchIcon><a href="#"><img
											src="../images/admin/searchEnd.gif" name="Image2a" border="0"
											id="Image2a"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_1,0,25,null,'Image2a')"
											onMouseOut="MM_startTimeout();" /></a></div>
										</DIV>

									
										</div--></td>
  </tr>
</table>
</td>
	</tr>
	<tr>
		<td>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				
				<div style="height: 321px; overflow: hidden;">
				<table width="100%" border="0" cellpadding="1" cellspacing="1"
					id="tableGridMemberInfo">
                    <tr id="tableGridTitle">
						<td width="38%" height="28" bgcolor="#EEEEEE"><a id="sortByNameLink"
							href="javascript:sortByName();"><strong><s:text name='execue.global.name' /></strong></a></td>
						<td width="41%" bgcolor="#EEEEEE"><a id="sortByDescLink"
							href="javascript:sortByDescr();"><strong><s:text name='execue.global.description' /></strong></a></td>
						<td width="11%" bgcolor="#EEEEEE"><strong><s:text name='execue.global.edit' /></strong></td>
						<td width="11%" bgcolor="#EEEEEE"><strong><s:text name='execue.global.delete' /></strong></td>
					</tr>

					<s:iterator value="instances" status="inst" id="instance">
						<tr>
							<td width="38%"><s:property value="%{#instance.displayName}" /></td>

							<td width="46%"><s:property value="%{#instance.description}" /></td>

							<td width="8%">
							<div id="showEdit1Link"><a
								href="javascript:getInstance(<s:property value="%{#instance.parentConcept.id}"/>,<s:property value="%{#instance.id}" />);"
								class="links" id="<s:property value="%{#instance.id}" />"><img src="../images/admin/editIcon.gif"
								alt="Edit Record" height="16" border="0" title="Edit Record" /></a></div>
							<div id="loadingShowEdit1Link" style="display: none;"><img
								src="../images/admin/loading.gif" width="20" height="20"></div>
							</td>
							<td width="8%">
							<div id='showDeleteLink<s:property value="%{#instance.id}" />'><a href="#"
								onclick="return deleteInstanceHeirarchy(<s:property value="%{#instance.parentConcept.id}"/>,<s:property value="%{#instance.id}" />);"
								class="links" id="<s:property value="%{#instance.id}" />"><img src="../images/admin/disabledIcon.gif"
								alt="Delete Record" height="16" border="0" title="Delete Record" /></a></div>
							<div id='loadingShowDeleteLink<s:property value="%{#instance.id}" />' style="display: none;"><img
								src="../images/admin/loading.gif" width="20" height="20"></div>
							</td>

						</tr>
					</s:iterator>
				</table>
				</div>
				</td>
			</tr>

			<tr>

				<td height="35" align="left">
				<table width="100%">
					<tr>

						<td width="25%">
						<div id="addNewInstanceLink"><a
							href="javascript:addInstance(<s:property value="concept.id"/>);"
							class="arrowBg" id="addNewInstance"><s:text
							name="execue.instance.addNewInstance" /></a></div>
						<div id="loadingAddNewInstanceLink" style="display: none;"><img
							src="images/admin/loading.gif" width="20" height="20"></div>
						</td>
					</tr>
				</table>

				</td>


			</tr>
		</table>
		<table align="center">
			<tr>
				<td>
				<s:set name="url" id="url" value="%{'getInstances.action?concept.id='+concept.id}"></s:set>
				<div id="paginationDiv2"><pg:page targetURL="${url}" targetPane="dynamicPane" /></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<s:hidden id="conceptId" name="concept.id" />
<s:hidden id="pageSizeInstances" name="pageDetail.pageSize" />
<s:hidden id="recordCountInstances" name="pageDetail.recordCount" />
<s:hidden id="pageCountInstances" name="pageDetail.pageCount" />
<s:hidden id="noOfLinksInstances" name="pageDetail.numberOfLinks" />
<s:hidden id="paginationTypeInstances" name="paginationType" />


<script>
$(function(){
showSearch2('divSearchInstances');
				   });
function showSearch2(divName){
	obj=" ";
  $("#"+divName+obj+"#searchTables2").hide();
  $("#"+divName+obj+"#roundedSearch2").fadeIn("slow");
  $("div#searchIcon1 img#Image2").bind("mouseover",function(){findString="span.tableHolder"; })
  $('#searchText2').focus(function(){
    if($(this).attr('value') == ''|| $(this).attr('value') == 'Search'){  //test to see if the search field value is empty or 'search' on focus, then remove our holding text
      $(this).attr('value', '');
    }
    $(this).css('color', '#000');
    
  });
  
  $('#searchText2').blur(function(){ //test to see if the value of our search field is empty, then fill with our holding text
    if($(this).attr('value') == ''){
      $(this).attr('value', 'Search');
      $(this).css('color', '#777');
    }
  });
}
</script>
