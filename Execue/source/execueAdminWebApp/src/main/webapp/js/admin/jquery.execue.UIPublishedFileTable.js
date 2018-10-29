(function($) {
    $.fn.extend({
        // this method is specific for saving the column evaluation/absorbtion screen
        gridToJSON : function (o){
            o = $.extend({
                columnName      : 'columnName',
                kdxDataType     : 'kdxDataType',
                columnDetail    : 'columnDetail',
                granularity     : 'granularity',
                defaultMetric   : 'defaultMetric',
                format          : 'format', 
                unit            : 'unit'
            }, o);

            var jsonArray = [];
            this.each(function() {
                var $y = this;
                if(!$y) { alert('grid undefined!'); return;}
                var jsonRow = o;
                var colSize = $y.grid.cols.length;
                for(var i = 1; i < colSize; i++){
                    // initializing the variable.
                    jsonRow = new Object();
                    // parse the grid column wise.
                    $.each($y.rows, function(index, row) {
                        $("td",row).each(function(ctr, col) {
                            if(ctr == i){
                                transformUI(jsonRow, col, index);
                            }
                        });
                    });
                    // check for null coulumnar values. This should not be included as part of submit jsonString.
                    if(jsonRow.columnName != undefined)
                        jsonArray[i-1] = jsonRow;
                }
            });
            return jsonArray;
        },
        // this method is to convert/get the conversion types based on the selected entity.
        convertToJSON : function (o){
            o = $.extend({
                publishedFileTableDetailId  : "-99",
                conversionType              : "conversionType",
                conceptExist				: "false",
                conceptId					: "-999"
            }, o);
            
            var jsonUIFormat = o;
            this.each(function() {
                var $x = $(this);
                if(!$x) {return;}
                var id = $($x).attr('id');
                // initializing the variable.
                jsonUIFormat = new Object();
                transformConversions(jsonUIFormat, id);
            });
            return jsonUIFormat;
         },
         saveColumnToJSON : function (o) {
            o = $.extend({
                id            	: "-99",
                name          	: "name",
                description   	: "description",
                instanceExist 	: "false",
                optionalMemberId : "-999",
                parentConceptId : "-999"                
            }, o);
            
            var jsonArray = [];
            this.each(function() {
                var $z = this;
                if(!$z) { alert('grid undefined!'); return;}
                var jsonRow = o;
                // parse the grid
                $.each($z.rows, function(index, row) {
                	// initializing the variable.
                	jsonRow = new Object();
                    jsonRow.id 			  	= row.cells[0].innerHTML;
                    jsonRow.name 		  	= row.cells[1].innerHTML;
                    jsonRow.description   	= $("input", row.cells[2]).val();
                   	jsonRow.instanceExist 	= row.cells[3].innerHTML;
                   	jsonRow.optionalMemberId = $("input", row.cells[4]).val();
                   	jsonRow.parentConceptId = $("input", row.cells[5]).val();                   	
                    jsonArray[index] = jsonRow;
                });
            });
            return jsonArray;
         }
  });  
})(jQuery);

function transformConversions(uiFormat, id) {
    uiFormat.conversionType             = $("#" + id).val();
    uiFormat.publishedFileTableDetailId = $("#" + id + "_publishedFileTableDetailId").val();
    uiFormat.conceptExist				= $("#" + id + "_conceptExist").val();
    uiFormat.conceptId					= $("#" + id + "_conceptId").val();
}

function transformUI(uiFormat, cell, rowIndex) {
    if(rowIndex == 0)
        uiFormat.columnName     = $("input", cell).val();
    else if(rowIndex == 1){
        var id = $("select", cell).attr('id');
        uiFormat.columnDetail   = $("#"+id).convertToJSON();
    }
    else if(rowIndex == 2)
        uiFormat.format         = $("select", cell).val();
    else if(rowIndex == 3)
        uiFormat.unit           = $("select", cell).val();
    else if(rowIndex == 4)
        uiFormat.kdxDataType    = $("select", cell).val();
    else if(rowIndex == 5) {
        uiFormat.granularity    = $("input:checked", cell).val();
		if(uiFormat.granularity ==undefined){ uiFormat.granularity="NA"}
	}
}