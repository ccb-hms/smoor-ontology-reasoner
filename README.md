# smoor-ontology-reasoner

**_smoor_**—<u>S</u>ubconcept <u>M</u>eta <u>O</u>WL <u>O</u>ntology <u>R</u>easoner—is an OWL reasoner wrapper 
that materializes inferences obtained from reasoning over an ontology after saturating it with named subconcepts 
(via equivalence axioms between (temporary) named classes and complex class expressions that appear in ontology axioms).

Consider the axioms: 

`C SubClassOf part-of (located-in E)`

`B SubClass C and part-of D`

`A SubClassOf B`

While a reasoner entails that `A SubClassOf part-of (located-in E)`, it will not include this inference in the inferred 
axiom set, since the classification reasoning task computes subsumptions between named classes only.

For querying purposes (e.g. in a triple store with insufficient reasoning support), it is some times practical to explicitly assert
inferences between named classes and ontology _subconcepts_ (i.e. complex OWL class expressions).

**_smoor_** treats subconcepts as named classes, by adding `EquivalentClasses` axioms between temporary classes and 
subconcepts that occur in the ontology. **_smoor_** then performs reasoning over the ontology saturated with the equivalence 
axioms. For example:

`X1 EquivalentTo part-of (located-in E)`

`X2 EquivalentTo located-in E`

`X3 EquivalentTo part-of D`

The output from **_smoor_** would then contain inferences such as:

`A SubClassOf part-of D`

`A SubClassOf part-of (located-in E)`


## Installation

The build tool used is [Maven](https://maven.apache.org). Make sure it is [installed](https://maven.apache.org/install.html) and then run:

```mvn clean package``` 

This will create a self-contained JAR file in `target/smoor.jar`.

## Usage

```java -jar target/smoor.jar```

## Dependencies

* OWL API v5.1.20
* HermiT reasoner v1.4.5
* ELK reasoner v0.5.0
* JFact v5.0.3