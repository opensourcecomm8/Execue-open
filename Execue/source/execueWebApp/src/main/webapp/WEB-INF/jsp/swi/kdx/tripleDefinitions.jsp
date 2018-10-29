<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground">Triple  Definitions</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder">
		<table width="98%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td colspan="3" valign="top" class="descriptionText"><s:text
					name="execue.defineBusinessTerm.description" /></td>
			</tr>

			<tr>
				<td colspan="3" valign="top" background="../images/blueLine.jpg"><img
					src="../images/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td width="19%" valign="top">
				<table width="100%" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td height="27"><span class="tableSubHeading"><s:text
							name='execue.businessterms.concepts.subheading' /></span></td>
					</tr>
					<tr>
						<td height="25"><a href="javascript:createNewConcept();"
							class="arrowBg" id="addNewConcept"><s:text
							name='execue.businessterms.AddNewConcept.link' /></a> <!--div id="loadingAddNewConceptLink" style="display:none;"><img src="../images/loading.gif" width="20" height="20" /></div--></td>
					</tr>

					<tr>
						<td align="left">
						<div class="tableBorder"
							style="padding-top: 5px; height: 344px; width: auto; margin-bottom: 5px; border: none;">
						<table width="99%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" align="left" valign="top">
								<div id="divBTerms">
								</div>
								</td>
							</tr>
							<tr>
								<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
										<div id="dynamicPaneBTerms" style="width: 207px;"></div>
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
				</td>
				<td width="3%" height="350" align="center" class="blueLineVeritical">&nbsp;</td>
				<td width="78%" valign="top">
				<table border="0" cellpadding="0" cellspacing="0">
					<!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->

					<tr>
						<td height="350" align="left" valign="top">

						<div id="TabbedPanels1" class="TabbedPanels"
							style="margin-bottom: 10px; margin-top: 10px; display: none;">
						<ul class="TabbedPanelsTabGroup">
							<li class="TabbedPanelsTab" tabindex="0">Define Hierarchies</li>                            
                            <li class="TabbedPanelsTab" tabindex="2">Define Relations</li>
                           <div id="conceptNameHeadingDiv" style="padding-top:17px;text-align:right;font-weight:bold;"></div>
						</ul>
						<div class="TabbedPanelsContentGroup">
						<div class="TabbedPanelsContent"
							style="min-height: 490px; height: auto;">
						<div id="dynamicPane" style="width: 720px"></div>
						
						</div>
                        <div class="TabbedPanelsContent"
                                    style="min-height: 490px; height: auto; width: 720px;">
                                   <div id="dynamicPaneRelations" style="width: 720px"><img
					 style=" margin-top: 160px;margin-left:350px;"
					src="../images/loadingBlue.gif" /></div>
                                </div>
                                
						</div>
                        
                       
                                
						</div>
						</div>
						</td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="3" valign="top" background="../images/blueLine.jpg"><img
					src="../images/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				<td colspan="3">&nbsp;</td>
			</tr>

		</table>
		</div>
		</td>
	</tr>

</table>
<script type="text/javascript">
<!--
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1");
//-->
</script>
