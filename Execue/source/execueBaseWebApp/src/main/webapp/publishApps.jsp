<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
    <%@page import="com.execue.web.core.util.ExecueWebConstants"%>
    <c:set var="adminPath" value="<%=application.getAttribute(ExecueWebConstants.ADMIN_CONTEXT)%>" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>semantifi : Publish Apps </title>
<meta name="keywords" content=" search databases, search datasets, search secfilings, meaning based search, index datasets, index databases, Search Results for datasets, business intelligence, bi tools, BI installations for their enterprise clients , bi solutions, datafeeds , individual investors to research equities, automated software, appliance for business intelligence, fully automated, search based BI solution, searching multiple databases,  data cubes , simple search interface, immediate results from databases , using a catalog of cubes , data marts , Integrating your data sources , Semantifi Enterprise , simple process, works with most databases, cubes and data marts, Semantic search engine,  driven by knowledge base , relevant answers, Automatic data summaries , visualizations , interpret database results, Index multi-terabyte databases, fast results, Crawl ,  database , compose complex queries, build data search apps, search data , search unstructured content, searches documents , highly relevant search results , natural language processing , NLP,  search engines, deep web," />

<meta name="description" content="deep web, search databases, search datasets, search secfilings, meaning based search, index datasets, index databases, Search Results for datasets, Publish Apps. Search Datasets. Share Results. Build Apps to search any dataset using semantic search technology hosted here. Build Apps of personal interest, share apps publicly, within social networks or to paid-subscribers, You can apply Semantifi technology on enterprise data assets that include relational databases, data warehouses (DW), OLAP cubes, and datamarts. Semantifi is the solution for all your business intelligence needs, datafeeds for individual investors to research equities, automated software appliance for business intelligence, fully automated search based BI solution, searching multiple databases and data cubes via a simple search interface, immediate results from databases using a catalog of cubes and data marts , Integrating your data sources into Semantifi Enterprise is a simple process, It works with most databases, cubes and data marts, Semantic search engine driven by knowledge base for relevant answers, Automatic data summaries & visualizations to interpret database results, Index multi-terabyte databases for fast results, Crawl any type of database to compose complex queries, marketplace for a community of publishers to build data search apps for all users to search, meaning based search platform to search both structured data and unstructured content, searches both databases and documents to deliver highly relevant search results compared to keyword or natural language processing (NLP) search engines, deep web"/>

<link id="page_favicon" href="favicon.ico" rel="icon" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />


<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #F2F5F7;
}
.table-center {
	margin: 0px;
	padding: 0px;
	float: none;
	width: 1000px;
	position: relative;
	background-position: center;
	text-align: center;
}
h3{
color:#CE0624;
margin-bottom:4px;
}
-->
</style>
<script language="JavaScript" src="js/common/jquery.js"></script>
<script type="text/javascript" src="js/common/jquery.ui.all.js"></script>

<link href="css/main/semantifi-new.css" rel="stylesheet" type="text/css" />
<link href="css/main/semantify-new.css" rel="stylesheet" type="text/css" /><script language="JavaScript" src="js/common/jquery.js"></script>
 <script language="JavaScript" src="js/common/goog_analytics.js"></script>
<script>
$(document).ready(function(){

$.get("views/main/footer-search.jsp",{},function(data){
$("#footerContent").html(data);
});

$.get("topMenu.jsp",{},function(data){
$("#topMenuContent").html(data);
});

});
</script>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td align="left" valign="middle" bgcolor="#487CAD" class="top-bg"><a href="index.html"><a href="../index.html"></a></td>
    <td width="82%" class="top-bg"><div id="topMenuContent"></div></td>
  </tr>
  </table></td>
  </tr>
  <tr>
    <td valign="top">
    <table width="100%" border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td colspan="2" align="center" valign="top"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="innerPages-bgColor">
          <tr>
            <td height="50" colspan="2" align="center" valign="top" bgcolor="#FFFFFF" class="technology-1"><table width="916" border="0" align="center" cellpadding="0" cellspacing="0">
              <tr>
                <td width="20%" height="50" align="left" valign="middle"><a href="index.html"><img src="images/main/inner-page-logo.png" width="112" height="30" vspace="10" border="0" /></a></td>
                <td width="3%" valign="middle">&nbsp;</td>
                <td align="right" valign="middle" class="white18">A Marketplace of <span class="orange18"> Search Apps </span>for a community of <span class="orange18"> Publishers and Consumers</span></td>
                <td width="1%" align="right" valign="middle" class="white18">&nbsp;</td>
              </tr>
            </table></td>
          </tr>
          <tr>
            <td height="370" colspan="2" align="center" valign="top"><table width="900" border="0" align="center" cellpadding="3" cellspacing="0">
              <tr>
                <td width="56%" colspan="2" align="left" valign="top"><table border="0" align="right" cellpadding="5" cellspacing="0">
                  <tr>
                    <td nowrap="nowrap" class="sub-headding">PUBLISH APPS</td>
                    <td>&nbsp;</td>
                    <td class="sub-headding"><strong><a href="http://wiki.semantifi.com/index.php/HowToBuildApps" target="_blank" class="link">HOW TO BUILD APPS</a></strong></td>
                    <td>&nbsp;</td>
                    <td class="content-textInner"><strong><a href="http://wiki.semantifi.com/index.php/App_Ideas" target="_blank" class="link">APP IDEAS</a></strong></td>
                    <td>&nbsp;</td>
                    <td id="publisherUserConsole"><a href="<c:out value='${adminPath}' />swi/showSearchAppsDashboard.action" class="link">PUBLISHER CONSOLE</a></td>
                  </tr>
                </table></td>
              </tr>
              <tr>
                <td colspan="2" align="left" valign="top"><table width="900" border="0" align="center" cellpadding="3" cellspacing="0">
                  <tr>
                    <td align="left" valign="top" class="content-textInner"><span class="headding-innerpage">Publish Apps</span></td>
                    </tr>
                  <tr>
                    <td align="left" valign="top" class="content-textInner" height="10"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td align="left" bgcolor="#F4F8FB" >
                        <div id="loginTabId" style="border-top:1px solid #ccc;border-bottom:1px solid #ccc;padding:20px;width:90%;height:160px;background-color:#F4F8FB;padding-left:80px;margin:auto;margin-bottom:10px;margin-top:10px;">
                        <!-- login box-->
                          <table  border="0" width="100%" align="center" cellpadding="0" cellspacing="0"   style="float:left;">
                            <tr>
                              <td  align="center" width="46%"><form action="<c:out value='${adminPath}'/><c:url value='j_spring_security_check'/>"
							method="post" name="Login" id="Login">
                                <table width="96%" cellspacing="0" cellpadding="0" border="0"  align="left">
                                  <tbody>
                                    <tr>
                                      <td width="22%" align="left" class="main-headding" style="text-indent:0px;">SIGN IN</td>
                                      <td width="78%" align="left">&nbsp;</td>
                                    </tr>
                                    <tr>
                                      <td class="Arial11Blue">User Name</td>
                                      <td height="30" align="left"><input maxlength="33" tabindex="1" size="22" id="username" class="inputText" value="" name="j_username"/></td>
                                    </tr>
                                    
                                    <tr>
                                      <td size="25" maxlength="25" class="Arial11Blue">Password</td>
                                      <td height="30" align="left"><input type="password" tabindex="2" size="22" id="password" class="inputText" name="j_password"/></td>
                                    </tr>
                                    <tr>
                                      <td>&nbsp;</td>
                                      <td height="30" align="left"><label>
                                        <input type="image" tabindex="3" src="images/main/login_submit.gif" id="submit" name="submit"/>
                                        <a href="BiSignup.jsp"/>
                                      </label></td>
                                    </tr>
                                    <tr>
                                      <td>&nbsp;</td>
                                      <td height="18" align="left" class="Arial11Blue">&nbsp;<a style="font-size:11px;" href="forgotUserPassword.action">Forgot Password</a></td>
                                    </tr>
                                    <tr>
                                      <td>&nbsp;</td>
                                      <td height="18" align="left" class="Arial11Blue" style="padding-left:5px;font-size:11px;">Don't have a Login Id? &nbsp; <a style="font-size:11px;" href="biSignUp.action">Sign Up</a>&nbsp;</td>
                                    </tr>
                                  </tbody>
                                </table>
                              </form></td>
                              <td width="54%"  align="left"><strong>Sign Up benefits:</strong>
                                <ul><li>Publish your datasets as Search Apps</li><li>Vew Longer history for certain Apps</li><li>Personalize search experience</li><li>Access and preview more Apps</li><li>Bookmark or save interesting search questions</li></ul></td>
                            </tr>
                          </table>
                          <!-- login box -->
                          </div>
                          </td>
                      </tr>
                    </table></td>
                  </tr>
                  <tr>
                    <td width="70%" align="left" valign="top"><div class="content-textInner"> <span class="headding2"> Who can launch Apps?</span> <br />
                      Anyone as in such as individuals,  businesses, or groups can use Semantifi Search platform for free to launch  apps. Personal interest, bragging rights, and profit should be good enough  reasons for most of us to launch an App that searches some data.</div>
                      <br />
                      <div class="content-textInner" style="padding-left:25px;text-align:justify;"><span class="sub-headding"><strong>1. INDIVIDUALS INTERNET USERS</strong></span><br />
                        Some of these users who are  passionate about data and investigating them will configure their data and  publish Apps. They may share with public, or within their social groups, or to  private subscribers. When users search, they get search results from all  matching Apps but only results from free Apps can be viewed. Users can request  access to socially shared Apps or subscribe to premium Apps. </div>
                        <br>
</td>
                  </tr>
                  <tr>
                    <td align="left" valign="top"><table width="100%" border="0" cellspacing="0" cellpadding="0">
                     
                      <tr>
                        <td class="content-textInner" style="padding-left:25px;"><span class="sub-headding"><strong>2. CONTENT OWNERS &amp; PRODUCERS</strong></span><br />
                          Millions of content owners and  producers can launch Apps to allow their customers to search their content  either for free or a fee. For instance, a travel portal could configure their  reservations databases so travelers can see best itineraries, a retailer like  J&amp;R could configure its product catalog so shoppers can see products that  match its catalog, a finance content distributor like Zacks can configure its  financial market datafeeds for individual investors to research equities  possibly with a subscription, and so on.<br />
                          <br /></td>
                        </tr>
                      <tr>
                        <td width="83%" class="content-textInner" style="padding-left:25px;"><span class="sub-headding" ><strong>3. ENTERPRISE CLIENTS</strong></span><br />
                          Enterprise clients looking to search  their enterprise wide data sources using this search technology have two  choices: </td>
                        </tr>
                      <tr>
                        <td class="content-textInner" style="padding-left:50px;padding-top:10px;">a) A web hosted search solution  delivered with a custom or skinnable interface with optional data hosting. <br />
                          b) An internal product  deployment for enterprises where a web hosted search platform is not an option. </td>
                        </tr>
                      <tr>
                        <td>&nbsp;</td>
                        </tr>
                      <tr>
                        <td class="content-textInner" style="padding-left:25px;"><span class="sub-headding"><strong>4. INTERMEDIARIES/CONSULTING FIRMS </strong></span><br />
                          Technology Integrators can  build and manage BI installations for their enterprise clients for professional  services</td>
                        </tr>
                      <tr>
                        <td class="headding2">&nbsp;</td>
                        </tr>
                      <tr>
                        <td height="130">&nbsp;</td>
                        </tr>
                    </table></td>
                    </tr>
                  </table></td>
              </tr>
            </table></td>
          </tr>
          </table></td>
      </tr>

    </table></td>
  </tr>
  <tr>
    <td ><div id="footerContent"></div></td>
  </tr>
</table>
</body>
</html>
<script>
$(document).ready(function () { 
				
							
 var loginPublisherId='<security:authentication property="principal.username"/>'; 
//alert(loginPublisherId);
if(loginPublisherId==-1){
	$("#loginTabId").show();
	return;
}
if(loginPublisherId=="guest"){	
	$("#loginTabId").show();
	$("#guestUserConsole").show();
	$("#publisherUserConsole").hide();
	//$("#signUpAppsTr").show();
}else{	
	window.location="<c:out value='${adminPath}' />swi/showSearchAppsDashboard.action";
	$("#loginTabId").hide();
	$("#guestUserConsole").hide();
	$("#publisherUserConsole").show();
	$("#signUpAppsTr").hide();
	
}

$("#username").focus();			
});
</script>
