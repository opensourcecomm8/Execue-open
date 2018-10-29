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


/*
 * Created on Aug 21, 2008
 */
package com.execue.nlp.bean.rules.association;

import com.execue.core.common.bean.algorithm.BaseBean;

/**
 * Association Words Info
 * 
 * @author kaliki
 */
public class CAAssociationWord extends BaseBean {

   private String word;
   private double weight;

   public String getWord () {
      return word;
   }

   public void setWord (String word) {
      this.word = word;
   }

   public double getWeight () {
      return weight;
   }

   public void setWeight (double weight) {
      this.weight = weight;
   }

}
