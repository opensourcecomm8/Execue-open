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


package com.execue.core.common.bean.publisher;

import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

/**
 * This bean represents the batch data context
 * 
 * @author Vishay
 * @since 20/10/09
 * @version 1.0
 */
public class PublisherBatchDataReadContext {

   private CsvReader           csvReader;
   private int                 batchSize;
   private int                 startingLine;
   private String              delimeter;
   private int                 maxDataLength;
   private List<CharSequence>  charactersToEscape;
   private String              escapeCharacter;
   private int                 numColumns;
   private String              csvEmptyField;
   private Map<String, String> replaceValueMapToAvoidScriptInjection;

   public int getBatchSize () {
      return batchSize;
   }

   public void setBatchSize (int batchSize) {
      this.batchSize = batchSize;
   }

   public int getStartingLine () {
      return startingLine;
   }

   public void setStartingLine (int startingLine) {
      this.startingLine = startingLine;
   }

   public String getDelimeter () {
      return delimeter;
   }

   public void setDelimeter (String delimeter) {
      this.delimeter = delimeter;
   }

   public int getMaxDataLength () {
      return maxDataLength;
   }

   public void setMaxDataLength (int maxDataLength) {
      this.maxDataLength = maxDataLength;
   }

   public List<CharSequence> getCharactersToEscape () {
      return charactersToEscape;
   }

   public void setCharactersToEscape (List<CharSequence> charactersToEscape) {
      this.charactersToEscape = charactersToEscape;
   }

   public String getEscapeCharacter () {
      return escapeCharacter;
   }

   public void setEscapeCharacter (String escapeCharacter) {
      this.escapeCharacter = escapeCharacter;
   }

   public int getNumColumns () {
      return numColumns;
   }

   public void setNumColumns (int numColumns) {
      this.numColumns = numColumns;
   }

   public CsvReader getCsvReader () {
      return csvReader;
   }

   public void setCsvReader (CsvReader csvReader) {
      this.csvReader = csvReader;
   }

   public String getCsvEmptyField () {
      return csvEmptyField;
   }

   public void setCsvEmptyField (String csvEmptyField) {
      this.csvEmptyField = csvEmptyField;
   }

   public Map<String, String> getReplaceValueMapToAvoidScriptInjection () {
      return replaceValueMapToAvoidScriptInjection;
   }

   public void setReplaceValueMapToAvoidScriptInjection (Map<String, String> replaceValueMapToAvoidScriptInjection) {
      this.replaceValueMapToAvoidScriptInjection = replaceValueMapToAvoidScriptInjection;
   }

}
