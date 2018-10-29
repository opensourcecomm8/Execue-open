<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@taglib uri="/WEB-INF/tlds/pagination.tld" prefix="pg"%>

<div class="tableList"
                      style="height: 235px; width: 190px; margin: auto; margin-bottom: 5px; margin-left: 0px;overflow-y:hidden;">
                 
                    <table width="100%" border="0" cellspacing="0" cellpadding="2">                    
                      <s:iterator value="businessTerms" status="inst" id="businessTerm">
			               <s:if test="#inst.even == true">
                            <tr  id="tableRow3" >				                
				                <td width="90%" colspan="2"  style="padding-left:5px;">				              
				                  <s:if test='businessEntityTermType.value=="I" || businessEntityTermType.value=="IP"'>
				                   <a href="#" id="<s:property value='%{#businessTerm.businessEntityTermId}'/>" onclick="showEntityVariations('<s:property value='%{#businessTerm.businessEntityTermDisplayName}'/>',<s:property value='%{#businessTerm.businessEntityTermId}'/>);" class="links"><s:property value="%{#businessTerm.businessEntityTermDisplayName}" /></a>(<s:property value="%{#businessTerm.parentBusinessEntityTermDisplayName}"/>)
				                  </s:if>
				                  <s:else>
				                    <a href="#" id="<s:property value='%{#businessTerm.businessEntityTermId}'/>" onclick="showEntityVariations('<s:property value='%{#businessTerm.businessEntityTermDisplayName}'/>',<s:property value='%{#businessTerm.businessEntityTermId}'/>);" class="links"><s:property value="%{#businessTerm.businessEntityTermDisplayName}" /></a>
				                  </s:else>

				                <div id="loadingShowConcept1Link" style="display:none;"><img src="../images/admin/loadingBlue.gif" width="20" height="20" /></div></td>
				                
				              </tr>
				              </s:if>
				              <s:else>
				                <tr id="tableRow3">				                
				                <td width="90%" colspan="2"  style="padding-left:5px;">
				                   <s:if test='businessEntityTermType.value=="I" || businessEntityTermType.value=="IP"'>
				                   <a href="#" id="<s:property value='%{#businessTerm.businessEntityTermId}'/>" onclick="showEntityVariations('<s:property value='%{#businessTerm.businessEntityTermDisplayName}'/>',<s:property value='%{#businessTerm.businessEntityTermId}'/>);" class="links"><s:property value="%{#businessTerm.businessEntityTermDisplayName}"/></a>(<s:property value="%{#businessTerm.parentBusinessEntityTermDisplayName}"/>)
				                  </s:if>
				                  <s:else>
				                    <a href="#" id="<s:property value='%{#businessTerm.businessEntityTermId}'/>" onclick="showEntityVariations('<s:property value='%{#businessTerm.businessEntityTermDisplayName}'/>',<s:property value='%{#businessTerm.businessEntityTermId}'/>);" class="links"><s:property value="%{#businessTerm.businessEntityTermDisplayName}"/></a>
				                  </s:else>				                
				                 <div id="loadingShowConcept1Link" style="display:none;"><img src="../images/admin/loadingBlue.gif" width="20" height="20" /></div></td>
				                 
				              </tr>
				              </s:else>
			             
                        </s:iterator>                                       
                     </table>
                    </div>                
                    <div id="paginationDiv2" style="margin-top:10px;margin-bottom:5px;width:235px" ><pg:page targetURL="showEntityVariationBTs.action?businessEntityTermType=${businessEntityTermType}" targetPane="dynamicPaneBTerms"/></div>
<s:hidden id="pageSizeConcepts" name="pageDetail.pageSize" />
<s:hidden id="recordCountConcepts" name="pageDetail.recordCount" />
<s:hidden id="pageCountConcepts" name="pageDetail.pageCount" />
<s:hidden id="noOfLinksConcepts" name="pageDetail.numberOfLinks" />
<s:hidden id="paginationTypeConcepts" name="paginationType" />

<script>
if(searchTypeClicked=="contains"){
 $.each($("div.tableList table tr"),function(i,k){
		$temp=$(this).find("a");
		$temp.html(autoHighlight($temp.text(), searchString));
		});
 }
 if(searchTypeClicked=="startWith"){
 $.each($("div.tableList table tr"),function(i,k){
			$temp=$(this).find("a");
			$temp.html(autoHighlight($temp.text(), searchString));
		   });
 }
  if(searchTypeClicked=="all"){ 
 $.each($("div.tableList table tr"),function(i,k){
		    $temp=$(this).find("a");									   		
			$temp.html($temp.text());
		   });
 }

</script>