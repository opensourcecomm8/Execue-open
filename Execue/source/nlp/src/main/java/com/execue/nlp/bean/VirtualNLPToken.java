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


//
// Project : Execue NLP
// File Name : VirtualNLPToken.java
// Date : 7/22/2008
// Author : Kaliki
//
//

package com.execue.nlp.bean;

import java.io.Serializable;

/**
 * This call is used for creating Helper tokens what will be used during processing.
 * 
 * @author kaliki
 */

public class VirtualNLPToken extends NLPToken implements Cloneable, Serializable {

   private static final long serialVersionUID = 1L;

   public VirtualNLPToken (NLPToken token) {
      this.word = token.word;
   }

   public VirtualNLPToken (String word) {
      super(word);
   }

   @Override
   public Object clone () throws CloneNotSupportedException {
      return super.clone();
   }

}
