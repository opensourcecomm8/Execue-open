/**
 * Licensed to the Execue Software Foundation (ESF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ESF licenses this file
 * to you under the Execue License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. 
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.execue.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.execue.core.bean.Menu;
import com.execue.core.bean.MenuItem;
import com.execue.core.bean.Role;
import com.thoughtworks.xstream.XStream;

public class MenuGeneration {

   public Menu generateMenu () {
      Menu menu = null;
      try {
         InputStream is = getClass().getResourceAsStream("/menu.xml");
         String inputXML = ExecueCoreUtil.readFileAsString(is);
         XStream xstream = new XStream();
         xstream.alias("Menu", Menu.class);
         xstream.alias("MenuItem", MenuItem.class);
         xstream.alias("Role", Role.class);
         xstream.addImplicitCollection(Menu.class, "menuItems");
         menu = (Menu) xstream.fromXML(inputXML);
         // TODO: -KA- Need to remove if MenuId are referred outside.
         generateIds(menu);
      } catch (IOException exception) {
         exception.printStackTrace();
      }
      return menu;
   }

   private void generateIds (Menu menu) {
      generateIdForMenuItems(menu.getMenuItems(), "ME", new LinkedList<String>());
   }

   private void generateIdForMenuItems (List<MenuItem> items, String startString, LinkedList<String> parentIds) {
      int count = 0;
      for (MenuItem menuItem : items) {
         String id = startString + count;
         menuItem.setId(id);
         List<String> parentIdList = new ArrayList<String>();
         parentIdList.addAll(parentIds);
         menuItem.setParentIds(parentIdList);
         if (!menuItem.isLeaf()) {
            parentIds.addLast(id);
         }
         if (menuItem.getMenuItems() != null) {
            generateIdForMenuItems(menuItem.getMenuItems(), id, parentIds);
         }
         count++;
      }

      if (parentIds.size() > 0)
         parentIds.removeLast();

   }

}
