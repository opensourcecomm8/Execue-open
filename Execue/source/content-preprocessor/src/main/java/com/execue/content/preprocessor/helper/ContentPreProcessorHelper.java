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


package com.execue.content.preprocessor.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

import com.execue.core.constants.ExecueConstants;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;

/**
 * @author John Mallavalli
 */
public class ContentPreProcessorHelper {

   public static String getTrimmedForHTMLTags (String input) {
      String output = input;
      if (ExecueCoreUtil.isNotEmpty(input)) {
         // Remove all the HTML tags and the data which comes in between < and > character
         output = input.replaceAll("<[^\\s][^>]*>", ExecueConstants.SPACE);
         // Trim any other HTML entity like &xyz;
         output = output.replaceAll("&[^\\s]*;", ExecueConstants.EMPTY_STRING);
         if (output.toUpperCase().indexOf("CDATA") >= 0) {
            output = output.substring(output.indexOf("CDATA") + 6);
            if (output.indexOf("]") > 0) {
               output = output.substring(0, output.indexOf("]"));
            }
         }
      }
      return output;
   }

   public static String removeContinuousChars (String input) {
      if (ExecueCoreUtil.isNotEmpty(input)) {
         // if any character occurs more than 3 times then replace the entire character sequence with empty string
         // currently replacing if we find the "-+$%#*"
         String pattern = "[-+$%#*]{4,}";
         String replaceString = ExecueConstants.EMPTY_STRING;
         input = input.replaceAll(pattern, replaceString);
      }
      return input;
   }

   public static List<String> getLinesFromSummary (String summary, int tokenLimit) {
      List<String> afterLimitCheck = new ArrayList<String>();
      if (ExecueCoreUtil.isNotEmpty(summary)) {
         Pattern p = Pattern.compile("[\\.\\!\\?]\\s+", Pattern.MULTILINE);
         String[] split = p.split(summary);
         afterLimitCheck = new ArrayList<String>(1);
         for (String line : split) {
            // Note: Maxent Tagger can tag 300 tokens at max, after that it was giving ArrayIndexOutOfBoundException,
            // So we again split the news article based on the configurable token limit sent to this method
            splitBasedOnLength(line, afterLimitCheck, tokenLimit);
         }
      }
      return afterLimitCheck;
   }

   private static void splitBasedOnLength (String line, List<String> afterLimitCheck, int tokenLimit) {
      if (ExecueCoreUtil.isNotEmpty(line) && ExecueCoreUtil.isCollectionNotEmpty(afterLimitCheck)) {
         String[] split2 = line.split(ExecueConstants.SPACE);
         if (split2.length > tokenLimit) {
            String[] subArray = (String[]) ArrayUtils.subarray(split2, 0, tokenLimit);
            String[] remainingArray = (String[]) ArrayUtils.subarray(split2, tokenLimit, split2.length);
            afterLimitCheck.add(ExecueStringUtil.compactWithDelim(subArray, ExecueConstants.SPACE));
            splitBasedOnLength(ExecueStringUtil.compactWithDelim(remainingArray, ExecueConstants.SPACE),
                     afterLimitCheck, tokenLimit);
         } else {
            afterLimitCheck.add(line);
         }
      }
   }

   // TODO: -JM- move to config or database
   public static String getActualCharsFromEscapeChars (String input) {
      String output = input;
      if (ExecueCoreUtil.isNotEmpty(input)) {
         output = output.replaceAll("&amp;", "&");
         output = output.replaceAll("&quot;", "\"");
         output = output.replaceAll("&gt;", ">");
         output = output.replaceAll("&lt;", "<");
         output = output.replaceAll("&nbsp;", ExecueConstants.SPACE);
      }
      return output;
   }
}
