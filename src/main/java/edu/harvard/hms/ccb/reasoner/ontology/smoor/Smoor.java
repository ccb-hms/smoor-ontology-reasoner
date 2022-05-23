package edu.harvard.hms.ccb.reasoner.ontology.smoor;

import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.elk.owlapi.ElkReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.ac.manchester.cs.jfact.JFactFactory;
import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;
import static com.google.common.base.Preconditions.checkNotNull;

public class Smoor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Smoor.class);

    /**
     * Constructor
     */
    public Smoor() {
        // no-op
    }

    /**
     * Perform reasoning over the ontology at the given IRI
     * @param ontologyIri   IRI of ontology to load and reason over
     * @param reasonerName  Name of reasoner to be used (hermit, jfact, elk)
     * @param saturateOntology  true if ontology should be saturated with equivalence axioms between named classes and
     *                          complex class expressions, enabling "subconcept" reasoning.
     * @param directOnly    true if only direct subclass relations should be included, false otherwise to include indirect ones
     * @return ClassificationResults containing inferred ontology, entailed axioms, and a map of classes to their inferred superclass expressions
     * @throws OWLOntologyCreationException if unable to load ontology from the specified file
     */
    @Nonnull
    public ReasoningResults loadOntologyAndReason(@Nonnull String ontologyIri, @Nonnull String reasonerName,
                                                  boolean saturateOntology, boolean directOnly)
            throws OWLOntologyCreationException {
        checkNotNull(ontologyIri);
        checkNotNull(reasonerName);
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = man.loadOntologyFromOntologyDocument(IRI.create(ontologyIri));
        Reasoner reasoner = Reasoner.valueOf(reasonerName.toUpperCase());
        return reason(ontology, reasoner, saturateOntology, directOnly);
    }

    /**
     * Perform reasoning over the given loaded ontology
     * @param ontology   OWL ontology to reason over
     * @param reasonerName  Name of reasoner to be used (hermit, jfact, elk)
     * @param saturateOntology  true if ontology should be saturated with equivalence axioms between named classes and
     *                          complex class expressions, enabling "subconcept" reasoning.
     * @param directOnly    true if only direct subclass relations should be included, false otherwise to include indirect ones
     * @return ClassificationResults containing inferred ontology, entailed axioms, and a map of classes to their inferred superclass expressions
     */
    @Nonnull
    public ReasoningResults reason(@Nonnull OWLOntology ontology, @Nonnull Reasoner reasonerName,
                                   boolean saturateOntology, boolean directOnly) {
        checkNotNull(ontology);
        checkNotNull(reasonerName);
        ReasoningResults reasoningResults;
        if(saturateOntology) {
            Set<OWLClassExpression> classExpressions = new ClassExpressionCollector().getAllClassExpressions(ontology);
            SaturationDetails saturationDetails = new OntologySaturator().saturateOntology(ontology, classExpressions);
            OWLReasoner reasoner = performReasoning(ontology, reasonerName);
            reasoningResults = new InferenceMaterializer().materializeInferences(ontology, reasoner,
                    directOnly, Optional.of(saturationDetails));
        } else {
            OWLReasoner reasoner = performReasoning(ontology, reasonerName);
            reasoningResults = new InferenceMaterializer().materializeInferences(ontology, reasoner,
                    directOnly, Optional.empty());
        }
        return reasoningResults;
    }

    /**
     * Perform reasoning over the given ontology using the specified reasoner
     * @param ontology   OWL ontology to reason over
     * @param reasonerName  Name of reasoner to be used (hermit, jfact, elk)
     * @return OWL reasoner after reasoning
     */
    @Nonnull
    private OWLReasoner performReasoning(@Nonnull OWLOntology ontology, @Nonnull Reasoner reasonerName) {
        LOGGER.info("Reasoning over ontology...");
        long start = System.currentTimeMillis();
        OWLReasoner reasoner = createReasoner(checkNotNull(ontology), checkNotNull(reasonerName));
        reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.CLASS_HIERARCHY,
                InferenceType.OBJECT_PROPERTY_ASSERTIONS, InferenceType.DATA_PROPERTY_ASSERTIONS);
        long end = System.currentTimeMillis();
        LOGGER.info("...done (" + (end-start)/1000.0 + " seconds)");
        return reasoner;
    }

    /**
     * Create the specified OWL Reasoner with the given ontology
     * @return OWLReasoner
     */
    private OWLReasoner createReasoner(@Nonnull OWLOntology ontology, @Nonnull Reasoner reasonerName) {
        OWLReasonerConfiguration config = new SimpleConfiguration(new ConsoleProgressMonitor());
        OWLReasonerFactory reasonerFactory;
        switch (reasonerName) {
            case ELK: reasonerFactory = new ElkReasonerFactory(); break;
            case JFACT: reasonerFactory = new JFactFactory(); break;
            case HERMIT:
            default: reasonerFactory = new ReasonerFactory();
        }
        OWLReasoner reasoner = reasonerFactory.createReasoner(ontology, config);
        LOGGER.info("Created reasoner " + reasoner.getReasonerName() + " v" + reasoner.getReasonerVersion());
        return reasoner;
    }
}