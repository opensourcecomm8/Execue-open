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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;

import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.bean.rules.RuleFunction;
import com.execue.nlp.bean.rules.RulePart;
import com.execue.nlp.bean.rules.RulePattern;
import com.execue.nlp.bean.rules.RuleRecognitionConceptReference;
import com.execue.nlp.bean.rules.RuleRecognitionInfo;
import com.execue.nlp.bean.rules.RuleRecognitionValue;
import com.execue.nlp.bean.rules.RuleRegexComponent;
import com.execue.nlp.bean.rules.RuleValueFunctionPart;
import com.execue.nlp.bean.rules.RuleValuePart;
import com.execue.nlp.bean.rules.RuleValueSeparator;
import com.execue.nlp.parser.helper.RuleNodeFilter;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IBaseKDXRetrievalService;
import com.execue.util.xml.XMLParserHelper;

/**
 * @author kaliki
 */
public class BasicRulesParser {

   protected Document               rulesDoc;

   protected String                 fileName;
   List<RuleRegexComponent>         ruleComponents;

   private IBaseKDXRetrievalService baseKDXRetrievalService;

   public String getFileName () {
      return fileName;
   }

   public void setFileName (String fileName) {
      this.fileName = fileName;
   }

   // Other Methods

   protected List<RuleRegexComponent> buildRuleComponentList () {
      List<RuleRegexComponent> ruleComponents = new ArrayList<RuleRegexComponent>();

      Node root = rulesDoc.getDocumentElement();
      NodeFilter filter = new RuleNodeFilter("component");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(root, NodeFilter.SHOW_ALL, filter, false);
      Node curNode;
      curNode = walker.firstChild();
      while (curNode != null) {
         ruleComponents.add(buildRuleComponent(curNode));
         curNode = walker.nextNode();
      }
      return ruleComponents;
   }

   protected void buildRulePart (Node partNode, RulePart rulePart, List<RuleRegexComponent> componentList) {

      Element idElement = (Element) ((Element) partNode).getElementsByTagName("partID").item(0);
      rulePart.setId(idElement.getChildNodes().item(0).getNodeValue().trim());

      Element ruleElement = (Element) ((Element) partNode).getElementsByTagName("regex").item(0);
      rulePart.setExpression(ruleElement.getChildNodes().item(0).getNodeValue().trim());

      Node conceptNode = ((Element) partNode).getElementsByTagName("concept").item(0);
      if (conceptNode != null) {
         rulePart.setDefaultConceptName(conceptNode.getChildNodes().item(0).getNodeValue().trim());
      }

      Element requiredElement = (Element) ((Element) partNode).getElementsByTagName("required").item(0);
      rulePart.setRequired(Boolean.valueOf(requiredElement.getChildNodes().item(0).getNodeValue().trim()));

      buildExpandedRulePart(rulePart, componentList);

   }

   private void buildExpandedRulePart (RulePart rulePart, List<RuleRegexComponent> componentList) {
      Iterator<RuleRegexComponent> iter = componentList.iterator();
      RuleRegexComponent component = null;
      String expandedRule = rulePart.getExpression();

      while (iter.hasNext()) {
         component = iter.next();
         expandedRule = expandedRule.replaceAll(component.getId(), component.getExpression());
      }
      rulePart.setExpandedExpression(expandedRule);
      try {
         java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(expandedRule);
         rulePart.setExpandedExpressionPattern(pattern);
      } catch (Exception e) {
         // TODO: handle exception
      }
   }

   protected RuleRegexComponent buildRuleComponent (Node componentNode) {
      RuleRegexComponent component = new RuleRegexComponent();

      Element nameElement = (Element) ((Element) componentNode).getElementsByTagName("name").item(0);
      component.setName(nameElement.getChildNodes().item(0).getNodeValue().trim());

      Element idElement = (Element) ((Element) componentNode).getElementsByTagName("patternHolder").item(0);
      component.setId(idElement.getChildNodes().item(0).getNodeValue().trim());

      NodeList conceptNodeList = ((Element) componentNode).getElementsByTagName("concept");
      if (conceptNodeList != null && conceptNodeList.getLength() > 0) {
         Element conceptElement = (Element) conceptNodeList.item(0);
         component.setConcepts(ExecueStringUtil.getAsList(conceptElement.getChildNodes().item(0).getNodeValue().trim(),
                  ","));
      }

      NodeList nlpTagTypeNodeList = ((Element) componentNode).getElementsByTagName("nlpTag");
      if (nlpTagTypeNodeList != null && nlpTagTypeNodeList.getLength() > 0) {
         Element nlpTagTypeElement = (Element) nlpTagTypeNodeList.item(0);
         component.setNlpTagTypes(ExecueStringUtil.getAsList(nlpTagTypeElement.getChildNodes().item(0).getNodeValue()
                  .trim(), ","));
      }

      Element expressionElement = (Element) ((Element) componentNode).getElementsByTagName("regex").item(0);
      component.setExpression(expressionElement.getChildNodes().item(0).getNodeValue().trim());

      return component;
   }

   public void buildPattern (Node patternNode, RulePattern pattern) {
      Element idElement = (Element) ((Element) patternNode).getElementsByTagName("patternID").item(0);

      if (idElement == null) {
         idElement = (Element) ((Element) patternNode).getElementsByTagName("partID").item(0);
      }

      pattern.setId(idElement.getChildNodes().item(0).getNodeValue());

      Node regexNode = ((Element) patternNode).getElementsByTagName("regex").item(0);
      if (regexNode != null) {
         pattern.setRegex(regexNode.getChildNodes().item(0).getNodeValue());
      }

      Node superConceptNode = ((Element) patternNode).getElementsByTagName("superConcept").item(0);
      if (superConceptNode != null && superConceptNode.getChildNodes().getLength() > 0) {
         pattern.setSuperConcepts(ExecueStringUtil.getAsList(superConceptNode.getChildNodes().item(0).getNodeValue(),
                  ","));
      }

      Node excludeSuperConceptNode = ((Element) patternNode).getElementsByTagName("excludeSuperConcept").item(0);
      if (excludeSuperConceptNode != null && excludeSuperConceptNode.getChildNodes().getLength() > 0) {
         pattern.setExcludeSuperConcepts(ExecueStringUtil.getAsList(excludeSuperConceptNode.getChildNodes().item(0)
                  .getNodeValue(), ","));
      }

      Node nlpTagNode = ((Element) patternNode).getElementsByTagName("recognitionNlpTag").item(0);
      if (nlpTagNode != null) {
         pattern.setNlpTags(ExecueStringUtil.getAsList(nlpTagNode.getChildNodes().item(0).getNodeValue(), ","));
      }

      Node excludeNlpTagNode = ((Element) patternNode).getElementsByTagName("excludeRecognitionNlpTag").item(0);
      if (excludeNlpTagNode != null) {
         pattern.setExcludeNlpTags(ExecueStringUtil.getAsList(excludeNlpTagNode.getChildNodes().item(0).getNodeValue(),
                  ","));
      }

      Node fixedPartNode = ((Element) patternNode).getElementsByTagName("fixedPart").item(0);
      if (fixedPartNode != null) {
         pattern.setFixedPart(Boolean.valueOf(fixedPartNode.getChildNodes().item(0).getNodeValue()));
      }

      Node requiredElementNode = ((Element) patternNode).getElementsByTagName("required").item(0);
      if (requiredElementNode != null) {
         pattern.setRequired(Boolean.valueOf(requiredElementNode.getChildNodes().item(0).getNodeValue()));
      }
   }

   protected List<RuleRecognitionInfo> buildRecognitionInfos (Node recognitionInfoNode) throws KDXException {
      List<RuleRecognitionInfo> recognitionInfo = new ArrayList<RuleRecognitionInfo>();

      NodeFilter filter = new RuleNodeFilter("recognitionInfo");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(recognitionInfoNode, NodeFilter.SHOW_ALL, filter, false);
      Node curNode;
      curNode = walker.firstChild();
      while (curNode != null) {
         recognitionInfo.add(buildRecognitionInfo(curNode));
         curNode = walker.nextNode();
      }
      return recognitionInfo;
   }

   private RuleRecognitionInfo buildRecognitionInfo (Node recognitionInfoNode) throws KDXException {
      RuleRecognitionInfo recognitionInfo = new RuleRecognitionInfo();

      if (((Element) recognitionInfoNode).getElementsByTagName("resultIdSet").item(0) != null) {
         recognitionInfo.setResultIds(ExecueStringUtil.getAsList(XMLParserHelper.getStringValueForElement(
                  recognitionInfoNode, "resultId"), ","));
      }

      if (((Element) recognitionInfoNode).getElementsByTagName("recognitionGroup").item(0) != null) {
         recognitionInfo.setRecognitionGroup(XMLParserHelper.getStringValueForElement(recognitionInfoNode,
                  "recognitionGroup"));
      }

      if (((Element) recognitionInfoNode).getElementsByTagName("recognitionConcept").item(0) != null) {
         String recConcept = XMLParserHelper.getStringValueForElement(recognitionInfoNode, "recognitionConcept");
         recognitionInfo.setRecognitionConcept(recConcept);
         // recognitionInfo.setRecognitionConceptID(baseKDXRetrievalService.getConceptBEDByName(recConcept).getId());
         // TODO: NK: should set the recognition type id instead of concept id in future(also recognitionType in rules
         // xml)
         recognitionInfo.setRecognitionConceptID(baseKDXRetrievalService.getTypeBEDByName(recConcept).getId());
      }

      if (((Element) recognitionInfoNode).getElementsByTagName("recognitionNlpTag").item(0) != null) {
         recognitionInfo.setRecognitionNLPTag(XMLParserHelper.getStringValueForElement(recognitionInfoNode,
                  "recognitionNlpTag"));
      }

      if (((Element) recognitionInfoNode).getElementsByTagName("recognitionGroupSuffix").item(0) != null) {
         recognitionInfo.setRecognitionGroupSuffix(XMLParserHelper.getStringValueForElement(recognitionInfoNode,
                  "recognitionGroupSuffix"));
      }

      Node valueReferenceNode = ((Element) recognitionInfoNode).getElementsByTagName("recognitionValueReference").item(
               0);
      if (valueReferenceNode != null && ((Element) valueReferenceNode).getElementsByTagName("partID").item(0) != null) {
         recognitionInfo.setRecognitionValueReference(XMLParserHelper.getStringValueForElement(valueReferenceNode,
                  "partID"));
      }

      Node conceptReferenceNode = ((Element) recognitionInfoNode).getElementsByTagName("recognitionConceptReference")
               .item(0);
      if (conceptReferenceNode != null) {
         recognitionInfo.setRecognitionConceptReference(extractRecognitionConceptReference(conceptReferenceNode));
      }

      Node placeValueNode = ((Element) recognitionInfoNode).getElementsByTagName("recognitionValue").item(0);
      if (placeValueNode != null) {
         recognitionInfo.setRecognitionValue(extractRecognitionValue(placeValueNode));
      }

      if (((Element) recognitionInfoNode).getElementsByTagName("recognitionCandidates").item(0) != null) {
         Node recCandidatesNode = ((Element) recognitionInfoNode).getElementsByTagName("recognitionCandidates").item(0);
         recognitionInfo.setRecognitionCandidates(ExecueStringUtil.getAsList(XMLParserHelper.getStringValueForElement(
                  recCandidatesNode, "partIDs"), ","));
      }

      return recognitionInfo;
   }

   private RuleRecognitionConceptReference extractRecognitionConceptReference (Node conceptReferenceNode) {
      RuleRecognitionConceptReference ruleRecognitionConceptReference = new RuleRecognitionConceptReference();
      ruleRecognitionConceptReference.setPartIds(ExecueStringUtil.getAsList(XMLParserHelper.getStringValueForElement(
               conceptReferenceNode, "partIDs"), ","));
      return ruleRecognitionConceptReference;
   }

   private RuleRecognitionValue extractRecognitionValue (Node placeValueNode) {
      // Create Recognition Value
      RuleRecognitionValue recognitionValue = new RuleRecognitionValue();
      // Create List of value parts
      List<RuleValuePart> valuePartsList = new ArrayList<RuleValuePart>();
      // Create a filter for getting all the value parts
      NodeFilter filter = new RuleNodeFilter("valuePart");
      // Create Tree traversal and tree walker objects to traverse the tree
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(placeValueNode, NodeFilter.SHOW_ALL, filter, false);
      // Iterate over each value part and process it
      Node curNode = walker.firstChild();
      while (curNode != null) {
         valuePartsList.add(extractValuePart(curNode));
         curNode = walker.nextNode();
      }
      // Set the Recognition value parts
      recognitionValue.setValuePartList(valuePartsList);
      // Return recognition value
      return recognitionValue;
   }

   private RuleValuePart extractValuePart (Node valuePartNode) {
      // Place holder for value parts
      RuleValuePart valuePart = null;
      // Get the type of value part
      String type = ((Element) valuePartNode).getElementsByTagName("type").item(0).getFirstChild().getNodeValue()
               .trim();
      // Based on the type process each value part
      if (type.equalsIgnoreCase(RuleValuePart.FUNCTION)) {
         valuePart = new RuleValueFunctionPart();
         ((RuleValueFunctionPart) valuePart).setFunction(extractFunction(valuePartNode));
      } else if (type.equalsIgnoreCase(RuleValuePart.SEPARATOR)) {
         valuePart = new RuleValueSeparator();
         ((RuleValueSeparator) valuePart).setSeparator(extractSeparator(valuePartNode));
      }
      return valuePart;
   }

   private String extractSeparator (Node valuePartNode) {
      return StringEscapeUtils.unescapeXml(((Element) valuePartNode).getElementsByTagName("value").item(0)
               .getFirstChild().getNodeValue());
   }

   private RuleFunction extractFunction (Node functionNode) {
      RuleFunction function = new RuleFunction();

      Element nameElement = (Element) ((Element) functionNode).getElementsByTagName("function").item(0);
      function.setName(nameElement.getChildNodes().item(0).getNodeValue().trim());

      Node argsNode = ((Element) functionNode).getElementsByTagName("args").item(0);
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

   public IBaseKDXRetrievalService getBaseKDXRetrievalService () {
      return baseKDXRetrievalService;
   }

   public void setBaseKDXRetrievalService (IBaseKDXRetrievalService baseKDXRetrievalService) {
      this.baseKDXRetrievalService = baseKDXRetrievalService;
   }

   /**
    * @return the ruleComponents
    */
   public List<RuleRegexComponent> getRuleComponents () {
      return ruleComponents;
   }

   /**
    * @param ruleComponents
    *           the ruleComponents to set
    */
   public void setRuleComponents (List<RuleRegexComponent> ruleComponents) {
      this.ruleComponents = ruleComponents;
   }
}
