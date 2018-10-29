<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<link href="../css/common/styles.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
body {
	background-color: #FFF;
}

li {
	line-height: 22px;
	list-style-type: none;
}
-->
</style>
<script type="text/javascript" src="../js/common/jquery.js"></script>
<script type="text/javascript" src="../js/admin/jquery.execue.htmlResponse.js"></script>
<script src="../js/admin/menun.js" language="JavaScript"></script>
<script src="../js/mm_menu.js" language="JavaScript"></script>
<script>
$(document).ready(function() {			 
  showMessageBox();
});
function showMessage(notificationId,notificationType){
  $.getJSON('getNotificationBody.action',{"selectedNotification.id":notificationId,"selectedNotification.notificationType":notificationType,},function(data){
     $("#showMessageDiv").empty();
      $("#showMessageDiv").append(data);
  });	
}
function showMessageBox(){
  $.get('showMessageBox.action',function(data){
      $("#dynamicPane").empty();
      $("#dynamicPane").append(data);
  });	
}
</script>

</html>