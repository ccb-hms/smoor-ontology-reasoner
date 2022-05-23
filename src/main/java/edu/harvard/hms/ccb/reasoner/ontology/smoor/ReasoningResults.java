package edu.harvard.hms.ccb.reasoner.ontology.smoor;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.*;
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

    /**
     * Get the ontology with all inferences explicitly asserted
     * @return OWL ontology with inferred axioms asserted
     */
    @Nonnull
    public OWLOntology getOntology() {
        return ontology;
    }

    /**
     * Get the set of all inferred axioms
     * @return Set of inferred OWL axioms
     */
    @Nonnull
    public Set<OWLAxiom> getInferredAxioms() {
        return inferredAxioms;
    }

    /**
     * Get the map of ontology classes to their inferred superclass expressions
     * @return Inferred class frame map that includes superclass expressions
     */
    @Nonnull
    public Map<OWLClass, Set<OWLClassExpression>> getInferredClassFrameMap() {
        return inferredClassFrameMap;
    }

    /**
     * Save the inferred ontology to the specified output file
     * @param outputFile    Output file for the inferred ontology
     * @throws OWLOntologyStorageException  if unable to save ontology to specified file
     */
    public void saveInferredOntology(@Nonnull String outputFile) throws OWLOntologyStorageException {
        checkNotNull(outputFile);
        ontology.getOWLOntologyManager().saveOntology(ontology, IRI.create(outputFile));
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