import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ActionPlan from './action-plan';
import ActionPlanDetail from './action-plan-detail';
import ActionPlanUpdate from './action-plan-update';
import ActionPlanDeleteDialog from './action-plan-delete-dialog';

const ActionPlanRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ActionPlan />} />
    <Route path="new" element={<ActionPlanUpdate />} />
    <Route path=":id">
      <Route index element={<ActionPlanDetail />} />
      <Route path="edit" element={<ActionPlanUpdate />} />
      <Route path="delete" element={<ActionPlanDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ActionPlanRoutes;
