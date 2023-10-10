import  { Container, Col, Navbar } from 'react-bootstrap';
import React from 'react';
import { Link } from 'react-router-dom';



function Navigation(props) {
    return (
    <Navbar>
        <Container fluid>
            <Col xs="auto"> 
                <Link to="/"><Navbar.Brand><svg xmlns="http://www.w3.org/2000/svg" x="0px" y="0px" width="40" height="40" viewBox="0 0 64 64">
<path d="M 32 3 L 1 28 L 1.4921875 28.654297 C 2.8591875 30.477297 5.4694688 30.791703 7.2304688 29.345703 L 32 9 L 56.769531 29.345703 C 58.530531 30.791703 61.140812 30.477297 62.507812 28.654297 L 63 28 L 54 20.742188 L 54 8 L 45 8 L 45 13.484375 L 32 3 z M 32 13 L 8 32 L 8 56 L 56 56 L 56 35 L 32 13 z M 26 34 L 38 34 L 38 52 L 26 52 L 26 34 z"></path>
</svg></Navbar.Brand></Link>
            </Col>
            <Col>
            </Col>
        </Container>
    </Navbar>);
}

export default Navigation;