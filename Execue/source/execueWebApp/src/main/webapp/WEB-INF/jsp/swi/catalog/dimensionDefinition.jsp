<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<table id="rangeDefinition" width="100%">
  <thead>
    <tr>
      <th colspan="2" align="center" class="innerPaneHeading"><strong><s:text name="execue.cube.dimension.details.heading"/></strong></th>
    </tr>
    <tr>
      <td colspan="2"><div id="errorMessage" align="left"></div></td>
    </tr>
    <tr>
      <td ><strong><s:text name="execue.cube.dimension.concept.name"/> :</strong> <s:property value="concept.displayName"/></td>
    </tr>
  </thead>
</table>

<s:if test="range == null">
  <jsp:include page="simpleDimensionDefinition.jsp"/>
</s:if>
<s:else>
  <jsp:include page="rangeDimensionDefinition.jsp"/>
</s:else>
