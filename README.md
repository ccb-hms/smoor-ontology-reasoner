# smoor-ontology-reasoner

**_smoor_**—**S**ubconcept **M**eta **O**WL **O**ntology **R**easoner—is an OWL reasoner wrapper 
that materializes inferences obtained from reasoning over an ontology after saturating it with named subconcepts 
(via equivalence axioms between (temporary) named classes and complex class expressions that appear in ontology axioms).

Consider the axioms: 

`C SubClassOf part-of (part-of E)`

`B SubClass C and part-of D`

`A SubClassOf B`

`part-of o part-of SubPropertyOf part-of` (i.e. `part-of` is *Transitive*)

While a reasoner entails that `A SubClassOf part-of (part-of E)`, it will not include this inference in the inferred 
axiom set, since the classification reasoning task computes subsumptions between named classes only.

For querying purposes (e.g. in a triple store with insufficient reasoning support), it is often practical to explicitly assert
inferences between named classes and ontology _subconcepts_ (i.e. complex OWL class expressions).

**_smoor_** treats subconcepts as named classes, by adding `EquivalentClasses` axioms between temporary classes and 
subconcepts that occur in the ontology. For the example above, it would create the axioms:

`X1 EquivalentTo part-of (part-of E)`

`X2 EquivalentTo part-of E`

`X3 EquivalentTo part-of D`

Where `Xi` stands for a new, temporary class. The output from **_smoor_** would then contain inferences such as:

`A SubClassOf part-of D`

`A SubClassOf part-of E`

`A SubClassOf part-of (part-of E)`


## Installation

The build tool used is [Maven](https://maven.apache.org). Make sure it is [installed](https://maven.apache.org/install.html) and then run:

```
mvn clean package
``` 

This will create a self-contained JAR file in `target/smoor.jar`.

## Usage

```
java -jar target/smoor.jar
```

## Dependencies

* OWL API v5.1.20
* HermiT reasoner v1.4.5
* ELK reasoner v0.5.0
* JFact v5.0.3