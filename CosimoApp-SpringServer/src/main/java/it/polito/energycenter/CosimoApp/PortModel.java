package it.polito.energycenter.CosimoApp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PortModel {
    private String name;
    private boolean causality;
    private String flow;
   // private int valueReference;
}