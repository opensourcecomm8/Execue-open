<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>

<%
   int ct = 0;
%>
<%@page import="com.execue.core.common.type.CheckType"%>
<style>
.tableBorder {
	padding-left: 3px;
	border: 1px solid #ccc;
}

#tableGridMemberInfo td {
	padding-left: 0px;
	padding-top: 0px;
}

.granular {
	width: 100px;
}
</style>
<s:form id="columnListForm">
	<div id="errorMessage"><s:actionmessage /></div>
	<div
		style="width: 490px; height: 298px; overflow: auto; overflow-y: hidden; position: relative; border: 1px;"
		id="collist">
	<table width='auto' border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td>
			<table width="auto" border="0" cellpadding="0" cellspacing="0">

				<tr>
					<td colspan="13">

					<div
						style="width: 990px; height: 220px; overflow: hidden; overflow-x: hidden; position: relative; border: 1px;"
						id="collist">
					<table width='auto' border="0" cellspacing="0" cellpadding="0"
						id="tableGrid">
						<tr>
							<td class="columnHeading"><s:text
								name="execue.asset.ws.column.label.name" /></td>
							<%--
					<td  class="columnHeading" ><s:text
						name="asset.ws.column.label.description" /></td>
					--%>
							<td class="columnHeading"><s:text
								name="execue.asset.ws.column.label.column-type" /></td>
							<td class="columnHeading"><s:text
								name="execue.asset.ws.column.label.data-type" /></td>
							<td class="columnHeading"><s:text
								name="execue.asset.ws.column.label.precision" /></td>
							<td class="columnHeading"><s:text
								name="execue.asset.ws.column.label.scale" /></td>
							<td class="columnHeading">
							<div style="width: 65px;"><s:text
								name="execue.asset.ws.column.label.granularity" /></div>
							</td>
							<%--
					<td class="columnHeading"><s:text
						name="asset.ws.column.label.required-flag" /></td>
					<td  class="columnHeading" ><s:text
						name="asset.ws.column.label.default-value" /></td>
					<td  class="columnHeading" ><s:text name="asset.ws.column.label.relation" /></td>
					 --%>
							<td class="columnHeading" width="8%"><s:text
								name="execue.asset.ws.column.label.conversionType" /></td>
							<td class="columnHeading">
							<div style="width: 70px;"><s:text
								name="execue.asset.ws.column.label.dataFormat" /></div>
							</td>
							<td class="columnHeading">
							<div style="width: 65px;"><s:text
								name="execue.asset.ws.column.label.unit" /></div>
							</td>
							<td class="columnHeading">
							<div style="width: 65px; white-space: nowrap;"><s:text
								name="execue.asset.ws.column.label.defaultMetric" /></div>
							</td>


							<!-- <td><input type=text name='format select' value='format select'></td>
        <td><input type=text name='unit select' value='unit select'></td>-->
						</tr>
						<s:iterator value="subListForColumnInfo" status="even_odd"
							id="column">
							<tr>
								<td><input class="col" type="text"
									id="subListForColumnInfo<s:property value="#even_odd.index"/>displayName"
									name="subListForColumnInfo[<s:property value="#even_odd.index"/>].displayName"
									value="<s:property value="displayName"/>"
									title='<s:property value="displayName"/>' readonly="readonly" /></td>
								<%--
						<td><input class="desc" type="text"
							id="subListForColumnInfo<s:property value="#even_odd.index"/>Description"
							name="subListForColumnInfo[<s:property value="#even_odd.index"/>].description"
							value="<s:property value="description"/>" /></td>
						--%>
								<td><s:if test="table.lookupType.value != ''">
									<s:select cssClass="textBox1"
										id="subListForColumnInfo%{#even_odd.index}kdxDataType"
										name="subListForColumnInfo[%{#even_odd.index}].kdxDataType"
										list='columnTypesForLookupTableColumnsTab'
										listValue="description" />
								</s:if> <s:else>
									<s:select cssClass="textBox1"
										id="subListForColumnInfo%{#even_odd.index}kdxDataType"
										name="subListForColumnInfo[%{#even_odd.index}].kdxDataType"
										list='columnTypesForFactTableColumnsTab'
										listValue="description" onchange="javascript:handleCorrespondingDefaultMetricAndGrain('%{#even_odd.index}',this.value)"/>
								</s:else></td>

								<td><s:hidden cssClass="textBox1" cssStyle="width:80px;"
									id="subListForColumnInfo%{#even_odd.index}DataType"
									name="subListForColumnInfo[%{#even_odd.index}].dataType" /> <s:textfield
									cssClass="textBox1" cssStyle="width:80px;" readonly="true"
									id="subListForColumnInfo%{#even_odd.index}DataTypeDescription"
									name="subListForColumnInfo[%{#even_odd.index}].dataType.description" /></td>
								<td><s:textfield cssClass="textBox1" cssStyle="width:50px;"
									readonly="true"
									id="subListForColumnInfo%{#even_odd.index}Precision"
									name="subListForColumnInfo[%{#even_odd.index}].precision" /></td>
								<td><s:textfield cssClass="textBox1" cssStyle="width:30px;"
									readonly="true"
									id="subListForColumnInfo%{#even_odd.index}Scale"
									name="subListForColumnInfo[%{#even_odd.index}].scale" /></td>
								<td><s:if
									test='subListForColumnInfo[#even_odd.index].kdxDataType.value=="DIMENSION" || 
									   subListForColumnInfo[#even_odd.index].kdxDataType.value=="ID" ||
									   subListForColumnInfo[#even_odd.index].kdxDataType.value=="SL" ||
									   subListForColumnInfo[#even_odd.index].kdxDataType.value=="RL"'>
									<s:select cssClass="granular" cssStyle="width:80px;"
										id="subListForColumnInfo%{#even_odd.index}granularity"
										name="subListForColumnInfo[%{#even_odd.index}].granularity"
										list="granularityTypes" listKey="value"
										listValue="description" />
								</s:if> <s:else>
									<s:select cssClass="granular" cssStyle="width:80px;"
										id="subListForColumnInfo%{#even_odd.index}granularity"
										name="subListForColumnInfo[%{#even_odd.index}].granularity"
										list="granularityTypes" listKey="value"
										listValue="description" disabled="true" />
								</s:else></td>

								<%--
							//NOT Using presently					
						<td><s:select cssClass='textBox1' cssStyle="width:50px;"
							id='subListForColumnInfo%{#even_odd.index}Required'
							name='subListForColumnInfo[%{#even_odd.index}].required'
							list='checkTypes' listValue="description"/></td>
						<td><input type="text"
							id="subListForColumnInfo<s:property value="#even_odd.index"/>DefaultValue"
							name="subListForColumnInfo[<s:property value="#even_odd.index"/>].defaultValue"
							value="<s:property value="defaultValue"/>" /></td>
						<td><s:select cssClass='textBox1' cssStyle="width:50px;"
							id='subListForColumnInfo%{#even_odd.index}IsConstraintColum'
							name='subListForColumnInfo[%{#even_odd.index}].isConstraintColum'
							list='checkTypes' listValue="description"/></td>
						--%>
								<td><s:select cssClass="desc"
									id="subListForColumnInfo%{#even_odd.index}conversionType"
									name="subListForColumnInfo[%{#even_odd.index}].conversionType"
									list='conversionTypes' listValue="description"
									onchange="changeFormat('%{#even_odd.index}',this.value);" /></td>
								<!-- <td><input class="desc" type="text" id="subListForColumnInfo<s:property value="#even_odd.index"/>DataFormat" name="subListForColumnInfo[<s:property value="#even_odd.index"/>].dataFormat" value="<s:property value="dataFormat"/>"/></td>
        <td><input type="text" id="subListForColumnInfo<s:property value="#even_odd.index"/>Unit" name="subListForColumnInfo[<s:property value="#even_odd.index"/>].unit" value="<s:property value="unit"/>"/></td>-->
								<td>
								<div style="display: inline; white-space: nowrap; width: 180px;"><span
									id="<%=ct%>"><input type="text"
									style="width: 118px; float: left; font-size: 11px; height: 12px;"
									id="subListForColumnInfo1<s:property value="#even_odd.index"/>DataFormat"
									name="subListForColumnInfo[<s:property value="#even_odd.index"/>].dataFormat" onclick="changedFormatsAndUnits('<%=ct%>');"
									value="<s:property value="dataFormat"/>" readonly  /></span><span
									style="float: left"><img id="selectForamt<%=ct%>"
									src="../images/buttonSmall.jpg" style="cursor:pointer;" title="Click to Edit Data Format" alt="Click to Edit Data Format"
									onclick="changedFormatsAndUnits('<%=ct%>');"></span></div>
								</td>
								<td>
								<div style="display: inline; white-space: nowrap; width: 180px;"><span
									id="unit<%=ct%>"><input type="text"
									style="width: 65px; float: left; font-size: 11px; height: 12px;"
									id="subListForColumnInfo1<s:property value="#even_odd.index"/>Unit"
									name="subListForColumnInfo[<s:property value="#even_odd.index"/>].unit"
									value="<s:property value="unit"/>" disabled="disabled" /></span><span
									style="float: left"><img id="selectUnit<%=ct%>"
									src="../images/buttonSmall.jpg"
									onclick="changedFormatsAndUnits('<%=ct%>');"> <input
									type="hidden"
									id="subListForColumnInfo<s:property value="#even_odd.index"/>DataFormat"
									name="subListForColumnInfo[<s:property value="#even_odd.index"/>].dataFormat"
									value="<s:property value="dataFormat"/>" /> <input
									type="hidden"
									id="subListForColumnInfo<s:property value="#even_odd.index"/>Unit"
									name="subListForColumnInfo[<s:property value="#even_odd.index"/>].unit"
									value="<s:property value="unit"/>" /> </span></div>
								</td>
								<td><s:if
									test='subListForColumnInfo[#even_odd.index].kdxDataType.value=="MEASURE"'>									
									<s:if
										test="subListForColumnInfo[#even_odd.index].defaultMetric.value=='Y'">
										<input type="checkbox"
											id="subListForColumnInfo<s:property value="#even_odd.index"/>defaultMetric"
											name="subListForColumnInfo[<s:property value="#even_odd.index"/>].defaultMetric"
											checked="checked" value="<%=CheckType.YES %>" />
									</s:if>
									<s:else>
										<input type="checkbox"
											id="subListForColumnInfo<s:property value="#even_odd.index"/>defaultMetric"
											name="subListForColumnInfo[<s:property value="#even_odd.index"/>].defaultMetric"
											value="<%=CheckType.YES %>"/>
									</s:else>
								</s:if> <s:else>
									<input type="checkbox"
										id="subListForColumnInfo<s:property value="#even_odd.index"/>defaultMetric"
										name="subListForColumnInfo[<s:property value="#even_odd.index"/>].defaultMetric"
										value="<%=CheckType.YES %>" disabled="disabled"/>
								</s:else></td>
							</tr>
							<%
							   ct++;
							%>
						</s:iterator>
						<%-- 
      <s:property value="pagination.pageCount"/> || <s:property value="pagination.pageSize"/> || <s:property value="pagination.baseURL"/>
          
           <s:iterator value="pageList" status="count">           	
           	<a href="#" id="<s:property value="#count.index"/>" onclick="subList(this.id)"><s:property value="#count.index"/></a>
           </s:iterator>
           --%>


					</table>
					</div>
					</td>
				</tr>


				<tr>
					<td colspan="13">
					<div id="paginationDiv2"><pg:paginateResults /></div>
					</td>
				</tr>
				<tr>
					<td height="16" valign="top" colspan="12"><span id="enableUpdateColumn">
					<label> <input type="button" class="buttonSize80"
						style="width: 80px;" name="imageField3"
						onclick="javascript:updateColumns();" id="imageField3"
						value="<s:text name='execue.asset.columnList.updateColumns' />" /> </label> </span> <span
						id="updateColumnProcess" style="display: none"> <input
						type="button" disabled="disabled" class="buttonSize80"
						style="width: 80px;"
						value="<s:text name='execue.asset.columnList.updateColumns' />" /></span> <span
						id="updateColumnProcessLoader" style="display: none"> <input
						type="button" disabled="disabled" class="buttonLoaderSize80"
						style="width: 80px;"
						value="<s:text name='execue.asset.columnList.updateColumns' />" /></span> <span
						class="rightButton" id="enableRestColumn"> <input
						type="button" hspace="10" class="buttonSize80"
						style="width: 80px; margin-left: 10px;" name="imageField3"
						onclick="javascript:resetFields();" id="imageField3"
						value="<s:text name='execue.global.resetButton.name' />" /> </span> <span
						class="fieldNames" id="restColumnProcess" style="display: none">
					<input type="button" disabled="disabled" class="buttonLoaderSize80"
						style="width: 80px;" name="imageField3" id="resetColumn"
						value="<s:text name='execue.global.resetButton.name' />" /> </span></td>
				</tr>
			</table>
			<s:iterator value="subListForColumnInfo" status="even_odd"
				id="column">
				<input type="hidden"
					id="subListForColumnInfo<s:property value="#even_odd.index"/>Id"
					name="subListForColumnInfo[<s:property value="#even_odd.index"/>].id"
					value="<s:property value="id"/>" />
					<input type="hidden"
					id="subListForColumnInfo<s:property value="#even_odd.index"/>Name"
					name="subListForColumnInfo[<s:property value="#even_odd.index"/>].name"
					value="<s:property value="name"/>" />
					<input type="hidden"
					id="subListForColumnInfo<s:property value="#even_odd.index"/>FileDateFormat"
					name="subListForColumnInfo[<s:property value="#even_odd.index"/>].fileDateFormat"
					value="<s:property value="fileDateFormat"/>" />
			</s:iterator></td>
		</tr>
	</table>
	</div>
</s:form>
<input type='hidden' name='ccolName' id='ccolName' value='<%=ct%>' />

<script>
	//initializing for ever pagelet.
	tableValueChange();
	sameData=true;
	$("#collist").width(collistWidth);
	function tableValueChange(){
		//console.log("init page");
		var tableData = $( "#tableGrid :input" );//table id
		//console.log(jInput);
		// Bind the onchange event for dirty saves
		tableData.bind('change',function( objEvent ){
				// Add dirty flag
				//console.log("value changed");
				sameData=false;
			}
		);
	}
function handleCorrespondingDefaultMetricAndGrain(index,kdxType){
	  if(kdxType=='MEASURE'){
	    $("#subListForColumnInfo"+index+"defaultMetric").removeAttr("disabled"); 
	    $("#subListForColumnInfo"+index+"granularity option[value='NA']").attr('selected', 'selected');   
	    $("#subListForColumnInfo"+index+"granularity").attr("disabled",true);  
	  }else if(kdxType=='DIMENSION' || kdxType=='ID'){
	     $("#subListForColumnInfo"+index+"granularity").removeAttr("disabled");
	     $("#subListForColumnInfo"+index+"defaultMetric").attr("disabled","disabled");
	     $("#subListForColumnInfo"+index+"defaultMetric").attr('checked', false);    
	  }else{
	     $("#subListForColumnInfo"+index+"defaultMetric").attr("disabled","disabled"); 
	     $("#subListForColumnInfo"+index+"granularity").attr("disabled",true); 
	     $("#subListForColumnInfo"+index+"granularity option[value='NA']").attr('selected', 'selected');
	     $("#subListForColumnInfo"+index+"defaultMetric").attr('checked', false);   
	  }
}
</script>