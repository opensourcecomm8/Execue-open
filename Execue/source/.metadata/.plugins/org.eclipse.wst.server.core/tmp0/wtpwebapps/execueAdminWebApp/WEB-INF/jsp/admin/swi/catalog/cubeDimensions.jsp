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
                <tr><td><jsp:include page='/views/admin/showSelectAsset.jsp' flush="true" /></td></tr>
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
                        <jsp:include page="cubeDimensionsInner.jsp"></jsp:include>
					
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
<s:hidden id="assetId" name="baseAsset.id" />

