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


package com.execue.platform.swi.conversion.unitscale;

import java.util.Date;

import com.execue.core.common.bean.entity.Conversion;
import com.execue.platform.swi.conversion.unitscale.exception.UnitScaleConversionException;

/**
 * This interface is for conversions in the system. it converts value from source format to target format
 * 
 * @author Vishay
 * @version 1.0
 * @since 19/06/09
 */
public interface ITypeConvertor {

   public String convert (Conversion sourceConversion, Conversion targetConversion, String value)
            throws UnitScaleConversionException;

   public String convert (Conversion targetConversion, Date value) throws UnitScaleConversionException;
}