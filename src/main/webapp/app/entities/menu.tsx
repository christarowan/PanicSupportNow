import React from 'react';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/action-plan">
        Action Plan
      </MenuItem>
      <MenuItem icon="asterisk" to="/soundtrack">
        Soundtrack
      </MenuItem>
      <MenuItem icon="asterisk" to="/coping-strategies">
        Coping Strategies
      </MenuItem>
      <MenuItem icon="asterisk" to="/phone-link">
        Phone Link
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
