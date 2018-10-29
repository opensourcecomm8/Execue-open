<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table cellspacing="0" cellpadding="0" border="0" align="left"
	width="100%">
	<tbody>

		<tr>
			<td width="100%" colspan="2">
			<table cellspacing="0" cellpadding="2" border="0" align="center"
				width="100%">
				<tbody>
					<tr>
						<td colspan="2">
						<div align="left" style="color: red;" id="message"></div>
						</td>
					</tr>
					<tr>
						<td>
						<table cellspacing="0" border="0">
							<tbody>
								<tr>
									<td width="100" valign="top" style="padding-top: 5px;">Select
									Concepts</td>
									<td style="padding-top: 5px;"><select multiple="multiple"
										size="6" style="width: 205px;" name="select3"
										id="conceptsForHierarchy">
										<s:iterator value="eligibleHierarchyConcepts">
											<option value='<s:property value="bedId"/>'><s:property
												value="displayName" /></option>
										</s:iterator>
									</select>
									<div id="selectedPathDefinitionsHierarchyDiv"><s:iterator
										value="selectedHierarchyPathDefinitions">
										<input type="hidden" name="selectedPathDefinitionsHierarchy"
											sourceConceptBedId='<s:property value="sourceConceptBedId"/>'
											sourceConceptName='<s:property value="sourceConceptName"/>'
											destinationConceptBedId='<s:property value="destinationConceptBedId"/>'
											destinationConceptName='<s:property value="destinationConceptName"/>'
											relationBedId='<s:property value="relationBedId"/>'
											relationName='<s:property value="relationName"/>'
											entityTripleDefinitionId='<s:property value="entityTripleDefinitionId"/>' />
									</s:iterator></div>
									</td>
									<td valign="bottom" style="padding-left: 15px;"><a
										href="javascript:addRow('Hierarchy');">Add</a></td>
								</tr>
							</tbody>
						</table>
						</td>
					</tr>
					<tr>
						<td colspan="2">
						<table cellspacing="0" cellpadding="0" border="0" align="center"
							width="100%">
							<tbody>
								<tr>
									<td colspan="4">
									<div
										style="width: 565px; height:auto; border: 1px solid rgb(204, 204, 204); padding: 5px; margin-top: 10px;"
										id="behaviourDiv">

									<table cellspacing="0" cellpadding="3" border="0">
										<tbody>

											<tr>
												<td colspan="4">
												<table cellspacing="0" cellpadding="0" border="0">
													<tbody>
														<tr>
															<td><strong>Hierarchy</strong></td>
															<td><span style="display: none;"
																id="realizationHeading"><strong>Realization</strong></span></td>
															<td></td>
														</tr>
													</tbody>
												</table>
												</td>
											</tr>
											<tr>
												<td align="left" colspan="4">
												<div
													style="height: 150px; overflow-y: auto; overflow-x: hidden; width: 560px;"
													id="assoiationDisplayDivHierarchy"></div>

												</td>
											</tr>



										</tbody>
									</table>


									</div>

									</td>
								</tr>

							</tbody>
						</table>
						</div>
						</td>
					</tr>
				</tbody>
			</table>
			</td>
		</tr>

	</tbody>
</table>
<script>
$(document).ready(function(){
	   showExistingPathDefinitions('Hierarchy');
}); 
	</script>