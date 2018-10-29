<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%@ page language="java"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />

<%
   response.setHeader("Pragma", "No-cache");
   response.setDateHeader("Expires", 0);
   response.setHeader("Cache-Control", "no-cache");
   response.setHeader("Cache-Control", "no-store");
   response.addHeader("Cache-Control", "post-check=0, pre-check=0");
   response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
%>
</head>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0"
	bgcolor="#FFFFFF">

	<tr>
		<td class="center-bg1">
		<div id="center-content" style="height: auto;">
		<table  border="0" align="center" cellpadding="0" id="outerTable"cellspacing="0">
			<tr>
				<td valign="top" >
				<table width="100%"  border="0" align="center" cellpadding="0" cellspacing="0" class="main-box">
          
            <tr>
             <td valign="top" width="15" style="background:url('<c:out value="${basePath}"/>/images/quinnox/leftBg.png') repeat-y ; "  >
             
            
             <div class="centerBg" style="background:url('<c:out value="${basePath}"/>/images/quinnox/leftBg.png') repeat-y ; height:15px;width:15px;margin-top:40px;" ><img src="<c:out value="${basePath}"/>/images/main/app/leftBg.png" /></div>
          </td>
              <td valign="top" bgcolor="#FFFFFF" class="box1" style="padding-left:10px;"><!-- Box1 Content -->
                
                  
                    <table width="100%" border="0" align="center" cellpadding="0"
							cellspacing="0" style="margin-left: 10px; margin-top: 10px;">
							<tr>
								<td height="34" class="headding-apps"
									style="padding-bottom: 10px;">FEATURED APPS&#13;</td>
							</tr>
							<tr>
								<td><!--div style="overflow:auto;height:480px;width:313px;overflow-x:none;"-->

								<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0" style="margin-top: 3px;">
	<s:iterator value="applicationList" id="applicationListId">
		<tr>
			<td>
			<table width="290" border="0" align="left" cellpadding="0" cellspacing="0" id="t1" class="appTable">
				<tr>
					<s:if test="applicationImageId != -1">
						<td width="90" align="left" valign="top"><s:if test="applicationName != 'Craigslist Auto'">
							<a href='<c:out value="${basePath}"/>/appIndex.action?applicationId=<s:property value="applicationId"/>'
								style="float: left"> <img
								src="qi/getImage.action?applicationId=<s:property value='applicationId'/>&appImageId=<s:property value='applicationImageId'/>"
								name="Image1" width="90" height="90" border="0" id="Image1" /></a>
						</s:if> <s:else>
							<a href='<c:out value="${basePath}"/>/appIndex.action?applicationId=<s:property value="applicationId"/>'
								style="float: left"><img
								src="qi/getImage.action?applicationId=<s:property value='applicationId'/>&appImageId=<s:property value='applicationImageId'/>"
								name="Image1" width="90" height="90" border="0" id="Image1" /></a>
						</s:else></td>
						<td width="200" valign="top" class="content-text" style="padding-left: 12px;">

						<div><s:property value="completeAppDescription" /></div>
						</td>
					</s:if>
					<s:else>
						<TD class=content-text height=34><a
							href="<c:out value="${basePath}"/>/appIndex.action?applicationId=<s:property value="applicationId"/>"
							title="<s:property value='completeAppDescription'/>" alt="<s:property value='completeAppDescription'/>"
							style="float: left"><img src="<c:out value="${basePath}"/>/images/main/noImage-icon.gif" width="90"
							height="90" border="0" id="Image1"></img></a></TD>
					</s:else>
				</tr>
				<tr>

					<td colspan="2" align="left" style="padding-left: 11px;">
					<div class="appName"><a class="appLink" style="color: #003366"
						href="appIndex.action?applicationId=<s:property value='applicationId' />"> <s:property value="applicationName" />
					</a></div>
					</td>
				</tr>
				<tr>

					<td colspan="2" class="lineSeperator" align="center">&nbsp;</td>
				</tr>
			</table>
			</td>
		</tr>
	</s:iterator>
</table>
								</td>
							</tr>
						</table>
                    
                      
               
               </td>
               
               
               
                <td valign="top" width="15" bgcolor="#FFFFFF"  style="padding-left:10px;" >
             
             <div style="background:url('<c:out value="${basePath}"/>/images/quinnox/lineTop.png') repeat-y ; height:100px;width:15px;" >&nbsp;</div>
             <div class="centerLineDiv" style="background:url('<c:out value="${basePath}"/>/images/quinnox/lineMiddle.png') repeat-y ; height:400px;width:15px;" >&nbsp;</div>
              <div  style="background:url('<c:out value="${basePath}"/>/images/quinnox/lineBottom.png') repeat-y ; height:175px;width:15px;" >&nbsp;</div>
          </td>
          
          
              <td  valign="top" bgcolor="#FFFFFF" class="box2" style="padding-left:10px;border-left:1px solid #ccc">
                
                 <table width="98%" border="0" align="center" cellpadding="0"
							cellspacing="0">

							<tr>
								<td align="left" height="44">
								<div class="headding-apps" style="padding-top: 8px;">&nbsp;POPULAR
								SEARCHES...</div>
								</td>
							</tr>

							<tr>
								<td>
								<div id="queriesDiv" style="display: none"></div>

								<div id="t2app" class="appsExamplesLinks">
								<table width="95%" border="0" align="center" cellpadding="0"
									cellspacing="0">

									<tr>
										<td>
										<table width="100%" border="0" cellspacing="0"
											id="appsDetailsDiv" cellpadding="0">

											<s:iterator value="applicationList" id="applicationListId">
												<tr>
													<td>
													<div class="appName"
														style="padding-left: 9px; font-size: 14px;"><s:property
														value="applicationName" /></div>
													</td>
												</tr>
												<tr>
													<td width="61%" valign="middle">
													<TABLE border=0 class="exampleQueries" cellSpacing=0
														width="95%" align=center>
														<TBODY>
															<s:iterator value="appExamples" id="appExamplesListId">
																<TR>
																	<s:if
																		test="%{#appExamplesListId.queryName != #appExamplesListId.truncatedQueryName}">
																		<TD align=left><A style="CURSOR: pointer"
																			class=box2-link
																			title="<s:property value='queryName'/>"
																			alt="<s:property value='queryName'/>"><s:property
																			value="truncatedQueryName" />...</A></TD>
																	</s:if>
																	<s:else>
																		<TD align=left><A style="CURSOR: pointer"
																			class=box2-link
																			title="<s:property value='queryName'/>"
																			alt="<s:property value='queryName'/>"><s:property
																			value="truncatedQueryName" /></A></TD>
																	</s:else>
																</TR>
															</s:iterator>
														</TBODY>
													</TABLE>

													</td>
												</tr>
												<tr>
													<td>&nbsp;</td>
												</tr>
											</s:iterator>
										</table>
										</td>
									</tr>
								</table>

								</div>

								</td>
							</tr>
						</table>
                </td>
              <td  valign="top" bgcolor="#FFFFFF" class="box3">
               <table width="95%" border="0" align="center" cellpadding="0"
							cellspacing="0">

							<tr>
							<tr>
								<td>
								<table width="95%" border="0" align="center" cellpadding="0"
									cellspacing="0">


									<tr>
										<td colspan="2" align="center">
										
										</td>
									</tr>
									<tr>
										<td colspan="2" align="left">&nbsp;</td>
									</tr>


									<tr>
										<td colspan="2" align="left">&nbsp;</td>
									</tr>

									<tr>
										<td colspan="2" align="left">&nbsp;</td>
									</tr>



								</table>
								</td>
							</tr>
						</table>
              </td>
                <td width="15" valign="top"  style="background:url('<c:out value="${basePath}"/>/images/quinnox/rightBg.png') repeat-y ; "  >
             <div class="centerBg" style="background:url('<c:out value="${basePath}"/>/images/quinnox/rightBg.png') repeat-y ; height:15px;width:15px;margin-top:40px;" ><img src="<c:out value="${basePath}"/>/images/main/app/rightBg.png" /></div>
            </td>
              </tr>
              
                <tr>
          <td colspan="6" height="50" valign="top" class="home-page-bg-bottom">&nbsp;</td>
        </tr>
        
            </table>
				</td>
			</tr>
			
		</table>
		</div>
		</td>
	</tr>
	<tr>
		<td></td>
	</tr>
	
	<tr>
		<td align="center"><img src="images/main/1pix.jpg" width="1"
			height="1" id="img1" /></td>
	</tr>
</table>




</body>
</html>
<script>

$(document).ready(function(){
$(".exampleQueries a").click(function(){
		$("#metrics").val($(this).attr("title")); 
			example=setTimeout(showHide,200);
			$("#searchBtnFreeForm").click();							  
  });
 });
</script>