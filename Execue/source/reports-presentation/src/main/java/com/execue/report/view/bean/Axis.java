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
public class Axis {

   private String    position;     // X,Y,Y1,Y2
   private AxisScale axisScale;
   private Color     positionColor;
   private AxisLabel axisLabel;

   public String getPosition () {
      return position;
   }

   public void setPosition (String position) {
      this.position = position;
   }

   public AxisScale getAxisScale () {
      return axisScale;
   }

   public void setAxisScale (AxisScale axisScale) {
      this.axisScale = axisScale;
   }

   public Color getPositionColor () {
      return positionColor;
   }

   public void setPositionColor (Color positionColor) {
      this.positionColor = positionColor;
   }

   public AxisLabel getAxisLabel () {
      return axisLabel;
   }

   public void setAxisLabel (AxisLabel axisLabel) {
      this.axisLabel = axisLabel;
   }

}
