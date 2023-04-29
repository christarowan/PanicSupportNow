import { IActionPlan } from 'app/shared/model/action-plan.model';

export interface ICopingStrategies {
  id?: number;
  contents?: string | null;
  name?: string | null;
  actionPlan?: IActionPlan | null;
}

export const defaultValue: Readonly<ICopingStrategies> = {};
