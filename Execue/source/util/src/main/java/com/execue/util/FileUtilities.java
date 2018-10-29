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


package com.execue.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.RandomStringUtils;

import com.execue.core.util.ExecueCoreUtil;

/**
 * @author Abhijit
 * @since Apr 2, 2009 : 1:17:14 PM
 */
public class FileUtilities {

   public static void writeToFile (String str, String fileName) throws IOException {
      BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
      out.write(str);
      out.close();
   }

   /**
    * This method will return the path of the first file in zip folder
    * 
    * @param sourceFileWithPath
    * @param destinationPath
    * @return
    */
   public static String unzipFile (String sourceFileWithPath, String destinationPath) {
      ZipEntry zipEntry = null;
      String actualFileName = null;

      try {
         ZipInputStream zis = new ZipInputStream(new FileInputStream(sourceFileWithPath));

         while ((zipEntry = zis.getNextEntry()) != null) {
            //System.out.println("main(), Zip Entry       : " + zipEntry.getName());

            actualFileName = getValidFileName(zipEntry);

            //System.out.println("main(), actualFileName  : " + actualFileName);

            if (actualFileName == null) {
              // System.out.println("main(), Skipping invalid/not-required entry : " + zipEntry.getName());
              // System.out.println();
               continue;
            }

            //System.out.println("main(), Actual Size     : " + zipEntry.getSize());
            //System.out.println("main(), Compressed Size : " + zipEntry.getCompressedSize());
            //System.out.println("Uncompressing ...");
            actualFileName = ExecueCoreUtil.getFormattedSystemCurrentMillis() + "_"
                     + RandomStringUtils.randomAlphabetic(3) + "_" + actualFileName;
            actualFileName = destinationPath + "/" + actualFileName;
            extractFile(actualFileName, zis);
           // System.out.println("Uncompressed ...");
           // System.out.println();

            // NOTE : Break here, if we want only one file out of it

            break;
         }
         zis.close();
      } catch (IOException e) {
         e.printStackTrace();
      }
      return actualFileName;
   }

   private static void extractFile (String actualFileName, ZipInputStream zis) throws IOException {
      int size;
      byte[] buffer = new byte[2048];

      BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(actualFileName), buffer.length);

      while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
         bos.write(buffer, 0, size);
      }

      bos.flush();
      bos.close();
   }

   private static String getValidFileName (ZipEntry zipEntry) {
      String validFileName = null;
      String tempValidFileName = null;
      tempValidFileName = getActualFileName(zipEntry.getName());
      if (!isDirectory(zipEntry, tempValidFileName)) {
         validFileName = tempValidFileName;
      }
      return validFileName;
   }

   private static String getActualFileName (String inFileName) {
      String actualFileName = inFileName.trim();
      int length = actualFileName.length();
      int lastIndex = actualFileName.lastIndexOf("\\");
      if (lastIndex <= 0) {
         lastIndex = actualFileName.lastIndexOf("/");
      }
      if (lastIndex >= 0 && (lastIndex + 1) != length) {
         actualFileName = actualFileName.substring(lastIndex + 1);
      }
      return actualFileName;
   }

   private static boolean isDirectory (ZipEntry zipEntry, String actualFileName) {
      boolean isDirectory = false;
      if (zipEntry.getName().equals(actualFileName) && zipEntry.getName().contains("/")) {
         isDirectory = true;
      }
      return isDirectory;
   }

}
