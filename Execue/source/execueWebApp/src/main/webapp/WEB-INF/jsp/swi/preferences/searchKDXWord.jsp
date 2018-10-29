<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%><head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>semantifi | meaning based search | business intelligence |
search databases | search data sets | bi tools</title>
</head>

<form id="form1" name="form1" method="post" action="">
<table width="40%" border="0" cellspacing="0" cellpadding="0">
	<!--tr>
		<td width="39%">&nbsp;</td>
		<td width="61%">&nbsp;</td>
	</tr-->
	<tr>
		<td>Enter Search Word</td>
		<td><s:textfield name="keyWord.word" cssClass="textBox"
			id="searchWord" /></td>
		<td><input type="button" name="button" id="button" value="Submit"
			onclick="javascript:showDetails('swi/getWords.action?keyWord.word='+$('#searchWord').val(),'innerPane','post');" /></td>
	</tr>
</table>
<br />
<br />
</form>

<div id="innerPane" class="innerPane" style="width: 99%;">
</div>