package it.polito.energycenter.CosimoApp;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.springframework.context.annotation.Configuration;
import org.swrlapi.core.SWRLRuleEngine;
import org.swrlapi.factory.SWRLAPIFactory;
import de.derivo.sparqldlapi.Query;
import de.derivo.sparqldlapi.QueryEngine;
import de.derivo.sparqldlapi.QueryResult;

@Configuration
public class OntoManager {

   private ArrayList<OWLOntology> models;
   private ArrayList<OWLOntology> scenarios;

   private final QueryEngine engine;
   private final OWLOntology cosimo;
   private final OWLOntologyManager manager;
   private int cnt = 0;

   String ModelsRepoPath = "home/vboxuser/Desktop/models/";
   String ScenarioReposPath = "home/vboxuser/Descktop/scenarios";

   public OntoManager() throws Exception {
      File f = new File("/home/vboxuser/Desktop/proto-cosimo.rdf");
      scenarios = new ArrayList<OWLOntology>();
      models = new ArrayList<OWLOntology>();
      manager = new OWLManager().createOWLOntologyManager();
      cosimo = manager.loadOntologyFromOntologyDocument(f);
      StructuralReasonerFactory factory = new StructuralReasonerFactory();
      OWLReasoner reasoner = factory.createReasoner(cosimo);
      // Create an instance of the SPARQL-DL query engine
      engine = QueryEngine.create(manager, reasoner, true);
   }

   private ArrayList<OWLOntology> loadModels() throws Exception {

      File directory = new File(ModelsRepoPath);
      if (directory.isDirectory()) {
         File[] files = directory.listFiles();
         if (files != null) {
            for (File file : files) {
               models.add(manager.loadOntologyFromOntologyDocument(file));
            }
         }
      }
      return models;
   }

   private OWLOntology createScenarioOnto() throws Exception {
      OWLOntology scen = manager.createOntology(IRI.create("http://scenario/cosimoscen"));
      OWLImportsDeclaration importDeclaration = manager.getOWLDataFactory()
            .getOWLImportsDeclaration(cosimo.getOntologyID().getOntologyIRI().get());
      manager.applyChange(new AddImport(scen, importDeclaration));

      return scen;
   }

   public QueryResult doQuery(String s) throws Exception {
      Query query = Query.create(s);
      return engine.execute(query);
   }

   public IRI getIRIforImportingCosimo() {
      return cosimo.getOntologyID().getOntologyIRI().get();
   }

   public OWLClass getClass(String classname) {
      IRI iri = IRI.create(cosimo.getOntologyID().getOntologyIRI().get() + "#" + classname);
      return manager.getOWLDataFactory().getOWLClass(iri);
   }

   public OWLNamedIndividual getFlow(String flow) {
      IRI iri = IRI.create(cosimo.getOntologyID().getOntologyIRI().get() + "#" + flow);
      OWLNamedIndividual ind = manager.getOWLDataFactory()
            .getOWLNamedIndividual(iri);
      return ind;
   }

   public OWLObjectProperty getFlowProperty(boolean causality) {
      IRI iri;
      if (causality) {
         iri = IRI.create("http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#hasInPort");
         OWLObjectProperty hasInPort = manager.getOWLDataFactory().getOWLObjectProperty(iri);
         return hasInPort;
      } else {
         iri = IRI.create(  "http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#hasOutPort");
         OWLObjectProperty hasOutPort = manager.getOWLDataFactory().getOWLObjectProperty(
               iri);
         return hasOutPort;
      }
   }

   public void ReasonTopology() throws Exception {

      models = loadModels();
      OWLOntology ont = createScenarioOnto();
      System.out.println(ont.getOntologyID().getOntologyIRI().get());
      scenarios.add(ont);
      IRI MODEL_CLASS = IRI.create("http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#Object");
      IRI PORT_CLASS = IRI.create("http://www.semanticweb.org/vboxuser/ontologies/2023/9/untitled-ontology-18#Port");

      Set<OWLNamedIndividual> objs;

      //maybe need to create objproperties and classassertions in the new ontology
      for (OWLOntology model : models) {
         objs = model.getIndividualsInSignature();
         for (OWLNamedIndividual ind : objs) {
            for (OWLClassAssertionAxiom classassertion : model.getClassAssertionAxioms(ind)) {

               if (classassertion.getClassExpression().asOWLClass().getIRI().equals(MODEL_CLASS) ||
                     classassertion.getClassExpression().asOWLClass().getIRI().equals(PORT_CLASS)) {

                  OWLAxiom axiom = manager.getOWLDataFactory().getOWLDeclarationAxiom(ind);
                  manager.addAxiom(scenarios.get(cnt), axiom);
               }
            }
         }
      }

      // do I need to do something in order to use classes and properties of a custom namespace in the rule?
      String swrlrule = ""; //rule to infer port's compatibility

      SWRLRuleEngine ruleEngine = SWRLAPIFactory.createSWRLRuleEngine(scenarios.get(cnt));
      ruleEngine.createSWRLRule("ports compatibility", swrlrule);
      ruleEngine.infer();


      String sparqlquery = ""; //query for asking connectable ports

      Query query = Query.create(sparqlquery);
      QueryResult answer = engine.execute(query);
      String result = answer.toJSON();
      
      cnt++;
      System.out.println(result);
   }

   public void generateYAML() {
      /* if no inputs pending in scenario ontology:
            load YAML template
            retrieve connections
            fill template
            serve to executor
      */ 
   
   }
}
