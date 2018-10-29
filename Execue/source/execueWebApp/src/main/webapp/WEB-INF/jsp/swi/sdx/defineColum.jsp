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
 <div class="TabbedPanelsContent">
                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                      <tr>
                        <td><table width="100%" border="0" cellpadding="2" cellspacing="1" id="tableGrid" >
                          <tr>
                            <td width="13%">Column Name</td>
                            <td width="13%">Description</td>
                            <td width="13%">Data Type</td>
                            <td width="13%">Relation </td>
                            <td width="13%">Default Value</td>
                            <td width="13%">Required Flag</td>
                            <td width="13%">Stats</td>
                            <td width="13%">Column Name</td>
                          </tr>
                          <tr>
                          <tr>
                          <td>
                          <s:iterator value="cols" status="status" id="column" > 
                          	<s:property value="%{#column.name}"/>  
								</s:iterator>
								</td>
								 <td>
                          <s:iterator value="cols" status="status" id="column" > 
                          	<s:property value="%{#column.description}"/>  
								</s:iterator>
								</td>
                          </tr>
                         
                        </table></td>
                      </tr>
                      <tr>
                        <td height="40"><span class="fieldNames">
                          <label>
                            <input type="image" name="imageField3" id="imageField3" src="../images/submitButton.jpg">
                          </label>
                          <label>
                            <input type="image" name="imageField3" id="imageField4" src="../images/resetButton.gif">
                          </label>
                        </span></td>
                      </tr>
                    </table>
                  </div>
</body>
</html>