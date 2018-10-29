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


package com.execue.web.core.action.swi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class WikiQueryAction extends SWIAction {

   private static String          SITEURL = "http://wiki.semantifi.com/";
   private DocumentBuilderFactory domBuilderFactory;
   private DocumentBuilder        domBuilder;
   private Document               dom;

   public WikiQueryAction () {
      try {
         domBuilderFactory = DocumentBuilderFactory.newInstance();
         domBuilder = domBuilderFactory.newDocumentBuilder();
      } catch (ParserConfigurationException e) {
         e.printStackTrace();
      }
   }

   /**
    * Returns the page number of the asked page name. If return Zero, that means the page doesn't exists.
    * 
    * @param pageName
    * @return int
    */
   public int getPageNumber (String query) {
      int pageNumber = 0;
      try {
         String params = "api.php?action=query&titles=" + query + "&format=xml";
         try {
            InputStream is = doGet(SITEURL + "api.php?", params);
            dom = domBuilder.parse(is);
            Node page = dom.getElementsByTagName("page").item(0).getAttributes().getNamedItem("pageid");
            if (null != page)
               pageNumber = Integer.parseInt(page.getTextContent());
         } catch (SAXException e) {
            e.printStackTrace();
         }

      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return pageNumber;
   }

   private boolean isPageExist (String pageName) {
      boolean doesPageExist = false;
      if (getPageNumber(pageName) > 0)
         doesPageExist = true;
      return doesPageExist;
   }

   /**
    * This method returns the InputStream object after an execution of a post request. Override it to get different return types.
    * 
    * @param url
    * @param params
    * @return
    */
   private InputStream doPost (String url, String params) {
      InputStream connectionInputStream = null;
      try {
         URL postUrl = new URL(url);
         URLConnection connection = postUrl.openConnection();
         connection.setDoOutput(true);
         OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
         writer.write(params);
         writer.flush();
         connectionInputStream = connection.getInputStream();
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

      return connectionInputStream;
   }

   /**
    * This method returns the InputStream object after an execution of a get request. Override it to get different return types.
    * 
    * @param url
    * @param params
    * @return
    */
   private InputStream doGet (String url, String params) {
      InputStream connectionInputStream = null;
      try {
         URL postUrl = new URL(url);
         URLConnection connection = postUrl.openConnection();
         connectionInputStream = connection.getInputStream();
      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

      return connectionInputStream;
   }

   public void createUser () {
   }

   public void createPage (String pageName, String description) {
      try {
         InputStream is = doPost(SITEURL + "api.php?", "action=edit&title=" + pageName + "&text=" + description
                  + "&createonly=true&token=+\\&format=xml");
         BufferedReader bf = new BufferedReader(new InputStreamReader(is));
         StringBuilder sb = new StringBuilder();
         String line = "";
         while ((line = bf.readLine()) != null) {
            sb.append(line);
         }
         System.out.println("\nCreation Msg: " + sb.toString());
         /*
          * try { if (null != is) { dom = domBuilder.parse(is); Node result = dom.getElementsByTagName("login").item(0).getAttributes().getNamedItem("result");
          * System.out.println("\nCreation Msg: " + result.getTextContent()); } } catch (SAXException e) { e.printStackTrace(); }
          */

      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void deletePage () {
   }

   public void login () {
      try {
         InputStream is = doPost(SITEURL + "api.php?", "action=login&lgname=user&lgpassword=password&format=xml");
         try {
            if (null != is) {
               dom = domBuilder.parse(is);
               Node result = dom.getElementsByTagName("login").item(0).getAttributes().getNamedItem("result");
               System.out.println("\nLogin Msg: " + result.getTextContent());
            }
         } catch (SAXException e) {
            e.printStackTrace();
         }

      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

   }

   public void logout () {
      try {
         InputStream is = doPost(SITEURL + "api.php?", "action=logout&format=xml");
         // BufferedReader bf = new BufferedReader(new InputStreamReader(is));
         try {
            if (null != is) {
               dom = domBuilder.parse(is);
               int attributeSize = dom.getElementsByTagName("api").item(0).getAttributes().getLength();
               if (attributeSize == 0)
                  System.out.println("Successfully logged out!");
               else
                  System.out.println("Error occured while logging out!");
            }
         } catch (SAXException e) {
            e.printStackTrace();
         }

      } catch (MalformedURLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void blockUser () {
   }

   public static void main (String[] args) {
      WikiQueryAction wikiQueryAction = new WikiQueryAction();
      // wikiQueryAction.login();
      // wikiQueryAction.logout();
      wikiQueryAction.createPage("test", "this is a test content<br><br> please delete it if you see this page.");
      // System.out.println(wikiQueryAction.getPageNumber("Ratingsss"));
   }
}
