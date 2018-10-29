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


package com.execue.web.core.util;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

public class SubmitButtonTagImpl implements Tag, Serializable {

   private static final long serialVersionUID = -7580184297858153105L;
   private Tag               tag;
   private PageContext       pageContext;
   private int               height;
   private int               width;
   private String            text;
   private String            bgColor;
   private String            border;
   private String            fontFamily;
   private int               fontSize;

   public int doEndTag () throws JspException {
      return EVAL_PAGE;
   }

   public int doStartTag () throws JspException {
      try {
         // change it based on the bgcolor defined as parameter.
         String imageUrl = "../images/plain_Button.gif";
         pageContext.getOut().write(
                  "<input type=\"button\" value=\"" + text + "\" style=\"background:url(" + imageUrl
                           + ")  repeat 0px 4px;" + " height:" + height + "px; width:" + width + "px; border:"
                           + border + "; font-family:" + fontFamily + "; font-size:" + fontSize + "pt; \"/>");

      } catch (IOException e) {
         e.printStackTrace();
         throw new JspException(e.getMessage(), e);
      }
      return SKIP_BODY;
   }

   public Tag getParent () {
      return getTag();
   }

   public void release () {
      pageContext = null;
      tag = null;
   }

   public void setPageContext (PageContext pageContext) {
      this.pageContext = pageContext;
   }

   public void setParent (Tag parent) {
      this.tag = parent;
   }

   public Tag getTag () {
      return tag;
   }

   public void setTag (Tag tag) {
      this.tag = tag;
   }

   public PageContext getPageContext () {
      return pageContext;
   }

   public int getHeight () {
      return height;
   }

   public void setHeight (int height) {
      this.height = height;
   }

   public int getWidth () {
      return width;
   }

   public void setWidth (int width) {
      this.width = width;
   }

   public String getText () {
      return text;
   }

   public void setText (String text) {
      this.text = text;
   }

   public String getBgColor () {
      return bgColor;
   }

   public void setBgColor (String bgColor) {
      this.bgColor = bgColor;
   }

   public String getBorder () {
      return border;
   }

   public void setBorder (String border) {
      this.border = border;
   }

   public String getFontFamily () {
      return fontFamily;
   }

   public void setFontFamily (String fontFamily) {
      this.fontFamily = fontFamily;
   }

   public int getFontSize () {
      return fontSize;
   }

   public void setFontSize (int fontSize) {
      this.fontSize = fontSize;
   }

}
