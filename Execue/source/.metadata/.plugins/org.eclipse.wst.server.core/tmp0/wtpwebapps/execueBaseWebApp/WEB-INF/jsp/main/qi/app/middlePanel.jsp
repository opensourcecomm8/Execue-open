<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td style="padding-top: 12px;" class="headding-apps">Examples</td>
	</tr>
	<tr>
		<td style="padding-top: 12px;">

		<table width="100%" class="exampleQueriesApp" border="0" cellspacing="0" cellpadding="0">
			<s:iterator value="uiApplicationInfo.appExamples" id="appExamplesListId">
				<tr>
				 <s:if test="%{#appExamplesListId.queryName != #appExamplesListId.truncatedQueryName}">
				   <td align="left"><a style="cursor: pointer;" class="box2-link"  title="<s:property value='queryName'/>"
										alt="<s:property value='queryName'/>" ><s:property
						value="truncatedQueryName" /> </a>...</td>
				 </s:if>
				 <s:else>
				   <td align="left"><a style="cursor: pointer;" class="box2-link"  title="<s:property value='queryName'/>"
										alt="<s:property value='queryName'/>" ><s:property
						value="truncatedQueryName" /> </a></td>
				 </s:else>
					
				</tr>
			</s:iterator>
		</table>


		</td>
	</tr>
	<tr class="businessTerms">
		<td width="49%" style="padding-top: 12px;" class="headding-apps">Business Terms</td>
	</tr>
	<tr class="businessTerms">
		<td valign="top">
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td>
				<table width="100%" border="0" align="left" cellpadding="3"
					cellspacing="0" id="t2" class="appTable">
					<TR>
						<TD vAlign=top>
						<TABLE border=0 cellSpacing=0 cellPadding=0 width="100%"
							align=center>
							<TBODY>
								<TR>
									<TD>
									<table cellspacing="0" class="exampleConcepts"   cellpadding="0" border="0"
													width="100%" id="searchList">
													<tbody>

														<s:iterator value="uiConcepts" id="uiConcepts"
															status="idx">
															<s:if test="#idx.index <= 20">
																<tr >
																	<s:if test="%{#uiConcepts.instances.size>0}">
																		<td width="1%" valign="top" class="dotBullet"><a
																			id="a<s:property value='id'/>" 
																			href="javascript:getInstance('<s:property value="id"/>');"><img
																			id="img<s:property value='id'/>"
																			src="<c:out value="${basePath}"/>/images/main/bullet_toggle_plus.png"
																			border="0" /></a></td>
																	</s:if>
																	<s:else>
																		<td width="1%" valign="top" class="dotBullet"></td>
																	</s:else>
																	<td width="99%" class="fieldNames">
																	<div id="showConcept<s:property value='id'/>Link"
																		style="float: left; width: 100%;"><a
																		class="box2-link"
																		href="#"><s:property
																		value="displayName" /></a></div>
																	<br>
																	<div
																		style="display: none; float: left; padding-left: 20px;"
																		id="loadingShowConcept<s:property value='id'/>Link">

																	<table width="150" border="0" cellspacing="0"
																		cellpadding="0">
																		<s:iterator value="%{#uiConcepts.instances}"
																			id="instances">
																			<tr>
																				<td><a class="instance-link" style="cursor: pointer;"><s:property value="%{#instances.displayName}" /></a></td>
																			</tr>
																		</s:iterator>
																	</table>
																	</div>
																	</td>
																</tr>
															</s:if>
															<s:else>
																<tr style="display:none;" class="moreConceptsTr" >
							
																			<s:if test="%{#uiConcepts.instances.size>0}">
																				<td width="1%" valign="top" class="dotBullet"><a
																					id="a<s:property value='id'/>" class="box2-link"
																					href="javascript:getInstance('<s:property value="id"/>');"><img
																					id="img<s:property value='id'/>"
																					src="<c:out value="${basePath}"/>/images/main/bullet_toggle_plus.png"
																					border="0" /></a></td>
																			</s:if>
																			<s:else>
																				<td width="1%" valign="top" class="dotBullet"></td>
																			</s:else>
																			<td width="99%" class="fieldNames">
																			<div id="showConcept<s:property value='id'/>Link"
																				style="float: left; width: 100%;"><a
																				class="box2-link"
																				href="#"><s:property value="displayName" /></a></div>
																			<br>
																			<div
																				style="display: none; float: left; padding-left: 20px;"
																				id="loadingShowConcept<s:property value='id'/>Link">

																			<table width="150" border="0" cellspacing="0"
																				cellpadding="0">
																				<s:iterator value="%{#uiConcepts.instances}"
																					id="instances">
																					<tr>
																						<td><a class="instance-link" style="cursor: pointer;"><s:property value="%{#instances.displayName}" /></a></td>
																					</tr>
																				</s:iterator>
																			</table>
																			</div>
																			</td>
																		</tr>
																	
															</s:else>
														</s:iterator>
														</div>
														</td>
														</tr>
													</tbody>
												</table>
									</TD>
								</TR>
								<TR>
									<TD class=inner-line align=middle style="padding-top: 5px;">
									<table width="90%" border="0" align="center" cellpadding="0"
										cellspacing="0">
										<tr>
											<td width="83%" height="25"
												style="border-top: 1px dotted #999;">&nbsp;</td>
											<td width="17%" align="right"
												style="border-top: 1px dotted #999;"><a
												onFocus="this.blur()" id="showMoreConceptsLink"
												href="javascript:showMoreConcepts();"><img
												src="<c:out value="${basePath}"/>/images/quinnox/more.png" width="50"
												height="15" border="0"></a></td>
										</tr>
									</table>
									</TD>
								</TR>
							</TBODY>
						</TABLE>
						</TD>
					</TR>
					<TR>
					<tr>
						<td height="25" align="left">&nbsp;</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td align="center" class="inner-line">&nbsp;</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<s:hidden id="modelId" name="uiApplicationInfo.modelId" />
<script>
  $(document).ready(function() {
   // showBusinessTerms();
 });

function getInstance(x){

	$("#loadingShowConcept"+x+"Link").slideDown();
	$("#img"+x).attr("src","<c:out value='${basePath}'/>/images/main/bullet_toggle_minus.png");
	$("#a"+x).attr("href","javascript:close("+x+");");
    setShadedHeights();
}
function close(x){
	$("#loadingShowConcept"+x+"Link").slideUp();
	$("#img"+x).attr("src","<c:out value='${basePath}'/>/images/main/bullet_toggle_plus.png");
	$("#a"+x).attr("href","javascript:getInstance("+x+");");
    setShadedHeights();
}
function showMoreConcepts(){
	$(".moreConceptsTr").show(); 
	$("#showMoreConceptsLink img").attr("src","<c:out value='${basePath}'/>/images/quinnox/less.png");
	$("#showMoreConceptsLink").attr("href","javascript:hideMoreConceptsLink();");
    setShadedHeights();
}
function hideMoreConceptsLink(){
	$(".moreConceptsTr").hide(); 
	$("#showMoreConceptsLink img").attr("src","<c:out value='${basePath}'/>/images/quinnox/more.png");
	$("#showMoreConceptsLink").attr("href","javascript:showMoreConcepts();");
	setShadedHeights();
}

function showBusinessTerms(){
   var modelId=$("#modelId").val();   
  showDetails("showConceptForApp.action?appModelId="+modelId,"dynamicPaneBTerms","get");  
}
</script>