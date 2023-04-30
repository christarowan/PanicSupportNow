import './home.scss';
import './home.css';

import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  const [audioPlaying, setAudioPlaying] = useState(false);
  const audio = new Audio('../../../content/audio/weightless.m4a');

  useEffect(() => {
    audio.play();
    setAudioPlaying(true);
  }, []);

  const handleAudioButtonClick = () => {
    if (!audioPlaying) {
      audio.play();
      setAudioPlaying(true);
    } else {
      audio.pause();
      setAudioPlaying(false);
    }
  };

  return (
    <Row className="my-custom-class">
      <Col md="4" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="8">
        <div className="text-left">
          <h2 style={{ marginLeft: '0', textAlign: 'left' }}>Panic Support Now</h2>
          <p className="lead" style={{ marginLeft: '0', textAlign: 'left' }}>
            Help is here
          </p>
        </div>
        <div className="text-right">
          <Alert color="warning" className="custom-alert custom-alert-large">
            Have you taken your medication?
            <span>&nbsp;</span>
            <Link to="/login" className="alert-link"></Link>
            <br style={{ marginBottom: '10px' }} />
            <br style={{ marginTop: '10px' }} />
            Do you have a rubberband to snap?
            <br style={{ marginBottom: '10px' }} />
            <br />
            Would you like to call your support person? Press the button below this box.
            <br style={{ marginBottom: '10px' }} />
            <br />
            Press the 'Next prompt' button on the left to walk through a grounding exercise.
            <br />
            <div className="button-container" style={{ display: 'flex', alignItems: 'center' }}>
              {audioPlaying ? (
                <button className="audio-button" onClick={handleAudioButtonClick}>
                  Click to stop audio
                </button>
              ) : (
                <button className="audio-button" onClick={handleAudioButtonClick}>
                  Click to play audio
                </button>
              )}
              <button className="call-button">Call</button>
              <button className="next-button btn btn-primary">Next prompt</button>
            </div>
          </Alert>
        </div>
      </Col>
    </Row>
  );
};

export default Home;
