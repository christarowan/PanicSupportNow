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
import { ISoundtrack } from 'app/shared/model/soundtrack.model';
import { getEntity, updateEntity, createEntity, reset } from './soundtrack.reducer';

export const SoundtrackUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const actionPlans = useAppSelector(state => state.actionPlan.entities);
  const soundtrackEntity = useAppSelector(state => state.soundtrack.entity);
  const loading = useAppSelector(state => state.soundtrack.loading);
  const updating = useAppSelector(state => state.soundtrack.updating);
  const updateSuccess = useAppSelector(state => state.soundtrack.updateSuccess);

  const handleClose = () => {
    navigate('/soundtrack');
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
      ...soundtrackEntity,
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
          ...soundtrackEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="panicSupportNowApp.soundtrack.home.createOrEditLabel" data-cy="SoundtrackCreateUpdateHeading">
            Create or edit a Soundtrack
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="soundtrack-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="File Name" id="soundtrack-fileName" name="fileName" data-cy="fileName" type="text" />
              <ValidatedField label="Name" id="soundtrack-name" name="name" data-cy="name" type="text" />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/soundtrack" replace color="info">
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

export default SoundtrackUpdate;
