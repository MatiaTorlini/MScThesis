package it.polito.energycenter.CosimoApp;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.springframework.context.annotation.Configuration;

import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;

@Configuration
public class OntoManager {
   
   private ArrayList<OWLOntology> models;
   private final QueryEngine engine;
   private final OWLOntology cosimo;
   private final OWLOntologyManager manager; 

   private void loadModels() {
      String path = "home/vboxuser/Desktop/models/";
   }

   public OntoManager() throws Exception{
    File f = new File("/home/vboxuser/Desktop/proto-cosimo.rdf");
    manager = new OWLManager().createOWLOntologyManager();
    cosimo = manager.loadOntologyFromOntologyDocument(f);
    StructuralReasonerFactory factory = new StructuralReasonerFactory();
    OWLReasoner reasoner = factory.createReasoner(cosimo);
    // Optionally let the reasoner compute the most relevant inferences in advance
    reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS);
    // Create an instance of the SPARQL-DL query engine
    engine = QueryEngine.create(manager, reasoner, true);
   }

   public QueryResult doQuery(String s) throws Exception {
    Query query = Query.create(s);
    return engine.execute(query);
   }

   public IRI getIRIforImportingCosimo() {
      return cosimo.getOntologyID().getOntologyIRI().get();
   }

   public OWLClass getClass(String classname) {
      return manager.getOWLDataFactory().getOWLClass(cosimo.getOntologyID().getOntologyIRI().get() + "#" + classname);
     }

   public OWLNamedIndividual getFlow(String flow) {
      OWLNamedIndividual ind = manager.getOWLDataFactory().getOWLNamedIndividual(cosimo.getOntologyID().getOntologyIRI().get() + "#" + flow);
      return ind;
   }

   public OWLObjectProperty getFlowProperty(boolean causality) {
      if (causality) {
         OWLObjectProperty hasInPort = manager.getOWLDataFactory().getOWLObjectProperty("http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#hasInPort");
         return hasInPort;
      }
      else {
         OWLObjectProperty hasOutPort = manager.getOWLDataFactory().getOWLObjectProperty("http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#hasOutPort");
         return hasOutPort;
      }
   }

   public void ReasonTopology() {
      
   }

}
