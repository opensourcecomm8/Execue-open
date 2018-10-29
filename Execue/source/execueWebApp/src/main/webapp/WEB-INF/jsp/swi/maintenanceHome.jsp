<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
a.thickbox:hover {
	color: #CF030E;
}

a.thickbox {
	text-decoration: none;
	color: #003399;
}

a.links {
	line-height: 21px;
	font-size: 11px;
}
</style>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.maintenance.main.heading"></s:text></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText">
                <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
                  <tr>
                    <td> <s:text name="execue.maintenance.main.description" /></td>
                  </tr>
                </table>

               </td>
			</tr>
			<tr>
				<td>
				<div id="message" STYLE="color: green;" align="center"></div>
				</td>
			</tr>
			<tr>
				<td valign="top">
				<table border="0" align="center" cellpadding="0" cellspacing="0">
					<tr>
						<td>
						<div id="container"
							style="width: 970px; height: 400px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<div class="ui-layout-east"
							style="overflow-x: hidden; width: 22%; float: left; height: 300px; padding-top: 10px;padding-left:6px;">
						<table width="94%" border="0" align="center" cellpadding="0"
							cellspacing="0">


							<tr>
								<td><a class="links" href=""><s:text name="execue.maintenance.job.parallelword"/></a></td>
							</tr>

							<!-- <tr>
								<td><a class="links" href="#" onclick="showRIOntoTermAbsorption();"><s:text name="maintenance.job.riontoterm"/></a></td>
							</tr>
              
                            <tr>
                                <td><a class="links" href="#" onclick="showSnowFlakeAbsorption();"><s:text name="maintenance.job.snowflakes"/></a></td>
                            </tr>-->

							<tr>
								<td><a class="links" href="#"
									onclick="showPopularityHit();"><s:text name="execue.maintenance.job.popularityhit"/></a></td>
							</tr>
							
							<tr>
								<td><a class="links" href="#"
									onclick="showRIOntoPopularityHit();"><s:text name="execue.maintenance.job.rionto.popularityhit"/></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" onclick="showFileOntology();"><s:text name="execue.maintenance.job.absorbontology"/></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#"
									onclick="showCorrectMapping();"><s:text name="execue.maintenance.job.correctmapping"/></a></td>
							</tr>
							
							<tr>
								<td><a class="links" href="#"
									onclick="showPopularityCollection();"><s:text name="execue.maintenance.job.popularityCollection"/></a></td>
							</tr>
							<tr>
								<td><a class="links" href="#"
									onclick="showPopularityDispersion();"><s:text name="execue.maintenance.job.popularityDispersion"/></a></td>
							</tr>
							
							<tr>
								<td><a class="links" href="#"
									onclick="showSFLTokenWeight();"><s:text name="execue.maintenance.job.sflTermWeight"/></a></td>
							</tr>
							
							<tr>
								<td><a class="links" href="#"
									onclick="showIndexFormManagement();"><s:text name="execue.maintenance.job.indexFormManagement"/></a></td>
							</tr>
							
							<tr>
								<td><a class="links" href="#"
									onclick="showDefaultMetricPopulation();"><s:text name="execue.maintenance.job.defaultMetricsPopulation"/></a></td>
							</tr>
							<tr>
								<td><a class="links" href="#"
									onclick="showSFLWeightBySecWord();"><s:text name="execue.maintenance.job.sflWeightBySecWord"/></a></td>
							</tr>
							
							<tr>
								<td><a class="links" href="#"
									onclick="showConceptTypeAssociation();"><s:text name="execue.maintenance.job.ConceptTypeAssociation"/></a></td>
							</tr>
							
							<tr>
								<td><a class="links" href="#"
									onclick="showMemberSynchronization();"><s:text name="execue.maintenance.job.memberSynchronization"/></a></td>
							</tr>

						</table>
						</div>
						<div id="seperatorLeft"></div>
						<div class="ui-layout-center" style="width: 55%; float: left">
						<div id="dynamicPaneOntology"></div>
						</div>
						</div>
						<!-- <div id="seperatorLeft"></div>
						<div class="ui-layout-west"
							style="overflow-x: hidden; width: 21%; float: left"></div>
						</div>--> &nbsp;
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>