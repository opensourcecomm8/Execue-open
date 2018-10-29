<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>   
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<tiles:insertAttribute name="title" />
<meta name="robots" content="index,follow" />
<meta name="keywords" content="<s:property value='uiApplicationInfo.applicationTag'/>">
<meta name="description" content="<s:property value='uiApplicationInfo.applicationDescription'/>">
<jsp:include page="../WEB-INF/jsp/main/selectCss.jsp" />

<head>
<%
response.setHeader("Pragma", "No-cache");
response.setDateHeader("Expires", 0);
response.setHeader("Cache-Control", "no-cache");
response.setHeader("Cache-Control","no-store"); 
response.addHeader("Cache-Control", "post-check=0, pre-check=0");
response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<meta http-equiv="X-UA-Compatible" content="IE=6;" />

<link id="page_favicon" href="favicon.ico" rel="icon" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />



<style type="text/css">
a{
white-space:normal;	
}
p{
text-align:inherit;	
}
a.linkWhite{
color:#FFF;	
text-decoration:none;
}
a.linkWhite:hover{
color:#FFF;	
text-decoration:underline;
}

.appsExamplesLinks{
margin-bottom:20px;	
}


</style>
<LINK rel=stylesheet type=text/css href="<c:out value="${basePath}"/>/css/vertical/news-tab.css">

<tiles:insertAttribute name="head" />
<tiles:insertAttribute name="apphead" />
<link href="<c:out value="${basePath}"/>/css/main/semantifi.css" rel="stylesheet" type="text/css" />
<body>
<tiles:insertAttribute name="header" />
<table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">

  <tr>
    <td class="center-bg1" >
    
    <div id="center-content" style="height:auto;">
      <table  border="0" align="center" cellpadding="0" cellspacing="0" id="outerTable">
        <tr>
         
        <td valign="top" >
          <table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="main-box">
          
            <tr>
             <td valign="top" width="15"  >
             
            
             <div class="centerBg" style="background:url('<c:out value="${basePath}"/>/images/quinnox/leftBg.png') repeat-y ; height:635px;width:15px;margin-top:40px;" ><img src="<c:out value="${basePath}"/>/images/quinnox/leftBg.png" /></div>
          </td>
              <td valign="top" bgcolor="#FFFFFF" class="box1" style="padding-left:10px;"><!-- Box1 Content -->
                
                  
                    <tiles:insertAttribute name="leftPanel" />
                    
                      
               
               </td>
               
               
               
                <td valign="top" width="15" bgcolor="#FFFFFF" style="border-right:1px solid #ccc"  >
             
             <div style="background:url('<c:out value="${basePath}"/>/images/quinnox/lineTop.png') repeat-y ; height:100px;width:15px;" >&nbsp;</div>
             <div class="centerLineDiv" style="background:url('<c:out value="${basePath}"/>/images/quinnox/lineMiddle.png') repeat-y ; height:100px;width:15px;" >&nbsp;</div>
              <div  style="background:url('<c:out value="${basePath}"/>/images/quinnox/lineBottom.png') repeat-y ; height:175px;width:15px;" >&nbsp;</div>
          </td>
          
          
              <td  valign="top" bgcolor="#FFFFFF" class="box2" style="padding-left:10px;">
                
                 <tiles:insertAttribute name="middlePanel" />
                </td>
              <td  valign="top" bgcolor="#FFFFFF" class="box3">
               <tiles:insertAttribute name="rightPanel" />
              </td>
                <td width="15" valign="top"  >
             <div class="centerBg" style="background:url('<c:out value="${basePath}"/>/images/quinnox/rightBg.png') repeat-y ; height:635px;width:15px;margin-top:40px;" ><img src="<c:out value="${basePath}"/>/images/quinnox/leftBg.png" /></div>
            </td>
              </tr>
              
                <tr>
          <td colspan="6" height="50" valign="top" class="home-page-bg-bottom">&nbsp;</td>
        </tr>
        
            </table>
            
            </td>
          
        </tr>
      
        </table>
    </div></td>
  </tr>
  <tr>

  </tr>
  <tr>
    <td><tiles:insertAttribute name="footer" /></td>
  </tr>
  <tr><td align="center"><img src="<c:out value="${basePath}"/>/images/main/1pix.jpg" width="1" height="1" id="img1"  /></td></tr>
</table>


</body>
</html>

<script>
$(function(){
//alert($("table.main-box").height());
setShadedHeights();
		   });
function setShadedHeights(){
	$(".centerBg").height($("table.main-box").height());	
		  
$(".centerLineDiv").height($("table.main-box").height()-330);	

setTimeout(function(){$(".centerBg").height($("table.main-box").height()); },3000);	
}
</script>

