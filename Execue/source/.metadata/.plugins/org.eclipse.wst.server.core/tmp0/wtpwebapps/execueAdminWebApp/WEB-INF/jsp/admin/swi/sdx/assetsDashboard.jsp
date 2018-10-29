<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
td.heading {
	background-image: url(../images/admin/rowBg.jpg);
	font-size: 11px;
	padding-left: 5px;
}

#tableGridMemberInfo td,.tableGridMemberInfo td {
	border: 1px solid #D3D3D3;
	font-size: 11px;
	line-height: 20px;
	padding-bottom: 0;
	padding-left: 5px;
	padding-top: 0;
	text-align: left;
	height: 22px;
}

tr .ui-state-highlight {
	border: 1px solid #ccc;
	bacground: none repeat scroll 0 0 transparent;
	color: #363636;
}
</style>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><!--s:text  name="publishInfo.publish.heading" /--><s:text
					name='execue.assetDashboard.DatasetsDashboard' /></td>					
			</tr>
			<tr>
				<td><div id="message" style="display:none"></div> </td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder"
			style="min-height: 400px; height: auto; margin-bottom: 20px; padding-bottom: 15px;padding-top:15px;">
		<table width="96%" border="0" align="center" cellpadding="5"
			cellspacing="0" style="white-space: nowrap;">

			<!-- Start file upload dash Board -->
			<tr>
				<td valign="top">
				<table width="100%" cellspacing="0" cellpadding="0" border="0"
					align="center">
					<tr>
						<td>
						<div id="dashBoardGrid"></div>
						</td>
					</tr>
					<tr>
						<td valign="top">
						<table width="100%" cellspacing="0" cellpadding="0" border="0"
							align="center">
							<tr>
								<td>
								<table width="70%" border="0" cellspacing="0" cellpadding="0"
									style="margin-top: 15px;">
									<tr>
										<td width="71%" height="26" style="padding-left: 2px;"><span
											class="descriptionText"> <s:if
											test="dataSources.size > 0">
											<s:text name='execue.assetDashboard.numberOfConnections' />
											<s:property value="dataSources.size" />
										</s:if> <s:else>
											<s:text name='execue.assetDashboard.noConnectionsAvailable' />
										</s:else> </span></td>
										<td width="29%" align="right">&nbsp;</td>
									</tr>
								</table>
								</td>
							</tr>
							<tr>
								<td><s:if test="dataSources.size > 0">
									<table width="100%" cellspacing="0" cellpadding="0" border="0">
										<tr>
											<td>

											<div style="width: 80%; min-height: 40px; height: auto;">

											<table width="100%" cellspacing="1" cellpadding="0"
												border="0" id="tableGridMemberInfo">

												<tr class="tableGridTitle">
													<td width="30%" height="25" class="heading"><strong><s:text
														name='execue.assetDashboard.datasetConnectionName' /></strong></td>
													<td width="70%" class="heading"><strong><s:text
														name='execue.assetDashboard.datasetConnectionDesc' /></strong></td>
												</tr>

												<s:iterator value="dataSources">
													<tr>
														<td width="30%"><A
															href="../swi/showDataSource.action?dataSource.name=<s:property value="name"/>"><s:property
															value="displayName" /></A></td>
														<td width="70%"><s:property value="description" /></td>
													</tr>
												</s:iterator>
											</table>
											</div>
											</td>
										</tr>
									</table>
								</s:if></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
	<!-- End file upload dash Board -->
</table>
<SCRIPT type=text/javascript>
$(document).ready(function() {
	generateGrid();
 });
function generateGrid(){
$grid=$('<TABLE id="grid" style="font-weight: normal;"></TABLE>');
$page=$('<DIV id=pager></DIV>');
$("#dashBoardGrid").empty().append($grid).append($page);
$grid.jqGrid({
	  url: 'datasetList.action',
      datatype: 'json',
      mtype: 'GET',
	  width: 740,
	  height: 'auto',
	  rowNum: 10,
      rowList: [10,20,30],
      pager: $('#pager'),
      sortname: 'fileName',
      viewrecords: true,
	  gridview: true,
      colNames:[ '<s:text name="execue.upload.main.file.name"/>','<s:text name="execue.upload.main.file.description"/>','<s:text name="execue.upload.main.file.type"/>','<s:text name="execue.upload.main.file.eligible.absorption"/>','<s:text name="execue.upload.main.file.delete.link"/>'],
	  colModel:[
	  			{name:'fileName',width:80,search:false},
	  			{name:'fileDescription',width:120,search:false,sortable:false},
				{name:'sourceType',width:50,sortable:true,sorttype:"text", search:true, stype:'text'},
				{name:'fileId',width:100,sortable:false, search:false,formatter: showLoader},
				{name:'fileId',width:100,sortable:false, search:false,formatter: showDeleteLoader}],
	jsonReader: {
		root: "rows",
		page: "page",
     	total: "total",
     	records: "records",		
	    repeatitems: false
	},
	"prmNames":{"page":"requestedPage","rows":"pageSize","sort":"sortField","order":"sortOrder","id":"id"},
	"pager":"#pager"});
	$('#grid').jqGrid('navGrid','#pager',
					{"edit":false,"add":false,"del":false,"search":false,"refresh":false,"view":false,"excel":false}, 		   // options
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // edit
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // add
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},
					{"drag":true,"closeAfterSearch":true, sopt: ['eq','bw','ew','cn'],sOper:"searchType","closeOnEscape":true,"dataheight":150})   // del
}
function showLoader(cellvalue, options, rowObject){
	$mainDiv = $("<div></div>");
	if(rowObject.eligibleForAbsorption){
		$rowData = $("<div style='width:30px;float:left;white-space:nowrap' id='showStatus"+rowObject.fileId+"'></div>");
		$mainDiv.append($rowData);
		if(rowObject.currentOperation == 'FILE_TRANSFER'){
		   $rowData.append("<a href='javascript:createSource("+rowObject.fileId+");'>"+
			  "<s:text name='execue.assetDashboard.uploadFile.analyse'/></a>");
		}
		else if(rowObject.currentOperationStatus == 'PENDING' || rowObject.currentOperationStatus == 'INPROGRESS'){
		    $rowData.append("<a href='javascript:showStatus(\""+rowObject.fileId+"\",\""+rowObject.jobRequestId+"\");'>");
		    if(rowObject.currentOperation == 'ABSORPTION_AND_DATA_ANALYSIS')
	           $rowData.append("<s:text name='execue.assetDashboard.uploadFile.analysis.inprocess' />");
	        else
	          $rowData.append("<s:text name='execue.assetDashboard.uploadFile.enable.inprocess' />");
	        
	        $rowData.append("</a>");
		}
		else{
		  	 $rowData.append("<a href='../publisher/showPublishedFileTables.action?publishedFileId="+rowObject.fileId+"&jobRequestId="+rowObject.jobRequestId+"'><s:text name='execue.assetDashboard.uploadFile.confirm'/></a>");
		}												
		$mainDiv.append("<div id='"+rowObject.fileId+"Div' style='position:absolute;z-index:100000;display:none;float:left;min-width:240px;width:auto;'></div>");
	}
	else{
		$rowData = $("<div id='showStatus"+rowObject.fileId+"Link'></div>");
		$mainDiv.append($rowData);
		if(rowObject.fileAbsorbed == 'YES')
		   $rowData.append("<s:text name='execue.assetDashboard.uploadFile.enabled'/>");
		else
		    $rowData.append("<s:text name='execue.assetDashboard.uploadFile.unsupported'/>");
	}
	return $mainDiv.html();
}
function showDeleteLoader(cellvalue, options, rowObject){
	$mainDiv = $("<div></div>");
	if(rowObject.eligibleForAbsorption){
		$rowData = $("<div style='width:30px;float:left;white-space:nowrap' id='showStatus"+rowObject.fileId+"'></div>");
		$mainDiv.append($rowData);
		$rowData.append("<a href='javascript:deleteDatasetCollection("+rowObject.fileId+")';><img height='20' width='25' border='0' title='Delete' alt='Delete' src='../images/admin/disabledIcon.gif'></a>");			  
	    $mainDiv.append("<div id='"+rowObject.fileId+"Div' style='position:absolute;z-index:100000;display:none;float:left;min-width:240px;width:auto;'></div>");	
	}else{
		$rowData = $("<div id='showStatus"+rowObject.fileId+"Link'></div>");
		$mainDiv.append($rowData);
		if(rowObject.fileAbsorbed == 'YES'){
		    if(rowObject.datasetCollectionCreation=='NO'){
		      $rowData.append("<a href='javascript:deleteDatasetCollection("+rowObject.fileId+")';><img height='20' width='25' border='0' title='Delete' alt='Delete' src='../images/admin/disabledIcon.gif'></a>");			  
			  $mainDiv.append("<div id='"+rowObject.fileId+"Div' style='position:absolute;z-index:100000;display:none;float:left;min-width:240px;width:auto;'></div>");
		    }else{
		      $rowData.append("<img height='20' width='25' border='0' title='Unsupported Operation' alt='Unsupported Operation' style='opacity:0.2' src='../images/admin/disabledIcon.gif'>");
		    }		    
		 }
	}		
	
	return $mainDiv.html();
}
</SCRIPT>