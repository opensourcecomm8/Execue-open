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


package com.execue.core.common.bean.reports.prsntion;

import java.io.Serializable;

public class HierarchicalReportColumnMetaData implements Serializable {

   private String  name;
   private String  index;
   private int     width  = 180;
   private boolean hidden = false;
   private boolean key    = false;
   private String  align  = "right";

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   public String getIndex () {
      return index;
   }

   public void setIndex (String index) {
      this.index = index;
   }

   public int getWidth () {
      return width;
   }

   public void setWidth (int width) {
      this.width = width;
   }

   public boolean isHidden () {
      return hidden;
   }

   public void setHidden (boolean hidden) {
      this.hidden = hidden;
   }

   public boolean isKey () {
      return key;
   }

   public void setKey (boolean key) {
      this.key = key;
   }

   public String getAlign () {
      return align;
   }

   public void setAlign (String align) {
      this.align = align;
   }

}
