<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
        <table width="100%" border="0" cellspacing="0" cellpadding="3">
           <tr>
			<td colspan="3">
			<div id="errorMessageId" style="color: red">
			 <s:iterator value="uiStatus.errorMessages">
			   <li><s:property/></li>
			  </s:iterator></div>		
			  <div id="actionMessageId" style="color: green"></div>
			</td>
		</tr>
            <tr>
              <td width="44%"><strong><s:text name="execue.global.name"/><span class='fontRed'>*</span></strong></td>
              <td width="56%"><s:textfield cssStyle="width:180px;" name="targetAsset.displayName" id="targetAsset_name" /></td>
            </tr>           
           <!--  <tr>
              <td valign="top"><strong><s:text name="execue.global.displayName"/><span class='fontRed'>*</span></strong></td>
              <td><s:textarea cssStyle="width:180px;" name="targetAsset.displayName" /></td>
            </tr>-->
             <tr>
              <td valign="top"><strong><s:text name="execue.global.description"/></strong></td>
              <td><s:textarea cssStyle="width:180px;" name="targetAsset.description" /></td>
            </tr>
          </table>
</body>
</html>