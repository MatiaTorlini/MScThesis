package it.polito.energycenter.CosimoApp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;
import de.derivo.sparqldlapi.exceptions.QueryEngineException;
import de.derivo.sparqldlapi.exceptions.QueryParserException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.SimpleIRIShortFormProvider;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController

// This Controller returns all the Components of our toolbox

public class ControllerListComponents {

    @Autowired
    private OntoManager om;
    @CrossOrigin
    @GetMapping("/components")

    public ArrayList<String> getComponents() throws Exception{

        System.out.println("GET COMPONENTS REQUEST RECEIVED");

        ArrayList<String> returnedValue = new ArrayList<>();
      
            // Some queries which cover important basic language constructs of SPARQL-DL
             String q = "PREFIX cosimo: <http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#>\n"
                    +
                    "SELECT * WHERE {\n" +
                    "Type(?x, cosimo:Component)" +
                    "}";
            QueryResult answer = om.doQuery(q);
            String res = answer.toJSON();
            
            SimpleIRIShortFormProvider spf = new SimpleIRIShortFormProvider();
            IRI iri;
            
            ObjectMapper objmap = new ObjectMapper(null, null, null);
            JsonNode jsonNode = objmap.readTree(res);

            JsonNode bindingsNode = jsonNode.get("results").get("bindings");

            for (JsonNode binding : bindingsNode) {
                JsonNode xNode = binding.get("x");
                if (xNode != null) {
                    String val = xNode.get("value").asText();
                    iri = IRI.create(val);
                    returnedValue.add(spf.getShortForm(iri));
                }
            } 
            /*for(String s : returnedValue)
                System.out.println(s + "\n");*/
            return returnedValue;
    }
}
