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


package com.execue.handler.comparator;

import java.util.Comparator;

import com.execue.handler.bean.UIRangeDetail;

public class UIRangeDetailOrderComparator implements Comparator<UIRangeDetail> {

   public int compare (UIRangeDetail fromRangeDetail, UIRangeDetail toRangeDetail) {
      int fromRangeDetailOrder = Integer.parseInt(fromRangeDetail.getOrder());
      int toRangeDetailOrder = Integer.parseInt(toRangeDetail.getOrder());
      if (fromRangeDetailOrder > toRangeDetailOrder) {
         return 1;
      } else if (fromRangeDetailOrder < toRangeDetailOrder) {
         return -1;
      } else {
         return 0;
      }
   }

}
