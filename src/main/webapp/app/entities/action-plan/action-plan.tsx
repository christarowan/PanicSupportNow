import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IActionPlan } from 'app/shared/model/action-plan.model';
import { getEntities } from './action-plan.reducer';

export const ActionPlan = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const actionPlanList = useAppSelector(state => state.actionPlan.entities);
  const loading = useAppSelector(state => state.actionPlan.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="action-plan-heading" data-cy="ActionPlanHeading">
        Action Plans
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/action-plan/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Action Plan
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {actionPlanList && actionPlanList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Soundtrack</th>
                <th>Coping Strategies</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {actionPlanList.map((actionPlan, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/action-plan/${actionPlan.id}`} color="link" size="sm">
                      {actionPlan.id}
                    </Button>
                  </td>
                  <td>
                    {actionPlan.soundtrack ? <Link to={`/soundtrack/${actionPlan.soundtrack.id}`}>{actionPlan.soundtrack.id}</Link> : ''}
                  </td>
                  <td>
                    {actionPlan.copingStrategies ? (
                      <Link to={`/coping-strategies/${actionPlan.copingStrategies.id}`}>{actionPlan.copingStrategies.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/action-plan/${actionPlan.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/action-plan/${actionPlan.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/action-plan/${actionPlan.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Action Plans found</div>
        )}
      </div>
    </div>
  );
};

export default ActionPlan;
