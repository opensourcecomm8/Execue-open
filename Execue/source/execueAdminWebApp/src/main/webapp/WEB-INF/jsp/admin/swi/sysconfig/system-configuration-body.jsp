<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="s" uri="/struts-tags"%>

<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground">System Configuration Details</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
			<div id="greyBorder" style="min-height: 420px; height: auto;">
				<div id="container" style="width: 950px; min-height: 430px; height: auto; margin: auto;">
					<!-- manually attach allowOverflow method to pane -->
					<!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true --> 
					<div id="configDynamicPane" style="height: auto;">
						<table width="100%" border="0" cellspacing="0" align="left" cellpadding="0">
							<tr>
								<td height="20" class="fieldNames">Configuration details of the system</td>
							</tr>
							<tr>
								<td>
									<div id="TabbedPanelsId" class="TabbedPanels" style="margin-bottom: 10px;">

										<ul class="TabbedPanelsTabGroup">
											<li class="TabbedPanelsTab TabbedPanelsTabSelected" tabindex="0">Common</li>
											<li class="TabbedPanelsTab" tabindex="1">Answers Catalog</li>
											<li class="TabbedPanelsTab" tabindex="2">Aggregation</li>
											<li class="TabbedPanelsTab" tabindex="3">Report Presentation</li>
										</ul>

										<div class="TabbedPanelsContentGroup">
										
											<div class="TabbedPanelsContent TabbedPanelsContentVisible" style="min-height:375px; height:auto;">
												<div id="commonConfig">
													<s:include value="system-configuration-common.jsp"/>
												</div> <!-- ##commonConfig -->
											</div> <!-- ##TabbedPanelsContent.##commonConfig -->
										
											<div class="TabbedPanelsContent" style="min-height:375px; height:auto;">
												<div id="answersCatalogConfig">
													<s:include value="system-configuration-answers-catalog.jsp"/>
												</div> <!-- ##answersCatalogConfig -->
											</div> <!-- ##TabbedPanelsContent.##answersCatalogConfig -->
										
											<div class="TabbedPanelsContent" style="min-height:375px; height:auto;">
												<div id="reportAggregationConfig">
													<s:include value="system-configuration-report-aggregation.jsp"/>
												</div> <!-- ##reportAggregationConfig  -->
											</div> <!-- ##TabbedPanelsContent.##reportAggregationConfig -->
										
											<div class="TabbedPanelsContent" style="min-height:375px; height:auto;">
												<div id="reportPresentationConfig">
													<s:include value="system-configuration-report-presentation.jsp"/>
												</div> <!-- ##reportPresentationConfig -->
											</div> <!-- ##TabbedPanelsContent.##reportPresentationConfig -->
										</div> <!-- ##TabbedPanelsContentGroup -->
									</div> <!-- ##TabbedPanels1 -->
								</td>
							</tr>
						</table>
					</div> <!-- ##dynamicPane -->
		        </div> <!-- ##container -->
			</div> <!-- ##greyBorder -->
		</td>
	</tr>
</table>