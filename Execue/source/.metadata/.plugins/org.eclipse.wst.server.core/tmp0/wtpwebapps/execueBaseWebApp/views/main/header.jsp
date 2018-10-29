<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix='security' uri='http://www.springframework.org/security/tags'%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />

<style>
.login_bg {
	background-image: url(<c:out value="${basePath}"/>/images/main/login_r2_c1.jpg);
}

.top-bg {
	background-image: url(<c:out value="${basePath}"/>/images/main/results_header_bg.jpg);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}
.technology-1 {
	background-image: url(<c:out value="${basePath}"/>/images/main/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
	height:52px;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;

}

.welcome_user_type{
color:#333;	
}
.tableTitles td{
padding-left:2px;
height:25px;
}
</style>


      
      
      
      <table width="100%" cellpadding="0" cellspacing="0" border="0" >
  <tr>
    <td class="top-bg">


                
                <table width="1166" border="0" cellspacing="0" cellpadding="0" align="center">
                    <tr>
                      <td width="8%">&nbsp;</td>
                      <td width="92%" align="center">
                      
                      
                      <table width="100%" border="0" cellspacing="0" cellpadding="3" align="right">
                          <tr>
                            <td width="31%" height="20" align="center"><!-- sub links starts -->
                              &nbsp;</td>
                          
                                    
                                    
                                   
                            
                           
                                    <td width="63%" align="right" style="color:#333;font-size:11px;">  <s:text name="execue.login.admin.welcome.message">
				<s:param><security:authentication property="principal.fullName"/></s:param> 
			</s:text></td>
                          <td width="6%" align="right"><div style="margin-top:0px;"><span style="padding-top:0px;" ><!--a href="#" class="logout" tabindex="-1">Profile</a--></span><span style="padding-left:10px;"><a href="<c:url value='/j_spring_security_logout'/>" class="logout"  style="color:#039" tabindex="-1">Logout</a></span></div></td>
                           
                          </tr>
                        </table>
                        
                        
                        </td>
                    </tr>
                </table>
                
                


</td>
</tr>
  
  
  <tr >
    <td class="technology-1" height="42" >
    
    <table width="1166" border="0" cellspacing="0" cellpadding="0" align="center">
  <tr>
    <td width="8%" valign="top"><div style="padding:0px;"><a href="../index.jsp"><img name="index_r1_c1" src="../images/main/inner-page-logo.png" border="0"
					id="index_r1_c1" alt="" /></a></div></td>
    <td width="92%" align="left"> <!--jsp:include page="../../../freeFormSearch.jsp"  flush="true" /--></td>
  </tr>
</table>


      </td>
  </tr>
</table>