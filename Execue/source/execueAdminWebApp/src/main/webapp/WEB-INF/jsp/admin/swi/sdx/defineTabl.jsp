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
                 <s:form namespace="/swi" method="POST" action="defineTabls">
                      <table width="98%" border="0" align="center" cellpadding="2" cellspacing="0">
                        <tr>
                          <td class="fieldNames">&nbsp;</td>
                          <td>&nbsp;</td>
                          <td class="fieldNames">&nbsp;</td>
                          <td class="fieldNames">&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td width="22%" class="fieldNames">Name</td>
                          <td width="18%"><input name="table.name" type="text" class="textBox1" id="textfield"></td>
                          <td width="28%" class="fieldNames">Description Column</td>
                          <td width="21%"><input name="table" type="text" class="textBox1" id="textfield5" size="10" /></td>
                        </tr>
                        <tr>
                          <td class="fieldNames">Description </td>
                          <td><input name="table.description" type="text" class="textBox1" id="textfield2"></td>
                          <td class="fieldNames">&nbsp;</td>
                          <td class="fieldNames">Lower Limit Column</td>
                          <td><input name="table.loweLimitColumn" type="text" class="textBox1" id="textfield12" /></td>
                        </tr>
                        <tr>
                          <td class="fieldNames">Aggregation Level</td>
                          <td><input name="textfield3" type="text" class="textBox1" id="textfield3"></td>
                          <td class="fieldNames">&nbsp;</td>
                          <td class="fieldNames">Upper Limit Column</td>
                          <td><input name="table.upperLimitColumn" type="text" class="textBox1" id="textfield10"></td>
                        </tr>
                        <tr>
                          <td class="fieldNames">Type</td>
                          <td><input name="textfield8" type="text" class="textBox1" id="textfield8" /></td>
                          <td class="fieldNames">&nbsp;</td>
                          <td class="fieldNames">Parent Table Name</td>
                          <td><input name="table.parentTable" type="text" class="textBox1" id="textfield11"></td>
                        </tr>
                        <tr>
                          <td class="fieldNames">Value Column</td>
                          <td><input name="table.lookupValue" type="text" class="textBox1" id="textfield9" /></td>
                          <td class="fieldNames">&nbsp;</td>
                          <td class="fieldNames">Parent Column Name</td>
                          <td><input name="table.parentColumn" type="text" class="textBox1" id="textfield4"></td>
                        </tr>
                        <tr>
                          <td class="fieldNames">&nbsp;</td>
                          <td>&nbsp;</td>
                          <td class="fieldNames">&nbsp;</td>
                          <td class="fieldNames">&nbsp;</td>
                          <td>&nbsp;</td>
                        </tr>
                        <tr>
                          <td height="40" colspan="5" align="center"><input type="image" name="imageField" id="imageField" src="images/admin/createNewDataSourceButton.gif" />
                            <label>
                              <input type="image" name="imageField2" id="imageField2" src="images/admin/clearAllFields.gif">
                          </label></td>
                        </tr>
                      </table>
                    </s:form>
                  </div>
</body>
</html>