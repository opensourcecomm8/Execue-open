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


package com.execue.core.common.bean.algorithm;

/* 
 * Gram.java
 * 
 * This class implements a part of N-Gram algorithm.
 * 
 */

/**
 * <p>This class implements a part of N-Gram algorithm. It calculates the number
 * of occurences of a string <i>s1</i> in a string <i>s2</i>.</p>
 *
 * @author Abhijit Patil
 * @version 1.0
 */

public class Gram {
  private String gram;            //	String to be searched for
  private String str;            //	String to be searched within
  private int occurences;    //	Number of Occurences

  /**
   * Initializes the variables used to calculate the grams.
   *
   * @param inGram   String to be searched for
   * @param inString String to be searched within
   */
  public Gram(String inGram, String inString) {
    gram = inGram;
    str = inString;
    occurences = 0;
  }

  /**
   * Calculates how many times a string <i>gram</i> occurs inside a string <i>str</i>.
   * @return number of Occurences of <i>gram</i> String
   */
  public int calculateOccurences() {
    int ng = gram.length();        //	Get the length of the string to be searched
    int len = str.length();        //	Get the length of the target string

    for (int i = 0; i <= (len - ng); i++) {
      //	Get a sub-string of the target string
      //	which is of the same length of the gram string

      String gr = str.substring(i, i + ng);

      if (gram.equalsIgnoreCase(gr)) {
        //	If the sub-string matches the gram string increament occurences

        occurences++;
      }
    }

    return occurences;
  }

  /**
   * Get the number of occurences.
   *
   * @return number of occrences
   */
  public int getOccurences() {
    return occurences;
  }

  /**
   * This method returns the Gram string associated with this class
   *
   * @return gram string
   */
  public String getGramString() {
    return gram;
  }

  /**
   * This method compares two Gram objects and returns true if they are same
   * and fasle if they are not.
   *
   * @param g The Gram object
   * @return true if two gram objects are same, false otherwise
   */
  public boolean equals(Gram g) {
    boolean areEqual = false;

    if ((g.gram).equals(gram)) {
      areEqual = true;
    }

    return areEqual;
  }
}
