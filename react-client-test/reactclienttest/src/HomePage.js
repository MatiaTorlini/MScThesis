import React from 'react';
import { Link } from 'react-router-dom';

function HomePage(props) {
  

  return (
    <div>
      
      <div className="homepage-content">
        <h1>EC-Framework</h1>
        <div className="button-container">
            <Link to="/annotate"> 
                <button className="square-button">Annotate Model</button>
            </Link>
            <Link to="/compose">
                <button className="square-button">Build Scenario</button>
            </Link>
        </div>
      </div>
    </div>
  );
}

export default HomePage;