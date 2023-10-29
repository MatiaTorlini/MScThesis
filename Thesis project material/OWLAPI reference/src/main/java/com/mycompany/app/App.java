package com.mycompany.app;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Optional;

//import org.semanticweb.HermiT.ReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.FunctionalSyntaxDocumentFormat;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.InferenceType;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

import uk.ac.manchester.cs.owl.owlapi.OWLDataPropertyImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectPropertyImpl;

/**
 * THIS DOCUMENT PROVIDES AN OVERVIEW OF OWLAPI 5.0 USAGE
 *
 */

//RECURSIVE LISTING OF ALL SUBCLASSES OF A CLASS
public class App 
{
    public static Set<OWLClass> findAllSubClasses(OWLOntology ontology, OWLClass targetClass) {
        Set<OWLClass> subclasses = new HashSet<>();

        for (OWLSubClassOfAxiom axiom : ontology.getAxioms(AxiomType.SUBCLASS_OF)) {
            if (axiom.getSuperClass().equals(targetClass)) {
                if(axiom.getSubClass().isOWLClass()) {
                    OWLClassExpression subclassExpression = axiom.getSubClass();
                    subclasses.add(subclassExpression.asOWLClass());
                    subclasses.addAll((findAllSubClasses(ontology, subclassExpression.asOWLClass())));
                }
            }
        }
        return subclasses;
    }

    public static void main( String[] args )
    {
        //OPEN ONTOLOGY FILE, DECLARE ONTOLOGY MANAGER AND AN OWLONTOLOGY OBJECT

        OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); //ontology manager manages the ontology
		File university = new File("resources/university.owl");    //open file
		OWLOntology localUni; //ontology datatype where the ontology will be loadded
		try
		{
            //CREATE A NEW ONTOLOGY (TO BE CHECKED)
            /*
             manager.loadOntologyFromOntologyDocument()
             */

			// LOAD ONTOLOGY FROM FILE
			localUni = manager.loadOntologyFromOntologyDocument(university); //loads ontology in OWLOntology object from a file
			IRI base_iri = localUni.getOntologyID().getOntologyIRI().get(); //retrieve the ontology base IRI
            OWLDataFactory df = manager.getOWLDataFactory(); //datafactory is used to create classes, individuals, properties and axioms

            
        /* CREATE ANNOTATION PROPERTY
            IRI annotationPropertyIri = IRI.create(base_iri + "#hasDescription");
            OWLLiteral annotationValue = df.getOWLLiteral("this is KT", "en");
            OWLAnnotation annotation = df.getOWLAnnotation(df.getOWLAnnotationProperty(annotationPropertyIri), annotationValue);

            //CREATE INDIVIDUAL
*/
            IRI KatiaIRI = IRI.create(base_iri + "#KatiaTorlonia");  //create new IRI for the individual 
            OWLNamedIndividual individual = df.getOWLNamedIndividual(KatiaIRI); //create tmp individual
            OWLAxiom axiom = df.getOWLDeclarationAxiom(individual); //create temporary axiom on existence of individual above
            manager.addAxiom(localUni, axiom); //add axiom to the ontology. Since now the individual has been inserted into the ontolgy

            //ADD CLASS TO INDIVIDUAL
            OWLClass classAssertion = df.getOWLClass("http://elite.polito.it/ontologies/university.owl#Person");
            manager.addAxiom(localUni, df.getOWLClassAssertionAxiom(classAssertion, individual));

            //ADD AN OBJECT PROPERTY LINKING TWO INDIVIDUALS
            OWLNamedIndividual individual2 = df.getOWLNamedIndividual("http://elite.polito.it/ontologies/university.owl#ComputerScience_TO"); //retrieve individual to link
            OWLObjectProperty hasRelation = df.getOWLObjectProperty("http://elite.polito.it/ontologies/university.owl#teaches");
            manager.addAxiom(localUni, df.getOWLObjectPropertyAssertionAxiom(hasRelation, individual, individual2));

            //ADD A DATAPROPERTY TO AN INDIVIDUAL
            OWLDataProperty hasData = df.getOWLDataProperty("http://elite.polito.it/ontologies/university.owl#personName");
            OWLLiteral literalValue = df.getOWLLiteral("Katia");     
            manager.addAxiom(localUni, df.getOWLDataPropertyAssertionAxiom(hasData, individual, literalValue));       
            
            //LIST ALL SUBCLASSES OF A CLASS 
            OWLClass targetClass = df.getOWLClass("http://elite.polito.it/ontologies/university.owl#Person");
            Set<OWLClass> subclasses = findAllSubClasses(localUni,targetClass);

            for (OWLClass c : subclasses) {
                System.out.println(c.toString());
            }

            System.out.println(KatiaIRI);

            //SAVE ONTOLOGY
             try {
            
                    manager.saveOntology(localUni);
                 }
            catch(OWLException e) {
                  System.out.println("Impossible to save the ontology\n");
                  }
        }
        catch(OWLOntologyCreationException e)
        {
            System.err.println("Impossible to load " + university.getAbsolutePath());
            e.printStackTrace();
        }
   
  }
}