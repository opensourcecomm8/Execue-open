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


package com.execue.swi.dataaccess;

import com.execue.dataaccess.swi.dao.IKeywordDAO;
import com.execue.dataaccess.swi.dao.IParallelWordDAO;
import com.execue.dataaccess.swi.dao.IProfileDAO;
import com.execue.dataaccess.swi.dao.IRangeDAO;

public abstract class PreferencesDAOComponents {

   private IProfileDAO      profileDAO;
   private IParallelWordDAO parallelWordDAO;
   private IKeywordDAO      keywordDAO;
   private IRangeDAO        rangeDAO;

   public IProfileDAO getProfileDAO () {
      return profileDAO;
   }

   public void setProfileDAO (IProfileDAO profileDAO) {
      this.profileDAO = profileDAO;
   }

   public IParallelWordDAO getParallelWordDAO () {
      return parallelWordDAO;
   }

   public void setParallelWordDAO (IParallelWordDAO parallelWordDAO) {
      this.parallelWordDAO = parallelWordDAO;
   }

   public IKeywordDAO getKeywordDAO () {
      return keywordDAO;
   }

   public void setKeywordDAO (IKeywordDAO keywordDAO) {
      this.keywordDAO = keywordDAO;
   }

   public IRangeDAO getRangeDAO () {
      return rangeDAO;
   }

   public void setRangeDAO (IRangeDAO rangeDAO) {
      this.rangeDAO = rangeDAO;
   }

}
