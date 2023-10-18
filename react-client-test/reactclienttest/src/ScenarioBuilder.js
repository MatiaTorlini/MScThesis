import React from "react";
import { Button, Container, Row, Col } from "react-bootstrap";
import 'reactflow/dist/style.css'

function ScenarioBuilder(props) {



    return <>
        <Container style={{ width: '100vw', height: '100vh' }}>
            <Row>
                <Col>
                    <input type = "file" onChange={(e) => props.addFileInterface(e.target.files[0])}></input>
                </Col>
            </Row>

            <Row>
                
            </Row>
        </Container>
    </>
}

export default ScenarioBuilder;