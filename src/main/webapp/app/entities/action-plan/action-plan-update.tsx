import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ISoundtrack } from 'app/shared/model/soundtrack.model';
import { getEntities as getSoundtracks } from 'app/entities/soundtrack/soundtrack.reducer';
import { ICopingStrategies } from 'app/shared/model/coping-strategies.model';
import { getEntities as getCopingStrategies } from 'app/entities/coping-strategies/coping-strategies.reducer';
import { IActionPlan } from 'app/shared/model/action-plan.model';
import { getEntity, updateEntity, createEntity, reset } from './action-plan.reducer';

export const ActionPlanUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const soundtracks = useAppSelector(state => state.soundtrack.entities);
  const copingStrategies = useAppSelector(state => state.copingStrategies.entities);
  const actionPlanEntity = useAppSelector(state => state.actionPlan.entity);
  const loading = useAppSelector(state => state.actionPlan.loading);
  const updating = useAppSelector(state => state.actionPlan.updating);
  const updateSuccess = useAppSelector(state => state.actionPlan.updateSuccess);

  const handleClose = () => {
    navigate('/action-plan');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getSoundtracks({}));
    dispatch(getCopingStrategies({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...actionPlanEntity,
      ...values,
      soundtrack: soundtracks.find(it => it.id.toString() === values.soundtrack.toString()),
      copingStrategies: copingStrategies.find(it => it.id.toString() === values.copingStrategies.toString()),
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
          ...actionPlanEntity,
          soundtrack: actionPlanEntity?.soundtrack?.id,
          copingStrategies: actionPlanEntity?.copingStrategies?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="panicSupportNowApp.actionPlan.home.createOrEditLabel" data-cy="ActionPlanCreateUpdateHeading">
            Create or edit a Action Plan
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="action-plan-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField id="action-plan-soundtrack" name="soundtrack" data-cy="soundtrack" label="Soundtrack" type="select">
                <option value="" key="0" />
                {soundtracks
                  ? soundtracks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="action-plan-copingStrategies"
                name="copingStrategies"
                data-cy="copingStrategies"
                label="Coping Strategies"
                type="select"
              >
                <option value="" key="0" />
                {copingStrategies
                  ? copingStrategies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/action-plan" replace color="info">
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

export default ActionPlanUpdate;
