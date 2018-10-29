<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>   
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>semantifi : Browse all Apps </title>
<meta name="robots" content="index,follow" />
<meta name="keywords" content=" search databases, search datasets, search secfilings, meaning based search, index datasets, index databases, Search Results for datasets, business intelligence, bi tools, BI installations for their enterprise clients , bi solutions, datafeeds , individual investors to research equities, automated software, appliance for business intelligence, fully automated, search based BI solution, searching multiple databases,  data cubes , simple search interface, immediate results from databases , using a catalog of cubes , data marts , Integrating your data sources , Semantifi Enterprise , simple process, works with most databases, cubes and data marts, Semantic search engine,  driven by knowledge base , relevant answers, Automatic data summaries , visualizations , interpret database results, Index multi-terabyte databases, fast results, Crawl ,  database , compose complex queries, build data search apps, search data , search unstructured content, searches documents , highly relevant search results , natural language processing , NLP,  search engines, deep web," />

<meta name="description" content="deep web, search databases, search datasets, search secfilings, meaning based search, index datasets, index databases, Search Results for datasets, Publish Apps. Search Datasets. Share Results. Build Apps to search any dataset using semantic search technology hosted here. Build Apps of personal interest, share apps publicly, within social networks or to paid-subscribers, You can apply Semantifi technology on enterprise data assets that include relational databases, data warehouses (DW), OLAP cubes, and datamarts. Semantifi is the solution for all your business intelligence needs, datafeeds for individual investors to research equities, automated software appliance for business intelligence, fully automated search based BI solution, searching multiple databases and data cubes via a simple search interface, immediate results from databases using a catalog of cubes and data marts , Integrating your data sources into Semantifi Enterprise is a simple process, It works with most databases, cubes and data marts, Semantic search engine driven by knowledge base for relevant answers, Automatic data summaries & visualizations to interpret database results, Index multi-terabyte databases for fast results, Crawl any type of database to compose complex queries, marketplace for a community of publishers to build data search apps for all users to search, meaning based search platform to search both structured data and unstructured content, searches both databases and documents to deliver highly relevant search results compared to keyword or natural language processing (NLP) search engines, deep web"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link id="page_favicon" href="favicon.ico" rel="icon" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<TITLE><tiles:getAsString name="title"/></TITLE>
<link href="../css/xmlTree.css" type="text/css" rel="stylesheet">
<script src="../js/xmlTree.js" type="text/javascript"></script>
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
<style>
body,html{
height:100%;	
}
</style>
<BODY id="execueBody">


<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
  <tr height="50">
    <td height="50" valign="top" bgcolor="#FFFFFF">
    
    <table border="0" cellpadding="0" cellspacing="0" width="100%">
    <!-- fwtable fwsrc="index.png" fwpage="Page 1" fwbase="index.jpg" fwstyle="Dreamweaver" fwdocid = "738978224" fwnested="1" -->
    <tr>
      <td ><tiles:insertAttribute name="header" /></td>
    </tr>

  </table>
  
  
  </td>
  </tr>
  <tr>
    <td  id="contentTd" valign="top" height="90%">
    
    <table width="1155" border="0" align="center" cellpadding="0" cellspacing="0">
  
  <tr>
    
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

</BODY>
</HTML>