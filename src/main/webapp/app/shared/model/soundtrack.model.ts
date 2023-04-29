import { IActionPlan } from 'app/shared/model/action-plan.model';

export interface ISoundtrack {
  id?: number;
  fileName?: string | null;
  name?: string | null;
  actionPlan?: IActionPlan | null;
}

export const defaultValue: Readonly<ISoundtrack> = {};
