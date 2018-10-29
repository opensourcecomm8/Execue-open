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


package com.execue.publisher.validator;

import java.util.List;
import java.util.Map;

import com.execue.core.common.bean.entity.PublishedFileTableDetails;
import com.execue.core.common.bean.publisher.DBTableColumn;
import com.execue.publisher.exception.PublisherException;
import com.execue.swi.exception.PublishedFileException;
import com.execue.swi.exception.SWIException;

/**
 * This validator is for validating the publisher file table columns for datatypes, name and size.
 * 
 * @author Vishay Gupta
 */
public interface IPublisherColumnValidator {

   public List<DBTableColumn> normalizeColumnNames (String[] columns, boolean isColumnsAvailable)
            throws PublisherException;

   public List<String> validateColumnConversionType (Long fileTableId,
            List<PublishedFileTableDetails> fileTableColumns, Map<Long, PublishedFileTableDetails> fileTableColumnsMap,
            List<PublishedFileTableDetails> validTableColumns) throws SWIException, PublishedFileException;

}
