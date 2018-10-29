
$(document).ready(function() {

});
/** TODO-JT- Need to move all common code for edit and create datasource screen here **/
function deleteDatasource(datasourceId,basePath) {
		var removeRecords = confirm("Are you sure you want to remove datasource?");
		if (!removeRecords) {
			return;
		} else {			
			var url = basePath+"/swi/removeDatasource.action?dataSource.id=" + datasourceId;				
			$.get(url, function(data) {
				if(data.status){
				  $("#dynamicPaneDatasource").hide();
				  $("#deleteMessagesPane").empty().append(data.message);	
				  $("#deleteMessagesPane").show();
				}else{	
				 $.each(data.errorMessages, function () {
				      $("#deleteErrorMessagesPane").empty().append("<li>"+this+"<\li>");
				      $("#deleteErrorMessagesPane").show();
				    });					 
				}
			});
		}
	}