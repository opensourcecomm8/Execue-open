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

table.jobs a {
	line-height: 20px;
}

span.fieldNames {
	float: left;
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
				<table width="98%" border="0" cellspacing="0" cellpadding="0"
					align="center">
					<tr>
						<td><s:text name="execue.maintenance.main.description" /></td>
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
							style="overflow-x: hidden; width: 30%; float: left; height: 300px; padding-top: 10px; padding-left: 6px;">
						<table width="94%" border="0" align="center" cellpadding="0"
							cellspacing="0" class="jobs">

							<tr>
								<td><a class="links" href="#" id="1"
									onclick="showPopularityHit(this);"><s:text
									name="execue.maintenance.job.popularityhit" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="2"
									onclick="showRIOntoPopularityHit(this);"><s:text
									name="execue.maintenance.job.rionto.popularityhit" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#"
									onclick="showFileOntology(this);" id="2a"><s:text
									name="execue.maintenance.job.absorbontology" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="3"
									onclick="showCorrectMapping(this);"><s:text
									name="execue.maintenance.job.correctmapping" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="4"
									onclick="showPopularityCollection(this);"><s:text
									name="execue.maintenance.job.popularityCollection" /></a></td>
							</tr>
							<tr>
								<td><a class="links" href="#" id="5"
									onclick="showPopularityDispersion(this);"><s:text
									name="execue.maintenance.job.popularityDispersion" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="6"
									onclick="showSFLTokenWeight(this);"><s:text
									name="execue.maintenance.job.sflTermWeight" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="7"
									onclick="showIndexFormManagement(this);"><s:text
									name="execue.maintenance.job.indexFormManagement" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="8"
									onclick="showSFLWeightBySecWord(this);"><s:text
									name="execue.maintenance.job.sflWeightBySecWord" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="9"
									onclick="showConceptTypeAssociation(this);"><s:text
									name="execue.maintenance.job.ConceptTypeAssociation" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="10"
									onclick="showSDXSynchronization(this);"><s:text
									name="execue.maintenance.job.sdxSynchronization" /></a></td>
							</tr>

							<tr>
								<td><a class="links" href="#" id="10"
									onclick="showOptimalDSet(this);"><s:text
									name="execue.maintenance.job.optmalDSet" /></a></td>
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
						</div>--> &nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>