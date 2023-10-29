import React from 'react';
import { Link } from 'react-router-dom';
import { Container, Row, Col } from 'react-bootstrap';

function HomePage(props) {
  

  return (
      
      <Container className="homepage-content">
        <h1>EC-Framework</h1>
        <div className="button-container">
          <Row>
            <Col><Link to="/compose">
                <button className="square-button">Build Scenario</button>
            </Link>
            </Col>
            <Col> 
             <Link to="/annotate"> 
                <button className="square-button">Annotate Model</button>
            </Link>
            </Col>
            <Col>
            <Link to="/add_model">
            <button className = "square-button">Add Model </button>
            </Link>
            </Col>
          </Row>
          
          
            
            
        </div>
      </Container>
  );
}

export default HomePage;