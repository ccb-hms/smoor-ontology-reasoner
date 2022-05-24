package edu.harvard.hms.ccb.reasoner.ontology.smoor;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import javax.annotation.Nonnull;
import java.io.File;

@Command(name = "smoor", mixinStandardHelpOptions = true, version = "1.0.0",
        description = "Subconcept Meta OWL Ontology Reasoner")
public class SmoorCli implements Runnable {

    @Parameters(index = "0", description = "File path to ontology.")
    private String ontologyPath;

    @Option(names = {"-r", "--reasoner"}, showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            description = "Reasoner to use: hermit, jfact, elk",
            defaultValue = "hermit")
    private String reasoner;

    @Option(names = {"-o", "--output"},
            description = "File path to desired output reasoned ontology")
    private String output;

    @Option(names = {"-s", "--saturate"}, showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            description = "Saturate ontology with equivalence axioms between named classes and complex class expressions")
    private boolean saturate = true;

    @Option(names = {"-d", "--directOnly"}, showDefaultValue = CommandLine.Help.Visibility.ALWAYS,
            description = "Include only direct subclass relations in the output")
    private boolean directOnly = false;

    public static void main(String... args) {
        int exitCode = new CommandLine(new SmoorCli()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        ontologyPath = isValidIri(ontologyPath);
        if (output == null) {
            output = isValidIri(System.getProperty("user.dir") + File.separator + "smoor-output.owl");
        }
        try {
            Smoor smoorReasoner = new Smoor();
            ReasoningResults results = smoorReasoner.loadOntologyAndReason(ontologyPath, reasoner, saturate, directOnly);
            results.saveInferredOntology(output);
        } catch (OWLOntologyCreationException | OWLOntologyStorageException e) {
            e.printStackTrace();
        }
    }

    private String isValidIri(@Nonnull String path) {
        if(!path.startsWith("file:")) {
            path = "file:" + path;
        }
        IRI.create(path);
        return path;
    }
}