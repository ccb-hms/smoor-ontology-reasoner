package edu.harvard.hms.ccb.reasoner.ontology.smoor;

import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;
import static com.google.common.base.Preconditions.checkNotNull;

public class ClassExpressionCollector {

    /**
     * Constructor
     */
    public ClassExpressionCollector() {
        // no-op
    }

    /**
     * Get all (atomic and complex) OWL class expressions in the given ontology
     * @param ontology  OWL ontology to collect class expressions from
     * @return Set of OWL Class Expressions
     */
    @Nonnull
    public Set<OWLClassExpression> getAllClassExpressions(@Nonnull OWLOntology ontology) {
        checkNotNull(ontology);
        Set<OWLClassExpression> allClassExpressions = new HashSet<>();
        Set<OWLLogicalAxiom> logicalAxioms = ontology.getLogicalAxioms();
        for(OWLAxiom axiom : logicalAxioms) {
            for(OWLClassExpression ce : axiom.getNestedClassExpressions()) {
                addClassExpression(ce, allClassExpressions);
            }
        }
        return allClassExpressions;
    }

    /**
     * If the given class expression is complex (ie anonymous), add it to the set of all class expressions and
     * then add any nested class expressions
     * @param classExpression	OWL Class Expression
     * @param allClassExpressions	Set of all class expressions
     */
    private void addClassExpression(@Nonnull OWLClassExpression classExpression,
                                    @Nonnull Set<OWLClassExpression> allClassExpressions) {
        checkNotNull(classExpression);
        if(classExpression.isAnonymous() && !allClassExpressions.contains(classExpression)
                && !classExpression.isOWLThing() && !classExpression.isOWLNothing()) {
            allClassExpressions.add(classExpression);
            for(OWLClassExpression nestedClassExpression : classExpression.getNestedClassExpressions()) {
                addClassExpression(nestedClassExpression, allClassExpressions);
            }
        }
    }
}