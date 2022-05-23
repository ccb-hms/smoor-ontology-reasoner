package edu.harvard.hms.ccb.reasoner.ontology.smoor;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static com.google.common.base.Preconditions.checkNotNull;

public final class ReasoningResults {

    @Nonnull
    private final OWLOntology ontology;
    @Nonnull
    private final Set<OWLAxiom> inferredAxioms;
    @Nonnull
    private final Map<OWLClass, Set<OWLClassExpression>> inferredClassFrameMap;

    /**
     * Constructor
     * @param ontology  OWL Ontology
     * @param inferredAxioms   Set of inferred axioms added to the ontology
     * @param inferredClassFrameMap Map of ontology classes to their inferred superclass expressions
     */
    public ReasoningResults(@Nonnull OWLOntology ontology, @Nonnull Set<OWLAxiom> inferredAxioms,
                            @Nonnull Map<OWLClass, Set<OWLClassExpression>> inferredClassFrameMap) {
        this.ontology = checkNotNull(ontology);
        this.inferredAxioms = new HashSet<>(checkNotNull(inferredAxioms));
        this.inferredClassFrameMap = new HashMap<>(checkNotNull(inferredClassFrameMap));
    }

    @Nonnull
    public OWLOntology getOntology() {
        return ontology;
    }

    @Nonnull
    public Set<OWLAxiom> getInferredAxioms() {
        return inferredAxioms;
    }

    @Nonnull
    public Map<OWLClass, Set<OWLClassExpression>> getInferredClassFrameMap() {
        return inferredClassFrameMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReasoningResults that = (ReasoningResults) o;
        return Objects.equal(ontology, that.ontology) && Objects.equal(inferredAxioms, that.inferredAxioms) &&
                Objects.equal(inferredClassFrameMap, that.inferredClassFrameMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ontology, inferredAxioms, inferredClassFrameMap);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ontology", ontology)
                .add("inferredAxioms", inferredAxioms)
                .add("inferredClassFrameMap", inferredClassFrameMap)
                .toString();
    }
}