<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><s:text name="execue.asset.heading"/></title>
</head>
<body>
<div class="innerPane" style="width:99%" >
<s:form namespace="/swi" method="POST" action="defineAsset">
            <table width="60%" border="0" align="center" cellpadding="3" cellspacing="0">
              <tr>
                <td class="fieldNames">&nbsp;</td>
                <td height="40">&nbsp;</td>
              </tr>
              <tr>
                <td width="19%" class="fieldNames">Data Source</td>
                <td width="33%"><select name="asset.dataSource.name" id="dataSource.name" >
                  <option value="ABC Cards DW">ABC Cards DW</option>
                </select></td>
              </tr>
              <tr>
                <td class="fieldNames">Name</td>
                <td><input name="asset.name" type="text" class="textBox" id="name"></td>
              </tr>
              <tr>
                <td class="fieldNames">Display Name</td>
                <td><input name="asset.displayName" type="text" class="textBox" id="displayName"></td>
              </tr>
              <tr>
                <td class="fieldNames">Description</td>
                <td><input name="asset.description" type="text" class="textBox" id="descriotion"></td>
              </tr>
              <!--<tr>
                <td class="fieldNames">Detail Level</td>
                <td><input name="textfield12" type="text" class="textBox" id="textfield12"></td>
              </tr>
              --><tr>
                <td class="fieldNames">&nbsp;</td>
                <td height="40"><span class="fieldNames">
                  <input type="image" name="imageField" id="imageField" src="../images/submitButton.jpg" />
                <input type="image" name="imageField2" id="imageField2" src="../images/resetButton.gif" />
                </span></td>
              </tr>
              <tr>
                <td height="35" colspan="2" class="fieldNames">&nbsp;</td>
              </tr>
            </table>
          </s:form>
          </div>
</body>
</html>