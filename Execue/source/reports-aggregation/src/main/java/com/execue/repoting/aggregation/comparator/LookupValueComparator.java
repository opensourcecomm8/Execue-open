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


package com.execue.repoting.aggregation.comparator;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Comparator;

import org.apache.log4j.Logger;

public class LookupValueComparator<T> implements Comparator<T> {

   public static Logger logger = Logger.getLogger(LookupValueComparator.class);

   public int compare (T firstObject, T secondObject) {
      if (firstObject != null && secondObject != null) {
         try {
            if (firstObject instanceof Integer) {
               return ((Integer) firstObject < (Integer) secondObject ? -1 : (firstObject == secondObject ? 0 : 1));
            } else if (firstObject instanceof Long) {
               return (((Long) firstObject).compareTo((Long) secondObject));
            } else if (firstObject instanceof Double) {
               return (((Double) firstObject).compareTo((Double) secondObject));
            } else if (firstObject instanceof BigDecimal) {
               return (((BigDecimal) firstObject).compareTo((BigDecimal) secondObject));
            } else if (firstObject instanceof String) {
               return ((String) firstObject).compareToIgnoreCase((String) secondObject);
            } else if (firstObject instanceof Date) {
               return (((Date) firstObject).compareTo((Date) secondObject));
            } else {
               return 0;
            }
         } catch (Exception e) {
            // hack to stop class ClassCast or any other exception.
            // need to revisit here later as the data type for each item has to be figured out even before coming here.
            logger.error("Exception occured " + e.getMessage() + " while comparing elements " + firstObject + " with "
                     + secondObject);
         }
      }
      return 0;
   }
}