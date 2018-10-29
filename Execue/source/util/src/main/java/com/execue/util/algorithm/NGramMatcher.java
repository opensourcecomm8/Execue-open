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
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.execue.util.algorithm;

/**
 * User: Abhijit Date: Jun 11, 2005 Time: 2:25:52 PM
 */
/*
 * NGramMatcher.java This Class implements an NGram based algorithm to find Similarity.
 */

import java.util.Vector;

import com.execue.core.common.bean.algorithm.Gram;
import com.execue.core.common.bean.algorithm.NGram;
import com.execue.core.util.ExecueCoreUtil;

/**
 * This class implements an NGram based Match algorithm. NGram for the Strings to be matched are calculated and Dice
 * Coefficient is applied to find the Similarity.
 * 
 * @author Abhijit Patil
 * @version 1.0
 */
public class NGramMatcher {

   static int ngram;

   /**
    * Initializes the NGram Matcher with value of "n" = 2
    */
   public NGramMatcher () {
      ngram = 2;
   }

   /**
    * This method takes as argument, two strings to be matched and returns their Match Score.<p/> It preprocesses the
    * Strings, finds there NGrams and applies DICE COEFFICIENT to the NGram vectors to find the similarity.<p/>
    * 
    * @param str1
    *           String to be matched
    * @param str2
    *           String to be matched
    * @return Match Value
    */
   public static double compare (String str1, String str2) {
      // Clean strings

      str1 = str1.toLowerCase();
      str2 = str2.toLowerCase();

      String str = " _";
      char blank = str.charAt(0);
      char underline = str.charAt(1);

      str1 = str1.replace(blank, underline);
      str2 = str2.replace(blank, underline);

      // str1 = PreProcessText.preProcess(str1);
      // str2 = PreProcessText.preProcess(str2);

      // str1 = PorterStemmer.stem(str1) ;
      // str2 = PorterStemmer.stem(str2) ;

      // Find the NGram Vector for first String str1

      NGram nGram = new NGram(str1, ngram);

      Vector gram_vect1 = nGram.getNGramVector();

      // Find the NGram Vector for first String str2

      nGram.setNGramString(str2);
      Vector gram_vect2 = nGram.getNGramVector();

      // Dice Coeficient

      // Get the intersection set of NGram vecotrs

      Vector intersection = intersection(gram_vect1, gram_vect2);

      // Find the Similarity

      return 2 * intersection.size() / (double) (gram_vect1.size() + gram_vect2.size());
   }

   /**
    * This method sets the value of "n" for calculating NGram Vector.
    * 
    * @param val
    *           Value of N for NGram
    */
   public static void setNGramValue (int val) {
      ngram = val;
   }

   /**
    * This method takes as argument, two NGram Vectors and calcualtes the intersection of the two NGram vectors.
    * 
    * @param a
    *           NGram Vector
    * @param b
    *           NGram Vector
    * @return Vector contaning intersection of the Vectors a and b
    */
   private static Vector intersection (Vector a, Vector b) {
      Vector intersection = new Vector();

      for (int i = 0; i < a.size(); i++) {
         for (int j = 0; j < b.size(); j++) {
            Gram gram = (Gram) b.get(j);
            if (gram.getGramString().equals(((Gram) a.get(i)).getGramString())) {
               intersection.addElement(gram);
               break;
            }
         }
      }
      return intersection;
   }

   /**
    * It gets the similarity for different "n" values and then gets its average to avoid false positive matches.
    * 
    * @param str1
    *           String to be compared
    * @param str2
    *           String to be compared
    * @return match value between 0 and 1
    */
   public static double getSimilarityOld (String str1, String str2) {
      double similarity;
      int count = 1;

      if (str1.equalsIgnoreCase(str2)) {
         return 1.0;
      }

      setNGramValue(2);
      double sim2 = compare(str1, str2);
      double sim3 = 0.0;
      double sim4 = 0.0;

      if (str1.length() > 2 && str2.length() > 2) {
         setNGramValue(3);
         sim3 = compare(str1, str2);
         count++;
      }

      if (str1.length() > 3 && str2.length() > 3) {
         setNGramValue(4);
         sim4 = compare(str1, str2);
         count++;
      }

      similarity = (sim2 + sim3 + sim4) / count;

      return similarity;
   }

   /**
    * It gets the similarity for different "n" values and then gets its average to avoid false positive matches.
    * 
    * @param str1
    *           String to be compared
    * @param str2
    *           String to be compared
    * @return match value between 0 and 1
    */
   public static double getSimilarity (String str1, String str2) {
      double similarity = 0.0;
      if (str1 == null || str2 == null)
         return similarity;

      str1 = str1.trim().toLowerCase();
      str2 = str2.trim().toLowerCase();

      boolean isNumber1 = false;
      boolean isNumber2 = false;

      if (ExecueCoreUtil.isNumber(str1) || ExecueCoreUtil.isDouble(str1)) {
         isNumber1 = true;
      }
      if (ExecueCoreUtil.isNumber(str2) || ExecueCoreUtil.isDouble(str2)) {
         isNumber2 = true;
      }

      if ((!isNumber1 && isNumber2) || (isNumber1 && !isNumber2))
         return similarity;

      if (str1.equalsIgnoreCase(str2)) {
         return 1.0;
      }

      int len1 = str1.length();
      int len2 = str2.length();
      int nValue;

      if (len1 < len2)
         nValue = len1 / 2;
      else
         nValue = len2 / 2;

      int len = nValue + 1;
      for (int k = 2; k <= len; k++) {
         setNGramValue(k);
         similarity += compare(str1, str2);
      }

      if (nValue == 0)
         similarity = 0;
      else {
         similarity = similarity / nValue;
      }

      return similarity;
   }
}