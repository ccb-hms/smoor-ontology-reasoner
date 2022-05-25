package edu.harvard.hms.ccb.reasoner.ontology.smores;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import javax.annotation.Nonnull;
import java.util.*;
import static com.google.common.base.Preconditions.checkNotNull;

public class OntologySaturator {

    /**
     * Constructor
     */
    public OntologySaturator() {
        // no-op
    }

    /**
     * Create a new ontology term for every given class expression in the ontology, then generate an equivalence axiom
     * between the new term and the class expression. Saturate the given ontology with the resulting axioms. Returns
     * the added axioms and a map of the new terms to their equivalent class expressions.
     * @param ontology  OWL ontology to saturate
     * @param classExpressions  Set of class expressions to be named in the given ontology
     * @return SaturationDetails containing the added axioms, and a map of the new terms to class expressions
     */
    @Nonnull
    public SaturationDetails saturateOntology(@Nonnull OWLOntology ontology,
                                              @Nonnull Set<OWLClassExpression> classExpressions) {
        checkNotNull(classExpressions);
        OWLDataFactory df = OWLManager.getOWLDataFactory();
        Map<OWLClass,OWLClassExpression> saturationMap = new HashMap<>();
        Set<OWLAxiom> additionalAxioms = new HashSet<>();
        for(OWLClassExpression classExpression : classExpressions) {
            if(classExpression.isAnonymous()) {
                OWLClass newDefinedClass = df.getOWLClass(IRI.create("C_" + UUID.randomUUID()));
                saturationMap.put(newDefinedClass, classExpression);
                OWLAxiom newEquivalentClassesAxiom = df.getOWLEquivalentClassesAxiom(newDefinedClass, classExpression);
                additionalAxioms.add(newEquivalentClassesAxiom);
            }
        }
        ontology.getOWLOntologyManager().addAxioms(ontology, additionalAxioms);
        return new SaturationDetails(additionalAxioms, saturationMap);
    }
}
