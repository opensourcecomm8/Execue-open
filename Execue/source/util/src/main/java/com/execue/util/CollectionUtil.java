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


package com.execue.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.execue.util.comparision.KeyComparator;
import com.execue.util.comparision.ValueComparator;


public class CollectionUtil {

  private CollectionUtil() {
    
  }
  
  public static Collection getObjectAsCollection(Object object) {
    Collection objectCollection = null;
    if (object instanceof Collection) {
      objectCollection = (Collection)object;
    } else {
      objectCollection = new ArrayList();
      objectCollection.add(object);
    }
    return objectCollection;
  }

  public static <K, V> Map<K, Double> sortMapOnValue (Map<K, Double> map) {
     return getTopNByValue(map, map.size());
  }  
   
  public static <K, V> Map<K, Double> getTopNByValue (Map<K, Double> hashmap, int n) {
     Map<K, Double> map = new LinkedHashMap<K, Double>();
     List<Map.Entry<K, Double>> entrylist = new ArrayList<Map.Entry<K, Double>>(hashmap.entrySet());
     Collections.sort(entrylist, new ValueComparator());
  
     for (Map.Entry<K, Double> entry : entrylist) {
        map.put(entry.getKey(), entry.getValue());
     }
     return map;
  }
  
  public static <K, V> Map<Long, V> sortMapOnKey (Map<Long, V> map) {
     return getTopNByKey(map, map.size());
  }

  public static <K, V> Map<Double, V> sortMapOnKeyAsDouble (Map<Double, V> map) {
     return getTopNByKeyAsDouble(map, map.size());
  }

  public static <K, V> Map<Double, V> getTopNByKeyAsDouble (Map<Double, V> hashmap, int n) {
     Map<Double, V> map = new LinkedHashMap<Double, V>();
     List<Map.Entry<Double, V>> entrylist = new ArrayList<Map.Entry<Double, V>>(hashmap.entrySet());
     Collections.sort(entrylist, new KeyComparator());
  
     for (Map.Entry<Double, V> entry : entrylist) {
        map.put(entry.getKey(), entry.getValue());
     }
     return map;
 }
  
  public static <K, V> Map<Long, V> getTopNByKey (Map<Long, V> hashmap, int n) {
      Map<Long, V> map = new LinkedHashMap<Long, V>();
      List<Map.Entry<Long, V>> entrylist = new ArrayList<Map.Entry<Long, V>>(hashmap.entrySet());
      Collections.sort(entrylist, new KeyComparator());
   
      for (Map.Entry<Long, V> entry : entrylist) {
         map.put(entry.getKey(), entry.getValue());
      }
      return map;
  }
}
