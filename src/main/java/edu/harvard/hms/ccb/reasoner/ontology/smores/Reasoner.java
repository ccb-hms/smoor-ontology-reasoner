package edu.harvard.hms.ccb.reasoner.ontology.smores;

public enum Reasoner {
    HERMIT("hermit"),
    ELK("elk"),
    JFACT("jfact");

    private final String name;

    Reasoner(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }
}