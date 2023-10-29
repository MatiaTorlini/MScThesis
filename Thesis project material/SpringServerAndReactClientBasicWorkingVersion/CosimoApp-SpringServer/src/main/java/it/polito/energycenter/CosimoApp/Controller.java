package it.polito.energycenter.CosimoApp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.CrossOrigin;



@RestController

public class Controller {

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/")
    public String react() {
        return "Hello, React!\n";
    }
}
