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


package com.execue.ontology.configuration;

import com.execue.core.configuration.SystemConstants;

public interface OntologyConstants extends SystemConstants {

   public String TIME_CONCEPT               = "Time";
   public String MEASURE_CONCEPT            = "Measure";
   public String PERIODIC_INFORMATION       = "PeriodicInformation";
   public String INSTANCE_CONCEPT           = "EnumerationConcept";
   public String ATTRIBUTE_CONCEPT          = "AttributeConcept";
   public String DIMENSION_CONCEPT          = "DimensionConcept";
   public String POPULATION_CONCEPT         = "PopulationConcept";
   public String ATTRIBUTE                  = "Attribute";
   public String CONCEPT                    = "Concept";
   public String DOMAIN_CONCEPT             = "DomainConcept";
   public String QUANTITATIVE_CONCEPT       = "QuantitativeConcept";
   public String GRAIN_CONCEPT              = "GrainConcept";
   public String EXECUE_CONCEPT             = "ExecueConcept";
   public String SUPER_CONCEPT              = "SuperConcept";
   public String EXCLUDED_CONCEPT           = "ExcludedConcept";
   public String INDICATOR_CONCEPT          = "Indicator";
   public String VALUE_CONCEPT              = "Value";
   public String NUMBER_CONCEPT             = "Number";
   public String STATISTICS_CONCEPT         = "Statistics";
   public String LOOKUP_CONCEPT             = "LookupConcept";
   public String SUBORDINATING_CONCJUNCTION = "SubordinatingConjunction";
   public String COORDINATING_CONCJUNCTION  = "CoordinatingConjunction";
   public String OPERATOR_CONCEPT           = "Operator";
   public String CONJUNCTION_CONCEPT        = "Conjunction";
   public String BY_CONCEPT                 = "By-Conjunction";
   public String YEAR_CONCEPT               = "Year";

   public String UNION_CLASS                = "UnionClass";
   public String INTERSECTION_CLASS         = "IntersectionClass";
   public String SIMPLE_CLASS               = "SimpleClass";

   public String PERIODIC_INFO_PROPERTY     = "hasPeriodicInformation";
   public String ID_PROPERTY                = "idProperty";
   public String DEFAULT_PROPERTY           = "defaultProperty";
   public String REQUIRED_PROPERTY          = "requiredProperty";
   public String PREFERRED_PROPERTY         = "preferredProperty";
   public String DEFAULT_VALUE_PROPERTY     = "defaultValue";
   public String PARENT_PROPERTY            = "parentResource";
   public String INVERESE_PROPERTY          = "inverse";
   public String COMPARATIVE_CONCEPT        = "ComparativeConcept";
   public String ABSTRACT_CONCEPT           = "AbstractConcept";
   public String DISTRIBUTION_CONCEPT       = "DistributionConcept";
   public String TIME_FRAME_PROPERTY        = "hasTimeFrame";
   public String CITY_CONCEPT               = "City";
   public String LOCATION                   = "Location";
   public String UNIT_SCALE_CONCEPT         = "UnitScale";
   public String ABSOLUTE_MONTH             = "AbsoluteMonth";
   public String ABSOLUTE_QUARTER           = "AbsoluteQuarter";
   public String ABSOLUTE_YEAR              = "AbsoluteYear";
   public String ABSTRACT_YEAR              = "AbstractYear";
   public String ABSTRACT_QUARTER           = "AbstractQuarter";
   public String ABSTRACT_MONTH             = "AbstractMonth";
   public String YEAR                       = "Year";
   public String QUARTER                    = "Quarter";
   public String MONTH                      = "Month";
   public String ADJECTIVE_CONCEPT          = "Adjective";
   public String PUNCTUATION                = "Punctuation";
}
