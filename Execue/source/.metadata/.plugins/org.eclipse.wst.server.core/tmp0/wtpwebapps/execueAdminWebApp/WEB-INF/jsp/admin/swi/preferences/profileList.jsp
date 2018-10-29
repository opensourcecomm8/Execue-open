<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<script src="../SpryAssets/SpryTabbedPanels.js" type="text/javascript"></script>
<body>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td width="25%" >
      <div id="TabbedPanels1" class="TabbedPanels">
                <ul class="TabbedPanelsTabGroup">
                  <li class="TabbedPanelsTab" >Base Profiles</li>
                  <li class="TabbedPanelsTab" >User Profiles</li>
</ul>
                <div class="TabbedPanelsContentGroup"> 
                  <div class="TabbedPanelsContent" style="height:267px;"><div class="tableList" style="height:267px;">
                    <table width="100%" border="0" cellspacing="0" cellpadding="2">
                      <s:iterator value="baseProfileConcepts" status="inst" id="profile">
                     
                      <tr id="baseProfileRow1" class="rowSelected">
                        <td width="15%" class="dotBullet">&#8226;</td>
                        <td width="85%" class="fieldNames"><div id="showBaseProfile1Link" ><a href="javascript:;" class="links" id="baseProfile1"><s:property value="%{#profile.displayName}" /></a></div><div id="loadingShowBaseProfile1Link" style="display:none;"><img src="images/admin/loadingBlue.gif" width="20" height="20"></div></td>
                      </tr>
                      
                      </s:iterator>
                      </table>
                  </div></div>
                  
</div>
    </div></td>
    <td width="3%" class="fieldNames">&nbsp;</td>
    <td width="72%" class="fieldNames"><div id="profilesPane"></div></td>
  </tr>
</table>
<script type="text/javascript">
<!--
var TabbedPanels1 = new Spry.Widget.TabbedPanels("TabbedPanels1","profiles");
//-->
</script>

<script>
$(document).ready(function() { 

$.get("baseProfile1.htm", {}, function(data) { 
$("#profilesPane").fadeIn("fast");    
$("#profilesPane").append(data);
$("#profilesPane").show();  
}); 

$("a").click(function(){
obj1 =this.id;
maxRecords=2;
obj=obj1.substring(11);
obj2=obj1.substring(0,1);
obj3=""
if(obj2=="b"){obj3="BaseProfile"+obj}
if(obj2=="u"){obj3="UserProfile"+obj}

$("#show"+obj3+"Link").hide(); 	
$("#loadingShow"+obj3+"Link").show(); 							   
$.get(obj1+".htm", {}, function(data) { 
$("#loadingShow"+obj3+"Link").hide(); 
$("#profilesPane").empty(); 
$("#profilesPane").append(data);
$("#profilesPane").show(); 
$("#show"+obj3+"Link").show();

for(i=1;i<=maxRecords;i++){
	if(obj2=="b"){
		if(i==obj){

		$("#baseProfileRow"+obj).removeClass("rowNotSelected");
		$("#baseProfileRow"+obj).addClass("rowSelected");
			
		}else{
		$("#baseProfileRow"+i).removeClass("rowSelected");
		$("#baseProfileRow"+i).addClass("rowNotSelected");
		}
	}
	
	
	if(obj2=="u"){
		if(i==obj){

		$("#userProfileRow"+obj).removeClass("rowNotSelected");
		$("#userProfileRow"+obj).addClass("rowSelected");
			
		}else{
		$("#userProfileRow"+i).removeClass("rowSelected");
		$("#userProfileRow"+i).addClass("rowNotSelected");
		}
	}
	
	
}
});
});




});
</script>
</body>
</html>