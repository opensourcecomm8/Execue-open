<%@ taglib prefix="s" uri="/struts-tags"%>
<table border="0" cellpadding="0" cellspacing="0" width="100%" bgcolor="#CCCCCC">
    <!-- fwtable fwsrc="index.png" fwpage="Page 1" fwbase="index.jpg" fwstyle="Dreamweaver" fwdocid = "738978224" fwnested="1" -->
    
    <tr>
      <td align="center"  height="25" ><span style="font-size:10px;color:#333"><s:text name="execue.footer.text" /></span></td>
    </tr>
  </table>
<script>

$(function(){
		   
		  //setTimeout( function(){$("a.links,a.arrowBg").click(function(){$("a.links,a.arrowBg").css("fontWeight","normal");$(this).css("fontWeight","bold");});},1000);
		 // alert("3");
		 // $("a.links").click( function(){alert("3a");;$(this).css("fontWeight","bold");}); 
		   
		   });

$(document).ajaxComplete(function() {
 $("a.links,a.arrowBg").click(function(){$("a.links,a.arrowBg").css("fontWeight","normal");$(this).css("fontWeight","bold");});
});
</script>
<style>

</style>