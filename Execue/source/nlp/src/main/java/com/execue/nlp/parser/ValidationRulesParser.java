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


package com.execue.nlp.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

import com.execue.core.common.type.ValidationRuleType;
import com.execue.nlp.bean.rules.RuleFunction;
import com.execue.nlp.bean.rules.RuleRegexComponent;
import com.execue.nlp.bean.rules.timeframe.TimeFrameRuleFormula;
import com.execue.nlp.bean.rules.timeframe.TimeFrameRuleOperand;
import com.execue.nlp.bean.rules.timeframe.TimeFrameRuleResult;
import com.execue.nlp.bean.rules.validation.ValidationRule;
import com.execue.nlp.bean.rules.validation.ValidationRulesContent;
import com.execue.nlp.parser.helper.RuleNodeFilter;
import com.execue.nlp.rule.IValidationRule;
import com.execue.swi.exception.KDXException;
import com.execue.util.xml.BaseXMLParser;

public class ValidationRulesParser extends BasicRulesParser {

   private static final Logger log = Logger.getLogger(ValidationRulesParser.class);

   /**
    * This method returns the ValidationRulesContent object.
    * 
    * @return ValidationRulesContent
    * @throws ParserConfigurationException
    * @throws SAXException
    * @throws IOException
    * @throws KDXException
    */
   public ValidationRulesContent getValidationRulesContent (List<RuleRegexComponent> validationComponents)
            throws ParserConfigurationException, SAXException, IOException, KDXException {

      ValidationRulesContent validatoniRulesContent = new ValidationRulesContent();

      List<ValidationRule> validationRules = new ArrayList<ValidationRule>();
      rulesDoc = BaseXMLParser.getXMLDoc(fileName);
      Node root = rulesDoc.getDocumentElement();
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      NodeFilter filter = new RuleNodeFilter("validationRule");
      TreeWalker walker = docTraversal.createTreeWalker(root, NodeFilter.SHOW_ALL, filter, false);
      Node curNode = walker.firstChild();
      while (curNode != null) {
         validationRules.add(buildValidationRule(curNode, validationComponents));
         curNode = walker.nextNode();
      }
      if (log.isDebugEnabled()) {
         log.debug("getValidationRulesContent() - validationRuleList size : " + validationRules.size());
      }

      validatoniRulesContent.setValidationRules(getValidationRulesMap(validationRules));
      return validatoniRulesContent;
   }

   private Map<Long, IValidationRule> getValidationRulesMap (List<ValidationRule> validationRules) {
      Map<Long, IValidationRule> validRulesMap = new HashMap<Long, IValidationRule>();
      for (ValidationRule validationRule : validationRules) {
         validRulesMap.put(validationRule.getRuleId(), validationRule);
      }
      return validRulesMap;
   }

   private ValidationRule buildValidationRule (Node timeFrameRuleNode, List<RuleRegexComponent> componentList)
            throws KDXException {
      ValidationRule validationRule = new ValidationRule();

      Element nameElement = (Element) ((Element) timeFrameRuleNode).getElementsByTagName("name").item(0);
      validationRule.setName(nameElement.getChildNodes().item(0).getNodeValue().trim());

      Element ruleIdElement = (Element) ((Element) timeFrameRuleNode).getElementsByTagName("ruleId").item(0);
      String ruleId = ruleIdElement.getChildNodes().item(0).getNodeValue().trim();
      validationRule.setRuleId(Long.parseLong(ruleId));

      Element ruleElement = (Element) ((Element) timeFrameRuleNode).getElementsByTagName("rule").item(0);
      validationRule.setRule(ruleElement.getChildNodes().item(0).getNodeValue().trim());

      Element typeElement = (Element) ((Element) timeFrameRuleNode).getElementsByTagName("ruleType").item(0);
      if (typeElement != null) {
         validationRule.setRuleType(ValidationRuleType.getRuleType(typeElement.getChildNodes().item(0).getNodeValue()
                  .trim()));
      }

      // Expand the rule if it is regex rule
      if (validationRule.getRuleType() == ValidationRuleType.REGEX_RULE) {
         buildExpandedRule(validationRule, componentList);
         Pattern compiledRegexPattern = Pattern.compile(validationRule.getExpandedRule());
         validationRule.setCompiledRegexPattern(compiledRegexPattern);
      }
      return validationRule;
   }

   private void buildExpandedRule (ValidationRule validationRule, List<RuleRegexComponent> componentList) {
      Iterator<RuleRegexComponent> iter = componentList.iterator();
      RuleRegexComponent component = null;
      String expandedRule = validationRule.getRule();

      while (iter.hasNext()) {
         component = iter.next();
         expandedRule = expandedRule.replaceAll(component.getId(), component.getExpression());
      }
      validationRule.setExpandedRule(expandedRule);
   }

   private TimeFrameRuleFormula extractFormula (Node formulaNode) {
      TimeFrameRuleFormula formula = new TimeFrameRuleFormula();

      Element idElement = (Element) ((Element) formulaNode).getElementsByTagName("id").item(0);
      formula.setId(idElement.getChildNodes().item(0).getNodeValue().trim());

      Element stringFormElement = (Element) ((Element) formulaNode).getElementsByTagName("stringForm").item(0);
      formula.setStringForm(stringFormElement.getChildNodes().item(0).getNodeValue().trim());

      Node lhsOperandNode = ((Element) formulaNode).getElementsByTagName("lhsOperand").item(0);
      formula.setLhsOpernad(extractOperand(lhsOperandNode));

      Node operatorNode = ((Element) formulaNode).getElementsByTagName("operator").item(0);
      if (operatorNode != null) {
         formula.setOperator(((Element) operatorNode).getChildNodes().item(0).getNodeValue().trim());
      }

      Node rhsOperandNode = ((Element) formulaNode).getElementsByTagName("rhsOperand").item(0);
      if (rhsOperandNode != null) {
         formula.setRhsOpernad(extractOperand(rhsOperandNode));
      }

      Node resultSetNode = ((Element) formulaNode).getElementsByTagName("resultSet").item(0);
      formula.setResults(extractResultSet(resultSetNode));

      return formula;
   }

   private List<TimeFrameRuleResult> extractResultSet (Node resultSetNode) {
      List<TimeFrameRuleResult> resultList = new ArrayList<TimeFrameRuleResult>();
      NodeFilter filter = new RuleNodeFilter("result");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(resultSetNode, NodeFilter.SHOW_ALL, filter, false);
      Node curNode;
      curNode = walker.firstChild();
      while (curNode != null) {
         resultList.add(extractResult(curNode));
         curNode = walker.nextNode();
      }
      return resultList;
   }

   private TimeFrameRuleOperand extractOperand (Node operandNode) {
      TimeFrameRuleOperand operand = new TimeFrameRuleOperand();

      Element idElement = (Element) ((Element) operandNode).getElementsByTagName("id").item(0);
      operand.setId(idElement.getChildNodes().item(0).getNodeValue().trim());

      Node functionNode = ((Element) operandNode).getElementsByTagName("function").item(0);
      if (functionNode != null) {
         operand.setFunction(extractFunction(functionNode));
      }

      Node formulaNode = ((Element) operandNode).getElementsByTagName("formula").item(0);
      if (formulaNode != null) {
         operand.setFormula(extractFormula(formulaNode));
      }

      return operand;
   }

   private RuleFunction extractFunction (Node functionNode) {
      RuleFunction function = new RuleFunction();

      Element nameElement = (Element) ((Element) functionNode).getElementsByTagName("name").item(0);
      function.setName(nameElement.getChildNodes().item(0).getNodeValue().trim());

      Node argsNode = ((Element) functionNode).getElementsByTagName("arg").item(0);
      if (argsNode != null) {
         NodeList parIdNodeList = ((Element) argsNode).getElementsByTagName("partID");
         int partIdCount = parIdNodeList.getLength();
         List<String> partIdList = new ArrayList<String>();
         for (int index = 0; index < partIdCount; index++) {
            partIdList.add(parIdNodeList.item(index).getChildNodes().item(0).getNodeValue().trim());
         }
         function.setArgList(partIdList);
      }
      return function;
   }

   private TimeFrameRuleResult extractResult (Node resultNode) {
      TimeFrameRuleResult result = new TimeFrameRuleResult();

      Element idElement = (Element) ((Element) resultNode).getElementsByTagName("id").item(0);
      result.setId(idElement.getChildNodes().item(0).getNodeValue().trim());

      Element compareOperatorElement = (Element) ((Element) resultNode).getElementsByTagName("compareOperator").item(0);
      result.setCompareOperator(compareOperatorElement.getChildNodes().item(0).getNodeValue().trim());

      Element valueElement = (Element) ((Element) resultNode).getElementsByTagName("val").item(0);
      result.setValue(valueElement.getChildNodes().item(0).getNodeValue().trim());

      Element valueTypeElement = (Element) ((Element) resultNode).getElementsByTagName("type").item(0);
      result.setValueType(valueTypeElement.getChildNodes().item(0).getNodeValue().trim());

      return result;
   }
}