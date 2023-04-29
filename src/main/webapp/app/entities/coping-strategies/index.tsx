import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CopingStrategies from './coping-strategies';
import CopingStrategiesDetail from './coping-strategies-detail';
import CopingStrategiesUpdate from './coping-strategies-update';
import CopingStrategiesDeleteDialog from './coping-strategies-delete-dialog';

const CopingStrategiesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CopingStrategies />} />
    <Route path="new" element={<CopingStrategiesUpdate />} />
    <Route path=":id">
      <Route index element={<CopingStrategiesDetail />} />
      <Route path="edit" element={<CopingStrategiesUpdate />} />
      <Route path="delete" element={<CopingStrategiesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CopingStrategiesRoutes;
