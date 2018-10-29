<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
					name="execue.cubeCreation.dimension.selection.heading" /></td>
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
                <tr><td><jsp:include page='/views/showSelectAsset.jsp' flush="true" /></td></tr>
  <tr>
    <td  width="82%" style="padding-top:5px;padding-bottom:5px;">  
     <table width="98%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="1000%"><s:text name="execue.cubeCreation.dimension.selection.description" /></td>
                    
                  </tr>
                </table></td>
     <!-- <td width="20%"><a href="#" onclick="MM_openBrWindow('text.jsp','','width=600,height=400')">cube.request.status</a></td>-->
    <!-- <td width="18%" style="padding-left:10px;" align="right"><a href="showCubeStatus.action"><s:text name="cube.request.status"/></a>-->
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

				<table width="100%" border="0" align="center" cellpadding="0"
					cellspacing="0" id="contentTable" >
					<tr>
						<td>


						<div id="container"
							style="width: 100%; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
						<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
						<div class="ui-layout-west" style="overflow-x: hidden;padding-left:0px;">

						<table width="100%" border="0" cellspacing="0" cellpadding="0">
							<tr>
								<td height="27"><span class="tableSubHeading"><s:text
									name="execue.cubeCreation.dimension.selection.availableDimensions.heading" /></span></td>
							</tr>
							<tr>
								<td width="200">

								<div class="tableBorder"
									style="padding-top: 5px; height: 305px;border:none;">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
    <td height="30"  align="left" valign="top"><div id="divDimensions">
										
										</div>
</td>
  </tr>
									<tr>
										<td>
                                        
                                        <div id="dynamicPaneCubeConcepts"></div>
										
										</td>
									</tr>
								</table>

								</div>
								</td>
							</tr>
						</table>



						</div>
						<div class="ui-layout-center">
						<div id="dynamicPane"></div>
						</div>

						<div class="ui-layout-east" style="overflow-x: hidden;padding-left:0px;">
						<div id="rightPane"></div>
						</div>
						</div>
						<div  id="nextButton" style="float:right;">
                         <input type="button" class="buttonSize51" style="margin:3px;cursor:pointer;"  value="<s:text name='execue.global.next' />" value="Next" onclick="location.href='showSnapshot.action?baseAsset.id=<s:property value="baseAsset.id"/>'"/></div></td>
					</tr>

				</table>
				</td>
			</tr>
		</table>
		</div>
		</td>
	</tr>
</table>

<s:hidden id="assetId" name="baseAsset.id" />
