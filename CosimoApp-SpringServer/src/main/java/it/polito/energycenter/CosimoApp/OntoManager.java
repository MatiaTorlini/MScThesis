package it.polito.energycenter.CosimoApp;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
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
   
   private QueryEngine engine;
   
   public OntoManager() throws Exception{
    File f = new File("proto-cosimo.rdf");
    final OWLOntologyManager manager = new OWLManager().createOWLOntologyManager();
    OWLOntology ont = manager.loadOntologyFromOntologyDocument(f);
    StructuralReasonerFactory factory = new StructuralReasonerFactory();
    OWLReasoner reasoner = factory.createReasoner(ont);
    // Optionally let the reasoner compute the most relevant inferences in advance
    reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS, InferenceType.OBJECT_PROPERTY_ASSERTIONS);
    // Create an instance of the SPARQL-DL query engine
    engine = QueryEngine.create(manager, reasoner, true);
   }

   public QueryResult doQuery(String s) throws Exception {
    Query query = Query.create(s);
    return engine.execute(query);
   }
}
