<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name='execue.businessterms.heading' /></td>
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
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
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
							name='execue.businessterms.AddNewConcept.link' /></a> <!--div id="loadingAddNewConceptLink" style="display:none;"><img src="../images/admin/loading.gif" width="20" height="20" /></div--></td>
					</tr>

					<tr>
						<td align="left">
						<div class="tableBorder"
							style="padding-top: 5px; height: 344px; width: auto; margin-bottom: 5px; border: none;">
						<table width="99%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="30" align="left" valign="top">
								<div id="divBTerms"><!--DIV id=roundedSearch  >
                      <div class=searchStart ></div><INPUT class=searchField id=searchText 
                    type=search value=Search><div class=searchEnd id=searchIcon><a href="#"><img
                                                                src="../images/admin/searchEnd.gif" name="Image2" border="0"
                                                                id="Image2"
                                                                onMouseOver="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2')"
                                                                onMouseOut="MM_startTimeout();" /></a></div--></DIV>

								<!--div id="searchTables" ><a href="javascript:showSearch('divBTerms');" class="links">Search</a></div-->
								</div>
								</td>
							</tr>
							<tr>
								<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td><!-- table width="100%" border="0" cellspacing="0" cellpadding="2">
                                        <s:iterator value="concepts" status="inst" id="concept">
                                          <tr id="conceptRow1" >
                                            <td width="1%" class="dotBullet">&nbsp;</td>
                                            <td width="99%" class="fieldNames"><div id="showConcept1Link" ><a href="javascript:getConcept(<s:property value="%{#concept.id}"/>);" class="links" id=<s:property value="%{#concept.id}"/>)><s:property value="%{#concept.displayName}" /></a></div>
                                              <div id="loadingShowConcept1Link" style="display:none;"><img src="../images/admin/loadingBlue.gif" width="25" height="25" /></div></td>
                                            </tr>
                                          </s:iterator>
                                          </table--->
										<div id="dynamicPaneBTerms" style="width: 207px;"></div>
                                        <input type="hidden" id="search_string" value="" />
                                        <input type="hidden" id="search_type" value="" />
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
                        <div id="actionMessage" style="display: none; color: green; padding-top: 10px; padding-left: 20px;"></div>
						<div id="TabbedPanels1" class="TabbedPanels"
							style="margin-bottom: 10px; margin-top: 10px; display: none;">
						<ul class="TabbedPanelsTabGroup">
							<li class="TabbedPanelsTab" tabindex="0">Concept Details</li>
							<!--li class="TabbedPanelsTab" tabindex="1">Add Meaning</li-->
                            
                            <li class="TabbedPanelsTab" tabindex="2">Advanced Meaning</li>
                            <li class="TabbedPanelsTab" tabindex="3">Advanced Concept Details</li>
                             <li class="TabbedPanelsTab" tabindex="2">Concept Associations</li>
                            
                            <div id="conceptNameHeadingDiv" style="padding-top:17px;text-align:right;font-weight:bold;"></div>
						</ul>
						<div class="TabbedPanelsContentGroup">
						<div class="TabbedPanelsContent" style="min-height: 450px; height: auto;">
						<div id="dynamicPane" style="width: 720px"></div>
						
						</div>
						<!-- <div class="TabbedPanelsContent"
							style="min-height: 410px; height: auto; width: 720px;">
						   <div id="dynamicPaneTypes" style="width: 720px"></div>
						   <div id="dynamicPanePathDefinition" style="width: 720px"></div>
						</div>-->
                       
                        <div class="TabbedPanelsContent" style="min-height: 440px; height: auto; width: 720px;">
                                   <div id="dynamicPaneAdvancedPathDefinition" style="width: 720px" class="dynamicPaneBgNoLoader"><img
					 style=" margin-top: 160px;margin-left:350px;"
					src="../images/admin/loadingBlue.gif" /></div>
                                </div>
                                
                                
                                 <div class="TabbedPanelsContent" style="min-height: 420px; height: auto; width: 720px;">
                                   <div id="dynamicPaneAdvancedConceptDetails" style="width: 720px"></div>
                                </div>
                                
                                
                                <div class="TabbedPanelsContent" style="min-height: 440px; height: auto; width: 720px;">
                                   <div id="dynamicPaneConceptAssociations" style="width: 720px;height:300px;" class="dynamicPaneBgNoLoader"><img
					 style=" margin-top: 160px;margin-left:350px;"
					src="../images/admin/loadingBlue.gif" /></div>
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
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
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
$(function(){
		   $("li.TabbedPanelsTab").click(function(){
												  $(this).blur();
												  });
		   });
function showPanel1(){
TabbedPanels1.showPanel(0);	
}
</script>
