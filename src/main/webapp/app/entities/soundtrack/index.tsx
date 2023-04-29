import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Soundtrack from './soundtrack';
import SoundtrackDetail from './soundtrack-detail';
import SoundtrackUpdate from './soundtrack-update';
import SoundtrackDeleteDialog from './soundtrack-delete-dialog';

const SoundtrackRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Soundtrack />} />
    <Route path="new" element={<SoundtrackUpdate />} />
    <Route path=":id">
      <Route index element={<SoundtrackDetail />} />
      <Route path="edit" element={<SoundtrackUpdate />} />
      <Route path="delete" element={<SoundtrackDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default SoundtrackRoutes;
