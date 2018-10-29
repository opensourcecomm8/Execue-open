<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>
<%@page import="com.execue.core.common.type.CheckType"%>
<style>
#dataTypeTd select {
	width: 90px;
}

#entityDataTypeTr select {
	width: 90px;
}

#unitTypeTd select,#formatTd select,#unitTd select,#granularityTd select
	{
	width: 90px;
}
</style>
<div class="innerPane" style="width: 99%" id="evalCollist"><s:form
	id="evaluateColumnForm" namespace="/publisher" method="POST">
	<table width="100%" border="0" cellspacing="0" cellpadding="0">
		<thead>
			<tr>
				<td colspan="2">
				<div id="errorMessage" align="center" style="color: red"><s:actionerror />
				</div>
				<div id="actionMessage" align="center" style="color: green"><s:actionmessage /></div>
				</td>
			</tr>
		</thead>
		<tr>
			<td>
			<table cellspacing="1" cellpadding="1" border="0" width=""
				id="tableGrid">
				<tbody>
					<tr>
						<td width="8%"><strong>Name</strong></td>
						<td width="8%"><strong>Data Type</strong></td>
						<td width="8%"><strong>Precision</strong></td>
						<td width="8%"><strong>Scale</strong></td>

						<td width="10%" id="entityDataTypeHeader"><strong>Entity
						Data Type</strong></td>
						<td width="8%" id="dataTypeHeader"><strong>Unit Type</strong></td>
						<td width="8%" id="formatHeader"><strong>Format</strong></td>
						<td width="8%" id="unitHeader"><strong>Unit</strong></td>
						<td width="8%" id="granularityHeader"><strong>Granularity</strong></td>
						<td width="8%" style="white-space: nowrap"
							id="defaultMetricHeader"><strong>Default Metric</strong></td>


						<!-- <td width="10%"><strong>is Location</strong></td>
						<td width="10%"><strong>is Time</strong></td>-->
					</tr>
					<s:iterator value="subListForEvaluatedColumsInfo" status="even_odd"
						id="evaluatedSubCol">
						<tr>
							<td><input class="col" type="hidden"
								id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>id"
								name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].id"
								value="<s:property value="id"/>" /> <input class="col"
								type="hidden" style="width: 130px;"
								id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>BaseName"
								name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].baseColumnName"
								value="<s:property value="baseColumnName"/>" /> <input
								class="col" type="text" style="width: 130px;"
								id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>Name"
								name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].evaluatedColumnName"
								value="<s:property value="evaluatedColumnName"/>"
								title='<s:property value="evaluatedColumnName"/>' /></td>
							<td id="dataTypeTd"><s:select cssClass="textBox1"
								cssStyle="display:none"
								id="subListForEvaluatedColumsInfo%{#even_odd.index}baseDataType"
								name="subListForEvaluatedColumsInfo[%{#even_odd.index}].baseDataType"
								list='dataTypes' listValue="description" /> <s:select
								cssClass="textBox1"
								id="subListForEvaluatedColumsInfo%{#even_odd.index}DataType"
								name="subListForEvaluatedColumsInfo[%{#even_odd.index}].evaluatedDataType"
								list='dataTypes' listValue="description"
								onchange="javascript:correctDataLengthFields('%{#even_odd.index}')" /></td>
							<td><input class="col" type="hidden" style="width: 50px;"
								id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>basePrecition"
								name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].basePrecision"
								value="<s:property value="basePrecision"/>" /> <s:if
								test='subListForEvaluatedColumsInfo[#even_odd.index].evaluatedDataType.value=="DATE" ||subListForEvaluatedColumsInfo[#even_odd.index].evaluatedDataType.value=="DATETIME" ||subListForEvaluatedColumsInfo[#even_odd.index].evaluatedDataType.value=="TIME"'>
								<input class="col" type="text" style="width: 50px;"
									id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>Precition"
									readonly="readonly"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].evaluatedPrecision"
									value="<s:property value="evaluatedPrecision"/>"
									title='<s:property value="evaluatedPrecision"/>' />
							</s:if> <s:else>
								<input class="col" type="text" style="width: 50px;"
									id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>Precition"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].evaluatedPrecision"
									value="<s:property value="evaluatedPrecision"/>"
									title='<s:property value="evaluatedPrecision"/>' />

							</s:else></td>
							<td><input class="col" type="hidden" style="width: 50px;"
								id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>baseScale"
								name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].baseScale"
								value="<s:property value="baseScale"/>" /> <s:if
								test='subListForEvaluatedColumsInfo[#even_odd.index].evaluatedDataType.value=="DATE" ||subListForEvaluatedColumsInfo[#even_odd.index].evaluatedDataType.value=="DATETIME" ||subListForEvaluatedColumsInfo[#even_odd.index].evaluatedDataType.value=="TIME"'>
								<input class="col" type="text" style="width: 50px;"
									id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>Scale"
									readonly="readonly"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].evaluatedScale"
									value="<s:property value="evaluatedScale"/>"
									title='<s:property value="evaluatedScale"/>' />
							</s:if> <s:else>
								<input class="col" type="text" style="width: 50px;"
									id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>Scale"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].evaluatedScale"
									value="<s:property value="evaluatedScale"/>"
									title='<s:property value="evaluatedScale"/>' />
							</s:else></td>
							<td id="entityDataTypeTr<s:property value='#even_odd.index'/>">
							<s:select
								cssClass="textBox1"
								id="subListForEvaluatedColumsInfo%{#even_odd.index}kdxDataType"
								name="subListForEvaluatedColumsInfo[%{#even_odd.index}].kdxDataType"
								cssStyle="width:100px;" list='columnTypes'
								listValue="description" onchange="javascript:handleCorrespondingDefaultMetricAndGrain('%{#even_odd.index}',this.value)" /></td>
							<td id="unitTypeTd<s:property value='#even_odd.index'/>"><s:select
								cssClass="textBox1"
								id="subListForEvaluatedColumsInfo%{#even_odd.index}unitType"
								name="subListForEvaluatedColumsInfo[%{#even_odd.index}].unitType"
								cssStyle="width:80px;" list='conversionTypes'
								listValue="description"
								onchange="getCorrespondingFormatsAndUnits('%{#even_odd.index}',this.value);" />
							</td>

							<td id="formatTd<s:property value='#even_odd.index'/>">
							<div style="display: inline; white-space: nowrap;"><span
								id="format<s:property value="#even_odd.index"/>"> <s:select
								cssClass="textBox1"
								id="subListForEvaluatedColumsInfo%{#even_odd.index}Format"
								name="subListForEvaluatedColumsInfo[%{#even_odd.index}].format"
								list="%{#evaluatedSubCol.qiConversion.dataFormats}"
								listKey="name" listValue="displayName"></s:select> </span></div>
							</td>
							<td id="unitTd<s:property value='#even_odd.index'/>">
							<div style="display: inline; white-space: nowrap;"><span
								id="unit<s:property value="#even_odd.index"/>"> <s:select
								cssClass="textBox1"
								id="subListForEvaluatedColumsInfo%{#even_odd.index}Unit"
								name="subListForEvaluatedColumsInfo[%{#even_odd.index}].unit"
								list="%{#evaluatedSubCol.qiConversion.units}" listKey="name"
								listValue="displayName"></s:select> </span></div>
							</td>
							<td id="granularityTd<s:property value='#even_odd.index'/>">
							<s:if
								test='subListForEvaluatedColumsInfo[#even_odd.index].kdxDataType.value=="DIMENSION" || subListForEvaluatedColumsInfo[#even_odd.index].kdxDataType.value=="ID"'>
								<s:select cssClass="textBox1"
									id="subListForEvaluatedColumsInfo%{#even_odd.index}granularity"
									name="subListForEvaluatedColumsInfo[%{#even_odd.index}].granularity"
									cssStyle="width:60px;" list='granularityTypes' listKey="value"
									listValue="description" />
							</s:if> <s:else>
								<s:select cssClass="textBox1"
									id="subListForEvaluatedColumsInfo%{#even_odd.index}granularity"
									name="subListForEvaluatedColumsInfo[%{#even_odd.index}].granularity"
									cssStyle="width:60px;" list='granularityTypes' listKey="value"
									listValue="description" disabled="true" />
							</s:else></td>
							<td id="defaultMetricTd<s:property value='#even_odd.index'/>"><s:if
								test='subListForEvaluatedColumsInfo[#even_odd.index].kdxDataType.value=="MEASURE"'>
								<s:if test="subListForEvaluatedColumsInfo[#even_odd.index].defaultMetric.value=='Y'">
								  <input type="checkbox"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].defaultMetric"
									id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>defaultMetric"
									value="<%=CheckType.YES%>" checked="checked">
								</s:if>
								<s:else>
								  <input type="checkbox"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].defaultMetric"
									id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>defaultMetric"
									value="<%=CheckType.YES%>">
								</s:else>
								
							</s:if> <s:else>
								<input type="checkbox"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].defaultMetric"
									id="subListForEvaluatedColumsInfo<s:property value="#even_odd.index"/>defaultMetric"
									value="<%=CheckType.YES%>" disabled="disabled">
							</s:else></td>


							<!-- Hidden filed for the population and distributions -->
							<s:if test="%{#evaluatedSubCol.isDistribution.value=='Y'}">
								<input type="checkbox" style="display: none"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isDistribution"
									id="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isDistribution"
									value="YES" checked="checked">
							</s:if>
							<s:else>
								<input type="checkbox" style="display: none"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isDistribution"
									id="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isDistribution">
							</s:else>

							<s:if test="%{#evaluatedSubCol.isPopulation.value=='Y'}">
								<input type="checkbox" style="display: none"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isPopulation"
									id="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isPopulation"
									value="YES" checked="checked">
							</s:if>
							<s:else>
								<input type="checkbox" style="display: none"
									name="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isPopulation"
									id="subListForEvaluatedColumsInfo[<s:property value="#even_odd.index"/>].isPopulation">
							</s:else>

						</tr>
						<script>
				        hideColumnDetail('<s:property value="#even_odd.index"/>');
				        function hideColumnDetail(id){
				            if("NO"==$("#datasetCollectionCreationId").val()){
				            	//Hide DatasetCollection creation related fields		
							  $("#entityDataTypeTr"+id).hide();							  
							  $("#unitTypeTd"+id).hide();
							  $("#formatTd"+id).hide();
							  $("#unitTd"+id).hide();
							  $("#granularityTd"+id).hide();
							  $("#defaultMetricTd"+id).hide();	
							  	//Hide header					  
							  $("#entityDataTypeHeader").hide();
						      $("#dataTypeHeader").hide();
						      $("#formatHeader").hide();
						      $("#unitHeader").hide();
						      $("#granularityHeader").hide();
						      $("#defaultMetricHeader").hide();						      						 
							}
				        }				       
				      </script>
					</s:iterator>
				</tbody>
			</table>
			</td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>


	</table>
	<table align="left" cellpadding="0" cellspacing="0" border="0">
		<tr>
			<td><span id="confirmButton"><input type="button"
				class="buttonSize160"
				value="<s:text name='execue.global.confirmColumnDetails' />"
				onclick="javascript:updateEvaluateColumnTable();" /></span> <span
				id="confirmButtonLoader" style="display: none;"><input
				type="button" disabled="disabled" class="buttonLoaderSize160"
				value="<s:text name='execue.global.confirmColumnDetails' />"></span></td>
		</tr>
	</table>

	<table align="center">
		<tr>
			<td>
			<div id="paginationDiv2"><pg:paginateResults /></div>
			</td>
		</tr>
	</table>
	<s:hidden id="fileTableInfoId" name="fileTableInfoId" />

</s:form></div>


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
				//console.log("value changed");
				sameData=false;
			}
		);
	}
</script>

