import React from "react";
import { Button, Container, Row, Col } from "react-bootstrap";
import 'reactflow/dist/style.css'

const x_pos = 0;
const y_pos =  0;

function ModelAdder(props) {



    return <>
        <Container style={{ width: '100vw', height: '100vh' }}>
            <Row>
                <Col>
                    <input type = "file" onChange={(e) => props.addModel(e.target.files[0])}></input>
                </Col>
            </Row>

                
            
        </Container>
    </>
}

export default ModelAdder;