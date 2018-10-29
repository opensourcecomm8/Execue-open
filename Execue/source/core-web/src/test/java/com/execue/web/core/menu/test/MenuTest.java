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


package com.execue.web.core.menu.test;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.execue.core.bean.Menu;
import com.execue.core.bean.MenuItem;
import com.execue.core.util.MenuGeneration;
import com.execue.web.core.helper.MenuHelper;

public class MenuTest {

   private static final Logger logger = Logger.getLogger(MenuTest.class);
   private static Menu         menu   = null;

   @BeforeClass
   public static void setup () {
      if (logger.isDebugEnabled()) {
         logger.debug("teting done");
      }
      menu = new MenuGeneration().generateMenu();

   }

   @Test
   public void testValidIdSelection () {
      String id = "MB2";
      MenuItem item = MenuHelper.getMenuItemById(menu, id);
      Assert.assertNotNull(item);
      Assert.assertEquals(item.getId(), id);
      if (logger.isDebugEnabled()) {
         logger.debug(item.getId());
      }

   }

   @Test
   public void testInValidIdSelection () {
      MenuItem item = MenuHelper.getMenuItemById(menu, "Mdd2");
      Assert.assertNull(item);
   }
}
