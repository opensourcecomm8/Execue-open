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
	///Data Sources ////
	
	///Business Terminology ////
	action3="showBusinessTerms.action";
	action4="showProfiles.action";
	action5="showRanges.action";
	///Business Terminology ////
	
	///mapping////
	action6="showAssetSelectionForMappings.action";
	action6a="showMappings.action";
	///mapping////
	
	///security////
	action7="showRoles.action";
	action8="showGroups.action";
	action9="showUsers.action";
	///security////
	
	if($pathName.indexOf(homePage)>-1)$("#bi_home").attr("src","../images/bi_home_f2.jpg");
	if(($pathName.indexOf(action1)>-1)||($pathName.indexOf(action1a)>-1)||($pathName.indexOf(action2)>-1)||($pathName.indexOf(action2a)>-1))$("#bi_ds").attr("src","../images/bi_ds_f2.jpg");
	if(($pathName.indexOf(action3)>-1)||($pathName.indexOf(action4)>-1)||($pathName.indexOf(action5)>-1))$("#bi_bt").attr("src","../images/bi_bt_f2.jpg");
	if(($pathName.indexOf(action6)>-1)||($pathName.indexOf(action6a)>-1))$("#bi_mapping").attr("src","../images/bi_mapping_f2.jpg");
	if(($pathName.indexOf(action7)>-1)||($pathName.indexOf(action8)>-1)||($pathName.indexOf(action9)>-1))$("#bi_security").attr("src","../images/bi_security_f2.jpg");
	
	});
</script>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img name="index_r2_c1" src="../images/blueband.jpg" width="1000" height="15" border="0" id="index_r2_c1" alt="" /></td>
  </tr>
  <tr>
    <td><table align="left" border="0" cellpadding="0" cellspacing="0" width="1000">
	  <tr>
	   <td><img name="index_r3_c1" src="../images/leftband.jpg" width="308" height="24" border="0" id="index_r3_c1" alt="" /></td>
	   <td><img name="index_r3_c3" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c3" alt="" /></td>
	   <td><a href="../swi/showConsole.action" tabindex="-1"><img name="bi_home" src="../images/bi_home.jpg" width="59" height="24" border="0" id="bi_home" alt="" /></a></td>
	   <td><img name="index_r3_c5" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c5" alt="" /></td>
	   <td><a tabindex="-1" href="javascript:;" onMouseOver="MM_swapImage('bi_ds','','../images/bi_ds_f2.jpg',1);MM_showMenu(window.mm_menu_0105220345_0,0,26,null,'bi_ds')" onMouseOut="MM_swapImgRestore();MM_startTimeout();"><img name="bi_ds" src="../images/bi_ds.jpg" width="90" height="24" border="0" id="bi_ds" alt="" /></a></td>
	   <td><img name="index_r3_c7" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c7" alt="" /></td>
	   <td><a tabindex="-1" href="javascript:;" onMouseOver="MM_swapImage('bi_bt','','../images/bi_bt_f2.jpg',1);MM_showMenu(window.mm_menu_0113133940_0,0,25,null,'bi_bt')" onMouseOut="MM_swapImgRestore();MM_startTimeout();"><img name="bi_bt" src="../images/bi_bt.jpg" width="130" height="24" border="0" id="bi_bt" alt="" /></a></td>
	   <td><img name="index_r3_c9" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c9" alt="" /></td>
	  <!--<td><a tabindex="-1" href="../swi/showMappings.action?domainId=1&assetId=13" onMouseOver="MM_swapImage('bi_mapping','','../images/bi_mapping_f2.jpg',1);MM_showMenu(window.mm_menu_0113135351_0,0,25,null,'bi_mapping')" onMouseOut="MM_swapImgRestore();MM_startTimeout();"><img name="bi_mapping" src="../images/bi_mapping.jpg" width="62" height="24" border="0" id="bi_mapping" alt="" /></a></td>-->
	   <td><a tabindex="-1" href="../swi/showAssetSelectionForMappings.action?sourceName=mapping" onMouseOver="MM_swapImage('bi_mapping','','../images/bi_mapping_f2.jpg',1);MM_showMenu(window.mm_menu_0113135351_0,0,25,null,'bi_mapping')" onMouseOut="MM_swapImgRestore();MM_startTimeout();"><img name="bi_mapping" src="../images/bi_mapping.jpg" width="62" height="24" border="0" id="bi_mapping" alt="" /></a></td>
	   <td><img name="index_r3_c11" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c11" alt="" /></td>
	   <td><a tabindex="-1" href="javascript:;" onMouseOver="MM_swapImage('bi_da','','../images/bi_da_f2.jpg',1);MM_showMenu(window.mm_menu_0113135822_0,0,25,null,'bi_da')" onMouseOut="MM_swapImgRestore();MM_startTimeout();"><img name="bi_da" src="../images/bi_da.jpg" width="76" height="24" border="0" id="bi_da" alt="" /></a></td>
	   <td><img name="index_r3_c13" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c13" alt="" /></td>
	   <td><a tabindex="-1" href="javascript:;" onMouseOver="MM_swapImage('bi_security','','../images/bi_security_f2.jpg',1);MM_showMenu(window.mm_menu_0113140100_0,0,25,null,'bi_security')" onMouseOut="MM_swapImgRestore();MM_startTimeout();"><img name="bi_security" src="../images/bi_security.jpg" width="60" height="24" border="0" id="bi_security" alt="" /></a></td>
	   <td><img name="index_r3_c15" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c15" alt="" /></td>
	   <td><a tabindex="-1" href="javascript:;" onMouseOver="MM_swapImage('bi_system','','../images/bi_system_f2.jpg',1);MM_showMenu(window.mm_menu_0113140235_0,0,25,null,'bi_system')" onMouseOut="MM_swapImgRestore();MM_startTimeout();"><img name="bi_system" src="../images/bi_system.jpg" width="59" height="24" border="0" id="bi_system" alt="" /></a></td>
	   <td><img name="index_r3_c17" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c17" alt="" /></td>
	   <td><a tabindex="-1" href="javascript:;"  onmouseout="MM_swapImgRestore();" onMouseOver="MM_swapImage('bi_help','','../images/bi_help_f2.jpg',1);" ><img name="bi_help" src="../images/bi_help.jpg" width="61" height="24" border="0" id="bi_help" alt="" /></a></td>
	   <td><img name="index_r3_c19" src="../images/1pix.jpg" width="1" height="24" border="0" id="index_r3_c19" alt="" /></td>
	   <td><img name="index_r3_c20" src="../images/rightband.jpg" width="86" height="24" border="0" id="index_r3_c20" alt="" /></td>
	  </tr>
	</table></td>
  </tr>
</table>