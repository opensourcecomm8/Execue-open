<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="pg" uri="/WEB-INF/tlds/pagination.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div id="div${verticalSelectName}Search"></div>
<select id="apps${verticalSelectName}"
	style="height: 200px; width: 250px; margin-top: 10px;" multiple="multiple">
	<s:iterator value="existingApplications">
		<option value='<s:property value="applicationId"/>'><s:property
			value="applicationName" /></option>
	</s:iterator>
</select>
<c:set var="url"
	value="getVerticalApps.action?selectedVerticalName=${selectedVerticalName}&verticalSelectName=${verticalSelectName}" />
<c:set var="divName"
	value="div${verticalSelectName}Apps" />
<div id="pagination${verticalSelectName}AppsDiv"
	style="margin-top: 10px; margin-bottom: 5px;"><pg:page
	targetURL="${url}" targetPane="${divName}" /></div>
<input type="hidden" id="userInterruptInput<s:property value='verticalSelectName' />" value="false"/>
<script type="text/javascript">
$("#div<s:property value='verticalSelectName' />Search").searchRecordsComponent({
	actionName:"getVerticalApps.action?selectedVerticalName=<s:property value='selectedVerticalName' />&verticalSelectName=<s:property value='verticalSelectName' />",
	targetDivName:"div<s:property value='verticalSelectName' />Apps",
	leftSize: 175,
	userInterrupt: "userInterruptInput<s:property value='verticalSelectName' />"});
// reinitializing the elements on this page load
// by this it's assumed that all the changes have been saved or ignored by the user on navigation.
movedIds = new Array;
isCopied = false;
</script>