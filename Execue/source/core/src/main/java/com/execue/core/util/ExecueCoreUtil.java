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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

public class ExecueCoreUtil implements ExecueUtilConstant {

   /**
    * A utility class for utility methods. Adding method should be considered only after verifying with commons lang and
    * collection or any other utility packages
    * 
    * @author Raju Gottumukkala
    * @version 4.0
    * @since 4.0
    */

   private static final String DEFAULT_COMMA_SEPERATOR = ",";

   /**
    * Converts a Collection of String into one string using "," (COMMA) as separator
    * 
    * @param inputStrings
    * @return
    */
   public static String joinCollection (Collection<String> inputStrings) {
      return joinCollection(inputStrings, DEFAULT_COMMA_SEPERATOR);
   }

   public static String getDoubleQuoteBasedString (String inputString) {
      return "\"" + inputString + "\"";
   }

   public static List<String> getDoubleQuoteBasedStrings (List<String> inputStrings) {
      List<String> doubleQuoteBasedStrings = new ArrayList<String>();
      for (String inputString : inputStrings) {
         doubleQuoteBasedStrings.add(getDoubleQuoteBasedString(inputString));
      }
      return doubleQuoteBasedStrings;
   }

   public static String joinCollectionWithDoubleQuotesOnValues (List<String> inputStrings) {
      List<String> doubleQuoteBasedStrings = getDoubleQuoteBasedStrings(inputStrings);
      return joinCollection(doubleQuoteBasedStrings, DEFAULT_COMMA_SEPERATOR);
   }

   /**
    * Converts a Collection of String into one string using the separator
    * 
    * @param inputStrings
    * @param separator
    * @return
    */
   public static String joinCollection (Collection<String> inputStrings, String separator) {
      return StringUtils.join(inputStrings.toArray(), separator);
   }

   /**
    * Converts a Collection of Long into one string using the separator
    * 
    * @param inputStrings
    * @param separator
    * @return
    */
   public static String joinLongCollection (Collection<Long> inputStrings, String separator) {
      return StringUtils.join(inputStrings.toArray(), separator);
   }

   public static List<Double> parseInputStringAsDouble (List<String> input, String delimeter) {
      List<Double> doubleList = new ArrayList<Double>();
      if (ExecueCoreUtil.isCollectionNotEmpty(input)) {
         for (String token : input) {
            doubleList.add(new Double(token));
         }
      }
      return doubleList;
   }
   
   public static List<Integer> parseInputStringAsInteger (List<String> input, String delimeter) {
      List<Integer> integerList = new ArrayList<Integer>();
      if (ExecueCoreUtil.isCollectionNotEmpty(input)) {
         for (String token : input) {
            integerList.add(new Integer(token.trim()));
         }
      }
      return integerList;
   }
   
   public static List<Long> parseInputStringAsLong (List<String> input, String delimeter) {
      List<Long> longList = new ArrayList<Long>();
      if (ExecueCoreUtil.isCollectionNotEmpty(input)) {
         for (String token : input) {
            longList.add(new Long(token.trim()));
         }
      }
      return longList;
   }

   public static String joinDoubleCollection (Collection<Double> inputStrings, String separator) {
      return StringUtils.join(inputStrings.toArray(), separator);
   }

   public static String joinIntegerCollection (Collection<Integer> inputStrings, String separator) {
      return StringUtils.join(inputStrings.toArray(), separator);
   }

   public static String joinLongCollection (Collection<Long> inputStrings) {
      return StringUtils.join(inputStrings.toArray(), DEFAULT_COMMA_SEPERATOR);
   }

   /**
    * This method checks whether list is null or empty. This method will be used to check whether there are any elements
    * in the list. If there are elements, this will check whether they any element is null(in this case it will consider
    * the list as empty).
    * 
    * @param list
    * @return boolean value
    */
   @SuppressWarnings ("unchecked")
   // TODO: -VG- optimize this method.
   public static boolean isListElementsEmpty (List list) {
      boolean isEmpty = false;
      if (list == null || list.size() == 0) {
         isEmpty = true;
      } else {
         for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
               isEmpty = true;
               break;
            }
         }
      }
      return isEmpty;
   }

   public static boolean isListElementsNotEmpty (List list) {
      return !isListElementsEmpty(list);
   }

   @SuppressWarnings ("unchecked")
   /**
    * This method checks whether collection is empty or not.
    * 
    * @param collection
    * @return boolean value indicating whether true or false.
    */
   public static boolean isCollectionEmpty (Collection collection) {
      return collection == null || collection.size() == 0;
   }

   public static boolean isCollectionNotEmpty (Collection collection) {
      return !isCollectionEmpty(collection);
   }

   /**
    * This method checks whether a map is empty or not.
    * 
    * @param map
    * @return boolean value indicating whether true or false.
    */
   @SuppressWarnings ("unchecked")
   public static boolean isMapEmpty (Map map) {
      return map == null || map.size() == 0;
   }

   public static boolean isMapNotEmpty (Map map) {
      return !isMapEmpty(map);
   }

   /**
    * Provides a 3 character String to be used as Alias<br/> aliasList should be the existing aliases against which the
    * generated alias needs to be checked for uniqueness
    * 
    * @param aliasList
    * @return
    */
   public static String getAlias (List<String> aliasList) {
      String alias = null;
      while (true) {
         alias = RandomStringUtils.randomAlphabetic(3).toUpperCase();
         alias += RandomStringUtils.randomNumeric(2);
         if (ExecueCoreUtil.isCollectionEmpty(aliasList)) {
            aliasList.add(alias);
            break;
         }
         if (!aliasList.contains(alias)) {
            aliasList.add(alias);
            break;
         }
      }
      return alias;
   }

   public static Integer getRandomNumberLessThanHundred () {
      return new Random().nextInt(100);
   }

   public static boolean isNameExists (List<String> existingNames, String name) {
      boolean isExists = false;
      for (String string : existingNames) {
         if (string.equalsIgnoreCase(name)) {
            isExists = true;
            break;
         }
      }
      return isExists;
   }

   public static Double getSmoothenedNumber (Double inNumber) {
      Double outNumber = null;
      DecimalFormat smoothenedNumberFormat = new DecimalFormat("#####################.##");
      DecimalFormat smoothenedNumberFormatWithoutScale = new DecimalFormat("#####################");
      if (inNumber > 99 || inNumber < -99) {
         outNumber = new Double(smoothenedNumberFormatWithoutScale.format(inNumber));
      } else {
         outNumber = new Double(smoothenedNumberFormat.format(inNumber));
      }
      return outNumber;
   }

   public static String getSmoothenedNumberDescription (Double inNumber) {
      String smoothenedNumber = inNumber.toString();
      BigDecimal bigDecNumber = new BigDecimal(inNumber);
      if (bigDecNumber.scale() == 0) {
         DecimalFormat smoothNumberFormat = new DecimalFormat("###############################");
         if (inNumber > 99 || inNumber < -99) {
            smoothenedNumber = smoothNumberFormat.format(inNumber);
         }
      }
      return smoothenedNumber;
   }

   public static String removeSpecialCharacters (String inputWithSpecialCharacters) {
      Pattern pattern = Pattern.compile("([^a-zA-z0-9])");
      return pattern.matcher(inputWithSpecialCharacters).replaceAll("");
   }

   public static String readFileAsString (InputStream is) throws java.io.IOException {
      InputStreamReader inputStreamReader = new InputStreamReader(is);
      StringBuffer fileData = new StringBuffer(1000);
      BufferedReader reader = new BufferedReader(inputStreamReader);
      char[] buf = new char[1024];
      int numRead = 0;
      while ((numRead = reader.read(buf)) != -1) {
         String readData = String.valueOf(buf, 0, numRead);
         fileData.append(readData);
         buf = new char[1024];
      }
      reader.close();
      return fileData.toString();
   }

   public static void copyFile (File sourceFile, File destFile) throws IOException {
      if (!destFile.exists()) {
         destFile.createNewFile();
      }

      FileChannel source = null;
      FileChannel destination = null;
      try {
         source = new FileInputStream(sourceFile).getChannel();
         destination = new FileOutputStream(destFile).getChannel();
         destination.transferFrom(source, 0, source.size());
      } finally {
         if (source != null) {
            source.close();
         }
         if (destination != null) {
            destination.close();
         }
      }
   }

   public static int getCorrespondingBooleanValueAsInteger (String stringBooleanValue) {
      int intBooleanValue = 0;
      if ("true".equalsIgnoreCase(stringBooleanValue)) {
         intBooleanValue = 1;
      }
      return intBooleanValue;
   }

   public static boolean getCorrespondingBooleanValueAsBoolean (String stringBooleanValue) {
      boolean booleanValue = false;
      if ("true".equalsIgnoreCase(stringBooleanValue)) {
         booleanValue = true;
      }
      return booleanValue;
   }

   public static <K, V> Map<K, Double> getRelativePercentageMap (Map<K, Double> percentageMap) {
      Map<K, Double> percentileMap = new HashMap<K, Double>(1);
      if (percentageMap.isEmpty()) {
         return percentileMap;
      }
      Object[] vals = percentageMap.values().toArray();
      Map<Double, Double> percentiles = getRelativePercentageMap(vals);
      for (K key : percentageMap.keySet()) {
         percentileMap.put(key, percentiles.get(percentageMap.get(key)));
      }
      return percentileMap;
   }

   public static Map<Double, Double> getRelativePercentageMap (Object[] values) {
      double[] vals = new double[values.length];
      for (int i = 0; i < values.length; i++) {
         if (values[i] instanceof Double) {
            vals[i] = (Double) values[i];
         } else {
            // TODO throw ExecueNLPException
         }
      }
      return getRelativePercentageMap(vals);
   }

   public static Map<Double, Double> getRelativePercentageMap (double[] values) {
      double[] unsorted = new double[values.length];
      Map<Double, Double> percentiles = new HashMap<Double, Double>(1);

      System.arraycopy(values, 0, unsorted, 0, values.length);
      Arrays.sort(values);

      double max = values[values.length - 1];

      // Calculating the Percentiles

      for (double anUnsorted : unsorted) {
         String value_key = Double.toString(anUnsorted);
         double relativePercentage = 0;
         if (max > 0) {
            relativePercentage = anUnsorted / max * 100;
         }
         percentiles.put(new Double(value_key), relativePercentage);
      }
      return percentiles;
   }

   public static String getFormattedSystemCurrentMillis () {
      SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHMMssSSS");
      Date currentTime = new Date();
      currentTime.setTime(System.currentTimeMillis());
      String dateTime = sf.format(currentTime);
      return dateTime;
   }

   /**
    * This method takes in the string to be converted using MD5 algorithm and spits out a MD5-hashcode of the string.
    * 
    * @param inputString
    * @return hashcode of the input string
    * @throws NoSuchAlgorithmException
    * @throws UnsupportedEncodingException
    */
   public static String generateMd5 (String inputString) throws NoSuchAlgorithmException, UnsupportedEncodingException {

      MessageDigest messageDigest = MessageDigest.getInstance("md5");

      StringBuffer sb = new StringBuffer();

      byte[] buf = inputString.getBytes("iso-8859-1");
      byte[] md5 = messageDigest.digest(buf);

      for (int i = 0; i < md5.length; i++) {
         String hexString = "0" + Integer.toHexString((0xff & md5[i]));
         sb.append(hexString.substring(hexString.length() - 2));
      }

      return sb.toString();
   }

   /**
    * This method take the twoDigitYear as string and returns the 4 digit year based on the SimpleDateFormat 'yyyy'
    * 
    * @param twoDigitYear
    * @return the 4 digit year based on the simple date format
    */
   public static String get4DigitYearFrom2DigitYear (String twoDigitYear) {
      String fourDigitYear = twoDigitYear;
      if (StringUtils.isBlank(twoDigitYear) || twoDigitYear.length() != 2) {
         return fourDigitYear;
      }
      try {
         fourDigitYear = fourDigitYearSimpleDateFormat.format(twoDigitYearSimpleDateFormat.parse(twoDigitYear));
      } catch (ParseException e) {
      }// eaten;
      return fourDigitYear;
   }

   /**
    * This method applies and returns the default number format value on the given input value
    * 
    * @param value
    * @return the number formatted String value
    */
   public static String getNumberFormattedValue (Object value) {
      NumberFormat myFormatter = NumberFormat.getInstance();
      return myFormatter.format(value);
   }

   /**
    * This method checks whether string is empty or not.
    * 
    * @param str
    * @return boolean value indicating whether true or false.
    */
   public static boolean isEmpty (String str) {
      return str == null || str.length() == 0;
   }

   public static boolean isNotEmpty (String str) {
      return !isEmpty(str);
   }

   public static boolean isNumber (Object str) {
      return Pattern.matches("[\\d]*", str.toString().trim());
   }

   public static boolean isDouble (Object str) {
      return Pattern.matches("([\\d]*)(\\.)?([\\d]+)", str.toString().trim());
   }

   public static boolean isCardinalNumber (Object str) {
      return Pattern.matches("^([\\d]*)(\\.)?([\\d]+)(st|nd|rd|th)?", str.toString().trim());
   }

   public static boolean isAlbhabetExists (Object str) {
      Pattern pattern = Pattern.compile("[a-zA-Z]+");
      Matcher matcher = pattern.matcher(str.toString().trim());
      return matcher.find();
   }

   public static boolean isOperator (char operChar) {
      return ExecueStringUtil.isOperator(String.valueOf(operChar));
   }

   public static boolean isOperator (Object operChar) {
      return ExecueStringUtil.isOperator(String.valueOf(operChar));
   }

   public static boolean isSpace (char operChar) {
      return operChar == ' ';
   }

   public static boolean isDataType (String str) {
      return str.equals("string") || str.equals("decimal") || str.equals("int") || str.equals("float")
               || str.equals("double") || str.equals("long") || str.equals("short") || str.equals("date")
               || str.equals("positiveinteger") || str.equals("negativeinteger");
   }

   @SuppressWarnings ("unchecked")
   public static String convertAsString (Collection<String> inputStringList) {
      String outPutStr;
      StringBuffer sb = new StringBuffer();
      Iterator iter = inputStringList.iterator();
      while (iter.hasNext()) {
         sb.append(iter.next().toString());
         sb.append(" ");
      }
      outPutStr = sb.toString().trim();
      return outPutStr;
   }

   public static String getRandomString (int n) {
      char[] pw = new char[n];
      int c = 'A';
      int r1 = 0;
      for (int i = 0; i < n; i++) {
         r1 = (int) (Math.random() * 12);
         switch (r1) {
            case 0:
            case 5:
            case 7:
            case 1:
            case 4:
            case 9:
            case 11:
               c = 'a' + (int) (Math.random() * 26);
               break;
            case 2:
            case 3:
            case 6:
            case 8:
               c = 'A' + (int) (Math.random() * 26);
               break;
            case 10:
               c = '_';
               break;
         }
         pw[i] = (char) c;
      }
      return new String(pw);
   }

   public static String splitOnUpperCaseByREGEX (String txt) {
      String result = "";
      java.util.regex.Pattern p = java.util.regex.Pattern.compile("[A-Z]{1}[^A-Z]*");
      java.util.regex.Matcher m = p.matcher(txt);
      while (m.find()) {
         result += m.group() + " ";
      }
      return result.trim();
   }

   public static String getCorrectSQLValue (String str) {
      if (!str.startsWith("'") && !isNumber(str)) {
         str = "'" + str + "'";
      }
      return str;
   }

   public static String getDateAsString (Date date) {
      String dateStr = "";
      SimpleDateFormat dbInsertFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      dateStr = dbInsertFormat.format(date);
      return dateStr;
   }

   public static String processForPhoneNumber (String userQuerySentence) {
      String regEx = "(\\(\\s?)?(\\d{3})\\s?(\\)\\s?)?\\s?(-)?\\s?(\\d{3})\\s?(-)\\s?(\\d{4})";
      Pattern pattern = Pattern.compile(regEx);
      Matcher matcher = null;
      matcher = pattern.matcher(userQuerySentence);
      while (matcher.find()) {
         String phoneNumber = matcher.group();
         phoneNumber = StringEscapeUtils.escapeHtml(phoneNumber);
         String validPhone = matcher.group(2) + matcher.group(4) + matcher.group(5) + matcher.group(6)
                  + matcher.group(7);
         userQuerySentence = userQuerySentence.replace(phoneNumber, validPhone);
      }
      return userQuerySentence;
   }

   /**
    * Adds spaces before capital letters, if any, in the input string. However this method is different from the normal
    * split where in groups of capital letters do no get split <BR>
    * Eg: "StateProject" will get split as "State Project", whereas<BR>
    * "StateUUIDProjectForBetterGDP" will get split as "State UUID Project For Better GDP"
    * 
    * @param stringWithCaps
    * @return
    */
   public static String splitOnUpperCase (String stringWithCaps) {
      StringBuilder sb = new StringBuilder();
      List<Integer> upperCaseIndexList = new ArrayList<Integer>();
      for (int i = 0; i < stringWithCaps.length(); i++) {
         if (Character.isUpperCase(stringWithCaps.charAt(i))) {
            upperCaseIndexList.add(i);
         }
      }
      correctUpperCaseIndices(upperCaseIndexList);
      String lastTokenAdded = null;
      boolean appendWithSpace = false;
      if (ExecueCoreUtil.isCollectionNotEmpty(upperCaseIndexList)) {
         for (int i = 0; i < upperCaseIndexList.size(); i++) {
            String token = null;
            int prevIndex = -1;
            if (i != 0) {
               prevIndex = upperCaseIndexList.get(i - 1);
            }
            int currentIndex = upperCaseIndexList.get(i);
            if (currentIndex > 0) {
               if (prevIndex < 0) {
                  // get substring from beginning till current index
                  token = stringWithCaps.substring(0, currentIndex);
               } else {
                  // get substring from previous index till current index
                  token = stringWithCaps.substring(prevIndex, currentIndex);
               }
            } else {
               // first letter is in upper case
               continue;
            }
            appendWithSpace = stringWithCaps.charAt(currentIndex - 1) != ' ';
            if (lastTokenAdded == null || lastTokenAdded.endsWith(" ")) {
               appendWithSpace = false;
            }
            addToken(sb, token, appendWithSpace);
            lastTokenAdded = token;
         }
         if (upperCaseIndexList.get(upperCaseIndexList.size() - 1) <= stringWithCaps.length() - 1) {
            int startIndex = upperCaseIndexList.get(upperCaseIndexList.size() - 1);
            // check if the last split index and the last char index are same and if the last char of the previous token
            // is a capital letter then append the token without a space
            appendWithSpace = true;
            if (startIndex > 0) {
               appendWithSpace = stringWithCaps.charAt(startIndex - 1) != ' ';
            }
            if (lastTokenAdded != null && startIndex == stringWithCaps.length() - 1
                     && Character.isUpperCase(lastTokenAdded.charAt(lastTokenAdded.length() - 1)) || startIndex == 0
                     && upperCaseIndexList.size() == 1) {
               appendWithSpace = false;
            }
            addToken(sb, stringWithCaps.substring(startIndex), appendWithSpace);
         }
      } else {
         sb.append(stringWithCaps);
      }
      return sb.toString();
   }

   /**
    * This method take a string with separator and return a list of Long
    * 
    * @param stringWithSeprator
    * @param separator
    * @return
    */
   public static List<Long> getLongListFromString (String stringWithSeprator, String separator) {
      List<String> stringlist = Arrays.asList(stringWithSeprator.split(separator));
      List<Long> longList = new ArrayList<Long>(stringlist.size());
      for (String string : stringlist) {
         longList.add(Long.valueOf(string));
      }
      return longList;
   }

   private static void correctUpperCaseIndices (List<Integer> upperCaseIndexList) {
      List<Integer> group = new ArrayList<Integer>();
      // remove the indices which are consecutive
      for (int i = 0; i < upperCaseIndexList.size(); i++) {
         int prevIndexValue = -1;
         int nextIndexValue = -1;
         if (i > 0) {
            prevIndexValue = upperCaseIndexList.get(i - 1);
         }
         if (i < upperCaseIndexList.size() - 1) {
            nextIndexValue = upperCaseIndexList.get(i + 1);
         }
         int currentIndexValue = upperCaseIndexList.get(i);
         if (prevIndexValue > -1 && nextIndexValue > -1) {
            if (currentIndexValue - prevIndexValue == 1 && nextIndexValue - currentIndexValue == 1) {
               // remove the current index
               group.add(currentIndexValue);
            }
         }
      }
      if (ExecueCoreUtil.isCollectionNotEmpty(group)) {
         upperCaseIndexList.removeAll(group);
      }
   }

   private static void addToken (StringBuilder sb, String token, boolean appendWithSpace) {
      if (appendWithSpace) {
         sb.append(" ");
      }
      sb.append(token);
   }

   /**
    * @param sqlType
    * @return the boolean true/false based on the input type is matched to one of the below sql type
    */
   public static boolean isSQLTypeNumeric (Integer sqlType) {
      return sqlType == java.sql.Types.TINYINT || sqlType == java.sql.Types.SMALLINT
               || sqlType == java.sql.Types.INTEGER || sqlType == java.sql.Types.BIGINT
               || sqlType == java.sql.Types.NUMERIC;
   }

   /**
    * @param sqlType
    * @return the boolean true/false based on the input type is matched to one of the below sql type
    */
   public static boolean isSQLTypeDecimal (Integer sqlType) {
      return sqlType == java.sql.Types.DOUBLE || sqlType == java.sql.Types.DECIMAL || sqlType == java.sql.Types.FLOAT;
   }

   public static <T> List<T> mergeCollectionAsList (Collection<List<T>> values) {
      List<T> mergedList = new ArrayList<T>();
      if (isCollectionEmpty(values)) {
         return mergedList;
      }
      for (Collection<T> list : values) {
         mergedList.addAll(list);
      }
      return mergedList;
   }

   public static <T> Set<T> mergeCollectionAsSet (Collection<List<T>> values) {
      Set<T> mergedList = new HashSet<T>();
      if (isCollectionEmpty(values)) {
         return mergedList;
      }
      for (Collection<T> list : values) {
         mergedList.addAll(list);
      }
      return mergedList;
   }

   public static String generateAlphanumericString (String input) {
      char[] charArray = input.toString().toCharArray();
      StringBuilder output = new StringBuilder();
      for (char c : charArray) {
         if (Character.isLetterOrDigit(c)) {
            output.append(c);
         }
      }
      int length = output.length();
      if (length < 6) {
         output.append(RandomStringUtils.randomAlphanumeric(6 - length));
      }
      return output.toString();
   }

   public static Long getNextPageNumber (Long currentPageNumber, Long pageSize, Long totalLineCount) {
      Long nextPageNumber = currentPageNumber + 1;
      Long remainingLineCount = totalLineCount - currentPageNumber * pageSize;
      if (remainingLineCount <= 0) {
         nextPageNumber = -99L;
      }
      return nextPageNumber;
   }

   public static String getSingleQuoteBasedString (String inputString) {
      return "'" + inputString + "'";
   }

   public static List<String> getSingleQuoteBasedStrings (List<String> inputStrings) {
      List<String> singleQuoteBasedStrings = new ArrayList<String>();
      for (String inputString : inputStrings) {
         singleQuoteBasedStrings.add(getSingleQuoteBasedString(inputString));
      }
      return singleQuoteBasedStrings;
   }

   public static String joinCollectionWithSingleQuotesOnValues (List<String> inputStrings) {
      List<String> doubleQuoteBasedStrings = getSingleQuoteBasedStrings(inputStrings);
      return joinCollection(doubleQuoteBasedStrings, DEFAULT_COMMA_SEPERATOR);
   }

   public static String getFormattedNumberString (double value, int maxFractionDigits) {
      NumberFormat instance = NumberFormat.getInstance();
      instance.setMaximumFractionDigits(maxFractionDigits);
      String formattedValue = instance.format(value);
      return formattedValue;
   }

}
