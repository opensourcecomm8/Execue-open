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


/**
 * 
 */
package com.execue.sus.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.execue.core.common.bean.qdata.NewsItem;
import com.execue.core.util.ExecueStringUtil;

/**
 * @author Nihar
 */
public class NewsUtil {

   private static final Logger log = Logger.getLogger(NewsUtil.class);

   public static List<String> getLinesFromSummary (String summary, int tokenLimit) {
      Pattern p = Pattern.compile("[\\.\\!\\?]\\s+", Pattern.MULTILINE);
      String[] split = p.split(summary);
      List<String> afterLimitCheck = new ArrayList<String>(1);
      for (String line : split) {
         // Note: Maxtent Tagger can tag 300 tokens at max, after that it was giving ArrayIndexOutOfBoundException,
         // So we again split the news article based on the configurable token limit sent to this method
         splitBasedOnLength(line, afterLimitCheck, tokenLimit);
      }
      return afterLimitCheck;
   }

   private static void splitBasedOnLength (String line, List<String> afterLimitCheck, int tokenLimit) {
      String[] split2 = line.split(" ");
      if (split2.length > tokenLimit) {
         String[] subArray = (String[]) ArrayUtils.subarray(split2, 0, tokenLimit);
         String[] remainingArray = (String[]) ArrayUtils.subarray(split2, tokenLimit, split2.length);
         afterLimitCheck.add(ExecueStringUtil.compactWithDelim(subArray, " "));
         splitBasedOnLength(ExecueStringUtil.compactWithDelim(remainingArray, " "), afterLimitCheck, tokenLimit);
      } else {
         afterLimitCheck.add(line);
      }
   }

   public static String cleanupNewsData (String newsData) {
      if (StringUtils.isEmpty(newsData)) {
         return newsData;
      }
      newsData = getTrimmedForHTMLTags(newsData);
      newsData = getTrimmedForSpecialChars(newsData);
      newsData = ExecueStringUtil.removeExtraWhiteSpaces(newsData);
      return newsData;
   }

   public static void cleanupNewsItem (NewsItem newsItem) {
      newsItem.setSummary(getTrimmedForHTMLTags(newsItem.getSummary()));
      newsItem.setSummary(getTrimmedForSpecialChars(newsItem.getSummary()));
      newsItem.setSummary(ExecueStringUtil.removeExtraWhiteSpaces(newsItem.getSummary()));

      newsItem.setTitle(getTrimmedForHTMLTags(newsItem.getTitle()));
      newsItem.setTitle(getTrimmedForSpecialChars(newsItem.getTitle()));
      newsItem.setTitle(ExecueStringUtil.removeExtraWhiteSpaces(newsItem.getTitle()));
   }

   public static String getTrimmedForHTMLTags (String input) {
      // Remove all the html tags and the data which comes in between < and > character
      String outPut = input.replaceAll("<[^\\s][^>]*>", "");
      // Replace &nbsp; with space
      outPut = outPut.replaceAll("&nbsp;", " ");
      // Trim Any other html enity like &xyz;
      outPut = outPut.replaceAll("&[^\\s]*;", "");
      if (outPut.toUpperCase().indexOf("CDATA") >= 0) {
         outPut = outPut.substring(outPut.indexOf("CDATA") + 6);
         if (outPut.indexOf("]") > 0) {
            outPut = outPut.substring(0, outPut.indexOf("]"));
         }
      }
      return outPut;
   }

   public static String getTrimmedForSpecialChars (String inPut) {
      String outPut = inPut;
      outPut = outPut.replaceAll("\\%", " Percentage ");
      outPut = outPut.replaceAll(",", "");
      outPut = outPut.replaceAll("'", "");
      outPut = outPut.replaceAll("\\?", "");
      outPut = outPut.replaceAll("!", "");
      outPut = outPut.replaceAll("\"", "");
      outPut = outPut.replaceAll("Inc\\.", "Inc");
      outPut = outPut.replaceAll("Co\\.", "Co");
      outPut = outPut.replaceAll("Ltd\\.", "Ltd");
      outPut = outPut.replaceAll("\\.com", "com");
      outPut = outPut.replaceAll("&", "and");
      return outPut;
   }

   public static void main (String[] args) {
      String article = "A bill that would give $26 billion in aid to cash-strapped state and local governments cleared a Senate hurdle on Wednesday, as a vote on final passage was expected later this week and a report said House lawmakers may cut their vacation short to vote on the aid.";

      List<String> linesFromSummary = getLinesFromSummary(article, 20);
      System.out.println(linesFromSummary);
   }
}
