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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.TreeWalker;
import org.xml.sax.SAXException;

import com.execue.core.common.bean.entity.EntityTripleDefinition;
import com.execue.nlp.bean.rules.AssociationRuleType;
import com.execue.nlp.bean.rules.association.CAAssociationWord;
import com.execue.nlp.bean.rules.association.WeightReductionPart;
import com.execue.nlp.bean.rules.association.WeightReductionPartType;
import com.execue.nlp.bean.rules.weight.assignment.DefaultWeightAssignmentRuleContent;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRule;
import com.execue.nlp.bean.rules.weight.assignment.WeightAssignmentRulesContent;
import com.execue.nlp.parser.helper.RuleNodeFilter;
import com.execue.nlp.type.AssociationDirectionType;
import com.execue.ontology.bean.Triple;
import com.execue.swi.exception.SWIException;
import com.execue.swi.service.IKDXModelService;
import com.execue.util.xml.BaseXMLParser;
import com.execue.util.xml.XMLParserHelper;

public class WeightAssignmentRulesParser extends BasicRulesParser {

   private static final Logger log = Logger.getLogger(WeightAssignmentRulesParser.class);

   private IKDXModelService    kdxModelService;

   public IKDXModelService getKdxModelService () {
      return kdxModelService;
   }

   public void setKdxModelService (IKDXModelService kdxModelService) {
      this.kdxModelService = kdxModelService;
   }

   public WeightAssignmentRulesContent getWeightAssignmentRules () throws ParserConfigurationException, SAXException,
            IOException, SWIException {
      WeightAssignmentRulesContent weightAssignmentRulesContent = new WeightAssignmentRulesContent();
      rulesDoc = BaseXMLParser.getXMLDoc(fileName);
      Node root = rulesDoc.getDocumentElement();

      // Set the regex components
      weightAssignmentRulesContent.setRegexComponents(getRuleComponents());

      // Build the weight assignment rules
      NodeFilter filter = new RuleNodeFilter("weightAssignmentRule");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(root, NodeFilter.SHOW_ALL, filter, false);
      Node curNode;
      curNode = walker.firstChild();
      while (curNode != null) {
         buildWeightAssignmentRules(weightAssignmentRulesContent, curNode);
         curNode = walker.nextNode();
      }

      return weightAssignmentRulesContent;
   }

   private void buildWeightAssignmentRules (WeightAssignmentRulesContent weightAssignmentRulesContent,
            Node associationWeightNode) {

      String associationID = XMLParserHelper.getStringValueForElement(associationWeightNode, "associationID");
      if ("DEFAULT-LEFT".equalsIgnoreCase(associationID)) {
         WeightAssignmentRule defaultLeftWeightAssignmentRule = getDefaultWeightAssignmentRule(associationWeightNode);
         weightAssignmentRulesContent.setDefaultLeftWeightAssignmentRule(defaultLeftWeightAssignmentRule);
      } else if ("DEFAULT-RIGHT".equalsIgnoreCase(associationID)) {
         WeightAssignmentRule defaultRightWeightAssignmentRule = getDefaultWeightAssignmentRule(associationWeightNode);
         weightAssignmentRulesContent.setDefaultRightWeightAssignmentRule(defaultRightWeightAssignmentRule);
      } else {
         addWeightAssignmentRule(weightAssignmentRulesContent, associationWeightNode);
      }
   }

   private WeightAssignmentRule getDefaultWeightAssignmentRule (Node weightAssignmentRuleNode) {
      WeightAssignmentRule defaultWeightAssignmentRule = new WeightAssignmentRule();
      DefaultWeightAssignmentRuleContent defaultWeightAssignmentRuleContent = new DefaultWeightAssignmentRuleContent();

      // Set the association ID
      String associationID = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "associationID");
      defaultWeightAssignmentRuleContent.setAssociationID(associationID);

      String direction = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "associationDirection");

      // Set the association direction
      if ("LEFT".equalsIgnoreCase(direction)) {
         defaultWeightAssignmentRuleContent.setAssociationDirection(AssociationDirectionType.LEFT_ASSOCIATION);
      } else if ("RIGHT".equalsIgnoreCase(direction)) {
         defaultWeightAssignmentRuleContent.setAssociationDirection(AssociationDirectionType.RIGHT_ASSOCIATION);
      }

      String ruleType = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "ruleType");
      AssociationRuleType associationRuleType = AssociationRuleType.getType(ruleType);
      defaultWeightAssignmentRuleContent.setAssociationRuleType(associationRuleType);
      defaultWeightAssignmentRule.setAssociationRuleType(associationRuleType);
      // Set the proximity reduction
      Element prElement = (Element) ((Element) weightAssignmentRuleNode).getElementsByTagName("proximityReduction")
               .item(0);
      double proximityReduction = 1.0;
      try {
         proximityReduction = Double.parseDouble(prElement.getChildNodes().item(0).getNodeValue());
      } catch (Exception e) {
      }
      defaultWeightAssignmentRuleContent.setProximityReduction(proximityReduction);

      // Set the default weight
      Element weightElement = (Element) ((Element) weightAssignmentRuleNode).getElementsByTagName("defaultWeight")
               .item(0);
      double weight = 10.0;
      try {
         weight = Double.parseDouble(weightElement.getChildNodes().item(0).getNodeValue());
      } catch (Exception e) {
         e.printStackTrace();
      }
      defaultWeightAssignmentRuleContent.setDefaultWeight(weight);

      // Set the words based weight
      NodeFilter filter = new RuleNodeFilter("wordBasedWeight");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(weightAssignmentRuleNode, NodeFilter.SHOW_ALL, filter, false);
      Node curNode = walker.firstChild();
      while (curNode != null) {
         defaultWeightAssignmentRuleContent.setAssociationWordsInfo(buildAssociationWordList(curNode));
         curNode = walker.nextNode();
      }

      defaultWeightAssignmentRule.setDefaultWeightAssignmentRuleContent(defaultWeightAssignmentRuleContent);
      return defaultWeightAssignmentRule;
   }

   /**
    * @param weightAssignmentRulesContent
    * @param weightAssignmentRuleNode
    */
   private void addWeightAssignmentRule (WeightAssignmentRulesContent weightAssignmentRulesContent,
            Node weightAssignmentRuleNode) {

      WeightAssignmentRule weightAssignmentRule = new WeightAssignmentRule();

      // Set the association ID
      String associationID = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "associationID");
      weightAssignmentRule.setAssociationID(associationID);

      // Set the default weight assignment rule
      String defaultExtendedRule = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "extends");
      String ruleType = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "ruleType");
      AssociationRuleType associationRuleType = AssociationRuleType.getType(ruleType);
      if (associationRuleType != null && associationRuleType.equals(AssociationRuleType.VALIDATION)) {
         weightAssignmentRule.setAssociationRuleType(associationRuleType);
      }
      if ("DEFAULT-LEFT".equalsIgnoreCase(defaultExtendedRule)) {
         DefaultWeightAssignmentRuleContent defaultLeftWeightAssignmentRule = ((WeightAssignmentRule) weightAssignmentRulesContent
                  .getDefaultLeftWeightAssignmentRule()).getDefaultWeightAssignmentRuleContent();
         weightAssignmentRule.setDefaultWeightAssignmentRuleContent(defaultLeftWeightAssignmentRule);
      } else if ("DEFAULT-RIGHT".equalsIgnoreCase(defaultExtendedRule)) {
         DefaultWeightAssignmentRuleContent defaultWeightAssignmentRuleContent = ((WeightAssignmentRule) weightAssignmentRulesContent
                  .getDefaultRightWeightAssignmentRule()).getDefaultWeightAssignmentRuleContent();
         weightAssignmentRule.setDefaultWeightAssignmentRuleContent(defaultWeightAssignmentRuleContent);
      }

      // Set the rule ID
      String ruleID = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "ruleId");
      weightAssignmentRule.setRuleId(new Long(ruleID));
      String defaultWeight = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "defaultWeight");
      if (!"".equals(defaultWeight)) {
         double weight = Double.parseDouble(defaultWeight);
         DefaultWeightAssignmentRuleContent defaultWeightAssignmentRuleContent;
         try {
            defaultWeightAssignmentRuleContent = weightAssignmentRule.getDefaultWeightAssignmentRuleContent().clone();
            defaultWeightAssignmentRuleContent.setDefaultWeight(weight);
            weightAssignmentRule.setDefaultWeightAssignmentRuleContent(defaultWeightAssignmentRuleContent);
         } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }

      }

      // Set the triple info
      NodeFilter filter = new RuleNodeFilter("tripleInfo");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(weightAssignmentRuleNode, NodeFilter.SHOW_ALL, filter, false);
      Node curNode = walker.firstChild();
      while (curNode != null) {
         Triple triple = new Triple();
         Element element = (Element) ((Element) curNode).getElementsByTagName("subject").item(0);
         triple.setDomain(element.getChildNodes().item(0).getNodeValue());
         element = (Element) ((Element) curNode).getElementsByTagName("object").item(0);
         triple.setRange(element.getChildNodes().item(0).getNodeValue());
         element = (Element) ((Element) curNode).getElementsByTagName("predicate").item(0);
         triple.setProperty(element.getChildNodes().item(0).getNodeValue());
         element = (Element) ((Element) curNode).getElementsByTagName("subjectBedId").item(0);
         triple.setDomainBedId(Long.valueOf(element.getChildNodes().item(0).getNodeValue()));
         element = (Element) ((Element) curNode).getElementsByTagName("objectBedId").item(0);
         triple.setRangeBedId(Long.valueOf(element.getChildNodes().item(0).getNodeValue()));
         element = (Element) ((Element) curNode).getElementsByTagName("predicateBedId").item(0);
         triple.setPropertyBedId(Long.valueOf(element.getChildNodes().item(0).getNodeValue()));

         // TODO -AP-VISHAY- Add Etd ID as part of Triple info in Concept Attribute Association file

         EntityTripleDefinition etd = null;
         try {
            etd = getKdxModelService().getEntityTriple(triple.getDomainBedId(), triple.getPropertyBedId(),
                     triple.getRangeBedId());
         } catch (SWIException e) {
            e.printStackTrace();
         }
         if (etd != null) {
            weightAssignmentRule.addTriple(etd);
         }
         curNode = walker.nextNode();
      }

      // Set the association Type
      String associationType = XMLParserHelper.getStringValueForElement(weightAssignmentRuleNode, "associationType");
      weightAssignmentRule.setAssociationType(associationType);

      // Set the weight reduction pattern list
      weightAssignmentRule.setPatternList(buildWeightReductionPatternList(weightAssignmentRuleNode));

      weightAssignmentRulesContent.addWeightAssignmentRule(weightAssignmentRule);
   }

   private List<WeightReductionPart> buildWeightReductionPatternList (Node ruleNode) {
      List<WeightReductionPart> patternList = new ArrayList<WeightReductionPart>();

      NodeFilter filter = new RuleNodeFilter("weightReductionRule");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(ruleNode, NodeFilter.SHOW_ALL, filter, false);
      Node curNode = walker.firstChild();
      while (curNode != null) {

         WeightReductionPart part = new WeightReductionPart();
         Element element = (Element) ((Element) curNode).getElementsByTagName("partType").item(0);
         part.setPartType(WeightReductionPartType.getType(element.getChildNodes().item(0).getNodeValue()));

         element = (Element) ((Element) curNode).getElementsByTagName("weightReductionValue").item(0);
         double weight = 10.0;
         try {
            String wtVal = element.getChildNodes().item(0).getNodeValue();
            weight = Double.parseDouble(wtVal);
         } catch (Exception e) {
         }
         part.setWeightReductionValue(weight);

         buildRulePart(curNode, part, getRuleComponents());

         patternList.add(part);
         curNode = walker.nextNode();
      }
      return patternList;
   }

   private List<CAAssociationWord> buildAssociationWordList (Node associationWordNode) {
      List<CAAssociationWord> associationWordList = new ArrayList<CAAssociationWord>();

      NodeFilter filter = new RuleNodeFilter("associationWord");
      DocumentTraversal docTraversal = (DocumentTraversal) rulesDoc;
      TreeWalker walker = docTraversal.createTreeWalker(associationWordNode, NodeFilter.SHOW_ALL, filter, false);
      Node curNode = walker.firstChild();
      while (curNode != null) {
         CAAssociationWord associationWord = new CAAssociationWord();

         Element nameElement = (Element) ((Element) curNode).getElementsByTagName("word").item(0);
         associationWord.setWord(nameElement.getChildNodes().item(0).getNodeValue());

         Element weightElement = (Element) ((Element) curNode).getElementsByTagName("weight").item(0);
         double weight = 10.0;
         try {
            weight = Double.parseDouble(weightElement.getChildNodes().item(0).getNodeValue());
         } catch (Exception e) {
         }
         associationWord.setWeight(weight);
         associationWordList.add(associationWord);

         curNode = walker.nextNode();
      }
      return associationWordList;
   }
}
