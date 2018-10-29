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


package com.execue.nlp.parser.helper;

import org.w3c.dom.Node;
import org.w3c.dom.traversal.NodeFilter;

/**
 * @author Gopal
 * @since May 25 2006
 */
public class RuleNodeFilter implements NodeFilter {

   private String tagName = "";

   public RuleNodeFilter (String tagName) {
      this.tagName = tagName;
   }

   public void setTagName (String tagName) {
      this.tagName = tagName;
   }

   public short acceptNode (Node node) {
      if (node.getNodeType() == Node.ELEMENT_NODE) {
         if (node.getNodeName().equalsIgnoreCase(tagName)) {
            return NodeFilter.FILTER_ACCEPT;
         } else {
            return NodeFilter.FILTER_SKIP;
         }
      }
      return NodeFilter.FILTER_REJECT;
   }

}
