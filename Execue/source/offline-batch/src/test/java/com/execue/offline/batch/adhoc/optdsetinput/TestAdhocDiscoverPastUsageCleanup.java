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


package com.execue.offline.batch.adhoc.optdsetinput;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;

public class TestAdhocDiscoverPastUsageCleanup {

   public static void main (String[] args) throws IOException {
      Set<String> uniqueStrings = new TreeSet<String>();
      fillUniqueStrings("/selects.csv", uniqueStrings, false);
      fillUniqueStrings("/groupbys.csv", uniqueStrings, false);
      fillUniqueStrings("/wheres.csv", uniqueStrings, true);
      
      for (String string : uniqueStrings) {
         System.out.println(string);
      }
      
   }

   private static void fillUniqueStrings (String fileName, Set<String> uniqueStrings, boolean considerPair) throws IOException {
      BufferedReader inStream = null;
      try {
         inStream = new BufferedReader(new InputStreamReader(TestAdhocDiscoverPastUsageCleanup.class.getClass()
                  .getResourceAsStream(fileName)));
         
         String line = null;
         List<String> tempStrings = null;
         
         while ((line = inStream.readLine()) != null) {
            line = line.trim();
            tempStrings = null;
            if (!StringUtils.isBlank(line)) {
               tempStrings = java.util.Arrays.asList(line.split(","));
               for (String tempString : tempStrings) {
                  tempString = tempString.trim();
                  if (considerPair) {
                     tempString = tempString.split("=")[0];
                     tempString = tempString.trim();
                  }
                  uniqueStrings.add(tempString);
               }
            }
         }

      } catch (Exception exp) {
         exp.printStackTrace();
      } finally {
         if (inStream != null) {inStream.close();}
      }
   }
}
