package it.polito.energycenter.CosimoApp;

import java.util.ArrayList;

import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.util.SimpleIRIShortFormProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.derivo.sparqldlapi.QueryResult;

@RestController
@CrossOrigin
public class ControllerListFlows {
    @Autowired
    OntoManager om;
    @GetMapping(value="/flows/{param}")
    public ArrayList<String> getComponents(@PathVariable("param") String param) throws Exception{

        ArrayList<String> returnedValue = new ArrayList<>();
        
            String q = "PREFIX cosimo: <http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#>\n"
                    +
                    "SELECT ?x WHERE {\n" +
                    "Type(?y,cosimo:" + param + "),\n" +
                    "Type(?x, cosimo:Flow),\n" +
                    "PropertyValue(?y,cosimo:hasFlow,?x)" +
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
