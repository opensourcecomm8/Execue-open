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


package com.execue.nlp.bean.pword;

import java.util.ArrayList;
import java.util.List;

import com.execue.nlp.bean.ProcessorData;

/**
 * @author kaliki
 */
public class PWProcessorData extends ProcessorData {

   private List<PWProcessorToken> input;

   public List<PWProcessorToken> getInput () {
      if (input == null) {
         // this is happening for query like "c1 < 100".
         // Here all the tokens are marked either number or as determiners so
         // the tokens are not getting set in PWProcessorData.
         input = new ArrayList<PWProcessorToken>(1);
      }
      return input;
   }

   public void setInput (List<PWProcessorToken> input) {
      this.input = input;
   }
}
