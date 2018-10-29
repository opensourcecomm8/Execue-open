<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<table>
<tr><td><b>Parallel Words</b></td></tr>
<tr><td>---------------------</td></tr>
	<s:iterator value="uiParallelWords"  id="pwords">		
			<tr><td><s:property value="parallelWord"/></td></tr>
		
	</s:iterator>
</table>
<br />
<br />
<table >
<tr><td><b>SFL Terms</b></td></tr>
<tr><td>---------------------</td></tr>
	<s:iterator value="sflTerms" id="pwords">
		<tr>
			<td ><s:property value="businessTerm" /></td>
		</tr>
	</s:iterator>
</table>
<br />
<br />
<br />
<table>
<tr><td><b>RI Onto Terms</b></td></tr>
<tr><td>---------------------</td></tr>
	<s:iterator value="riOntoTerms" status="ct" id="pwords">
		<tr>
			<td><s:property value="word" /></td>
		</tr>
	</s:iterator>
</table>
