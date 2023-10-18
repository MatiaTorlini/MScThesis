package it.polito.energycenter.CosimoApp;

import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController

public class ScenarioManager {

    static ArrayList<OWLOntology> models;

    @CrossOrigin
    @PostMapping("/addnode")
    @ResponseBody

    public void AddNode(@RequestParam("file") MultipartFile modelinterface) throws Exception {

        String filePath = "/home/vboxuser/Desktop/Models/" + modelinterface.getOriginalFilename();
        modelinterface.transferTo(new File(filePath));
        //build json object in response to render the node
      //  ReadInterface(modelinterface.getOriginalFilename());
    }  
    /* 
    private void ReadInterface(String filename) throws Exception {
        File file = new File("/home/vboxuser/Desktop/Models/" + filename);
        OWLOntologyManager manager = new OWLManager().createOWLOntologyManager();
        OWLOntology RDFinterface = manager.loadOntologyFromOntologyDocument(file);
    }*/
}
