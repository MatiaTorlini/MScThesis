import React from "react";
import {  Row, Col } from 'react-bootstrap'


function AnnotationPage(props) {

    return (
        <>
            
                <div className="container">
                    <h2>Component class</h2>
                    <Row> 
                        <Col>
                            <input type="text" placeholder="Insert the interface name" onChange={e => props.changeInterfaceName(e.target.value)}></input>
                        </Col>
                        <Col>
                            <select id="objectClass" onChange={(e) => props.setClass(e.target.value)}>
                                {props.componentsList.map((str, idx) => (
                                    <option key={idx} value={str}>{str}</option>
                                ))
                                }</select>
                        </Col>
                        
                    </Row>
                    <Row>
                       <Col>
                            <button className="add-port-button" onClick={props.addPort}>Add Port</button>
                        </Col>
                    </Row>

                </div>

                <div className="container">
                    {props.ports.map((p, idx) => (
                        <>
                            <Row > 
                                <Col>
                                    <input type="text" placeholder='portName' onChange={(e) => props.handleNameChange(idx, e.target.value)}></input>
                                </Col>
                                <Col>
                                    <select onChange={(e) => props.handleCausalityChange(idx, e.target.value)}>
                                        <option value="true">Input</option>
                                        <option value="false">Output</option>
                                    </select>
                                </Col>
                                <Col>
                                    <select onChange={(e) => props.handleFlowChange(idx, e.target.value)}>
                                        {props.flows.map((f) => (
                                            <>
                                                <option key={f} value={f} >{f}</option>
                                            </>
                                        ))}
                                    </select>
                                </Col>
                                <Col>
                                    <button className="buttondelete" onClick={() => props.removePort(idx)}>X</button>
                                </Col>
                            </Row>

                        </>
                    ))}
                </div>

                <div className="container">
                    <button onClick={() => props.addInterface()}>Save Interface</button>
                </div>
        </>
    );
};

export default AnnotationPage;

/*
        return (
        <>
            <Container >
                <Row> <h2 class="center-element">Here you can annotate a model for creating an rdf interface based on CoSimO</h2></Row>
                <Row><h3 class="center-element">Select component class</h3></Row>
                <Row>
                    <select onChange={(e) => props.setClass(e.target.value)} class="center-element">
                    {props.componentsList.map(( value) => (
                            <option  value={value} >{value}</option>
                        ))}        
                    </select>
                </Row>
            </Container>
        </>
    );     */