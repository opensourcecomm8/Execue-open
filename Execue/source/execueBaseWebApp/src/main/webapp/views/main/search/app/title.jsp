<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
<title><s:if test='verticalId==1001'>Semantifi : Finance</s:if><s:if
	test='verticalId==1003'>Semantifi : Other Featured Apps</s:if><s:if
	test='verticalId==1002'>Semantifi : Government</s:if><s:else><c:choose><c:when test="${empty uiApplicationInfo.applicationTitle}"><s:property value="uiApplicationInfo.applicationName" /> Search App</c:when><c:otherwise><s:property value="uiApplicationInfo.applicationTitle" /></c:otherwise></c:choose></s:else></title>