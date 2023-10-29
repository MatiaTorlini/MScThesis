package it.polito.energycenter.CosimoApp;

import java.io.File;
import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.exceptions.QueryParserException;

import org.springframework.web.bind.annotation.CrossOrigin;

import org.semanticweb.owlapi.apibinding.OWLManager;

import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController


public class Controller {

    private static QueryEngine engine;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/")

    public ArrayList<String> getFlows() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File ontofile = new File("/home/vboxuser/Desktop/onto.rdf");
        OWLOntology ont;
        ArrayList<String> returnedValue = new ArrayList<>();
       
        try {
            ont = manager.loadOntologyFromOntologyDocument(ontofile);
            StructuralReasonerFactory factory = new StructuralReasonerFactory();
			OWLReasoner reasoner = factory.createReasoner(ont);
            // Optionally let the reasoner compute the most relevant inferences in advance
			reasoner.precomputeInferences(InferenceType.CLASS_ASSERTIONS,InferenceType.OBJECT_PROPERTY_ASSERTIONS);

			// Create an instance of the SPARQL-DL query engine
			engine = QueryEngine.create(manager, reasoner, true);

            // Some queries which cover important basic language constructs of SPARQL-DL
       
                String q = "PREFIX my: <http://www.semanticweb.org/vboxuser/ontologies/2023/8/untitled-ontology-2#>\n" +
				"SELECT ?x WHERE {\n" +
                    "Type(?x, my:BadSuperHero),\n" +
                    "Type(?y, my:GoodSuperHero),\n" +
                    "PropertyValue(?x,my:hasBrother,?y)" +
				"}";
            Query query = Query.create(q);
            QueryResult answer = engine.execute(query);
            String res = answer.toJSON();
            
            ObjectMapper objmap = new ObjectMapper(null, null, null);
            JsonNode jsonNode = objmap.readTree(res);
                     
                String val = jsonNode.get("results")
                .get("bindings")
                .get(0)
                .get("x")
                .get("value")
                .asText();
                returnedValue.add(val);
            
            return returnedValue;
        }
        catch(OWLOntologyCreationException e) {
            System.err.println("Error\n");
        }
        catch(UnsupportedOperationException exception) {
            System.out.println("Unsupported reasoner operation.");
        }
        catch(QueryParserException e) {
        	System.out.println("Query parser error: " + e);
        }
        catch(QueryEngineException e) {
        	System.out.println("Query engine error: " + e);
        } 
        catch(JsonProcessingException e) {
            System.out.println("Error processing Json: " + e);
        }
       return null;
    }
}

