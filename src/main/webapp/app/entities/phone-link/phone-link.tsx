import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IPhoneLink } from 'app/shared/model/phone-link.model';
import { getEntities } from './phone-link.reducer';

export const PhoneLink = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const phoneLinkList = useAppSelector(state => state.phoneLink.entities);
  const loading = useAppSelector(state => state.phoneLink.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="phone-link-heading" data-cy="PhoneLinkHeading">
        Phone Links
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/phone-link/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Phone Link
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {phoneLinkList && phoneLinkList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Number</th>
                <th>Name</th>
                <th>Action Plan</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phoneLinkList.map((phoneLink, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/phone-link/${phoneLink.id}`} color="link" size="sm">
                      {phoneLink.id}
                    </Button>
                  </td>
                  <td>{phoneLink.number}</td>
                  <td>{phoneLink.name}</td>
                  <td>
                    {phoneLink.actionPlan ? <Link to={`/action-plan/${phoneLink.actionPlan.id}`}>{phoneLink.actionPlan.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/phone-link/${phoneLink.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/phone-link/${phoneLink.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/phone-link/${phoneLink.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Phone Links found</div>
        )}
      </div>
    </div>
  );
};

export default PhoneLink;
