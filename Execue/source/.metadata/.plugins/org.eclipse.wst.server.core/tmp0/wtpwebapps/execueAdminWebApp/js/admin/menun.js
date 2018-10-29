function mmLoadMenus() {
  
  if (window.mm_menu_0105220345_0) { 
    return;
  }
  
  window.mm_menu_0105220345_0 = new Menu("root",124,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#CCCCCC","#9A9A9A","left","middle",3,1,1000,-5,7,true,false,true,0,false,false);
  mm_menu_0105220345_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Connections","location='../swi/showDataSources.action'");
  mm_menu_0105220345_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Datasets","location='../swi/showAssets.action'");
  mm_menu_0105220345_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Constraints","location='../swi/showConstraints.action'");
  mm_menu_0105220345_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Joins","location='../swi/showJoins.action'");
  mm_menu_0105220345_0.hideOnMouseOut=true;
  mm_menu_0105220345_0.bgColor='#555555';
  mm_menu_0105220345_0.menuBorder=1;
  mm_menu_0105220345_0.menuLiteBgColor='#FFFFFF';
  mm_menu_0105220345_0.menuBorderBgColor='#777777';
  
  window.mm_menu_0113133940_0 = new Menu("root",130,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#CCCCCC","#9A9A9A","left","middle",3,1,1000,-5,7,true,false,true,0,true,false);
  mm_menu_0113133940_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Concepts","location='../swi/showBusinessTerms.action?domain.id=1'");
  mm_menu_0113133940_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Instances","location='../swi/showBusinessTerms.action?domain.id=1'");
  mm_menu_0113133940_0.hideOnMouseOut=true;
  mm_menu_0113133940_0.childMenuIcon="../images/arrows.gif";
  mm_menu_0113133940_0.bgColor='#555555';
  mm_menu_0113133940_0.menuBorder=1;
  mm_menu_0113133940_0.menuLiteBgColor='#FFFFFF';
  mm_menu_0113133940_0.menuBorderBgColor='#777777';
  
  window.mm_menu_0113135351_0 = new Menu("root",140,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#CCCCCC","#9A9A9A","left","middle",3,1,1000,-5,7,true,false,true,0,true,false);
  mm_menu_0113135351_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Mapping","location='../swi/showMappings.action'");
  mm_menu_0113135351_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Dataset&nbsp;Grain","location='../swi/showAssetsGrain.action'");
  mm_menu_0113135351_0.hideOnMouseOut=true;
  mm_menu_0113135351_0.bgColor='#555555';
  mm_menu_0113135351_0.menuBorder=1;
  mm_menu_0113135351_0.menuLiteBgColor='#FFFFFF';
  mm_menu_0113135351_0.menuBorderBgColor='#777777';

  window.mm_menu_0113135822_0 = new Menu("root",124,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#CCCCCC","#9A9A9A","left","middle",3,1,1000,-5,7,true,false,true,0,true,false);
  mm_menu_0113135822_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Profiles","location='../swi/showProfiles.action?domainId=1'");
  mm_menu_0113135822_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Ranges","location='../swi/showRanges.action?domainID=1'");
  mm_menu_0113135822_0.addMenuItem("&nbsp;&nbsp;Parallel&nbsp;Words","location='../swi/showParallelWords.action'");
  mm_menu_0113135822_0.hideOnMouseOut=true;
  mm_menu_0113135822_0.childMenuIcon="../images/arrows.gif";
  mm_menu_0113135822_0.bgColor='#555555';
  mm_menu_0113135822_0.menuBorder=1;
  mm_menu_0113135822_0.menuLiteBgColor='#FFFFFF';
  mm_menu_0113135822_0.menuBorderBgColor='#777777';
   
  window.mm_menu_0113140100_0 = new Menu("root",153,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#CCCCCC","#9A9A9A","left","middle",3,1,1000,-5,7,true,false,true,0,true,false);
  mm_menu_0113140100_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;configuration","location='#'");  
  mm_menu_0113140100_0.addMenuItem("&nbsp;&nbsp;Build&nbsp;Custom&nbsp;Cubes","location='../swi/showCubeRequest.action'");
  mm_menu_0113140100_0.addMenuItem("&nbsp;&nbsp;Build&nbsp;Custom&nbsp;Datamarts","location=''");
  mm_menu_0113140100_0.hideOnMouseOut=true;
  mm_menu_0113140100_0.bgColor='#555555';
  mm_menu_0113140100_0.menuBorder=1;
  mm_menu_0113140100_0.menuLiteBgColor='#FFFFFF';
  mm_menu_0113140100_0.menuBorderBgColor='#777777';

  window.mm_menu_0113140235_0 = new Menu("root",180,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#CCCCCC","#9A9A9A","left","middle",3,1,1000,-5,7,true,false,true,0,false,false);
  mm_menu_0113140235_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Roles","location='../swi/showRoles.action'");  
  mm_menu_0113140235_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Users","location='../swi/showUsers.action'");
  mm_menu_0113140235_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Groups","location='../swi/showGroups.action'");
  mm_menu_0113140235_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Access&nbsp;Definitions","location='#'");
  mm_menu_0113140235_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;System&nbsp;Configuration","location='#'");
  mm_menu_0113140235_0.addMenuItem("&nbsp;&nbsp;Manage&nbsp;Application&nbsp;Configuration","location='#'");
  mm_menu_0113140235_0.hideOnMouseOut=true;
  mm_menu_0113140235_0.bgColor='#555555';
  mm_menu_0113140235_0.menuBorder=1;
  mm_menu_0113140235_0.menuLiteBgColor='#FFFFFF';
  mm_menu_0113140235_0.menuBorderBgColor='#777777';
   
  window.mm_menu_0113140999_0 = new Menu("root",100,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#D7E3E3","#386C93","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_0113140999_0.addMenuItem("Contains","location='javascript:containsString(1);'");
  mm_menu_0113140999_0.addMenuItem("Starts&nbsp;&nbsp;With","location='javascript:startsWithString(1);'");
  mm_menu_0113140999_0.addMenuItem("Show&nbsp;&nbsp;All","location='javascript:showAll(1);'");
  mm_menu_0113140999_0.hideOnMouseOut=true;
  mm_menu_0113140999_0.bgColor='#555555';
  mm_menu_0113140999_0.menuBorder=1;
  mm_menu_0113140999_0.menuLiteBgColor='#FFFFFF';
  mm_menu_0113140999_0.menuBorderBgColor='#777777';
   
  window.mm_menu_0113140999_1 = new Menu("root",100,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#D7E3E3","#386C93","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_0113140999_1.addMenuItem("Contains","location='javascript:containsString(2);'");
  mm_menu_0113140999_1.addMenuItem("Starts&nbsp;&nbsp;With","location='javascript:startsWithString(2);'");
  mm_menu_0113140999_1.addMenuItem("Show&nbsp;&nbsp;All","location='javascript:showAll(2);'");
  mm_menu_0113140999_1.hideOnMouseOut=true;
  mm_menu_0113140999_1.bgColor='#555555';
  mm_menu_0113140999_1.menuBorder=1;
  mm_menu_0113140999_1.menuLiteBgColor='#FFFFFF';
  mm_menu_0113140999_1.menuBorderBgColor='#777777';
   
  window.mm_menu_0113140999_3 = new Menu("root",100,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#D7E3E3","#386C93","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_0113140999_3.addMenuItem("Contains","location='javascript:containsString(3);'");
  mm_menu_0113140999_3.addMenuItem("Starts&nbsp;&nbsp;With","location='javascript:startsWithString(3);'");
  mm_menu_0113140999_3.addMenuItem("Show&nbsp;&nbsp;All","location='javascript:showAll(3);'");
  mm_menu_0113140999_3.hideOnMouseOut=true;
  mm_menu_0113140999_3.bgColor='#555555';
  mm_menu_0113140999_3.menuBorder=1;
  mm_menu_0113140999_3.menuLiteBgColor='#FFFFFF';
  mm_menu_0113140999_3.menuBorderBgColor='#777777';
   
  window.mm_menu_0113140999_4 = new Menu("root",100,24,"Arial, Helvetica, sans-serif",11,"#000000","#FFFFFF","#D7E3E3","#386C93","left","middle",3,0,1000,-5,7,true,false,true,0,false,false);
  mm_menu_0113140999_4.addMenuItem("Contains","location='javascript:containsString(4);'");
  mm_menu_0113140999_4.addMenuItem("Starts&nbsp;&nbsp;With","location='javascript:startsWithString(4);'");
  mm_menu_0113140999_4.addMenuItem("Show&nbsp;&nbsp;All","location='javascript:showAll(4);'");
  mm_menu_0113140999_4.hideOnMouseOut=true;
  mm_menu_0113140999_4.bgColor='#555555';
  mm_menu_0113140999_4.menuBorder=1;
  mm_menu_0113140999_4.menuLiteBgColor='#FFFFFF';
  mm_menu_0113140999_4.menuBorderBgColor='#777777';

  mm_menu_0113140235_0.writeMenus();
} // mmLoadMenus()