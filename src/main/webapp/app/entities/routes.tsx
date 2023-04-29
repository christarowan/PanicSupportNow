import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ActionPlan from './action-plan';
import Soundtrack from './soundtrack';
import CopingStrategies from './coping-strategies';
import PhoneLink from './phone-link';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="action-plan/*" element={<ActionPlan />} />
        <Route path="soundtrack/*" element={<Soundtrack />} />
        <Route path="coping-strategies/*" element={<CopingStrategies />} />
        <Route path="phone-link/*" element={<PhoneLink />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
