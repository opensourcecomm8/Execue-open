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


package com.execue.handler.bean;

import java.util.List;

import com.execue.core.common.bean.entity.KeyWord;

public class UIParallelWord {

   private Long          id;
   private String        checkedState;
   private String        parallelWord;
   private KeyWord       keyword;
   private List<KeyWord> listKeyWord;
   private String        UsersParallelWord;
   private String        isInvalidParallelWord;

   public KeyWord getKeyword () {
      return keyword;
   }

   public void setKeyword (KeyWord keyword) {
      this.keyword = keyword;
   }

   public String getCheckedState () {
      return checkedState;
   }

   public void setCheckedState (String checkedState) {
      this.checkedState = checkedState;
   }

   public String getParallelWord () {
      return parallelWord;
   }

   public void setParallelWord (String parallelWord) {
      this.parallelWord = parallelWord;
   }

   public Long getId () {
      return id;
   }

   public void setId (Long id) {
      this.id = id;
   }

   /**
    * @return the listKeyWord
    */
   public List<KeyWord> getListKeyWord () {
      return listKeyWord;
   }

   /**
    * @param listKeyWord
    *           the listKeyWord to set
    */
   public void setListKeyWord (List<KeyWord> listKeyWord) {
      this.listKeyWord = listKeyWord;
   }

   /**
    * @return the usersParallelWord
    */
   public String getUsersParallelWord () {
      return UsersParallelWord;
   }

   /**
    * @param usersParallelWord
    *           the usersParallelWord to set
    */
   public void setUsersParallelWord (String usersParallelWord) {
      UsersParallelWord = usersParallelWord;
   }

   /**
    * @return the isInvalidParallelWord
    */
   public String getIsInvalidParallelWord () {
      return isInvalidParallelWord;
   }

   /**
    * @param isInvalidParallelWord
    *           the isInvalidParallelWord to set
    */
   public void setIsInvalidParallelWord (String isInvalidParallelWord) {
      this.isInvalidParallelWord = isInvalidParallelWord;
   }
}