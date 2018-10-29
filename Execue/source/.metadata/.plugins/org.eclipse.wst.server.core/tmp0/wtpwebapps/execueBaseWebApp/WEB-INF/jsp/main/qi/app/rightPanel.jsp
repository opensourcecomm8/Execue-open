<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<TABLE border=0 cellSpacing=0 cellPadding=0 width="95%" 
                  align=center>
                    <TBODY>
                    <s:if test="uiApplicationInfo.applicationId == 1508">
                     <TR>
                      <TD height=46 align=left>
                        <DIV style="PADDING-TOP: 6px" 
                        class=headding-apps>&nbsp;Video</DIV></TD></TR>
                    <TR>
                    <TR>
                      <TD vAlign=top style="padding-bottom:30px;">
                        <TABLE border=0 cellSpacing=0 cellPadding=0 width="100%" 
                        align=left>
                          <TBODY>
                          <TR>
                            <TD   height=30 align="center" vAlign=middle  ><a href="http://www.youtube.com/watch?v=eSI1ZE7wR6k" target="_blank"><img src="<c:out value='${basePath}'/>/images/main/craigslist/video1.jpg"  /></a></TD></TR>
                          </TBODY></TABLE></TD></TR>
</s:if>
                    <TR>
                      <TD height=46 align=left>
                        <DIV style="PADDING-TOP: 6px" 
                        class=headding-apps>&nbsp;Published By</DIV></TD></TR>
                    <TR>
                    <TR>
                      <TD vAlign=top>
                        <TABLE border=0 cellSpacing=0 cellPadding=0 width="95%" 
                        align=left>
                          <TBODY>
                          <TR>
                            <TD class=content-text height=30 vAlign=middle  width="97%" style="padding-left:6px;">Name : <s:property value="uiApplicationInfo.publisherName" /></TD></TR>
                          </TBODY></TABLE></TD></TR>
                                        <TR>
                      <TD vAlign=top align=left></TD></TR>
                    </TBODY>
                  </TABLE>
