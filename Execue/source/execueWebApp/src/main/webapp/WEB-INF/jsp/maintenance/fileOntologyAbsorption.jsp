<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="50" cellpadding="0" cellspacing="0">
	<tr>
		<td>
		<div id="greyBorder1">
		<table width="50%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td valign="top" class="descriptionText"><s:text
					name="execue.ontology.job.description" /></td>
			</tr>
			<tr>
				<td valign="top">

				<table width="200" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>


						<div id="container" style="width: 600px; height: 405px;">

						<div class="ui-layout-center"
							style="width: 90%; overflow: hidden;">
						<div id="dynamicPane">

						<div id="errorMessage"><s:fielderror /> <s:actionmessage />
						<s:actionerror /></div>

						<div class="innerPane" style="width: 99%"><s:form id="absorbOntologyForm"
							namespace="/swi" action="absorbFileOntology" method="post"
							enctype="multipart/form-data">
							<table width="100%" border="0" cellspacing="0" cellpadding="0">
								<tr>
									<td width="40%">

									<table width="50%" border="0" align="center" cellpadding="3"
										cellspacing="0">
										<tr>
											<td class="fieldNames"><s:text
												name="execue.ontology.model.name" /> &nbsp;&nbsp;<%=request.getAttribute("modelId")%></td>
										</tr>
										<tr>
											<td height="35" colspan="2" class="fieldNames">&nbsp;</td>
										</tr>
									</table>

									</td>
									<td width="49%" valign="top">
									<table width="50%" border="0" align="center" cellpadding="3"
										cellspacing="0">
										<tr>
											<td class="fieldNames">&nbsp;</td>
											<td height="40">&nbsp;</td>
										</tr>
										<tr> 
											<td  width="24%" class="fieldNames" style="white-space:nowrap"><s:text
												name="execue.ontology.upload.source-file" /></td>
											<td width="76%" align="left"><label> <s:file
												name="sourceData" label="File" /> </label></td>
										</tr>
										<tr>
											<td height="35" colspan="2" class="fieldNames">
											<div
												style="width: 90%; border: 1px solid #CCC; padding: 10px;">
											<table width="80%" border="0" align="center" cellpadding="4"
												cellspacing="0">
												<tr>
													<td colspan="2"></td>
												</tr>
												<tr>
													<td width="71%"><s:text
														name="execue.ontology.generate.sfl.check" /></td>
													<td width="29%"><label> <s:checkbox
														name="generateSFLTerms" value="true" fieldValue="true" />
													</label></td>
												</tr>
												<tr>
													<td width="71%"><s:text
														name="execue.ontology.generate.rionto.check" /></td>
													<td width="29%"><label> <s:checkbox
														name="generateRIOntoterms" value="true" fieldValue="true" />
													</label></td>
												</tr>
											</table>
											</div>
											</td>
										</tr>
									</table>
								<tr>
									<td colspan="2">&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2" align="center" height="40" valign="bottom"><span
										class="fieldNames"> <input type="submit" class="buttonSize108"
										name="imageField" id="imageField"
										value="<s:text name='execue.global.absorbButton' />" /> <input type="button" class="buttonSize51"
										name="imageField2" id="imageField2"
										value="<s:text name='execue.global.cancel' />" /> </span></td>
								</tr>
							</table>

							<input type=hidden name="modelId" id="modelId"
								value="<%=request.getAttribute("modelId") %>" />
							<!-- <input type=hidden name="applicationId" id="applicationId"
								value="" />-->

						</s:form></div>
						</div>
						</div>
						<!-- 
            <div class="ui-layout-east" style="overflow-x: hidden;width:10px">
            </div>
            --></div>
						&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>