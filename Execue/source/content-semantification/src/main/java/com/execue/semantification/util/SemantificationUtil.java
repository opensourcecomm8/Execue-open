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


package com.execue.semantification.util;

import java.util.Date;

import org.apache.log4j.Logger;

import com.execue.core.common.bean.entity.unstructured.SemantifiedContent;
import com.execue.core.common.bean.entity.unstructured.SourceContent;
import com.execue.semantification.bean.SemantificationInput;

/**
 * @author Prasanna
 */
public class SemantificationUtil {

   private static final Logger log = Logger.getLogger(SemantificationUtil.class);

   public static SemantificationInput getSemantificationInput (SourceContent sourceContent) {
      SemantificationInput semantificationInput = new SemantificationInput();
      semantificationInput.setId(sourceContent.getId());
      semantificationInput.setUrl(sourceContent.getUrl());
      semantificationInput.setTitle(sourceContent.getTitle());
      semantificationInput.setSummary(sourceContent.getSummary());
      semantificationInput.setSource(sourceContent.getSource());
      semantificationInput.setContextId(sourceContent.getContextId());
      semantificationInput.setAddedDate(sourceContent.getAddedDate());
      return semantificationInput;
   }

   public static SemantificationInput getSemantificationInputForExistingSemantifiedContent (SemantifiedContent existingSemantifiedContent) {
      SemantificationInput semantificationInput = new SemantificationInput();
      semantificationInput.setId(existingSemantifiedContent.getId());
      semantificationInput.setUrl(existingSemantifiedContent.getUrl());
      semantificationInput.setTitle(existingSemantifiedContent.getShortDescription());
      semantificationInput.setSummary(existingSemantifiedContent.getLongDescription());
      semantificationInput.setSource(existingSemantifiedContent.getContentSource());
      semantificationInput.setContextId(existingSemantifiedContent.getContextId());
      semantificationInput.setAddedDate(existingSemantifiedContent.getContentDate());
      semantificationInput.setResemantification(true);
      semantificationInput.setExistingSemantifiedContent(existingSemantifiedContent);
      return semantificationInput;
   }

   public static SemantifiedContent getSemantifiedContentForSourceContent (SemantificationInput semantificationInput) {
      SemantifiedContent semantifiedContent = new SemantifiedContent();
      semantifiedContent.setContextId(semantificationInput.getContextId());
      semantifiedContent.setArticleRefId(semantificationInput.getId());
      semantifiedContent.setUrl(semantificationInput.getUrl());
      semantifiedContent.setShortDescription(semantificationInput.getTitle());
      semantifiedContent.setLongDescription(semantificationInput.getSummary());
      semantifiedContent.setContentSource(semantificationInput.getSource());
      semantifiedContent.setCreatedDate(new Date());
      semantifiedContent.setContentDate(semantificationInput.getAddedDate());
      return semantifiedContent;
   }
}
