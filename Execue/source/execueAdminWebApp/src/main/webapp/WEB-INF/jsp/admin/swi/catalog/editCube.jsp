<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<script type="text/javascript">
<!--
function MM_openBrWindow(theURL,winName,features) { //v2.0
  window.open(theURL,winName,features);
}
//-->
</script>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text
					name="execue.cube.mangeCubes.updateCube" /></td>
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
				<td valign="top" >
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                
  <tr>
    <td  width="82%" style="padding-top:5px;padding-bottom:5px;">  
     <table width="98%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="1000%"><s:text name="execue.editCube.description" /></td>
                    
                  </tr>
                </table></td>   
 </td> 
  </tr>
</table>
</td>
			</tr>
			<!-- tr>
          <td valign="top" background="images/blueLine.jpg"><img src="images/blueLine.jpg" width="10" height="1" /></td>
          </tr -->
			<tr>
				<td valign="top">

				<table width="100%" border="0" align="center" cellpadding="5"
					cellspacing="0" id="contentTable" >
                    <tr>
				<td valign="top" >
                <div style="background:url(../images/admin/blueLine.jpg);width:100%;height:1px;"></div></td>
			</tr>
                    <tr>
								<td ><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><span class="tableSubHeading"><s:text name="execue.cubeCreation.snapshot"></s:text> </span></td>
    <td align="right"><a href="manageCubes.action"><s:text name="execue.global.link.back"></s:text></a></td>
  </tr>
</table>
</td>
								
							</tr>
                <tr><td>
                
                <div style="overflow: auto; height: auto; width: 760px; left: 50px">
								 <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="50%"><table width="100%" border="0" cellspacing="0" cellpadding="4">
      <tr>
        <td width="22%"><strong><s:text name="execue.cubeCreation.snapshot.asset"/></>: </strong></td>
        <td width="78%"><s:property value="cubeCreationContext.targetAsset.name" /></td>
      </tr>
      <tr>
			
					<td ><strong><s:text name="execue.cube.selected.simple.lookup"></s:text> :</strong></td>
					<td >
                            <s:iterator value="cubeCreationContext.simpleLookupDimensions" status="cubeStatus">
					<s:property/> <s:if test="cubeCreationContext.simpleLookupDimensions.size > (#cubeStatus.index+1)">,</s:if>
				</s:iterator>
					</td>
				</tr>
				
			
      
				<tr>
					<td ><strong><s:text name="execue.cube.selected.range.lookup" ></s:text> :</strong></td>
					<td ><s:iterator value="cubeCreationContext.rangeLookupDimensions" status="lookup">
					<s:property value="dimensionName" /> <s:if test="cubeCreationContext.rangeLookupDimensions.size > (#lookup.index+1)">,</s:if>
				</s:iterator></td>
				
				
			
		</tr>
		
        <td><strong><s:text name="execue.cube.frequesncy.measures"></s:text> :</strong></td>
        <td><s:iterator value="cubeCreationContext.frequencyMeasures" status="frequencyMeasures" >
				<s:property />
			</s:iterator> <s:if test="cubeCreationContext.frequencyMeasures.size > (#frequencyMeasures.index+1)">,</s:if></td>
      </tr>
    </table></td>
	    
  </tr>
</table>
								</div>
                
                </td></tr>
					<tr>
						<td> <!-- commented jsp include cubeDimensionsInner.jsp -->
                      
                            <table width="20%" border="0" cellspacing="5" cellpadding="0">
                              <tr>
                                <td><input class="buttonSize80" name="Refresh" type="button" value="Refresh" id="refreshCube" onclick="javascript:refreshCube();"/></td>
                                <td><input class="buttonSize80" name="Update" type="button" value="Update" id="updateCube" onclick="javascript:updateCube();" /></td> 
                                <td style="white-space: nowrap;"><div  id="actionMessage" style="color: green"/>
                                <div  id="errorMessage" style="color: red"/></td>                              
                              </tr>                             
                              <tr>
                                <td colspan="2" style="padding-top:10px;"><s:text name="execue.catalog.cube.update"></s:text> </td>                                
                              </tr>
                              <tr>
                                <td colspan="2"><s:text name="execue.catalog.cube.refresh"></s:text></td>                                
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
</table>
<s:hidden id="existingAssetId" name="existingAssetId" />
<script type="text/javascript">
function updateCube(){
	$("#updateCube").removeClass("buttonSize80");		
	$("#updateCube").addClass("buttonLoaderSize80"); 
	var existingAssetId= $("#existingAssetId").val();	
	$.getJSON("updateCube.action?existingAssetId="+existingAssetId,function(data) {	
	  if(data.status){
	    $("#actionMessage").empty().append("Cube update request is in process. Please check the <a href='getJobStatus.action?jobRequest.id="+data.jobRequestId+"'>Status </a>");	
	    $('#updateCube').removeAttr('onclick'); 
	    $('#refreshCube').removeAttr('onclick');      
	  }else{
	     showErrorMessages(data.errorMessages);
	  }
	  $("#updateCube").removeClass("buttonLoaderSize80"); 
	  $("#updateCube").addClass("buttonSize80");
	});
}

function refreshCube(){
	$("#refreshCube").removeClass("buttonSize80");		
	$("#refreshCube").addClass("buttonLoaderSize80"); 
	var existingAssetId= $("#existingAssetId").val();	
	$.getJSON("refreshCube.action?existingAssetId="+existingAssetId,function(data) {	
	  if(data.status){
	    $("#actionMessage").empty().append("Cube refresh is in process. Please check the <a href='getJobStatus.action?jobRequest.id="+data.jobRequestId+"'>Status </a>");	
	    $('#refreshCube').removeAttr('onclick');  
	    $('#updateCube').removeAttr('onclick');     
	  }else{
	   showErrorMessages(data.errorMessages);
	  }
	  $("#refreshCube").removeClass("buttonLoaderSize80"); 
	  $("#refreshCube").addClass("buttonSize80");	
	});
}
function showErrorMessages(errorMessages){
    $.each(errorMessages, function () {
			$("#errorMessage").empty().append("<li>"+this+"<\li>");	     
      });
}
</script>
