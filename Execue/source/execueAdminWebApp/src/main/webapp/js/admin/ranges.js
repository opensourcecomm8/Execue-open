var nonDataRowCount = 1;

/* Set the next range member's lower limit as the current range member's upper limit */
function setNextLowerLimit(index) {
  var ll = getLowerLimitId(index+1);
  var ul = getUpperLimitId(index);
  document.getElementById(ll).value=document.getElementById(ul).value;
}

function getIdColumnId(index) {
    return "rangeDetailList["+index+"].id";
}

function getOrderId(index) {
    return "rangeDetailList["+index+"].order";
}

function getValueId(index) {
    return "rangeDetailList["+index+"].value";
}

function getLowerLimitId(index) {
    return "rangeDetailList["+index+"].lowerLimit";
}

function getUpperLimitId(index) {
    return "rangeDetailList["+index+"].upperLimit";
}

function getDescriptionId(index) {
    return "rangeDetailList["+index+"].description";
}

/* Add a row dynamically to the table */
function addRowToTable(index) {
  
  var tbl = document.getElementById('rangeDefinitionDetail');
  
  var rowCount = tbl.rows.length;
  if (rowCount == (nonDataRowCount+20)) {
    alert('Maximum number of range definitions is 20');
  } else {
    
      var lastRow = index + (nonDataRowCount+1);
      var nextIndex = index+1;
      
      var newRow = tbl.insertRow(lastRow);
            
      
      var curUpperLimit = document.getElementById(getUpperLimitId(index)).value;
      var nextLowerLimit;
      if (tbl.rows.length == nextIndex+(nonDataRowCount+1)) {
        curUpperLimit = document.getElementById(getLowerLimitId(index)).value;
        nextLowerLimit = 'High';
        document.getElementById(getUpperLimitId(index)).value = document.getElementById(getLowerLimitId(index)).value;
      } else {
        nextLowerLimit = document.getElementById(getLowerLimitId(index+1)).value;
      }
      
      adjustIndexesForAdd(index, tbl.rows.length);
      
      newRow.innerHTML = "<input name='"+getIdColumnId(nextIndex)+"' value='' id='"+getIdColumnId(nextIndex)+"' type='hidden'> "+
      "<input name='"+getOrderId(nextIndex)+"' value='"+nextIndex+"' id='"+getOrderId(nextIndex)+"' type='hidden'> "+
      "<input name='"+getValueId(nextIndex)+"' value='"+nextIndex+"' id='"+getValueId(nextIndex)+"' type='hidden'>";
      
      var newCell = newRow.insertCell(0);
      newCell.innerHTML = "<input type='text' class='grayed' id='"+getLowerLimitId(nextIndex)+"' name='"+getLowerLimitId(nextIndex)+"' value='"+curUpperLimit+"' maxlength='25' readonly='readonly' tabindex='-1'/>";
      
      if (tbl.rows.length == nextIndex+(nonDataRowCount+1)) {
        newCell = newRow.insertCell(1);
        newCell.innerHTML = "<input type='text' class='grayed' id='"+getUpperLimitId(nextIndex)+"' name='"+getUpperLimitId(nextIndex)+"' value='"+nextLowerLimit+"' maxlength='25' readonly='readonly' tabindex='-1'/>";
      } else {
        newCell = newRow.insertCell(1);
        newCell.innerHTML = "<input type='text' id='"+getUpperLimitId(nextIndex)+"' name='"+getUpperLimitId(nextIndex)+"' value='"+nextLowerLimit+"' maxlength='25' onchange='setNextLowerLimit("+nextIndex+")'/>";
      }
      
      newCell = newRow.insertCell(2);
      newCell.innerHTML = "<input type='text' id='"+getDescriptionId(nextIndex)+"' name='"+getDescriptionId(nextIndex)+"' value='Description' maxlength='35'/>";
      
      newCell = newRow.insertCell(3);
      newCell.innerHTML = "<img class='plusminus' src='../images/admin/plus.gif' alt='Add range below' title='Add range below' onclick='return addRowToTable("+nextIndex+")' tabindex='-1'/>";
      
      newCell = newRow.insertCell(4);
      newCell.innerHTML = "<img class='plusminus' src='../images/admin/remove.jpg' alt='Remove this range' title='Remove this range' onclick='return removeRowFromTable("+nextIndex+")' tabindex='-1'/>";
      
      if (tbl.rows.length == index+(nonDataRowCount+2)) {
        description = document.getElementById(getDescriptionId(index)).value;
        lowerLimit = document.getElementById(getLowerLimitId(index)).value;
        upperLimit = document.getElementById(getUpperLimitId(index)).value;
        id = document.getElementById(getIdColumnId(index)).value;
        
        adjustIndex(index+nonDataRowCount, index, lowerLimit, upperLimit, description, id, tbl.rows.length);
      }//This condition is required when a row is added to the end of the table
      
      document.getElementById(getDescriptionId(nextIndex)).focus();
      document.getElementById(getDescriptionId(nextIndex)).select();
      
  }
  return false;
}

function removeRowFromTable(index) {
  var tbl = document.getElementById('rangeDefinitionDetail');
  
  var rowCount = tbl.rows.length;
  if (rowCount == (nonDataRowCount+3)) {
    alert('Minimum number of range definitions is 3');
  } else {
      if (index == 0) {
        document.getElementById(getLowerLimitId(index+1)).value = 'Low'; 
      } else if (index+nonDataRowCount == (rowCount-1)) {
        document.getElementById(getUpperLimitId(index-1)).value = 'High';
      } else {
        document.getElementById(getLowerLimitId(index+1)).value = document.getElementById(getUpperLimitId(index-1)).value;
      }
      
      tbl.deleteRow(index+nonDataRowCount);
      adjustIndexesForDelete(index, tbl.rows.length);
  }
  return false;
}

function adjustIndexesForDelete(valueIndex, tableLength) {

    var description = null;
    var lowerLimit = null;
    var upperLimit = null;
    var id = null;

    var dataValueIndex = 0;
    var nextIndex = 0;
    
    // alert('adjusting rows');
    
    var startIndex = valueIndex + nonDataRowCount;

    for (index = startIndex; index < tableLength; index++) {
        dataValueIndex = index;
        
        description = document.getElementById(getDescriptionId(dataValueIndex)).value;
        lowerLimit = document.getElementById(getLowerLimitId(dataValueIndex)).value;
        upperLimit = document.getElementById(getUpperLimitId(dataValueIndex)).value;
        id = document.getElementById(getIdColumnId(dataValueIndex)).value;
        
        // alert (lowerLimit +' - '+ upperLimit +' - '+ description);
        
        nextIndex = index - nonDataRowCount;
        
        // alert('next Index : '+nextIndex+' Old Index '+dataValueIndex);
        
        adjustIndex(index, nextIndex, lowerLimit, upperLimit, description, id, tableLength);
    }
    
}

function adjustIndexesForAdd(valueIndex, tableLength) {
    var description = null;
    var lowerLimit = null;
    var upperLimit = null;
    var id = null;

    var dataValueIndex = 0;
    var nextIndex = 0;
    
    // alert('adjusting rows '+ valueIndex +' - '+ tableLength);
    
    var startIndex = valueIndex + nonDataRowCount + 2;

    for (index = tableLength-1; index >= startIndex; index--) {
        
        dataValueIndex = index - nonDataRowCount - 1;
        
        description = document.getElementById(getDescriptionId(dataValueIndex)).value;
        lowerLimit = document.getElementById(getLowerLimitId(dataValueIndex)).value;
        upperLimit = document.getElementById(getUpperLimitId(dataValueIndex)).value;
        id = document.getElementById(getIdColumnId(dataValueIndex)).value;
        //id = '';//ID should not be created while adding a row
        
        // alert (lowerLimit +' - '+ upperLimit +' - '+ description);
        
        nextIndex = index - nonDataRowCount;
        
        // alert('next Index : '+nextIndex+' Old Index '+dataValueIndex);
        
        adjustIndex(index, nextIndex, lowerLimit, upperLimit, description, id, tableLength);
    }
}

function adjustIndex(index, nextIndex, lowerLimit, upperLimit, description, id, tableLength) {
    var row = document.getElementById('rangeDefinitionDetail').rows[index];
    
    row.innerHTML = "<input name='"+getIdColumnId(nextIndex)+"' value='"+id+"' id='"+getIdColumnId(nextIndex)+"' type='hidden'> "+
      "<input name='"+getOrderId(nextIndex)+"' value='"+nextIndex+"' id='"+getOrderId(nextIndex)+"' type='hidden'> "+
      "<input name='"+getValueId(nextIndex)+"' value='"+nextIndex+"' id='"+getValueId(nextIndex)+"' type='hidden'>";
    
    var cell = row.insertCell(0);
    cell.innerHTML = "<input type='text' class='grayed' class='textBox' id='"+getLowerLimitId(nextIndex)+"' name='"+getLowerLimitId(nextIndex)+"' value='"+lowerLimit+"' maxlength='25' readonly='readonly' tabindex='-1'/>";
    // alert(tableLength +' - '+ nextIndex);
    if (tableLength == nextIndex+(nonDataRowCount)) {
        cell = row.insertCell(1);
        cell.innerHTML = "<input type='text' class='grayed' class='textBox' id='"+getUpperLimitId(nextIndex)+"' name='"+getUpperLimitId(nextIndex)+"' value='"+upperLimit+"' maxlength='25' readonly='readonly' tabindex='-1' onchange='setNextLowerLimit("+nextIndex+")'/>";
    } else {
        cell = row.insertCell(1);
        cell.innerHTML = "<input type='text' id='"+getUpperLimitId(nextIndex)+"' name='"+getUpperLimitId(nextIndex)+"' class='textBox' value='"+upperLimit+"' maxlength='25' onchange='setNextLowerLimit("+nextIndex+")'/>";
    }
    
    cell = row.insertCell(2);
    cell.innerHTML = "<input type='text' id='"+getDescriptionId(nextIndex)+"' name='"+getDescriptionId(nextIndex)+"' class='textBox' value='"+description+"' maxlength='35'/>";
    
    cell = row.insertCell(3);
    cell.innerHTML = "<img class='plusminus' src='../images/admin/plus.gif' alt='Add range below' title='Add range below' onclick='return addRowToTable("+nextIndex+")'/>";
    
    cell = row.insertCell(4);
    cell.innerHTML = "<img class='plusminus' src='../images/admin/remove.jpg' alt='Remove this range' title='Remove this range' onclick='return removeRowFromTable("+nextIndex+")'/>";
}

function setFocus() {
    if (document.getElementById(getUpperLimitId(0))) {
        document.getElementById(getUpperLimitId(0)).focus();
    }
}