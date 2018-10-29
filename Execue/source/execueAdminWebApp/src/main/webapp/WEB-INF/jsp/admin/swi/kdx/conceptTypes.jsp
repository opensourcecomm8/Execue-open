<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<style>
#dynamicPane select {
    width: 200px;
}
</style>
<table width="100%" border="0" align="center" cellpadding="0"
    cellspacing="0">
    <tr>
        <td height="30" valign="bottom">
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td height="29" class="titleWithBackground"><s:text
                    name='execue.conceptTypes.heading' /></td>
            </tr>
        </table>
        </td>
    </tr>
    <tr>
        <td>
        <div id="greyBorder">
        <table width="98%" border="0" align="center" cellpadding="0"
            cellspacing="0">
            <tr>
                <td colspan="3" valign="top" class="descriptionText"><s:text
                    name="execue.conceptTypes.description" /></td>
            </tr>

            <tr>
                <td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
                    src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
            </tr>
            <tr>

                <td width="98%" valign="top">
                <table border="0" cellpadding="0" cellspacing="0">
                    <!-- fwtable fwsrc="blueBox.png" fwpage="Page 1" fwbase="blueBox.jpg" fwstyle="Dreamweaver" fwdocid = "466227697" fwnested="1" -->

                    <tr>
                        <td align="left" valign="top" style="padding-bottom: 20px;">
                        <div id="dynamicPane" style="width: 670px">
                        <div class="innerPane2" style="width: 430px; height: auto">
                        <div id="errorMessage" style="color: red; padding-top: 10px;"><s:actionerror /><s:fielderror /></div>
                        <div id="actionMessage" style="color: green; padding-top: 10px;"><s:actionmessage /></div>
                        <form id="typeCreation">
                        <table width="80%" border="0" cellspacing="0" cellpadding="0"
                            style="margin-left: 20px;">
                            <tr>
                                <td height="30" class="fieldNames"><s:text
                                    name="execue.conceptTypes.type"></s:text></td>
                                <td><s:select cssClass="textBox1" id="selectedType"
                                    name="selectedType" list='types' listKey="id" listValue="name"
                                    onchange="getConceptsAndAttributesTobeRealizedForType();" /></td>
                            </tr>
                            <tr>
                                <td height="30" class="fieldNames" valign="top"><s:text
                                    name="execue.conceptTypes.concepts"></s:text></td>
                                <td><select multiple="multiple" id="eligibleConcepts"
                                    size="6" name="eligibleConcepts">
                                </select></td>
                            </tr>
                            <tr>
                                <td height="30" class="fieldNames"><s:text
                                    name="execue.conceptTypes.TypeToBeRealized"></s:text></td>
                                <td><select id="typesToBeRealized" name="typesToBeRealized"
                                    onchange="getRealizedConceptsForAttribute();">

                                </select></td>
                            </tr>
                            <tr>
                                <td height="30" class="fieldNames" valign="top"><s:text
                                    name="execue.conceptTypes.realizedConcepts"></s:text></td>
                                <td><select multiple="multiple" size="6"
                                    name="realizedConcepts" id="realizedConcepts">
                                </select></td>
                            </tr>
                            <tr>
                                <td height="45" colspan="2"><input id="saveButton"
                                    type="button" class="singleButton" value="Save"
                                    onclick="saveTypeForConcepts();" /><input type="button"
                                    id="saveButtonLoader" style="display: none"
                                    class="singleButtonLoader" disabled="disabled" value="Save" />
                                </td>
                            </tr>
                        </table>
                        </form>
                        </div>
                        </td>
                    </tr>

                </table>
                </td>
            </tr>
            <tr>
                <td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
                    src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
            </tr>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>

        </table>
        </div>
        </td>
    </tr>

</table>