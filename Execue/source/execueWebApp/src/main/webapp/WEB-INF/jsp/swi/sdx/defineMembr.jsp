<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="../SpryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
<link href="../SpryAssets/SpryTabbedPanels.css" rel="stylesheet" type="text/css">
<style type="text/css">
<!--

}
-->
</style>
<link href="css/styles.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div class="TabbedPanelsContent">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><table width="100%" border="0" cellpadding="2" cellspacing="1" id="tableGridMemberInfo" >
                          <tr class="tableTitles">
                            <td width="13%">Value</td>
                            <td width="13%">Description</td>
                            <td width="13%">Long Description</td>
                            <td width="13%">Lower Limit</td>
                            <td width="13%">Upper Limit</td>
                            <td width="13%">Parent Value</td>
                            <td width="13%">Column Name</td>
                            <td width="13%">Column Name</td>
                          </tr>
                          <tr>
                            <td>
                             <s:iterator value="members" status="status" id="member" > 
                          	<s:property value="%{#member.lookupDescription}"/>  
								</s:iterator>
                            </td>
                          </tr>
                         
                        </table></td>
                      </tr>
                      <tr>
                        <td height="40"><span class="fieldNames">
                          <label>
                            <input type="image" name="imageField4" id="imageField5" src="../images/submitButton.jpg">
                          </label>
                          <label>
                            <input type="image" name="imageField4" id="imageField6" src="../images/resetButton.gif">
                          </label>
                        </span></td>
                      </tr>
                    </table>
                  </div>
</body>
</html>