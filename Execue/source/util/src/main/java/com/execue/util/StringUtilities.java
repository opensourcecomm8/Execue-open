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

import com.execue.util.algorithm.NGramMatcher;
import com.execue.util.algorithm.PorterStemmer;
import com.ibm.icu.text.Normalizer;

public class StringUtilities {

   static {

      // Add the special chars
      /*
       * specialChars.add("."); specialChars.add(","); specialChars.add(";"); specialChars.add("-");
       * specialChars.add("_"); specialChars.add("("); specialChars.add(")"); specialChars.add("*");
       * specialChars.add("/"); specialChars.add("#"); specialChars.add("+"); specialChars.add("&");
       * specialChars.add("'");
       */

   }

   private StringUtilities () {
   }

   public static boolean matchSimilarly (String first, String second) {
      return NGramMatcher.getSimilarity(first, second) >= 0.8;
   }

   public static double getSimilarlityAboveThreshold (String first, String second) {
      double similarity = NGramMatcher.getSimilarity(first, second);
      if (similarity >= 0.8) {
         return similarity;
      }
      return 0.0;
   }

   public static boolean matchBasedOnSelectionCommittee (String first, String second) {
      double sim = NGramMatcher.getSimilarity(first, second);
      if (sim < 0.95) {
         if (sim < 0.8 || !hasSameLinguisticRoot(first, second)) {
            return false;
         }
      } else if (Double.isNaN(sim)) {
         return false;
      }
      return true;
   }

   public static boolean hasSameLinguisticRoot (String first, String second) {
      return PorterStemmer.getStemmedWord(first).equalsIgnoreCase(PorterStemmer.getStemmedWord(second));
   }

   public static String getLinguisticRoot (String str) {
      return PorterStemmer.getStemmedWord(str);
   }

   public static double getMatch (String first, String second) {
      return NGramMatcher.getSimilarity(first, second);
   }

   public static String normalizeForAccentedcharacters (String inString) {
      return Normalizer.normalize(inString, Normalizer.NFD).replaceAll("[^\\p{ASCII}]", "");
   }

}
