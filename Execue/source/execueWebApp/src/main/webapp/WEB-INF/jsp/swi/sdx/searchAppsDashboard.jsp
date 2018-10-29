<%@ taglib prefix="s" uri="/struts-tags"%>

<style type="text/css">
<!--
td.heading {
	background-image: url(../images/rowBg.jpg);
	font-size: 11px;
	padding-left: 5px;
}

span.red {
	color: red;
	font-size: 13px;
	font-weight: bold;
}
select{
width:40px;
}
.imgFade{
opacity: .50;
filter:Alpha(Opacity=50); 	
}
.imgDontFade{
opacity: 1;
filter:Alpha(Opacity=100); 	
}
-->
</style>

<table width="100%" border="0" cellspacing="0" cellpadding="0" >
  <tr>
    <td class="uploadOuterBg"><div  id="uploadFileInfoOuterDiv" style="display:none;">
    <div class="uploadTopBox" >
                                        
                                        <div style="margin:auto;text-align:center;"> <span class="active"><s:text name="execue.global.locate" /></span> <span class="arrow-active"> >></span><span class="no-active"><s:text name="execue.global.describe" /></span> <span class="arrow-no-active">>></span> <span class="no-active"><s:text name="execue.global.publish" /></span></div>
</div>
                                        
                                       
<div id="uploadFileInfo" class="uploadFileMainBox" >
</div>	
                                        
</div></td>
  </tr>
</table>


                                            
                                            

<div id="searchAppDashboardDiv">                                        
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.search.apps.dashboard" /></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder" style="min-height: 330px; height: auto;">
		<table width="99%" border="0" align="center" cellpadding="10"
			cellspacing="0">
			<tr>
				<td valign="top">

				<table width="100%" cellspacing="0" cellpadding="0" border="0"
					align="center">
					<tr>
						<td>

						<table width="100%" border="0" cellspacing="0" cellpadding="0"
							style="white-space: nowrap;">
							<tr>
								<td>
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
								    <s:if test="menuSelection.menuType.value != 1">
									<tr>
										<td align="left" height="22" style="white-space: nowrap;"><a
											style="font-size: 14px" href="showApplications.action"><s:text
											name="execue.create.app" /></a></td>
									</tr>
									</s:if> 
									<s:if test="menuSelection.menuType.value == 1">
									<tr>
										<td align="center" style="padding-bottom: 5px;">
										<div style="width:955px;"> 
                                       
                                        <div style="width:830px;float:left;text-align:left;margin-top:3px;margin-bottom:3px;">
                                        <a id="createAppFromFileLink" href="#" onclick="javascript:createAppFromFile();" style="font-size: 14px;"><s:text name="execue.upload.main.app"/> </a>
                                        </div>
                                        

                                        
											</div>
											</td>
									</tr>
									</s:if>
									<tr>
								</table>
								</td>
								<td>&nbsp;</td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td width="50%" align="center" valign="top">
						<div style="padding-top: 5px;padding-bottom: 5px;display:none;" id="appSelectedMessageDiv"></div>
						<s:if test="selectApp">
							<div id="pleaseSelectApplication" style="padding-bottom:5px;"><s:text
								name="execue.select.application" /></div>
						</s:if>
					</tr>
					<tr>
						<td>
						<TABLE id="grid" style="font-weight: normal;"></TABLE>
						<DIV id=pager></DIV>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
<map name="Map" id="Map">
	<area shape="circle" coords="121,119,53"
		href="../publisher/showUploadDataset.action?show=upload" alt="Upload"
		title="Upload" />
	<area shape="circle" coords="122,248,55"
		href="../publisher/showUploadDataset.action?show=connect"
		alt="Connect" title="Connect" />
	<area shape="rect" coords="256,70,494,117"
		href="../swi/showApplications.action" alt="App" title="App" />
	<area shape="rect" coords="257,130,492,172"
		href="../swi/showAsset.action" alt="Metadata" title="Metadata" />
	<area shape="rect" coords="258,190,494,240"
		href="../swi/showJoins.action" alt="Joins" title="Joins" />
	<area shape="rect" coords="256,251,496,299"
		href="../swi/showMappings.action" alt="Mapping" title="Mapping" />
	<area shape="rect" coords="578,77,698,301"
		href="../swi/showPublishDatasets.action" alt="Publish" title="Publish" />
</map>
<SCRIPT type=text/javascript>
var applIdFromSession = '<s:property value="applicationContext.appId"/>';
var appSize=Number("<s:property value='applications.size' />");
var isPublisher='<s:property value="isPublisher"/>';
       
$(document).ready(function() {
$('#diagramImage,#diagramHeading').click(function(){
var src=null;
$obj=null;
if($(this).attr('id')=="diagramImage"){
src=$(this).find('img').attr('src');
$obj=$(this);
}else{
src=$(this).prev('div').find('img').attr('src');
$obj=$(this).prev('div');
}
/*if(src=="../images/btn-open.gif"){
  $('#flowDiagramDiv').slideDown(); 
  $obj.find('img').attr('src','../images/btn-close.gif'); 
  $('#diagramHeading').html('Hide Flow Diagram');
} else{
  $('#flowDiagramDiv').slideUp(); 
  $obj.find('img').attr('src','../images/btn-open.gif'); 
  $('#diagramHeading').html('Show Flow Diagram');
}*/

});
  if(appSize>0){
    /*
  	if(isPublisher=="NO"){
	  $('#flowDiagramDiv').show();
	}else{
	  $('#flowDiagramDiv').hide();
	}	
	$('#diagramHeading').show();
	$('#diagramImage').show();
	*/
	$('#grid').jqGrid({
	  url: 'applicationList.action',
      datatype: 'json',
      mtype: 'GET',
	  width: 950,
	  height: 270,
	  rowNum: 10,
	  scroll:1,
      rowList: [10,20,30],
      pager: $('#pager'),
      sortname: 'name',
      viewrecords: true,
	  gridview: true,
	  colNames:[ 'Select App','ModelId','<s:text name="execue.app.name"/>','<s:text name="execue.app.description"/>','Meta Data','<s:text name="execue.publish.mode"/>','<s:text name="status"/>'],
	  colModel:[
	  			{name:"id",index:'id',width:50,hidden:true,formatter:showRadio, search:false,sortable:false},
	  			{name:"modelId",index:'modelId',hidden:true,search:false,sortable:false},
				{name:"name",index:'name',width:140,sortable:true,sorttype:"text", search:true, stype:'text', 
						formatter:showRadio, formatoptions:{baseLinkUrl:'../swi/showApplications.action', idName:'application.id'}},
				{name:"desc",width:230,sortable:false, search:false,formatter:showShortDesc},	
				{name:"metadataLink",width:40,sortable:false, search:false,formatter:showMetadataLink},	
				{name:"mode",width:60,sortable:true,sorttype:"text", search:false,formatter:showMode},
				{name:"status",width:90, sortable:true, search:false,formatter:showStatus}],
	jsonReader: {
	    repeatitems: false
	},
	prmNames:{page:"requestedPage",rows:"pageSize",sort:"sortField",order:"sortOrder",id:"id",nd:null,search:null},
	pager:"#pager"});
	
	$('#grid').jqGrid('navGrid','#pager',
					{"edit":false,"add":false,"del":false,"search":true,"refresh":true,"view":false,"excel":false}, 		   // options
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // edit
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // add
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // del
					{"drag":true,"closeAfterSearch":true, sopt: ['cn','bw','ew','eq'],sOper:"searchType","closeOnEscape":true,"dataheight":150}) // search
			.navSeparatorAdd("#pager",{})
			.navButtonAdd('#pager',{
					caption:"Delete", 
   					buttonicon:"ui-icon-trash", 
   					onClickButton: function(){
   						if($("input[name='applicationId']:checked").val() > 0){
   							deleteApp("Are you sure you want to delete ?",$("input[name='applicationId']:checked").val());
   						} else {
   							alert("Please select a record to delete");
   						}
   					}, 
   					position:"last"
					}); 
			
							$("#fbox_grid input[type='text']").live("keydown",function(e){ 
							if(e.keyCode==13 || e.which==13){
							$("#fbox_grid .ui-search").click();	
							}
											 });
		}else{
		  // $('#flowDiagramDiv').show();
		}
		$("input[name='applicationId']").live("click",function(){ 
															   
			var applId=$(this).attr("applId");	
			var appName=$(this).attr("appName");
			var modId=$(this).attr("modId");
			var appSrcType=$(this).attr("appSrcType");
			 setSelected(applId, appName, modId, appSrcType); });
 });
var sDesc="shortDesc";
var lDescClose="longDescClose";
var currentStatus="";
var currentDiv="";

function showMode(cellvalue, options, rowObject){
var $metaDiv = $("<div ></div>");
	var divId="mode"+rowObject.id;
	var $innerDiv = $("<div id='"+divId+"' ></div>");
	
var $a=$("<a href='../swi/showPublishApp.action?applicationId="+rowObject.id+"'></a>"); 
	if(cellvalue!="None"){
	$innerDiv.append($a);	$a.append(cellvalue);
	}else{
	$innerDiv.append(cellvalue);	
	}
	
	$metaDiv.append($innerDiv);
return $metaDiv.html();	
}
function showMetadataLink(cellvalue, options, rowObject){
	var $metaDiv = $("<div ></div>");
	var divId="meta"+rowObject.id;
	var $innerDiv = $("<div id='"+divId+"' ></div>");
	var $a=$("<a href='../publisher/editMetadata.action?applicationId="+rowObject.id+"'></a>"); 
	var $editIcon=$('<img height="17" border="0" style="margin-left:15px;" title="Edit" alt="Edit" src="../images/editIcon.gif">');
	$a.append($editIcon);
	if(cellvalue){ 
		$innerDiv.append($a);
	}else{
		$editIcon.attr("src","../images/editIconfaded.gif");
		$innerDiv.append($editIcon);
		
	}
	
	$metaDiv.append($innerDiv);
	return $metaDiv.html();
}
function showShortDesc(cellvalue, options, rowObject){
	var $descDiv = $("<div ></div>");
	if(cellvalue!=null && cellvalue!=""){
	var $shortDescDiv = $("<div name='shortDesc' ></div>");
	var $longDescDiv = $("<div name='longDesc' style='display:none;z-index:inherit;position:absolute;width:400px;height:60px;overflow-y:auto;min-height:50px;border:2px solid #ccc;background-color:#CFDDDD;color:#000;white-space:normal;padding:5px;' ></div>");
	var $longDescContent= $("<div style='margin-right:20px;' ></div>");
	var $longDescContentCloseButton= $("<div style='width:16px;float:right;color:#fff;font-weight:bold' ></div>");
	$longDescContentCloseButton.append("<a style='clocr:#fff;font-weight:bold;' id='"+lDescClose+rowObject.id+"' href=javascript:closeDesc('"+rowObject.id+"');><img border='0' src='../images/closeButtonForMessage.png' /></a>");
	$descDiv.append($shortDescDiv);
	$descDiv.append($longDescDiv);
	var shortDesc=cellvalue.substring(0,60);
	
	if(cellvalue.length>60){
		
		shortDesc=cellvalue.substring(0,60)+"...<a id='"+sDesc+rowObject.id+"' href=javascript:showDesc('"+rowObject.id+"');>More</a>";
		$longDescContent.append(cellvalue);
		$longDescDiv.append($longDescContentCloseButton);
		$longDescDiv.append($longDescContent);
	}
	$shortDescDiv.append(shortDesc);
	}
	return $descDiv.html();
}
function closeDesc(id){
$longDescDiv=$("#"+sDesc+id).parent().next();	
$longDescDiv.hide();
}
function showDesc(id){
	$("div[name='longDesc']").hide();
$longDescDiv=$("#"+sDesc+id).parent().next();
$longDescDiv.show();
top=$("#"+id).position().top; 
//gridTop=$("#gbox_grid").position().top;
//alert(top+"::"+gridTop);
if(top<=90){
$longDescDiv.css("top",top+"px");
}else{
	$longDescDiv.css("top",top-40+"px");
}
}
function showRadio(cellvalue, options, rowObject){
	$radioDiv = $("<div ></div>");
	$table=$("<table border='0'></table>");
	$tr=$("<tr></tr>");
	$table.append($tr);
	$radioSelectDiv = $("<td style='width:20px; float:left; margin-left:2px;border:none;padding-top:5px;height:15px;'></td>");
	if(applIdFromSession == rowObject.id)
		$radioSelect = $("<input type='radio' checked  name='applicationId' value='"+rowObject.id+"' applId='"+rowObject.id+"'  appName='"+rowObject.name+"' modId='"+rowObject.modelId+"' appSrcType='"+rowObject.appSourceType+"' />");	
	else
		$radioSelect = $("<input type='radio'  name='applicationId' value='"+rowObject.id+"' applId='"+rowObject.id+"'  appName='"+rowObject.name+"' modId='"+rowObject.modelId+"' appSrcType='"+rowObject.appSourceType+"' />");	
	$radioSelectDiv.append($radioSelect);
	$radioDataDiv = $("<td style='float:left;border:none;padding-top:5px;height:15px;'></td>");
	$rowData = $("<a href='../swi/showApplications.action?application.id="+rowObject.id+"'>"+cellvalue+"</a>");
	$radioDataDiv.append($rowData);
	$radioDiv.append($table);
	$tr.append($radioSelectDiv);
	$tr.append($radioDataDiv);

	return $radioDiv.html();
}
function showDelStatus(cellvalue, options, rowObject){
	//alert(rowObject.statusLink);
	$mainDiv = $("<div>");
	$rowData = $("<div style='width:150px; float:left; white-space:normal;text' id='showStatus"+rowObject.id+"'></div>");
	
	$statusDiv = $("<div style='width:30px; float:left; margin-left:5px;' id='"+rowObject.id+"Div'>");
	$statusDiv.append(rowObject.status);
	$mainDiv.append($statusDiv);
	$mainDiv.append($rowData);
	return $mainDiv.html();
}

function showStatus(cellvalue, options, rowObject){
	var $mainDiv = $("<div ></div>");
	var $outerDiv = $("<div style='position:relative' ></div>");
	var $innerDiv = $("<div ></div>");
	var $a=$('<a href="#" ></a>');
	var operationType="";
	var jobrequestId=100;
	var $statusDiv = $("<div style='width:30px; float:left; margin-left:5px;' id='"+rowObject.id+"Div'>");
	$a.append(rowObject.status);
	if(rowObject.statusLink){ 
		$mainDiv.append($statusDiv);
		$statusDiv.append($a);
		if(!rowObject.inProgress){ 
		operationType=rowObject.operationType; //un comment when values are coming
		jobrequestId=rowObject.jobRequestId; //un comment when values are coming
		$a.attr("href","test.action? operationType="+ operationType+"&jobRequestId="+jobrequestId);
		}else{
			jobrequestId=rowObject.jobRequestId; //un comment when values are coming
			$innerDiv.append($a);
			$innerDiv.attr("id","status"+rowObject.id);
			$outerDiv.append($innerDiv);
			$mainDiv.append($outerDiv);
			var divId="status"+rowObject.id; 
			
			$a.attr("href","javascript:showJobstatus('"+jobrequestId+"');");
			currentStatus=$mainDiv.html(); 
			currentDiv=rowObject.id;
		}
		
	}else{
		$mainDiv.append($statusDiv.append(rowObject.status));
		
	}
	$rowData = $("<div style='width:150px; float:left; white-space:normal;text' id='showStatus"+rowObject.id+"'></div>");
	$mainDiv.append($rowData);
	return $mainDiv.html();
}

function showJobstatus(jobRequestId){

$("#status"+currentDiv).empty();	
$("#status"+currentDiv).jobStatus({
		//requestJobURL : "../swi/invokePublishAssetsMaintenaceJob.action",
		jobId:jobRequestId,
		postCall : function(jobRequestId,status) {updateStatusInfo(jobRequestId,status)}
	});	
}


function updateStatusInfo(jobid,status) {
	// specifc code for showing specific
 
	if(status=="SUCCESS"){
	var $statusDiv= $("#status"+currentDiv).css("textAlign","left").css("marginLeft","5px");
	var $modeDiv= $("#mode"+currentDiv);
	var $metaDiv= $("#meta"+currentDiv);
	var $aMeta=$("<a href='../publisher/editMetadata.action?applicationId="+currentDiv+"'></a>"); 
	var $editIcon=$('<img height="17" border="0" style="margin-left:15px;" title="Edit" alt="Edit" src="../images/editIcon.gif">');
	
	$aMeta.append($editIcon);
	$metaDiv.empty().html($aMeta);
	$metaDiv.children("a img").removeClass("imgFade").addClass("imgDontFade");
	
	var $aMode=$("<a href='../swi/showPublishApp.action?applicationId="+currentDiv+"'>Local</a>"); 
	$modeDiv.empty().html($aMode);
	
	$statusDiv.show();
	$statusDiv.html("Fulfilled");
	$metaDiv.html();
	}else{ 
	$("#status"+currentDiv).html(currentStatus);
	}
	//$("#uploadAnotherFile").show().css({marginLeft:"0px"});
}
 </SCRIPT>
<BR>