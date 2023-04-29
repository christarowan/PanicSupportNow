import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './phone-link.reducer';

export const PhoneLinkDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const phoneLinkEntity = useAppSelector(state => state.phoneLink.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phoneLinkDetailsHeading">Phone Link</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{phoneLinkEntity.id}</dd>
          <dt>
            <span id="number">Number</span>
          </dt>
          <dd>{phoneLinkEntity.number}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{phoneLinkEntity.name}</dd>
          <dt>Action Plan</dt>
          <dd>{phoneLinkEntity.actionPlan ? phoneLinkEntity.actionPlan.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/phone-link" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phone-link/${phoneLinkEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhoneLinkDetail;
