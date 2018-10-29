<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>   
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<TITLE><tiles:getAsString name="title"/></TITLE>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<link href="../css/common/xmlTree.css" type="text/css" rel="stylesheet">
<script src="../js/common/xmlTree.js" type="text/javascript"></script>
<tiles:insertAttribute name="head" />
<%
response.setHeader("Pragma", "No-cache");
response.setDateHeader("Expires", 0);
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache-Control","no-store"); 
response.addHeader("Cache-Control", "post-check=0, pre-check=0");
response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" /></HEAD>
<meta name="robots" content="index,follow" />
<style>
body,html{
height:100%;	
}
.dynamicPaneBgLoader{
background-image:	url('<c:out value="${basePath}"/>/images/uss/Loader.gif');
background-repeat:no-repeat;
background-position:center ;
}
.dynamicPaneBgNoLoader{
background-image:	none;
}
</style>

<BODY id="execueBody">

<table width="100%" border="0" cellspacing="0" cellpadding="0" height="101%">
  <tr height="50">
    <td height="50" valign="top" bgcolor="#FFFFFF">
    
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <!-- fwtable fwsrc="index.png" fwpage="Page 1" fwbase="index.jpg" fwstyle="Dreamweaver" fwdocid = "738978224" fwnested="1" -->
    <tr>
      <td ><tiles:insertAttribute name="header" /></td>
    </tr>
    <tr>
      <td bgcolor="#FFFFFF"> <tiles:insertAttribute name="menu" /></td>
    </tr>
  </table>
  
  
  </td>
  </tr>
  <tr>
    <td  id="contentTd" valign="top" height="90%">
    
    <table width="1155" border="0" align="center" cellpadding="0" cellspacing="0">
  
  <tr>
    <td width="15%" align="left" valign="top" ><tiles:insertAttribute name="leftMenu" /></td>
    <td  align="left" valign="top"  width="85%" ><tiles:insertAttribute name="body" /></td>
  </tr>
</table>
</td>
  </tr>
  <tr>
    <td valign="bottom"><DIV id=footer>
  <tiles:insertAttribute name="footer" />
</DIV></td>
  </tr>
</table>



<map name="index_r2_c1Map">
  <area shape="rect" coords="282,14,349,35" href="#" alt="Home">
  <area shape="rect" coords="374,16,455,34" href="#" onMouseOver="MM_showMenu(window.mm_menu_0105220345_0,370,40,null,'index_r2_c1')" onMouseOut="MM_startTimeout();">
  <area shape="rect" coords="464,17,587,35" href="#" onMouseOver="MM_showMenu(window.mm_menu_0113133940_0,460,40,null,'index_r2_c1')" onMouseOut="MM_startTimeout();">
  <area shape="rect" coords="598,18,650,36" href="#" onMouseOver="MM_showMenu(window.mm_menu_0113135351_0,590,40,null,'index_r2_c1')" onMouseOut="MM_startTimeout();">
  <area shape="rect" coords="657,17,727,35" href="#" onMouseOver="MM_showMenu(window.mm_menu_0113135822_0,653,40,null,'index_r2_c1')" onMouseOut="MM_startTimeout();">
  <area shape="rect" coords="735,17,790,34" href="#" onMouseOver="MM_showMenu(window.mm_menu_0113140100_0,730,40,null,'index_r2_c1')" onMouseOut="MM_startTimeout();">
  <area shape="rect" coords="795,17,849,33" href="#" onMouseOver="MM_showMenu(window.mm_menu_0113140235_0,791,40,null,'index_r2_c1')" onMouseOut="MM_startTimeout();">
  <area shape="rect" coords="857,18,911,33" href="#">
</map>

</BODY>
</HTML>