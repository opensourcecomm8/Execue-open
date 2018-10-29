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


package com.execue.core.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class ExecueStringUtil implements ExecueUtilConstant {

   public static String NON_ASCII_CHAR_MATCH_REGEX = "[^(\\x20-\\x7F)\\s\\t\\n\\r]";

   /**
    * Trims the inputString passed to the maximum of lengthRequested.<br/> Characters from the middle of the string are
    * taken off to make the inputString to the lengthRequested
    * 
    * @param inputString
    * @param lengthRequested
    * @return String - corrected inputString for lengthRequested
    */
   public static String trimStringToLength (String inputString, int lengthRequested) {
      String trimmedString = inputString;
      int curLength = trimmedString.length();
      boolean isOfCorrectLength = false;
      if (curLength > lengthRequested) {
         /*
          * Keep taking off the characters from the middle of the string till input string is less than or equal to
          * lengthRequested
          */
         while (!isOfCorrectLength) {
            if (curLength != lengthRequested) {
               trimmedString = trimmedString.substring(0, curLength / 2) + trimmedString.substring(curLength / 2 + 1);
               curLength = trimmedString.length();
            } else {
               isOfCorrectLength = true;
            }
         }
      }
      return trimmedString;
   }

   /**
    * Breaks the input string wherever a capital letter is encountered and adds a space. <BR>
    * eg: NetIncome -> Net Income
    * 
    * @param String
    *           the input string
    */
   public static String addSpaceAtUpperCase (String str) {
      StringBuffer sb = new StringBuffer();
      char parts[] = str.toCharArray();
      for (int i = 0; i < parts.length; i++) {
         if (Character.isUpperCase(parts[i])) {
            sb.append(' ');
         }
         sb.append(parts[i]);
      }
      return sb.toString().trim();
   }

   public static String trimStringToLengthFromEnd (String inputString, int lengthRequested) {
      String trimmedString = inputString;
      int curLength = trimmedString.length();
      boolean isOfCorrectLength = false;

      /*
       * Keep taking off the characters from the end of the string till input string is less than or equal to
       * lengthRequested
       */
      boolean fromRight = true;
      while (!isOfCorrectLength) {
         if (curLength > lengthRequested) {
            if (fromRight) {
               trimmedString = trimmedString.substring(0, (curLength - 1));
               fromRight = false;
            } else {
               trimmedString = trimmedString.substring(1);
               fromRight = true;
            }
            curLength = trimmedString.length();
         } else {
            isOfCorrectLength = true;
            if (trimmedString.charAt(0) == '_') {
               trimmedString = trimmedString.substring(1);
            }
            if (trimmedString.charAt(trimmedString.length() - 1) == '_') {
               trimmedString = trimmedString.substring(0, trimmedString.length());
            }
         }
      }
      return trimmedString;
   }

   public static String getNormalizedName (String name, int maxLength) {
      String normalizedString = name;
      normalizedString = StringUtils.remove(normalizedString, " ");
      normalizedString = trimStringToLength(normalizedString, maxLength);
      return normalizedString;
   }

   public static String getNormalizedNameByTrimmingFromMiddle (String name, List<String> existingNames, int maxLength) {
      String normalizedString = name;
      normalizedString = StringUtils.remove(normalizedString, " ");
      normalizedString = trimStringToLength(normalizedString, maxLength);
      int numOfIterations = 0;
      while (ExecueCoreUtil.isNameExists(existingNames, normalizedString)) {
         normalizedString = trimStringToLength(normalizedString, maxLength - ++numOfIterations);
      }
      return normalizedString;
   }

   public static String getTruncatedString (String longString, int maxLength) {
      String shortString = "";
      if (longString.length() > maxLength) {
         String[] tokens = longString.split("\\s");
         if (tokens.length > 1) {
            for (String token : tokens) {
               shortString = shortString + token + " ";
               if (shortString.length() > maxLength + 1) {
                  shortString = shortString.substring(0, shortString.length() - token.length() - 1);
                  break;
               }
            }
            if (shortString.length() == 0) {
               shortString = longString.substring(0, maxLength);
            }
         } else {
            shortString = longString.substring(0, maxLength);
         }
      } else {
         shortString = longString;
      }
      return shortString.trim();
   }

   /**
    * This method takes in 2 arguments one input string for wrapping and the character limit to wrap at. Returns a list
    * of strings which are wrapped to the next character where a SPACE is encountered after max limit of characters
    * specified.
    */
   public static List<String> wrapText (String inputString, int maxCharsAllowed) {

      // start trimming.
      String levelNTrim = inputString;
      List<String> wrapedTextList = new ArrayList<String>();
      int beginIndex = 0;
      // check if the N'th level of trimmed string is longer than allowed
      try {
         while (levelNTrim.length() > maxCharsAllowed) {
            // regex to find spaces
            Pattern specialCharRegEx = Pattern.compile("[^\\S]+");
            Matcher regExMatcher = specialCharRegEx.matcher(levelNTrim);

            boolean regExpressionExist = false;
            regExpressionExist = regExMatcher.find();
            int currentPosition = regExMatcher.start();
            while (regExpressionExist) {
               if (currentPosition >= maxCharsAllowed) {
                  wrapedTextList.add(levelNTrim.substring(beginIndex, currentPosition));
                  // start trimming from the next line
                  levelNTrim = levelNTrim.substring(currentPosition).trim();
                  // break the inner iteration to reset the matcher and
                  // re-do for the next line
                  break;
               }
               // find next match.
               regExpressionExist = regExMatcher.find();
               currentPosition = regExMatcher.start();
            }
         }
      } catch (IllegalStateException e) {
         // No Match found further - let the default code handle the rest of
         // the string.
      }
      // default addition to the list for the leftover characters.
      wrapedTextList.add(levelNTrim);
      return wrapedTextList;
   }

   /**
    * This method replace special character with space
    * 
    * @param inputString
    * @return
    */
   public static String replaceSpecialCharacterWithSpace (String inputString) {
      // TODO-JT-We can improve this method later on
      String outPutString = null;
      if (inputString != null) {
         outPutString = inputString.replace("_", " ");
      }
      return outPutString;
   }

   public static String getNormalizedName (String name) {
      return getNormalizedName(name, DEFAULT_NAME_LENGTH);
   }

   public static boolean isStringDataBaseNull (String str) {
      return str == null || str.equalsIgnoreCase("null");
   }

   public static boolean matches (String first, String second) {
      return first.equalsIgnoreCase(second);
   }

   public static List<String> getAsList (String input) {
      return getAsList(input, " ");
   }

   public static List<String> getAsList (String input, String delimiter) {
      List<String> output = new ArrayList<String>();
      if (input != null) {
         String[] strArr = input.split(delimiter);
         int len = strArr.length;
         for (int index = 0; index < len; index++) {
            output.add(strArr[index].trim());
         }
      }
      return output;
   }

   public static String processForCommasAndSpecialChars (String str) {
      StringTokenizer st = new StringTokenizer(str);
      StringBuffer newStr = new StringBuffer();
      while (st.hasMoreTokens()) {
         String temp = st.nextToken();
         if (Pattern.matches("[\\d]*([,][\\d]*)*(\\.)?(\\d+)", temp)) {
            temp = temp.replaceAll("[,]", "");
         } else if (temp.endsWith(".")) {
            temp = trimTrailingDots(temp);
         } else if (temp.indexOf(",") != -1) {
            temp = temp.replaceAll(",", " , ");
         } else if (temp.indexOf("@") != -1) {
            temp = temp.replaceAll("@", "");
         } else if (temp.indexOf("\\") != -1) {
            temp = temp.replace('\\', ' ');
         } else if (temp.indexOf("{") != -1) {
            temp = temp.replace('{', ' ');
         } else if (temp.indexOf("}") != -1) {
            temp = temp.replace('}', ' ');
         }
         newStr.append(temp).append(" ");
      }
      return newStr.toString();
   }

   /**
    * This method trims the trailing dots in the given str eg: 1) miles... -> miles 2) yahoo.com. -> yahoo.com 3) 1.2 ->
    * 1.2
    * 
    * @param str
    * @return the trimmed string without trailing dots
    */
   public static String trimTrailingDots (String str) {
      String temp = str.trim();
      if (StringUtils.isEmpty(temp)) {
         return temp;
      }
      if (temp.endsWith(".")) {
         temp = trimTrailingDots(temp.substring(0, temp.length() - 1));
      }
      return temp;
   }

   public static String processForPunctuation (String str, String punctuation) {
      StringTokenizer st = new StringTokenizer(str);
      StringBuffer newStr = new StringBuffer();
      while (st.hasMoreTokens()) {
         String temp = st.nextToken();
         String punct = punctuation;
         if (punct.indexOf("\\") != -1) {
            punct = punct.substring(1, punct.length());
         }
         if (temp.indexOf(punct) != -1) {
            temp = temp.replaceAll("\\" + punctuation, ' ' + punct + ' ');
         }
         newStr.append(temp).append(" ");
      }
      return newStr.toString();
   }

   public static String removePunctuation (String str, String punctuation) {
      StringTokenizer st = new StringTokenizer(str);
      StringBuffer newStr = new StringBuffer();
      while (st.hasMoreTokens()) {
         String temp = st.nextToken();
         String punct = punctuation;
         if (punct.indexOf("\\") != -1) {
            punct = punct.substring(1, punct.length());
         }
         if (temp.indexOf(punct) != -1) {
            // if (!Pattern.matches("[\\d]+([-][\\d]+)", temp))
            {
               temp = temp.replaceAll(punctuation, " ");
            }
         }
         newStr.append(temp).append(" ");
      }
      return newStr.toString();
   }

   public static boolean isHyphenExistsInTextString (Object str) {
      Pattern pattern = Pattern.compile("[a-zA-Z]+[-][a-zA-Z]+");
      Matcher matcher = pattern.matcher(str.toString().trim());
      return matcher.find();
   }

   public static boolean isSpecialCharExistsInTextString (Object str, String specialChar) {
      Pattern pattern = Pattern.compile("([a-zA-Z]*[\\" + specialChar + "][a-zA-Z\\s]+)|([a-zA-Z]+[\\" + specialChar
               + "][a-zA-Z]*)");
      Matcher matcher = pattern.matcher(str.toString().trim());
      return matcher.find();
   }

   /**
    * This method replaces the valid special characters with spaces if they occurs in text string Example: 1)
    * "execue.com" -> "execue com" 2) "1.2" - > "1.2" 3) Spider 2.0 T.Spark.L -> Spider 2.0 T Spark L 4) Speed MX-5
    * Miata -> Speed MX 5 Miata
    */
   public static String replaceSpecialCharInTextString (String inputString, String specialChar, String charToReplaceWith) {
      Pattern pattern = Pattern.compile("([a-zA-Z]*[\\" + specialChar + "][a-zA-Z\\s]+)|([a-zA-Z]+[\\" + specialChar
               + "][a-zA-Z]*)");
      String str = inputString;
      Matcher matcher = pattern.matcher(str);
      while (matcher.find()) {
         String matchedString = matcher.group();
         String replacedString = matchedString.replace(specialChar, charToReplaceWith);
         str = str.replace(matchedString, replacedString);
      }
      return str;
   }

   public static String replaceMatchedRegexWithInputString (String text, String regex, String strToReplace) {
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(text);
      text = matcher.replaceAll(strToReplace);
      return text;
   }

   public static String removeExtraWhiteSpaces (String str) {
      StringTokenizer st = new StringTokenizer(str);
      StringBuffer sb = new StringBuffer();
      String prev = "";
      while (st.hasMoreTokens()) {
         String s = st.nextToken();
         if (isOperator(s) && isOperator(prev)) {
            sb.append(s);
         } else {
            sb.append(" ").append(s);
         }
         prev = s;
      }
      return sb.toString().trim();
   }

   public static String compactString (String str) {
      StringTokenizer st = new StringTokenizer(str);
      StringBuffer sb = new StringBuffer();
      while (st.hasMoreTokens()) {
         String s = st.nextToken();
         sb.append(s);
      }
      return sb.toString().trim();
   }

   public static String insertWhiteSpaces (String str) {
      StringBuffer sb = new StringBuffer();
      char[] charArray = str.toCharArray();

      sb.append(charArray[0]);

      for (int i = 1; i < charArray.length; i++) {
         char thisChar = charArray[i];
         char prevChar = charArray[i - 1];

         if (!ExecueCoreUtil.isOperator(prevChar) && !ExecueCoreUtil.isSpace(prevChar)
                  && ExecueCoreUtil.isOperator(thisChar)) {
            sb.append(' ');
         } else if (ExecueCoreUtil.isOperator(prevChar) && !ExecueCoreUtil.isSpace(thisChar)
                  && !ExecueCoreUtil.isOperator(thisChar)) {
            sb.append(' ');
         }
         sb.append(thisChar);
      }
      return sb.toString();
   }

   public static String[] getAsStringArray (String val) {
      if (val == null) {
         return null;
      }
      return val.split(",");
   }

   public static String[] splitOnUpperCase (String str) {
      StringBuffer sb = new StringBuffer();

      char parts[] = str.toCharArray();

      for (int i = 0; i < parts.length; i++) {
         if (Character.isUpperCase(parts[i])) {
            sb.append(' ');
         }
         sb.append(parts[i]);
      }
      return sb.toString().trim().split(" ");
   }

   public static String getUncompactedDisplayString (String str) {
      StringBuffer sb = new StringBuffer();
      String[] parts = splitOnUpperCase(str);
      for (int i = 0; i < parts.length; i++) {
         sb.append(parts[i]).append(' ');
      }
      return sb.toString().trim();
   }

   public static double getLevenshteinDistance (String str1, String str2) {
      return StringUtils.getLevenshteinDistance(str1, str2);
   }

   /**
    * Method to handle the special character in a string while preparing SFL term. there will be multiple combination
    * for String which contains Special Chars.
    * 
    * @param val
    * @return
    */
   public static Set<String> handleSpecialChars (String val) {
      Set<String> possibleTerms = new HashSet<String>();
      possibleTerms.add(val);
      boolean containsSpecialChar = false;
      for (String specialChar : specialChars) {
         if (val.contains(specialChar)) {
            containsSpecialChar = true;
            val = val.replaceAll("\\" + specialChar, " " + "\\" + specialChar + " ");
         }
      }

      val = removeExtraWhiteSpaces(val);
      if (containsSpecialChar) {
         possibleTerms = getPossibleTerms(val);
      }
      return possibleTerms;
   }

   /**
    * Method to get the Combination of tokens for the String. This is a recursive call with gets possible combination of
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

      if (StringUtils.isEmpty(remainingString.trim())) {
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

   /**
    * @param tokens
    * @param delim
    * @return
    */
   public static String compactWithDelim (String[] tokens, String delim) {
      StringBuilder sb = new StringBuilder(1);
      if (ArrayUtils.isEmpty(tokens)) {
         return sb.toString();
      }
      for (String token : tokens) {
         sb.append(token).append(delim);
      }
      sb.replace(sb.length() - delim.length(), sb.length(), ""); // chop the last delim
      return sb.toString();
   }

   /**
    * Method to return the new String wuth merge Single Character words if they Exists consecutivly in the passed
    * String. So for "I P O DAte" this return "IPO DATE".
    * 
    * @param word
    * @return
    */
   public static String mergeSingleCharsIfExists (String word) {
      List<String> newTokens = new ArrayList<String>(1);
      boolean lastTokenSingleChar = false;
      String[] tokens = word.split(" ");
      for (String token : tokens) {
         if (lastTokenSingleChar && token.length() == 1) {
            String prevToken = newTokens.remove(newTokens.size() - 1);
            prevToken = prevToken + token;
            newTokens.add(prevToken);
         } else {
            newTokens.add(token);
         }
         if (token.length() == 1 && !ExecueCoreUtil.isNumber(token)) {
            lastTokenSingleChar = true;
         } else {
            lastTokenSingleChar = false;
         }
      }
      String changedWord = "";
      for (String token : newTokens) {
         changedWord = changedWord + token + " ";
      }
      return changedWord.trim();
   }

   public static String processForTime (String userQuerySentence) {
      String regEx = "(?i)(0?[1-9]|1[012])(:[0-5]\\d){0,1}(:[0-5]\\d){0,1}(\\s)?([AP]M)($|\\s+)";
      Pattern pattern = Pattern.compile(regEx);
      Matcher matcher = null;
      matcher = pattern.matcher(userQuerySentence);

      while (matcher.find()) {
         Map<Integer, String> indexGroupMap = new HashMap<Integer, String>(1);
         // Get all groups for this match
         // start the loop from 1 as 0 will always denote the complete string
         for (int i = 1; i < matcher.groupCount() + 1; i++) {
            if (i == 1 && matcher.group(i) == null) {
               break;
            }
            indexGroupMap.put(i, matcher.group(i));
         }
         if (ExecueCoreUtil.isMapNotEmpty(indexGroupMap)) {
            if (indexGroupMap.get(1) != null && indexGroupMap.get(2) != null && indexGroupMap.get(4) == null) { // eg:
               // 9:10PM
               userQuerySentence = userQuerySentence.replaceAll(indexGroupMap.get(1) + indexGroupMap.get(2)
                        + indexGroupMap.get(5), indexGroupMap.get(1) + indexGroupMap.get(2) + " "
                        + indexGroupMap.get(5));
            } else if (indexGroupMap.get(2) == null && indexGroupMap.get(4) != null) { // eg: 9 PM
               userQuerySentence = userQuerySentence.replaceAll(indexGroupMap.get(1) + indexGroupMap.get(4)
                        + indexGroupMap.get(5), indexGroupMap.get(1) + ":00" + indexGroupMap.get(4)
                        + indexGroupMap.get(5));
            } else if (indexGroupMap.get(2) == null && indexGroupMap.get(4) == null) { // eg: 9PM
               userQuerySentence = userQuerySentence.replaceAll(indexGroupMap.get(1) + indexGroupMap.get(5),
                        indexGroupMap.get(1) + ":00 " + indexGroupMap.get(5));
            }
         }
      }
      return userQuerySentence;
   }

   public static String removeDuplicates (String str, boolean ignoreCase) {
      if (StringUtils.isEmpty(str)) {
         return str;
      }
      List<String> strTokens = getAsList(ignoreCase ? str.toLowerCase().trim() : str.trim());
      Set<String> uniqueTokens = new LinkedHashSet<String>(strTokens);
      String uniqueTokensString = StringUtils.join(uniqueTokens, " ").trim();
      return uniqueTokensString;
   }

   public static boolean matchEscaped (String first, String second) {
      first = StringEscapeUtils.escapeXml(first);
      second = StringEscapeUtils.escapeXml(second);
      return first.equalsIgnoreCase(second);
   }

   public static boolean isVerbForm (String tag) {
      return tag != null
               && (tag.equalsIgnoreCase(ExecueUtilConstant.VERB_BASE_FORM)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.VERB_PAST_TENSE)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.VERB_PRESENT_PARTICIPLE)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.VERB_PAST_PARTICIPLE)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.VERB_NON_3D_PERSON_PRESENT_SINGULAR) || tag
                        .equalsIgnoreCase(ExecueUtilConstant.VERB_3D_PERSON_PRESENT_SINGULAR));
   }

   public static boolean isOperator (String operString) {
      operString = operString.trim().toLowerCase();
      return operString.indexOf(ExecueUtilConstant.GREATER_THAN_EQUAL_TO_SYM) != -1
               || operString.indexOf(ExecueUtilConstant.LESS_THAN_EQUAL_TO_SYM) != -1
               || operString.equalsIgnoreCase(ExecueUtilConstant.GREATER_THAN_EQUAL_TO_SHORT)
               || operString.indexOf(ExecueUtilConstant.GREATER_THAN_EQUAL_TO) != -1
               || operString.indexOf(ExecueUtilConstant.LESS_THAN_EQUAL_TO) != -1
               || operString.equalsIgnoreCase(ExecueUtilConstant.LESS_THAN_EQUAL_TO_SHORT)
               || operString.indexOf(ExecueUtilConstant.GREATER_THAN) != -1
               || operString.indexOf(ExecueUtilConstant.GREATER_THAN_SYM) != -1
               || operString.equalsIgnoreCase(ExecueUtilConstant.GREATER_THAN_SHORT)
               || operString.indexOf(ExecueUtilConstant.MORE_THAN) != -1
               || operString.indexOf(ExecueUtilConstant.LESS_THAN) != -1
               || operString.indexOf(ExecueUtilConstant.LESS_THAN_SYM) != -1
               || operString.equalsIgnoreCase(ExecueUtilConstant.LESS_THAN_SHORT)
               || operString.indexOf(ExecueUtilConstant.EQUAL_TO_SYM) != -1
               || operString.indexOf(ExecueUtilConstant.EQUAL_TO) != -1
               || operString.equalsIgnoreCase(ExecueUtilConstant.EQUAL_TO_SHORT)
               || operString.toLowerCase().equalsIgnoreCase("morethan")
               || operString.toLowerCase().equalsIgnoreCase("lessthan")
               || operString.toLowerCase().equalsIgnoreCase("under")
               || operString.toLowerCase().equalsIgnoreCase("over")
               || operString.indexOf(ExecueUtilConstant.EQUIVALENCE_SYM) != -1;
   }

   public static boolean isPartOfOperator (String str) {
      str = str.trim().toLowerCase();
      return ExecueUtilConstant.GREATER_THAN_EQUAL_TO_SYM.indexOf(str) != -1
               || ExecueUtilConstant.LESS_THAN_EQUAL_TO_SYM.indexOf(str) != -1
               || ExecueUtilConstant.GREATER_THAN_EQUAL_TO_SHORT.indexOf(str) != -1
               || ExecueUtilConstant.GREATER_THAN_EQUAL_TO.indexOf(str) != -1
               || ExecueUtilConstant.LESS_THAN_EQUAL_TO.indexOf(str) != -1
               || ExecueUtilConstant.LESS_THAN_EQUAL_TO_SHORT.indexOf(str) != -1
               || ExecueUtilConstant.GREATER_THAN.indexOf(str) != -1
               || ExecueUtilConstant.GREATER_THAN_SYM.indexOf(str) != -1
               || ExecueUtilConstant.GREATER_THAN_SHORT.indexOf(str) != -1
               || ExecueUtilConstant.MORE_THAN.indexOf(str) != -1 || ExecueUtilConstant.LESS_THAN.indexOf(str) != -1
               || ExecueUtilConstant.LESS_THAN_SYM.indexOf(str) != -1
               || ExecueUtilConstant.LESS_THAN_SHORT.indexOf(str) != -1
               || ExecueUtilConstant.EQUAL_TO_SYM.indexOf(str) != -1 || ExecueUtilConstant.EQUAL_TO.indexOf(str) != -1
               || ExecueUtilConstant.EQUAL_TO_SHORT.indexOf(str) != -1 || "under".indexOf(str) != -1
               || "over".indexOf(str) != -1 || "morethan".indexOf(str) != -1 || "lessthan".indexOf(str) != -1
               || ExecueUtilConstant.EQUIVALENCE_SYM.indexOf(str) != -1;
   }

   public static boolean isOperatorSymbol (String operString) {
      operString = operString.trim().toLowerCase();
      return operString.equals(ExecueUtilConstant.GREATER_THAN_EQUAL_TO_SYM)
               || operString.equals(ExecueUtilConstant.LESS_THAN_EQUAL_TO_SYM)
               || operString.equals(ExecueUtilConstant.EQUIVALENCE_SYM)
               || operString.equals(ExecueUtilConstant.GREATER_THAN_SYM)
               || operString.equals(ExecueUtilConstant.LESS_THAN_SYM)
               || operString.equals(ExecueUtilConstant.EQUAL_TO_SYM);
   }

   public static boolean isConjunctionTag (String tag) {
      return tag != null
               && (tag.equalsIgnoreCase(ExecueUtilConstant.SUBORDINATING_CONJUNCTION)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.COORDINATING_CONJUNCTION)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.DETERMINER)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.MODAL_AUXILIARY) || tag.equalsIgnoreCase("PDT")
                        || tag.equalsIgnoreCase("WDT") || tag.equalsIgnoreCase(ExecueUtilConstant.INTERJECTION)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.TO)
                        || tag.equalsIgnoreCase(ExecueUtilConstant.BETWEEN) || tag
                        .equalsIgnoreCase(ExecueUtilConstant.COMMA));
   }

   public static boolean isConjunction (String str) {
      return isConjunctionTag(str);
   }

   /**
    * Removes the non-ASCII characters from the input string<BR>
    * For example, if the input string is "amazon BEST BUY <special chars which are not ascii> net sales net income"<BR>
    * the output string will be "amazon BEST BUY net sales net income"
    */
   public static String removeNonASCIICharacters (String inputWithAsciiCharacters) {
      return inputWithAsciiCharacters.replaceAll("[^\\x00-\\x7f]", "");
   }

   public static String trimSpaces (String inputWithSpaces) {
      if (inputWithSpaces == null) {
         return inputWithSpaces;
      }
      return inputWithSpaces.trim();
   }

   public static List<String> convertToLowerCase (List<String> words) {
      if (ExecueCoreUtil.isCollectionEmpty(words)) {
         return words;
      }
      List<String> wordsInLowerCase = new ArrayList<String>();
      
      for (String word : words) {
         if (!StringUtils.isBlank(word)) {
            wordsInLowerCase.add(StringUtils.lowerCase(word));
         } else {
            wordsInLowerCase.add(word);
         }
      }
      
      return wordsInLowerCase;
   }
}
