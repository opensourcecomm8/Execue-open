<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<s:if test="reportListWrapper != null">
	<s:iterator value="reportListWrapper.chartFxReports" id="chart"
		status="charts_stat">
		<div id="<s:property value="#chart.id"/>" class="inactive"
			align="center" style="margin-top: 5px;"><s:property
			value="#chart.report" escape="html" /></div>
	</s:iterator>
</s:if>
<s:else>
	<p>No Data to display for charts</p>
</s:else>