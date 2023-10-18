package it.polito.energycenter.CosimoApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ControllerAddInterface {
    @Autowired
    OntoManager om;

    @CrossOrigin
    @PostMapping("/newobject")

    public void createRDFInterface(@RequestBody InterfaceModel RDFint, BindingResult bind) throws Exception {

        String filename = "/home/vboxuser/Desktop/Interface_" + RDFint.getModelName() + ".rdf";

        IRI importedOntology = om.getIRIforImportingCosimo();

        File f = new File(filename);
        OutputStream os = new FileOutputStream(f);
        OWLOntologyManager man = OWLManager.createOWLOntologyManager();
        OWLOntology onto = man.createOntology(IRI.create(filename));

        String component = RDFint.getComponent();
        ArrayList<PortModel> ports = RDFint.getPorts();

        OWLImportsDeclaration importDeclaration = man.getOWLDataFactory().getOWLImportsDeclaration(importedOntology);
        man.applyChange(new AddImport(onto, importDeclaration));

        String componentIRI = IRI.create(onto.getOntologyID().getOntologyIRI().get().toString() + "#") + component;
        OWLNamedIndividual componentIndividual = man.getOWLDataFactory().getOWLNamedIndividual(componentIRI);
        OWLClass classAssertion = om.getClass(component);
        classAssertion.toString();
        man.addAxiom(onto, man.getOWLDataFactory().getOWLClassAssertionAxiom(classAssertion, componentIndividual));
        
        for (PortModel p : ports) {
            String name = p.getName();
            IRI portIRI = IRI.create(onto.getOntologyID().getOntologyIRI().get() + "#" + name);
            OWLNamedIndividual portIndividual = man.getOWLDataFactory().getOWLNamedIndividual(portIRI);
            OWLAxiom axiom = man.getOWLDataFactory().getOWLDeclarationAxiom(portIndividual);
            man.addAxiom(onto, axiom);
            OWLNamedIndividual flow = om.getFlow(p.getFlow());
            OWLObjectProperty property = om.getFlowProperty(p.isCausality());
            man.addAxiom(onto, man.getOWLDataFactory().getOWLObjectPropertyAssertionAxiom(property, portIndividual, flow));
        }

        man.saveOntology(onto, os);
    }
}
