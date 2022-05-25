package edu.harvard.hms.ccb.reasoner.ontology.smores;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static com.google.common.base.Preconditions.checkNotNull;

public final class SaturationDetails {

    @Nonnull
    private final Set<OWLAxiom> addedAxioms;
    @Nonnull
    private final Map<OWLClass, OWLClassExpression> saturationMap;

    /**
     * Constructor
     * @param addedAxioms   Set of axioms added to ontology during saturation
     * @param saturationMap Map of newly created classes to the (potentially complex) class expressions they represent
     */
    public SaturationDetails(@Nonnull Set<OWLAxiom> addedAxioms, @Nonnull Map<OWLClass, OWLClassExpression> saturationMap) {
        this.addedAxioms = new HashSet<>(checkNotNull(addedAxioms));
        this.saturationMap = new HashMap<>(checkNotNull(saturationMap));
    }

    @Nonnull
    public Set<OWLAxiom> getAddedAxioms() {
        return addedAxioms;
    }

    @Nonnull
    public Map<OWLClass, OWLClassExpression> getSaturationMap() {
        return saturationMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SaturationDetails that = (SaturationDetails) o;
        return Objects.equal(addedAxioms, that.addedAxioms) && Objects.equal(saturationMap, that.saturationMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(addedAxioms, saturationMap);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("addedAxioms", addedAxioms)
                .add("saturationMap", saturationMap)
                .toString();
    }
}
