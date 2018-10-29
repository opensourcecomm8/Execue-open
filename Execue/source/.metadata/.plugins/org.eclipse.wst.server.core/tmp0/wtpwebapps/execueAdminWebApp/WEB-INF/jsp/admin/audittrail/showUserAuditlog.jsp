<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="basePath" value="<%=request.getContextPath()%>" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

</head>
<body>

<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td width="650" align="left"> 
		
		<input type="text" value="Choose a Date" style="display:none;" id="rangeA" />	
        <!--input type="text" value="Choose a Date" style="display:none;" id="rangeA_Cohort" /-->	
        <input type="hidden" value="<s:property value='qiJSONString' />"  id="qiJSONString" />
        <input type="hidden" value=""  id="currentDiv" />
		</td>
	</tr>
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground"><s:text name="execue.audittrail.level.title"></s:text></td>
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
				<td colspan="3" height="40" valign="top" class="descriptionText"><s:text name="execue.audittrail.level.description"/></td>
			</tr>

			<tr>
				<td colspan="3" valign="top" background="../images/admin/blueLine.jpg"><img
					src="../images/admin/blueLine.jpg" width="10" height="1" /></td>
			</tr>
			<tr>
				
				<td width="78%" valign="top">
				<table border="0" cellpadding="0" cellspacing="0">
					<tr>
						<td height="350" align="left" valign="top">
						   <!-- form start -->
						
						      <form id="auditLogSearchForm" action='searchUserAccessAuditlog.action' accept-charset="utf-8" method="post">                    
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
								 <tr>
									<td align="center" height="30" ></td>
								</tr>
								  <tr>
								    <td>
								       <!-- Select metrics  starts -->
										  <div id="business_metrics"
											style="padding-bottom: 0px; padding-top: 10px;">
																
											<div id="selectMetrics"
												style="padding-top: 0px; float: left; padding-left: 30px; display: none;">
											</div>	
											<div id="selectMetrics_tb"
												style="padding-top: 0px; float: left; padding-left: 30px;">
												<input id="selectMetrics_textbox" type="text" value="" />
											 </div>											
										   </div>					                                
								          <!-- Select metrics  ends -->
								      </td>								   
								   </tr>
								   <tr>
								   <td>
								    <!-- Select condition  starts -->
								        <div id="condition" style="display: none;">
								    		<div class="search_header_text_qi"><s:text name="execue.audittrail.level.select.timeframe"/><span id="sampleValueId"></span>
											</div>
											<div class="pad10px">
												<table width="100%" border="0" cellspacing="0" cellpadding="0">
													<tr>
														<td width="535">
														<div id="advOpt_condition"
															style="padding-top: 0px; float: left; width: 530px;"></div>
														</td>
														
													</tr>
												  </table>
											 </div>
										 </div>
								                    
								      <!-- Select condition  ends -->
								   </td>								    
								  </tr>
								  <tr>
								    <td>
								     <!-- Select group  starts -->								    
								    <div id="groups" style="display: none;">
										<div class="search_header_text_qi"><s:text name="execue.audittrail.level.select.audittype"/>
										</div>
										<div class="pad10px">
											<table width="10" border="0" cellspacing="0" cellpadding="0">
												<tr>
													<td width="535">
													<div id="advOpt_summarize" style="width: 530px;"></div>
													</td>
													<td width="100" style="padding-left:20px;">
													
													<div id="search_image" ><input type="button"
														id="searchBtn" src=""
														alt="Generate" title="Generate" tabindex="-1" width="90" value="Generate"
														height="27" /></div>
													</td>
												</tr>												
											</table>								
							             </div>								
										</div>
								                                
								        <!-- Select group  ends -->                        
								      </td>								    
								  </tr>
								  
								  <tr id="showAllContainer">
										<td height="35" align="left" valign="top">
										<div style="width: 802px; margin: auto;">
										
										<div id="showAll"></div>
										
										</div>
										</td>
									</tr>
								</table>								                
								     <div id="pleaseWaitDiv"
										style="display: none; margin: auto; width: 133px; margin-top: 5px; margin-bottom: 5px;">
									   <img id="waiting_img" src="images/main/wait.gif" width="133"
										height="11" />
									</div>   
									 <div id="hiddenFieldDiv"
										style="display: none; margin: auto; width: 133px; margin-top: 5px; margin-bottom: 5px;">									   
									</div>         
							</form>
						
                           <!-- form end -->
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
</body>
</html>

 <script>
	$(function(){
						   
		 $("#searchBtn").click(function(){	
		     $("#hiddenFieldDiv").empty();	
		     var hiddenUserAccessAuditInputId="<input type='hidden' name='userAccessAuditInput.id' value='-1'></input>";				    
		     $("#hiddenFieldDiv").append(hiddenUserAccessAuditInputId);	
		    var userIndex=0; 
            $.each($("ul.qiSuggestTerm-holder li.bit-box")	,function(){					
				var objectId= $(this).attr("objid");				
				if(objectId && objectId != 'undefined'){
				    var hiddenField="<input type='hidden' name='userAccessAuditInput.userIds["+userIndex+"]' value="+objectId+"></input>";				    
				    $("#hiddenFieldDiv").append(hiddenField);		
				    userIndex++;
				}									  
			 });
			  var audittypeIndex=0; 			 
			  $.each($("ul.qiSuggestTerm-holder li.bit-box")	,function(){
				var audittype= $(this).attr("audittype");				
				if(audittype && audittype != 'undefined'){				    
				     var hiddenAudittype="<input type='hidden' name='userAccessAuditInput.auditLogTypeIds["+audittypeIndex+"]' value="+audittype+"></input>";
					 $("#hiddenFieldDiv").append(hiddenAudittype);	
				     audittypeIndex++;
				}									  
			 });												
				
		
			 var operandIndex=0;								
			$.each($("ul.qiSuggestCondition-holder li.bit-box")	,function(i,v){						
				var timeframe = $(this).attr("displayName");
				//alert(timeframe);
				if(timeframe && timeframe != 'undefined'){
					if(i==0){
					   var hiddenTimeFrame="<input type='hidden' name='userAccessAuditInput.operator' value="+timeframe+"></input>";				    
				       $("#hiddenFieldDiv").append(hiddenTimeFrame);
					}else{
					    var hiddenTimeFrame="<input type='hidden' name='userAccessAuditInput.operands["+operandIndex+"]' value="+timeframe+"></input>";
					    $("#hiddenFieldDiv").append(hiddenTimeFrame);	
					   operandIndex++;
					}				
				   
				}						  
			});	
		 $("#auditLogSearchForm").submit();
		   
		});						
										
	 });
</script>













