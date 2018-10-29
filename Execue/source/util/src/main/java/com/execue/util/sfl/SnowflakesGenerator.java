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


package com.execue.util.sfl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.util.ExecueStringUtil;

/**
 * @author Abhijit
 * @since Apr 2, 2009 : 10:45:57 AM
 */
public class SnowflakesGenerator {

   static List<String> specialChars = new ArrayList<String>();

   public static String generateSnowFlakesForConcepts (BufferedWriter bufTerms, BufferedWriter bufTokens,
            Set<String> conceptList) throws SQLException, ClassNotFoundException, IOException {
      Map<String, Integer> secondaryWordWeightMap = new HashMap<String, Integer>();

      int termId = 0;
      int termTokenId = 0;
      for (String key : conceptList) {
         key = ExecueStringUtil.removeExtraWhiteSpaces(key.trim());
         Set<String> possibleTerms = handleSpecialChars(key);
         for (String value : possibleTerms) {
            key = value;
            key = ExecueStringUtil.mergeSingleCharsIfExists(key);
            String[] tokens = key.split(" ");
            if (tokens.length <= 1) {
               continue;
            }

            termId++;

            int weightAccountedFor = 0;
            Map<String, Integer> weightMap = new HashMap<String, Integer>(1);
            for (int i = 1; i < tokens.length; i++) {
               String str = tokens[i];
               if (str.trim().length() == 0) {
                  continue;
               }
               if (secondaryWordWeightMap.containsKey(str)) {
                  weightMap.put(str, secondaryWordWeightMap.get(str));
                  weightAccountedFor = weightAccountedFor + secondaryWordWeightMap.get(str);
               }
            }
            double individualWeight = (100 - weightAccountedFor) / (tokens.length - weightMap.size());
            weightAccountedFor = 0;

            for (int tokenOrder = 1; tokenOrder < tokens.length; tokenOrder++) {
               String str = tokens[tokenOrder].trim();
               if (str.trim().length() == 0) {
                  continue;
               }

               int weight = (int) individualWeight;

               if (weightMap.containsKey(str)) {
                  weight = weightMap.get(str);
               }
               termTokenId++;

               bufTokens.write(termTokenId + "\t" + termId + "\t" + tokens[tokenOrder] + "\t0\t" + weight + "\t1\t"
                        + tokenOrder);
               bufTokens.newLine();
               weightAccountedFor += weight;
            }
            termTokenId++;
            bufTokens.write(termTokenId + "\t" + termId + "\t" + tokens[0] + "\t0\t" + (100 - weightAccountedFor)
                     + "\t1\t0");
            bufTokens.newLine();
            bufTerms.write(termId + "\t" + key);
            bufTerms.newLine();
         }
      }
      return termTokenId + "," + termId;
   }

   public static String generateSnowFlakesForRelations (BufferedWriter bufTerms, BufferedWriter bufTokens,
            Set<String> relationList, int id, int count) throws SQLException, ClassNotFoundException, IOException {
      Map<String, Integer> secondaryWordWeightMap = new HashMap<String, Integer>();

      int termId = count;
      int termTokenId = id;
      for (String key : relationList) {
         key = ExecueStringUtil.removeExtraWhiteSpaces(key.trim());
         Set<String> possibleTerms = handleSpecialChars(key);
         for (String value : possibleTerms) {
            key = value;
            key = ExecueStringUtil.mergeSingleCharsIfExists(key);
            String[] tokens = key.split(" ");
            if (tokens.length <= 1) {
               continue;
            }

            termId++;

            int weightAccountedFor = 0;
            Map<String, Integer> weightMap = new HashMap<String, Integer>(1);
            for (int i = 1; i < tokens.length; i++) {
               String str = tokens[i];
               if (str.trim().length() == 0) {
                  continue;
               }
               if (secondaryWordWeightMap.containsKey(str)) {
                  weightMap.put(str, secondaryWordWeightMap.get(str));
                  weightAccountedFor = weightAccountedFor + secondaryWordWeightMap.get(str);
               }
            }
            double individualWeight = (100 - weightAccountedFor) / (tokens.length - weightMap.size());
            weightAccountedFor = 0;

            for (int tokenOrder = 1; tokenOrder < tokens.length; tokenOrder++) {
               String str = tokens[tokenOrder].trim();
               if (str.trim().length() == 0) {
                  continue;
               }

               int weight = (int) individualWeight;

               if (weightMap.containsKey(str)) {
                  weight = weightMap.get(str);
               }
               termTokenId++;

               bufTokens.write(termTokenId + "\t" + termId + "\t" + tokens[tokenOrder] + "\t0\t" + weight + "\t1\t"
                        + tokenOrder);
               bufTokens.newLine();
               weightAccountedFor += weight;
            }
            termTokenId++;
            bufTokens.write(termTokenId + "\t" + termId + "\t" + tokens[0] + "\t0\t" + (100 - weightAccountedFor)
                     + "\t1\t0");
            bufTokens.newLine();
            bufTerms.write(termId + "\t" + key);
            bufTerms.newLine();
         }
      }
      return termTokenId + "," + termId;
   }

   public static String generateSnowFlakesForConcepts (BufferedWriter bufTerms, BufferedWriter bufTokens, long modelId,
            int startIndex) throws SQLException, ClassNotFoundException, IOException {
      Set<String> conceptList = DataLoader.loadDisplayConceptsSetFromDB(modelId, startIndex);
      // Set<String> conceptList = DataLoader.loadDisplayConceptsSetFromDBIgnoreExisting(0);

      return generateSnowFlakesForConcepts(bufTerms, bufTokens, conceptList);
   }

   public static String generateSnowFlakesForRelations (BufferedWriter bufTerms, BufferedWriter bufTokens,
            long modelId, int startIndex, int id, int count) throws SQLException, ClassNotFoundException, IOException {
      Set<String> relationsList = DataLoader.loadDisplayRelationsSetFromDB(modelId, startIndex);
      // Set<String> conceptList = DataLoader.loadDisplayConceptsSetFromDBIgnoreExisting(0);

      return generateSnowFlakesForRelations(bufTerms, bufTokens, relationsList, id, count);
   }

   public static void generateSnowFlakesForInstances (BufferedWriter bufTerms, BufferedWriter bufTokens, int id,
            int count) throws SQLException, IOException, ClassNotFoundException {
      // Set<String> instanceList = DataLoader.loadDisplayInstancesSetFromDB();
      Set<String> instanceList = DataLoader.loadDisplayInstancesSetFromDBIgnoreExisting();

      generateSnowFlakesForInstances(bufTerms, bufTokens, null, id, count, instanceList);
   }

   public static void generateSnowFlakesForInstanceDescriptionsAndDisplayNames (BufferedWriter bufferTerms,
            BufferedWriter bufferTokens, long modelId, int startIndex, int id, int count)
            throws ClassNotFoundException, SQLException, IOException {
      Set<String> instanceList = DataLoader.loadInstanceDescriptions(modelId, startIndex);

      String ids = generateSnowFlakesForInstances(bufferTerms, bufferTokens, null, id, count, instanceList);

      instanceList = DataLoader.loadInstanceDisplayName(modelId, startIndex);
      System.out.println(ids);
      ids = generateSnowFlakesForInstances(bufferTerms, bufferTokens, null, Integer.parseInt(ids.split(",")[0]),
               Integer.parseInt(ids.split(",")[1]), instanceList);

   }

   public static String generateSnowFlakesForInstances (BufferedWriter bufTerms, BufferedWriter bufTokens,
            BufferedWriter ontoTerms, int id, int count, Set<String> instanceList) throws SQLException, IOException,
            ClassNotFoundException {
      String[] ignoreWordsArray = { "a", "an", "the", "of", "on", "at", "with", "from", "for", "after", "though",
               "before", "until", "unless", "since", "or", "in" };
      List<String> ignoreWords = Arrays.asList(ignoreWordsArray);

      StringBuffer sb = new StringBuffer();
      for (String val : instanceList) {
         Set<String> values = handleSpecialChars(val);
         String originalVal = val;
         System.out.println("Processing [" + originalVal + "]");
         for (String term : values) {
            val = term;
            val = val.trim();
            val = ExecueStringUtil.removeExtraWhiteSpaces(val);
            val = ExecueStringUtil.mergeSingleCharsIfExists(val);
            if (val.trim().length() == 0) {
               continue;
            }

            List<String> tokens = ExecueStringUtil.getAsList(val);

            // Skip single word sfls
            if (tokens.size() <= 1) {
               continue;
            }

            count++;

            double possibleWeight = 100 / tokens.size();

            int weightAccountedFor = 0;
            Map<String, Integer> weightMap = new HashMap<String, Integer>(1);
            for (String str : tokens) {
               if (str.trim().length() == 0) {
                  continue;
               }
            }

            double[] multiplers = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

            // double[] multiplers = {1, 0.9, 0.85, 0.8, 0.7, 0.65, 0.6, 0.5, 0.45, 0.4, 0.35, 0.3};

            double divider = 0;
            int multiplierPos = 0;
            for (int i = 0; i < tokens.size() - weightMap.size(); i++) {
               if (i < multiplers.length) {
                  multiplierPos = i;
               }
               divider = divider + multiplers[multiplierPos];
            }
            double individualWeight = (100 - weightAccountedFor) / divider;
            weightAccountedFor = 0;
            multiplierPos = 0;
            for (int tokenOrder = 0; tokenOrder < tokens.size() - 1; tokenOrder++) {
               if (tokenOrder < multiplers.length) {
                  multiplierPos = tokenOrder;
               }
               String str = tokens.get(tokenOrder);
               if (str.trim().length() == 0) {
                  continue;
               }

               int weight = (int) (individualWeight * multiplers[multiplierPos]);

               if (weightMap.containsKey(str)) {
                  weight = weightMap.get(str);
               }
               id++;
               bufTokens.write(id + "\t" + count + "\t" + tokens.get(tokenOrder).replaceAll(",", " ") + "\t0\t"
                        + weight + "\t1\t" + tokenOrder);
               bufTokens.newLine();
               weightAccountedFor += weight;
            }

            if (weightAccountedFor > 100) {
               System.out.println("Error in weights for " + val);
            } else {

               int remainingWeight = 100 - weightAccountedFor;
               id++;
               bufTokens.write(id + "\t" + count + "\t" + tokens.get(tokens.size() - 1).replaceAll(",", " ") + "\t0\t"
                        + remainingWeight + "\t1\t" + (tokens.size() - 1));
               bufTokens.newLine();
               bufTerms.write(count + "\t" + val);
               bufTerms.newLine();
            }
         }
         bufTerms.flush();
         bufTokens.flush();
         if (ontoTerms != null) {
            ontoTerms.flush();
         }
      }
      return id + "," + count;
   }

   /**
    * Method to handle the special character in a string while preparing SFL term. there will be multiple combination
    * for String which contains Special Chars.
    * 
    * @param val
    * @return
    */
   private static Set<String> handleSpecialChars (String val) {
      Set<String> possibleTerms = new HashSet<String>();
      possibleTerms.add(val);
      boolean containsSpecialChar = false;
      for (String specialChar : specialChars) {
         if (val.contains(specialChar)) {
            containsSpecialChar = true;
            val = val.replaceAll("\\" + specialChar, " " + "\\" + specialChar + " ");
         }
      }

      val = ExecueStringUtil.removeExtraWhiteSpaces(val);
      if (containsSpecialChar) {
         possibleTerms = getPossibleTerms(val);
      }
      return possibleTerms;
   }

   /**
    * Methdo to get the Combination of tokens for the String. This is a recursive call with gets possible combination of
    * tokens w with spaces around. e.g >>> "a b c d" --> "ab cd", "a bcd", "abc d" , "ab c d", "a bc d", "a b c d"
    * 
    * @param val
    *           String to tokenize
    * @return Set containing these combinations
    */
   private static Set<String> getPossibleTerms (String val) {
      String[] tokens = val.split(" ");
      Set<String> posiibleTerms = new HashSet<String>();
      String token = tokens[0];
      String remainingString = null;
      if (Character.isLetterOrDigit(token.charAt(0))) {
         remainingString = val.replaceFirst(token, "");
      } else {
         remainingString = val.replaceFirst("\\" + token, "");
      }

      if (remainingString.trim().length() <= 0) {
         posiibleTerms.add(token);
         return posiibleTerms;
      }
      Set<String> restTokens = getPossibleTerms(remainingString.trim());
      for (String newToken : restTokens) {
         String newString = token + " " + newToken;
         posiibleTerms.add(newString);
         posiibleTerms.add(token + newToken);
      }
      return posiibleTerms;
   }

   public static String splitOnUpperCase (String txt) {
      String result = "";
      java.util.regex.Pattern p = java.util.regex.Pattern.compile("[A-Z]{1}[^A-Z]*");
      java.util.regex.Matcher m = p.matcher(txt);
      while (m.find()) {
         result += m.group() + " ";
      }
      return result.trim();
   }

   public static void main (String[] args) throws IOException, SQLException, ClassNotFoundException {
      String termsFileName = "SFLTerm.txt", tokensFileName = "SFLTermToken.txt";
      File output = new File(termsFileName);
      BufferedWriter bufferTerms = new BufferedWriter(new FileWriter(output));
      File output2 = new File(tokensFileName);
      BufferedWriter bufferTokens = new BufferedWriter(new FileWriter(output2));

      // TODO: Commenting out for government spending app snow flakes generation as discussed with kaliki
      // add the special chars
      // specialChars.add(".");
      // specialChars.add(",");
      // specialChars.add(";");
      // specialChars.add("-");
      // specialChars.add("_");
      // specialChars.add("(");
      // specialChars.add(")");
      // specialChars.add("*");
      // specialChars.add("/");
      // specialChars.add("#");
      // specialChars.add("+");
      // specialChars.add("&");
      // specialChars.add("'");

      long modelId = 104L;
      int startIndex = 1000;
      String ids = generateSnowFlakesForConcepts(bufferTerms, bufferTokens, modelId, startIndex);
      System.out.println(ids);
      ids = SnowflakesGenerator.generateSnowFlakesForRelations(bufferTerms, bufferTokens, modelId, startIndex, Integer
               .parseInt(ids.split(",")[0]), Integer.parseInt(ids.split(",")[1]));
      System.out.println(ids);
      generateSnowFlakesForInstanceDescriptionsAndDisplayNames(bufferTerms, bufferTokens, modelId, startIndex, Integer
               .parseInt(ids.split(",")[0]), Integer.parseInt(ids.split(",")[1]));

      bufferTerms.close();
      bufferTokens.close();
   }
}
