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


package com.execue.core.common.bean.reports.view.data;

public class ChartAppearanceLayout {

   private int     WIDTH;
   private int     HEIGHT;
   private int     MINWIDTH;
   private int     PLOTAREAMARGINRIGHT;
   private int     PLOTAREAMARGINLEFT;
   private int     PLOTAREAMARGINTOP;
   private int     PLOTAREAMARGINBOTTOM;
   private short   XAXISLABELANGLE;
   private short   XAXISLABELMAX;
   private String  PLOTAREACOLOR;
   private String  BACKGROUNDCOLOR;
   private boolean ISTOOLBARVISBLE;
   private boolean IMAGEINTERACTIVE;
   private boolean YAXISGRIDVISIBLE;
   private boolean YAXISMINORTICKERVISIBLE;
   private boolean XAXISGRIDVISIBLE;
   private boolean Y2AXISGRIDVISIBLE;
   private boolean Y2AXISMINORTICKERVISIBLE;
   private boolean ISLEGENDBOXVISIBLE;
   private String  CHARTTYPEDEFAULT;
   private String  CHARTTYPECLUSTER;

   public String getCHARTTYPEDEFAULT () {
      return CHARTTYPEDEFAULT;
   }

   public void setCHARTTYPEDEFAULT (String charttypedefault) {
      CHARTTYPEDEFAULT = charttypedefault;
   }

   public String getCHARTTYPECLUSTER () {
      return CHARTTYPECLUSTER;
   }

   public void setCHARTTYPECLUSTER (String charttypecluster) {
      CHARTTYPECLUSTER = charttypecluster;
   }

   public int getWIDTH () {
      return WIDTH;
   }

   public void setWIDTH (int width) {
      WIDTH = width;
   }

   public int getHEIGHT () {
      return HEIGHT;
   }

   public void setHEIGHT (int height) {
      HEIGHT = height;
   }

   public int getMINWIDTH () {
      return MINWIDTH;
   }

   public void setMINWIDTH (int minwidth) {
      MINWIDTH = minwidth;
   }

   public int getPLOTAREAMARGINRIGHT () {
      return PLOTAREAMARGINRIGHT;
   }

   public void setPLOTAREAMARGINRIGHT (int plotareamarginright) {
      PLOTAREAMARGINRIGHT = plotareamarginright;
   }

   public int getPLOTAREAMARGINLEFT () {
      return PLOTAREAMARGINLEFT;
   }

   public void setPLOTAREAMARGINLEFT (int plotareamarginleft) {
      PLOTAREAMARGINLEFT = plotareamarginleft;
   }

   public int getPLOTAREAMARGINTOP () {
      return PLOTAREAMARGINTOP;
   }

   public void setPLOTAREAMARGINTOP (int plotareamargintop) {
      PLOTAREAMARGINTOP = plotareamargintop;
   }

   public int getPLOTAREAMARGINBOTTOM () {
      return PLOTAREAMARGINBOTTOM;
   }

   public void setPLOTAREAMARGINBOTTOM (int plotareamarginbottom) {
      PLOTAREAMARGINBOTTOM = plotareamarginbottom;
   }

   public short getXAXISLABELANGLE () {
      return XAXISLABELANGLE;
   }

   public void setXAXISLABELANGLE (short xaxislabelangle) {
      XAXISLABELANGLE = xaxislabelangle;
   }

   public short getXAXISLABELMAX () {
      return XAXISLABELMAX;
   }

   public void setXAXISLABELMAX (short xaxislabelmax) {
      XAXISLABELMAX = xaxislabelmax;
   }

   public String getPLOTAREACOLOR () {
      return PLOTAREACOLOR;
   }

   public void setPLOTAREACOLOR (String plotareacolor) {
      PLOTAREACOLOR = plotareacolor;
   }

   public String getBACKGROUNDCOLOR () {
      return BACKGROUNDCOLOR;
   }

   public void setBACKGROUNDCOLOR (String backgroundcolor) {
      BACKGROUNDCOLOR = backgroundcolor;
   }

   public boolean isISTOOLBARVISBLE () {
      return ISTOOLBARVISBLE;
   }

   public void setISTOOLBARVISBLE (boolean istoolbarvisble) {
      ISTOOLBARVISBLE = istoolbarvisble;
   }

   public boolean isIMAGEINTERACTIVE () {
      return IMAGEINTERACTIVE;
   }

   public void setIMAGEINTERACTIVE (boolean imageinteractive) {
      IMAGEINTERACTIVE = imageinteractive;
   }

   public boolean isYAXISGRIDVISIBLE () {
      return YAXISGRIDVISIBLE;
   }

   public void setYAXISGRIDVISIBLE (boolean yaxisgridvisible) {
      YAXISGRIDVISIBLE = yaxisgridvisible;
   }

   public boolean isYAXISMINORTICKERVISIBLE () {
      return YAXISMINORTICKERVISIBLE;
   }

   public void setYAXISMINORTICKERVISIBLE (boolean yaxisminortickervisible) {
      YAXISMINORTICKERVISIBLE = yaxisminortickervisible;
   }

   public boolean isXAXISGRIDVISIBLE () {
      return XAXISGRIDVISIBLE;
   }

   public void setXAXISGRIDVISIBLE (boolean xaxisgridvisible) {
      XAXISGRIDVISIBLE = xaxisgridvisible;
   }

   public boolean isY2AXISGRIDVISIBLE () {
      return Y2AXISGRIDVISIBLE;
   }

   public void setY2AXISGRIDVISIBLE (boolean y2axisgridvisible) {
      Y2AXISGRIDVISIBLE = y2axisgridvisible;
   }

   public boolean isY2AXISMINORTICKERVISIBLE () {
      return Y2AXISMINORTICKERVISIBLE;
   }

   public void setY2AXISMINORTICKERVISIBLE (boolean y2axisminortickervisible) {
      Y2AXISMINORTICKERVISIBLE = y2axisminortickervisible;
   }

   public boolean isISLEGENDBOXVISIBLE () {
      return ISLEGENDBOXVISIBLE;
   }

   public void setISLEGENDBOXVISIBLE (boolean islegendboxvisible) {
      ISLEGENDBOXVISIBLE = islegendboxvisible;
   }

}
