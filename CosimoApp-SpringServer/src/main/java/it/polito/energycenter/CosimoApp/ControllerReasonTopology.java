package it.polito.energycenter.CosimoApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class ControllerReasonTopology {
    
    @Autowired
    OntoManager om;
    @CrossOrigin
    @GetMapping("/reason")
    

    public void reasontopology() throws Exception {
        om.ReasonTopology();
    }
}
