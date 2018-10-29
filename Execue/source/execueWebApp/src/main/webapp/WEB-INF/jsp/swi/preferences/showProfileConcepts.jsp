<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<style>
a.links {
	padding-top: 0px;
	padding-bottom: 0px;
}
</style>
<div class="tableList"
	style="height: 215px; width: 190px;; margin: auto; margin-bottom: 5px; margin-left: 3px; overflow-y: hidden; padding-top: 3px;">
<table width="100%" border="0" cellspacing="0" cellpadding="1"
	id="searchList2">
	<s:iterator value="baseProfileConcepts" status="even_odd"
		id="baseProfile">
		<tr id="tableRow1">
			<td width="1%" class="dotBullet">&nbsp;</td>
			<td width="99%" class="fieldNames"><s:if
				test='#baseProfile.displayName == "Measure Profile"'>
				<div id="showProfile<s:property value="id" />Link"><a
					href="javascript:getNewProfileDefinition('<s:property value="id" />','CONCEPT','NO')"
					class="links" id="<s:property value="name"/>"><s:property
					value="displayName" /></a></div>
				<div id="loadingShowProfile<s:property value="id" />Link"
					style="display: none;"><img src="../images/loading.gif"
					width="20" height="20"></div>
			</s:if> <s:elseif test='#baseProfile.displayName == "Hybrid Profile"'>
				<div id="showProfileHybridLink"><a
					href="javascript:getNewProfileDefinition('<s:property value="id" />','CONCEPT','YES')"
					class="links" id="<s:property value="name"/>"><s:property
					value="displayName" /></a></div>
				<div id="loadingShowProfileHybridLink"
					style="display: none;"><img src="../images/loading.gif"
					width="20" height="20"></div>
			</s:elseif> <s:else>
				<div id="showProfile<s:property value="id" />Link"><a
					href="javascript:getNewProfileDefinition('<s:property value="id" />','CONCEPT_LOOKUP_INSTANCE','NO')"
					class="links" id="<s:property value="name"/>"><s:property
					value="displayName" /></a></div>
			</s:else>
			<div id="loadingShowProfile<s:property value="id" />Link"
				style="display: none;"><img src="../images/loading.gif"
				width="20" height="20"></div>
			</td>
		</tr>
	</s:iterator>
</table>
</div>

<div id="paginationDiv2" style="margin-top: 10px; margin-bottom: 5px;"><pg:paginateResults /></div>



