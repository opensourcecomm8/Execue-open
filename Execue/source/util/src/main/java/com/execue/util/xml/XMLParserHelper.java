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


/*
 * Created on Aug 29, 2008
 */
package com.execue.util.xml;

import java.util.Arrays;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author kaliki
 */
public class XMLParserHelper {

	public static String getStringValueForElement(Node node, String tagName) {

		Element nameElement = (Element) ((Element) node).getElementsByTagName(
				tagName).item(0);
		if (nameElement == null) {
			return "";
		} else {
			return nameElement.getChildNodes().item(0).getNodeValue();
		}

	}

	public static List<String> getStringListForElement(Node node, String tagName,
			String split) {
		String value = getStringValueForElement(node, tagName);
		return Arrays.asList(value.split(","));
	}

	public static boolean getBooleanValueForElement(Node node, String tagName) {
		String value = getStringValueForElement(node, tagName);
		return Boolean.parseBoolean(value);
	}
}
