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

import java.util.List;

import com.execue.core.common.bean.entity.SFLTerm;
import com.execue.core.common.bean.entity.SFLTermToken;
import com.execue.swi.exception.KDXException;

public interface ISFLDataAccessManager {

   public SFLTerm getSFLTermByWord (String word) throws KDXException;

   public void createSFLTerm (SFLTerm sflTerm) throws KDXException;

   public void deleteSFLTerm (SFLTerm sflTerm) throws KDXException;

   public SFLTerm getSFLTermByWordAndContextId (String name, Long contextId) throws KDXException;

   public void deleteSFLTermTokens (List<SFLTermToken> sflTermTokens) throws KDXException;

}
