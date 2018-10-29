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
// File Name : TagEntity.java
// Date : 7/22/2008
// Author : Kaliki
//
//

package com.execue.nlp.bean.entity;

import java.io.Serializable;

public class TagEntity extends RecognitionEntity implements Cloneable, Serializable {

   @Override
   public Object clone () throws CloneNotSupportedException {
      return super.clone();
   }
}
