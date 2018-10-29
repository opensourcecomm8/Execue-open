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


/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.execue.report.view.bean;

import java.awt.Font;

/**
 * @author Owner
 */
public class AxisLabel {

   public Font getFont () {
      return font;
   }

   public void setFont (Font font) {
      this.font = font;
   }

   public int getLayoutAngle () {
      return layoutAngle;
   }

   public void setLayoutAngle (int layoutAngle) {
      this.layoutAngle = layoutAngle;
   }

   public String getName () {
      return name;
   }

   public void setName (String name) {
      this.name = name;
   }

   private String name;
   private Font   font;
   private int    layoutAngle; // horizontal, vertical, 45 degrees
   private int    size;

   public int getSize () {
      return size;
   }

   public void setSize (int size) {
      this.size = size;
   }
}
