/**************************************************************************************
*                                     <ExcCueLite - Reporting views>
*                                         www.vbsoftindia.com
* 
* Document author : Ankur M. Bhalodia 
* File Name       : ExeCueScript.js
* Perpose         : For Charts Viewing
* Created on      : 18 Febuary,2009
**************************************************************************************/

function chartView(chartName)
{
    var activeChart = chartName ;
    var id = document.getElementById("tempId").value;
    var id_array = id.split(",");
   $("#disclaimerDetails").hide();
    for(var i = 0;i < id_array.length;i++)
    {
    	document.getElementById(id_array[i]).className = 'inactive';
    	
    }
    document.getElementById(activeChart).className = 'active';
    
}

function showAll(iconEl)
{
    var id = document.getElementById("tempId").value;
    var id_array = id.split(",");
    $("#disclaimerDetails").hide();
    for(var i = 0;i < id_array.length;i++)
    {
        document.getElementById(id_array[i]).className = 'active';
    } 
}

function hideAll(){
 var id = document.getElementById("tempId").value;
    var id_array = id.split(",");
   // $("#disclaimerDetails").hide();
    for(var i = 0;i < id_array.length;i++)
    {
    	document.getElementById(id_array[i]).className = 'inactive';
    	
    }	
}
