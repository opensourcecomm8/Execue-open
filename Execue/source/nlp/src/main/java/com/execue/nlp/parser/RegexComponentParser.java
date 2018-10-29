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


/**
 * 
 */
package com.execue.nlp.parser;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.execue.nlp.bean.rules.RuleRegexComponent;
import com.execue.util.xml.BaseXMLParser;

/**
 * @author Nitesh
 */
public class RegexComponentParser extends BasicRulesParser {

   private static final Logger log = Logger.getLogger(RegexComponentParser.class);

   public List<RuleRegexComponent> getRegExComponents () throws ParserConfigurationException, SAXException, IOException {
      rulesDoc = BaseXMLParser.getXMLDoc(fileName);
      return buildRuleComponentList();
   }
}
