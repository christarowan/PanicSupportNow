import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActionPlan } from 'app/shared/model/action-plan.model';
import { getEntities as getActionPlans } from 'app/entities/action-plan/action-plan.reducer';
import { ICopingStrategies } from 'app/shared/model/coping-strategies.model';
import { getEntity, updateEntity, createEntity, reset } from './coping-strategies.reducer';

export const CopingStrategiesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const actionPlans = useAppSelector(state => state.actionPlan.entities);
  const copingStrategiesEntity = useAppSelector(state => state.copingStrategies.entity);
  const loading = useAppSelector(state => state.copingStrategies.loading);
  const updating = useAppSelector(state => state.copingStrategies.updating);
  const updateSuccess = useAppSelector(state => state.copingStrategies.updateSuccess);

  const handleClose = () => {
    navigate('/coping-strategies');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getActionPlans({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...copingStrategiesEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...copingStrategiesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="panicSupportNowApp.copingStrategies.home.createOrEditLabel" data-cy="CopingStrategiesCreateUpdateHeading">
            Create or edit a Coping Strategies
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="coping-strategies-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Contents" id="coping-strategies-contents" name="contents" data-cy="contents" type="text" />
              <ValidatedField label="Name" id="coping-strategies-name" name="name" data-cy="name" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/coping-strategies" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default CopingStrategiesUpdate;
