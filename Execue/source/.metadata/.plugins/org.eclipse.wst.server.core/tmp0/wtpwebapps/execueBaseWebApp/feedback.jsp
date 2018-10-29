<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- saved from url=(0014)about:internet -->
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link id="page_favicon" href="favicon.ico" rel="icon" type="image/x-icon" />
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon" />
<style>
.top-bg {
	background-image: url(images/main/results_header_bg.jpg);
	background-repeat: repeat-x;
	background-position: left bottom;
	height: 25px;
	background-color: #FBFBFB;
}
.technology-1 {
	background-image: url(images/main/top-bg-inner-2.png);
	background-repeat: repeat-x;
	background-position: left top;
	height:51px;
}
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
	background-color: #ccc;
}
.breadCrumbLink,#showAllA,#loginId {
	font-size: 8pt;
	color: blue;
}
.sendEmail a {
	color: blue;
}

.breadCrumbNoLink,.linkDescription {
	font-size: 8pt;
	color: #3F5356;
}
#feedbackIntro{
text-align:left;

}
form#feedbackForm input[type='text'],form#feedbackForm textarea{
border:1px solid #CCC;	
}
</style>
<jsp:include page="views/main/search/qi-head-simpleSearch.jsp"  flush="true" />

<title>semantifi.com | feedback</title><table width="100%" cellpadding="0" cellspacing="0" border="0" >
  <tr>
    <td class="top-bg">

<div style="margin:auto;width:91%">
                
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                    <tr>
                    
                      <td width="100%" align="center">
                      
                      
                      <table width="100%" border="0" cellspacing="0" cellpadding="3" align="right">
                          <tr>
                            <td width="43%" height="20" align="right"><!-- sub links starts -->
                              <!--div id="pleaseWaitDiv" style="display: none; margin: auto; width: 133px; margin-top: 1px; margin-bottom: 0px;height:10px;float:right;"> <img id="waiting_img" src="images/wait1e.gif"  /> </div-->
                              <div style="height:16px;padding-top:2px;z-index:20;position:absolute;" id="waitImage"><div id="pleaseWaitDiv" style="display: none; margin: auto; width: 110px;">
								<img id="waiting_img" src="images/main/Loader-main-page-3.gif" width="22" height="22" />
							</div>
                        </div></td>
                          
                                    
                                    
                                   
                            
                           
                                    <td width="40%" align="right"><div id="showWelcome" style="padding-right:20px;color:#3F5356;padding-top:0px;font-family:'Trebuchet MS', Arial, Helvetica, sans-serif;text-align:right;width:200px;">
   </div></td>
                         <!--  <td width="9%" align="right"><a href="feedback.jsp"><s:text name="execue.Feedback.link" /></a></td><td width="1%">|</td>-->
                            <td width="6%" align="left" >
                            
                            <span id="showLoginLink" style="padding-left:3px;"><a href="javascript:;" class="links_sem3"
   id="loginId" ><s:text name="execue.login.link" /></a>
  <a href="<c:url value='/j_spring_security_logout'/>" class="links_sem3"
   id="logoutId" style="display: none;"><s:text name="execue.logout.link" /></a></span>
  <span id="loadingShowLoginLink" style="display: none;"><img
   src="images/main/loaderTrans.gif" ></span>
                            
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
    <td width="8%" valign="top"><div style="padding-top:10px;padding-left:0px;padding-right:0px;padding-bottom:1px;"><a href="index.jsp"><img name="index_r1_c1" src="images/main/inner-page-logo.png" border="0"
					id="index_r1_c1" alt="" /></a></div></td>
    <td width="92%" align="left"> <jsp:include page="freeFormSearch.jsp"  flush="true" /></td>
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
		<td width="96%" height="25" background="images/main/bc-bg.jpg"  class="normalText" >
		<span style="padding-left:1px;color:#3F5356;">
                <a href="index.jsp"	 class="breadCrumbLink"><s:text name="execue.home.link" /></a><Span class="textArrow"> >> </Span> <a href="contact.html"	 class="breadCrumbLink">Contact</a><Span class="textArrow"> >> </Span><span class="breadCrumbNoLink"><s:text name="execue.feedback.breadcrumb" /></span>
		</span></td>
        
        
        <!-- <td width="4%" height="25" align="right" background="images/bc-bg.jpg"  class="normalText" >
		<span style="padding-left:1px;color:#3F5356;">
                <a href="javascript:history.back();"	 class="breadCrumbLink"><s:text name="execue.Back.link" /></a>
		</span></td>-->
        
        
	</tr>
	
</table>

</div>
  
      </td>
  </tr>
</table>
<div id="hiddenPane"
   style="position: absolute; width: 350px; height: 200px; z-index: 2; display: none;">
      <table border="0" cellpadding="0" cellspacing="0" width="310" align="left" >
        <!-- fwtable fwsrc="boxTemplate.png" fwpage="Page 1" fwbase="boxTemplate.gif" fwstyle="Dreamweaver" fwdocid = "901881679" fwnested="0" -->
        
<tr>
          <td width="15"><img name="boxTemplate_r1_c1"
     src="images/main/boxTemplate_r1_c1.gif" width="15" height="40"
     border="0" id="boxTemplate_r1_c1" alt="" /></td>
          <td background="images/main/boxTemplate_r1_c2.gif"><img
     name="boxTemplate_r1_c2" src="images/main/boxTemplate_r1_c2.gif"
     width="279" height="40" border="0" id="boxTemplate_r1_c2" alt=""/></td>
          <td width="15"><img
     name="boxTemplate_r1_c3" src="images/main/boxTemplate_r1_c3.gif"
     width="15" height="40" border="0" id="boxTemplate_r1_c3"
     alt="Close" title="Close"/></td>
          <td width="13"><img src="images/main/spacer.gif" width="1" height="15"
     border="0" alt="" /></td>
          </tr>
        <tr>
          <td background="images/main/boxTemplate_r2_c1.gif"><img
     name="boxTemplate_r2_c1" src="images/main/boxTemplate_r2_c1.gif"
     width="15" height="10" border="0" id="boxTemplate_r2_c1" alt="" /></td>
          <td width="1045" valign="top" bgcolor="#FFFFFF" >
            <div id="hiddenPaneContent"></div>
            </td>
          <td background="images/main/boxTemplate_r2_c3.gif" ><img
     name="boxTemplate_r2_c3" src="images/main/boxTemplate_r2_c3.gif"
     width="15" height="1" border="0" id="boxTemplate_r2_c3" alt="" /></td>
          <td><img src="images/main/spacer.gif" width="1" height="10"
     border="0" alt="" /></td>
          </tr>
        <tr>
          <td><img name="boxTemplate_r3_c1"
     src="images/main/boxTemplate_r3_c1.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c1" alt="" /></td>
          <td background="images/main/boxTemplate_r3_c2.gif" valign="top"><img
     name="boxTemplate_r3_c2" src="images/main/boxTemplate_r3_c2.gif"
     width="25" height="13" border="0" id="boxTemplate_r3_c2" alt="" /></td>
          <td><img name="boxTemplate_r3_c3"
     src="images/main/boxTemplate_r3_c3.gif" width="15" height="13"
     border="0" id="boxTemplate_r3_c3" alt="" /></td>
          <td><img src="images/main/spacer.gif" width="1" height="13"
     border="0" alt="" /></td>
          </tr>
        </table>
      </div><table width="100%" border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
  <tr>
    <td height="600" align="center" valign="top">    
    <table width="100%" border="0" cellspacing="5" cellpadding="0">
      <tr>
        <td width="32%"><DIV id=feedbackIntro style="background-color:#F3F4FE;border:1px #DFE8FD solid;width:76%;padding-left:20px;height:270px;float:right;margin-top:5px;padding-right:20px;">
<s:text name="execue.feedback.leftPaneContent.text" />
</DIV></td>
        <td width="62%" align="left" valign="top">
        
    <DIV  style="background-color:#FFFFFF;;border:1px #DFE8FD solid;width:100%;padding-left:20px;height:270px;float:left;margin-top:5px;margin-left:8px;">
        
      <s:form id="feedbackForm" action="sendFeedBack">
        <table width="490" border="0" align="left" cellpadding="2" cellspacing="0">
          <tr>
            <td colspan="3" align="left"><div id="errorMessage" style="color: red;padding:10px;font-size:10px;">
              <s:actionerror />
              <s:fielderror />
              <span style="color:#036;font-size:18px"><s:text name="execue.tellUsWhatYouThink.text" /></span></div>
              <div id="actionMessage" style="color: green">
                <s:actionmessage />
              </div></td>
          </tr>
          <tr>
            <td width="7%" align="left">&nbsp;</td>
            <td width="19%" align="left" class="fieldNames"><s:text name="execue.feedback.name.label" /><span class='fontRed'>*</span></td>
            <td width="73%" align="left"><input name="userRequest.firstName" type="text" id="textfield4" size="39" /></td>
            <td width="1%">&nbsp;</td>
          </tr>
          <tr>
            <td align="left">&nbsp;</td>
            <td align="left" class="fieldNames"> <s:text name="execue.feedback.email.label" /><span class='fontRed'>*</span></td>
            <td align="left"><input name="userRequest.emailId" type="text" id="textfield5" size="39" /></td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td align="left">&nbsp;</td>
            <td align="left" valign="top" class="fieldNames"> <s:text name="execue.feedback.feedback.label" /><span class='fontRed'>*</span><span class='fontRed'></span></td>
            <td align="left"><label>
              <textarea name="userRequest.notes" cols="30" rows="5" id="textfield6"></textarea>
            </label></td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td align="left">
            <input type="image" name="imageField" id="sendFeedback"
							src="images/main/sendButton.jpg"/>
                            
                            <img id="button2" value="Clear" src="images/main/clearButton.jpg" style="cursor:pointer;" />
                                
            <!--<a href="#"><img src="images/sendButton.png" id="sendFeedback" width="70" height="27" vspace="8" border="0" /></a>--></td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td align="left">&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
        </table>
        </s:form>
        </DIV>
        </td>
        <td width="6%" align="center" valign="middle">&nbsp;</td>
      </tr>
    </table>
    
    
    </td>
  </tr>
  <tr>
    <td><jsp:include page="views/main/footer-search.jsp" flush="true" /></td>
  </tr>
</table>
<script>
$("#sendFeedback").click(function(){
    //alert($('#feedbackForm').serialize());
	$("#dynamicPane").createInput('<s:url action="sendFeedBack!sendFeedBack"/>',"","",$('#feedbackForm').serialize()); //passing URL, user clicked link, loader div	
     return false;
});

$("#button2").click(function(){
			$("form#feedbackForm input[type='text']").val("");			
			$("form#feedbackForm textarea").val("");
			$("form#feedbackForm #textfield4").focus();
							 });
$(document).ready(function(){

$("form#feedbackForm #textfield4").focus();

						   });
</script>
