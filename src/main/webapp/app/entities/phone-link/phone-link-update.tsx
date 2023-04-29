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
import { IPhoneLink } from 'app/shared/model/phone-link.model';
import { getEntity, updateEntity, createEntity, reset } from './phone-link.reducer';

export const PhoneLinkUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const actionPlans = useAppSelector(state => state.actionPlan.entities);
  const phoneLinkEntity = useAppSelector(state => state.phoneLink.entity);
  const loading = useAppSelector(state => state.phoneLink.loading);
  const updating = useAppSelector(state => state.phoneLink.updating);
  const updateSuccess = useAppSelector(state => state.phoneLink.updateSuccess);

  const handleClose = () => {
    navigate('/phone-link');
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
      ...phoneLinkEntity,
      ...values,
      actionPlan: actionPlans.find(it => it.id.toString() === values.actionPlan.toString()),
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
          ...phoneLinkEntity,
          actionPlan: phoneLinkEntity?.actionPlan?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="panicSupportNowApp.phoneLink.home.createOrEditLabel" data-cy="PhoneLinkCreateUpdateHeading">
            Create or edit a Phone Link
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="phone-link-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Number" id="phone-link-number" name="number" data-cy="number" type="text" />
              <ValidatedField label="Name" id="phone-link-name" name="name" data-cy="name" type="text" />
              <ValidatedField id="phone-link-actionPlan" name="actionPlan" data-cy="actionPlan" label="Action Plan" type="select">
                <option value="" key="0" />
                {actionPlans
                  ? actionPlans.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/phone-link" replace color="info">
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

export default PhoneLinkUpdate;
