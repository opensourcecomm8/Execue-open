<%@taglib uri="/WEB-INF/tlds/adminConsole.tld" prefix="admin"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.execue.web.core.util.ExecueWebConstants"%>

<%
String mainPath= (String) application.getAttribute(ExecueWebConstants.MAIN_CONTEXT)+"/index.jsp";
%>

<script language="JavaScript1.2" type="text/javascript">
<!--
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}
function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}

function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}

//-->
$(document).ready(function(){
						   
	$pathName=window.location.pathname;
	
	homePage="showConsole.action";
	///Data Sources ////
	action1="showDataSources.action";
	action1a="showDataSource.action";
	action2="showAssets.action";
	action2a="showAsset.action";
  actionDA3="showConstraints.action";
  actionDA4="showJoins.action";
	///Data Sources ////
	
	///Business Terminology ////
	action3="showBusinessTerms.action";
	action4="showProfiles.action";
	action5="showRanges.action";
	///Business Terminology ////
	
	///mapping////
	action6="showAssetSelectionForMappings.action";
	action6a="showMappings.action";
  actionMP6="showAssetsGrain";
	///mapping////
	
	///security////
	action7="showRoles.action";
	action8="showGroups.action";
	action9="showUsers.action";
	///security////
	
	if($pathName.indexOf(homePage)>-1)$("#bi_home").attr("src","../images/admin/publisherHome_hover.jpg");
	if(($pathName.indexOf(action1)>-1)||($pathName.indexOf(action1a)>-1)||($pathName.indexOf(action2)>-1)||($pathName.indexOf(action2a)>-1)||($pathName.indexOf(actionDA3)>-1)||($pathName.indexOf(actionDA4)>-1))$("#bi_ds").attr("src","../images/bi_ds_f2.jpg");
	if(($pathName.indexOf(action3)>-1))$("#bi_bt").attr("src","../images/admin/pub/searchView_hover.jpg");
	if(($pathName.indexOf(action6)>-1)||($pathName.indexOf(action6a)>-1)||($pathName.indexOf(actionMP6)>-1))$("#bi_mapping").attr("src","../images/bi_mapping_f2.jpg");
	if(($pathName.indexOf(action7)>-1)||($pathName.indexOf(action8)>-1)||($pathName.indexOf(action9)>-1))$("#bi_system").attr("src","../images/admin/pub/adminSecurity_hover.jpg");
	if(($pathName.indexOf(action4)>-1)||($pathName.indexOf(action5)>-1))$("#bi_da").attr("src","../images/admin/pub/preferences_hover.jpg");
  
	});


</script>

<table width="1164" border="0" align="center" cellpadding="0"  cellspacing="0">
  <tr>
    <td height="18" align="left" style="padding-left:1px;" ><admin:showBreadcrumb homeName="Semantifi" homeLink="<%=mainPath %>" appDisplayName="App" assetDisplayName="Dataset Collection"/></td>
  </tr>
</table>
