<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<s:if test="reportWrapper != null">
	<s:iterator value="reportWrapper.chartFxReports" id="chartThumbnails">
		<div id="scatterReportDivId" style="height: 60px; width: 80px; float: left; margin-left: 2px;"><s:property
			value="#chartThumbnails.thumbnail" escape="html" />			
			
		</div>	
		
	</s:iterator>
	
</s:if>
