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


package com.execue.nlp.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: anujit
 * Date: Oct 29, 2009
 * Time: 8:29:23 PM
 * To change this template use File | Settings | File Templates.
 */

public class RegExTest {

   private Logger logger = Logger.getLogger(RegExTest.class);

   private static final String NEW_LINE = System.getProperty("line.separator");

   @Test
   public void testRegex() {
      String expression = "";

      //expression = "(?!(between\\s[a-z]*\\s(and)))\\s[a-z]*\\s(and)\\s[a-z]*";

      //x and y
      expression = "^[a-z]*\\s(and)\\s[a-z]*";

      expression = "^(?:(?!(between$|bet$)).)*$";


      //expression = "(?<!between)((\\s)?(^)?[a-z]*\\s(and)\\s[a-z]*)";

      expression = "(Token@word:\\w+#superConcept:([\\w~]*)((?<!(absoluteQuarter))(~absoluteYear~)(?!(absoluteQuarter)))([\\w~]*)#nlpTag:OC(\\s)?)";
      //expression = "(Token@word:\\w+#superConcept:([\\w~]*)(absoluteYear)([\\w~]*)#nlpTag:OC(\\s)?)";

      expression = "(Token@word:\\w+#superConcept:([\\w~]*)((?<!(AbsoluteMonth))([\\w~]*[(AbsoluteQuarter][\\w~]*)(?!(AbsoluteMonth)))([\\w~]*)#nlpTag:OI(\\s)?)";
      //expression = "(Token@word:([a-zA-Z]+[1-4]?)#superConcept:([\\w~]*)((?<!(AbsoluteMonth))(~AbsoluteQuarter~)(?!(AbsoluteMonth)))([\\w~]*)#nlpTag:OI(\\s)?)";

      String regex = "Token@word:\\w+#superConcept:((?!.*AbsoluteMonth).*)((?=.*AbsoluteQuarter).*)#nlpTag:OI(\\s)?";
      String str = "Token@word:NominalQuarter#superConcept:NominalQuarter~AbsoluteQuarter~AbsoluteYear~AbsoluteTime~Time~AbsoluteTime~Time~#nlpTag:OC Token@word:1#superConcept:Number~#nlpTag:OI Token@word:2008#superConcept:Number~#nlpTag:OI";
      //str = "Token@word:NominalQuarter#superConcept:~AbsoluteYear~AbsoluteTime~Time~AbsoluteTime~Time~#nlpTag:OC Token@word:1#superConcept:Number~#nlpTag:OI Token@word:2008#superConcept:Number~#nlpTag:OI";
      //str = "Token@word:NominalQuarter#superConcept:NominalQuarter~AbsoluteYear~AbsoluteQuarter~AbsoluteTime~Time~AbsoluteTime~Time~#nlpTag:OC Token@word:1#superConcept:Number~#nlpTag:OI Token@word:2008#superConcept:Number~#nlpTag:OI";

      //String str = "in x and y";

      //expression = "Token@word:\\w+#superConcept:([\\w~]*)((?<!(PeriodicInformation))(~Thing~)(?!(PeriodicInformation)))([\\w~]*)#nlpTag:[\\w]+";
      //expression = "Token@word:\\w+#superConcept:([\\w~]*)(?!(PeriodicInformation))([\\w~]*)#nlpTag:[\\w]+";
      //str = "Token@word:Company#superConcept:~Thing~Company~#nlpTag:OI";

      str = "Token@word:Month1#superConcept:~Thing~Month~AbsoluteMonth~AbsoluteTime~Time~Year~Quarter~AbsoluteQuarter~AbsoluteYear~#nlpTag:OI";
//      str = "Token@word:Population Total#superConcept:null#nlpTag:SFL Token@word:Month1#superConcept:~Thing~Month~Quarter~Year~Time~AbsoluteMonth~Time~Year~Year~Time~AbsoluteQuarter~AbsoluteQuarter~Time~AbsoluteYear~AbsoluteYear~AbsoluteTime~Time~AbsoluteTime~AbsoluteQuarter~AbsoluteYear~AbsoluteTime~Time~AbsoluteTime~AbsoluteQuarter~AbsoluteYear~Time~Time~AbsoluteYear~AbsoluteYear~AbsoluteTime~Time~AbsoluteTime~AbsoluteTime~AbsoluteTime~AbsoluteYear~AbsoluteYear~Time~AbsoluteTime~Time~AbsoluteTime~AbsoluteTime~Time~Time~AbsoluteTime~Time~Time~AbsoluteTime~Time~Time~AbsoluteTime~AbsoluteTime~AbsoluteTime~AbsoluteTime~Time~Time~Time~Time~Time~Time~Time~AbsoluteTime~Time~AbsoluteTime~Time~Time~AbsoluteTime~AbsoluteTime~Time~Time~Time~Time~Time~Time~Time~Time~Time~Time~Time~Time~Time~AbsoluteTime~Time~Time~Time~Time~Time~Time~Time~#nlpTag:OI Token@word:2007#superConcept:Number~#nlpTag:OI";

      Pattern pattern = Pattern.compile(regex.toLowerCase());
      Matcher matcher = null;

      matcher = pattern.matcher(str.toLowerCase());
      while (matcher.find()) {
         System.out.println("1. Match : "+matcher.group().trim());
      }
   }
}