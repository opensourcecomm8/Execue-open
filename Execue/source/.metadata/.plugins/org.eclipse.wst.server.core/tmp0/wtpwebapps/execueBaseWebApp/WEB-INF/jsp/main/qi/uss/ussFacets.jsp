<%@ page language="java" contentType="text/html; charset=iso-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="f" %>

<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix='security'
	uri='http://www.springframework.org/security/tags'%>
    <c:set var="basePath" value="<%=request.getContextPath()%>" />


			<s:set name="maxItemsDisplayCount" value="3" />
            <s:set name="maxFacetsDisplayCount" value="3" />
             <div id="facetContainer">	
            <s:iterator value="facets" id="facetId" status="facetsstatus">
            <div facetid="<s:property value='#facetId.id' />" checked  facetname="<s:property value='#facetId.name' />" facettype="<s:property value='#facetId.type' />" class="facet">	
                    <s:if test="prominent.value=='Y'">
                    <div class="facetHeading" ><div class="moreFacetDiv"><div class="moreFacetToggle"><img border="0" src="images/main/bullet_toggle_minus.png" ></div><div class="facetName" ><s:property value="name" /></div></div></div>
              		<div class="facetItems">
                    </s:if>
                    <s:else>
                    <div class="facetHeadingHidden" ><div class="moreFacetDiv"><div class="moreFacetToggle"><img border="0" src="images/main/bullet_toggle_plus.png" ></div><div class="facetName" ><s:property value="name" /></div></div></div>
                    <div class="facetItemsHidden">
                    </s:else>
                    
            		<s:iterator value="facetDetails" id="fDetails" status="facetDetailsstatus" >
                    <s:if test="#facetDetailsstatus.count==(#maxItemsDisplayCount+1)">
						 <div class="hiddenFacetItems" >
                    </s:if>
                    <s:if test="#facetDetailsstatus.count<=#maxItemsDisplayCount">
                     <div class="facetItem" >

                     <s:if test="selected.value=='Y'">
                     <input checked  value="<s:property value='#fDetails.name' />" type="checkbox" />  
                     </s:if>
                     <s:else>
                     <input  value="<s:property value='#fDetails.name' />" type="checkbox" />  
                     </s:else>
                     
                     <s:property value="name" />( <s:property value="count" /> )</div>
                     </s:if>
                     <s:else>
                      <div  class="facetItemHidden" > 
                      
                      <s:if test="selected.value=='Y'">
                      <input  type="checkbox" checked value="<s:property value='#fDetails.name' />" />
                       </s:if>
                     <s:else>
                     <input  value="<s:property value='#fDetails.name' />" type="checkbox" />  
                     </s:else>
                      
                      <s:property value="name" />( <s:property value="count" /> )</div>
                     </s:else>
                     <s:if test="#facetDetailsstatus.last && facetDetails.size() > #maxItemsDisplayCount" >
                     </div>
                     
                      <div class="moreFacetDetailsDiv"> <div class="moreToggle" ><img border="0" src="images/main/arrow.png" ></div><div class="moreLinkDiv" > <a href="#">More</a></div></div>
                     </s:if>
                     </s:iterator>
                     </div>
                </div>
            </s:iterator>
            </div>
            <script> 
			$(function(){ 
			 $("div.moreFacetDetailsDiv").toggle(
				  function () {
					$(this).find("img").attr({"src":"images/main/arrowLess.png"});
					$(this).find("a").text("Less");
					$(this).prev(".hiddenFacetItems").slideDown("slow");
				  },
				  function () {
					$(this).find("img").attr({"src":"images/main/arrow.png"});
					$(this).prev(".hiddenFacetItems").slideUp("slow")
					$(this).find("a").text("More");
				  }
				);
			 
			 
			 $("div.facetHeadingHidden").toggle(
				  function () {
					$(this).find("img").attr({"src":"images/main/bullet_toggle_minus.png"});
					//$(this).find("a").text("Less");
					$(this).next(".facetItemsHidden").show();
				  },
				  function () {
					$(this).find("img").attr({"src":"images/main/bullet_toggle_plus.png"});
					$(this).next(".facetItemsHidden").hide();
					//$(this).find("a").text("More");
				  }
				);
			 
			 
			 $("div.facetHeading").toggle(
										  
					function () {
					$(this).find("img").attr({"src":"images/main/bullet_toggle_plus.png"});
					$(this).next(".facetItems").hide();
					//$(this).find("a").text("More");
				  },					  
				  function () {
					$(this).find("img").attr({"src":"images/main/bullet_toggle_minus.png"});
					//$(this).find("a").text("Less");
					$(this).next(".facetItems").show();
				  }
				  
				);

				$("#facetContainer input[type='checkbox']").bind("click",function(){
																				  
					
					generateFacetString();
					processClickAction("fromFacets");
						 
						 
				});

			});
			function generateFacetString(){
			facetsString="";
			var count=0;
					$.each($("div.facet"),function(k,v){
						var facetid=$(this).attr("facetid");
						 var facetname=$(this).attr("facetname");
						var facettype=$(this).attr("facettype");
						if( $(this).find("input[type='checkbox']:checked").length>0){
						facetsString=facetsString+"&facets["+count+"].id="+facetid;
						facetsString=facetsString+"&facets["+count+"].name="+facetname;
						facetsString=facetsString+"&facets["+count+"].type="+facettype;
						}
						var facetItems=$(this)
						 $.each($(this).find("input[type='checkbox']:checked"),function(s,t){
								var facetDetailsName=$(this).val();
								//alert(facetDetailsName);
								facetsString=facetsString+"&facets["+count+"].facetDetails["+s+"].name="+facetDetailsName;
						 });
						 if( $(this).find("input[type='checkbox']:checked").length>0){
							count++;
						}
						
					});
					facetsString=facetsString.substr(1);
			}
			</script>