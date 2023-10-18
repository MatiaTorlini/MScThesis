package it.polito.energycenter.CosimoApp;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor

public class InterfaceModel {
    private String modelName;
    private String component;
    private ArrayList<PortModel> ports;   
}
