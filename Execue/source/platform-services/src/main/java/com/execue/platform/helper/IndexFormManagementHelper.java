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


package com.execue.platform.helper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.execue.core.common.bean.entity.BusinessEntityDefinition;
import com.execue.core.common.bean.entity.CloudComponent;
import com.execue.core.common.bean.entity.Concept;
import com.execue.core.common.bean.entity.ConceptProfile;
import com.execue.core.common.bean.entity.EntityNameVariation;
import com.execue.core.common.bean.entity.Instance;
import com.execue.core.common.bean.entity.InstanceProfile;
import com.execue.core.common.bean.entity.RICloud;
import com.execue.core.common.bean.entity.Relation;
import com.execue.core.common.type.BusinessEntityType;
import com.execue.core.common.type.RecognitionType;
import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.swi.configuration.ISWIConfigurationService;
import com.execue.swi.exception.KDXException;
import com.execue.swi.service.IKDXRetrievalService;

/**
 * @author John Mallavalli
 */
public class IndexFormManagementHelper {

   /**
    * list of the special characters, if occurred, will be replaced by spaces
    */
   private static List<String>      specialChars      = new ArrayList<String>();
   /**
    * list of the special characters which are valid
    */
   private static List<String>      validSpecialChars = new ArrayList<String>();

   private static final String      SINGLE_SPACE      = " ";
   static {

      // This validSpecialChars are used to trim the variation:
      // for example abc#def ==> abcdef

      specialChars.add("`");
      specialChars.add("~");
      specialChars.add("!");
      specialChars.add("@");
      specialChars.add("#");
      specialChars.add("$");
      specialChars.add("%");
      specialChars.add("^");
      specialChars.add("&");
      specialChars.add("*");
      specialChars.add("(");
      specialChars.add(")");
      specialChars.add("_");
      specialChars.add("=");
      specialChars.add("+");
      specialChars.add("[");
      specialChars.add("{");
      specialChars.add("]");
      specialChars.add("}");
      specialChars.add("\\");
      specialChars.add("|");
      specialChars.add(";");
      specialChars.add(":");
      specialChars.add("'");
      specialChars.add("\"");
      specialChars.add(",");
      specialChars.add("<");
      specialChars.add(">");
      specialChars.add("/");
      specialChars.add("?");

      // This validSpecialChars are used to generate the below variation:
      // for example execue.com ==> "execue com" and one-way ==> "one way"
      validSpecialChars.add(".");
      validSpecialChars.add("-");
   }

   private IKDXRetrievalService     kdxRetrievalService;
   private ISWIConfigurationService swiConfigurationService;

   /**
    * This method returns all the possible variations that can be considered for generating the RI Onto Terms and the
    * SFL Terms
    * 
    * @param BusinessEntityDefinition
    *           businessTerm for which the index forms are to be generated
    * @return list of possible variations
    */
   public List<EntityNameVariation> generateVariations (BusinessEntityDefinition businessTerm) {
      List<EntityNameVariation> variations = new ArrayList<EntityNameVariation>();
      BusinessEntityType entityType = businessTerm.getEntityType();
      // Flag to By default skip the secondary word processing for entity variation in sfl generation logic
      boolean skipSecondaryWordCheck = true;

      // If type of the entity variation is measurable entity, onto entity or name type, then
      // we need to check for the secondary token in the sfl term generation logic, hence set skipSecondaryWordCheck to
      // false
      if (businessTerm.getType() != null) {
         String typeName = businessTerm.getType().getName();
         List<String> typeNames = getSwiConfigurationService().getTypeNames();
         if (typeNames.contains(typeName)) {
            skipSecondaryWordCheck = false;
         }
      }
      if (BusinessEntityType.CONCEPT.equals(entityType)) {
         Concept concept = businessTerm.getConcept();
         // variation for name
         String exactVariationName = concept.getName().trim();
         String displayVariationName = concept.getDisplayName();
         String descriptionVariationName = concept.getDescription();
         populateVariations(variations, exactVariationName, displayVariationName, descriptionVariationName,
                  skipSecondaryWordCheck);
      } else if (BusinessEntityType.CONCEPT_LOOKUP_INSTANCE.equals(entityType)
               || BusinessEntityType.TYPE_LOOKUP_INSTANCE.equals(entityType)) {
         Instance instance = businessTerm.getInstance();
         String displayVariation = instance.getDisplayName();
         String descriptionVariation = instance.getDescription();
         String abbreviationVariation = instance.getAbbreviatedName();
         String expressionVariation = instance.getExpression();

         populateVariationsForInstance(variations, displayVariation, descriptionVariation, abbreviationVariation,
                  expressionVariation, skipSecondaryWordCheck);
      } else if (BusinessEntityType.RELATION.equals(entityType)) {
         Relation relation = businessTerm.getRelation();
         // variation for name
         String relationName = relation.getName().trim();
         prepareVariations(variations, relationName, RecognitionType.Exact, false, skipSecondaryWordCheck);
         // variation for display name
         String relationDisplayName = relation.getDisplayName();
         prepareVariations(variations, relationDisplayName, RecognitionType.DisplayName, false, skipSecondaryWordCheck);
         // check if the display name contains valid special chars
         String relationDisplayNameWithValidSpChars = handleValidSpecialChars(relationDisplayName);
         if (ExecueCoreUtil.isNotEmpty(relationDisplayNameWithValidSpChars)) {
            relationDisplayNameWithValidSpChars = ExecueStringUtil
                     .removeExtraWhiteSpaces(relationDisplayNameWithValidSpChars);
            prepareVariations(variations, relationDisplayNameWithValidSpChars, RecognitionType.DisplayName, false,
                     skipSecondaryWordCheck);
         }
         // variation for description
         String relationDescription = relation.getDescription();
         if (ExecueCoreUtil.isNotEmpty(relationDescription)) {
            prepareVariations(variations, relationDescription, RecognitionType.Description, false,
                     skipSecondaryWordCheck);
            // check if the display name contains valid special chars
            String relationDescWithValidSpChars = handleValidSpecialChars(relationDescription);
            if (ExecueCoreUtil.isNotEmpty(relationDescWithValidSpChars)) {
               relationDescWithValidSpChars = ExecueStringUtil.removeExtraWhiteSpaces(relationDescWithValidSpChars);
               prepareVariations(variations, relationDescWithValidSpChars, RecognitionType.Description, false,
                        skipSecondaryWordCheck);
            }
         }
      } else if (BusinessEntityType.CONCEPT_PROFILE.equals(entityType)) {
         ConceptProfile conceptProfile = businessTerm.getConceptProfile();
         String exactVariationName = conceptProfile.getName().trim();
         String displayVariationName = conceptProfile.getDisplayName();
         String descriptionVariationName = conceptProfile.getDescription();

         populateVariations(variations, exactVariationName, displayVariationName, descriptionVariationName,
                  skipSecondaryWordCheck);
      } else if (BusinessEntityType.INSTANCE_PROFILE.equals(entityType)) {
         InstanceProfile instanceProfile = businessTerm.getInstanceProfile();
         String exactVariationName = instanceProfile.getName();
         String displayVariationName = instanceProfile.getDisplayName();
         String descriptionVariationName = instanceProfile.getDescription();
         populateVariations(variations, exactVariationName, displayVariationName, descriptionVariationName,
                  skipSecondaryWordCheck);
      }
      // Handle Business Entity Variations for the business term
      try {
         // get the BEV objects for the BE in hand
         List<String> businessEntityVariationNames = getKdxRetrievalService().getBusinessEntityVariationNames(
                  businessTerm.getId());
         if (ExecueCoreUtil.isCollectionNotEmpty(businessEntityVariationNames)) {
            for (String entityVariation : businessEntityVariationNames) {
               prepareVariations(variations, entityVariation, RecognitionType.EntityVariant, false,
                        skipSecondaryWordCheck);
            }
         }
      } catch (KDXException e) {
         e.printStackTrace();
      }
      // clean up the list of variations by removing the duplicates
      variations = removeVariationDupes(variations);
      return variations;
   }

   /**
    * @param variations
    * @param displayVariation
    * @param descriptionVariationName
    * @param abbreviationVariation
    * @param expressionVariation
    * @param skipSecondaryWordCheck
    *           TODO
    */
   private void populateVariationsForInstance (List<EntityNameVariation> variations, String displayVariation,
            String descriptionVariationName, String abbreviationVariation, String expressionVariation,
            boolean skipSecondaryWordCheck) {
      // variation for display name
      // For Instances Display Name be treated as Name so 'Exact'
      prepareVariations(variations, displayVariation, RecognitionType.Exact, false, skipSecondaryWordCheck);
      // check if the display name contains valid special chars
      String instanceDisplayNameWithValidSpChars = handleValidSpecialChars(displayVariation);
      if (ExecueCoreUtil.isNotEmpty(instanceDisplayNameWithValidSpChars)) {
         instanceDisplayNameWithValidSpChars = ExecueStringUtil
                  .removeExtraWhiteSpaces(instanceDisplayNameWithValidSpChars);
         prepareVariations(variations, instanceDisplayNameWithValidSpChars, RecognitionType.Exact, false,
                  skipSecondaryWordCheck);
      }
      // variation for description
      if (ExecueCoreUtil.isNotEmpty(descriptionVariationName)) {
         prepareVariations(variations, descriptionVariationName, RecognitionType.DisplayName, false,
                  skipSecondaryWordCheck); // For
         // Instances
         // Description be treated as Display Name so 'DisplayName'
         // check if the display name contains valid special chars
         String instanceDescWithValidSpChars = handleValidSpecialChars(descriptionVariationName);
         if (ExecueCoreUtil.isNotEmpty(instanceDescWithValidSpChars)) {
            instanceDescWithValidSpChars = ExecueStringUtil.removeExtraWhiteSpaces(instanceDescWithValidSpChars);
            prepareVariations(variations, instanceDescWithValidSpChars, RecognitionType.DisplayName, false,
                     skipSecondaryWordCheck);
         }
      }

      // Variation for abbreviation
      if (ExecueCoreUtil.isNotEmpty(abbreviationVariation)) {
         prepareVariations(variations, abbreviationVariation, RecognitionType.Abbreviation, false,
                  skipSecondaryWordCheck);
         String instanceAbbrWithValidSpecialChars = handleSpecialChars(abbreviationVariation);
         if (ExecueCoreUtil.isNotEmpty(instanceAbbrWithValidSpecialChars)) {
            instanceAbbrWithValidSpecialChars = ExecueStringUtil
                     .removeExtraWhiteSpaces(instanceAbbrWithValidSpecialChars);
            prepareVariations(variations, instanceAbbrWithValidSpecialChars, RecognitionType.Abbreviation, false,
                     skipSecondaryWordCheck);
         }
      }

      // Variation for expression
      if (ExecueCoreUtil.isNotEmpty(expressionVariation)) { // As expression is equivalent to abbreviation
         prepareVariations(variations, expressionVariation, RecognitionType.Abbreviation, false, skipSecondaryWordCheck);
         String instanceExpressionWithValidSpecialChars = handleSpecialChars(expressionVariation);
         if (ExecueCoreUtil.isNotEmpty(instanceExpressionWithValidSpecialChars)) {
            instanceExpressionWithValidSpecialChars = ExecueStringUtil
                     .removeExtraWhiteSpaces(instanceExpressionWithValidSpecialChars);
            prepareVariations(variations, instanceExpressionWithValidSpecialChars, RecognitionType.Abbreviation, false,
                     skipSecondaryWordCheck);
         }
      }
   }

   /**
    * @param variations
    * @param exactVariationName
    * @param displayVariationName
    * @param descriptionVariationName
    * @param skipSecondaryWordCheck
    *           TODO
    */
   private void populateVariations (List<EntityNameVariation> variations, String exactVariationName,
            String displayVariationName, String descriptionVariationName, boolean skipSecondaryWordCheck) {
      // variation for name
      prepareVariations(variations, exactVariationName, RecognitionType.Exact, true, skipSecondaryWordCheck);
      // variation for display name
      prepareVariations(variations, displayVariationName, RecognitionType.DisplayName, false, skipSecondaryWordCheck);
      // check if the display name contains valid special chars
      String displayNameWithValidSpChars = handleValidSpecialChars(displayVariationName);
      if (ExecueCoreUtil.isNotEmpty(displayNameWithValidSpChars)) {
         displayNameWithValidSpChars = ExecueStringUtil.removeExtraWhiteSpaces(displayNameWithValidSpChars);
         prepareVariations(variations, displayNameWithValidSpChars, RecognitionType.DisplayName, false,
                  skipSecondaryWordCheck);
      }
      // variation for description
      if (ExecueCoreUtil.isNotEmpty(descriptionVariationName)) {
         prepareVariations(variations, descriptionVariationName, RecognitionType.Description, false,
                  skipSecondaryWordCheck);
         // check if the display name contains valid special chars
         String conceptDescWithValidSpChars = handleValidSpecialChars(descriptionVariationName);
         if (ExecueCoreUtil.isNotEmpty(conceptDescWithValidSpChars)) {
            conceptDescWithValidSpChars = ExecueStringUtil.removeExtraWhiteSpaces(conceptDescWithValidSpChars);
            prepareVariations(variations, conceptDescWithValidSpChars, RecognitionType.Description, false,
                     skipSecondaryWordCheck);
         }
      }
   }

   /**
    * This method handles the preparation of the variations from the different versions of the
    * name/display_name/description of the business term
    */
   private void prepareVariations (List<EntityNameVariation> variations, String term, RecognitionType type,
            boolean isForName, boolean skipSecondaryWordCheck) {
      // variation for the term without any changes
      // variations.add(getVariation(term, type));
      // If no alphabet exists, then will create the variation as is.
      // eg: "9-5 2.1" can come as car model name

      if (!ExecueCoreUtil.isAlbhabetExists(term)) {
         EntityNameVariation entityNameVariation = getVariation(term, type, skipSecondaryWordCheck);
         variations.add(entityNameVariation);
         return;
      }
      // TODO: -JVK- consider only the first occurrence of hyphen
      // check if the term contains a '-' and is surrounded by text
      // Eg: park-in, a-2 is valid and will trim hyphen from it
      // but 9-5 is not valid so we wont trim them
      if (ExecueStringUtil.isSpecialCharExistsInTextString(term, "-") && !RecognitionType.Exact.equals(type)) {
         String hyphenatedTerm = ExecueStringUtil.replaceSpecialCharInTextString(term, "-", "");
         if (ExecueCoreUtil.isNotEmpty(hyphenatedTerm)) {
            variations.add(getVariation(hyphenatedTerm, type, skipSecondaryWordCheck));
         }
      }
      // normalized version
      String normalizedTerm = normalize(term);
      // special handling for RecognitionType Exact - should not contain spaces
      if (RecognitionType.Exact.equals(type) && isForName) {
         if (normalizedTerm.contains(SINGLE_SPACE)) {
            normalizedTerm = getCompactString(normalizedTerm);
         }
      }
      // compacted version
      String compactedTerm = getCompactString(normalizedTerm);
      if (ExecueCoreUtil.isNotEmpty(compactedTerm)) {
         variations.add(getVariation(compactedTerm, type, skipSecondaryWordCheck));
      }
      // merge single chars version
      // 22Apr2010 -JVK- added the below fix to ensure that the original term having
      // multiple characters separated by spaces doesn't get added as a variation

      // Eg: "H Q State" if occurs as display name or description, will NOT be added as a variation
      // Only HQState and HQ State are the variations that need to get generated
      String mergedTerm = mergeSingleCharsIfExists(normalizedTerm);
      if (ExecueCoreUtil.isNotEmpty(mergedTerm)) {
         variations.add(getVariation(mergedTerm, type, skipSecondaryWordCheck));
      } else {
         variations.add(getVariation(normalizedTerm, type, skipSecondaryWordCheck));
      }

      // get variation by splitting at upper case occurrences
      String splitOnUpperCaseTerm = ExecueCoreUtil.splitOnUpperCase(normalizedTerm);
      variations.add(getVariation(splitOnUpperCaseTerm, type, skipSecondaryWordCheck));
   }

   // private String getHyphenatedString (String term) {
   // StringBuilder sbHyphenatedString = new StringBuilder();
   // StringTokenizer tokenizer = new StringTokenizer(term, "-");
   // while (tokenizer.hasMoreTokens()) {
   // sbHyphenatedString.append(tokenizer.nextToken());
   // }
   // return sbHyphenatedString.toString();
   // }

   private EntityNameVariation getVariation (String name, RecognitionType type, boolean skipSecondaryWordCheck) {
      EntityNameVariation variation = new EntityNameVariation();
      variation.setName(name);
      variation.setType(type);
      variation.setSkipSecondaryWordCheck(skipSecondaryWordCheck);
      return variation;
   }

   /**
    * This method removes the duplicates from the variations
    */
   private List<EntityNameVariation> removeVariationDupes (List<EntityNameVariation> variations) {
      Set<String> variationSet = new HashSet<String>();
      List<EntityNameVariation> finalVariationList = new ArrayList<EntityNameVariation>();
      // The variations have been added in the correct order of priority, hence using set to check for dupes
      for (EntityNameVariation variation : variations) {
         if (variationSet.add(variation.getName())) {
            finalVariationList.add(variation);
         }
      }
      return finalVariationList;
   }

   /**
    * This method cleans the input string and ensures that the extra spaces are removed
    * 
    * @param String
    *           input string
    */
   public static String normalize (String str) {
      // handle special characters
      str = handleSpecialChars(str);
      // remove extra white spaces
      str = ExecueStringUtil.removeExtraWhiteSpaces(str);
      return str;
   }

   /**
    * This method replaces the special characters other than those specified as valid special characters with spaces
    * 
    * @param String
    *           input string which needs to be handled for special characters
    */
   public static String handleSpecialChars (String val) {
      String newVal = null;
      val = val.trim();
      for (String specialChar : specialChars) {
         if (ExecueStringUtil.isSpecialCharExistsInTextString(val, specialChar)) {
            newVal = ExecueStringUtil.replaceSpecialCharInTextString(val, specialChar, SINGLE_SPACE);
            val = newVal;
         }
      }
      return val;
   }

   /**
    * This method replaces the valid special characters with spaces if they occurs in text string eg: 1) "execue.com"
    * will become "execue com" but not "1.2" 2) "one-way" will become "one way"
    * 
    * @param String
    *           input string which needs to be handled for valid special characters
    */
   public static String handleValidSpecialChars (String val) {
      String newVal = null;
      val = val.trim();
      for (String validSpecialChar : validSpecialChars) {
         if (ExecueStringUtil.isSpecialCharExistsInTextString(val, validSpecialChar)) {
            newVal = ExecueStringUtil.replaceSpecialCharInTextString(val, validSpecialChar, SINGLE_SPACE);
            val = newVal;
         }
      }
      return newVal;
   }

   /**
    * This method removes the spaces and combines the parts of the string separated by the space to make one word
    * 
    * @param String
    *           word the string to be compacted
    */
   public static String getCompactString (String word) {
      String compactWord = null;
      String compactedWord = ExecueStringUtil.compactString(word);
      if (!compactedWord.equalsIgnoreCase(word)) {
         compactWord = compactedWord;
      }
      return compactWord;
   }

   /**
    * This method merges the single characters occurring continuously into a single word. <BR>
    * It doesn't merge the single character if it is number Example: I P O Date -> IPO Date Example: Renault 1 ->
    * Renault 1
    */
   public static String mergeSingleCharsIfExists (String word) {
      String newWord = null;
      String mergedWord = ExecueStringUtil.mergeSingleCharsIfExists(word);
      if (!word.equalsIgnoreCase(mergedWord)) {
         newWord = mergedWord;
      }
      return newWord;
   }

   public RICloud prepareRICloudEntry (CloudComponent cloudComponent, Long modelGroupId) throws KDXException {
      RICloud riCloud = new RICloud();
      riCloud.setCloudId(cloudComponent.getCloud().getId());
      riCloud.setCloudName(cloudComponent.getCloud().getName());
      riCloud.setCloudOutputBusinessEntityId(cloudComponent.getCloud().getOutputBedId());
      riCloud.setCloudOutputName(cloudComponent.getCloud().getOutputName());
      riCloud.setCloudCategory(cloudComponent.getCloud().getCategory());
      riCloud.setCloudOutput(cloudComponent.getCloud().getCloudOutput());
      riCloud.setCloudPart(cloudComponent.getCloudPart());
      riCloud.setCloudSelection(cloudComponent.getCloudSelection());
      riCloud.setModelGroupId(cloudComponent.getComponentBed().getModelGroup().getId());
      riCloud.setComponentBusinessEntityId(cloudComponent.getComponentBed().getId());
      BusinessEntityDefinition componentBED = getKdxRetrievalService().getBusinessEntityDefinitionById(
               riCloud.getComponentBusinessEntityId());

      String componentName = null;
      if (BusinessEntityType.BEHAVIOR.equals(componentBED.getEntityType())) {
         componentName = cloudComponent.getComponentBed().getBehavior().getBehavior();
      } else if (BusinessEntityType.TYPE.equals(componentBED.getEntityType())) {
         componentName = cloudComponent.getComponentBed().getType().getName();
      } else if (BusinessEntityType.REALIZED_TYPE.equals(componentBED.getEntityType())) {
         componentName = cloudComponent.getComponentBed().getConcept().getName();
      } else if (BusinessEntityType.CONCEPT.equals(componentBED.getEntityType())) {
         componentName = cloudComponent.getComponentBed().getConcept().getName();
      } else if (BusinessEntityType.CONCEPT_PROFILE.equals(componentBED.getEntityType())) {
         componentName = cloudComponent.getComponentBed().getConceptProfile().getName();
      } else if (BusinessEntityType.RELATION.equals(componentBED.getEntityType())) {
         componentName = cloudComponent.getComponentBed().getRelation().getName();
      }
      riCloud.setComponentName(componentName);
      riCloud.setComponentTypeBusinessEntityId(cloudComponent.getComponentTypeBed().getId());
      riCloud.setComponentTypeName(cloudComponent.getComponentTypeBed().getType().getName());
      riCloud.setRealizationBusinessEntityId(cloudComponent.getComponentBed().getId());
      riCloud.setRealizationName(componentName);
      riCloud.setRequiredBehaviorBusinessEntityId(cloudComponent.getRequiredBehavior());
      if (cloudComponent.getRequiredBehavior() != null) {
         riCloud.setRequiredBehaviorName(getKdxRetrievalService().getBehaviorById(cloudComponent.getRequiredBehavior())
                  .getBehavior());
      }
      riCloud.setComponentCategory(cloudComponent.getComponentCategory());
      riCloud.setRepresentativeEntityType(cloudComponent.getRepresentativeEntityType());
      riCloud.setImportance(cloudComponent.getImportance());
      riCloud.setFrequency(cloudComponent.getFrequency());
      riCloud.setRequired(cloudComponent.getRequired());
      riCloud.setDefaultValue(cloudComponent.getDefaultValue());
      riCloud.setOutputComponent(cloudComponent.getOutputComponent());
      riCloud.setPrimaryRICloudId(null);
      return riCloud;
   }

   /**
    * @return the kdxRetrievalService
    */
   public IKDXRetrievalService getKdxRetrievalService () {
      return kdxRetrievalService;
   }

   /**
    * @param kdxRetrievalService
    *           the kdxRetrievalService to set
    */
   public void setKdxRetrievalService (IKDXRetrievalService kdxRetrievalService) {
      this.kdxRetrievalService = kdxRetrievalService;
   }

   public ISWIConfigurationService getSwiConfigurationService () {
      return swiConfigurationService;
   }

   public void setSwiConfigurationService (ISWIConfigurationService swiConfigurationService) {
      this.swiConfigurationService = swiConfigurationService;
   }
}
