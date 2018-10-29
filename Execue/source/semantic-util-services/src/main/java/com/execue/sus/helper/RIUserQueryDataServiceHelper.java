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


package com.execue.sus.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.execue.core.common.bean.nlp.DomainRecognition;
import com.execue.core.common.bean.nlp.SemanticPossibility;
import com.execue.core.common.bean.graph.GraphPath;
import com.execue.core.common.bean.graph.IGraphComponent;
import com.execue.core.common.bean.graph.IGraphPath;
import com.execue.core.common.bean.nlp.InstanceInformation;
import com.execue.core.common.bean.nlp.NLPConstants;
import com.execue.core.common.bean.qdata.RIUserQuery;
import com.execue.core.common.type.PathProcessingType;
import com.execue.core.common.type.RFXVariationSubType;
import com.execue.core.common.type.TripleVariationSubType;
import com.execue.core.common.type.TripleVariationType;

/**
 * This is a helper class to populate the RIUserQuery object from a ReducedForm object
 * 
 * @author Laura
 * @version 1.0
 * @since 08/18/09
 */

public class RIUserQueryDataServiceHelper {

   private static Collection<List<String>> getRIUserQueryEntry (IGraphComponent component) {
      Set<List<String>> result = new HashSet<List<String>>();
      if (((DomainRecognition) component).getNlpTag().contains(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
         String entityBeId = "";
         if (((DomainRecognition) component).getConceptBEDId() != null) {
            entityBeId = ((DomainRecognition) component).getConceptBEDId() + "";
         }
         // TODO should we do something if ((DomainRecognition)c).getDomainEntityID() is null?
         if (entityBeId.length() > 0) {
            result.add(Arrays.asList(new String[] { entityBeId, "0", "0", TripleVariationType.SUBJECT.getValue(),
                     TripleVariationSubType.SUBJECTCONCEPT.getValue() }));
            result.add(Arrays.asList(new String[] { entityBeId, "0", "0", TripleVariationType.OBJECT.getValue(),
                     TripleVariationSubType.OBJECTCONCEPT.getValue() }));
         }
      } else if (((DomainRecognition) component).getNlpTag().contains(NLPConstants.NLP_TAG_ONTO_INSTANCE)) {
         String eID = "";
         if (((DomainRecognition) component).getInstanceInformations() != null) {
            for (InstanceInformation instanceInformation : ((DomainRecognition) component).getInstanceInformations()) {
               eID = instanceInformation.getInstanceBedId() + "";
               if (eID.length() > 0) {
                  result.add(Arrays.asList(new String[] { eID, "0", "0", TripleVariationType.SUBJECT.getValue(),
                           TripleVariationSubType.SUBJECTINSTANCE.getValue() }));
                  result.add(Arrays.asList(new String[] { eID, "0", "0", TripleVariationType.OBJECT.getValue(),
                           TripleVariationSubType.OBJECTINSTANCE.getValue() }));
               }
            }

         }
         // TODO should we do something if ((DomainRecognition)c).getDomainInstanceID() is null?

      } else if (((DomainRecognition) component).getNlpTag().contains(NLPConstants.NLP_TAG_ONTO_PROPERTY)) {
         String eID = "";
         if (((DomainRecognition) component).getRelationBEDId() != null) {
            eID = ((DomainRecognition) component).getRelationBEDId() + "";
         }
         if (eID.length() > 0) {
            result.add(Arrays.asList(new String[] { eID, "0", "0", TripleVariationType.RELATION.getValue(),
                     TripleVariationSubType.RELATION.getValue() }));
         }
      }
      return result;

   }

   private static Collection<List<String>> getRIUserQueryEntries (List<IGraphComponent> components) {
      String subtype1 = TripleVariationSubType.SUBJECTCONCEPT.getValue(), subtype2 = TripleVariationSubType.OBJECTCONCEPT
               .getValue(), subtype3 = TripleVariationSubType.RELATION.getValue(), subtype4 = TripleVariationSubType.SUBJECTCONCEPT_RELATION
               .getValue(), subtype5 = TripleVariationSubType.RELATION_OBJECTCONCEPT.getValue(), subtype6 = TripleVariationSubType.SUBJECTCONCEPT_OBJECTCONCEPT
               .getValue(), subtype7 = TripleVariationSubType.SUBJECTCONCEPT_RELATION_OBJECTCONCEPT.getValue();
      Set<List<String>> result = new HashSet<List<String>>();
      if (components.size() == 3) {
         boolean isInstanceTriple = false;
         List<String> subjects = new ArrayList<String>(1);
         List<String> objects = new ArrayList<String>(1);
         String property = "";
         DomainRecognition sourceRecognition = (DomainRecognition) components.get(0);
         DomainRecognition destRecognition = (DomainRecognition) components.get(2);
         DomainRecognition propRecognition = (DomainRecognition) components.get(1);
         if ((sourceRecognition).getConceptBEDId() != null && sourceRecognition.getInstanceInformations() == null) {
            subjects.add(sourceRecognition.getConceptBEDId() + "");
         }
         if ((destRecognition).getConceptBEDId() != null && destRecognition.getInstanceInformations() == null) {
            objects.add(destRecognition.getConceptBEDId() + "");
         }
         if ((propRecognition).getRelationBEDId() != null) {
            property = (propRecognition).getRelationBEDId() + "";
         }
         if ((sourceRecognition).getNlpTag().contains(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
            if (!(destRecognition).getNlpTag().contains(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
               // only subject is a concept
               subtype2 = TripleVariationSubType.OBJECTINSTANCE.getValue();
               subtype5 = TripleVariationSubType.RELATION_OBJECTINSTANCE.getValue();
               subtype6 = TripleVariationSubType.SUBJECTCONCEPT_OBJECTINSTANCE.getValue();
               subtype7 = TripleVariationSubType.SUBJECTCONCEPT_RELATION_OBJECTINSTANCE.getValue();
               if ((destRecognition).getInstanceInformations() != null) {
                  for (InstanceInformation instanceInformation : (destRecognition).getInstanceInformations()) {
                     objects.add(instanceInformation.getInstanceBedId() + "");
                  }

               } else {
                  objects.add("");
               }
            }
         } else {
            if ((destRecognition).getNlpTag().contains(NLPConstants.NLP_TAG_ONTO_CONCEPT)) {
               // only object is a concept
               subtype1 = TripleVariationSubType.SUBJECTINSTANCE.getValue();
               subtype4 = TripleVariationSubType.SUBJECTINSTANCE_RELATION.getValue();
               subtype6 = TripleVariationSubType.SUBJECTINSTANCE_OBJECTCONCEPT.getValue();
               subtype7 = TripleVariationSubType.SUBJECTINSTANCE_RELATION_OBJECTCONCEPT.getValue();
               if ((sourceRecognition).getInstanceInformations() != null) {
                  for (InstanceInformation instanceInformation : (sourceRecognition).getInstanceInformations()) {
                     subjects.add(instanceInformation.getInstanceBedId() + "");
                  }

               } else {
                  subjects.add("");
               }
            } else {
               // neither subject, nor object are a concept
               isInstanceTriple = true;
               subtype1 = TripleVariationSubType.SUBJECTINSTANCE.getValue();
               subtype2 = TripleVariationSubType.OBJECTINSTANCE.getValue();
               subtype4 = TripleVariationSubType.SUBJECTINSTANCE_RELATION.getValue();
               subtype5 = TripleVariationSubType.RELATION_OBJECTINSTANCE.getValue();
               subtype6 = TripleVariationSubType.SUBJECTINSTANCE_OBJECTINSTANCE.getValue();
               subtype7 = TripleVariationSubType.SUBJECTINSTANCE_RELATION_OBJECTINSTANCE.getValue();
               if ((sourceRecognition).getInstanceInformations() != null) {
                  for (InstanceInformation instanceInformation : (sourceRecognition).getInstanceInformations()) {
                     subjects.add(instanceInformation.getInstanceBedId() + "");
                  }

               } else {
                  subjects.add("");
               }
               if ((destRecognition).getInstanceInformations() != null) {
                  for (InstanceInformation instanceInformation : (destRecognition).getInstanceInformations()) {
                     objects.add(instanceInformation.getInstanceBedId() + "");
                  }

               } else {
                  objects.add("");
               }
            }
         }
         if ((sourceRecognition).getOriginalPositions() != null && (propRecognition).getOriginalPositions() != null
                  && (destRecognition).getOriginalPositions() != null) {
            // complete triple in user query
            for (String subject : subjects) {
               for (String object : objects) {
                  if (subject.length() > 0 && property.length() > 0 && object.length() > 0) {
                     if (isInstanceTriple) {
                        result.add(Arrays.asList(new String[] { subject, property, object,
                                 TripleVariationType.SUBJECT_RELATION_OBJECT.getValue(), subtype7 }));
                     } else {
                        result.add(Arrays.asList(new String[] { subject, "0", "0",
                                 TripleVariationType.SUBJECT.getValue(), subtype1 }));
                        result.add(Arrays.asList(new String[] { object, "0", "0",
                                 TripleVariationType.OBJECT.getValue(), subtype2 }));
                        result.add(Arrays.asList(new String[] { property, "0", "0",
                                 TripleVariationType.RELATION.getValue(), subtype3 }));
                        result.add(Arrays.asList(new String[] { subject, property, "0",
                                 TripleVariationType.SUBJECT_RELATION.getValue(), subtype4 }));
                        result.add(Arrays.asList(new String[] { property, object, "0",
                                 TripleVariationType.RELATION_OBJECT.getValue(), subtype5 }));
                        result.add(Arrays.asList(new String[] { subject, object, "0",
                                 TripleVariationType.SUBJECT_OBJECT.getValue(), subtype6 }));
                        result.add(Arrays.asList(new String[] { subject, property, object,
                                 TripleVariationType.SUBJECT_RELATION_OBJECT.getValue(), subtype7 }));
                     }
                  }
                  // TODO should we do something if subject, property and/or object lenght=0?
               }
            }
         } else {
            if ((sourceRecognition).getOriginalPositions() != null && (destRecognition).getOriginalPositions() != null) {
               // Subject & Object in user query
               for (String subject : subjects) {
                  for (String object : objects) {
                     if (subject.length() > 0 && object.length() > 0) {
                        result.add(Arrays.asList(new String[] { subject, "0", "0",
                                 TripleVariationType.SUBJECT.getValue(), subtype1 }));
                        result.add(Arrays.asList(new String[] { object, "0", "0",
                                 TripleVariationType.OBJECT.getValue(), subtype2 }));
                        result.add(Arrays.asList(new String[] { subject, object, "0",
                                 TripleVariationType.SUBJECT_OBJECT.getValue(), subtype6 }));
                     }
                     // TODO should we do something if subject and/or object lenght=0?
                  }
               }
            } else {
               if ((sourceRecognition).getOriginalPositions() != null) {
                  // Subject and property in user query
                  for (String subject : subjects) {
                     if (subject.length() > 0 && property.length() > 0) {
                        result.add(Arrays.asList(new String[] { subject, "0", "0",
                                 TripleVariationType.SUBJECT.getValue(), subtype1 }));
                        result.add(Arrays.asList(new String[] { property, "0", "0",
                                 TripleVariationType.RELATION.getValue(), subtype3 }));
                        result.add(Arrays.asList(new String[] { subject, property, "0",
                                 TripleVariationType.SUBJECT_RELATION.getValue(), subtype4 }));
                     }
                     // TODO should we do something if subject and/or property lenght=0?

                  }
               } else if ((destRecognition).getOriginalPositions() != null) {
                  // Object and property in user query
                  for (String object : objects) {
                     if (property.length() > 0 && object.length() > 0) {
                        result.add(Arrays.asList(new String[] { object, "0", "0",
                                 TripleVariationType.OBJECT.getValue(), subtype2 }));
                        result.add(Arrays.asList(new String[] { property, "0", "0",
                                 TripleVariationType.RELATION.getValue(), subtype3 }));
                        result.add(Arrays.asList(new String[] { property, object, "0",
                                 TripleVariationType.RELATION_OBJECT.getValue(), subtype5 }));
                     }
                     // TODO should we do something if property and/or object lenght=0?
                  }

               }
            }
         }
      }

      return result;
   }

   public static List<RIUserQuery> generateRIUserQueryEntries (Long userQueryId,
            SemanticPossibility reducedFormPossibility) {
      List<RIUserQuery> result = new ArrayList<RIUserQuery>();
      Map<String, List<IGraphComponent>> triples = new HashMap<String, List<IGraphComponent>>();
      triples.putAll(getTriplesSet(reducedFormPossibility));
      for (List<String> l : getRIEntries(userQueryId, triples.values())) {
         RIUserQuery uq = new RIUserQuery();
         uq.setQueryId(userQueryId);
         uq.setBeId1(Long.valueOf(l.get(0)));
         uq.setBeId2(Long.valueOf(l.get(1)));
         uq.setBeId3(Long.valueOf(l.get(2)));
         uq.setVariationType(Integer.valueOf(l.get(3)));
         uq.setVariationSubType(RFXVariationSubType.getType(Integer.valueOf(l.get(4))));
         // TODO as of now adding it as 100.
         uq.setVariationWeight(100.00);
         result.add(uq);
      }
      return result;
   }

   private static Set<List<String>> getRIEntries (Long userQueryId, Collection<List<IGraphComponent>> triples) {
      Set<List<String>> result = new HashSet<List<String>>();
      for (List<IGraphComponent> triple : triples) {
         if (triple.size() == 1) {
            result.addAll(getRIUserQueryEntry(triple.get(0)));
         } else if (triple.size() == 3) {
            result.addAll(getRIUserQueryEntries(triple));
         }
      }
      return result;
   }

   private static Map<String, List<IGraphComponent>> getTriplesSet (SemanticPossibility reducedForm) {
      Map<String, List<IGraphComponent>> result = new HashMap<String, List<IGraphComponent>>(1);
      if (reducedForm.getReducedForm().getPaths() == null) {
         for (IGraphComponent looseNode : reducedForm.getAllGraphComponents()) {
            List<IGraphComponent> s = new ArrayList<IGraphComponent>();
            s.add(looseNode);
            result.put(looseNode.getBusinessEntityName(), s);
         }
      } else {
         for (IGraphPath path : reducedForm.getReducedForm().getPaths()) {
            GraphPath graphPath = (GraphPath) path;
            if (path.getPathLength() == 3) {
               if (graphPath.getPathProcessingType() == PathProcessingType.SKIP) {
                  continue;
               } else if (graphPath.getPathProcessingType() == PathProcessingType.IMPLICIT_PATH) {
                  // If graph path is implicit, then just consider the end vertex
                  List<IGraphComponent> s1 = new ArrayList<IGraphComponent>(1);
                  s1.add(path.getEndVertex());
                  result.put(path.getStartVertex().getBusinessEntityName(), s1);
               } else if (graphPath.getPathProcessingType() == PathProcessingType.CUT) {
                  List<IGraphComponent> s1 = new ArrayList<IGraphComponent>(1);
                  s1.add(path.getStartVertex());
                  List<IGraphComponent> s2 = new ArrayList<IGraphComponent>(1);
                  s2.add(path.getEndVertex());
                  result.put(path.getStartVertex().getBusinessEntityName(), s1);
                  result.put(path.getEndVertex().getBusinessEntityName(), s2);
               } else {
                  List<IGraphComponent> s = new ArrayList<IGraphComponent>(1);
                  s.add(path.getStartVertex());
                  s.add(path.getConnectors().get(0));
                  s.add(path.getEndVertex());
                  result.put(path.getStartVertex().getBusinessEntityName()
                           + path.getConnectors().get(0).getBusinessEntityName()
                           + path.getEndVertex().getBusinessEntityName(), s);
               }
            } else if (path.getPathLength() == 1) {
               List<IGraphComponent> s = new ArrayList<IGraphComponent>();
               s.add(path.getStartVertex());
               result.put(path.getStartVertex().getBusinessEntityName(), s);
            } else {
               for (IGraphComponent looseNode : path.getConnectors()) {
                  // TODO: get the triples from the long path
               }
            }
         }
      }
      return result;
   }
}