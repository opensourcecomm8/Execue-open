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


package com.execue.nlp.preprocessor;

import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.MapUtils;

import com.execue.core.util.ExecueCoreUtil;
import com.execue.core.util.ExecueStringUtil;
import com.execue.nlp.configuration.INLPConfigurationService;
import com.execue.nlp.util.NLPUtilities;

public class QueryNormalizer implements IPreProcessor {

   private INLPConfigurationService nlpConfigurationService;

   public String preProcess (String userQuerySentence) {

      // TODO: NK: Currently removing the @ character from the user query sentence
      // in the below call as we use '@' in unmarshalling the name and value(nlptag)
      // since the maxent tagger was also giving the @ as seperator for some nlp tag.
      // Later we should get rid off this check once we find the
      // way to supply our own custom separator to the maxent tagger and use this custom separator in unmarshalling.

      userQuerySentence = ExecueStringUtil.processForCommasAndSpecialChars(userQuerySentence);
      String[] punctuations = { ";", "(", ")", "!" };
      for (String punct : punctuations) {
         userQuerySentence = ExecueStringUtil.processForPunctuation(userQuerySentence, punct);
      }

      Map<String, String> unitScaleByNameMap = MapUtils.invertMap(getNlpConfigurationService().getUnitScaleMap());
      userQuerySentence = NLPUtilities.processCurrencyPattern(userQuerySentence,
               getNlpConfigurationService().getOperatorMap(), unitScaleByNameMap);

      userQuerySentence = normalizeRegexPatterns(userQuerySentence);

      userQuerySentence = ExecueStringUtil.processForTime(userQuerySentence);
      userQuerySentence = ExecueCoreUtil.processForPhoneNumber(userQuerySentence);
      userQuerySentence = NLPUtilities.processForSpecialChars(userQuerySentence);

      // This method will remove any markers added in above processing. For eg: ~# is marked for currency pattern
      userQuerySentence = removeMarkers(userQuerySentence);

      return userQuerySentence;
   }

   private String normalizeRegexPatterns (String userQuerySentence) {
      String normalizedSentence = NLPUtilities.normalizeByRegexPattern(userQuerySentence, getNlpConfigurationService()
               .getCurrencyPatternRegex());
      normalizedSentence = NLPUtilities.normalizeByRegexPattern(normalizedSentence, getNlpConfigurationService()
               .getAreaPatternRegex());
      return normalizedSentence;
   }

   private String removeMarkers (String userQuerySentence) {
      StringTokenizer st = new StringTokenizer(userQuerySentence);
      StringBuilder newStr = new StringBuilder();
      while (st.hasMoreTokens()) {
         String compString = st.nextToken();
         if (compString.startsWith("~#") && compString.endsWith("~#")) {
            compString = compString.replaceAll("~#", "");
         }
         newStr.append(compString).append(" ");
      }
      return newStr.toString().trim();
   }

   /**
    * @return the nlpConfigurationService
    */
   public INLPConfigurationService getNlpConfigurationService () {
      return nlpConfigurationService;
   }

   /**
    * @param nlpConfigurationService
    *           the nlpConfigurationService to set
    */
   public void setNlpConfigurationService (INLPConfigurationService nlpConfigurationService) {
      this.nlpConfigurationService = nlpConfigurationService;
   }

}
