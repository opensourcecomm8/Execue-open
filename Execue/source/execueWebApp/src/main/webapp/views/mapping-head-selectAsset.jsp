<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<%@page import="com.execue.core.common.bean.ApplicationContext"%>
<script src="../js/SpryTabbedPanels.js" type="text/javascript"></script>
<script language="JavaScript" src="../js/jquery.js"></script>
<script language="JavaScript" src="../js/menun.js"></script>
<script language="JavaScript" src="../js/mm_menu.js"></script>
<script type="text/javascript" src="../js/jquery.ui.all.js"></script>
<script type="text/javascript" src="../js/thickbox.js"></script>
<script language="JavaScript" src="../js/jquery.execue.js"></script>
<script language="JavaScript" type="text/javascript"
	src="../js/mapping.js"></script>
<SCRIPT src="../js/jquery.autocomplete.js" type=text/javascript></SCRIPT>
<SCRIPT src="../js/common.js" type=text/javascript></SCRIPT>
<link href="../css/treeDrag.css" rel="stylesheet" type="text/css">

<link href="../css/SpryTabbedPanels.css" rel="stylesheet"
	type="text/css">
<link rel="stylesheet" href="../css/thickbox.css" type="text/css"
	media="screen" />
<link href="../css/styles.css" rel="stylesheet" type="text/css">
<!--link href="css/ui-layout-styles.css" rel="stylesheet" type="text/css"-->
<LINK href="../css/roundedSearch.css" rel=stylesheet>
<style type="text/css">
.ac_input {
	width: 200px;
}

.ac_results {
	width: 200px;
	background: #eee;
	cursor: pointer;
	position: absolute;
	left: 0;
	font-size: 90%;
	z-index: 101;
}

.ac_results ul {
	width: 194px;
	list-style-position: outside;
	list-style: none;
	padding: 0;
	margin: 0;
	border: 1px solid #000;
	background-color: #FFF;
}

.ac_results iframe {
	display: none; /*sorry for IE5*/
	display /**/: block; /*sorry for IE5*/
	position: absolute;
	top: 0;
	left: 0;
	z-index: -1;
	filter: mask();
	width: 3000px;
	height: 3000px;
}

.ac_results li {
	width: 100%;
	padding: 3px 0px 3px;
}

.ac_results a {
	width: 100%;
}

.ac_loading {
	background: url('../images/loaderTrans.gif') right center no-repeat;
}

.ac_over {
	background: #8CA0FD;
}
</style>
<script type="text/javascript"> 
$(document).ready(function () {	   
  var srcName=document.getElementById("srcName").value;
  var assetId='';
 var clickedMenuItem= $("#clickedMenuItem").val();
  assetId='<s:property value="applicationContext.assetId"/>';   
   if(assetId ==''){
     if(srcName=="cubeRequest"){     
       tb_show("Show Dataset Collection ","../swi/showAllParentAssets.action?sourceName="+srcName+"&clickedMenuItem="+clickedMenuItem+"&keepThis=true&TB_iframe=true&height=50&width=50 class=thickbox title=Dataset Selection");
     }else{       
        tb_show("Show Dataset Collection ","../swi/showAllAssets.action?sourceName="+srcName+"&clickedMenuItem="+clickedMenuItem+"&keepThis=true&TB_iframe=true&height=50&width=50 class=thickbox title=Dataset Selection");
     }
   }else{  
     if(srcName=="cubeRequest"){     
       window.location="../swi/showAllParentAssets.action?sourceName="+srcName+"&clickedMenuItem="+clickedMenuItem;
     }else{    
        window.location="../swi/showAllAssets.action?sourceName="+srcName+"&clickedMenuItem="+clickedMenuItem;
     }
   }   
	 //tb_show("Show Datasets ","../views/showAssets.jsp?keepThis=true&amp;TB_iframe=true&amp;height=260&amp;width=360 class=thickbox title=Dataset Selection");
 });
</script>
<s:hidden name="sourceName" id="srcName" />
<s:hidden name="clickedMenuItem" id="clickedMenuItem" />
