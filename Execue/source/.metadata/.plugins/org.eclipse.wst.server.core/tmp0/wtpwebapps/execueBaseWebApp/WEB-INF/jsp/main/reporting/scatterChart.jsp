<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<s:if test="reportWrapper != null">
	<s:iterator value="reportWrapper.chartFxReports" id="chartReport">
	 <s:property
			value="#chartReport.report" escape="html" />		
	</s:iterator>
	
</s:if>

