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
					name="execue.mart.editMart" /></td>
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
                    <td width="1000%"><s:text name="execue.mart.editMart.description" /></td>
                    
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
    <td><span class="tableSubHeading"><s:text name="execue.mart.editMart.snapshot"></s:text> </span></td>
    <td align="right"><a href="manageMarts.action"><s:text name="execue.global.link.back"></s:text></a></td>
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
        <td width="78%"><s:property value="martCreationContext.targetAsset.name" /></td>
      </tr>
      
       <tr>
        <td width="22%"><strong><s:text name="execue.mart.editMart.population"></s:text> : </strong></td>
        <td width="78%"><s:property value="martCreationContext.population" /></td>
      </tr>
           <tr>
				<td ><strong><s:text name="execue.mart.editMart.distributions"></s:text> :</strong></td>
				 <td>
                   <s:iterator value="martCreationContext.distributions" status="status">
					 <s:property/> <s:if test="martCreationContext.status.size > (#status.index+1)">,</s:if>
				   </s:iterator>
				 </td>
			</tr>
			<tr>
				<td ><strong><s:text name="execue.mart.editMart.prominentMeasures"></s:text> :</strong></td>
				 <td>
                   <s:iterator value="martCreationContext.prominentMeasures" status="status">
					 <s:property/> <s:if test="martCreationContext.status.size > (#status.index+1)">,</s:if>
				   </s:iterator>
				 </td>
			 </tr>
			 <tr>
				<td ><strong><s:text name="execue.mart.editMart.prominentDimensions"></s:text> :</strong></td>
				 <td>
                   <s:iterator value="martCreationContext.prominentDimensions" status="status">
					 <s:property/> <s:if test="martCreationContext.status.size > (#status.index+1)">,</s:if>
				   </s:iterator>
				 </td>
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
                                <td><input class="buttonSize80" name="Refresh" type="button" value="Refresh" id="refreshMart"  onclick="javascript:refreshMart();"/></td>
                                <!-- <td><input class="buttonSize80" name="Update" type="button" value="Update" id="updateMart" onclick="javascript:updateMart();/></td> -->
                                <td style="white-space: nowrap;"><div  id="actionMessage" style="color: green">
                                <div  id="errorMessage" style="color: red"></div></td>                              
                              </tr>
                             
                             <!--  <tr>
                                <td colspan="2" style="padding-top:10px;"><s:text name="execue.catalog.mart.update"></s:text> </td>
                                
                              </tr>-->
                              <tr>
                                <td colspan="2"><s:text name="execue.catalog.mart.refresh"></s:text></td>
                                
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
function refreshMart(){
	$("#refreshMart").removeClass("buttonSize80");		
	$("#refreshMart").addClass("buttonLoaderSize80"); 
	var existingAssetId= $("#existingAssetId").val();	
	$.getJSON("refreshMart.action?existingAssetId="+existingAssetId,function(data) {	
	 if(data.status){
	    $("#actionMessage").empty().append("Mart refresh request is in process. Please check the <a href='getJobStatus.action?jobRequest.id="+data.jobRequestId+"'>Status </a>");	
	    $('#refreshMart').removeAttr('onclick');  
	    $('#updateMart').removeAttr('onclick');  
	  }else{
	    showErrorMessages(data.errorMessages);
	  }
	  $("#refreshMart").removeClass("buttonLoaderSize80"); 
	  $("#refreshMart").addClass("buttonSize80");
	});
}

function updateMart(){
	$("#updateMart").removeClass("buttonSize80");		
	$("#updateMart").addClass("buttonLoaderSize80"); 
	var existingAssetId= $("#existingAssetId").val();	
	$.getJSON("updateMart.action?existingAssetId="+existingAssetId,function(data) {	
	  if(data.status){
	   $("#actionMessage").empty().append("Mart update request is in process. Please check the <a href='getJobStatus.action?jobRequest.id="+data.jobRequestId+"'>Status </a>");	
	   $('#refreshMart').removeAttr('onclick');  
	   $('#updateMart').removeAttr('onclick');  
	  }else{
	    showErrorMessages(data.errorMessages);
	  }
	  $("#updateMart").removeClass("buttonLoaderSize80"); 
	  $("#updateMart").addClass("buttonSize80");	
	});
}

function showErrorMessages(errorMessages){
    $.each(errorMessages, function () {
			$("#errorMessage").empty().append("<li>"+this+"<\li>");	    
      });
}
</script>
