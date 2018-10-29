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


package com.execue.nlp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities class on Regular Expression based matchings
 * 
 * @author Raju Gottumukkala
 * @since NLP Version 3.0
 */
public class RegexUtilities {

   /**
    * To restrict the usage as static class
    */
   private RegexUtilities () {

   }

   /**
    * Returns the portions of the inputString which matched the regularExpression
    * 
    * @param regularExpression
    * @param inputString
    * @return list of matchedPortions from inputString
    */
   public static List<String> getMatchedPortions (String regularExpression, String inputString) {
      Pattern pattern = Pattern.compile(regularExpression);
      return getMatchedPortions(pattern, inputString);
   }

   public static List<String> getMatchedPortions (Pattern pattern, String inputString) {
      List<String> matchedPortions = new ArrayList<String>();
      Matcher matcher = pattern.matcher(inputString);
      while (matcher.find()) {
         matchedPortions.add(matcher.group());
      }
      return matchedPortions;
   }

   public static boolean matches (Pattern compiledPattern, String inputString) {
      Matcher matcher = compiledPattern.matcher(inputString);
      return matcher.matches();
   }

   public static boolean matches (String regularExpression, String inputString) {
      return matches(Pattern.compile(regularExpression), inputString);
   }

   public static int getNumberOfMatches (String expression, String input) {
      int count = 0;
      Pattern pattern = Pattern.compile(expression);
      Matcher matcher = pattern.matcher(input);
      while (matcher.find()) {
         count++;
      }
      return count;
   }

   public static void main (String[] args) {
      String str = "Token@word:2000#type:Year#concept:null#nlpTag:OTI#behavior:ATTRIBUTE~QUANTITATIVE~ENUMERATION~";
      str = "Token@word:NominalYear#type:TimeFrame#concept:NominalYear#nlpTag:OC#behavior:ENUMERATION~ATTRIBUTE~QUANTITATIVE~DISTRIBUTION~ Token@word:Operator3#type:Operator#concept:Operator#nlpTag:ORTI#behavior:ATTRIBUTE~QUANTITATIVE~ENUMERATION~ Token@word:2008#type:Year#concept:null#nlpTag:OTI#behavior:ATTRIBUTE~QUANTITATIVE~ENUMERATION~";
      String regex = "Token@word:[yY][eE][aA][rR]\\s*\\d+#type:Year#concept:\\w+#nlpTag:(OTI|OI)#behavior:[\\w~]+(\\s)?";
      Pattern pattern = Pattern.compile(regex.toLowerCase());
      Matcher matcher = null;

      matcher = pattern.matcher(str.toLowerCase());
      while (matcher.find()) {
         System.out.println("1. Match : " + matcher.group().trim());
      }
   }
}
