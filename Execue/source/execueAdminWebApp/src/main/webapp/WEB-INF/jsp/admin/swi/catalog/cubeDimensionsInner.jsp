<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<div id="container"
	style="width: 100%; height: 365px; margin: auto; border-top: #CCC dashed 1px; border-bottom: #ccc dashed 1px;">
<!-- manually attach allowOverflow method to pane --> <!-- allowOverflow auto-attached by option: west__showOverflowOnHover = true -->
<div class="ui-layout-west"
	style="overflow-x: hidden; padding-left: 0px;">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td height="27"><span class="tableSubHeading"><s:text
			name="execue.cubeCreation.dimension.selection.availableDimensions.heading" /></span></td>
	</tr>
	<tr>
		<td width="200">

		<div class="tableBorder"
			style="padding-top: 5px; height: 305px; border: none;">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="30" align="left" valign="top">
				<div id="divDimensions"></div>
				</td>
			</tr>
			<tr>
				<td>

				<div id="dynamicPaneCubeConcepts" style="background:no-repeat url(../images/admin/loading.gif) center center;height:225px;width:190px;border:1px solid #ccc;"></div>

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

<div class="ui-layout-east"
	style="overflow-x: hidden; padding-left: 0px;">
<div id="rightPane"></div>
</div>
</div>
<div  id="showSnapshotLink" style="float:right;">
<span id="selectDimensionErrorMessage" style="float:left; color:red"></span>
<input type="button" class="buttonSize51" style="margin:3px;cursor:pointer;"  value="<s:text name='execue.global.next' />" value="Next" onclick="javascript:showSnapShot();"/></div>
<div style="display: none;float:right;" id="loadingShowSnapshotLink"><img height="16" src="../images/admin/loadingWhite.gif"></div>
  
</body>
</html>
<s:hidden id="existingAssetId" name="existingAssetId"/>
<script type="text/javascript">
 
 

var selectAssetId =$("#selectAssetId").val();
if(selectAssetId == 0){
 $("#showSnapshotLink").hide();
}

function showSnapShot(){
	$("#showSnapshotLink").hide();
     $("#selectDimensionErrorMessage").empty().fadeIn('fast');
	$("#loadingShowSnapshotLink").show();
    var rowCount =$("#searchList input[type='checkbox']").length; 	
	if(rowCount >0){
	   location.href='showSnapshot.action?existingAssetId='+$("#existingAssetId").val()+'&baseAsset.id='+$("#assetId").val();
	}else{
	  $("#selectDimensionErrorMessage").empty().append( 'Please add atleast one dimension');	  
	  setTimeout(function() {
          $('#selectDimensionErrorMessage').fadeOut('fast');
      }, 2000);
	}
	 $("#loadingShowSnapshotLink").hide();
	 $("#showSnapshotLink").show();
}

</script>
