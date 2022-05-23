package edu.harvard.hms.ccb.reasoner.ontology.smoor;

import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import javax.annotation.Nonnull;
import java.util.*;
import static com.google.common.base.Preconditions.checkNotNull;

public class InferenceMaterializer {

    /**
     * Constructor
     */
    public InferenceMaterializer() {
        // no-op
    }

    /**
     * Materialize inferences from the given reasoner and then remove the axioms added during saturation
     * @param ontology  OWL ontology to materialize inferences to
     * @param reasoner  Initialized OWL Reasoner
     * @param directOnly    true if only direct subclass relations should be included, false to include indirect ones
     * @param saturationDetails Ontology saturation details (added axioms and saturation map)
     * @return ReasoningResults containing inferred ontology, entailed axioms, and a map of classes to their inferred superclass expressions
     */
    @Nonnull
    public ReasoningResults materializeInferences(@Nonnull OWLOntology ontology,
                                                  @Nonnull OWLReasoner reasoner,
                                                  boolean directOnly,
                                                  @Nonnull Optional<SaturationDetails> saturationDetails) {
        checkNotNull(ontology);
        checkNotNull(reasoner);
        checkNotNull(saturationDetails);
        final OWLOntologyManager owlOntologyManager = ontology.getOWLOntologyManager();
        final OWLDataFactory owlDataFactory = owlOntologyManager.getOWLDataFactory();
        Set<OWLAxiom> inferences = new HashSet<>();
        Map<OWLClass, Set<OWLClassExpression>> inferencesMap = new HashMap<>();
        Map<OWLClass, OWLClassExpression> saturationMap = new HashMap<>();
        if(saturationDetails.isPresent()) {
            saturationMap = saturationDetails.get().getSaturationMap();
        }
        for(OWLClass owlClass : ontology.getClassesInSignature()) {
            if(!saturationMap.containsKey(owlClass)) {
                Set<OWLClass> superClasses = reasoner.getSuperClasses(owlClass, directOnly).getFlattened();
                Set<OWLClassExpression> superClassExpressions = new HashSet<>();
                for (OWLClass superClass : superClasses) {
                    if (!superClass.isOWLThing()) {
                        OWLClassExpression superClassExpression = superClass;
                        if (saturationMap.containsKey(superClass)) {
                            superClassExpression = saturationMap.get(superClass);
                        }
                        inferences.add(owlDataFactory.getOWLSubClassOfAxiom(owlClass, superClassExpression));
                        superClassExpressions.add(superClassExpression);
                    }
                }
                inferencesMap.put(owlClass, superClassExpressions);
            }
        }
        owlOntologyManager.addAxioms(ontology, inferences);
        saturationDetails.ifPresent(details -> ontology.removeAxioms(details.getAddedAxioms()));
        return new ReasoningResults(ontology, inferences, inferencesMap);
    }
}