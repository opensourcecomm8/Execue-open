<%@taglib prefix="s" uri="/struts-tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<s:if test="reportListWrapper != null">
	<!--
	<s:iterator value="reportListWrapper.htmlReports" id="grid"
		status="grid_stat">
		<s:if test="#grid.report!=null">
			<div id="<s:property value="#grid.id"/>" class="inactive"
				style="margin-top: 5px;"><s:property value="#grid.report"
				escape="html" /></div>
		</s:if>
	</s:iterator>
	-->

	<s:iterator value="reportListWrapper.htmlReports" id="grid"
		status="grid_stat">
		<s:if
			test="!#grid.id.contains('hierarchyReportGrid')">
			<s:if test="#grid.report!=null">
				<div id="<s:property value="#grid.id"/>" class="inactive"
					style="margin-top: 5px;"><s:property value="#grid.report"
					escape="html" /></div>
			</s:if>
		</s:if>
	</s:iterator>
	
	<s:iterator value="reportListWrapper.htmlReports" id="grid"
        status="grid_stat">
        <s:if
            test="#grid.id.contains('hierarchyReportGrid')">
            <s:if test="#grid.report!=null">
                <div id="<s:property value="#grid.id"/>" class="inactive"
                    style="margin-top: 5px;"><s:property value="#grid.report"
                    escape="html" /></div>
            </s:if>
        </s:if>
    </s:iterator>

</s:if>
<s:else>
	<p>No Data to display for Grids</p>
</s:else>