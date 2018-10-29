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
import java.util.Map;

/**
 * @author Abhijit
 * @since Oct 8, 2008 : 3:20:51 PM
 */
public class ValueComparator implements Comparator {
  public int compare(Object o1, Object o2) {
    if(o1 instanceof Map.Entry && o2 instanceof Map.Entry) {
      Map.Entry e1 = (Map.Entry) o1;
      Map.Entry e2 = (Map.Entry) o2;
      Comparable c1 = (Comparable) e1.getValue();
      Comparable c2 = (Comparable) e2.getValue();
      return c2.compareTo(c1);
    } else {
      Comparable c1 = (Comparable) o1;
      Comparable c2 = (Comparable) o2;
      return c2.compareTo(c1);
    }
  }
}

