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


package com.execue.handler.util;

import org.apache.commons.lang.StringUtils;

/**
 * This class has utility method to format the SQL Query.
 * 
 * @author Jiten
 * @version 1.0
 * @since 06/02/09
 * 
 */
public class SqlQueryFormat {
   public static final String newLine = "%3CBR%3E";
	public static String formatQuery(String sql) {
		StringBuilder stringBuilder = new StringBuilder();
		String select = StringUtils.substringBetween(sql, "SELECT", "FROM");
		select = "SELECT" + newLine + select + newLine;
		String from = StringUtils.substringBetween(sql, "FROM", "WHERE");
		from = "FROM" + newLine + from + newLine;
		String where = StringUtils.substringAfter(sql, "WHERE");
		where = "WHERE" + newLine + where;
		StringBuilder sqlQuery = stringBuilder.append(select).append(from)
				.append(where);
		return sqlQuery.toString();
	}
}
