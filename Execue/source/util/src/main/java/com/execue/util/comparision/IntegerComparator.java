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


package com.execue.util.comparision;

import java.util.Comparator;

/**
 * @author Abhijit
 * @since May 3, 2007 - 3:13:40 PM
 */
public class IntegerComparator implements Comparator {
  public int compare(Object firstObj, Object secondObj) {

    String firstStr = null;
    if (firstObj instanceof String)
      firstStr = (String) firstObj;

    String secondStr = null;
    if (secondObj instanceof String)
      secondStr = (String) secondObj;

    if ("".equals(firstStr) || firstStr == null)
      firstStr = "-99";
    if ("".equals(secondStr) || secondStr == null)
      secondStr = "-99";

    Integer firstInt = new Integer(Integer.parseInt(firstStr));
    Integer secondInt = new Integer(Integer.parseInt(secondStr));

    return firstInt.compareTo(secondInt);
  }
}