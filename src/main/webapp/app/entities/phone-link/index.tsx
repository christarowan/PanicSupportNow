import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import PhoneLink from './phone-link';
import PhoneLinkDetail from './phone-link-detail';
import PhoneLinkUpdate from './phone-link-update';
import PhoneLinkDeleteDialog from './phone-link-delete-dialog';

const PhoneLinkRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<PhoneLink />} />
    <Route path="new" element={<PhoneLinkUpdate />} />
    <Route path=":id">
      <Route index element={<PhoneLinkDetail />} />
      <Route path="edit" element={<PhoneLinkUpdate />} />
      <Route path="delete" element={<PhoneLinkDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PhoneLinkRoutes;
