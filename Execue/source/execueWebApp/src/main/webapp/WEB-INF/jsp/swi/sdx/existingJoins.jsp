<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table border="0" cellpadding="0" cellspacing="0" style="margin-left:10px;">
	<tr>
		<td height="30" class="tableHeading">
		<table width="100%" border="0" cellspacing="0" cellpadding="3">
			<tr>
				<td width="80%" valign="bottom" class="tableSubHeading"><s:text
					name="execue.joins.right-pane.heading" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td width="222" valign="top">
		<div class="tableBorder" style="width: 195px;border:none;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			 <tr>
          <td height="30" valign="top"><div id="divExistingJoins">
										<!--DIV id=roundedSearch >
										<div class=searchStart></div>
										<INPUT class=searchField id=searchText type=search
											value=Search>
										<div class=searchEnd id=searchIcon><a href="#"><img
											src="../images/searchEnd.gif" name="Image2a" border="0"
											id="Image2a"
											onMouseOver="MM_showMenu(window.mm_menu_0113140999_0,0,25,null,'Image2a')"
											onMouseOut="MM_startTimeout();" /></a></div>
										</DIV-->
										</div>
										<!--div id="searchTables"><a
											href="javascript:showSearch('divExistingJoins');" class="links"><s:text
											name="global.search" /></a></div>
										</div--></td>
        </tr>
			<tr>
				<td>
				<div class="tableListWithoutBorder"
					style="height: 280px; width: 190px;"><!-- start form for center -->
				<form id="existingJoins">
				<%
				   int count = 0;
				%>
				<table width="300" border="0" cellspacing="0" cellpadding="1" id="searchList" >

					<s:iterator value="uiAssetJoins" status="even_odd"
						id="existingJoin">
						
						<s:if test="#even_odd.even == true">
							<tr class="joinRowEven" id="tableRow3">
						</s:if>
						<s:else>
							<tr id="tableRow3">
						</s:else>
						<td width="10%" class="dotBullet">
						<input name='uiAssetJoins[<%=count%>].checkedStatus' type="checkbox"
							value='Yes' id='uiAssetJoins[<%=count%>].checkedStatus'	onclick="checkedJoin();" />							
						<input type=hidden name='uiAssetJoins[<%=count%>].lhsTableName'	value='<s:property value="lhsTableName"/>'
							id='uiAssetJoins[<%=count%>].lhsTableName' /> <input type=hidden
							name='uiAssetJoins[<%=count%>].rhsTableName'
							value='<s:property value="rhsTableName"/>'
							id='uiAssetJoins[<%=count%>].rhsTableName' /></td>
						<td width="90%" colspan="2" class="fieldNames"><a
							href="javascript:;" onclick="showJoindefinitions('<s:property value="lhsTableName"/>','<s:property value="rhsTableName"/>')"
							class="links"><s:property value="lhsTableDisplayName" /> : <s:property
							value="rhsTableDisplayName" /></a>
                            <input type="hidden" name="uiAssetJoins"
							value="<s:property value="value"/>" />
                            </td>
						</tr>
						<%
						   count++;
						%>
					</s:iterator>
				</table>
				</form>
				</div>
				</td>
			</tr>
			<tr>
				<!-- td align="center"><img src="../images/deleteSelectedButton.gif" width="136" height="27" hspace="3" vspace="5" onclick="deleteUIAssetJoins();" /></td-->
				<td align="center" ><div style="display: none" id="enableDeleteJoin"><input type="button"	class="buttonSize108" value="<s:text name='execue.global.deleteSelectedButton.name' />" onclick="deleteUIAssetJoins();" /></div><span id="deleteProcess" style="display:none">
                <input type="button"	class="buttonLoaderSize108" disabled="disabled"
					value="<s:text name='execue.global.deleteSelectedButton.name' />"  /></span>
				<div  id="disableDeleteJoin"><input type="button" disabled="disabled" class="buttonSize108" value="<s:text name='execue.global.deleteSelectedButton.name' />" /></div></td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>
<script>
$(function(){
		   $("#divExistingJoins").searchRecordsComponent({actionName:"",targetDivName:"existingJoins"});
		   });
</script>