<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="aw" uri="/WEB-INF/tlds/absorptionWizard.tld"%>
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.joins.heading" /></td>
				<td height="29" align="left">
					
				</td>
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
				<td valign="top"  >
               <table width="98%" border="0" cellspacing="0" cellpadding="0" align="center">
               <tr><td height="25">
             
                  
                    <jsp:include page='/views/showSelectAsset.jsp' flush="true" />
                    </td></tr>
                  <tr>
                    <td  class="descriptionText"> <s:text	name="execue.joins.description" /></td>
                    
                  </tr>
                </table>

                </td>
			</tr>
			<tr>
				<td>
				<div id="message" STYLE="color: red" align="center"></div>
				</td>
			</tr>
			<!-- tr>
          <td valign="top" background="../images/blueLine.jpg"><img src="../images/blueLine.jpg" width="10" height="1" /></td>
          </tr -->
			<tr>
				<td valign="top">

				<table width="400" border="0" align="center" cellpadding="0"
					cellspacing="0">
					<tr>
						<td>


						<div id="container"
							style="width: 970px; height: 400px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;display:none;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<div class="ui-layout-west"
							style="overflow-x: hidden; width: 21%; float: left;padding-left:3px;">
						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="27"><span class="tableSubHeading"  style="padding-left:5px;"><s:text
									name="execue.joins.left-pane.heading" /></span></td>
							</tr>
							<tr>
								<td>

								<div class="tableBorder" style="padding-top: 5px;border:none;">
								<table width="99%" border="0" cellspacing="0" cellpadding="0">
									<tr>
                                     <td height="30"  align="left" valign="top">
                                     
                                     <div id="divJoins">
										
													</div>

                                   </td>
                               </tr>
									<tr>
										<td>
										<div id="divJoinsTable" class="tableListWithoutBorder"
											style="height: 320px; width: 190px;">
                                            <table width="100%" border="0" cellspacing="0" cellpadding="0" id="searchList2">
                                            <s:iterator	value="uiTableForJoins" status="even_odd" id="assetTable">
                                            <s:if test="virtual.value != 'Y'">                                       
                                            <tr><td>
                                                    
											<div class=tablesList style='width:30px;display:inline;white-space:nowrap;' 
											 displayName = '<s:property value="tableDisplayName"/>' id='<s:property value="tableName"/>'>
											<IMG class=folderImage src="../images/tableIcon.jpg" width="20" > <SPAN
												class=tableHolder><s:property value="tableDisplayName" /></SPAN></div>
                                                <div style='width:180px;'><img src='../images/1pix.gif' width='180' height='1' border='0' /></div>
                                                </td> </tr>
                                                </s:if>
										</s:iterator>
                                         </table>
                                        </div>
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>
						</div>
						<div id="seperatorLeft"></div>
						<div class="ui-layout-center" style="width: 53%; float: left">

						<!-- start form for center -->
						<form id="joinDefinitions">
						<table width="90%" border="0" align="center" cellpadding="0"
							cellspacing="0">
							<tr>
								<td>
								<table width="90%" border="0" align="center" cellpadding="0"
									cellspacing="2">
									<tr>
										<td valign="top">
										<div class=leftTable>
										<div class=leftTableHolder id=table1 style="margin-left:5px;width:235px">
										<div id="leftTableName"><s:text
											name="execue.joins.left-table.box.name" /></div>
										</div>
										</div>

										<div class="tableList"
											style="height: 160px; width: 245px; display: none;margin-left:5px;"
											id="columnsRowLeft">
										<div id=leftColumnList style="width:400px;"></div>
										</div>
										</td>
										<td>
										<div class=rightTable>
										<div class=rightTableHolder id=table2 style="width:235px"">
										<div id="rightTableName"><s:text
											name="execue.joins.right-table.box.name" /></div>
										</div>
										</div>

										<div class="tableList"
											style="height: 160px; width: 245px;; display: none;"
											id="columnsRowRight">
										<div id=rightColumnList style="width:400px;"></div>
										</div>
										</td>
									</tr>
								</table>
								</td>
							</tr>
							<tr style="display: none" id="resultsListHeading">
								<td height="25"><span class="subHeadings" style="padding-left:10px;"><s:text
									name="execue.joins.user-created.joins.heading" /></span></td>
							</tr>
							<tr style="display: none" id="resultsList">
								<td height="35">
								<div id="joinsContent"
									style="font-size: 11px; padding-left: 0px;overflow-x:auto;width:490px;margin-left:20px;"></div>
								</td>
							</tr>
							<tr>
								<td>
								<div id="autoSuggestedJoins" style="display: none;">
								<table width="100%" border="0" align="center" cellpadding="3"
									cellspacing="0">
									<tr id=autoSuggestedId>
										<td height="28" class="subHeadings" style="padding-left:10px;">
                                        
                                        
                                        
                                        <table width="98%" border="0" cellspacing="0" cellpadding="0">
                                          <tr>
                                            <td width="50%"><s:text name="execue.joins.suggested-existing.joins.heading" /></td>
                                            <td width="50%" align="right"><div id="suggestHeading" style="width:100%;padding:5px 0px;" ><a href="javascript:showSuggested();">Show Suggested Joins</a></div></td>
                                          </tr>
                                        </table>

                                        
                                        </td>
									</tr>
									<tr>
										<td>
									<div id="dynamicPane" style="overflow: auto; width: 500px; min-height: 30px;height:auto;margin-left:10px;">
                                       
                                        </div>
									  </td>
									</tr>
									<tr>
										<td>
										<span id="enabledSaveJoin"><input type="button"	class="buttonSize160"
											value="<s:text name='execue.joins.saveselectedjoin.button' />" alt=""  align="left"
											onclick="saveSelectedJoins();" id="saveSelectedJoinId" /></span>
										<span id="saveJoinLoader" style="display: none"><input type="button"	class="buttonLoaderSize160"
										disabled="disabled"	 align="left"
											value="<s:text name='execue.joins.saveselectedjoin.button' />" /></span>
										<!--<span id="saveProcess" style="display: none"><img
											width="153" height="26" hspace="22" vspace="0" align="left"
											src="../images/saveSelectedJoins_disabled.gif" /></span>-->
										<div id="disabledSaveJoin"><input type="button"	class="buttonSize160"
										disabled="disabled"	 align="left"
											value="<s:text name='execue.joins.saveselectedjoin.button' />"  /></div>
										</td>
									</tr>
								</table>


								</div>
								</td>
							</tr>
							<tr>
								<td>
								<div id="createNewJoins" style="display: none"><!-- Create New Joins by maniual process code taken off -RG- -->
								</div>
								</td>
							</tr>
						</table>
						</form>
						</div>
						<div id="seperatorRight"></div>
						<div class="ui-layout-east"
							style="overflow-x: hidden; width: 22%; float: left; vertical-align: top;padding-left:0px;">
						<div id="existingJoinsPane"></div>
						</div>
						</div>
						&nbsp;</td>
					</tr>
				</table>


				</td>
			</tr>
		</table>

		<!-- end form for center --></div>
		</td>
	</tr>
</table>
<form id="requiredValues"> <s:hidden
	name="sourceTableName" /> <s:hidden name="destTableName" /></form>

<s:iterator value="joinTypes" id="joinTypeList" status="li">
	<input type="hidden" name="joinTypes" class="jType" typeAttr="<s:property value="value"/>"
		value="<s:property value="description"/>" />
</s:iterator>
<p><tt id="results"></tt></p>

