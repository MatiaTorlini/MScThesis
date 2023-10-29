package com.mycompany.app;

import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.exceptions.QueryParserException;

import java.io.File;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
/**
 * Hello world!
 *
 */
public class App 
{
    private static QueryEngine engine;
    public static void main( String[] args )
    {
        try {
			// Create an ontology manager
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
            File ontofile = new File("/home/vboxuser/Desktop/onto.rdf");
            OWLOntology ont;
            ont = manager.loadOntologyFromOntologyDocument(ontofile);
			// Create an instance of an OWL API reasoner (we use the OWL API built-in StructuralReasoner for the purpose of demonstration here)
            StructuralReasonerFactory factory = new StructuralReasonerFactory();
			OWLReasoner reasoner = factory.createReasoner(ont);
            // Optionally let the reasoner compute the most relevant inferences in advance
			reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS,InferenceType.OBJECT_PROPERTY_ASSERTIONS);

			// Create an instance of the SPARQL-DL query engine
			engine = QueryEngine.create(manager, reasoner, true);

            // Some queries which cover important basic language constructs of SPARQL-DL
            processQuery(
                "PREFIX my: <http://www.semanticweb.org/vboxuser/ontologies/2023/8/untitled-ontology-2#>\n" +
				"SELECT ?x WHERE {\n" +
					"Individual(?x),\n" +
                    "Type(?x, my:BadSuperHero),\n" +
                    "Type(?y, my:GoodSuperHero),\n" +
                    "PropertyValue(?x,my:hasBrother,?y)" +
				"}"
			);

    }
    catch(UnsupportedOperationException exception) {
        System.out.println("Unsupported reasoner operation.");
    }
    catch(OWLOntologyCreationException e) {
        System.out.println("Could not load the wine ontology: " + e.getMessage());
    }

    }

    public static void processQuery(String q)
	{
		try {
			long startTime = System.currentTimeMillis();
			
			// Create a query object from it's string representation
			Query query = Query.create(q);
			
			System.out.println("Excecute the query:");
			System.out.println(q);
			System.out.println("-------------------------------------------------");
			
			// Execute the query and generate the result set
			QueryResult result = engine.execute(query);

			if(query.isAsk()) {
				System.out.print("Result: ");
				if(result.ask()) {
					System.out.println("yes");
				}
				else {
					System.out.println("no");
				}
			}
			else {
				if(!result.ask()) {
					System.out.println("Query has no solution.\n");
				}
				else {
					System.out.println("Results:");
					System.out.print(result);
					System.out.println("-------------------------------------------------");
					System.out.println("Size of result set: " + result.size());
				}
			}

			System.out.println("-------------------------------------------------");
			System.out.println("Finished in " + (System.currentTimeMillis() - startTime) / 1000.0 + "s\n");
		}
        catch(QueryParserException e) {
        	System.out.println("Query parser error: " + e);
        }
        catch(QueryEngineException e) {
        	System.out.println("Query engine error: " + e);
        }
	}
}
