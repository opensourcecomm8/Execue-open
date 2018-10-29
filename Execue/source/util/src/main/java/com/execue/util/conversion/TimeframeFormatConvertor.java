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


package com.execue.util.conversion;

/**
 * @author Abhijit
 * @since Apr 8, 2009 : 12:46:31 PM
 */
public class TimeframeFormatConvertor {
  public static final String INTERNAL_FORMAT = "((Month|Quarter)(mm)-)?(Year(yyyy))";
  public static final String YEAR_MONTH = "YYYYMM";

  public static String getFormattedString(String sourceFormat, String sourceVal, String destFormat) {
    if (sourceFormat.equals(TimeframeFormatConvertor.INTERNAL_FORMAT)) {
      String[] parts = sourceVal.split("-");
      if (parts.length == 2) {
        int yearValue = Integer.parseInt(parts[1].substring("Year".length()));
        if(parts[0].startsWith("Month")) {
          int monthValue = Integer.parseInt(parts[0].substring("Month".length()));
          if (destFormat.equals(TimeframeFormatConvertor.YEAR_MONTH)) {
            sourceVal = String.valueOf(yearValue * 100 + monthValue);
          }
        } else if(parts[0].startsWith("Quarter")){
          int qtrValue = Integer.parseInt(parts[0].substring("Quarter".length()));
          if (destFormat.equals(TimeframeFormatConvertor.YEAR_MONTH)) {
            sourceVal = String.valueOf(yearValue * 10 + qtrValue);
          }
        }
      } else if(parts.length == 1 && sourceVal.startsWith("Year")) {
        sourceVal = parts[0].substring("Year".length()); 
      }
    }
    return sourceVal;
  }
}
