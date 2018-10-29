<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE>Execue Lite</TITLE>
<META http-equiv=Content-Type content="text/html; charset=utf-8">
<LINK title=Limpid href="/" rel=author>
<LINK title="Anne van Kesteren" href="http://annevankesteren.nl/"
	rel=author>
<LINK title="Theory of fixed positioning" href="/lab/css/fixed/theory"
	rel=help>
<STYLE type=text/css>
BODY {
	PADDING-RIGHT: 0px;
	PADDING-LEFT: 0px;
	PADDING-BOTTOM: 25px;
	MARGIN: 0px;
	PADDING-TOP: 100px;
	background-color: #FFF;
}

DIV#header {
	LEFT: 0px;
	WIDTH: 100%;
	POSITION: absolute;
	TOP: 0px;
	HEIGHT: 100px;
	background-color: #FFF;
}

DIV#footer {
	LEFT: 0px;
	WIDTH: 100%;
	BOTTOM: 0px;
	POSITION: absolute;
	HEIGHT: 25px;
	background-color: #FFF;
}

@media Screen {
	BODY>DIV#header {
		POSITION: fixed
	}
	BODY>DIV#footer {
		POSITION: fixed
	}
}

* HTML BODY {
	OVERFLOW: hidden
}

* HTML DIV#content {
	OVERFLOW: auto;
	HEIGHT: 100%
}
.technology-1 {
	background-image: url(<c:out value="${basePath}"/>/images/main/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
}

.top-bg {
	background-image: url(<c:out value="${basePath}"/>/images/main/header_top.png);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 24px;
	
	background-color: #FBFBFB;
}
</STYLE>
<META content="MSHTML 6.00.6000.16788" name=GENERATOR>
<link href="<c:out value="${basePath}"/>/css/common/styles.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY onLoad="document.Login.j_username.focus();" style="background-color:#FFF">
<table width="100%" border="0" cellspacing="0" cellpadding="2" height="100%">
  <tr>
    <td>
<table width="100%" cellpadding="0" cellspacing="0" border="0" >
  <tr>
    <td class="top-bg">

<div style="margin:auto;width:91%">
                
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                    
                      <td width="100%" align="center">
                      
                      
                      <table width="100%" border="0" cellspacing="0" cellpadding="3" align="right">
                          <tr>
                            <td width="41%" height="20" align="right"><!-- sub links starts -->
                              <!--div id="pleaseWaitDiv" style="display: none; margin: auto; width: 133px; margin-top: 1px; margin-bottom: 0px;height:10px;float:right;"> <img id="waiting_img" src="images/wait1e.gif"  /> </div-->
                              <div style="height:16px;padding-top:2px;z-index:20;position:absolute;" id="waitImage"><div id="pleaseWaitDiv" style="display: none; margin: auto; width: 110px;">
								<img id="waiting_img" src="<c:out value="${basePath}"/>images/main/Loader-main-page-3.gif" width="22" height="22" />
							</div>
                        </div></td>
                          
                                    
                                    
                                   
                            
                           
                                    <td width="45%" align="center"><div id="showWelcome" style="padding-right:20px;color:#3F5356;font-size:14px;padding-top:0px;padding-right:18px;font-family:'Trebuchet MS', Arial, Helvetica, sans-serif;text-align:right;width:200px;margin:auto;">
   </div></td>
                          <td width="19%" align="right"><!-- <a href="<c:out value="${basePath}"/>/feedback.jsp">Feedback</a>--></td><td width="1%" style="font-size:11px;"></td> 
                            <td width="6%" align="left" >
                            
                            <!--span id="showLoginLink" style="padding-left:3px;"><a href="javascript:;" class="links_sem3"
   id="loginId" ><s:text name="login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img
   src="images/loaderTrans.gif" ></span-->
                            
                            </td>
                           
                          </tr>
                        </table>
                        
                        
                        </td>
                    </tr>
                </table>
                
                </div>


</td>
</tr>
  
  
  <tr >
    <td class="technology-1" height="42" >
    <div style="margin:auto;width:91%">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="8%" valign="top"><div style="padding-top:4px;padding-left:0px;padding-right:0px;padding-bottom:1px;"><a href="index.jsp"><img name="index_r1_c1" src="<c:out value="${basePath}"/>/images/main/inner-page-logo.png" border="0"
					id="index_r1_c1" alt="" /></a></div></td>
    <td width="92%" align="left"> <!--jsp:include page="freeFormSearch.jsp"  flush="true" /--></td>
  </tr>
</table>
</div>

      </td>
  </tr>
  
  <tr >
    <td  bgcolor="#FFFFFF" >
    
     <div style="width:90%;margin:auto;">

<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr >
		<td height="25"  class="normalText" background="images/main/bc-bg.jpg" style="font-family:Arial, Helvetica, sans-serif;font-size:12px;" >
		<span style="padding-left:1px;color:#3F5356;">
                <a href="<c:out value="${basePath}"/>/index.jsp"	 class="breadCrumbLink">Home</a><Span class="textArrow"> >> </Span> <span class="breadCrumbNoLink">Access Denied</span>
		</span></td>
	</tr>
	
</table>

</div>
  
      </td>
  </tr>
</table>
</td>
  </tr>
  <tr>
    <td>

<table width="90%" height="100%" border="0" align="center" cellpadding="0" cellspacing="0"> 
  <tr>
    <td height="500"  valign="top" class="fontRed"><c:if test="${not empty param.ajax}">
No Access </c:if>
<c:if test="${empty param.ajax}">
You are not allowed to view the page.
</c:if></td>
  </tr>
  <tr>
    <td valign="top">&nbsp;</td>
  </tr>
</table>

</td>
  </tr>
  <tr>
    <td>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td height="30" valign="middle" background="<c:out value="${basePath}"/>/images/quinnox/footer-bg-3.jpg"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr>
          <td width="1%" height="30" align="left">&nbsp;</td>
            <td width="50%" height="60" align="left"><!--a href="company.html" class="footer-sublink-bottom">Company</a>&nbsp;&nbsp;
           <a href="Portal.html" class="footer-sublink-bottom">Portal</a>&nbsp;&nbsp;
              <a href="semantifi-enterprise.html" class="footer-sublink-bottom">Enterprise</a>&nbsp;&nbsp; 
              <a href="technology.html" class="footer-sublink-bottom">Technology</a>&nbsp;&nbsp; <a href="contact.html" class="footer-sublink-bottom">Contact</a> &nbsp;&nbsp;<a href="feedback.jsp" title="Feedback" class="footer-sublink-bottom">Feedback</a-->&nbsp;</td>

            <td width="32%" align="left">&nbsp;</td>
            <td width="17%" class="copyright" style="font-size:10px">Copyright &copy; 2009-2011 SEMANTIFI
          </td></tr>
        </table></td>
      </tr>
    </table>
</td>
  </tr>
</table>

</BODY>
</HTML>
