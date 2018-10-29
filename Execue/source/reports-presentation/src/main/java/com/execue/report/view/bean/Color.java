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

/**
 * @author Owner
 */
public class Color {

   public int getBlue () {
      return blue;
   }

   public void setBlue (int blue) {
      this.blue = blue;
   }

   public int getGreen () {
      return green;
   }

   public void setGreen (int green) {
      this.green = green;
   }

   public int getRed () {
      return red;
   }

   public void setRed (int red) {
      this.red = red;
   }

   private int red;
   private int blue;
   private int green;

}
