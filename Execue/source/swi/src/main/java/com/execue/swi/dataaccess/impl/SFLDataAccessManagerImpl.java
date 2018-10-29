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


package com.execue.swi.dataaccess.impl;

import java.util.List;

import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.dataaccess.exception.DataAccessException;
import com.execue.dataaccess.swi.dao.ISFLWordDAO;
import com.execue.swi.dataaccess.ISFLDataAccessManager;
import com.execue.swi.exception.KDXException;

public class SFLDataAccessManagerImpl implements ISFLDataAccessManager {

   private ISFLWordDAO sflWordDAO;

   public ISFLWordDAO getSflWordDAO () {
      return sflWordDAO;
   }

   public void setSflWordDAO (ISFLWordDAO sflWordDAO) {
      this.sflWordDAO = sflWordDAO;
   }

   public void createSFLTerm (SFLTerm sflTerm) throws KDXException {
      try {
         for (SFLTermToken sfTermToken : sflTerm.getSflTermTokens()) {
            sfTermToken.setBusinessTermToken(sfTermToken.getBusinessTermToken().toLowerCase());
            sfTermToken.setSflTerm(sflTerm);
         }
         getSflWordDAO().create(sflTerm);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   public void deleteSFLTerm (SFLTerm sflTerm) throws KDXException {
      try {
         getSflWordDAO().delete(sflTerm);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }

   }

   public SFLTerm getSFLTermByWord (String word) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermByWord(word);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public SFLTerm getSFLTermByWordAndContextId (String word, Long contextId) throws KDXException {
      try {
         return getSflWordDAO().getSFLTermByWordAndContextId(word, contextId);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

   public void deleteSFLTermTokens (List<SFLTermToken> sflTermTokens) throws KDXException {
      try {
         getSflWordDAO().deleteAll(sflTermTokens);
      } catch (DataAccessException e) {
         throw new KDXException(e.code, e.getMessage(), e);
      }
   }

}
