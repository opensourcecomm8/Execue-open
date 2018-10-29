<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>

<style type="text/css">

td.heading {
	background-image: url(../images/admin/rowBg.jpg);
	font-size: 11px;
	padding-left: 5px;
}

span.red {
	color: red;
	font-size: 13px;
	font-weight: bold;
}
select{
width:40px;
}
.imgFade{
opacity: .50;
filter:Alpha(Opacity=50); 	
}
.imgDontFade{
opacity: 1;
filter:Alpha(Opacity=100); 	
}

</style>

</head>
<body>
<div id="searchUserAuditlogDiv">
<table width="100%" border="0" align="center" cellpadding="0"
	cellspacing="0">
	<tr>
		<td height="30" valign="bottom">
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td height="29" class="titleWithBackground">User Audit Result</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td>
		<div id="greyBorder" style="min-height: 330px; height: auto;">
		<table width="99%" border="0" align="center" cellpadding="10"
			cellspacing="0">
			<tr>
				<td valign="top">

				<table width="100%" cellspacing="0" cellpadding="0" border="0"
					align="center">					
					<tr>
						<td>
						<TABLE id="grid" style="font-weight: normal;"></TABLE>
						<DIV id=pager></DIV>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>
</div>
</body>
</html>
    <s:set name="id" value="%{'userAccessAuditInput.id='+userAccessAuditInput.id}" />
    <s:set name="queryString" value="%{#id}"/>

    <s:iterator value="userAccessAuditInput.userIds" status="indx"> 
         <s:set name="tempUserIds" value="%{'&userAccessAuditInput.userIds['+#indx.index+']='+userAccessAuditInput.userIds[#indx.index]}" />
         <s:set name="queryString" value="%{#queryString+#tempUserIds}"/> 		
	</s:iterator> 
	 <s:iterator value="userAccessAuditInput.auditLogTypeIds" status="indx">	      
		 <s:set name="auditLogTypeIds" value="%{'&userAccessAuditInput.auditLogTypeIds['+#indx.index+']='+userAccessAuditInput.auditLogTypeIds[#indx.index]}" />	    
	     <s:set name="queryString" value="%{#queryString+#auditLogTypeIds}"/> 		
	</s:iterator> 	
	 <s:iterator value="userAccessAuditInput.operands" status="indx">		 
		 <s:set name="operands" value="%{'&userAccessAuditInput.operands['+#indx.index+']='+userAccessAuditInput.operands[#indx.index]}" />	      
	     <s:set name="queryString" value="%{#queryString+#operands}"/> 		
	</s:iterator> 
	<s:if test="userAccessAuditInput.operator != null">
	  <s:set name="operator" value="%{'&userAccessAuditInput.operator='+userAccessAuditInput.operator}" />
	    <s:set name="queryString" value="%{#queryString+#operator}"/>
	</s:if>

	 		
	
	
	
	
	
<SCRIPT type=text/javascript>

       
$(document).ready(function() {
	var queryString='<s:property value="#queryString"/>';	
     queryString=queryString.replace(/&amp;/g,"&");   
  //  alert(queryString);
	$('#grid').jqGrid({
	  url: 'showUserAccessAuditDetail.action?'+queryString,
      datatype: 'json',
      mtype: 'GET',
	  width: 950,
	  height: 270,
	  rowNum: 10,
	  scroll:0,
      rowList: [10,20,30],
      pager: $('#pager'),
      sortname: 'name',
      viewrecords: true,
	  gridview: true,
	  colNames:[ "id",'User','IP Address','Timestamp','Login/Logout','Comment'],
	  colModel:[	
	  			{name:"id",index:'id',width:50,hidden:true,search:false,sortable:false},	  			
				{name:"user.displayName",index:'name',width:80,sortable:true,sorttype:"text", search:true, stype:'text',formatter: showUser},
				{name:"ipLocation",width:90,sortable:false, search:false},	
				{name:"accessedTime",width:100,sortable:false, search:false},	
				{name:"auditLogType",width:100,sortable:true,sorttype:"text", search:false},
				{name:"comments",width:130, sortable:true, search:false}],
	jsonReader: {
	    repeatitems: false
	},
	prmNames:{page:"requestedPage",rows:"pageSize",sort:"sortField",order:"sortOrder",id:"id",nd:null,search:null},
	pager:"#pager"});
	
	$('#grid').jqGrid('navGrid','#pager',
					{"edit":false,"add":false,"del":false,"search":true,"refresh":true,"view":false,"excel":false}, 		   // options
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // edit
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // add
					{"drag":true,"resize":true,"closeOnEscape":true,"dataheight":150,"mtype":"POST",reloadAfterSubmit:false},  // del
					{"drag":true,"closeAfterSearch":true,"afterShowSearch":setFocus, sopt: ['cn','bw','ew','eq'],sOper:"searchType","closeOnEscape":true,"dataheight":150}) // search
			.navSeparatorAdd("#pager",{})			
							$("#fbox_grid input[type='text']").live("keydown",function(e){ 
									if(e.keyCode==13 || e.which==13){
									   $("#fbox_grid .ui-search").click();	
									}
			               });
 });

function setFocus(){
 $("#fbox_grid input[type='text']").focus();
}
function showUser(cellvalue, options, rowObject){
  if(rowObject.anonymousUser=='NO'){      
      return rowObject.user.displayName;
  }else{
     return "Anonymous";
  }
}








 </SCRIPT>
